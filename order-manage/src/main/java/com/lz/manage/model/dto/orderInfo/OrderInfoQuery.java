package com.lz.manage.model.dto.orderInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import com.lz.manage.model.domain.OrderInfo;
/**
 * 订单Query对象 tb_order_info
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@Data
public class OrderInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 平台 */
    private String platform;

    /** 店铺 */
    private String storeId;

    /** 站点 */
    private String marketplaceId;

    /** 订单号 */
    private String amazonOrderId;

    /** 订购时间 */
    private String purchaseDate;

    /** ASIN */
    private String asin;

    /** 品名 */
    private String title;

    /** 扫码时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scanTime;

    /** 买家姓名 */
    private String buyerName;

    /** 买家邮箱 */
    private String buyerEmail;

    /** 请求评论状态 (1"" 2请求中 3长时未回复 4疫情求成) */
    private String begEvaluateStatus;

    /** 评论负责人 */
    private String evaluatePrincipal;

    /** 售后标记 */
    private String afterSaleSign;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 备注 */
    private String remark;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param orderInfoQuery 查询对象
     * @return OrderInfo
     */
    public static OrderInfo queryToObj(OrderInfoQuery orderInfoQuery) {
        if (orderInfoQuery == null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderInfoQuery, orderInfo);
        return orderInfo;
    }
}
