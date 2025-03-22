package com.lz.manage.model.api;

import com.lz.manage.model.dto.storeInfo.StoreInfoResult;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Project: order
 * Package: com.lz.manage.model.api
 * Author: YY
 * CreateTime: 2025-03-22  11:55
 * Description: StoreInfoResponse
 * Version: 1.0
 */
@Data
public class StoreInfoResponse implements Serializable {
    private String code;
    private String msg;
    private String requestId;
    private List<StoreInfoResult> data;
}
