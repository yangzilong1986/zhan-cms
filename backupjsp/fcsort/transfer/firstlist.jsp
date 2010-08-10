<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.fcsort.transfer.TransferPageobject" %>
<%@ page import="zt.cms.fcsort.transfer.FirstTr" %>
<%@ page import="zt.cms.fcsort.common.FcsortUtil" %>
<%@ page import="java.text.DecimalFormat;" %>
<%--
=============================================== 
Title:五级分类迁移情况查询一级页面
 * @version  $Revision: 1.8 $  $Date: 2007/05/31 03:13:56 $
 * @author   zhengxin
 * <p/>修改：$Author: zhengx $   dfd
=============================================== 
--%>
<%
	String path = request.getContextPath();
	String basePath= request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/transfer/";
	TransferPageobject vp =new TransferPageobject();
	vp.setRequest(request);
	FcsortUtil sd = new FcsortUtil();
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	String selectYM = "<input type=\"hidden\" id=\"stype\" name=\"stype\" value=\"\">";//:sd.selectType("stype","stype",vp.getStype());
	String clientmgr="";//客户经理
	String bt="";//是否合计
	String scbrhtype = SCBranch.getBrhtype(vp.getBrhid()).trim();
	String brhlevel =sd.getBrhlevel(vp.getBrhid().trim()).trim();
	if(!scbrhtype.trim().equals("9"))
	{
		clientmgr="客户经理："+sd.getCLIENTMGR("CLIENTMGR","CLIENTMGR",vp.getClientmgr(),vp.getBrhid());
	}
	String  listTH = "迁移类别,贷款笔数,贷款户数,合同金额,当前余额";

	List vplist = vp.getFirstList();
    													
    			
									
	List listTR = new ArrayList();
	for(int i =0;i<vplist.size();i++)
	{	List listTd = new ArrayList();
		FirstTr vo=(FirstTr)vplist.get(i);	
	
	
		listTd.add(vo.getEnudt());
		listTd.add(String.valueOf(vo.getTotalA()));
		listTd.add(String.valueOf(vo.getTotalB()));
	
		listTd.add(df.format(vo.getBalA()));
		
		listTd.add(df.format(vo.getBalB()));
		listTR.add(listTd);
	}
	String[] titles= new String[]{vp.getTitle()+"一级","单位名称："+vp.getScbrhname(),vp.getMTitle(),"单位：万元"};
	String[] sum =new String[]{"合计",String.valueOf(vp.getSumA()),String.valueOf(vp.getSumB()),df.format(vp.getSumE()),df.format(vp.getSumF())};
	HashMap printMap= new HashMap();
	printMap.put("titles",titles);
	
	printMap.put("listTH",listTH);
	printMap.put("listTR",listTR);
	printMap.put("sum",sum);
	request.getSession().setAttribute("printMap",printMap);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>五级分类迁移情况</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>fcsort/common/web.css">
		<script language="javascript" src="<%=basePath%>query/setup/meizzDate.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/ajax.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/common.js"></script>
		<script type="text/javascript" src="transfer.js"></script>
		<script type="text/javascript">
			var basePath="<%=basePath %>";
			var currentPath ="<%=currentPath %>";
			function print()
			{
				var printurl=basePath+"/servlet/ExportExcel";
				window.location=printurl;
				
			}
		</script>
	</head>

	<body background="/images/checks_02.jpg" >
	<form action="<%=currentPath%>firstlist.jsp" name="listfrom">
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
														<table class="table" cellSpacing=1 cellPadding=1 width=100% border=0>
																
															<tr class=head>
					
																<td>
																&nbsp;<%=sd.selectType2("MORY","MORY",vp.getMory()) %>
																&nbsp;网点名称
																<%=sd.getBrhids("brhid","brhid",vp.getBrhid()) %>
																
																&nbsp;
																查询时点：	
																<%=sd.setcreadate("creadate","creadate",vp.getCreadate()) %>
																&nbsp;
																<%=clientmgr %>	
																		
																&nbsp;贷款发放日：
																<input class='input' type=text id="startdate" name="startdate"size=10 value="<%=vp.getStartdate() %>">
																<input type='button' value='…' class='button'onclick='setday(this,document.getElementById("startdate"))'>
																至
																<input class='input' type=text id="enddate" name="enddate"size=10 value="<%=vp.getEnddate() %>">
																<input type='button' value='…' class='button'onclick='setday(this,document.getElementById("enddate"))'>
																<br>
																&nbsp;单笔贷款余额:
																<input type='text' size='3' name='sartbal' class='input' value="<%=vp.getSartbal() %>">
																至
																<input type='text' size='3' name='endbal' class='input' value="<%=vp.getEndbal() %>">
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
															
															<td align=center class=title >
																
															</td>
															
														</tr>
														<tr class=title>
															<td  class=title>
																单位名称:<%=vp.getScbrhname() %>
															</td>
									
															<td align=right  class=title>
																单位：万元
															</td>
														</tr>
													</table>
												</div>
												<table class=table cellSpacing=1 cellPadding=1 width=100%
													border=0 align=center>
													<TBODY>
														<tr>
															<td align="center" valign="bottom" class="head">
																类别
															</td>
															<td align="center" valign="bottom" class="head">
																贷款笔数
															</td>
															<td align="center" valign="bottom" class="head">
																贷款户数
															</td>
															<td align="center" valign="bottom" class="head">
																合同金额
															</td>
															<td align="center" valign="bottom" class="head">
																当前余额
															</td>
														
														</tr>
											
    													<%
    														
    														String calss="";
    														String onclick="";
    														for(int i =0;i<vplist.size();i++)
    														{
    															FirstTr vo=(FirstTr)vplist.get(i);
    																	if(brhlevel.trim().equals("1")||brhlevel.trim().equals("2"))
    															{
    																bt="A";
    															}
    															else if(scbrhtype.trim().equals("9"))
    															{
    																bt="B";
    															}
    															else
    															{
    																bt="C";
    															}
    															if(vp.getQueryid().trim().equals("DTRANSFER"))
    															{
    																calss=(8-i==1||11-i==1)?"head":"data";
    																onclick=(8-i==1||11-i==1)?"":"onclick=\"tosecondlistp('"+bt+"','"+vo.getEnutp()+"');\"";
    															
    															}
    															else
    															{
    																calss=(5-i==1||9-i==1||12-i==1)?"head":"data";
    																onclick=(5-i==1||9-i==1||12-i==1)?"":"onclick=\"tosecondlistp('"+bt+"','"+vo.getEnutp()+"');\"";
    															
    															}
    													
    															
    													%>
    													<tr bgcolor=#FFFFFF <%=onclick %> onmouseout="mOut(this);" onmouseover="mOvr(this);"class="<%=calss %>">
    															<td class=data >
																	<%=vo.getEnudt() %>
																	&nbsp;
																</td>
																<td class=data align=right>
																	&nbsp;<%=vo.getTotalA() %>
																</td>
																<td class=data align=right>
																	&nbsp;<%=vo.getTotalB() %>
																</td>
																<td class=data align="right">
																	&nbsp;<%=df.format(vo.getBalA()) %>
																</td>
																<td class=data align="right">
																	&nbsp;<%=df.format(vo.getBalB()) %>
																</td>
														</tr>
    													<% } %>
    													
    													<%for(int i=0;i<10-vplist.size();i++)
    													{
    														%>
    														<tr bgcolor=#FFFFFF class=data>
    														<%
    														for(int j=0;j<4;j++)
    														{
    														%>
    														
																<td class=data align="right">&nbsp;	
																</td>
    														
    														<%
    														} 
    														%>
    														</tr>
    														
    														<% 
    													} %>
														<tr class=head >
															<td align=center>
																合计
															</td>
													
															<td align=right>
																&nbsp;<%=vp.getSumA() %>
															</td>
													
															<td align=right>
																&nbsp;<%=vp.getSumB() %>
															</td>
															<td align=right>
																&nbsp;<%=df.format(vp.getSumE()) %>
															</td>
																<td align=right>
																&nbsp;<%=df.format(vp.getSumF()) %>
															</td>
													
															</tr>
													</TBODY>
												</TABLE>
												<table cellSpacing=0 cellPadding=0 width=100% border=0 class=head>
													<tr>

													
														<td align=right>
														<input type="button" value="返回初始页" class="button" onclick="backStartList();">&nbsp;
														<input type='button' value='返回上页'onclick='history.back();' class='button'>
															
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
		<div id=over style="position:absolute; top:0; left:0; z-index:1; display:none;"
			width=100% height=700>
			<table width=100% height=700>
				<tr>
					<td>
					</td>
				</tr>
			</table>
		</div>
		<div id=sending style="position:absolute; top:50%; left:37%; z-index:2; display:none;" align=center>
			<table width="250" height="80" border="0" cellpadding="0"cellspacing="1">
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
