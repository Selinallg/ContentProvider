package com.nolovr.contentprovider.master;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class NSettingsProvider extends ContentProvider {

    private static final String TAG = "NSettingsProvider";

    private Context mContext;
    DBHelper mDbHelper = null;
    SQLiteDatabase db = null;
    public static final String AUTOHORITY = "com.nolovr.settings.provider";
    // 设置ContentProvider的唯一标识

    public static final int User_Code = 1;
    public static final int Job_Code = 2;

    // UriMatcher类使用:在ContentProvider 中注册URI
    private static final UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 初始化
        mMatcher.addURI(AUTOHORITY, "T_Note", User_Code);
        mMatcher.addURI(AUTOHORITY, "T_Customer", Job_Code);
        // 若URI资源路径 = content://com.nolovr.settings.provider/user ，则返回注册码User_Code
        // 若URI资源路径 = content://com.nolovr.settings.provider/job ，则返回注册码Job_Code
    }

    // 以下是ContentProvider的6个方法

    /**
     * 初始化ContentProvider
     */
    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: ");
        mContext = getContext();
        // 在ContentProvider创建时对数据库进行初始化
        // 运行在主线程，故不能做耗时操作,此处仅作展示
        mDbHelper = new DBHelper(getContext());
        db = mDbHelper.getWritableDatabase();

        // 初始化两个表的数据(先清空两个表,再各加入一个记录)
//        db.execSQL("delete from T_Note");
//        db.execSQL("insert into T_Note values(1,'Carson-1','Carson-2');");
//        db.execSQL("insert into T_Note values(2,'Kobe-1','Kobe-2');");
//
//        db.execSQL("delete from T_Customer");
//        db.execSQL("insert into T_Customer values(1,'Android-1','Android-2');");
//        db.execSQL("insert into T_Customer values(2,'iOS-1','iOS-2');");

        return true;
    }

    /**
     * 添加数据
     */

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert: ");
        // 根据URI匹配 URI_CODE，从而匹配ContentProvider中相应的表名
        // 该方法在最下面
        String table = getTableName(uri);

        // 向该表添加数据
        db.insert(table, null, values);

        // 当该URI的ContentProvider数据发生变化时，通知外界（即访问该ContentProvider数据的访问者）
        mContext.getContentResolver().notifyChange(uri, null);

//        // 通过ContentUris类从URL中获取ID
//        long personid = ContentUris.parseId(uri);
//        System.out.println(personid);

        return uri;
    }

    /**
     * 查询数据
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: ");
        // 根据URI匹配 URI_CODE，从而匹配ContentProvider中相应的表名
        // 该方法在最下面
        String table = getTableName(uri);

//        // 通过ContentUris类从URL中获取ID
//        long personid = ContentUris.parseId(uri);
//        System.out.println(personid);

        // 查询数据
        return db.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    /**
     * 更新数据
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update: ");
        // 由于不展示,此处不作展开
        return 0;
    }

    /**
     * 删除数据
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // 由于不展示,此处不作展开
        Log.d(TAG, "delete: ");
        return 0;
    }

    @Override
    public String getType(Uri uri) {

        // 由于不展示,此处不作展开
        return null;
    }

    /**
     * 根据URI匹配 URI_CODE，从而匹配ContentProvider中相应的表名
     */
    private String getTableName(Uri uri) {
        Log.d(TAG, "getTableName: uri="+uri.toString());
        String tableName = null;
        switch (mMatcher.match(uri)) {
            case User_Code:
                tableName = DBHelper.NOTE_TABLE_NAME;
                break;
            case Job_Code:
                tableName = DBHelper.CUSTOMER_TABLE_NAME;
                break;
        }
        return tableName;
    }
}

