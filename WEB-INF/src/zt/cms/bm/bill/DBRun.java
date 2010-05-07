package zt.cms.bm.bill;

import zt.platform.db.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DBRun {
    public static int getBizStatusByBmNo(String strbmno, DatabaseConnection conn)
    {
        String strsql = null;
        strsql = "select BMSTATUS from bmtable where bmno='"+strbmno+"'";

//        System.out.println(strsql);
        if (strsql != null) {
            RecordSet rs = null;
            try {
                rs = conn.executeQuery(strsql);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (rs.next()) {
                return rs.getInt(0);
            }
        }
        return -1;
    }

    public static void main(String[] args)
    {
    }
}