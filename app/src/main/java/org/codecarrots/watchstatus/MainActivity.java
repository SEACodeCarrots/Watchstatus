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
    private static final String SIGNALTOGGLESTATE = "signal_toggle_button_state";

    private static Bundle bundle = new Bundle();

    private TextView mTextView;
    private IntentFilter mIntentFilter;
    private NotificationReceiver mNotificationReceiver;
    private ToggleButton signalToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTextView = (TextView) findViewById(R.id.cellSignalText);
        signalToggle = (ToggleButton) findViewById(R.id.cellSignalToggle);

        setContentView(R.layout.activity_main);

        mNotificationReceiver = new NotificationReceiver();
        mIntentFilter = new IntentFilter(NotificationReceiver.ACTION);
        mIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        registerReceiver(mNotificationReceiver, mIntentFilter);
    }

    public void onToggleClicked_cellSignal(View view) {
        Intent cellSignalStatusService = new Intent(this, SignalStatusService.class);
        if (((ToggleButton) view).isChecked()) {
            Log.d(LOGTAG, "CellSignalToggle is ON");
            startService(cellSignalStatusService);
        }
        else {
            stopService(cellSignalStatusService);
            Log.d(LOGTAG, "CellSignalToggle is OFF");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onResume() {
        super.onResume();
        signalToggle = (ToggleButton) findViewById(R.id.cellSignalToggle);
        signalToggle.setChecked(bundle.getBoolean(SIGNALTOGGLESTATE, false));
        registerReceiver(mNotificationReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bundle.putBoolean(SIGNALTOGGLESTATE, signalToggle.isChecked());
        unregisterReceiver(mNotificationReceiver);
    }

}
