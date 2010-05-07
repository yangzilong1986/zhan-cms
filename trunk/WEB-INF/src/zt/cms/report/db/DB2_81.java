package zt.cms.report.db;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author JGO(GZL)
 * @version 1.0
 */

import com.sun.rowset.CachedRowSetImpl;

import zt.platform.cachedb.DatabaseConnection;
import zt.platform.utils.Debug;

import javax.sql.rowset.CachedRowSet;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public class DB2_81 {

    /**
     * 构造
     */
    public DB2_81() {

    }

    /**
     * 获取结果集
     */
    public static CachedRowSet getRs(String p_sql) throws Exception {
        //Debugger.write("SqlManager_oracle817 getRs p_sql:" + p_sql);
        DatabaseConnection dbcm = new DatabaseConnection();
        //ConnectionManager dbcm = ConnectionManager.getInstance();
        java.sql.Connection conn = dbcm.getConnection();
        if (conn == null) throw new Exception("数据库连接错误！");
        try {
            java.sql.Statement stmt = conn.createStatement();
            Debug.debug(Debug.TYPE_SQL, p_sql);
            ResultSet rs = stmt.executeQuery(p_sql);

            CachedRowSet crs = DB2_81.createCachedRowSet();
            crs.populate(rs);
            rs.close();
            stmt.close();

            //crs.setTableName("to_contractManage");
            return crs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }


    public static sun.jdbc.rowset.CachedRowSet getRsForUpdate(String p_sql, String tablename) throws Exception {

        if (p_sql == null || tablename == null) return null;

//        DatabaseConnection dbcm = new DatabaseConnection();
//        java.sql.Connection conn = dbcm.getConnection();
//        if (conn == null) throw new Exception("数据库连接错误！");;

        Debug.debug(Debug.TYPE_SQL, p_sql);

        try {
//            CachedRowSet crs = DB2_81.createCachedRowSet();
//            crs.setDataSourceName(DatabaseConnection.DEFAULT_DATASOURCE_URL);
            sun.jdbc.rowset.CachedRowSet crs = new sun.jdbc.rowset.CachedRowSet();
//            Context initCtx = new InitialContext();
//            DataSource ds = (DataSource) initCtx.lookup(DatabaseConnection.DEFAULT_DATASOURCE_URL);
//            RowSetFactory rsfact = RowSetFactory.newInstance();
//            WLCachedRowSet crs = rsfact.newCachedRowSet();

//            crs.setDataSource(ds);
            sun.jdbc.rowset.JGORowSetWrite mywriter = new sun.jdbc.rowset.JGORowSetWrite();
            crs.setDataSourceName(DatabaseConnection.DEFAULT_DATASOURCE_URL);
            crs.setWriter(mywriter);
            crs.setCommand(p_sql);
            crs.execute();

            crs.setTableName(tablename);

            //crs.setTableName("to_contractManage");
            return crs;
        } catch (Exception ex) {
            Debug.debug(ex);
            return null;
        } finally {
//          if(conn != null) conn.close();
        }

    }

    public static String getCellValue(String p_field, String p_table, String p_where) throws Exception {
        DatabaseConnection dbcm = new DatabaseConnection();
        java.sql.Connection conn = dbcm.getConnection();
        if (conn == null) throw new Exception("数据库连接错误！");
        String sql = "select " + p_field;
        sql += " from " + p_table;
        sql += " where " + p_where;
        Debug.debug(Debug.TYPE_SQL, sql);
        // Debugger.write("SqlManager_oracle817 getCellValue sql:" + sql);
        try {
            java.sql.Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                return rs.getString(1);
            }
            rs.close();
            stmt.close();

            //crs.setTableName("to_contractManage");
            return "";
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }

        }
    }

    public static BigDecimal getCellValueOfBigDecimal(String p_field, String p_table, String p_where) throws Exception {
        DatabaseConnection dbcm = new DatabaseConnection();
        java.sql.Connection conn = dbcm.getConnection();
        if (conn == null) throw new Exception("数据库连接错误！");
        String sql = "select " + p_field;
        sql += " from " + p_table;
        sql += " where " + p_where;
        Debug.debug(Debug.TYPE_SQL, sql);
        // Debugger.write("SqlManager_oracle817 getCellValue sql:" + sql);
        try {
            java.sql.Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                return rs.getBigDecimal(1);
            }
            rs.close();
            stmt.close();

            //crs.setTableName("to_contractManage");
            return null;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }

        }
    }

    /**
     * common PageRs for all DB
     * created by: JGO(GZL) on 2005/6/25
     * modified by weiyb on 2007/4/10
     */
    public static java.util.Vector pageRs(String s_main,String s_count,String s_where) throws Exception {
        DatabaseConnection dbcm = new DatabaseConnection();
       
        java.sql.Connection conn = dbcm.getConnection();
        
        if (conn == null) throw new Exception("数据库连接错误！");
        int rows;
        String sql;

        //执行查询
        try {
            //总行数
            java.util.Vector MultiResult = new java.util.Vector(1, 1);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sql = "select count(1) from ("+s_count+s_where+") as temptable";
            Debug.debug(Debug.TYPE_SQL, sql);
            ResultSet rs = stmt.executeQuery(sql);
            rows = 0;
            if (rs.next()) {
                rows = rs.getInt(1);
            }
            rs.close();
            MultiResult.addElement(new Integer(rows));
  
            Debug.debug(Debug.TYPE_SQL, s_main);
            rs=stmt.executeQuery(s_main);
            CachedRowSet crs = DB2_81.createCachedRowSet();
            crs.populate(rs);
            MultiResult.addElement(crs);
            rs.close();
            stmt.close();
            return MultiResult;
        } catch (Exception ex) {
            Debug.debug(ex);
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
    /**
     * common PageRs for all DB
     * created by: JGO(GZL) on 2005/6/25
     */
    public static java.util.Vector getPageRs(String p_sql, int p_pn, int p_ps) throws Exception {
        DatabaseConnection dbcm = new DatabaseConnection();
        java.sql.Connection conn = dbcm.getConnection();
        if (conn == null) throw new Exception("数据库连接错误！");
        int rows, top1, top2;
        String sql, sql1;
        //String p_order;
        //参数检验
        if (p_pn < 1) {
            p_pn = 1;
        }
        if (p_ps < 1) {
            p_ps = 10;
        }
        //执行查询
        try {
            //总行数
            java.util.Vector MultiResult = new java.util.Vector(1, 1);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            int endidx = p_sql.indexOf("order by");
            sql = "select count(*)";
            if (endidx < 0)
                sql += p_sql.substring(p_sql.indexOf(" from "));
            else
                sql += p_sql.substring(p_sql.indexOf(" from "), endidx);

            Debug.debug(Debug.TYPE_SQL, sql);
            ResultSet rs = stmt.executeQuery(sql);
            rows = 0;
            if (rs.next()) {
                rows = rs.getInt(1);
            }
            rs.close();
            MultiResult.addElement(new Integer(rows));
            //top1：实际数据的开始位置rs>top1
            top1 = (p_pn - 1) * p_ps + 1;
//            top2：实际数据的结束位置rs<=top2
//            top2 = top1 + p_ps;
//            if (top2 > rows) {
//                top2 = rows;
//            }

            Debug.debug(Debug.TYPE_SQL, p_sql);
            stmt.setFetchSize(p_ps);
            stmt.setMaxRows(top1 - 1 + p_ps);
            rs = stmt.executeQuery(p_sql);
            if (top1 != 1) {
                rs.absolute(top1 - 1);
            }

            CachedRowSet crs = DB2_81.createCachedRowSet();
            crs.populate(rs);
            MultiResult.addElement(crs);
            rs.close();
            stmt.close();
            return MultiResult;
        } catch (Exception ex) {
            Debug.debug(ex);
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * 获取分页用的结果集
     */
    /* getPageRs for Oracle DB

    public static java.util.Vector getPageRs(String p_sql, int p_pn, int p_ps) throws Exception {
        DatabaseConnection dbcm = new DatabaseConnection();
        java.sql.Connection conn = dbcm.getConnection();
        if (conn == null) throw new Exception("数据库连接错误！");
        int rows, top1, top2;
        String sql;
        //String p_order;
        //参数检验
        if (p_pn < 1) {
            p_pn = 1;
        }
        if (p_ps < 1) {
            p_ps = 10;
        }
        //执行查询
        try {
            //总行数
            java.util.Vector MultiResult = new java.util.Vector(1, 1);
            Statement stmt = conn.createStatement();
            sql = "select count(*) from (" + p_sql + ")";
            Debug.debug(Debug.TYPE_SQL, sql);
            ResultSet rs = stmt.executeQuery(sql);
            rows = 0;
            if (rs.next()) {
                rows = rs.getInt(1);
            }
            rs.close();
            MultiResult.addElement(new Integer(rows));
            //top1：实际数据的开始位置rs>top1
            top1 = (p_pn - 1) * p_ps;
            //top2：实际数据的结束位置rs<=top2
            top2 = top1 + p_ps;
            if (top2 > rows) {
                top2 = rows;
            }
            //sql
            sql = "select rownum ooooid,a.* from (" + p_sql + ") a ";
            sql = "select * from (" + sql + ") where ooooid>" + top1 + " and ooooid <=" + top2;

            Debug.debug(Debug.TYPE_SQL,sql);
            rs = stmt.executeQuery(sql);
            CachedRowSet crs = DB2_81.createCachedRowSet();
            crs.populate(rs);
            MultiResult.addElement(crs);
            rs.close();
            stmt.close();
            return MultiResult;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
    */


    /**
     * 执行一条SQL
     */
    public static boolean ExecCmd(String p_sql) throws Exception {
        Debug.debug(Debug.TYPE_SQL, p_sql);
        DatabaseConnection dbcm = new DatabaseConnection();
        java.sql.Connection conn = dbcm.getConnection();
        if (conn == null) throw new Exception("数据库连接错误！");
        try {
            Statement stmt = conn.createStatement();
            if (stmt.executeUpdate(p_sql) >= 0) {
                conn.commit();
                stmt.close();
                return true;
            } else {
                conn.rollback();
                stmt.close();
                return false;
            }
        } catch (Exception ex) {
            conn.rollback();
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }

        }
    }

    /**
     * 执行Batch
     */
    public static boolean execBatch(String[] p_sql) throws Exception {
        DatabaseConnection dbcm = new DatabaseConnection();
        java.sql.Connection conn = dbcm.getConnection();
        Statement stmt = null;
        if (conn == null) throw new Exception("数据库连接错误！");
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (int i = 0; i < p_sql.length; i++) {
                if (p_sql[i] != null) {
                    if (p_sql[i].length() > 0) {
                        stmt.addBatch(p_sql[i]);
                        Debug.debug(Debug.TYPE_SQL, p_sql[i]);
                    }
                }
            }
            stmt.executeBatch();
            conn.commit();
            stmt.close();
        } catch (Exception ex) {
            conn.rollback();
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }

        }
        return true;
    }

    public static CachedRowSet createCachedRowSet() throws SQLException {
        Locale locale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.US);
            return new CachedRowSetImpl();
        }
        finally {
            Locale.setDefault(locale);
        }
    }

}

