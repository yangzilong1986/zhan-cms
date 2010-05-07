package zt.cmsi.pub.define;

/**
 * <p>Title: New Porgram</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Dreem</p>
 * @author JGO(GZL)
 * @version 1.1
 */

import zt.cms.pub.SCBranch;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class BMFCLimit {

    private static HashMap data = null;
    private static BMFCLimit ptr = null;

    private static boolean isDirty = true;

    public BMFCLimit() {
    }


    /**
     * 从数据库中装入业务数据
     *
     * @return
     */
    private boolean loadData() {

        try {
            data = new HashMap();

            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            Connection cn = dc.getConnection();
            Statement st = null;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from fcreviewlimit");
            while (rs.next()) {
                BMCPConf bmappcriteriadata = initDataBean(rs);
                if (bmappcriteriadata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "解析结果集数据失败");
                    continue;
                }
                data.put(bmappcriteriadata.brhID, bmappcriteriadata);
            }
            Debug.debug(Debug.TYPE_MESSAGE, "finished in initializing FCLimit!");
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
            bmappcriteriadata.brhID = rs.getString("brhid").trim();
            bmappcriteriadata.top10 = rs.getBigDecimal("limitamt");
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
    public static BMFCLimit getInstance() {
        if (ptr == null) {
            ptr = new BMFCLimit();
            return ptr;
        } else
            return ptr;
    }

    public BigDecimal getLimitofBrh(String BrhID) {
        if (BMFCLimit.isDirty == true) {
            if (!loadData()) {
                Debug.debug(Debug.TYPE_ERROR, "读取清分时点设置失败！！！");
                return null;
            } else
                BMFCLimit.isDirty = false;
        }

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


    public synchronized static void setDirty() {
        BMFCLimit.isDirty = true;
    }

}
