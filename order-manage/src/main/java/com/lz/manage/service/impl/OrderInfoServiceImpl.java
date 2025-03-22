package com.lz.manage.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;

import javax.annotation.Resource;

import com.lz.manage.model.api.OrderInfoResponse;
import com.lz.manage.model.domain.StoreInfo;
import com.lz.manage.model.dto.orderInfo.OrderInfoApiQuery;
import com.lz.manage.service.IApiService;
import com.lz.manage.service.IStoreInfoService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.manage.mapper.OrderInfoMapper;
import com.lz.manage.model.domain.OrderInfo;
import com.lz.manage.service.IOrderInfoService;
import com.lz.manage.model.dto.orderInfo.OrderInfoQuery;
import com.lz.manage.model.vo.orderInfo.OrderInfoVo;

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
            StoreInfo storeInfo = storeInfoService.selectStoreInfoById(info.getStoreId());
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
    public OrderInfoResponse.Data getOrderInfoByApi(OrderInfoApiQuery orderInfoApiQuery) {
        StoreInfo storeInfo = storeInfoService.selectStoreInfoById(orderInfoApiQuery.getStoreId());
        OrderInfoResponse.Data orderInfo = apiService.getOrderInfo(storeInfo.getStoreId(), orderInfoApiQuery.getAmazonOrderId());
        if (StringUtils.isNull(orderInfo)) {
            throw new RuntimeException("订单信息不存在");
        }
        return orderInfo;
    }
}
