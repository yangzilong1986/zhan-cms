package zt.cmsi.pub.define;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.cms.pub.code.SCUserRoleBranchImpl;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class BMTypeOpen {
    private static HashMap data = null;
    private static BMTypeOpen ptr = null;
    private static boolean isDirty = false;

    /**
     * 返回BMReviewLimit类的唯一实例
     *
     * @return zt.cmsi.pub.define.BMReviewLimit
     * @roseuid 3FE3F4F00390
     */
    public static BMTypeOpen getInstance() {
        if (ptr == null) {
            ptr = new BMTypeOpen();
        }
        if (isDirty) {
            ptr = new BMTypeOpen();
            isDirty = false;
        }
        return ptr;
    }

    /**
     * 构造方法，初始化数据
     */
    private BMTypeOpen() {
        data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "读取业务开开白表数据失败！！！");
        }
    }

    public static void setDirty(boolean dirty) {
        isDirty = true;
    }


    /**
     * 数据修改后刷新缓冲的数据，现采取被动更新的方式，即在下一次请求时才实际更新数据。
     */
    public static void refresh() {
        SCUserRoleBranchImpl.setAllTypeOpenDirty();
        isDirty = true;
        //BMTypeOpen.getInstance();//主动更新
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
            ResultSet rs = st.executeQuery("select * from BMTypeOpen");
            while (rs.next()) {
                BMTypeOpenData bmreviewdata = initDataBean(rs);
                if (bmreviewdata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "解析结果集数据失败");
                    continue;
                }
                data.put("" + bmreviewdata.brhid + "_" + bmreviewdata.brhLevel + "_" + bmreviewdata.typeNo, bmreviewdata);
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
    private BMTypeOpenData initDataBean(ResultSet rs) {
        BMTypeOpenData bmreviewdata = new BMTypeOpenData();
        try {
            bmreviewdata.brhid = rs.getString("brhid") == null ? null : rs.getString("brhid").trim();
            bmreviewdata.typeNo = rs.getInt("typeno");
            bmreviewdata.ifOpen = new Boolean(rs.getBoolean("open"));
            bmreviewdata.brhLevel = rs.getInt("BrhLevel");
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return null;
        }

        return bmreviewdata;
    }

    /**
     * 获得网点关于一个业务类型是否开办(即继承上级网点的设置).
     */
    public boolean ifBizOpenofBrh(String BrhID, int BMTypeNo) {
        String brhid = BrhID.trim();
        int brhlevel = 0;

        while (true) {
            try {
                brhlevel = Integer.parseInt(SCBranch.getBrhlevel(brhid));
            }
            catch (Exception e) {
            }
            Boolean isopen = ifDirectBizOpenofBrh(brhid, brhlevel, BMTypeNo);
            //System.out.println(brhid+"_"+brhlevel+"_"+BMTypeNo+":"+isopen);
            if (isopen != null) {
                return isopen.booleanValue();
            }
            //上级
            brhid = SCBranch.getSupBrh(brhid);
            if (brhid == null) {
                if (brhlevel > 0) {
                    brhlevel = 0;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * @param BrhID
     * @param BMTypeNo
     * @return boolean
     * @roseuid 3FE3F590019B
     */
    public boolean ifBizOpenofUsr(String loginName, int BMTypeNo) {
        String brhid = SCUser.getBrhId(loginName);
        if (brhid != null) {
            return ifBizOpenofBrh(brhid, BMTypeNo);
        } else {
            return false;
        }
    }

    /**
     * 获得直接网点关于一个业务类型是否开办(即不继承上级网点的设置).
     */
    private Boolean ifDirectBizOpenofBrh(String BrhID, int brhlevel, int BMTypeNo) {
        BMTypeOpenData bmreviewdata = null;
        //某一网点(不分等级)某一类型
        bmreviewdata = (BMTypeOpenData) data.get(BrhID + "_" + EnumValue.BrhLevel_SuoYou + "_" + BMTypeNo);
        if (bmreviewdata != null) {
            return bmreviewdata.ifOpen;
        }
        //某一网点(不分等级)所有类型
        bmreviewdata = (BMTypeOpenData) data.get(BrhID + "_" + EnumValue.BrhLevel_SuoYou + "_" + EnumValue.BMType_SuoYou);
        if (bmreviewdata != null) {
            return bmreviewdata.ifOpen;
        }
        //所有网点_某一等级_某一类型
        bmreviewdata = (BMTypeOpenData) data.get("null_" + brhlevel + "_" + BMTypeNo);
        if (bmreviewdata != null) {
            return bmreviewdata.ifOpen;
        }
        //所有网点_某一等级_所有类型
        bmreviewdata = (BMTypeOpenData) data.get("null_" + brhlevel + "_" + EnumValue.BMType_SuoYou);
        if (bmreviewdata != null) {
            return bmreviewdata.ifOpen;
        }
        //所有网点_所有等级_所有类型
        bmreviewdata = (BMTypeOpenData) data.get("null_" + EnumValue.BrhLevel_SuoYou + "_" + EnumValue.BMType_SuoYou);
        if (bmreviewdata != null) {
            return bmreviewdata.ifOpen;
        }
        return null;
    }

    public static void main(String[] args) {
        SCBranch.getBrhlevel("907020599");
        System.out.println(BMTypeOpen.getInstance().ifBizOpenofBrh("907060500", 1));
    }
}
