package com.lz.manage.model.dto.orderInfo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.OrderInfo;

/**
 * 订单Vo对象 tb_order_info
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@Data
public class OrderInfoInsert implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

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
    private Date purchaseDate;

    /**
     * ASIN
     */
    private String asin;

    /**
     * 品名
     */
    private String title;

    /**
     * 亚马逊评价链接
     */
    private String comment;

    /**
     * 亚马逊评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date evaluateTime;

    /**
     * 亚马逊评分星级
     */
    private String evaluateLevel;

    /**
     * 亚马逊评价内容
     */
    private String evaluateContent;

    /**
     * 亚马逊商品编号
     */
    private String orderItemId;

    /**
     * 亚马逊商品链接
     */
    private String goodsLink;

    /**
     * 扫码时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scanTime;

    /**
     * 买家姓名
     */
    private String buyerName;

    /**
     * 买家邮箱
     */
    private String buyerEmail;

    /**
     * 买家评分
     */
    private String buyerLevel;

    /**
     * 买家评论
     */
    private String buyerEvaluate;

    /**
     * 请求评论状态 (1"" 2请求中 3长时未回复 4疫情求成)
     */
    private String begEvaluateStatus;

    /**
     * 请求评论内容
     */
    private String begEvalueteContent;

    /**
     * 评论负责人
     */
    private String evaluatePrincipal;

    /**
     * 售后标记
     */
    private String afterSaleSign;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param orderInfoInsert 插入对象
     * @return OrderInfoInsert
     */
    public static OrderInfo insertToObj(OrderInfoInsert orderInfoInsert) {
        if (orderInfoInsert == null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderInfoInsert, orderInfo);
        return orderInfo;
    }
}
