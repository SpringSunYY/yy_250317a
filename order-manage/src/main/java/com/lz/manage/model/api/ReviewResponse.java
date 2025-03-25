package com.lz.manage.model.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            private int star;
            private String title;
            private String content;
            private String contentUrl;
            private List<String> shopNameList;
            private String marketplaceName;
            private String reviewId;

            @JsonInclude(JsonInclude.Include.NON_NULL)
            private String amazonOrderId;

            @JsonFormat(pattern = "yyyy-MM-dd")
            private LocalDate reviewDate;

            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            private LocalDateTime updateTime;

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
