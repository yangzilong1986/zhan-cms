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
	String clientno =(String)request.getParameter("clientno").trim();
	String brhid=(String)request.getParameter("brhid").trim();
	String date=(String)request.getParameter("date").trim();
	String bal=(String)request.getParameter("bal").trim();
	String clientname =(String)request.getParameter("clientname").trim();
	ClientInfo vo = vp.getClientInfo(clientno,brhid,date,bal);
	DecimalFormat df=new DecimalFormat("###,###,###,##0.0000");
	DecimalFormat df2=new DecimalFormat("###,###,###,##0.00");
	String bad = (String)request.getParameter("types");
	
 %>
  <%--
=============================================== 
Title:客户相关信息
 * @version  $Revision: 1.4 $  $Date: 2007/05/23 06:56:27 $
 * @author   zhengxin
 * <p/>修改：$Author: zhengx $   
=============================================== 
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=clientname %>客户相关信息</title>
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
													
											</TABLE>
												<div class=caption align=center>
													<font class="title"><%=clientname %>客户相关信息</font>
												</div>
												<div class=caption align="right">
													<font class="title">单位：万元</font>
												</div>
													<table class=table cellSpacing=1 cellPadding=1 width=100% border=0 align=center>
													
														  <tr >
														    <td align="right" class="head">所在地：</td>
														    <td  align="left" class="data"  bgcolor=#FFFFFF><%=vo.getDizhi() %></td>
														    <td align="right" class="head">不良贷款较年初：</td>
														    <td align="left" class="data"  bgcolor=#FFFFFF><%=df2.format(Double.valueOf(vo.getBY())) %></td>
														  </tr>
														  <tr>
														    <td align="right" class="head">行业：</td>
														    <td  align="left" class="data"  bgcolor=#FFFFFF><%=vo.getHangye() %></td>
														    <td align="right" class="head">不良贷款较期初：</td>
														    <td class="data"  bgcolor=#FFFFFF align="left"><%=df2.format(Double.valueOf(vo.getBM())) %></td>
														  </tr>
														  <tr>
														    <td align="right" class="head">企业性质：</td>
														    <td class="data"  bgcolor=#FFFFFF align="left"><%=vo.getXingzhi() %></td>
														    <td  class="head" align="right">占全部贷款比例：</td>
														    <td class="data"  bgcolor=#FFFFFF align="left"> <%=df.format(Double.valueOf(vo.getBA().trim())) %></td>
														  </tr>
													<%
													if(bad.trim().equals("one"))
													{
													 %>
														<tr>
														 
														    <td  class="head" align="right" colspan="3">占全部不良贷款比率：</td>
														    <td class="data"  bgcolor=#FFFFFF align="left"> <%=df.format(Double.valueOf(vo.getBad().trim())) %></td>
														  </tr>
													<%} %>
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
