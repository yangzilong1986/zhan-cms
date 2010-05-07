package zt.cmsi.pub.define;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.ListIterator;

public class BMBaseInst {
    public BigDecimal interest;
    public int bmType;
    public int periodbegin;
    public int periodend;

    private static LinkedList data = new LinkedList();
    private static boolean isDirty = true;


    public BMBaseInst() {
    }

    public synchronized static boolean loadData() {
        Connection con = null;
        Statement st = null;
        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            con = dc.getConnection();
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select * from scbaseinst");
            while (rs.next()) {
                //System.out.println("new line -----------------------");
                BMBaseInstData bmreviewdata = initDataBean(rs);
                if (bmreviewdata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "解析结果集数据失败");
                    continue;
                } else
                    //System.out.println("data is --------------"+bmreviewdata.bmType + "----"+bmreviewdata.period);
                    data.add(bmreviewdata);
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


    private static BMBaseInstData initDataBean(ResultSet rs) {
        BMBaseInstData bmreviewdata = new BMBaseInstData();
        try {
            bmreviewdata.bmType = rs.getInt("BMType");
            bmreviewdata.interest = rs.getBigDecimal("inst");
            bmreviewdata.periodBegin = rs.getInt("monthbegin");
            bmreviewdata.periodEnd = rs.getInt("monthend");
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return null;
        }
        return bmreviewdata;
    }


    public static BMBaseInst getBaseInst(int bmtype, int period) {
        if (BMBaseInst.isDirty == true) {
            if (BMBaseInst.loadData() == false) return null;
            BMBaseInst.isDirty = false;
        }

        BMBaseInst retdata = new BMBaseInst();

        for (int i = 0; i < data.size(); i++) {

        }

        BMBaseInstData retdata2 = (BMBaseInstData) BMBaseInst.geBaseInstData(bmtype, period);
        if (retdata2 == null) retdata2 = (BMBaseInstData) BMBaseInst.geBaseInstData(EnumValue.BMType_SuoYou, period);
        if (retdata2 == null) {
            return null;
        } else {
            retdata.bmType = retdata2.bmType;
            retdata.interest = retdata2.interest;
            retdata.periodbegin = retdata2.periodBegin;
            retdata.periodbegin = retdata2.periodEnd;
        }
        return retdata;
    }

    private static BMBaseInstData geBaseInstData(int bmtype, int period) {
        if (BMBaseInst.data == null) return null;
        ListIterator a = BMBaseInst.data.listIterator();
        BMBaseInstData temp = null;
        while (a.hasNext() == true) {
            temp = (BMBaseInstData) a.next();
            if (temp.bmType == bmtype && temp.periodBegin <= period && temp.periodEnd >= period)
                return temp;
        }

        return null;
    }

    public synchronized static void setDirty() {
        BMBaseInst.isDirty = true;
    }

}
