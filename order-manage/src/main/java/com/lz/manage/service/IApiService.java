package com.lz.manage.service;

import com.lz.manage.model.api.CommodityDetailResponse;
import com.lz.manage.model.api.OrderInfoResponse;
import com.lz.manage.model.api.ReviewResponse;
import com.lz.manage.model.dto.storeInfo.StoreInfoResult;

import java.util.List;

/**
 * Project: order
 * Package: com.lz.manage.service
 * Author: YY
 * CreateTime: 2025-03-21  23:09
 * Description: IApiService
 * Version: 1.0
 */
public interface IApiService {
    /**
     * description: 获取token
     * author: YY
     * method: getToken
     * date: 2025/3/21 23:26
     * param:
     * return: void
     **/
    String getToken();

    /**
     * description: 获取店铺信息
     * author: YY
     * method: getStoreInfo
     * date: 2025/3/21 23:26
     * param:
     * return: java.util.List<com.lz.manage.model.api.StoreInfoResponse>
     **/
    List<StoreInfoResult> getStoreInfo();

    /**
     * description: 获取订单信息
     * author: YY
     * method: getOrderInfo
     * date: 2025/3/21 23:26
     * param:
     * return: com.lz.manage.model.api.OrderInfoResponse
     **/
    OrderInfoResponse.Data getOrderInfo(String shopId, String amazonOrderId,String sellerOrderId);

    /**
     * description: 获取商品详情
     * author: YY
     * method: getCommodityDetail
     * date: 2025/3/21 23:26
     * param:
     * return: com.lz.manage.model.api.CommodityDetailResponse
     **/
    CommodityDetailResponse.Data getCommodityDetail(String id);

    /**
     * description: 获取商品评论详情
     * author: YY
     * method: getCommodityDetail
     * date: 2025/3/21 23:26
     * param:
     * return: com.lz.manage.model.api.CommodityDetailResponse
     **/
    ReviewResponse.Data getReviewDetailList(String amazonOrderId);
}
