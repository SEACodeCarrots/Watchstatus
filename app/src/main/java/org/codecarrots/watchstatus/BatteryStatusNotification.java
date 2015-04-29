package org.codecarrots.watchstatus;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.net.URI;

/**
 * Created by dipti on 4/28/15.
 */
public class BatteryStatusNotification extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batterystatusnotification);

        String output = "Inside the activity of notificationBattery";
        TextView dataIntent = (TextView) findViewById(R.id.text2);

        Uri url = getIntent().getData();
        Bundle extras = getIntent().getExtras();

        output = output + url.toString();

        if (extras != null) {
            output = output + " from " + extras.get("from");
        }
        dataIntent.setText(output);
    }
}
