package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class RegisterActivity extends AppCompatActivity {

    private String Account , psw,username;
    private MyDatabaseHelper dbHelper;
    private final String TAG = "Main2Activity";

    //正则表达式
//    String regular0 = "^[\\s\\S]";
//    String regular1 ="^[\\s\\S]";
//    String regular2 ="^[\\s\\S]";
    //正则表达式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new MyDatabaseHelper(this,"Store.db",null,1);
        Button btn_Register2 = (Button)findViewById(R.id.btn_Register) ;
        final EditText et_Account = (EditText)findViewById(R.id.et_Account);
        final EditText et_Password = (EditText)findViewById(R.id.et_Password);

        CheckBox cb_password2= (CheckBox)findViewById(R.id.cb_Password);




        cb_password2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        btn_Register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将TextView内容转化为字符串
                getEditString();
                //判断输入框内容
                if(TextUtils.isEmpty(Account)){
                    Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
//                }else if(!Account.matches(regular2)){
//                    Toast.makeText(RegisterActivity.this, "账号不全是数字哦~", Toast.LENGTH_SHORT).show();
//                    return;
//                }else if(!psw.matches(regular0)){
//                    Toast.makeText(RegisterActivity.this, "密码长度过短啦~\n密码至少要有8位哦~", Toast.LENGTH_SHORT).show();
//                    return;
//                }else if(!psw.matches(regular1)){
//                    Toast.makeText(RegisterActivity.this, "密码的输入不规范哦~\n密码数字、字母或符号组成哦~", Toast.LENGTH_SHORT).show();
//                    return;
                }else if(isExistUserName(Account)){
                    Toast.makeText(RegisterActivity.this, "此账号已经存在", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //添加数据到数据库
                    dbHelper.getWritableDatabase();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("account", Account);
                    String md5Psw = MD5.md5(psw);//对密码进行加密
                    values.put("password", md5Psw);
                    db.insert("User", null, values);
                    values.clear();
                    //添加数据到数据库
                    Cursor cursor = db.query("User",null,null,null,null,null,null);
                    if(cursor.moveToFirst()){
                        do{
                            String AccountCheck = cursor.getString(cursor.getColumnIndex("account"));
                            Log.w(TAG,AccountCheck);
                        }while(cursor.moveToNext());
                        cursor.close();
                    }
                    //
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                    if(loginActivity.instance2 != null){
                        loginActivity.instance2.finish();
                    }

                    //实现注册后的账号在登录界面上显示
                    Intent intent = new Intent(RegisterActivity.this,loginActivity.class);
                    intent.putExtra("account", Account);
                    intent.putExtra("psw",psw);
                    Log.w("R","S丁泽中" +psw);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                }
            }
            //将TextView内容转化为字符串
            private void getEditString() {
                Account = et_Account.getText().toString().trim();
                psw = et_Password.getText().toString().trim();;
            }
            //将TextView内容转化为字符串

            //从SQL中读取输入的用户名，判断SQL中是否有此用户名
            private boolean isExistUserName(String Account){
                boolean has_Account = false;
                //SQL查询是否有这个账号
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("User",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        String AccountCheck = cursor.getString(cursor.getColumnIndex("account"));
                        if(Account.equals(AccountCheck)) {//如果账号存在 则确实保存过这个用户名
                            has_Account = true;
                        }
                        Log.d(TAG,AccountCheck);
                    }while(cursor.moveToNext());
                    cursor.close();
                }
                return has_Account;
            }
        });
    }
}
