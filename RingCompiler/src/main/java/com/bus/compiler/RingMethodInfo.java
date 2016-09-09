package com.bus.compiler;

import com.bus.ring.DispatchPolicy;

/**
 * Created by handgunbreak@gmail.com on 16/7/16.
 * Mail: handgunbreak@gmail.com
 * Copyright: handgunbreak@gmail.com(2016)
 * Description: 出现@Ring注解的注解方法信息
 */
class RingMethodInfo {

    //Action名
    String mFilter;

    //回调方法执行线程
    DispatchPolicy mDispatchPolicy;

    //方法名
    String mMethodName;

    //方法参数名
    String mParamName;

    //方法参数类名
    String mParamCanonicalName;

}
