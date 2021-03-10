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
package com.nolovr.nolohome.statistics.http;

import android.content.Context;

import com.loopj.android.http.*;
import com.nolovr.nolohome.statistics.constants.StaticsConfig;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

/**
 *
 */
public class TcHttpClient {

    private static final String BASE_URL = "";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void get(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (StaticsConfig.DEBUG) {
            //client.setProxy(context.getString(R.string.proxy_ip), 8888);
        }
        client.get(context, getAbsoluteUrl(url), headers, params, responseHandler);
    }

    public static void post(Context context, String url, Header[] headers, RequestParams params, String contentType,
                            AsyncHttpResponseHandler responseHandler) {
        if (StaticsConfig.DEBUG) {
            //client.setProxy(context.getString(R.string.proxy_ip), 8888);
        }
        client.post(context, getAbsoluteUrl(url), headers, params, contentType, responseHandler);
    }


    public static void post(Context context, String url, Header[] headers, HttpEntity entity, String contentType,
                            AsyncHttpResponseHandler responseHandler) {
        if (StaticsConfig.DEBUG) {
            //client.setProxy(context.getString(R.string.proxy_ip), 8888);
        }
        client.post(context, getAbsoluteUrl(url), headers, entity, contentType, responseHandler);
    }

    public static void post(Context context, String url, HttpEntity entity, String contentType,
                            AsyncHttpResponseHandler responseHandler) {
        if (StaticsConfig.DEBUG) {
            //client.setProxy(context.getString(R.string.proxy_ip), 8888);
        }
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void cancle(String tag, boolean isRunning) {
        client.cancelRequestsByTAG(tag, isRunning);
    }

    public static void cancleAll(boolean isRunning) {
        client.cancelAllRequests(isRunning);
    }

    /**
     * setTimeOut
     *
     * @param time 秒数
     */
    public static void setTimeOut(int time) {
        client.setTimeout(time);
    }
}
