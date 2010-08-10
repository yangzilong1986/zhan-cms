<%@ page contentType="text/html; charset=GBK" isErrorPage="true"%>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%
	UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	if(um==null){
		response.sendRedirect("../fcworkbench/error.jsp");
	}
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
 %>
<html>
<head>
<base href="<%=basePath%>">
<title>异常页面</title>
<link type="text/css" href="query/web.css" rel="stylesheet">
<style type="text/css">
<!--
body {
	background-image: url(/images/checks_02.jpg);
}

.menutitle{
cursor:pointer;
margin-bottom: 5px;
background-color:#ECECFF;
color:#000000;
width:120px;
padding:2px;
text-align:center;
font-weight:bold;
/*/*/border:1px solid #000000;/* */
}

.submenu{
display:none
	margin-bottom: 0.1em;
	font-size:12px;
}

-->
</style>
<script language="javascript">
function disDet()
{
	var obj=document.all.item("msg");
	if (obj.style.display=="none")
	{
		obj.style.display="";
		
	}
	else
	{
		obj.style.display="none";
	}
}
</script>
</head>
<body bgcolor="#FFFFFF">
		<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TR>
				<TD vAlign="center" align="middle">
					<TABLE borderColor=#006699 height=325 cellSpacing=2 cellPadding=2
						align=center bgColor=#aaccee border=2>
						<TR align=left>
							<TD bgColor=#4477aa height=30>
								<IMG src="images/form/xing1.jpg" width="13" height="13"
									align=absMiddle>
								<FONT color=#ffffff size=2><B>错误信息</B>
								</FONT>
								<IMG src="images/form/xing1.jpg" width="13" height="13"
									align="absMiddle">
							</TD>
						</TR>
						<TR align="middle">
							<TD vAlign="center">
								<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%"
									border=0>
									<TR>
										<TD width=20>
											&nbsp;
										</TD>
										<TD vAlign="center" align="middle">
											<SCRIPT src="贷后管理_files/querybutton.js" type=text/javascript></SCRIPT>

											<SCRIPT src="贷后管理_files/meizzDate.js" type=text/javascript></SCRIPT>

											<TABLE>
												<TR>
													<TD>
														<TABLE cellSpacing=0 cellPadding=0 width=538 border=0>
															<TR>
																<TD align="middle" height=0>
																	&nbsp;
																</TD>
															</TR>
														</TABLE>
														<div id="masterdiv">
															<br>
															<table width=480 border=0 align='center' cellpadding=1
																cellspacing=1 class='list_form_table'>
													
													
													
													
																<tr class=list_form_tr>
																	<td align='center' class='table_list' height='0'>
																		<%
																			String msg=exception.getMessage();
																			if (!msg.equals(""))
																				out.println(msg+"<br>");
																					//out.write("系统繁忙，请稍后再试！<br>");
																			else
																				out.println("系统异常，未知错误！"+"<br>");
																	%>
																	</td>
																</tr>
																<tr>
																<td>
																<input type="button" name="btn1" value="查看详细" onclick="disDet();" class="button">
																</td>
																</tr>
																<tr>
																<td>
																<div id="msg" style='display:none'> 
																<%
																StackTraceElement el[]=exception.getStackTrace();
																for(int i=0;i<el.length;i++)
																{
																	out.println(el[i]+"<br>");
																}
																%>
																</div>
																<td>
																</tr>
															</table>
											</div>
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
			<TR vAlign="center" align="middle" height="35">
				<TD>
					&nbsp;
				</TD>
			</TR>
		</TABLE>
		</TD>
		</TR>
		</TABLE>
</body>
</html>