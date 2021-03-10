package com.nolovr.contentprovider.master;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // 数据库名
    private static final String DATABASE_NAME = "sonic.db";

    // 表名
    public static final String NOTE_TABLE_NAME = "T_Note";
    public static final String CUSTOMER_TABLE_NAME = "T_Customer";


    public static final String KEY_FIRST_CLOUMN = "firstcloumn";
    public static final String KEY_second_CLOUMN = "secondcloumn";

    private static final int DATABASE_VERSION = 1;
    //数据库版本号

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 创建两个表格:用户表 和职业表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NOTE_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + " firstcloumn text,secondcloumn text)");
        //db.execSQL("CREATE TABLE IF NOT EXISTS " + NOTE_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + " firstcloumn text,secondcloumn text,thirdcloumn text,forthcloumn text)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + CUSTOMER_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + "firstcloumn text,secondcloumn text)");
        //db.execSQL("CREATE TABLE IF NOT EXISTS " + CUSTOMER_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + "firstcloumn text,secondcloumn text,thirdcloumn text,forthcloumn text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
