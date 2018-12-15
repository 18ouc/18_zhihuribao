package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.accounts.Account;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lenovo_pc.zhihuribao.Activity.SQLite.MyDatabaseHelper;
import com.example.lenovo_pc.zhihuribao.R;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;//一些数据库基本操作

    private String ACCOUNT;
    private int i = 0,j= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        dbHelper = new MyDatabaseHelper(this, "Store.db", null, 1);
        Intent intent = getIntent();
        final String account = intent.getStringExtra("account");
        ACCOUNT = account;
        Log.e("eeee!", account);

        Button btn_ALL = (Button)findViewById(R.id.btn_ALL);
        Button btn_HOT = (Button)findViewById(R.id.btn_HOT);




        //RecycleView
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(linearLayoutManager3);
        RecyclerView recyclerView4 = findViewById(R.id.recyclerView4);
        recyclerView4.setLayoutManager(linearLayoutManager4);


        btn_ALL .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(j % 2==0) {

                    RecyclerView recyclerView3 = findViewById(R.id.recyclerView3);
                    final List<Total_Content> mTotal_ContentList = new ArrayList<>();
                    Total_ContentAdapter total_contentAdapter = new Total_ContentAdapter(mTotal_ContentList);
                    recyclerView3.setAdapter(total_contentAdapter);
                  //  recyclerView3.setLayoutManager(linearLayoutManager);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("Total", null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        //RecycleView
                        recyclerView3.setAdapter(total_contentAdapter);
                        do {
                            String AccountCheck = cursor.getString(cursor.getColumnIndex("account"));
                            if (AccountCheck.equals(account)) {
                                String name = cursor.getString(cursor.getColumnIndex("name"));
                                String date = cursor.getString(cursor.getColumnIndex("date"));
                                String imageId = cursor.getString(cursor.getColumnIndex("imageId"));
                                String ID = cursor.getString(cursor.getColumnIndex("ID"));
                                Log.e("eeee!!", account);
                                Total_Content total_content = new Total_Content();
                                total_content.setDate(date);
                                total_content.setName(name);//(total_Name + total_Description);
                                total_content.setimageId(imageId);
                                total_content.setID(ID);
                                mTotal_ContentList.add(total_content);
                            }

                        } while (cursor.moveToNext());
                        cursor.close();
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
                    j++;
                }
                else{
                    RecyclerView recyclerView3 = findViewById(R.id.recyclerView3);
                  //  recyclerView3.setLayoutManager(linearLayoutManager);
                    final List<Total_Content> mTotal_ContentList = new ArrayList<>();
                    Total_ContentAdapter total_contentAdapter = new Total_ContentAdapter(mTotal_ContentList);
                    recyclerView3.setAdapter(total_contentAdapter);

                    Total_Content total_content = new Total_Content();
                    total_content.setDate("再点击栏目新闻");
                    total_content.setName("可以继续查看");
                    mTotal_ContentList.add(total_content);

                    Total_ContentAdapter.setOnItemClickListener(new Total_ContentAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //实现导入item中的数据


                        }
                    });
                    j--;
                }
            }
        });



        btn_HOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if(i % 2==0) {

                    RecyclerView recyclerView4 = findViewById(R.id.recyclerView4);
                    final List<Hot> mHotList = new ArrayList<>();
                    HotAdapter hotAdapter = new HotAdapter(mHotList);
                    recyclerView4.setAdapter(hotAdapter);
             //       recyclerView4.setLayoutManager(linearLayoutManager);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("Hot", null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        //RecycleView
                        recyclerView4.setAdapter(hotAdapter);
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
                    i++;
                }
                else{
                    RecyclerView recyclerView4 = findViewById(R.id.recyclerView4);
     //               recyclerView4.setLayoutManager(linearLayoutManager);
                    final List<Hot> mHotList = new ArrayList<>();
                    HotAdapter hotAdapter = new HotAdapter(mHotList);
                    recyclerView4.setAdapter(hotAdapter);

                    Hot hot= new Hot();
                    hot.setTitle("再点击热门消息 可以继续查看");
                    mHotList.add(hot);

                    HotAdapter.setOnItemClickListener(new HotAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //实现导入item中的数据


                        }
                    });
                    i--;
                }
            }
        });
    }

    //调用此方法实现了返回后仍然能更新数据
//    @Override
//    public void onRestart() {
//        //查询数据库
//        super.onRestart();
//        Log.e("7894656", "9011254");
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        Cursor cursor = db.query("Total", null, null, null, null, null, null);
//        if (cursor.moveToFirst()) {
//            //RecycleView
//            RecyclerView recyclerView = findViewById(R.id.recyclerView3);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            recyclerView.setLayoutManager(linearLayoutManager);
//            List<Total_Content> mTotal_ContentList = new ArrayList<>();
//            Total_ContentAdapter total_contentAdapter = new Total_ContentAdapter(mTotal_ContentList);
//            recyclerView.setAdapter(total_contentAdapter);
//            do {
//                String AccountCheck = cursor.getString(cursor.getColumnIndex("account"));
//                if (AccountCheck.equals(ACCOUNT)) {
//
//                    String name = cursor.getString(cursor.getColumnIndex("name"));
//                    String date = cursor.getString(cursor.getColumnIndex("date"));
//                    String imageId = cursor.getString(cursor.getColumnIndex("imageId"));
//                    String ID = cursor.getString(cursor.getColumnIndex("ID"));
//                    Log.e("7894656", ACCOUNT);
//                    Total_Content total_content = new Total_Content();
//                    total_content.setDate(date);
//                    total_content.setName(name);//(total_Name + total_Description);
//                    total_content.setimageId(imageId);
//                    total_content.setID(ID);
//                    mTotal_ContentList.add(total_content);
//                }
//
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//    }
}