<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="zt.cms.fcsort.common.FcsortUtil" %>
<%@ page import="zt.cms.fcsort.anterior.*" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/anterior/";
	FcsortUtil pageutil = new FcsortUtil();
	AnteriorPageobject vp = new AnteriorPageobject();
	vp.setRequest(request);
	List list = vp.getFirstList();
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	String bad=vp.getQueryid().trim().equals("ANTERIALL")?"all":"one";
 %>
 <%--
=============================================== 
Title:五级分类正常贷款客户排名统计表
 * @version  $Revision: 1.7 $  $Date: 2007/05/28 11:46:47 $
 * @author   zhengxin
 * <p/>修改：$Author: zhengx $   
=============================================== 
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>五级分类贷款客户排名统计表</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<link rel="stylesheet" type="text/css" href="<%=basePath%>fcsort/common/web.css">
		<script language="javascript" src="<%=basePath%>js/meizzMonth.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/ajax.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/common.js"></script>
		<script type="text/javascript">
			var basePath="<%=basePath %>";
			var currentPath ="<%=currentPath %>";
			function tosecondlist(clientno,clienname)
			{
				var url = currentPath + "secondlist.jsp?clientno=" + clientno+"&QUERYID=<%=vp.getQueryid()%>&clienname="+clienname;
				//alert(url);
				var st="status=yes,toolbar=yes,menubar=no,location=no,scrollbars=yes,resizable=yes";
				top.window.open(url,"_blank",st).focus();
			}
			function backStartList()
			{
				var QUERYID = document.all.QUERYID.value;
				var url = currentPath + "firstlist.jsp?QUERYID=" + QUERYID;
				window.location = url;
			}
			function toclentinfo(clientno,brhid,clientname,bal,types)
			{	var date =document.all.creadate.value;
				
				var url = currentPath + "clentInfo.jsp?clientno=" + clientno+"&brhid="+brhid+"&clientname="+clientname+"&bal="+bal+"&date="+date+"&types="+types;
				var st="status=yes,toolbar=yes,menubar=no,location=no,scrollbars=yes,resizable=yes";
				top.window.open(url,"_blank",st).focus();
			}
			function tosumit()
			{
				var anterior = document.all.anterior.value;
				if(anterior>100)
				{
					alert("只允许检索排名在100以内的客户记录！");
					return false;
				} 
				else
				{
					document.listfrom.submit();
				}
			}
		</script>
	</head>
	<body background="/images/checks_02.jpg">
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
																<input type="button" value=" 检 索 " class="button"
																	onclick="tosumit();">
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
																		
																		&nbsp; 网点名称：<%=pageutil.getBrhids("brhid","brhid",vp.getBrhid()) %>
																
																		&nbsp; 有效日期：
																		<input class='input' type=text id="creadate"name="creadate" size=10 value="<%=vp.getCreadate() %>">
																			
																		<input type='button' value='…' class='button' onclick='setday(this,document.getElementById("creadate"))'>
																		
																		&nbsp; 排名： <input class='input' type=text id="anterior"name="anterior" size=5 value="<%=vp.getAnterior() %>">
																						
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
											</TABLE><font size="4"><%=vp.getTitile() %></font>
											<div id='showTable' align=center width=100%>
												<div class=caption align=center>
													
													<font class="title">有效日期：<%=vp.getMoeltitle()%></font>
												</div>
													<div align=center>
														<table cellSpacing=0 cellPadding=0 width=100% border=0>
															<tr class=title>
																<td>
																	网点名称：<%=vp.getScbrhname() %> 
																</td>
																<td align="center">
																	
																</td>
																<td align=right >
																	单位：万元
																</td>
															</tr>
														</table>
													</div>
													<table class=table cellSpacing=1 cellPadding=1 width=100% border=0 align=center>
														<TBODY>
															  <tr class="head" valign="bottom" align="center">
															    <td rowspan="2" >客户名称</td><!--
															    <td rowspan="2">网点号</td>
															    --><td rowspan="2" >笔数</td>
															    <td colspan="3" >贷款余额</td>
															    <td colspan="3" >担保方式</td>
															    <td colspan="2" >欠息</td>
															    <td colspan="5" >五级分类</td>
															    <td colspan="2" rowspan="2"> 操作</td>
															  </tr>
															  <tr class="head" valign="bottom" align="center">
															    <td >合计余额</td>
															    <td>贷款余额</td>
															    <td>承兑垫</td>
															    <td>信用</td>
															    <td>保证</td>
															    <td>抵、质押</td>
															    <td>表内</td>
															    <td>表外</td>
															    <% 
															   	if(vp.getQueryid().trim().equals("ANTERIALL"))
																	    	{
																%>
															    <td>正常</td>
															   	<td>关注</td>
															    	
															    <%}
															    else
															    { 
															    %>
															    <td colspan="2">不良合计</td>
															    <% }%>
															    
															  
															    <td>次级</td>
															    <td>可疑</td>
															    <td>损失</td>
														      </tr>
															 
															  <%
															  	for(int i=0;i<list.size();i++)
															  	{
															  		FirstTr tr =(FirstTr)list.get(i);
															  		
															  		%>
															  		<tr class=data bgcolor=#FFFFFF   >
																	  	<td class=data title="点击查看客户详细信息"  onclick="toclentinfo('<%=tr.getClentno() %>','<%=tr.getBrhid() %>','<%=tr.getClentname() %>','<%=tr.getCountbal() %>','<%=bad %>')" onmouseout="mOut(this);" onmouseover="mOvr(this);"><%=tr.getClentname() %></td>
																	
																	    <td class=data align="right"><%=tr.getDbcount() %></td>
																	    <td class=data align="right"><%=df.format(tr.getCountbal()) %></td>
																	    <td class=data align="right"><%=df.format(tr.getBalA()) %></td>
																	    <td class=data align="right"><%=df.format(tr.getBalB()) %></td>
																	    <td class=data align="right"><%=df.format(tr.getBal3()) %></td>
																	    <td class=data align="right"><%=df.format(tr.getBal4()) %></td>
																	    <td class=data align="right"><%=df.format(tr.getBal5()) %></td>
																	    <td class=data align="right"><%=df.format(tr.getDuebal1()) %></td>
																	    <td class=data align="right"><%=df.format(tr.getDuebal2()) %></td>
																	    
																	     <%
																	    	if(vp.getQueryid().trim().equals("ANTERIALL"))
																	    	{
															    		%>
																	    <td class=data align="right"><%=df.format(tr.getFczcbal()) %></td>
																	    <td class=data align="right"><%=df.format(tr.getFcgzbal()) %></td>
																	  
																	    <% } else
																	    {
																	    %>
																	    	<td class=data align="right" colspan="2"><%=df.format(tr.getSumbad()) %></td>
																	    <%
																	    }
																	     %>
																	    
																	    <td class=data align="right"><%=df.format(tr.getFcbalcj()) %></td>
																	    <td class=data align="right"><%=df.format(tr.getFcbalky()) %></td>
																	    <td class=data align="right"><%=df.format(tr.getFcbalsh()) %></td>
																	    <td class=data align="center" onclick="tosecondlist('<%=tr.getClentno() %>','<%=tr.getClentname() %>')" onmouseout="mOut(this);" onmouseover="mOvr(this);" title="点击查看客户相关贷款">贷款</td>
																	    
																	    
															  		</tr>
															  		<% 
															  	}
															   %>
															   <%
																for(int i=0;i<10-list.size();i++)
																{
																	%>
																	
																	<tr class=data bgcolor=#FFFFFF>
																		
																		<%
																		for(int j=0;j<=15;j++)
																		{
																		%>
																		<td class=data >&nbsp;</td>
																		<% 
																		}
																		 %>
																		
																		
																	</tr>
																	<% 
																}
															 %>
																<tr class=head>
																	<td align=center>
																		合计
																	</td>
																
																	<td align=right>
																		<%=vp.getSumA() %>
																	</td>
																	<%
																	double sumb[] =vp.getSumB();
																	for(int i=0 ;i<sumb.length;i++)
																	{
																		if(!vp.getQueryid().trim().equals("ANTERIALL"))
																		{
																			if(i==8||i==9)
																			{
																				if(i==8)
																				{
																					%>
																					<td colspan="2" align=right><%=df.format(vp.getSunE()) %></td>
																					<%
																				}
																				continue;
																			}
																			else
																			{
																				%>
																					<td align=right><%=df.format(sumb[i]) %></td>
																				<% 
																			}
																		}
																		else
																		{
																		%>
																		<td align=right><%=df.format(sumb[i]) %></td>
																		<% 
																		}
																	}
																		
																	 %>
																	<td align=center>
																		&nbsp;
																	</td>
																	
																
																</tr>
														</TBODY>
													</TABLE>
													<table cellSpacing=0 cellPadding=0 width=100% border=0
														class=head>
														<tr>
					
															<td align=right>
																<input type="button" value="返回初始页" class="button" onclick="backStartList();">	&nbsp;
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
