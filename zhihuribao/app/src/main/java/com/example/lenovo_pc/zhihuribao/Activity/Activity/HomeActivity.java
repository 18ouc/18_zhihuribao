package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private MyDatabaseHelper dbHelper;
    private List<Total> mTotalList = new ArrayList<>();
    private  String accountFrom = "无";
    private  String nameFrom = "无";

    //做一个对外的接口
    private OnItemClickListener mOnItemClickListener;




    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    //做一个对外的接口




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        final Total_Content total_content = new Total_Content();

//        //实现下拉刷新功能



//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




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
        TotalAdapter TotalAdapter = new TotalAdapter(mTotalList);
        recyclerView.setAdapter(TotalAdapter);


        //接受数据
        Intent intent = getIntent();
        accountFrom = intent.getStringExtra("account");
        Log.w("R", "R丁泽中" + nameFrom);
        //接受数据
        initData("https://news-at.zhihu.com/api/4/sections");


        TotalAdapter.setOnItemClickListener(new TotalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //实现导入item中的数据
                Total total = mTotalList.get(position);
                int id = total.getID();

                Intent intent = new Intent(view.getContext(), HomeActivity_content.class);
                //     Toast.makeText(HomeActivity.this,String.valueOf(id),Toast.LENGTH_SHORT).show();


                intent.putExtra("account", accountFrom);
                intent.putExtra("ID", String.valueOf(id));
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

                        initData("https://news-at.zhihu.com/api/4/sections");
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }



    //调用此方法实现了返回后仍然能更新数据
    @Override
    public void onRestart() {
        //查询数据库
        super.onRestart();
        String account, username, Email;
        dbHelper = new MyDatabaseHelper(this, "Store.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                account = cursor.getString(cursor.getColumnIndex("account"));
                username = cursor.getString(cursor.getColumnIndex("username"));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("photo"));
                if (accountFrom.equals(account)) {
                    TextView tv_name = (TextView) findViewById(R.id.textView_n);
                    TextView tv_account = (TextView) findViewById(R.id.textView_a);
                    if (username != null) tv_name.setText(username);
                    else tv_name.setText("无");
                    if (account != null) tv_account.setText(account);
                    else tv_account.setText("无");

                    ImageView picture = (ImageView) findViewById(R.id.imageView);
                    if (blob != null){
                        Bitmap bmp = BitmapFactory.decodeByteArray(blob,0,blob.length);
                        BitmapDrawable bd = new BitmapDrawable(bmp);
                        Log.e("dajioaduahduaduha","dadss");
                        picture.setImageDrawable(bd);
                    }
                }
                break;
            } while (cursor.moveToNext());
        }
        cursor.close();     //用过之后记得调用cursor的close函数
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //实现主界面数据的传递

        //查询数据库
        String account,username,Email;
        dbHelper = new MyDatabaseHelper(this, "Store.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                account = cursor.getString(cursor.getColumnIndex("account"));
                username = cursor.getString(cursor.getColumnIndex("username"));
                if (accountFrom.equals(account)) {
                    TextView tv_name = (TextView)findViewById(R.id.textView_n) ;
                    TextView tv_account = (TextView)findViewById(R.id.textView_a) ;
                    if(username != null)
                        tv_name.setText(username);
                    else tv_name.setText("无");
                    if(account != null)
                        tv_account.setText(account);
                    else tv_account.setText("无");
                }
                break;
            } while (cursor.moveToNext());
        }
        cursor.close();     //用过之后记得调用cursor的close函数




        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    //实现小菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_xiugai) {
            Intent intent = new Intent(HomeActivity.this,ChangeActivity.class);
            intent.putExtra("account",accountFrom);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_store) {
            checkstore();
        } else if (id == R.id.nav_information) {
            Intent intent = new Intent(HomeActivity.this, DataActivity.class);
            intent.putExtra("account",accountFrom);
            startActivity(intent);
        } else if (id == R.id.nav_leave) {
            Intent intent = new Intent(HomeActivity.this,loginActivity.class);
            intent.putExtra("account",accountFrom);
            HomeActivity.this.finish();
            startActivity(intent);
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(HomeActivity.this,RegisterActivity.class);

            startActivity(intent);
        } else if (id == R.id.nav_hot) {
            //设置热门消息点击事件
            Intent intent = new Intent(HomeActivity.this,HotActivity.class);
            intent.putExtra("account",accountFrom);
            startActivity(intent);

        }
        else if (id == R.id.nav_weather) {
            //设置热门消息点击事件
            Intent intent = new Intent(HomeActivity.this,WeatherActivity.class);
            intent.putExtra("account",accountFrom);
            startActivity(intent);

        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    private void initData(final String num) {
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
    public void parseJSONWithJSONObject(String jsonData) {
        try {
            //RecycleView
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            TotalAdapter TotalAdapter = new TotalAdapter(mTotalList);
            recyclerView.setAdapter(TotalAdapter);

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i = 0;i < jsonArray.length();++i) {
                jsonObject = jsonArray.getJSONObject(i);
                int Jid = jsonObject.getInt("id");
                String Jdescription = jsonObject.getString("description");
                String Jthumbnail = jsonObject.getString("thumbnail");
                String Jname = jsonObject.getString("name");
                Log.w("HomeActivity","Jname"+ Jname);
                Total total = new Total();
                if(!Jdescription.equals("")) Jdescription = "·"+ Jdescription;
                total.setName(Jname + Jdescription );
                total.setimageId(Jthumbnail);
                total.setID(Jid);
                mTotalList.add(total);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parseJSONWithJSONObject(response);
            }
        });
    }

    private void checkstore() {
        CharSequence[] items = {"全部消息", "栏目新闻","热门消息"};// 裁剪items选项

        // 弹出对话框提示用户拍照或者是通过本地图库选择图片
        new AlertDialog.Builder(HomeActivity.this).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    // 选择了拍照
                    case 0:
                        Intent intent1 = new Intent(HomeActivity.this, StoreActivity.class);
                        intent1.putExtra("account",accountFrom);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(HomeActivity.this, Store2Activity.class);
                        intent2.putExtra("account",accountFrom);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3= new Intent(HomeActivity.this, Store3Activity.class);
                        intent3.putExtra("account",accountFrom);
                        startActivity(intent3);
                        break;
                }

            }
        }).show();
    }
}
