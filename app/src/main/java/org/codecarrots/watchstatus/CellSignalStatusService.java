package org.codecarrots.watchstatus;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
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
        String message = watchSignalStatus(this);
        mIntent.putExtra("NOTIFICATION", message);
        mIntent.putExtra("ID", "101");
        mIntent.setClass(this, NotificationReceiver.class);
        Log.d(LOGTAG, "Broadcasting Cellular Signal Status");
        sendBroadcast(mIntent);
        Log.d(LOGTAG, "Broadcast sent");
    }

    private String watchSignalStatus(Context context) {
        CellSignalStrengthWcdma cssWcdma;
        CellSignalStrengthGsm cssGsm;
        CellSignalStrengthCdma cssCdma;
        CellSignalStrengthLte cssLte;

        TelephonyManager phonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CellInfo cInfo = (CellInfo) phonyManager.getAllCellInfo().get(0);

        if (cInfo instanceof CellInfoWcdma) {
            CellInfoWcdma ciWcdma = (CellInfoWcdma) cInfo;
            cssWcdma = ciWcdma.getCellSignalStrength();
            return signalStrengthString(cssWcdma.getAsuLevel(), cssWcdma.getDbm());
        }
        else if (cInfo instanceof CellInfoGsm) {
            CellInfoGsm ciGsm = (CellInfoGsm) cInfo;
            cssGsm = ciGsm.getCellSignalStrength();
            return signalStrengthString(cssGsm.getAsuLevel(), cssGsm.getDbm());
        }
        else if (cInfo instanceof CellInfoCdma) {
            CellInfoCdma ciCdma = (CellInfoCdma) cInfo;
            cssCdma = ciCdma.getCellSignalStrength();
            return signalStrengthString(cssCdma.getAsuLevel(), cssCdma.getDbm());
        }
        else if (cInfo instanceof CellInfoLte) {
            CellInfoLte ciLte = (CellInfoLte) cInfo;
            cssLte = ciLte.getCellSignalStrength();
            return signalStrengthString(cssLte.getAsuLevel(), cssLte.getDbm());
        }
        return new String("Cellular signal not detected");
    }

    private String signalStrengthString(int asu, int dbm) {
        return new String("ASU Level := " + asu + "\t DBM Level := " + dbm);
    }

}
