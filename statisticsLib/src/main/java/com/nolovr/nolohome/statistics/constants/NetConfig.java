package com.nolovr.nolohome.statistics.constants;

import android.content.Context;

public class NetConfig {

    Context mContext;

    /** 是否是需要加密 */
    public static boolean DECRYPT_TAG = false;

    /**
     * des 加密、解密 密码
     */
    public static final String DES_DECRYPT = "12345678";
    /**
     * constructor
     */
    private NetConfig(Context pContext) {
        this.mContext = pContext;
    }

    /**
     * You Url
     */
    public static String ONLINE_URL ="";

    /**
     * 数据上报Debug Url
     */
    public static final String URL ="";

    /**
     * 请求超时时间
     */
    public static final int TIME_OUT = 1000 * 50 * 1;

    /** 重新请求时间 */
    public static final int RETRY_TIMES = 3;

    /** HEADERS_KEY */
    public static final String HEADERS_KEY = "data_head";

    /** key*/
    public static final String PARAMS_KEY = "data_body";


}
