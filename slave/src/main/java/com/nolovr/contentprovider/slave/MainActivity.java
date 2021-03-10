package com.nolovr.contentprovider.slave;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nolovr.contentprovider.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "slave";
    private NSettingsObserver mContentObserver;
    private Uri uri_observer_t_note;
    private Uri uri_observer_t_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentObserver = new NSettingsObserver(new Handler());

        uri_observer_t_note = Uri.parse("content://com.nolovr.settings.provider/T_Note");
        uri_observer_t_customer = Uri.parse("content://com.nolovr.settings.provider/T_Customer");

        ContentResolver cr = getContentResolver();
        cr.registerContentObserver(uri_observer_t_note, true, mContentObserver);
       // cr.registerContentObserver(uri_Observer_T_Customer, true, mContentObserver);

        /**
         * 对T_Note表进行操作  com.nolovr.settings.provider
         */

        // 设置URI
        Uri uri_table_T_Note = Uri.parse("content://com.nolovr.settings.provider/T_Note");

        // 插入表中数据
        ContentValues values = new ContentValues();
        values.put("_id", 4);
        values.put(DBConstants.KEY_FIRST_CLOUMN, "Jordan-1");
        values.put(DBConstants.KEY_SECOND_CLOUMN, "Jordan-2");


        // 获取ContentResolver
        ContentResolver resolver = getContentResolver();
        // 通过ContentResolver 根据URI 向ContentProvider中插入数据
        resolver.insert(uri_table_T_Note, values);
        querDB(uri_table_T_Note, resolver, "query T_Note:");

        /**
         * 对T_Customer表进行操作
         */
        // 和上述类似,只是URI需要更改,从而匹配不同的URI CODE,从而找到不同的数据资源
        Uri uri_table_T_Customer = Uri.parse("content://com.nolovr.settings.provider/T_Customer");

        // 插入表中数据
        ContentValues values2 = new ContentValues();
        values2.put("_id", 4);
        values2.put(DBConstants.KEY_FIRST_CLOUMN, "Yao-1");
        values2.put(DBConstants.KEY_SECOND_CLOUMN, "Yao-2");

        // 获取ContentResolver
        ContentResolver resolver2 = getContentResolver();
        // 通过ContentResolver 根据URI 向ContentProvider中插入数据
        resolver2.insert(uri_table_T_Customer, values2);

        // 通过ContentResolver 向ContentProvider中查询数据
        querDB(uri_table_T_Customer, resolver2, "query T_Customer:");
        // 关闭游标
    }

    private void querDB(Uri uri_table, ContentResolver resolver, String s) {
        // 通过ContentResolver 向ContentProvider中查询数据
        Cursor cursor = resolver.query(uri_table, new String[]{"_id", DBConstants.KEY_FIRST_CLOUMN, DBConstants.KEY_SECOND_CLOUMN}, null, null, null);
        while (cursor.moveToNext()) {
            Log.d(TAG, s + cursor.getInt(0) + " " + cursor.getString(1)+ " " + cursor.getString(2));
            // 将表中数据全部输出
        }
        cursor.close();
        // 关闭游标
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ContentResolver cr = getContentResolver();

        cr.unregisterContentObserver(mContentObserver);
    }

    class NSettingsObserver extends ContentObserver {

        public NSettingsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //添加变化的处理逻辑
            Log.d(TAG, "onChange: selfChange = " + selfChange);
            Log.d(TAG, "----onChange-----do qurey agine----------------" + Thread.currentThread().getName());
            querDB(uri_observer_t_note,getContentResolver(),"--NSettingsObserver--");

        }
    }
}

