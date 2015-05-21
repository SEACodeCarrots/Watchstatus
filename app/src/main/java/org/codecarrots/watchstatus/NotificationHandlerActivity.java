package org.codecarrots.watchstatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


/**
 * This class handles events when user clicks on notification.
 * @author Dipti Nirmale
 */
public class NotificationHandlerActivity extends Activity {

    private static final String LOGTAG = "NotificationHandler";
    private static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    private String mMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_handler);

        Intent intent = getIntent();
        int id = intent.getIntExtra(NOTIFICATION_ID, -1);
        Log.d(LOGTAG, "Notification ID: " + id);
        getMessage(id);

        TextView messageView = (TextView) findViewById(R.id.message_view);
        messageView.setText(mMessage);
    }

    private void getMessage(int receivedId) {
        if (receivedId == Integer.parseInt(getString(R.string.notification_id_cell_signal))) {
            mMessage = "\tYou are losing cellular signal. You might like to call or send SMS before that. " +
                    "\n\n\tHow about starting Buzzer? Buzzer will help you finding your phone during emergency.";
        }
        else {
            mMessage = "Oops! Something went wrong. Please Restart the app";
        }
    }
}
