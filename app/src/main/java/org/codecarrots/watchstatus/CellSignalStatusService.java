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
    private Intent mIntent;

//  private static CellSignalStatusService instance = new CellSignalStatusService();

    public CellSignalStatusService() {
        super(LOGTAG);
    }

/*
    public CellSignalStatusService getInstance() {
        return instance;
    }
*/

    @Override
    public void onCreate() {
        Log.d(LOGTAG, "Creating new service for CellSignalStatus");
        super.onCreate();
        mIntent = new Intent(LOGTAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        broadcastSignalStatus();
    }

    private void broadcastSignalStatus() {
        mIntent.putExtra("NOTIFICATION", "Notification from CellSignalStatus");
        mIntent.putExtra("ID", "101");
        mIntent.setClass(this, NotificationReceiver.class);
        Log.d(LOGTAG, "Broadcasting Cellular Signal Status");
        sendBroadcast(mIntent);
        Log.d(LOGTAG, "Broadcast sent");
    }

}
