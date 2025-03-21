package com.lz.manage.model.dto.storeInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 店铺信息对象 tb_store_info
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@TableName("tb_store_info")
@Data
public class StoreInfoResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 亚马逊卖家编号
     */
    private String sellerId;

    /**
     * 大区
     */
    private String region;

    /**
     * 站点ID
     */
    private String marketplaceId;

    /**
     * 广告授权状态
     */
    private String adStatus;

    /**
     * 店铺状态
     */
    private String status;
}
