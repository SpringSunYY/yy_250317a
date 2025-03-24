package com.lz.manage.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.StringUtils;
import com.lz.manage.model.domain.SecretKeyInfo;
import com.lz.manage.service.ISecretKeyInfoService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.lz.common.constant.HttpStatus.BAD_REQUEST;
import static com.lz.common.constant.HttpStatus.UNAUTHORIZED;

/**
 * Project: order
 * Package: com.lz.manage.auth
 * Author: YY
 * CreateTime: 2025-03-24  13:32
 * Description: SecretKeyService
 * 校验安全key
 * Version: 1.0
 */
@Service("sk")
public class SecretKeyService {
    @Resource
    private ISecretKeyInfoService secretKeyInfoService;

    /**
     * 校验安全key
     *
     * @return
     */
    public boolean hasKey() {
        String accessKeyId = getCurrentHttpRequest().getHeader("accessKeyId");
        String secretKey = getCurrentHttpRequest().getHeader("secretKey");
        if (StringUtils.isEmpty(accessKeyId) || StringUtils.isEmpty(secretKey)) {
            throw new ServiceException("安全key校验失败,请输入key和密钥",BAD_REQUEST);
        }
        SecretKeyInfo secretKeyInfo = secretKeyInfoService.getOne(new LambdaQueryWrapper<SecretKeyInfo>().eq(SecretKeyInfo::getAccessKeyId, accessKeyId).eq(SecretKeyInfo::getSecretKey, secretKey));
        if (StringUtils.isNull(secretKeyInfo)) {
            throw new ServiceException("安全key校验失败",UNAUTHORIZED);
        }
        if (secretKeyInfo.getStatus() == 1) {
            return true;
        } else {
            throw new ServiceException("安全key校验失败,key已被禁用",UNAUTHORIZED);
        }
    }

    /**
     * 获取当前 HttpServletRequest
     */
    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }
}
