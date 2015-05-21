package org.codecarrots.watchstatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * This class handles events when user clicks on notification.
 * @author Dipti Nirmale
 */
public class NotificationHandlerActivity extends AppCompatActivity {

    private static final String LOGTAG = "NotificationHandler";
    private static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    private String mMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_handler);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int id = intent.getIntExtra(NOTIFICATION_ID, -1);
        Log.d(LOGTAG, "Notification ID: " + id);
        getMessage(id);

        TextView messageView = (TextView) findViewById(R.id.message_view);
        messageView.setText(mMessage);

        Button startBuzzerButton = (Button) findViewById(R.id.start_buzzer_button);
        Button noThanksButton = (Button) findViewById(R.id.no_thanks_button);

        noThanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
