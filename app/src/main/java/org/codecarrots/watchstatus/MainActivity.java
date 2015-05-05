package org.codecarrots.watchstatus;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * This is the launcher activity of this app.
 * User should set their preference here.
 *
 * @author Dipti Nirmale
 */
public class MainActivity extends Activity {

    private static final String LOGTAG = "MainActivity";
    private TextView mTextView;
    private IntentFilter mIntentFilter;
    private NotificationReceiver mNotificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.cellSignalText);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.cellSignalToggle);

        mNotificationReceiver = new NotificationReceiver();
        mIntentFilter = new IntentFilter(NotificationReceiver.ACTION);
        mIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        registerReceiver(mNotificationReceiver, mIntentFilter);
    }

    public void onToggleClicked_cellSignal(View view) {
        if (((ToggleButton) view).isChecked()) {
            Log.d(LOGTAG, "CellSignalToggle is ON");
            startService(new Intent(this, CellSignalStatusService.class));
        }
        else {
            Log.d(LOGTAG, "CellSignalToggle is OFF");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mNotificationReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNotificationReceiver);
    }

}
