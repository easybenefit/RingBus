package com.bus.ring;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by handgunbreak@gmail.com on 16/7/9.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2016)
 * Description: Ui线程分发
 */
class UiDispatch implements Dispatch {

    private Handler uiHandler;

    UiDispatch() {

        uiHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void dispatch(Runnable runnable) {

        uiHandler.post(runnable);
    }
}
