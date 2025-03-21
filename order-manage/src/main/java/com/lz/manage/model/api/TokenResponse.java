package com.lz.manage.model.api;

import lombok.Data;

/**
 * Project: order
 * Package: com.lz.manage.model.api
 * Author: YY
 * CreateTime: 2025-03-21  23:20
 * Description: TokenResponse
 * Version: 1.0
 */
@Data
public class TokenResponse {
    private String code;
    private String msg;
    private String requestId;
    private Data data;

    @lombok.Data
    public class Data {
        private String access_token;
        private String expires_in;
    }
}
