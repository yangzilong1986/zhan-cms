<%@ page language="java"  pageEncoding="GB2312"%>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.platform.db.DBUtil"%>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%--
=============================================== 
Title:按行业五级分类统计表
Description: 新增不良贷款查询一级页面.
 * @version  $Revision: 1.8 $  $Date: 2007/05/29 06:54:06 $
 * @author   houcs
 * <p/>修改：$Author: houcs $
=============================================== 
--%>
<%  
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/hangye/";
	pageContext.setAttribute("basePath", basePath);
	pageContext.setAttribute("currentPath", currentPath);
	
	ConnectionManager manager = ConnectionManager.getInstance();
    String create=request.getParameter("creadate");
    String colspan="7";
	String sql = "select DT from FCPRD  where INITIALIZED=1 order by SEQNO desc ";
	CachedRowSet crs1 =  manager.getRs(sql);
	if(create==null){
	String sql2="select max(dt) mdt from FCPRD  where INITIALIZED=1";
	CachedRowSet crs2 =  manager.getRs(sql2);
	String dt="";
	while(crs2.next()){
	dt=crs2.getString("mdt");
	create=dt;
	}
	 }
	  String startdate="";
	String enddate="";
	String params="";
	String strScbrhId=request.getParameter("brhidd")==null?"":request.getParameter("brhidd");
	
	String selectddate=request.getParameter("creadate")==null? "":request.getParameter("creadate");
	UserManager um = (UserManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	String strUserName = um.getUserName(); 
	if(strScbrhId==null||strScbrhId.equals("")||strScbrhId.equals("0"))
	{
	strScbrhId = SCUser.getBrhId(strUserName).trim();//网点编号
	}
	String br=strScbrhId;
	String brhiddd=SCUser.getBrhId(strUserName).trim();//网点编号
	String SUBBRHIDs = SCBranch.getSubBrh0(brhiddd).trim();
	if(SUBBRHIDs !=null || !SUBBRHIDs.endsWith(""))
	{
	int i=SUBBRHIDs.indexOf("999999");
	int j=SUBBRHIDs.length();
	if(i!=-1){
	SUBBRHIDs=SUBBRHIDs.substring(0,i-3).trim()+SUBBRHIDs.substring(i+1,j).trim();
	}
		}
	int i=SUBBRHIDs.length()/9;
	String[] SUBBRHID=new String[i];
	if(SUBBRHIDs.length()>9){
	SUBBRHID=SUBBRHIDs.split(",");
	}else{
	SUBBRHID[0]=SUBBRHIDs;
	}
    String strScbrhName = SCBranch.getSName(strScbrhId);//网点名称
    pageContext.setAttribute("strScbrhId",strScbrhId);
    //**************************SQL 部分*************************
    strScbrhId=SCBranch.getSubBranchAll(strScbrhId);
		strScbrhId=strScbrhId.replaceAll(",", "','");
		StringBuffer sql3 = new StringBuffer(
		           "select " +                                                        //'农林牧渔业'
		           "sum(case when r.dim1=1 and dim2 in(-1,1) then amt1 else 0 end) rs1," +//1-1
		           "sum(case when r.dim1=2 and dim2 in(-1,1) then amt1 else 0 end) rs2," +//1-2
		           "sum(case when r.dim1=3 and dim2 in(-1,1) then amt1 else 0 end) rs3," +//1-3
		           "sum(case when r.dim1=4 and dim2 in(-1,1) then amt1 else 0 end) rs4," +//1-4
		           "sum(case when r.dim1=5 and dim2 in(-1,1) then amt1 else 0 end) rs5," +//1-5
		                                                                                     //'采矿业'
		           "sum(case when r.dim1=1 and dim2=2 then amt1 else 0 end) rs6," +//2-1
		           "sum(case when r.dim1=2 and dim2=2 then amt1 else 0 end) rs7," +//2-2
		           "sum(case when r.dim1=3 and dim2=2 then amt1 else 0 end) rs8," +//2-3
		           "sum(case when r.dim1=4 and dim2=2 then amt1 else 0 end) rs9," +//2-4
		           "sum(case when r.dim1=5 and dim2=2 then amt1 else 0 end) rs10," +//2-5
		                                                                                     //'制造业'
		           "sum(case when r.dim1=1 and dim2=3 then amt1 else 0 end) rs11," +//3-1
		           "sum(case when r.dim1=2 and dim2=3 then amt1 else 0 end) rs12," +//3-2
		           "sum(case when r.dim1=3 and dim2=3 then amt1 else 0 end) rs13," +//3-3
		           "sum(case when r.dim1=4 and dim2=3 then amt1 else 0 end) rs14," +//3-4
		           "sum(case when r.dim1=5 and dim2=3 then amt1 else 0 end) rs15," +//3-5
		                                                                      //'电力、煤气及水的生产和供应业'
		           "sum(case when r.dim1=1 and dim2=4 then amt1 else 0 end) rs16," +//4-1
		           "sum(case when r.dim1=2 and dim2=4 then amt1 else 0 end) rs17," +//4-2
		           "sum(case when r.dim1=3 and dim2=4 then amt1 else 0 end) rs18," +//4-3
		           "sum(case when r.dim1=4 and dim2=4 then amt1 else 0 end) rs19," +//4-4
		           "sum(case when r.dim1=5 and dim2=4 then amt1 else 0 end) rs20," +//4-5
		                                                                                    //'建筑业'
		           "sum(case when r.dim1=1 and dim2=5 then amt1 else 0 end) rs21," +//5-1
		           "sum(case when r.dim1=2 and dim2=5 then amt1 else 0 end) rs22," +//5-2
		           "sum(case when r.dim1=3 and dim2=5 then amt1 else 0 end) rs23," +//5-3
		           "sum(case when r.dim1=4 and dim2=5 then amt1 else 0 end) rs24," +//5-4
		           "sum(case when r.dim1=5 and dim2=5 then amt1 else 0 end) rs25," +//5-5
		                                                                       //'交通运输、仓储及邮电通信业'
		           "sum(case when r.dim1=1 and dim2=6 then amt1 else 0 end) rs26," +//6-1
		           "sum(case when r.dim1=2 and dim2=6 then amt1 else 0 end) rs27," +//6-2
		           "sum(case when r.dim1=3 and dim2=6 then amt1 else 0 end) rs28," +//6-3
		           "sum(case when r.dim1=4 and dim2=6 then amt1 else 0 end) rs29," +//6-4
		           "sum(case when r.dim1=5 and dim2=6 then amt1 else 0 end) rs30," +//6-5
		                                                                     //'信息传输、计算机服务和软件业'
		           "sum(case when r.dim1=1 and dim2=7 then amt1 else 0 end) rs31," +//7-1
		           "sum(case when r.dim1=2 and dim2=7 then amt1 else 0 end) rs32," +//7-2
		           "sum(case when r.dim1=3 and dim2=7 then amt1 else 0 end) rs33," +//7-3
		           "sum(case when r.dim1=4 and dim2=7 then amt1 else 0 end) rs34," +//7-4
		           "sum(case when r.dim1=5 and dim2=7 then amt1 else 0 end) rs35," +//7-5
		                                                                                   //'批发与零售业'
		           "sum(case when r.dim1=1 and dim2=8 then amt1 else 0 end) rs36," +//8-1
		           "sum(case when r.dim1=2 and dim2=8 then amt1 else 0 end) rs37," +//8-2
		           "sum(case when r.dim1=3 and dim2=8 then amt1 else 0 end) rs38," +//8-3
		           "sum(case when r.dim1=4 and dim2=8 then amt1 else 0 end) rs39," +//8-4
		           "sum(case when r.dim1=5 and dim2=8 then amt1 else 0 end) rs40," +//8-5
		                                                                                //'住宿和餐饮业'
		           "sum(case when r.dim1=1 and dim2=9 then amt1 else 0 end) rs41," +//9-1
		           "sum(case when r.dim1=2 and dim2=9 then amt1 else 0 end) rs42," +//9-2
		           "sum(case when r.dim1=3 and dim2=9 then amt1 else 0 end) rs43," +//9-3
		           "sum(case when r.dim1=4 and dim2=9 then amt1 else 0 end) rs44," +//9-4
		           "sum(case when r.dim1=5 and dim2=9 then amt1 else 0 end) rs45," +//9-5
		                                                                                    //'金融业'
		           "sum(case when r.dim1=1 and dim2=10 then amt1 else 0 end) rs46," +//10-1
		           "sum(case when r.dim1=2 and dim2=10 then amt1 else 0 end) rs47," +//10-2
		           "sum(case when r.dim1=3 and dim2=10 then amt1 else 0 end) rs48," +//10-3
		           "sum(case when r.dim1=4 and dim2=10 then amt1 else 0 end) rs49," +//10-4
		           "sum(case when r.dim1=5 and dim2=10 then amt1 else 0 end) rs50," +//10-5
		                                                                                      //'房地产业'
		           "sum(case when r.dim1=1 and dim2=11 then amt1 else 0 end) rs51," +//11-1
		           "sum(case when r.dim1=2 and dim2=11 then amt1 else 0 end) rs52," +//11-2
		           "sum(case when r.dim1=3 and dim2=11 then amt1 else 0 end) rs53," +//11-3
		           "sum(case when r.dim1=4 and dim2=11 then amt1 else 0 end) rs54," +//11-4
		           "sum(case when r.dim1=5 and dim2=11 then amt1 else 0 end) rs55," +//11-5
		                                                                                 //'租赁和商业服务业'
		           "sum(case when r.dim1=1 and dim2=12 then amt1 else 0 end) rs56," +//12-1
		           "sum(case when r.dim1=2 and dim2=12 then amt1 else 0 end) rs57," +//12-2
		           "sum(case when r.dim1=3 and dim2=12 then amt1 else 0 end) rs58," +//12-3
		           "sum(case when r.dim1=4 and dim2=12 then amt1 else 0 end) rs59," +//12-4
		           "sum(case when r.dim1=5 and dim2=12 then amt1 else 0 end) rs60," +//12-5
		                                                                       //'科学研究、技术服务和地质勘察'
		           "sum(case when r.dim1=1 and dim2=13 then amt1 else 0 end) rs61," +//13-1
		           "sum(case when r.dim1=2 and dim2=13 then amt1 else 0 end) rs62," +//13-2
		           "sum(case when r.dim1=3 and dim2=13 then amt1 else 0 end) rs63," +//13-3
		           "sum(case when r.dim1=4 and dim2=13 then amt1 else 0 end) rs64," +//13-4
		           "sum(case when r.dim1=5 and dim2=13 then amt1 else 0 end) rs65," +//13-5
		                                                                          //'水利、环境和公共设施管理业'
		           "sum(case when r.dim1=1 and dim2=14 then amt1 else 0 end) rs66," +//14-1
		           "sum(case when r.dim1=2 and dim2=14 then amt1 else 0 end) rs67," +//14-2
		           "sum(case when r.dim1=3 and dim2=14 then amt1 else 0 end) rs68," +//14-3
		           "sum(case when r.dim1=4 and dim2=14 then amt1 else 0 end) rs69," +//14-4
		           "sum(case when r.dim1=5 and dim2=14 then amt1 else 0 end) rs70," +//14-5
		                                                                              //'居民服务和其他服务业'
		           "sum(case when r.dim1=1 and dim2=15 then amt1 else 0 end) rs71," +//15-1
		           "sum(case when r.dim1=2 and dim2=15 then amt1 else 0 end) rs72," +//15-2
		           "sum(case when r.dim1=3 and dim2=15 then amt1 else 0 end) rs73," +//15-3
		           "sum(case when r.dim1=4 and dim2=15 then amt1 else 0 end) rs74," +//15-4
		           "sum(case when r.dim1=5 and dim2=15 then amt1 else 0 end) rs75," +//15-5
		                                                                                           //'教育'
		           "sum(case when r.dim1=1 and dim2=16 then amt1 else 0 end) rs76," +//16-1
		           "sum(case when r.dim1=2 and dim2=16 then amt1 else 0 end) rs77," +//16-2
		           "sum(case when r.dim1=3 and dim2=16 then amt1 else 0 end) rs78," +//16-3
		           "sum(case when r.dim1=4 and dim2=16 then amt1 else 0 end) rs79," +//16-4
		           "sum(case when r.dim1=5 and dim2=16 then amt1 else 0 end) rs80," +//16-5
		                                                                             //'卫生、体育和社会福利业'
		           "sum(case when r.dim1=1 and dim2=17 then amt1 else 0 end) rs81," +//17-1
		           "sum(case when r.dim1=2 and dim2=17 then amt1 else 0 end) rs82," +//17-2
		           "sum(case when r.dim1=3 and dim2=17 then amt1 else 0 end) rs83," +//17-3
		           "sum(case when r.dim1=4 and dim2=17 then amt1 else 0 end) rs84," +//17-4
		           "sum(case when r.dim1=5 and dim2=17 then amt1 else 0 end) rs85," +//17-5
		                                                                                     //'文化与娱乐业'
		           "sum(case when r.dim1=1 and dim2=18 then amt1 else 0 end) rs86," +//18-1
		           "sum(case when r.dim1=2 and dim2=18 then amt1 else 0 end) rs87," +//18-2
		           "sum(case when r.dim1=3 and dim2=18 then amt1 else 0 end) rs88," +//18-3
		           "sum(case when r.dim1=4 and dim2=18 then amt1 else 0 end) rs89," +//18-4
		           "sum(case when r.dim1=5 and dim2=18 then amt1 else 0 end) rs90," +//18-5
		                                                                              //'公共管理和社会组织'
		           "sum(case when r.dim1=1 and dim2=19 then amt1 else 0 end) rs91," +//19-1
		           "sum(case when r.dim1=2 and dim2=19 then amt1 else 0 end) rs92," +//19-2
		           "sum(case when r.dim1=3 and dim2=19 then amt1 else 0 end) rs93," +//19-3
		           "sum(case when r.dim1=4 and dim2=19 then amt1 else 0 end) rs94," +//19-4
		           "sum(case when r.dim1=5 and dim2=19 then amt1 else 0 end) rs95," +//19-5
		                                                                                      //'国际组织'
		           "sum(case when r.dim1=1 and dim2=20 then amt1 else 0 end) rs96," +//20-1
		           "sum(case when r.dim1=2 and dim2=20 then amt1 else 0 end) rs97," +//20-2
		           "sum(case when r.dim1=3 and dim2=20 then amt1 else 0 end) rs98," +//20-3
		           "sum(case when r.dim1=4 and dim2=20 then amt1 else 0 end) rs99," +//20-4
		           "sum(case when r.dim1=5 and dim2=20 then amt1 else 0 end) rs100," +//20-5
		                                                                                     //'对境外贷款'
		           "sum(case when r.dim1=1 and dim2=21 then amt1 else 0 end) rs101," +//21-1
		           "sum(case when r.dim1=2 and dim2=21 then amt1 else 0 end) rs102," +//21-2
		           "sum(case when r.dim1=3 and dim2=21 then amt1 else 0 end) rs103," +//21-3
		           "sum(case when r.dim1=4 and dim2=21 then amt1 else 0 end) rs104," +//21-4
		           "sum(case when r.dim1=5 and dim2=21 then amt1 else 0 end) rs105 ");//21-5
        sql3.append(" from fcdata r where ");
       
        if (create!= null && !create.equals("")&& !create.equals("0")) {
			sql3.append(" r.dt='" + create.trim()+ "' and  ");
		}
        sql3.append(" r.brhid in ('"+strScbrhId+"') and ftype=21");
        UserManager um1 = (UserManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	String strUserName1 = um1.getUserName(); 
	String strScbrhId1 = SCUser.getBrhId(strUserName1).trim();//网点编号
	CachedRowSet crs=null;
	if(!strScbrhId1.substring(3,9).equals("999999")){
	 crs = manager.getRs(sql3.toString());
	}
     //**************************SQL 部分*************************
    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>按行业五级分类统计表</title>
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
		var brhid="<%=br%>";
		var create="<%=create%>";
		var params="<%=params%>";
		var yeardays="";
		var startdate="<%=startdate%>";
		var enddate="<%=enddate%>";
		var colspan="<%=colspan%>";
         var cou="";
		</script>
	</head>

	<body background="/images/checks_02.jpg">
	<form action="<%=currentPath%>list.jsp" name="listform" id="listform">
	<input name="referValue" type="hidden">
	<input name="brhid" type="hidden" value="<%=strScbrhId%>">
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
														<%
														if( !strScbrhId1.substring(3,9).equals("999999")){
														 %>
															<input type="button" value=" 检 索 " enabled="false" class="button"onclick="document.listform.submit();">
															&nbsp;&nbsp;
															<input type=button class=button enabled="false" value=" 打 印 "onclick="printTable()">
															&nbsp;&nbsp;
															<%
															}
															 %>
															<input type="button" class="button" value=" 关 闭 "onclick="self.close()">
															&nbsp;

														</div>
														<table class="table" cellSpacing=1 cellPadding=1 width=100% border=0>
																
															<tr class=head>
																<br>
																<td>
																<input type="hidden" name="show" value="true">
																网点名称：
																    <select id="brhidd" name="brhidd">
																    <%
																    if(strScbrhId1.substring(3,9).equals("999999")){
																    String sname=SCBranch.getSName(strScbrhId1);
																    %>
																    <option value="<%=strScbrhId1%>"><%=sname%></option>
																  <%}
																  else
																  {
																    for(int j =0;j<SUBBRHID.length;j++)
											    				{
											    				String brhid=SUBBRHID[j];
											    				String sname=SCBranch.getSName(brhid);
														        %>
											    				<option value="<%=brhid%>"><%=sname%></option>
											    				<%
											    				}
											    				}
																  %>
																    </select>
																查询时点：
																	<select id="creadate" name="creadate">
																	 <%
																	 while(crs1.next()){
																	 %>
                                                                    <option value="<%=crs1.getString("DT")%>"><%=DBUtil.fromDB(crs1.getString("DT"))%>
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
											<div id='showTable' align=center width=100%>
												<div class=caption align=center>
												<font size="4">
											按行业五级分类统计
											</font>
												</div>
												<div align=center>
													<table cellSpacing=0 cellPadding=0 width=100% border=0>
													<tr class=title align="center" height="1">
															<td align=center class=title >
																
															</td>
															
														</tr>
														<% 
														 String subCreate=create.substring(0,7);
                                                                   subCreate=subCreate.replaceAll("-","年");
                                                               if(subCreate.substring(5,6).equals("0")){
                                                              subCreate= subCreate.substring(0,5)+subCreate.substring(6,7)+"月";
                                                                 }else{
                                                             subCreate=subCreate+"月";
                                                                  }
                                                                  %>
														<tr class=title align="center">
															<td  class=title align="left">
																网点名称：<%=strScbrhName%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																查询日期：<%=subCreate%>
															</td>
															<td align=right  class=title>
																单位：元
															</td>
														</tr>
													</table>
												</div>
												<div align=center>
												<table class=table cellSpacing=1 cellPadding=1 width=100% 
													border=0 align=center  id="checkTable">
													<TBODY>
														<tr>
															<td rowspan="2" align="center" valign="bottom"class="head" width="25%">
															行业
															</td>
															<td rowspan="2" align="center" valign="bottom"class="head" width="10%">
															贷款合计
															</td>
															<td colspan="3" align="center" valign="bottom"class="head">
																正常
															</td>
															<td colspan="4" align="center" valign="bottom"class="head">
																不良
															</td>
														</tr>
														<tr>
														  <td  align="center" valign="bottom"class="head">
															合计
															</td>
															<td align="center" valign="bottom" class="head">
																正常
															</td>
															<td align="center" valign="bottom" class="head">
																关注
															</td>
															<td  align="center" valign="bottom"class="head">
															合计
															</td>
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
															 if(strScbrhId1.substring(3,9).equals("999999"))
															 
															 {
															
															 %>
															 	<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	农、林、牧、渔业
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
															</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	采矿业
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
															
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	制造业
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
															
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
															
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	电力、燃气及水的生产和供应业
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																建筑业
																</td>
																<td class=data align="right">
															
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
															
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	交通运输、仓储和邮政业
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	信息传输、计算机服务和软件业
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
															</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	批发和零售业
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	住宿和餐饮业
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	金融业
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	房地产业
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	租赁和商务服务业
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	科学研究、技术服务和地质勘查业
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	水利、环境和公共设施管理业
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	居民服务和其他服务业
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	教育
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	卫生、社会保障和社会福利业
																</td>
																<td class=data align="right">
																
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	文化、体育和娱乐业
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	公共管理和社会组织
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	国际组织
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	对境外贷款
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																	
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
																<td class=data align="right">
																</td>
															</tr>
															
    														<tr bgcolor=#E7E9CF  class=data >
															<td align=center>
																合计:
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
															<td align=right>
															</td>
															</tr>
															 <% 
															 }
															 if(crs !=null)
															while (crs.next()){
															 %>
    													<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	农、林、牧、渔业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs1")==null?"0.00":crs.getString("rs1")).doubleValue()
																+Double.valueOf(crs.getString("rs2")==null?"0.00":crs.getString("rs2")).doubleValue() +
																Double.valueOf(crs.getString("rs3")==null?"0.00":crs.getString("rs3")).doubleValue() 
																	+Double.valueOf(crs.getString("rs4")==null?"0.00":crs.getString("rs4")).doubleValue() +
																	Double.valueOf(crs.getString("rs5")==null?"0.00":crs.getString("rs5")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs1")==null?"0.00":crs.getString("rs1")).doubleValue() 
																+Double.valueOf(crs.getString("rs2")==null?"0.00":crs.getString("rs2")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs1")==null?"0.00":crs.getString("rs1")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs2")==null?"0.00":crs.getString("rs2")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs3")==null?"0.00":crs.getString("rs3")).doubleValue() 
																	+Double.valueOf(crs.getString("rs4")==null?"0.00":crs.getString("rs4")).doubleValue() +
																	Double.valueOf(crs.getString("rs5")==null?"0.00":crs.getString("rs5")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs3")==null?"0.00":crs.getString("rs3")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs4")==null?"0.00":crs.getString("rs4")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs5")==null?"0.00":crs.getString("rs5")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	采矿业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs6")==null?"0.00":crs.getString("rs6")).doubleValue() 
																+Double.valueOf(crs.getString("rs7")==null?"0.00":crs.getString("rs7")).doubleValue() +
																Double.valueOf(crs.getString("rs8")==null?"0.00":crs.getString("rs8")).doubleValue() 
																	+Double.valueOf(crs.getString("rs9")==null?"0.00":crs.getString("rs9")).doubleValue() +
																	Double.valueOf(crs.getString("rs10")==null?"0.00":crs.getString("rs10")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs6")==null?"0.00":crs.getString("rs6")).doubleValue() 
																+Double.valueOf(crs.getString("rs7")==null?"0.00":crs.getString("rs7")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs6")==null?"0.00":crs.getString("rs6")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs7")==null?"0.00":crs.getString("rs7")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs8")==null?"0.00":crs.getString("rs8")).doubleValue() 
																	+Double.valueOf(crs.getString("rs9")==null?"0.00":crs.getString("rs9")).doubleValue() +
																	Double.valueOf(crs.getString("rs10")==null?"0.00":crs.getString("rs10")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs8")==null?"0.00":crs.getString("rs8")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs9")==null?"0.00":crs.getString("rs9")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs10")==null?"0.00":crs.getString("rs10")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	制造业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs11")==null?"0.00":crs.getString("rs11")).doubleValue() 
																+Double.valueOf(crs.getString("rs12")==null?"0.00":crs.getString("rs12")).doubleValue() +
																Double.valueOf(crs.getString("rs13")==null?"0.00":crs.getString("rs13")).doubleValue() 
																	+Double.valueOf(crs.getString("rs14")==null?"0.00":crs.getString("rs14")).doubleValue() +
																	Double.valueOf(crs.getString("rs15")==null?"0.00":crs.getString("rs15")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs11")==null?"0.00":crs.getString("rs11")).doubleValue() 
																+Double.valueOf(crs.getString("rs12")==null?"0.00":crs.getString("rs12")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs11")==null?"0.00":crs.getString("rs11")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs12")==null?"0.00":crs.getString("rs12")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs13")==null?"0.00":crs.getString("rs13")).doubleValue() 
																	+Double.valueOf(crs.getString("rs14")==null?"0.00":crs.getString("rs14")).doubleValue() +
																	Double.valueOf(crs.getString("rs15")==null?"0.00":crs.getString("rs15")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs13")==null?"0.00":crs.getString("rs13")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs14")==null?"0.00":crs.getString("rs14")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs15")==null?"0.00":crs.getString("rs15")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	电力、燃气及水的生产和供应业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs16")==null?"0.00":crs.getString("rs16")).doubleValue() 
																+Double.valueOf(crs.getString("rs17")==null?"0.00":crs.getString("rs17")).doubleValue() +
																Double.valueOf(crs.getString("rs18")==null?"0.00":crs.getString("rs18")).doubleValue() 
																	+Double.valueOf(crs.getString("rs19")==null?"0.00":crs.getString("rs19")).doubleValue() +
																	Double.valueOf(crs.getString("rs20")==null?"0.00":crs.getString("rs20")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs16")==null?"0.00":crs.getString("rs16")).doubleValue() 
																+Double.valueOf(crs.getString("rs17")==null?"0.00":crs.getString("rs17")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs16")==null?"0.00":crs.getString("rs16")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs17")==null?"0.00":crs.getString("rs17")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs18")==null?"0.00":crs.getString("rs18")).doubleValue() 
																	+Double.valueOf(crs.getString("rs19")==null?"0.00":crs.getString("rs19")).doubleValue() +
																	Double.valueOf(crs.getString("rs20")==null?"0.00":crs.getString("rs20")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs18")==null?"0.00":crs.getString("rs18")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs19")==null?"0.00":crs.getString("rs19")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs20")==null?"0.00":crs.getString("rs20")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																建筑业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs21")==null?"0.00":crs.getString("rs21")).doubleValue() 
																+Double.valueOf(crs.getString("rs22")==null?"0.00":crs.getString("rs22")).doubleValue() +
																Double.valueOf(crs.getString("rs23")==null?"0.00":crs.getString("rs23")).doubleValue() 
																	+Double.valueOf(crs.getString("rs24")==null?"0.00":crs.getString("rs24")).doubleValue() +
																	Double.valueOf(crs.getString("rs25")==null?"0.00":crs.getString("rs25")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs21")==null?"0.00":crs.getString("rs21")).doubleValue() 
																+Double.valueOf(crs.getString("rs22")==null?"0.00":crs.getString("rs22")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs21")==null?"0.00":crs.getString("rs21")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs22")==null?"0.00":crs.getString("rs22")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs23")==null?"0.00":crs.getString("rs23")).doubleValue() 
																	+Double.valueOf(crs.getString("rs24")==null?"0.00":crs.getString("rs24")).doubleValue() +
																	Double.valueOf(crs.getString("rs25")==null?"0.00":crs.getString("rs25")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs23")==null?"0.00":crs.getString("rs23")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs24")==null?"0.00":crs.getString("rs24")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs25")==null?"0.00":crs.getString("rs25")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	交通运输、仓储和邮政业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs26")==null?"0.00":crs.getString("rs26")).doubleValue() 
																+Double.valueOf(crs.getString("rs27")==null?"0.00":crs.getString("rs27")).doubleValue() +
																Double.valueOf(crs.getString("rs28")==null?"0.00":crs.getString("rs28")).doubleValue() 
																	+Double.valueOf(crs.getString("rs29")==null?"0.00":crs.getString("rs29")).doubleValue() +
																	Double.valueOf(crs.getString("rs30")==null?"0.00":crs.getString("rs30")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs26")==null?"0.00":crs.getString("rs26")).doubleValue() 
																+Double.valueOf(crs.getString("rs27")==null?"0.00":crs.getString("rs27")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs26")==null?"0.00":crs.getString("rs26")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs27")==null?"0.00":crs.getString("rs27")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs28")==null?"0.00":crs.getString("rs28")).doubleValue() 
																	+Double.valueOf(crs.getString("rs29")==null?"0.00":crs.getString("rs29")).doubleValue() +
																	Double.valueOf(crs.getString("rs30")==null?"0.00":crs.getString("rs30")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs28")==null?"0.00":crs.getString("rs28")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs29")==null?"0.00":crs.getString("rs29")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs30")==null?"0.00":crs.getString("rs30")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	信息传输、计算机服务和软件业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs31")==null?"0.00":crs.getString("rs31")).doubleValue() 
																+Double.valueOf(crs.getString("rs32")==null?"0.00":crs.getString("rs32")).doubleValue() +
																Double.valueOf(crs.getString("rs33")==null?"0.00":crs.getString("rs33")).doubleValue() 
																	+Double.valueOf(crs.getString("rs34")==null?"0.00":crs.getString("rs34")).doubleValue() +
																	Double.valueOf(crs.getString("rs35")==null?"0.00":crs.getString("rs35")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs31")==null?"0.00":crs.getString("rs31")).doubleValue() 
																+Double.valueOf(crs.getString("rs32")==null?"0.00":crs.getString("rs32")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs31")==null?"0.00":crs.getString("rs31")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs32")==null?"0.00":crs.getString("rs32")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs33")==null?"0.00":crs.getString("rs33")).doubleValue() 
																	+Double.valueOf(crs.getString("rs34")==null?"0.00":crs.getString("rs34")).doubleValue() +
																	Double.valueOf(crs.getString("rs35")==null?"0.00":crs.getString("rs35")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs33")==null?"0.00":crs.getString("rs33")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs34")==null?"0.00":crs.getString("rs34")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs35")==null?"0.00":crs.getString("rs35")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	批发和零售业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs36")==null?"0.00":crs.getString("rs36")).doubleValue() 
																+Double.valueOf(crs.getString("rs37")==null?"0.00":crs.getString("rs37")).doubleValue() +
																Double.valueOf(crs.getString("rs38")==null?"0.00":crs.getString("rs38")).doubleValue() 
																	+Double.valueOf(crs.getString("rs39")==null?"0.00":crs.getString("rs39")).doubleValue() +
																	Double.valueOf(crs.getString("rs40")==null?"0.00":crs.getString("rs40")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs36")==null?"0.00":crs.getString("rs36")).doubleValue() 
																+Double.valueOf(crs.getString("rs37")==null?"0.00":crs.getString("rs37")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs36")==null?"0.00":crs.getString("rs36")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs37")==null?"0.00":crs.getString("rs37")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs38")==null?"0.00":crs.getString("rs38")).doubleValue() 
																	+Double.valueOf(crs.getString("rs39")==null?"0.00":crs.getString("rs39")).doubleValue() +
																	Double.valueOf(crs.getString("rs40")==null?"0.00":crs.getString("rs40")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs38")==null?"0.00":crs.getString("rs38")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs39")==null?"0.00":crs.getString("rs39")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs40")==null?"0.00":crs.getString("rs40")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	住宿和餐饮业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs41")==null?"0.00":crs.getString("rs41")).doubleValue() 
																+Double.valueOf(crs.getString("rs42")==null?"0.00":crs.getString("rs42")).doubleValue() +
																Double.valueOf(crs.getString("rs43")==null?"0.00":crs.getString("rs43")).doubleValue() 
																	+Double.valueOf(crs.getString("rs44")==null?"0.00":crs.getString("rs44")).doubleValue() +
																	Double.valueOf(crs.getString("rs45")==null?"0.00":crs.getString("rs45")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs41")==null?"0.00":crs.getString("rs41")).doubleValue() 
																+Double.valueOf(crs.getString("rs42")==null?"0.00":crs.getString("rs42")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs41")==null?"0.00":crs.getString("rs41")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs42")==null?"0.00":crs.getString("rs42")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs43")==null?"0.00":crs.getString("rs43")).doubleValue() 
																	+Double.valueOf(crs.getString("rs44")==null?"0.00":crs.getString("rs44")).doubleValue() +
																	Double.valueOf(crs.getString("rs45")==null?"0.00":crs.getString("rs45")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs43")==null?"0.00":crs.getString("rs43")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs44")==null?"0.00":crs.getString("rs44")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs45")==null?"0.00":crs.getString("rs45")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	金融业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs46")==null?"0.00":crs.getString("rs46")).doubleValue() 
																+Double.valueOf(crs.getString("rs47")==null?"0.00":crs.getString("rs47")).doubleValue() +
																Double.valueOf(crs.getString("rs48")==null?"0.00":crs.getString("rs48")).doubleValue() 
																	+Double.valueOf(crs.getString("rs49")==null?"0.00":crs.getString("rs49")).doubleValue() +
																	Double.valueOf(crs.getString("rs50")==null?"0.00":crs.getString("rs50")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs46")==null?"0.00":crs.getString("rs46")).doubleValue() 
																+Double.valueOf(crs.getString("rs47")==null?"0.00":crs.getString("rs47")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs46")==null?"0.00":crs.getString("rs46")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs47")==null?"0.00":crs.getString("rs47")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs48")==null?"0.00":crs.getString("rs48")).doubleValue() 
																	+Double.valueOf(crs.getString("rs49")==null?"0.00":crs.getString("rs49")).doubleValue() +
																	Double.valueOf(crs.getString("rs50")==null?"0.00":crs.getString("rs50")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs48")==null?"0.00":crs.getString("rs48")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs49")==null?"0.00":crs.getString("rs49")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs50")==null?"0.00":crs.getString("rs50")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	房地产业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs51")==null?"0.00":crs.getString("rs51")).doubleValue() 
																+Double.valueOf(crs.getString("rs52")==null?"0.00":crs.getString("rs52")).doubleValue() +
																Double.valueOf(crs.getString("rs53")==null?"0.00":crs.getString("rs53")).doubleValue() 
																	+Double.valueOf(crs.getString("rs54")==null?"0.00":crs.getString("rs54")).doubleValue() +
																	Double.valueOf(crs.getString("rs55")==null?"0.00":crs.getString("rs55")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs51")==null?"0.00":crs.getString("rs51")).doubleValue() 
																+Double.valueOf(crs.getString("rs52")==null?"0.00":crs.getString("rs52")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs51")==null?"0.00":crs.getString("rs51")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs52")==null?"0.00":crs.getString("rs52")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs53")==null?"0.00":crs.getString("rs53")).doubleValue() 
																	+Double.valueOf(crs.getString("rs54")==null?"0.00":crs.getString("rs54")).doubleValue() +
																	Double.valueOf(crs.getString("rs55")==null?"0.00":crs.getString("rs55")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs53")==null?"0.00":crs.getString("rs53")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs54")==null?"0.00":crs.getString("rs54")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs55")==null?"0.00":crs.getString("rs55")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	租赁和商务服务业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs56")==null?"0.00":crs.getString("rs56")).doubleValue() 
																+Double.valueOf(crs.getString("rs57")==null?"0.00":crs.getString("rs57")).doubleValue() +
																Double.valueOf(crs.getString("rs58")==null?"0.00":crs.getString("rs58")).doubleValue() 
																	+Double.valueOf(crs.getString("rs59")==null?"0.00":crs.getString("rs59")).doubleValue() +
																	Double.valueOf(crs.getString("rs60")==null?"0.00":crs.getString("rs60")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs56")==null?"0.00":crs.getString("rs56")).doubleValue() 
																+Double.valueOf(crs.getString("rs57")==null?"0.00":crs.getString("rs57")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs56")==null?"0.00":crs.getString("rs56")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs57")==null?"0.00":crs.getString("rs57")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs58")==null?"0.00":crs.getString("rs58")).doubleValue() 
																	+Double.valueOf(crs.getString("rs59")==null?"0.00":crs.getString("rs59")).doubleValue() +
																	Double.valueOf(crs.getString("rs60")==null?"0.00":crs.getString("rs60")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs58")==null?"0.00":crs.getString("rs58")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs59")==null?"0.00":crs.getString("rs59")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs60")==null?"0.00":crs.getString("rs60")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	科学研究、技术服务和地质勘查业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs61")==null?"0.00":crs.getString("rs61")).doubleValue() 
																+Double.valueOf(crs.getString("rs62")==null?"0.00":crs.getString("rs62")).doubleValue() +
																Double.valueOf(crs.getString("rs63")==null?"0.00":crs.getString("rs63")).doubleValue() 
																	+Double.valueOf(crs.getString("rs64")==null?"0.00":crs.getString("rs64")).doubleValue() +
																	Double.valueOf(crs.getString("rs65")==null?"0.00":crs.getString("rs65")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs61")==null?"0.00":crs.getString("rs61")).doubleValue() 
																+Double.valueOf(crs.getString("rs62")==null?"0.00":crs.getString("rs62")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs61")==null?"0.00":crs.getString("rs61")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs62")==null?"0.00":crs.getString("rs62")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs63")==null?"0.00":crs.getString("rs63")).doubleValue() 
																	+Double.valueOf(crs.getString("rs64")==null?"0.00":crs.getString("rs64")).doubleValue() +
																	Double.valueOf(crs.getString("rs65")==null?"0.00":crs.getString("rs65")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs63")==null?"0.00":crs.getString("rs63")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs64")==null?"0.00":crs.getString("rs64")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs65")==null?"0.00":crs.getString("rs65")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	水利、环境和公共设施管理业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs66")==null?"0.00":crs.getString("rs66")).doubleValue() 
																+Double.valueOf(crs.getString("rs67")==null?"0.00":crs.getString("rs67")).doubleValue() +
																Double.valueOf(crs.getString("rs68")==null?"0.00":crs.getString("rs68")).doubleValue() 
																	+Double.valueOf(crs.getString("rs69")==null?"0.00":crs.getString("rs69")).doubleValue() +
																	Double.valueOf(crs.getString("rs70")==null?"0.00":crs.getString("rs70")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs66")==null?"0.00":crs.getString("rs66")).doubleValue() 
																+Double.valueOf(crs.getString("rs67")==null?"0.00":crs.getString("rs67")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs66")==null?"0.00":crs.getString("rs66")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs67")==null?"0.00":crs.getString("rs67")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs68")==null?"0.00":crs.getString("rs68")).doubleValue() 
																	+Double.valueOf(crs.getString("rs69")==null?"0.00":crs.getString("rs69")).doubleValue() +
																	Double.valueOf(crs.getString("rs70")==null?"0.00":crs.getString("rs70")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs68")==null?"0.00":crs.getString("rs68")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs69")==null?"0.00":crs.getString("rs69")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs70")==null?"0.00":crs.getString("rs70")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	居民服务和其他服务业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs71")==null?"0.00":crs.getString("rs71")).doubleValue() 
																+Double.valueOf(crs.getString("rs72")==null?"0.00":crs.getString("rs72")).doubleValue() +
																Double.valueOf(crs.getString("rs73")==null?"0.00":crs.getString("rs73")).doubleValue() 
																	+Double.valueOf(crs.getString("rs74")==null?"0.00":crs.getString("rs74")).doubleValue() +
																	Double.valueOf(crs.getString("rs75")==null?"0.00":crs.getString("rs75")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs71")==null?"0.00":crs.getString("rs71")).doubleValue() 
																+Double.valueOf(crs.getString("rs72")==null?"0.00":crs.getString("rs72")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs71")==null?"0.00":crs.getString("rs71")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs72")==null?"0.00":crs.getString("rs72")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs73")==null?"0.00":crs.getString("rs73")).doubleValue() 
																	+Double.valueOf(crs.getString("rs74")==null?"0.00":crs.getString("rs74")).doubleValue() +
																	Double.valueOf(crs.getString("rs75")==null?"0.00":crs.getString("rs75")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs73")==null?"0.00":crs.getString("rs73")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs74")==null?"0.00":crs.getString("rs74")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs75")==null?"0.00":crs.getString("rs75")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	教育
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs76")==null?"0.00":crs.getString("rs76")).doubleValue() 
																+Double.valueOf(crs.getString("rs77")==null?"0.00":crs.getString("rs77")).doubleValue() +
																Double.valueOf(crs.getString("rs78")==null?"0.00":crs.getString("rs78")).doubleValue() 
																	+Double.valueOf(crs.getString("rs79")==null?"0.00":crs.getString("rs79")).doubleValue() +
																	Double.valueOf(crs.getString("rs80")==null?"0.00":crs.getString("rs80")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs76")==null?"0.00":crs.getString("rs76")).doubleValue() 
																+Double.valueOf(crs.getString("rs77")==null?"0.00":crs.getString("rs77")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs76")==null?"0.00":crs.getString("rs76")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs77")==null?"0.00":crs.getString("rs77")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs78")==null?"0.00":crs.getString("rs78")).doubleValue() 
																	+Double.valueOf(crs.getString("rs79")==null?"0.00":crs.getString("rs79")).doubleValue() +
																	Double.valueOf(crs.getString("rs80")==null?"0.00":crs.getString("rs80")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs78")==null?"0.00":crs.getString("rs78")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs79")==null?"0.00":crs.getString("rs79")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs80")==null?"0.00":crs.getString("rs80")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	卫生、社会保障和社会福利业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs81")==null?"0.00":crs.getString("rs81")).doubleValue() 
																+Double.valueOf(crs.getString("rs82")==null?"0.00":crs.getString("rs82")).doubleValue() +
																Double.valueOf(crs.getString("rs83")==null?"0.00":crs.getString("rs83")).doubleValue() 
																	+Double.valueOf(crs.getString("rs84")==null?"0.00":crs.getString("rs84")).doubleValue() +
																	Double.valueOf(crs.getString("rs85")==null?"0.00":crs.getString("rs85")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs81")==null?"0.00":crs.getString("rs81")).doubleValue() 
																+Double.valueOf(crs.getString("rs82")==null?"0.00":crs.getString("rs82")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs81")==null?"0.00":crs.getString("rs81")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs82")==null?"0.00":crs.getString("rs82")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs83")==null?"0.00":crs.getString("rs83")).doubleValue() 
																	+Double.valueOf(crs.getString("rs84")==null?"0.00":crs.getString("rs84")).doubleValue() +
																	Double.valueOf(crs.getString("rs85")==null?"0.00":crs.getString("rs85")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs83")==null?"0.00":crs.getString("rs83")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs84")==null?"0.00":crs.getString("rs84")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs85")==null?"0.00":crs.getString("rs85")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	文化、体育和娱乐业
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs86")==null?"0.00":crs.getString("rs86")).doubleValue() 
																+Double.valueOf(crs.getString("rs87")==null?"0.00":crs.getString("rs87")).doubleValue() +
																Double.valueOf(crs.getString("rs88")==null?"0.00":crs.getString("rs88")).doubleValue() 
																	+Double.valueOf(crs.getString("rs89")==null?"0.00":crs.getString("rs89")).doubleValue() +
																	Double.valueOf(crs.getString("rs90")==null?"0.00":crs.getString("rs90")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs86")==null?"0.00":crs.getString("rs86")).doubleValue() 
																+Double.valueOf(crs.getString("rs87")==null?"0.00":crs.getString("rs87")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs86")==null?"0.00":crs.getString("rs86")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs87")==null?"0.00":crs.getString("rs87")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs88")==null?"0.00":crs.getString("rs88")).doubleValue() 
																	+Double.valueOf(crs.getString("rs89")==null?"0.00":crs.getString("rs89")).doubleValue() +
																	Double.valueOf(crs.getString("rs90")==null?"0.00":crs.getString("rs90")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs88")==null?"0.00":crs.getString("rs88")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs89")==null?"0.00":crs.getString("rs89")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs90")==null?"0.00":crs.getString("rs90")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	公共管理和社会组织
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs91")==null?"0.00":crs.getString("rs91")).doubleValue() 
																+Double.valueOf(crs.getString("rs92")==null?"0.00":crs.getString("rs92")).doubleValue() +
																Double.valueOf(crs.getString("rs93")==null?"0.00":crs.getString("rs93")).doubleValue() 
																	+Double.valueOf(crs.getString("rs94")==null?"0.00":crs.getString("rs94")).doubleValue() +
																	Double.valueOf(crs.getString("rs95")==null?"0.00":crs.getString("rs95")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs91")==null?"0.00":crs.getString("rs91")).doubleValue() 
																+Double.valueOf(crs.getString("rs92")==null?"0.00":crs.getString("rs92")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs91")==null?"0.00":crs.getString("rs91")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs92")==null?"0.00":crs.getString("rs92")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs93")==null?"0.00":crs.getString("rs93")).doubleValue() 
																	+Double.valueOf(crs.getString("rs94")==null?"0.00":crs.getString("rs94")).doubleValue() +
																	Double.valueOf(crs.getString("rs95")==null?"0.00":crs.getString("rs95")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs93")==null?"0.00":crs.getString("rs93")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs94")==null?"0.00":crs.getString("rs94")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs95")==null?"0.00":crs.getString("rs95")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	国际组织
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs96")==null?"0.00":crs.getString("rs96")).doubleValue() 
																+Double.valueOf(crs.getString("rs97")==null?"0.00":crs.getString("rs97")).doubleValue() +
																Double.valueOf(crs.getString("rs98")==null?"0.00":crs.getString("rs98")).doubleValue() 
																	+Double.valueOf(crs.getString("rs99")==null?"0.00":crs.getString("rs99")).doubleValue() +
																	Double.valueOf(crs.getString("rs100")==null?"0.00":crs.getString("rs100")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs96")==null?"0.00":crs.getString("rs96")).doubleValue() 
																+Double.valueOf(crs.getString("rs97")==null?"0.00":crs.getString("rs97")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs96")==null?"0.00":crs.getString("rs96")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs97")==null?"0.00":crs.getString("rs97")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs98")==null?"0.00":crs.getString("rs98")).doubleValue() 
																	+Double.valueOf(crs.getString("rs99")==null?"0.00":crs.getString("rs99")).doubleValue() +
																	Double.valueOf(crs.getString("rs100")==null?"0.00":crs.getString("rs100")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs98")==null?"0.00":crs.getString("rs98")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs99")==null?"0.00":crs.getString("rs99")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs100")==null?"0.00":crs.getString("rs100")).doubleValue() )%>
																</td>
															</tr>
															<tr bgcolor=#FFFFFF class=data>
    															<td class=data align="left" >
																	对境外贷款
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs101")==null?"0.00":crs.getString("rs101")).doubleValue() 
																+Double.valueOf(crs.getString("rs102")==null?"0.00":crs.getString("rs102")).doubleValue() +
																Double.valueOf(crs.getString("rs103")==null?"0.00":crs.getString("rs103")).doubleValue() 
																	+Double.valueOf(crs.getString("rs104")==null?"0.00":crs.getString("rs104")).doubleValue() +
																	Double.valueOf(crs.getString("rs105")==null?"0.00":crs.getString("rs105")).doubleValue() )
																 %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs101")==null?"0.00":crs.getString("rs101")).doubleValue() 
																+Double.valueOf(crs.getString("rs102")==null?"0.00":crs.getString("rs102")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs101")==null?"0.00":crs.getString("rs101")).doubleValue()  )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs102")==null?"0.00":crs.getString("rs102")).doubleValue() )%>
																</td>
																<td class=data align="right">
																	<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs103")==null?"0.00":crs.getString("rs103")).doubleValue() 
																	+Double.valueOf(crs.getString("rs104")==null?"0.00":crs.getString("rs104")).doubleValue() +
																	Double.valueOf(crs.getString("rs105")==null?"0.00":crs.getString("rs105")).doubleValue() 
																	)%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs103")==null?"0.00":crs.getString("rs103")).doubleValue() ) %>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs104")==null?"0.00":crs.getString("rs104")).doubleValue() )%>
																</td>
																<td class=data align="right">
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs105")==null?"0.00":crs.getString("rs105")).doubleValue() )%>
																</td>
															</tr>
															
    														<tr bgcolor=#E7E9CF  class=data >
															<td align=center>
																合计:
															</td>
														     <td align=right>
														<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs1")==null?"0.00":crs.getString("rs1")).doubleValue() 
																+Double.valueOf(crs.getString("rs2")==null?"0.00":crs.getString("rs2")).doubleValue() +
																Double.valueOf(crs.getString("rs3")==null?"0.00":crs.getString("rs3")).doubleValue() 
																	+Double.valueOf(crs.getString("rs4")==null?"0.00":crs.getString("rs4")).doubleValue() +
																	Double.valueOf(crs.getString("rs5")==null?"0.00":crs.getString("rs5")).doubleValue() +
																	Double.valueOf(crs.getString("rs6")==null?"0.00":crs.getString("rs6")).doubleValue() 
																+Double.valueOf(crs.getString("rs7")==null?"0.00":crs.getString("rs7")).doubleValue() +
																Double.valueOf(crs.getString("rs8")==null?"0.00":crs.getString("rs8")).doubleValue() 
																	+Double.valueOf(crs.getString("rs9")==null?"0.00":crs.getString("rs9")).doubleValue() +
																	Double.valueOf(crs.getString("rs10")==null?"0.00":crs.getString("rs10")).doubleValue() +
																	Double.valueOf(crs.getString("rs11")==null?"0.00":crs.getString("rs11")).doubleValue() 
																   +Double.valueOf(crs.getString("rs12")==null?"0.00":crs.getString("rs12")).doubleValue() +
																    Double.valueOf(crs.getString("rs13")==null?"0.00":crs.getString("rs13")).doubleValue() 
																   +Double.valueOf(crs.getString("rs14")==null?"0.00":crs.getString("rs14")).doubleValue() +
																	Double.valueOf(crs.getString("rs15")==null?"0.00":crs.getString("rs15")).doubleValue() +
																	Double.valueOf(crs.getString("rs16")==null?"0.00":crs.getString("rs16")).doubleValue() 
																   +Double.valueOf(crs.getString("rs17")==null?"0.00":crs.getString("rs17")).doubleValue() +
																    Double.valueOf(crs.getString("rs18")==null?"0.00":crs.getString("rs18")).doubleValue() 
																   +Double.valueOf(crs.getString("rs19")==null?"0.00":crs.getString("rs19")).doubleValue() +
																	Double.valueOf(crs.getString("rs20")==null?"0.00":crs.getString("rs20")).doubleValue() +
																	Double.valueOf(crs.getString("rs21")==null?"0.00":crs.getString("rs21")).doubleValue() 
																+Double.valueOf(crs.getString("rs22")==null?"0.00":crs.getString("rs22")).doubleValue() +
																Double.valueOf(crs.getString("rs23")==null?"0.00":crs.getString("rs23")).doubleValue() 
																	+Double.valueOf(crs.getString("rs24")==null?"0.00":crs.getString("rs24")).doubleValue() +
																	Double.valueOf(crs.getString("rs25")==null?"0.00":crs.getString("rs25")).doubleValue() +
																	Double.valueOf(crs.getString("rs26")==null?"0.00":crs.getString("rs26")).doubleValue() 
																+Double.valueOf(crs.getString("rs27")==null?"0.00":crs.getString("rs27")).doubleValue() +
																Double.valueOf(crs.getString("rs28")==null?"0.00":crs.getString("rs28")).doubleValue() 
																	+Double.valueOf(crs.getString("rs29")==null?"0.00":crs.getString("rs29")).doubleValue() +
																	Double.valueOf(crs.getString("rs30")==null?"0.00":crs.getString("rs30")).doubleValue() +
																	Double.valueOf(crs.getString("rs31")==null?"0.00":crs.getString("rs31")).doubleValue() 
																+Double.valueOf(crs.getString("rs32")==null?"0.00":crs.getString("rs32")).doubleValue() +
																Double.valueOf(crs.getString("rs33")==null?"0.00":crs.getString("rs33")).doubleValue() 
																	+Double.valueOf(crs.getString("rs34")==null?"0.00":crs.getString("rs34")).doubleValue() +
																	Double.valueOf(crs.getString("rs35")==null?"0.00":crs.getString("rs35")).doubleValue() +
																	Double.valueOf(crs.getString("rs36")==null?"0.00":crs.getString("rs36")).doubleValue() 
																+Double.valueOf(crs.getString("rs37")==null?"0.00":crs.getString("rs37")).doubleValue() +
																Double.valueOf(crs.getString("rs38")==null?"0.00":crs.getString("rs38")).doubleValue() 
																	+Double.valueOf(crs.getString("rs39")==null?"0.00":crs.getString("rs39")).doubleValue() +
																	Double.valueOf(crs.getString("rs40")==null?"0.00":crs.getString("rs40")).doubleValue() +
																	Double.valueOf(crs.getString("rs41")==null?"0.00":crs.getString("rs41")).doubleValue() 
																+Double.valueOf(crs.getString("rs42")==null?"0.00":crs.getString("rs42")).doubleValue() +
																Double.valueOf(crs.getString("rs43")==null?"0.00":crs.getString("rs43")).doubleValue() 
																	+Double.valueOf(crs.getString("rs44")==null?"0.00":crs.getString("rs44")).doubleValue() +
																	Double.valueOf(crs.getString("rs45")==null?"0.00":crs.getString("rs45")).doubleValue() +
																	Double.valueOf(crs.getString("rs46")==null?"0.00":crs.getString("rs46")).doubleValue() 
																+Double.valueOf(crs.getString("rs47")==null?"0.00":crs.getString("rs47")).doubleValue() +
																Double.valueOf(crs.getString("rs48")==null?"0.00":crs.getString("rs48")).doubleValue() 
																	+Double.valueOf(crs.getString("rs49")==null?"0.00":crs.getString("rs49")).doubleValue() +
																	Double.valueOf(crs.getString("rs50")==null?"0.00":crs.getString("rs50")).doubleValue() +
																	Double.valueOf(crs.getString("rs51")==null?"0.00":crs.getString("rs51")).doubleValue() 
																+Double.valueOf(crs.getString("rs52")==null?"0.00":crs.getString("rs52")).doubleValue() +
																Double.valueOf(crs.getString("rs53")==null?"0.00":crs.getString("rs53")).doubleValue() 
																	+Double.valueOf(crs.getString("rs54")==null?"0.00":crs.getString("rs54")).doubleValue() +
																	Double.valueOf(crs.getString("rs55")==null?"0.00":crs.getString("rs55")).doubleValue() +
																	Double.valueOf(crs.getString("rs56")==null?"0.00":crs.getString("rs56")).doubleValue() 
																+Double.valueOf(crs.getString("rs57")==null?"0.00":crs.getString("rs57")).doubleValue() +
																Double.valueOf(crs.getString("rs58")==null?"0.00":crs.getString("rs58")).doubleValue() 
																	+Double.valueOf(crs.getString("rs59")==null?"0.00":crs.getString("rs59")).doubleValue() +
																	Double.valueOf(crs.getString("rs60")==null?"0.00":crs.getString("rs60")).doubleValue() +
																	Double.valueOf(crs.getString("rs61")==null?"0.00":crs.getString("rs61")).doubleValue() 
																+Double.valueOf(crs.getString("rs62")==null?"0.00":crs.getString("rs62")).doubleValue() +
																Double.valueOf(crs.getString("rs63")==null?"0.00":crs.getString("rs63")).doubleValue() 
																	+Double.valueOf(crs.getString("rs64")==null?"0.00":crs.getString("rs64")).doubleValue() +
																	Double.valueOf(crs.getString("rs65")==null?"0.00":crs.getString("rs65")).doubleValue() +
																	Double.valueOf(crs.getString("rs66")==null?"0.00":crs.getString("rs66")).doubleValue() 
																+Double.valueOf(crs.getString("rs67")==null?"0.00":crs.getString("rs67")).doubleValue() +
																Double.valueOf(crs.getString("rs68")==null?"0.00":crs.getString("rs68")).doubleValue() 
																	+Double.valueOf(crs.getString("rs69")==null?"0.00":crs.getString("rs69")).doubleValue() +
																	Double.valueOf(crs.getString("rs70")==null?"0.00":crs.getString("rs70")).doubleValue() +
																	Double.valueOf(crs.getString("rs71")==null?"0.00":crs.getString("rs71")).doubleValue() 
																+Double.valueOf(crs.getString("rs72")==null?"0.00":crs.getString("rs72")).doubleValue() +
																Double.valueOf(crs.getString("rs73")==null?"0.00":crs.getString("rs73")).doubleValue() 
																	+Double.valueOf(crs.getString("rs74")==null?"0.00":crs.getString("rs74")).doubleValue() +
																	Double.valueOf(crs.getString("rs75")==null?"0.00":crs.getString("rs75")).doubleValue() +
																	Double.valueOf(crs.getString("rs76")==null?"0.00":crs.getString("rs76")).doubleValue() 
																+Double.valueOf(crs.getString("rs77")==null?"0.00":crs.getString("rs77")).doubleValue() +
																Double.valueOf(crs.getString("rs78")==null?"0.00":crs.getString("rs78")).doubleValue() 
																	+Double.valueOf(crs.getString("rs79")==null?"0.00":crs.getString("rs79")).doubleValue() +
																	Double.valueOf(crs.getString("rs80")==null?"0.00":crs.getString("rs80")).doubleValue() +
																	Double.valueOf(crs.getString("rs81")==null?"0.00":crs.getString("rs81")).doubleValue() 
																+Double.valueOf(crs.getString("rs82")==null?"0.00":crs.getString("rs82")).doubleValue() +
																Double.valueOf(crs.getString("rs83")==null?"0.00":crs.getString("rs83")).doubleValue() 
																	+Double.valueOf(crs.getString("rs84")==null?"0.00":crs.getString("rs84")).doubleValue() +
																	Double.valueOf(crs.getString("rs85")==null?"0.00":crs.getString("rs85")).doubleValue() +
																	Double.valueOf(crs.getString("rs86")==null?"0.00":crs.getString("rs86")).doubleValue() +
																	Double.valueOf(crs.getString("rs87")==null?"0.00":crs.getString("rs87")).doubleValue() 
																+Double.valueOf(crs.getString("rs88")==null?"0.00":crs.getString("rs88")).doubleValue() +
																Double.valueOf(crs.getString("rs89")==null?"0.00":crs.getString("rs89")).doubleValue() 
																	+Double.valueOf(crs.getString("rs90")==null?"0.00":crs.getString("rs90")).doubleValue() +
																	Double.valueOf(crs.getString("rs91")==null?"0.00":crs.getString("rs91")).doubleValue() 
																+Double.valueOf(crs.getString("rs92")==null?"0.00":crs.getString("rs92")).doubleValue() +
																Double.valueOf(crs.getString("rs93")==null?"0.00":crs.getString("rs93")).doubleValue() 
																	+Double.valueOf(crs.getString("rs94")==null?"0.00":crs.getString("rs94")).doubleValue() +
																	Double.valueOf(crs.getString("rs95")==null?"0.00":crs.getString("rs95")).doubleValue() +
																	Double.valueOf(crs.getString("rs96")==null?"0.00":crs.getString("rs96")).doubleValue() 
																+Double.valueOf(crs.getString("rs97")==null?"0.00":crs.getString("rs97")).doubleValue() +
																Double.valueOf(crs.getString("rs98")==null?"0.00":crs.getString("rs98")).doubleValue() 
																	+Double.valueOf(crs.getString("rs99")==null?"0.00":crs.getString("rs99")).doubleValue() +
																	Double.valueOf(crs.getString("rs100")==null?"0.00":crs.getString("rs100")).doubleValue() +
																	Double.valueOf(crs.getString("rs101")==null?"0.00":crs.getString("rs101")).doubleValue() 
																+Double.valueOf(crs.getString("rs102")==null?"0.00":crs.getString("rs102")).doubleValue() +
																Double.valueOf(crs.getString("rs103")==null?"0.00":crs.getString("rs103")).doubleValue() 
																	+Double.valueOf(crs.getString("rs104")==null?"0.00":crs.getString("rs104")).doubleValue() +
																	Double.valueOf(crs.getString("rs105")==null?"0.00":crs.getString("rs105")).doubleValue() 
																	)
																 %>
															</td>
															<td align=right>
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs1")==null?"0.00":crs.getString("rs1")).doubleValue() 
																+Double.valueOf(crs.getString("rs2")==null?"0.00":crs.getString("rs2")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs6")==null?"0.00":crs.getString("rs6")).doubleValue() 
																+Double.valueOf(crs.getString("rs7")==null?"0.00":crs.getString("rs7")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs11")==null?"0.00":crs.getString("rs11")).doubleValue() 
																   +Double.valueOf(crs.getString("rs12")==null?"0.00":crs.getString("rs12")).doubleValue() +
																    
																	Double.valueOf(crs.getString("rs16")==null?"0.00":crs.getString("rs16")).doubleValue() 
																   +Double.valueOf(crs.getString("rs17")==null?"0.00":crs.getString("rs17")).doubleValue() +
																   
																	Double.valueOf(crs.getString("rs21")==null?"0.00":crs.getString("rs21")).doubleValue() 
																+Double.valueOf(crs.getString("rs22")==null?"0.00":crs.getString("rs22")).doubleValue() +
																	Double.valueOf(crs.getString("rs26")==null?"0.00":crs.getString("rs26")).doubleValue() 
																+Double.valueOf(crs.getString("rs27")==null?"0.00":crs.getString("rs27")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs31")==null?"0.00":crs.getString("rs31")).doubleValue() 
																+Double.valueOf(crs.getString("rs32")==null?"0.00":crs.getString("rs32")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs36")==null?"0.00":crs.getString("rs36")).doubleValue() 
																+Double.valueOf(crs.getString("rs37")==null?"0.00":crs.getString("rs37")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs41")==null?"0.00":crs.getString("rs41")).doubleValue() 
																+Double.valueOf(crs.getString("rs42")==null?"0.00":crs.getString("rs42")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs46")==null?"0.00":crs.getString("rs46")).doubleValue() 
																+Double.valueOf(crs.getString("rs47")==null?"0.00":crs.getString("rs47")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs51")==null?"0.00":crs.getString("rs51")).doubleValue() 
																+Double.valueOf(crs.getString("rs52")==null?"0.00":crs.getString("rs52")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs56")==null?"0.00":crs.getString("rs56")).doubleValue() 
																+Double.valueOf(crs.getString("rs57")==null?"0.00":crs.getString("rs57")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs61")==null?"0.00":crs.getString("rs61")).doubleValue() 
																+Double.valueOf(crs.getString("rs62")==null?"0.00":crs.getString("rs62")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs66")==null?"0.00":crs.getString("rs66")).doubleValue() 
																+Double.valueOf(crs.getString("rs67")==null?"0.00":crs.getString("rs67")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs71")==null?"0.00":crs.getString("rs71")).doubleValue() 
																+Double.valueOf(crs.getString("rs72")==null?"0.00":crs.getString("rs72")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs76")==null?"0.00":crs.getString("rs76")).doubleValue() 
																+Double.valueOf(crs.getString("rs77")==null?"0.00":crs.getString("rs77")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs81")==null?"0.00":crs.getString("rs81")).doubleValue() 
																+Double.valueOf(crs.getString("rs82")==null?"0.00":crs.getString("rs82")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs86")==null?"0.00":crs.getString("rs86")).doubleValue() 
																+Double.valueOf(crs.getString("rs87")==null?"0.00":crs.getString("rs87")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs91")==null?"0.00":crs.getString("rs91")).doubleValue() 
																+Double.valueOf(crs.getString("rs92")==null?"0.00":crs.getString("rs92")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs96")==null?"0.00":crs.getString("rs96")).doubleValue() 
																+Double.valueOf(crs.getString("rs97")==null?"0.00":crs.getString("rs97")).doubleValue() +
																
																	Double.valueOf(crs.getString("rs101")==null?"0.00":crs.getString("rs101")).doubleValue() 
																+Double.valueOf(crs.getString("rs102")==null?"0.00":crs.getString("rs102")).doubleValue() 
																
																	)
																 %>
															</td>
															<td align=right>
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs1")==null?"0.00":crs.getString("rs1")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs6")==null?"0.00":crs.getString("rs6")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs11")==null?"0.00":crs.getString("rs11")).doubleValue() 
																   +
																	Double.valueOf(crs.getString("rs16")==null?"0.00":crs.getString("rs16")).doubleValue() 
																   +
																	Double.valueOf(crs.getString("rs21")==null?"0.00":crs.getString("rs21")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs26")==null?"0.00":crs.getString("rs26")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs31")==null?"0.00":crs.getString("rs31")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs36")==null?"0.00":crs.getString("rs36")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs41")==null?"0.00":crs.getString("rs41")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs46")==null?"0.00":crs.getString("rs46")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs51")==null?"0.00":crs.getString("rs51")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs56")==null?"0.00":crs.getString("rs56")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs61")==null?"0.00":crs.getString("rs61")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs66")==null?"0.00":crs.getString("rs66")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs71")==null?"0.00":crs.getString("rs71")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs76")==null?"0.00":crs.getString("rs76")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs81")==null?"0.00":crs.getString("rs81")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs86")==null?"0.00":crs.getString("rs86")).doubleValue() +
																	
																	Double.valueOf(crs.getString("rs91")==null?"0.00":crs.getString("rs91")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs96")==null?"0.00":crs.getString("rs96")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs101")==null?"0.00":crs.getString("rs101")).doubleValue() 
																
																	)
																 %>
															</td>
															<td align=right>
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs2")==null?"0.00":crs.getString("rs2")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs7")==null?"0.00":crs.getString("rs7")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs12")==null?"0.00":crs.getString("rs12")).doubleValue() 
																   +
																	Double.valueOf(crs.getString("rs17")==null?"0.00":crs.getString("rs17")).doubleValue() 
																   +
																	Double.valueOf(crs.getString("rs22")==null?"0.00":crs.getString("rs22")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs27")==null?"0.00":crs.getString("rs27")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs32")==null?"0.00":crs.getString("rs32")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs37")==null?"0.00":crs.getString("rs37")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs42")==null?"0.00":crs.getString("rs42")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs47")==null?"0.00":crs.getString("rs47")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs52")==null?"0.00":crs.getString("rs52")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs57")==null?"0.00":crs.getString("rs57")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs62")==null?"0.00":crs.getString("rs62")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs67")==null?"0.00":crs.getString("rs67")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs72")==null?"0.00":crs.getString("rs72")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs77")==null?"0.00":crs.getString("rs77")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs82")==null?"0.00":crs.getString("rs82")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs87")==null?"0.00":crs.getString("rs87")).doubleValue() +
																	
																	Double.valueOf(crs.getString("rs92")==null?"0.00":crs.getString("rs92")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs97")==null?"0.00":crs.getString("rs97")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs102")==null?"0.00":crs.getString("rs102")).doubleValue() 
																
																	)
																 %>
															</td>
															<td align=right>
																<%=DBUtil.doubleToStr1(
																Double.valueOf(crs.getString("rs3")==null?"0.00":crs.getString("rs3")).doubleValue() 
																	+Double.valueOf(crs.getString("rs4")==null?"0.00":crs.getString("rs4")).doubleValue() +
																	Double.valueOf(crs.getString("rs5")==null?"0.00":crs.getString("rs5")).doubleValue() +
																	+
																Double.valueOf(crs.getString("rs8")==null?"0.00":crs.getString("rs8")).doubleValue() 
																	+Double.valueOf(crs.getString("rs9")==null?"0.00":crs.getString("rs9")).doubleValue() +
																	Double.valueOf(crs.getString("rs10")==null?"0.00":crs.getString("rs10")).doubleValue() +
																	
																    Double.valueOf(crs.getString("rs13")==null?"0.00":crs.getString("rs13")).doubleValue() 
																   +Double.valueOf(crs.getString("rs14")==null?"0.00":crs.getString("rs14")).doubleValue() +
																	Double.valueOf(crs.getString("rs15")==null?"0.00":crs.getString("rs15")).doubleValue() +
																	
																    Double.valueOf(crs.getString("rs18")==null?"0.00":crs.getString("rs18")).doubleValue() 
																   +Double.valueOf(crs.getString("rs19")==null?"0.00":crs.getString("rs19")).doubleValue() +
																	Double.valueOf(crs.getString("rs20")==null?"0.00":crs.getString("rs20")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs23")==null?"0.00":crs.getString("rs23")).doubleValue() 
																	+Double.valueOf(crs.getString("rs24")==null?"0.00":crs.getString("rs24")).doubleValue() +
																	Double.valueOf(crs.getString("rs25")==null?"0.00":crs.getString("rs25")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs28")==null?"0.00":crs.getString("rs28")).doubleValue() 
																	+Double.valueOf(crs.getString("rs29")==null?"0.00":crs.getString("rs29")).doubleValue() +
																	Double.valueOf(crs.getString("rs30")==null?"0.00":crs.getString("rs30")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs33")==null?"0.00":crs.getString("rs33")).doubleValue() 
																	+Double.valueOf(crs.getString("rs34")==null?"0.00":crs.getString("rs34")).doubleValue() +
																	Double.valueOf(crs.getString("rs35")==null?"0.00":crs.getString("rs35")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs38")==null?"0.00":crs.getString("rs38")).doubleValue() 
																	+Double.valueOf(crs.getString("rs39")==null?"0.00":crs.getString("rs39")).doubleValue() +
																	Double.valueOf(crs.getString("rs40")==null?"0.00":crs.getString("rs40")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs43")==null?"0.00":crs.getString("rs43")).doubleValue() 
																	+Double.valueOf(crs.getString("rs44")==null?"0.00":crs.getString("rs44")).doubleValue() +
																	Double.valueOf(crs.getString("rs45")==null?"0.00":crs.getString("rs45")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs48")==null?"0.00":crs.getString("rs48")).doubleValue() 
																	+Double.valueOf(crs.getString("rs49")==null?"0.00":crs.getString("rs49")).doubleValue() +
																	Double.valueOf(crs.getString("rs50")==null?"0.00":crs.getString("rs50")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs53")==null?"0.00":crs.getString("rs53")).doubleValue() 
																	+Double.valueOf(crs.getString("rs54")==null?"0.00":crs.getString("rs54")).doubleValue() +
																	Double.valueOf(crs.getString("rs55")==null?"0.00":crs.getString("rs55")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs58")==null?"0.00":crs.getString("rs58")).doubleValue() 
																	+Double.valueOf(crs.getString("rs59")==null?"0.00":crs.getString("rs59")).doubleValue() +
																	Double.valueOf(crs.getString("rs60")==null?"0.00":crs.getString("rs60")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs63")==null?"0.00":crs.getString("rs63")).doubleValue() 
																	+Double.valueOf(crs.getString("rs64")==null?"0.00":crs.getString("rs64")).doubleValue() +
																	Double.valueOf(crs.getString("rs65")==null?"0.00":crs.getString("rs65")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs68")==null?"0.00":crs.getString("rs68")).doubleValue() 
																	+Double.valueOf(crs.getString("rs69")==null?"0.00":crs.getString("rs69")).doubleValue() +
																	Double.valueOf(crs.getString("rs70")==null?"0.00":crs.getString("rs70")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs73")==null?"0.00":crs.getString("rs73")).doubleValue() 
																	+Double.valueOf(crs.getString("rs74")==null?"0.00":crs.getString("rs74")).doubleValue() +
																	Double.valueOf(crs.getString("rs75")==null?"0.00":crs.getString("rs75")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs78")==null?"0.00":crs.getString("rs78")).doubleValue() 
																	+Double.valueOf(crs.getString("rs79")==null?"0.00":crs.getString("rs79")).doubleValue() +
																	Double.valueOf(crs.getString("rs80")==null?"0.00":crs.getString("rs80")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs83")==null?"0.00":crs.getString("rs83")).doubleValue() 
																	+Double.valueOf(crs.getString("rs84")==null?"0.00":crs.getString("rs84")).doubleValue() +
																	Double.valueOf(crs.getString("rs85")==null?"0.00":crs.getString("rs85")).doubleValue() +
																		
																Double.valueOf(crs.getString("rs83")==null?"0.00":crs.getString("rs88")).doubleValue() 
																	+Double.valueOf(crs.getString("rs84")==null?"0.00":crs.getString("rs89")).doubleValue() +
																	Double.valueOf(crs.getString("rs85")==null?"0.00":crs.getString("rs90")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs93")==null?"0.00":crs.getString("rs93")).doubleValue() 
																	+Double.valueOf(crs.getString("rs94")==null?"0.00":crs.getString("rs94")).doubleValue() +
																	Double.valueOf(crs.getString("rs95")==null?"0.00":crs.getString("rs95")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs98")==null?"0.00":crs.getString("rs98")).doubleValue() 
																	+Double.valueOf(crs.getString("rs99")==null?"0.00":crs.getString("rs99")).doubleValue() +
																	Double.valueOf(crs.getString("rs100")==null?"0.00":crs.getString("rs100")).doubleValue() +
																	
																Double.valueOf(crs.getString("rs103")==null?"0.00":crs.getString("rs103")).doubleValue() 
																	+Double.valueOf(crs.getString("rs104")==null?"0.00":crs.getString("rs104")).doubleValue() +
																	Double.valueOf(crs.getString("rs105")==null?"0.00":crs.getString("rs105")).doubleValue() 
																	)
																 %>
															</td>
															<td align=right>
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs3")==null?"0.00":crs.getString("rs3")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs8")==null?"0.00":crs.getString("rs8")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs13")==null?"0.00":crs.getString("rs13")).doubleValue() 
																   +
																	Double.valueOf(crs.getString("rs18")==null?"0.00":crs.getString("rs18")).doubleValue() 
																   +
																	Double.valueOf(crs.getString("rs23")==null?"0.00":crs.getString("rs23")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs28")==null?"0.00":crs.getString("rs28")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs33")==null?"0.00":crs.getString("rs33")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs38")==null?"0.00":crs.getString("rs38")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs43")==null?"0.00":crs.getString("rs43")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs48")==null?"0.00":crs.getString("rs48")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs53")==null?"0.00":crs.getString("rs53")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs58")==null?"0.00":crs.getString("rs58")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs63")==null?"0.00":crs.getString("rs63")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs68")==null?"0.00":crs.getString("rs68")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs73")==null?"0.00":crs.getString("rs73")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs78")==null?"0.00":crs.getString("rs78")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs83")==null?"0.00":crs.getString("rs83")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs88")==null?"0.00":crs.getString("rs88")).doubleValue() +
																	
																	Double.valueOf(crs.getString("rs93")==null?"0.00":crs.getString("rs93")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs98")==null?"0.00":crs.getString("rs98")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs103")==null?"0.00":crs.getString("rs103")).doubleValue() 
																
																	)
																 %>
															</td>
															<td align=right>
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs4")==null?"0.00":crs.getString("rs4")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs9")==null?"0.00":crs.getString("rs9")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs14")==null?"0.00":crs.getString("rs14")).doubleValue() 
																   +
																	Double.valueOf(crs.getString("rs19")==null?"0.00":crs.getString("rs19")).doubleValue() 
																   +
																	Double.valueOf(crs.getString("rs24")==null?"0.00":crs.getString("rs24")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs29")==null?"0.00":crs.getString("rs29")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs34")==null?"0.00":crs.getString("rs34")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs39")==null?"0.00":crs.getString("rs39")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs44")==null?"0.00":crs.getString("rs44")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs49")==null?"0.00":crs.getString("rs49")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs54")==null?"0.00":crs.getString("rs54")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs59")==null?"0.00":crs.getString("rs59")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs64")==null?"0.00":crs.getString("rs64")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs69")==null?"0.00":crs.getString("rs69")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs74")==null?"0.00":crs.getString("rs74")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs79")==null?"0.00":crs.getString("rs79")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs84")==null?"0.00":crs.getString("rs84")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs89")==null?"0.00":crs.getString("rs89")).doubleValue() +
																	
																	Double.valueOf(crs.getString("rs94")==null?"0.00":crs.getString("rs94")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs99")==null?"0.00":crs.getString("rs99")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs104")==null?"0.00":crs.getString("rs104")).doubleValue() 
																
																	)
																 %>
															</td>
															<td align=right>
																<%=DBUtil.doubleToStr1(Double.valueOf(crs.getString("rs5")==null?"0.00":crs.getString("rs5")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs10")==null?"0.00":crs.getString("rs10")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs15")==null?"0.00":crs.getString("rs15")).doubleValue() 
																   +
																	Double.valueOf(crs.getString("rs20")==null?"0.00":crs.getString("rs20")).doubleValue() 
																   +
																	Double.valueOf(crs.getString("rs25")==null?"0.00":crs.getString("rs25")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs30")==null?"0.00":crs.getString("rs30")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs35")==null?"0.00":crs.getString("rs35")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs40")==null?"0.00":crs.getString("rs40")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs45")==null?"0.00":crs.getString("rs45")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs50")==null?"0.00":crs.getString("rs50")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs55")==null?"0.00":crs.getString("rs55")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs60")==null?"0.00":crs.getString("rs60")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs65")==null?"0.00":crs.getString("rs65")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs70")==null?"0.00":crs.getString("rs70")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs75")==null?"0.00":crs.getString("rs75")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs80")==null?"0.00":crs.getString("rs80")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs85")==null?"0.00":crs.getString("rs85")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs90")==null?"0.00":crs.getString("rs90")).doubleValue() +
																	
																	Double.valueOf(crs.getString("rs95")==null?"0.00":crs.getString("rs95")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs100")==null?"0.00":crs.getString("rs100")).doubleValue() 
																+
																	Double.valueOf(crs.getString("rs105")==null?"0.00":crs.getString("rs105")).doubleValue() 
																
																	)
																	
																 %>
																 
															</td>
															</tr>
															<%
															}
															 %>
													</TBODY>
													
												</TABLE>
											</div>
												<table cellSpacing=0 cellPadding=0 width=100% border=0 class=head>
													<tr>


													
														<td align=right>
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
<script>

document.listform.brhidd.value='<%=br%>';

document.listform.creadate.value='<%=create%>'
</script>
