<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cmsi.pub.define.UserRoleMan" %>
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
Title: 五级分类查询
Description: 查询五级分类相关信息。
 * @version  $Revision: 1.8 $  $Date: 2007/05/31 03:24:39 $
 * @author
 * <p/>修改：$Author: liuj $
===============================================
--%>

<%
    request.setCharacterEncoding("GBK");
    UserManager um = (UserManager) session
            .getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
        response.sendRedirect("error.jsp");
    }
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
    String BRHID = request.getParameter("brhid");
    String BRHNM = request.getParameter("lname2");

    if (CREATEDATEGO != null && CREATEDATEGO.trim().length() <= 0)
        CREATEDATEGO = null;
    if (CREATEDATEER != null && CREATEDATEER.trim().length() <= 0)
        CREATEDATEER = null;
    if (CLIENTNAME != null && CLIENTNAME.trim().length() <= 0)
        CLIENTNAME = null;
    if (IDNO != null && IDNO.trim().length() <= 0)
        IDNO = null;
    if (BMNO != null && BMNO.trim().length() <= 0)
        BMNO = null;
    if (CNLNO != null && CNLNO.trim().length() <= 0)
        CNLNO = null;
    if (DUEDATEGO != null && DUEDATEGO.trim().length() <= 0)
        DUEDATEGO = null;
    if (DUEDATEER != null && DUEDATEER.trim().length() <= 0)
        DUEDATEER = null;
    if (FCCRTTYPE != null && FCCRTTYPE.trim().length() <= 0)
        FCCRTTYPE = null;
    if (BRHID != null && BRHID.trim().length() <= 0) BRHID = null;

    //获得权限
    String status = "";
    if (UserRoleMan.getInstance().ifHasRole(
            Integer.parseInt(um.getUserId()), 6) == true)
        status += "1,";
    if (UserRoleMan.getInstance().ifHasRole(
            Integer.parseInt(um.getUserId()), 9) == true)
        status += "2,3,";
    if (status.trim().length() > 0) {
        status = status.substring(0, status.length() - 1);
    }


    String sql = "select * from fcmain where brhid ";
    if (BRHID != null) sql += "like '%" + BRHID.trim() + "%'";
    else {
        String SUBBRHIDs = SCBranch.getSubBranchAll(SCUser.getBrhId(um.getUserName()));
        if (SUBBRHIDs != null || SUBBRHIDs.length() > 0)
            SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
        sql += "in (" + SUBBRHIDs + ") ";
    }
    if (CREATEDATEGO != null || CREATEDATEER != null) { //lj chenged in 2007-05-16 for search with one date input.
        if (CREATEDATEGO != null)
            sql += " and CREATEDATE >= " + DBUtil.toSqlDate(CREATEDATEGO);
        if (CREATEDATEER != null)
            sql += " and CREATEDATE <= " + DBUtil.toSqlDate(CREATEDATEER);
    } else
        sql += " and CREATEDATE = (select max(Dt) from fcprd where initialized=1)";
    if (CLIENTNAME != null)
        sql += " and CLIENTNAME like '%'||ltrim(rtrim('" + DBUtil.toDB(CLIENTNAME)
                + "'))||'%'";
    if (IDNO != null)
        sql += " and IDNO like '%" + IDNO.trim() + "%'";
    if (BMNO != null)
        sql += " and BMNO like '%" + BMNO.trim() + "%'";
    if (FCCRTTYPE != null)
        sql += " and FCCRTTYPE = " + FCCRTTYPE + "";
    if (CNLNO != null)
        sql += " and CNLNO like '%" + CNLNO.trim() + "%'";
    if (DUEDATEGO != null) sql += " and DUEDATE >= " + DBUtil.toSqlDate(DUEDATEGO) + "";
    if (DUEDATEER != null) sql += " and DUEDATE <= " + DBUtil.toSqlDate(DUEDATEER) + "";

    sql += " order by CREATEDATE desc,fcstatus desc";
    String pnStr = request.getParameter("pn");
    if (pnStr == null || pnStr.trim().length() <= 0)
        pnStr = null;
    int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
    int ps = 10;
    Vector vec = manager.getPageRs(sql, pn, ps);
    int rows = ((Integer) vec.get(0)).intValue();
    CachedRowSet crs = (CachedRowSet) vec.get(1);
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
%>


<html>
<head>
    <title>信贷管理</title>
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
    <script language="JavaScript" type="text/JavaScript">
        document.title = "";
        document.focus();

        function checkLname() {
            //if (form1.brhid.value=="") lname1.innerText="";
            form1.lname2.value = lname1.innerText;
            form1.submit();
        }

        function deptrefer_click() {
            var url = "fc_view.jsp";
            var vArray;
            window.open(url, 0, "height=400,width=460,toolbar=no,scrollbars=yes,status");
        }
    </script>
    <script src='/js/check.js' type='text/javascript'></script>
    <script src='/js/pagebutton.js' type='text/javascript'></script>
    <script src="../js/flippage2.js" type="text/javascript"></script>
    <script src='/js/querybutton.js' type='text/javascript'></script>
    <script src='/js/meizzDate.js' type='text/javascript'></script>
</head>
<body background="../images/checks_02.jpg">
<form action="fc_query.jsp" name="form1" method="post" id="winform" onsubmit="checkLname()">
<table width="100%" height="100%" border="0" cellspacing="0"
       cellpadding="0">
<tr>
<td align="center" valign="middle">
<table height="325" border="2" align="center" cellpadding="2"
       cellspacing="5" bordercolor="#006699" bgcolor="AACCEE">
<tr align="left">
    <td height="30" bgcolor="#4477AA"><img
            src="../images/form/xing1.JPG" align="absmiddle"> <font
            size="2" color="#FFFFFF"><b>五级分类查询</b></font> <img
            src="../images/form/xing1.JPG" align="absmiddle"></td>
</tr>
<tr align="center">
<td height="260" valign="middle">
<table width="100%" height="100%" cellspacing="0" cellpadding="0"
       border="0">
<tr>
<td width="20">&nbsp;</td>
<td align="center" valign="middle">
<table>
<tr>
<td>
<table align='center' cellpadding='0' cellspacing='0' border='0'
       bgcolor='#AAAAAA' width='780'>
<input type="hidden" name="flag" value="<%//flag%>">
<input type="hidden" name="lname2">
<tr>
<td height="0">
<table id="findDiv" class="query_table" cellpadding='0'
       cellspacing='0' border='0' style='display:none'>
<tr class="query_tr">
<td class="query_td" width="80%">
    <table class='query_form_table' id='query_form_table'
           cellpadding='1' cellspacing='1' border='0'>
        <input type="hidden" name="pnstr"
               value="<%=pnStr==null?"1":pnStr%>">
        <tr class="query_form_tr" nowrap>
            <td height="19" nowrap class="page_form_title_td">清分日期</td>
            <td class="page_form_td" nowrap><input type="text"
                                                   name="CREATEDATEGO"
                                                   value="<%=CREATEDATEGO==null?"":CREATEDATEGO%>"
                                                   class="page_form_text" size="10"></td>
            <td class="page_form_td" nowrap><input
                    name="button" type="button" class='page_button_active'
                    onclick="setday(this,winform.CREATEDATEGO)" value="…">
            </td>
            <td class="page_form_td" nowrap>&nbsp;至&nbsp;</td>
            <td class="page_form_td" nowrap><input type="text"
                                                   name="CREATEDATEER"
                                                   value="<%=CREATEDATEER==null?"":CREATEDATEER%>"
                                                   class="page_form_text" size="10"></td>
            <td class="page_form_td" nowrap><input
                    name="button" type="button" class='page_button_active'
                    onclick="setday(this,winform.CREATEDATEER)" value="…">
            </td>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <td height="19" nowrap class="page_form_title_td">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;客户名称
            </td>
            <td class="page_form_td" colspan="3"><input
                    type="text" name="CLIENTNAME"
                    value="<%=CLIENTNAME==null?"":CLIENTNAME%>"
                    class="page_form_text" size="34"></td>
        </tr>
        <tr class="query_form_tr" nowrap>
            <td height="19" nowrap class="page_form_title_td">到期日期</td>
            <td class="page_form_td" nowrap><input type="text"
                                                   name="DUEDATEGO"
                                                   value="<%=DUEDATEGO==null?"":DUEDATEGO%>"
                                                   class="page_form_text" size="10"></td>
            <td class="page_form_td" nowrap><input
                    name="button" type="button" class='page_button_active'
                    onclick="setday(this,winform.DUEDATEGO)" value="…">
            </td>
            <td class="page_form_td" nowrap>&nbsp;至&nbsp;</td>
            <td class="page_form_td" nowrap><input type="text"
                                                   name="DUEDATEER"
                                                   value="<%=DUEDATEER==null?"":DUEDATEER%>"
                                                   class="page_form_text" size="10"></td>
            <td class="page_form_td" nowrap><input
                    name="button" type="button" class='page_button_active'
                    onclick="setday(this,winform.DUEDATEER)" value="…">
            </td>

            <td height="19" nowrap class="page_form_title_td">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;证件号码
            </td>
            <td class="page_form_td" colspan="3"><input
                    type="text" name="IDNO" value="<%=IDNO==null?"":IDNO%>"
                    class="page_form_text" size="34"></td>
        </tr>
        <tr class="query_form_tr" nowrap>
            <td height="19" nowrap class="page_form_title_td">业务号</td>
            <td class="page_form_td" colspan="5"><input
                    type="text" name="BMNO" value="<%=BMNO==null?"":BMNO%>"
                    class="page_form_text" size="34"></td>

            <td height="19" nowrap class="page_form_title_td">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;借据号码
            </td>
            <td class="page_form_td" colspan="3"><input
                    type="text" name="CNLNO"
                    value="<%=CNLNO==null?"":CNLNO%>" class="page_form_text"
                    size="34"></td>
        </tr>
        <tr class="query_form_tr" nowrap>
            <td height="19" nowrap class="page_form_title_td">业务网点</td>
            <td class="page_form_td"><input type="text" name="brhid" value="<%=BRHID==null?"":BRHID.trim()%>"
                                            class="page_form_text" minLength="0" maxlength="9"
                                            errInfo="业务网点" size="10" onchange='lname1.innerText=""'></td>
            <td class="page_form_td" colspan="3"><label id="lname1"><%=BRHNM == null ? "" : BRHNM%>
            </label></td>
            <td class="page_form_td"><input type="button" name="nameref" value="…" onclick="deptrefer_click()"
                                            class="page_form_refbutton"></td>
            <td height="19" nowrap class="page_form_title_td">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;产生类型</td>
            <td class="page_form_td"
                colspan="3"><%=level.levelHereExt("FCCRTTYPE", "FCCrtType", FCCRTTYPE,
                    "")%>
            </td>
        </tr>
    </table>
</td>
<td class="query_td" width="20%" align="center">
    <table border='0' width='100%' bgcolor='#F1F1F1'>
        <tr>
            <td nowrap valign="top"><input type="submit"
                                           class="query_button" name="Submit" value=" 检 索 "></td>
        </tr>
        <tr>
            <td nowrap valign="top"><input type="button"
                                           class="query_button" name="reset" value=" 重 置 "
                                           onclick="check_click()"></td>
        </tr>
    </table>
</td>
</tr>
</table>
</td>
</tr>
<tr>
    <td height="0" align="center"><img id='findDivHandle'
                                       title='点击查询' onClick='menuMove()'
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
        <td width='9%' class='list_form_title_td' nowrap align="center">清分日期</td>
        <td width='7%' class='list_form_title_td' nowrap align="center">产生类型</td>
        <td width='9%' class='list_form_title_td' nowrap align="center">网点</td>
        <td width='10%' class='list_form_title_td' nowrap align="center">业务号</td>
        <td width='9%' class='list_form_title_td' nowrap align="center">清分类型</td>
        <td width='11%' class='list_form_title_td' nowrap align="center">客户名称</td>
        <td width='7%' class='list_form_title_td' nowrap align="center">审批状态</td>
        <!--<td width='7%' class='list_form_title_td' nowrap align="center">借据号码</td>-->
        <td width='10%' class='list_form_title_td' nowrap align="center">时点余额</td>
        <td width='9%' class='list_form_title_td' nowrap align="center">到期日期</td>
        <td width='7%' class='list_form_title_td' nowrap align="center">目前结果</td>
        <td width='7%' class='list_form_title_td' nowrap align="center">清分标记</td>
        <td width='5%' class='list_form_title_td' nowrap align="center">查询</td>
    </tr>
    <%
        while (crs.next()) {
    %>
    <tr class='list_form_tr'>
        <td nowrap class='list_form_td' align="center"><%=crs.getString("CREATEDATE")%>
        </td>
        <td nowrap class='list_form_td'
            align="center"><%=crs.getString("FCCRTTYPE") == null ? "" : level.getEnumItemName("FCCrtType", crs.getString("FCCRTTYPE"))%>
        </td>
        <td nowrap class='list_form_td' align="left">
            <div title="<%=SCBranch.getLName(crs.getString("BRHID"))%>">
                <%=crs.getString("BRHID")%>
        </td>
        <td nowrap class='list_form_td' align="left"><%=crs.getString("BMNO")%>
        </td>
        <td nowrap class='list_form_td'
            align="center"><%=crs.getString("FCTYPE") == null ? "" : level.getEnumItemName("FCType", crs.getString("FCTYPE"))%>
        </td>
        <td nowrap class='list_form_td' align="left"><%=DBUtil.fromDB(crs.getString("CLIENTNAME"))%>
        </td>
        <td nowrap class='list_form_td'
            align="center"><%=crs.getString("FCSTATUS") == null ? "" : level.getEnumItemName("FCStatus", crs.getString("FCSTATUS"))%>
        </td>
        <%--<td nowrap class='list_form_td' align="left"><%=crs.getString("CNLNO")%></td>--%>
        <td nowrap class='list_form_td' align="right"><%=df.format(crs.getBigDecimal("BAL"))%>
        </td>
        <td nowrap class='list_form_td' align="center"><%=crs.getString("DUEDATE")%>
        </td>
        <td nowrap class='list_form_td'
            align="center">
            <%=crs.getString("FCCLASS") == null ? "" : (level.getEnumItemName("LoanCat1", crs.getString("FCCLASS")).equals("0")) ? "" : level.getEnumItemName("LoanCat1", crs.getString("FCCLASS"))%><!--lj changed in 2007-05-16-->
        </td>
        <td nowrap class='list_form_td'
            align="center"><%=crs.getString("FCOPRFLAG") == null ? "" : level.getEnumItemName("FCOprFlag", crs.getString("FCOPRFLAG"))%>
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
        <!--<td nowrap class='list_form_td'></td>-->
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
                            <td class="list_form_button_td"><input type='submit'
                                                                   name='a' class='list_button_active' value=' 刷 新 '
                                                                   onclick="return req();"></td>
                            <td class="list_form_button_td">
                                <script language="javascript" type="text/javascript">
                                    createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "fc_query.jsp?pn=", "form1");
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
<script language="javascript" type="text/javascript">
    document.title = "五级分类查询";
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
            window.open("history_qf/history_qf_info.jsp?FCNO=" + FCNO + "&BMNO=" + BMNO + "&FCSTATUS=" + FCSTATUS, "", "width=800,height=680,left=60,top=0,scrollbars");
        }
    }

    function checkSub(curChkName, formnm, funcnm) {
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
        form1.action = "fc_query.jsp?CREATEDATEGO=" + document.all.CREATEDATEGO.value + "" +
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
</script>