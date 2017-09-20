package com.example.chengyonghui.mp3demo.download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chengyonghui on 2017/9/20.
 */
public class HttpDownloader {
    private URL url = null;

    /*下载纯文本文件
    1、创建一个URL对象
    2、通过URL对象，创建一个HttpURLConnection对象
    3、得到InputStream
    4、得到InputStream当中读取数据
     */
    public String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    /*
    封装的方法得到InputStream
    根据URL得到输入流
     */
    public InputStream getInputStreamFromUrl(String urlStr) throws MalformedParameterizedTypeException, IOException {
        url = new URL(urlStr);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        return inputStream;
    }
}
