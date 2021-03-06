package com.nolovr.nolohome.statistics.core;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.nolovr.nolohome.statistics.constants.StaticsConfig;
import com.nolovr.nolohome.statistics.db.helper.DataConstruct;
import com.nolovr.nolohome.statistics.presenter.TcDeblockObserver;
import com.nolovr.nolohome.statistics.presenter.TcNetworkObserver;
import com.nolovr.nolohome.statistics.presenter.TcScreenObserver;
import com.nolovr.nolohome.statistics.util.StatLog;

import java.util.List;

/**
 * ObserverPresenter
 */
public class TcObserverPresenter implements TcNetworkObserver.INetworkListener, TcScreenObserver.IScreenListener,
        TcDeblockObserver.IKeyguardListener {

    /** NetworkObserver  */
    private TcNetworkObserver mNetworkObserver;
    /** ScreenObserver */
    private TcScreenObserver mScreenObserver;
    /** DeblockObserver */
    private TcDeblockObserver mKeyguardObserver;
    /** isForeground */
    private boolean isForeground;
    /**  isInit */
    private boolean isInit;
    /** PackageName */
    private String mPackageName;
    /** isTopTask */
    private boolean isTopTask;
    /** isScreenOff */
    private boolean isScreenOff;
    /** isScreenLocked */
    private boolean isScreenLocked;
    /** APP_STATUS_FOREGROUND */
    public static final char APP_STATUS_FOREGROUND = '0';
    /** APP_STATUS_BACKGROUND */
    public static final char APP_STATUS_BACKGROUND = '1';
    /** TAG */
    private static final String LOG_TAG = "TamicStat::ObserverPresenter";

    private ScheduleListener scheduleListener;


    public TcObserverPresenter(ScheduleListener listener) {
        scheduleListener = listener;
    }

    public void init(Context context ) {
        if (!isInit) {
            mPackageName = context.getPackageName();
            isTopTask = true;
            isScreenOff = false;
            isScreenLocked = false;
            isForeground = true;
            registerObserver(context);
            isInit = true;
        }
    }

    /**
     *  app is Foreground
     *
     * @return ????????????????????????true???????????????false
     */
    public boolean isForeground() {
        return isForeground;
    }

    /**
     * getApp Status
     *
     * @return ????????????0??????????????????1???
     */
    public char getAppStatus() {
        if (isForeground) {
            return APP_STATUS_FOREGROUND;
        } else {
            return APP_STATUS_BACKGROUND;
        }
    }


    /**
     * OnStart
     *
     * @param aContext
     *            Context
     */
    public void onStart(Context aContext) {
        if (!isTopTask) {
            Log.d(LOG_TAG, "onStart,false-->onForegroundChanged");
            isTopTask = true;
            onForegroundChanged(aContext, true);
        }
    }

    /**
     * OnPause
     *
     * @param aContext
     *            Context
     */
    public void onPause(Context aContext) {
        Log.d(LOG_TAG, "onPause");
        if (isTopTask) {
            ActivityManager.RunningTaskInfo taskInfo = getRunningTaskInfo(aContext);
            if (taskInfo != null && taskInfo.topActivity != null) {
                String packageName = taskInfo.topActivity.getPackageName();
                if (!TextUtils.isEmpty(packageName)) {
                    if (!packageName.equals(mPackageName)) {
                        isTopTask = false;
                        StatLog.d(LOG_TAG, "onPause --> onForegroundChanged(false)");
                        onForegroundChanged(aContext, false);
                    }
                }
            }
        }
    }

    /**
     * OnStop
     *
     * @param aContext
     *            Context
     */
    public void onStop(Context aContext) {
        StatLog.d(LOG_TAG, "onStop");
        if (isTopTask) {
            ActivityManager.RunningTaskInfo taskInfo = getRunningTaskInfo(aContext);
            if (taskInfo != null && taskInfo.topActivity != null) {
                String packageName = taskInfo.topActivity.getPackageName();
                if (!TextUtils.isEmpty(packageName)) {
                    if (!packageName.equals(mPackageName)) {
                        isTopTask = false;
                        StatLog.d(LOG_TAG, "onStop --> onForegroundChanged(false)");
                        onForegroundChanged(aContext, false);
                    }
                }
            }
        }
    }


    /**
     * registerObserver
     * @param aContext
     *            Context
     */
    private void registerObserver(Context aContext) {

        if (mScreenObserver == null) {
            mScreenObserver = new TcScreenObserver(aContext, this);
        }
        mScreenObserver.start();

        if (mNetworkObserver == null) {
            mNetworkObserver = new TcNetworkObserver(aContext, this);
        }
        mNetworkObserver.start();

        if (mKeyguardObserver == null) {
            mKeyguardObserver = new TcDeblockObserver(aContext, this);
        }
        mKeyguardObserver.start();
    }

    /**
     * unregisterObserver
     */
    private void unregisterObserver() {

        if (mScreenObserver != null) {
            mScreenObserver.stop();
        }

        if (mKeyguardObserver != null) {
            mKeyguardObserver.stop();
        }

        if (mNetworkObserver != null) {
            mNetworkObserver.stop();
        }

    }


    /**
     * Destroy
     */
    public void destroy() {
        unregisterObserver();
        mScreenObserver = null;
        mKeyguardObserver = null;
        mNetworkObserver = null;
        //mDateObserver = null;
        isInit = false;
    }

    /**
     * ?????????????????????TaskInfo
     *
     * @param aContext
     *            Context
     * @return RunningTaskInfo
     */
    private ActivityManager.RunningTaskInfo getRunningTaskInfo(Context aContext) {
        ActivityManager manager = (ActivityManager) aContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = manager.getRunningTasks(1);
        if (taskList == null || taskList.isEmpty()) {
            return null;
        } else {
            return taskList.get(0);
        }
    }

    /**
     * ????????????????????????
     *
     * @param aContext
     *            Context
     * @return true locked, false otherwise
     */
    private boolean isScreenLocked(Context aContext) {
        android.app.KeyguardManager km = (android.app.KeyguardManager) aContext
                .getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }


    /**
     * ???????????????
     *
     * @param aContext
     *            Context
     * @param aIsForeground
     *            ?????????????????????true????????????false
     */
    private synchronized void onForegroundChanged(Context aContext, boolean aIsForeground) {
        // ?????????????????????
        isForeground = aIsForeground;
        //????????????????????????report
        reportData(aContext);
        //?????????????????????
        if (aIsForeground) {
            if (StaticsConfig.DEBUG) {
                Log.d(LOG_TAG, "onForeground true");
            }
            // app??????
            DataConstruct.storeAppAction("3");
            //????????????????????????
            scheduleStart();

        } else {
            //?????????
            if (StaticsConfig.DEBUG) {
                Log.d(LOG_TAG, "onForeground false");
            }
            //????????????????????????
            TcStatSdk.getInstance(aContext).send();
            // ?????????????????????
            scheduleStop();

        }
    }

    private void reportData(Context context) {

         TcStatSdk.getInstance(context).send();
    }

    private void scheduleStart() {
        if (scheduleListener != null) {
            scheduleListener.onStart();
        }

    }

    private void scheduleStop() {
        if (scheduleListener != null) {
            scheduleListener.onStop();
        }

    }

    private void scheduleReStart() {
        if (scheduleListener != null) {
            scheduleListener.onReStart();
        }

    }

    @Override
    public void onNetworkConnected(Context aContext) {
        StatLog.d(LOG_TAG, "onNetworkConnected");
        // ??????????????????
        TcHeadrHandle.getHeader(aContext).setNetworkinfo(TcHeadrHandle.getNetWorkInfo(aContext));
        if (isForeground) {
            StatLog.d(LOG_TAG, "onNetworkConnected send data");
            reportData(aContext);
            scheduleReStart();
        } else {
            scheduleStop();
        }

    }

    @Override
    public void onNetworkUnConnected(Context aContext) {
        StatLog.d(LOG_TAG, "onNetworkUnConnected");
        scheduleStop();

    }

    @Override
    public void onScreenOn(Context aContext) {

        StatLog.d(LOG_TAG, "onScreenOn");
        //Toast.makeText(aContext, "????????????", Toast.LENGTH_SHORT).show();
        if (isTopTask) {
            if (isScreenOff) {
                isScreenOff = false;

                if (isScreenLocked(aContext)) { //??????????????????????????????
                    isScreenLocked = true;
                } else {
                    StatLog.d(LOG_TAG, "onScreenOn-->onForegroundChanged(true)");
                    isScreenLocked = false;
                    onForegroundChanged(aContext, true);
                }
            }
        }

    }

    @Override
    public void onScreenOff(Context aContext) {

        //????????????????????????????????????????????????????????????????????????????????????
        StatLog.d(LOG_TAG, "onScreenOff");
        if (isTopTask) {
            if (!isScreenOff) {
                isScreenOff = true;
                if (!isScreenLocked) {
                    StatLog.d(LOG_TAG, "onScreenOff-->onForegroundChanged(false)");
                    onForegroundChanged(aContext, false);
                }
            }
        }

    }

    @Override
    public void onKeyguardGone(Context aContext) {
        StatLog.d(LOG_TAG, "onKeyGuardGone");
        if (isTopTask) {
            StatLog.d(LOG_TAG, "onKeyGuardGone foreground");
            //???????????????????????????????????????????????????
            if (isScreenLocked) {
                isScreenLocked = false;
                onForegroundChanged(aContext, true);
            }
        }
    }

    /**
     * IKeyguardListener
     */
    public interface ScheduleListener {


        /** ??????
         *
         */
        void onStart();

        /**
         *
         */
        void onStop();


        /**
         * ??????
         */
        void onReStart();
    }


}
