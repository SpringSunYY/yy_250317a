package com.lz.manage.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;

import javax.annotation.Resource;

import com.lz.common.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.manage.mapper.SecretKeyInfoMapper;
import com.lz.manage.model.domain.SecretKeyInfo;
import com.lz.manage.service.ISecretKeyInfoService;
import com.lz.manage.model.dto.secretKeyInfo.SecretKeyInfoQuery;
import com.lz.manage.model.vo.secretKeyInfo.SecretKeyInfoVo;

import static com.lz.common.utils.SecurityUtils.getLoginUser;

/**
 * 密钥信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class SecretKeyInfoServiceImpl extends ServiceImpl<SecretKeyInfoMapper, SecretKeyInfo> implements ISecretKeyInfoService {
    @Resource
    private SecretKeyInfoMapper secretKeyInfoMapper;

    //region mybatis代码

    /**
     * 查询密钥信息
     *
     * @param id 密钥信息主键
     * @return 密钥信息
     */
    @Override
    public SecretKeyInfo selectSecretKeyInfoById(Long id) {
        return secretKeyInfoMapper.selectSecretKeyInfoById(id);
    }

    /**
     * 查询密钥信息列表
     *
     * @param secretKeyInfo 密钥信息
     * @return 密钥信息
     */
    @Override
    public List<SecretKeyInfo> selectSecretKeyInfoList(SecretKeyInfo secretKeyInfo) {
        return secretKeyInfoMapper.selectSecretKeyInfoList(secretKeyInfo);
    }

    /**
     * 新增密钥信息
     *
     * @param secretKeyInfo 密钥信息
     * @return 结果
     */
    @Override
    public int insertSecretKeyInfo(SecretKeyInfo secretKeyInfo) {
        secretKeyInfo.setUserName(SecurityUtils.getUsername());
        secretKeyInfo.setCreateTime(DateUtils.getNowDate());
        //生成key
        secretKeyInfo.setAccessKeyId(IdUtils.fastSimpleUUID());
        //加密
//        secretKeyInfo.setSecretKey(SecurityUtils.encryptPassword(secretKeyInfo.getSecretKey()));
        return secretKeyInfoMapper.insertSecretKeyInfo(secretKeyInfo);
    }

    /**
     * 修改密钥信息
     *
     * @param secretKeyInfo 密钥信息
     * @return 结果
     */
    @Override
    public int updateSecretKeyInfo(SecretKeyInfo secretKeyInfo) {
        //加密
//        secretKeyInfo.setSecretKey(SecurityUtils.encryptPassword(secretKeyInfo.getSecretKey()));
        secretKeyInfo.setUpdateBy(SecurityUtils.getUsername());
        secretKeyInfo.setUpdateTime(DateUtils.getNowDate());
        return secretKeyInfoMapper.updateSecretKeyInfo(secretKeyInfo);
    }

    /**
     * 批量删除密钥信息
     *
     * @param ids 需要删除的密钥信息主键
     * @return 结果
     */
    @Override
    public int deleteSecretKeyInfoByIds(Long[] ids) {
        return secretKeyInfoMapper.deleteSecretKeyInfoByIds(ids);
    }

    /**
     * 删除密钥信息信息
     *
     * @param id 密钥信息主键
     * @return 结果
     */
    @Override
    public int deleteSecretKeyInfoById(Long id) {
        return secretKeyInfoMapper.deleteSecretKeyInfoById(id);
    }

    //endregion
    @Override
    public QueryWrapper<SecretKeyInfo> getQueryWrapper(SecretKeyInfoQuery secretKeyInfoQuery) {
        QueryWrapper<SecretKeyInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = secretKeyInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = secretKeyInfoQuery.getId();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);

        String accessKeyId = secretKeyInfoQuery.getAccessKeyId();
        queryWrapper.like(StringUtils.isNotEmpty(accessKeyId), "access_key_id", accessKeyId);

        Long status = secretKeyInfoQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotNull(status), "status", status);

        String userName = secretKeyInfoQuery.getUserName();
        queryWrapper.like(StringUtils.isNotEmpty(userName), "user_name", userName);

        Date createTime = secretKeyInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String updateBy = secretKeyInfoQuery.getUpdateBy();
        queryWrapper.like(StringUtils.isNotEmpty(updateBy), "update_by", updateBy);

        Date updateTime = secretKeyInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<SecretKeyInfoVo> convertVoList(List<SecretKeyInfo> secretKeyInfoList) {
        if (StringUtils.isEmpty(secretKeyInfoList)) {
            return Collections.emptyList();
        }
        return secretKeyInfoList.stream().map(SecretKeyInfoVo::objToVo).collect(Collectors.toList());
    }

}
