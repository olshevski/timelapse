package me.olshevski.timelapse;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ForegroundService extends Service {

    private static final int SERVICE_NOTIFICATION_ID = 1;

    private static final String ACTION_START_FOREGROUND = "start_foreground";
    private static final String ACTION_START = "start";
    private static final String ACTION_PLUS = "plus";
    private static final String ACTION_MINUS = "minus";

    private NotificationManager notificationManager;
    private TimelapseManager timelapseManager;

    public static void startForeground(Context context) {
        Intent intent = new Intent(context, ForegroundService.class);
        intent.setAction(ACTION_START_FOREGROUND);
        context.startService(intent);
    }

    public static void stopForeground(Context context) {
        Intent intent = new Intent(context, ForegroundService.class);
        context.stopService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        timelapseManager = ((MyApplication) getApplication()).getTimelapseManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            // START_STICKY restart case
            startForeground();
        } else {
            String action = intent.getAction();
            // noinspection ConstantConditions
            switch (action) {
                case ACTION_START_FOREGROUND:
                    startForeground();
                    break;
                case ACTION_START:
                    handleRunAction();
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timelapseManager.isStarted()) {
            timelapseManager.stop();
        }
    }

    private void startForeground() {
        Notification notification = createNotification();
        startForeground(SERVICE_NOTIFICATION_ID, notification);
    }

    private void handleRunAction() {
        if (timelapseManager.isStarted()) {
            timelapseManager.stop();
        } else {
            timelapseManager.start();
        }
        updateNotification();
    }

    private void updateNotification() {
        Notification notification = createNotification();
        notificationManager.notify(SERVICE_NOTIFICATION_ID, notification);
    }

    private Notification createNotification() {
        Notification.Builder notificationBuilder =
                new Notification.Builder(this)
                        .setContentTitle("testTitle")
                        .setContentText("message")
                        .setSmallIcon(timelapseManager.isStarted()
                                ? R.drawable.ic_play_circle_outline_white_24dp
                                : R.drawable.ic_timelapse_white_24dp)
                        .setPriority(Notification.PRIORITY_DEFAULT);
        addAction(notificationBuilder, ACTION_START,
                timelapseManager.isStarted() ? "stop" : "start");
        addAction(notificationBuilder, ACTION_MINUS, "-1 sec");
        addAction(notificationBuilder, ACTION_PLUS, "+1 sec");
        return notificationBuilder.build();
    }

    private void addAction(Notification.Builder notificationBuilder, String intentAction,
            String title) {
        Intent intent = new Intent(this, ForegroundService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        Notification.Action action = new Notification.Action.Builder(0, title,
                pendingIntent).build();
        notificationBuilder.addAction(action);
    }

}