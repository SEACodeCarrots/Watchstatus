package org.codecarrots.watchstatus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


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

        Notification statusUpdate = new Notification();
        statusUpdate.icon = android.R.drawable.sym_def_app_icon;
        statusUpdate.tickerText = context.getText(R.string.app_name);
        statusUpdate.when = System.currentTimeMillis();

        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        statusUpdate.setLatestEventInfo(context, context.getText(R.string.app_name).toString(), message, pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Log.d(LOGTAG, "Sending notification with ID: " + id);
        notificationManager.notify(id, statusUpdate);
    }

}
