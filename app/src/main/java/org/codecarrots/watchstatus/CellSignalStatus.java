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
 * This Singleton class maintains the required information related to cellular signal of the Android device.
 *
 * @author Dipti Nirmale
 */
public class CellSignalStatus {
    private static final String LOGTAG = "CellSignalStatus";
    private static CellSignalStatus instance = new CellSignalStatus();

    private CellSignalStatus() { }
    public static CellSignalStatus getInstance() {
        return instance;
    }

    private enum SIGNALS_TYPES {GSM, WCDMA, CDMA, LTE}
    private SIGNALS_TYPES mSignalType;
    private CellSignalStrength mCss;

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

                mSignalType = SIGNALS_TYPES.WCDMA;
                mCss = ((CellInfoWcdma) cInfo).getCellSignalStrength();
                break;
            case "CellInfoGsm":
                Log.d(LOGTAG, "Signal is GSM");

                mSignalType = SIGNALS_TYPES.GSM;
                mCss = ((CellInfoGsm) cInfo).getCellSignalStrength();
                break;
            case "CellInfoCdma":
                Log.d(LOGTAG, "Signal is Cdma");

                mSignalType = SIGNALS_TYPES.CDMA;
                mCss = ((CellInfoCdma) cInfo).getCellSignalStrength();
                break;
            case "CellInfoLte":
                Log.d(LOGTAG, "Signal is Lte");

                mSignalType = SIGNALS_TYPES.LTE;
                mCss = ((CellInfoLte) cInfo).getCellSignalStrength();
                break;
            default:
                Log.d(LOGTAG, "Signal not detected");

                mSignalType = null;
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
        Log.d(LOGTAG, "mCss= " + mCss.getClass().getSimpleName() + ", dbm= " + mCss.getDbm() + ", mDbm= " + mDbmSignal);

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
        if (mCss == null)
            return;
        mAsuSignal = mCss.getAsuLevel();
        mDbmSignal = mCss.getDbm();
    }

    private String getStringForSignalStrength() {
        if (mSignalType == null)
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
