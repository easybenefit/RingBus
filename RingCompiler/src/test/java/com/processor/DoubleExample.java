package com.processor;

import com.bus.ring.DispatchPolicy;
import com.bus.ring.Ring;
import com.entity.DoubleEvent;

/**
 * Created by handgunbreak@gmail.com on 16/7/16.
 * Mail: handgunbreak@gmail.com
 * Copyright: handgunbreak@gmail.com(2016)
 * Description:
 */
public class DoubleExample {

    @Ring(filter = "100000", policy = DispatchPolicy.UiPolicy)
    protected void receiveDoubleEvent(DoubleEvent doubleEvent) {

        if (doubleEvent != null) {

            System.out.println(doubleEvent.toString());
        }
    }


}
