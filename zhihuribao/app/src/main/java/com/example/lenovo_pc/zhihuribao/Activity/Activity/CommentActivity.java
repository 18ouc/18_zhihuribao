package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private int i = 0,j = 0;

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Button tv_cL_num = (Button) findViewById(R.id.tv_Lcomment_num);
        Button tv_cS_num = (Button)findViewById(R.id.tv_Scomment_num);
        //接受数据
        Intent intent = getIntent();
        String Long_comments = intent.getStringExtra("Long_comments");
        String Short_comments = intent.getStringExtra("Short_comments");
        String Comments = intent.getStringExtra("Comments");
        id = intent.getStringExtra("id");
//        String Long_comments = intent.getStringExtra("Long_comments");
        tv_cL_num.setText(Long_comments+"条长评论");
        tv_cS_num.setText(Short_comments+"条短评论");
        //RecycleView
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        tv_cS_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(j % 2==0) {
                    //获取短评论
                    String webs = "https://news-at.zhihu.com/api/4/story/#{id}/short-comments".replace("#{id}", id);
                    Log.e("asd", webs);
                    initData_Commenturs(webs);
                    //获取短评论
                    j++;
                }
                else{
                    RecyclerView recyclerView = findViewById(R.id.recyclerView2);
                    List<Comment> mCommentList = new ArrayList<>();
                    CommentAdapter commentAdapter = new CommentAdapter(mCommentList);
                    recyclerView.setAdapter(commentAdapter);
                    Comment comment = new Comment();
                    comment.setAuthor("再点击一下短评论即可继续显示");
                    comment.setContent(" ");
                    comment.setGood("0");
                    comment.setimageId(" ");
                    comment.setTime("");
                    mCommentList.add(comment);
                    j--;
                }
            }
        });

        tv_cL_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i % 2==0) {
                    //获取长评论
                    String webl = "https://news-at.zhihu.com/api/4/story/#{id}/long-comments".replace("#{id}",id);
                    initData_Commenturl(webl);
                    //获取长评论
                    i++;
                }
                else{
                    RecyclerView recyclerView = findViewById(R.id.recyclerView1);
                    List<Comment> mCommentList = new ArrayList<>();
                    CommentAdapter commentAdapter = new CommentAdapter(mCommentList);
                    recyclerView.setAdapter(commentAdapter);
                    Comment comment = new Comment();
                    comment.setAuthor("再点击一下长评论即可继续显示");
                    comment.setContent(" ");
                    comment.setGood("0");
                    comment.setimageId(" ");
                    comment.setTime("");
                    mCommentList.add(comment);
                    i--;
                }
            }
        });


        //接受数据
    }


    //实现页面显示 获取长评论
    private void initData_Commenturl(final String num) {
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
                        showResponsel(response.toString());
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

    //实现页面显示 获取短评论
    private void initData_Commenturs(final String num) {
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
                        showResponses(response.toString());
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
    public void parseJSONWithJSONObject_commentl(String jsonData) {
        try {
            List<Comment> mCommentList = new ArrayList<>();
            //RecycleView
            RecyclerView recyclerView = findViewById(R.id.recyclerView1);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            CommentAdapter commentAdapter = new CommentAdapter(mCommentList);
            recyclerView.setAdapter(commentAdapter);
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for(int i = 0;i < jsonArray.length();++i) {
                String comment0 = " ";
                Comment comment = new Comment();
                jsonObject = jsonArray.getJSONObject(i);
                String  author = jsonObject.getString("author");
                String content = jsonObject.getString("content");
                String avatar = jsonObject.getString("avatar");
                String time = jsonObject.getString("time");
                Log.e("ouc110",author);
                String likes = jsonObject.getString("likes");
                try {
                    String reply_to = jsonObject.getString("reply_to");
                    JSONObject jsonObject0 = new JSONObject(reply_to);
                    {
                        String content0 = jsonObject0.getString("content");
                        String status = jsonObject0.getString("status");
                        String author0 = jsonObject0.getString("author");
                        if (Integer.parseInt(status) == 0) comment0 += ("\n"+ "//" + author0 + ":" + content0 + "\n");
                        Log.e("HEHE", author0);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                comment.setAuthor(author);
                comment.setContent(content +"\n" +comment0 +"\n"+"\n");
                comment.setGood(likes);
                comment.setimageId(avatar);
                comment.setTime(stampToDate(time)+"\n"+"\n");
                Log.e("ouc110",stampToDate(time));
                mCommentList.add(comment);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //解析JSON对象
    public void parseJSONWithJSONObject_comments(String jsonData) {
        try {
            List<Comment> mCommentList = new ArrayList<>();
            //RecycleView
            RecyclerView recyclerView = findViewById(R.id.recyclerView2);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            CommentAdapter commentAdapter = new CommentAdapter(mCommentList);
            recyclerView.setAdapter(commentAdapter);
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for(int i = 0;i < jsonArray.length();++i) {
                String comment0 = " ";
                Comment comment = new Comment();

                jsonObject = jsonArray.getJSONObject(i);
                String  author = jsonObject.getString("author");
                String content = jsonObject.getString("content");
                String avatar = jsonObject.getString("avatar");
                String time = jsonObject.getString("time");
                Log.e("ouc110",author);
                String likes = jsonObject.getString("likes");
                try {
                    String reply_to = jsonObject.getString("reply_to");
                    JSONObject jsonObject0 = new JSONObject(reply_to);
                    {
                        String content0 = jsonObject0.getString("content");
                        String status = jsonObject0.getString("status");
                        String author0 = jsonObject0.getString("author");
                        if (Integer.parseInt(status) == 0) comment0 += ("\n"+ "//" + author0 + ":" + content0 + "\n");
                        Log.e("HEHE", author0);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                comment.setAuthor(author);
                comment.setContent(content +"\n" +comment0+"\n"+"\n");
                comment.setGood(likes);
                comment.setimageId(avatar);
                comment.setTime(stampToDate(time)+"\n"+"\n");
                Log.e("ouc110",stampToDate(time));
                mCommentList.add(comment);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showResponses(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parseJSONWithJSONObject_comments(response);
            }
        });
    }
    private void showResponsel(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parseJSONWithJSONObject_commentl(response);
            }
        });
    }
    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
