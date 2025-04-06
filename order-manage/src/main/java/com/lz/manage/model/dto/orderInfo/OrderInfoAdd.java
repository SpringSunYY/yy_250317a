package com.lz.manage.model.dto.orderInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.StringUtils;
import com.lz.manage.model.domain.OrderInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单Vo对象 tb_order_info
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@Data
public class OrderInfoAdd implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @NotEmpty(message = "amazonOrderId订单号不能为空")
    private String amazonOrderId;


    /**
     * 亚马逊评价时间
     */
    @NotEmpty(message = "evaluateTime评价时间不能为空")
    private String evaluateTime;


    /**
     * 扫码时间
     */
    @NotEmpty(message = "scanTime扫码时间不能为空")
    private String scanTime;

    /**
     * 买家姓名
     */
    @NotEmpty(message = "buyerName买家姓名不能为空")
    private String buyerName;

    /**
     * 买家邮箱
     */
    @NotEmpty(message = "buyerEmail买家邮箱不能为空")
    private String buyerEmail;

    /**
     * 买家评分
     */
    @NotEmpty(message = "buyerLevel买家评分不能为空")
    private String buyerLevel;

    /**
     * 买家评论
     */
    @NotEmpty(message = "buyerEvaluate买家评论不能为空")
    private String buyerEvaluate;

    /**
     * 请求评论状态 (1"" 2请求中 3长时未回复 4请求完成)
     */
    private String begEvaluateStatus;


    /**
     * 对象转封装类
     *
     * @param orderInfoAdd 插入对象
     * @return OrderInfoInsert
     */
    public static OrderInfo insertToObj(OrderInfoAdd orderInfoAdd) {
        if (StringUtils.isNull(orderInfoAdd)) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderInfoAdd, orderInfo);
        return orderInfo;
    }
}
