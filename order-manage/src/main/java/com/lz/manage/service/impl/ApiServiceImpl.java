package com.lz.manage.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.utils.StringUtils;
import com.lz.manage.model.api.TokenResponse;
import com.lz.manage.service.IApiService;
import com.lz.system.service.ISysConfigService;

import javax.annotation.Resource;

/**
 * Project: order
 * Package: com.lz.manage.service.impl
 * Author: YY
 * CreateTime: 2025-03-21  23:09
 * Description: ApiServiceImpl
 * Version: 1.0
 */
public class ApiServiceImpl implements IApiService {
    @Resource
    private RedisCache redisCache;

    @Resource
    private ISysConfigService configService;

    public void getToken() {
        String clientId = configService.selectConfigByKey("clientId");
        String clientSecret = configService.selectConfigByKey("clientSecret");
        String grantType = configService.selectConfigByKey("grantType");
        if (StringUtils.isNotEmpty(clientId)
                || StringUtils.isNotEmpty(clientSecret)
                || StringUtils.isNotEmpty(grantType)) {
            throw new RuntimeException("请先配置clientId,clientSecret,grantType");
        }
        String url = StringUtils.format("https://openapi.sellfox.com/api/oauth/v2/token.json?client_id={}&client_secret={}&grant_type={}", clientId, clientSecret, grantType);
        System.out.println("curl -X GET \"" + url + "\"");
        //发送请求
        HttpRequest get = HttpUtil.createGet(url);
        String body = get.execute().body();
        TokenResponse tokenResponse = JSONObject.parseObject(body, TokenResponse.class);
        if (!tokenResponse.getCode().equals("0")) {
            throw new RuntimeException("获取token失败");
        }
        TokenResponse.Data data = tokenResponse.getData();
        redisCache.setCacheObject("token", data.getAccess_token());
    }
}
