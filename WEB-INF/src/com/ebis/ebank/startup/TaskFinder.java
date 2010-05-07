package com.ebis.ebank.startup;

import com.ebis.ebank.dx.*;
import com.ebis.ebank.ap.*;
import com.ebis.ebank.defines.*;
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

public class TaskFinder extends Thread {
    /**
     *  Main processing method for the TaskFinder object
     */
    public static Logger logger=Logger.getLogger("com.ebis.ebank.startup.TaskFinder");


    public void run() {
        ControlCenter center = new MidControlCenter();
        GatewayManager manager=GatewayManager.getInstance();
        Gateway gateway = manager.get("111");
        //logger.info("TaskFinder thread start!");
        ApDataPoint apData = new ApDataPoint(null);
        while (ServerStarter.isRunning()) {


            long  series = center.servRecv(apData, gateway,1000);
            if (series < 0) {
                try {
                    this.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } else{
                TransTask task=new TransTask(apData,gateway,series);
                TaskPool.addTransTask(task);
                System.out.println("Recieve one data from client, the data is \n"+apData.toString());
                logger.info("Recieve one data from client, the data is \n"+apData.toString());
                apData=new ApDataPoint(null);
            }
        }
    }
}
