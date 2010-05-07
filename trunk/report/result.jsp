<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>


<html>
	<head>
		<title>defaultform</title>
		<link href="../css/platform.css" rel="stylesheet" type="text/css">
		<script language="JavaScript" src="../js/meizzDate.js"></script>
		<script src='../js/pagebutton.js' type='text/javascript'></script>
	</head>
	<body background="../images/checks_02.jpg">
			     
<p>&nbsp;</p>
<p>&nbsp;</p>
<table height="219" border="" align="center" bordercolorlight="#336699" bordercolordark="#336699" bgcolor="#9DBBD9" onKeyDown="pressKeyDown(event.keyCode);" >
  <tr align="left">
			     
    <td height="30" class="form_title_label">&nbsp; </td>
			</tr>
			<tr align="center" valign="middle">
			     
    <td height="159"> 
      <table class="error_message_tbl">
        <tr class="error_message_tbl_tr"> 
          <td width="20">&nbsp;</td>
          <td class="error_message_li"><%=session.getAttribute("info")%>
            <%session.removeAttribute("info");%>
          </td>
          <td width="20">&nbsp;</td>
        </tr>
      </table></td>
			</tr>
			<tr align="center" valign="bottom">
			        
    <td>
        <input type="button" name="Submit2" value="上一步" onClick="history.go(-1);" class="page_form_button_active">
      <input name="Submit" type="submit" class="page_form_button_active" value="关闭" onClick="parent.close()"> </td>
			</tr>
	</table>
	</body>
</html>
