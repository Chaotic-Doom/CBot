package me.blayyke.cbot;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class CBHttp {
    private static final CBHttp instance;
    private OkHttpClient httpClient = new OkHttpClient.Builder().build();

    public static CBHttp getInstance() {
        return instance;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public String get(String url) {
        try {
            Request url1 = new Request.Builder().get().url(url).build();
            Response response = httpClient.newCall(url1).execute();
            if (response.body() == null) return null;
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static {
        instance = new CBHttp();
    }
}