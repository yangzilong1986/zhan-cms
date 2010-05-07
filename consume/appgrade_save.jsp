<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.cmsi.pub.define.XFGradeMark" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DecimalFormat" %>
<!--************************************lj del in 2007-06-07-->
<%--<%@ page import="java.math.BigDecimal" %>--%>
<!--************************************lj del end in 2007-06-07-->

<%--
===============================================
Title: 个人消费分期付款信用评分表提交
Description: 个人消费分期付款信用评分表提交。
 * @version  $Revision: 1.0 $  $Date: 2009/03/20 06:33:37 $
 * @author
 * <p/>修改：$Author: liuj $
===============================================
--%>

<%
    request.setCharacterEncoding("GBK");
//    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (um == null) {
//        response.sendRedirect("../fcworkbench/error.jsp");
//    }
//    System.out.println(request.getAttribute("APPNO"));

    String APPNO = request.getParameter("APPNO");                   //申请单编号
    String AGE = request.getParameter("AGE");                       //年龄
    String BURDENSTATUS = request.getParameter("BURDENSTATUS");    //负担状况
    String CCRPRATE = request.getParameter("CCRPRATE");            //债务收入比
    String SORT1 = request.getParameter("SORT1");
    String SORT2 = request.getParameter("SORT2");
    String SORT3 = request.getParameter("SORT3");
    String SORT4 = request.getParameter("SORT4");
    String SORT5 = request.getParameter("SORT5");
    String SORT6 = request.getParameter("SORT6");
    String SORT7 = request.getParameter("SORT7");
    String SORT8 = request.getParameter("SORT8");
    String SORT9 = request.getParameter("SORT9");
    String SORT10 = request.getParameter("SORT10");

    APPNO = (APPNO == null) ? "" : APPNO.trim();    //申请单编号
    if (APPNO.equals("")) {
        session.setAttribute("msg", "没有发现传送入的参数！");
        response.sendRedirect("../showinfo.jsp");
    }

    boolean temp = false;
    String flag = "";
    boolean query = true;
    if (!query) {
        flag = flag.substring(0, flag.length() - 1);
        request.setAttribute("mess", flag);
%>
<jsp:forward page="./appgrade.jsp"/>
<%
} else {

    //*****************************************************
    // 自动计算分值
    int gradesum = 0, grade = 0;

    grade = Integer.parseInt(XFGradeMark.getInstance().getGradeMarkGradeByLimit("Gender", SORT1, AGE));
    if (gradesum == -1 || grade == 0) gradesum = -1;
    else gradesum += grade;

    grade = Integer.parseInt(XFGradeMark.getInstance().getGradeMarkGradeByLimit("MarriageStatus", SORT2, BURDENSTATUS));
    if (gradesum == -1 || grade == 0) gradesum = -1;
    else gradesum += grade;

    grade = Integer.parseInt(XFGradeMark.getInstance().getGradeMarkGradeByLimit("EduLevel", SORT3, null));
    if (gradesum == -1 || grade == 0) gradesum = -1;
    else gradesum += grade;

    grade = Integer.parseInt(XFGradeMark.getInstance().getGradeMarkGradeByLimit("DeptEmpTp", SORT4, CCRPRATE));
    if (gradesum == -1 || grade == 0) gradesum = -1;
    else gradesum += grade;

    grade = Integer.parseInt(XFGradeMark.getInstance().getGradeMarkGradeByLimit("SectorLev", SORT5, null));
    if (gradesum == -1 || grade == 0) gradesum = -1;
    else gradesum += grade;

    grade = Integer.parseInt(XFGradeMark.getInstance().getGradeMarkGradeByLimit("EmpKind", SORT6, null));
    if (gradesum == -1 || grade == 0) gradesum = -1;
    else gradesum += grade;

    grade = Integer.parseInt(XFGradeMark.getInstance().getGradeMarkGradeByLimit("Position", SORT7, null));
    if (gradesum == -1 || grade == 0) gradesum = -1;
    else gradesum += grade;
    
    grade = Integer.parseInt(XFGradeMark.getInstance().getGradeMarkGradeByLimit("WorkYear", SORT8, null));
    if (gradesum == -1 || grade == 0) gradesum = -1;
    else gradesum += grade;

    if (gradesum == -1) gradesum = 0;
    //*****************************************************


    String[] sql = new String[1];
    String sql1 = " select * from XFAPPGRADE where APPNO='" + APPNO + "'";

    ConnectionManager manager = ConnectionManager.getInstance();
    try {
        CachedRowSet crs = manager.getRs(sql1);
        if (crs.next()) {
            sql[0] = "update XFAPPGRADE set sort1='" + SORT1 + "',sort2='" + SORT2 + "',sort3='" + SORT3 + "',sort4='" + SORT4 + "',sort5='" + SORT5 + "',sort6='" + SORT6 + "',sort7='" + SORT7 + "',sort8='" + SORT8 + "',sort9='" + SORT9 + "',sort10='" + SORT10 + "',grade='" + gradesum + "' where appno='" + APPNO + "'";
        } else {
            sql[0] = "insert into XFAPPGRADE (appno, sort1, sort2, sort3, sort4, sort5, sort6, sort7, sort8, sort9, sort10,grade) values ('" + APPNO + "','" + SORT1 + "','" + SORT2 + "','" + SORT3 + "','" + SORT4 + "','" + SORT5 + "','" + SORT6 + "','" + SORT7 + "','" + SORT8 + "','" + SORT9 + "','" + SORT10 + "','" + gradesum + "')";
        }
        temp = manager.execBatch(sql);
    } catch (Exception e) {
        e.printStackTrace();
    }

    if (temp) {
        session.setAttribute("msg", "提交成功！");
        session.setAttribute("isback", "0");
        response.sendRedirect("../showinfo.jsp");
    } else {
        session.setAttribute("msg", "提交信息失败!");
%>
<jsp:forward page="./appgrade.jsp"/>
<%
        }
    }
%>