package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.SecretKeyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 密钥信息Mapper接口
 * 
 * @author YY
 * @date 2025-03-24
 */
public interface SecretKeyInfoMapper extends BaseMapper<SecretKeyInfo>
{
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
     * 删除密钥信息
     * 
     * @param id 密钥信息主键
     * @return 结果
     */
    public int deleteSecretKeyInfoById(Long id);

    /**
     * 批量删除密钥信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSecretKeyInfoByIds(Long[] ids);
}
