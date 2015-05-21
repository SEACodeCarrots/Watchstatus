package org.codecarrots.watchstatus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * This class handles events when user clicks on notification.
 * @author Dipti Nirmale
 */
public class NotificationHandlerActivity extends AppCompatActivity {

    private static final String LOGTAG = "NotificationHandler";
    private static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    private int mId;
    private String mMessage;
    private Button mStartActionButton;
    private Button mCloseButton;
    private AlertDialog mAlertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_handler);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mId = intent.getIntExtra(NOTIFICATION_ID, -1);
        Log.d(LOGTAG, "Notification ID: " + mId);
        setMessage();

        TextView messageView = (TextView) findViewById(R.id.message_view);
        messageView.setText(mMessage);

        mStartActionButton = (Button) findViewById(R.id.action_button);
        mCloseButton = (Button) findViewById(R.id.close_button);
        setButtonsStates();
    }

    private void setButtonsStates() {
        if (mId == Integer.parseInt(getString(R.string.notification_id_cell_signal))) {
            mCloseButton.setText("No, Thanks");
            mStartActionButton.setVisibility(View.VISIBLE);
            mStartActionButton.setText("Start Buzzer");
            String[] options = {"10 minutes", "20 minutes", "30 minutes", "1 hour"};
            createAlertDialog(options);
            mStartActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog.show();
                }
            });
        }
        else if (mId == Integer.parseInt(getString(R.string.notification_id_battery))) {
            mCloseButton.setText("No, Thanks");
            mStartActionButton.setVisibility(View.VISIBLE);
            mStartActionButton.setText("Start Battery Saver mode");
        }
        else {
            mStartActionButton.setVisibility(View.GONE);
            mCloseButton.setText("Close");
        }
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void createAlertDialog(String[] options) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        Log.d(LOGTAG, "Alert Dialog Builder is created");
        alertDialogBuilder.setTitle("Buzz phone after every ")
                .setSingleChoiceItems(options, -1, null)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ListView listView = ((AlertDialog) dialog).getListView();
                        Object checkedOption = listView.getAdapter().getItem(listView.getCheckedItemPosition());
                        Log.d(LOGTAG, "Checked Option: " + checkedOption.toString());
                    }
                });
        mAlertDialog = alertDialogBuilder.create();
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

    private void setMessage() {
        if (mId == Integer.parseInt(getString(R.string.notification_id_cell_signal))) {
            mMessage = "\tYou are losing cellular signal. You might like to call or send SMS before that. " +
                    "\n\n\tHow about starting Buzzer? Buzzer will help you finding your phone during emergency.";
        }
        else {
            mMessage = "Oops! Something went wrong. Please Restart the app";
        }
    }
}
