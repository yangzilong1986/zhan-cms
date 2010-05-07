<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cmsi.pub.define.SystemDate" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Vector" %>

<%--
===============================================
Title: 五级分类重分
Description: 查询五级分类执行重分操作。
 * @version  $Revision: 1.6 $  $Date: 2007/05/29 02:36:20 $
 * @author
 * <p/>修改：$Author: liuj $
===============================================
--%>

<%
    //MyDB.getInstance().removeCurrentThreadConn("workbench.list.jsp"); //added by JGO on 2004-07-17
%>

<%
    request.setCharacterEncoding("GBK");
//request.setCharacterEncoding("GBK");
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
        response.sendRedirect("../fcworkbench/error.jsp");
    }
//String brhId = SCUser.getBrhId(um.getUserName()); //登录用户所在网点
    ConnectionManager manager = ConnectionManager.getInstance();

    String CREATEDATEGO = request.getParameter("CREATEDATEGO");
    String CREATEDATEER = request.getParameter("CREATEDATEER");
    String CLIENTNAME = request.getParameter("CLIENTNAME");
    String IDNO = request.getParameter("IDNO");
    String BMNO = request.getParameter("BMNO");
    String CNLNO = request.getParameter("CNLNO");
    String DUEDATEGO = request.getParameter("DUEDATEGO");
    String DUEDATEER = request.getParameter("DUEDATEER");
    String FCCRTTYPE = request.getParameter("FCCRTTYPE");

    if (CREATEDATEGO != null && CREATEDATEGO.trim().length() <= 0) CREATEDATEGO = null;
    if (CREATEDATEER != null && CREATEDATEER.trim().length() <= 0) CREATEDATEER = null;
    if (CLIENTNAME != null && CLIENTNAME.trim().length() <= 0) CLIENTNAME = null;
    if (IDNO != null && IDNO.trim().length() <= 0) IDNO = null;
    if (BMNO != null && BMNO.trim().length() <= 0) BMNO = null;
    if (CNLNO != null && CNLNO.trim().length() <= 0) CNLNO = null;
    if (DUEDATEGO != null && DUEDATEGO.trim().length() <= 0) DUEDATEGO = null;
    if (DUEDATEER != null && DUEDATEER.trim().length() <= 0) DUEDATEER = null;
    if (FCCRTTYPE != null && FCCRTTYPE.trim().length() <= 0) FCCRTTYPE = null;
//获得权限
//String status="";
//if(UserRoleMan.getInstance().ifHasRole(Integer.parseInt(um.getUserId()),6)==true)status += "1,";
//if(UserRoleMan.getInstance().ifHasRole(Integer.parseInt(um.getUserId()),9)==true)status += "2,3,";
//if(status.trim().length() > 0){
    //status = status.substring(0,status.length()-1);
//}
//if(!status.equals("1")){
    //response.sendRedirect("../fcworkbench/error.jsp");
//}
    String SUBBRHIDs = SCBranch.getSubBranchAll(SCUser.getBrhId(um.getUserName()).trim());
    if (SUBBRHIDs != null || SUBBRHIDs.length() > 0) SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
    String DT = "", ENDDT = "";
    CachedRowSet rs = manager.getRs("Select dt,enddt from FCPRD where initialized=1 ORDER BY dt desc");
    if (rs.next()) {
        DT = rs.getString("dt");
        ENDDT = rs.getString("enddt");
    }
//
////if(DT==null || DT.equals("")){
////System.out.println("***************************"+DT);
//    //response.sendRedirect("../fcworkbench/error.jsp");
////}
//    String sql = "select * from fcmain where brhid in (" + SUBBRHIDs + ") and FCSTATUS != 1 " +
//    //"and fcmain.fcstatus in ("+status+")";
//    if (DT != null && !DT.equals("")) sql += " and createdate='" + DT + "'";
////    if (CREATEDATEER != null) sql += " and CREATEDATE <= " + DBUtil.toSqlDate(CREATEDATEER);            //lj del in 2007-05-18
////    if (CREATEDATEGO != null) sql += " and CREATEDATE >= " + DBUtil.toSqlDate(CREATEDATEGO);
//    if (CLIENTNAME != null) sql += " and CLIENTNAME like '%" + DBUtil.toDB(CLIENTNAME) + "%'";
//    if (IDNO != null) sql += " and IDNO like '%" + IDNO + "%'";
//    if (BMNO != null) sql += " and BMNO like '%" + BMNO + "%'";
//    if (FCCRTTYPE != null) sql += " and FCCRTTYPE = " + FCCRTTYPE + "";
//    if (CNLNO != null) sql += " and CNLNO like '%" + CNLNO + "%'";
//    if (DUEDATEGO != null) sql += " and DUEDATE >= " + DBUtil.toSqlDate(DUEDATEGO);
//    if (DUEDATEER != null) sql += " and DUEDATE <= " + DBUtil.toSqlDate(DUEDATEER);
//    sql += " order by CREATEDATE desc,fcstatus desc";

//lj chenged in 2007-05-26
    String sql = "select * from ( " +
            "select a.* from fcmain a,fcprd b where brhid in (" + SUBBRHIDs + ") and FCSTATUS != 1 and FCCRTTYPE =1 " +
            " and a.createdate = b.dt ";
    if (SystemDate.getSystemDate2().compareToIgnoreCase(ENDDT) > 0) sql += " and 1>1 ";

    if (DT != null && !DT.equals("")) sql += " and createdate='" + DT + "'";
    if (CLIENTNAME != null) sql += " and CLIENTNAME like '%" + DBUtil.toDB(CLIENTNAME).trim() + "%'";
    if (IDNO != null) sql += " and IDNO like '%" + IDNO.trim() + "%'";
    if (BMNO != null) sql += " and BMNO like '%" + BMNO.trim() + "%'";
    if (CNLNO != null) sql += " and CNLNO like '%" + CNLNO.trim() + "%'";
    if (DUEDATEGO != null) sql += " and DUEDATE >= " + DBUtil.toSqlDate(DUEDATEGO);
    if (DUEDATEER != null) sql += " and DUEDATE <= " + DBUtil.toSqlDate(DUEDATEER);
    sql += " union ";
    sql += "select * from fcmain where brhid in (" + SUBBRHIDs + ") and FCSTATUS != 1 and FCCRTTYPE =2";
//    if (DT != null && !DT.equals("")) sql += " and createdate='" + DT + "'";
    if (CLIENTNAME != null) sql += " and CLIENTNAME like '%" + DBUtil.toDB(CLIENTNAME).trim() + "%'";
    if (IDNO != null) sql += " and IDNO like '%" + IDNO.trim() + "%'";
    if (BMNO != null) sql += " and BMNO like '%" + BMNO.trim() + "%'";
    if (CNLNO != null) sql += " and CNLNO like '%" + CNLNO.trim() + "%'";
    if (DUEDATEGO != null) sql += " and DUEDATE >= " + DBUtil.toSqlDate(DUEDATEGO);
    if (DUEDATEER != null) sql += " and DUEDATE <= " + DBUtil.toSqlDate(DUEDATEER);
    sql += ") T ";

    if (FCCRTTYPE != null) sql += " where FCCRTTYPE = " + FCCRTTYPE;
    sql += " order by CREATEDATE desc,fcstatus desc";

    //CachedRowSet crs=manager.getRs(sql);
    String pnStr = request.getParameter("pn");
    if (pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
    int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
    int ps = 10;
    Vector vec = manager.getPageRs(sql, pn, ps);
    int rows = ((Integer) vec.get(0)).intValue();
    CachedRowSet crs = (CachedRowSet) vec.get(1);
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");


    String mess = (String) request.getAttribute("mess");
    if (mess == null) {
        mess = "";
    } else {
        mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
    }
%>


<html>
<head>
    <title>五级分类重分</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
    <script language="JavaScript" type="text/JavaScript">
        function newAffair(affairName) {
            var flag = 0;
            if (affairName == 'add') {
                var url = 'Flyy_add.jsp?Flyy_add_del=' + affairName;
                window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
            }
            else {

                for (i = 0; i < form1.elements.length; i++) {
                    if (form1.elements[i].checked == true) {
                    }
                    else {
                        flag = flag + 1;
                    }
                }
                if (flag >= form1.elements.length) {
                    alert("请选择要删除的纪录！");
                    return false;
                }
                else {
                    if (confirm('确定删除吗？')) {
                        form1.action = 'Flyy_del.jsp';
                        form1.submit();
                    }
                    else {
                        return false;
                    }

                }
            }
        }
        function info(infoname) {
            var url = 'Flyy_info.jsp?SEQNO=' + infoname;
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        function buttonDisabled() {
            with (form2) {
                a.disabled = "true";
                b.disabled = "true";
                c.disabled = "true";
            }
        }
    </script>
    <script src='/js/check.js' type='text/javascript'></script>
    <script src='/js/pagebutton.js' type='text/javascript'></script>
    <script src="../js/flippage2.js" type="text/javascript"></script>
    <script src='/js/querybutton.js' type='text/javascript'></script>
    <script src='/js/meizzDate.js' type='text/javascript'></script>
</head>
<body background="../images/checks_02.jpg">
<form action="" name="form1" method="post" id="winform">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<table height="325" border="2" align="center" cellpadding="2" cellspacing="5" bordercolor="#006699" bgcolor="AACCEE">
<tr align="left">
    <td height="30" bgcolor="#4477AA"><img src="../images/form/xing1.JPG" align="absmiddle">
        <font size="2" color="#FFFFFF"><b>五级分类重分</b></font>
        <img src="../images/form/xing1.JPG" align="absmiddle">
        <font size="2" color="red"><%=mess%>
        </font></td>
</tr>
<tr align="center">
<td height="260" valign="middle">

<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
<td width="20">&nbsp;</td>
<td align="center" valign="middle">
<table>
<tr>
<td>
<table align='center' cellpadding='0' cellspacing='0' border='0' bgcolor='#AAAAAA' width='780'>
    <input type="hidden" name="flag" value="<%//flag%>">
    <tr>
        <td height="0">
            <table id="findDiv" class="query_table" cellpadding='0' cellspacing='0' border='0' style='display:none'>
                <tr class="query_tr">
                    <td class="query_td" width="80%">
                        <table class='query_form_table' id='query_form_table' cellpadding='1' cellspacing='1'
                               border='0'>
                            <input type="hidden" name="pnstr" value="<%=pnStr==null?"1":pnStr%>">
                            <tr class="query_form_tr" nowrap>
                                <%--<td height="19" nowrap class="page_form_title_td">清分日期</td>--%>
                                <%--<td class="page_form_td" nowrap>--%>
                                <%--<input type="text" name="CREATEDATEGO"--%>
                                <%--value="<%=CREATEDATEGO==null?"":CREATEDATEGO%>" class="page_form_text"--%>
                                <%--size="10">--%>
                                <%--<input name="button" type="button" class='page_button_active'--%>
                                <%--onclick="setday(this,winform.CREATEDATEGO)" value="…">--%>
                                <%--</td>--%>
                                <%--<td class="page_form_td" nowrap>&nbsp;至&nbsp;</td>--%>
                                <%--<td class="page_form_td" nowrap>--%>
                                <%--<input type="text" name="CREATEDATEER"--%>
                                <%--value="<%=CREATEDATEER==null?"":CREATEDATEER%>" class="page_form_text"--%>
                                <%--size="10">--%>
                                <%--<input name="button" type="button" class='page_button_active'--%>
                                <%--onclick="setday(this,winform.CREATEDATEER)" value="…">--%>
                                <%--</td>--%>
                                <td height="19" nowrap class="page_form_title_td">到期日期</td>
                                <td class="page_form_td" nowrap>
                                    <input type="text" name="DUEDATEGO" value="<%=DUEDATEGO==null?"":DUEDATEGO%>"
                                           class="page_form_text" size="10">
                                    <input name="button" type="button" class='page_button_active'
                                           onclick="setday(this,winform.DUEDATEGO)" value="…">
                                </td>
                                <td class="page_form_td" nowrap>&nbsp;至&nbsp;</td>
                                <td class="page_form_td" nowrap>
                                    <input type="text" name="DUEDATEER" value="<%=DUEDATEER==null?"":DUEDATEER%>"
                                           class="page_form_text" size="10">
                                    <input name="button" type="button" class='page_button_active'
                                           onclick="setday(this,winform.DUEDATEER)" value="…">
                                </td>
                                <td height="19" nowrap class="page_form_title_td">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;客户名称</td>
                                <td class="page_form_td" colspan="3"><input type="text" name="CLIENTNAME"
                                                                            value="<%=CLIENTNAME==null?"":CLIENTNAME%>"
                                                                            class="page_form_text" size="34"></td>
                            </tr>
                            <tr class="query_form_tr" nowrap>
                                <td height="19" nowrap class="page_form_title_td">业务号</td>
                                <td class="page_form_td" colspan="3"><input type="text" name="BMNO"
                                                                            value="<%=BMNO==null?"":BMNO%>"
                                                                            class="page_form_text" size="34"></td>

                                <td height="19" nowrap class="page_form_title_td">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;证件号码</td>
                                <td class="page_form_td" colspan="3"><input type="text" name="IDNO"
                                                                            value="<%=IDNO==null?"":IDNO%>"
                                                                            class="page_form_text" size="34"></td>
                            </tr>
                            <tr class="query_form_tr" nowrap>

                                <td height="19" nowrap class="page_form_title_td">产生类型</td>
                                <td class="page_form_td"
                                    colspan="3"><%=level.levelHereExt("FCCRTTYPE", "FCCrtType", FCCRTTYPE, "")%>
                                </td>
                                <td height="19" nowrap class="page_form_title_td">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;借据号码</td>
                                <td class="page_form_td" colspan="3"><input type="text" name="CNLNO"
                                                                            value="<%=CNLNO==null?"":CNLNO%>"
                                                                            class="page_form_text" size="34"></td>
                            </tr>
                        </table>
                    </td>
                    <td class="query_td" width="20%" align="center">
                        <table border='0' width='100%' bgcolor='#F1F1F1'>
                            <tr>
                                <td nowrap valign="top"><input type="submit" class="query_button" name="Submit"
                                                               value=" 检 索 "></td>
                            </tr>
                            <tr>
                                <td nowrap valign="top"><input type="button" class="query_button" name="reset"
                                                               value=" 重 置 " onclick="check_click()"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height="0" align="center"><img id='findDivHandle' title='点击查询' onClick='menuMove()'
                                           src='/images/form/button1.jpg' style='cursor:hand;'></td>
    </tr>
</table>

<table cellpadding='0' cellspacing='0' border='0'>
    <tr>
        <td height='5'></td>
    </tr>
</table>
<table class='list_form_table' width='780' align='center' cellpadding='0' cellspacing='1' border='0'>
    <tr class='list_form_title_tr'>
        <td width="3%" class='list_form_title_td' nowrap align="center"><input name="f02" type='checkbox' id="f02"
                                                                               onClick="checkSub('f02','form1','elements')"
                                                                               value="1"></td>
        <td width='7%' class='list_form_title_td' nowrap align="center">清分日期</td>
        <td width='7%' class='list_form_title_td' nowrap align="center">产生类型</td>
        <td width='9%' class='list_form_title_td' nowrap align="center">网点</td>
        <td width='9%' class='list_form_title_td' nowrap align="center">业务号</td>
        <td width='9%' class='list_form_title_td' nowrap align="center">清分类型</td>
        <td width='11%' class='list_form_title_td' nowrap align="center">客户名称</td>
        <td width='7%' class='list_form_title_td' nowrap align="center">审批状态</td>
        <td width='9%' class='list_form_title_td' nowrap align="center">借据号码</td>
        <td width='9%' class='list_form_title_td' nowrap align="center">时点余额</td>
        <td width='9%' class='list_form_title_td' nowrap align="center">到期日期</td>
        <td width='7%' class='list_form_title_td' nowrap align="center">清分结果</td>
        <td width='4%' class='list_form_title_td' nowrap align="center">查询</td>
    </tr>
    <%
        int j = 0;
        while (crs.next()) {
    %>
    <tr class='list_form_tr'>
        <td align='center' nowrap><input name="FCNO" type='checkbox' id="f020<%=j++%>"
                                         value="<%=crs.getString("fcno")%>"></td>
        <td nowrap class='list_form_td' align="center"><%=crs.getString("CREATEDATE")%>
        </td>
        <td nowrap class='list_form_td' align="center"><%=
        crs.getString("FCCRTTYPE") == null ? "" : level.getEnumItemName("FCCrtType", crs.getString("FCCRTTYPE"))%>
        </td>
        <td nowrap class='list_form_td' align="left">
            <div title="<%=SCBranch.getLName(crs.getString("BRHID"))%>">
                <%=crs.getString("BRHID")%>
        </td>
        <td nowrap class='list_form_td' align="left"><%=crs.getString("BMNO")%>
        </td>
        <td nowrap class='list_form_td' align="center"><%=
        crs.getString("FCTYPE") == null ? "" : level.getEnumItemName("FCType", crs.getString("FCTYPE"))%>
        </td>
        <td nowrap class='list_form_td' align="left"><%=DBUtil.fromDB(crs.getString("CLIENTNAME"))%>
        </td>
        <td nowrap class='list_form_td' align="center"><%=
        crs.getString("FCSTATUS") == null ? "" : level.getEnumItemName("FCStatus", crs.getString("FCSTATUS"))%>
        </td>
        <td nowrap class='list_form_td' align="left"><%=crs.getString("CNLNO")%>
        </td>
        <td nowrap class='list_form_td' align="right"><%=df.format(crs.getBigDecimal("BAL"))%>
        </td>
        <td nowrap class='list_form_td' align="center"><%=crs.getString("DUEDATE")%>
        </td>
        <td nowrap class='list_form_td' align="center">
            <%=crs.getString("FCCLASS") == null ? "" : (level.getEnumItemName("LoanCat1", crs.getString("FCCLASS")).equals("0")) ? "" : level.getEnumItemName("LoanCat1", crs.getString("FCCLASS"))%><!--lj changed in 2007-05-16-->
        </td>
        <td nowrap class='list_form_td' align="center"><a class="list_edit_href"
                                                          href='javascript:banli("<%=crs.getString("FCNO").trim()%>","<%=crs.getString("BMNO").trim()%>","<%=crs.getString("BRHID").trim()%>","<%=crs.getString("OPERBRHID").trim()%>","<%=crs.getString("FCCRTTYPE").trim()%>","<%=crs.getString("FCTYPE").trim()%>","<%=crs.getString("FCSTATUS").trim()%>","<%=crs.getString("BMTYPE").trim()%>","<%=crs.getString("CLIENTTYPE").trim()%>")'>查询</a>
        </td>
    </tr>
    <%
        }

        for (int i = crs.size(); i < 10; i++) {
    %>

    <tr class='list_form_tr'>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
    </tr>
    <%

        }
    %>
</table>
</td>
</tr>
</table>
</td>
<td width="20">&nbsp;</td>
</tr>
</table>
</td>
</tr>
<tr height="35" align="center" valign="middle">
    <td align="center">
        <table border="0" cellspacing="0" cellpadding="0" width="538">
            <tr>
                <td nowrap align="center">

                    <table class='list_button_tbl'>
                        <tr class="list_button_tbl_tr">
                            <td class="list_form_button_td"><input type='submit' name='tj' class='list_button_active'
                                                                   value=' 重 分 ' onclick="return newAffair();"></td>
                            <td class="list_form_button_td"><input type='submit' name='a' class='list_button_active'
                                                                   value=' 刷 新 ' onclick="return req();"></td>
                            <td class="list_form_button_td">
                                <script language="javascript">
                                    createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "fc_cf.jsp?pn=", "form1");
                                </script>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
</td>
</tr>
</table>
</form>
</body>
</html>
<script language="javascript">
document.title = "五级分类重分";
document.focus();

function banli(a, b, c, d, e, f, g, h, i) {
    var FCNO = a;
    var BMNO = b;
    var BRHID = c;
    var OPERBRHID = d;
    var FCCRTTYPE = e;
    var FCTYPE = f;
    var FCSTATUS = g;
    var BMTYPE = h;
    var CLIENTTYPE = i;
    if (true) {
        window.open("history_qf/history_qf_info.jsp?FCNO=" + FCNO + "&BMNO=" + BMNO + "&FCSTATUS=" + FCSTATUS, "", "width=900,height=680,left=60,top=0,scrollbars");
    }
    /*if(FCTYPE=="2"){
      window.open("/fcworkbench/xinfo.jsp?FCNO="+FCNO+"&BMNO="+BMNO+"&BRHID="+BRHID+""+
                 "&OPERBRHID="+OPERBRHID+"&FCCRTTYPE="+FCCRTTYPE+"&FCTYPE="+FCTYPE+"&FCSTATUS="+FCSTATUS+""+
                 "&BMTYPE="+BMTYPE+"&CLIENTTYPE="+CLIENTTYPE,"","width=900,height=680,left=60,top=0,scrollbars");

    }
    if(FCTYPE=="3"){
      window.open("/fcworkbench/xfirstcf.jsp?FCNO="+FCNO+"&BMNO="+BMNO+"&BRHID="+BRHID+""+
                 "&OPERBRHID="+OPERBRHID+"&FCCRTTYPE="+FCCRTTYPE+"&FCTYPE="+FCTYPE+"&FCSTATUS="+FCSTATUS+""+
                 "&BMTYPE="+BMTYPE+"&CLIENTTYPE="+CLIENTTYPE,"","width=900,height=680,left=60,top=0,scrollbars");
    }*/
}
function checkSub(curChkName, formnm, funcnm)
{
    //alert("dddddddd");
    if (curChkName == "" || formnm == "" || funcnm == "") {
        alert("checkSub error!");
        return;
    }
    var i;

    if (eval(formnm + "." + curChkName + ".checked")) {
        for (i = 0; i < eval(formnm + "." + funcnm + ".length"); i++) {
            if (eval(formnm + "." + funcnm + "[i].id.indexOf('" + curChkName + "')!=-1"))
                eval(formnm + "." + eval(formnm + "." + funcnm + "[i].id") + ".checked=true");
        }
    }

    if (eval(formnm + "." + curChkName + ".checked==false")) {
        for (i = 0; i < eval(formnm + "." + funcnm + ".length"); i++) {
            if (eval(formnm + "." + funcnm + "[i].id.indexOf('" + curChkName + "')!=-1"))
                eval(formnm + "." + eval(formnm + "." + funcnm + "[i].id") + ".checked=false");
        }
    }

    if (eval(formnm + "." + curChkName + ".checked")) {
        for (i = 0; i < eval(formnm + "." + funcnm + ".length"); i++) {
            if (eval(formnm + "." + funcnm + "[i].id.indexOf('" + curChkName + "')!=-1"))
                eval(formnm + "." + eval(formnm + "." + funcnm + "[i].id") + ".checked=true");
        }
    }
}
function check_click() {
    document.form1.CREATEDATEGO.value = "";
    document.form1.CREATEDATEER.value = "";
    document.form1.CLIENTNAME.value = "";
    document.form1.IDNO.value = "";
    document.form1.BMNO.value = "";
    document.form1.CNLNO.value = "";
    document.form1.DUEDATEGO.value = "";
    document.form1.DUEDATEER.value = "";
    document.form1.FCCRTTYPE.value = "";
}
function req() {
    //document.location.replace("/fcworkbench/fc_query.jsp");
    form1.action = "/fcworkbench/fc_cf.jsp?CREATEDATEGO=" + document.all.CREATEDATEGO.value + "" +
                   "&CREATEDATEER=" + document.all.CREATEDATEER.value + "" +
                   "&CLIENTNAME=" + document.all.CLIENTNAME.value + "" +
                   "&IDNO=" + document.all.IDNO.value + "" +
                   "&BMNO=" + document.all.BMNO.value + "" +
                   "&CNLNO=" + document.all.CNLNO.value + "" +
                   "&DUEDATEGO=" + document.all.DUEDATEGO.value + "" +
                   "&pn=" + document.all.pnstr.value + "" +
                   "&DUEDATEER=" + document.all.DUEDATEER.value + "";
    form1.submit();
}
function newAffair() {
    var flag = 0;
    for (i = 0; i < form1.elements.length; i++) {
        if (form1.elements[i].checked == true) {
        }
        else {
            flag = flag + 1;
        }
    }
    if (flag >= form1.elements.length) {
        alert("请选择要重分的纪录！");
        return false;
    }
    else {
        if (confirm('确定重分吗？')) {
            form1.action = 'fc_cfsave.jsp';
            form1.submit();
        }
        else {
            return false;
        }

    }

}

</script>

<%
    //MyDB.getInstance().removeCurrentThreadConn("workbench.list(END).jsp"); //added by JGO on 2004-07-17
%>