package com.bus.ring;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by handgunbreak@gmail.com on 16/7/9.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2016)
 * Description: 子线程分发
 */
class BackgroundDispatch implements Dispatch {

    //线程池
    private ExecutorService mThreadPool;

    BackgroundDispatch() {

        mThreadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void dispatch(Runnable runnable) {

        mThreadPool.execute(runnable);
    }
}
