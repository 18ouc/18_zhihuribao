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
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo_pc.zhihuribao.Activity.SQLite.MyDatabaseHelper;
import com.example.lenovo_pc.zhihuribao.R;

public class ChangeActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;

    private  String P1,P2,P3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        final EditText et_P1 = (EditText)findViewById(R.id.et_P1);
        final EditText et_P2 = (EditText)findViewById(R.id.et_P2);
        final EditText et_P3 = (EditText)findViewById(R.id.et_P3);
        Intent intent = getIntent();
        final String account = intent.getStringExtra("account");
        Log.e("!!!",account);


        Button btn_xiugai12 = (Button)findViewById(R.id.btn_xiugai12);
        dbHelper = new MyDatabaseHelper(this,"Store.db",null,1);
        btn_xiugai12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getEditString();
                if (TextUtils.isEmpty(P1)) {
                    Toast.makeText(ChangeActivity.this, "请输入旧密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(P2)) {
                    Toast.makeText(ChangeActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(P2)) {
                    Toast.makeText(ChangeActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!P2.equals(P3)) {
                    Toast.makeText(ChangeActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!match(account, MD5.md5(P1))) {
                    Toast.makeText(ChangeActivity.this, "输入的旧密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //修改数据库
                    dbHelper.getWritableDatabase();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    String md5Psw = MD5.md5(P3);//对密码进行加密
                    values.put("password",md5Psw);
                    db.update("User", values, "account = ?", new String[]{account});
                    values.clear();
                    Toast.makeText(ChangeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    //实现注册后的账号在登录界面上显示


                    Intent intent = new Intent(ChangeActivity.this,loginActivity.class);
                    intent.putExtra("account", account);
                    intent.putExtra("psw",P3);
                    startActivity(intent);
                    ChangeActivity.this.finish();
                    //修改数据库
                }


            }
            private   void getEditString() {
                P1 = et_P1.getText().toString().trim();
                P2 = et_P2.getText().toString().trim();
                P3 =  et_P3.getText().toString().trim();
            }
        });
    }

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

}
