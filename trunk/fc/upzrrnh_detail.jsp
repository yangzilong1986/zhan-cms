<%@ page contentType="text/html; charset=GBK"  errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.cmsi.fc.DBUtil, zt.cmsi.fc.FcUpXML,java.text.DecimalFormat" %>
<%--
=============================================== 
Title: �޸���Ȼ��һ��������������������Ϣ��ϸ
Description: �޸���Ȼ��һ��������������������Ϣ��ϸ��
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
		rs=DB2_81.getRs("select * from upzrrnh where fcno='"+fcno+"'");
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
		if (check(document.winform))
		{
			document.winform.submit();
		}
	}
	</script>
  </head>
  
	<body background="images/checks_02.jpg">
	<%if (rs!=null){ %>
	<form id='winform' name='winform' method='post' action='fc/upzrrnh_edit.jsp?fcno=<%=fcno %>'>
		<table width="100%" height="100%" border="0" cellspacing="0"
			cellpadding="0">
			<tr>
				<td align="center" valign="middle">
					<table height="325" border="2" align="center" cellpadding="2"
						cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" width="70%">
						<tr align="left">
							<td height="30" bgcolor="#4477AA">
								<img src="images/form/xing1.jpg" align="absmiddle">
								<font size="2" color="#FFFFFF"><b>��Ȼ��һ������������������</b>
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
														<td class="page_form_title_td" nowrap>�ͻ���*</td>
														<td class="page_form_td">
															<input type="text" name="khh" value="<%=DBUtil.fromDB2(rs.getString("khh"))%>"
																class="page_form_text" readonly>
														</td>
														<td class="page_form_title_td" nowrap>�����*</td>
														<td class="page_form_td">
															<input type="text" name="kehumc" class="page_form_text" value="<%=DBUtil.fromDB2(rs.getString("kehumc"))%>" readonly>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>�����ͬ����*</td>
														<td class="page_form_td">
															<input type="text" name="dkhthm" value="<%=DBUtil.fromDB2(rs.getString("dkhthm"))%>"
																class="page_form_text" mayNull='false' errInfo='�����ͬ����'>
														</td>
														<td class="page_form_title_td" nowrap>��������*</td>
														<td class="page_form_td">
															<select name="daikzl" class="page_form_select" title='��������'>
																<option value="" ></option>
																<%=fc.getEnumByField("daikzl2") %>
															</select>
															<script language="javascript">
															setVal(document.all.daikzl,'<%=DBUtil.fromDB2(rs.getString("daikzl")).trim()%>');
															</script>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>������ʽ*</td>
														<td class="page_form_td">
															<select name="daikfs" class="page_form_text" title='������ʽ' >
															<option vlaue=""></option>
															<%=fc.getEnumByField("daikfs") %>
															</select>
															<script language="javascript">
															setVal(document.all.daikfs,'<%=DBUtil.fromDB2(rs.getString("daikfs")).trim()%>');
															</script>
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
														<td class="page_form_title_td" nowrap>�����ͬ���*</td>
														<td class="page_form_td">
															<input type="text" name="dkhtje" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("dkhtje"))%>"
																class="page_form_text" readonly>
														</td>
														<td class="page_form_title_td" nowrap>�������*</td>
														<td class="page_form_td">
														<input type="text" name="daikye" class="page_form_text" value="<%=DBUtil.numberToMoney(rs.getBigDecimal("daikye"))%>" readonly>
															
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>��ͬ��Ч����*</td>
														<td class="page_form_td">
														<input type="text" name="htsxrq" value="<%=DBUtil.toDate(rs.getString("htsxrq"))%>" class="page_form_text"  maxlength="8"  mayNull='false' errInfo='��ͬ��Ч����' >
														<input type="button" value="��" class="page_form_refbutton" onclick="setday(this,winform.htsxrq)" >
														</td>
														<td class="page_form_title_td" nowrap>��ͬ��ֹ����*</td>
														<td class="page_form_td">
														<input type="text" name="htzzrq" value="<%=DBUtil.toDate(rs.getString("htzzrq"))%>" class="page_form_text"  maxlength="8"  mayNull='false' errInfo='��ͬ��ֹ����' >
														<input type="button" value="��" class="page_form_refbutton" onclick="setday(this,winform.htzzrq)" >
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>����ΥԼ����*</td>
														<td class="page_form_td">
														<input type="text" name="weiyqs" value="<%=DBUtil.fromDB2(rs.getString("weiyqs"))%>"class="page_form_text" mayNull='false' errInfo='����ΥԼ����'>
														</td>
														<td class="page_form_title_td" nowrap>������������*</td>
														<td class="page_form_td">
															<input type="text" name="yuqits" value="<%=DBUtil.fromDB2(rs.getString("yuqits"))%>"
																class="page_form_text" readonly >
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>��Ϣ��������*</td>
														<td class="page_form_td">
														<input type="text" name="lxyqts" class="page_form_text" value="<%=DBUtil.fromDB2(rs.getString("lxyqts"))%>" mayNull='false' errInfo='��Ϣ��������'> 
														</td>
														<td class="page_form_title_td" nowrap>���������*</td>
														<td class="page_form_td">
															<input type="text" name="fenlyj" value="<%=DBUtil.fromDB2(rs.getString("fenlyj"))%>"
																mayNull='false' errInfo='���������' class="page_form_text">
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>����״��(ѡ��)</td>
														<td class="page_form_td">
															<input type="text" name="dbzk" value="<%=DBUtil.fromDB2(rs.getString("dbzk"))%>" class="page_form_text" >
														</td>
														<td class="page_form_title_td" nowrap>�����������*</td>
														<td class="page_form_td">
														<input type="text" name="fljzrq" value="<%=DBUtil.toDate(rs.getString("fljzrq"))%>" class="page_form_text"  maxlength="8" readonly>
														<input type="button" value="��" class="page_form_refbutton" onclick="setday(this,winform.fljzrq)" disabled>
														</td>
														<td class='page_form_td'>&nbsp;
															
													  </td>
													</tr>
													<tr class='page_form_tr'>
														<td class="page_form_title_td" nowrap>������*</td>
														<td class="page_form_td" colspan=3>
														<select name="fleijg" title="������" class="page_form_select" onchange="setVal(document.all.fleijg,'<%=DBUtil.fromDB2(rs.getString("fleijg")).trim()%>');">
																<option value=""></option>
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
		</form>
		<%} %>
	</body>
</html>
