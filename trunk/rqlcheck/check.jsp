<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="zt.platform.cachedb.ConnectionManager"%>
<%@ page import="zt.platform.form.config.SystemAttributeNames"%>
<%@ page import="zt.platform.user.UserManager"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="zt.platform.db.*"%>
<%@ page import="java.lang.Double"%>
<%--
=============================================== 
Title: 贷款台帐总分核对
Description: 辖内台帐总分核对功能。
 * @version   $Revision: 1.2 $  $Date: 2007/05/14 02:31:11 $
 * @author   houcs
 * <p/>修改：$Author: houcs $
=============================================== 
--%>
<%
	request.setCharacterEncoding("GBK");
	UserManager um = (UserManager) session
			.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	if (um == null) {
		response.sendRedirect("../rqlcheck/error.jsp");
	}
	ConnectionManager manager = ConnectionManager.getInstance();
	String mess = (String) request.getAttribute("mess");
	if (mess == null) {
		mess = "";
	} else {
		mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
	}
	String BRHID = (String) request.getParameter("brhid");
	if (BRHID != null && BRHID.trim().length() <= 0) {
		BRHID = null;
	}
	String lname3 = "";

	String lname = (String) request.getParameter("lname2");
	if (lname == null)
		lname = lname3;
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
function checkLname(){
 form1.lname2.value=lname1.innerText;
 form1.submit();
 }

function close(){
window.close();
return false;
}
function info() {
alert(" 没有查询到结果！");
return false;
    
}
document.title = "贷款台帐总分核对";
document.focus();
function req() {
   if(form1.brhid ==null || form1.brhid.value.length ==0)
        {
        alert("请先选择网点再刷新!");
        return false;
        }
        form1.action = "/rqlcheck/check.jsp?brhid=" + document.all.BRHID.value + "" ;
          form1.submit(); 
    }
function deptrefer_click() {
        var url = "view.jsp";
        var vArray;
        document.all.referValue.value = window.open(url, 0, "height=400,width=460,toolbar=no,scrollbars=yes,status");
        if (document.all.referValue.value == "undefined") {
        return;
        }

    }
 function user_check() {
 this.close();
 return false;
    }
 function check1(){
  
 if(form1.brhid ==null || form1.brhid.value.length ==0)
  {
  alert("请先选择网点!");
  return false;
  }
 form1.submit();
}
    </script>
<script src='/js/check.js' type='text/javascript'></script>
<script src='/js/pagebutton.js' type='text/javascript'></script>
<script language="javascript" src="../js/flippage2.js"></script>
<script src='/js/querybutton.js' type='text/javascript'></script>
<script src='/js/meizzDate.js' type='text/javascript'></script>
</head>

<body background="../images/checks_02.jpg">

<form action="" name="form1" method="post" id="winform"
	onsubmit="checkLname()"><input name="referValue" type="hidden"><input
	type="hidden" name="lname2">
<table width="100%" height="100%" border="0" cellspacing="0"
	cellpadding="0" scrolling='yes'>
	<tr>
		<td align="center" valign="middle">
		<table height="200" border="2" align="center" cellpadding="2"
			cellspacing="5" bordercolor="#006699" bgcolor="AACCEE"
			scrolling='yes'>
			<tr align="left">
				<td height="30" bgcolor="#4477AA"><img
					src="../images/form/xing1.jpg" align="absmiddle"> <font
					size="2" color="#FFFFFF"><b>贷款台帐总分核对</b></font> <img
					src="../images/form/xing1.jpg" align="absmiddle"><font
					size="2" color="red"><%=mess%> </font></td>

			</tr>
			<tr align="center">
				<td height="150" valign="middle">

				<table width="100%" height="100%" cellspacing="0" cellpadding="0"
					border="0" scrolling='yes'>
					<tr>

						<td align="center" valign="middle">
						<table width='500' scrolling='yes'>
							<tr>
								<td>
								<table align='center' cellpadding='0' cellspacing='0' border='0'
									bgcolor='#AAAAAA' width='500'>
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
															name="brhid" value="<%=BRHID==null ? "":BRHID %>"
															class="query_form_text" e="25" readonly> <input
															type="button" name="nameref" value="…"
															onclick="deptrefer_click()" class="page_form_refbutton"></td>

													</tr>

												</table>
												</td>
												<td class="query_td" width="20%" align="center">
												<table border='0' width='80%' bgcolor='#F1F1F1'>
													<tr>
														<td nowrap valign="top"><input type="submit"
															class="query_button" name="Submit" value=" 检 索 "
															onclick="return check1();"></td>

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
								<table cellpadding='0' cellspacing='0' border='0'
									scrolling='yes'>
									<tr>
										<td width="162" align="left" nowrap><span class="style1">网点名称:<label
											id="lname1"><%=lname == null ? "" : lname%></label></span>
									</tr>
								</table>
								<table class='list_form_table' width='500' align='center'
									id="checkTable" cellpadding='0' cellspacing='1' border='0'
									scrolling='yes'>
									<tr class='list_form_title_tr'>

										<td width='10%' class='list_form_title_td' nowrap
											align="center">科目号</td>
										<td width='8%' class='list_form_title_td' nowrap
											align="center">科目名称</td>
										<td width='10%' class='list_form_title_td' nowrap
											align="center">科目余额</td>
										<td width='10%' class='list_form_title_td' nowrap
											align="center">台帐余额</td>
										<td width='10%' class='list_form_title_td' nowrap
											align="center">差额</td>
									</tr>
									<%
											if (BRHID != null)
											BRHID = BRHID.trim();
										String sql = "select a.ACCNO,a.ACCNAME,b.DAYDBBAL,(select sum(NOWBAL) as THENOWBAL"
												+ " from RQLOANLIST d "
												+ "where d.BRHID=b.BRHID"
												+ " and b.ACCNO=d.ACCNO)"
												+ " from SCHOSTACC a,BTACCBAL b "
												+ " where a.ACCNO=b.ACCNO "
												+ " and b.BRHID='"
												+ BRHID
												+ "' " + " and a.ACCTP='3' " + " and a.BALTYPE='1'";

										String pnStr = request.getParameter("pn");
										if (pnStr == null || pnStr.trim().length() <= 0)
											pnStr = null;

										if (BRHID != null && BRHID.trim().length() > 0) {
											CachedRowSet crs = manager.getRs(sql);

											String THENOWBAL, THELEFT;
											double cha = 0.00;
											while (crs.next()) {
												THENOWBAL = crs.getString("THENOWBAL") == null ? "0.00"
												: crs.getString("THENOWBAL");

												THELEFT = crs.getString("DAYDBBAL") == null ? "0.00" : crs
												.getString("DAYDBBAL");

												cha = (Double.valueOf(THELEFT).doubleValue() - Double
												.valueOf(THENOWBAL).doubleValue());
									%>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td' align="center"><%=crs.getString("ACCNO")%>
										</td>
										<td nowrap class='list_form_td' align="left"><%=DBUtil.fromDB(crs.getString("ACCNAME"))%></td>
										<td nowrap class='list_form_td' align="right"><%=crs.getString("DAYDBBAL")%>
										</td>
										<td nowrap class='list_form_td' align="right"><%=THENOWBAL%>
										</td>
										<td nowrap class='list_form_td' align="right"><%=DBUtil.doubleToStr(cha)%></td>

									</tr>
									<%
									}
									%>


									<%
									if (crs.size() <= 0) {
									%>
									<script language="javascript">
											alert("未查询到结果！");
											window.location="/rqlcheck/check.jsp";
											</script>
									<%
										}
										} else {
									%>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
									</tr>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
									</tr>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
									</tr>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
									</tr>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
									</tr>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
									</tr>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
									</tr>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
									</tr>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
									</tr>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
										<td nowrap class='list_form_td'></td>
									</tr>
									<%
									}
									%>
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
									name='a' class='list_button_active' value=' 刷 新 '
									onclick="return req();"></td>
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





