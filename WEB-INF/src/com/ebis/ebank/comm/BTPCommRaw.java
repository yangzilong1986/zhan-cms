package com.ebis.ebank.comm;

/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2002</p> <p>
 *
 *  Company: </p>
 *
 *@author     unascribed
 *@created    2003Äê11ÔÂ4ÈÕ
 *@version    1.0
 */

public class BTPCommRaw {

    private static boolean LoadLibaryFailed = false;


    /**
     *  Send data to server
     *
     *@param  hostid   In the CommServer.ini, store the host's name
     *@param  data     The data will sent to server, its structure is at
     *@param  datalen  The data's length
     *@return          The data uid keeped by server
     */
    public native static long clientSendB(String hostid, byte data[], long datalen);


    /**
     *  Recieve data form server
     *
     *@param  data     The data send by server
     *@param  datalen  The data's length
     *@param  rtype    The transport type send by server. 0,1.
     *@param  datauid  The uid delegate the transation instance
     *@return          Return the
     */
    public native static long clientRecvB(byte data[], long datalen, int rtype, long datauid);


    /**
     *  Description of the Method
     *
     *@param  dataptr  The data will send by client
     *@param  datalen  The data's length
     *@param  rtype    Transport type
     *@param  portid   The portid which stored in
     *@param  datauid  Description of the Parameter
     *@return          Description of the Return Value
     */
    public native static long serverRecvB(byte dataptr[], long datalen, int rtype, String portid, long datauid[]);


    /**
     *  Description of the Method
     *
     *@param  dataptr  The data will send by client
     *@param  datalen  The data's length
     *@param  datauid  The
     *@return          Description of the Return Value
     */
    public native static long serverSendB(byte dataptr[], long datalen, long datauid);


    /**
     *  Description of the Method
     *
     *@param  hostid   Description of the Parameter
     *@param  data     Description of the Parameter
     *@param  datalen  Description of the Parameter
     *@return          Description of the Return Value
     */
    public static long clientSend(String hostid, byte data[], long datalen) {
        if (LoadLibaryFailed == true) {
            return CommErrorCode.COMM_LIBRARY_LOAD_ERROR;
        } else {
            return clientSendB(hostid, data, datalen);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  data     Description of the Parameter
     *@param  datalen  Description of the Parameter
     *@param  rtype    Description of the Parameter
     *@param  datauid  Description of the Parameter
     *@return          Description of the Return Value
     */
    public static long clientRecv(byte data[], long datalen, int rtype, long datauid) {
        if (LoadLibaryFailed == true) {
            return CommErrorCode.COMM_LIBRARY_LOAD_ERROR;
        } else {
            //return -1;
            return clientRecvB(data, datalen, rtype, datauid);
        }

    }


    /**
     *  Description of the Method
     *
     *@param  dataptr  Description of the Parameter
     *@param  datalen  Description of the Parameter
     *@param  rtype    Description of the Parameter
     *@param  portid   Description of the Parameter
     *@param  datauid  Description of the Parameter
     *@return          Description of the Return Value
     */
    public static long serverRecv(byte dataptr[], long datalen, int rtype, String portid, long[] datauid) {
        if (LoadLibaryFailed == true) {
            return CommErrorCode.COMM_LIBRARY_LOAD_ERROR;
        } else {
            //return -1;
            long ret= serverRecvB(dataptr, datalen, rtype, portid, datauid);
            return ret;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  dataptr  Description of the Parameter
     *@param  datalen  Description of the Parameter
     *@param  datauid  Description of the Parameter
     *@return          Description of the Return Value
     */
    public static long serverSend(byte dataptr[], long datalen, long datauid) {
        if (LoadLibaryFailed == true) {
            return CommErrorCode.COMM_LIBRARY_LOAD_ERROR;
        } else {
            return serverSendB(dataptr, datalen, datauid);
        }
    }

    static {
        try {
            System.loadLibrary("CommClient");
            LoadLibaryFailed = false;
        } catch (Throwable e) {
            e.printStackTrace();
            LoadLibaryFailed = true;
        }
    }


    /**
     *  The main program for the BTPCommRaw class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        long ret = 0;
        BTPCommRaw BTPComm1 = new BTPCommRaw();
        String host = "uhost";
        String data = "happy data test";
        long len = 9;

        byte[] data2 = new byte[10];
        data2[0] = '1';
        data2[1] = '2';
        data2[2] = '3';
        data2[3] = '4';
        data2[4] = '5';
        data2[6] = 0;
        ret = BTPCommRaw.clientSend("HOST1", data2, 5);

        System.out.println(ret);
        byte[] buf = new byte[200];
        ret = BTPComm1.clientRecv(buf, 200, 1, ret);
        //ret = BTPComm1.clientSend(host,data,len);
        System.out.println(ret);
        System.out.println(new String(data2));

    }
}
