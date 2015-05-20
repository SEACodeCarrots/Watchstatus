package org.codecarrots.watchstatus;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Service for sending notifications to the user.
 * @author Dipti Nirmale
 */
public class NotificationService extends IntentService {

    private static final String LOGTAG = "NotificationService";
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(LOGTAG, "Preferences: ");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Boolean isNotificationEnabled = sharedPreferences.getBoolean(getString(R.string.pref_key_new_alert_notifications), true);
        Log.v(LOGTAG, "Notification: " + isNotificationEnabled.toString());
        Boolean isVibrateModeOn = sharedPreferences.getBoolean(getString(R.string.pref_key_vibrate), true);
        Log.v(LOGTAG, "Vibrate_mode: " + isVibrateModeOn.toString());
        String ringtone = sharedPreferences.getString(getString(R.string.pref_key_ringtone), android.provider.Settings.System.DEFAULT_RINGTONE_URI.toString());
        Log.v(LOGTAG, "Ringtone: " + ringtone);

        if (isNotificationEnabled) {
            Log.d(LOGTAG, "Sending notification");
            sendNotification(intent, isVibrateModeOn, ringtone);
        }
        else {
            Log.d(LOGTAG, "Select 'New Alert Notifications' to enable notifications");
        }
    }

    private void sendNotification(Intent intent, Boolean isVibrateModeOn, String ringtone) {
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
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setSound(Uri.parse(ringtone));

            if(isVibrateModeOn) {
                long[] pattern = {0, 1000, 100, 50};
                statusUpdate.setVibrate(pattern);
            }

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
