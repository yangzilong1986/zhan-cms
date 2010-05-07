package zt.platform.cachedb;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

public class DatabaseConnection {
    public final static String DEFAULT_DATASOURCE_URL = "CreditDB";
    private java.sql.Connection connect;
    private boolean isAuto = true;
    //private String errorMsg = "";

    /**
     * 申请连接，失败则抛出例外NoAvailableResourceException
     */
    public DatabaseConnection() throws NoAvailableResourceException {
        try {
            Context initCtx = new InitialContext();
            DataSource ds = (DataSource) initCtx.lookup(DEFAULT_DATASOURCE_URL);
            connect = ds.getConnection();
        }
        catch (Exception e) {
            throw new NoAvailableResourceException(e.getMessage());
        }
    }

    public DatabaseConnection(String jndiname) throws NoAvailableResourceException {
        try {
            Context initCtx = new InitialContext();
            DataSource ds = (DataSource) initCtx.lookup(jndiname);
            connect = ds.getConnection();
        }
        catch (Exception e) {
            throw new NoAvailableResourceException(e.getMessage());
        }
    }

    /**
     * 开始事务
     */
    public void begin() {
        try {
            connect.setAutoCommit(false);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public void commit() {
        try {
            connect.commit();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 回滚事务
     */
    public void rollback() {
        try {
            connect.rollback();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 判断记录是否存在
     */
    public boolean isExist(String sql) {
        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            boolean exist = rs.next();
            st.close();
            return exist;
        }
        catch (SQLException sqle) {
            return false;
        }
    }

    public boolean isAuto() {
        return isAuto;
    }

    /**
     * 设置事务的提交方式
     */
    public void setAuto(boolean isAuto) {
        try {
            connect.setAutoCommit(isAuto);
        }
        catch (SQLException sqle) {
        }
        this.isAuto = isAuto;
    }

    public java.sql.PreparedStatement getPreparedStatement(String str) {
        try {
            PreparedStatement st = connect.prepareStatement(str, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return st;
        }
        catch (SQLException ex) {
            return null;
        }
    }

    public Connection getConnection() {
        return connect;
    }

    public void releaseConnection() {
        try {
            if (this.connect != null) {
                this.connect.close();
            }
        }
        catch (SQLException ex) {
            System.out.println("this.connect" + ex);
        }

    }
}




