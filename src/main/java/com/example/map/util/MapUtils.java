package com.example.map.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {

    /**日志对象*/
    private static final Logger logger = LoggerFactory.getLogger(MapUtils.class);

    /**
     * 驾车路线规划
     */
    public final static String DRIVING_ROUTE_PLANNING ="https://restapi.amap.com/v5/direction/driving?parameters";

    /**
     * 步行路线规划
     */
    public final static String WALKING_ROUTE_PLANNING ="https://restapi.amap.com/v3/direction/walking?parameters";

    /**
     * 骑行路线规划
     */
    public final static String CYCLING_ROUTE_PLANNING ="https://restapi.amap.com/v5/direction/bicycling?parameters";

    /**
     * 公交路线规划
     */
    public final static String BUS_ROUTE_PLANNING ="https://restapi.amap.com/v5/direction/transit/integrated?parameters";

    /**
     * 高德key
     */
    public final static String KEY ="d119f705d878066f789f9a9ec7ad29ca";

    public static String todo(HttpResponse response) throws IOException {
        // 解析响应结果
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            responseBuilder.append(line);
        }
        bufferedReader.close();

        System.out.println(responseBuilder.toString());
        return responseBuilder.toString();
    }

    public static String sendGet(String url, Map<String, String> params){

        String res = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建URI对象，并且设置请求参数
        try {
            URI uri = getBuilder(url, params);
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse execute = httpclient.execute(httpGet);

            res = todo(execute);
            System.out.println(res);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }


    /**
     * 发送get请求
     * @param url
     * @return
     */
    /*public static Map<String, String> sendGet(String url, Map<String, String> params){
        Map<String, String> map = new HashMap<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建URI对象，并且设置请求参数
        try {
            URI uri = getBuilder(url, params);
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse execute = httpclient.execute(httpGet);

            String todo = todo(execute);
            System.out.println(todo);


            *//*CloseableHttpResponse response = httpclient.execute(httpGet);

            System.out.println("-----------------");
            System.out.println(response);
            System.out.println("-----------------");
            // 判断返回状态是否为200
            map = getRoute(response, url);
            String route = map.get("route");
            System.out.println(route);
            httpclient.close();*//*
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }*/



    /**
     * 根据不同的路径规划获取距离
     * @param response
     * @return
     */
    private static Map<String, String> getRoute(CloseableHttpResponse response, String url) throws Exception{
        Map<String, String> map = new HashMap<>();
        String distance = "";
        String duration = "";
        // 判断返回状态是否为200
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.info("调用高德地图接口返回的结果为:{}",content);
            JSONObject jsonObject = (JSONObject)JSONObject.parse(content);
            JSONObject route = (JSONObject)jsonObject.get("route");
            if(BUS_ROUTE_PLANNING.equals(url)){
                JSONArray transits = (JSONArray)route.get("transits");
                JSONObject transit = (JSONObject)transits.get(0);
                distance = (String)transit.get("distance"); // 距离
                JSONObject cost = (JSONObject)transit.get("cost");
                duration = (String)cost.get("duration");    // 时间
            } else if(DRIVING_ROUTE_PLANNING.equals(url)) {
                JSONArray paths = (JSONArray)route.get("paths");
                JSONObject path = (JSONObject)paths.get(0);
                distance = (String)path.get("distance"); 	// 距离
                JSONObject cost = (JSONObject)path.get("cost");
                duration = (String)cost.get("duration");    // 时间
            } else {
                JSONArray paths = (JSONArray)route.get("paths");
                JSONObject path = (JSONObject)paths.get(0);
                distance = (String)path.get("distance"); // 距离
                duration = (String)path.get("duration"); // 时间
            }

            map.put("distance", distance);
            map.put("duration", duration);

            logger.info("返回的结果为:{}",JSONObject.toJSONString(map));
        }
        return map;
    }

    /**
     * 封装URI
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    private static URI getBuilder(String url, Map<String, String> params) throws Exception{
        // 驾车路线规划
        String origin = params.get("origin");
        String destination = params.get("destination");
        String city1 = params.get("city1");
        String city2 = params.get("city2");

        // 步行路线规划
        String isindoor = params.get("isindoor");

        URIBuilder uriBuilder = new URIBuilder(url);
        // 公共参数
        uriBuilder.setParameter("key", KEY);
        uriBuilder.setParameter("origin", origin);
        uriBuilder.setParameter("destination", destination);
        uriBuilder.setParameter("show_fields", "cost");

        // 驾车路线规划
        if(StringUtils.isNotBlank(city1) && StringUtils.isNotBlank(city2)){
            uriBuilder.setParameter("city1", city1);
            uriBuilder.setParameter("city2", city2);
        }
        // 步行路线规划
        if(StringUtils.isNotBlank(isindoor)){
            uriBuilder.setParameter("isindoor", isindoor);
        }
        URI uri = uriBuilder.build();
        return uri;
    }
}

