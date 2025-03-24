package com.lz.manage.model.dto.secretKeyInfo;

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
import com.lz.manage.model.domain.SecretKeyInfo;
/**
 * 密钥信息Query对象 tb_secret_key_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SecretKeyInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 密钥编号 */
    private String accessKeyId;

    /** 状态 */
    private Long status;

    /** 创建人 */
    private String userName;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param secretKeyInfoQuery 查询对象
     * @return SecretKeyInfo
     */
    public static SecretKeyInfo queryToObj(SecretKeyInfoQuery secretKeyInfoQuery) {
        if (secretKeyInfoQuery == null) {
            return null;
        }
        SecretKeyInfo secretKeyInfo = new SecretKeyInfo();
        BeanUtils.copyProperties(secretKeyInfoQuery, secretKeyInfo);
        return secretKeyInfo;
    }
}
