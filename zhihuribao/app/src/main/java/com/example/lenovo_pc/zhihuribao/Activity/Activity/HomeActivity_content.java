package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lenovo_pc.zhihuribao.Activity.Activity.Total;
import com.example.lenovo_pc.zhihuribao.Activity.Activity.TotalAdapter;
import com.example.lenovo_pc.zhihuribao.Activity.SQLite.MyDatabaseHelper;
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

public class HomeActivity_content extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private List<Total_Content> mTotal_ContentList = new ArrayList<>();


    //做一个对外的接口
    private HomeActivity.OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(HomeActivity.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    //做一个对外的接口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_content);


//
       Intent intent = getIntent();
       String id= intent.getStringExtra("ID");
       final String account = intent.getStringExtra("account");




       initData_content("https://news-at.zhihu.com/api/4/section/" + id);
        //RecycleView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        Total_ContentAdapter total_contentAdapter = new Total_ContentAdapter(mTotal_ContentList);
        recyclerView.setAdapter(total_contentAdapter);


        Total_ContentAdapter.setOnItemClickListener(new Total_ContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //实现导入item中的数据
                Total_Content total_content = mTotal_ContentList.get(position);
                String ID = total_content.getID();
                Intent intent = new Intent(view.getContext(), TotalurlActivity.class);
                intent.putExtra("imageId",total_content.getimageId());
                intent.putExtra("Name",total_content.getName());
                intent.putExtra("Date",total_content.getDate());
                intent.putExtra("ID",ID);
                intent.putExtra("account",account);
                intent.putExtra("ID",ID);
                startActivity(intent);
            }
        });

    }

    private void initData_content(final String num) {
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
            Total_ContentAdapter total_contentAdapter = new Total_ContentAdapter(mTotal_ContentList);
            recyclerView.setAdapter(total_contentAdapter);
            JSONObject jsonObject = new JSONObject(jsonData);
            String timestamp = jsonObject.getString("timestamp");
            String name = jsonObject.getString("name");
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); ++i) {
                jsonObject = jsonArray.getJSONObject(i);
                int Jid = jsonObject.getInt("id");
                String Jdate = jsonObject.getString("date");
                String Jthumbnail = jsonObject.getString("images");
                String Jtitle = jsonObject.getString("title");
                String JID = jsonObject.getString("id");

                {//去掉中括号和冒号
                    Jthumbnail = Jthumbnail.replace("[", "");
                    Jthumbnail = Jthumbnail.replace("]", "");
                    Jthumbnail = Jthumbnail.replace("\"", "");
                }
                Log.e("HomeActivity1", "Jthumbnail" + Jthumbnail);
                Total_Content total_content = new Total_Content();
                i++;
                Log.w("dzz123", String.valueOf(i));
                total_content.setDate(Jdate);
                total_content.setName(Jtitle);//(total_Name + total_Description);
                total_content.setimageId(Jthumbnail);
                total_content.setID(JID);
                mTotal_ContentList.add(total_content);

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
