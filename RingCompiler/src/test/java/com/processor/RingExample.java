package com.processor;

import com.bus.ring.Ring;
import com.entity.DoubleEvent;

/**
 * Created by handgunbreak@gmail.com on 16/7/16.
 * Mail: handgunbreak@gmail.com
 * Copyright: handgunbreak@gmail.com(2016)
 * Description:
 */
public class RingExample {

    @Ring
    public void receivePackage(String event) {

        if (event != null) {

            System.out.println(event);
            System.out.println("2...");
        }
    }

    @Ring
    protected void receiveDoubleEvent(DoubleEvent doubleEvent) {

        if (doubleEvent != null) {

            System.out.println(doubleEvent.toString());
        }
    }
}
