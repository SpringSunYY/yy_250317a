package com.lz.manage.model.dto.orderInfo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Project: order
 * Package: com.lz.manage.model.dto.orderInfo
 * Author: YY
 * CreateTime: 2025-03-22  15:33
 * Description: OrderInfoApiQuery
 * Version: 1.0
 */
@Data
public class OrderInfoApiQuery implements Serializable {
    /**
     * 店铺
     */
    @NotBlank(message = "店铺唯一标识不能为空")
    private String storeId;

    /**
     * 站点
     */
    @NotBlank(message = "订单编号不能为空")
    private String amazonOrderId;
}
