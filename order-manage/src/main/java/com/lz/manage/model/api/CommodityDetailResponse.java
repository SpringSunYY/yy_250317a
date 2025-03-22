package com.lz.manage.model.api;

import java.io.Serializable;

/**
 * Project: order
 * Package: com.lz.manage.model.api
 * Author: YY
 * CreateTime: 2025-03-22  23:06
 * Description: CommodityDetailResponse
 * Version: 1.0
 */
@lombok.Data
public class CommodityDetailResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;
    private String requestId;
    private Data data;
    @lombok.Data
    public static class Data {
        private String id;
        private String sourceUrls;
    }
}
