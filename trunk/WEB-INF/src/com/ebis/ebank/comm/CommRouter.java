package com.ebis.ebank.comm;


import com.ebis.ebank.defines.Gateway;
import com.ebis.ebank.comm.BTPComm;
import com.ebis.ebank.comm.DataPackUTag;

/**
 *  Description of the Class
 *
 *@author     sun
 *@created    2003Äê11ÔÂ3ÈÕ
 */
public class CommRouter {

    //DataPackUTag is the series number give by server!
    //

    /**
     *  Description of the Method
     *
     *@param  gateway  The gateway holds the hostid the data should be send to.
     *@param  data     The data will be sent out!
     *@param  datalen  The data's length
     *@param  uid      Description of the Parameter
     *@return          The series number send by server, it is equals to uid!
     */
    public static long clientSend(Gateway gateway, byte[] data, long datalen, DataPackUTag uid) {
        //type = 0; non-blocked mode, otherwise block mode

        if (gateway == null || data == null || datalen <= 0 || uid == null) {
            return CommErrorCode.COMM_ROUTER_PARM_ERROR;
        }

        String hostid = gateway.getHostName();
        return BTPComm.clientSend(hostid, data, datalen, uid);
    }


    /**
     *  Description of the Method
     *
     *@param  gateway  Description of the Parameter
     *@param  data     Description of the Parameter
     *@param  datalen  Description of the Parameter
     *@param  type     Description of the Parameter
     *@param  datauid  Description of the Parameter
     *@param  timeout  Description of the Parameter
     *@return          Description of the Return Value
     */
    public static long clientRecv(Gateway gateway, byte[] data, long datalen, int type, DataPackUTag datauid, long timeout) {
        if (gateway == null || data == null || datalen <= 0 || datauid == null || timeout < 0) {
            return CommErrorCode.COMM_ROUTER_PARM_ERROR;
        }

        String hostid = gateway.getHostName();
        return BTPComm.clientRecv(data, datalen, type, datauid, timeout);
    }


    /**
     *  Description of the Method
     *
     *@param  gateway  Description of the Parameter
     *@param  data     Description of the Parameter
     *@param  datalen  Description of the Parameter
     *@param  type     Description of the Parameter
     *@param  timeout  Description of the Parameter
     *@param  datauid  Description of the Parameter
     *@return          Description of the Return Value
     */
    public static long serverRecv(Gateway gateway, byte[] data, long datalen, int type, long timeout, DataPackUTag datauid) {
        if (gateway == null || data == null || datalen <= 0 || datauid == null || timeout < 0) {
            return CommErrorCode.COMM_ROUTER_PARM_ERROR;
        }

        String hostid = gateway.getHostName();
        //System.out.println(gateway.getHostName());
        //return BTPComm.serverRecv(data, datalen, type, hostid, datauid, timeout);
        long ret = BTPComm.serverRecv(data, datalen, type, hostid, datauid, timeout);
        //System.out.println("Ret is : "+ret);
        return ret;
    }


    /**
     *  Description of the Method
     *
     *@param  gateway  Description of the Parameter
     *@param  data     Description of the Parameter
     *@param  datalen  Description of the Parameter
     *@param  datauid  Description of the Parameter
     *@return          Description of the Return Value
     */
    public static long serverSend(Gateway gateway, byte[] data, long datalen, DataPackUTag datauid) {
        if (gateway == null || data == null || datalen <= 0 || datauid == null) {
            return CommErrorCode.COMM_ROUTER_PARM_ERROR;
        }

        String hostid = gateway.getHostName();
        return BTPComm.serverSend(data, datalen, datauid);
    }


    /**
     *  The main program for the CommRouter class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        CommRouter commRouter1 = new CommRouter();
        String s;
        byte[] yes = new byte[5];
        byte[] yes1 = new byte[5];
        yes[0] = 't';
        yes[1] = 0;
        yes[2] = 21;
        yes[3] = 'm';

        s = new String(yes);
        System.out.println(s);
        yes1 = s.getBytes();
    }
}
