package org.codecarrots.watchstatus;

/**
 * This Interface is to be implemented by Stuatus classes.
 * @author Dipti Nirmale
 */
interface Notifier {

    boolean isNotifierSet();
    void setNotifier();
    void sendNotification();
}
