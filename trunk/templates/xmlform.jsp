<%--
*******************************************************************
*    ��������:    xmlform.jsp
*    �����ʶ:
*    ��������:    Form����ҳ��
*    ������ҳ:
*    ���ݲ���:
*    ��   ��:     JGO(GZL)
*    ��������:    2005-8-05
*    �޸�����:    
*    ��    Ȩ:    �ൺ������Ϣ�������޹�˾
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