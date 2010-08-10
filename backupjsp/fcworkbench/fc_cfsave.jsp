<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%
    request.setCharacterEncoding("GBK");

    String[] FCNO = request.getParameterValues("FCNO");
    if (FCNO == null || FCNO.length == 0) {
        response.sendRedirect("fc_cf.jsp");
    }
    ConnectionManager manager = ConnectionManager.getInstance();
    boolean temp = false;
    int flag = 0;
    String sqls = "";
    String operbrhid = "";

    for (int i = 0; i < FCNO.length; i++) {
        CachedRowSet rs = manager.getRs("Select brhid from fcmain where FCNO='" + FCNO[i] + "'");
        if (rs.next()) {
            operbrhid = SCBranch.getSupBrh(rs.getString("brhid"), EnumValue.BrhLevel_XinYongShe);
            if (operbrhid == null) {
                request.setAttribute("mess", "获得信贷部门级别网点失败");
%>
<jsp:forward page="/fcworkbench/fc_cf.jsp"/>
<%
            }
        }
//        sqls = "update FCMAIN set  FCSTATUS=1,operbrhid='" + operbrhid + "' where FCNO='" + FCNO[i] + "' "; //lj delete in 2007-06-12
//        temp = manager.ExecCmd(sqls);                                                                       //lj delete in 2007-06-12
        
        //****************lj changed in 2007-06-12 for 清除清分意见及清分结果。
        String[] sql = new String[2];
        sql[0] = "update FCMAIN set FC1=null,FC2=null,FC3=null,FCSTATUS=1,operbrhid='" + operbrhid + "' where FCNO='" + FCNO[i] + "' ";
        sql[1] = "delete from FCCMT where FCCMTTYPE=1 and FCNO='" + FCNO[i] + "'";
        temp = manager.execBatch(sql);
        //****************lj chenged end        

        if (temp == false) {
            flag = flag + 1;
        }
    }
    if (temp == true && flag == 0) {
        //request.setAttribute("mess","重分成功");
%>
<jsp:forward page="/fcworkbench/fc_cf.jsp"/>
<%
} else {
    request.setAttribute("mess", "重分失败");
%>
<jsp:forward page="/fcworkbench/fc_cf.jsp"/>
<%
    }

%>