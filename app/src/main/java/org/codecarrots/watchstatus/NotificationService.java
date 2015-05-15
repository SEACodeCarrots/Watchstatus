package org.codecarrots.watchstatus;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
public class NotificationService extends IntentService {

    private static final String LOGTAG = "NotificationService";
    private static final String PREFERENCE_FILE = "org_codecarrots_watchstatus_preferences";
    private static final String RINGTONE = "ringtone";
    private static final String VIBRATE_MODE = "vibrate_mode";
    private static final String NOTIFICATION_ENABLED = "notification_enabled";

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(LOGTAG, "Preferences: ");
        SharedPreferences prefs = getSharedPreferences(PREFERENCE_FILE, MODE_PRIVATE);
        Boolean isNotificationEnabled = prefs.getBoolean(NOTIFICATION_ENABLED, true);
        Log.v(LOGTAG, isNotificationEnabled.toString());
        Boolean isVibrateModeOn = prefs.getBoolean(VIBRATE_MODE, true);
        Log.v(LOGTAG, isVibrateModeOn.toString());
        String ringtone = prefs.getString(RINGTONE, android.provider.Settings.System.DEFAULT_RINGTONE_URI.toString());
        Log.v(LOGTAG, ringtone);

        sendNotification(intent);
    }

    private void sendNotification(Intent intent) {
        try {
            Context context = getApplicationContext();
            String message = intent.getStringExtra("NOTIFICATION");
            int id = Integer.parseInt(intent.getStringExtra("ID"));
            Log.d(LOGTAG, "Preparing to send notification");

            String notificationTitle = context.getText(R.string.app_name).toString();

            NotificationCompat.Builder statusUpdate = new NotificationCompat.Builder(context)
                    .setContentTitle(notificationTitle)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_STATUS)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setSmallIcon(android.R.drawable.sym_def_app_icon);

            Intent notificationIntent = new Intent(context, CellSignalStatusHandler.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(CellSignalStatusHandler.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            statusUpdate.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Log.d(LOGTAG, "Sending notification with ID: " + id);
            notificationManager.notify(id, statusUpdate.build());
        }
        catch (NullPointerException npe) {
            Log.e(LOGTAG, npe.getMessage() + npe.getStackTrace());
        }
        catch (Exception e) {
            Log.e(LOGTAG, e.getMessage() + e.getStackTrace());
        }
    }
}
