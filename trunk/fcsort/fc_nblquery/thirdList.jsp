<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ page import="zt.cms.fcsort.fcnbloan.FcnblTPageobject" %>
<%@ page import="zt.cms.fcsort.fcnbloan.FcnbloanT" %>
<%@ page import="zt.platform.db.DBUtil"%>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%--
===============================================
Title: �������������ѯ3��ҳ��
Description: �������������ѯ3��ҳ��.
 * @version   $Revision: 1.14 $  $Date: 2007/05/28 09:55:13 $
 * @author   houcs
 * <p/>�޸ģ�$Author: houcs $
===============================================
--%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/fc_nblquery/";
	String brhid = request.getParameter("brhid")==null? "":request.getParameter("brhid");
	String strScbrhName = SCBranch.getSName(brhid);//��������
	String loanFiled=request.getParameter("count");
	ConnectionManager manager = ConnectionManager.getInstance();
	String sql1="select distinct(clientmgr),username from SCUSER s,RQLOANLIST r where r.brhid='"+brhid+"'" +
	    		" and r.clientmgr=s.loginname";
	CachedRowSet crs1=  manager.getRs(sql1);
	String create=request.getParameter("creadate")==null? "":request.getParameter("creadate");
	String startdate=request.getParameter("startdate")==null? "":request.getParameter("startdate");
	String enddate=request.getParameter("enddate")==null? "":request.getParameter("enddate");
	String sartbal=request.getParameter("sartbal")==null? "":request.getParameter("sartbal");
	String balend=request.getParameter("endbal")==null? "0":request.getParameter("endbal");
	String yearday=request.getParameter("yearday")==null ? "0" : request.getParameter("yearday");
	String reason=request.getParameter("reason")==null ? "0" : request.getParameter("reason");
	String loanday=request.getParameter("loanday")==null ? "0" : request.getParameter("loanday");
	String method=request.getParameter("method")==null ? "0" : request.getParameter("method");
	String fenlei=request.getParameter("fenlei")==null ? "0" : request.getParameter("fenlei");
	String managers=request.getParameter("managers")==null ? "0" : request.getParameter("managers");
	String firstPerson=request.getParameter("firstPerson")==null ? "0" : request.getParameter("firstPerson");
	String cou=request.getParameter("count");
	String counts=request.getParameter("count");
	String colspan="11";
	FcnblTPageobject vp = new FcnblTPageobject(request);
	vp.setBrhid(brhid);
	vp.setCreadate(create);
	vp.setYearday(yearday);
	vp.setStartdate(startdate);
	vp.setEnddate(enddate);
	vp.setSartbal(sartbal);
	vp.setEndbal(balend);
	vp.setReason(reason);
	vp.setLoanday(loanday);
	vp.setMethod(method);
	vp.setFenlei(fenlei);
	vp.setManagers(managers);
	vp.setLoanFiled(loanFiled);
	vp.setFirstPerson(firstPerson);
	if(counts !=null && !counts.equals("")){
	if(counts.equals("1")){
	counts="���귢�����굽��";
	}else if(counts.equals("2")){
	counts="���귢�ŵ��굽��";
	}else if(counts.equals("3")){
	counts="���귢�ŵ������ڵ���";
	}else if(counts.equals("4")){
	counts="���귢�ű��ڵ���";
	}else if(counts.equals("5")){
	counts="���귢�ŵ������";
	}else if(counts.equals("6")){
	counts="���귢�ű��ں���";
	}
	else if(counts.equals("7")){
	counts="�ϼƽ����ϸ";
	}
	else if(counts.equals("8")){
	counts="���귢�źϼƽ����ϸ";
	}
	else if(counts.equals("9")){
	counts="���귢�źϼƽ����ϸ";
	}
	}
	String params="&startdate="+
	startdate+"&enddate="+enddate+"&creadate="
	+create+"&sartbal="+sartbal+"&endbal="
	+balend+"&count="+cou+"&yearday="+yearday
	+"&fenlei="+fenlei+"&method="
	+method+"&loanday="+loanday+"&reason="+reason
	+"&loanFiled="+loanFiled;	
	//****************************��ҳ���****************************
	vp.setPagecount();//��������¼��
	vp.setMaxpage();//�������ҳ��
	int pagecount = vp.getPagecount();//ҳ���¼��
	int pagesize = vp.getPagesize();// ҳ���С
	int maxpage = vp.getMaxpage();// ���ҳ
	int currpage = vp.getCurrpage();// ��ǰҳ
	String updisabled =currpage==1?"disabled":"";
	String downdisabled =currpage==maxpage?"disabled":"";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>�������������ѯ</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
        <script type="text/javascript" src="fcsort.js"></script>
		<link rel="stylesheet" type="text/css" href="web.css">
<script type="text/javascript">
        var surl ="<%=currentPath%>";
        var yeardays="<%=yearday%>";
		var startdate="<%=startdate%>";
		var enddate="<%=enddate%>";
		var brhid="<%=brhid%>";
		var create="<%=create%>";
		var colspan="<%=colspan%>";
		var cou="<%=cou%>";
		var params="<%=params%>";
function sub(){
      var managers=document.listform.managers.value;
      var firstPerson=document.listform.firstPerson.value;
      var url=surl+"thirdList.jsp?brhid="+brhid+params+"&managers="+managers
      +"&firstPerson="+firstPerson;
     window.location=url;
}
function mOvr(src){
	if (!src.contains(event.fromElement)) {
	dataBgColor=src.bgColor;
	src.style.cursor = 'hand';
	src.bgColor = '#E7E9CF';
	}
}
function mOut(src){
	if (!src.contains(event.toElement)) {
	src.style.cursor = 'default';
	src.bgColor = '#FFFFFF';
	}
}
function goPage(sort) {

			var currpage=document.all.currpage.value;
			var maxpage=document.all.maxpage.value;
			var pageer =document.all.pageer.value;	
			if (sort == "top") {
				document.all.currpage.value = 1;
			}
			if (sort == "bottom") {
				document.all.currpage.value = maxpage;
			}
			if (sort == "up") {
				document.all.currpage.value = parseInt(parseInt(currpage) - 1);
			}
			if (sort == "down") {
				document.all.currpage.value = parseInt(parseInt(currpage) + 1);
			}
			if(currpage>maxpage||currpage=="")
			{
				window.alert("��������ȷ��ҳ�룡");
				document.all.currpage.value =pageer ;
				return false;
			}
			else
			{
				document.all.listform.submit();
			}
			
		}
	function showLoanInfo(bmno)
	{
			var url = "<%=currentPath%>loanInfo.jsp?bmno="+bmno;
			var st="status=yes,toolbar=yes,menubar=no,location=no,scrollbars=yes,resizable=yes";
		window.open(url,"123",st);
			
	}
   function reset(){
   document.listform.managers.value="0";
   document.listform.firstPerson.value="0";
   }
   function upLevel(){
   var url= surl+"secondList.jsp?brhid="+brhid+params;
   window.location=url;
   }
</script>

	</head>

	<body background="/images/checks_02.jpg">
	<form action="<%=currentPath%>/thirdList.jsp" name="listform">
	<input name="referValue" type="hidden">
	   <input type="hidden" name="cou" value="<%=cou%>"> 
	   <input type="hidden" name="brhid" value="<%=brhid%>">
	<input type="hidden" name="creadate" value="<%=create%>">
	<input type="hidden" name="yearday" value="<%=yearday%>">
	<input type="hidden" name="startdate" value="<%=startdate%>">
	<input type="hidden" name="enddate" value="<%=enddate%>">
	<input type="hidden" name="sartbal" value="<%=sartbal%>">
	<input type="hidden" name="endbal" value="<%=balend%>">
	<input type="hidden" name="yearday" value="<%=yearday%>">
	<input type="hidden" name="reason" value="<%=reason%>">
	<input type="hidden" name="loanday" value="<%=loanday%>">
	<input type="hidden" name="method" value="<%=method%>">
	<input type="hidden" name="fenlei" value="<%=fenlei%>">
	<input type="hidden" name="count" value="<%=loanFiled%>">
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
															<input type="button" value=" �� �� " class="button"onclick="sub();">
															&nbsp;&nbsp;
															<input type="button" value=" �� �� " class="button"onclick="reset();">
															&nbsp;&nbsp;
															<input type=button class=button value=" �� ӡ "onclick="printTable()">
															&nbsp;&nbsp;
															<input type="button" class="button" value=" �� �� "onclick="self.close()">
															&nbsp;

														</div>
														<br>
														<table class="table" cellSpacing=1 cellPadding=1 width=100% border=0>
														 <tr class=head>
														    <td  valign="bottom">
														   �ͻ�����:<select id="managers" name="managers">
														   <option value="0">ȫ��</option>
														      <%
															   while(crs1.next()){
																	   %>
                                                               <option value="<%=crs1.getString("clientmgr")%>"><%=DBUtil.fromDB(crs1.getString("username"))%>
																	</option>
																   <%
																  
															   }
															   
															   %>
														   </select>
														    
														   ��һ������:<select id="firstPerson" name="firstPerson">
														   <option value="0">ȫ��</option>
														      <%
														       crs1.beforeFirst();
															   while(crs1.next()){
																	   %>
                                                               <option value="<%=crs1.getString("clientmgr")%>"><%=DBUtil.fromDB(crs1.getString("username"))%>
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
											����������������ͳ�Ʊ�
											<%
											 }else{
											 %>
											����������������ͳ�Ʊ�
											<%
											 }
											 %>
											</font>
											<div id='showTable' align=center width=100%>
									
												<div class=caption align=center>
												<table>
												<tr>
												<td>
                                               <font class="title">������գ�
                                               <%if(!startdate.equals("") && !enddate.equals(""))
                                               { 
                                               %>
                                               <%=startdate %>&nbsp;��&nbsp;<%=enddate %>
                                               <%
                                               }
                                               else if(!startdate.equals("") && enddate.equals("")){%>
													<%=startdate%>&nbsp;�Ժ�
													<% 
													}
													else if(startdate.equals("") && !enddate.equals("")){
													%>
													<%=enddate%>&nbsp;��ǰ
													<% 
													}else{
													%>
													ȫʱ��� 
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
																��������:<%=strScbrhName %>
																&nbsp;
																���:<%=counts %>
															</td>
									
															<td align=right  class=title>
																��λ��Ԫ
															</td>
														</tr>
													</table>
												</div>
												<table class=table cellSpacing=1 cellPadding=1 width=100% border=0 align=center id="checkTable">
													<TBODY>
														<tr>
															<td  valign="bottom"class="head">
															���������
															</td>
															<td  align="center" valign="bottom"class="head">
															������;
															</td>
															<td  align="center" valign="bottom"class="head">
															��ͬ���
															</td>
															<td  align="center" valign="bottom"class="head">
															��Ƿ���
															</td>
															<td align="center" valign="bottom"class="head">
															����
															</td>
															<td align="center" valign="bottom"class="head">
															����
															</td>
															<td  align="center" valign="bottom"class="head">
															������
															</td>
															<td align="center" valign="bottom"class="head">
															������
															</td>
															<td align="center" valign="bottom"class="head">
															�ļ���̬
															</td>
															<td align="center" valign="bottom"class="head">
															�弶��̬
															</td>
															<td  align="center" valign="bottom"class="head">
															��һ������
															</td>
															<td  align="center" valign="bottom"class="head">
															�γ�ԭ��
															</td>
														</tr>
													<%
													  List vplist = vp.getListvp();
														double sum1=0.00;
														double sum2=0.00;
														int margin= pagesize-vplist.size();
														for(int i=0;i<vplist.size();i++)
														{
															FcnbloanT vo= (FcnbloanT)vplist.get(i);
															double loanAmount=Double.valueOf(vo.getLoanbal1()).doubleValue();
															double bal=Double.valueOf(vo.getBal1()).doubleValue();
															sum1+=loanAmount;
															sum2+=bal;
													 %>
														
															<tr bgcolor=#FFFFFF onclick="showLoanInfo('<%=vo.getBmno()%>');" onmouseout="mOut(this);" onmouseover="mOvr(this);"class=data>
																<td class=data align="left">
																<%=vo.getClientname()==null?"":vo.getClientname()%>
																</td>
																	<td class=data align="left">
																	
																	<%=vo.getLoanyt()==null?"":vo.getLoanyt() %>
																</td>
																	<td class=data align="right">
																
																	<%=vo.getLoanbal()==null?"0.00":vo.getLoanbal() %>
																	
																</td>
																	<td class=data align="right">
																	
																	<%=vo.getBal()==null?"0.00": vo.getBal()%>
																</td>
																	<td class=data align="center">
																	
																	<%=vo.getEnudate()==null?"": vo.getEnudate()%>
																</td>
																
																<td class=data align="center">
																	
																	<%=vo.getPerimon()==null?"":vo.getPerimon() %>
																</td>
																<td class=data align="center">
																	
																	<%=vo.getPadate()==null?"": vo.getPadate()%>
																</td>
																
																<td class=data align="center">
																	
																	<%=vo.getDuedate() ==null?"":vo.getDuedate()%>
																	
																</td>
																<td class=data align="center">
																	
																	<%=vo.getFourS()==null?"": vo.getFourS()%>
																	
																</td>
																<td class=data align="center">
																	
																	<%=vo.getFiveS()==null?"":vo.getFiveS() %>
																	
																</td>
																<td class=data align="center">
																	
																	<%=vo.getFirstPerson() ==null?"":vo.getFirstPerson() %>
																	
																</td>
																<td class=data align="center">
																	
																	<%=vo.getBadReason()==null?"":vo.getBadReason() %>
																	
																</td>
															</tr>
														
													<%
													}
													String sumLoanbal=String.valueOf(DBUtil.doubleToStr1(sum1));
													    String sumbal=String.valueOf(DBUtil.doubleToStr1(sum2));
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
																�ϼ�
															</td>
															<td align=right>
																
															</td>
															<td align=right>
																<%=sumLoanbal%>
															</td>
															<td align=right>
																<%=sumbal %>
															</td>
															<td align=right>
																
															</td>
															<td align=right>
																
															</td>
															<td align=right>
																
															</td>
															<td align=right>
																
															</td>
															<td align=right>
																
															</td>
															<td align=right>
																
															</td>
														    <td align=right>
																
															</td>
															<td align=left>
																
															</td>
															</tr>
													</TBODY>
												</TABLE>
												<table cellSpacing=0 cellPadding=0 width=100% border=0 class=head>
													<tr>


														<td>


															ÿҳ
																<input type='text' name="pagesize" size='3'value="<%=pagesize %>" class='input'>
																�� | ��<%=maxpage %>ҳ<%=pagecount%>����¼ &nbsp;
																<input type='button' value='�� ҳ'onClick="goPage('top');" class='button'>
																&nbsp;
																<input type='button' value='�� ҳ' onClick='goPage("up");' class='button'
																<%=updisabled %>/>
																&nbsp;
																<input type='button' value='�� ҳ' onClick='goPage("down");' class='button'
																<%=downdisabled %>/>
																	
																&nbsp;
																<input type='button' value='ĩ ҳ' onClick='goPage("bottom");' class='button'>
																<input type="hidden" name="maxpage" value="<%=maxpage %>">	
																&nbsp; ��
																<input type="text" name="currpage" value="<%=currpage %>" class="input" size="3">
																<input type="hidden" name="pageer"  value="<%=currpage %>">
																ҳ<input type='button' value='ȷ��' onclick='goPage("gopage");' class='button'>
															</td>
														<td align=right>
															<input type='button' value='������ҳ'onclick='history.back();' class='button'>
															&nbsp;
															<input type='button' value='�����ϼ�'onclick='upLevel();' class='button'>
															&nbsp;
															<input type='button' value='���س�ʼҳ'onclick='back();' class='button'>
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
						���ڴ����С���
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
document.listform.firstPerson.value='<%=firstPerson.equals("0")?"0":firstPerson%>'
document.listform.managers.value='<%=managers.equals("0")?"0":managers%>'
</script>