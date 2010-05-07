package com.ebis.ebank.comm;


import com.ebis.ebank.comm.BTPCommRaw;
import com.ebis.ebank.comm.DataPackUTag;

/**
 *  Description of the Class
 *
 *@author     sun
 *@created    2003Äê11ÔÂ3ÈÕ
 */
public class BTPComm {
    /**
     *  Description of the Method
     *
     *@param  hostid   Description of the Parameter
     *@param  data     Description of the Parameter
     *@param  datalen  Description of the Parameter
     *@param  uid      Description of the Parameter
     *@return          Description of the Return Value
     */
    public static long clientSend(String hostid, byte data[], long datalen, DataPackUTag uid) {
        try {
            uid.dataUID = BTPCommRaw.clientSend(hostid, data, datalen);
        } catch (java.lang.Throwable e) {
            return CommErrorCode.COMM_JNI_ERROR;
        }

        if (uid.dataUID >= 0) {
            return 0;
        } else {
            return uid.dataUID;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  data     Description of the Parameter
     *@param  datalen  Description of the Parameter
     *@param  rtype    Description of the Parameter
     *@param  datauid  Description of the Parameter
     *@param  timeout  Description of the Parameter
     *@return          Description of the Return Value
     */
    public static long clientRecv(byte data[], long datalen, int rtype, DataPackUTag datauid, long timeout) {
        // rtype == 0: non-block mode, otherwise block mode

        if (rtype == 0) {
            try {
                return BTPCommRaw.clientRecv(data, datalen, 0, datauid.dataUID);
            } catch (java.lang.Throwable e) {
                return CommErrorCode.COMM_JNI_ERROR;
            }
        } else {
            long startTime;
            long retcode;
            startTime = System.currentTimeMillis();
            while (true) {
                //for(;(System.currentTimeMillis() - startTime) < timeout;)

                try {
                    retcode = BTPCommRaw.clientRecv(data, datalen, 0, datauid.dataUID);
                } catch (java.lang.Throwable e) {
                    return CommErrorCode.COMM_JNI_ERROR;
                }

                if (retcode >= 0) {
                    return retcode;
                } else {
                    if ((System.currentTimeMillis() - startTime) >= timeout) {
                        break;
                    } else {
                        try {
                            Thread.currentThread().sleep(1);
                        } catch (Exception e) {

                        }
                    }
                }
            }
            return CommErrorCode.COMM_ROUTER_TIMEOUT;
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
     *@param  timeout  Description of the Parameter
     *@return          Description of the Return Value
     */
    public static long serverRecv(byte dataptr[], long datalen, int rtype, String portid, DataPackUTag datauid, long timeout) {
        long[] uid = new long[1];
        long retcode;

        if (rtype == 0) {
            try {
                retcode = BTPCommRaw.serverRecv(dataptr, datalen, 0, portid, uid);
            } catch (java.lang.Throwable e) {
                e.printStackTrace();
                return CommErrorCode.COMM_JNI_ERROR;
            }
            datauid.dataUID = uid[0];
            dataptr=null;
            return retcode;
        } else {
            long startTime;
            startTime = System.currentTimeMillis();
            //for(;(System.currentTimeMillis() - startTime) < timeout;)
            while (true) {
                try {
                    retcode = BTPCommRaw.serverRecv(dataptr, datalen, 0, portid, uid);
                } catch (java.lang.Throwable e) {
                  e.printStackTrace();
                    return CommErrorCode.COMM_JNI_ERROR;
                }

                datauid.dataUID = uid[0];

                if (retcode >= 0) {
                    return retcode;
                } else {
                    if ((System.currentTimeMillis() - startTime) >= timeout) {
                        break;
                    } else {
                        try {
                            Thread.currentThread().sleep(1);
                        } catch (Exception e) {
                        }
                    }
                }
            }
            //dataptr=null;
            return CommErrorCode.COMM_ROUTER_TIMEOUT;
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
    public static long serverSend(byte dataptr[], long datalen, DataPackUTag datauid) {
        try {
            return BTPCommRaw.serverSend(dataptr, datalen, datauid.dataUID);
        } catch (java.lang.Throwable e) {
            return CommErrorCode.COMM_JNI_ERROR;
        }

    }

}
