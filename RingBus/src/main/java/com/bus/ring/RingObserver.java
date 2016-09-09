package com.bus.ring;

import android.text.TextUtils;

import java.lang.ref.WeakReference;

/**
 * Created by handgunbreak@gmail.com on 16/7/6.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2016)
 * Description:
 */
public abstract class RingObserver<T, O> {

    //过滤条件
    public String mFilter;
    //方法名
    public String mMethodName;
    //post对象类型
    public Class mBusEventClass;
    //线程策略
    public DispatchPolicy mDispatchPolicy;
    //Target reference;
    private WeakReference<T> mTargetWeakReference;

    /**
     * constructor
     *
     * @param target         target
     * @param filter         过滤
     * @param busEventClass  发送对象类
     * @param dispatchPolicy 线程
     */
    public RingObserver(T target, String filter, Class busEventClass, DispatchPolicy dispatchPolicy, String methodName) {


        mMethodName = methodName;
        mBusEventClass = busEventClass;
        mDispatchPolicy = dispatchPolicy;
        mTargetWeakReference = new WeakReference<>(target);
        mFilter = TextUtils.isEmpty(filter) ? RingConstants.RingFilter : filter;
    }

    /**
     * 判断Target是否为null;
     *
     * @return result
     */
    boolean checkObjectNull() {

        return mTargetWeakReference.get() == null;
    }

    /**
     * update
     *
     * @param filter 过滤字
     * @param object 发送对象
     * @return target是否还存活
     */
    boolean update(String filter, final O object) {

        T target = mTargetWeakReference.get();
        if (target != null) {

            if (match(filter, object)) {

                call(object);
            }
            return true;
        }
        return false;
    }

    /**
     * 过滤匹配
     *
     * @param filter 匹配字符串
     * @param object post对象
     * @return 结果
     */
    private boolean match(String filter, O object) {

        if (object == null) {

            return false;
        }
        if (TextUtils.isEmpty(filter)) {

            mFilter = RingConstants.RingFilter;
        }
        return filter.equals(mFilter);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof RingObserver) {

            RingObserver ringObserver = (RingObserver) obj;
            return ringObserver.mBusEventClass == mBusEventClass && ringObserver.mFilter.equals(mFilter) && ringObserver.mMethodName.equals(mMethodName);
        }
        return false;
    }

    public abstract void call(O object);

}
