package com.lz.manage.model.dto.storeInfo;

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
import com.lz.manage.model.domain.StoreInfo;
/**
 * 店铺信息Query对象 tb_store_info
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@Data
public class StoreInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    private String id;

    /** 店铺名称 */
    private String name;

    /** 亚马逊卖家编号 */
    private String sellerId;

    /** 大区 */
    private String region;

    /** 站点ID */
    private String marketplaceId;

    /** 广告授权状态 */
    private String adStatus;

    /** 店铺状态 */
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param storeInfoQuery 查询对象
     * @return StoreInfo
     */
    public static StoreInfo queryToObj(StoreInfoQuery storeInfoQuery) {
        if (storeInfoQuery == null) {
            return null;
        }
        StoreInfo storeInfo = new StoreInfo();
        BeanUtils.copyProperties(storeInfoQuery, storeInfo);
        return storeInfo;
    }
}
