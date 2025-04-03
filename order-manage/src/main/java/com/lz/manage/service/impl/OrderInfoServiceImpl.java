package com.lz.manage.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.manage.mapper.OrderInfoMapper;
import com.lz.manage.model.api.CommodityDetailResponse;
import com.lz.manage.model.api.OrderInfoResponse;
import com.lz.manage.model.api.OrderResponse;
import com.lz.manage.model.api.ReviewResponse;
import com.lz.manage.model.domain.OrderInfo;
import com.lz.manage.model.domain.StoreInfo;
import com.lz.manage.model.dto.orderInfo.OrderInfoApiQuery;
import com.lz.manage.model.dto.orderInfo.OrderInfoQuery;
import com.lz.manage.model.vo.orderInfo.OrderInfoVo;
import com.lz.manage.service.IApiService;
import com.lz.manage.service.IOrderInfoService;
import com.lz.manage.service.IStoreInfoService;
import com.lz.system.service.ISysConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.HttpStatus.NOT_MODIFIED;
import static com.lz.common.constant.HttpStatus.NO_CONTENT;

/**
 * 订单Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {
    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private IApiService apiService;

    @Resource
    private IStoreInfoService storeInfoService;

    @Resource
    private ISysConfigService configService;

    //多线程


    //region mybatis代码

    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    @Override
    public OrderInfo selectOrderInfoById(Long id) {
        return orderInfoMapper.selectOrderInfoById(id);
    }

    /**
     * 查询订单列表
     *
     * @param orderInfo 订单
     * @return 订单
     */
    @Override
    public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo) {
        List<OrderInfo> orderInfos = orderInfoMapper.selectOrderInfoList(orderInfo);
        for (OrderInfo info : orderInfos) {
            StoreInfo storeInfo = storeInfoService.selectStoreInfoByStoreId(info.getStoreId());
            if (StringUtils.isNotNull(storeInfo)) {
                info.setStoreName(storeInfo.getName());
            }
        }
        return orderInfos;
    }

    /**
     * 新增订单
     *
     * @param orderInfo 订单
     * @return 结果
     */
    @Override
    public int insertOrderInfo(OrderInfo orderInfo) {
        //判断是否已经创建 根据亚马逊订单号判断
        OrderInfo one = this.getOne(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getAmazonOrderId, orderInfo.getAmazonOrderId()));
        if (StringUtils.isNotNull(one)) {
            throw new ServiceException("该订单已经存在");
        }
        if (StringUtils.isNull(orderInfo.getUserName())) {
            orderInfo.setUserName(SecurityUtils.getUsername());
        }
        orderInfo.setCreateTime(DateUtils.getNowDate());
        return orderInfoMapper.insertOrderInfo(orderInfo);
    }

    /**
     * 修改订单
     *
     * @param orderInfo 订单
     * @return 结果
     */
    @Override
    public int updateOrderInfo(OrderInfo orderInfo) {
        //判断是否已经创建 根据亚马逊订单号判断
        OrderInfo one = this.getOne(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getAmazonOrderId, orderInfo.getAmazonOrderId()));
        OrderInfo myOld = this.selectOrderInfoById(orderInfo.getId());
        if (StringUtils.isNotNull(one) && !myOld.getId().equals(orderInfo.getId())) {
            throw new ServiceException("该订单已经存在");
        }
        orderInfo.setUpdateBy(SecurityUtils.getUsername());
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        return orderInfoMapper.updateOrderInfo(orderInfo);
    }

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的订单主键
     * @return 结果
     */
    @Override
    public int deleteOrderInfoByIds(Long[] ids) {
        return orderInfoMapper.deleteOrderInfoByIds(ids);
    }

    /**
     * 删除订单信息
     *
     * @param id 订单主键
     * @return 结果
     */
    @Override
    public int deleteOrderInfoById(Long id) {
        return orderInfoMapper.deleteOrderInfoById(id);
    }

    //endregion
    @Override
    public QueryWrapper<OrderInfo> getQueryWrapper(OrderInfoQuery orderInfoQuery) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = orderInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = orderInfoQuery.getId();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);

        String platform = orderInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String storeId = orderInfoQuery.getStoreId();
        queryWrapper.eq(StringUtils.isNotEmpty(storeId), "store_id", storeId);

        String marketplaceId = orderInfoQuery.getMarketplaceId();
        queryWrapper.like(StringUtils.isNotEmpty(marketplaceId), "marketplace_id", marketplaceId);

        String amazonOrderId = orderInfoQuery.getAmazonOrderId();
        queryWrapper.eq(StringUtils.isNotEmpty(amazonOrderId), "amazon_order_id", amazonOrderId);

        Date purchaseDate = orderInfoQuery.getPurchaseDate();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginPurchaseDate")) && StringUtils.isNotNull(params.get("endPurchaseDate")), "purchase_date", params.get("beginPurchaseDate"), params.get("endPurchaseDate"));

        String asin = orderInfoQuery.getAsin();
        queryWrapper.like(StringUtils.isNotEmpty(asin), "asin", asin);

        String title = orderInfoQuery.getTitle();
        queryWrapper.like(StringUtils.isNotEmpty(title), "title", title);

        Date scanTime = orderInfoQuery.getScanTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginScanTime")) && StringUtils.isNotNull(params.get("endScanTime")), "scan_time", params.get("beginScanTime"), params.get("endScanTime"));

        String buyerName = orderInfoQuery.getBuyerName();
        queryWrapper.like(StringUtils.isNotEmpty(buyerName), "buyer_name", buyerName);

        String buyerEmail = orderInfoQuery.getBuyerEmail();
        queryWrapper.eq(StringUtils.isNotEmpty(buyerEmail), "buyer_email", buyerEmail);

        String begEvaluateStatus = orderInfoQuery.getBegEvaluateStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(begEvaluateStatus), "beg_evaluate_status", begEvaluateStatus);

        String evaluatePrincipal = orderInfoQuery.getEvaluatePrincipal();
        queryWrapper.like(StringUtils.isNotEmpty(evaluatePrincipal), "evaluate_principal", evaluatePrincipal);

        String afterSaleSign = orderInfoQuery.getAfterSaleSign();
        queryWrapper.eq(StringUtils.isNotEmpty(afterSaleSign), "after_sale_sign", afterSaleSign);

        Date createTime = orderInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String remark = orderInfoQuery.getRemark();
        queryWrapper.eq(StringUtils.isNotEmpty(remark), "remark", remark);

        return queryWrapper;
    }

    @Override
    public List<OrderInfoVo> convertVoList(List<OrderInfo> orderInfoList) {
        if (StringUtils.isEmpty(orderInfoList)) {
            return Collections.emptyList();
        }
        return orderInfoList.stream().map(OrderInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public OrderInfo getOrderInfoByApi(OrderInfoApiQuery orderInfoApiQuery) {
        // 1. 获取店铺信息
        StoreInfo storeInfo = storeInfoService.selectStoreInfoByStoreId(orderInfoApiQuery.getStoreId());
        if (StringUtils.isNull(storeInfo)) {
            throw new ServiceException("店铺信息不存在", NO_CONTENT);
        }

        // 2. 获取订单信息
        OrderInfoResponse.Data orderInfoByApi = apiService.getOrderInfo(
                storeInfo.getStoreId(),
                orderInfoApiQuery.getAmazonOrderId(),
                orderInfoApiQuery.getSellerOrderId()
        );
        //如果没有采购时间表示订单找不到
        if (StringUtils.isNull(orderInfoByApi.getPurchaseDate())) {
            throw new ServiceException("订单信息不存在", NO_CONTENT);
        }
//        System.out.println("227getOrderInfoByApi orderInfoByApi = " + orderInfoByApi);
        // 3. 组装订单基本信息
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setAmazonOrderId(orderInfoApiQuery.getAmazonOrderId());
        orderInfo.setStoreId(storeInfo.getStoreId());
        orderInfo.setMarketplaceId(orderInfoByApi.getMarketplaceId());
        orderInfo.setPurchaseDate(orderInfoByApi.getPurchaseDate());
        orderInfo.setAsin(orderInfoByApi.getAsin());
        orderInfo.setTitle(orderInfoByApi.getTitle());
        orderInfo.setOrderItemId(orderInfoByApi.getOrderItemId());
        orderInfo.setGoodsLink(orderInfoByApi.getGoodsLink());
        orderInfo.setSellerOrderId(orderInfoByApi.getSellerOrderId());

        // 4. 并行调用 `getCommodityDetail` 和 `getReviewDetailList`
        CompletableFuture<CommodityDetailResponse.Data> commodityDetailFuture = CompletableFuture.supplyAsync(() ->
                apiService.getCommodityDetail(orderInfo.getOrderItemId())
        );

        CompletableFuture<ReviewResponse.Data> reviewDataFuture = CompletableFuture.supplyAsync(() ->
                apiService.getReviewDetailList(orderInfo.getAmazonOrderId())
        );

        try {
            // 5. 获取并处理 `commodityDetail`
            CommodityDetailResponse.Data commodityDetail = commodityDetailFuture.get();
            if (StringUtils.isNotNull(commodityDetail) && StringUtils.isNotEmpty(commodityDetail.getSourceUrls())) {
                orderInfo.setGoodsLink(commodityDetail.getSourceUrls());
                orderInfo.setTitle(commodityDetail.getName());
            }

            // 6. 获取并处理 `reviewData`
            ReviewResponse.Data reviewData = reviewDataFuture.get();
            if (StringUtils.isNotNull(reviewData) && StringUtils.isNotEmpty(reviewData.getRows())) {
                ReviewResponse.Data.Review review = reviewData.getRows().get(0);
                orderInfo.setEvaluateContent(review.getContent()); // 评论内容
                orderInfo.setEvaluateTime(review.getReviewDate()); // 评论时间
                orderInfo.setEvaluateLevel(review.getStar()); // 星级
                orderInfo.setComment(review.getContentUrl()); // 评论链接
            }
        } catch (InterruptedException | ExecutionException e) {
//            log.error("获取订单信息失败", e);
            return orderInfo;
        }
//        System.out.println("270getOrderInfoByApi orderInfo = " + orderInfo);
        return orderInfo;
    }

    @Override
    public OrderInfo externalQuery(OrderInfo orderInfo) {
        //根据Amazon订单号查询订单信息
//        OrderInfo one = this.getOne(new LambdaQueryWrapper<>(OrderInfo.class).eq(OrderInfo::getAmazonOrderId, orderInfo.getAmazonOrderId()));
//        if (StringUtils.isNotNull(one)) {
//            throw new ServiceException("订单信息已经评价，无需再评价", NOT_MODIFIED);
//        }
//        OrderInfoApiQuery orderInfoApiQuery = new OrderInfoApiQuery();
//        StoreInfo storeInfo = storeInfoService.selectStoreInfoByStoreId(orderInfo.getStoreId());
//        if (StringUtils.isNull(storeInfo)) {
//            throw new ServiceException("店铺信息不存在", NO_CONTENT);
//        }
        //遍历所有的店铺
        List<StoreInfo> list = storeInfoService.list();
        //打开线程查询使用多线程获取订单信息
        String executorSize = configService.selectConfigByKey("executorSize");
        int size = 0;
        try {
            size = Integer.parseInt(executorSize);
        } catch (NumberFormatException e) {
            size = 5;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        List<Future<OrderInfo>> futures = new ArrayList<>();
//        System.out.println("293orderInfo = " + orderInfo);
        for (StoreInfo store : list) {
            Future<OrderInfo> future = executorService.submit(() -> {
                OrderInfoApiQuery apiQuery = new OrderInfoApiQuery();
                OrderInfo info = new OrderInfo();
                BeanUtils.copyProperties(orderInfo, info);
//                System.out.println("info = " + info);
//                System.out.println("orderInfo = " + orderInfo);
                apiQuery.setStoreId(store.getStoreId());
                apiQuery.setAmazonOrderId(info.getAmazonOrderId());
                apiQuery.setSellerOrderId(info.getSellerOrderId());
                return this.getOrderInfo(info, apiQuery);
            });
            futures.add(future);
        }
        OrderInfo result = new OrderInfo();
        //等待所有进程执行完成
        for (Future<OrderInfo> future : futures) {//判断进程是否有报错，如果没有则表示找到订单
            try {
                OrderInfo info = future.get();
                //如果找到了订单，则跳出循环
                if (StringUtils.isNotNull(info.getPurchaseDate())) {
                    BeanUtils.copyProperties(info, result);
//                    System.out.println("info = " + info);
                    break;
                }
            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
            }
        }
        if (StringUtils.isNull(result.getPurchaseDate())) {
            throw new ServiceException("订单未找到", NO_CONTENT);
        } else {
//            this.insertOrderInfo(result);
            return result;
        }
    }

    @Override
    public int externalAdd(OrderInfo orderInfo) {
        //根据Amazon订单号查询订单信息
        OrderInfo one = this.getOne(new LambdaQueryWrapper<>(OrderInfo.class).eq(OrderInfo::getAmazonOrderId, orderInfo.getAmazonOrderId()));
        if (StringUtils.isNotNull(one)) {
            throw new ServiceException("订单信息已经评价，无需再评价", NOT_MODIFIED);
        }
        StoreInfo storeInfo = storeInfoService.selectStoreInfoByStoreId(orderInfo.getStoreId());
        if (StringUtils.isNull(storeInfo)) {
            throw new ServiceException("店铺信息不存在", NO_CONTENT);
        }
        orderInfo.setUserName("用户创建");
        return this.insertOrderInfo(orderInfo);
    }

    private OrderInfo getOrderInfo(OrderInfo orderInfo, OrderInfoApiQuery orderInfoApiQuery) {
//        System.out.println("orderInfoApiQuery = " + orderInfoApiQuery);
        OrderInfo orderInfoByApi = this.getOrderInfoByApi(orderInfoApiQuery);
        //设置初值
        orderInfo.setStoreId(orderInfoApiQuery.getStoreId());
        orderInfo.setEvaluateContent(orderInfoByApi.getEvaluateContent()); // 评论内容
        orderInfo.setEvaluateTime(orderInfoByApi.getEvaluateTime()); // 评论时间
        orderInfo.setEvaluateLevel(orderInfoByApi.getEvaluateLevel()); // 星级
        orderInfo.setComment(orderInfoByApi.getComment()); // 评论链接
        orderInfo.setMarketplaceId(orderInfoByApi.getMarketplaceId());
        orderInfo.setGoodsLink(orderInfoByApi.getGoodsLink());
        orderInfo.setPurchaseDate(orderInfoByApi.getPurchaseDate());
        orderInfo.setAsin(orderInfoByApi.getAsin());
        orderInfo.setTitle(orderInfoByApi.getTitle());
        orderInfo.setOrderItemId(orderInfoByApi.getOrderItemId());
        orderInfo.setGoodsLink(orderInfoByApi.getGoodsLink());
        orderInfo.setSellerOrderId(orderInfoByApi.getSellerOrderId());
        orderInfo.setUserName("用户创建");
        return orderInfo;
    }

    @Override
    public void autoGetOrderInfo() {
        //现在的三小时前
        String dateStart = DateUtil.format(DateUtil.offsetHour(DateUtil.date(), -3), "yyyy-MM-dd HH:mm:ss");
        String dateEnd = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss");
        OrderResponse.Data orderInfoList = apiService.getOrderInfoList(dateStart, dateEnd);
        boolean b = StringUtils.isNotNull(orderInfoList) && StringUtils.isNotEmpty(orderInfoList.getRows());
        if (!b) {
            return;
        }
        orderInfoList.getRows().forEach(orderInfo -> {
            OrderInfo info = new OrderInfo();
            //先查询是否有此订单
            OrderInfo one = this.getOne(new LambdaQueryWrapper<>(OrderInfo.class).eq(OrderInfo::getAmazonOrderId, orderInfo.getAmazonOrderId()));
            if (StringUtils.isNotNull(one)) {
                BeanUtils.copyProperties(orderInfo, info);
            } else {
                //没有
                info.setUserName("自动获取");
                info.setCreateTime(new Date());
            }
            info.setPurchaseDate(orderInfo.getPurchaseDate());
            info.setAmazonOrderId(orderInfo.getAmazonOrderId());
            info.setStoreId(orderInfo.getShopId());
            info.setEvaluateContent(orderInfo.getAmazonOrderId());
            info.setMarketplaceId(orderInfo.getMarketplaceId());
            info.setComment(orderInfo.getComment());
            info.setBuyerEmail(orderInfo.getBuyerEmail());
            info.setBuyerName(orderInfo.getBuyerName());
            List<OrderResponse.Row.OrderItem> orderItemVoList = orderInfo.getOrderItemVoList();
            if (StringUtils.isNotEmpty(orderItemVoList) && StringUtils.isNotEmpty(orderItemVoList.get(0).getAsinUrl())) {
                info.setGoodsLink(orderItemVoList.get(0).getAsinUrl());
            }
            if (StringUtils.isNotEmpty(orderItemVoList) && StringUtils.isNotEmpty(orderItemVoList.get(0).getAsin())) {
                info.setAsin(orderItemVoList.get(0).getAsin());
            }
            if (StringUtils.isNotEmpty(orderItemVoList) && StringUtils.isNotEmpty(orderItemVoList.get(0).getTitle())) {
                info.setTitle(orderItemVoList.get(0).getTitle());
            }
            if (StringUtils.isNotEmpty(orderItemVoList) && StringUtils.isNotEmpty(orderItemVoList.get(0).getOrderItemId())) {
                info.setOrderItemId(orderItemVoList.get(0).getOrderItemId());
            }
            this.saveOrUpdate(info);
        });
    }
}
