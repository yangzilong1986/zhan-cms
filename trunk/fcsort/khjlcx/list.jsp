<%@ page language="java" pageEncoding="GB2312" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%--
=============================================== 
Title:五级分类分类统计页面
Description:五级分类分类统计页面
 * @version  $Revision: 1.4 $  $Date: 2007/05/30 03:03:35 $
 * @author   houcs
 * <p/>修改：$Author: houcs $
=============================================== 
--%>
<%
    //***************************页面属性*****************************
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String currentPath = basePath + "fcsort/khjlcx/";
    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("currentPath", currentPath);
    ConnectionManager manager = ConnectionManager.getInstance();
    //*******************************表头SQL*********************************************
    String create = request.getParameter("creadate");
    String colspan = "7";
    String sql = "select DT from FCPRD  where INITIALIZED=1 order by SEQNO desc ";
    CachedRowSet crs = manager.getRs(sql);
    if (create == null) {
        String sql2 = "select max(dt) mdt from FCPRD  where INITIALIZED=1";
        CachedRowSet crs2 = manager.getRs(sql2);
        String dt = "";
        while (crs2.next()) {
            dt = crs2.getString("mdt");
            create = dt;
        }
    }
    //***********页面参数*********************************************
    String startdate=request.getParameter("startdate")==null?"":request.getParameter("startdate");
	String enddate=request.getParameter("enddate")==null?"":request.getParameter("enddate");
    String params = "";
    String strScbrhId = request.getParameter("brhidd") == null ? "" : request.getParameter("brhidd");
    String selectddate = request.getParameter("creadate") == null ? "" : request.getParameter("creadate");
    //*************网点处理*********************************************
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String strUserName = um.getUserName();
    if (strScbrhId == null || strScbrhId.equals("") || strScbrhId.equals("0")){
         String brno = SCUser.getBrhId(strUserName).trim();//网点编号
    String SUBBRHIDs = SCBranch.getSubBranchAll(brno).trim();
    int m = SUBBRHIDs.length() / 9;
    String[] SUBBRHID = new String[m];
    if (SUBBRHIDs.length() > 9) {
        SUBBRHID = SUBBRHIDs.split(",");
    } 
    else
     {
        SUBBRHID[0] = SUBBRHIDs;
    }
    strScbrhId= SUBBRHID[0];
    System.out.println(strScbrhId+"....................................");
    }
    String strScbrhName=SCBranch.getSName(strScbrhId);
    String br = strScbrhId;
    String brhiddd = SCUser.getBrhId(strUserName).trim();//网点编号
    String SUBBRHIDs = SCBranch.getSubBranchAll(brhiddd).trim();
    int m = SUBBRHIDs.length() / 9;
    String[] SUBBRHID = new String[m];
    if (SUBBRHIDs.length() > 9) {
        SUBBRHID = SUBBRHIDs.split(",");
    } 
    else
     {
        SUBBRHID[0] = SUBBRHIDs;
    }

    //************************数据显示部分*****************************

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>客户经理经营情况查询表</title>
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
        var select = "<%=selectddate%>";
        var surl = "<%=currentPath%>";
        var params = "<%=params%>";
        var yeardays = "";
        var startdate = "<%=startdate%>";
        var enddate = "<%=enddate%>";
        var brhid = "<%=br%>";
        var create = "<%=create%>";
        var colspan = "<%=colspan%>";
        var cou = "";
        function reset1(){
        document.listform.startdate.value="";
        document.listform.enddate.value="";
		}
    </script>
</head>
<body background="/images/checks_02.jpg">
<form action="<%=currentPath%>list.jsp" name="listform" id="listform">
<input name="referValue" type="hidden">
<input type="hidden" name="brhid" value="<%=strScbrhId%>">

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
                <input type="button" value=" 检 索 " class="button" onclick="document.listform.submit();">
                &nbsp;&nbsp;
               <input type="button" value=" 重 置 " class="button"onclick="reset1()">
															&nbsp;&nbsp;
                <input type=button class=button value=" 打 印 " onclick="printTable()">
                &nbsp;&nbsp;
                <input type="button" class="button" value=" 关 闭 " onclick="self.close()">
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
                                for (int j = 0; j < SUBBRHID.length; j++) {
                                    String brhid = SUBBRHID[j];
                                    String sname = SCBranch.getSName(brhid);
                            %>
                            <option value="<%=brhid%>"><%=sname%>
                            </option>
                            <%
                                }
                            %>
                        </select>
                        查询时点：
                        <select id="creadate" name="creadate">
                            <%
                                while (crs.next()) {
                            %>
                            <option value="<%=crs.getString("DT")%>"><%=DBUtil.fromDB(crs.getString("DT"))%>
                            </option>
                            <%
                                }
                            %>
                        </select>
                        贷款发放日：
																<input class='input' type=text id="startdate" name="startdate"size=10 value="<%=startdate %>">
																<input type='button' value='…' class='button'onclick='setday(this,document.getElementById("startdate"))'>
																至
																<input class='input' type=text id="enddate" name="enddate"size=10 value="<%=enddate %>">
																<input type='button' value='…' class='button'onclick='setday(this,document.getElementById("enddate"))'>
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
<font size="4">
    客户经理经营情况查询表
</font>

<div align=center>
    <table cellSpacing=0 cellPadding=0 width=100% border=0>
        <tr class=title align="center" height="1">
            <td align=center class=title>
            </td>
        </tr>
        <tr class=title >
            <td class=title align="left" width="20%">
                网点名称：<%=strScbrhName%>
                <td align="center">
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
                
            </td>
            <td align=right class=title>
                单位：元
            </td>
        </tr>
    </table>
</div>
<table class=table cellSpacing=1 cellPadding=1 width=100%
       border=0 align=center id="checkTable">
<TBODY>
<tr>
    <td rowspan="2" align="center" valign="bottom" class="head" width="10%">
        客户经理名称
    </td>
    <td colspan="6" align="center" valign="bottom" class="head" width="40%">
        管理贷款情况
    </td>
    <td colspan="3" align="center" valign="bottom" class="head" width="25%">
       正常调不良
    </td>
    <td colspan="3" align="center" valign="bottom" class="head">
       不良调正常
    </td>
</tr>
<tr>
    <td align="center" valign="bottom" class="head">
      贷款户数
    </td>
    <td align="center" valign="bottom" class="head">
       贷款笔数
    </td>
    <td align="center" valign="bottom" class="head">
       当前余额
    </td>
    <td align="center" valign="bottom" class="head">
        当期新增贷款
    </td>
    <td align="center" valign="bottom" class="head">
       收息额
    </td>
    <td align="center" valign="bottom" class="head">
       欠息额
    </td>
    <td align="center" valign="bottom" class="head">
       贷款笔数
    </td>
    <td align="center" valign="bottom" class="head">
       贷款户数
    </td>
    <td align="center" valign="bottom" class="head">
       下调金额
    </td>
    <td align="center" valign="bottom" class="head">
       贷款笔数
    </td>
    <td align="center" valign="bottom" class="head">
       贷款户数
    </td>
    <td align="center" valign="bottom" class="head">
      上调金额
    </td>
</tr>
<%
    //************************数据显示部分*****************************
        StringBuffer sql4 = new StringBuffer(
        " with T as(select s.username names,"+//客户名称
        "count(f.bmno) bmnos1, "+//贷款笔数
        "count(distinct(f.idno)) idnos1, "+//贷款户数
        " sum(f.bal) fcbals1, "+//当前余额
        "sum(case when f.LMFCCLASS=0 then f.bal else 0 end) bals,"+//当期新增贷款
        "(case when f.LMFCCLASS<3 and f.FCCLASS between 3 and 5 then count(f.bmno) else 0 end ) bmnos2,"+//正常调不良-贷款笔数
        "(case when f.LMFCCLASS<3 and f.FCCLASS between 3 and 5 then count(distinct(f.idno)) else 0 end) idnos2,"+//正常调不良-贷款户数
        "sum(case when f.LMFCCLASS<3 and f.FCCLASS between 3 and 5 then f.bal else 0 end) fcbals2,"+//下调金额
        "(case when f.LMFCCLASS between 3 and 5 and f.FCCLASS <3 then count(f.bmno) else 0 end) bmnos3,"+//不良调正常-贷款笔数
        "(case when f.LMFCCLASS between 3 and 5 and f.FCCLASS <3 then count(distinct(f.idno)) else 0 end) idnos3,"+//不良调正常-贷款户数
        "sum(case when f.LMFCCLASS between 3 and 5 and f.FCCLASS <3 then f.bal else 0 end) fcbals3 "+//上调金额
        " from fcmain f,rqloanlist r,scuser s where  "
        );
        if ((startdate!= null && !startdate.equals(""))&&(enddate!= null && !enddate.equals(""))) 
		{
			sql4.append(" f.paydate between '" + startdate.trim()+ "' and '" + enddate.trim() + "' and ");
		}
		if ((startdate != null && !startdate.equals("")) && ((enddate.equals("") || enddate== null ))) 
		{
			
			sql4.append(" f.paydate >= '" + startdate.trim()+"' and ");
		}
		if ((startdate == null || startdate.equals("")) && ((enddate!= null && !enddate.equals("")))) 
		{
			sql4.append(" f.paydate <= '" +enddate.trim()+"' and ");
		}
        sql4.append(" f.CREATEDATE='"+create+"' and r.clientmgr=s.loginname and f.brhid=r.brhid and f.bmno=r.bmno and f.brhid='"+strScbrhId+"' and");
        sql4.append(" r.accno <> '1115' and f.fccrttype=1 group by s.username,f.LMFCCLASS,f.FCCLASS)");
        sql4.append(" select names,sum(bmnos1) bmnos1,sum(idnos1) idnos1,sum(fcbals1) fcbals1,sum(bals) bals,sum(bmnos2) bmnos2,sum(idnos2) idnos2,");
        sql4.append("sum(fcbals2) fcbals2,sum(bmnos3) bmnos3,sum(idnos3) idnos3,sum(fcbals3) fcbals3 from T group by names");
        System.out.println(sql4);
        CachedRowSet crs4 = manager.getRs(sql4.toString());
        int i=crs4.size();
        int j=10-i;
        while (crs4.next()) {
%>
<tr bgcolor=#FFFFFF class=data>
    <td class=data align="left">
        <%=DBUtil.fromDB(crs4.getString("names")) %>
    </td>
    <td class=data align="right">
        <%=crs4.getString("idnos1") %>
    </td>
    <td class=data align="right">
        <%=crs4.getString("bmnos1")%>
    </td>
    <td class=data align="right">
        <%=DBUtil.doubleToStr1(Double.valueOf(crs4.getString("fcbals1")).doubleValue()) %>
    </td>
    <td class=data align="right">
        <%=DBUtil.doubleToStr1(Double.valueOf(crs4.getString("bals")).doubleValue())%>
    </td>
    <td class=data align="right">
       
    </td>
    <td class=data align="right">
        
    </td>
    <td class=data align="right">
        <%=crs4.getString("bmnos2") %>
    </td>
    <td class=data align="right">
        <%=crs4.getString("idnos2")%>
    </td>
    <td class=data align="right">
        <%=DBUtil.doubleToStr1(Double.valueOf(crs4.getString("fcbals2")).doubleValue())%>
    </td>
    <td class=data align="right">
        <%=crs4.getString("bmnos3") %>
    </td>
    <td class=data align="right">
        <%=crs4.getString("idnos3")%>
    </td>
    <td class=data align="right">
        <%=DBUtil.doubleToStr1(Double.valueOf(crs4.getString("fcbals3")).doubleValue())%>
    </td>
</tr>
<%
    }
    if (j>0) {
        for (int k = 1; k <=j; k++) {
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
    <td class=data align="right"></td>
    <td class=data align="right"></td>
    <td class=data align="right"></td>
    <td class=data align="right"></td>
    <td class=data align="right"></td>
</tr>
<%
        }
    }
   

%>

</TBODY>

</TABLE>

<table cellSpacing=0 cellPadding=0 width=100% border=0 class=head>
    <tr>


        <td align=right>
            <input type='button' value='返回上页' onclick='history.back();' class='button'>
            &nbsp;
            <input type='button' value='返回初始页' onclick='back();' class='button'>
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
    <table width="250" height="80" border="0" cellpadding="0" cellspacing="1">
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
<script type="text/javascript">
    document.listform.brhidd.value = '<%=br%>';
    document.listform.creadate.value = '<%=create%>'
</script>
</html>

