package zt.cmsi.extrans;
/**
 * 请求管理类，所有需要发送授权请求的代码需要得到本类的instance，后操作。
 * The class manage the pool in manner of queue.
 */

import zt.cmsi.biz.LoanGranted;
import zt.platform.utils.Debug;

public class RequestMan {
    private static RequestMan requestMan;
    private static RequestPool pool;
    private static RequestPool poolResp;

    public RequestMan() {
        pool = new RequestPool();
        poolResp = new RequestPool();
    }

    /**
     * 得到本类的实例， 只有一个
     * 在第一次启动本类时,
     * LOOP ALL RECORDS IN BMLOANGRANTED
     * if STATUS = 未授权
     * 　　add this records into POOL
     * end if
     * END LOOP
     */
    public static RequestMan getInstance() {
        if (requestMan == null) {
            requestMan = new RequestMan();
            return requestMan;
        } else {
            return requestMan;
        }
    }

    /**
     * this function must synchronize  POOL member variable and write the request to pool
     * After writing, the function should notify the POOL
     */
    public long addRequest(IOEntity data) {
        long tag;
        synchronized (pool) {
            tag = SequenceNo.nextSeqNo();
            data.ioTag = tag;
            data.sendOK = false;
            data.timeCreated = System.currentTimeMillis();
            data.failedTimes = 0;
            pool.addRequest(data);
            pool.notify();
            return tag;
        }
    }

    public long reSendRequest(IOEntity data) {
        long tag;
        synchronized (pool) {
            tag = SequenceNo.nextSeqNo();
            data.ioTag = tag;
            data.sendOK = false;
            pool.addRequest(data);
            pool.notify();
            return tag;
        }
    }

    /**
     * check the queue,
     * if the queue is empty, then wait. Otherwise return the first request in queue.
     */
    public IOEntity getRequest() {
        synchronized (pool) {
            IOEntity entity = pool.getRequest();
            if (entity == null) {
                try {
                    pool.wait(3000);
                    return pool.getRequest();
                }
                catch (InterruptedException e) {
                    if (Debug.isDebugMode == true) {
                        Debug.debug(e);
                    }
                    return null;
                }
            } else {
                return entity;
            }
        }

    }

    /**
     * @param iotag
     * @return zt.cmsi.extrans.IOEntity
     * @roseuid 3FE942C90231
     */
    public long addResponse(IOEntity data) {
        long tag;
        synchronized (poolResp) {
            tag = SequenceNo.nextSeqNo();
            poolResp.addRequest(data);
            poolResp.notify();
            return tag;
        }
    }

    public IOEntity getResponse(long iotag) {
        synchronized (poolResp) {
            IOEntity entity = poolResp.getRequestByTag(iotag);
            if (entity == null) {
                try {
                    poolResp.wait(5000);
                    return poolResp.getRequestByTag(iotag);
                }
                catch (InterruptedException e) {
                    if (Debug.isDebugMode == true) {
                        Debug.debug(e);
                    }
                    return null;
                }
            } else {
                return entity;
            }
        }
    }

    /**
     * this function do following job
     * 1. get an IOTag
     * 2.
     * 3. create a IOEntity object
     * 4. add IOEntity to pool
     * 5. notify POOL object
     */
    public static int processRequest(IOEntity data) {
        int errorcode = SendGrant.send(data);
        if (errorcode >= 0) {
            if (data.ifCreateGrant == true) {
                errorcode = LoanGranted.sendGrantOK(data.BMNo);
            } else {
                errorcode = LoanGranted.cancelGrant(data.BMNo);
            }
        }
        if (errorcode >= 0) {
            data.sendOK = true;
        } else {
            Debug.debug(Debug.TYPE_MESSAGE, "Current Failed time for sending request is:" + data.failedTimes);
            data.failedTimes = data.failedTimes + 1;
            //Debug.debug(Debug.TYPE_MESSAGE, "Current Failed time for sending request is:"+data.failedTimes);
            data.sendOK = false;
            if (data.failedTimes >= 4) {
                errorcode = 0; // if failure times exceeds 4, then donot send again, but SendOK is false.
            }
        }
        return errorcode;
    }
}
