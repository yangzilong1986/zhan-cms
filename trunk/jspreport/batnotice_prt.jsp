<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/Ming.tld" prefix="ming" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="com.ming.webreport.*" %>
<%@ page import="zt.cms.report.notify.PNotify" %>

<HTML>
<HEAD>
<TITLE> �������֪ͨ��������ӡ </TITLE>
</HEAD>
<%
String errMsg=null;
String strRs=null;
RequestDispatcher rd=null;

PNotify pnotify=new PNotify(request);
errMsg=pnotify.errMsg;
if(errMsg!=null)
{
     request.setAttribute("msg",errMsg);
     rd=request.getRequestDispatcher("/showinfo.jsp");
     rd.forward(request,response);
}
else
{
	strRs=pnotify.getRS();
	errMsg=pnotify.getErrMsg();
	if(errMsg!=null)
	{
	   request.setAttribute("msg",errMsg);
	   rd=request.getRequestDispatcher("/showinfo.jsp");
	   rd.forward(request,response);
	}
	  
}

%>
<BODY>
<table height="100%" width="100%" border=0 cellpadding=0 cellspacing=0>
  <tr height="95%">
    <td valign="top">
<%
	if(strRs==null)
	{
		request.setAttribute("msg","��������Ԥ֪����");
        rd=request.getRequestDispatcher("/showinfo.jsp");
        rd.forward(request,response);
     }
	else
	   out.print(strRs);
%>
    </td>
  </tr>
  <tr height="5%" bgColor="lightskyblue">
    <td align=center>
      <input type="button" name="back" class="list_button_active" value="��һ��" onClick="history.go(-1);">
      <input type=button   name="close" value="�ر�" onclick="window.close()	">
    </td>
  </tr>
</table>
</BODY>
</HTML>