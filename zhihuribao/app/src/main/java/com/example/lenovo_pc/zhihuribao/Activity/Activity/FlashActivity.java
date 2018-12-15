package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.lenovo_pc.zhihuribao.R;

public class FlashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 2000; // 两秒后进入系统

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(FlashActivity.this,
                        loginActivity.class);
                FlashActivity.this.startActivity(mainIntent);
                FlashActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);

    }
}
