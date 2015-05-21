package org.codecarrots.watchstatus;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CellSignalStatusService extends Service {
    private static final String LOGTAG = "SignalStatusService";
    private static final String NOTIFICATION = "NOTIFICATION";
    private static final String ID = "ID";

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String mNotificationMessage;
    private Intent mIntent;
    private CellSignalStatus mSignalStatus;
    private Context mContext;

    public CellSignalStatusService() {
        mContext = this;
        mSignalStatus = CellSignalStatus.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(LOGTAG, "Creating new service for CellSignalStatus");
        super.onCreate();
        mIntent = new Intent(LOGTAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
        scheduleStatusCheckForCellSignal();
        return START_NOT_STICKY;
    }

    private void scheduleStatusCheckForCellSignal() {
        final Runnable statusChecker = new Runnable() {
            @Override
            public void run() {
                try {
                    mSignalStatus.setSignalType(mContext);
                    mNotificationMessage = mSignalStatus.getSignalStatus(mContext);
                    broadcastSignalStatus();
                }
                catch (Exception e) {
                    Log.e(LOGTAG, "Exception occurred in Scheduler" + e.getMessage());
                    Log.e(LOGTAG, e.getCause().getMessage() + e.getStackTrace());
                }
            }
        };
/*
//        Lambda expressions: Need source 8 or higher

        final Runnable statusChecker = () -> {
                try {
                    mSignalStatus.setSignalType(mContext);
                    mNotificationMessage = mSignalStatus.getSignalStatus(mContext);
                    broadcastSignalStatus();
                }
                catch (Throwable t) {
                    Log.e(LOGTAG, "Exception occurred in Scheduler" + t.getMessage());
                    Log.e(LOGTAG, "Stack trace " + t.getStackTrace());
                }
        };
*/
        final ScheduledFuture<?> statusCheckerHandle = scheduler.scheduleAtFixedRate(statusChecker, 10, 30, TimeUnit.SECONDS);
/*        try {
            statusCheckerHandle.get();
        }
        catch (Exception e) {
            Log.e(LOGTAG, "Stacktrace " + e.getStackTrace());
        }*/
    }

    private void broadcastSignalStatus() {
        mIntent.putExtra(NOTIFICATION, mNotificationMessage);
        mIntent.putExtra(ID, getString(R.string.notification_id_cell_signal));
        mIntent.setClass(this, NotificationReceiver.class);
        Log.d(LOGTAG, "Broadcasting Cellular Signal Status");
        sendBroadcast(mIntent);
        Log.d(LOGTAG, "Broadcast sent");
    }

    @Override
    public void onDestroy() {
        Log.i(LOGTAG, "Stopping the service for cell signal status");
        scheduler.shutdown();
    }
}
