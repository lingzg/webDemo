package com.elvis.webDemo.core.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class HttpUtils {

	private final static Logger log = Logger.getLogger(HttpUtils.class);

    public static String doGet(String url) {
        log.info("URL："+url);
        // Http客户端
        CloseableHttpClient httpClient = null;
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClientBuilder.create().build();
            // 创建Get请求
            HttpGet httpGet = new HttpGet(url);
            // 设置ContentType
            httpGet.setHeader("Content-Type", "application/json;charset=utf8");
            // 由客户端执行(发送)请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            log.info("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                String content = EntityUtils.toString(responseEntity, "UTF-8");
                log.info("响应内容为:" + content);
                return content;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String doPost(String url, Object params) {
        log.info("URL："+url);
        log.info("参数："+params);
        // 获得Http客户端
        CloseableHttpClient httpClient = null;
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClientBuilder.create().build();
            // 创建Post请求
            HttpPost httpPost = new HttpPost(url);
            if (params != null) {
                String jsonString = JSON.toJSONString(params);
                StringEntity entity = new StringEntity(jsonString, "UTF-8");
                // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
                httpPost.setEntity(entity);
            }
            // 设置ContentType
            httpPost.setHeader("Content-Type", "application/json;charset=utf8");
            
            // 由客户端执行(发送)请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            log.info("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                String content = EntityUtils.toString(responseEntity, "UTF-8");
                log.info("响应内容为:" + content);
                return content;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
