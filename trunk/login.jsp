<%@ page contentType="text/html; charset=gb2312"%>
<%
//Edited by wxj at 040228,delete relogin.jsp
String loginassistorAct=(String)session.getAttribute("loginassistorAct");
String errMsg="";
if(loginassistorAct!=null && loginassistorAct.equals("relogin")){
	errMsg="该用户不可用或密码不正确，请仔细确认后重新登录！";
	session.setAttribute("loginassistorAct",null);
}
if(loginassistorAct!=null && loginassistorAct.equals("exceed")){
	errMsg="用户已经登录系统,系统不允许同一用户多点登录!";
	session.setAttribute("loginassistorAct",null);
}
%>
<script language="javascript">			
	function FocusUsername() {
		with(document.winform) {
			username.focus();
		}
	}
	
	function FocusPassword() {
		with(document.winform) {
			if(username.value.length == 8) {
				password.focus();
			}
		}
	}

	function FocusSubmit() {
		with(document.winform) {
			if(password.value.length == 6) {
				submit.focus();
			}
		}
	}

	function ValidateLength() {
		if(!(document.winform.username.value.length >= 1)) {
			window.alert("用户标识为8位!");
		}
		if(!(document.winform.password.value.length >= 1)) {
			window.alert("用户密码为6位!");
		}
	}
</script>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>海尔财务公司信贷管理系统</title>
</head>
<script src='/js/focusnext.js' type='text/javascript'></script>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form action="home.jsp" method="POST" name="winform" onSubmit="ValidateLength();">
<table width="100%" height="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle">
		<table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right" valign="bottom"><img src="images/login/login1.jpg" width="390" height="107"></td>
        <td align="left" valign="bottom"><img src="images/login/login4.jpg" width="390" height="107"></td>
      </tr>
      <tr>
        <td align="right" valign="middle"><img src="images/login/login2.jpg" width="390" height="150"></td>
        <td align="left" valign="bottom">
					<table width="390" height="100%" border="0" cellspacing="0" cellpadding="0">
            <tr height="30">
              <td width="240">&nbsp;</td>
							<td width="175">&nbsp;</td>
            </tr>
            <tr height="30">
              <td colspan="2" valign="bottom"><font color="#FF0000" size="2">&nbsp;&nbsp;&nbsp;<%=errMsg%></font></td>
            </tr>
            <tr height="30">
              <td align="center" valign="middle"><font size="-1">用户帐号&nbsp;</font>
                  <input name="username" type="text" size="30" style="width:120 ; height:18" maxlength="8" onKeyUp="FocusPassword();"></td>
              <td>&nbsp;</td>
            </tr>
            <tr height="30">
              <td align="center" valign="middle"><font size="-1">用户密码&nbsp;</font>
                  <input name="password" type="password" size="30" style="width:120; height:18" maxlength="6" AUTOCOMPLETE="off"></td>
              <td valign="middle" valign="top"><input name="submit" type="submit" value=" 登 录 " style="width:50; height:20"></td>
            </tr>
        	</table>
				</td>
      </tr>
      <tr>
        <td align="right" valign="top"><img src="images/login/login3.jpg" width="390" height="103"></td>
        <td align="left" valign="top"><img src="images/login/login5.jpg" width="390" height="103"></td>
      </tr>
    </table>
		</td>
  </tr>
</table>
</form>
</body>
</html>
<script language="javascript">
document.all("username").focus();
</script>