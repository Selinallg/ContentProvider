package com.nolovr.nolohome.statistics.core;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nolovr.nolohome.statistics.constants.NetConfig;
import com.nolovr.nolohome.statistics.constants.StaticsConfig;
import com.nolovr.nolohome.statistics.http.TcHttpClient;
import com.nolovr.nolohome.statistics.util.DES;
import com.nolovr.nolohome.statistics.util.JsonUtil;
import com.nolovr.nolohome.statistics.util.StatLog;
import java.io.UnsupportedEncodingException;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 *
 */
public class TcNetEngine {


    private Context context;

    private TcHttpClient mHttpClient;

    private String mKey;

    /**
     * 重试次数
     */
    protected int mRetrytimes = NetConfig.RETRY_TIMES;

    public static final String TAG = "TamicStat::TaNetEngine";

    /**
     * 是否支持断点
     */
    protected boolean mCanContinue;

    private String mHostUrl = NetConfig.ONLINE_URL;

    private PaJsonHttpResponseHandler mTaskHandler;

    private IUpLoadlistener mUpLoadlistener;

    public TcNetEngine(Context context, IUpLoadlistener upLoadlistener) {

        this(context, null, upLoadlistener);

    }

    public TcNetEngine(Context context, TcHttpClient httpClient, IUpLoadlistener upLoadlistener) {
        this.context = context;
        mHttpClient = httpClient;
        mCanContinue = true;
        mTaskHandler = new PaJsonHttpResponseHandler(true);
        mUpLoadlistener = upLoadlistener;
        init();
    }

    private void init() {

        if (StaticsConfig.DEBUG && !TextUtils.isEmpty(NetConfig.URL)) {
            mHostUrl = NetConfig.URL;
        } else {
            mHostUrl = NetConfig.ONLINE_URL;
        }
    }

    public TcHttpClient getHttpClient() {
        return mHttpClient;
    }

    public void setHttpClient(TcHttpClient mHttpClient) {
        this.mHttpClient = mHttpClient;
    }

    public String start(final String... strings) {

        if (TextUtils.isEmpty(mHostUrl)) {
            init();
        }

        String str = JsonUtil.toJSONString(TcHeadrHandle.getHeader(context));

        StatLog.d(TAG, "head:" + str);

        String contentEntity = "{" + NetConfig.HEADERS_KEY + ":" + str + "," + NetConfig.PARAMS_KEY + ":" + strings[0] + "}";

        if (NetConfig.DECRYPT_TAG) {
            // 加密
            try {
                contentEntity = DES.encrypt(contentEntity, NetConfig.DES_DECRYPT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StringEntity entity = new StringEntity(contentEntity, "utf-8");

        StatLog.d(TAG, "body:" + strings[0]);

        TcHttpClient.post(context, mHostUrl, entity, "application/json", mTaskHandler);
        //TcHttpClient.post(context, mHostUrl, reqHeaders, entity, "application/json", mTaskHandler);
        //TcHttpClient.post(context, mHostUrl, reqHeaders, requestParams, "application/json;charset=UTF-8", mTaskHandler);//乱码
        return null;
    }

    void cancel() {

        TcHttpClient.cancle(mKey, true);
    }

    private class PaJsonHttpResponseHandler extends AsyncHttpResponseHandler {

        public PaJsonHttpResponseHandler() {
        }

        public PaJsonHttpResponseHandler(Looper looper) {
            super(looper);
        }

        public PaJsonHttpResponseHandler(boolean usePoolThread) {
            super(usePoolThread);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            String result = null;
            try {
                result = new String(responseBody, "utf-8");
                try {
                    // 解密
                    if (NetConfig.DECRYPT_TAG) {
                        result = DES.decrypt(result, NetConfig.DES_DECRYPT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            StatLog.d(TAG, "-----------------" + result);

            if (mUpLoadlistener != null) {
                mUpLoadlistener.onSucess();
            }

            for (Header tmp : headers) {
                StatLog.d(TAG, tmp.getName() + ":" + tmp.getValue());
            }

            StatLog.d(TAG, "response code: " + statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                StatLog.d(TAG, "onSuccess");
                mCanContinue = false;
            } else if (statusCode == HttpStatus.SC_PARTIAL_CONTENT) {
                mCanContinue = true;
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            StatLog.d(TAG, statusCode + ":" + statusCode);
            if (mUpLoadlistener != null) {
                mUpLoadlistener.onFailure();
            }
            cancel();
        }

    }

}
