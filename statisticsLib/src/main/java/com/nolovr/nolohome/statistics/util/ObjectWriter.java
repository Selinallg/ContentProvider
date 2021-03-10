package com.nolovr.nolohome.statistics.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * =========================================================
 * 作    者:刘良国
 * e—mail:liu.lg@163.com
 * 版    本:1.0
 * 描    述:对象持久化
 * 创建日期: 2017/4/19   16:28
 * =========================================================
 */

public class ObjectWriter {

    /**
     * 写入本地文件
     *
     * @param context
     * @param obj
     * @param fileName
     */
    public static void write(Context context, Object obj, String fileName) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(bout);
            oout.writeObject(obj);
            oout.flush();
            oout.close();
            bout.close();
            byte[] b = bout.toByteArray();
            File file = new File(context.getFilesDir(), fileName);
            FileOutputStream out = new FileOutputStream(file);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 从本地文件读取
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Object read(Context context, String fileName) {
        // 拿出持久化数据
        Object obj = null;
        try {
            File file = new File(context.getFilesDir(), fileName);
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(in);
            obj = oin.readObject();
            in.close();
            oin.close();
        } catch (Exception e) {
        }
        return obj;
    }

}
