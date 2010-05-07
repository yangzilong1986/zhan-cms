//Source file: e:\\java\\zt\\cmsi\\biz\\LoanLedgerMan.java

package zt.cmsi.biz;

import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 贷款台帐管理程序
 *
 * @author zhouwei
 *         $Date: 2005/06/28 07:00:35 $
 * @version 1.0
 *          <p/>
 *          版权：青岛中天公司
 */

public class LoanLedgerMan {

    /**
     * 根据业务号返回贷款台帐信息类
     *
     * @param BMNo
     * @return zt.cmsi.biz.LoanLedger
     * @roseuid 3FE6FC4D0180
     */
    public static LoanLedger getLoanLedger(String BMNo) {
        if (BMNo == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s)BMNo is null when getting Loan Ledger Data!");
            return null;
        }

        Connection con = null;
        Statement st = null;

        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            if (dc == null) {
                Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection!");
                return null;
            }

            con = dc.getConnection();
            st = con.createStatement();

            String sSql = "select * from RQLoanLedger where bmno='" + BMNo + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);

            ResultSet rs = st.executeQuery(sSql);
            LoanLedger loanledger = null;
            if (rs.next()) {
                loanledger = initDataBean(rs);
            } else {
                Debug.debug(Debug.TYPE_ERROR, "NO DB Record when getting Loan Ledger Data!");
            }
            return loanledger;
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) Debug.debug(e);
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            return null;
        } finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    /**
     * 根据表结构，初始化数据类
     *
     * @param rs
     * @return
     */
    private static LoanLedger initDataBean(ResultSet rs) {
        LoanLedger loanledger = new LoanLedger();
        try {
            UpToDateApp app = null;
            loanledger.BMNo = rs.getString("bmno");

            if (loanledger.BMNo != null) {
                app = BMTable.getUpToDateApp(loanledger.BMNo);
            }

            loanledger.actNo = rs.getString("actNo");
            loanledger.cnlNo = rs.getString("cnlNo");
            loanledger.curNo = rs.getString("curNo");
            //loanledger.BMTypeNo = new Integer(rs.getInt("typeno"));
            if (app != null) loanledger.BMTypeNo = app.bmTypeNo;

            loanledger.brhID = rs.getString("brhID");
            //loanledger.clientNo = rs.getString("clientNo");
            if (app != null) loanledger.clientNo = app.clientNo;
            //loanledger.clientName = rs.getString("CLIENTMGR");

            //loanledger.contractNo = rs.getString("contractNo");
            if (app != null) loanledger.contractNo = app.contractNo;
            //loanledger.sContractNo = rs.getString("sContractNo");
            if (app != null) loanledger.sContractNo = app.sContractNo;
            loanledger.crtRate = rs.getBigDecimal("crtRate");
            loanledger.contractAMt = rs.getBigDecimal("contractAMt");
            loanledger.nowBal = rs.getBigDecimal("nowBal");
            loanledger.payDate = util.dateToCalendar(rs.getDate("payDate"));
            loanledger.nowEndDate = util.dateToCalendar(rs.getDate("NowEndDate"));
            loanledger.endDate = util.dateToCalendar(rs.getDate("endDate"));
            loanledger.closeDate = util.dateToCalendar(rs.getDate("CloseDate"));
            loanledger.perimon = rs.getString("perimon") == null ? null : ((Integer) rs.getObject("perimon"));
            loanledger.isExtented = new Integer(rs.getInt("isExtend"));
            loanledger.isClosed = new Integer(rs.getInt("isClosed"));
            //loanledger.firstResp = rs.getString("FIRSTRESP");
            if (app != null) loanledger.firstResp = app.firstResp;
            //loanledger.firstRespPct = rs.getBigDecimal("firstRespPct");
            if (app != null) loanledger.firstRespPct = app.firstRespPct;
            loanledger.loanCat2 = new Integer(rs.getInt("loanCat2"));
            //loanledger.loanType3 = new Integer(rs.getInt("loanType3"));
            if (app != null) loanledger.loanType3 = app.loanType3;
            //loanledger.loanType5 = new Integer(rs.getInt("loanType5"));
            if (app != null) loanledger.loanType5 = app.loanType5;
        } catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return null;
        }
        return loanledger;
    }

    /*
       private static Calendar getCalendar(java.sql.Date dd) {
         Calendar c = Calendar.getInstance();
         c.set(1900 + dd.getYear(), dd.getMonth(), dd.getDate());
         return c;
       }
    */

    /**
     * @param actNo
     * @param cnlNo
     * @return zt.cmsi.biz.LoanLedger
     * @roseuid 3FE6FCA20237
     */
    public static LoanLedger getLoanLedger(String actNo, String cnlNo) {
        if (actNo == null || cnlNo == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):ACTNO or CNLNO is null when getting Loan Ledger Data!");
            return null;
        }

        Connection con = null;
        Statement st = null;

        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            if (dc == null) {
                Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection!");
                return null;
            }

            con = dc.getConnection();
            st = con.createStatement();

            String sSql = "select * from RQLoanLedger where actno='" + actNo + "' and cnlno='" + cnlNo + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);

            ResultSet rs = st.executeQuery(sSql);
            LoanLedger loanledger = null;
            if (rs.next()) {
                loanledger = initDataBean(rs);
            } else {
                Debug.debug(Debug.TYPE_ERROR, "NO DB Record when getting Loan Ledger Data using ActNo and CnlNo!");
            }
            return loanledger;
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return null;
        } finally {
            if (st != null) {
                try {
                    st.close();
                }
                catch (Exception e) {
                    Debug.debug(Debug.TYPE_WARNING, "Close Statement Failed!!!");
                    if (Debug.isDebugMode == true)
                        Debug.debug(e);
                }
            }
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    public static void main(String[] args) {
        System.out.println(LoanLedgerMan.getLoanLedger("3"));
        System.out.println(LoanLedgerMan.getLoanLedger("1", "1"));
    }
}
