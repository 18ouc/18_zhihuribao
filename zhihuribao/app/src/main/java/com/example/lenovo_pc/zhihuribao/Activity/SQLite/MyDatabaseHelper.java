package com.example.lenovo_pc.zhihuribao.Activity.SQLite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_USER = "create table User ("
            + "id integer primary key autoincrement, "
            + "username text,"
            + "account text,"
            + "password text,"
            + "photo blob,"
            + "Email text)";

    private static final String CREATE_TOTAL = "create table Total ("
            + "id1 integer primary key autoincrement, "
            + "imageId  text, "
            + "name text,"
            + "ID text,"
            + "date text,"
            + "account text)";

    private static final String CREATE_TOTAL_CONTENT = "create table Total_Content ("
            + "id integer primary key autoincrement, "
            + "timestamp integer,"
            + "all_name text,"
            + "web text, "
            + "name text,"
            + "image integer,"
            + "date text)";



    private static final String CREATE_HOT = "create table Hot ("
            + "id3 integer primary key autoincrement, "
            + "url text, "
            + "account text,"
            + "ID text,"
            + "image text,"
            + "title text)";





    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_HOT );
        db.execSQL(CREATE_TOTAL );
        db.execSQL(CREATE_TOTAL_CONTENT );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists Shop");       //升级数据库 = 删掉可能存在的旧表 + 创建新表
        db.execSQL("drop table if exists Hot");
        db.execSQL("drop table if exists Total");
        db.execSQL("drop table if exists Total_Content");
        onCreate(db);
    }
}

