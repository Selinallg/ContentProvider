/*
 *    Copyright (C) 2016 Tamic
 *
 *    link :
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.nolovr.nolohome.statistics.core;

import android.content.Context;
import android.text.TextUtils;


import com.nolovr.nolohome.statistics.constants.NetConfig;
import com.nolovr.nolohome.statistics.db.helper.StaticsAgent;
import com.nolovr.nolohome.statistics.http.TcHttpClient;
import com.nolovr.nolohome.statistics.service.Platform;
import com.nolovr.nolohome.statistics.util.NetworkUtil;
import com.nolovr.nolohome.statistics.util.StatLog;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Stat UpLoadManager
 */
public class TcUpLoadManager implements IUpLoadlistener {


    /**
     * context
     */
    private Context mContext;
    /**
     * http client
     */
    private TcHttpClient mHttpClient;
    /**
     * UpLoadManager
     */
    private static TcUpLoadManager sInstance;

    private Boolean isRunning = false;

    private AtomicReference<TcNetEngine> atomic;

    private TcNetEngine netEngine;
    /**
     * Log TAG
     */
    private static final String TAG = TcNetEngine.class.getSimpleName();

    /**
     * getInstance
     *
     * @param aContext context
     * @return UpLoadManager
     */
    public static synchronized TcUpLoadManager getInstance(Context aContext) {
        if (sInstance == null) {
            sInstance = new TcUpLoadManager(aContext);
        }
        return sInstance;
    }

    /**
     * constructor
     *
     * @param aContext context
     */
    private TcUpLoadManager(Context aContext) {
        mContext = aContext;
        init();
    }

    /**
     * init
     */
    private void init() {
        mHttpClient = getHttpclient();
        atomic = new AtomicReference<>();
        netEngine = new TcNetEngine(mContext, this);
    }


    /**
     * report
     */
    public void report(String jsonString) {

        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            return;
        }

        if (TextUtils.isEmpty(jsonString)) {
            return;
        }
        //netEngine.setHttpClient(getHttpclient());
        atomic.set(netEngine);
        atomic.getAndSet(netEngine).start(jsonString);
    }

    /**
     * cancle
     */
    public void cancle() {

        if (atomic.get() != null) {
            atomic.get().cancel();

        }

    }


    /**
     * get http client
     *
     * @return http client
     */
    public TcHttpClient getHttpclient() {
        if (mHttpClient == null) {
            // HttpClient
            mHttpClient = new TcHttpClient();
            mHttpClient.setTimeOut(NetConfig.TIME_OUT);
        }
        return mHttpClient;

    }


    @Override
    public void onStart() {

        isRunning = true;
    }

    @Override
    public void onUpLoad() {

        isRunning = true;
    }

    @Override
    public void onSucess() {

        isRunning = false;
        // delete data
        StatLog.d(TAG, "DELETE  ???StaticsAgent.deleteTable()");
        // delete data
        Platform.get().execute(new Runnable() {
            @Override
            public void run() {
                StaticsAgent.deleteData();
                //StatLog.d(TAG, "delete after :>>>>>>" + JsonUtil.toJSONString(StaticsAgent.getDataBlock()));
                //StatLog.d(TAG, "delete after :>>>>>>" + JsonUtil.toJSONString(StaticsAgent.getDataBlock()));
            }
        });

    }

    @Override
    public void onFailure() {

        isRunning = false;

    }

    @Override
    public void onCancell() {

        isRunning = false;
    }
}
