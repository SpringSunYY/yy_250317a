package com.lz.manage.model.vo.secretKeyInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.SecretKeyInfo;
/**
 * 密钥信息Vo对象 tb_secret_key_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SecretKeyInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    private Long id;

    /** 密钥编号 */
    @Excel(name = "密钥编号")
    private String accessKeyId;

    /** 安全KEY */
    @Excel(name = "安全KEY")
    private String secretKey;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 创建人 */
    @Excel(name = "创建人")
    private String userName;

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


     /**
     * 对象转封装类
     *
     * @param secretKeyInfo SecretKeyInfo实体对象
     * @return SecretKeyInfoVo
     */
    public static SecretKeyInfoVo objToVo(SecretKeyInfo secretKeyInfo) {
        if (secretKeyInfo == null) {
            return null;
        }
        SecretKeyInfoVo secretKeyInfoVo = new SecretKeyInfoVo();
        BeanUtils.copyProperties(secretKeyInfo, secretKeyInfoVo);
        return secretKeyInfoVo;
    }
}
