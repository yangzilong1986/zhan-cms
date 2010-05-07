package com.ebis.ebank.startup;

/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author     not attributable
 *@created    2003Äê11ÔÂ11ÈÕ
 *@version    1.0
 */

public class ServerStarter {
    /**
     *  Description of the Field
     */
    public static long THREAD_WAIT_TIME = 500;
    /**
     *  Description of the Field
     */
    public static int THREAD_NUMBER = 3;

    private static boolean running = true;


    /**
     *  Constructor for the ServerStarter object
     */
    private ServerStarter() { }


    /**
     *  Description of the Method
     */
    public static void startUp() {
        for (int i = 0; i < THREAD_NUMBER; i++) {
            TaskExecuter executer = new TaskExecuter();
            TaskFinder finder = new TaskFinder();
            executer.start();
            finder.start();

        }
    }


    /**
     *  The main program for the ServerStarter class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        startUp();
    }


    /**
     *  Gets the running attribute of the ServerStarter class
     *
     *@return    The running value
     */
    public static boolean isRunning() {
        return running;
    }


    /**
     *  Sets the running attribute of the ServerStarter class
     *
     *@param  arg  The new running valu
     */

    public static void stop(){
        running = false;
    }

}
