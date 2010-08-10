<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cmsi.pub.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.platform.form.util.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>

<%
MyDB.getInstance().removeCurrentThreadConn("workbench.new_affair.jsp"); //added by JGO on 2004-07-17
%>

<%
request.setCharacterEncoding("GB2312");

String affairName=request.getParameter("affairName");
//System.out.println(affairName);
request.setAttribute(SessionAttributes.BACKGROUND_DISPATCH,"ok");


String formId="";
if(affairName.equals("creditRegist")){
    formId="NEWCREDTPAGE";
}else if(affairName.equals("assertToDebtRegist")){
    
}else{
    formId="BMASSERTTODEBTLIST";
}



String flag="read";


RequestDispatcher rd=request.getRequestDispatcher("/templates/defaultform.jsp?Plat_Form_Request_Form_ID="+formId+"&Plat_Form_Request_Event_ID=0&flag="+flag+"");
rd.forward(request,response);

%>