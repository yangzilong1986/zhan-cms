<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>

<%--
===============================================
Title: 贷款用途保存
Description: 贷款用途信息保存。
 * @version   $Revision: 1.3 $  $Date: 2007/05/29 09:34:03 $
 * @author
 * <p/>修改：$Author: liuj $
===============================================
--%>

<%
    request.setCharacterEncoding("GBK");
    ConnectionManager manager = ConnectionManager.getInstance();
    String FCNO = request.getParameter("FCNO");
    String FCSTATUS = request.getParameter("FCSTATUS");
    if (FCNO == null || FCSTATUS == null) {
        session.setAttribute("lettermess", "没有发现传送入的参数！");
        response.sendRedirect("/fcworkbench/lettersucces.jsp");
    }
    boolean temp = false;
    String CMT1 = null;
    if (request.getParameter("CMT1") == null) {
        CMT1 = null;
    } else {
        CMT1 = request.getParameter("CMT1").trim();
    }
    String CMT2 = null;
    if (request.getParameter("CMT2") == null) {
        CMT2 = null;
    } else {
        CMT2 = request.getParameter("CMT2").trim();
    }
    String sql = "";
    String query_sql = "select *from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=5";
    CachedRowSet crs = manager.getRs(query_sql);
    if (crs.next()) {
        sql = "update FCCMT set cmt1='" + DBUtil.toDB(CMT1).trim() + "',cmt2='" + DBUtil.toDB(CMT2).trim() + "'" +
                " where FCNO='" + FCNO + "' and FCCMTTYPE=5";
    } else {
        sql = "insert into FCCMT(FCNO,FCCMTTYPE,CMT1,CMT2) values('" + FCNO + "',5,'" + DBUtil.toDB(CMT1).trim() + "','" + DBUtil.toDB(CMT2).trim() + "')";
    }
    temp = manager.ExecCmd(sql);

    if (temp) {
        request.setAttribute("mess", "添加信息成功");
%>
<jsp:forward page="/fcworkbench/q_dkyt/q_dkyt.jsp"/>
<%
} else {
    request.setAttribute("mess", "添加信息失败");
%>
<jsp:forward page="/fcworkbench/q_dkyt/q_dkyt.jsp"/>
<%
    }
%>