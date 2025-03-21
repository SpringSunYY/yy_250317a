package com.lz.test;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lz.common.utils.StringUtils;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Project: order
 * Package: com.lz.test
 * Author: YY
 * CreateTime: 2025-03-19  15:58
 * Description: CommonTest
 * Version: 1.0
 */
@SpringBootTest
public class CommonTest {

    private static final String CLIENT_ID = "367971";
    private static final String CLIENT_SECRET = "df2bac03-1006-4974-89f1-8163162d4910";
    private static final String ACCESS_TOKEN = "131db684-96a3-4b97-8d7e-85ca281b72c7";

    @Test
    public void test() {
        System.out.println("test");
    }

    @Test
    public void testGetToken() {
        /*
            请求参数：
                名称	位置	类型	必选	说明
                client_id	query	string	否	应用的APPID ,如何获取请查看申请API权限
                client_secret	query	string	否	client_secret如何获取请查看申请API权限
                grant_type	query	string	否	固定为client_credentials
                请求示例：
                https://openapi.sellfox.com/api/oauth/v2/token.json?client_id=aaaa&client_secret=bbb&grant_type=cccc
                "clientId":367971
                "clientSecret":df2bac03-1006-4974-89f1-8163162d4910
                "token":1744c230-dbde-4fdf-99a5-fa0f2deb5de9
         */
        //使用hutool 发送请求
        String clientId = "367971";
        String clientSecret = "df2bac03-1006-4974-89f1-8163162d4910";
        String grantType = "client_credentials";
        String url = StringUtils.format("https://openapi.sellfox.com/api/oauth/v2/token.json?client_id={}&client_secret={}&grant_type={}", clientId, clientSecret, grantType);
        System.out.println("curl -X GET \"" + url+"\"");
        //发送请求
        HttpRequest get = HttpUtil.createGet(url);
        String body = get.execute().body();
        System.out.println(body);
    }

    /*
    获取店铺列表
        POST /api/shop/pageList.json
        系统
        请求参数
        Query 参数
        access_token string 必需 通过获取token接口获得的token，详见 获取 Access Token 默认值: {{access_token}}
        client_id string 必需 client_id, 获取方式详见 申请API权限 默认值: {{client_id}}
        timestamp string 必需 13位毫秒时间戳，与当前时间差异不超过正负15分钟，示例：1668153
        nonce string 必需 随机整数值，保证每个请求唯一，示例：11251 默认值: 121212
        sign string 必需
        请求签名，详见 生成sign（签名） 默认值: 121212121
        Header 参数
        Content-Type string 可选 固定再header位置加入Content-Type:application/json 默认值: application/json
        Body 参数 application/json
        pageNo string 第几页 可选
        pageSize string 每页大小 可选 示例
     */
    @Test
    public void testGetShopList() throws Exception {
        //url
        String url = "https://openapi.sellfox.com/api/shop/pageList.json";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(new Random().nextInt(100000));
        String sign = generateSign("/api/shop/pageList.json", "post", ACCESS_TOKEN, CLIENT_ID, timestamp, nonce, CLIENT_SECRET);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", ACCESS_TOKEN);
        queryParams.put("client_id", CLIENT_ID);
        queryParams.put("nonce", nonce);
        queryParams.put("timestamp", timestamp);
        queryParams.put("sign", sign);
        // 拼接URL（处理编码）
        String fullUrl = HttpUtil.urlWithForm(url, queryParams, null, true);

        Map<String, Object> bodyParams = new HashMap<>();
//        bodyParams.put("shopId", "");
//        bodyParams.put("pageNo", "1");
//        bodyParams.put("pageSize", "10");
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
        if (response.isOk()) {
            String responseBody = response.body();
            System.out.println(responseBody);
        }else {
            System.out.println("请求失败，状态码：" + response.getStatus());
            System.out.println("请求失败，响应体：" + response.body());
        }

    }

    /*
    # 订单详情
     /api/order/detailByOrderId.json**
     请求参数
        Query 参数
        access_token string 必需
        通过获取token接口获得的token，详见 [获取 Access Token](https://sellfoxapi.apifox.cn/doc-1589130)
        默认值: {{access_token}} client_id string  必需
        client_id, 获取方式详见 [申请API权限](https://sellfoxapi.apifox.cn/1748360) 默认值: {{client_id}}
        timestamp string  必需 13位毫秒时间戳，与当前时间差异不超过正负15分钟，示例：1668153260508 默认值: 121212
        nonce string 必需 随机整数值，保证每个请求唯一，示例：11251 默认值: 121212
        sign string  必需 请求签名，详见 [生成sign（签名）](https://sellfoxapi.apifox.cn/doc-1749562) 默认值: 121212121
        Header 参数
        Content-Type string  可选 固定再header位置加入Content-Type:application/json 默认值: application/json
        Body 参数application/json
        shopId string  店铺ID 必需
        amazonOrderId string 可选 亚马逊订单ID，amazonOrderId，sellerOrderId至少一个不能为空，二者都填则按amazonOrderId搜索
        sellerOrderId string 可选 卖家订单ID，amazonOrderId，sellerOrderId至少一个不能为空，二者都填则按amazonOrderId搜索
     */
    @Test
    public void testGetOrderDetail() throws Exception {
        // 基础参数
        String url = "https://openapi.sellfox.com/api/order/detailByOrderId.json";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(new Random().nextInt(100000));

        // 生成签名
        String sign = generateSign("/api/order/detailByOrderId.json", "post", ACCESS_TOKEN, CLIENT_ID, timestamp, nonce, CLIENT_SECRET);

        // 构建Query参数（逐个put）
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", ACCESS_TOKEN);
        queryParams.put("client_id", CLIENT_ID);
        queryParams.put("nonce", nonce);
        queryParams.put("timestamp", timestamp);
        queryParams.put("sign", sign);

        // 拼接URL（处理编码）
        String fullUrl = HttpUtil.urlWithForm(url, queryParams, null, true);

        // 构建请求体（JSON格式）
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("shopId", "123");
        bodyParams.put("amazonOrderId", "111-3433490-9681013");
        // bodyParams.put("sellerOrderId", "备用订单ID"); // 与amazonOrderId二选一

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        try {
            HttpResponse response = HttpRequest.post(fullUrl)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(bodyParams))
                    .timeout(5000)
                    .execute();
            //打印完整请求
            String command = generateCurlCommand(url, queryParams, headers, JSONUtil.toJsonStr(bodyParams));
            System.out.println(command);
            if (response.isOk()) {
                String responseBody = response.body();
                System.out.println("响应成功: " + responseBody);
                // 这里可以添加JSON解析逻辑
            } else {
                System.err.println("请求失败: HTTP " + response.getStatus());
                System.err.println("错误信息: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("请求异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String generateCurlCommand(String url, Map<String, Object> queryParams, Map<String, String> headers, String body) {
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
    public static String generateSign(String url, String method,
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
    public static String hmacsha256(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(secret_key);
        return new String(Hex.encodeHex(hmac.doFinal(data.getBytes(StandardCharsets.UTF_8))));
    }

}
