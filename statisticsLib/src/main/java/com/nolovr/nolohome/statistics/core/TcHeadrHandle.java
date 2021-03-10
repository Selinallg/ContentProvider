package com.nolovr.nolohome.statistics.core;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.nolovr.nolohome.statistics.model.header.AppInfo;
import com.nolovr.nolohome.statistics.model.header.DeviceInfo;
import com.nolovr.nolohome.statistics.model.header.HeaderInfo;
import com.nolovr.nolohome.statistics.model.header.NetworkInfo;
import com.nolovr.nolohome.statistics.util.DeviceUtil;
import com.nolovr.nolohome.statistics.util.IpGetUtil;
import com.nolovr.nolohome.statistics.util.NetworkUtil;

import java.util.List;
import java.util.Locale;

/**
 * 头部句柄  初始化Header信息
 *
 */
public class TcHeadrHandle {

    private static AppInfo appinfo;

    private static DeviceInfo deviceinfo;

    private static NetworkInfo networkinfo;

    private static TelephonyManager mTelephonyMgr;

    private static HeaderInfo headerInfo;

    private static boolean isInit;

    private static String appId;

    private static String mChannel;


    protected static boolean initHeader(Context context, String AppId, String channel) {


        if (headerInfo == null) {
            appId = AppId;
            mChannel = channel;
            networkinfo = new NetworkInfo();
            headerInfo = new HeaderInfo(getAppInfo(context), getDeviceInfo(context), getNetWorkInfo(context));
            isInit = true;
        }

        return isInit;

    }

    public static boolean isInit() {
        return isInit;
    }


    protected static HeaderInfo getHeader(Context context) {


        if (headerInfo == null) {
            return new HeaderInfo(getAppInfo(context), getDeviceInfo(context), getNetWorkInfo(context));
        }

        return headerInfo;

    }

    /**
     * get AppInfo
     *
     * @param context
     */
    private static AppInfo getAppInfo(Context context) {

        if (appinfo != null) {
            return appinfo;
        }

        appinfo = new AppInfo();
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        String appLabel = "";

        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            appinfo.setApp_id(appId);

            if (info != null) {
                appinfo.setApp_version(info.versionName);
                appinfo.setPkgname(info.packageName);
            }
            appinfo.setApp_id(appId);
            appinfo.setChannel(mChannel);
            // sdk 版本信息
            appinfo.setSdk_version(DeviceUtil.getSdkCode(context));
            appinfo.setSdk_verson_name(DeviceUtil.getSdkName(context));

            return appinfo;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * get Device Info
     *
     * @param context
     */
    private static DeviceInfo getDeviceInfo(Context context) {

        if (deviceinfo != null) {
            return deviceinfo;
        }
        deviceinfo = new DeviceInfo();

        // 设备ID，

        mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyMgr != null) {

            deviceinfo.setDevice_id(mTelephonyMgr.getDeviceId());
            // android Imei
            deviceinfo.setImei(mTelephonyMgr.getDeviceId());
        }

        // AndroidId
        try {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            deviceinfo.setAndroid_id(androidId);
            if (TextUtils.isEmpty(deviceinfo.getImei())) {
                deviceinfo.setImei(androidId);
            }
        } catch (Exception e) {
            // do nothing. not use the data
        }

        deviceinfo.setMac(DeviceUtil.getMacAddress(context));

        deviceinfo.setModel(android.os.Build.MODEL);

        deviceinfo.setOs(android.os.Build.BRAND);

        deviceinfo.setSerial(Build.SERIAL);

        deviceinfo.setOs_version(android.os.Build.VERSION.RELEASE);

        // UniqueId
        String openId = deviceinfo.getDevice_id();
        if (openId == null || openId.trim().length() == 0) {
            openId = deviceinfo.getAndroid_id();
        }
        if (openId == null || openId.trim().length() == 0) {
            openId = deviceinfo.getMac();
        }

        deviceinfo.setOpenudid(openId);
        deviceinfo.setResolution(DeviceUtil.getScreenWidth(context) + "*" + DeviceUtil.getScreenHeight(context));
        deviceinfo.setDensity(String.valueOf(DeviceUtil.getScreenDensity(context)));
        deviceinfo.setLocale(Locale.getDefault().getLanguage());

        return deviceinfo;

    }

    /**
     * get NetWork Info
     *
     * @param context
     */
    protected static NetworkInfo getNetWorkInfo(Context context) {


        if (networkinfo == null) {

            networkinfo = new NetworkInfo();
        }
        //networkinfo.setIp_addr(NetworkUtil.getLocalIpAddress());
        networkinfo.setIp_addr(IpGetUtil.getIPAddress(context));

        networkinfo.setWifi_ind(NetworkUtil.isWifi(context));

        if (mTelephonyMgr.getSimState() == TelephonyManager.SIM_STATE_READY) {
            networkinfo.setCarrier(mTelephonyMgr.getSimOperatorName());
        }

        Location location = getLocation(context);
        if (location != null) {
            networkinfo.setLatitude(String.valueOf(location.getLatitude()));
            networkinfo.setLongitude(String.valueOf(location.getLongitude()));
        }

        if (NetworkUtil.isWifi(context)){
            String wifiChannelInfo = NetworkUtil.getWifiChannelInfo(context);
            networkinfo.setFrequency_band(wifiChannelInfo);
        }

        return networkinfo;
    }

    /**
     * 获取Location
     *
     * @param context
     * @return
     */
    private static Location getLocation(Context context) {
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);
        String locationProvider;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.PASSIVE_PROVIDER)) {
            locationProvider = LocationManager.PASSIVE_PROVIDER;
        } else {
            locationProvider = LocationManager.GPS_PROVIDER;
        }

        if (Build.VERSION.SDK_INT > 23) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return locationManager.getLastKnownLocation(locationProvider);
            }
        }

        return locationManager.getLastKnownLocation(locationProvider);
    }

}
