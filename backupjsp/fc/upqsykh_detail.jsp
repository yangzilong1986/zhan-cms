<%@ page contentType="text/html; charset=GBK"  errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.cmsi.fc.DBUtil, zt.cmsi.fc.FcUpXML,java.text.DecimalFormat" %>
<%--
=============================================== 
Title: 修改企事业贷款客户信息明细
Description: 修改企事业贷款分类客户明细。
 * @version  $Revision: 1.4 $  $Date: 2007/05/31 02:29:40 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	java.text.DecimalFormat df = new java.text.DecimalFormat("###,###,###,##0");
	FcUpXML fc= new FcUpXML();
	String serialno=request.getParameter("serialno");
	String flag=request.getParameter("flag");
	//ConnectionManager manager=ConnectionManager.getInstance();
	CachedRowSet rs=null;
	if (serialno!=null)
		rs=DB2_81.getRs("select * from UPqsykh where serialno="+serialno);
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
		
		if(!isEmptySelect(document.all.jkrxzh)) return;
		if (!isEmptySelect(document.all.xinydj))return;
		if (check(document.winform))
		{
			document.winform.submit();
		}
	}
	</script>		
	</head>

	<body background="images/checks_02.jpg">
	<%if (rs!=null){ %>
	<form id='winform' name='winform' method='post' action='fc/upqsykh_edit.jsp?serialno=<%=serialno %>'>
		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td align="center" valign="middle">
					<table height="325" border="2" align="center" cellpadding="2"
						cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" width="70%">
						<tr align="left">
							<td height="30" bgcolor="#4477AA">
								<img src="images/form/xing1.jpg" align="absmiddle">
								<font size="2" color="#FFFFFF"><b>企事业贷款客户信息</b>
								</font>
								<img src="images/form/xing1.jpg" align="absmiddle">
							</td>
						</tr>
						<tr align="center">
							<td height="260" valign="middle">
								<table width="100%" height="100%" cellspacing="0"
									cellpadding="0" border="0">
									<tr>
										<td width="20">&nbsp;
											
										</td>
										<td align="center" valign="middle" >

											<%while (rs.next()){ %>
												<table class='page_form_table' id='page_form_table' width="100%">
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>机构代码*</td>
														<td class="page_form_td">
															<input type="text" name='jrjgdm' value="<%=DBUtil.fromDB2(rs.getString("jrjgdm"))%>"
																mayNull='false' errInfo='机构代码' class="page_form_text" readonly>
														</td>
														<td class="page_form_title_td" nowrap>客户号*</td>
														<td class="page_form_td">
															<input type="text" name="khh" value="<%=DBUtil.fromDB2(rs.getString("khh"))%>"
																mayNull='false' errInfo='客户号'  class="page_form_text" readonly>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>借款人名*</td>
														<td class="page_form_td">
															<input type="text" name="zhgwmc" value="<%=DBUtil.fromDB2(rs.getString("zhgwmc"))%>"
																class="page_form_text"  mayNull='false' errInfo='借款人名'  readonly>
														</td>
														<td class="page_form_title_td" nowrap>借款人性质*</td>
														<td class="page_form_td">
															<select name="jkrxzh" id="jkrxzh" title="借款人性质" class="page_form_select">
																<option value=""></option>
																<%=fc.getEnumByField("jkrxzh") %>
															</select>
															<script language="javascript">
															setVal(document.all.jkrxzh,'<%=DBUtil.fromDB2(rs.getString("jkrxzh")).trim()%>');
															</script>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>法定代表人(选填)</td>
														<td class="page_form_td">
															<input type="text" name="faddbr" value="<%=DBUtil.fromDB2(rs.getString("faddbr"))%>"
																class="page_form_text" title="法定代表人">
														</td>
														<td class="page_form_title_td" nowrap>主管部门或母公司(选填)</td>
														<td class="page_form_td">
														<input type="text" name="zhgubm" class="page_form_text" value="<%=DBUtil.fromDB2(rs.getString("zhgubm"))%>">
															
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>登记注册日期	*</td>
														<td class="page_form_td">
														<input type="text" name="djzcrq" value="<%=DBUtil.toDate(rs.getString("djzcrq"))%>" class="page_form_text"  maxlength="8" mayNull="false"  errInfo="登记注册日期" >
														<input type="button" value="…" class="page_form_refbutton" onclick="setday(this,winform.djzcrq)">
														</td>
														<td class="page_form_title_td" nowrap>企业注册资金	*</td>
														<td class="page_form_td">
															<input type="text" name="zczj" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("zczj"))%>" mayNull='false' errInfo='企业注册资金' class="page_form_text">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>经营地址(选填)</td>
														<td class="page_form_td"><input type="text" name="tngxdz" value="<%=DBUtil.fromDB2(rs.getString("tngxdz"))%>"
																class="page_form_text" maxlength="15"  title="经营地址" /></td>
														<td class="page_form_title_td" nowrap>信用等级*</td>
														<td class="page_form_td">
															<select name="xinydj" class="page_form_select" title='信用等级'>
																<option value="" ></option>
																<%=fc.getEnumByField("xinydj") %>
															</select>
															<script language="javascript">
															setVal(document.all.xinydj,'<%=DBUtil.fromDB2(rs.getString("xinydj")).trim()%>');
															</script>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>主营业务收入*</td>
														<td class="page_form_td">
														<input type="text" name="zyywsr" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("zyywsr"))%>"  mayNull="false"  errInfo="主营业务收入" class="page_form_text"> 
														</td>
														<td class="page_form_title_td" nowrap>基本账户开户行(选填)</td>
														<td class="page_form_td">
															<input type="text" name="zhhkhh" value="<%=DBUtil.fromDB2(rs.getString("zhhkhh"))%>"
																class="page_form_text"  title="基本账户开户行">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>企业经营项目(选填)</td>
														<td class="page_form_td">
															<input type="text" name="jngyxm" value="<%=DBUtil.fromDB2(rs.getString("jngyxm"))%>"
																class="page_form_text"  title="企业经营项目">
														</td>
														<td class="page_form_title_td" nowrap>资产之土地使用权实际面积(数量)*</td>
														<td class="page_form_td">
															<input type="text" name="sjmj1"  value="<%=DBUtil.numberToMoney(rs.getBigDecimal("sjmj1"))%>"
																class="page_form_text"  onfocus="this.select();">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>资产之土地使用权已办证面积</td>
														<td class="page_form_td">
															<input type="text" name="ybzmj1" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("ybzmj1"))%>"
																class="page_form_text" onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>资产之土地使用权评估价值</td>
														<td class="page_form_td">
															<input type="text" name="pgjz1" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("pgjz1"))%>"
																class="page_form_text" onfocus="this.select();">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>资产之土地使用权抵押情况</td>
														<td class="page_form_td">
														<select name="dyzk1" >
														<option value=""></option>
														<%=fc.getEnumByField("dyzk1") %>
														</select>
														<script language="javascript">
															setVal(document.all.dyzk1,'<%=DBUtil.fromDB2(rs.getString("dyzk1")).trim()%>');
														</script>
														</td>
														<td class="page_form_title_td" nowrap>资产之房产使用权实际面积(数量)</td>
														<td class="page_form_td">
														<input type="text" name="sjmj2" class="page_form_text"  value="<%=DBUtil.numberToMoney(rs.getBigDecimal("sjmj2"))%>" onfocus="this.select();">
														</td>
													    <td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>资产之房产使用权已办证面积</td>
														<td class="page_form_td">
															<input type="text" name="ybzmj2" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("ybzmj2"))%>"
																class="page_form_text" onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>资产之房产使用权评估价值</td>
														<td class="page_form_td">
														<input type="text" name="pgjz2" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("pgjz2"))%>" onfocus="this.select();">
														</td>
															<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class='page_form_title_td' nowrap>资产之房产使用权抵押情况</td>
														<td class='page_form_td'>
															<select name="dyzk2">
															<option value=""></option>
															<%=fc.getEnumByField("dyzk1")%>
															</select>
															<script language="javascript">
															setVal(document.all.dyzk2,'<%=DBUtil.fromDB2(rs.getString("dyzk2")).trim()%>');
														</script>
													  </td>
														<td class='page_form_title_td'>资产之主要设备及交通工具实际数量 </td>
														<td class='page_form_td'>
															<input type="text" name="sjmj3" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("sjmj3")).substring(0,DBUtil.numberToMoney(rs.getBigDecimal("sjmj3")).length()-3)%>" onfocus="this.select();">
													  </td>
													<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>资产之主要设备及交通工具已办证数量</td>
														<td class="page_form_td">
															<input type="text" name="ybzmj3" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("ybzmj3")).substring(0,DBUtil.numberToMoney(rs.getBigDecimal("ybzmj3")).length()-3)%>" onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>资产之主要设备及交通工具评估价值</td>
														<td class="page_form_td">
														<input type="text" name="pgjz3" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("pgjz3"))%>" onfocus="this.select();">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>资产之主要设备及交通工具抵押情况</td>
														<td class="page_form_td">
															<select name="dyzk3">
															<option value=""></option>
															<%=fc.getEnumByField("dyzk1") %>
															</select>
															<script language="javascript">
															setVal(document.all.dyzk3,'<%=DBUtil.fromDB2(rs.getString("dyzk3")).trim()%>');
														</script>
														</td>
														
														<td class="page_form_title_td" nowrap>第一还款来源*</td>
														<td class="page_form_td">
															<input type="text" class="page_form_text" name="hukly1" mayNull='false' errInfo='第一还款来源' value="<%=DBUtil.fromDB2(rs.getString("hukly1"))%>" >
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>第二还款来源(选填)</td>
														<td class="page_form_td">
															<input type="text" name="hukly2"  value="<%=DBUtil.fromDB2(rs.getString("hukly2"))%>"
																class="page_form_text" >
														</td>
														<td class="page_form_title_td" nowrap>第三还款来源(选填)</td>
														<td class="page_form_td">
															<input type="text" name="hukly3"  value="<%=DBUtil.fromDB2(rs.getString("hukly3"))%>"
																class="page_form_text" >
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>担保分析(选填)</td>
														<td class="page_form_td">
													<textarea name="dbfx" class="page_form_textfield"><%=DBUtil.fromDB2(rs.getString("dbfx"))%></textarea>
														</td>
														<td class="page_form_title_td" nowrap>财务分析(选填)</td>
														<td class="page_form_td">
														<textarea name="cwfx" class="page_form_textfield"><%=DBUtil.fromDB2(rs.getString("cwfx"))%></textarea>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>非财务分析(选填)</td>
														<td class="page_form_td">
															<textarea name="fcwfx" class="page_form_textfield"><%=DBUtil.fromDB2(rs.getString("fcwfx"))%></textarea>
														</td>
														<td class="page_form_title_td" nowrap>主要财务指标之净现金流量*</td>
														<td class="page_form_td">
															<input type="text" name="cwzb1" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cwzb1"))%>"
																mayNull='false' errInfo='主要财务指标之净现金流量' class="page_form_text" onfocus="this.select();">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>主要财务指标之经营性净现金流量*</td>
														<td class="page_form_td">
															<input type="text" name="cwzb2" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cwzb2"))%>"
																mayNull='false' errInfo='主要财务指标之经营性净现金流量' class="page_form_text" onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>主要财务指标之投资活动净现金流量*</td>
														<td class="page_form_td">
															<input type="text" name="cwzb3" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cwzb3"))%>"
																mayNull='false' errInfo='主要财务指标之投资活动净现金流量'  class="page_form_text" onfocus="this.select();">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>主要财务指标之筹资活动净现金流量*</td>
														<td class="page_form_td">
															<input type="text" name="cwzb4" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cwzb4"))%>"
																mayNull='false' errInfo='主要财务指标之筹资活动净现金流量' class="page_form_text" onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>主要财务指标之流动比率*</td>
														<td class="page_form_td">
															<input type="text" name="cwzb5" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cwzb5"))%>"
																mayNull='false' errInfo='主要财务指标之流动比率' class="page_form_text" onfocus="this.select();">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>主要财务指标之资产负债率(%)*</td>
														<td class="page_form_td">
															<input type="text" name="cwzb6" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cwzb6"))%>"
																mayNull='false' errInfo='主要财务指标之资产负债率' class="page_form_text" onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>主要财务指标之销售利润率(%)*</td>
														<td class="page_form_td">
															<input type="text" name="cwzb7" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cwzb7"))%>"
																mayNull='false' errInfo='主要财务指标之销售利润率'  class="page_form_text" onfocus="this.select();">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>主要财务指标之资产利润率(%)*</td>
														<td class="page_form_td">
															<input type="text" name="cwzb8" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cwzb8"))%>"
																mayNull='false' errInfo='主要财务指标之资产利润率' class="page_form_text" onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>主要财务指标之应收款周转率(次/年)*</td>
														<td class="page_form_td">
															<input type="text" name="cwzb9" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cwzb9"))%>"
																mayNull='false' errInfo='主要财务指标之应收款周转率' class="page_form_text" onfocus="this.select();">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>主要财务指标之存货周转率(次/年)*</td>
														<td class="page_form_td">
														<input type="text" name="cwzb10" class="page_form_text" mayNull='false' errInfo='主要财务指标之存货周转率' value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cwzb10"))%>" onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>财务指标日期*</td>
														<td class="page_form_td">
														<input type="text" name="cwzbrq" mayNull='false' errInfo='财务指标日期' value="<%=DBUtil.toDate(rs.getString("cwzbrq"))%>" class="page_form_text"  maxlength="8" onfocus="this.select();">
														<input type="button" value="…" class="page_form_refbutton" onclick="setday(this,winform.cwzbrq)">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>其他事项说明(选填)</td>
														<td class="page_form_td" colspan=3>
															<textarea name="qtsxsm" rows="3"
																class="page_form_textfield"   maxlength="500"><%=DBUtil.fromDB2(rs.getString("qtsxsm"))%></textarea>

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
															id='savebtn' name='save' value=' 提 交 ' onclick='goSave();'>
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
