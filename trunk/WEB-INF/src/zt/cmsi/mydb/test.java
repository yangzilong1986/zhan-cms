package zt.cmsi.mydb;

import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

public class test extends Thread {
    long avg = 0;

    public test() {
    }

    static public void startThreads() {
        boolean errorflag = false;
        boolean hasOne = false;
        test iocheck;
        for (int i = 1; i <= 50; i++) {
            iocheck = new test();
            if (iocheck == null) {
                Debug.debug(Debug.TYPE_ERROR, "failed to create RequestThread object in startup for NO " + i);
            } else {
                hasOne = true;
                iocheck.setDaemon(false);
                iocheck.start();
                Debug.debug(Debug.TYPE_ERROR, "Thread has started up for NO " + i);
                // set to user thead to prevent system from exit
                //this.clientThread.add(iocheck);
            }
        }
    }

    public void run() {
        for (int i = 0; i <= 2000; i++) {
            this.doTest();
        }
    }

    public void doTest() {
        long timenow;
        long timespent = 0;

        timenow = System.currentTimeMillis();

        DatabaseConnection ttt = null, tt2 = null, tt3 = null;
        ttt = MyDB.getInstance().apGetConn();
        timespent += System.currentTimeMillis() - timenow;

        timenow = System.currentTimeMillis();
        tt2 = MyDB.getInstance().apGetConn();
        timespent += System.currentTimeMillis() - timenow;
        if (tt2.hashCode() != ttt.hashCode())
            System.out.println("===**************=======================fatal error=====**********=================");

        timenow = System.currentTimeMillis();
        tt3 = MyDB.getInstance().apGetConn();
        timespent += System.currentTimeMillis() - timenow;
        if (tt3.hashCode() != ttt.hashCode())
            System.out.println("==========================fatal error2============***********************==========");

        try {
            //System.out.println("===in Progress1111===");
            Thread.currentThread().sleep(5000);
            //System.out.println("===in Progress222===");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        timenow = System.currentTimeMillis();
        MyDB.getInstance().apReleaseConn(0);
        MyDB.getInstance().apReleaseConn(0);
        MyDB.getInstance().apReleaseConn(0);
        timespent += System.currentTimeMillis() - timenow;

        this.avg = (this.avg + timespent) / 2;
        System.out.print("-[" + timespent + "]-");
    }
}
