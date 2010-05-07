package zt.cmsi.biz;
/**
 * 贷款登记的检查点检查
 * 返回CriCheckResult的集合
 * @param param
 * @return Vector
 * @roseuid 3FE512C001DA
 */

import zt.cms.pub.SCBranch;
import zt.cmsi.client.CMClient;
import zt.cmsi.client.CMClientMan;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.Param;
import zt.cmsi.pub.ParamName;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.define.BMAppCriteria;
import zt.cmsi.pub.define.BMCPConfMan;
import zt.cmsi.pub.define.BMReviewLimit;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.util.ListIterator;
import java.util.Vector;

public class AppCriteria {

    public static Vector checkAppCriteria(Param param) {
        //DatabaseConnection dc = null;
        Vector data = null;
        try {
            //dc = MyDB.getInstance().apGetConn();
            data = new Vector();
            CMClient client = null;
            //bmtype
            Integer bmtype = (Integer) param.getParam(ParamName.BMType);
            if (bmtype == null) {
                CriCheckResult ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "登记信息的业务类型为空";
                data.add(ret);
            }
            //BrhID
            if (param.getParam(ParamName.BrhID) == null) {
                CriCheckResult ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "登记信息的业务网点号为空";
                data.add(ret);
            }
            //不良核销和以资抵债不检查
            if (bmtype.intValue() == EnumValue.BMType_BuLiangDaiKuanHeXiao ||
                    bmtype.intValue() == EnumValue.BMType_YiZiDiZhai) {
                return data;
            }
            //检查点检查
            if (data.size() < 1) {
                if (param.getParam(ParamName.CLientNo) != null) {
                    client = CMClientMan.getCMClient((String) param.getParam(ParamName.CLientNo));
                }
                AppCriteria.checkMultiLocLoan(param, data, client);
                AppCriteria.checkBMTypeOpenStatus(param, data, client);
                AppCriteria.checkClientRela(param, data, client);
                AppCriteria.checkClientRelaCorp(param, data, client);
                AppCriteria.checkCreditClass(param, data, client);
                AppCriteria.checkBlackList(param, data, client);
                AppCriteria.checkTop100(param, data, client);
                AppCriteria.checkTopOne(param, data, client);
                AppCriteria.checkNegInfo(param, data, client);
                AppCriteria.checkFamUnInactLoan(param, data, client);
                AppCriteria.checkCreditLimit(param, data, client);
                AppCriteria.checkAssureHistory(param, data, client);
            }
        }
        catch (Exception e) {
            CriCheckResult ret = new CriCheckResult();
            ret.alertType = EnumValue.AlertType_LanJie;
            ret.message = "检查点检查出现错误(异常)";
            data.add(ret);
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception in CheckPoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            //MyDB.getInstance().apReleaseConn(0);
            return data;
        }

    }

    protected static void checkMultiLocLoan(Param param, Vector data, CMClient client) {
        int errorcode = 0;
        try {
            CriCheckResult ret = null;

            if (client == null) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "客户号为空或客户信息不存在";
                data.add(ret);
                errorcode = ErrorCode.CLIENT_NOT_FOUND;
                return;
            }
            client.appBrhID = client.appBrhID.trim();
            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.
                    CheckPoint_DuoTouDaiKuan, (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());

            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "多头贷款检查点设置不正确";
                data.add(ret);
                errorcode = -1;
                return;
            }

            if (alerttype != EnumValue.AlertType_BuJianCha) {
                if (client.appBrhID == null) {
                    ret = new CriCheckResult();
                    ret.alertType = alerttype;
                    ret.message = "客户的业务网点号为空";
                    data.add(ret);
                    errorcode = -1;
                    return;
                } else if (client.appBrhID.compareToIgnoreCase(((String) param.getParam(ParamName.BrhID)).trim()) != 0) {
                    ret = new CriCheckResult();
                    ret.alertType = alerttype;
                    ret.message = "客户的业务网点和本业务申请网点不同，本贷款申请为多头贷款申请!";
                    Integer bmtype = (Integer) param.getParam(ParamName.BMType);
                    if (bmtype != null) {
                        if (bmtype.intValue() == EnumValue.BMType_ZhanQi)
                            ret.alertType = EnumValue.AlertType_JingGao;
                    }
                    data.add(ret);
                    errorcode = -1;
                    return;
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = -1;
        }
    }

    protected static void checkBMTypeOpenStatus(Param param, Vector data, CMClient client) {
        int errorcode = 0;
        try {
            CriCheckResult ret = null;
            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_KaiBanYeWuJianCha,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());
            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "开办业务检查点设置不正确";
                data.add(ret);
                errorcode = -1;
                return;
            }
            if (alerttype != EnumValue.AlertType_BuJianCha) {
                if (BMReviewLimit.getInstance().ifBizOpenofBrh((String) param.getParam(ParamName.BrhID),
                        ((Integer) param.getParam(ParamName.BMType)).intValue()) == false) {
                    ret = new CriCheckResult();
                    ret.alertType = alerttype;
                    ret.message = "申请的业务网点不能开办本业务";
                    data.add(ret);
                    errorcode = -1;
                    return;
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = -1;
        }
    }

    protected static void checkClientRela(Param param, Vector data, CMClient client) {
        try {
            CriCheckResult ret = null;
            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_GuanLianRen,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());

            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "关联人检查点设置不正确";
                data.add(ret);
                return;
            }
            if (client == null) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户号为空或客户信息不存在";
                data.add(ret);
                return;
            }
            if (alerttype != EnumValue.AlertType_BuJianCha && client.ID != null) {
                AppCriteria.checkClientRela_real(param, data, client, alerttype);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
    }

    protected static void checkClientRela_real(Param param, Vector data, CMClient client, int alerttype) {
        CriCheckResult ret = null;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            ret = new CriCheckResult();
            ret.alertType = EnumValue.AlertType_LanJie;
            ret.message = "获取数据库连接失败";
            data.add(ret);
            return;
        }
        try {
            //自己的家庭成员
            //String sSql = "select distinct(b.clientno),b.name from CMIndvRela a,CMClient b,BMTable c where ((a.clientno='" + client.clientNo + "' and a.idno=b.id) or (a.idno='"+client.ID+"' and a.clientno=b.clientno)) and b.clientno=c.clientno and c.bmstatus<10";
            String sSql1 = "select a.clientno,a.name,a.id,b.bmno,b.bmstatus ";
            sSql1 += "from cmindvclient a,BMTable b ";
            //sSql1 += "where (a.id in (select idno from CMIndvRela where clientno='"+client.clientNo+"') or a.clientno in (select clientno from CMIndvRela where idno='"+client.ID+"')) and a.clientno=b.clientno and b.bmstatus<10 and (b.typeno<="+EnumValue.BMType_FangDiChanKaiFaDaiKuan +" or b.typeno="+EnumValue.BMType_GeTiGongShangHuDaiKuan+" or b.typeno="+EnumValue.BMType_GeRenQiTaDaiKuan+" or b.typeno="+EnumValue.BMType_BuShouQuan+")";
            sSql1 += "where a.id in (select idno from CMIndvRela where clientno='" + client.clientNo + "') and a.clientno=b.clientno and b.bmstatus<10 and (b.typeno<=" + EnumValue.BMType_FangDiChanKaiFaDaiKuan + " or b.typeno=" + EnumValue.BMType_GeTiGongShangHuDaiKuan + " or b.typeno=" + EnumValue.BMType_GeRenQiTaDaiKuan + " or b.typeno=" + EnumValue.BMType_BuShouQuan + ")";
            RecordSet rs1 = null;
            Debug.debug(Debug.TYPE_SQL, sSql1);
            rs1 = dc.executeQuery(sSql1);
            String bmnos = "";
            while (rs1.next()) {
                int stat = rs1.getInt("bmstatus");
                if (stat <= 7) {
                    ret = new CriCheckResult();
                    ret.alertType = alerttype;
                    ret.message = "客户有关联人贷款：" + DBUtil.fromDB(rs1.getString("name").trim()) + "（业务号：" + rs1.getString("bmno").trim() + "）";
                    data.add(ret);
                } else {
                    bmnos += "'" + rs1.getString("bmno").trim() + "',";
                }
            }
            //台帐中核实已发放贷款的当前余额
            if (bmnos.endsWith(",")) {
                bmnos = bmnos.substring(0, bmnos.length() - 1);
                String sSql2 = "select a.name,c.cnlno ";
                sSql2 += "from cmindvclient a,BMTable b,RQLoanLedger c ";
                sSql2 += "where c.bmno in(" + bmnos + ") and c.nowbal>0 and c.bmno=b.bmno and b.clientno=a.clientno";
                Debug.debug(Debug.TYPE_SQL, sSql2);
                RecordSet rs2 = dc.executeQuery(sSql2);
                while (rs2.next()) {
                    ret = new CriCheckResult();
                    ret.alertType = alerttype;
                    ret.message = "客户有关联人贷款：" + DBUtil.fromDB(rs2.getString("name").trim()) + "（借据号：" + rs2.getString("cnlno").trim() + "）";
                    data.add(ret);
                }
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception in CheckPoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    protected static void checkClientRelaCorp(Param param, Vector data, CMClient client) {
        try {
            CriCheckResult ret = null;
            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_GuanLianQiYe,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());
            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "关联企业检查点设置不正确";
                data.add(ret);
                return;
            }
            if (client == null) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户号为空或客户信息不存在";
                data.add(ret);
                return;
            }
            if (alerttype != EnumValue.AlertType_BuJianCha && client.ID != null) {
                AppCriteria.checkClientRelaCorp_real(param, data, client, alerttype);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
    }

    protected static void checkClientRelaCorp_real(Param param, Vector data, CMClient client, int alerttype) {
        CriCheckResult ret = null;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            ret = new CriCheckResult();
            ret.alertType = EnumValue.AlertType_LanJie;
            ret.message = "数据库连接未找到";
            data.add(ret);
            return;
        }
        try {
            String sSql =
                    //"select Name from CMCorpRelaCorp where ClientNo='" + client.clientNo + "' and CorpCode='"+client.ID+"'";;
                    "select Name from CMCorpRelaCorp where CorpCode='" + client.ID + "'";

            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户有关联企业：" + rs.getString(0);
                data.add(ret);
            }

            sSql = "select name from CMCorpClient where LawPersonID='" + client.ID + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_JingGao;
                ret.message = "客户是下列企业的法人：" + DBUtil.fromDB(rs.getString(0));
                data.add(ret);
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception in CheckPoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    protected static void checkCreditClass(Param param, Vector data, CMClient client) {
        try {
            CriCheckResult ret = null;
            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_MeiYouPingDingXinYongDengJiHuoXinYongDengJiDiYuAJi,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());

            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "信用等级检查点设置不正确";
                data.add(ret);
                return;
            }

            if (client == null) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户号为空或客户信息不存在";
                data.add(ret);
                //errorcode = ErrorCode.CLIENT_NOT_FOUND;
                return;
            }

            if (alerttype != EnumValue.AlertType_BuJianCha) {
                if (client.creditClass == null) {
                    ret = new CriCheckResult();
                    ret.alertType = alerttype;
                    ret.message = "客户没有信用等级！";
                    data.add(ret);
                } else if (client.creditClass.intValue() != EnumValue.CreditClass_AAA &&
                        client.creditClass.intValue() != EnumValue.CreditClass_AA &&
                        client.creditClass.intValue() != EnumValue.CreditClass_A) {
                    ret = new CriCheckResult();
                    ret.alertType = alerttype;
                    ret.message = "客户信用等级小于Ａ级！";
                    data.add(ret);
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }

    }

    protected static void checkCreditLimit(Param param, Vector data, CMClient client) {
        try {
            CriCheckResult ret = null;
            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_ChaoGuoShouXinEDu,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());
            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "信用等级检查点设置不正确";
                data.add(ret);
                return;
            }
            if (client == null) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户号为空或客户信息不存在";
                data.add(ret);
                return;
            }

            BigDecimal amt = null;
            String amtstr = null;
            amtstr = (String) param.getParam(ParamName.AppAmt);
            amt = new BigDecimal(amtstr);
            if (amt == null) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "登记金额没有输入！";
                data.add(ret);
            }
            if (alerttype != EnumValue.AlertType_BuJianCha) {
                CMCreditLimitData credata = CMClientMan.getCMCreditLimit(client.clientNo, 0);
                if (credata == null || credata.creditLimit == null || credata.limitCommit == null || credata.limitCommit == null) {
                    ret = new CriCheckResult();
                    ret.alertType = EnumValue.AlertType_JingGao;
                    ret.message = "客户没有信用额度信息或额度数据为空！";
                    data.add(ret);
                } else if (credata.disabled == false) {
                    ret = new CriCheckResult();
                    ret.alertType = alerttype;
                    ret.message = "客户授信额度已经被禁止使用！";
                    data.add(ret);
                } else {
                    credata.loanBal = credata.loanBal.add(credata.limitCommit);
                    credata.creditLimit = credata.creditLimit.subtract(credata.loanBal);
                    if (amt.compareTo(credata.creditLimit) > 0) {
                        ret = new CriCheckResult();
                        ret.alertType = alerttype;
                        ret.message = "申请金额超过客户的授信额度！";
                        data.add(ret);
                    }
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }

    }

    protected static void checkBlackList(Param param, Vector data, CMClient client) {
        //int errorcode = 0;

        try {
            CriCheckResult ret = null;

            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_HeiMingDan,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());

            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "黑名单检查点设置不正确";
                data.add(ret);
                return;
            }

            if (client == null) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户号为空或客户信息不存在";
                data.add(ret);
                //errorcode = ErrorCode.CLIENT_NOT_FOUND;
                return;
            }

            if (alerttype != EnumValue.AlertType_BuJianCha && client.ID != null) {
                AppCriteria.checkBlackList_real(param, data, client, alerttype);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }

    }

    protected static void checkBlackList_real(Param param, Vector data, CMClient client, int alerttype) {
        CriCheckResult ret = null;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            ret = new CriCheckResult();
            ret.alertType = EnumValue.AlertType_LanJie;
            ret.message = "数据库连接未找到";
            data.add(ret);
            return;
        }
        try {
            String sSql =
                    "select Name from CMBlackList where CreditCardNo='" + client.ID + "'";
            ;

            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户在黑名单中：" + DBUtil.fromDB(rs.getString(0));
                data.add(ret);
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception in CheckPoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    protected static void checkNegInfo(Param param, Vector data, CMClient client) {
        try {
            CriCheckResult ret = null;
            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_BuLiangJiLu,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());
            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "不良记录检查点设置不正确";
                data.add(ret);
                return;
            }
            if (client == null) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户号为空或客户信息不存在";
                data.add(ret);
                return;
            }
            if (alerttype != EnumValue.AlertType_BuJianCha && client.ID != null) {
                AppCriteria.checkNegInfo_real(param, data, client, alerttype);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
    }

    protected static void checkNegInfo_real(Param param, Vector data, CMClient client, int alerttype) {
        CriCheckResult ret = null;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            ret = new CriCheckResult();
            ret.alertType = EnumValue.AlertType_LanJie;
            ret.message = "数据库连接未找到";
            data.add(ret);
            return;
        }
        try {
            String sSql =
                    "select SeqNo from CMCorpNegInfo where ClientNo='" + client.clientNo +
                            "' union select SeqNo from CMCorpProsct where ClientNo='" + client.clientNo + "'";

            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户有负面信息或有诉讼情况！";
                data.add(ret);
            }

            //sSql = "select * from BMCreditLimit where ClientNo='" + client.clientNo + "'";
            sSql = "select * from bmtable where clientno='" + client.clientNo + "' and bmtable.bmstatus=" + EnumValue.BMStatus_BuLiangDaiKuan;
            //sSql = "select * from bmtable where clientno='" + client.clientNo + "' and exists(select bmno from bminactloan where bminactloan.bmno=bmtable.bmno and bminactloan.ilstatus=" + EnumValue.ILStatus_BuLiangDaiKuan + ")";
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户有不良贷款！";
                data.add(ret);
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception in CheckPoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    protected static void checkTop100(Param param, Vector data, CMClient client) {
        try {
            CriCheckResult ret = null;

            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_ChaoShiDaHuDaiKuanBiLi,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());
            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "超十大户贷款比例检查点设置不正确";
                data.add(ret);
                return;
            }
            if (alerttype != EnumValue.AlertType_BuJianCha) {
                AppCriteria.checkTop10_real(param, data, client, alerttype);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
    }

    protected static void checkTop10_real(Param param, Vector data, CMClient client, int alerttype) {
        CriCheckResult ret = null;
        BigDecimal pct = BMCPConfMan.getInstance().getTop10ofBrh((String) param.getParam(ParamName.BrhID));
        if (pct == null) {
            ret = new CriCheckResult();
            ret.alertType = alerttype;
            ret.message = "超十大户贷款比例未设置！";
            data.add(ret);
            return;
        }
        String upbrh = SCBranch.getSupBrh((String) param.getParam(ParamName.BrhID), EnumValue.BrhLevel_XinYongShe);
        if (upbrh == null) {
            ret = new CriCheckResult();
            ret.alertType = alerttype;
            ret.message = "上级信贷部门网点未找到！";
            data.add(ret);
            return;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            ret = new CriCheckResult();
            ret.alertType = alerttype;
            ret.message = "数据库连接未找到";
            data.add(ret);
            return;
        }
        try {
            String sSql =
                    "select ItmData from RQTblData where BrhId='" + upbrh +
                            "' and TblItmNo='B00010'";
            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            double realdata;
            if (rs.next()) {
                realdata = rs.getDouble(0);
                if (pct.doubleValue() < realdata) {
                    ret = new CriCheckResult();
                    ret.alertType = alerttype;
                    ret.message = "超十大户贷款比例!上限是：" + pct + "% 目前是：" + util.numberToString(new BigDecimal(realdata), 4, 2, true) + "%";
                    data.add(ret);
                }
            } else {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "超十大户贷款比例数据未找到!";
                data.add(ret);
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception in CheckPoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    protected static void checkFamUnInactLoan(Param param, Vector data, CMClient client) {
        try {
            CriCheckResult ret = null;
            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_LianBaoChengYuanBuLiangDaiKuanJianCha,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());
            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "联保成员不良贷款检查设置不正确";
                data.add(ret);
                return;
            }
            if (alerttype != EnumValue.AlertType_BuJianCha) {
                AppCriteria.checkFamUnInactLoan_real(param, data, client, alerttype);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
    }


    protected static void checkFamUnInactLoan_real(Param param, Vector data, CMClient client, int alerttype) {
        CriCheckResult ret = null;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            ret = new CriCheckResult();
            ret.alertType = alerttype;
            ret.message = "数据库连接未找到";
            data.add(ret);
            return;
        }
        try {
            String sSql = "select bmno from bmtable where clientno in (select clientno from cmindvclient where unionno = (select unionno from cmindvclient where clientno='"
                    + client.clientNo + "')) and clientno != '" + client.clientNo + "' and bmtable.bmstatus = " + EnumValue.BMStatus_BuLiangDaiKuan;

            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "联保成员有不良贷款：业务号是" + rs.getString("BMNO");
                data.add(ret);
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception in CheckPoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }


    protected static void checkTopOne(Param param, Vector data, CMClient client) {
        try {
            CriCheckResult ret = null;
            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_ChaoDaHuDaiKuanBiLi,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());
            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "超大户贷款比例检查点设置不正确";
                data.add(ret);
                return;
            }
            if (alerttype != EnumValue.AlertType_BuJianCha) {
                AppCriteria.checkTopOne_real(param, data, client, alerttype);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
    }


    protected static void checkTopOne_real(Param param, Vector data, CMClient client, int alerttype) {
        CriCheckResult ret = null;
        BigDecimal pct = BMCPConfMan.getInstance().getTopOneofBrh((String) param.getParam(ParamName.BrhID));
        if (pct == null) {
            ret = new CriCheckResult();
            ret.alertType = alerttype;
            ret.message = "超大户贷款比例未设置！";
            data.add(ret);
            return;
        }
        String upbrh = SCBranch.getSupBrh((String) param.getParam(ParamName.BrhID), EnumValue.BrhLevel_XinYongShe);
        if (upbrh == null) {
            ret = new CriCheckResult();
            ret.alertType = alerttype;
            ret.message = "上级信贷部门网点未找到！";
            data.add(ret);
            return;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            ret = new CriCheckResult();
            ret.alertType = alerttype;
            ret.message = "数据库连接未找到";
            data.add(ret);
            return;
        }
        try {
            String sSql =
                    "select ItmData from RQTblData where BrhId='" + upbrh +
                            "' and TblItmNo='B00011'";
            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            double realdata;
            if (rs.next()) {
                realdata = rs.getDouble(0);
                if (pct.doubleValue() < realdata) {
                    ret = new CriCheckResult();
                    ret.alertType = alerttype;
                    ret.message = "超大户贷款比例!上限是：" + pct + "% 目前是：" + util.numberToString(new BigDecimal(realdata), 4, 2, true) + "%";
                    data.add(ret);
                }
            } else {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "超大户贷款比例数据未找到!";
                data.add(ret);
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception in CheckPoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    protected static void checkAssureHistory(Param param, Vector data, CMClient client) {
        try {
            CriCheckResult ret = null;
            int alerttype = BMAppCriteria.getInstance().getAlertTypeofBrh(EnumValue.CheckPoint_DanBaoLiShi,
                    (String) param.getParam(ParamName.BrhID),
                    ((Integer) param.getParam(ParamName.BMType)).intValue());
            if (alerttype < 0) {
                ret = new CriCheckResult();
                ret.alertType = EnumValue.AlertType_LanJie;
                ret.message = "担保历史检查点设置不正确";
                data.add(ret);
                return;
            }
            if (client == null) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户号为空或客户信息不存在";
                data.add(ret);
                return;
            }
            if (alerttype != EnumValue.AlertType_BuJianCha && client.ID != null) {
                AppCriteria.checkAssureHistory_real(param, data, client, alerttype);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception found in Checkpoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
    }

    protected static void checkAssureHistory_real(Param param, Vector data, CMClient client, int alerttype) {
        CriCheckResult ret = null;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            ret = new CriCheckResult();
            ret.alertType = EnumValue.AlertType_LanJie;
            ret.message = "获取数据库连接错误";
            data.add(ret);
            return;
        }
        try {
            String sSql = "select * ";
            sSql += "from BMPLDGSECURITY a,BMTABLE b ";
            sSql += "where a.ID='" + client.ID + "' and a.bmno=b.bmno and b.bmstatus<10";
            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                ret = new CriCheckResult();
                ret.alertType = alerttype;
                ret.message = "客户的担保历史纪录中有" + rs.getRecordCount() + "笔正在办理或尚未结清的贷款！";
                data.add(ret);
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception in CheckPoint!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    public static void saveAppCriteriaData(String p_bmno, Vector p_data) {
        if (p_bmno == null || p_data == null) {
            return;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return;
        }
        try {
            String sql1 = "delete from BMAppCriteriaData where BMNO='" + p_bmno + "'";
            dc.executeUpdate(sql1);
            for (int i = 0; i < p_data.size(); i++) {
                CriCheckResult item = (CriCheckResult) p_data.get(i);
                if (item == null) continue;
                int type = item.getAlertType();
                String msg = item.getMessage();
                String sql2 = "insert into BMAppCriteriaData ";
                sql2 += "values('" + p_bmno + "'," + type + ",'" + DBUtil.toDB(msg) + "')";
                dc.executeUpdate(sql2);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
    }

    public static Vector getAppCriteriaData(String p_bmno) {
        if (p_bmno == null) {
            return null;
        }
        Vector data = new Vector();
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return null;
        }
        try {
            String sql = "select * from BMAppCriteriaData where BMNO='" + p_bmno + "'";
            RecordSet rs = dc.executeQuery(sql);
            while (rs.next()) {
                CriCheckResult item = new CriCheckResult();
                int type = rs.getInt("ALERTTYPE");
                String msg = rs.getString("MESSAGE");
                item.setAlertType(type);
                item.setMessage(msg);
                data.add(item);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return data;
        }
    }

    public static void main(String argv[]) {
        Param in = new Param();
        in.addParam(ParamName.BrhID, "907010130");
        in.addParam(ParamName.CLientNo, "2");
        in.addParam(ParamName.BMType, new Integer(1));
        SCBranch.getSupBrh((String) in.getParam(ParamName.BrhID), EnumValue.BrhLevel_XinYongShe);
        Vector ddd = AppCriteria.checkAppCriteria(in);
        if (ddd != null) {
            ListIterator a = ddd.listIterator();

            while (a.hasNext() == true) {

                CriCheckResult entity = (CriCheckResult) a.next();

                if (entity != null) {
                    System.out.print("type:" + entity.alertType);
                    System.out.println(" Msg:" + entity.message);
                }
            }
        }
    }

}
