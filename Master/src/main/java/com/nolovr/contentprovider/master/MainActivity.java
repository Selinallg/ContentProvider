package com.nolovr.contentprovider.master;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nolovr.contentprovider.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Master";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 对T_Note表进行操作
         */

        // 设置URI
        Uri uri_T_Note = Uri.parse("content://com.nolovr.settings.provider/T_Note");

        // 插入表中数据
        ContentValues values = new ContentValues();
        values.put("_id", 3);
        values.put(DBHelper.KEY_FIRST_CLOUMN, "Iverson-1");
        values.put(DBHelper.KEY_second_CLOUMN, "Iverson-2");
        queryDB(uri_T_Note, values, "query T_Note:");


        /**
         * 对T_Customer表进行操作
         */
        // 和上述类似,只是URI需要更改,从而匹配不同的URI CODE,从而找到不同的数据资源
        Uri uri_T_Customer = Uri.parse("content://com.nolovr.settings.provider/T_Customer");

        // 插入表中数据
        ContentValues values2 = new ContentValues();
        values2.put("_id", 3);
        values2.put(DBHelper.KEY_FIRST_CLOUMN, "NBA Player-1");
        values2.put(DBHelper.KEY_second_CLOUMN, "NBA Player-2");

        // 获取ContentResolver
        queryDB(uri_T_Customer, values2, "query T_Customer:");
        // 关闭游标
    }

    private void queryDB(Uri uri_table, ContentValues values, String s) {
        // 获取ContentResolver
        ContentResolver resolver = getContentResolver();
        // 通过ContentResolver 根据URI 向ContentProvider中插入数据
        resolver.insert(uri_table, values);

        // 通过ContentResolver 向ContentProvider中查询数据
        Cursor cursor = resolver.query(uri_table, new String[]{"_id", DBHelper.KEY_FIRST_CLOUMN}, null, null, null);
        while (cursor.moveToNext()) {
            Log.d(TAG, s + cursor.getInt(0) + " " + cursor.getString(1));
            // 将表中数据全部输出
        }
        cursor.close();
        // 关闭游标
    }
}
