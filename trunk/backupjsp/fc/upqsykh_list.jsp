<%@ page contentType="text/html; charset=GBK" errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.cmsi.fc.DBUtil,zt.cmsi.fc.FcUpXML" %>
<%--
=============================================== 
Title: 查询企事业贷款分类信息列表
Description: 查询企事业贷款分类信息列表。
 * @version  $Revision: 1.5 $  $Date: 2007/05/31 07:22:37 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<%
	//企业贷款客户信息
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	FcUpXML upxml= new FcUpXML();
	String qryFlag=request.getParameter("qryFlag");
	String brhid=request.getParameter("BRHID");
	String clientname=request.getParameter("NAME");
	String enddate=request.getParameter("fljzrq");
	//登录用户可能为信贷部门(包括虚、实网点),可能为实网点,根据登录的机构编号判断一下
	UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	if(um==null){
		response.sendRedirect("error.jsp");
	}
	if (brhid==null||brhid.equals(""))
		brhid = SCUser.getBrhId(um.getUserName()); //登录用户所在网点
	//brhid="907010000";
	//ConnectionManager manager=ConnectionManager.getInstance();
	//String brhlist=SCBranch.getAllSubBrh2(brhid);
	//brhlist="('"+brhlist.replaceAll(",","','")+"','"+brhid+"')";
	CachedRowSet rs=DB2_81.getRs("select *  from UPBRHID_MAPING where branchid='"+brhid+"'");
	String jgdm="";
	while (rs.next())
	{
		jgdm=rs.getString("upbranchid");
	}
	jgdm=jgdm.trim();
	if (jgdm.equals("")) throw new Exception("对应的银监局的机构编码为空！");
	SCBranch branch=new SCBranch();
	String str=branch.getAllSubBrh2(brhid);
	String brhlist="('"+str.replaceAll(",","','")+"','"+brhid+"')";
	//rs.close();
	//分页
	int start=0;
	int end=0;
	String pnStr = request.getParameter("pn");
	if(pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
	int pn=Integer.parseInt(pnStr==null?"1":pnStr);
	int ps=20;
	start = (pn - 1) * ps + 1;
	end = pn * ps;		
	Vector vec=null;
	int rows=0;
	CachedRowSet crs=null;
	String s_count="select serialno from UPqsykh  kh";	
	String s_main="select * from(select rownumber() over(order by kh.isempty desc) AS rn,kh.* from UPqsykh kh ";
	String s_where=" where 1=1 ";
	//s_where+=" and kh.jrjgdm like '"+jgdm+"%'";
	if (clientname!=null&&!clientname.equals(""))
		s_where+=" and kh.zhgwmc like '%"+clientname.trim()+"%'";
	if (enddate!=null&&!enddate.equals(""))
	{
		s_where+=" and kh.builddate='"+enddate+"'";
		s_where+=" and exists (select 1 from upqsydk where kh.khh=khh";
		s_where+=" and fljzrq='"+enddate.trim()+"' and brhid in "+brhlist;
		s_where+=" and fleijg in ('1','2'))";
	}
	String sql=" left join CMCORPCLIENT c on kh.khh=c.CLIENTNO";
	s_where +=" and c.APPBRHID in "+brhlist;
	s_main+=sql+s_where+" ) AS a1   "+" WHERE a1.rn BETWEEN "+start+" AND "+end;
	s_where=sql+s_where;
	if (qryFlag!=null)
	{
		vec=DB2_81.pageRs(s_main,s_count,s_where);
		rows=((Integer)vec.get(0)).intValue();
		crs=(CachedRowSet)vec.get(1);
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<link href="query/setup/web.css" rel="stylesheet" type="text/css">
		<script src='js/check.js' type='text/javascript'></script>
		<script src='js/pagebutton.js' type='text/javascript'></script>
		<script language="javascript" src="js/flippage3.js"></script>
		<script src='js/querybutton.js' type='text/javascript'></script>
		<script src='js/fc.js' type='text/javascript'></script>
		<script src='js/fcmain.js' type='text/javascript'></script>
		<link href="css/platform.css" rel="stylesheet" type="text/css">
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
		function query(){
			if (document.all.fljzrq.value=='')
			{
				alert('请选择清分截至日期！');
				return;
			}
			if (check(winform))
			{
				document.forms[0].submit();
			}
		}
		function deptrefer_click() {
        var url = "view.jsp";
        var vArray;
        document.all.referValue.value = window.open(url, 0, "height=400,width=460,toolbar=no,scrollbars=yes,status");
        if (document.all.referValue.value == "undefined") {
        return;
        }

    }
   function clsForm()
    {
    	document.all.BRHID.value="";
    	document.all.NAME.value="";
    	document.all.fljzrq.value="";
    }
		</script>
	</head>
	<body background="images/checks_02.jpg">
		<form id="winform" method="post" action="/fc/upqsykh_list.jsp">
		<input type="hidden" value="true" name="qryFlag"/>
		<input name="referValue" type="hidden">
		<table width="100%" height="100%" border="0" cellspacing="0"
			cellpadding="0">
			<tr>
				<td align="center" valign="middle">
					<table  border="2" align="center" cellpadding="2"
						cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" width="70%">
						<tr align="left">
							<td height="30" bgcolor="#4477AA" colspan=2>
								<img src="images/form/xing1.jpg" align="absmiddle">
								<font size="2" color="#FFFFFF"><b>企业贷款客户查询</b>
								</font>
								<img src="images/form/xing1.jpg" align="absmiddle">
							</td>
						</tr>
						<tr align="center">
							<td valign="top" colspan=2>
								<table width="100%" height="100%" cellspacing="0"
									cellpadding="0" border="0">
									<tr>
										<td width="20">
											&nbsp;
										</td>
										<td align="center" valign="top">
											<script src='js/querybutton.js' type='text/javascript'></script>
											<script src='js/meizzDate.js' type='text/javascript'></script>
											<table class='error_message_tbl'>
												<tr class='error_message_tbl_tr'>
													<td class='error_message_tbl_td'>
														<li class='error_message_li'>
															请输入查询条件后,再检索数据(-98)
														</li>
													</td>
												</tr>
											</table>
											<table width="100%">
												<tr>
													<td >
														<table align='center' cellpadding='0' cellspacing='0'
															border='0' bgcolor='#AAAAAA' width='100%'>
															<tr>
																<td height="0">
																	<table id="findDiv" class="query_table" cellpadding='0'
																		cellspacing='0' border='0' style='display:none' >
																		<tr class="query_tr">
																			<td class="query_td" width="100%">
																				<table class='query_form_table'
																					id='query_form_table' cellpadding='1'
																					cellspacing='1' border='0'>
																					<tr class="query_form_tr" nowrap>
																						<td class="page_form_title_td" nowrap>
																							维护网点
																						</td>
																						<td class="page_form_td">
																						<input type="text" name="BRHID" id='BRHID' value="<%=brhid %>" class="page_form_text" e="25"readonly> 
																						<input type="button" name="nameref" value="…" onclick="deptrefer_click()" class="page_form_refbutton">
																						</td>
																						<td class="page_form_title_td" nowrap>
																							清分时点
																						</td>
																						<td class="page_form_td">
																						<select name="fljzrq" >
                            															<%=upxml.getFCDateList() %>
                            															</select>
																						<script language="javascript">
																						setVal(document.all.fljzrq,'<%=DBUtil.fromDB2(enddate)%>');
																						</script>
																						</td>
																						
																					</tr>
																					<tr class="query_form_tr" nowrap>
																						<td class="page_form_title_td" nowrap>
																							客户名称
																						</td>
																						<td class="page_form_td">
																							<input type="text" name="NAME" value="<%=DBUtil.fromDB2(clientname) %>"
																								class="page_form_text" maxlength="80"
																								mayNull="1" dataType="1" errInfo="客户名称">
																						</td>
																					</tr>
																				</table>
																			</td>
																			<td class="query_td" width="20%" align="center">
																				<table border='0' width='100%' bgcolor='#F1F1F1'>
																					<tr>
																						<td nowrap valign="top">
																							<input type="button" class="query_button"
																								name="btnqry" value=" 检 索 " onclick="query();">
																						</td>
																					</tr>
																					<tr>
																						<td nowrap valign="top">
																							<input type="button" class="query_button"
																								name="reset1" value=" 重 置 " onclick="clsForm();">
																						</td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr>
																<td height="0" align="center">
																	<img id='findDivHandle' title='点击查询'
																		onClick='menuMove()' src='images/form/button1.jpg'
																		style='cursor:hand;'>
																</td>
															</tr>
														</table>
														<table cellpadding='0' cellspacing='0' border='0'>
															<tr>
																<td height='5'></td>
															</tr>
														</table>
														<%if (crs!=null){ %>
														<table class='list_form_table'width="100%" align='center' cellpadding='0' cellspacing='1' border='0'>
															<tr class='list_form_title_tr'>
																<td  class='list_form_title_td' nowrap>
																	客户号
																</td>
																<td  class='list_form_title_td' nowrap>
																	借款人中文名
																</td>
																<td class='list_form_title_td' nowrap>
																	法定代表人
																</td>
															</tr>
														<%
														String color="";
														while(crs.next()){
														if (crs.getString("isempty")==null) 
																color="#FFFF00";
															else
															   color="#ffffff";
														%>
															<tr  bgcolor="<%=color%>" onmouseover="mOvr(this)" onclick="mClk('/fc/upqsykh_detail.jsp?serialno=<%=crs.getString("serialno")%>');" onmouseout="mOut(this,'<%=color%>');">
																<td class='list_form_td'>
																	<%=DBUtil.fromDB2(crs.getString("khh"))%>
																</td>
																<td class='list_form_td'>
																	<%=DBUtil.fromDB2(crs.getString("zhgwmc"))%>
																</td>
																<td class='list_form_td'>
																	<%=DBUtil.fromDB2(crs.getString("faddbr"))%>
																</td>
															</tr>
															<%} %>
														</table>
														<%} %>
													</td>
													
												</tr>
											</table>
										</td>
										<td width="20">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr height="35" align="center" valign="middle">
							<td align="center" >
								<table border="0" cellspacing="0" cellpadding="0" width="538">
									<tr>
										<td nowrap align="center" >
												<input type='hidden' name='Plat_Form_Request_Instance_ID'
													value='5'>
												<input type='hidden' name='Plat_Form_Request_Event_ID'
													value='8'>
												<input type='hidden' name='Plat_Form_Request_List_Pageno'
													value=''>
												<input type='hidden' name='Plat_Form_Request_List_PageCount'
													value=''>
									</td>
									</tr>
									<script language="javascript">
									createFlipPage(<%=pn%>,<%=ps%>,<%=rows%>,"/fc/upqsykh_list.jsp?pn=","winform");
									</script>
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
