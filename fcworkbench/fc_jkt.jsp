<%@ page import="zt.cms.pub.SCBranch"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="zt.cms.pub.SCUser"%>
<%@ page import="zt.cmsi.pub.define.UserRoleMan"%>
<%@ page import="zt.platform.cachedb.ConnectionManager"%>
<%@ page import="zt.platform.db.DBUtil"%>
<%@ page import="zt.platform.form.config.SystemAttributeNames"%>
<%@ page import="zt.platform.user.UserManager"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%--
===============================================
Title: 辖内清分情况监控台
Description: 监控辖内贷款时点清分情况。
 * @version   $Revision: 1.14 $  $Date: 2007/05/14 01:17:52 $
 * @author   houcs
 * <p/>修改：$Author: liuj $
===============================================
--%>
<%
	request.setCharacterEncoding("GBK");
	UserManager um = (UserManager) session
			.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	if (um == null) {
		response.sendRedirect("../fcworkbench/error.jsp");
	}
	ConnectionManager manager = ConnectionManager.getInstance();

	String brhid2 = SCUser.getBrhId(um.getUserName().trim());
	if (request.getParameter("brhid") != null)
		brhid2 = request.getParameter("brhid");

	String lname3 = SCBranch.getSName(brhid2);
	String sql4 = "select max(dt) dt,enddt from FCPRD where INITIALIZED=1 group by enddt";
	CachedRowSet crs4 = manager.getRs(sql4);
	String maxdt = "";
	String enddt = "";
	while (crs4.next()) {
		maxdt = crs4.getString("dt");
		enddt = crs4.getString("enddt");
	}

	String mess = (String) request.getAttribute("mess");
	mess = (mess == null) ? "" : "<li class='error_message_li'>"
			+ mess.trim() + "</li>";

	String lname = request.getParameter("lname2");
	if (lname == null)
		lname = lname3;

	String status = "";
	if (UserRoleMan.getInstance().ifHasRole(
			Integer.parseInt(um.getUserId()), 6) == true)
		status += "1,";
	if (UserRoleMan.getInstance().ifHasRole(
			Integer.parseInt(um.getUserId()), 9) == true)
		status += "2,3,";
	if (status.trim().length() > 0)
		status = status.substring(0, status.length() - 1);
%>
<html>
<head>
<title>信贷管理</title>
<link href="../css/platform.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type="text/css">
        .style1 {
            font-family: "宋体", Courier New, Arial;
            font-size: 12;
        }
    </style>
<script language="JavaScript" type="text/JavaScript">
        document.title = "联社辖内清分情况监控台";
        document.focus();

        function checkLname() {
            form1.lname2.value = lname1.innerText;
            form1.submit();
        }

        function deptrefer_click() {
            var url = "fc_view.jsp";
            var vArray;
            document.all.referValue.value = window.open(url, 0, "height=400,width=460,toolbar=no,scrollbars=yes,status");
            if (document.all.referValue.value == "undefined") {
                return;
            }
        }
    </script>
<script src='/js/querybutton.js' type='text/javascript'></script>
</head>

<body background="../images/checks_02.jpg">

<form action="" name="form1" method="post" id="winform"
	onsubmit="checkLname()"><input name="referValue" type="hidden">
<input type="hidden" name="lname2">
<table width="100%" height="100%" border="0" cellspacing="0"
	align="center" cellpadding="0" scrolling='yes'>
	<tr>
		<td align="center" valign="middle">
		<table height="200" border="2" align="center" cellpadding="2"
			cellspacing="5" bordercolor="#006699" bgcolor="AACCEE"
			scrolling='yes'>
			<tr align="left">
				<td height="30" bgcolor="#4477AA"><img
					src="../images/form/xing1.jpg" align="absmiddle"> <font
					size="2" color="#FFFFFF"><b>联社辖内清分情况监控台</b></font> <img
					src="../images/form/xing1.jpg" align="absmiddle"> <font
					size="2" color="red"><%=mess%> </font></td>
			</tr>
			<tr align="center">
				<td height="100" valign="middle">
				<table width="100%" height="100%" cellspacing="0" cellpadding="0"
					border="0" scrolling='yes'>
					<tr>
						<td align="center" valign="middle">
						<table width='700' scrolling='yes'>
							<tr>
								<td>
								<table align='center' cellpadding='0' cellspacing='0' border='0'
									bgcolor='#AAAAAA' width='700'>
									<tr>
										<td height="0">
										<table id="findDiv" class='query_form_table' cellpadding='0'
											cellspacing='0' border='0' style='display:none'>
											<tr class="query_tr">
												<td class="query_td" width="80%">
												<table class='query_form_table' id='query_form_table'
													cellpadding='1' cellspacing='1' border='0'>
													<tr class="query_form_tr" nowrap>
														<td class="query_form_td" nowrap>网点选择</td>
														<td class="query_form_td" nowrap><input type="text"
															name="brhid" value="<%=brhid2==null ? "":brhid2 %>"
															class="page_form_text" errInfo="网点选择"> <input
															type="button" name="nameref" value="…"
															onclick="deptrefer_click()" class="page_form_refbutton">
														</td>
													</tr>
												</table>
												</td>
												<td class="query_td" width="20%" align="center">
												<table border='0' width='80%' bgcolor='#F1F1F1'>
													<tr>
														<td nowrap valign="top"><input type="submit"
															class="query_button" name="Submit" value=" 检 索 ">
														</td>
													</tr>
												</table>
												</td>
											</tr>
										</table>
										</td>
									</tr>
									<tr>
										<td height="0" align="center"><img id='findDivHandle'
											title='点击查询' onClick='menuMove()'
											src='/images/form/button1.jpg' style='cursor:hand;'></td>
									</tr>
								</table>
								<table cellpadding='0' cellspacing='0' border='0' align="center"
									width='700'>
									<tr height="20">
										<td align="left" nowrap><span class="style1">网点名称:<label
											id="lname1"><%=lname == null ? "" : lname%> </label></span></td>
										<td align="center" nowrap><span class="style1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;清分日期:<%=maxdt%></span>
										</td>
										<td align="right"><span class="style1">清分截止日期:<%=enddt%></span></td>
									</tr>
								</table>
								<table class='list_form_table' width='100%' align='center'
									id="checkTable" cellpadding='0' cellspacing='1' border='0'>
									<tr class='list_form_title_tr'>
										<td class='list_form_title_td' align="center" width="20%">网点名称</td>
										<td class='list_form_title_td' align="center" width="10%">新增类总数</td>
										<td class='list_form_title_td' align="center" width="15%">新增类未清分数</td>
										<td class='list_form_title_td' align="center" width="10%">异常类总数</td>
										<td class='list_form_title_td' align="center" width="15%">异常类未清分数</td>
										<td class='list_form_title_td' align="center" width="10%">其它类总数</td>
										<td class='list_form_title_td' align="center" width="15%">其它类未清分数</td>
									</tr>
									<%
									    
										String SUBBRHIDs4 = SCBranch.getSubBrh1(brhid2).trim();
										if(SUBBRHIDs4 !=null || !SUBBRHIDs4.endsWith(""))
										{
										int i=SUBBRHIDs4.indexOf("999999");
										int j=SUBBRHIDs4.length();
										if(i!=-1){
										SUBBRHIDs4=SUBBRHIDs4.substring(0,i-3).trim()+SUBBRHIDs4.substring(i+1,j).trim();
										}
										}
										SUBBRHIDs4=SUBBRHIDs4.replaceAll(",","','");
										SUBBRHIDs4 = (SUBBRHIDs4.equals("")) ? brhid2 : SUBBRHIDs4;
										String sql = "select sname,brhid from SCBRANCH where brhid in ('"
												+ SUBBRHIDs4 + "')";
										int sst1 = 0;
										int sst2 = 0;
										int sst3 = 0;
										int sst4 = 0;
										int sst5 = 0;
										int sst6 = 0;
										CachedRowSet crs1 = manager.getRs(sql);
										int i1 = crs1.size();
										int j1 = 10;
										while (crs1.next()) {
											String sname = DBUtil.fromDB(crs1.getString("sname"));
											String brhid = crs1.getString("brhid");
											String SUBBRHIDs1 = SCBranch.getSubBranchAll(brhid);
											SUBBRHIDs1 = SUBBRHIDs1.replaceAll(",", "','");
											String sql2 = "with"
											+ " T as (select s.brhid,fcstatus,FCOPRTYPE,sname,createdate,enddt from FCMAIN f,SCBRANCH s,FCPRD where f.brhid in "
											+ "('"
											+ SUBBRHIDs1
											+ "') and FCCrtType=1 and f.brhid=s.brhid and createdate=dt and createdate=(select max(dt) from FCPRD where INITIALIZED=1)), "
											+ " F as (select brhid,createdate,enddt,"
											+ " case when fcoprtype=1 then count(fcoprtype) else 0 end fcoprtype1,"
											+ " case when fcoprtype=1 and fcstatus <8 then count(fcoprtype) else 0 end fcoprtype2, "
											+ " case when fcoprtype=2 then count(fcoprtype) else 0 end fcoprtype3, "
											+ " case when fcoprtype=2 and fcstatus <8 then count(fcoprtype) else 0 end fcoprtype4, "
											+ " case when fcoprtype=9 then count(fcoprtype) else 0 end fcoprtype5, "
											+ " case when fcoprtype=9 and fcstatus <8 then count(fcoprtype) else 0 end fcoprtype6, "
											+ " sname from T group by brhid,sname,fcoprtype,fcstatus,createdate,enddt)"
											+ " select brhid,sum(fcoprtype1) s1,sum(fcoprtype2) s2,sum(fcoprtype3) s3, "
											+ " sum(fcoprtype4) s4,sum(fcoprtype5) s5,sum(fcoprtype6) s6,max(sname) sname,max(createdate) createdate,max(enddt) enddt from F group by brhid";

											CachedRowSet crs2 = manager.getRs(sql2);

											int st1 = 0;
											int st2 = 0;
											int st3 = 0;
											int st4 = 0;
											int st5 = 0;
											int st6 = 0;

											while (crs2.next()) {
												int t1 = Integer
												.parseInt(crs2.getString("s1") == null ? "0" : crs2
												.getString("s1"));
												st1 += t1;

												int t2 = Integer
												.parseInt(crs2.getString("s2") == null ? "0" : crs2
												.getString("s2"));
												st2 += t2;

												int t3 = Integer
												.parseInt(crs2.getString("s3") == null ? "0" : crs2
												.getString("s3"));
												st3 += t3;

												int t4 = Integer
												.parseInt(crs2.getString("s4") == null ? "0" : crs2
												.getString("s4"));
												st4 += t4;

												int t5 = Integer
												.parseInt(crs2.getString("s5") == null ? "0" : crs2
												.getString("s5"));
												st5 += t5;

												int t6 = Integer
												.parseInt(crs2.getString("s6") == null ? "0" : crs2
												.getString("s6"));
												st6 += t6;
											}
											sst1 += st1;
											sst2 += st2;
											sst3 += st3;
											sst4 += st4;
											sst5 += st5;
											sst6 += st6;
									%>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td' align="left"><%=sname.trim()%></td>
										<td nowrap class='list_form_td' align="right"><%=st1%></td>
										<td nowrap class='list_form_td' align="right"><%=st2%></td>
										<td nowrap class='list_form_td' align="right"><%=st3%></td>
										<td nowrap class='list_form_td' align="right"><%=st4%></td>
										<td nowrap class='list_form_td' align="right"><%=st5%></td>
										<td nowrap class='list_form_td' align="right"><%=st6%></td>
									</tr>
									<%
										}
										if (i1 < 10) {
											for (int k = 1; k <= j1 - i1; k++) {
									%>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td' align="center"></td>
										<td nowrap class='list_form_td' align="right"></td>
										<td nowrap class='list_form_td' align="right"></td>
										<td nowrap class='list_form_td' align="right"></td>
										<td nowrap class='list_form_td' align="right"></td>
										<td nowrap class='list_form_td' align="right"></td>
										<td nowrap class='list_form_td' align="right"></td>
									</tr>
									<%
										}
										}
									%>
									<tr class='list_form_tr'>
										<td class='list_form_title_td' nowrap align="left">合计:</td>
										<td nowrap class='list_form_td' align="right"><%=sst1%></td>
										<td nowrap class='list_form_td' align="right"><%=sst2%></td>
										<td nowrap class='list_form_td' align="right"><%=sst3%></td>
										<td nowrap class='list_form_td' align="right"><%=sst4%></td>
										<td nowrap class='list_form_td' align="right"><%=sst5%></td>
										<td nowrap class='list_form_td' align="right"><%=sst6%></td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr height="35" align="center" valign="middle">
				<td align="center">
				<table border="0" cellspacing="0" cellpadding="0" width="538">
					<tr>
						<td nowrap align="center">
						<table class='list_button_tbl'>
							<tr class="list_button_tbl_tr">
								<td class="list_form_button_td"><input type='submit'
									name='a' class='list_button_active' value=' 刷 新 '></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>





