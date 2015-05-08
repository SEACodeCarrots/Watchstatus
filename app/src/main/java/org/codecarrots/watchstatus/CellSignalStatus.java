package org.codecarrots.watchstatus;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * This Singleton class maintains all the information related to cellular signal of the Android device.
 *
 * @author Dipti Nirmale
 */
public class CellSignalStatus {
    private static CellSignalStatus instance = new CellSignalStatus();
    private static final String LOGTAG = "CellSignalStatus";

    private enum SIGNALS_TYPES {GSM, WCDMA, CDMA, LTE}
    private static SIGNALS_TYPES signalType;
    private static CellSignalStrength css;

    public static CellSignalStatus getInstance() {
        return instance;
    }

    private CellSignalStatus() {
    }

    private int mDbmSignal;
    private int mAsuSignal;
    private int mPercentSignal;

    /**
     * This method sets the value for signal type.
     * @param context
     */
    public void setSignalType(Context context) {
        TelephonyManager phonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CellInfo cInfo = (CellInfo) phonyManager.getAllCellInfo().get(0);

        switch (cInfo.getClass().getSimpleName()) {
            case "CellInfoWcdma":
                Log.d(LOGTAG, "Signal is Wcdma");

                signalType = SIGNALS_TYPES.WCDMA;
                css = ((CellInfoWcdma) cInfo).getCellSignalStrength();
                break;
            case "CellInfoGsm":
                Log.d(LOGTAG, "Signal is GSM");

                signalType = SIGNALS_TYPES.GSM;
                css = ((CellInfoGsm) cInfo).getCellSignalStrength();
                break;
            case "CellInfoCdma":
                Log.d(LOGTAG, "Signal is Cdma");

                signalType = SIGNALS_TYPES.CDMA;
                css = ((CellInfoCdma) cInfo).getCellSignalStrength();
                break;
            case "CellInfoLte":
                Log.d(LOGTAG, "Signal is Lte");

                signalType = SIGNALS_TYPES.LTE;
                css = ((CellInfoLte) cInfo).getCellSignalStrength();
                break;
            default:
                Log.d(LOGTAG, "Signal not detected");

                signalType = null;
                break;
        }
    }
    /**
     * This method returns status of cellular signal
     * @param context
     * @return String
     */
    public String getSignalStatus(Context context) {
        String statusMessage = watchSignalStatus(context);
        if (statusMessage != null)
            return statusMessage;

        String errorMessage = "Cellular signal not detected";
        Log.d(LOGTAG, errorMessage);
        return errorMessage;
    }

    private String watchSignalStatus(Context context) {
        setSignalStrengthValues();
        setPercentSignalStrength();
        return getStringForSignalStrength();
    }

    private void setSignalStrengthValues() {
        if (css == null)
            return;
        mAsuSignal = css.getAsuLevel();
        mDbmSignal = css.getDbm();
    }

    private String getStringForSignalStrength() {
        if (signalType == null)
            return null;

        String signalString = "ASU Level : " + mAsuSignal + "\t DBM Level := " + mDbmSignal + "\n Percentage : " + mPercentSignal;
        return signalString;
    }

    // from http://www.ces.clemson.edu/linux/dbm-rssi.shtml
    private void setPercentSignalStrength() {
        int maxSignal = -20;
        int disassociationSignal = -85;
        mPercentSignal = 100 - 80*(maxSignal - mDbmSignal) / (maxSignal - disassociationSignal);
    }

}
