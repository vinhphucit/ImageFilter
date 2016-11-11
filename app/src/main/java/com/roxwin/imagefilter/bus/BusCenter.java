package com.roxwin.imagefilter.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by PhucTV on 5/27/15.
 */
public class BusCenter {
    private static Bus _INSTANCE;

    public BusCenter() {
    }

    public static Bus getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new Bus(ThreadEnforcer.MAIN);
        }
        return _INSTANCE;
    }

}
