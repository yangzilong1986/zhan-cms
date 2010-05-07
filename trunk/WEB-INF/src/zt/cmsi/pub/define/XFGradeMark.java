package zt.cmsi.pub.define;


import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


public class XFGradeMark {


    private static HashMap data = null;
    private static XFGradeMark ptr = null;

    private static boolean isDirty = true;

    public XFGradeMark() {
        if (XFGradeMark.isDirty) {
            if (!loadData()) {
                Debug.debug(Debug.TYPE_ERROR, "读XFGradeMark设置失败！！！");
            } else
                XFGradeMark.isDirty = false;
        }
    }


    private boolean loadData() {

        try {
            data = new HashMap();

            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            Connection cn = dc.getConnection();
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from xfgrademark order by enumtp,enumval,seqno");
            while (rs.next()) {
                XFGradeMarkNode xfgrademarkdata = initDataBean(rs);
                if (xfgrademarkdata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "解析结果集数据失败");
                    continue;
                }
                data.put("" + xfgrademarkdata.seqno, xfgrademarkdata);
            }
            Debug.debug(Debug.TYPE_MESSAGE, "finished in initializing XFGradeMark!");
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
     * @param rs ResultSet
     * @return XFGradeMarkNode
     */
    private XFGradeMarkNode initDataBean(ResultSet rs) {

        XFGradeMarkNode xfgrademarkdata = new XFGradeMarkNode();
        try {
            xfgrademarkdata.seqno = rs.getString("seqno");
            xfgrademarkdata.enumtp = rs.getString("enumtp");
            xfgrademarkdata.enumval = rs.getString("enumval");
            xfgrademarkdata.condition = rs.getString("condition");
            if (xfgrademarkdata.condition != null)
                xfgrademarkdata.condition = xfgrademarkdata.condition.toLowerCase().trim();
            xfgrademarkdata.grade = rs.getString("grade");
        } catch (Exception e) {
            Debug.debug(e);
            return null;
        }

        return xfgrademarkdata;
    }


    /**
     * 返回XFGradeMark类的唯一实例
     *
     * @return zt.cmsi.pub.define.XFGradeMark
     */
    public static XFGradeMark getInstance() {
        if (ptr == null) {
            ptr = new XFGradeMark();
            return ptr;
        } else
            return ptr;
    }


    /**
     * lj Created in 20090325
     *
     * @param seqno String
     * @return GradeMarkGrade
     */
    public String getGradeMarkGrade(String seqno) {
        XFGradeMarkNode gn = getGradeMark(seqno);
        if (gn != null) {
            return gn.grade;
        } else {
            return null;
        }
    }

    /**
     * lj Created in 20090325
     *
     * @param seqno String
     * @return GradeMarkSeqno
     */
    public String getGradeMarkSeqno(String seqno) {
        XFGradeMarkNode gn = getGradeMark(seqno);
        if (gn != null) {
            return gn.seqno;
        } else {
            return null;
        }
    }

    /**
     * lj Created in 20090325
     *
     * @param seqno String
     * @return GradeMarkLimit
     */
    public String getGradeMarkLimit(String seqno) {
        XFGradeMarkNode gn = getGradeMark(seqno);
        if (gn != null) {
            return gn.condition;
        } else {
            return null;
        }
    }

    /**
     * lj Created in 20090325
     *
     * @param seqno String
     * @return XFGradeMarkNode
     */
    public XFGradeMarkNode getGradeMark(String seqno) {
        if (seqno == null) return null;
        return (XFGradeMarkNode) data.get(seqno);
    }

    /**
     * lj Created in 20090325
     *
     * @param seqno String
     * @param limit String
     * @return GradeMarkByLimit
     */
    public String getGradeMarkGradeByLimit(String seqno, String limit) {
        XFGradeMarkNode gn = getGradeMark(seqno);
        if (gn != null) {
            if (limit != null && !limit.trim().equals("")) {  //限制条件不为空
                if (checkLimit(limit, gn.condition)) return gn.grade;
                else return "0";
            } else return gn.grade;
        } else {
            return "0";
        }
    }

    /**
     * lj Created in 20090325
     *
     * @param enumtp  String
     * @param enumval String
     * @param limit   String
     * @return GradeMarkGradeByLimit
     */
    public String getGradeMarkGradeByLimit(String enumtp, String enumval, String limit) {
        HashMap gmm = getGradeMarkMapReverse(enumtp, enumval);
        for (int i = 0; i < gmm.size(); i++) {
            XFGradeMarkNode gmnode = (XFGradeMarkNode) gmm.get(String.valueOf(i));
            if (gmnode.condition == null) return gmnode.grade;
            else if (Integer.parseInt(getGradeMarkGradeByLimit(gmnode.seqno, limit)) > 0) {
                return gmnode.grade;
            }
        }
        return "0";
    }

    /**
     * lj Created in 20090325
     * 目前只支持and
     *
     * @param limit     String
     * @param condition String
     * @return ifPassLimit
     */
    public static boolean checkLimit(String limit, String condition) {
        if (limit == null || condition == null) return true;
        condition = condition.replaceAll("\\?", "").trim();

        String[] conStr = condition.split(" and ");
        boolean[] flag = new boolean[conStr.length];

        for (int i = 0; i < conStr.length; i++) {
            flag[i] = false;
            if (conStr[i].indexOf(">=") >= 0)
                flag[i] = Double.parseDouble(limit) >= Double.parseDouble(conStr[i].replaceFirst(">=", ""));
            else if (conStr[i].indexOf("<=") >= 0)
                flag[i] = Double.parseDouble(limit) <= Double.parseDouble(conStr[i].replaceFirst("<=", ""));
            else if (conStr[i].indexOf("!=") >= 0)
                flag[i] = Double.parseDouble(limit) != Double.parseDouble(conStr[i].replaceFirst("!=", ""));
            else if (conStr[i].indexOf(">") >= 0)
                flag[i] = Double.parseDouble(limit) > Double.parseDouble(conStr[i].replaceFirst(">", ""));
            else if (conStr[i].indexOf("<") >= 0)
                flag[i] = Double.parseDouble(limit) < Double.parseDouble(conStr[i].replaceFirst("<", ""));
            else if (conStr[i].indexOf("=") >= 0)
                flag[i] = Double.parseDouble(limit) == Double.parseDouble(conStr[i].replaceFirst("=", ""));
            if (!flag[i]) return false;
        }
        return true;
    }


    /**
     * @return GradeMark
     */
    public HashMap getGradeMarkMap() {
        return data;
    }


    /**
     * @param enumtp  String
     * @param enumval String
     * @return GradeMarkMap
     */
    public HashMap getGradeMarkMap(String enumtp, String enumval) {
        HashMap gmm = new HashMap();
        int i = 0;
        String cond=" ";
        for (Iterator gmit = getGradeMarkMap().values().iterator(); gmit.hasNext();) {
            XFGradeMarkNode gmnode = (XFGradeMarkNode) gmit.next();
            if (gmnode.enumtp.equals(enumtp) && gmnode.enumval.equals(enumval)) {
                cond = (gmnode.condition == null) ? " " : gmnode.condition;
                gmm.put(cond + "-" + i++, gmnode);
            }
        }
        return gmm;
    }

    /**
     * @param enumtp  String
     * @param enumval String
     * @return GradeMarkMap
     */
    public HashMap getGradeMarkMapReverse(String enumtp, String enumval) {
        HashMap gmmReverse = new HashMap();
        HashMap gmm = getGradeMarkMap(enumtp, enumval);

        Object[] key = gmm.keySet().toArray();
        Arrays.sort(key, Collections.reverseOrder());//按照降序排列限制条件，有值的在前，空值的在后
        for (int i = 0; i < gmm.size(); i++) {
            XFGradeMarkNode gmnode = (XFGradeMarkNode) gmm.get(key[i]);
            gmmReverse.put(String.valueOf(i), gmnode);
        }
        return gmmReverse;
    }

    public synchronized static void setDirty() {
        XFGradeMark.isDirty = true;
        ptr = null; //todo  lj added in 20090326
    }

}