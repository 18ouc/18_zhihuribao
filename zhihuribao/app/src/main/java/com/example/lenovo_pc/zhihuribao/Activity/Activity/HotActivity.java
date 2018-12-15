package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lenovo_pc.zhihuribao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HotActivity extends AppCompatActivity {
    private String Long_comments  = " ",Popularity = " ",Short_comments  = " ",Comments = " ";
    private List<Hot> mHotList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);

        Intent intent = getIntent();
        final String account = intent.getStringExtra("account");

        initData_hot("https://news-at.zhihu.com/api/4/news/hot");


        SwipeRefreshLayout swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                  refreshData();
            }
        });






        //RecycleView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        HotAdapter hotAdapter = new HotAdapter(mHotList);
        recyclerView.setAdapter(hotAdapter);

        HotAdapter.setOnItemClickListener(new HotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //实现导入item中的数据
                Hot hot = mHotList.get(position);
                String url = hot.getUrl();
                Intent intent = new Intent(view.getContext(), HoturlActivity.class);
              //  Toast.makeText(HotActivity.this,url,Toast.LENGTH_SHORT).show();



                intent.putExtra("url",url);
                intent.putExtra("hot_id",hot.getID());
                intent.putExtra("Long_comments",Long_comments);
                intent.putExtra("Popularity",Popularity);
                intent.putExtra("Comments",Comments);
                intent.putExtra("Short_comments",Short_comments);
                intent.putExtra("account",account);
                intent.putExtra("title",hot.getTitle());
                intent.putExtra("image",hot.getImageId());

                startActivity(intent);
            }
        });

    }

    private void refreshData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SwipeRefreshLayout swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
                        initData_hot("https://news-at.zhihu.com/api/4/news/hot");
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initData_hot(final String num) {
        //RecycleView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        HotAdapter hotAdapter = new HotAdapter(mHotList);
        recyclerView.setAdapter(hotAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader bufferedReader = null;
                InputStream in = null;
                try {
                    URL url = new URL(num);
                    Log.e("dzz!!",num);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    if (connection.getResponseCode() == 200) {
                        in = connection.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null)        // for(String line = ""; (line = bufferedReader.readLine()) != null;)
                        {
                            response.append(line);
                        }
                        showResponse(response.toString());
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(bufferedReader != null) {
                        try{
                            bufferedReader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(connection != null) connection.disconnect();
                }
            }
        }).start();
    }


    //解析JSON对象
    public void parseJSONWithJSONObject_content(String jsonData) {
        try {
            //RecycleView
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            HotAdapter hotAdapter = new HotAdapter(mHotList);
            recyclerView.setAdapter(hotAdapter);
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("recent");
            for (int i = 0; i < jsonArray.length(); ++i) {
                jsonObject = jsonArray.getJSONObject(i);
                String Jid = jsonObject.getString("news_id");
                String Jurl = jsonObject.getString("url");
                String Jthumbnail = jsonObject.getString("thumbnail");
                String Jtitle = jsonObject.getString("title");

                Log.e("Hotac D", "Jthumbnail" + Jthumbnail);
                Hot hot = new Hot();
                hot.setTitle(Jtitle);
                hot.setImageId(Jthumbnail);
                hot.setUrl(Jurl);
                hot.setID(Jid);
                mHotList.add(hot);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parseJSONWithJSONObject_content(response);
            }
        });
    }

}
