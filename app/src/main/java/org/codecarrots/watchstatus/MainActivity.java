package org.codecarrots.watchstatus;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * This is the launcher activity of this app.
 * User should set their preference here.
 *
 * @author Dipti Nirmale
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOGTAG = "MainActivity";
    private static final String SIGNAL_TOGGLE_BUTTON_STATE = "signal_toggle_button_state";
    private static final String BATTERY_TOGGLE_BUTTON_STATE = "battery_toggle_button_state";

    private static Bundle bundle = new Bundle();

    private TextView mTextView;
    private IntentFilter mIntentFilter;
    private NotificationReceiver mNotificationReceiver;
    private ToggleButton signalToggle;
    private ToggleButton batteryToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTextView = (TextView) findViewById(R.id.cell_signal_text);
        signalToggle = (ToggleButton) findViewById(R.id.cell_signal_toggle);
        batteryToggle = (ToggleButton) findViewById(R.id.battery_toggle);

        setContentView(R.layout.activity_main);

        mNotificationReceiver = new NotificationReceiver();
        mIntentFilter = new IntentFilter(NotificationReceiver.ACTION);
        mIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        registerReceiver(mNotificationReceiver, mIntentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onToggleClicked_cellSignal(View view) {
        Intent cellSignalStatusService = new Intent(this, CellSignalStatusService.class);
        if (((ToggleButton) view).isChecked()) {
            Log.d(LOGTAG, "CellSignalToggle is ON");
            startService(cellSignalStatusService);
        }
        else {
            stopService(cellSignalStatusService);
            Log.d(LOGTAG, "CellSignalToggle is OFF");
        }
    }

    public void onToggleClicked_battery(View view) {
        Intent batteryStatusService = new Intent(this, BatteryStatusService.class);
        if (((ToggleButton) view).isChecked()) {
            Log.d(LOGTAG, "BatteryToggle is ON");
            startService(batteryStatusService);
        }
        else {
            Log.d(LOGTAG, "BatteryToggle is OFF");
            stopService(batteryStatusService);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onResume() {
        super.onResume();
        signalToggle = (ToggleButton) findViewById(R.id.cell_signal_toggle);
        signalToggle.setChecked(bundle.getBoolean(SIGNAL_TOGGLE_BUTTON_STATE, false));

        batteryToggle = (ToggleButton) findViewById((R.id.battery_toggle));
        batteryToggle.setChecked(bundle.getBoolean(BATTERY_TOGGLE_BUTTON_STATE, false));
        registerReceiver(mNotificationReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bundle.putBoolean(SIGNAL_TOGGLE_BUTTON_STATE, signalToggle.isChecked());
        bundle.putBoolean(BATTERY_TOGGLE_BUTTON_STATE, batteryToggle.isChecked());
        unregisterReceiver(mNotificationReceiver);
    }

}
