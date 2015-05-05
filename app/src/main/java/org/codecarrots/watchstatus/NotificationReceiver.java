package org.codecarrots.watchstatus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String LOGTAG = "NotificationReceiver";
    public static final String ACTION = "org.codecarrots.watchstatus.MainActivity.NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "Broadcast received");
        Intent activityIntent = new Intent(intent);
        activityIntent.setClass(context, NotificationActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}