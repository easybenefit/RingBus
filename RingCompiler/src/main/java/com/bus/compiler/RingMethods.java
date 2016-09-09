package com.bus.compiler;

import com.bus.ring.RingConstants;

import java.util.List;
import java.util.Locale;

/**
 * Created by handgunbreak@gmail.com on 16/7/16.
 * Mail: handgunbreak@gmail.com
 * Copyright: handgunbreak@gmail.com(2016)
 * Description: 出现@Ring注解的绑定对象及注解方法信息
 */
public class RingMethods {


    //RingBus.bind(Target target),bind的绑定对象信息
    String mBindTargetClassName;

    String mBindTargetPackageName;

    //@Ring注解的方法信息
    List<RingMethodInfo> mRingMethodInfo;


    RingMethods(String targetClassName, String targetPackageName) {

        mBindTargetClassName = targetClassName;
        mBindTargetPackageName = targetPackageName;
    }

    String getBoundClassName() {

        return String.format(Locale.getDefault(), "%s%s", mBindTargetClassName, RingConstants.Ring_SUFFIX);
    }
}
