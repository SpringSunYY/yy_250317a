package com.lz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.manage.mapper.OrderInfoMapper;
import com.lz.manage.model.api.CommodityDetailResponse;
import com.lz.manage.model.api.OrderInfoResponse;
import com.lz.manage.model.api.ReviewResponse;
import com.lz.manage.model.domain.OrderInfo;
import com.lz.manage.model.domain.StoreInfo;
import com.lz.manage.model.dto.orderInfo.OrderInfoApiQuery;
import com.lz.manage.model.dto.orderInfo.OrderInfoQuery;
import com.lz.manage.model.vo.orderInfo.OrderInfoVo;
import com.lz.manage.service.IApiService;
import com.lz.manage.service.IOrderInfoService;
import com.lz.manage.service.IStoreInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

        String purchaseDate = orderInfoQuery.getPurchaseDate();
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
        if (StringUtils.isNull(orderInfoByApi)) {
            throw new ServiceException("订单信息不存在", NO_CONTENT);
        }
        System.out.println("227getOrderInfoByApi orderInfoByApi = " + orderInfoByApi);
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
            if (StringUtils.isNotNull(commodityDetail)) {
                orderInfo.setGoodsLink(commodityDetail.getSourceUrls());
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
            log.error("获取订单信息失败", e);
            return orderInfo;
        }
        System.out.println("270getOrderInfoByApi orderInfo = " + orderInfo);
        return orderInfo;
    }

    @Override
    public OrderInfo externalAdd(OrderInfo orderInfo) {
        //根据Amazon订单号查询订单信息
        OrderInfo one = this.getOne(new LambdaQueryWrapper<>(OrderInfo.class).eq(OrderInfo::getAmazonOrderId, orderInfo.getAmazonOrderId()));
        if (StringUtils.isNotNull(one)) {
            throw new ServiceException("订单信息已经评价，无需再评价", NOT_MODIFIED);
        }
        OrderInfoApiQuery orderInfoApiQuery = new OrderInfoApiQuery();
        StoreInfo storeInfo = storeInfoService.selectStoreInfoByStoreId(orderInfo.getStoreId());
        if (StringUtils.isNull(storeInfo)) {
            throw new ServiceException("店铺信息不存在", NO_CONTENT);
        }
        orderInfoApiQuery.setStoreId(storeInfo.getStoreId());
        orderInfoApiQuery.setAmazonOrderId(orderInfo.getAmazonOrderId());
        orderInfoApiQuery.setSellerOrderId(orderInfo.getSellerOrderId());
        OrderInfo orderInfoByApi = this.getOrderInfoByApi(orderInfoApiQuery);
        //设置初值
        orderInfo.setStoreId(storeInfo.getStoreId());
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
        this.insertOrderInfo(orderInfo);
        return orderInfo;
    }
}
