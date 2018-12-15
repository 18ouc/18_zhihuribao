package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class HoturlActivity extends AppCompatActivity {
    private String Long_comments = "", Popularity= "",  Short_comments = "", Comments= "";
    private String id0 = "";
    private   MyDatabaseHelper dbHelper;//一些数据库基本操作
    int i = 0;
    private  String id3;
    private  boolean xianshi = false;
    private String URL;
//    private String Long_comments= intent.getStringExtra("Long_comments");
//    private String Popularity= intent.getStringExtra("Popularity");
//    private String Short_comments= intent.getStringExtra("Short_comments");
//
//    private String Comments= intent.getStringExtra("Comments");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoturl);

        //实现在toolbar显示点赞数与评论数
        Log.w("中国海洋大学001","popularity:" + Popularity);
        //实现toolbar


        //接受从HotActivity传来的数据
        Intent intent = getIntent();
        String url= intent.getStringExtra("url");
        URL = url;
        String image= intent.getStringExtra("image");
        String title=intent.getStringExtra("title");
        String account = intent.getStringExtra("account");
        //一定要加这句话
        dbHelper = new MyDatabaseHelper(this,"Store.db",null,1);
        //一定要加这句话
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Hot",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                String AccountCheck = cursor.getString(cursor.getColumnIndex("account"));
                if (AccountCheck.equals(account)) {
                    id3 = cursor.getString(cursor.getColumnIndex("id3"));
                    String title0 = cursor.getString(cursor.getColumnIndex("title"));
                    String image0 = cursor.getString(cursor.getColumnIndex("image"));

                    if(image0.equals(image) && title0.equals(title) ){
                        xianshi = true;
                    }
                }

            } while (cursor.moveToNext());
            cursor.close();
        }



        initData_hoturl(url);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.url,menu);

        //接受从HotActivity传来的数据
        Intent intent = getIntent();
        String id= intent.getStringExtra("hot_id");
        id0 = id;
        MenuItem item_cm = menu.findItem(R.id.comment_num);
        MenuItem item_gd = menu.findItem(R.id.good_num);
        initData_show_num("https://news-at.zhihu.com/api/4/story-extra/" + id,item_cm,item_gd);
        MenuItem item_xs = menu.findItem(R.id.store);
        if(xianshi){
            item_xs.setIcon(R.mipmap.shoucanghuang);
            i++;
            Log.e("QWER","1008611" + i);
        }
        return  true;
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        //接受从TotalActivity传来的数据
        Intent intent0 = getIntent();
        String image= intent0.getStringExtra("image");
        String title=intent0.getStringExtra("title");
        String account = intent0.getStringExtra("account");

        dbHelper = new MyDatabaseHelper(this,"Store.db",null,1);
        switch (item.getItemId()){
            case R.id.store:
                if(i % 2== 0) {
                    Toast.makeText(HoturlActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.mipmap.shoucanghuang);
                    //添加数据到数据库
                    dbHelper.getWritableDatabase();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("account", account);
                    Log.e("gqwdygd", account +title+image);
                    values.put("image ", image );
                    values.put("title", title);
                    values.put("url", URL );
                    values.put("ID", id0);
                    db.insert("Hot", null, values);
                    values.clear();
                    //添加数据到数据库
                }
                else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete("Hot","id3 = ?", new String[]{id3});
                    Toast.makeText(HoturlActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    item.setIcon(R.mipmap.shoucangbai);
                }
                break;
            case R.id.good:
                break;
            case R.id.good_num:
                break;
            case  R.id.comment:
                //跳转到评论活动
                Intent intent = new Intent(HoturlActivity.this,CommentActivity.class);
                intent.putExtra("Long_comments", Long_comments);
                intent.putExtra("Short_comments",Short_comments);
                intent.putExtra("Comments", Comments);
                intent.putExtra("id", id0);
                Log.w("OUC.COM","ID" +id0);
                startActivity(intent);
                break;
            case  R.id.comment_num:
                break;
            default:

        }
        return  true;
    }


    //实现页面显示
    private void initData_hoturl(final String num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader bufferedReader = null;
                InputStream in = null;
                try {
                    URL url = new URL(num);
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
            JSONObject jsonObject = new JSONObject(jsonData);
            String share_url = jsonObject.getString("share_url");
            WebView webhot = (WebView)findViewById(R.id.wv_hot);
            webhot.getSettings().setJavaScriptEnabled(true);
            webhot.setWebViewClient(new WebViewClient());
            webhot.loadUrl(String.valueOf(share_url));

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


    //实现数字传递
    private void initData_show_num(final String web, final MenuItem item_cm, final MenuItem item_gd){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader bufferedReader = null;
                InputStream in = null;
                try {
                    URL url = new URL(web);
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
                        showResponse1(response.toString(),item_cm,item_gd);
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
    public void parseJSONWithJSONObject_content_num(String jsonData,MenuItem item_cm,MenuItem item_gd) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            Long_comments = jsonObject.getString("long_comments");
            Popularity = jsonObject.getString("popularity");
            Log.w("中国海洋大学","popularity:" + Popularity);
            Short_comments = jsonObject.getString("short_comments");
            Comments = jsonObject.getString("comments");


            item_cm.setTitle(Comments);
            item_gd.setTitle(Popularity);
//= menu.findItem(R.id.comment_num);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showResponse1(final String response1, final MenuItem item_cm, final MenuItem item_gd){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parseJSONWithJSONObject_content_num(response1,item_cm,item_gd);
            }
        });
    }



    //实现数字传递

}
