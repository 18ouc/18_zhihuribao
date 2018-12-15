package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import java.net.MalformedURLException;
import java.net.URL;

public class loginActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;//一些数据库基本操作
    private String account,psw;
    public static loginActivity instance2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //一些数据库基本操作
        dbHelper = new MyDatabaseHelper(this,"Store.db",null,1);

        //绑定监听
        Button btn_Register=(Button)findViewById(R.id.btn_Register);

        Button btn_Forget = (Button)findViewById(R.id.btn_Forget);
        Button btn_Log = (Button)findViewById(R.id.btn_Log);
        CheckBox cb_password = (CheckBox)findViewById(R.id.cb_Password);
        final EditText et_Password=(EditText)findViewById(R.id.et_Password);
        final EditText et_Account = (EditText)findViewById(R.id.et_Account);
        //绑定监听

        //接受数据
        Intent intent = getIntent();
        String accountFrom = intent.getStringExtra("account");
        et_Account.setText(accountFrom);
        et_Account.setText(accountFrom);
        String pswFrom = intent.getStringExtra("psw");
        et_Password.setText(pswFrom);
        if(accountFrom != null) et_Account.setSelection(accountFrom.length());
        //接受数据
        //initData();

        //点击登录发生的事件
        btn_Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获得EditView中的输入
                getEditString();
                if (TextUtils.isEmpty(account)) {
                    Toast.makeText(loginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(loginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (match(account, MD5.md5(psw))) {// md5Psw.equals(); 判断，输入的密码加密后，是否与保存在数据库中一致
                    Toast.makeText(loginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //Intent data = new Intent();
                    //data.putExtra("isLogin", true);//RESULT_OK为Activity系统常量，状态码为-1
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    // setResult(RESULT_OK, data);

            //实现将账号导入登录界面(导入)
                    Intent intent = new Intent(loginActivity.this,HomeActivity.class);
                    intent.putExtra("account", account);
                    startActivity(intent);
                    //实现将账号导入登录界

//                    //跳转到主界面，登录成功的状态传递到 Main3Activity 中
//                    startActivity(new Intent(loginActivity.this, HomeActivity.class));
                    return;
                } else if (psw != null && !TextUtils.isEmpty(psw) && !match(account, MD5.md5(psw))) {
                    Toast.makeText(loginActivity.this, "输入的帐号和密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(loginActivity.this, "此账号不存在", Toast.LENGTH_SHORT).show();
                }

            }
            //将EditView内容转化为字符
            private void getEditString() {
                account = et_Account.getText().toString().trim();
                psw = et_Password.getText().toString().trim();
            }


            //判断账号和密码是否匹配
            private boolean match(String Account,String Mpsw){
                boolean match = false;
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("User",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        String AccountCheck = cursor.getString(cursor.getColumnIndex("account"));
                        String PswCheck = cursor.getString(cursor.getColumnIndex("password"));
                        if(Account.equals(AccountCheck) && Mpsw.equals(PswCheck)) {//如果账号存在 则确实保存过这个用户名
                            match = true;
                        }
                    }while(cursor.moveToNext());
                    cursor.close();
                }
                return match;
            }
        });

        //点击注册发生的事件
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //点击注册发生的事件

        btn_Forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置热门消息点击事件
                Intent intent = new Intent(loginActivity.this,WeatherActivity.class);
                startActivity(intent);
            }
        });
        //实现密码可见与不可见
        cb_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_Password.setInputType(0x90);
                    et_Password.setSelection(et_Password.length());
                }
                else{
                    et_Password.setInputType(0x81);
                    et_Password.setSelection(et_Password.length());
                }
            }
        });
    }


    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader bufferedReader = null;
                InputStream in = null;
                try {
                    URL url = new URL("https://news-at.zhihu.com/api/4/sections");
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
                        parseJSONWithJSONObject(response.toString());
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
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i = 0;i < jsonArray.length();++i) {
                jsonObject = jsonArray.getJSONObject(i);
                int Jid = jsonObject.getInt("id");
                String Jdescription = jsonObject.getString("description");
                String Jthumbnail = jsonObject.getString("thumbnail");
                String Jname = jsonObject.getString("name");
                Log.w("HomeActivity","Jname"+ Jname);
          /*    Total total = new Total();
                total.setName(Jname);
                total.setDescription(Jdescription);
                total.setimageId(getNetBitmap(Jthumbnail));
                mTotalList.add(total);*/

                //添加数据到数据库
                dbHelper.getWritableDatabase();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("id", Jid);
                values.put("web",Jthumbnail);
                Log.e("丁泽中2",Jthumbnail);
                values.put("name", Jname);
                values.put("description ", Jdescription );
                db.insert("Total", null, values);
                values.clear();
                //添加数据到数据库
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



   /* private Bitmap decodeUriAsBitmapFromNet(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }*/
}
