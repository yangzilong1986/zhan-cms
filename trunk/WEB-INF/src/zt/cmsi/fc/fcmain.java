package zt.cmsi.fc;

/**
 * <p>Title: New Porgram</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Dreem</p>
 * @author JGO(GZL)
 * @version 1.1
 */

import zt.cms.pub.SCBranch;
import zt.cmsi.client.CMClient;
import zt.cmsi.client.CMClientMan;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.code.FCMAIN;
import zt.cmsi.pub.define.BMFCLimit;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.cachedb.ConnectionManager;
import zt.platform.db.DBUtil;
import zt.platform.utils.Debug;

import javax.sql.rowset.CachedRowSet;
import java.math.BigDecimal;
import java.sql.Date;

public class fcmain {

    private static int pastduedays = 0;

    static public int getFCType(int typeno, boolean ifindv, String brhid, BigDecimal nowbal) {
        int fctype;
//        if (typeno == EnumValue.BMType_DaiKuanZhengDaiKuan || typeno == EnumValue.BMType_GeRenZhuXueDaiKuan)
//            fctype = EnumValue.FCType_ZiRanRenNongHu;
//        else if (ifindv == true)
//            fctype = EnumValue.FCType_ZiRanRenQiTa;
//        else
//            fctype = EnumValue.FCType_QiYe;

        try {
            if (ifindv == true) {
                BigDecimal limit = BMFCLimit.getInstance().getLimitofBrh(brhid);
                if (limit == null) fctype = EnumValue.FCType_ZiRanRenNongHu;
                else {
                    if (limit.compareTo(nowbal) > 0)
                        fctype = EnumValue.FCType_ZiRanRenNongHu;
                    else
                        fctype = EnumValue.FCType_ZiRanRenQiTa;
                }
            } else
                fctype = EnumValue.FCType_QiYe;
        }
        catch (Exception e) {
            Debug.debug(e);
            fctype = EnumValue.FCType_ZiRanRenNongHu;
        }

        return fctype;
    }

    static private int calcDays(Date date1, Date date2) {
        if (date1 == null || date2 == null) return 0;

        int days = 0;

        days += (date1.getYear() - date2.getYear()) * 365;
        days += (date1.getMonth() - date2.getMonth()) * 30;
        days += date1.getDay() - date2.getDay();

        if (days < 0)
            return 0;
        else
            return days;
    }

    static public int intDays(String date1, String date2) {
        if (date1 == null || date2 == null)
            return 0;
        else {
            date1 = date1.trim().replaceAll("-", "");
            date2 = date2.trim().replaceAll("-", "");
            if (date1.length() != 8 || date2.length() != 8) return 0;
            int days = 0;

            days += (Integer.parseInt(date1.substring(0, 4)) - Integer.parseInt(date2.substring(0, 4))) * 365;
            days += (Integer.parseInt(date1.substring(4, 6)) - Integer.parseInt(date2.substring(4, 6))) * 30;
            days += (Integer.parseInt(date1.substring(6, 8)) - Integer.parseInt(date2.substring(6, 8)));

            if (days < 0)
                return 0;
            else
                return days;
        }
    }


    static public int autoFCRating(String strbmno, int fctype, int bmtype, int loantype3, int creditclass, Date duedate, int oldloancat1) {
        int ret = 0;
        Date today = SystemDate.getSystemDate4();

        ConnectionManager manager = ConnectionManager.getInstance();
        try {
            CachedRowSet rs = manager.getRs("select oricrtdt from rqdueintrst where bmno='" + strbmno + "'");
            if (rs.next() && rs.getDate("oricrtdt") != null) duedate = rs.getDate("oricrtdt");
        } catch (Exception e) {
            Debug.debug(e);
            return 0;
        }

        fcmain.pastduedays = fcmain.calcDays(today, duedate);

        if (fctype == EnumValue.FCType_QiYe) {
            if (pastduedays <= 0)
                ret = EnumValue.LoanCat1_ZhengChang;
            else if (pastduedays <= 90)
                ret = EnumValue.LoanCat1_GuanZhu;
            else if (pastduedays <= 360)
                ret = EnumValue.LoanCat1_CiJi;
            else if (pastduedays >= 361) ret = EnumValue.LoanCat1_KeYi;
        } else {
            if (bmtype == EnumValue.BMType_GeRenZhuFangXiaoFeiDaiKuan || bmtype == EnumValue.BMType_GeRenQiCheXiaoFeiDaiKuan || bmtype == EnumValue.BMType_GeRenShangFangDaiKuan) {
                if (pastduedays <= 0)
                    ret = EnumValue.LoanCat1_ZhengChang;
                else if (pastduedays <= 90)
                    ret = EnumValue.LoanCat1_GuanZhu;
                else if (pastduedays <= 180)
                    ret = EnumValue.LoanCat1_CiJi;
                else if (pastduedays >= 181) ret = EnumValue.LoanCat1_KeYi;
            } else {
                if (loantype3 == EnumValue.LoanType3_DiYa) {
                    if (pastduedays <= 30)
                        ret = EnumValue.LoanCat1_ZhengChang;
                    else if (pastduedays <= 90)
                        ret = EnumValue.LoanCat1_GuanZhu;
                    else if (pastduedays <= 360)
                        ret = EnumValue.LoanCat1_CiJi;
                    else if (pastduedays >= 361) ret = EnumValue.LoanCat1_KeYi;
                } else if (loantype3 == EnumValue.LoanType3_ZhiYa) {
                    ret = EnumValue.LoanCat1_ZhengChang;
                } else {
                    if (creditclass == EnumValue.CreditClass_AAA) {
                        if (pastduedays <= 90)
                            ret = EnumValue.LoanCat1_ZhengChang;
                        else if (pastduedays <= 180)
                            ret = EnumValue.LoanCat1_GuanZhu;
                        else if (pastduedays <= 360)
                            ret = EnumValue.LoanCat1_CiJi;
                        else if (pastduedays >= 361) ret = EnumValue.LoanCat1_KeYi;
                    } else if (creditclass == EnumValue.CreditClass_AA) {
                        if (pastduedays <= 30)
                            ret = EnumValue.LoanCat1_ZhengChang;
                        else if (pastduedays <= 90)
                            ret = EnumValue.LoanCat1_GuanZhu;
                        else if (pastduedays <= 360)
                            ret = EnumValue.LoanCat1_CiJi;
                        else if (pastduedays >= 361) ret = EnumValue.LoanCat1_KeYi;
                    } else {
                        if (pastduedays <= 0)
                            ret = EnumValue.LoanCat1_ZhengChang;
                        else if (pastduedays <= 90)
                            ret = EnumValue.LoanCat1_GuanZhu;
                        else if (pastduedays <= 360)
                            ret = EnumValue.LoanCat1_CiJi;
                        else if (pastduedays >= 361) ret = EnumValue.LoanCat1_KeYi;
                    }
                }
            }
        }

        return ret;
    }

    static public FCParam createFC(String bmno, boolean confirmed, String operator) {

        FCParam fp = new FCParam();
        fp.errorCode = 0;
        if (bmno != null) bmno = bmno.trim();
        if (bmno.length() <= 0) bmno = null;
        if (bmno == null) {
            fp.errorCode = ErrorCode.PARAM_IS_NULL;
            return fp;
        }

        int errorcode = 0;
        CachedRowSet rs = null, rs3 = null;
        sun.jdbc.rowset.CachedRowSet rs2 = null;
        int fctype = 0;
        int autofc = 0;

        ConnectionManager manager = null;
        try {
            manager = ConnectionManager.getInstance();
            if (manager == null) {
                fp.errorCode = ErrorCode.NO_DB_CONN;
                errorcode = ErrorCode.NO_DB_CONN;
                return fp;
            }

            rs = manager.getRs("select fcno from fcmain where bmno='" + bmno +
                    "' and fcstatus <> " + EnumValue.FCStatus_WanCheng + " and fcstatus<>" + EnumValue.FCStatus_QuXiao);

            if (rs.next()) {
                if (confirmed == false) {
                    errorcode = 1;
                    fp.errorCode = 1;
                    return fp;
                }
            }

            rs = manager.getRs("select rqloanledger.*, bmtable.typeno,bmtable.clientno,bmtableapp.loantype3,bmtableapp.firstresp "
                    + " from rqloanledger,bmtable,bmtableapp where rqloanledger.bmno='" + bmno + "' and rqloanledger.bmno=bmtable.bmno and "
                    + " bmtableapp.bmno = bmtable.bmno");

            if (rs.next()) {
                String operbrhid = SCBranch.getSupBrh(rs.getString("brhid"), EnumValue.BrhLevel_XinYongShe);

                if (operbrhid == null) {
                    fp.errorCode = errorcode = ErrorCode.GET_UP_BRH_ERROR;

                }

                CMClient client = CMClientMan.getCMClient(rs.getString("clientno"));
                if (client == null) fp.errorCode = errorcode = ErrorCode.CLIENT_NOT_FOUND;


                String fcno = FCMAIN.getNextNo();
                if (fcno == null) fp.errorCode = errorcode = ErrorCode.NEXT_FCNO_IS_NULL;

                java.sql.Date today = SystemDate.getSystemDate4();
                if (today == null) fp.errorCode = errorcode = ErrorCode.BUSI_DATE_NOT_FOUND;


                if (errorcode >= 0) {
                    int fc1, fc2, fc3, fcclass;
                    fc1 = fc2 = fc3 = fcclass = 0;

                    fctype = fcmain.getFCType(rs.getInt("typeno"), client.ifIndv, rs.getString("brhid"), rs.getBigDecimal("nowbal"));

                    autofc = fcmain.autoFCRating(bmno, fctype, rs.getInt("typeno"), rs.getInt("loantype3"), client.creditClass.intValue(), rs.getDate("nowenddate"), rs.getInt("loancat1"));

                    rs3 = manager.getRs("Select fc1,fc2,fc3 from fcmain t where t.bmno='" + bmno + "' and t.fcstatus=" + EnumValue.FCStatus_WanCheng + " order by createdate desc");
                    if (rs3.next()) {
                        fc1 = rs3.getInt("fc1");
                        fc2 = rs3.getInt("fc2");
                        fc3 = rs3.getInt("fc3");
                        if (fc1 <= 0) fc1 = autofc;
                        if (fc2 <= 0) fc2 = fc1;
                        if (fc3 <= 0) fc3 = fc2;
                        fcclass = rs.getInt("loancat1");
                    } else {
                        fc1 = fc2 = fc3 = fcclass = autofc;
                    }

                    rs2 = manager.getRsForUpdate("SELECT * FROM FCMAIN WHERE 1<>1", "FCMAIN");
                    rs2.moveToInsertRow();

                    rs2.updateString("fcno", fcno);
                    rs2.updateString("bmno", bmno);
                    rs2.updateString("brhid", rs.getString("brhid"));
                    rs2.updateString("operbrhid", operbrhid);
                    rs2.updateDate("createdate", today);
                    rs2.updateInt("fccrttype", EnumValue.FCCrtType_FeiShiDianQingFen);
                    rs2.updateInt("fctype", fctype);
                    rs2.updateInt("fcstatus", EnumValue.FCStatus_DengJi);
                    rs2.updateInt("bmtype", rs.getInt("typeno"));
                    rs2.updateInt("clienttype", client.clientType.intValue());
                    rs2.updateString("clientname", DBUtil.toDB(client.name));
                    rs2.updateString("idno", client.ID);
                    rs2.updateString("cnlno", rs.getString("cnlno"));
                    rs2.updateBigDecimal("bal", rs.getBigDecimal("nowbal"));
                    rs2.updateDate("paydate", rs.getDate("paydate"));
                    rs2.updateDate("duedate", rs.getDate("nowenddate"));
                    rs2.updateInt("isextend", rs.getInt("isextend"));
                    rs2.updateInt("loancat2", rs.getInt("loancat2"));
                    rs2.updateInt("loantype3", rs.getInt("loantype3"));
                    rs2.updateString("firstresp", rs.getString("firstresp"));
                    rs2.updateString("curno", rs.getString("curno"));
                    rs2.updateInt("creditclass", client.creditClass.intValue());
                    rs2.updateInt("fcauto", autofc);
//                    rs2.updateInt("fc1", fc1);
//                    rs2.updateInt("fc2", fc2);
//                    rs2.updateInt("fc3", fc3);
                    rs2.updateInt("fcclass", fcclass);
                    rs2.updateInt("pastduedays", fcmain.pastduedays);

                    rs2.updateString("operator", operator);
                    rs2.updateDate("lastmodified", today);
                    rs2.insertRow();
                    rs2.moveToCurrentRow();

                    rs2.acceptChanges();

                    fp.fcNo = fcno;
                    fp.bmNo = bmno;
                    fp.brhId = rs.getString("brhid");
                    fp.operBrhId = operbrhid;
                    fp.fcCrtType = EnumValue.FCCrtType_FeiShiDianQingFen;
                    fp.fcType = fctype;
                    fp.fcStatus = EnumValue.FCStatus_DengJi;
                    fp.bmType = rs.getInt("typeno");
                    fp.clientType = client.clientType.intValue();

                    //System.out.println("===================here=======fcno"+fcno+"========"+bmno+"=="+client.name);
                }
            } else {
                fp.errorCode = errorcode = ErrorCode.LOAN_LEDGER_REC_NOT_FOUND;
            }
        } catch (Exception e) {
            Debug.debug(e);
            fp.errorCode = errorcode = ErrorCode.EXCPT_FOUND;
            return fp;
        } catch (Throwable ex) {
            ex.printStackTrace();
            fp.errorCode = errorcode = ErrorCode.EXCPT_FOUND;
            return fp;
        } finally {
            try {
                if (rs != null) rs.close();
                if (rs2 != null) rs2.close();
                if (rs3 != null) rs3.close();
            } catch (Exception e) {
                Debug.debug(e);
            }
            return fp;
        }

    }

    static public FCParam createBillDisFC(int billdisno, boolean confirmed, String operator) {

        FCParam fp = new FCParam();
        fp.errorCode = 0;

        String bmno = null;

        int errorcode = 0;
        CachedRowSet rs = null, rs3 = null, rs4 = null;
        sun.jdbc.rowset.CachedRowSet rs2 = null;
        int fctype = 0;
        int autofc = 0;

        ConnectionManager manager = null;
        try {
            manager = ConnectionManager.getInstance();
            if (manager == null) {
                fp.errorCode = errorcode = ErrorCode.NO_DB_CONN;
                return fp;
            }


            rs = manager.getRs("select bmbilldis.*, bmtableapp.loantype3,bmtableapp.firstresp "
                    + " from bmbilldis,bmtableapp where bmbilldis.billdisno=" + billdisno + " and bmtableapp.bmno=bmbilldis.bmno"
                    + " and (billdisstatus=" + EnumValue.BillDisStatus_TieXian + " or billdisstatus=" + EnumValue.BillDisStatus_ZhuanTieXian + ")");

            if (rs.next()) {
                bmno = rs.getString("bmno");
                if (bmno == null)
                    fp.errorCode = errorcode = ErrorCode.NOT_FOUND_BMNO_IN_ACPTBILLLEDGER;
                else {
                    rs4 = manager.getRs("select fcno from fcmain where bmno='" + rs.getString("bmno")
                            + "' and billno=" + billdisno +
                            " and fcstatus <> " + EnumValue.FCStatus_WanCheng +
                            " and fcstatus<>" + EnumValue.FCStatus_QuXiao);

                    if (rs4.next()) {
                        if (confirmed == false) {
                            fp.errorCode = errorcode = 1;
                            return fp;
                        }
                    }
                }


                String operbrhid = SCBranch.getSupBrh(rs.getString("brhid"), EnumValue.BrhLevel_XinYongShe);

                if (operbrhid == null) fp.errorCode = errorcode = ErrorCode.GET_UP_BRH_ERROR;

                CMClient client = CMClientMan.getCMClient(rs.getString("clientno"));
                //if(client == null) errorcode = ErrorCode.CLIENT_NOT_FOUND;


                String fcno = FCMAIN.getNextNo();
                if (fcno == null) fp.errorCode = errorcode = ErrorCode.NEXT_FCNO_IS_NULL;

                java.sql.Date today = SystemDate.getSystemDate4();
                if (today == null) fp.errorCode = errorcode = ErrorCode.BUSI_DATE_NOT_FOUND;


                if (errorcode >= 0) {
                    int fc1, fc2, fc3, fcclass;
                    fc1 = fc2 = fc3 = fcclass = 0;
                    int typeno = 0;

                    if (rs.getInt("billdisstatus") == EnumValue.BillDisStatus_TieXian)
                        typeno = EnumValue.BMType_TieXian;
                    else
                        typeno = EnumValue.BMType_ZhuanTieXian;

                    fctype = EnumValue.FCType_QiYe;

                    autofc = fcmain.autoFCRating(bmno, fctype, typeno, rs.getInt("loantype3"),
                            client != null ? client.creditClass.intValue() : EnumValue.CreditClass_Wu, rs.getDate("duedate"), rs.getInt("loancat1"));

                    rs3 = manager.getRs("Select fc1,fc2,fc3 from fcmain t where t.bmno='" + bmno + "' and billno=" + billdisno + " and t.fcstatus=" + EnumValue.FCStatus_WanCheng + " order by createdate desc");
                    if (rs3.next()) {
                        fc1 = rs3.getInt("fc1");
                        fc2 = rs3.getInt("fc2");
                        fc3 = rs3.getInt("fc3");
                        if (fc1 <= 0) fc1 = autofc;
                        if (fc2 <= 0) fc2 = fc1;
                        if (fc3 <= 0) fc3 = fc2;
                        fcclass = rs.getInt("loancat1");
                    } else {
                        fc1 = fc2 = fc3 = fcclass = autofc;
                    }

                    rs2 = manager.getRsForUpdate("SELECT * FROM FCMAIN WHERE 1<>1", "FCMAIN");
                    rs2.moveToInsertRow();

                    rs2.updateString("fcno", fcno);
                    rs2.updateString("bmno", bmno);
                    rs2.updateString("brhid", rs.getString("brhid"));
                    rs2.updateString("operbrhid", operbrhid);
                    rs2.updateDate("createdate", today);
                    rs2.updateInt("fccrttype", EnumValue.FCCrtType_FeiShiDianQingFen);
                    rs2.updateInt("fctype", fctype);
                    rs2.updateInt("fcstatus", EnumValue.FCStatus_DengJi);
                    rs2.updateInt("bmtype", typeno);
                    rs2.updateInt("clienttype", client != null ? client.clientType.intValue() : EnumValue.ClientType_QiTa);
                    rs2.updateString("clientname", (typeno != EnumValue.BMType_TieXian && rs.getString("redisby") != null) ? rs.getString("redisby") : rs.getString("clientname"));
                    if (client != null) rs2.updateString("idno", client.ID);
                    if (rs.getString("banknoteno") != null) rs2.updateString("cnlno", rs.getString("banknoteno"));

                    if (rs.getBigDecimal("actdisamt") != null)
                        rs2.updateBigDecimal("bal", rs.getBigDecimal("actdisamt"));
                    else
                        rs2.updateBigDecimal("bal", new BigDecimal(0));

                    rs2.updateDate("paydate", rs.getDate("disdate"));
                    rs2.updateDate("duedate", rs.getDate("duedate"));
                    rs2.updateInt("isextend", 0);
                    rs2.updateInt("loancat2", EnumValue.LoanCat2_ZhengChang);
                    rs2.updateInt("loantype3", rs.getString("loantype3") != null ? rs.getInt("loantype3") : EnumValue.LoanType3_XinYong);
                    rs2.updateString("firstresp", rs.getString("firstresp"));
                    rs2.updateString("curno", "01");
                    rs2.updateInt("creditclass", client != null ? client.creditClass.intValue() : EnumValue.CreditClass_Wu);
                    rs2.updateInt("fcauto", autofc);
//                    rs2.updateInt("fc1", fc1);
//                    rs2.updateInt("fc2", fc2);
//                    rs2.updateInt("fc3", fc3);
                    rs2.updateInt("fcclass", fcclass);
                    rs2.updateInt("pastduedays", fcmain.pastduedays);

                    rs2.updateString("operator", operator);
                    rs2.updateDate("lastmodified", today);
                    rs2.updateInt("billno", billdisno);
                    rs2.insertRow();
                    rs2.moveToCurrentRow();

                    rs2.acceptChanges();

                    fp.fcNo = fcno;
                    fp.bmNo = bmno;
                    fp.brhId = rs.getString("brhid");
                    fp.operBrhId = operbrhid;
                    fp.fcCrtType = EnumValue.FCCrtType_FeiShiDianQingFen;
                    fp.fcType = fctype;
                    fp.fcStatus = EnumValue.FCStatus_DengJi;
                    fp.bmType = typeno;
                    fp.clientType = client != null ? client.clientType.intValue() : EnumValue.ClientType_QiTa;

                }
            } else {
                fp.errorCode = errorcode = ErrorCode.NOT_FOUND_BMNO_IN_BILLDISLEDGER;
            }
        } catch (Exception e) {
            Debug.debug(e);
            fp.errorCode = errorcode = ErrorCode.EXCPT_FOUND;
            return fp;
        } catch (Throwable ex) {
            ex.printStackTrace();
            fp.errorCode = errorcode = ErrorCode.EXCPT_FOUND;
            return fp;
        } finally {
            try {
                if (rs != null) rs.close();
                if (rs2 != null) rs2.close();
                if (rs3 != null) rs3.close();
                if (rs4 != null) rs4.close();
            } catch (Exception e) {
                Debug.debug(e);
            }
            return fp;
        }

    }

    static public FCParam createBillFC(int billdisno, boolean confirmed, String operator) {

        FCParam fp = new FCParam();
        fp.errorCode = 0;

        String bmno = null;

        int errorcode = 0;
        CachedRowSet rs = null, rs3 = null, rs4 = null;
        sun.jdbc.rowset.CachedRowSet rs2 = null;
        int fctype = 0;
        int autofc = 0;

        ConnectionManager manager = null;
        try {
            manager = ConnectionManager.getInstance();

            if (manager == null) {
                fp.errorCode = errorcode = ErrorCode.NO_DB_CONN;
                return fp;
            }


            rs = manager.getRs("select bmacptbill.*, bmtableapp.loantype3,bmtableapp.firstresp "
                    + " from bmacptbill,bmtableapp where bmacptbill.acptbillno=" + billdisno + " and bmtableapp.bmno=bmacptbill.bmno"
                    + " and acptbillstatus=" + EnumValue.AcptBillStatus_FaFang);

            if (rs.next()) {
                bmno = rs.getString("bmno");
                if (bmno == null)
                    fp.errorCode = errorcode = ErrorCode.NOT_FOUND_BMNO_IN_ACPTBILLLEDGER;
                else {
                    rs4 = manager.getRs("select fcno from fcmain where bmno='" + rs.getString("bmno")
                            + "' and billno=" + billdisno +
                            " and fcstatus <> " + EnumValue.FCStatus_WanCheng +
                            " and fcstatus<>" + EnumValue.FCStatus_QuXiao);

                    if (rs4.next()) {
                        if (confirmed == false) {
                            fp.errorCode = errorcode = 1;
                            return fp;
                        }
                    }
                }


                String operbrhid = SCBranch.getSupBrh(rs.getString("brhid"), EnumValue.BrhLevel_XinYongShe);

                if (operbrhid == null) fp.errorCode = errorcode = ErrorCode.GET_UP_BRH_ERROR;

                CMClient client = CMClientMan.getCMClient(rs.getString("clientno"));
                //if(client == null) errorcode = ErrorCode.CLIENT_NOT_FOUND;


                String fcno = FCMAIN.getNextNo();
                if (fcno == null) fp.errorCode = errorcode = ErrorCode.NEXT_FCNO_IS_NULL;

                java.sql.Date today = SystemDate.getSystemDate4();
                if (today == null) fp.errorCode = errorcode = ErrorCode.BUSI_DATE_NOT_FOUND;


                if (errorcode >= 0) {
                    int fc1, fc2, fc3, fcclass;
                    fc1 = fc2 = fc3 = fcclass = 0;
                    int typeno = 0;

                    typeno = EnumValue.BMType_ChengDuiHuiPiao;

                    fctype = EnumValue.FCType_QiYe;

                    autofc = fcmain.autoFCRating(bmno, fctype, typeno, rs.getInt("loantype3"),
                            client != null ? client.creditClass.intValue() : EnumValue.CreditClass_Wu, rs.getDate("duedate"), rs.getInt("loancat1"));

                    rs3 = manager.getRs("Select fc1,fc2,fc3 from fcmain t where t.bmno='" + bmno + "' and billno=" + billdisno + " and t.fcstatus=" + EnumValue.FCStatus_WanCheng + " order by createdate desc");
                    if (rs3.next()) {
                        fc1 = rs3.getInt("fc1");
                        fc2 = rs3.getInt("fc2");
                        fc3 = rs3.getInt("fc3");
                        if (fc1 <= 0) fc1 = autofc;
                        if (fc2 <= 0) fc2 = fc1;
                        if (fc3 <= 0) fc3 = fc2;
                        fcclass = rs.getInt("loancat1");
                    } else {
                        fc1 = fc2 = fc3 = fcclass = autofc;
                    }

                    rs2 = manager.getRsForUpdate("SELECT * FROM FCMAIN WHERE 1<>1", "FCMAIN");
                    rs2.moveToInsertRow();

                    rs2.updateString("fcno", fcno);
                    rs2.updateString("bmno", bmno);
                    rs2.updateString("brhid", rs.getString("brhid"));
                    rs2.updateString("operbrhid", operbrhid);
                    rs2.updateDate("createdate", today);
                    rs2.updateInt("fccrttype", EnumValue.FCCrtType_FeiShiDianQingFen);
                    rs2.updateInt("fctype", fctype);
                    rs2.updateInt("fcstatus", EnumValue.FCStatus_DengJi);
                    rs2.updateInt("bmtype", typeno);
                    rs2.updateInt("clienttype", client != null ? client.clientType.intValue() : EnumValue.ClientType_QiTa);
                    rs2.updateString("clientname", rs.getString("clientname") != null ? rs.getString("clientname") : "");
                    if (client != null) rs2.updateString("idno", client.ID);
                    if (rs.getString("billno") != null) rs2.updateString("cnlno", rs.getString("billno"));

                    if (rs.getBigDecimal("faceamt") != null)
                        rs2.updateBigDecimal("bal", rs.getBigDecimal("faceamt"));
                    else
                        rs2.updateBigDecimal("bal", new BigDecimal(0));

                    rs2.updateDate("paydate", rs.getDate("issuedate"));
                    rs2.updateDate("duedate", rs.getDate("duedate"));
                    rs2.updateInt("isextend", 0);
                    rs2.updateInt("loancat2", EnumValue.LoanCat2_ZhengChang);
                    rs2.updateInt("loantype3", rs.getString("loantype3") != null ? rs.getInt("loantype3") : EnumValue.LoanType3_XinYong);
                    rs2.updateString("firstresp", rs.getString("firstresp"));
                    rs2.updateString("curno", "01");
                    rs2.updateInt("creditclass", client != null ? client.creditClass.intValue() : EnumValue.CreditClass_Wu);
                    rs2.updateInt("fcauto", autofc);
//                    rs2.updateInt("fc1", fc1);
//                    rs2.updateInt("fc2", fc2);
//                    rs2.updateInt("fc3", fc3);
                    rs2.updateInt("fcclass", fcclass);
                    rs2.updateInt("pastduedays", fcmain.pastduedays);

                    rs2.updateString("operator", operator);
                    rs2.updateDate("lastmodified", today);
                    rs2.updateInt("billno", billdisno);
                    rs2.insertRow();
                    rs2.moveToCurrentRow();

                    rs2.acceptChanges();

                    fp.fcNo = fcno;
                    fp.bmNo = bmno;
                    fp.brhId = rs.getString("brhid");
                    fp.operBrhId = operbrhid;
                    fp.fcCrtType = EnumValue.FCCrtType_FeiShiDianQingFen;
                    fp.fcType = fctype;
                    fp.fcStatus = EnumValue.FCStatus_DengJi;
                    fp.bmType = typeno;
                    fp.clientType = client != null ? client.clientType.intValue() : EnumValue.ClientType_QiTa;

                }
            } else {
                fp.errorCode = errorcode = ErrorCode.ACPT_BILL_STS_NOT_ZHENGCHANG;
            }
        } catch (Exception e) {
            Debug.debug(e);
            fp.errorCode = errorcode = ErrorCode.EXCPT_FOUND;
            return fp;
        } catch (Throwable ex) {
            ex.printStackTrace();
            fp.errorCode = errorcode = ErrorCode.EXCPT_FOUND;
            return fp;
        } finally {
            try {
                if (rs != null) rs.close();
                if (rs2 != null) rs2.close();
                if (rs3 != null) rs3.close();
                if (rs4 != null) rs4.close();
            } catch (Exception e) {
                Debug.debug(e);
            }
            return fp;
        }

    }

}
