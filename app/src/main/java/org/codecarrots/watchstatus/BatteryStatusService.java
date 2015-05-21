package org.codecarrots.watchstatus;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The service to schedule watchstatus for battery.
 * The service is to be started/stopped from launcher activity.
 * @author Dipti Nirmale
 */
public class BatteryStatusService extends Service {
    private static final String LOGTAG = "BatteryStatusService";
    private static final String NOTIFICATION = "NOTIFICATION";
    private static final String ID = "ID";

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String mNotificationMessage;
    private Intent mIntent;
    private BatteryStatus mBatteryStatus;
    private Context mContext;

    public BatteryStatusService() {
        mContext = this;
        mBatteryStatus = BatteryStatus.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(LOGTAG, "Creating new service for BatteryStatus");
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
                    mNotificationMessage = mBatteryStatus.getBatteryStatus(mContext);
                    broadcastSignalStatus();
                }
                catch (Exception e) {
                    Log.e(LOGTAG, "Exception occurred in Scheduler" + e.getMessage());
                    Log.e(LOGTAG, e.getCause().getMessage() + e.getStackTrace());
                }
            }
        };

        final ScheduledFuture<?> statusCheckerHandle = scheduler.scheduleAtFixedRate(statusChecker, 30, 120, TimeUnit.SECONDS);
    }

    private void broadcastSignalStatus() {
        mIntent.putExtra(NOTIFICATION, mNotificationMessage);
        mIntent.putExtra(ID, getString(R.string.notification_id_battery));
        mIntent.setClass(this, NotificationReceiver.class);
        Log.d(LOGTAG, "Broadcasting Battery Status");
        sendBroadcast(mIntent);
        Log.d(LOGTAG, "Broadcast sent");
    }

    @Override
    public void onDestroy() {
        Log.i(LOGTAG, "Stopping the service for BatteryStatus");
        scheduler.shutdown();
    }
}
