package zt.cmsi.pub.define;

import zt.cms.pub.SCBranch;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class BMCPConfMan {
    private static HashMap data = null;
    private static BMCPConfMan ptr = null;

    public BMCPConfMan() {
        data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "读取超大户贷款比例设置失败！！！");
        }

    }


    /**
     * 从数据库中装入业务数据
     *
     * @return
     */
    private boolean loadData() {

        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            Connection cn = dc.getConnection();
            Statement st = null;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from BMCPConf");
            while (rs.next()) {
                BMCPConf bmappcriteriadata = initDataBean(rs);
                if (bmappcriteriadata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "解析结果集数据失败");
                    continue;
                }
                data.put(bmappcriteriadata.brhID, bmappcriteriadata);
            }
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return false;
        } finally {
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
    private BMCPConf initDataBean(ResultSet rs) {

        BMCPConf bmappcriteriadata = new BMCPConf();
        try {
            bmappcriteriadata.brhID = rs.getString("BrhID").trim();
            bmappcriteriadata.top10 = rs.getBigDecimal("Top10");
            bmappcriteriadata.topOne = rs.getBigDecimal("TopOne");
        } catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return null;
        }

        return bmappcriteriadata;
    }


    /**
     * 返回BMCPConfMan类的唯一实例
     *
     * @return zt.cmsi.pub.define.BMType
     * @roseuid 3FE3E4770039
     */
    public static BMCPConfMan getInstance() {
        if (ptr == null) {
            ptr = new BMCPConfMan();
            return ptr;
        } else
            return ptr;
    }

    public BigDecimal getTop10ofBrh(String BrhID) {
        int loop = 0;
        if (BrhID == null) return null;

        String brhid = BrhID.trim();
        BMCPConf dt = null;
        while (true) {
            loop++;
            dt = (BMCPConf) data.get(brhid);
            if (dt != null) {
                return dt.top10;
            } else {
                brhid = SCBranch.getSupBrh(brhid);
                if (brhid == null)
                    return null;
            }
            if (loop >= 10) return null;
        }
    }

    public BigDecimal getTopOneofBrh(String BrhID) {
        int loop = 0;
        if (BrhID == null) return null;

        String brhid = BrhID.trim();
        BMCPConf dt = null;
        while (true) {
            loop++;
            dt = (BMCPConf) data.get(brhid);
            if (dt != null) {
                return dt.topOne;
            } else {
                brhid = SCBranch.getSupBrh(brhid);
                if (brhid == null)
                    return null;
            }
            if (loop >= 10) return null;
        }
    }


    public static void main(String[] args) {
        //BMCPConfMan BMCPConfMan1 = new BMCPConfMan();
        System.out.println(BMCPConfMan.getInstance().getTop10ofBrh(""));
        System.out.println(BMCPConfMan.getInstance().getTop10ofBrh("907010100"));
        System.out.println(BMCPConfMan.getInstance().getTop10ofBrh("907010320"));
        System.out.println(BMCPConfMan.getInstance().getTopOneofBrh(""));
        System.out.println(BMCPConfMan.getInstance().getTopOneofBrh("907010100"));
        System.out.println(BMCPConfMan.getInstance().getTopOneofBrh("907010320"));

    }

}