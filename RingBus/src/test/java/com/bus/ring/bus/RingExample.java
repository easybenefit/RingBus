package com.bus.ring.bus;

import com.bus.ring.Ring;

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
            System.out.println("222");
        }
    }


}
