package zt.cmsi.extrans;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import com.ebis.ebank.startup.ServerStarter;
import zt.platform.utils.Debug;

import java.util.Enumeration;
import java.util.Vector;

public class StartService {
    private static StartService startup = null;
    private static Vector clientThread = new Vector();
    private boolean serviceStarted = false;
    private static boolean exitFlag = false;

    public StartService() {
    }

    public static StartService getInstance() {
        if (StartService.startup == null) {
            StartService.startup = new StartService();
        }
        return StartService.startup;
    }

    private boolean startThreads() {
        boolean errorflag = false;
        boolean hasOne = false;
        RequestThread iocheck;
        for (int i = 1; i <= 1; i++) {
            iocheck = new RequestThread();
            if (iocheck == null) {
                Debug.debug(Debug.TYPE_ERROR, "failed to create RequestThread object in startup for NO " + i);
            } else {
                hasOne = true;
                iocheck.setDaemon(false);
                // set to user thead to prevent system from exit
                this.clientThread.add(iocheck);
            }
        }
        if (hasOne == false) {
            errorflag = true;
        }
        return (!errorflag);
    }

    private boolean runThreads() {
        RequestThread iocheck;
        int i;
        Enumeration e;
        try {
            e = this.clientThread.elements();
            while (e.hasMoreElements()) {
                iocheck = (RequestThread) e.nextElement();
                if (iocheck != null) {
                    iocheck.start();
                }
            }
            return true;
        }
        catch (Exception e2) {
            Debug.debug(Debug.TYPE_ERROR, "There are exceptions when starting the created threads!");
            Debug.debug(Debug.TYPE_ERROR, e2.getMessage());
            return false;
        }
    }

    public int startupService() {
        boolean flag = true;
        int ret;
        if (RequestMan.getInstance() == null) {
            flag = false;
            Debug.debug(Debug.TYPE_ERROR, "failed to get instance of RequestMan in Interfece Startup!");
        }

        if (flag == false) {
            //this.exitCleanup();
            Debug.debug(Debug.TYPE_ERROR, "System encounter fatal error when getting instance of necessary resources, can not continue!");
            return -1;
        }

        if (this.startThreads() == false) {
            this.exitCleanup();
            Debug.debug(Debug.TYPE_ERROR, "System encounter fatal error when creating thread objects, can not continue!");
            return -2;
        }

        if (this.runThreads() == false) {
            this.exitCleanup();
            Debug.debug(Debug.TYPE_ERROR, "System encounter fatal error when starting threads, can not continue!");
            return -3;
        }

        ServerStarter.startUp();

        return 0;
    }

    /**
     * Description of the Method
     */
    public void exitCleanup() {
        clientThread = null;
        serviceStarted = false;
        exitFlag = false;
        //startup = null;
    }

    public static void main(String[] args) {
        //System.out.println(StartService.getInstance().startupService());
        //System.out.println("cancel ret:" + LoanGranted.sendLoanGrant("00000000024",false));
    }

}
