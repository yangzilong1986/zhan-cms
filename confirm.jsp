<%--
*******************************************************************
*    程序名称:    confirm.jsp
*    程序标识:
*    功能描述:    显示确认信息的页面。
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

<%@ page contentType="text/html; charset=GBK" %>
<%
String strTitle = (String)request.getAttribute("title");
String strMsg = (String)request.getAttribute("msg");
String tmpvalue = (String)request.getAttribute("tmpvalue");
String Instance_ID = (String)request.getAttribute("Plat_Form_Request_Instance_ID");
String Event_ID = (String)request.getAttribute("Plat_Form_Request_Event_ID");
String Event_Value = (String)request.getAttribute("Plat_Form_Request_Event_Value");
String Button_Event = (String)request.getAttribute("Plat_Form_Request_Button_Event");

strTitle = (strTitle==null?"":strTitle.trim());
strMsg = (strMsg==null?"":strMsg.trim());
tmpvalue = (tmpvalue==null?"":tmpvalue.trim());
Instance_ID = (Instance_ID==null?"":Instance_ID.trim());
Event_ID = (Event_ID==null?"":Event_ID.trim());
Event_Value = (Event_Value==null?"":Event_Value.trim());
Button_Event = (Button_Event==null?"":Button_Event.trim());
%>
<html>
	<head>
		<title>确认信息</title>
		<link href="../css/platform.css" rel="stylesheet" type="text/css">
	</head>
	<style type="text/css">
	<!--
		.success { color:#000000;}
		.fail { color:#FF0000;}
	-->
	</style>
	<script language="javascript">
		function gosubmit(){
			woindow.form1.submit();
		}
	</script>
	<body background="../images/checks_02.jpg">
	<form name="form1" method="post" action="action='/templates/defaultform.jsp'">
		<input type='hidden' name='Plat_Form_Request_Instance_ID' value='<%=Instance_ID%>'>
		<input type='hidden' name='Plat_Form_Request_Event_ID' value='<%=Event_ID%>'>
		<input type='hidden' name='Plat_Form_Request_Event_Value' value='<%=Event_Value%>'>
		<input type='hidden' name='Plat_Form_Request_Button_Event' value='<%=Button_Event%>'>
		<input type='hidden' name='tmpvalue' value='<%=tmpvalue%>'>
	</form>
  <table width="160" height="200" border="" align="center" bordercolorlight="#336699" bordercolordark="#336699" bgcolor="#9DBBD9" onKeyDown="pressKeyDown(event.keyCode);" >
			<tr align="center">
			    <td valign="top">
					<table>
						<tr>
							<td><img src="../images/title.jpg" width="290" height="49"></td>
						</tr>
					</table>
					<table width="100%" height="100" class='page_form_table'>
						<tr class='page_form_tr'>
							<td class='page_form_td'><%=strMsg%></td>
						</tr>
					</table>
					<table class='page_button_tbl'>
						<tr class='page_button_tbl_tr'
							<td class='page_button_tbl_td'>
								<input class="list_button_active" type="button" name="LASTSTEP" value=" 确 定 " onClick="gosubmit();">
							</td>>
							<td class='page_button_tbl_td'>
								<input class="list_button_active" type="button" name="OKBUTTON" value="取 消 " onClick="window.close();">
							</td>
						</tr>
					</table>
					</td>
			</tr>
	</table>
	</body>
</html>
