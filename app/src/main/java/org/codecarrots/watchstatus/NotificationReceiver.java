package org.codecarrots.watchstatus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This class is Broadcast Receiver for notifications sent by Status Services.
 * It forwards the received content to NotificationActivity for further actions.
 *
 * @author Dipti Nirmale
 */
public class NotificationReceiver extends BroadcastReceiver {

    private static final String LOGTAG = "NotificationReceiver";
    public static final String ACTION = "org.codecarrots.watchstatus.NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "Broadcast received");
        Intent serviceIntent = new Intent(intent);
        serviceIntent.setClass(context, NotificationService.class);
        serviceIntent.setAction("org.codecarrots.watchstatus.NotificationService");
        context.startService(serviceIntent);
    }
}