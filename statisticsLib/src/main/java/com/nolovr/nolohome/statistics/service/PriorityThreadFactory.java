package com.nolovr.nolohome.statistics.service;

import android.os.Process;
import android.util.Log;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class PriorityThreadFactory implements ThreadFactory {

    private static final String TAG = "PriorityThreadFactory";

    private final String mName;
    private final int mPriority;
    private final AtomicInteger mNumber = new AtomicInteger();

    public PriorityThreadFactory(String name, int priority) {
        mName = name;
        mPriority = priority;
    }
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, mName +"-"+mNumber.getAndIncrement()){
            @Override
            public void run() {
                try {
                    Process.setThreadPriority(mPriority);
                    super.run();
                } catch (Exception e) {
                    // TODO: 2018/7/28 上报异常信息
                    Log.e(TAG, "run: 出异常了" );
                    e.printStackTrace();
                }
            }
        };
    }
}
