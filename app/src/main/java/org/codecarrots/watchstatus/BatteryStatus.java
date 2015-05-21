package org.codecarrots.watchstatus;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

/**
 * This Singleton class maintains the required information related to battery of the Android device.
 * @author Dipti Nirmale
 */
public class BatteryStatus {
    private static final String LOGTAG = "BatteryStatus";
    private static BatteryStatus instance = new BatteryStatus();

    private BatteryStatus() { }
    public static BatteryStatus getInstance() {
        return instance;
    }

    public String getBatteryStatus(Context context){
        Log.d(LOGTAG, "Getting battery status");

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);

        StringBuilder statusMessage = new StringBuilder("BATTERY STATUS: ");

        //are we charging / charged?
        int chargingStatus = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = (chargingStatus == BatteryManager.BATTERY_STATUS_CHARGING
                || chargingStatus == BatteryManager.BATTERY_STATUS_FULL);

        if (isCharging) {
            statusMessage.append("CHARGING ");

            //how are we charging
            int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_USB);
            boolean acCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_AC);

            if (usbCharge) {
                statusMessage.append("through USB.  ");
            }

            if (acCharge) {
                statusMessage.append("through Electrical Outlet. ");
            }
        }
        else {
            Log.d(LOGTAG, "Not charging");
        }

        //get battery level
        int batteryLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        statusMessage.append(" Battery Level is " + batteryLevel + " Percent");

        //get battery temperature
        /*int batteryTemperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        statusMessage.append(" Battery Temperature is " + batteryTemperature);*/

        //get battery voltage
        /*int voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        statusMessage.append(" Battery Voltage is " + voltage);*/

        return statusMessage.toString();

    }
}
