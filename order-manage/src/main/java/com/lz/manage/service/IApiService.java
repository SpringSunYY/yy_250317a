package com.lz.manage.service;

import com.lz.manage.model.api.StoreInfoResponse;
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
    void getToken();

    /**
     * description: 获取店铺信息
     * author: YY
     * method: getStoreInfo
     * date: 2025/3/21 23:26
     * param:
     * return: java.util.List<com.lz.manage.model.api.StoreInfoResponse>
     **/
    List<StoreInfoResult> getStoreInfo(Long pageNo, Long pageSize);
}
