<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/Ming.tld" prefix="ming" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="com.ming.webreport.*" %>
<%@ page import="zt.cms.report.notify.LoanBiz" %>

<HTML>
<HEAD>
<TITLE> 凭证或授权打印 </TITLE>
</HEAD>
<%
  String errMsg=null;
   String year = null;
  String month = null;
  String day = null;
  String root=null;
  String head= null;
  RequestDispatcher rd=null;
  String strRs=null;

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
	  year = loanbiz.getYear();
	  month = loanbiz.getMonth();
	  day = loanbiz.getDate();
	  root= loanbiz.getRoot();
	  head=loanbiz.getHead();


	  strRs=loanbiz.getRS();
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
            if(strRs==null)
			 {
				request.setAttribute("msg","发生不可预知错误！");
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
		<input type="button" name="back" class="list_button_active" value="上一步" onClick="history.go(-1);">
		<input type=button   name="close" value="关闭" onclick="window.close()	">
	</td>
  </tr>
</table>
</BODY>

<script language="JavaScript">
var year= '<%=year%>';
var month='<%=month%>';
var day='<%=day%>';
var head='<%=head%>';
var obj = document.getElementById('MRViewer');
 obj.setVariable('year',year);
 obj.setVariable('month',month);
 obj.setVariable('day',day);
 obj.setVariable('head',head);
 obj.preview();
</script>

</HTML>