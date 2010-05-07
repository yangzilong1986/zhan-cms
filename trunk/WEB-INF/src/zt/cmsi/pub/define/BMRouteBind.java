package zt.cmsi.pub.define;


import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


public class BMRouteBind {


    private static HashMap data = null;
    private static BMRouteBind ptr = null;

    private static boolean isDirty = true;

    public BMRouteBind() {
        if (BMRouteBind.isDirty) {                                               //lj added in 20090325
            if (!loadData()) {                                                    //lj added in 20090325
                Debug.debug(Debug.TYPE_ERROR, "读BMRouteBind设置失败！！！");  //lj added in 20090325
            } else                                                               //lj added in 20090325
                BMRouteBind.isDirty = false;                                    //lj added in 20090325
        }                                                                         //lj added in 20090325
    }


    private boolean loadData() {

        try {
            data = new HashMap();

            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            Connection cn = dc.getConnection();
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from bmroutebind order by routeno,bndtype,seqno");
            while (rs.next()) {
                BMRouteBindNode bmappcriteriadata = initDataBean(rs);
                if (bmappcriteriadata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "解析结果集数据失败");
                    continue;
                }
                //data.put("" + bmappcriteriadata.bndtype + "_" + bmappcriteriadata.bndid, bmappcriteriadata);
                if (bmappcriteriadata.bndtype.equals(String.valueOf(EnumValue.BndType_WangDianDaiMa)) ||
                        bmappcriteriadata.bndtype.equals(String.valueOf(EnumValue.BndType_YongHuDaiMa)))
                    data.put("" + bmappcriteriadata.bndtype + "_" + bmappcriteriadata.bndid, bmappcriteriadata);
                else
                    data.put(bmappcriteriadata.routeno + "_" + bmappcriteriadata.bndtype + "_" + bmappcriteriadata.bndid, bmappcriteriadata);//lj changed in 20090325
            }
            Debug.debug(Debug.TYPE_MESSAGE, "finished in initializing BMRouteBind!");
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
    private BMRouteBindNode initDataBean(ResultSet rs) {

        BMRouteBindNode bmappcriteriadata = new BMRouteBindNode();
        try {
            bmappcriteriadata.routeno = rs.getString("routeno");
            bmappcriteriadata.bndid = rs.getString("bndid");
            if (bmappcriteriadata.bndid != null) bmappcriteriadata.bndid = bmappcriteriadata.bndid.trim();
            bmappcriteriadata.bndtype = rs.getString("bndtype");
            bmappcriteriadata.seqno = rs.getString("seqno");
            bmappcriteriadata.status = rs.getString("status");
            bmappcriteriadata.limitcondition = rs.getString("limitcondition");  //lj changed in 20090314
        } catch (Exception e) {
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
    public static BMRouteBind getInstance() {
        if (ptr == null) {
            ptr = new BMRouteBind();
            return ptr;
        } else
            return ptr;
    }

    public Integer getRouteNoByBindNo(int bndtype, String bndid) {
        if (BMRouteBind.isDirty == true) {
            if (!loadData()) {
                Debug.debug(Debug.TYPE_ERROR, "读BMRouteBind设置失败！！！");
                return null;
            } else
                BMRouteBind.isDirty = false;
        }

        if (bndid == null) return null;

        BMRouteBindNode dt = null;
        dt = (BMRouteBindNode) data.get("" + bndtype + "_" + bndid.trim());
        if (dt != null) {
            return new Integer(dt.routeno);
        } else {
            return null;
        }
    }

    /**
     * lj Created in 20090325
     *
     * @param routeno
     * @param bndtype
     * @param bndid
     * @return
     */
    public String getRouteBindStatus(String routeno, String bndtype, String bndid) {
        if (routeno == null || bndtype == null || bndid == null) return null;

        BMRouteBindNode dt = (BMRouteBindNode) data.get(routeno + "_" + bndtype + "_" + bndid);
        if (dt != null) {
            return dt.status;
        } else {
            return null;
        }
    }

    /**
     * lj Created in 20090325
     *
     * @param routeno
     * @param bndtype
     * @param bndid
     * @return
     */
    public String getRouteBindSeqno(String routeno, String bndtype, String bndid) {
        if (routeno == null || bndtype == null || bndid == null) return null;

        BMRouteBindNode dt = (BMRouteBindNode) data.get(routeno + "_" + bndtype + "_" + bndid);
        if (dt != null) {
            return dt.seqno;
        } else {
            return null;
        }
    }

    /**
     * lj Created in 20090325
     *
     * @param routeno
     * @param bndtype
     * @param bndid
     * @return
     */
    public String getRouteBindLimit(String routeno, String bndtype, String bndid) {
        if (routeno == null || bndtype == null || bndid == null) return null;

        BMRouteBindNode dt = (BMRouteBindNode) data.get(routeno + "_" + bndtype + "_" + bndid);
        if (dt != null) {
            return dt.limitcondition;
        } else {
            return null;
        }
    }

    /**
     * lj Created in 20090325
     *
     * @param routeno
     * @param bndtype
     * @param bndid
     * @return
     */
    public BMRouteBindNode getRouteBind(String routeno, String bndtype, String bndid) {
        if (routeno == null || bndtype == null || bndid == null) return null;
        return (BMRouteBindNode) data.get(routeno + "_" + bndtype + "_" + bndid);
    }

    /**
     * lj Created in 20090325
     *
     * @param routeno
     * @param bndtype
     * @param bndid
     * @param limit
     * @return
     */
    public String getRouteBindStatusByLimit(String routeno, String bndtype, String bndid, String limit) {
        if (routeno == null || bndtype == null || bndid == null) return null;

        BMRouteBindNode dt = (BMRouteBindNode) data.get(routeno + "_" + bndtype + "_" + bndid);
        if (dt != null) {
            if (limit != null && !limit.trim().equals("")) {  //限制条件不为空
                if (checkLimit(limit, dt.limitcondition)) return dt.status;
                else return null;
            } else return dt.status;
        } else {
            return null;
        }
    }


    /**
     * lj Created in 20090325
     *
     * @param limit
     * @param condition
     * @return
     */
    public static boolean checkLimit(String limit, String condition) {
        if (limit == null || condition == null) return true;

        if (condition.startsWith(">="))
            return Double.parseDouble(limit) >= Double.parseDouble(condition.replaceFirst(">=", ""));
        if (condition.startsWith("<="))
            return Double.parseDouble(limit) <= Double.parseDouble(condition.replaceFirst("<=", ""));
        if (condition.startsWith("!="))
            return Double.parseDouble(limit) != Double.parseDouble(condition.replaceFirst("!=", ""));
        if (condition.startsWith(">"))
            return Double.parseDouble(limit) > Double.parseDouble(condition.replaceFirst(">", ""));
        if (condition.startsWith("<"))
            return Double.parseDouble(limit) < Double.parseDouble(condition.replaceFirst("<", ""));
        if (condition.startsWith("="))
            return Double.parseDouble(limit) == Double.parseDouble(condition.replaceFirst("=", ""));
        else return true;
    }


    /**
     * lj Created in 20090411
     *
     * @return
     */
    public HashMap getBindMap() {
        return data;
    }

    /**
     * lj Created in 20090411
     * <p/>
     * 根据现有流程状态返回下一状态
     *
     * @return String
     */
    public String getNextStatus(String routeno, String bndtype, String statusStr, String condition) {
        HashMap Binddata = getSpecifyBindNode(routeno, bndtype);//根据流程类型和绑定类型，得到指定的 BindNode
        Object[] key = bindNodeKeyAsc(Binddata.keySet().toArray());//按照增序排列序号

        boolean equalFlag = false;
        for (int i = 0; i < Binddata.size(); i++) {
            BMRouteBindNode bln = (BMRouteBindNode) Binddata.get(key[i]);
            if (equalFlag) {
                if (condition != null && !condition.trim().equals("") && bln.limitcondition != null) {
                    if (!checkLimit(condition, bln.limitcondition))
                        getNextStatus(routeno, bndtype, bln.status, condition);
                    else return bln.status;
                } else return bln.status;
            }

            if (bln.status.equals(statusStr)) {//如果当前状态等于传入的状态，置标志位为真。
                equalFlag = true;
            }
        }
        return "";
    }

    /**
     * lj Created in 20090411
     * <p/>
     * 根据现有流程状态返回上一状态
     *
     * @return String
     */
    public String getBeforeStatus(String routeno, String bndtype, String statusStr, String condition) {
        HashMap Binddata = getSpecifyBindNode(routeno, bndtype);//根据流程类型和绑定类型，得到指定的 BindNode
        Object[] key = bindNodeKeyDesc(Binddata.keySet().toArray());//按照降序排列序号

        boolean equalFlag = false;
        for (int i = 0; i < Binddata.size(); i++) {
            BMRouteBindNode bln = (BMRouteBindNode) Binddata.get(key[i]);
            if (equalFlag) {
                if (condition != null && !condition.trim().equals("") && bln.limitcondition != null) {
                    if (!checkLimit(condition, bln.limitcondition))
                        getBeforeStatus(routeno, bndtype, bln.status, condition);
                    else return bln.status;
                } else return bln.status;
            }

            if (bln.status.equals(statusStr)) {//如果当前状态等于传入的状态，置标志位为真。
                equalFlag = true;
            }
        }
        return "";
    }

    /**
     * lj Created in 20090411
     * <p/>
     * 按照增序排列BindNode序号
     *
     * @return Object[]
     */
    public Object[] bindNodeKeyAsc(Object[] key) {
        Arrays.sort(key);//按照增序排列序号
        return key;
    }

    /**
     * lj Created in 20090411
     * <p/>
     * 按照降序排列BindNode序号
     *
     * @return Object[]
     */
    public Object[] bindNodeKeyDesc(Object[] key) {
        Arrays.sort(key, Collections.reverseOrder());//按照降序排列序号
        return key;
    }

    /**
     * lj Created in 20090411
     * <p/>
     * 根据流程类型和绑定类型，得到指定的 BindNode
     *
     * @return HashMap
     */
    public HashMap getSpecifyBindNode(String routeno, String bndtype) {
        HashMap Binddata = new HashMap();
        for (Iterator bindit = data.values().iterator(); bindit.hasNext();) {
            BMRouteBindNode bindnode = (BMRouteBindNode) bindit.next();
            if (bindnode.routeno.equals(routeno) && bindnode.bndtype.equals(bndtype)) {
                Binddata.put(bindnode.seqno, bindnode);
            }
        }
        return Binddata;
    }


    public synchronized static void setDirty() {
        BMRouteBind.isDirty = true;
        ptr = null; //todo  lj added in 20090326
    }

}
