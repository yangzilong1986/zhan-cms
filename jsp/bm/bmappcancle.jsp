<%--
*******************************************************************
*    程序名称:    bmappcancle.jsp
*    程序标识:
*    功能描述:    登记取消确认页面
*    连接网页:
*    传递参数:
*    作   者:     wxj
*    开发日期:    2004-02-06
*    修 改 人:    
*    修改日期:    
*    版    权:    青岛中天信息技术有限公司
*
*
*******************************************************************
--%>

<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cmsi.biz.*" %>
<%
//rooturl
String rooturl="http://"+request.getServerName();
int svrport=request.getServerPort();
if(svrport!=80){
	rooturl+=":"+svrport;
}
rooturl+=request.getContextPath();
//request
String act = (String)request.getParameter("act");
String BMNo = null;
String BMTransNo = null;
String OPERATOR = null;
if(act==null){
	BMNo = (String)request.getAttribute("BMNo");
	BMTransNo = (String)request.getAttribute("BMTransNo");
	OPERATOR = (String)request.getAttribute("OPERATOR");
}
else{
	BMNo = (String)request.getParameter("BMNo");
	BMTransNo = (String)request.getParameter("BMTransNo");
	OPERATOR = (String)request.getParameter("OPERATOR");
}

String msg = "你确定要取消该笔业务吗？";
int flg=0;

BMNo = (BMNo==null?"":BMNo.trim());
BMTransNo = (BMTransNo==null?"":BMTransNo.trim());
OPERATOR = (OPERATOR==null?"":OPERATOR.trim());
act = (act==null?"":act.trim());

if(act.equals("del")){
	try{
		flg = BMTrans.cancelBMTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
		if (flg >= 0) {
			msg="登记取消成功！";
		}
		else{
			msg="登记取消失败！";
		}
	}
	catch(Exception ex){
		msg="未知原因导致的系统错误！";
	}
}
%>
<html>
	<head>
		<title>确认信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rooturl%>/css/platform.css" rel="stylesheet" type="text/css">
	</head>
	<body background="<%=rooturl%>/images/checks_02.jpg">
	<form name="form1" method="post" action="<%=rooturl%>/jsp/bm/bmappcancle.jsp">
		<input type='hidden' name='BMNo' value='<%=BMNo%>'>
		<input type='hidden' name='BMTransNo' value='<%=BMTransNo%>'>
		<input type='hidden' name='OPERATOR' value='<%=OPERATOR%>'>
		<input type='hidden' name='act' value=''>
	</form>
	<script language="javascript">
	function gosubmit(){
		document.form1.act.value="del";
		document.form1.submit();
	}
	</script>
  <table width="160" height="200" border="" align="center" bordercolorlight="#336699" bordercolordark="#336699" bgcolor="#9DBBD9" onKeyDown="pressKeyDown(event.keyCode);" >
			<tr align="center">
			    <td valign="top">
					<table>
						<tr>
							<td><img src="<%=rooturl%>/images/title.jpg" width="290" height="49"></td>
						</tr>
					</table>
					<table width="100%" height="100" class='page_form_table'>
						<tr class='page_form_tr'>
							<td class='page_form_td'><%=msg%></td>
						</tr>
					</table>
					<table class='page_button_tbl'>
						<tr class='page_button_tbl_tr'>
							<td class='page_button_tbl_td'>
							<%
								if(!act.equals("del")){
							%>
								<input class="list_button_active" type="button" name="ok" value=" 确 定 " onClick="gosubmit();">
							<%
								}
							%>
								<input class="list_button_active" type="button" name="close" value=" 关 闭 " onClick="window.close();">
							</td>
						</tr>
					</table>
					</td>
			</tr>
	</table>
	</body>
</html>
