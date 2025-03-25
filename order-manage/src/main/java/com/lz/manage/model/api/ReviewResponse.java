package com.lz.manage.model.api;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

@lombok.Data
public class ReviewResponse {

    @lombok.Data
    public static class Data {
        private int pageNo;
        private int pageSize;
        private int totalPage;
        private int totalSize;
        private List<Review> rows;

        @lombok.Data
        public static class Review {
            private String imageUrl;
            private String asin;
            private String parentAsin;
            private String asinUrl;
            private String star;
            private String title;
            private String content;
            private String contentUrl;
            private List<String> shopNameList;
            private String marketplaceName;
            private String reviewId;

            private String amazonOrderId;

            private Date reviewDate;

            private Date updateTime;

            private String remark;
            private int reviewStatus;
            private int status;
            private int vn;
            private int vp;
        }
    }

    private String code;
    private String msg;
    private Data data;
    private String requestId;
}
