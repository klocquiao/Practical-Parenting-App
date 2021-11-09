package com.example.parentsupportapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationAssistant extends ContextWrapper {
    /*
    Referenced notification setup from:
    https://developer.android.com/training/notify-user/build-notification#SimpleNotification
     */

    private static final String CHANNEL_ID_1 = "timerActivityChannel1";
    private static final int REQUEST_CODE_TIMER_ACTIVITY = 0;
    public static final int REQUEST_CODE_NOTIF_BTN_SILENCE = 1;

    public NotificationAssistant(Context base) {
        super(base);

        createNotificationChannel();
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notif_assist_alarm_silence);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_1, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public NotificationManagerCompat createManager() {
        NotificationManagerCompat newManager = NotificationManagerCompat.from(this);
        return newManager;
    }

    public NotificationCompat.Builder createBuilder() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(getString(R.string.notif_assist_timer_comp))
                .setContentText(getString(R.string.notif_assist_notif_text1))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(createPendingIntent())
                .addAction(R.drawable.ic_alarm_off_icon,
                            getString(R.string.notif_assist_alarm_silence),
                            createAlarmSilencePendingIntent());

        return builder;
    }

    private PendingIntent createPendingIntent() {
        Intent timerActivityIntent = TimerActivity.makeIntent(this);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(timerActivityIntent);
        return stackBuilder.getPendingIntent(REQUEST_CODE_TIMER_ACTIVITY, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent createAlarmSilencePendingIntent() {
        Intent silenceAlarmIntent = new Intent(this, TimerBroadcastReceiver.class);
        silenceAlarmIntent.setAction("com.example.parentsupportapp.Broadcast");
        silenceAlarmIntent.putExtra("Silence button ID", 1);
        return PendingIntent.getBroadcast(this, REQUEST_CODE_NOTIF_BTN_SILENCE, silenceAlarmIntent, 0);
    }
}