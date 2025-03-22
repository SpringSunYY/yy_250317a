package com.lz.manage.service.impl;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.StringUtils;
import com.lz.manage.model.api.StoreInfoResponse;
import com.lz.manage.model.api.TokenResponse;
import com.lz.manage.service.IApiService;
import com.lz.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Project: order
 * Package: com.lz.manage.service.impl
 * Author: YY
 * CreateTime: 2025-03-21  23:09
 * Description: ApiServiceImpl
 * Version: 1.0
 */
@Slf4j
public class ApiServiceImpl implements IApiService {
    @Resource
    private RedisCache redisCache;

    @Resource
    private ISysConfigService configService;

    @Override
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
        HttpRequest request = HttpUtil.createGet(url);
        HttpResponse response = request.execute();
        if (!response.isOk()) {
            throw new RuntimeException("获取token请求失败");
        }
        String body = response.body();
        TokenResponse tokenResponse = JSONObject.parseObject(body, TokenResponse.class);
        if (!tokenResponse.getCode().equals("0")) {
            throw new RuntimeException("获取token失败");
        }
        TokenResponse.Data data = tokenResponse.getData();
        redisCache.setCacheObject("token", data.getAccess_token());
    }

    @Override
    public List<StoreInfoResponse> getStoreInfo(Long pageNo, Long pageSize) {
        String clientId = configService.selectConfigByKey("clientId");
        String clientSecret = configService.selectConfigByKey("clientSecret");
        String token = redisCache.getCacheObject("token");
        if (StringUtils.isNotEmpty(clientId)
                || StringUtils.isNotEmpty(clientSecret)) {
            throw new RuntimeException("请先配置clientId,clientSecret");
        }
        //url
        String url = "https://openapi.sellfox.com/api/shop/pageList.json";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(new Random().nextInt(100000));
        String sign = null;
        try {
            sign = generateSign("/api/shop/pageList.json", "post", token, clientId, timestamp, nonce, clientSecret);
        } catch (Exception e) {
            log.error("生成签名失败！！！", e);
            throw new ServiceException("生成签名失败！！！");
        }
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", token);
        queryParams.put("client_id", clientId);
        queryParams.put("nonce", nonce);
        queryParams.put("timestamp", timestamp);
        queryParams.put("sign", sign);

        // 拼接URL（处理编码）
        String fullUrl = HttpUtil.urlWithForm(url, queryParams, null, true);

        Map<String, Object> bodyParams = new HashMap<>();
        if (StringUtils.isNotNull(pageNo) && StringUtils.isNotNull(pageSize)) {
            bodyParams.put("pageNo", pageNo);
            bodyParams.put("pageSize", pageSize);
        }
        String body = JSONUtil.toJsonStr(bodyParams);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        HttpResponse response = HttpRequest.post(fullUrl)
                .headerMap(headers, true)
                .body(body)
                .timeout(5000)
                .execute();
        //打印完整请求
        String command = generateCurlCommand(url, queryParams, headers, body);
        System.out.println(command);
        if (!response.isOk()) {
            throw new RuntimeException("获取店铺信息失败");
        }
        String responseBody = response.body();
        StoreInfoResponse storeInfoResponse = JSONObject.parseObject(responseBody, StoreInfoResponse.class);
        if (!storeInfoResponse.getCode().equals("0")) {
            throw new RuntimeException("获取店铺信息失败");
        }
        return storeInfoResponse.getData();
    }

    private String generateCurlCommand(String url, Map<String, Object> queryParams, Map<String, String> headers, String body) {
        StringBuilder curlCommand = new StringBuilder("curl -X POST ");

        // 1. 手动拼接 Query 参数（确保编码正确）
        StringBuilder urlWithQuery = new StringBuilder(url);
        if (!queryParams.isEmpty()) {
            urlWithQuery.append("?");
            List<String> paramPairs = new ArrayList<>();
            queryParams.forEach((key, value) ->
                    paramPairs.add(key + "=" + URLUtil.encode(value.toString()))
            );
            urlWithQuery.append(String.join("&", paramPairs));
        }

        // 2. 添加 Headers
        headers.forEach((key, value) ->
                curlCommand.append("-H \"").append(key).append(": ").append(value).append("\" ")
        );

        // 3. 添加 Body（JSON格式）
        curlCommand.append("-d '").append(body).append("' ");

        // 4. 添加完整 URL（包含 Query）
        curlCommand.append("\"").append(urlWithQuery).append("\"");

        return curlCommand.toString();
    }

    /**
     * 生成签名主方法
     *
     * @return
     */
    public String generateSign(String url, String method,
                               String token, String clientId,
                               String timestamp, String nonce,
                               String clientSecret) throws Exception {
        Map<String, Object> params = new HashMap<>();
        // 接口请求路径
        params.put("url", url);
        params.put("method", method);
        // 通过接口获取的token信息
        params.put("access_token", token);
        // 开发者id
        params.put("client_id", clientId);
        //请求时间戳 (ms)
        params.put("timestamp", timestamp);
        // 请求随机整数
        params.put("nonce", nonce);
        // 参数排序
        String data = params.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).sorted().collect(Collectors.joining("&"));
        System.out.println("data = " + data);
        // HmacSHA256签名, 【密钥】(需要填写跟clientId配对的密钥)
        return hmacsha256(clientSecret, data);
    }

    /**
     * HmacSHA256签名
     *
     * @param key  密钥 (需要填写跟clientId配对的密钥)
     * @param data 被签名字符串
     */
    public String hmacsha256(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(secret_key);
        return new String(Hex.encodeHex(hmac.doFinal(data.getBytes(StandardCharsets.UTF_8))));
    }
}
