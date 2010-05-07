<%--
*******************************************************************
*    程序名称:    defaultform.jsp
*    程序标识:
*    功能描述:    Form处理页面
*    连接网页:
*    传递参数:
*    作   者:     wdl
*    开发日期:    2004-02-05
*    修 改 人:    wxj
*    修改日期:    
*    版    权:    青岛中天信息技术有限公司
*
*
*******************************************************************
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>

<!--jsp:include page="/checkpermission.jsp"/-->

<% 
MyDB.getInstance().removeCurrentThreadConn("defaultform.jsp"); //added by JGO on 2004-07-17

ServiceProxy sp = new SeviceProxyHttpImpl(); 
sp.service(request,response);
%>
<html>
	<head>
		<title>信贷管理</title>
		<link href="../css/platform.css" rel="stylesheet" type="text/css">
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<style type="text/css">
		<!--
		body {
			margin-top: 5px;
		}
		-->
		</style>
	</head>
	<body background="../images/checks_02.jpg">
    <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" valign="middle">
				<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#816A82" bgcolor="#E0E0D3" >
          <tr align="left">
            <td height="30" bgcolor="#A4AEB5">
						<img src="../images/form/xing1.jpg" align="absmiddle">
						<font size="2" color="#FFFFFF"><b><%=sp.getHead()%></b></font>
						<img src="../images/form/xing1.jpg" align="absmiddle"></td>
          </tr>
          <tr align="center">
            <td height="260" valign="middle">
              <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                <tr>
                  <td width="20">&nbsp;</td>
                  <td align="center" valign="middle">
<%=sp.getBody()%>
									</td>
                  <td width="20">&nbsp;</td>
                </tr>
            	</table>
						</td>
          </tr>
          <tr height="35" align="center" valign="middle">
						<td align="center">
							<table border="0" cellspacing="0" cellpadding="0" width="538">
								<tr>
<%
String sysButton=sp.getSysButton();
String afterSysButton=sp.getAfterSysButton();
if(afterSysButton!=null){
%>
									<td nowrap align="left" width="60%"><%=sysButton%></td>
									<td nowrap align="left" width="40%">&nbsp;<font size="2" color="#000066"><%=afterSysButton%></font></td>
<%
}
else{
%>
									<td nowrap align="center"><%=sysButton%></td>
<%
}

MyDB.getInstance().removeCurrentThreadConn(sp.getProgInfo(request)); //added by JGO on 2004-07-17
%>
								</tr>
							</table>
						</td>
          </tr>
        </table>
				</td>
      </tr>
    </table>    
  </body>
</html>
<script language="javascript">
document.title="<%=sp.getHead()%>";
document.focus();
</script>
