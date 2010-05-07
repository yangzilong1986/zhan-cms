package zt.cms.cm.common;

import zt.platform.db.*;

/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author     not attributable
 *@created    2004Äê3ÔÂ9ÈÕ
 *@version    1.0
 */

public class ClientSum {
    /**
     *  Gets the clientSum attribute of the ClientSum class
     *
     *@param  clientNo  Description of the Parameter
     *@param  table     Description of the Parameter
     *@param  column    Description of the Parameter
     *@param  con       Description of the Parameter
     *@return           The clientSum value
     */
    public static String getClientSum(String clientNo, String table, String column, DatabaseConnection con) {
        String str = "select sum(" + column + ") from " + table + " where clientno='" + clientNo+"'";
        System.out.println(str);
        RecordSet rs = con.executeQuery(str);
        if (rs.next()) {
            return DBUtil.doubleToStr1(rs.getDouble(0));
        } else {
            return "0.00";
        }
    }

    public static String getClientSum2(String clientNo, String table, String column, DatabaseConnection con) {
    String str = "select sum(" + column + ") from " + table + ",bmtable where bmtable.clientno='" + clientNo+"' and bmtable.bmno="+table+".bmno";
    System.out.println(str);
    RecordSet rs = con.executeQuery(str);
    if (rs.next()) {
        return DBUtil.doubleToStr1(rs.getDouble(0));
    } else {
        return "0.00";
    }
}


//    public static double getClientSum2(String clientNo,String column,DatabaseConnection con) {
//    String str = "select sum(" + column + ") from " + table + " where clientno='" + clientNo+"'";
//    System.out.println(str);
//    RecordSet rs = con.executeQuery(str);
//    if (rs.next()) {
//        return rs.getDouble(0)/10000;
//    } else {
//        return 0;
//    }
//}



    /**
     *  Gets the clientSumWithUnit attribute of the ClientSum class
     *
     *@param  clientNo  Description of the Parameter
     *@param  table     Description of the Parameter
     *@param  column    Description of the Parameter
     *@param  con       Description of the Parameter
     *@param  unit      Description of the Parameter
     *@return           The clientSumWithUnit value
     */
    public static String getClientSumWithUnit(String clientNo, String table, String column, DatabaseConnection con, String unit) {
        String str = "select " + column + "," + unit + " from " + table + " where clientno='" + clientNo+"'";
        System.out.println(str);
        RecordSet rs = con.executeQuery(str);
        double value = 0;
        while (rs.next()) {
            if (rs.getInt(1) == 1) {
                value += rs.getDouble(0) / 10000;
            } else if (rs.getInt(1) == 2) {
                value += rs.getDouble(0);
            }
        }
        return DBUtil.doubleToStr1(value*10000) ;
    }

    public static void main(String[] args) {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        System.out.println(""+getClientSumWithUnit("10","CMINDVASSET","estimate",con,"estimateunit"));
    }

}
