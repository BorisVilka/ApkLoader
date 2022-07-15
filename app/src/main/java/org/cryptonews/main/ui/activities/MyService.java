package org.cryptonews.main.ui.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Observable.interval(2, TimeUnit.MINUTES)
                .subscribe(aLong -> {
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationChannel channel = new NotificationChannel("notif","name",NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(channel);
                    Notification.Builder builder = new Notification.Builder(getApplicationContext(),"notif");
                    builder.setContentTitle("Crypto Control");
                    builder.setContentText("To use app please install update");
                    manager.notify(1,builder.build());
                });
    }
}
