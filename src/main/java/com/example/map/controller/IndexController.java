package com.example.map.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: jzhang
 * @Date: 2019/10/23 09:10
 * @Description: 主页
 */
@Controller
@RequestMapping("/home")
public class IndexController {
    @CrossOrigin
    @GetMapping("/map")
    public String toIndex(){

        System.out.println(12345);
        return "index.html";
    }

    @CrossOrigin
    @GetMapping("/mapTest")
    public String toIndexTest(){
        return "mapTest.html";
    }

    @CrossOrigin
    @GetMapping("/map1")
    public String cycNavigation(){

        return "map.html";
    }
    @CrossOrigin
    @GetMapping("/map2")
    public String cycNavigation2(){

        return "map2.html";
    }
    @CrossOrigin
    @GetMapping("/map3")
    public String cycNavigation3(){

        return "map3.html";
    }

    // 高德地图API的请求URL
    private static final String URL = "https://restapi.amap.com/v3/geocode/geo";

    // 高德地图API的密钥
    private static final String KEY = "d119f705d878066f789f9a9ec7ad29ca";

    @GetMapping("/geocode")
    public ResponseEntity<String> geocode(/*@RequestParam("address") String address*/) {
        try {
            String address = "四川省成都市双流区中德A区";
            // 对地址进行URL编码
            String encodedAddress = URLEncoder.encode(address, "UTF-8");

            // 构建请求URL
            String requestUrl = URL + "?address=" + encodedAddress + "&key=" + KEY;

            // 发送GET请求
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);

            System.out.println(response);
            // 返回响应结果
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
