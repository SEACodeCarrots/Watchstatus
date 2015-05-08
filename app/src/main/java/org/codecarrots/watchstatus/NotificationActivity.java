package org.codecarrots.watchstatus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This activity sends notification to the user forwarded by NotificationReceiver.
 *
 * @author Dipti Nirmale
 */
public class NotificationActivity extends ActionBarActivity {

    private static final String LOGTAG = "NotificationActivity";
    private static NotificationActivity instance = new NotificationActivity();

    public static NotificationActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      setContentView(R.layout.activity_notification);

        Log.d(LOGTAG, "Notification Activity created");

        sendNotification(getApplicationContext(), getIntent());
    }
    private void sendNotification(Context context, Intent intent) {
        String message = intent.getStringExtra("NOTIFICATION");
        int id = Integer.parseInt(intent.getStringExtra("ID"));
        Log.d(LOGTAG, "Preparing to send notification");

        String notificationTitle = context.getText(R.string.app_name).toString();

        NotificationCompat.Builder statusUpdate = new NotificationCompat.Builder(context)
                .setContentTitle(notificationTitle)
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.sym_def_app_icon);

        Intent notificationIntent = new Intent(context, CellSignalStatusHandler.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(CellSignalStatusHandler.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        statusUpdate.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Log.d(LOGTAG, "Sending notification with ID: " + id);
        notificationManager.notify(id, statusUpdate.build());
    }

}
