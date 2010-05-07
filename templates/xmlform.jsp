<%--
*******************************************************************
*    程序名称:    xmlform.jsp
*    程序标识:
*    功能描述:    Form处理页面
*    连接网页:
*    传递参数:
*    作   者:     JGO(GZL)
*    开发日期:    2005-8-05
*    修改日期:    
*    版    权:    青岛中天信息技术有限公司
*
*
*******************************************************************
--%>
<?xml version="1.0" encoding="GBK" ?> 
<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.ServiceProxyXMLImpl" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>

<!--jsp:include page="/checkpermission.jsp"/-->

<%  
request.setCharacterEncoding("GBK");
MyDB.getInstance().removeCurrentThreadConn("defaultform.jsp"); //added by JGO on 2004-07-17

ServiceProxy sp = new ServiceProxyXMLImpl(); 
sp.service(request,response);
//System.out.println("XML body========="+sp.getBody());
%>
<%=sp.getBody()%>