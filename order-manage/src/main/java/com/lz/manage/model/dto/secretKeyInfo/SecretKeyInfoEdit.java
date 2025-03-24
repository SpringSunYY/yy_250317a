package com.lz.manage.model.dto.secretKeyInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.SecretKeyInfo;
/**
 * 密钥信息Vo对象 tb_secret_key_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SecretKeyInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 安全KEY */
    private String secretKey;

    /** 状态 */
    private Long status;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param secretKeyInfoEdit 编辑对象
     * @return SecretKeyInfo
     */
    public static SecretKeyInfo editToObj(SecretKeyInfoEdit secretKeyInfoEdit) {
        if (secretKeyInfoEdit == null) {
            return null;
        }
        SecretKeyInfo secretKeyInfo = new SecretKeyInfo();
        BeanUtils.copyProperties(secretKeyInfoEdit, secretKeyInfo);
        return secretKeyInfo;
    }
}
