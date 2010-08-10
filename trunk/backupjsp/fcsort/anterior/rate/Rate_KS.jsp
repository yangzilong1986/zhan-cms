<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="zt.cms.fcsort.common.FcsortUtil" %>
<%@ page import="zt.cms.fcsort.anterior.rate.*" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/anterior/rate/";
	FcsortUtil pageutil = new FcsortUtil();
	RatePageObject vp  = new RatePageObject();
	vp.setRequest(request);
	DecimalFormat df1=new DecimalFormat("###,###,###,##0.00");
	DecimalFormat df2=new DecimalFormat("###,###,###,##0.0000");
	
 %>
<%--
=============================================== 
Title:五级分类非正常类迁徙率
 * @version  $Revision: 1.3 $  $Date: 2007/05/31 03:13:58 $
 * @author   zhengxin
 * <p/>修改：$Author: zhengx $   
=============================================== 
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>五级分类非正常类贷款迁徙率</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<link rel="stylesheet" type="text/css" href="<%=basePath%>fcsort/common/web.css">
		<script language="javascript" src="<%=basePath%>js/meizzMonth.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/ajax.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/common.js"></script>
		<script language="javascript" src="rate.js"></script>
		<script type="text/javascript">
			var basePath="<%=basePath %>";
			var currentPath ="<%=currentPath %>";
			
		</script>
	</head>
	<body background="/images/checks_02.jpg">
		<form action="<%=currentPath%>Rate_KS.jsp" name="listfrom">
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
																<!--<input type=button class=button value=" 打 印 " onclick="print();">
																&nbsp;&nbsp;
																--><input type="button" class="button" value=" 关 闭 "onclick="self.close()">
																&nbsp;
															</div>
															<br>
															<table class="table" cellSpacing=1 cellPadding=1
																width=100% border=0>
																<tr class=head>
																	<td>
																		&nbsp;<%=pageutil.selectType2("MORY","MORY",vp.getMory()) %> 
																	
																		&nbsp; 网点名称：<%=pageutil.getBrhids("brhid","brhid",vp.getBrhid()) %>
																		&nbsp; 查询时点：<%=pageutil.setcreadate("creadate","creadate",vp.getCreadate()) %>
																
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
											</TABLE><font size="4"></font>
											<div id='showTable' align=center width=100%>
												<div class=caption align=center>
													
													<font ><%=vp.getTitles() %></font>
												</div> 有效日期：<%=vp.getCreadate() %>
													<div align=center>
														<table cellSpacing=0 cellPadding=0 width=100% border=0>
															<tr class=title>
																<td>
																	网点名称：<%=vp.getScbrhname() %>
																</td>
																<td align="center">
																	
																</td>
																<td align=right >
																	单位：万元 %
																</td>
															</tr>
														</table>
													</div>
													<table class=table cellSpacing=1 cellPadding=1 width=100% border=0 align=center>
														<TBODY>
															  <tr class="head">
															    <td  align="center" valign="bottom" >网点</td>
															    <td align="center" valign="bottom">期初向下迁徙金额</td>
															    <td  align="center" valign="bottom">期初贷款余额</td>
															    <td  align="center" valign="bottom">期间减少额</td>
															    <td  align="center" valign="bottom">迁徙率</td>
															  </tr>
															  <%
															  List list = vp.getListKS();
															  double bala =0.00;
															  double balb =0.00;
															  double balc =0.00;
															  double bale =0.00;
															  double rate =0.00;
															  	for(int i=0;i<list.size();i++)
															  	{
															  		FirstTr tr = (FirstTr)list.get(i);
															  		
															  		bala+=tr.getBala();
															  		
															  		balb+=tr.getBalb();
															  		
															  		balc+=tr.getBalc();
															  	
															  		 bale=(balb-balc)==0?1:(balb-balc);
															  		
															  		 rate=bala/bale*100;
															  		
															  		%>
															  		 <tr class=data bgcolor=#FFFFFF onmouseout="mOut(this);" onmouseover="mOvr(this);" onclick="tosecondlistKS('<%=tr.getBrhid().trim() %>');">
															    	<td > <%=tr.getBrhname() %></td>
															    	<td align="right"><%=df1.format(tr.getBala()) %></td>
															   		<td align="right"><%=df1.format(tr.getBalb()) %></td>
															    	<td align="right"><%=df1.format(tr.getBalc()) %></td>
															    	<td align="right"><%=df2.format(tr.getRate()) %>%</td>
															  		</tr>
															  		<% 
															  		
															  	}
															   %>
															   <%
															   for(int i=0;i<10-list.size();i++)
															   {
															   	%>
															   	 <tr class=data bgcolor=#FFFFFF >
															    	<td >&nbsp; </td>
															    	<td align="right">&nbsp;</td>
															   		<td align="right">&nbsp;</td>
															    	<td align="right">&nbsp;</td>
															    	<td align="right">&nbsp;</td>
															  		</tr>
															   	<%
															   }
															    %>
															  <tr class="head">
															    	<td align="center">合计</td>
															    	<td align="right"><%=df1.format(bala) %></td>
															   		<td align="right"><%=df1.format(balb) %></td>
															    	<td align="right"><%=df1.format(balc) %></td>
															    	<td align="right"><%=df2.format(rate) %>%</td>
															 
															   
															  		</tr>
														</TBODY>
													</TABLE>
													<table cellSpacing=0 cellPadding=0 width=100% border=0
														class=head>
														<tr>
					
															<td align=right>
																<input type="button" value="返回初始页" class="button" onclick="backStartListKS();">	&nbsp;
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
			<div id=sending style="position:absolute; top:50%; left:37%; z-index:2; display:none;" align=center>
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
