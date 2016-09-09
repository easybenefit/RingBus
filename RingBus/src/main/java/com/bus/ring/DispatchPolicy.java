package com.bus.ring;

/**
 * Created by handgunbreak@gmail.com on 16/7/9.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2016)
 * Description:
 */
public enum DispatchPolicy {

    //UI线程执行
    UiPolicy,

    //当前线程执行
    DefaultPolicy,

    //子线程执行
    ThreadPolicy
}
