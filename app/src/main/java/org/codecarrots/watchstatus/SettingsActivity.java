package org.codecarrots.watchstatus;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;


import java.util.List;

/**
 * Class to define settings for notifications
 * @author Dipti Nirmale
 */
public class SettingsActivity extends PreferenceActivity {

    private static final String LOGTAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new NotificationPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
 //       loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    public static class NotificationPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {


        @Override
        public void onCreate(Bundle savedInstanceState) {
            Log.d(LOGTAG, "creating SettingsActivity");
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
        }

        @Override
        public void onResume() {
            Log.d(LOGTAG, "resuming Settings fragment");
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

            // This is workaround for RingtonePreference.
            // SharedPreference.OnSharedPreferenceChangeListener doesn't work for RingtonePreference.
            // Bug: "https://code.google.com/p/android/issues/detail?id=21766".
            RingtonePreference ringtonePreference = (RingtonePreference) findPreference(getString(R.string.pref_key_ringtone));
            ringtonePreference.setOnPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            Log.d(LOGTAG, "pausing Settings fragment");
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.d(LOGTAG, "SharedPreference changed");
            Preference preference;
            preference = findPreference(key);
            Log.d(LOGTAG, "Preference: " + preference.toString());

            // SharedPreference.OnSharedPreferenceChangeListener doesn't work for RingtonePreference.
            // Bug: "https://code.google.com/p/android/issues/detail?id=21766"
            /*if (preference instanceof RingtonePreference) {
                String value = sharedPreferences.getString(key, android.provider.Settings.System.DEFAULT_RINGTONE_URI.toString());
                Log.d(LOGTAG, "Key: Ringtone, Value: " + value);
                if (TextUtils.isEmpty(value)) {
                    Log.d(LOGTAG, "setting silent mode");
                    preference.setSummary(R.string.pref_ringtone_silent);
                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(value));
                    Log.d(LOGTAG, "Ringtone: " + ringtone);
                    if (ringtone == null) {
                        preference.setSummary(null);
                    } else {
                        preference.setSummary(ringtone.getTitle(preference.getContext()));
                    }
                }
            }*/
            if (preference instanceof CheckBoxPreference) {
                Boolean value = sharedPreferences.getBoolean(key, true);
                preference.setSummary(value.toString());
                Log.d(LOGTAG, "Key: " + key + ", Value: " + value);
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.d(LOGTAG, "Preference: " + preference.toString());

            String key = preference.getKey();
            String value = (String) newValue;
            Log.d(LOGTAG, "Key: " + key + ", Value: " + value);

            if (TextUtils.isEmpty(value)) {
                Log.d(LOGTAG, "setting silent mode");
                preference.setSummary(R.string.pref_ringtone_silent);
            }
            else {
                Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(value));
                Log.d(LOGTAG, "Ringtone: " + ringtone);
                if (ringtone == null) {
                    preference.setSummary(null);
                }
                else {
                    preference.setSummary(ringtone.getTitle(preference.getContext()));
                }
            }
            editor.putString(key, value);
            editor.commit();
            return true;
        }
    }
}