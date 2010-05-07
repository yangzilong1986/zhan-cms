package com.ebis.ebank.startup;


import com.ebis.ebank.dx.*;
import com.ebis.ebank.ap.*;
import java.util.logging.*;
//import com.ebis.ebank.service.*;
import zt.cmsi.extrans.*;
import com.ebis.ebank.defines.*;
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
 *@created    2003年11月9日
 *@version    1.0
 */

public class TaskExecuter extends Thread {

    /**
     *  Description of the Field
     *
     *@author    sun
     *@since     2003年11月9日
     */
    public static Logger logger = Logger.getLogger("com.ebis.ebank.startup.TaskExecuter");
    /**
     *  Description of the Field
     *
     *@author    sun
     *@since     2003年11月9日
     */
    public static ConfigManager config = ConfigManager.getInstance();


    /**
     *  Main processing method for the TaskExecuter object
     */
    public void run() {
        //logger.info("TaskExecuter thread start!");
        MidControlCenter center = new MidControlCenter();
        while (ServerStarter.isRunning()) {
            TransTask task = TaskPool.getTransTask();
            if (task != null) {
                logger.info("Get one task from task pool ");
                if (doTask(task) == 0) {
                    logger.info("Do task ok and returned data is:\n"+task.getData().toString());
                    long ret = center.servSend(task.getData(), task.getSeries());
                    if (ret < 0) {
                        logger.warning("Do task ok,but send back info failed,return code is "+ret);
                    }else{
                        logger.info("Task send back!");
                    }
                }
                task=null;
            } else {
                try {
                    this.sleep(200);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    /**
     *  Description of the Method
     *
     *@param  task  Description of the Parameter
     *@return       Description of the Return Value
     */
    public int doTask(TransTask task) {
        ApDataPoint data = task.getData();
        CMSTrans service = getCMSTransInstance(data);
        if (service == null) {
            return -1;
        } else {
            data = service.service(data);
            task.setData(data);
            return 0;
        }
    }


    /**
     *  Gets the cMSTransInstance attribute of the TaskExecuter object
     *
     *@param  data  Description of the Parameter
     *@return       The cMSTransInstance value
     */
    protected CMSTrans getCMSTransInstance(ApDataPoint data) {
        data.initRead();
        if (!data.eof()) {
            String transId = (String) data.getData("TxnTp");
            Transaction trans = config.getTransaction(transId);
            String serviceClass = trans.getServiceClass();
            try {
                //System.out.println("JGO---"+serviceClass);
                return (CMSTrans) getClass().forName(serviceClass).newInstance();
            } catch (ClassNotFoundException ex) {
                logger.warning("servic class '" + serviceClass + "' could not find ");
            } catch (IllegalAccessException ex) {
            } catch (InstantiationException ex) {
            } finally {

            }
        }
        return null;
    }

}
