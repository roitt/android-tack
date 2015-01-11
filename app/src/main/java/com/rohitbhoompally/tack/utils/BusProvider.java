package com.rohitbhoompally.tack.utils;

import com.squareup.otto.Bus;

/**
 * Created by bearcatmobile on 1/9/15.
 */
public class BusProvider {
    public static Bus bus;

    private  BusProvider() {
    }

    public static Bus getInstance(){
        if(bus == null)
            bus = new Bus();
        return bus;
    }
}
