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
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;
    private String requestId;
    private Data data;
    @lombok.Data
    public static class Data {
        private Long pageNo;
        private Long pageSize;
        private Long totalPage;
        private Long totalSize;
        private List<StoreInfoResult> rows;
    }
}
