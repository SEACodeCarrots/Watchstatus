package org.codecarrots.watchstatus;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by dipti on 4/28/15.
 */
class CellSignalStatusNotification extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellsignalstatusnotification);
        CharSequence charSq = "Inside the activity of notificationCellSignal";
        int id = 0;

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            charSq = "error";
        }
        else {
            id = extras.getInt("notificationId");
        }
        TextView t = (TextView) findViewById(R.id.text1);
        charSq = charSq + "with id= " + id;
        t.setText(charSq);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}

