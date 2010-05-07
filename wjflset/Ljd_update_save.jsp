<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cmsi.pub.define.BMFCLimit" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    request.setCharacterEncoding("GBK");
    ConnectionManager manager = ConnectionManager.getInstance();

    boolean temp = false;
    String brhid;
    if (request.getParameter("brhid") == null) {
        brhid = null;
    } else {
        brhid = request.getParameter("brhid").trim();
    }

    String LIMITAMT;
    if (request.getParameter("LIMITAMT") == null) {
        LIMITAMT = null;
    } else {
        LIMITAMT = request.getParameter("LIMITAMT").trim().replaceAll(",", "");
    }

    String FCTypeAMT;
    if (request.getParameter("FCTypeAMT") == null) {
        FCTypeAMT = null;
    } else {
        FCTypeAMT = request.getParameter("FCTypeAMT").trim().replaceAll(",", "");
    }

    String sql = "update FCREVIEWLIMIT set LIMITAMT=" + LIMITAMT + ",FCTypeAMT=" + FCTypeAMT + " where BRHID='" + brhid + "'";
    try {
        temp = manager.ExecCmd(sql);
    } catch (Exception e) {
        e.printStackTrace();
    }

    BMFCLimit.setDirty();
    if (temp) {
        session.setAttribute("mess", "修改信息成功");
        response.sendRedirect("../wjflset/Ljd_info.jsp?BRHID=" + brhid);
    } else {
        session.setAttribute("mess", "修改信息失败");
        response.sendRedirect("../wjflset/Ljd_info.jsp?BRHID=" + brhid);
    }
%>  