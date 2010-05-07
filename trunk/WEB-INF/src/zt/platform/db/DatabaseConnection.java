package zt.platform.db;
/**
 *  ����������ݿ�
 *
 *@author     <a href="mailto:wangdl@zhongtian.biz">WangDeLiang</a>
 *@created 2003��10��8��
 *@version $Revision: 1.1 $ $Date: 2007/04/28 14:08:36 $
 *@see        com.zt.db.RecordSet
 */

import zt.platform.utils.Debug;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static Logger logger = Logger.getLogger("zt.platform.db.DatabaseConnection");
    public final static String DEFAULT_DATASOURCE_URL = "CreditDB";
    private java.sql.Connection connect;
    private boolean isAuto = true;
    private String errorMsg = "";

    /**
     * �������ӣ�ʧ�����׳�����NoAvailableResourceException
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

    /**
     * ��ʼ����
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
     * �ύ����
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
     * �ع�����
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
     * ִ�����ݿ�������
     */
    public int executeUpdate(String sql) {
        if (sql == null || sql.trim().length() == 0) {
            logger.severe("DatabaseConnection.executeUpdate's sql parameter is null!!!");
            return -1000;
        }
        setErrorMsg("");
        try {
            Statement st = connect.createStatement();
            int result = st.executeUpdate(sql);
            st.close();
            return result;
        }
        catch (SQLException sqle) {
            setErrorMsg(sqle.getMessage() + sql);
            Debug.debug(sqle);
            if (sqle.getErrorCode() < 0) {
                return sqle.getErrorCode();
            } else {
                return sqle.getErrorCode() * (-1);
            }
        }
    }

    /**
     * ִ��sql��ѯ���
     */
    public RecordSet executeQuery(String sql) {
        if (sql == null || sql.trim().length() == 0) {
            logger.severe("DatabaseConnection.executeQuery's sql parameter is null!!!");
            return new RecordSet();
        }
        setErrorMsg("");
        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            RecordSet records = new RecordSet(rs);
            if (records == null) {
                return new RecordSet();
            }
            rs.close();
            st.close();
            return records;
        }
        catch (SQLException sqle) {
            setErrorMsg(sqle.getMessage() + sql);
            Debug.debug(sqle);
            return new RecordSet();
        }
    }

    /**
     * Description of the Method
     *
     * @param sql        Description of the Parameter
     * @param beginIndex Description of the Parameter
     * @param resultNo   Description of the Parameter
     * @return Description of the Return Value
     */
    public RecordSet executeQuery(String sql, int beginIndex, int resultNo) {
        if (sql == null || sql.trim().length() == 0) {
            logger.severe("DatabaseConnection.executeQuery's sql parameter is null!!!");
            return new RecordSet();
        }
        setErrorMsg("");
        try {
            Statement st = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            st.setMaxRows(beginIndex - 1 + resultNo);
            ResultSet rs = st.executeQuery(sql);
            if (beginIndex == 1) {
            } else {
                rs.absolute(beginIndex - 1);
            }
            RecordSet records = new RecordSet(rs);
            if (records == null) {
                return new RecordSet();
            }
            rs.close();
            st.close();
            return records;
        }
        catch (SQLException sqle) {
            setErrorMsg(sqle.getMessage() + sql);
            logger.severe(this.errorMsg + sql);
            Debug.debug(sqle);
            return new RecordSet();
        }
    }

    /**
     * �жϼ�¼�Ƿ����
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

    /**
     * �ж������Ƿ��Զ��ύ
     */
    public boolean isAuto() {
        return isAuto;
    }

    /**
     * ����������ύ��ʽ
     */
    public void setAuto(boolean isAuto) {
        try {
            connect.setAutoCommit(isAuto);
        }
        catch (SQLException sqle) {
        }
        this.isAuto = isAuto;
    }

    /**
     * �ر����ݿ�����,ʹ����֮��һ�����ø÷����ͷ����ݿ�����,����������Դ���˷�
     */
    protected void close() {
        try {
            connect.close();
        }
        catch (SQLException ex) {
            Debug.debug(ex);
        }
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

    /**
     * Description of the Method
     */
    public RecordSet executeQuery(PreparedStatement pst) {
        if (pst == null) {
            return new RecordSet();
        }
        setErrorMsg("");
        try {
            ResultSet rs = pst.executeQuery();
            RecordSet records = new RecordSet(rs);
            if (records == null) {
                return new RecordSet();
            }
            pst.close();
            return records;
        }
        catch (SQLException sqle) {
            setErrorMsg(sqle.getMessage());
            logger.severe(DBUtil.fromDB(sqle.getMessage()));
            Debug.debug(sqle);
            return new RecordSet();
        }
    }

    /**
     * Description of the Method
     */
    public RecordSet executeQuery(PreparedStatement pst, int beginIndex, int resultNo) {
        if (pst == null) {
            logger.severe("DatabaseConnection.executeQuery's pst parameter is null!!!");
            return new RecordSet();
        }
        setErrorMsg("");
        try {
            pst.setMaxRows(beginIndex - 1 + resultNo);
            ResultSet rs = pst.executeQuery();
            if (beginIndex != 1) {
                rs.absolute(beginIndex - 1);
            }
            RecordSet records = new RecordSet(rs, resultNo);
            if (records == null) {
                return new RecordSet();
            }
            rs.close();
            pst.close();
            return records;
        }
        catch (SQLException sqle) {
            setErrorMsg(sqle.getMessage());
            logger.severe(DBUtil.fromDB(sqle.getMessage()));
            return new RecordSet();
        }
    }

    public Connection getConnection() {
        return connect;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        if (errorMsg != null) {
            errorMsg = DBUtil.fromDB(errorMsg);
        }
        this.errorMsg = errorMsg;
    }

}
