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
 * @version  $Revision: 1.5 $  $Date: 2007/05/29 06:52:08 $
 * @author   houcs
 * <p/>修改：$Author: houcs $
=============================================== 
--%>
<%
    //***************************页面属性*****************************
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String currentPath = basePath + "fcsort/fenlei/";
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
    String startdate = "";
    String enddate = "";
    String params = "";
    String strScbrhId = request.getParameter("brhidd") == null ? "" : request.getParameter("brhidd");
    String selectddate = request.getParameter("creadate") == null ? "" : request.getParameter("creadate");
    //*************网点处理*********************************************
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String strUserName = um.getUserName();
    if (strScbrhId == null || strScbrhId.equals("") || strScbrhId.equals("0")) {
        strScbrhId = SCUser.getBrhId(strUserName).trim();//网点编号
    }
    String br = strScbrhId;
    String brhiddd = SCUser.getBrhId(strUserName).trim();//网点编号
    String strScbrhName = SCBranch.getSName(strScbrhId);//网点名称
    String SUBBRHIDs = SCBranch.getSubBrh0(brhiddd).trim();
    int m = SUBBRHIDs.length() / 9;
    String[] SUBBRHID = new String[m];
    if (SUBBRHIDs.length() > 9) {
        SUBBRHID = SUBBRHIDs.split(",");
    } else {
        SUBBRHID[0] = SUBBRHIDs;
    }

    //************************数据显示部分*****************************

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>五级分类分类统计</title>
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
    五级分类分类统计
</font>

<div align=center>
    <table cellSpacing=0 cellPadding=0 width=100% border=0>
        <tr class=title align="center" height="1">
            <td align=center class=title>
            </td>
        </tr>
        <%
            String subCreate = create.substring(0, 7);
            subCreate = subCreate.replaceAll("-", "年");
            if (subCreate.substring(5, 6).equals("0")) {
                subCreate = subCreate.substring(0, 5) + subCreate.substring(6, 7) + "月";
            } else {
                subCreate = subCreate + "月";
            }
        %>
        <tr class=title align="center">
            <td class=title align="left">
                网点名称：<%=strScbrhName%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                查询日期：<%=subCreate%>
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
    <td rowspan="2" align="center" valign="bottom" class="head" width="25%">
        网点
    </td>
    <td rowspan="2" align="center" valign="bottom" class="head" width="10%">
        笔数合计
    </td>
    <td rowspan="2" align="center" valign="bottom" class="head" width="10%">
        金额合计
    </td>
    <td colspan="2" align="center" valign="bottom" class="head">
        正常
    </td>
    <td colspan="3" align="center" valign="bottom" class="head">
        不良
    </td>
</tr>
<tr>
    <td align="center" valign="bottom" class="head">
        正常
    </td>
    <td align="center" valign="bottom" class="head">
        关注
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
    //************************数据显示部分*****************************
    String SUBBRHIDs4 = SCBranch.getSubBrh1(strScbrhId).trim();
    if (SUBBRHIDs4 != null || !SUBBRHIDs4.endsWith("")) {
        int i = SUBBRHIDs4.indexOf("999999");
        int j = SUBBRHIDs4.length();
        if (i != -1) {
            SUBBRHIDs4 = SUBBRHIDs4.substring(0, i - 3).trim() + SUBBRHIDs4.substring(i + 1, j).trim();
        }
    }
    SUBBRHIDs4 = SUBBRHIDs4.replaceAll(",", "','");
    SUBBRHIDs4 = (SUBBRHIDs4.equals("")) ? strScbrhId : SUBBRHIDs4;
    String sql3 = "select sname,brhid from SCBRANCH where brhid in ('"
            + SUBBRHIDs4 + "')";
    CachedRowSet crs3 = manager.getRs(sql3);
    int i1 = crs3.size();
    int j1 = 10;
    double ss1 = 0.00;
    double ss2 = 0.00;
    double ss3 = 0.00;
    double ss4 = 0.00;
    double ss5 = 0.00;
    double ss6 = 0.00;
    int ss7 = 0;
    while (crs3.next()) {
        double s1 = 0.00;
        double s2 = 0.00;
        double s3 = 0.00;
        double s4 = 0.00;
        double s5 = 0.00;
        double s6 = 0.00;
        int s7 = 0;
        String sname1 = DBUtil.fromDB(crs3.getString("sname"));
        String brhid1 = crs3.getString("brhid");
        String SUBBRHIDs1 = SCBranch.getSubBranchAll(brhid1);
        SUBBRHIDs1 = SUBBRHIDs1.replaceAll(",", "','");
        StringBuffer sql4 = new StringBuffer(
                "select " +
                        "sum(amt2) count," +
                        "sum(case when r.dim1=1 then amt1 else 0 end) rs1," +//1-1
                        "sum(case when r.dim1=2 then amt1 else 0 end) rs2," +//1-2
                        "sum(case when r.dim1=3 then amt1 else 0 end) rs3," +//1-3
                        "sum(case when r.dim1=4 then amt1 else 0 end) rs4," +//1-4
                        "sum(case when r.dim1=5 then amt1 else 0 end) rs5" //1-5
        );
        sql4.append(" from fcdata r where ");
        if (create != null && !create.equals("") && !create.equals("0")) {
            sql4.append(" r.dt='" + create.trim() + "' and  ");
        }
        sql4.append(" r.brhid in ('" + SUBBRHIDs1 + "') and ftype=1");
        CachedRowSet crs4 = manager.getRs(sql4.toString());
        while (crs4.next()) {
            s7 += Double.valueOf(crs4.getString("count") == null ? "0" : crs4.getString("count")).intValue();
            s1 += Double.valueOf(crs4.getString("rs1") == null ? "0.00" : crs4.getString("rs1")).doubleValue();
            s2 += Double.valueOf(crs4.getString("rs2") == null ? "0.00" : crs4.getString("rs2")).doubleValue();
            s3 += Double.valueOf(crs4.getString("rs3") == null ? "0.00" : crs4.getString("rs3")).doubleValue();
            s4 += Double.valueOf(crs4.getString("rs4") == null ? "0.00" : crs4.getString("rs4")).doubleValue();
            s5 += Double.valueOf(crs4.getString("rs5") == null ? "0.00" : crs4.getString("rs5")).doubleValue();
            s6 += Double.valueOf(crs4.getString("rs1") == null ? "0.00" : crs4.getString("rs1")).doubleValue()
                    + Double.valueOf(crs4.getString("rs2") == null ? "0.00" : crs4.getString("rs2")).doubleValue()
                    + Double.valueOf(crs4.getString("rs3") == null ? "0.00" : crs4.getString("rs3")).doubleValue()
                    + Double.valueOf(crs4.getString("rs4") == null ? "0.00" : crs4.getString("rs4")).doubleValue()
                    + Double.valueOf(crs4.getString("rs5") == null ? "0.00" : crs4.getString("rs5")).doubleValue();
        }
        ss1 += s1;
        ss2 += s2;
        ss3 += s3;
        ss4 += s4;
        ss5 += s5;
        ss6 += s6;
        ss7 += s7;
        String sum1 = DBUtil.doubleToStr1(s1);
        String sum2 = DBUtil.doubleToStr1(s2);
        String sum3 = DBUtil.doubleToStr1(s3);
        String sum4 = DBUtil.doubleToStr1(s4);
        String sum5 = DBUtil.doubleToStr1(s5);
        String sum6 = DBUtil.doubleToStr1(s6);
%>
<tr bgcolor=#FFFFFF class=data>
    <td class=data align="left">
        <%=sname1%>
    </td>
    <td class=data align="right">
        <%=s7%>
    </td>
    <td class=data align="right">
        <%=sum6%>
    </td>
    <td class=data align="right">
        <%=sum1%>
    </td>
    <td class=data align="right">
        <%=sum2%>
    </td>
    <td class=data align="right">
        <%=sum3%>
    </td>
    <td class=data align="right">
        <%=sum4%>
    </td>
    <td class=data align="right">
        <%=sum5%>
    </td>
</tr>
<%
    }
    if (i1 < 10) {
        for (int k = 1; k <= j1 - i1; k++) {
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
<%
        }
    }
    String sum1 = DBUtil.doubleToStr1(ss1);
    String sum2 = DBUtil.doubleToStr1(ss2);
    String sum3 = DBUtil.doubleToStr1(ss3);
    String sum4 = DBUtil.doubleToStr1(ss4);
    String sum5 = DBUtil.doubleToStr1(ss5);
    String sum6 = DBUtil.doubleToStr1(ss6);

%>
<tr bgcolor=#E7E9CF class=data>
    <td align=center>
        合计:
    </td>
    <td align=right>
        <%=ss7 %>
    </td>
    <td align=right>
        <%=sum6 %>
    </td>
    <td align=right>
        <%=sum1 %>
    </td>
    <td align=right>
        <%=sum2 %>
    </td>
    <td align=right>
        <%=sum3 %>
    </td>
    <td align=right>
        <%=sum4 %>
    </td>
    <td align=right>
        <%=sum5 %>
        </td>
</tr>

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

