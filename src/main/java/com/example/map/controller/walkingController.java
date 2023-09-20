package com.example.map.controller;

import com.example.map.util.MapRespoonseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
public class walkingController {

    @Autowired
    private MapRespoonseUtil mapRespoonseUtil;

    @GetMapping("/navigation")
    public String getNavigationData(@RequestParam("origin") String origin,
                                    @RequestParam("destination") String destination) throws IOException {
        // 构建步行导航API请求URL
        String apiKey = "d119f705d878066f789f9a9ec7ad29ca";
        String apiUrl = "https://restapi.amap.com/v3/direction/walking?"
                + "origin=" + URLEncoder.encode(origin, "UTF-8")
                + "&destination=" + URLEncoder.encode(destination, "UTF-8")
                + "&key=" + apiKey;

        // 发起HTTP GET请求并获取响应
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);
        HttpResponse response = httpClient.execute(httpGet);

        // 解析响应结果
        String res = mapRespoonseUtil.todo(response);

        return res;
    }
}
