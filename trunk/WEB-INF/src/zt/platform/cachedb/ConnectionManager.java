package zt.platform.cachedb;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import javax.sql.rowset.CachedRowSet;
import java.math.BigDecimal;


public class ConnectionManager {
    //private static Logger logger = Logger.getLogger("zt.platform.db.ConnectionManager");
    private static ConnectionManager manager = null;

    private ConnectionManager() {
    }

    public static ConnectionManager getInstance() {
        if (manager == null) {
            manager = new ConnectionManager();
        }
        return manager;
    }


    public CachedRowSet getRs(String p_sql) throws Exception {
        return DB2_81.getRs(p_sql);
    }

    public String getCellValue(String p_field, String p_table, String p_where) throws
            Exception {
        return DB2_81.getCellValue(p_field, p_table, p_where);
    }

    public BigDecimal getCellValueOfBigDecimal(String p_field, String p_table,
                                               String p_where) throws Exception {
        return DB2_81.getCellValueOfBigDecimal(p_field, p_table,
                p_where);
    }

    public java.util.Vector getPageRs(String p_sql, int p_pn, int p_ps) throws
            Exception {
        return DB2_81.getPageRs(p_sql, p_pn, p_ps);
    }

    public boolean ExecCmd(String p_sql) throws Exception {
        return DB2_81.ExecCmd(p_sql);
    }

    public boolean execBatch(String[] p_sql) throws Exception {
        return DB2_81.execBatch(p_sql);
    }

    public sun.jdbc.rowset.CachedRowSet getRsForUpdate(String p_sql, String tablename) throws
            Exception {
        return DB2_81.getRsForUpdate(p_sql, tablename);
    }


}
