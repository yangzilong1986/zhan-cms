<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/Ming.tld" prefix="ming" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="com.ming.webreport.*" %>
<%@ page import="zt.cms.report.notify.LoanBiz" %>

<HTML>
<HEAD>
<TITLE> 签发银行承兑汇票通知书打印 </TITLE>
</HEAD>
<%
  String errMsg=null;
  String root=null;
  String strPo=null;
  
  RequestDispatcher rd=null;

  LoanBiz loanbiz=new LoanBiz(request);
  errMsg=loanbiz.getErrMsg();
  if(errMsg!=null)
  {
     request.setAttribute("msg",errMsg);
     rd=request.getRequestDispatcher("/showinfo.jsp");
     rd.forward(request,response);
  }
  else
  {

	  strPo=loanbiz.getRSPO();
	  errMsg=loanbiz.getErrMsg();
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
	     if(strPo==null)
			 {
				request.setAttribute("msg","发生不可预知错误！");
                rd=request.getRequestDispatcher("/showinfo.jsp");
                rd.forward(request,response);
			 }
			 else
			   out.print(strPo);
	  %>
    </td>

  </tr>
  <tr height="5%" bgColor="lightskyblue">
	<td align=center>
		<input type="button" name="back" class="list_button_active" value="上一步" onClick="history.go(-1);">
		<input type=button   name="close" value="关闭" onclick="window.close()	">
	</td>
  </tr>
</table>
</BODY>

</HTML>