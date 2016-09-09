package com.bus.ring;

import android.text.TextUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by handgunbreak@gmail.com on 16/7/17.
 * Mail: handgunbreak@gmail.com
 * Copyright: handgunbreak@gmail.com(2016)
 * Description:
 */
@SuppressWarnings({"unchecked", "unused"})
public class RingSubscriber {

    //缓存
    private static final Map<Class<?>, RingBind<Object>> BINDERS = new LinkedHashMap();
    private static final RingBind<Object> NOP_RING_BINDER = new RingBind<Object>() {

        public void bind(Object var1) {

        }

        public void unbind(Object var1) {

        }
    };
    //debug模式设置
    private static boolean debug = false;

    public static void setDebug(boolean debug) {

        RingSubscriber.debug = debug;
    }

    /**
     * bind target object
     *
     * @param object object
     */
    public static void subscribe(Object object) {

        try {

            Class clazz = object.getClass();
            RingBind<Object> binder = findRingBinderForClass(clazz);
            binder.bind(object);
        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    /**
     * unbind target object
     *
     * @param object object
     */
    public static void unSubscribe(Object object) {

        try {

            Class clazz = object.getClass();
            RingBind<Object> binder = findRingBinderForClass(clazz);
            binder.unbind(object);
        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    /**
     * 查询绑定对象，即查找由RingSubscriber.subscribe(this)所生成的新类
     *
     * @param clazz 类名
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static RingBind<Object> findRingBinderForClass(Class<?> clazz) throws IllegalAccessException, InstantiationException {

        RingBind ringBinder = BINDERS.get(clazz);
        if (ringBinder != null) {

            if (debug) {

                System.out.println("RingSubscriber: " + "HIT: Cached in RingBus subscribe map.");
            }

            return ringBinder;
        } else {

            String clazzName = clazz.getName();
            if (!clazzName.startsWith("android.") && !clazzName.startsWith("java.")) {

                try {

                    Class bindingClazz = Class.forName(clazzName + RingConstants.Ring_SUFFIX);
                    ringBinder = (RingBind) bindingClazz.newInstance();
                    if (debug) {

                        System.out.println("RingSubscriber: " + "HIT: Loaded RingBus binder class.");
                    }
                } catch (ClassNotFoundException var4) {

                    if (debug) {

                        System.out.println("RingSubscriber: " + "Not found. Trying superclass " + clazz.getSuperclass().getName());
                    }

                    ringBinder = findRingBinderForClass(clazz.getSuperclass());
                }

                BINDERS.put(clazz, ringBinder);
                return ringBinder;
            } else {

                if (debug) {

                    System.out.println("RingSubscriber: " + "MISS: Reached framework class. Abandoning search.");
                }

                return NOP_RING_BINDER;
            }
        }
    }


    /**
     * 发布
     *
     * @param object 发布内容
     */
    public static void ringCall(Object object) {

        ringCall(RingConstants.RingFilter, object);
    }

    /**
     * 发布
     *
     * @param filter 过滤器
     * @param object 发布内容
     */
    public static void ringCall(String filter, Object object) {

        RingBus.getInstance().post(TextUtils.isEmpty(filter) ? RingConstants.RingFilter : filter, object);
    }
}
