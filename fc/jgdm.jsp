<%@ page contentType="text/html; charset=GBK"  errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.platform.db.DBUtil,zt.cmsi.fc.FcUpXML" %>
<jsp:useBean id="form1" scope="page" class="zt.cmsi.fc.form.FormJGDM"  />
<jsp:setProperty name="form1" property="*"  />

<%--
=============================================== 
Title: 修改网点所对应的银监会机构编码
Description: 修改网点所对应的银监会机构编码。
 * @version  $Revision: 1.3 $  $Date: 2007/05/28 09:32:21 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String jgdmfrom=request.getParameter("jgdm");
	FcUpXML upxml=new FcUpXML();
	UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	if(um==null){
		response.sendRedirect("error.jsp");
	}
	String	brhid = SCUser.getBrhId(um.getUserName()); //登录用户所在网点
	String brhidlist=upxml.getBrhidList(brhid);
	String jgdmlist=upxml.getJgdmListByBrhid(brhid);
	String arrbrhid[]=brhidlist.split("</option>");
	String arrjgdm[]=jgdmlist.split("</option>");
	if(jgdmfrom!=null&&!jgdmfrom.equals(""))
	{
		if (upxml.updateJgdm(brhid,jgdmfrom))
		{
			out.write("<script language='javascript'>{");
			out.write("alert('修改成功!');}");
			out.write("</script>");
		}
	}
	String jgdm=upxml.getJgdm(brhid);
	//分页
	int start=0;
	int end=0;
	String pnStr = request.getParameter("pn");
	if(pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
	int pn=Integer.parseInt(pnStr==null?"1":pnStr);
	int ps=20;
	start = (pn - 1) * ps + 1;
	end = pn * ps;		
	int rows=0;
	rows=arrbrhid.length;	
	String isEdit=request.getParameter("isEdit");
	if(isEdit!=null)
	{
		if(upxml.editBranchMaping(form1))
		{
			out.write("<script language='javascript'>{");
			out.write("alert('修改成功!');}");
			out.write("</script>");
		}
	}
%>
<HTML>
	<HEAD>
		<base href="<%=basePath%>">
		<TITLE>信贷管理</TITLE>
		<script src='js/check.js' type='text/javascript'></script>
		<script src='js/pagebutton.js' type='text/javascript'></script>
		<script language="javascript" src="js/flippage3.js"></script>
		<script src='js/querybutton.js' type='text/javascript'></script>
		<script src='js/fc.js' type='text/javascript'></script>
		<script src='js/fcmain.js' type='text/javascript'></script>
		<link href="css/platform.css" rel="stylesheet" type="text/css">
		<link type="text/css" href="query/setup/web.css" rel="stylesheet">
		<style type="text/css">
<!--
body {
	background-image: url(images/checks_02.jpg);
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
function save()
{
	if(<%=rows%>==0)
		return;
	document.forms[0].action='fc/jgdm.jsp?isEdit=true&pn=<%=pn%>';
	document.forms[0].submit();
}
</script>
	</head>

	<BODY >
	<form name="winform" method="post">
		<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TR>
				<TD vAlign="center" align="middle">
					<TABLE borderColor=#006699 height=325 cellSpacing=2 cellPadding=2
						align=center bgColor=#aaccee border=2 >
						<TR align=left>
							<TD bgColor=#4477aa height=30 colspan=2>
								<IMG src="images/form/xing1.jpg" width="13" height="13"
									align=absMiddle>
								<FONT color=#ffffff size=2><B>机构映射维护</B>
								</FONT>
								<IMG src="images/form/xing1.jpg" width="13" height="13"
									align="absMiddle">
							</TD>
						</TR>
						<TR align="middle">
							<TD vAlign="center" colspan=2>
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
															<table width=100% border=0 align='center' cellpadding=1
																cellspacing=1 class='list_form_table'>
																<%
																for(int i=start;i<=end;i++){ if(i>rows)break;%>
																<tr align='center' class='list_form_title_tr'>
																<td align="left">
																网点
																<select name="brhid" id="brhid<%=i-1%>">
																<%=arrbrhid[i-1]+"</option>" %>
																</select>
																</td><td>
																上报机构代码
																<select name="upbrhid" id="upbrhid<%=i-1%>">
																<%=jgdmlist%>
																</select>
																<script language="javascript">
																<%
																String strtemp=arrbrhid[i-1].substring("<option value='".length(),arrbrhid[i-1].length());
																%>
															setVal(document.all.upbrhid<%=i-1%>,'<%=upxml.getJgdm(strtemp.substring(0,strtemp.indexOf("'"))).trim()%>');
														</script>
																</td>
																</tr>
																<%} %>
																<tr>
																</tr>
															</table>
											</table>
											</div>
										</TD>
									</TR>
								</TABLE>
							</TD>
							
						</TR>
						<tr>
						<td align="center" colspan=2>
						<input type="submit" class="button" name="btn1" value="提交" onclick="save();">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="reset" class="button" name="btn12" value="重置">
						</td>
						</tr>
						<tr>
						<td >
						<table width="30%">
						<script language="javascript">
						createFlipPage(<%=pn%>,<%=ps%>,<%=rows%>,"/fc/jgdm.jsp?pn=","winform");
						</script>
						</table>
						</td>		
						</tr>	
						
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

</form>
	</BODY>
</HTML>
