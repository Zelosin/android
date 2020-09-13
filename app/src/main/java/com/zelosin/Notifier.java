package com.zelosin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

class Notifier {

    public static void showNotification(Context context, Object notificationService,  String text, String title) {
        Intent resultIntent = new Intent(context, LoginInActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setContentText(text);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) notificationService;
        notificationManager.notify(1, notification);
    }

}
