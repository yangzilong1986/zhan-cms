<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cmsi.pub.define.BMFCLimit" %>
<%@ page import="zt.cmsi.pub.define.SystemDate" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="java.math.BigDecimal" %>

<%--
===============================================
Title: 清分工作台批量提交
Description: 暂时不使用，如果用须改sql中逻辑，现阶段批量提交由清分截止时点批量完成。
 * @version   $Revision: 1.2 $  $Date: 2007/04/30 08:21:19 $
 * @author   houcs
 * <p/>修改：$Author: liuj $
===============================================
--%>


<%
    request.setCharacterEncoding("GBK");
    BMFCLimit bmf = BMFCLimit.getInstance();
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
        response.sendRedirect("../fcworkbench/error.jsp");
    }
    ConnectionManager manager = ConnectionManager.getInstance();
    boolean temp = false;
    int flag = 0;
    String sqls = "";
    String SUBBRHIDs = SCBranch.getSubBranchAll(SCUser.getBrhId(um.getUserName()).trim());
    if (SUBBRHIDs != null || SUBBRHIDs.length() > 0) SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
    BigDecimal ljd = bmf.getLimitofBrh(SCUser.getBrhId(um.getUserName()));
    if (ljd == null) {
        request.setAttribute("mess", "临界点额度未设置");
%>
<%--<jsp:forward page="/fcworkbench/fc_workbench_save.jsp"/>--%>
<!--lj chenged in 2007-04-29-->
<jsp:forward page="/fcworkbench/fc_workbench.jsp"/>
<%
    }
    String DT = "";
    //CachedRowSet rs=manager.getRs("Select dt from FCPRD where initialized=1 ORDER BY dt desc");
    //if(rs.next()){
    //DT=rs.getString("dt");

    //}
    sqls = "update FCMAIN a set  FCSTATUS=" + String.valueOf(EnumValue.FCStatus_WanCheng) + ",OPERATOR='" + um.getUserName() + "'," +
            "LASTMODIFIED=" + DBUtil.toSqlDate(SystemDate.getSystemDate5("")) + " " +
            //"FC1=(select FC1 from fcmain t where a.bmno=t.bmno and a.billno=t.billno and createdate='"+DT+"'),"+
            //"FC2=(select FC2 from fcmain t where a.bmno=t.bmno and a.billno=t.billno and createdate='"+DT+"'),"+
            //"FC3=(select FC3 from fcmain t where a.bmno=t.bmno and a.billno=t.billno and createdate='"+DT+"') "+
            "where fcstatus=" + String.valueOf(EnumValue.FCStatus_DengJi) + " " +
            "and exists (select * from fcmain t where t.fcno <> a.fcno and a.bmno=t.bmno " +
            "and (a.billno=t.billno or a.billno is null) and  t.fcstatus=" + String.valueOf(EnumValue.FCStatus_WanCheng) + ") and " +
            "fcauto = (select LOANCAT1 from RQLOANLEDGER where a.bmno=rqloanledger.bmno) and " +
            "fcauto=1 and bal < " + ljd + " and " +
            "FCTYPE=" + String.valueOf(EnumValue.FCType_ZiRanRenNongHu) + " and " +
            "brhid in (" + SUBBRHIDs + ")";
    temp = manager.ExecCmd(sqls);

//if(temp==true){
    //request.setAttribute("mess","重分成功");
%>
<jsp:forward page="/fcworkbench/fc_workbench.jsp"/>
<%
    //}


%>