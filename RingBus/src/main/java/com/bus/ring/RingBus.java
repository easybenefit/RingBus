package com.bus.ring;

import android.text.TextUtils;
import android.util.Log;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by handgunbreak@gmail.com on 16/7/6.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2016)
 * Description: 总线实现
 */
public class RingBus {

    private static final String TAG = "RingBus";

    private static volatile RingBus mRingBus;

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<RingObserver>> mConcurrentRingObserverHasMap;

    /**
     * Constructor
     */
    private RingBus() {

        mConcurrentRingObserverHasMap = new ConcurrentHashMap<>();
    }

    /**
     * @return RingBus
     */
    public static RingBus getInstance() {

        RingBus ringBus = mRingBus;
        if (ringBus == null) {

            synchronized (RingBus.class) {

                ringBus = mRingBus;
                if (ringBus == null) {

                    ringBus = new RingBus();
                    mRingBus = ringBus;
                }
            }
        }
        return ringBus;
    }

    /**
     * 订阅
     *
     * @param object        宿主对象
     * @param ringObservers subject
     */
    public static void register(Object object, List<RingObserver> ringObservers) {

        if (ringObservers != null && ringObservers.size() > 0) {

            for (RingObserver _ringObserver : ringObservers) {

                getInstance().subscribe(object, _ringObserver);
            }
        }
    }

    /**
     * 订阅
     *
     * @param object       宿主对象
     * @param ringObserver subject
     */
    public static void register(Object object, RingObserver ringObserver) {

        if (ringObserver != null) {

            getInstance().subscribe(object, ringObserver);
        }
    }

    /**
     * 取消订阅
     *
     * @param object 宿主
     */
    public static void unregister(Object object) {

        unregister(object, null);
    }

    /**
     * 取消订阅
     *
     * @param object 宿主
     * @param filter 过滤器
     */
    public static void unregister(Object object, String filter) {

        getInstance().unSubscribe(object, filter);
    }

    /**
     * 订阅
     *
     * @param object       宿主
     * @param ringObserver subject
     */
    private void subscribe(Object object, RingObserver ringObserver) {

        if (ringObserver == null || TextUtils.isEmpty(ringObserver.mMethodName) || ringObserver.mBusEventClass == null) {

            return;
        }
        CopyOnWriteArrayList<RingObserver> ringObservers = mConcurrentRingObserverHasMap.get(object.toString());
        if (ringObservers == null) {

            ringObservers = new CopyOnWriteArrayList<>();
            ringObservers.add(ringObserver);
            mConcurrentRingObserverHasMap.put(object.toString(), ringObservers);
        } else if (!hasRegistered(ringObservers, ringObserver)) {

            ringObservers.add(ringObserver);
        }
    }

    /**
     * 判断是否已经注册过
     *
     * @param ringObservers 订阅列表
     * @param ringObserver  新订阅
     * @return result
     */
    private boolean hasRegistered(List<RingObserver> ringObservers, RingObserver ringObserver) {

        for (RingObserver _ringObserver : ringObservers) {

            if (_ringObserver.equals(ringObserver)) {

                return true;
            }
        }
        return false;
    }

    /**
     * 取消订阅
     *
     * @param object 宿主
     * @param filter 过滤器
     */
    private void unSubscribe(Object object, String filter) {

        if (object != null) {

            List<RingObserver> ringObservers = mConcurrentRingObserverHasMap.get(object.toString());
            if (ringObservers != null) {

                int index = 0;
                int size = ringObservers.size();
                boolean removeAll = TextUtils.isEmpty(filter);
                for (RingObserver ringObserver : ringObservers) {

                    if (removeAll || ringObserver.mFilter.equals(filter)) {

                        ringObservers.remove(ringObserver);
                        Log.i(TAG, String.format(Locale.getDefault(), "unSubscribe: (method: %s; filter: %s) size(%d/%d) ", ringObserver.mMethodName, ringObserver.mFilter, index++, (size - 1)));
                    }
                }
                if (removeAll) {

                    mConcurrentRingObserverHasMap.remove(object.toString());
                }
            }
        }
    }

    /**
     * 发布对象
     *
     * @param filter 过滤器
     * @param object 对象
     */
    @SuppressWarnings("unchecked")
    void post(final String filter, final Object object) {

        if (object == null) {

            return;
        }
        Collection<CopyOnWriteArrayList<RingObserver>> values = mConcurrentRingObserverHasMap.values();
        for (List<RingObserver> ringObservers : values) {

            for (final RingObserver ringObserver : ringObservers) {

                if (!ringObserver.checkObjectNull() && ringObserver.mBusEventClass == object.getClass()) {

                    BusDispatcher.getInstance().dispatch(ringObserver.mDispatchPolicy, new Runnable() {

                        @Override
                        public void run() {

                            boolean result = ringObserver.update(filter, object);
                            if (!result) {

                                unSubscribe(object, filter);
                                Log.i(TAG, "target is destroying");
                            }
                        }
                    });
                } else {

                    unSubscribe(object, filter);
                }
            }
            if (ringObservers.size() == 0) {

                unSubscribe(object, null);
            }
        }
    }


}
