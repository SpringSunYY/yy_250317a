package com.lz.manage.model.api;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;
    private String requestId;
    private Data data;

    @lombok.Data
    public static class Data {
        private String pageNo;
        private String pageSize;
        private int totalSize;
        private List<Row> rows;
    }

    @lombok.Data
    public static class Row {
        private String shopId;
        private String amazonOrderId;
        private String orderStatus;
        private String orderTotalCurrency;
        private String orderTotalAmount;
        private String marketplaceId;
        private String orderType;
        private String comment;
        private String commentColor;
        private String buyerName;
        private String buyerEmail;
        private String fulfillmentChannel;
        private List<OrderItem> orderItemVoList;
        private String orderProfit;
        private String fbmCostOrigin;
        private String fbmCost;
        private String isReturnOrder;
        private String isBusinessOrder;
        private String isReplacementOrder;
        private String replacedOrderId;
        private String promotionFlag;
        private String orderFlag;
        private String promotionIds;
        private String isBuyerRequestedCancel;
        private String refundStatus;
        private String refundDate;
        private Date purchaseDate;
        private String lastUpdateDate;
        private String isVineOrder;
        private String earliestShipDate;
        private String latestShipDate;
        private String latestDeliveryDate;
        private String earliestDeliveryDate;
        private String isPrime;
        private String customOrder;
        private int lowCostStore;

        @lombok.Data
        public static class OrderItem {
            private String orderItemId;
            private String commoditySku;
            private String title;
            private String asin;
            private String sellerSku;
            private String quantityOrdered;
            private String quantityShipped;
            private String quantityUnfulfillable;
            private String refundNum;
            private String refundAmount;
            private String itemPriceCurrency;
            private String itemPriceAmount;
            private String imageUrl;
            private String fnsku;
            private String mergePurchaseCost;
            private String purchaseCost;
            private String headTripCost;
            private String headTripShare;
            private String fbmShipCost;
            private String asinUrl;
            private String iossNumber;
            private String itemPrincipalAmountOri;
            private String itemTaxAmount;
            private String promotionIds;
            private String fbaPerUnitFulfillmentFee;
            private String commission;
            private String amazonBackToArticle;
            private String withheldTaxAmount;
            private String giftWrapTaxAmount;
            private String shippingTaxAmount;
            private String giftWrapAmount;
            private String shippingCharge;
            private int promotionDiscountAmount;
            private String otherAmount;
        }
    }
}
