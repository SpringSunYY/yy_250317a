package com.lz.manage.model.dto.orderInfo;

import com.lz.common.utils.StringUtils;
import com.lz.manage.model.domain.OrderInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 订单Vo对象 tb_order_info
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@Data
public class OrderQuery implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 订单号
     */
    @NotEmpty(message = "amazonOrderId订单号不能为空")
    private String amazonOrderId;


    /**
     * 对象转封装类
     *
     * @param orderQuery 插入对象
     * @return OrderInfoInsert
     */
    public static OrderInfo insertToObj(OrderQuery orderQuery) {
        if (StringUtils.isNull(orderQuery)) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderQuery, orderInfo);
        return orderInfo;
    }
}
