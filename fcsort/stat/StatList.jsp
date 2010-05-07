<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="zt.cms.fcsort.common.FcsortUtil" %>
<%@ page import="zt.cms.fcsort.stat.*" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/stat/";
	FcsortUtil pageutil = new FcsortUtil();
	RataPageObject vp  = new RataPageObject();
	vp.setRequest(request);
	DecimalFormat df1=new DecimalFormat("###,###,###,##0.00");
	DecimalFormat df2=new DecimalFormat("###########0");
	
 %>
<%--
=============================================== 
Title:五级分类五级分类按科目统计
 * @version  $Revision: 1.2 $  $Date: 2007/05/31 03:13:57 $
 * @author   zhengxin
 * <p/>修改：$Author: zhengx $   
=============================================== 
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.linar.jintegra.Param"%>
<html>
	<head>
		<title>五级分类按科目统计表</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<link rel="stylesheet" type="text/css" href="<%=basePath%>fcsort/common/web.css">
		<script language="javascript" src="<%=basePath%>js/meizzMonth.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/ajax.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/common.js"></script>
		<script language="javascript" src="<%=basePath%>js/main.js"></script>
		<script language="javascript" src="Stat.js"></script>
		<script type="text/javascript">
			var basePath="<%=basePath %>";
			var currentPath ="<%=currentPath %>";
			function getCategory()
			{
				if (document.all.Layer1.style.display=='none')
				{
					document.all.Layer1.style.display='';
				}
				else
				{
					document.all.Layer1.style.display='none';
					var s=new Array();
					var list = new Array();
					var acclist = document.all.ACCLIST;
					for (var i=0;i<acclist.options.length;i++)
					{
						if (acclist.options[i].selected==true)
						s.push(acclist.options[i].value.Trim());
						acclist.options[i].selected=false;
					}
					document.all.listfrom.arracclist.value=s.join(',');
				}
			}
			function backStartList()
			{
				var QUERYID = document.all.QUERYID.value;
				var url = currentPath + "StatList.jsp?QUERYID=" + QUERYID;
				window.location = url;
			}
		</script>
		<style type="text/css">
		<!--
		#Layer1 {
			position:absolute;
			width:250px;
			height:373px;
			z-index:1;
			left: 480px;
			top: 80px;
		}
		-->
		</style>
	</head>
	<body background="/images/checks_02.jpg">
		<form action="<%=currentPath%>StatList.jsp" name="listfrom">
		<input name="arracclist" id="arracclist" type="hidden" value="<%=vp.getKumulist() %>">
		<div id="Layer1" style="display:none" align="center" >
		<select id="ACCLIST" name="ACCLIST"  multiple="multiple" size="30">
		<option value="">请选择</option>
		<%=pageutil.getAccList() %>
		</select>
		</div>
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
																		&nbsp; 网点名称：<%=pageutil.getBrhids("brhid","brhid",vp.getBrhid()) %>
																		&nbsp; 查询时点：
																		<%=pageutil.setcreadate("creadate","creadate",vp.getCreadate()) %>
																		&nbsp;
																		<input class="button" name="btnSelCategory" value="选择科目" type="button" onclick="getCategory();">
																		
																
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
													
													<font > 五级分类按科目统计表          </font>
												</div> 统计日期：<%=vp.getCreadate() %>
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
															  <tr class="head" align="center" valign="bottom">
															    <td  rowspan="2">科目号 </td>
															    <td  rowspan="2">科目名称 </td>
															    <td    rowspan="2">笔数合计 </td>
															    <td  rowspan="2">贷款合计 </td>
															    <td    colspan="3">正常 </td>
															    <td   colspan="4">不良 </td>
															  </tr>
															  <tr class="head" align="center" valign="bottom">
															    <td   >合计 </td>
															    <td   >正常 </td>
															    <td   >关注</td>
															    <td   >合计 </td>
															    <td  >次级</td>
															    <td   >可疑 </td>
															    <td   >损失 </td>
															  </tr>
															<%
															List list = vp.getStatByKemuList();
															for(int i=0;i<list.size();i++)
															{
																FirstTr tr = (FirstTr)list.get(i);
																%>
																<tr bgcolor=#FFFFFF  class=data>
    															<td class=data >
																	<%=tr.getAccno() %>
																	 
																</td>
																<td class=data >
																	<%=tr.getAccname() %>
																	 
																</td>
																<td class=data align="right" >
																	<%=df2.format(tr.getCount()) %>
																	 
																</td>
																<td class=data align="right">
																	
																	<%=df1.format(tr.getBal()) %>
																	 
																</td>
																<td class=data align="right">
																	
																	<%=df1.format(tr.getBala()) %>
																	 
																</td>
																<td class=data align="right">
																	<%=df1.format(tr.getH1()) %>
																	 
																</td>
																<td class=data align="right">
																	<%=df1.format(tr.getH2()) %>
																	 
																</td>
																<td class=data align="right">
																	<%=df1.format(tr.getBalb()) %>
																	 
																</td>
																<td class=data align="right">
																	<%=df1.format(tr.getH3()) %>
																	 
																</td>
																<td class=data align="right">
																	<%=df1.format(tr.getH4()) %>
																	
																</td>
																<td class=data align="right">
																	<%=df1.format(tr.getH5()) %>
																	
																</td>
																</tr>
																<% 
															}
															 %>
															 <%
															 for(int i=0;i<10-list.size();i++)
															 {
															 	%>
															 	 <tr class="data" bgcolor=#FFFFFF align="center">
															 	 <%
															 	 for(int j=0;j<11;j++)
															 	 {
															 	 	%>
																	<td>&nbsp;</td>
															<%
															 	 }
															 	  %>
															 	 </tr>
															 	<% 
															 }
															  %>
															 <tr class="head" align="center">
    															<td   >
																	
																	合计 
																</td>
																<td   >
																	
																	 
																</td>
																<td   align="right" >
																	<%=df2.format(vp.getSun2()) %>
																	 
																</td>
																<td   align="right">
																	
																	<%=df1.format(vp.getSun1()) %>
																	 
																</td>
																<td   align="right">
																	
																	<%=df1.format(vp.getSun3()) %>
																	 
																</td>
																<td   align="right">
																	<%=df1.format(vp.getSun4()) %>
																	 
																</td>
																<td   align="right">
																	<%=df1.format(vp.getSun5()) %>
																	 
																</td>
																<td   align="right">
																	<%=df1.format(vp.getSun6()) %>
																	 
																</td>
																<td   align="right">
																	<%=df1.format(vp.getSun7()) %>
																	 
																</td>
																<td   align="right">
																	<%=df1.format(vp.getSun8()) %>
																	
																</td>
																<td align="right">
																	<%=df1.format(vp.getSun9()) %>
																	
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
