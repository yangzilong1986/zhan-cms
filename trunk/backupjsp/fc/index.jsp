<%@ page contentType="text/html; charset=GBK"  errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.cmsi.fc.DBUtil,zt.cms.pub.SCBranch,zt.cmsi.fc.FcUpXML" %>
<%--
=============================================== 
Title: �弶�������ݱ��͵���ҳ
Description: �弶�������ݱ��͵���ҳ��
 * @version  $Revision: 1.6 $  $Date: 2007/06/22 01:36:35 $
 * @author   weiyb
 * <p/>�޸ģ�$Author: weiyb $
=============================================== 
--%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	if(um==null){
		response.sendRedirect("error.jsp");
	}
	String	brhid = SCUser.getBrhId(um.getUserName()); //��¼�û���������
	String sql="select m.upbranchid,u.branchname  from UPBRHID_MAPING m  left join upbranch u on m.upbranchid=u.branchid where m.branchid='"+brhid+"'";
	CachedRowSet rs=DB2_81.getRs(sql);
	String jgdm="";
	String jgmc="";
	while (rs.next())
	{
		jgdm=rs.getString("upbranchid");
		jgmc=DBUtil.fromDB2(rs.getString("branchname"));
	}				
%>
<HTML>
	<HEAD>
		<base href="<%=basePath%>">
		<TITLE>�Ŵ�����</TITLE>

		<link type="text/css" href="query/web.css" rel="stylesheet">
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
		<script type="text/javascript">

/***********************************************
* Switch Menu script- by Martial B of http://getElementById.com/
* Modified by Dynamic Drive for format & NS4/IE4 compatibility
* Visit http://www.dynamicdrive.com/ for full source code
***********************************************/

if (document.getElementById){ //DynamicDrive.com change
document.write('<style type="text/css">\n')
document.write('.submenu{display: none;}\n')
document.write('</style>\n')
}

function SwitchMenu(obj){
	if(document.getElementById){
	var el = document.getElementById(obj);
	var ar = document.getElementById("masterdiv").getElementsByTagName("table"); //DynamicDrive.com change
		if(el.style.display != "block"){ //DynamicDrive.com change
			for (var i=0; i<ar.length; i++){
				if (ar[i].className=="submenu") //DynamicDrive.com change
				ar[i].style.display = "none";
			}
			el.style.display = "block";
		}else{
			el.style.display = "none";
		}
	}
}

function openwin(str){
var st="width=800,height=600,status=yes,toolbar=yes,menubar=no,location=no,scrollbars=yes,resizable=yes";
	top.window.open(str,"_blank",st).focus();
}

</script>
	</head>

	<BODY >
		<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TR>
				<TD vAlign="center" align="middle">
					<TABLE borderColor=#006699 height=325 cellSpacing=2 cellPadding=2
						align=center bgColor=#aaccee border=2>
						<TR align=left>
							<TD bgColor=#4477aa height=30>
								<IMG src="images/form/xing1.jpg" width="13" height="13"
									align=absMiddle>
								<FONT color=#ffffff size=2><B>�弶�������ݱ���</B>
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
										<TD vAlign="top" align="middle">
											<SCRIPT src="�������_files/querybutton.js" type=text/javascript></SCRIPT>
											<SCRIPT src="�������_files/meizzDate.js" type=text/javascript></SCRIPT>

											<TABLE>
												<TR>
													<TD>
														<TABLE cellSpacing=0 cellPadding=0 width=538 border=0>
															<%if(jgdm.equals("")){ %>
															<TR>
																<TD align="middle" height=0 class='table_list'>
																����û�趨������ţ��������Ա��ϵ����
																</TD>
															</TR>
															<%}else{ %>
															<TR >
																<TD align="left" height=0 class='table_list'>
																	������ţ���<%=jgdm %>��
															</TD></TR>
															<TR>
																<TD align="left" height=0 class='table_list'>	
																	�������ƣ���<%=jgmc %>��
															</TD></TR>
															<%} %>
														</TABLE>
														<div id="masterdiv">
															<br>
															<table width=480 border=0 align='center' cellpadding=1
																cellspacing=1 class='list_form_table'>
																<tr align='center' class='list_form_title_tr'>
																	<td align='center' class='table_list1'>
																		<span class='style6'> <input name='button2'
																				type=button class='button' value='�ϱ���������'
																				height='14' onclick="SwitchMenu('tb0')"> </span>
																	</td>
																</tr>
																<tr class=list_form_tr>
																	<td align='center' class='table_list' height='0'>
																		<table class='submenu' width='70%' border='0'
																			cellpadding='2' cellspacing='0' id='tb0'>
																			<tr>
																				<td class='table_list'>
																					<a href="javascript:openwin('/fc/importData.jsp')">��������</a>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																<tr align='center' class='list_form_title_tr'>
																	<td align='center' class='table_list1'>
																		<span class='style6'> <input name='button2'
																				type=button class='button' value='�ϱ�����ά��'
																				height='14' onclick="SwitchMenu('tb3')"> </span>
																	</td>
																</tr>
																<tr class=list_form_tr>
																	<td align='center' class='table_list' height='0'>
																		<table class='submenu' width='70%' border='0'
																			cellpadding='2' cellspacing='0' id='tb3'>
																			<tr>
																				<td class='table_list'>
																					<a href="javascript:openwin('/fc/upqsykh.jsp')">����ҵ����ͻ���Ϣά��</a>
																				</td>
																			</tr>
																			<tr>
																				<td class='table_list'>
																					<a href="javascript:openwin('/fc/upqsydk.jsp')">����ҵ���������Ϣά��</a>
																				</td>
																			</tr>
																			<tr>
																				<td class='table_list'>
																					<a href="javascript:openwin('/fc/upzrrqt.jsp')">��Ȼ��������΢����ҵ������Ϣά��</a>
																				</td>
																			</tr>
																			<tr>
																				<td class='table_list'>
																					<a href="javascript:openwin('/fc/upzrrnh.jsp')">��Ȼ��һ��������������������Ϣά��</a>
																				</td>
																			</tr>
																	
																		</table>
																	</td>
																</tr>
																<tr align='center' class='list_form_title_tr'>
																	<td align='center' class='table_list1'>
																		<span class='style6'> <input name='button2'
																				type=button class='button' value='�ϱ����ݵ���'
																				height='14' onclick="SwitchMenu('tb6')"> </span>
																	</td>
																</tr>
																<tr class=list_form_tr>
																	<td align='center' class='table_list' height='0'>
																		<table class='submenu' width='70%' border='0'
																			cellpadding='2' cellspacing='0' id='tb6'>
																			<tr>
																				<td class='table_list'>
																					<a href="javascript:openwin('/fc/exportXML.jsp')">������XML�ļ�</a>
																				</td>
																			</tr>
															
																		</table>
																	</td>
																</tr>
															</table>
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

	</BODY>
</HTML>
