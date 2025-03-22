package com.lz.manage.model.api;

import com.lz.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Project: order
 * Package: com.lz.manage.model.api
 * Author: YY
 * CreateTime: 2025-03-22  12:32
 * Description: OrderInfoResponse
 * Version: 1.0
 */
@Data
public class OrderInfoResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;
    private String requestId;
    private Data data;

    @lombok.Data
    public static class Data {
        /**
         * 平台
         */
        private String platform;

        /**
         * 店铺
         */
        private String storeId;

        /**
         * 站点
         */
        private String marketplaceId;

        /**
         * 订单号
         */
        private String amazonOrderId;

        /**
         * 卖家订单编号
         */
        private String sellerOrderId;
        /**
         * 订购时间
         */
        private String purchaseDate;

        /**
         * ASIN
         */
        private String asin;

        /**
         * 品名
         */
        private String title;

        private List<OrderItemVoList> orderItemVoList;

        /**
         * 商品id
         */
        private String orderItemId;

        /**
         * 亚马逊商品链接
         */
        private String goodsLink;
    }

    @lombok.Data
    public static class OrderItemVoList {
        private String title;
        private String asin;
        private String orderItemId;
    }
}
