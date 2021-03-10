package com.nolovr.nolohome.statistics.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.nolovr.nolohome.statistics.R;


/**
 * DeviceUtil
 */
public class DeviceUtil {


    /**
     * getAppVersionName
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // Get the package info
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionCode = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(packInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * getAppVersionCode
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            // Get the package info
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
        }
        return versionCode;
    }

    /**
     * getSdkCode
     */
    public static int getSdkCode(Context context) {
        int sdkVersionCode = -1;
        try {
            String sdkCode = AppUtils.getAppMetaData(context, context.getString(R.string.sdkVersionCode));
            if (sdkCode.startsWith("x")){
                sdkCode = sdkCode.substring(1);
            }
            sdkVersionCode = Integer.parseInt(sdkCode);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return sdkVersionCode;
        //return StaticsConfig.SDK_VERSION_CODE;
    }

    /**
     * getSdkName
     */
    public static String getSdkName(Context context) {
        String sdkVersionName = AppUtils.getAppMetaData(context, context.getString(R.string.sdkVersion));
        return sdkVersionName;
        //return StaticsConfig.SDK_VERSION_NAME;
    }

    /**
     * getMacAddress
     *
     * @param context
     * @return MAC
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info != null ? info.getMacAddress() : "";
    }

    /**
     * getScreenDisplay
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getScreenDisplay(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * getScreenWidth
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * getScreenHeight
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * etScreenDensity
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取当前手机型号
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }


    /**
     * 获取手机系统类型
     */
    public static String getSystemModel() {
        return Build.BRAND;
    }

    /**
     * 获取手机系统版本
     */
    public static int getSystemVersion() {
        return Build.VERSION.SDK_INT;
    }


}
