<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.cmsi.pub.*" %>
<%@ page import=" zt.cmsi.biz.*" %>
<%@ page import=" java.util.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>

<%
MyDB.getInstance().removeCurrentThreadConn("workbench.check.jsp"); //added by JGO on 2004-07-17
%>

<html>
	<head>
		<title>风险检查报告</title>
		<link href="../css/platform.css" rel="stylesheet" type="text/css">
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<style type="text/css">
		<!--
		body {
			margin-top: 5px;
		}
		.lanjie {
			color: #FF0000;
		}
		.jinggao {
			color:#FF6600;
		}
		-->
		</style>
	</head>
	<script src='/js/pagebutton.js' type='text/javascript'></script>
	<body background="../images/checks_02.jpg">
    <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" valign="middle">
				<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" >
          <tr align="left">
            <td height="30" bgcolor="#4477AA">
						<img src="../images/form/xing1.jpg" align="absmiddle">
						<font size="2" color="#FFFFFF"><b>风险检查报告</b></font>
						<img src="../images/form/xing1.jpg" align="absmiddle"></td>
          </tr>
          <tr align="center">
            <td height="260" valign="middle">
              <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                <tr>
                  <td width="20">&nbsp;</td>
                  <td valign="middle">
<table width='466' border="0" align="center" cellpadding="0" cellspacing="1" class='list_form_table'>
	<tr class='list_form_title_tr'>
		<td width='115' class='list_form_title_td'>风险类型</td>
		<td width='337' class='list_form_title_td'>详细说明</td>
	</tr>
	<%
	Param param = (Param)request.getAttribute("BMPARAM");	
	Vector results =(Vector)request.getAttribute("results");
	session.setAttribute("CriCheckResult",results);
	String tmp1=(String)param.getParam("if3rddec");
	boolean if3rddec=true;
	if(tmp1==null || !tmp1.equals("yes")){
		if3rddec=false;
	}
	boolean canContinue=true;
	int index=0;
	Iterator iterCriCheckResult = results.iterator();
	//System.out.println("" + results.size());
	for(index=0;index<10;index++){
		if(iterCriCheckResult.hasNext()){
			CriCheckResult item = (CriCheckResult) iterCriCheckResult.next();
			//System.out.println("" + item.toString());
		if(item.isLanjie()){
			canContinue=false;
		}
		String msg=item.getMessage();
		if(if3rddec) msg=DBUtil.fromDB(msg);
	%>
	<tr class='list_form_tr'>
		<td class='list_form_td'><div align="center" class="<%=item.isLanjie()?"lanjie":"jinggao"%>">风险<%=item.getAlertTypeName()%></div></td>
		<td class='list_form_td'><%=msg%></td>
	</tr>
	<%
		}
		else{
	%>
	<tr class='list_form_tr'>
		<td class='list_form_td'>&nbsp;</td>
		<td class='list_form_td'>&nbsp;</td>
	</tr>
	<%
		}
	}
	%>
</table>
									</td>
                  <td width="20">&nbsp;</td>
                </tr>
            	</table>
						</td>
          </tr>
          <tr height="35" align="center" valign="middle">
            <td>
<form action="/workbench/checkresult.jsp" name="form2">
<%
if(!if3rddec){
	for (Iterator iter = param.getParams().keySet().iterator(); iter.hasNext(); ) {
		String item = (String)iter.next();
%>
	<input type="hidden" name="<%=item%>" value="<%=param.getParams().get(item)%>">
<%
	}
%>
	<table border="0" cellspacing="0" cellpadding="0" class="list_form_button_table">
		<tr class="list_button_tbl_tr">
		 <td class="list_form_button_td"></td>
		 <td class="list_form_button_td"><input name='submit33' type='button' class='list_button_active'  onClick="this.disabled=true;form2.submit()" value=" 继 续 " <%=canContinue?"":"disabled"%>></td>
		 <td class="list_form_button_td"><input type='button' name='button' class='list_button_active' value=' 放 弃 ' onClick="history.go(-1)"></td>
		</tr>
	</table>
<%
}
else{
%>
	<table border="0" cellspacing="0" cellpadding="0" class="list_form_button_table">
		<tr class="list_button_tbl_tr">
		 <td class="list_form_button_td"></td>
		 <td class="list_form_button_td"><input type='button' name='button' class='list_button_active' value=' 退 出 ' onClick="window.close()"></td>
		</tr>
	</table>
<%
}
%>
</form>
						</td>
          </tr>
        </table>
				</td>
      </tr>
    </table>    
  </body>
</html>