<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.fcsort.transfer.TransferPageobject" %>
<%@ page import="zt.cms.fcsort.transfer.ThirdTr"%>
<%@ page import="zt.cms.fcsort.common.FcsortUtil" %>
<%@ page import="java.text.DecimalFormat;" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/transfer/";
	TransferPageobject vp = new TransferPageobject();
	vp.setRequest(request);
	FcsortUtil sd = new FcsortUtil();
	DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	String title = vp.getTitle();

	//****************************分页相关****************************
	vp.setPagecount();//设置最大记录数
	vp.setMaxpage();//设置最大页数
	int pagecount = vp.getPagecount();//页面记录数
	int pagesize = vp.getPagesize();// 页面大小
	int maxpage = vp.getMaxpage();// 最大页
	int currpage = vp.getCurrpage();// 当前页
	String updisabled =currpage==1?"disabled":"";
	String downdisabled =currpage==maxpage?"disabled":"";
	String selectYM = "<input type=\"hidden\" id=\"stype\" name=\"stype\" value=\"\">";//:sd.selectType("stype","stype",vp.getStype());
	String clientmgr="";//客户经理
	String bt="";//是否合计
	String scbrhtype = SCBranch.getBrhtype(vp.getBrhid()).trim();
	String brhlevel =sd.getBrhlevel(vp.getBrhid().trim()).trim();
	if(!scbrhtype.trim().equals("9"))
	{
		clientmgr="客户经理："+sd.getCLIENTMGR("CLIENTMGR","CLIENTMGR",vp.getClientmgr(),vp.getBrhid());
	}
	String  listTH = "借款人名称,贷款用途,合同金额,结欠金额,期限,利率,发放日,到期日,四级形态,五级分类,第一责任人,形成原因";
	

																	
			
	List list = vp.getThirdList();								
	List listTR = new ArrayList();
	for(int i =0;i<list.size();i++)
	{	List listTd = new ArrayList();
		
		ThirdTr tr = (ThirdTr) list.get(i);	
		listTd.add(tr.getTd1());
		listTd.add(tr.getTd2());
		listTd.add(tr.getContractamt());
		listTd.add(tr.getTd5());
		
		listTd.add(tr.getTd3());
	
		listTd.add(tr.getTd4());
		
		listTd.add(tr.getTd6());
		
		listTd.add(tr.getTd7());
		listTd.add(tr.getTdF());
		
		listTd.add(tr.getTd8());
		listTd.add(tr.getTd9());	
		listTd.add(tr.getTd10());
		listTR.add(listTd);

	}
	String[] titles= new String[]{vp.getTitle()+"明细","单位名称："+vp.getScbrhname(),vp.getMTitle(),"单位：元"};
	String[] sum =new String[]{"合计","",String.valueOf(df.format(vp.getSumD())),df.format(vp.getSumD()),"","","","","","","",""};
	HashMap printMap= new HashMap();
	printMap.put("titles",titles);
	
	printMap.put("listTH",listTH);
	printMap.put("listTR",listTR);
	printMap.put("sum",sum);
	request.getSession().setAttribute("printMap",printMap);
%>
<%--
=============================================== 
Title:五级分类迁移情况查询二级页面
 * @version  $Revision: 1.7 $  $Date: 2007/05/28 11:46:35 $
 * @author   zhengxin
 * <p/>修改：$Author: zhengx $   dfd
=============================================== 
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>五级分类不良资产台帐</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<link rel="stylesheet" type="text/css" href="<%=basePath%>fcsort/common/web.css">
		<script language="javascript" src="<%=basePath%>query/setup/meizzDate.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/ajax.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/common.js"></script>
		<script language="javascript" src="transfer.js"></script>
		<script type="text/javascript">
			var basePath="<%=basePath %>";
			var currentPath ="<%=currentPath %>";
			function print()
			{
				var printurl=basePath+"/servlet/ExportExcel";
				window.location=printurl;
				
			}
			function print()
			{
				var printurl=basePath+"/servlet/ExportExcel";
				window.location=printurl;
				
			}
		</script>
	</head>
	<body background="/images/checks_02.jpg">
		<form action="<%=currentPath%>secondlist.jsp" name="listfrom">
			<div id=aaaa align="center">
				<TABLE borderColor=#999999 cellSpacing=0 cellPadding=0 align=center
					border=1 width=100% bgcolor=#f1f1f1>
					<TBODY>
						<TR Align=center>
							<TD align="center">
								<TABLE width=95%>
									<TBODY>
										<TR align="center">
											<TD align="center">
												<table width=100% border=0 align="center" cellPadding=0
													cellSpacing=0>
													<TR>
														<TD height=2>
															&nbsp;

														</TD>
													</TR>
													<TR>
														<td id=detailTab align="center">
															<div class=head align="right">
																<!-- 保存页面参数 -->
																<input type="hidden" name="sqlpartname" value="<%=vp.getSqlpartname() %>">
																<input type="hidden" name="sqlpartvalue" value="<%=vp.getSqlpartvalue() %>">
																<input type="hidden" id="brhid" name="brhid" value="<%=vp.getBrhid() %>">
																<input type="hidden" id="QUERYID" name="QUERYID" value="<%=vp.getQueryid() %>">
																<input type="button" value=" 检 索 " class="button"onclick="document.listfrom.submit();">
																&nbsp;&nbsp;
																<input type="reset" value=" 重 置 " class="button" >
																&nbsp;&nbsp;
																<input type=button class=button value=" 打 印 " onclick="print();">
																&nbsp;&nbsp;
																<input type="button" class="button" value=" 关 闭 "onclick="self.close()">
																&nbsp;
															</div>
															<br>
															<table class="table" cellSpacing=1 cellPadding=1
																width=100% border=0>
																<tr class=head>
																	<td>
																		
																		单位名称:<%=vp.getScbrhname() %>
																
																		&nbsp; 查询时点：
																		<%=sd.setcreadate("creadate", "creadate", vp.getCreadate())%>
																		&nbsp;
																				<%=clientmgr %>	
																		&nbsp;&nbsp;贷款发放日：
																		<input class='input' type=text id="startdate"
																			name="startdate" size=10
																			value="<%=vp.getStartdate() %>">
																		<input type='button' value='…' class='button'
																			onclick='setday(this,document.getElementById("startdate"))'>
																		至
																		<input class='input' type=text id="enddate"
																			name="enddate" size=10 value="<%=vp.getEnddate() %>">
																		<input type='button' value='…' class='button'
																			onclick='setday(this,document.getElementById("enddate"))'>
																		单笔贷款余额:
																		<input type='text' size='3' name='sartbal'
																			class='input' value="<%=vp.getSartbal() %>">
																		至
																		<input type='text' size='3' name='endbal'
																			class='input' value="<%=vp.getEndbal() %>">
																		万元
																	</td>
																</tr>
															</table>
														</td>
													</TR>
													<TR>
														<TD colspan=2 height=2>
															&nbsp;
														</TD>
													</TR>
											</TABLE><font size="4"><%=vp.getTitle() %></font>
											<div id='showTable' align=center width=100%>
												<div class=caption align=center>
													
													<font class="title"><%=vp.getMTitle() %></font>
												</div>
													<div align=center>
														<table cellSpacing=0 cellPadding=0 width=100% border=0>
															<tr class=title align="center">

																<td align=center class=title>

																</td>

															</tr>
															<tr class=title>
																<td class=title>
																	迁移类型:<%=vp.getLefttitle() %>
																	
																</td>

																<td align=right class=title>
																	单位：元
																</td>
															</tr>
														</table>
													</div>
													<table class=table cellSpacing=1 cellPadding=1 width=100%
														border=0 align=center>
														<TBODY>
															<tr>
																<td valign="bottom" class="head">

																	借款人名称
																</td>
																<td align="center" valign="bottom" class="head">
																	贷款用途
																</td>
																<td align="center" valign="bottom" class="head">
																	合同金额
																</td>
																<td align="center" valign="bottom" class="head">
																	结欠余额
																</td>
																<td align="center" valign="bottom" class="head">
																	期限
																</td>
																<td align="center" valign="bottom" class="head">
																	利率
																</td>
																<td align="center" valign="bottom" class="head">
																	发放日
																</td>
																<td align="center" valign="bottom" class="head">
																	到期日
																</td>
																<td align="center" valign="bottom" class="head">
																	四级形态
																</td>
																<td align="center" valign="bottom" class="head">
																	现五级分类
																</td>
																<td align="center" valign="bottom" class="head">
																	第一责任人
																</td>
																<td align="center" valign="bottom" class="head">
																	形成原因
																</td>
															</tr>

															<%
																
																int margin= pagesize-list.size();
																for (int i = 0; i < list.size(); i++) {
																	ThirdTr tr = (ThirdTr) list.get(i);
															%>
															<tr bgcolor=#FFFFFF
																onclick="showLoanInfo('<%=tr.getTd0() %>');"
																onmouseout="mOut(this);" onmouseover="mOvr(this);"
																class=data>
																<td class=data>
																	<%=tr.getTd1()%>

																</td>
																<td class=data align=right>
																	<%=tr.getTd2()%>


																</td>
																<td class=data align=right>
																	<%=tr.getContractamt()%>


																</td>
																<td class=data align="right">
																	<%=tr.getTd5()%>


																</td>
																<td class=data align=center>

																	<%=tr.getTd3()%>

																</td>

																<td class=data align="right">
																	<%=tr.getTd4()%>


																</td>

																<td class=data align="center">
																	<%=tr.getTd6()%>


																</td>
																<td class=data align="center">
																	<%=tr.getTd7()%>


																</td>
																<td class=data align="center">
																	<%=tr.getTdF()%>


																</td>
																<td class=data align="center">
																<%=tr.getTd8()%>
																</td>
																<td class=data>
																	<%=tr.getTd9()%>
																</td>
																<td class=data>
																	<%=tr.getTd10()%>


																</td>

															</tr>
															<%
															}
															%>
															
															<%
															if(margin>0)
															{
																for(int i=0;i<margin;i++)
																{
																	 %>
																	 <tr bgcolor=#FFFFFF class=data>
																	 <%
																	 for(int j=0;j<12;j++)
																	 {
																	 %>
																	 <td class=data>&nbsp;</td>
																	 <% 
																	 }
																	  %>
																	 </tr>
																	 <% 
																}
															}
																
															 %>
															<tr class=head>
																<td align=center>
																	合计
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																
																<%=df.format(vp.getSumD()) %>
																</td>
																<td align=right>
																	&nbsp;
																
																<%=df.format(vp.getSumE()) %>
																</td>
																<td align=right>
																	&nbsp;
																
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>

															</tr>
														</TBODY>
													</TABLE>
													<table cellSpacing=0 cellPadding=0 width=100% border=0
														class=head>
														<tr>
															<td>
																每页
																<input type='text' name="pagesize" size='3'value="<%=pagesize %>" class='input'>
																条 | 共<%=maxpage %>页<%=pagecount%>条记录 &nbsp;
																<input type='button' value='首 页'onClick="goPage('top');" class='button'>
																&nbsp;
																<input type='button' value='上 页' onClick='goPage("up");' class='button' <%=updisabled %> />
																&nbsp;
																<input type='button' value='下 页' onClick='goPage("down");' class='button' <%=downdisabled %> />
																	
																&nbsp;
																<input type='button' value='末 页' onClick='goPage("bottom");' class='button'>
																<input type="hidden" name="maxpage" value="<%=maxpage %>">	
																&nbsp; 第
																<input type="text" name="currpage" value="<%=currpage %>" class="input" size="3">
																<input type="hidden" name="pageer"  value="<%=currpage %>">
																页<input type='button' value='确定' onclick='goPage("gopage");' class='button'>
															</td>
															<td align=right>
																<input type="button" value="返回初始页" class="button" onclick="backStartList();">	&nbsp;
																<input type="button" value="返回上级" class="button" onclick=" backfistList();">&nbsp;
																<input type='button' value='返回上页' onclick='history.back();' class='button'>
																&nbsp;
															</td>
														</tr>
													</table>

												</div>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
								<br>
							</TD>
						</TR>
					</TBODY>
				</TABLE>

			</div>
			<div id=bbbb>
			</div>
			<div id=over
				style="position:absolute; top:0; left:0; z-index:1; display:none;"
				width=100% height=700>
				<table width=100% height=700>
					<tr>
						<td>
						</td>
					</tr>
				</table>
			</div>
			<div id=sending
				style="position:absolute; top:50%; left:37%; z-index:2; display:none;"
				align=center>
				<table width="250" height="80" border="0" cellpadding="0"
					cellspacing="1">
					<tr>
						<td bgcolor=#999999 align=center height=20 width=100>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td bgcolor=eeeeee align=center height=50>
							正在处理中……
						</td>
					</tr>
					<tr>
						<td bgcolor=#cacaca align=center height=10>
							&nbsp;
						</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>
