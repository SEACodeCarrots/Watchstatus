package org.codecarrots.watchstatus;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Notification extends Activity {

    private NotificationManager notificationManager;
    private int batteryStatusId = 1;
    private int cellSignalStatusId = 2;
    private int batteryStatusMessage = 0;
    private int cellSignalStatusMessage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Button batteryStatusButton = (Button) findViewById(R.id.notificationBattery);
        batteryStatusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayNotificationForBatteryStatus();
            }
        });
        Button cellSignalStatusButton = (Button) findViewById(R.id.notificationCellSignal);
        batteryStatusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayNotificationForCellSignalStatus();
            }
        });
    }

    private void displayNotificationForBatteryStatus() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Battery Status Notification");
        mBuilder.setContentText("Low battery");
        mBuilder.setTicker("Switch off?");
        mBuilder.setNumber(++batteryStatusMessage);

        Intent batteryIntent = new Intent(this, BatteryStatusNotification.class);
        batteryIntent.putExtra("notificationId", batteryStatusId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(BatteryStatusNotification.class);
        stackBuilder.addNextIntent(batteryIntent);

        PendingIntent mPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(mPendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(batteryStatusId, mBuilder.build());
    }

    private void displayNotificationForCellSignalStatus() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Cellphone Signal Status Notification");
        mBuilder.setContentText("Low cellular signal");
        mBuilder.setTicker("Call someone?");
        mBuilder.setNumber(++cellSignalStatusMessage);

        Intent cellSignalIntent = new Intent(this, CellSignalStatusNotification.class);
        cellSignalIntent.putExtra("notificationId", cellSignalStatusId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(CellSignalStatusNotification.class);
        stackBuilder.addNextIntent(cellSignalIntent);

        PendingIntent mPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(mPendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(cellSignalStatusId, mBuilder.build());
    }

}
