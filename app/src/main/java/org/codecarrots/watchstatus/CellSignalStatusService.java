package org.codecarrots.watchstatus;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * This class is to keep track of cellular signal status.
 * This service runs in background and sends notification when required.
 *
 * @author Dipti Nirmale
 */
public class CellSignalStatusService extends IntentService {

    private static final String LOGTAG = "CellSignalStatusService";
    private static final String NOTIFICATION = "NOTIFICATION";
    private static final String ID = "ID";
    private static final String IDVALUE = "101";

    private String mNotificationMessage;
    private Intent mIntent;
    private CellSignalStatus signalStatus = CellSignalStatus.getInstance();

    public CellSignalStatusService() {
        super(LOGTAG);
    }

    @Override
    public void onCreate() {
        Log.d(LOGTAG, "Creating new service for CellSignalStatus");
        super.onCreate();
        mIntent = new Intent(LOGTAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        signalStatus.setSignalType(this);
        mNotificationMessage = signalStatus.getSignalStatus(this);
        broadcastSignalStatus();
    }

    private void broadcastSignalStatus() {
        mIntent.putExtra(NOTIFICATION, mNotificationMessage);
        mIntent.putExtra(ID, IDVALUE);
        mIntent.setClass(this, NotificationReceiver.class);
        Log.d(LOGTAG, "Broadcasting Cellular Signal Status");
        sendBroadcast(mIntent);
        Log.d(LOGTAG, "Broadcast sent");
    }

}
