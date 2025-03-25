package com.lz.test;

import cn.hutool.core.date.DateUtil;
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
    private static final String ACCESS_TOKEN = "52d72d85-0b08-44c2-8397-eec3e329abef";

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
        System.out.println("curl -X GET \"" + url + "\"");
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
        } else {
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
    /*
    [root@VM-0-8-opencloudos ~]# curl -X POST -H "Content-Type: application/json" -d '{"amazonOrderId":"114-9000120-2631449","shopId":"284413"}' "https://openapi.sellfox.com/api/order/detailByOrderId.json?access_token=131db684-96a3-4b97-8d7e-85ca281b72c7&sign=6aa53c7542abb24dc44f636ecc04eb6fe64f1f2730af00ecec4ffdc1c55c41c4&nonce=90904&client_id=367971&timestamp=1742566280608"
        {
            "code":0,
            "msg":"",
            "data":
                {"shopId":"284413","amaoviderName":null,"agentCat":null,"trackNo":null,"etplaceId":"US","orderType":"StandardOrder","buye:"*****","county":"*****","district":"*****","sta[{"orderItemId":"123880799672761","commoditySku":Modes, 40-Minute Runtime & Detachable Battery, LitityShipped":"0","quantityUnfulfillable":null,"rerl":"https://m.media-amazon.com/images/I/418dJOF+":"-2.82","headTripShare":"false","fbmShipCost":"9.99","itemTaxAmount":"0.0","promotionIds":"","fbnt":"0.0","giftWrapTaxAmount":"0.0","shippingTaxAnt":"0.0"}],"productAmount":"69.99","productAmoununt":"0.0","purchaseCost":"-31.9","headTripCost":"isReturnOrder":"0","refundDate":null,"purchaseDassOrder":"0","replacedOrderId":null,"fbmCostOrigimentComment":null,"receivedDate":null,"shippingChle":"49.94","withheldTaxAmount":"0.0","giftWrapAmt":"0.0","promotionFlag":"false","taxNumber":nullsExpire":"false","orderFlag":"0","numberOfItemsUn
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
        bodyParams.put("shopId", "284413");
        bodyParams.put("amazonOrderId", "114-9000120-2631449");
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

    /*
    查看商品详情 POST /api/commodity/getCommodityDetail.json
        商品列表
        请求参数
        Query 参数
            access_token string 必需 通过获取token接口获得的token，详见 获取 Access Token 默认值: {{access_token}}
            client_id string 必需 client_id, 获取方式详见 申请API权限 默认值: {{client_id}}
            timestamp string 必需 13位毫秒时间戳，与当前时间差异不超过正负15分钟，示例：1668153260508 默认值: 121212
            nonce string 必需 随机整数值，保证每个请求唯一，示例：11251 默认值: 121212
            sign string 必需 请求签名，详见 生成sign（签名） 默认值: 121212121 Header 参数 Content-Type string
        固定再header位置加入Content-Type:application/json
        默认值: application/json
        Body 参数 application/json
        id string 商品Id
     */
    @Test
    public void getCommodityDetail() throws Exception {
        String url = "https://openapi.sellfox.com/api/commodity/getCommodityDetail.json";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(new Random().nextInt(100000));
        String sign = generateSign("/api/commodity/getCommodityDetail.json", "post", ACCESS_TOKEN, CLIENT_ID, timestamp, nonce, CLIENT_SECRET);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", ACCESS_TOKEN);
        queryParams.put("client_id", CLIENT_ID);
        queryParams.put("nonce", nonce);
        queryParams.put("timestamp", timestamp);
        queryParams.put("sign", sign);

        //拼接url
        String fullUrl = HttpUtil.urlWithForm(url, queryParams, null, true);
        // 构建请求体（JSON格式）
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("id", "123880799672761");
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
            }else{
                System.err.println("请求失败: HTTP " + response.getStatus());
                System.err.println("错误信息: " + response.body());
            }
        }catch (Exception e) {
            System.err.println("请求异常: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /*
    获取评价明细列表 POST /api/review/pageDetailList.json
    请求参数
        Query 参数
        access_token string 必需 通过获取token接口获得的token，详见 获取 Access Token 默认值:{{access_token}}
        client_id string 必需 client_id, 获取方式详见 申请API权限 默认值:{{client_id}}
        timestamp string 必需 13位毫秒时间戳，与当前时间差异不超过正负15分钟，示例：1668153260508 默认值:121212
        nonce string 必需 随机整数值，保证每个请求唯一，示例：11251 默认值:121212
        sign string 必需 请求签名，详见 生成sign（签名）默认值:121212121
        Header 参数 Content-Type string 可选
        固定再header位置加入Content-Type:application/json
        默认值:application/json
        Body 参数 application/json
        marketplaceIdList array[string] 站点ID 可选
        shopIdList array[string] 店铺id 可选
        starList array[string] 可选 星级，传整数值(1至5)
        imageAndVideo string 可选 是否为图片或视频评论(0:全部评论[默认]，1:图片或视频评论)
        dateType string 可选 筛选的日期类型(reviewDate:评价时间[默认]，updateTime:更新时间)
        startDate string 开始时间 必需 示例值: 2023-01-01
        endDate string 必需 结束时间，时间范围不能超过1年 示例值: 2023-02-01
        matchStateList array[string] 可选 匹配状态(1:自动匹配，2:手动匹配，3:未匹配)
        searchType enum<string> 可选 搜索类型(asin:按ASIN[默认]，parentAsin:父ASIN，remark:备注搜索，buyer:买家信息，amazonOrderId:订单号，reviewID:按reviewID) 枚举值: asin parentAsin remark buyer amazonOrderId reviewID
        searchValue string 可选 搜索的内容,多个用%±%拼接
        pageNo string  第几页 可选
        pageSize string 每页大小 可选
        reviewerTypeList array[string] 可选 买家标识，传整数值(0:直评，1:VP，2:VN)
        statusList array[string] 可选 处理状态(0:待处理，1:处理中，2:已处理)
        reviewStatusList array[string] 可选 评论状态(0:无变动，1:已更新，2:已删除) 示例
     */
    @Test
    public void getReviewDetailList() throws Exception {
        String url = "https://openapi.sellfox.com/api/review/pageDetailList.json";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(new Random().nextInt(100000));
        String sign = generateSign("/api/review/pageDetailList.json", "post", ACCESS_TOKEN, CLIENT_ID, timestamp, nonce, CLIENT_SECRET);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", ACCESS_TOKEN);
        queryParams.put("client_id", CLIENT_ID);
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
        bodyParams.put("searchValue", "112-4802775-1444225");
        bodyParams.put("pageNo", "1");
        bodyParams.put("pageSize", "20");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        try {
            HttpResponse response = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(bodyParams))
                    .timeout(5000)
                    .execute();
            //打印完整请求
            String command = generateCurlCommand(url, queryParams, headers, JSONUtil.toJsonStr(bodyParams));
            System.out.println(command);
        }catch (Exception e) {
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
