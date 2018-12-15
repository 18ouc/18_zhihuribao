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
import android.widget.Button;

import com.example.lenovo_pc.zhihuribao.Activity.SQLite.MyDatabaseHelper;
import com.example.lenovo_pc.zhihuribao.R;

import java.util.ArrayList;
import java.util.List;


public class Store2Activity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;//一些数据库基本操作
    private String ACCOUNT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store2);
        dbHelper = new MyDatabaseHelper(this, "Store.db", null, 1);
        Intent intent = getIntent();
        final String account = intent.getStringExtra("account");
        ACCOUNT = account;





        //RecycleView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        final List<Total_Content> mTotal_ContentList = new ArrayList<>();
        Total_ContentAdapter total_contentAdapter = new Total_ContentAdapter(mTotal_ContentList);
        recyclerView.setAdapter(total_contentAdapter);
        //  recyclerView3.setLayoutManager(linearLayoutManager);
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
        Cursor cursor1 = db1.query("Total", null, null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            //RecycleView
            recyclerView.setAdapter(total_contentAdapter);
            do {
                String AccountCheck = cursor1.getString(cursor1.getColumnIndex("account"));
                if (AccountCheck.equals(account)) {
                    String name = cursor1.getString(cursor1.getColumnIndex("name"));
                    String date = cursor1.getString(cursor1.getColumnIndex("date"));
                    String imageId = cursor1.getString(cursor1.getColumnIndex("imageId"));
                    String ID = cursor1.getString(cursor1.getColumnIndex("ID"));
                    Log.e("eeee!!", account);
                    Total_Content total_content = new Total_Content();
                    total_content.setDate(date);
                    total_content.setName(name);//(total_Name + total_Description);
                    total_content.setimageId(imageId);
                    total_content.setID(ID);
                    mTotal_ContentList.add(total_content);
                }

            } while (cursor1.moveToNext());
            cursor1.close();
        }



        Total_ContentAdapter.setOnItemClickListener(new Total_ContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //实现导入item中的数据

                Total_Content total_content = mTotal_ContentList.get(position);
                String ID = total_content.getID();
                Intent intent = new Intent(view.getContext(), TotalurlActivity.class);
                intent.putExtra("imageId", total_content.getimageId());
                intent.putExtra("Name", total_content.getName());
                intent.putExtra("Date", total_content.getDate());
                intent.putExtra("ID", ID);
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

                        final List<Total_Content> mTotal_ContentList = new ArrayList<>();
                        Total_ContentAdapter total_contentAdapter = new Total_ContentAdapter(mTotal_ContentList);
                        recyclerView.setAdapter(total_contentAdapter);
                        //  recyclerView3.setLayoutManager(linearLayoutManager);
                        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                        Cursor cursor1 = db1.query("Total", null, null, null, null, null, null);
                        if (cursor1.moveToFirst()) {
                            //RecycleView
                            recyclerView.setAdapter(total_contentAdapter);
                            do {
                                String AccountCheck = cursor1.getString(cursor1.getColumnIndex("account"));
                                if (AccountCheck.equals(account)) {
                                    String name = cursor1.getString(cursor1.getColumnIndex("name"));
                                    String date = cursor1.getString(cursor1.getColumnIndex("date"));
                                    String imageId = cursor1.getString(cursor1.getColumnIndex("imageId"));
                                    String ID = cursor1.getString(cursor1.getColumnIndex("ID"));
                                    Log.e("eeee!!", account);
                                    Total_Content total_content = new Total_Content();
                                    total_content.setDate(date);
                                    total_content.setName(name);//(total_Name + total_Description);
                                    total_content.setimageId(imageId);
                                    total_content.setID(ID);
                                    mTotal_ContentList.add(total_content);
                                }

                            } while (cursor1.moveToNext());
                            cursor1.close();
                        }



                        Total_ContentAdapter.setOnItemClickListener(new Total_ContentAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //实现导入item中的数据

                                Total_Content total_content = mTotal_ContentList.get(position);
                                String ID = total_content.getID();
                                Intent intent = new Intent(view.getContext(), TotalurlActivity.class);
                                intent.putExtra("imageId", total_content.getimageId());
                                intent.putExtra("Name", total_content.getName());
                                intent.putExtra("Date", total_content.getDate());
                                intent.putExtra("ID", ID);
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
