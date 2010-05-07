package com.ebis.ebank.startup;

import java.util.*;
import java.util.logging.*;
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
 *@created    2003Äê11ÔÂ3ÈÕ
 *@version    1.0
 */

public class TaskPool {
    private static Vector pool = new Vector();

    /**
     *  Gets the transTask attribute of the TaskPool object
     *
     *@return    The transTask value
     */
    public static TransTask getTransTask() {
        synchronized(pool){
            if(pool.isEmpty()){
                try {
                    pool.wait(ServerStarter.THREAD_WAIT_TIME);
                    return null;
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }else{
                return (TransTask)pool.remove(0);
            }
        }
    }


    /**
     *  Adds a feature to the TransTask attribute of the TaskPool object
     *
     *@param  task  The feature to be added to the TransTask attribute
     */
    public  static void addTransTask(TransTask task) {
        synchronized(pool){
            pool.add(task);
            pool.notify();
        }
    }

}
