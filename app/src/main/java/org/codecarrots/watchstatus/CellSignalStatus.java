package org.codecarrots.watchstatus;

import android.content.Context;
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

/**
 * @author Dipti Nirmale
 */
public class CellSignalStatus implements Notifier {
    private final int MINDBM_GSM = -100;
    private final int MINDBM_CDMA = -105;
    private final int MINDBM_WCDMA = -110;
    private final int MINDBM_LTE = -110;

    private static final CellSignalStatus INSTANCE = new CellSignalStatus();

    public static CellSignalStatus getInstance() {
        return INSTANCE;
    }

    private CellSignalStatus() { }

    private void watchSignalStatus(Context context) {
        CellSignalStrengthWcdma cssWcdma;
        CellSignalStrengthGsm cssGsm;
        CellSignalStrengthCdma cssCdma;
        CellSignalStrengthLte cssLte;

        String tmp;

        TelephonyManager phonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CellInfo cInfo = (CellInfo) phonyManager.getAllCellInfo().get(0);

        if (cInfo instanceof CellInfoWcdma) {
            CellInfoWcdma ciWcdma = (CellInfoWcdma) cInfo;
            cssWcdma = ciWcdma.getCellSignalStrength();
            tmp = cssWcdma.getAsuLevel() + " " + cssWcdma.getDbm();
        }
        else if (cInfo instanceof CellInfoGsm) {
            CellInfoGsm ciGsm = (CellInfoGsm) cInfo;
            cssGsm = ciGsm.getCellSignalStrength();
            tmp = cssGsm.getAsuLevel() + " " + cssGsm.getDbm();
        }
        else if (cInfo instanceof CellInfoCdma) {
            CellInfoCdma ciCdma = (CellInfoCdma) cInfo;
            cssCdma = ciCdma.getCellSignalStrength();
            tmp = cssCdma.getAsuLevel() + " " + cssCdma.getDbm();
        }
        else if (cInfo instanceof CellInfoLte) {
            CellInfoLte ciLte = (CellInfoLte) cInfo;
            cssLte = ciLte.getCellSignalStrength();
            tmp = cssLte.getAsuLevel() + " " + cssLte.getDbm();
        }
        else {
            tmp = "None";
        }
    }

    @Override
    public boolean isNotifierSet() {
        return false;
    }

    @Override
    public void setNotifier() {

    }

    @Override
    public void sendNotification() {

    }

    
}
