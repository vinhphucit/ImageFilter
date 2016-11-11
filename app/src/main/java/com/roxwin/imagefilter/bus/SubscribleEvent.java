package com.roxwin.imagefilter.bus;

import android.os.Bundle;

/**
 * Created by PhucTV on 6/2/15.
 */
public class SubscribleEvent {
    //EVENT
    public static final String AUDIO_PLAYING = "AUDIO_PLAYING";
    public static final String AUDIO_STOPPED = "AUDIO_STOPPED";
    public static final String PAUSE_THE_AUDIO = "PAUSE_THE_AUDIO";

    public static final String UPGRADE_REQUIRED_EVENT = "UPGRADE_REQUIRED_EVENT";
    public static final String FAILED_AUTO_LOGIN_EVENT = "FAILED_AUTO_LOGIN_EVENT";

    public static final String NETWORK_CONNECTED_EVENT = "NETWORK_CONNECTED_EVENT";
    public static final String OP_WAIT_SETTINGS = "OP_WAIT_SETTINGS";

    //Data Name
    public static final String EX_AUDIO_ID = "mAudioId";

    public String event;
    public Bundle data;

    public SubscribleEvent(String event, Bundle data) {
        this.event = event;
        this.data = data;
    }
}
