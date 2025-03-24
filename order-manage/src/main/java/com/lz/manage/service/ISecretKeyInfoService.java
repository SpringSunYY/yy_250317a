package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.SecretKeyInfo;
import com.lz.manage.model.vo.secretKeyInfo.SecretKeyInfoVo;
import com.lz.manage.model.dto.secretKeyInfo.SecretKeyInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 密钥信息Service接口
 * 
 * @author YY
 * @date 2025-03-24
 */
public interface ISecretKeyInfoService extends IService<SecretKeyInfo>
{
    //region mybatis代码
    /**
     * 查询密钥信息
     * 
     * @param id 密钥信息主键
     * @return 密钥信息
     */
    public SecretKeyInfo selectSecretKeyInfoById(Long id);

    /**
     * 查询密钥信息列表
     * 
     * @param secretKeyInfo 密钥信息
     * @return 密钥信息集合
     */
    public List<SecretKeyInfo> selectSecretKeyInfoList(SecretKeyInfo secretKeyInfo);

    /**
     * 新增密钥信息
     * 
     * @param secretKeyInfo 密钥信息
     * @return 结果
     */
    public int insertSecretKeyInfo(SecretKeyInfo secretKeyInfo);

    /**
     * 修改密钥信息
     * 
     * @param secretKeyInfo 密钥信息
     * @return 结果
     */
    public int updateSecretKeyInfo(SecretKeyInfo secretKeyInfo);

    /**
     * 批量删除密钥信息
     * 
     * @param ids 需要删除的密钥信息主键集合
     * @return 结果
     */
    public int deleteSecretKeyInfoByIds(Long[] ids);

    /**
     * 删除密钥信息信息
     * 
     * @param id 密钥信息主键
     * @return 结果
     */
    public int deleteSecretKeyInfoById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param secretKeyInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<SecretKeyInfo> getQueryWrapper(SecretKeyInfoQuery secretKeyInfoQuery);

    /**
     * 转换vo
     *
     * @param secretKeyInfoList SecretKeyInfo集合
     * @return SecretKeyInfoVO集合
     */
    List<SecretKeyInfoVo> convertVoList(List<SecretKeyInfo> secretKeyInfoList);
}
