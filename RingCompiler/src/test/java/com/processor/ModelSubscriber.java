package com.processor;

import com.bus.ring.DispatchPolicy;
import com.bus.ring.Ring;
import com.bus.ring.RingSubscriber;
import com.entity.DoubleEvent;

/**
 * Created by handgunbreak@gmail.com on 16/7/17.
 * Mail: handgunbreak@gmail.com
 * Copyright: handgunbreak@gmail.com(2016)
 * Description:
 */
public class ModelSubscriber {

    public ModelSubscriber() {

        RingSubscriber.subscribe(this);
    }

    public static void mian(String... args) {

        ModelSubscriber modelSubscriber = new ModelSubscriber();

    }

    public void onDestory() {

        RingSubscriber.unSubscribe(this);
    }
    @Ring(filter = "10245")
    public void onRefresh10245(DoubleEvent doubleEvent) {

        if (doubleEvent != null) {

            System.out.println(doubleEvent.toString());
            System.out.println(Thread.currentThread().toString());
        }
    }

    @Ring(filter = "10246", policy = DispatchPolicy.UiPolicy)
    public void onRefresh10246(DoubleEvent doubleEvent) {

        if (doubleEvent != null) {

            System.out.println(doubleEvent.toString());
            System.out.println(Thread.currentThread().toString());
        }
    }

    @Ring(filter = "10247", policy = DispatchPolicy.ThreadPolicy)
    public void onRefresh10247(DoubleEvent doubleEvent) {

        if (doubleEvent != null) {

            System.out.println(doubleEvent.toString());
            System.out.println(Thread.currentThread().toString());
        }
    }
}
