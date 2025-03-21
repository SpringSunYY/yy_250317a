package com.lz.manage.model.dto.storeInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.StoreInfo;
/**
 * 店铺信息Vo对象 tb_store_info
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@Data
public class StoreInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    private String id;

    private String storeId;

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

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param storeInfoInsert 插入对象
     * @return StoreInfoInsert
     */
    public static StoreInfo insertToObj(StoreInfoInsert storeInfoInsert) {
        if (storeInfoInsert == null) {
            return null;
        }
        StoreInfo storeInfo = new StoreInfo();
        BeanUtils.copyProperties(storeInfoInsert, storeInfo);
        return storeInfo;
    }
}
