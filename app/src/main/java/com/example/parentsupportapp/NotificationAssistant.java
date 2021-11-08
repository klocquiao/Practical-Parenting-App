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
    In order to have access to the context and thus have access to functions like getString from
    the project resources, we need to extends ContextWrapper. Reasoning for this found at following
    link: https://stackoverflow.com/questions/10641144/difference-between-getcontext-getapplicationcontext-getbasecontext-and

    Notifications were setup using the developers.android tutorial at following link:
    https://developer.android.com/training/notify-user/build-notification#SimpleNotification
     */

    private static final String CHANNEL_ID_1 = "timerActivityChannel1";

    public NotificationAssistant(Context base) {
        super(base);

        createNotificationChannel();
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
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
                .setContentTitle("Timer Complete!")
                .setContentText("You may free the child O_O")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true);

        return builder;
    }

    public PendingIntent createPendingIntent() {
        Intent timerActivityIntent = TimerActivity.makeIntent(this);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(timerActivityIntent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
