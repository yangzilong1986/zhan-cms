<%@ page language="java" import="java.util.*" pageEncoding="GB2312"%>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.fcsort.fcnbloan.FcnbloanPageobject" %>
<%@ page import="zt.cms.fcsort.fcnbloan.FirstnbLoan" %>
<%@ page import="zt.platform.db.DBUtil"%>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%--
=============================================== 
Title:�������������ѯһ��ҳ��
Description: �������������ѯһ��ҳ��.
 * @version  $Revision: 1.13 $  $Date: 2007/05/30 05:49:25 $
 * @author   houcs
 * <p/>�޸ģ�$Author: houcs $
=============================================== 
--%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/fc_nblquery/";
	pageContext.setAttribute("basePath", basePath);
	pageContext.setAttribute("currentPath", currentPath);
	ConnectionManager manager = ConnectionManager.getInstance();
	FcnbloanPageobject vp =new FcnbloanPageobject(request);
    String create=request.getParameter("creadate");
    String colspan="7";
	String sql = "select DT from FCPRD  where INITIALIZED=1 order by SEQNO desc ";
	CachedRowSet crs =  manager.getRs(sql);
	if(create==null){
	String sql2="select max(dt) mdt from FCPRD  where INITIALIZED=1";
	CachedRowSet crs2 =  manager.getRs(sql2);
	String dt="";
	while(crs2.next()){
	dt=crs2.getString("mdt");
	create=dt;
	}
	 vp.setCreadate(dt);
	 }
	String strScbrhId=request.getParameter("brhid");
	String brhidLevel="";
	String yearday=request.getParameter("yearday")==null ? "2" :request.getParameter("yearday");
	String selectddate=request.getParameter("creadate")==null? "":request.getParameter("creadate");
	if(strScbrhId==null||strScbrhId.equals("")||strScbrhId.equals("0"))
	{
	UserManager um = (UserManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	String strUserName = um.getUserName(); 
	strScbrhId = SCUser.getBrhId(strUserName).trim();//������
	}
    String strScbrhName = SCBranch.getSName(strScbrhId);//��������
    //ҳ��״̬
    String startdate=request.getParameter("startdate")==null?"":request.getParameter("startdate");
	String enddate=request.getParameter("enddate")==null?"":request.getParameter("enddate");
	String sartbal=request.getParameter("sartbal")==null? "":request.getParameter("sartbal");
	String balend=request.getParameter("endbal")==null? "":request.getParameter("endbal");
	String params="&startdate="+startdate+"&enddate="+enddate+"&creadate="+create+"&sartbal="+sartbal+"&endbal="+balend+"&yearday="+yearday+"&upbrhid="+strScbrhId;
    pageContext.setAttribute("strScbrhId",strScbrhId);
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
		<link rel="stylesheet" type="text/css" href="web.css">
		<script language="javascript" src="<%= basePath %>query/setup/meizzDate.js"></script>
		<script language="javascript" src="ajax.js"></script>
		<script type="text/javascript" src="fcsort.js"></script>
		<script type="text/javascript">
		var select="<%=selectddate%>";
		var surl ="<%=currentPath%>";
		var params="<%=params%>";
		var yeardays="<%=yearday%>";
		var startdate="<%=startdate%>";
		var enddate="<%=enddate%>";
		var brhid="<%=strScbrhId%>";
		var create="<%=create%>";
		var colspan="<%=colspan%>";
		var cou="";
		function reset1(){
        document.listform.startdate.value="";
        document.listform.enddate.value="";
        document.listform.sartbal.value="";
        document.listform.endbal.value="";
		}
   
		</script>
	</head>

	<body background="/images/checks_02.jpg">
	<form action="<%=currentPath%>list.jsp" name="listform" id="listform">
	<input name="referValue" type="hidden">
	<input type="hidden" name="brhid" value="<%=strScbrhId%>">
		<div id=aaaa align="center">
			<TABLE borderColor=#999999 cellSpacing=0 cellPadding=0 align=center
				border=1 width=100% bgcolor=#f1f1f1 >
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
															<input type="button" value=" �� �� " class="button"onclick="document.listform.submit();">
															&nbsp;&nbsp;
															<input type="button" value=" �� �� " class="button"onclick="reset1()">
															&nbsp;&nbsp;
															<input type=button class=button value=" �� ӡ "onclick="printTable()">
															&nbsp;&nbsp;
															<input type="button" class="button" value=" �� �� "onclick="self.close()">
															&nbsp;

														</div>
														<table class="table" cellSpacing=1 cellPadding=1 width=100% border=0>
																
															<tr class=head>
																<br>
																<td>
																<input type="hidden" name="show" value="true">
																    <select id="yearday" name="yearday">
																    <option value="1" >����
																     <option value="2">����
																    </select>
																    
																��ѯʱ�㣺
																	<select id="creadate" name="creadate">
																	 <%
																	 while(crs.next()){
																	 %>
                                                                    <option value="<%=crs.getString("DT")%>"><%=DBUtil.fromDB(crs.getString("DT"))%>
																	</option>
																   <%
																   }
																   %>
																	</select>					
																������գ�
																<input class='input' type=text id="startdate" name="startdate"size=10 value="<%=startdate %>">
																<input type='button' value='��' class='button'onclick='setday(this,document.getElementById("startdate"))'>
																��
																<input class='input' type=text id="enddate" name="enddate"size=10 value="<%=enddate %>">
																<input type='button' value='��' class='button'onclick='setday(this,document.getElementById("enddate"))'>
																���ʴ�����
																<input type='text' size='3' name='sartbal' class='input' value="<%=sartbal %>">
																��
																<input type='text' size='3' name='endbal' class='input' value="<%=balend %>">
																��Ԫ
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
											
											
											 
											<div id='showTable' align=center width=100%>
									           
												<div class=caption align=center>
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
												<table>
												<tr>
												<td>
                                               <font class="title">������գ�
                                               <%
                                                                                                                                             if(!startdate.equals("") && !enddate.equals(""))
                                                                                                                                             {
                                               %>
                                               <%=startdate%>&nbsp;��&nbsp;<%=enddate%>
                                               <%
                                                                                                                                             }
                                                                                                                                             else if(!startdate.equals("") && enddate.equals("")){
                                               %>
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
																��������:<%=strScbrhName%>
															</td>
									
															<td align=right  class=title>
																��λ����Ԫ
															</td>
														</tr>
													</table>
												</div>
												<table class=table cellSpacing=1 cellPadding=1 width=100% 
													border=0 align=center  id="checkTable">
													<TBODY>
														<tr>
															<td rowspan="2" align="center" valign="bottom"class="head">
															��������
															</td>
															<td rowspan="2" align="center" valign="bottom"class="head">
															��������
															</td>
															<td rowspan="2" align="center" valign="bottom"class="head">
															��������
															</td>
															<td rowspan="2" align="center" valign="bottom"class="head">
															��ͬ���
															</td>
															<td rowspan="2" align="center" valign="bottom"class="head">
															��Ƿ���
															</td>
															<td colspan="3" align="center" valign="bottom"class="head">
																����
															</td>
														</tr>
														<tr>
															<td align="center" valign="bottom" class="head">
																�μ�
															</td>
															<td align="center" valign="bottom" class="head">
																����
															</td>
															<td align="center" valign="bottom" class="head">
																��ʧ
															</td>
														</tr>
											
    													<%
											    																		    					                                                          	vp.setBrhid(strScbrhId);
											    																		    					                                                          	vp.setYearday(yearday);
											    																		    					    		List vplist = vp.getListvp();
											    																		    					    		brhidLevel=vp.getBrhidLevel();
											    																		    					    		
											    																		    					    			    int i1=vplist.size();
											    																		    					    			    int j=10-i1;
											    																		    					    		int a1=0;
											    																		    					    		int a2=0;
											    																		    					    		double a3=0.00;
											    																		    					    		double a4=0.00;
											    																		    					    		double a5=0.00;
											    																		    					    		double a6=0.00;
											    																		    					    		double a7=0.00;
											    																		    					    		String sumA="";
											    																		    					    		String sumB="";
											    																		    					    		String sumC="";
											    																		    					    		String sumD="";
											    																		    					    		String sumE="";
											    																		    					    		for(int i =0;i<vplist.size();i++)
											    																		    					    		{
											    																		    					    			FirstnbLoan vo=(FirstnbLoan)vplist.get(i);
											    																		    					    			int sum1=Integer.valueOf(vo.getNewloansum()==null?"0":vo.getNewloansum()).intValue();
											    																		    					    			int sum2=Integer.valueOf(vo.getNewpersonsum()==null?"0":vo.getNewpersonsum()).intValue();
											    																		    					    			double sum3=Double.valueOf(vo.getLoanbal1()==null?"0.00":vo.getLoanbal1()).doubleValue();
											    																		    					    			double sum4=Double.valueOf(vo.getBal1()==null ? "0.00":vo.getBal1()).doubleValue();
											    																		    					    			double sum5=Double.valueOf(vo.getHh3()==null ? "0.00":vo.getHh3()).doubleValue();
											    																		    					    			double sum6=Double.valueOf(vo.getHh4()==null?"0.00":vo.getHh4()).doubleValue();
											    																		    					    			double sum7=Double.valueOf(vo.getHh5()==null?"0.00":vo.getHh5()).doubleValue();
											    																		    					    			a1+=sum1;
											    																		    					    			a2+=sum2;
											    																		    					    			a3+=sum3;
											    																		    					    			a4+=sum4;
											    																		    					    			a5+=sum5;
											    																		    					    			a6+=sum6;
											    																		    					    			a7+=sum7;
											    																		    					    		 sumA=DBUtil.doubleToStr1(a3);
											    																		    					    		 sumB=DBUtil.doubleToStr1(a4);
											    																		    					    		 sumC=DBUtil.doubleToStr1(a5);
											    																		    					    		 sumD=DBUtil.doubleToStr1(a6);
											    																		    					    		 sumE=DBUtil.doubleToStr1(a7);
											    													%>
    													<tr bgcolor=#FFFFFF onclick="toseconlist('<%=vo.getBrhid() %>','<%=vo.getBrhtype() %>');" onmouseout="mOut(this);" onmouseover="mOvr(this);"class=data>
    															<td class=data align="left" >
																	<%=vo.getSname() %>
																	
																</td>
																<td class=data align="right">
																<%=vo.getNewloansum()==null ? "0" : vo.getNewloansum() %>
																	
																	
																</td>
																<td class=data align="right">
																
																	<%=vo.getNewpersonsum()==null ? "0":vo.getNewpersonsum() %>
																	
																</td>
																<td class=data align="right">
																
																	<%=vo.getLoanbal()==null ? "0.00" :vo.getLoanbal() %>
																	
																</td>
																
																<td class=data align="right">
																	<%=vo.getBal()==null? "0.00" : vo.getBal()%>
																	
																	
																</td>
																<td class=data align="right">
																<%=vo.getH3()==null ? "0.00" :vo.getH3() %>
																	
																	
																</td>
																<td class=data align="right">
																<%=vo.getH4()==null ? "0.00" : vo.getH4()%>
																	
																	
																</td>
																<td class=data align="right">
																<%=vo.getH5()==null ?"0.00": vo.getH5()%>
																	
																	
																</td>

															</tr>
    													<% } 
    													if(j>0){
    												
    													  for(int k=0; k<j;k++){
    													  %>
    													  <tr height="15" bgcolor=#FFFFFF onmouseout="mOut(this)" class=data>
    													    <td class=data align="right"></td>
    													     <td class=data align="right"></td>
    													      <td class=data align="right"></td>
    													       <td class=data align="right"></td>
    													        <td class=data align="right"></td>
    													         <td class=data align="right"></td>
    													          <td class=data align="right"></td>
    													           <td class=data align="right"></td>
    													  </tr>
    													<%  }
    													}
    													%>
    														<tr bgcolor=#E7E9CF onclick="toseconlist('<%=strScbrhId%>','<%=brhidLevel%>');" onmouseover="mOvr(this);" class=data >
															<td align=center>
																�ϼ�:
															</td>
														     <td align=right>
																<%=a1 %>
															</td>
															<td align=right>
																<%=a2 %>
															</td>
															<td align=right>
																<%=sumA %>
															</td>
															<td align=right>
																<%=sumB %>
															</td>
															<td align=right>
																<%=sumC%>
															</td>
															<td align=right>
																<%=sumD%>
															</td>
															<td align=right>
																<%=sumE%>
															</td>
															</tr>
															
													</TBODY>
													
												</TABLE>
											
												<table cellSpacing=0 cellPadding=0 width=100% border=0 class=head>
													<tr>


													
														<td align=right>
															<input type='button' value='������ҳ'onclick='history.back();' class='button'>
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
document.listform.yearday.value="<%=yearday.equals("2")?"2":yearday%>";
document.listform.creadate.value="<%=create%>";
</script>
