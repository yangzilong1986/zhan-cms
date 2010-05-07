//Source file: e:\\java\\zt\\cmsi\\biz\\InactLoanMan.java

package zt.cmsi.biz;

import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

/**
 * ��������������
 *
 * @author zhouwei
 *         $Date: 2005/06/28 07:00:35 $
 * @version 1.0
 *          <p/>
 *          ��Ȩ���ൺ���칫˾
 */

public class InactLoanMan {

    /**
     * ����ҵ��ţ���ò��������¼
     *
     * @param BMNo
     * @return zt.cmsi.biz.InactLoan
     * @roseuid 3FE7F11D01C9
     */
    public static InactLoan getInactLoan(String BMNo) {

        if (BMNo == null) {
            Debug.debug(Debug.TYPE_ERROR,
                    "Parameter(s)BMNo is null when getting Inactive Loan Ledger Data!");
            return null;
        }

        try {
            String sSql = "select * from bminactloan where bmno='" + BMNo + "'";

            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            if (dc == null) {
                Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection!");
                return null;
            }

            Debug.debug(Debug.TYPE_SQL, sSql);
            RecordSet rs = dc.executeQuery(sSql);

            InactLoan inactloan = null;
            if (rs.next()) {
                inactloan = initDataBean(rs);
            } else {
                Debug.debug(Debug.TYPE_ERROR,
                        "No DB Record when getting Inactive Loan Ledger Data! BMNO=" + BMNo);
            }
            return inactloan;
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            return null;
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    /**
     * ���ݱ�ṹ����ʼ��������
     *
     * @param rs
     * @return
     */
    private static InactLoan initDataBean(RecordSet rs) {
        InactLoan inactloan = new InactLoan();
        try {
            inactloan.ifAccepted = rs.getBoolean("ifaccepted");
            inactloan.BMNo = rs.getString("bmno");
            inactloan.acceptDate = rs.getCalendar("acceptdate");
            inactloan.ILStatus = new Integer(rs.getInt("ilstatus"));
            inactloan.adminedBy = rs.getString("adminedby");
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            return null;
        }
        return inactloan;
    }

    /**
     * ����������ձ�
     * <p/>
     * ���Ĳ������������������
     * --��BMILNotifi���еõ�����������
     * --����BMInactLoan�е�LastNotifyDate�ֶ�
     *
     * @param NotifyNo
     * @return int
     * @roseuid 3FE7F55002D4
     */
    public static int loanNotify(int NotifyNo) {
        int errorcode = 0;
        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            if (dc == null) {
                Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection!");
                return ErrorCode.NO_DB_CONN;
            }

            String sqlStr =
                    " update bminactloan "
                            + " set lastnotifydate = (select \"DATE\" from bmilnotifi where notifno=" +
                            NotifyNo + ") "
                            + " where bmno = (select bmno from bmilnotifi where notifno=" +
                            NotifyNo + ") ";

            Debug.debug(Debug.TYPE_SQL, sqlStr);

            int result = dc.executeUpdate(sqlStr);
            if (result < 0) {
                errorcode = ErrorCode.UPDATE_LSTNOTIFIED_DATE_OF_INACTLOAN_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.UPDATE_LSTNOTIFIED_DATE_OF_INACTLOAN_FAILED;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * ����Ͳ���������
     * <p/>
     * �������������
     * --��BMPostLoanCheck�еõ����������
     * --����BMTable�е�LastCheckDate�ֶ�
     *
     * @param CheckNo
     * @return int
     * @roseuid 3FE8F0A101E8
     */
    public static int checkLoan(int CheckNo) {
        int errorcode = 0;
        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            String sqlStr =
                    " update bmtable "
                            +
                            " set lastcheckdate = (select checkdate from BMPostLoanCheck where checkno=" +
                            CheckNo + ") "
                            + " where bmno = (select bmno from BMPostLoanCheck where checkno=" +
                            CheckNo + ") ";

            Debug.debug(Debug.TYPE_SQL, sqlStr);

            int result = dc.executeUpdate(sqlStr);
            if (result < 0) {
                errorcode = ErrorCode.UPDATE_CHECK_DATE_OF_INACTLOAN_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.UPDATE_CHECK_DATE_OF_INACTLOAN_FAILED;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * 1. ����PDNO��PDTRANS�����г�����ծ�ʲ�������
     * 2. ����PDASSETS��BAL=�ֶ�ԭ�����+�ֶ�ԭ������Ϣ-���г�����ծ�ʲ����
     *
     * @param PDNo
     * @return int
     * @roseuid 3FE8F6180186
     */
    public static int processPDTrans(int PDNo) {
        int errorcode = 0;
        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            if (dc == null) {
                Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection!");
                return ErrorCode.NO_DB_CONN;
            }
            String sqlStr =
                    " update BMPDAssets "
                            + " set Bal = (Amt-(select sum(OffsetAmt) from bmpdtrans where pdno="
                            + PDNo +
                            ")), LedgerAmt= (select sum(OffsetAmt) from bmpdtrans where pdno=" +
                            PDNo + ")"
                            + " where pdno = " + PDNo;

            Debug.debug(Debug.TYPE_SQL, sqlStr);
            int result = dc.executeUpdate(sqlStr);
            if (result < 0) {
                errorcode = ErrorCode.UPDATE_TRANS_OF_ASSERT_FAILED;
            } else {
                int temp = EnumValue.PDStatus_JianLi;
                sqlStr = "select managemode from bmpdtrans where pdno=" + PDNo +
                        " order by CreateDate desc,pdtransno desc";
                Debug.debug(Debug.TYPE_SQL, sqlStr);
                RecordSet rs = dc.executeQuery(sqlStr);
                if (result < 0) {
                    errorcode = ErrorCode.UPDATE_TRANS_OF_ASSERT_FAILED;
                } else {
                    if (rs.next()) {
                        int mode = rs.getInt("managemode");
                        if (mode == EnumValue.ManageMode_BianXian) {
                            temp = EnumValue.PDStatus_BiaoXian;
                        } else {
                            temp = EnumValue.PDStatus_ZuLin;
                        }
                    }

                    sqlStr = "update BMPDAssets set pdstatus=" + temp + " where pdno=" +
                            PDNo;
                    Debug.debug(Debug.TYPE_SQL, sqlStr);
                    result = dc.executeUpdate(sqlStr);
                    if (result < 0) {
                        errorcode = ErrorCode.UPDATE_TRANS_OF_ASSERT_FAILED;
                    }

                }

            }

        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.UPDATE_TRANS_OF_ASSERT_FAILED;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * ���Ĵ���������
     * --����BMInactLoan�е�AdminedBy�ֶ�
     *
     * @param BMNo
     * @param newAdm
     * @return int
     * @roseuid 3FEA55670066
     */
    public static int loanAdmByChange(String BMNo, String newAdm) {
        int errorcode = 0;
        if (BMNo == null || newAdm == null) {
            Debug.debug(Debug.TYPE_ERROR,
                    "Parameter(s) is null when changing Loan Administrator!");
            return ErrorCode.PARAM_IS_NULL;
        }

        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            if (dc == null) {
                Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection!");
                return ErrorCode.NO_DB_CONN;
            }

            String sqlStr =
                    " update bminactloan "
                            + " set AdminedBy = '" + newAdm + "' "
                            + " where bmno = '" + BMNo + "' ";

            Debug.debug(Debug.TYPE_SQL, sqlStr);

            int result = dc.executeUpdate(sqlStr);
            if (result < 0) {
                errorcode = ErrorCode.UPDATE_ADMINBY_OF_INACTLOAN_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.UPDATE_ADMINBY_OF_INACTLOAN_FAILED;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    public static void main(String[] args) {
        //System.out.println(InactLoanMan.getInactLoan("12"));
        //InactLoanMan.loanNotify(34);
        //InactLoanMan.checkLoan(34);
        //InactLoanMan.loanAdmByChange("12","4");
        //InactLoanMan.processPDTrans(1);
    }
}
