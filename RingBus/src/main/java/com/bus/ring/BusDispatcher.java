package com.bus.ring;


import android.util.Log;

/**
 * Created by handgunbreak@gmail.com on 16/7/9.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2016)
 * Description: Bus调度器
 */
public class BusDispatcher {

    private static final String TAG = "BusDispatcher";
    //调度度
    private static volatile BusDispatcher mBusDispatcher;
    //UI线程事件接收处理
    private UiDispatch uiPolicyHandler;
    //后台线程事件接收处理
    private BackgroundDispatch threadPolicyHandler;
    //默认的线程事件接收处理,哪个线程post,哪个线程接收处理.
    private DefaultDispatch defaultPolicyHandler;

    private BusDispatcher() {

        uiPolicyHandler = new UiDispatch();
        threadPolicyHandler = new BackgroundDispatch();
        defaultPolicyHandler = new DefaultDispatch();
    }

    //单例
    static BusDispatcher getInstance() {

        BusDispatcher busDispatcher = mBusDispatcher;
        if (busDispatcher == null) {

            synchronized (RingBus.class) {

                busDispatcher = mBusDispatcher;
                if (busDispatcher == null) {

                    busDispatcher = new BusDispatcher();
                    mBusDispatcher = busDispatcher;
                }
            }
        }
        return busDispatcher;
    }


    /**
     * 事件分发
     *
     * @param dispatchPolicy 线程策略
     * @param runnable       runnable
     */
    void dispatch(DispatchPolicy dispatchPolicy, Runnable runnable) {

        if (dispatchPolicy == null || runnable == null) {

            Log.i(TAG, "dispatch: dispatchPolicy and runnable cannot be null. please check it.");
            return;
        }

        Dispatch dispatch = null;
        switch (dispatchPolicy) {

            case ThreadPolicy:

                dispatch = threadPolicyHandler;
                break;

            case UiPolicy:

                dispatch = uiPolicyHandler;
                break;

            case DefaultPolicy:

                dispatch = defaultPolicyHandler;
                break;

            default:
                break;
        }
        if (dispatch != null) {

            dispatch.dispatch(runnable);
        }
    }
}
