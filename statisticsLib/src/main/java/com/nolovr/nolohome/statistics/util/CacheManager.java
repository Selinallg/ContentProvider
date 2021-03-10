package com.nolovr.nolohome.statistics.util;

import android.content.Context;

import java.io.File;

/**
 * =========================================================
 * 作    者:刘良国
 * e—mail:liu.lg@163.com
 * 版    本:1.0
 * 描    述:缓存管理 缓存到sp中
 * 创建日期: 2017/4/24   9:30
 * =========================================================
 */

public class CacheManager {

    private static final String TAG = "CacheManager";

    public static final String FILE_NAME_USER_LOGIN_INFO = "user_login.bak";

    public static final String FILE_NAME_STRATEGY = "strategy.bak";

    public static final String FILE_NAME_GAME_LIST = "gamelist.bak";

    private Context context;

    public CacheManager(Context context) {
        this.context = context;
    }

    // read
    public Object readCache(String fileName) {
        return ObjectWriter.read(context, fileName);
    }

    // write
    public void writeCache(Object mPushMsg, String fileName) {
        ObjectWriter.write(context, mPushMsg, fileName);
    }

    // 读完之后删除文件
    public Object readCacheAndDel(String fileName) {

        File file = new File(context.getFilesDir(), fileName);
        Object obj = null;
        if (file.exists()) {
            obj = ObjectWriter.read(context, fileName);
            file.delete();
        }
        return obj;
    }

    public void del(String fileName) {

        File file = new File(context.getFilesDir(), fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public void delAll() {
       del(FILE_NAME_STRATEGY);
       del(FILE_NAME_GAME_LIST);
    }
}
