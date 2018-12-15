package com.example.lenovo_pc.zhihuribao.Activity.Activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DebugUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo_pc.zhihuribao.Activity.SQLite.MyDatabaseHelper;
import com.example.lenovo_pc.zhihuribao.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.content.Context;

import static com.example.lenovo_pc.zhihuribao.R.*;
import static com.example.lenovo_pc.zhihuribao.R.mipmap.*;


public class DataActivity extends AppCompatActivity {
    private int i = 0;
    private String accountFrom;
    private MyDatabaseHelper dbHelper;
    private String account = "无";
    private String username = "无";
    private String Email = "无";


    private String name0, Email0;


    public static final int TAKE_PHOTO = 1;
    private ImageButton picture;
    private Uri imageUri;


    public static final int CHOOSE_PHOTO = 2;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_data);

        picture = (ImageButton) findViewById(id.in_picture);




        //接受数据
        Intent intent = getIntent();
        accountFrom = intent.getStringExtra("account");

        //查询数据库
        dbHelper = new MyDatabaseHelper(this, "Store.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                account = cursor.getString(cursor.getColumnIndex("account"));
                username = cursor.getString(cursor.getColumnIndex("username"));
                Email = cursor.getString(cursor.getColumnIndex("Email"));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("photo"));
                //byte转bitmap
                if (accountFrom.equals(account)) {
                    if(blob != null){
                        picture = (ImageButton) findViewById(R.id.in_picture);
                        Bitmap bmp = BitmapFactory.decodeByteArray(blob,0,blob.length);
                        BitmapDrawable bd = new BitmapDrawable(bmp);
                        Log.e("dajioaduahduaduha","dadss");
                        picture.setImageDrawable(bd);

                        Log.w("1346","10086");
                    }

                    EditText et_name = (EditText) findViewById(R.id.et_name);
                    EditText et_Email = (EditText) findViewById(R.id.et_Email);
                    if (username != null) et_name.setText(username);
                    else et_name.setText("无");

                    if (Email != null) et_Email.setText(Email);
                    else et_Email.setText("无");

                }
                break;
            } while (cursor.moveToNext());
        }
        cursor.close();     //用过之后记得调用cursor的close函数


        TextView TV_HINT = (TextView) findViewById(id.tv_wel);

        TV_HINT.setText("欢迎您，用户：" + accountFrom);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data, menu);

        //接受从HotActivity传来的数据
        Intent intent = getIntent();

        MenuItem item_pen = menu.findItem(id.xiugai);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.xiugai:
                if (i % 2 == 0) {
                    EditText et_name = (EditText) findViewById(id.et_name);
                    EditText et_Email = (EditText) findViewById(id.et_Email);
                    et_name.setInputType(InputType.TYPE_CLASS_TEXT);
                    et_Email.setInputType(InputType.TYPE_CLASS_TEXT);
                    et_name.setFocusableInTouchMode(true);
                    et_name.requestFocus();
                    et_Email.setFocusableInTouchMode(true);
                    et_Email.requestFocus();
                    ImageButton iv_show_picture = (ImageButton) findViewById(id.in_picture);
                    iv_show_picture.setEnabled(true);
                    iv_show_picture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            takePhotoOrSelectPicture();
                        }
                    });
                    item.setIcon(right);
                }

                if (i % 2 != 0) {
                    EditText et_name = (EditText) findViewById(id.et_name);
                    EditText et_Email = (EditText) findViewById(id.et_Email);
                    et_name.setInputType(InputType.TYPE_NULL);
                    et_Email.setInputType(InputType.TYPE_NULL);
                    item.setIcon(pen);
                    ImageButton iv_show_picture = (ImageButton) findViewById(id.in_picture);
                    iv_show_picture.setEnabled(false);
                    getEditString();
                    et_name.setFocusable(false);
                    et_Email.setFocusable(false);
                    et_name.setFocusableInTouchMode(false);
                    et_Email.setFocusableInTouchMode(false);
                    //修改数据库
                    dbHelper = new MyDatabaseHelper(this, "Store.db", null, 1);
                    dbHelper.getWritableDatabase();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("username", name0);
                    values.put("Email", Email0);
                    db.update("User", values, "account = ?", new String[]{accountFrom});
                    values.clear();
                    //修改数据库


                    Toast.makeText(DataActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                i++;
                break;

            default:

        }
        return true;
    }

    //将EditView内容转化为字符
    private void getEditString() {
        EditText et_name = (EditText) findViewById(id.et_name);
        EditText et_Email = (EditText) findViewById(id.et_Email);
        name0 = et_name.getText().toString().trim();
        Email0 = et_Email.getText().toString().trim();
    }

    private void takePhotoOrSelectPicture() {
        CharSequence[] items = {"拍照", "从相册选择"};// 裁剪items选项

        // 弹出对话框提示用户拍照或者是通过本地图库选择图片
        new AlertDialog.Builder(DataActivity.this).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    // 选择了拍照
                    case 0:
                        getPicFromCamera();
                        break;
                    case 1:
                        getPicFromLocal();
                        break;
                }

            }
        }).show();
    }
////实现调用相册的功能
    private void getPicFromLocal() {
        if (ContextCompat.checkSelfPermission(DataActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DataActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"你取消了调用相册",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }

    @TargetApi(19)
    private void handleImageOnKiKat(Intent data) {
        String imagepath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagepath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.document".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagepath = getImagePath(contentUri, null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagepath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagepath = uri.getPath();
        }
        displayImage(imagepath);
    }

    private void handImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
            picture = (ImageButton) findViewById(id.in_picture);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,os);


            //修改数据库
            dbHelper = new MyDatabaseHelper(this, "Store.db", null, 1);
            dbHelper.getWritableDatabase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("photo",os.toByteArray());
            db.update("User", values, "account = ?", new String[]{accountFrom});
            values.clear();
            //修改数据库
        }else{
            Toast.makeText(this,"未能获得图片",Toast.LENGTH_SHORT).show();
        }
    }









    private void getPicFromCamera() {
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(DataActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Toast.makeText(this,"未能获得图片",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Log.e("MAIN", "1008611");
                        //将拍摄的照片显示出来
                        ImageButton picture = (ImageButton) findViewById(id.in_picture);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);




                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                        byte[]byteArray = baos.toByteArray();


//                        //修改数据库
//                        dbHelper = new MyDatabaseHelper(this, "Store.db", null, 1);
//                        dbHelper.getWritableDatabase();
//                        SQLiteDatabase db = dbHelper.getWritableDatabase();
//                        ContentValues values = new ContentValues();
//                        values.put("photo", byteArray);
//                        db.update("User", values, "account = ?", new String[]{accountFrom});
//                        values.clear();
//                        //修改数据库

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

                //相册
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKiKat(data);
                    }else{
                        handImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }


}










