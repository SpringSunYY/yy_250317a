package com.lz.manage.model.vo.orderInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.OrderInfo;
/**
 * 订单Vo对象 tb_order_info
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@Data
public class OrderInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Excel(name = "编号")
    private Long id;

    /** 平台 */
    @Excel(name = "平台")
    private String platform;

    /** 店铺 */
    @Excel(name = "店铺")
    private String storeId;
    private String storeName;

    /** 站点 */
    @Excel(name = "站点")
    private String marketplaceId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String amazonOrderId;

    /**
     * 卖家订单编号
     */
    @Excel(name = "卖家订单编号")
    private String sellerOrderId;

    /** 订购时间 */
    @Excel(name = "订购时间")
    private String purchaseDate;

    /** ASIN */
    @Excel(name = "ASIN")
    private String asin;

    /** 品名 */
    @Excel(name = "品名")
    private String title;

    /** 亚马逊评价链接 */
    @Excel(name = "亚马逊评价链接")
    private String comment;

    /** 亚马逊评价时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "亚马逊评价时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date evaluateTime;

    /** 亚马逊评分星级 */
    @Excel(name = "亚马逊评分星级")
    private String evaluateLevel;

    /** 亚马逊评价内容 */
    @Excel(name = "亚马逊评价内容")
    private String evaluateContent;

    /** 亚马逊商品链接 */
    @Excel(name = "亚马逊商品链接")
    private String goodsLink;

    /** 亚马逊商品编号 */
    @Excel(name = "亚马逊商品编号")
    private String orderItemId;

    /** 扫码时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "扫码时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date scanTime;

    /** 买家姓名 */
    @Excel(name = "买家姓名")
    private String buyerName;

    /** 买家邮箱 */
    @Excel(name = "买家邮箱")
    private String buyerEmail;

    /** 买家评分 */
    @Excel(name = "买家评分")
    private String buyerLevel;

    /** 买家评论 */
    @Excel(name = "买家评论")
    private String buyerEvaluate;

    /** 请求评论状态 (1"" 2请求中 3长时未回复 4疫情求成) */
    @Excel(name = "请求评论状态")
    private String begEvaluateStatus;

    /** 请求评论内容 */
    @Excel(name = "请求评论内容")
    private String begEvalueteContent;

    /** 评论负责人 */
    @Excel(name = "评论负责人")
    private String evaluatePrincipal;

    /** 售后标记 */
    @Excel(name = "售后标记")
    private String afterSaleSign;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long userId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /** 更新人 */
    @Excel(name = "更新人")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;


     /**
     * 对象转封装类
     *
     * @param orderInfo OrderInfo实体对象
     * @return OrderInfoVo
     */
    public static OrderInfoVo objToVo(OrderInfo orderInfo) {
        if (orderInfo == null) {
            return null;
        }
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        BeanUtils.copyProperties(orderInfo, orderInfoVo);
        return orderInfoVo;
    }
}
