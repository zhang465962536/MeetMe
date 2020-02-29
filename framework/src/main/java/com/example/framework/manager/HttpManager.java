package com.example.framework.manager;

import com.example.framework.cloud.CloudManager;
import com.example.framework.utils.SHA1;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

//使用okhttp 封装
public class HttpManager {

    //Url
    private static final String TOKEN_URL = "http://api-cn.ronghub.com/user/getToken.json";

    private static volatile HttpManager mInstance = null;
    private OkHttpClient mOkHttpClient;

    public HttpManager() {
        mOkHttpClient = new OkHttpClient();
    }

    public static HttpManager getInstance(){
        if(mInstance == null){
            synchronized (HttpManager.class){
                if(mInstance == null){
                    mInstance = new HttpManager();
                }
            }
        }
        return mInstance;
    }

    //Okhttp 请求融云 Token
    public String postCloudToken(HashMap<String,String> map){

        //签名参数
        String Timestamp = String.valueOf(System.currentTimeMillis() / 1000); //时间戳
        String Nonce = String.valueOf(Math.floor(Math.random() * 100000));   //随机数
        //Signature (数据签名)计算方法：将系统分配的 App Secret、Nonce (随机数)、Timestamp (时间戳)三个字符串按先后顺序拼接成一个字符串并进行 SHA1 哈希计算。
        String Signature = SHA1.sha1(CloudManager.CLOUD_SECRET + Nonce + Timestamp);  //数字签名
        //参数填充
        FormBody.Builder  builder = new FormBody.Builder();
        for(String key : map.keySet()){
            builder.add(key,map.get(key));
        }
        RequestBody requestBody = builder.build();
        //添加签名规则
        Request request = new Request.Builder()
                .url(CloudManager.TOKEN_URL)
                .addHeader("Timestamp", Timestamp)
                .addHeader("App-Key", CloudManager.CLOUD_KEY)
                .addHeader("Nonce", Nonce)
                .addHeader("Signature", Signature)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();

        try {
            return mOkHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
