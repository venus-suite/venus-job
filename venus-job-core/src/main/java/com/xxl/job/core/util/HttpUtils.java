package com.xxl.job.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public class HttpUtils {

    public static String post(String url, String data, int timeout) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().
                    setConnectionRequestTimeout(timeout)
                    .setSocketTimeout(2000).
                    setConnectTimeout(2000).build();
            httpPost.setConfig(requestConfig);
            StringEntity se = new StringEntity(data, "UTF-8");
            se.setContentType("application/json");
            httpPost.setEntity(se);

            response = httpClient.execute(httpPost);
            HttpEntity entity1 = response.getEntity();
            String resStr = null;
            if (entity1 != null) {
                resStr = EntityUtils.toString(entity1, "UTF-8");
            }
            return resStr;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                response = null;
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                    httpClient = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
