package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.StoreInfo;
import com.lz.manage.model.vo.storeInfo.StoreInfoVo;
import com.lz.manage.model.dto.storeInfo.StoreInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.validation.constraints.NotBlank;

/**
 * 店铺信息Service接口
 * 
 * @author ruoyi
 * @date 2025-03-21
 */
public interface IStoreInfoService extends IService<StoreInfo>
{
    //region mybatis代码
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
     * 批量删除店铺信息
     * 
     * @param ids 需要删除的店铺信息主键集合
     * @return 结果
     */
    public int deleteStoreInfoByIds(String[] ids);

    /**
     * 删除店铺信息信息
     * 
     * @param id 店铺信息主键
     * @return 结果
     */
    public int deleteStoreInfoById(String id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param storeInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<StoreInfo> getQueryWrapper(StoreInfoQuery storeInfoQuery);

    /**
     * 转换vo
     *
     * @param storeInfoList StoreInfo集合
     * @return StoreInfoVO集合
     */
    List<StoreInfoVo> convertVoList(List<StoreInfo> storeInfoList);

    int syncStoreInfo(StoreInfo storeInfo);

    StoreInfo selectStoreInfoByStoreId( String storeId);
}
