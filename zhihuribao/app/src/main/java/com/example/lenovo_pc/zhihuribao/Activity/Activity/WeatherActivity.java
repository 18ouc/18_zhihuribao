package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lenovo_pc.zhihuribao.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);
        WebView webView = (WebView)findViewById(R.id.www);
        webView .getSettings().setJavaScriptEnabled(true);
        webView .setWebViewClient(new WebViewClient());
        webView .loadUrl("http://m.weather.com.cn/d/town/index?lat=36.163944&lon=120.499023");
    }

}
