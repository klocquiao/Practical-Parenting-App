package com.example.parentsupportapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimerBroadcastReceiver extends BroadcastReceiver {
    /*
    Basic class that extends BroadcastReceiver, allowing user to stop the alarm from the notification.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("broadcast", "Broadcast received for notification button handling");
        TimerActivity.stopRingtone();
    }
}
