package org.edrik.tool.http;

import com.alibaba.fastjson.JSON;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mohuangNPC
 * @version 1.0
 * @date 2022/5/16 9:56
 */
public class HttpTool {
    private static CloseableHttpClient aDefault;
    private static HttpTool httpInstance;
    private HttpTool(){

    }

    /**
     * 初始化HttpTool实例
     * @return
     */
    public static HttpTool InitHttpTool(){
        if(httpInstance != null){
            return httpInstance;
        }
        httpInstance = new HttpTool();
        aDefault = HttpClients.createDefault();
        return httpInstance;
    }

    /**
     * GET请求
     * @param httpConfig
     * @return
     * @throws ParseException
     */
    public String doGet(HttpConfig httpConfig) throws ParseException {
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(httpConfig.getGetUrl());
            if(httpConfig != null){
                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Timeout.ofMilliseconds(httpConfig.getConnectTimeout()))// 连接主机服务超时时间
                        .setConnectionRequestTimeout(Timeout.ofMilliseconds(httpConfig.getConnectionRequestTimeout()))
                        .build();
                httpGet.setConfig(requestConfig);
                if(httpConfig.getHeader() != null){
                    for (String key : httpConfig.getHeader().keySet()) {
                        httpGet.addHeader(key,httpConfig.getHeader().get(key));
                    }
                }
                if(httpConfig.getContentType() != null){
                    httpGet.setHeader("Content-Type",httpConfig.getContentType().getType());
                }
            }
            response = aDefault.execute(httpGet);
            int statusCode = response.getCode();
            if (statusCode != HttpStatus.SC_OK) {
                return String.valueOf(statusCode);
            }else{
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * POST请求
     * @param httpConfig
     * @return
     */
    public String doPost(HttpConfig httpConfig){
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(httpConfig.getUrl());
            if(httpConfig != null){
                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Timeout.ofMilliseconds(httpConfig.getConnectTimeout()))// 连接主机服务超时时间
                        .setConnectionRequestTimeout(Timeout.ofMilliseconds(httpConfig.getConnectionRequestTimeout()))
                        .build();
                httpPost.setConfig(requestConfig);
                if(httpConfig.getHeader() != null){
                    for (String key : httpConfig.getHeader().keySet()) {
                        httpPost.addHeader(key,httpConfig.getHeader().get(key));
                    }
                }
                if(httpConfig.getContentType() != null){
                    httpPost.setHeader("Content-Type",httpConfig.getContentType().getType());
                    if(httpConfig.getParam() != null){
                        if(ContentType.XWwwFormUrlencoded == httpConfig.getContentType()){
                            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                            for (String key : httpConfig.getParam().keySet()) {
                                nvps.add(new BasicNameValuePair(key, httpConfig.getParam().get(key).toString()));
                            }
                            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8")));
                        }else if(ContentType.Json == httpConfig.getContentType()){
                            String jsonString = JSON.toJSONString(httpConfig.getParam());
                            StringEntity entity = new StringEntity(jsonString, Charset.forName("UTF-8"));
                            httpPost.setEntity(entity);
                        }
                    }
                }else{
                    if(httpConfig.getParam() != null){
                        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                        for (String key : httpConfig.getParam().keySet()) {
                            nvps.add(new BasicNameValuePair(key, httpConfig.getParam().get(key).toString()));
                        }
                        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8")));
                    }
                }
            }
            response = aDefault.execute(httpPost);
            int statusCode = response.getCode();
            if (statusCode != HttpStatus.SC_OK) {
                return String.valueOf(statusCode);
            }else{
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
