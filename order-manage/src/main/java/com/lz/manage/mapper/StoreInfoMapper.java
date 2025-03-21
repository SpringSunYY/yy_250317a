package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.StoreInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 店铺信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-21
 */
public interface StoreInfoMapper extends BaseMapper<StoreInfo>
{
    /**
     * 查询店铺信息
     * 
     * @param id 店铺信息主键
     * @return 店铺信息
     */
    public StoreInfo selectStoreInfoById(String id);

    /**
     * 查询店铺信息列表
     * 
     * @param storeInfo 店铺信息
     * @return 店铺信息集合
     */
    public List<StoreInfo> selectStoreInfoList(StoreInfo storeInfo);

    /**
     * 新增店铺信息
     * 
     * @param storeInfo 店铺信息
     * @return 结果
     */
    public int insertStoreInfo(StoreInfo storeInfo);

    /**
     * 修改店铺信息
     * 
     * @param storeInfo 店铺信息
     * @return 结果
     */
    public int updateStoreInfo(StoreInfo storeInfo);

    /**
     * 删除店铺信息
     * 
     * @param id 店铺信息主键
     * @return 结果
     */
    public int deleteStoreInfoById(String id);

    /**
     * 批量删除店铺信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreInfoByIds(String[] ids);
}
