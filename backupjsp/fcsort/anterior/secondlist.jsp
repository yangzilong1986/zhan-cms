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
	request.setCharacterEncoding("GBK");   
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/anterior/";
	FcsortUtil pageutil = new FcsortUtil();
	AnteriorPageobject vp = new AnteriorPageobject();
	vp.setRequest(request);
	List list = vp.getSecondList();
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	String ClientName=(String)request.getParameter("clienname");
 %>
  <%--
=============================================== 
Title:客户其他贷款明细
 * @version  $Revision: 1.3 $  $Date: 2007/05/22 07:32:04 $
 * @author   zhengxin
 * <p/>修改：$Author: zhengx $   
=============================================== 
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>客户其他贷款明细</title>
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
													
											</TABLE><font size="4">客户贷款明细清单</font>
												<div class=caption align=center>
													
													<font class="title">有效日期：<%=vp.getMoeltitle()%></font>
												</div>
													<div align=center>
														<table cellSpacing=0 cellPadding=0 width=100% border=0>
															
															<tr class=title>
																<td class=title align="left">
																	客户名称：<%=ClientName%>																</td>
																<td align="center">
																
																</td>
																<td align=right class=title>
																	单位：万元
																</td>
															</tr>
														</table>
													</div>
													<table class=table cellSpacing=1 cellPadding=1 width=100% border=0 align=center>
														<TBODY>
															  <tr class="head" valign="bottom" align="center">
															    <td>业务号</td>									
															    <td>借据号</td>
															    <td>网点号</td>
															    <td>贷款用途</td>
															    <td>结欠余额</td>
															    <td>期限</td>
															    <td>利率</td>
															    <td>发放日</td>
															    <td>到期日</td>
															    <td>四级形态</td>
															    <td>五级分类</td>
															    <td>第一责任人</td>
															  </tr>
															  <%
															  for(int i=0;i<list.size();i++)
															  {
															  	SecondTr tr =(SecondTr)list.get(i);
															  	%>
															  	<tr class=data bgcolor=#FFFFFF onclick="showLoanInfo('<%=tr.getBmno()%>');" onmouseout="mOut(this);" onmouseover="mOvr(this);">
															  	<td ><%=tr.getBmno() %></td>
															  	<td ><%=tr.getCnlno() %></td>
															  	<td><%=tr.getBrhid() %></td>
															  	<td align="center"><%=tr.getLoancat3() %></td>
															  	<td align=right><%=df.format(tr.getNowbal()) %></td>
															  	<td align=right><%=tr.getPerimon() %></td>
															  	<td align=right><%=tr.getCrteate() %></td>
															  	<td align=right><%=tr.getPaydate() %></td>
															  	<td align=right><%=tr.getEnddate() %></td>
															  	<td align=center><%=tr.getLoancat2() %></td>
															  	<td align=center><%=tr.getLoancat1() %></td>
															  	<td ><%=tr.getFirstresp() %></td>
															  	</tr>
															  	<% 
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
																		</td>
																		<td align=right>
																			&nbsp;
																		</td>
																		<td align=right>
																			<%=df.format(vp.getSumB()[0]) %>
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
					
															<td align=right>
															<br>
																<input type="button" class="button" value=" 关 闭 "onclick="self.close()">
																
																&nbsp;
															</td>
														</tr>
													</table>

												
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
