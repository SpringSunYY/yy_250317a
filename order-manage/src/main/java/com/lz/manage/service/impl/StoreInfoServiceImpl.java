package com.lz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.manage.mapper.StoreInfoMapper;
import com.lz.manage.model.domain.StoreInfo;
import com.lz.manage.model.dto.storeInfo.StoreInfoQuery;
import com.lz.manage.model.vo.storeInfo.StoreInfoVo;
import com.lz.manage.service.IStoreInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 店铺信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@Service
public class StoreInfoServiceImpl extends ServiceImpl<StoreInfoMapper, StoreInfo> implements IStoreInfoService {
    @Resource
    private StoreInfoMapper storeInfoMapper;

    //region mybatis代码

    /**
     * 查询店铺信息
     *
     * @param id 店铺信息主键
     * @return 店铺信息
     */
    @Override
    public StoreInfo selectStoreInfoById(String id) {
        return storeInfoMapper.selectStoreInfoById(id);
    }

    /**
     * 查询店铺信息列表
     *
     * @param storeInfo 店铺信息
     * @return 店铺信息
     */
    @Override
    public List<StoreInfo> selectStoreInfoList(StoreInfo storeInfo) {
        return storeInfoMapper.selectStoreInfoList(storeInfo);
    }

    /**
     * 新增店铺信息
     *
     * @param storeInfo 店铺信息
     * @return 结果
     */
    @Override
    public int insertStoreInfo(StoreInfo storeInfo) {
        storeInfo.setUserId(SecurityUtils.getUserId());
        storeInfo.setCreateTime(DateUtils.getNowDate());
        return storeInfoMapper.insertStoreInfo(storeInfo);
    }

    /**
     * 修改店铺信息
     *
     * @param storeInfo 店铺信息
     * @return 结果
     */
    @Override
    public int updateStoreInfo(StoreInfo storeInfo) {
        storeInfo.setUpdateBy(SecurityUtils.getUsername());
        storeInfo.setUpdateTime(DateUtils.getNowDate());
        return storeInfoMapper.updateStoreInfo(storeInfo);
    }

    /**
     * 批量删除店铺信息
     *
     * @param ids 需要删除的店铺信息主键
     * @return 结果
     */
    @Override
    public int deleteStoreInfoByIds(String[] ids) {
        return storeInfoMapper.deleteStoreInfoByIds(ids);
    }

    /**
     * 删除店铺信息信息
     *
     * @param id 店铺信息主键
     * @return 结果
     */
    @Override
    public int deleteStoreInfoById(String id) {
        return storeInfoMapper.deleteStoreInfoById(id);
    }

    //endregion
    @Override
    public QueryWrapper<StoreInfo> getQueryWrapper(StoreInfoQuery storeInfoQuery) {
        QueryWrapper<StoreInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = storeInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String id = storeInfoQuery.getId();
        queryWrapper.eq(StringUtils.isNotEmpty(id), "id", id);

        String name = storeInfoQuery.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);

        String sellerId = storeInfoQuery.getSellerId();
        queryWrapper.like(StringUtils.isNotEmpty(sellerId), "seller_id", sellerId);

        String region = storeInfoQuery.getRegion();
        queryWrapper.like(StringUtils.isNotEmpty(region), "region", region);

        String marketplaceId = storeInfoQuery.getMarketplaceId();
        queryWrapper.like(StringUtils.isNotEmpty(marketplaceId), "marketplace_id", marketplaceId);

        String adStatus = storeInfoQuery.getAdStatus();
        queryWrapper.like(StringUtils.isNotEmpty(adStatus), "ad_status", adStatus);

        String status = storeInfoQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status), "status", status);

        Date createTime = storeInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<StoreInfoVo> convertVoList(List<StoreInfo> storeInfoList) {
        if (StringUtils.isEmpty(storeInfoList)) {
            return Collections.emptyList();
        }
        return storeInfoList.stream().map(StoreInfoVo::objToVo).collect(Collectors.toList());
    }

}
