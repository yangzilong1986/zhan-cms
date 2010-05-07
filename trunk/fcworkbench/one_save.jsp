<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.define.SystemDate" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.cachedb.DatabaseConnection" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>
<!--************************************lj del in 2007-06-07-->
<%--<%@ page import="java.math.BigDecimal" %>--%>
<!--************************************lj del end in 2007-06-07-->

<%--
===============================================
Title: 清分操作-信贷员认定提交
Description: 信贷员认定提交。
 * @version  $Revision: 1.13 $  $Date: 2007/07/04 06:33:37 $
 * @author
 * <p/>修改：$Author: liuj $
===============================================
--%>

<%
    request.setCharacterEncoding("GBK");
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
        response.sendRedirect("../fcworkbench/error.jsp");
    }
    ConnectionManager manager = ConnectionManager.getInstance();
    String FCNO = request.getParameter("FCNO");
    String FCSTATUS = request.getParameter("FCSTATUS");
    String FCTYPE = request.getParameter("FCTYPE");
    String CLIENTNO = request.getParameter("CLIENTNO");     //客户代码     lj added in 2007-05-28

    if (FCNO == null || FCSTATUS == null || FCTYPE == null) {
        session.setAttribute("lettermess", "没有发现传送入的参数！");
        response.sendRedirect("../fcworkbench/lettersucces.jsp");
    }
    boolean temp = false;
    String FC1 = (request.getParameter("FC1") == null) ? null : request.getParameter("FC1").trim();
    String BADREASON = request.getParameter("BADREASON");//贷款形成不良原因                      lj added in 2007-04-28
    if (BADREASON.equals("")) BADREASON = "NULL";
    String LOANWAY = request.getParameter("LOANWAY");    //贷款投向                              lj added in 2007-04-28

    String CMT11 = (request.getParameter("CMT1") == null) ? null : request.getParameter("CMT1").trim();
    String CMT12 = (request.getParameter("CMT2") == null) ? null : request.getParameter("CMT2").trim();

    DatabaseConnection dbcm = new DatabaseConnection();
    java.sql.Connection conn = dbcm.getConnection();
    if (conn == null) throw new Exception("数据库连接错误！");
    conn.setAutoCommit(false);

    if (FCTYPE.equals(String.valueOf(EnumValue.FCType_ZiRanRenQiTa))) {//自然人其他              lj comment in 2007-04-28
//************************************lj del in 2007-06-07
//        BigDecimal AMT1;
//        BigDecimal AMT2;
//        BigDecimal AMT3;
//        BigDecimal AMT4;
//        BigDecimal AMT5;
//        BigDecimal AMT6;
//        BigDecimal AMT7;
//        BigDecimal AMT8;
//        String CMT1 = "";
//        String CMT2 = "";
//        String CMT3 = "";
//        String CMT4 = "";
////        String CMT5 = "";
//************************************lj del end in 2007-06-07        
        String flag = "";
        boolean query = true;
//************************************lj del in 2007-06-07
//        //自然人财务状况
//        String sql1 = "select * from FCMCMT " +
//                "where FCNO='" + FCNO + "' and FCCMTTYPE=7 order by DT desc";
//        CachedRowSet crs1 = manager.getRs(sql1);
//        if (crs1.size() == 0) {
//            ////System.out.println("*********************************************crs.size()"+crs1.size());
//            flag = "财务状况未填写完整,";
//            query = false;
//        } else {
//            while (crs1.next()) {
//                AMT1 = crs1.getBigDecimal("AMT1");
//                AMT2 = crs1.getBigDecimal("AMT2");
//                AMT3 = crs1.getBigDecimal("AMT3");
//                AMT4 = crs1.getBigDecimal("AMT4");
//                AMT5 = crs1.getBigDecimal("AMT5");
//                AMT6 = crs1.getBigDecimal("AMT6");
//                AMT7 = crs1.getBigDecimal("AMT7");
//                AMT8 = crs1.getBigDecimal("AMT8");
//                //CMT1=crs1.getString("CMT1");
//
//                if (AMT1 == null || AMT2 == null || AMT3 == null || AMT4 == null || AMT5 == null ||
//                        AMT6 == null || AMT7 == null || AMT8 == null) {
//                    flag = "财务状况未填写完整,";
//                    query = false;
//                }
//            }
//        }
//        //自然人担保状况
//        String sql2 = "select * from FCMCMT where FCNO='" + FCNO + "' and FCCMTTYPE=4";
//        CachedRowSet crs2 = manager.getRs(sql2);
//        if (crs2.size() == 0) {
//            query = false;
//            flag += "担保状况未填写完整,";
//        } else {
//            while (crs2.next()) {
//                CMT1 = crs2.getString("CMT1");
//                CMT2 = crs2.getString("CMT2");
//                CMT3 = crs2.getString("CMT3");
//                CMT4 = crs2.getString("CMT4");
//                //CMT5=crs2.getString("CMT5");
//            }
//            if (CMT1 == null || CMT1.equals("") || CMT2 == null || CMT2.equals("") || CMT3 == null || CMT3.equals("") ||
//                    CMT4 == null || CMT4.equals("")) {
//                flag += "担保状况未填写完整,";
//                query = false;
//            }
//        }
//        //自然人贷款基本状况
//        String sql3 = "select * from FCMCMT where FCNO='" + FCNO + "' and FCCMTTYPE=2";
//        CachedRowSet crs3 = manager.getRs(sql3);
//        if (crs3.size() == 0) {
//            query = false;
//            flag += "贷款基本状况未填写完整,";
//        } else {
//            while (crs3.next()) {
//                CMT1 = crs3.getString("CMT1");
//                CMT2 = crs3.getString("CMT2");
//                CMT3 = crs3.getString("CMT3");
//                //CMT4=crs3.getString("CMT4");
//            }
//            if (CMT1 == null || CMT1.equals("") || CMT2 == null || CMT2.equals("") || CMT3 == null || CMT3.equals("")) {
//                flag += "贷款基本状况未填写完整,";
//                query = false;
//            }
//        }
//        //自然人非财务因素
//        String sql4 = "select * from FCMCMT where FCNO='" + FCNO + "' and FCCMTTYPE=3";
//        CachedRowSet crs4 = manager.getRs(sql4);
//        if (crs4.size() == 0) {
//            query = false;
//            flag += "非财务因素未填写完整,";
//        } else {
//            while (crs4.next()) {
//                CMT1 = crs4.getString("CMT1");
//                CMT2 = crs4.getString("CMT2");
//                CMT3 = crs4.getString("CMT3");
//                CMT4 = crs4.getString("CMT4");
//                //CMT5=crs4.getString("CMT5");
//            }
//            if (CMT1 == null || CMT1.equals("") || CMT2 == null || CMT2.equals("") || CMT3 == null || CMT3.equals("") ||
//                    CMT4 == null || CMT4.equals("")) {
//                flag += "非财务因素未填写完整,";
//                query = false;
//            }
//        }
//************************************lj del end in 2007-06-07
        if (!query) {
            flag = flag.substring(0, flag.length() - 1);
            request.setAttribute("mess", flag);
%>
<jsp:forward page="/fcworkbench/one.jsp"/>
<%
} else {
//    String[] sql = new String[2];
    String sql11 = "", sql12 = "";
    String query_sql = " select * from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=1";
//    sql[0] = "update FCMAIN set FC1=" + FC1 + ",FCSTATUS=2,USER1='" + um.getUserName() + "',OPERATOR='" + um.getUserName()
    sql11 = "update FCMAIN set FC1=" + FC1 + ",FCSTATUS=2,USER1='" + um.getUserName() + "',OPERATOR='" + um.getUserName()
            + "',LASTMODIFIED=" + DBUtil.toSqlDate(SystemDate.getSystemDate5(""))
            + ",BADREASON=" + BADREASON + " ,LOANWAY='" + LOANWAY + "'"                                                 //lj added BADREASON,LOANWAY in 2007-04-28
            + " where FCNO='" + FCNO + "'";
//    CachedRowSet crs = manager.getRs(query_sql);
//    if (crs.next()) {
//        sql[1] = "update FCCMT set CMT1='" + DBUtil.toDB(CMT11) + "',CMT2='" + DBUtil.toDB(CMT12) + "' where FCCMTTYPE=1 and FCNO='" + FCNO + "'";
//    } else {
//        sql[1] = "insert into FCCMT(FCNO,FCCMTTYPE,CMT1,CMT2) values('" + FCNO + "',1,'" + DBUtil.toDB(CMT11) + "','" + DBUtil.toDB(CMT12) + "')";
//    }
//    temp = manager.execBatch(sql);

    try {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query_sql);
        if (rs.next()) {
            sql12 = "update FCCMT set CMT1='" + DBUtil.toDB(CMT11) + "',CMT2='" + DBUtil.toDB(CMT12) + "' where FCCMTTYPE=1 and FCNO='" + FCNO + "'";
        } else {
            sql12 = "insert into FCCMT(FCNO,FCCMTTYPE,CMT1,CMT2) values('" + FCNO + "',1,'" + DBUtil.toDB(CMT11) + "','" + DBUtil.toDB(CMT12) + "')";
        }
        rs.close();
        if (stmt.executeUpdate(sql11) >= 0 && stmt.executeUpdate(sql12) >= 0) {
            conn.commit();
            temp = true;
            stmt.close();
        } else {
            conn.rollback();
            stmt.close();
        }
    } catch (Exception
            ex) {
        conn.rollback();
        throw new Exception(ex.getMessage());
    } finally {
        if (conn != null) {
            conn.close();
        }
    }
    //System.out.println("****************************自然人其他");

    if (temp) {
        session.setAttribute("mess", "提交到三级审批！");
        response.sendRedirect("../fcworkbench/lettersucces.jsp");

    } else {
        request.setAttribute("mess", "提交信息失败");
%>
<jsp:forward page="/fcworkbench/one.jsp"/>
<%
        }
    }
} else if (FCTYPE.equals(String.valueOf(EnumValue.FCType_QiYe)) || FCTYPE.equals(String.valueOf(EnumValue.FCType_WeiXingQiYe))) {//企业类贷款   lj added FCType_WeiXingQiYe in 2007-05-15
//    BigDecimal AMT1;
//    BigDecimal AMT2;
//    BigDecimal AMT3;
//    BigDecimal AMT4;
//    BigDecimal AMT5;
//    BigDecimal AMT6;
//    BigDecimal AMT7;
//    BigDecimal AMT8;
//    BigDecimal AMT9;
//    BigDecimal AMT10;
    String CMT1 = "";
    String CMT2 = "";

    String flag = "";
    boolean query = true;

    if (FCTYPE.equals(String.valueOf(EnumValue.FCType_QiYe))) {
        //五级分类企业基本财务信息
        String sql0 = "select max(dt) mdt from FCPRD  where INITIALIZED=1 having max(dt) is not null";
        CachedRowSet crs0 = manager.getRs(sql0);
        if (crs0.size() == 0) {
            query = false;
            flag = "未设置清分时点，请联系省中心。";
        } else {
            String mdt = "";
            if (crs0.next()) {
                mdt = crs0.getString("mdt");
            }

            String sql1 = "select * from FCQYCW where CLIENTNO='" + CLIENTNO + "' and DT>'" +
                    SystemDate.getLastMonthDate(SystemDate.getLastQuarterDate(mdt), "-") +
                    "' and DT<='" + SystemDate.getLastMonthDate(SystemDate.getSystemDate2(),"-") + "'";
            CachedRowSet crs1 = manager.getRs(sql1);
            if (crs1.size() == 0) {
                query = false;
                flag = "上季度最后一月至上月末企业基本财务信息均未填写,请填写。";
            }
        }
    }

    /*//企业主要财务指标
    String sql1 = "select * from FCMCMT where FCNO='" + FCNO + "' and FCCMTTYPE=10";
    CachedRowSet crs1 = manager.getRs(sql1);
    if (crs1.size() == 0) {
        //System.out.println("*********************************************企业主要财务指标"+crs1.size());
        query = false;
        flag = "主要财务指标未填写完整,";
    } else {
        while (crs1.next()) {
            AMT1 = crs1.getBigDecimal("AMT1");
            AMT2 = crs1.getBigDecimal("AMT2");
            AMT3 = crs1.getBigDecimal("AMT3");
            AMT4 = crs1.getBigDecimal("AMT4");
            AMT5 = crs1.getBigDecimal("AMT5");
            AMT6 = crs1.getBigDecimal("AMT6");
            AMT7 = crs1.getBigDecimal("AMT7");
            AMT8 = crs1.getBigDecimal("AMT8");
            AMT9 = crs1.getBigDecimal("AMT9");
            AMT10 = crs1.getBigDecimal("AMT10");

            if (AMT1 == null || AMT2 == null || AMT3 == null || AMT4 == null || AMT5 == null ||
                    AMT6 == null || AMT7 == null || AMT8 == null || AMT9 == null || AMT10 == null) {
                flag = "主要财务指标未填写完整,";
                query = false;
            }
        }
    }*/

    //企业贷款用途
    String sql2 = "select * from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=5";
    CachedRowSet crs2 = manager.getRs(sql2);
    if (crs2.size() == 0) {
        //System.out.println("*********************************************企业贷款用途"+crs2.size());
        flag += "贷款用途未填写完整,";
        query = false;
    } else {
        if (crs2.next()) {
            CMT1 = crs2.getString("CMT1");
            CMT2 = crs2.getString("CMT2");
        }
        if (CMT1 == null || CMT1.equals("") || CMT2 == null || CMT2.equals("")) {
            flag += "贷款用途未填写完整,";
            query = false;
        }
    }

    /*//企业担保分析
    String sql5 = "select * from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=6";
    CachedRowSet crs5 = manager.getRs(sql5);
    if (crs5.size() == 0) {
        //System.out.println("*********************************************企业担保分析"+crs5.size());
        flag += "担保分析未填写完整,";
        query = false;
    } else {
        if (crs5.next()) {
            CMT1 = crs5.getString("CMT1");
        }
        if (CMT1 == null || CMT1.equals("")) {
            flag += "担保分析未填写完整,";
            query = false;
        }
    }
    //企业财务分析
    String sql10 = "select * from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=7";
    CachedRowSet crs10 = manager.getRs(sql10);
    if (crs10.size() == 0) {
        //System.out.println("*********************************************企业财务分析"+crs5.size());
        flag += "财务分析未填写完整,";
        query = false;
    } else {
        if (crs10.next()) {
            CMT1 = crs10.getString("CMT1");
        }
        if (CMT1 == null || CMT1.equals("")) {
            flag += "财务分析未填写完整,";
            query = false;
        }
    }
    //企业非财务因素分析
    String sql3 = "select * from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=8";
    CachedRowSet crs3 = manager.getRs(sql3);
    if (crs3.size() == 0) {
        System.out.println("*********************************************企业非财务因素分析" + crs3.size());
        flag += "非财务因素分析未填写完整,";
        query = false;
    } else {
        if (crs3.next()) {
            CMT1 = crs3.getString("CMT1");
            //CMT2=crs3.getString("CMT2");
            //CMT3=crs3.getString("CMT3");
            //CMT4=crs3.getString("CMT4");
        }
        if (CMT1 == null || CMT1.equals("")) {
            flag += "非财务因素分析未填写完整,";
            query = false;
        }

    }*/

    //企业其它
    /* String sql4="select * from FCMCMT where FCNO='"+FCNO+"' and FCCMTTYPE=9";
       CachedRowSet crs4=manager.getRs(sql4);
       if(crs4.size()==0){
           //System.out.println("*********************************************企业其它"+crs4.size());
         flag+="其它未填写完整,";
         query=false;
       }
       else{
           while(crs4.next()){
             CMT1=crs4.getString("CMT1");
             if(CMT1==null || CMT1.equals("")){
                 flag+="其它未填写完整,";
                 query=false;
             }
          }
       }*/

    if (!query) {
        flag = flag.substring(0, flag.length() - 1);
        request.setAttribute("mess", flag);
%>
<jsp:forward page="/fcworkbench/one.jsp"/>
<%
} else {
//    String[] sql = new String[2];
    String sql11 = "", sql12 = "";
    String query_sql = " select * from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=1";
//    sql[0] = "update FCMAIN set FC1=" + FC1 + ",FCSTATUS=2,USER1='" + um.getUserName() + "',OPERATOR='" + um.getUserName()
    sql11 = "update FCMAIN set FC1=" + FC1 + ",FCSTATUS=2,USER1='" + um.getUserName() + "',OPERATOR='" + um.getUserName()
            + "',LASTMODIFIED=" + DBUtil.toSqlDate(SystemDate.getSystemDate5(""))
            + ",BADREASON=" + BADREASON + " ,LOANWAY='" + LOANWAY + "'"                                                 //lj added BADREASON,LOANWAY in 2007-04-28
            + " where FCNO='" + FCNO + "'";
//    CachedRowSet crs = manager.getRs(query_sql);
//    if (crs.next()) {
//        sql[1] = "update FCCMT set CMT1='" + DBUtil.toDB(CMT11) + "',CMT2='" + DBUtil.toDB(CMT12) + "' where FCCMTTYPE=1 and FCNO='" + FCNO + "'";
//    } else {
//        sql[1] = "insert into FCCMT(FCNO,FCCMTTYPE,CMT1,CMT2) values('" + FCNO + "',1,'" + DBUtil.toDB(CMT11) + "','" + DBUtil.toDB(CMT12) + "')";
//    }
//    temp = manager.execBatch(sql);

    try {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query_sql);
        if (rs.next()) {
            //sql[1] = "update FCCMT set CMT1='" + DBUtil.toDB(CMT11) + "',CMT2='" + DBUtil.toDB(CMT12) + "' where FCCMTTYPE=1 and FCNO='" + FCNO + "'";
            sql12 = "update FCCMT set CMT1='" + DBUtil.toDB(CMT11) + "',CMT2='" + DBUtil.toDB(CMT12) + "' where FCCMTTYPE=1 and FCNO='" + FCNO + "'";
        } else {
            //sql[1] = "insert into FCCMT(FCNO,FCCMTTYPE,CMT1,CMT2) values('" + FCNO + "',1,'" + DBUtil.toDB(CMT11) + "','" + DBUtil.toDB(CMT12) + "')";
            sql12 = "insert into FCCMT(FCNO,FCCMTTYPE,CMT1,CMT2) values('" + FCNO + "',1,'" + DBUtil.toDB(CMT11) + "','" + DBUtil.toDB(CMT12) + "')";
        }
        rs.close();
        if (stmt.executeUpdate(sql11) >= 0 && stmt.executeUpdate(sql12) >= 0) {
            conn.commit();
            temp = true;
            stmt.close();
        } else {
            conn.rollback();
            stmt.close();
        }
    } catch (Exception
            ex) {
        conn.rollback();
        throw new Exception(ex.getMessage());
    } finally {
        if (conn != null) {
            conn.close();
        }
    }

    //System.out.println("****************************企业");

    if (temp) {
        session.setAttribute("mess", "提交到三级审批！");
        response.sendRedirect("../fcworkbench/lettersucces.jsp");

    } else {
        request.setAttribute("mess", "提交信息失败");
%>
<jsp:forward page="/fcworkbench/one.jsp"/>
<%
        }
    }
} else { //自然人农户
    //String[] sql = new String[2];
    String sql11 = "", sql12 = "";
    String query_sql = " select * from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=1";
    //sql[0] = "update FCMAIN set FC1=" + FC1 + ",FCSTATUS=2,USER1='" + um.getUserName() + "',OPERATOR='" + um.getUserName()
    sql11 = "update FCMAIN set FC1=" + FC1 + ",FCSTATUS=2,USER1='" + um.getUserName() + "',OPERATOR='" + um.getUserName()
            + "',LASTMODIFIED=" + DBUtil.toSqlDate(SystemDate.getSystemDate5(""))
            + ",BADREASON=" + BADREASON + " ,LOANWAY='" + LOANWAY + "'"                                                 //lj added BADREASON,LOANWAY in 2007-04-28
            + " where FCNO='" + FCNO + "'";
//    CachedRowSet crs = manager.getRs(query_sql);
//    if (crs.next()) {
//        //sql[1] = "update FCCMT set CMT1='" + DBUtil.toDB(CMT11) + "',CMT2='" + DBUtil.toDB(CMT12) + "' where FCCMTTYPE=1 and FCNO='" + FCNO + "'";
//        sql2 = "update FCCMT set CMT1='" + DBUtil.toDB(CMT11) + "',CMT2='" + DBUtil.toDB(CMT12) + "' where FCCMTTYPE=1 and FCNO='" + FCNO + "'";
//    } else {
//        //sql[1] = "insert into FCCMT(FCNO,FCCMTTYPE,CMT1,CMT2) values('" + FCNO + "',1,'" + DBUtil.toDB(CMT11) + "','" + DBUtil.toDB(CMT12) + "')";
//        sql2 = "insert into FCCMT(FCNO,FCCMTTYPE,CMT1,CMT2) values('" + FCNO + "',1,'" + DBUtil.toDB(CMT11) + "','" + DBUtil.toDB(CMT12) + "')";
//    }


    try {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query_sql);
        if (rs.next()) {
            sql12 = "update FCCMT set CMT1='" + DBUtil.toDB(CMT11) + "',CMT2='" + DBUtil.toDB(CMT12) + "' where FCCMTTYPE=1 and FCNO='" + FCNO + "'";
        } else {
            sql12 = "insert into FCCMT(FCNO,FCCMTTYPE,CMT1,CMT2) values('" + FCNO + "',1,'" + DBUtil.toDB(CMT11) + "','" + DBUtil.toDB(CMT12) + "')";
        }
        rs.close();
        if (stmt.executeUpdate(sql11) >= 0 && stmt.executeUpdate(sql12) >= 0) {
            conn.commit();
            temp = true;
            stmt.close();
        } else {
            conn.rollback();
            stmt.close();
        }
    } catch (Exception
            ex) {
        conn.rollback();
        throw new Exception(ex.getMessage());
    } finally {
        if (conn != null) {
            conn.close();
        }
    }

    //temp = manager.execBatch(sql);
    //System.out.println("****************************自然人农户");

    if (temp) {
        session.setAttribute("mess", "提交到三级审批！");
        response.sendRedirect("../fcworkbench/lettersucces.jsp");
    } else {
        request.setAttribute("mess", "提交信息失败");
%>
<jsp:forward page="/fcworkbench/one.jsp"/>
<%
        }
    }
%>