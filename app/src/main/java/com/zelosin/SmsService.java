package com.zelosin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

class SmsService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifier.showNotification(getApplicationContext(), getSystemService(NOTIFICATION_SERVICE),
                intent.getExtras().getString("sms_body"), "Chel");
        return START_STICKY;
    }
}
