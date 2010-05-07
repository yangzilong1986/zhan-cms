<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.define.BMFCLimit" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
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

    String query_sql = "select * from FCREVIEWLIMIT where brhid='" + brhid + "'";
    try {
        CachedRowSet crs = manager.getRs(query_sql);
        if (crs.next()) {
            request.setAttribute("mess", "网点号:" + brhid + "的临界点额度已经存在。");
%>
<jsp:forward page="/wjflset/Ljd_add.jsp"/>
<%
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    String sql = "insert into FCREVIEWLIMIT values('" + brhid + "'," + LIMITAMT + "," + FCTypeAMT + ")";
    try {
        temp = manager.ExecCmd(sql);
    } catch (Exception e) {
        e.printStackTrace();
    }
    BMFCLimit.setDirty();
    if (temp) {
        request.setAttribute("mess", "添加信息成功");
%>
<jsp:forward page="/wjflset/Ljd_add.jsp"/>
<%
} else {
    request.setAttribute("mess", "添加信息失败");
%>
<jsp:forward page="/wjflset/Ljd_add.jsp"/>
<%
    }
%>