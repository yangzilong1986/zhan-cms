<%@ page language="java" pageEncoding="GB2312"%>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.fcsort.fcnbloan.FcnbloanPageobject" %>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.cms.fcsort.fcnbloan.FirstnbLoan" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil"%>
<%--
===============================================
Title: 新增不良贷款查询2级页面
Description: 新增不良贷款查询2级页面.
 * @version   $Revision: 1.16 $  $Date: 2007/05/29 06:52:31 $
 * @author   houcs
 * <p/>修改：$Author: houcs $
===============================================
--%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/fc_nblquery/";
	
	String brhid = request.getParameter("brhid")==null? "0":request.getParameter("brhid");
	ConnectionManager manager = ConnectionManager.getInstance();
	String sql1="select enutp,enudt,enuid from PTENUMINFODETL where enuid='LoanWay' " +
	"or enuid='BadReason' or enuid='LoanType2' or enuid='LoanType3'";
    String sql2="select distinct(clientmgr) clientmgr,username from SCUSER s,RQLOANLIST r where r.brhid='"+brhid+"'" +
	    		" and s.brhid=r.brhid and r.clientmgr=s.loginname";
    CachedRowSet crs1=  manager.getRs(sql1);
	CachedRowSet crs2=  manager.getRs(sql2);
	String strScbrhName = SCBranch.getSName(brhid);//网点名称
	String create=request.getParameter("creadate")==null? "0":request.getParameter("creadate");
	String yearday=request.getParameter("yearday")==null ? "0" : request.getParameter("yearday");
	String reason=request.getParameter("reason")==null ? "0" : request.getParameter("reason");
	String loanday=request.getParameter("loanday")==null ? "0" : request.getParameter("loanday");
	String method=request.getParameter("method")==null ? "0" : request.getParameter("method");
	String fenlei=request.getParameter("fenlei")==null ? "0" : request.getParameter("fenlei");
	String managers=request.getParameter("managers")==null ? "0" : request.getParameter("managers");
    String startdate=request.getParameter("startdate")==null? "":request.getParameter("startdate");
	String enddate=request.getParameter("enddate")==null? "":request.getParameter("enddate");
	String sartbal=request.getParameter("sartbal")==null? "":request.getParameter("sartbal");
	String balend=request.getParameter("endbal")==null? "":request.getParameter("endbal");
	String btype=request.getParameter("btype")==null?"":request.getParameter("btype");
	String upbrhid=request.getParameter("upbrhid")==null?"":request.getParameter("upbrhid");
	String colspan="7";
	FcnbloanPageobject vp= new FcnbloanPageobject(request);
	vp.setBrhid(brhid); //页面状态
	vp.setCreadate(create);
	vp.setYearday(yearday);
	vp.setStartdate(startdate);
	vp.setEnddate(enddate);
	vp.setSartbal(sartbal);
	vp.setEndbal(balend);
	String params="&startdate="+startdate+"&enddate="
	+enddate+"&creadate="+create
	+"&sartbal="+sartbal+"&endbal="
	+balend+"&yearday="+yearday+"&managers="
	+managers+"&fenlei="+fenlei+"&method="
	+method+"&loanday="+loanday+"&reason="+reason;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>新增不良贷款查询</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<link rel="stylesheet" type="text/css" href="web.css">
		<script language="javascript" src="ajax.js"></script>
		<script type="text/javascript" src="fcsort.js"></script>
		<script type="text/javascript">
		var create="<%=create%>";
		var surl ="<%=currentPath%>";
		var params="<%=params%>";
		var yeardays="<%=yearday%>";
		var startdate="<%=startdate%>";
		var enddate="<%=enddate%>";
		var brhid="<%=brhid%>";
		var thirdbrhid="<%=brhid%>";
		var colspan="<%=colspan%>";
		var cou="";
		var upbrhid="<%=upbrhid%>";
		function reset(){
   document.listform.reason.value="0";
   document.listform.loanday.value="0";
   document.listform.method.value="0";
   document.listform.fenlei.value="0";
   document.listform.managers.value="0";
   
   }
   function upLevel(){
   var url= surl+"list.jsp?brhid="+upbrhid+params;
   window.location=url;
   }
		</script>

	</head>

	<body background="/images/checks_02.jpg" >
	<form action="<%=currentPath%>/secondList.jsp" name="listform">
	<input name="referValue" type="hidden">
	<input type="hidden" name="brhid" value="<%=brhid%>">
	<input type="hidden" name="creadate" value="<%=create%>">
	<input type="hidden" name="yearday" value="<%=yearday%>">
	<input type="hidden" name="startdate" value="<%=startdate%>">
	<input type="hidden" name="enddate" value="<%=enddate%>">
	<input type="hidden" name="sartbal" value="<%=sartbal%>">
	<input type="hidden" name="endbal" value="<%=balend%>">
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
															<input type="button" value=" 检 索 " class="button"onclick="document.listform.submit();">
															&nbsp;&nbsp;
															<input type="button" value=" 重 置 " class="button"onclick="reset();">
															&nbsp;&nbsp;
															<input type=button class=button value=" 打 印 "onclick="printTable()">
															&nbsp;&nbsp;
															<input type="button" class="button" value=" 关 闭 "onclick="self.close()">
															&nbsp;

														</div>
														<table class="table" cellSpacing=1 cellPadding=1 width=100% border=0>

															<tr class=head>
																<br>
																<td>
																	<input type="hidden" name="show" value="true">
															   形成原因：<select id="reason" name="reason">
															     <option value="0">全部</option>
															   <%
															   					   		   while(crs1.next()){
															   					   		   if(crs1.getString("enuid").trim().equals("BadReason")){
															   %>
                                                                    <option value="<%=crs1.getString("enutp")%>"><%=DBUtil.fromDB(crs1.getString("enudt"))%>
																	</option>
																   <%
																   						   			   }
																   						   			   }
																   %>
														 </select>
																贷款期限：
																<select id="loanday" name="loanday">
																<option value="0">全部</option>
																 <%
																 						 			    crs1.beforeFirst();
																 						 			    while(crs1.next()){
																 						 		   if(crs1.getString("enuid").trim().equals("LoanType2")){
																 %>
                                                                    <option value="<%=crs1.getString("enutp")%>"><%=DBUtil.fromDB(crs1.getString("enudt"))%>
																	</option>
																   <%
																   						   			   }
																   						   			   }
																   %>
																
																</select>	
																担保方式：
																<select id="method" name="method">
																<option value="0">全部</option>
																 <%
																 						 			   crs1.beforeFirst();
																 						 			   while(crs1.next()){
																 						 		   if(crs1.getString("enuid").trim().equals("LoanType3")){
																 %>
                                                                    <option value="<%=crs1.getString("enutp")%>"><%=DBUtil.fromDB(crs1.getString("enudt"))%>
																	</option>
																   <%
																   						   			   }
																   						   			   }
																   %>
																</select>	
																贷款投向：
																<select id="fenlei" name="fenlei">
																<option value="0">全部</option>
																 <%
																 						 			   crs1.beforeFirst();
																 						 			   while(crs1.next()){
																 						 		   if(crs1.getString("enuid").trim().equals("LoanWay")){
																 %>
                                                                    <option value="<%=crs1.getString("enutp")%>"><%=DBUtil.fromDB(crs1.getString("enudt"))%>
																	</option>
																	
																   <%
																																	   						   if(Integer.valueOf(crs1.getString("enutp")).intValue()==199)
																																	   						   {
																																	   %><option value="allOne">企事业单位-全部
																	</option><%
																																	   						    }
																																	   						    if(Integer.valueOf(crs1.getString("enutp")).intValue()==299)
																																	   						   {
																																	   %>
																   	<option value="allTwo">自然人一般-全部
																	</option>
																   	<%
																   							    }
																   							    if(Integer.valueOf(crs1.getString("enutp")).intValue()==399)
																   							   {
																   	%>
																   	<option value="allThree">自然人其他-全部
																	</option><%
																   							   			   }
																   							   			   }
																   							   			   }
																   	%>
																</select>
																
																客户经理：
																<select id="managers" name="managers">
																<option value="0">全部</option>
															      <%
															      while(crs2.next()){
															      %>
                                                               <option value="<%=crs2.getString("clientmgr")%>"><%=DBUtil.fromDB(crs2.getString("username"))%>
																	</option>
																   <%
																   }
																   %>
																</select>				
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
											</TABLE>
											<font size="4">
											<%
											if(yearday.equals("2")){
											%>
											本月新增不良贷款统计表
											<%
											}else{
											%>
											本年新增不良贷款统计表
											<%
											}
											%>
											</font>
											<div id='showTable' align=center width=100%>
									
												<div class=caption align=center>
												<table>
												<tr>
												<td>
                                               <font class="title">贷款发放日：
                                               <%
                                                                                                                                             if(!startdate.equals("") && !enddate.equals(""))
                                                                                                                                             {
                                               %>
                                               <%=startdate%>&nbsp;至&nbsp;<%=enddate%>
                                               <%
                                                                                                                                             }
                                                                                                                                             else if(!startdate.equals("") && enddate.equals("")){
                                               %>
													<%=startdate%>&nbsp;以后
													<%
																			}
																			else if(startdate.equals("") && !enddate.equals("")){
													%>
													<%=enddate%>&nbsp;以前
													<%
													}else{
													%>
													全时间段 
													<%
													}
													%>
													
													</font>
													</td>
													</tr>
													</table>
												</div>
												<div align=center>
													<table cellSpacing=0 cellPadding=0 width=100% border=0>
													<tr class=title align="center">
															
															<td align=center class=title >
																
															</td>
															
														</tr>
														<tr class=title>
															<td  class=title>
																网点名称：<%=strScbrhName%>
															</td>
									
															<td align=right  class=title>
																单位：万元
															</td>
														</tr>
													</table>
												</div>
												<table class=table cellSpacing=1 cellPadding=1 width=100% border=0 align=center id="checkTable">
													<TBODY>
														<tr>
															<td  rowspan=2 valign="bottom"class="head">
															类别
															</td>
															<td rowspan=2 align="center" valign="bottom"class="head">
															新增贷款笔数
															</td>
															<td rowspan=2 align="center" valign="bottom"class="head">
															新增贷款户数
															</td>
															<td rowspan=2 align="center" valign="bottom"class="head">
															合同金额
															</td>
															<td  rowspan=2 align="center" valign="bottom"class="head">
															结欠余额
															</td>
															<td colspan="3" align="center" valign="bottom"class="head">
																其中
															</td>
														</tr>
														<tr>
															<td align="center" valign="bottom" class="head">
																次级
															</td>
															<td align="center" valign="bottom" class="head">
																可疑
															</td>
															<td align="center" valign="bottom" class="head">
																损失
															</td>
														</tr>
														<%
																						  FirstnbLoan vo1=vp.get1LRs();
																						  if(btype.endsWith("1") || btype.endsWith("2")){
														%>
														<tr bgcolor=#FFFFFF class=data>
														<%
														}else{
														%>
														 <tr bgcolor=#FFFFFF onclick="tothirdlist('1');" onmouseout="mOut(this);" onmouseover="mOvr(this);"class=data>
														 <%
														 }
														 %>
    															<td class=data align="left" >
    															往年发放往年到期
																</td>
																<td class=data align="right">
																		<%=vo1.getNewloansum()==null ? "0" : vo1.getNewloansum()%>
																</td>
																<td class=data align="right">
																		<%=vo1.getNewpersonsum() ==null ? "0":vo1.getNewpersonsum()%>
																</td>
																<td class=data align="right">
																		<%=vo1.getLoanbal() ==null ? "0.00" :vo1.getLoanbal()%>
																</td>
																<td class=data align="right">
																		<%=vo1.getBal()==null? "0.00" : vo1.getBal()%>
																</td>
																<td class=data align="right">
																		<%=vo1.getH3()==null ? "0.00" :vo1.getH3()%>
																</td>
																<td class=data align="right">
																		<%=vo1.getH4()==null ? "0.00" : vo1.getH4()%>
																</td>
																<td class=data align="right">
																		<%=vo1.getH5()==null ?"0.00": vo1.getH5()%>
																</td>

															</tr>
															<%
																						 FirstnbLoan vo2=vp.get2LRs();
																						 if(btype.endsWith("1") || btype.endsWith("2")){
															%>
														<tr bgcolor=#FFFFFF class=data>
														<%
														}else{
														%>
														 <tr bgcolor=#FFFFFF onclick="tothirdlist('2');" onmouseout="mOut(this);" onmouseover="mOvr(this);"class=data>
														 <%
														 }
														 %>
														 <td class=data align="left" >
    															往年发放当年到期
																</td>
																<td class=data align="right">
																		<%=vo2.getNewloansum()==null ? "0" : vo2.getNewloansum()%>
																</td>
																<td class=data align="right">
																		<%=vo2.getNewpersonsum()==null ? "0":vo2.getNewpersonsum()%>
																</td>
																<td class=data align="right">
																		<%=vo2.getLoanbal()==null ? "0.00" :vo2.getLoanbal()%>
																</td>
																<td class=data align="right">
																		<%=vo2.getBal()==null? "0.00" : vo2.getBal()%>
																</td>
																<td class=data align="right">
																		<%=vo2.getH3()==null ? "0.00" :vo2.getH3()%>
																</td>
																<td class=data align="right">
																		<%=vo2.getH4()==null ? "0.00" : vo2.getH4()%>
																</td>
																<td class=data align="right">
																		<%=vo2.getH5() ==null ?"0.00": vo2.getH5()%>
																</td>

															</tr>
																<%
																								  FirstnbLoan vo8=vp.get8LRs();
																								  if(btype.endsWith("1") || btype.endsWith("2")){
																%>
														<tr bgcolor=#FFFFFF class=data>
														<%
														}else{
														%>
														 <tr bgcolor=#FFFFFF onclick="tothirdlist('5');" onmouseout="mOut(this);" onmouseover="mOvr(this);"class=data>
														 <%
														 }
														 %>
    															<td class=data align="left" >
    															往年发放当年后到期
																</td>
																<td class=data align="right">
																		<%=vo8.getNewloansum()==null ? "0" : vo8.getNewloansum()%>
																</td>
																<td class=data align="right">
																		<%=vo8.getNewpersonsum()==null ? "0":vo8.getNewpersonsum()%>
																</td>
																<td class=data align="right">
																		<%=vo8.getLoanbal()==null ? "0.00" :vo8.getLoanbal()%>
																</td>
																<td class=data align="right">
																		<%=vo8.getBal()==null? "0.00" : vo8.getBal()%>
																</td>
																<td class=data align="right">
																		<%=vo8.getH3()==null ? "0.00" :vo8.getH3()%>
																</td>
																<td class=data align="right">
																		<%=vo8.getH4()==null ? "0.00" : vo8.getH4()%>
																</td>
																<td class=data align="right">
																		<%=vo8.getH5() ==null ?"0.00": vo8.getH5()%>
																</td>

															</tr>
															<%
																						 FirstnbLoan vo3=vp.get5LRs();
																						 if(btype.endsWith("1") || btype.endsWith("2")){
															%>
														<tr bgcolor=#E7E9CF class=data>
														<%
														}else{
														%>
														 <tr bgcolor=#E7E9CF onclick="tothirdlist('8');" onmouseover="mOvr(this);" class=data>
														 <%
														 }
														 %>
    															<td class=data align="left" >
    															往年发放形成不良合计
																</td>
																<td class=data align="right">
																		<%=vo3.getNewloansum()==null ? "0" : vo3.getNewloansum()%>
																</td>
																<td class=data align="right">
																		<%=vo3.getNewpersonsum()==null ? "0":vo3.getNewpersonsum()%>
																</td>
																<td class=data align="right">
																		<%=vo3.getLoanbal() ==null ? "0.00" :vo3.getLoanbal()%>
																</td>
																<td class=data align="right">
																		<%=vo3.getBal() ==null? "0.00" : vo3.getBal()%>
																</td>
																<td class=data align="right">
																		<%=vo3.getH3() ==null ? "0.00" :vo3.getH3()%>
																</td>
																<td class=data align="right">
																		<%=vo3.getH4()==null ? "0.00" : vo3.getH4()%>
																</td>
																<td class=data align="right">
																		<%=vo3.getH5()  ==null ?"0.00": vo3.getH5()%>
																</td>

															</tr>
															<%
																						  FirstnbLoan vo4=vp.get3LRs();
																						  if(btype.endsWith("1") || btype.endsWith("2")){
															%>
														<tr bgcolor=#FFFFFF class=data>
														<%
														}else{
														%>
														 <tr bgcolor=#FFFFFF onclick="tothirdlist('3');" onmouseout="mOut(this);" onmouseover="mOvr(this);"class=data>
														 <%
														 }
														 %>
    															<td class=data align="left" >
    															当年发放当年往期到期
																</td>
																<td class=data align="right">
																		<%=vo4.getNewloansum()==null ? "0" : vo4.getNewloansum()%>
																</td>
																<td class=data align="right">
																		<%=vo4.getNewpersonsum()==null ? "0":vo4.getNewpersonsum()%>
																</td>
																<td class=data align="right">
																		<%=vo4.getLoanbal() ==null ? "0.00" :vo4.getLoanbal()%>
																</td>
																<td class=data align="right">
																		<%=vo4.getBal()==null? "0.00" : vo4.getBal()%>
																</td>
																<td class=data align="right">
																		<%=vo4.getH3()==null ? "0.00" :vo4.getH3()%>
																</td>
																<td class=data align="right">
																		<%=vo4.getH4()==null ? "0.00" : vo4.getH4()%>
																</td>
																<td class=data align="right">
																		<%=vo4.getH5() ==null ?"0.00": vo4.getH5()%>
																</td>

															</tr>
															<%
																						 FirstnbLoan vo5=vp.get4LRs();
																						 if(btype.endsWith("1") || btype.endsWith("2")){
															%>
														<tr bgcolor=#FFFFFF class=data>
														<%
														}else{
														%>
														 <tr bgcolor=#FFFFFF onclick="tothirdlist('4');" onmouseout="mOut(this);" onmouseover="mOvr(this);"class=data>
														 <%
														 }
														 %>
    															<td class=data align="left" >
    															当年发放本期到期
																</td>
																<td class=data align="right">
																		<%=vo5.getNewloansum() ==null ? "0" : vo5.getNewloansum()%>
																</td>
																<td class=data align="right">
																		<%=vo5.getNewpersonsum() ==null ? "0":vo5.getNewpersonsum()%>
																</td>
																<td class=data align="right">
																		<%=vo5.getLoanbal()==null ? "0.00" :vo5.getLoanbal()%>
																</td>
																<td class=data align="right">
																		<%=vo5.getBal()==null? "0.00" : vo5.getBal()%>
																</td>
																<td class=data align="right">
																		<%=vo5.getH3() ==null ? "0.00" :vo5.getH3()%>
																</td>
																<td class=data align="right">
																		<%=vo5.getH4()==null ? "0.00" : vo5.getH4()%>
																</td>
																<td class=data align="right">
																		<%=vo5.getH5() ==null ?"0.00": vo5.getH5()%>
																</td>
															</tr>
																<%
																								  FirstnbLoan vo9=vp.get9LRs();
																								  if(btype.endsWith("1") || btype.endsWith("2")){
																%>
														<tr bgcolor=#FFFFFF class=data>
														<%
														}else{
														%>
														 <tr bgcolor=#FFFFFF onclick="tothirdlist('6');" onmouseout="mOut(this);" onmouseover="mOvr(this);"class=data>
														 <%
														 }
														 %>
    															<td class=data align="left" >
    															当年发放本期后到期
																</td>
																<td class=data align="right">
																		<%=vo9.getNewloansum() ==null ? "0" : vo9.getNewloansum()%>
																</td>
																<td class=data align="right">
																		<%=vo9.getNewpersonsum() ==null ? "0":vo9.getNewpersonsum()%>
																</td>
																<td class=data align="right">
																		<%=vo9.getLoanbal()==null ? "0.00" :vo9.getLoanbal()%>
																</td>
																<td class=data align="right">
																		<%=vo9.getBal()==null? "0.00" : vo9.getBal()%>
																</td>
																<td class=data align="right">
																		<%=vo9.getH3() ==null ? "0.00" :vo9.getH3()%>
																</td>
																<td class=data align="right">
																		<%=vo9.getH4()==null ? "0.00" : vo9.getH4()%>
																</td>
																<td class=data align="right">
																		<%=vo9.getH5() ==null ?"0.00": vo9.getH5()%>
																</td>
															</tr>
															<%
																					 		 FirstnbLoan vo6=vp.get6LRs();
																					 		 if(btype.endsWith("1") || btype.endsWith("2")){
															%>
														<tr bgcolor=#E7E9CF class=data>
														<%
														}else{
														%>
														 <tr bgcolor=#E7E9CF onclick="tothirdlist('9');" onmouseover="mOvr(this);" class=data>
														 <%
														 }
														 %>
															
    															<td class=data align="left" >
    															当年发放形成不良合计
																</td>
																<td class=data align="right">
																		<%=vo6.getNewloansum()==null ? "0" : vo6.getNewloansum()%>
																</td>
																<td class=data align="right">
																		<%=vo6.getNewpersonsum() ==null ? "0":vo6.getNewpersonsum()%>
																</td>
																<td class=data align="right">
																		<%=vo6.getLoanbal() ==null ? "0.00" :vo6.getLoanbal()%>
																</td>
																<td class=data align="right">
																		<%=vo6.getBal()==null? "0.00" : vo6.getBal()%>
																</td>
																<td class=data align="right">
																		<%=vo6.getH3()==null ? "0.00" :vo6.getH3()%>
																</td>
																<td class=data align="right">
																		<%=vo6.getH4()==null ? "0.00" : vo6.getH4()%>
																</td>
																<td class=data align="right">
																		<%=vo6.getH5()==null ?"0.00": vo6.getH5()%>
																</td>
															</tr>
															<%
																						 FirstnbLoan vo7=vp.get7LRs();
																						 if(btype.endsWith("1") || btype.endsWith("2")){
															%>
														<tr bgcolor=#E7E9CF class=data>
														<%
														}else{
														 %>
														 <tr bgcolor=#E7E9CF onclick="tothirdlist('7');" onmouseover="mOvr(this);" class=data>
														 <%
														}
														 %>
														 <td class=data align="center" >
    															合计:
																</td>
																<td class=data align="right">
																		<%=vo7.getNewloansum()==null ? "0" : vo7.getNewloansum()%>
																</td>
																<td class=data align="right">
																		<%=vo7.getNewpersonsum()  ==null ? "0":vo7.getNewpersonsum()%>
																</td>
																<td class=data align="right">
																		<%=vo7.getLoanbal() ==null ? "0.00" :vo7.getLoanbal() %>
																</td>
																<td class=data align="right">
																		<%=vo7.getBal()==null? "0.00" : vo7.getBal() %>
																</td>
																<td class=data align="right">
																		<%=vo7.getH3()==null ? "0.00" :vo7.getH3() %>
																</td>
																<td class=data align="right">
																		<%=vo7.getH4()==null ? "0.00" : vo7.getH4() %>
																</td>
																<td class=data align="right">
																		<%=vo7.getH5() ==null ?"0.00": vo7.getH5()%>
																</td>
																</tr>
													</TBODY>
												</TABLE>
												<table cellSpacing=0 cellPadding=0 width=100% border=0 class=head>
													<tr><td align=right>
															<input type='button' value='返回上页'onclick='history.back();' class='button'>
															&nbsp;
															<input type='button' value='返回上级'onclick='upLevel();' class='button'>
															&nbsp;
															<input type='button' value='返回初始页'onclick='back();' class='button'>
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
<script>
document.listform.reason.value='<%=reason.equals("0")?"0":reason%>'
document.listform.loanday.value='<%=loanday.equals("0")?"0":loanday%>'
document.listform.method.value='<%=method.equals("0")?"0":method%>'
document.listform.fenlei.value='<%=fenlei.equals("0")?"0":fenlei%>'
document.listform.managers.value='<%=managers.equals("0")?"0":managers%>'
</script>

