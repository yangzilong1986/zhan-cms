package zt.cmsi.pub.define;

/**
 * 对BMReviewLimit的数据作缓冲，初始化时一次读入内存
 * 采用单态模式
 *
 * @author zhouwei
 * $Date: 2005/06/28 07:00:39 $
 * @version 1.0
 *
 * 版权：青岛中天公司
 */

import zt.cms.pub.SCBranch;
import zt.cms.pub.code.SCUserRoleBranchImpl;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class BMReviewLimit {
    private static HashMap data = null;
    private static BMReviewLimit ptr = null;
    private static boolean isDirty = false;

    /**
     * 返回BMReviewLimit类的唯一实例
     */
    public static BMReviewLimit getInstance() {
        if (ptr == null) {
            ptr = new BMReviewLimit();
        }
        if (isDirty) {
            ptr = new BMReviewLimit();
            isDirty = false;
        }
        return ptr;
    }

    /**
     * 构造方法，初始化数据
     */
    private BMReviewLimit() {
        data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "读取业务审批表数据失败！！！");
        }
        BMTypeOpen.getInstance(); //initialize BMTypeOpen class
    }

    public static void setDirty(boolean dirty) {
        isDirty = true;
    }

    /**
     * 数据修改后刷新缓冲的数据，现采取被动更新的方式，即在下一次请求时才实际更新数据。
     */
    public static void refresh() {
        SCUserRoleBranchImpl.setAllRevLimitDirty();
        isDirty = true;
    }

    /**
     * 从数据库中装入业务数据
     *
     * @return
     */
    private boolean loadData() {

        Connection con = null;
        Statement st = null;
        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            con = dc.getConnection();
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select * from bmreviewlimit");
            while (rs.next()) {
                BMReviewData bmreviewdata = initDataBean(rs);
                if (bmreviewdata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "解析结果集数据失败");
                    continue;
                }
                data.put("" + bmreviewdata.BrhID + "_" + bmreviewdata.BrhLevel + "_" +
                        bmreviewdata.loanType5 + "_" +
                        bmreviewdata.BMTypeNo + "_" +
                        bmreviewdata.clientType + "_" +
                        bmreviewdata.loanType3
                        , bmreviewdata);
            }
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return false;
        }
        finally {
            if (st != null) {
                try {
                    st.close();
                }
                catch (Exception e) {
                    Debug.debug(Debug.TYPE_WARNING, "Close Statement Failed!!!");
                    if (Debug.isDebugMode == true) {
                        Debug.debug(e);
                    }
                }
            }
            MyDB.getInstance().apReleaseConn(0);
        }
        return true;
    }

    /**
     * 根据表结构，初始化数据类
     *
     * @param rs
     * @return
     */
    private BMReviewData initDataBean(ResultSet rs) {
        BMReviewData bmreviewdata = new BMReviewData();
        try {
            bmreviewdata.BrhID = rs.getString("brhid") == null ? null : rs.getString("brhid").trim();
            bmreviewdata.BMTypeNo = rs.getInt("RLBMType");
            bmreviewdata.clientType = rs.getInt("RLClientType");
            bmreviewdata.loanType3 = rs.getInt("RLLoanType3");
            bmreviewdata.loanType5 = rs.getInt("RLLoanType5");
            bmreviewdata.Limit = rs.getBigDecimal("reviewlimit");
            bmreviewdata.BrhLevel = rs.getInt("RLBrhLevel");
            bmreviewdata.reviewLimitType = rs.getInt("ReviewLimitType");
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return null;
        }

        return bmreviewdata;
    }

    public boolean ifBizOpenofBrh(String BrhID, int BMTypeNo) {
        return BMTypeOpen.getInstance().ifBizOpenofBrh(BrhID, BMTypeNo);
    }

    /**
     * @param BrhID
     * @param BMTypeNo
     * @return boolean
     * @roseuid 3FE3F590019B
     */
    public boolean ifBizOpenofUsr(String loginName, int BMTypeNo) {
        return BMTypeOpen.getInstance().ifBizOpenofUsr(loginName, BMTypeNo);
    }

    /**
     * this method has been scrapped
     *
     * @param BrhID
     * @param BMTypeNo
     * @param clienttype
     * @param loantype5
     * @param loantype3
     * @return
     */
    private BigDecimal getReviewLimitofBrh(String BrhID, int BMTypeNo, int clienttype, int loantype5, int loantype3) {
        String brhid = BrhID.trim();
        int brhlevel = 0;
        try {
            brhlevel = Integer.parseInt(SCBranch.getBrhlevel(brhid));
        }
        catch (Exception e) {
        }
        BigDecimal limit = null;
//    System.out.println("brhid:" + brhid);
//    System.out.println("brhlevel:" + brhlevel);
//    System.out.println("BMTypeNo:" + BMTypeNo);
//    System.out.println("clienttype:" + clienttype);
//    System.out.println("loantype5:" + loantype5);
//    System.out.println("loantype3:" + loantype3);
        limit = getDirectReviewLimitofBrh(brhid, brhlevel, BMTypeNo, clienttype, loantype5, loantype3);
        if (limit == null) {
            limit = getDirectReviewLimitofBrh(brhid, EnumValue.BrhLevel_SuoYou, BMTypeNo, clienttype, loantype5, loantype3);
        }
        if (limit == null) {
            limit = getDirectReviewLimitofBrh("null", brhlevel, BMTypeNo, clienttype, loantype5, loantype3);
        }
        return limit;
    }

    private BigDecimal getDirectReviewLimitofBrh(String BrhID, int brhlevel, int typeno, int clienttype, int loantype5, int loantype3) {
        BMReviewData bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + loantype5 + "_" + typeno + "_" + clienttype + "_" + loantype3);
        if (bmreviewdata != null) {
            return bmreviewdata.Limit;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + loantype5 + "_" + typeno + "_" + EnumValue.ClientType_SuoYou + "_" + loantype3)) != null) {
            return bmreviewdata.Limit;
        }

        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + typeno + "_" + clienttype + "_" + loantype3)) != null) {
            return bmreviewdata.Limit;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + typeno + "_" + EnumValue.ClientType_SuoYou + "_" + loantype3)) != null) {
            return bmreviewdata.Limit;
        }

        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + loantype5 + "_" + typeno + "_" + EnumValue.ClientType_SuoYou + "_" + EnumValue.LoanType3_SuoYou)) != null) {
            return bmreviewdata.Limit;
        }


        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + typeno + "_" + clienttype + "_" + EnumValue.LoanType3_SuoYou)) != null) {
            return bmreviewdata.Limit;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + typeno + "_" + EnumValue.ClientType_SuoYou + "_" + EnumValue.LoanType3_SuoYou)) != null) {
            return bmreviewdata.Limit;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + EnumValue.BMType_SuoYou + "_" + clienttype + "_" + loantype3)) != null) {
            return bmreviewdata.Limit;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + EnumValue.BMType_SuoYou + "_" + EnumValue.ClientType_SuoYou + "_" + loantype3)) != null) {
            return bmreviewdata.Limit;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + EnumValue.BMType_SuoYou + "_" + EnumValue.ClientType_SuoYou + "_" + EnumValue.LoanType3_SuoYou)) != null) {
            return bmreviewdata.Limit;
        }
        return null;
    }


    public BMReviewData getReviewLimitofBrh2(String BrhID, int BMTypeNo, int clienttype, int loantype5, int loantype3) {
        BMReviewData bmreviewdata = null;
        String brhid = BrhID.trim();
        int brhlevel = 0;
        try {
            brhlevel = Integer.parseInt(SCBranch.getBrhlevel(brhid));
        }
        catch (Exception e) {
        }
        BigDecimal limit = null;
//    System.out.println("brhid:" + brhid);
//    System.out.println("brhlevel:" + brhlevel);
//    System.out.println("BMTypeNo:" + BMTypeNo);
//    System.out.println("clienttype:" + clienttype);
//    System.out.println("loantype5:" + loantype5);
//    System.out.println("loantype3:" + loantype3);
        bmreviewdata = getDirectReviewLimitofBrh2(brhid, brhlevel, BMTypeNo, clienttype, loantype5, loantype3);
        if (bmreviewdata == null) {
            bmreviewdata = getDirectReviewLimitofBrh2(brhid, EnumValue.BrhLevel_SuoYou, BMTypeNo, clienttype, loantype5, loantype3);
        }
        if (bmreviewdata == null) {
            bmreviewdata = getDirectReviewLimitofBrh2("null", brhlevel, BMTypeNo, clienttype, loantype5, loantype3);
        }
        return bmreviewdata;
    }

    /**
     * @param BrhID      String
     * @param brhlevel   int
     * @param typeno     int
     * @param clienttype int
     * @param loantype5  int:新增,存量
     * @param loantype3  int:信用,保证,抵押,质押
     * @return BMReviewData
     */

    private BMReviewData getDirectReviewLimitofBrh2(String BrhID, int brhlevel, int typeno, int clienttype, int loantype5, int loantype3) {
        BMReviewData bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + loantype5 + "_" + typeno + "_" + clienttype + "_" + loantype3);
        if (bmreviewdata != null) {
            return bmreviewdata;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + loantype5 + "_" + typeno + "_" + EnumValue.ClientType_SuoYou + "_" + loantype3)) != null) {
            return bmreviewdata;
        }

        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + typeno + "_" + clienttype + "_" + loantype3)) != null) {
            return bmreviewdata;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + typeno + "_" + EnumValue.ClientType_SuoYou + "_" + loantype3)) != null) {
            return bmreviewdata;
        }

        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + loantype5 + "_" + typeno + "_" + EnumValue.ClientType_SuoYou + "_" + EnumValue.LoanType3_SuoYou)) != null) {
            return bmreviewdata;
        }


        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + typeno + "_" + clienttype + "_" + EnumValue.LoanType3_SuoYou)) != null) {
            return bmreviewdata;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + typeno + "_" + EnumValue.ClientType_SuoYou + "_" + EnumValue.LoanType3_SuoYou)) != null) {
            return bmreviewdata;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + EnumValue.BMType_SuoYou + "_" + clienttype + "_" + loantype3)) != null) {
            return bmreviewdata;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + EnumValue.BMType_SuoYou + "_" + EnumValue.ClientType_SuoYou + "_" + loantype3)) != null) {
            return bmreviewdata;
        }
        if ((bmreviewdata = (BMReviewData) data.get(BrhID + "_" + brhlevel + "_" + EnumValue.LoanType5_SuoYou + "_" + EnumValue.BMType_SuoYou + "_" + EnumValue.ClientType_SuoYou + "_" + EnumValue.LoanType3_SuoYou)) != null) {
            return bmreviewdata;
        }
        return null;
    }


    public static void main(String[] args) {
        BMReviewLimit brl = BMReviewLimit.getInstance();
        //System.out.println(brl.getReviewLimitofBrh("907090299",1));
        //System.out.println(brl.getReviewLimitofBrh("90702019",2));
        //System.out.println(BMReviewLimit.getInstance().ifBizOpenofBrh("907070160",1));
        //System.out.println(BMReviewLimit.getInstance().getReviewLimitofBrh("907020599",1,8,2,230));
        System.out.println(BMReviewLimit.getInstance().ifBizOpenofBrh("907020599", 6));
    }
}
