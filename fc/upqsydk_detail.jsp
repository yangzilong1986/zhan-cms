<%@ page contentType="text/html; charset=GBK"  errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.cmsi.fc.DBUtil, zt.cmsi.fc.FcUpXML,java.text.DecimalFormat" %>
<%--
=============================================== 
Title: 修改企事业贷款分类信息明细
Description: 修改企事业贷款分类信息明细。
 * @version  $Revision: 1.3 $  $Date: 2007/05/29 12:52:13 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	FcUpXML fc= new FcUpXML();
	String fcno=request.getParameter("fcno");
	String flag=request.getParameter("flag");
	//ConnectionManager manager=ConnectionManager.getInstance();
	CachedRowSet rs=null;
	if (fcno!=null)
		rs=DB2_81.getRs("select * from UPqsydk where fcno='"+fcno+"'");
	if(flag!=null)
	{
		if(flag.equals("true"))
		{
		out.write("<script language='javascript'>{");
		out.write("alert('修改成功!');}");
		out.write("</script>");
		}
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
		<link href="css/platform.css" rel="stylesheet" type="text/css">
		<script src='js/fc.js' type='text/javascript'></script>
		<script src='js/check.js' type='text/javascript'></script>
		<script src='js/meizzDate.js' type='text/javascript'></script>
		<script src='js/checkID2.js' type='text/javascript'></script>
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
		<style type="text/css">
		<!--
		body {
			margin-top: 0px;
		}
		-->
		</style>
	<script language="javascript">
	function isEmptySelect(obj)
	{
		if (obj.value=='')
		{
			alert(obj.title+'不能为空！');
			obj.focus();
			return false;
		}
		return true;
	}
	function goSave()
	{
		
		if(!isEmptySelect(document.all.daikzl)) return;
		if (!isEmptySelect(document.all.daikfs))return;
		if (!isEmptySelect(document.all.fleijg))return;
		if (!isEmptySelect(document.all.sfdydj))return;
		if (check(document.winform))
		{
			document.winform.submit();
		}
	}
	</script>
  </head>
  
	<body background="images/checks_02.jpg">
	<%if (rs!=null){ %>
	<form id='winform' name="winform" method='post' action='fc/upqsydk_edit.jsp?fcno=<%=fcno%>'>
		<table width="100%" height="100%" border="0" cellspacing="0"
			cellpadding="0">
			<tr>
				<td align="center" valign="middle">
					<table height="325" border="2" align="center" cellpadding="2"
						cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" width="70%">
						<tr align="left">
							<td height="30" bgcolor="#4477AA">
								<img src="images/form/xing1.jpg" align="absmiddle">
								<font size="2" color="#FFFFFF"><b>企事业贷款分类信息</b>
								</font>
								<img src="images/form/xing1.jpg" align="absmiddle">
							</td>
						</tr>
						<tr align="center">
							<td height="260" valign="middle" >
							
								<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
									<tr>
										<td width="20">&nbsp;
											
										</td>
										<td align="center" valign="middle" >
											
											<%while (rs.next()){ %>
												<table class='page_form_table' id='page_form_table' width="100%">
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>机构代码*</td>
														<td class="page_form_td">
															<input type="text" name="jrjgdm" value="<%=DBUtil.fromDB2(rs.getString("jrjgdm"))%>"
																 class="page_form_text" readonly>
														</td>
														<td class="page_form_title_td" nowrap>客户号*</td>
														<td class="page_form_td">
															<input type="text" name="khh" value="<%=DBUtil.fromDB2(rs.getString("khh"))%>"
																 class="page_form_text" readonly>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>贷款合同代码*</td>
														<td class="page_form_td">
															<input type="text" name="dkhthm" value="<%=DBUtil.fromDB2(rs.getString("dkhthm"))%>"
																mayNull='false' errInfo='贷款合同代码' class="page_form_text" >
														</td>
														<td class="page_form_title_td" nowrap>贷款合同金额	*</td>
														<td class="page_form_td">
															<input type="text" name="dkhtje" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("dkhtje"))%>"
																class="page_form_text" readonly>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>贷款余额*</td>
														<td class="page_form_td">
															<input type="text" name="daikye" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("daikye"))%>"
																class="page_form_text" readonly>
														</td>
														<td class="page_form_title_td" nowrap>贷款种类*</td>
														<td class="page_form_td">
															<select name="daikzl" class="page_form_select" title='贷款种类'>
																<option value=""></option>
																<%=fc.getEnumByField("daikzl") %>
															</select>
															<script language="javascript">
															setVal(document.all.daikzl,'<%=DBUtil.fromDB2(rs.getString("daikzl")).trim()%>');
															</script>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>担保方式*</td>
														<td class="page_form_td">
															<select name="daikfs" class="page_form_select" title="担保方式">
																<option value="" ></option>
																<%=fc.getEnumByField("daikfs") %>
															</select>
															<script language="javascript">
															setVal(document.all.daikfs,'<%=DBUtil.fromDB2(rs.getString("daikfs")).trim()%>');
															</script>
														</td>
														<td class="page_form_title_td" nowrap>合同生效日期*</td>
														<td class="page_form_td">
														<input type="text" name="htsxrq" value="<%=DBUtil.toDate(rs.getString("htsxrq"))%>" class="page_form_text" mayNull='false' errInfo='合同生效日期'>
														<input type="button" value="…" class="page_form_refbutton" onclick="setday(this,winform.htsxrq)" >
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>合同终止日期*</td>
														<td class="page_form_td">
														<input type="text" name="htzzrq" value="<%=DBUtil.toDate(rs.getString("htzzrq"))%>" class="page_form_text"  maxlength="8"  mayNull='false' errInfo='合同终止日期'>
														<input type="button" value="…" class="page_form_refbutton" onclick="setday(this,winform.htzzrq)" >
														</td>
														<td class="page_form_title_td" nowrap>展期开始日期	</td>
														<td class="page_form_td">
														<input type="text" name="zqksrq" value="<%=DBUtil.toDate(rs.getString("zqksrq"))%>" class="page_form_text"  maxlength="8" >
														<input type="button" value="…" class="page_form_refbutton" onclick="setday(this,winform.zqksrq)">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>展期到期日期</td>
														<td class="page_form_td"><input type="text" name="zqdqrq" value="<%=DBUtil.toDate(rs.getString("zqdqrq"))%>"
																class="page_form_text" maxlength="8" >
																<input type="button" value="…" class="page_form_refbutton" onclick="setday(this,winform.zqdqrq)">
																</td>
														<td class="page_form_title_td" nowrap>合同确定用途(选填)</td>
														<td class="page_form_td">
															<input type="text" name="htqdyt" value="<%=DBUtil.fromDB2(rs.getString("htqdyt"))%>"
																class="page_form_text"  >
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>实际贷款用途(选填)	</td>
														<td class="page_form_td">
															<input type="text" name="sjdkyt" class="page_form_text" value="<%=DBUtil.fromDB2(rs.getString("sjdkyt"))%>">
														</td>
														<td class="page_form_title_td" nowrap>保证人名称(选填)</td>
														<td class="page_form_td">
															<input type="text" name="bzhrmc" value="<%=DBUtil.fromDB2(rs.getString("bzhrmc"))%>" class="page_form_text" >
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>抵质押物名称(选填)</td>
														<td class="page_form_td">
															<input type="text" name="dyawmc" value="<%=DBUtil.fromDB2(rs.getString("bzhrmc"))%>"
																class="page_form_text" >
														</td>
														<td class="page_form_title_td" nowrap>抵质押物评估价值(选填)</td>
														<td class="page_form_td">
															<input type="text" name="dypgjz" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("dypgjz"))%>"
																class="page_form_text" >
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>抵质押比率(选填)</td>
														<td class="page_form_td">
															<input type="text" name="dzyabl" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("dzyabl"))%>"
																class="page_form_text" >
														</td>
														<td class="page_form_title_td" nowrap>是否办理抵质押登记*</td>
														<td class="page_form_td">
															<select name="sfdydj" class="page_form_select" title='是否办理抵质押登记'>
																<option value=""></option>
																<%=fc.getEnumByField("sfdydj") %>
															</select>
															<script language="javascript">
															setVal(document.all.sfdydj,'<%=DBUtil.fromDB2(rs.getString("sfdydj")).trim()%>');
															</script>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>本息逾期情况(选填)</td>
														<td class="page_form_td">
														<input type="text"  name="bxyqqk" class="page_form_text" value="<%=DBUtil.fromDB2(rs.getString("bxyqqk"))%>">
														</td>
														<td class="page_form_title_td" nowrap>本金逾期天数*</td>
														<td class="page_form_td">
														<input type="text" name="yuqits" class="page_form_text" value="<%=DBUtil.fromDB2(rs.getString("yuqits"))%>" mayNull='false' errInfo='本金逾期天数'>
														</td>
													    <td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>利息逾期天数*</td>
														<td class="page_form_td">
															<input type="text" name="lxyqts" value="<%=DBUtil.fromDB2(rs.getString("lxyqts"))%>"
																class="page_form_text" mayNull='false' errInfo='利息逾期天数'>
														</td>
														<td class="page_form_title_td" nowrap>欠息金额*</td>
														<td class="page_form_td">
														<input type="text" name="qxjine" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("qxjine"))%>" mayNull='false' errInfo='欠息金额'>
														</td>
															<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class='page_form_title_td' nowrap>分类截至日期*</td>
														<td class='page_form_td'>
														<input type="text" name="fljzrq" value="<%=DBUtil.toDate(rs.getString("fljzrq"))%>" class="page_form_text"  maxlength="8" readonly >
														<input type="button" value="…" class="page_form_refbutton" onclick="setday(this,winform.fljzrq)" disabled>
													  </td>
														<td class='page_form_title_td'>分类结果*</td>
														<td class='page_form_td'>
															<select name="fleijg" title="分类结果" class="page_form_select" onchange="setVal(document.all.fleijg,'<%=DBUtil.fromDB2(rs.getString("fleijg")).trim()%>');" >
																<option value="" ></option>
																<%=fc.getEnumByField("fleijg") %>
															</select>
															<script language="javascript">
															setVal(document.all.fleijg,'<%=DBUtil.fromDB2(rs.getString("fleijg")).trim()%>');
															</script>
													  </td>
													<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
												</table>
													<%} %>
												<script src='js/pagebutton.js' type='text/javascript'></script>
										</td>
										<td width="20">&nbsp;
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
											<table class='page_button_tbl'>
												<tr class='page_button_tbl_tr'>
									
													<td class='page_button_tbl_td'>
														<input type='button' class='page_button_active'
															id='savebtn' name='save' onclick='goSave();' value=' 提 交 ' >
													</td>
													<td class='page_button_tbl_td'>
														<input type='button' class='page_button_active'
															name='button' value=' 关 闭 ' onClick="pageWinClose();">
													</td>
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
		<%} %>
	</body>
</html>
