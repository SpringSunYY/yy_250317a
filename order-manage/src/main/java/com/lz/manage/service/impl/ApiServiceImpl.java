package com.lz.manage.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.StringUtils;
import com.lz.manage.model.api.*;
import com.lz.manage.model.dto.storeInfo.StoreInfoResult;
import com.lz.manage.service.IApiService;
import com.lz.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Project: order
 * Package: com.lz.manage.service.impl
 * Author: YY
 * CreateTime: 2025-03-21  23:09
 * Description: ApiServiceImpl
 * Version: 1.0
 */
@Service
@Slf4j
public class ApiServiceImpl implements IApiService {
    @Resource
    private RedisCache redisCache;

    @Resource
    private ISysConfigService configService;

    @Override
    public String getToken() {
        String clientId = configService.selectConfigByKey("clientId");
        String clientSecret = configService.selectConfigByKey("clientSecret");
        String grantType = configService.selectConfigByKey("grantType");
        String apiUrl = configService.selectConfigByKey("getTokenApi");
        if (StringUtils.isEmpty(clientId)
                || StringUtils.isEmpty(clientSecret)
                || StringUtils.isEmpty(grantType)
                || StringUtils.isEmpty(apiUrl)) {
            throw new RuntimeException("请先配置clientId,clientSecret,grantType,以及获取token接口");
        }
        String url = StringUtils.format(apiUrl, clientId, clientSecret, grantType);
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
        redisCache.setCacheObject("token", data.getAccess_token(), 60 * 12, TimeUnit.SECONDS);
        return data.getAccess_token();
    }

    @Override
    public List<StoreInfoResult> getStoreInfo() {
        String clientId = configService.selectConfigByKey("clientId");
        String clientSecret = configService.selectConfigByKey("clientSecret");
        String apiUrl = configService.selectConfigByKey("getStoreApi");
        String pageSize = configService.selectConfigByKey("pageSize");
        if (StringUtils.isEmpty(clientId)
                || StringUtils.isEmpty(clientSecret)
                || StringUtils.isEmpty(apiUrl)) {
            throw new RuntimeException("请先配置clientId,clientSecret以及获取店铺接口");
        }
        //获取token
        String token = redisCache.getCacheObject("token");
        if (StringUtils.isEmpty(token)) {
            token = getToken();
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(new Random().nextInt(100000));
        String sign = null;
        try {
            sign = generateSign("/api/shop/pageList.json", "post", token, clientId, timestamp, nonce, clientSecret);
        } catch (Exception e) {
//            log.error("生成签名失败！！！", e);
            throw new ServiceException("生成签名失败！！！");
        }
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", token);
        queryParams.put("client_id", clientId);
        queryParams.put("nonce", nonce);
        queryParams.put("timestamp", timestamp);
        queryParams.put("sign", sign);

        // 拼接URL（处理编码）
        String fullUrl = HttpUtil.urlWithForm(apiUrl, queryParams, null, true);

        Map<String, Object> bodyParams = new HashMap<>();

        bodyParams.put("pageNo", "1");
        bodyParams.put("pageSize", pageSize);

        String body = JSONUtil.toJsonStr(bodyParams);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        HttpResponse response = HttpRequest.post(fullUrl)
                .headerMap(headers, true)
                .body(body)
                .timeout(10000)
                .execute();
        //打印完整请求
//        String command = generateCurlCommand(apiUrl, queryParams, headers, body);
//        System.out.println(command);
        String responseBody = response.body();
        System.err.println(responseBody);
        StoreInfoResponse storeInfoResponse = JSONObject.parseObject(responseBody, StoreInfoResponse.class);
        System.err.println(storeInfoResponse);
        if (storeInfoResponse.getCode().equals("40001") || storeInfoResponse.getMsg().equals("access_token 失效")) {
            getToken();
            throw new ServiceException("access_token 失效，正在重新获取token,请重新操作", 40001);
        }
        if (!response.isOk()) {
//            log.error("获取店铺信息失败,responseBody:{}", responseBody);
            throw new RuntimeException("获取店铺信息失败");

        }
        if (!storeInfoResponse.getCode().equals("0")) {
            throw new RuntimeException("获取店铺信息失败");
        }
        return storeInfoResponse.getData().getRows();
    }

    @Override
    public OrderInfoResponse.Data getOrderInfo(String shopId, String amazonOrderId, String sellerOrderId) {
        if (StringUtils.isEmpty(shopId) || StringUtils.isEmpty(amazonOrderId)) {
            throw new RuntimeException("店铺唯一标识,亚马逊订单ID不能为空");
        }
        String clientId = configService.selectConfigByKey("clientId");
        String clientSecret = configService.selectConfigByKey("clientSecret");
        String apiUrl = configService.selectConfigByKey("getOrderInfoApi");
        if (StringUtils.isEmpty(clientId)
                || StringUtils.isEmpty(clientSecret)
                || StringUtils.isEmpty(apiUrl)) {
            throw new RuntimeException("请先配置clientId,clientSecret以及获取订单接口");
        }
        //获取token
        String token = redisCache.getCacheObject("token");
        if (StringUtils.isEmpty(token)) {
            token = getToken();
        }

        String nonce = String.valueOf(new Random().nextInt(100000));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = null;
        // 生成签名
        try {
            sign = generateSign("/api/order/detailByOrderId.json", "post", token, clientId, timestamp, nonce, clientSecret);
        } catch (Exception e) {
//            log.error("生成签名失败！！！", e);
            throw new ServiceException("生成签名失败");
        }
        // 构建Query参数（逐个put）
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", token);
        queryParams.put("client_id", clientId);
        queryParams.put("nonce", nonce);
        queryParams.put("timestamp", timestamp);
        queryParams.put("sign", sign);

        // 拼接URL（处理编码）
        String fullUrl = HttpUtil.urlWithForm(apiUrl, queryParams, null, true);

        // 构建请求体（JSON格式）
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("shopId", shopId);
        if (StringUtils.isNotEmpty(sellerOrderId)) {
            bodyParams.put("sellerOrderId", sellerOrderId);
        }
        if (StringUtils.isNotEmpty(amazonOrderId)) {
            bodyParams.put("amazonOrderId", amazonOrderId);
        }

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        HttpResponse response = HttpRequest.post(fullUrl)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(bodyParams))
                .timeout(10000)
                .execute();
        //打印完整请求
//        String command = generateCurlCommand(apiUrl, queryParams, headers, JSONUtil.toJsonStr(bodyParams));
//        System.out.println("207getOrderInfo response = " + response);
        String responseBody = response.body();
//        System.err.println("201getOrderInfo responseBody" + responseBody);
        OrderInfoResponse orderInfoResponse = JSONObject.parseObject(responseBody, OrderInfoResponse.class);
//        System.out.println("211getOrderInfo orderInfoResponse = " + orderInfoResponse);
//        System.err.println("212getOrderInfo response = " + response);
//        System.out.println(command);

        if (orderInfoResponse.getCode().equals("40001") || orderInfoResponse.getMsg().equals("access_token 失效")) {
            getToken();
            throw new ServiceException("access_token 失效，正在重新获取token,请重新操作", 40001);
        }
        if (!orderInfoResponse.getCode().equals("0")) {
            throw new RuntimeException("获取订单信息失败");
        }
        if (!response.isOk()) {
            throw new RuntimeException("获取订单信息失败");
        }

        OrderInfoResponse.Data data = orderInfoResponse.getData();
        if (StringUtils.isNull(data)) {
            return new OrderInfoResponse.Data();
        }

        List<OrderInfoResponse.OrderItemVoList> orderItemVoList = data.getOrderItemVoList();
        if (StringUtils.isNotEmpty(orderItemVoList)) {
            OrderInfoResponse.OrderItemVoList orderItem = orderItemVoList.get(0);
            data.setAsin(orderItem.getAsin());
            data.setTitle(orderItem.getCommoditySku());
            data.setOrderItemId(orderItem.getCommoditySku());
            data.setGoodsLink(orderItem.getAsinUrl());
        }
//        System.out.println("238getOrderInfo orderItemVoList = " + orderItemVoList);
//        System.out.println("239getOrderInfo data = " + data);
        return data;

    }

    @Override
    public CommodityDetailResponse.Data getCommodityDetail(String id) {
        if (StringUtils.isEmpty(id)) {
            id = "";
        }
        String clientId = configService.selectConfigByKey("clientId");
        String clientSecret = configService.selectConfigByKey("clientSecret");
        String apiUrl = configService.selectConfigByKey("getCommodityDetailApi");
        if (StringUtils.isEmpty(clientId)
                || StringUtils.isEmpty(clientSecret)
                || StringUtils.isEmpty(apiUrl)) {
            throw new RuntimeException("请先配置clientId,clientSecret以及获取订单接口");
        }

        //获取token
        String token = redisCache.getCacheObject("token");
        if (StringUtils.isEmpty(token)) {
            token = getToken();
        }
        String nonce = String.valueOf(new Random().nextInt(100000));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = null;
        try {
            sign = generateSign("/api/commodity/getCommodityDetail.json", "post", token, clientId, timestamp, nonce, clientSecret);
        } catch (Exception e) {
//            log.error("生成签名失败！！！", e);
            throw new ServiceException("生成签名失败");
        }

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", token);
        queryParams.put("client_id", clientId);
        queryParams.put("nonce", nonce);
        queryParams.put("timestamp", timestamp);
        queryParams.put("sign", sign);

        String fullUrl = HttpUtil.urlWithForm(apiUrl, queryParams, null, true);
        // 构建请求体（JSON格式）
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("id", id);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        HttpResponse response = HttpRequest.post(fullUrl)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(bodyParams))
                .timeout(10000)
                .execute();

        //打印完整请求
//        String command = generateCurlCommand(apiUrl, queryParams, headers, JSONUtil.toJsonStr(bodyParams));
        String responseBody = response.body();
//        System.err.println("296getCommodityDetail responseBody" + responseBody);
        CommodityDetailResponse detailResponse = JSONObject.parseObject(responseBody, CommodityDetailResponse.class);
//        System.out.println("298getCommodityDetail detailResponse = " + detailResponse);
//        System.err.println("299getCommodityDetail response = " + response);
//        System.out.println(command);

        if (detailResponse.getCode().equals("40001") || detailResponse.getMsg().equals("access_token 失效")) {
            getToken();
            throw new ServiceException("access_token 失效，正在重新获取token,请重新操作", 40001);
        }
        if (!detailResponse.getCode().equals("0")) {
//            throw new RuntimeException("获取商品信息失败");
//            log.error("获取商品信息失败");
            return new CommodityDetailResponse.Data();
        }
        if (!response.isOk()) {
//            throw new RuntimeException("获取商品信息失败");
//            log.error("获取商品信息失败");
            return new CommodityDetailResponse.Data();
        }
        CommodityDetailResponse.Data data = detailResponse.getData();
        if (StringUtils.isNull(data)) {
            return new CommodityDetailResponse.Data();
        }
        return data;
    }

    @Override
    public ReviewResponse.Data getReviewDetailList(String amazonOrderId) {
        if (StringUtils.isEmpty(amazonOrderId)) {
            amazonOrderId = "";
        }
        String clientId = configService.selectConfigByKey("clientId");
        String clientSecret = configService.selectConfigByKey("clientSecret");
        String apiUrl = configService.selectConfigByKey("getReviewDetailListApi");
        String pageSize = configService.selectConfigByKey("pageSize");
        if (StringUtils.isEmpty(clientId)
                || StringUtils.isEmpty(clientSecret)
                || StringUtils.isEmpty(apiUrl)) {
            throw new RuntimeException("请先配置clientId,clientSecret以及获取评论接口");
        }
        //获取token
        String token = redisCache.getCacheObject("token");
        if (StringUtils.isEmpty(token)) {
            token = getToken();
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(new Random().nextInt(100000));
        String sign = null;
        try {
            sign = generateSign("/api/review/pageDetailList.json", "post", token, clientId, timestamp, nonce, clientSecret);
        } catch (Exception e) {
//            log.error("生成签名失败！！！", e);
        }
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", token);
        queryParams.put("client_id", clientId);
        queryParams.put("nonce", nonce);
        queryParams.put("timestamp", timestamp);
        queryParams.put("sign", sign);
        Map<String, Object> bodyParams = new HashMap<>();
        //开始时间，一年前，结束时间今天
        bodyParams.put("startDate", DateUtil.format(DateUtil.offsetDay(DateUtil.date(), -365), "yyyy-MM-dd"));
        bodyParams.put("endDate", DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        //店铺ID，订单号
//        bodyParams.put("shopIdList", Arrays.asList("284413"));
        bodyParams.put("searchType", "amazonOrderId");
        bodyParams.put("searchValue", amazonOrderId);
        bodyParams.put("pageNo", "1");
        bodyParams.put("pageSize", pageSize);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        //拼接url
        String fullUrl = HttpUtil.urlWithForm(apiUrl, queryParams, null, true);
        HttpResponse response = HttpRequest.post(fullUrl)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(bodyParams))
                .timeout(5000)
                .execute();
        //打印完整请求
//        String command = generateCurlCommand(apiUrl, queryParams, headers, JSONUtil.toJsonStr(bodyParams));
        String responseBody = response.body();
//        System.err.println("378getReviewDetailList responseBody" + responseBody);
        ReviewResponse reviewResponse = JSONObject.parseObject(responseBody, ReviewResponse.class);
//        System.out.println("380getReviewDetailList reviewResponse = " + reviewResponse);
//        System.err.println("381getReviewDetailList response = " + response);
//        System.out.println(command);

        if (reviewResponse.getCode().equals("40001") || reviewResponse.getMsg().equals("access_token 失效")) {
            getToken();
            throw new ServiceException("access_token 失效，正在重新获取token,请重新操作", 40001);
        }
        if (!reviewResponse.getCode().equals("0")) {
//            throw new RuntimeException("获取商品信息失败");
            log.error("获取商品评论信息失败");
            return new ReviewResponse.Data();
        }
        if (!response.isOk()) {
//            throw new RuntimeException("获取商品信息失败");
//            log.error("获取商品评论信息失败");
            return new ReviewResponse.Data();
        }
        ReviewResponse.Data data = reviewResponse.getData();
        if (StringUtils.isNull(data)) {
//            log.info("商品评论信息为空");
            return new ReviewResponse.Data();
        }
//        System.out.println("403getReviewDetailList data = " + data);
        return data;
    }

    @Override
    public OrderResponse.Data getOrderInfoList(String dateStart, String dateEnd, int pageNo) {
        String clientId = configService.selectConfigByKey("clientId");
        String clientSecret = configService.selectConfigByKey("clientSecret");
        String apiUrl = configService.selectConfigByKey("getOrderInfoListApi");
        if (StringUtils.isEmpty(clientId)
                || StringUtils.isEmpty(clientSecret)
                || StringUtils.isEmpty(apiUrl)) {
            throw new RuntimeException("请先配置clientId,clientSecret以及获取评论接口");
        }
        //获取token
        String token = redisCache.getCacheObject("token");
        if (StringUtils.isEmpty(token)) {
            token = getToken();
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(new Random().nextInt(100000));
        String sign = null;
        try {
            sign = generateSign("/api/order/pageList.json", "post", token, clientId, timestamp, nonce, clientSecret);
        } catch (Exception e) {
            log.error("生成签名失败！！！", e);
        }
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", token);
        queryParams.put("client_id", clientId);
        queryParams.put("nonce", nonce);
        queryParams.put("timestamp", timestamp);
        queryParams.put("sign", sign);
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("dateType", "purchase");
        bodyParams.put("dateStart", dateStart);
        bodyParams.put("dateEnd", dateEnd);
        bodyParams.put("pageNo", pageNo);
        bodyParams.put("pageSize", "200");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try {
            String fullUrl = HttpUtil.urlWithForm(apiUrl, queryParams, null, true);
            HttpResponse response = HttpRequest.post(fullUrl)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(bodyParams))
                    .timeout(5000)
                    .execute();
            //打印完整请求
            String command = generateCurlCommand(apiUrl, queryParams, headers, JSONUtil.toJsonStr(bodyParams));
            String responseBody = response.body();
//            System.err.println("getOrderInfoList responseBody" + responseBody);
            OrderResponse orderResponse = JSONObject.parseObject(responseBody, OrderResponse.class);
//            System.out.println("298getCommodityDetail orderResponse = " + orderResponse);
//            System.err.println("getOrderInfoList response = " + response);
//            System.out.println(command);

            if (orderResponse.getCode().equals("40001") || orderResponse.getMsg().equals("access_token 失效")) {
                getToken();
                throw new ServiceException("access_token 失效，正在重新获取token,请重新操作", 40001);
            }
            if (!orderResponse.getCode().equals("0")) {
//            throw new RuntimeException("获取商品信息失败");
                log.error("获取商品信息失败");
                return new OrderResponse.Data();

            }
            if (!response.isOk()) {
//            throw new RuntimeException("获取商品信息失败");
                log.error("获取商品信息失败");
                return new OrderResponse.Data();
            }
            return orderResponse.getData();
        } catch (Exception e) {
            System.err.println("请求异常: " + e.getMessage());
//            e.printStackTrace();
            throw new RuntimeException("请求异常: " + e.getMessage());
        }
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
     * @return String
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
