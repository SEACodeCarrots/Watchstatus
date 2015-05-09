package org.codecarrots.watchstatus;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
    private CellSignalStatus mSignalStatus;
    private Context mContext;

    public CellSignalStatusService() {
        super(LOGTAG);
        mContext = this;
        mSignalStatus = CellSignalStatus.getInstance();
    }

    @Override
    public void onCreate() {
        Log.d(LOGTAG, "Creating new service for CellSignalStatus");
        super.onCreate();
        mIntent = new Intent(LOGTAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        scheduleStatusCheckForCellSignal();
    }

    private void scheduleStatusCheckForCellSignal() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final Runnable statusChecker = new Runnable() {
            @Override
            public void run() {
                mSignalStatus.setSignalType(mContext);
                mNotificationMessage = mSignalStatus.getSignalStatus(mContext);
                broadcastSignalStatus();
            }
        };
        final ScheduledFuture<?> statusCheckerHandle = scheduler.scheduleAtFixedRate(statusChecker, 10, 30, TimeUnit.SECONDS);
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
