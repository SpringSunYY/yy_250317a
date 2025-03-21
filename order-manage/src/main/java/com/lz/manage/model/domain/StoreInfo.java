package com.lz.manage.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * 店铺信息对象 tb_store_info
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@TableName("tb_store_info")
@Data
public class StoreInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    @Excel(name = "编号")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 店铺唯一标识 */
    @Excel(name = "店铺唯一标识")
    private String storeId;


    /** 店铺名称 */
    @Excel(name = "店铺名称")
    private String name;

    /** 亚马逊卖家编号 */
    @Excel(name = "亚马逊卖家编号")
    private String sellerId;

    /** 大区 */
    @Excel(name = "大区")
    private String region;

    /** 站点ID */
    @Excel(name = "站点ID")
    private String marketplaceId;

    /** 广告授权状态 */
    @Excel(name = "广告授权状态")
    private String adStatus;

    /** 店铺状态 */
    @Excel(name = "店铺状态")
    private String status;

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

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
