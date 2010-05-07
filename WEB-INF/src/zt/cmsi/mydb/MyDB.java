package zt.cmsi.mydb;

import zt.cmsi.pub.ErrorCode;
import zt.platform.db.ConnectionManager;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.util.HashMap;
import java.util.logging.Logger;

public class MyDB {
    private static Logger logger = Logger.getLogger("zt.cmsi.mydb.MyDB");
    private static MyDB inst = null;
    private static HashMap data = null;
    private static HashMap datanew = null;
    private static HashMap datatime = null;
    private static int OldDB = -1000;
    private static Object m_Lock = new Object();

    private void MyDB() {

    }

    /**
     * @param conn
     * @return int
     * @roseuid 3FEA4AB10135
     */
    public int addDBConn(DatabaseConnection conn) {
        String id;
        id = "" + Thread.currentThread().hashCode();
        try {
            synchronized (this.m_Lock) {
                data.put(id, conn);
                datanew.put(id, new Integer(OldDB));
                datatime.put(id, new Long(System.currentTimeMillis())); //current time
            }
            return 0;
        }
        catch (Exception e) {
            logger.info(e.getMessage());
            return ErrorCode.MYDB_EXCEPT_ADDCONN;
        }
    }

    /**
     * @return zt.cmsi.mydb.MyDB
     * @roseuid 3FEA4B650148
     */
    static public MyDB getInstance() {
        if (inst == null) {
            inst = new MyDB();
            inst.data = new HashMap(20);
            inst.datanew = new HashMap(20);
            inst.datatime = new HashMap(20);
        }
        return inst;
    }

    /**
     * @return zt.platform.db.DatabaseConnection
     * @roseuid 3FEA4BCB00EB
     */
    public DatabaseConnection apGetConn() {

        DatabaseConnection conn;
        String id;
        id = "" + Thread.currentThread().hashCode();
        //logger.info("ap getconn!"+id);
        boolean ifcon = false;
        try {
            /**
             * if thread already owns DB connection assigned by DBAddConn();
             * then check its created time. If created time is 3 mins earlier than current time
             * then regard it as connection that missed DBReleaseConn();
             */
            synchronized (this.m_Lock) {
                ifcon = data.containsKey(id);
                if (ifcon) {
                    if (((Integer) datanew.get(id)).intValue() <= OldDB) {
                        if (datatime.get(id) != null) {
                            if (System.currentTimeMillis() -
                                    ((Long) datatime.get(id)).longValue() > 180000) {
                                logger.info(
                                        "Release a connection 3 mins earlier assigned by DBAddConn()");
                                data.remove(id);
                                datanew.remove(id);
                                datatime.remove(id);
                            }
                        }
                    }
                }
            }
            //if ap create its own db connection, then increase its hop by one
            if (ifcon) {
                synchronized (this.m_Lock) {
                    if (((Integer) datanew.get(id)).intValue() > OldDB) {
                        datanew.put(id,
                                new Integer(((Integer) datanew.get(id)).intValue() + 1));
                    }
                    return (DatabaseConnection) data.get(id);
                }
            }
            //create a new db connection from ConnectionManager
            else {
                conn = ConnectionManager.getInstance().getConnection();
                if (conn != null) {
                    conn.setAuto(false);
                    synchronized (this.m_Lock) {
                        data.put(id, conn);
                        datanew.put(id, new Integer(1));
                        datatime.put(id, new Long(System.currentTimeMillis()));
                    }
                }
                return conn;
            }
        }
        catch (Exception e) {
            Debug.debug(e);
            return null;
        }
    }

    /*
    * if retcode >=0, this represent user applications returns successfully,
    * ask MYDB to commit DB transaction
    * otherwise, AP fails and ask MYDB to rollback
    */

    public void apReleaseConn(int retcode) {

        String id = "" + Thread.currentThread().hashCode();
        //logger.info("ap release!!!!!!!!!!!!!!!!!!!"+id);
        if (!data.containsKey(id)) {
            return;
        }
        DatabaseConnection conn;
        try {
            synchronized (this.m_Lock) {
                if (datanew.containsKey(id)) {
                    int count = ((Integer) datanew.get(id)).intValue();
                    if (count > OldDB) {
                        count = count - 1;
                        if (count > 0) {
                            datanew.put(id, new Integer(count));
                        } else {
                            conn = (DatabaseConnection) data.get(id);
                            if (retcode < 0) {
                                Debug.debug(Debug.TYPE_ERROR, "roll back in mydb, ErrorCode is " + retcode);
                                conn.rollback();
                            } else {
                                conn.commit();
                            }
                            ConnectionManager.getInstance().releaseConnection(conn);
                            data.remove(id);
                            datanew.remove(id);
                            datatime.remove(id);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            Debug.debug(e);
        }
    }

    /**
     * @return int
     * @roseuid 3FEA4BDF007B
     */
    public int releaseDBConn() {
        String id;
        try {
            id = "" + Thread.currentThread().hashCode();
            //Added by wxj:连接从池子中移除前要先关闭它
            if ((Integer) datanew.get(id) != null && ((Integer) datanew.get(id)).intValue() > OldDB) {
                logger.info("Release a connection assigned by apGetConn() and releaseed by releaseDBConn()");
                ConnectionManager.getInstance().releaseConnection((DatabaseConnection) data.get(id));
            }
            //we don't need release DatabaseConnection here
            synchronized (this.m_Lock) {

                data.remove(id);
                datanew.remove(id);
                datatime.remove(id);
            }
        }
        catch (Exception e) {
            logger.info(e.getMessage());
        }
        return 0;
    }

    /**
     * remove all resevered connection for current thread
     * should only be called from the defaultform.jsp
     *
     * @param args
     */

    public void removeCurrentThreadConn(String progid) {
        String id = "" + Thread.currentThread().hashCode();
        //logger.info("Checking MYDB's Connection for thread no:["+id + "] PROGID:"+progid);
        try {
            synchronized (this.m_Lock) {

                if (!data.containsKey(id)) {
                    return;
                } else {
//        DatabaseConnection conn = null;
//        conn = (DatabaseConnection) data.get(id);
//        ConnectionManager.getInstance().releaseConnection(conn);

                    logger.warning(
                            "Has found unrelease DatabaseConnection,releasing it.thread no:[" +
                                    id + "] PROGID:" + progid);
                    data.remove(id);
                    datanew.remove(id);
                    datatime.remove(id);
                }
            }
        }
        catch (Exception e) {
            Debug.debug(e);
        }

    }


    static public void main(String[] args) {

        DatabaseConnection db;
        //db = ConnectionManager.getInstance().getConnection();
        //System.out.println(db.hashCode());
        //System.out.println(MyDB.getInstance().addDBConn(db));
        System.out.println("ss1 is:" + MyDB.getInstance().apGetConn().hashCode());
        System.out.println("ss2 is:" + MyDB.getInstance().apGetConn().hashCode());
        MyDB.getInstance().apReleaseConn(3);
        System.out.println("now is:" + MyDB.getInstance().apGetConn().hashCode());
        MyDB.getInstance().apReleaseConn(-1);
        MyDB.getInstance().releaseDBConn();
        //System.out.println("sfdsf"+MyDB.getInstance().getCurrConn().hashCode());
        //ConnectionManager.getInstance().releaseConnection(db);

    }

}
