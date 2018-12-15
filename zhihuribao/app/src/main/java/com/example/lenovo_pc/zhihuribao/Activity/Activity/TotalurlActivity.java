package com.example.lenovo_pc.zhihuribao.Activity.Activity;

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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.lenovo_pc.zhihuribao.Activity.SQLite.MyDatabaseHelper;
import com.example.lenovo_pc.zhihuribao.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TotalurlActivity extends AppCompatActivity {
    private String Long_comments = "", Popularity= "",  Short_comments = "", Comments= "";
    private String id0 = "";
    private int i =0;
    private   MyDatabaseHelper dbHelper;//一些数据库基本操作
    private  boolean xianshi = false;
    private String id1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalurl);

        //接受从TotalActivity传来的数据
        Intent intent = getIntent();
        String ID= intent.getStringExtra("ID");
        String account = intent.getStringExtra("account");
        String imageId1 = intent.getStringExtra("imageId");
        String NAME1= intent.getStringExtra("Name");
        String DATE1 = intent.getStringExtra("Date");
        Log.e("djsaio","jdu"+account);
        Log.e("djsaio",DATE1);
        String abc = "10086";
        Log.e("djsaio",abc);
        Log.e("djsaio","abc"+abc);

        //一定要加这句话
        dbHelper = new MyDatabaseHelper(this,"Store.db",null,1);
        //一定要加这句话
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Total",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                Log.e("djsaio!!","abc"+abc);
                String AccountCheck = cursor.getString(cursor.getColumnIndex("account"));
                if (AccountCheck.equals(account)) {
                    id1 = cursor.getString(cursor.getColumnIndex("id1"));
                    String name0 = cursor.getString(cursor.getColumnIndex("name"));
                    String date0 = cursor.getString(cursor.getColumnIndex("date"));
                    String imageId0 = cursor.getString(cursor.getColumnIndex("imageId"));
                    String ID0 = cursor.getString(cursor.getColumnIndex("ID"));
                    Log.e("djsaio","abc"+abc);
                    if((name0.equals(NAME1) && date0.equals(DATE1) && ID0.equals(ID) &&imageId0.equals(imageId1))){
                        xianshi = true;
                    }
                }

            } while (cursor.moveToNext());
            cursor.close();
        }




        WebView webhot = (WebView)findViewById(R.id.wv_total);
        webhot.getSettings().setJavaScriptEnabled(true);
        webhot.setWebViewClient(new WebViewClient());
        webhot.loadUrl("http://daily.zhihu.com/story/" + ID);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.url,menu);

        //接受从TotalActivity传来的数据
        Intent intent = getIntent();
        String ID= intent.getStringExtra("ID");
        String account = intent.getStringExtra("account");
        id0 = ID;
        MenuItem item_cm = menu.findItem(R.id.comment_num);
        MenuItem item_gd = menu.findItem(R.id.good_num);
        MenuItem item_xs = menu.findItem(R.id.store);

        if(xianshi){
            item_xs.setIcon(R.mipmap.shoucanghuang);
            i++;
            Log.e("QWER","1008611" + i);
        }
        initData_show_num("https://news-at.zhihu.com/api/4/story-extra/" + ID,item_cm,item_gd);
        Log.w("中国海洋大学002","popularity:" + Popularity);

//控制五角星的显示状态
        //接受从TotalActivity传来的数据




        return  true;
    }
    //控制五角星的显示状态
    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        //接受从TotalActivity传来的数据
        Intent intent0 = getIntent();
        String ID= intent0.getStringExtra("ID");
        String account = intent0.getStringExtra("account");
        String imageId = intent0.getStringExtra("imageId");
        String NAME= intent0.getStringExtra("Name");
        String DATE = intent0.getStringExtra("Date");
        dbHelper = new MyDatabaseHelper(this,"Store.db",null,1);
        switch (item.getItemId()){
            case R.id.store:
                if(i % 2== 0) {
                    Toast.makeText(TotalurlActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.mipmap.shoucanghuang);
                    //添加数据到数据库
                    dbHelper.getWritableDatabase();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("account", account);
                    Log.e("QQQQ",NAME);
                    values.put("ID", ID);
                    values.put("imageId ", imageId );
                    values.put("name", NAME);
                    values.put("date", DATE);
                    db.insert("Total", null, values);
                    values.clear();
                    //添加数据到数据库
                }
                else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete("Total","id1 = ?", new String[]{id1});
                    Toast.makeText(TotalurlActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    item.setIcon(R.mipmap.shoucangbai);
                }
                i++;
                break;
            case R.id.good:
                break;
            case R.id.good_num:
                break;
            case  R.id.comment:
                //跳转到评论活动
                Intent intent = new Intent(TotalurlActivity.this,CommentActivity.class);
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
