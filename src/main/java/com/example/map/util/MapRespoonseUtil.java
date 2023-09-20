package com.example.map.util;

import org.apache.http.HttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class MapRespoonseUtil {

    public String todo(HttpResponse response) throws IOException {
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
}
