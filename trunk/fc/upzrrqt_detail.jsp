<%@ page contentType="text/html; charset=GBK"  errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.cmsi.fc.DBUtil, zt.cmsi.fc.FcUpXML,java.text.DecimalFormat" %>
<%--
=============================================== 
Title: �޸���Ȼ��������΢����ҵ������Ϣ��ϸ
Description: �޸���Ȼ��������΢����ҵ���������Ϣ��ϸ��
 * @version  $Revision: 1.7 $  $Date: 2007/06/20 10:28:06 $
 * @author   weiyb
 * <p/>�޸ģ�$Author: weiyb $
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
		rs=DB2_81.getRs("select * from upzrrqt where fcno='"+fcno+"'");
	if(flag!=null)
	{
		if(flag.equals("true"))
		{
		out.write("<script language='javascript'>{");
		out.write("alert('�޸ĳɹ�!');}");
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
			alert(obj.title+'����Ϊ�գ�');
			obj.focus();
			return false;
		}
		return true;
	}
	function goSave()
	{
		if(!isEmptySelect(document.all.daikzl)) return;
		if (!isEmptySelect(document.all.daikfs))return;
		if (!isEmptySelect(document.all.xinydj))return;
		if (!isEmptySelect(document.all.fleijg))return;
		if (!isEmptySelect(document.all.dk_sfaydytsy))return;
		if (!isEmptySelect(document.all.fcw_jyqksfzc))return;
		if (!isEmptySelect(document.all.fcw_cpscxqqk))return;
		if (!isEmptySelect(document.all.fcw_jkrhkyy))return;
		if (!isEmptySelect(document.all.fcw_sfjnqk))return;
		if (!isEmptySelect(document.all.db_dbhtsfyx))return;
		if (!isEmptySelect(document.all.db_dbrdcnl))return;
		if (!isEmptySelect(document.all.db_dzywdbxnl))return;
		if (check(document.winform))
		{
			document.winform.submit();
		}
	}
	</script>		
  </head>
  
	<body background="images/checks_02.jpg">
	<%if (rs!=null){ %>
	<form id='winform' name="winform" method='post' action='fc/upzrrqt_edit.jsp?fcno=<%=fcno %>'>
		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td align="center" valign="middle">
					<table height="325" border="2" align="center" cellpadding="2"
						cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" width="70%">
						<tr align="left">
							<td height="30" bgcolor="#4477AA">
								<img src="images/form/xing1.jpg" align="absmiddle">
								<font size="2" color="#FFFFFF"><b>��Ȼ��������΢����ҵ����</b>
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
										<td align="center" valign="middle">

											<%while (rs.next()){ %>
												<table class='page_form_table' id='page_form_table' width="100%">
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>�ͻ���*
														</td>
														<td class="page_form_td">
															<input type="text" name="khh" value="<%=DBUtil.fromDB2(rs.getString("khh"))%>"
																class="page_form_text" readonly>
														</td>
														<td class="page_form_title_td" nowrap>�����*
														</td>
														<td class="page_form_td">
															<input type="text" name="kehumc" value="<%=DBUtil.fromDB2(rs.getString("kehumc"))%>"
																class="page_form_text" readonly>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>������(ѡ��)</td>
														<td class="page_form_td">
															<input type="text" name="fzeren" value="<%=DBUtil.fromDB2(rs.getString("fzeren"))%>"
																class="page_form_text" >
														</td>
														<td class="page_form_title_td" nowrap>��ͬ��*</td>
														<td class="page_form_td">
														<input type="text" name="dkhthm" class="page_form_text" value="<%=DBUtil.fromDB2(rs.getString("dkhthm"))%>" mayNull='false' errInfo='��ͬ��'> 
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>�������*</td>
														<td class="page_form_td">
														<input type="text" name="fljzrq" value="<%=DBUtil.toDate(rs.getString("fljzrq"))%>" class="page_form_text"  maxlength="8"  readonly>
														<input type="button" value="��" class="page_form_refbutton" onclick="setday(this,winform.fljzrq)" disabled>
														</td>
														<td class="page_form_title_td" nowrap>��Ӫ��Ŀ*</td>
														<td class="page_form_td">
														<input type="text" name="jinyxm" class="page_form_text" value="<%=DBUtil.fromDB2(rs.getString("jinyxm"))%>"
														mayNull='false' errInfo='��Ӫ��Ŀ'
														>
															
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>����˾�������(ѡ��)</td>
														<td class="page_form_td">
															<input type="text" name="jjixzh" value="<%=DBUtil.fromDB2(rs.getString("jjixzh"))%>"
																class="page_form_text" >
														</td>
														<td class="page_form_title_td" nowrap>��ͬ���*</td>
														<td class="page_form_td">
														<input type="text" name="dkhtje" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("dkhtje"))%>" readonly>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>���ʽ*</td>
														<td class="page_form_td">
														<select name="daikfs" title="���ʽ" class="page_form_select" >
																<option value="" ></option>
																<%=fc.getEnumByField("daikfs") %>
														</select>
														<script language="javascript">
														setVal(document.all.daikfs,'<%=DBUtil.fromDB2(rs.getString("daikfs")).trim()%>');
														</script>
														</td>
														<td class="page_form_title_td" nowrap>��ͬ��Ч����*</td>
														<td class="page_form_td">
														<input type="text" name="htsxrq" value="<%=DBUtil.toDate(rs.getString("htsxrq"))%>" class="page_form_text"  maxlength="8" mayNull='false' errInfo='��ͬ��Ч����' >
														<input type="button" value="��" class="page_form_refbutton" onclick="setday(this,winform.htsxrq)" >
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>��ͬ��ֹ����	*</td>
														<td class="page_form_td">
														<input type="text" name="htzzrq" value="<%=DBUtil.toDate(rs.getString("htzzrq"))%>" class="page_form_text"  maxlength="8"  mayNull='false' errInfo='��ͬ��ֹ����' >
														<input type="button" value="��" class="page_form_refbutton" onclick="setday(this,winform.htzzrq)" >
														</td>
														<td class="page_form_title_td" nowrap>�Ƿ�Լ����;ʹ��*</td>
														<td class="page_form_td">
														<select name="dk_sfaydytsy" class="page_form_select" title='�Ƿ�Լ����;ʹ��'>
																<option value="" ></option>
																<%=fc.getEnumByField("ny") %>
														</select>
														<script language="javascript">
														setVal(document.all.dk_sfaydytsy,'<%=DBUtil.fromDB2(rs.getString("dk_sfaydytsydk")).trim()%>');
														</script>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>�������(ѡ��)</td>
														<td class="page_form_td">
															<input type="text" name="dk_dkyqqk" value="<%=DBUtil.fromDB2(rs.getString("dk_dkyqqk"))%>"
																class="page_form_text" >
														</td>
														<td class="page_form_title_td" nowrap>ǷϢ���(ѡ��)</td>
														<td class="page_form_td">
															<input type="text" name="dk_dkqxqk" value="<%=DBUtil.fromDB2(rs.getString("dk_dkqxqk"))%>"
																class="page_form_text">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>�������*</td>
														<td class="page_form_td">
															<input type="text" name="daikye" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("daikye"))%>"
																mayNull='false' errInfo='�������' class="page_form_text" readonly >
														</td>
														<td class="page_form_title_td" nowrap>�ʲ��ܶ�*</td>
														<td class="page_form_td">
															<input type="text" name="cw_zcze" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cw_zcze"))%>"
																mayNull='false' errInfo='�ʲ��ܶ�'  class="page_form_text" onfocus="this.select();">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>�̶��ʲ�*</td>
														<td class="page_form_td">
														<input type="text"  name="cw_gdzc" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cw_gdzc"))%>" mayNull='false' errInfo='�̶��ʲ�' onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>��ծ�ܶ�*</td>
														<td class="page_form_td">
														<input type="text" name="cw_fzze" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cw_fzze"))%>" mayNull='false' errInfo='��ծ�ܶ�' onfocus="this.select();">
														</td>
													    <td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>�Ŵ����Ž��*</td>
														<td class="page_form_td">
															<input type="text" name="cw_xysjk" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cw_xysjk"))%>"
																mayNull='false' errInfo='�Ŵ����Ž��' class="page_form_text" onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>��Ӫ����*</td>
														<td class="page_form_td">
														<input type="text" name="cw_jysr" class="page_form_text"  value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cw_jysr"))%>" mayNull='false' errInfo='��Ӫ����' onfocus="this.select();">
														</td>
															<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class='page_form_title_td' nowrap>Ԥ��������*</td>
														<td class='page_form_td'>
															<input type="text" name="cw_yjnjysr" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cw_yjnjysr"))%>" mayNull='false' errInfo='Ԥ��������' onfocus="this.select();">
													  </td>
														<td class='page_form_title_td'>������*</td>
														<td class='page_form_td'>
															<input type="text" name="cw_jsy" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cw_jsy"))%>" mayNull='false' errInfo='������' onfocus="this.select();">
													  </td>
													<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>Ԥ��������*</td>
														<td class="page_form_td">
															<input type="text" name="cw_yjnjsy" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("cw_yjnjsy"))%>" mayNull='false' errInfo='Ԥ��������' onfocus="this.select();">
														</td>
														<td class="page_form_title_td" nowrap>��Ӫ����*</td>
														<td class="page_form_td">
														<select name="fcw_jyqksfzc" class="page_form_select" title='��Ӫ����'>
																<option value="" ></option>
																<%=fc.getEnumByField("ny") %>
														</select>
														<script language="javascript">
														setVal(document.all.fcw_jyqksfzc,'<%=DBUtil.fromDB2(rs.getString("fcw_jyqksfzc")).trim()%>');
														</script>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>�г����*</td>
														<td class="page_form_td">
														<select name="fcw_cpscxqqk" class="page_form_select" title='�г����'>
																<option value="" ></option>
																<%=fc.getEnumByField("fcw_cpscxqqk") %>
															</select>
															<script language="javascript">
														setVal(document.all.fcw_cpscxqqk,'<%=DBUtil.fromDB2(rs.getString("fcw_cpscxqqk")).trim()%>');
														</script>
														</td>
														
														<td class="page_form_title_td" nowrap>������Ը*</td>
														<td class="page_form_td">
														<select name="fcw_jkrhkyy" class="page_form_select" title='������Ը'>
																<option value="" ></option>
																<%=fc.getEnumByField("fcw_jkrhkyy") %>
															</select>
															<script language="javascript">
														setVal(document.all.fcw_jkrhkyy,'<%=DBUtil.fromDB2(rs.getString("fcw_jkrhkyy")).trim()%>');
														</script>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>��˰���*</td>
														<td class="page_form_td">
														<select name="fcw_sfjnqk" class="page_form_select" title='��˰���'>
																<option value="" ></option>
																<%=fc.getEnumByField("fcw_sfjnqk") %>
															</select>
															<script language="javascript">
														setVal(document.all.fcw_sfjnqk,'<%=DBUtil.fromDB2(rs.getString("fcw_sfjnqk")).trim()%>');
														</script>
														</td>
														<td class="page_form_title_td" nowrap>��Ч��ͬ*</td>
														<td class="page_form_td">
														<select name="db_dbhtsfyx" class="page_form_select" title='��Ч��ͬ'>
																<option value="" ></option>
																<%=fc.getEnumByField("ny") %>
															</select>
															<script language="javascript">
														setVal(document.all.db_dbhtsfyx,'<%=DBUtil.fromDB2(rs.getString("db_dbhtsfyx")).trim()%>');
														</script>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>��������*</td>
														<td class="page_form_td">
														<select name="db_dbrdcnl" class="page_form_select" title='��������'>
																<option value="" ></option>
																<%=fc.getEnumByField("db_dbrdcnl") %>
															</select>
															<script language="javascript">
														setVal(document.all.db_dbrdcnl,'<%=DBUtil.fromDB2(rs.getString("db_dbrdcnl")).trim()%>');
														</script>
														</td>
														<td class="page_form_title_td" nowrap>�֣��ʣ�Ѻ�Ｐ���ֵ</td>
														<td class="page_form_td">
														<textarea input="text" name="db_dzywjqjz" class="page_form_text"><%=DBUtil.fromDB2(rs.getString("db_dzywjqjz")).trim()%></textarea>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>��������*</td>
														<td class="page_form_td">
														<select name="db_dzywdbxnl" class="page_form_select" title='��������'>
																<option value=""></option>
																<%=fc.getEnumByField("db_dzywdbxnl") %>
															</select>
															<script language="javascript">
														setVal(document.all.db_dzywdbxnl,'<%=DBUtil.fromDB2(rs.getString("db_dzywdbxnl")).trim()%>');
														</script>
														</td>
														<td class="page_form_title_td" nowrap>������*</td>
														<td class="page_form_td">
														<select name="fleijg" title="������" class="page_form_select" onchange="setVal(document.all.fleijg,'<%=DBUtil.fromDB2(rs.getString("fleijg")).trim()%>');">
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
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>��������*</td>
														<td class="page_form_td">
															<input type="text" name="flly" value="<%=DBUtil.fromDB2(rs.getString("flly"))%>"
																mayNull='false' errInfo='��������' class="page_form_text" >
														</td>
														<td class="page_form_title_td" nowrap>��������*</td>
														<td class="page_form_td">
														<input type="text" name="dcriqi" value="<%=DBUtil.toDate(rs.getString("dcriqi"))%>" class="page_form_text"  maxlength="8" mayNull='false' errInfo='��������' >
														<input type="button" value="��" class="page_form_refbutton" onclick="setday(this,winform.dcriqi)">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>����˵��(ѡ��)</td>
														<td class="page_form_td">
															<input type="text" name="qtsm" value="<%=DBUtil.fromDB2(rs.getString("qtsm"))%>"
																class="page_form_text" >
														</td>
														<td class="page_form_title_td" nowrap>���õȼ�*</td>
														<td class="page_form_td">
														<select name="xinydj" title="���õȼ�" class="page_form_select" >
																<option value="" ></option>
																<%=fc.getEnumByField("xinydj1") %>
															</select>
															<script language="javascript">
														setVal(document.all.xinydj,'<%=DBUtil.fromDB2(rs.getString("xinydj")).trim()%>');
														</script>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>��������*</td>
														<td class="page_form_td">
														<select name="daikzl" class="page_form_select" title="��������">
																<option value="" ></option>
																<%=fc.getEnumByField("daikzl3") %>
															</select>
														<script language="javascript">
														setVal(document.all.daikzl,'<%=DBUtil.fromDB2(rs.getString("daikzl")).trim()%>');
														</script>
														</td>
														<td class="page_form_title_td" nowrap>������������*</td>
														<td class="page_form_td">
															<input type="text" name="yuqits" value="<%=DBUtil.fromDB2(rs.getString("yuqits"))%>"
																class="page_form_text" readonly mayNull='false' errInfo='������������'>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>��Ϣ��������*</td>
														<td class="page_form_td">
															<input type="text" name="lxyqts" value="<%=DBUtil.fromDB2(rs.getString("lxyqts"))%>"
																class="page_form_text" mayNull='false' errInfo='��Ϣ��������'>
														</td>
														<td class="page_form_title_td" nowrap></td>
														<td class="page_form_td">
													
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
															id='savebtn' name='save' value=' �� �� ' onclick="goSave();">
													</td>
													<td class='page_button_tbl_td'>
														<input type='button' class='page_button_active'
															name='button' value=' �� �� ' onClick="pageWinClose();">
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
		<%} %>
		</form>
	</body>
</html>
