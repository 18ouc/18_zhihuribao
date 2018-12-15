package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.lenovo_pc.zhihuribao.Activity.SQLite.MyDatabaseHelper;
import com.example.lenovo_pc.zhihuribao.R;

import java.util.ArrayList;
import java.util.List;

public class Store3Activity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;//一些数据库基本操作
    private String ACCOUNT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store3);

        dbHelper = new MyDatabaseHelper(this, "Store.db", null, 1);
        Intent intent = getIntent();
        final String account = intent.getStringExtra("account");
        ACCOUNT = account;

       


        //RecycleView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        final List<Hot> mHotList = new ArrayList<>();
        HotAdapter hotAdapter = new HotAdapter(mHotList);
        recyclerView.setAdapter(hotAdapter);
        //  recyclerView3.setLayoutManager(linearLayoutManager);
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
        Cursor cursor = db1.query("Hot", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            //RecycleView
            recyclerView.setAdapter(hotAdapter);
            do {
                String AccountCheck = cursor.getString(cursor.getColumnIndex("account"));
                if (AccountCheck.equals(account)) {
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String image = cursor.getString(cursor.getColumnIndex("image"));
                    String ID = cursor.getString(cursor.getColumnIndex("ID"));
                    String URL = cursor.getString(cursor.getColumnIndex("url"));
                    Log.e("fdhsuie","fdjosf");
                    Hot hot= new Hot();
                    hot.setTitle(title);
                    Log.e("TITLR",title);
                    hot.setImageId(image);
                    hot.setID(ID);
                    hot.setUrl(URL);
                    mHotList.add(hot);
                }

            } while (cursor.moveToNext());
            cursor.close();
        }

        HotAdapter.setOnItemClickListener(new HotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //实现导入item中的数据

                Hot hot = mHotList.get(position);
                Intent intent = new Intent(view.getContext(), HoturlActivity.class);
                intent.putExtra("image", hot.getImageId());
                intent.putExtra("title", hot.getTitle());
                intent.putExtra("hot_id", hot.getID());
                intent.putExtra("url", hot.getUrl());
                Log.e("hehiai",hot.getUrl());
                intent.putExtra("account", account);
                startActivity(intent);
            }
        });

        SwipeRefreshLayout swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
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

                        Intent intent = getIntent();
                        final String account = intent.getStringExtra("account");
                        ACCOUNT = account;




                        RecyclerView recyclerView = findViewById(R.id.recyclerView);


                        //  recyclerView3.setLayoutManager(linearLayoutManager);
                        //RecycleView

//                        RecyclerView recyclerView = findViewById(R.id.recyclerView);


                        final List<Hot> mHotList = new ArrayList<>();
                        HotAdapter hotAdapter = new HotAdapter(mHotList);
                        recyclerView.setAdapter(hotAdapter);

                        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                        Cursor cursor = db1.query("Hot", null, null, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            //RecycleView
                            recyclerView.setAdapter(hotAdapter);
                            do {
                                String AccountCheck = cursor.getString(cursor.getColumnIndex("account"));
                                if (AccountCheck.equals(account)) {
                                    String title = cursor.getString(cursor.getColumnIndex("title"));
                                    String image = cursor.getString(cursor.getColumnIndex("image"));
                                    String ID = cursor.getString(cursor.getColumnIndex("ID"));
                                    String URL = cursor.getString(cursor.getColumnIndex("url"));
                                    Log.e("fdhsuie","fdjosf");
                                    Hot hot= new Hot();
                                    hot.setTitle(title);
                                    Log.e("TITLR",title);
                                    hot.setImageId(image);
                                    hot.setID(ID);
                                    hot.setUrl(URL);
                                    mHotList.add(hot);
                                }

                            } while (cursor.moveToNext());
                            cursor.close();
                        }

                        HotAdapter.setOnItemClickListener(new HotAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //实现导入item中的数据

                                Hot hot = mHotList.get(position);
                                Intent intent = new Intent(view.getContext(), HoturlActivity.class);
                                intent.putExtra("image", hot.getImageId());
                                intent.putExtra("title", hot.getTitle());
                                intent.putExtra("hot_id", hot.getID());
                                intent.putExtra("url", hot.getUrl());
                                Log.e("hehiai",hot.getUrl());
                                intent.putExtra("account", account);
                                startActivity(intent);
                            }
                        });
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
