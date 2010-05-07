package zt.cmsi.biz;

import com.zt.util.setup.SetupManager;
import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.cmsi.client.CMClient;
import zt.cmsi.client.CMClientMan;
import zt.cmsi.extrans.IOEntity;
import zt.cmsi.extrans.RequestMan;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.confitem;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.util.Calendar;

public class LoanGranted {
    public static boolean ifExistLoanGrant(String bmno) {
        boolean exist = false;
        if (bmno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter:BMNO is null when checking existance of Loan Grant Info!");
            return false;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when checking existance of Loan Grant Info!");
            return false;
        }

        LoanGrantData bmdata = null;
        try {
            String sSql = "select * from BMLoanGranted where BMNo='" + bmno + "'";

            RecordSet rs = null;
            //st = cn.createStatement();
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                exist = true;
            } else {
                //Debug.debug(Debug.TYPE_ERROR, "NO DB Record when checking existance of Loan Grant Info!");
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when checking existance of Loan Grant Data!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return exist;
        }
    }

    public static LoanGrantData getLoanGrant(String bmno) {
        if (bmno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter:BMNO is null when getting Loan Grant Info!");
            return null;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when getting Loan Grant Info!");
            return null;
        }

        LoanGrantData bmdata = null;
        try {
            String sSql = "select * from BMLoanGranted where BMNo='" + bmno + "'";

            RecordSet rs = null;
            //st = cn.createStatement();
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                bmdata = new LoanGrantData();
                bmdata.bmNo = rs.getString("BMNo");
                bmdata.brhID = rs.getString("BrhID");
                bmdata.authorizedStatus = rs.getInt("AuthorizedStatus");
                bmdata.beginDate = rs.getCalendar("AuthBeginDate");
                bmdata.endDate = rs.getCalendar("AuthEndDate");
                bmdata.upToDateApp = BMTable.getUpToDateApp(bmno);
                String clientno = null;
                BMTableData ddd = BMTable.getBMTable(bmno);
                if (ddd != null) {
                    clientno = ddd.clientNo;
                }
                if (clientno != null) {
                    bmdata.cmClient = CMClientMan.getCMClient(clientno);
                }
            } else {
                Debug.debug(Debug.TYPE_ERROR, "NO DB Record when getting Loan Grant Info!");
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when getting Loan Grant Data!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            if (bmdata != null) {
                if (bmdata.upToDateApp == null) {
                    Debug.debug(Debug.TYPE_ERROR, "Not found UpToDateApp Info when getting Loan Grant Info!BMNO=" + bmno);
                    bmdata = null;
                }
            }

            if (bmdata != null) {
                if (bmdata.cmClient == null) {
                    Debug.debug(Debug.TYPE_ERROR,
                            "Not found Client Info when getting Loan Grant Info!BMNO=" + bmno);
                    bmdata = null;
                }
            }

            MyDB.getInstance().apReleaseConn(0);
            return bmdata;
        }
    }

    /**
     * @param BMNo
     * @param Operator
     * @param BrhID
     * @return int
     * @roseuid 3FE934A30193
     * this funcdtion is called when first Loan passes Authorization Step, or Authorization Recreation for Loan without BMNO
     */
    public static int createGrant(String BMNo, String Operator, String BrhID) {
        if (BMNo == null || BrhID == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        if (SetupManager.getProperty(confitem.GRANT_MODULE + ":" +
                confitem.GRANT_DAYS) == null) {
            return ErrorCode.GRANT_DAYS_NOT_DEFINE;
        }

        int days = SetupManager.getIntProperty(confitem.GRANT_MODULE + ":" +
                confitem.GRANT_DAYS);
        if (days < 0) {
            return ErrorCode.GRANT_DAYS_INVALID;
        }

        //if (SCUser.isExist(Operator) == false)
        //  return ErrorCode.USER_NOT_FOUND;

        if (SCBranch.isExist(BrhID) == false) {
            return ErrorCode.BRANCH_NOT_FOUND;
        }

        if (LoanGranted.ifExistLoanGrant(BMNo) == true) {
            return ErrorCode.GRANT_RECORD_ALREADY_EXIST;
        }
        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        Calendar toDate = SystemDate.getSystemDate1();
        toDate.add(Calendar.DATE, days);

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        int errorcode = 0;

        try {
            String sSql = "insert into BMLoanGranted(BMNo,CreateDate,LastModified,AuthorizedStatus,AuthBeginDate,AuthEndDate,Operator,BrhID) values('" +
                    BMNo + "','" + sysdate + "','" + sysdate + "'," +
                    EnumValue.AuthorizedStatus_WeiShouQuan + ",'" + sysdate + "','" +
                    util.calToString(toDate, "-") + "','" +
                    Operator + "','" + BrhID + "')";

            //st = cn.createStatement();
            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "Failed to create BMLoanGranted!");
                errorcode = ErrorCode.DB_INSERT_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating BMLoanGranted!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    /**
     * @param BMNo
     * @param Operator
     * @return int
     * @roseuid 3FE934BF028D
     * this functions only should be called when LoanGrant Sending thread send revoke successfully.
     */
    public static int cancelGrant(String BMNo) {
        if (BMNo == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        if (LoanGranted.ifExistLoanGrant(BMNo) == false) {
            return ErrorCode.GRANT_RECORD_NOT_EXIST;
        }

        int errorcode = 0;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        try {
            String sSql = "update BMLoanGranted set AuthorizedStatus=" + EnumValue.AuthorizedStatus_ShouQuanQuXiao +
                    ",LastModified='" + sysdate + "' where BMNo='" + BMNo + "'";

            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "Failed to cancel BMLoanGranted!");
                errorcode = ErrorCode.DB_INSERT_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when cancelling BMLoanGranted!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * @param BMNo
     * @param Operator
     * @return int
     * @roseuid 3FE934BF028D
     * this functions only should be called when LoanGrant Sending thread send grant successfully.
     */
    public static int sendGrantOK(String BMNo) {
        if (BMNo == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        if (LoanGranted.ifExistLoanGrant(BMNo) == false) {
            return ErrorCode.GRANT_RECORD_NOT_EXIST;
        }
        int errorcode = 0;

        try {
            String sSql = "update BMLoanGranted set AuthorizedStatus=" + EnumValue.AuthorizedStatus_YiShouQuan +
                    ",LastModified='" + sysdate + "' where BMNo='" + BMNo + "'";

            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "Failed to update BMLoanGranted status to Authorized!");
                errorcode = ErrorCode.DB_INSERT_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when updating BMLoanGranted status to Authorized!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * As for the occured loan from core banking system that are without CMS loan grant info,
     * user pick up an existing Client for each of them and then call this function.
     * the table concerning include BTErrLedger, CMClient
     *
     * @param AccNo
     * @param CnlNo
     * @return int
     * @roseuid 3FEAB6D3028F
     */

    static public int reIssueGrant(String bmno, String operator) {
        if (bmno == null || operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        BTErrLedgerData ap = BMErrLedgerMan.getErrLedger(bmno);
        if (ap == null) {
            return ErrorCode.BT_ERR_LEDGER_NOT_FOUND;
        }
        if (ap.sBMNo != null && ap.sBMNo.trim().length() <= 0) {
            ap.sBMNo = null;
        }
        if (ap.clientNo != null && ap.clientNo.trim().length() <= 0) {
            ap.clientNo = null;

        }

        LoanGrantData bmdata = null;
        int errorcode = 0;

        try {
            if (ap.ProcStatus.intValue() == EnumValue.ProcStatus_WeiChuLi) {
                if (ap.clientNo == null && ap.sBMNo != null) {
                    errorcode = LoanGranted.reIssueGrantForBMNo(ap.sBMNo, bmno, operator);
                } else if (ap.clientNo != null && ap.sBMNo == null) {
                    errorcode = LoanGranted.reIssueGrantForClient(bmno, ap.clientNo, operator);
                } else {
                    errorcode = ErrorCode.BT_ERR_LEDGER_BOTH_CLIENT_BMNO;
                }
            } else {
                errorcode = ErrorCode.BT_ERR_LEDGER_STATSUS_NOT_UNPROCESSED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when reissuing Loan Grant Data!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            return errorcode;
        }

    }

    /**
     * As for the occured loan from core banking system that are without CMS loan grant info,
     * user pick up an existing Client for each of them and then call this function.
     * the table concerning include BTErrLedger, CMClient
     *
     * @param AccNo
     * @param CnlNo
     * @return int
     * @roseuid 3FEAB6D3028F
     */
    static protected int reIssueGrantForClient(String bmno, String clientno, String operator) {
        if (bmno == null || clientno == null || operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        BTErrLedgerData data = BMErrLedgerMan.getErrLedger(bmno);
        if (data == null) {
            return ErrorCode.GET_BTERRLEDGER_FAILED;
        }

        LoanLedger ledger = LoanLedgerMan.getLoanLedger(bmno);
        if (ledger == null) {
            return ErrorCode.LOAN_LEDGER_REC_NOT_FOUND;
        }

        if (data.ProcStatus.intValue() != EnumValue.ProcStatus_WeiChuLi) {
            return ErrorCode.ERRLEDGER_STATUS_NOT_UNPROCESSED;
        }

        CMClient client = CMClientMan.getCMClient(clientno);
        if (client == null) {
            return ErrorCode.CLIENT_NOT_FOUND;
        }

        if (SCUser.isExist(operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }
        if (SCBranch.isExist(data.brhID) == false) {
            return ErrorCode.BRANCH_NOT_FOUND;
        }

        int errorcode = 0;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        UpToDateApp newappdata = null;
        String newbmno = null;

        try {
            if (errorcode >= 0) {
                newbmno = BMTable.createBMTable(bmno, EnumValue.BMType_BuShouQuan,
                        clientno,
                        data.brhID,
                        data.brhID, operator);
                if (newbmno == null) {
                    errorcode = ErrorCode.CAN_NOT_CREATE_BMTABLE;
                } else {
                    newappdata = new UpToDateApp();
                    newappdata.finalAmt = ledger.nowBal;

                    newappdata.finalEndDate = ledger.nowEndDate;
                    if (newappdata.finalEndDate == null) {
                        newappdata.finalEndDate = ledger.endDate;
                    }
                    if (newappdata.finalEndDate == null) {
                        newappdata.finalEndDate = ledger.closeDate;

                    }
                    newappdata.finalStartDate = ledger.payDate;
                    newappdata.finalRate = ledger.crtRate;
                    newappdata.clientNo = clientno;
                    newappdata.bmTypeNo = new Integer(EnumValue.BMType_BuShouQuan);
                    newappdata.finalCurNo = ledger.curNo;
                    newappdata.finalMonths = ledger.perimon;

                    errorcode = BMTable.updateUpToDateApp(newbmno, newappdata);
                    if (errorcode >= 0) {
                        errorcode = LoanGranted.createGrant(newbmno, operator, data.brhID);
                        if (errorcode >= 0) {
                            errorcode = BMErrLedgerMan.finishErrLedgerProcess(bmno);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when recreating Loan Grant!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * As for the occured loan from core banking system that are without CMS loan grant info,
     * user pick up an existing BMNO for each of them and then call this function.
     * the table concerning include BTErrLedger, BMTable
     *
     * @param Accno
     * @param CnlNo
     * @return int
     * @roseuid 3FEAB77E0226
     */
    static protected int reIssueGrantForBMNo(String bmno, String newbmno, String operator) {
        int errorcode = 0;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB connection when reIssueGrantForBMNo!");
            return ErrorCode.NO_DB_CONN;
        }

        BTErrLedgerData data = BMErrLedgerMan.getErrLedger(newbmno);
        if (data == null) {
            return ErrorCode.GET_BTERRLEDGER_FAILED;
        }

        BMTableData bmtbl = BMTable.getBMTable(bmno);
        if (bmtbl == null) {
            return ErrorCode.GET_BMTABLE_ERROR;
        }

        try {
            String ttt = BMTable.createBMTable(newbmno, EnumValue.BMType_BuShouQuan,
                    bmtbl.clientNo,
                    data.brhID,
                    data.brhID, operator);

            if (ttt == null) {
                errorcode = ErrorCode.CAN_NOT_CREATE_BMTABLE;
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateBMNo(bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateTableStatus(newbmno, EnumValue.BMStatus_FaFang, operator);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.cancelBMTable(bmno, EnumValue.BMStatus_QuXiao, operator);
            }
            if (errorcode >= 0) {
                errorcode = BMErrLedgerMan.finishErrLedgerProcess(newbmno);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when reIssueGrantForBMNo!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * phycically send Loan Grant to pool so that Loan Granting Thread can send them to
     * core banking system
     * bmno: business no
     * grantorrevoke: true: grant loan to client, false -- revoke loan from client
     * <p/>
     * retcode: if send to pool sucessfully. >= 0: OK, otherwise failed
     * this function is used to resend or cancel Grant that have been sent by CMS
     */
    static public int sendLoanGrant(String bmno, boolean grantorrevoke) {
        LoanGrantData grantdata = LoanGranted.getLoanGrant(bmno);
        if (grantdata == null) {
            return ErrorCode.GRANT_RECORD_NOT_EXIST;
        }
        if (grantdata.authorizedStatus == EnumValue.AuthorizedStatus_ShouQuanQuXiao
                && grantorrevoke != false) {
            return ErrorCode.GRNAT_HAS_BEEN_CANCELLED;
        }
        if (grantorrevoke == false && grantdata.authorizedStatus != EnumValue.AuthorizedStatus_YiShouQuan) {
            return ErrorCode.GRNAT_MUST_BE_YISHOUQUAN;
        }

        long tag = 0;
        if (bmno == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        IOEntity io = new IOEntity();
        io.BMNo = bmno;
        io.ifCreateGrant = grantorrevoke;

        tag = RequestMan.getInstance().addRequest(io);
        if (grantorrevoke == true) { //grant authorization
            if (tag < 0) {
                return ErrorCode.GRANT_SEND_REQUEST_FAILED;
            } else {
                return 0;
            }
        } else { //revoke authorization
            if (tag < 0) {
                return ErrorCode.GRANT_SEND_REQUEST_FAILED;
            } else {
                IOEntity ret = null;
                ret = RequestMan.getInstance().getResponse(tag);
                if (ret == null) {
                    return ErrorCode.SEND_AUTHORIZATION_TIME_OUT;
                } else if (ret.sendOK == false) {
                    return ErrorCode.SEND_AUTHORIZATION_RET_ERROR;
                } else {
                    return 0;
                }
            }
        }
    }

    static public int addAllUnsendGrant() {
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when checking existance of Loan Grant Info!");
            return ErrorCode.NO_DB_CONN;
        }

        LoanGrantData bmdata = null;
        String bmno = null;
        int errorcode = 0;
        try {
            String sSql = "select BMLoanGranted.BMNo, AuthorizedStatus,BMStatus from BMLoanGranted,BMTable where BMLoanGranted.BMNo=BMTable.BMNo and AuthorizedStatus=" +
                    EnumValue.AuthorizedStatus_WeiShouQuan + " and BMStatus<" + EnumValue.BMStatus_FaFang;
            RecordSet rs = null;
            rs = dc.executeQuery(sSql);
            while (rs.next()) {
                bmno = rs.getString("BMNo");
                if (LoanGranted.sendLoanGrant(bmno, true) < 0) {
                    Debug.debug(Debug.TYPE_ERROR, "Failed to Send LoanGrant Info when Grant-Sending thread starts!BMNO=" + bmno);
                    errorcode = ErrorCode.NOT_ALL_GRANT_SEND_OK;
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when send all Loan Grant Data when Grant-Sending thread starts!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return errorcode;
        }
    }

    static public void main(String[] args) {
        System.out.println(SCBranch.getSupBrh("907010130", EnumValue.BrhLevel_XinYongShe));
        //System.out.println(LoanGranted.createGrant("00000000017","system","907010000"));
        //System.out.println(LoanGranted.cancelGrant("00000000014"));
        //System.out.println(LoanGranted.sendLoanGrant("00000000014", true));
        System.out.println(LoanGranted.reIssueGrant("00000000010", "system"));
    }
}
