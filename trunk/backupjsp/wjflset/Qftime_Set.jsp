
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.util.Vector" %>

<%--
=============================================== 
Title: 清分时点设置显示页面
Description: 显示多条清分时点设置的信息。
 * @version   $Revision: 1.3 $  $Date: 2007/05/24 08:35:34 $
 * @author   
 * <p/>修改：$Author: liuj $
=============================================== 
--%>

<%
    request.setCharacterEncoding("GBK");
    ConnectionManager manager = ConnectionManager.getInstance();
    String INITIALIZED = request.getParameter("INITIALIZED");
    String DTGO = request.getParameter("DTGO");
    String DTER = request.getParameter("DTER");

    if (INITIALIZED != null && INITIALIZED.trim().length() <= 0) INITIALIZED = null;
    if (DTGO != null && DTGO.trim().length() <= 0) DTGO = null;
    if (DTER != null && DTER.trim().length() <= 0) DTER = null;

    String sql = "select * from FCPRD where 1<>2";
    if (INITIALIZED != null) sql += " and INITIALIZED=" + INITIALIZED + "";
    if (DTGO != null) sql += " and DT >= " + DBUtil.toSqlDate(DTGO) + "";
    if (DTGO != null && DTER != null)
        sql += " and DT > " + DBUtil.toSqlDate(DTGO) + " and DT <= " + DBUtil.toSqlDate(DTER) + "";
    sql += " order by DT desc";

    //CachedRowSet crs=manager.getRs(sql);
    String pnStr = request.getParameter("pn");
    if (pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
    int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
    int ps = 10;
    Vector vec = manager.getPageRs(sql, pn, ps);
    int rows = ((Integer) vec.get(0)).intValue();
    CachedRowSet crs = (CachedRowSet) vec.get(1);


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
                var url = 'Qftime_add.jsp?Flyy_add_del=' + affairName;
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
                        form1.action = 'Qftime_del.jsp';
                        form1.submit();
                    }
                    else {
                        return false;
                    }

                }
            }
        }
        function info(infoname) {
            var url = 'Qftime_info.jsp?SEQNO=' + infoname;
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
    <script src='/js/meizzDate.js' type='text/javascript'></script>
</head>
<body background="../images/checks_02.jpg">
<form action="Qftime_Set.jsp" name="form1" method="post" id="winform">
<input type="hidden" name="pnstr" value="<%=pnStr==null?"1":pnStr%>">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE">
<tr align="left">
    <td height="30" bgcolor="#4477AA"><img src="../images/form/xing1.JPG" align="absmiddle"> <font size="2"
                                                                                                   color="#FFFFFF"><b>清分时点设置</b></font>
        <img src="../images/form/xing1.JPG" align="absmiddle"></td>
</tr>
<tr align="center">
<td height="260" valign="middle">
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
<td width="20">&nbsp;</td>
<td align="center" valign="middle">
<script src='/js/querybutton.js' type='text/javascript'></script>

<table>
<tr>
<td>
<table align='center' cellpadding='0' cellspacing='0' border='0' bgcolor='#AAAAAA' width='560'>
    <input type="hidden" name="flag" value="<%//flag%>">
    <tr>
        <td height="0">
            <table id="findDiv" class="query_table" cellpadding='0' cellspacing='0' border='0'
                   style='display:none'>
                <tr class="query_tr">
                    <td class="query_td" width="80%">
                        <table class='query_form_table' id='query_form_table' cellpadding='1'
                               cellspacing='1' border='0'>
                            <tr class="query_form_tr" nowrap>
                                <td height="19" nowrap class="page_form_title_td">已经产生清分业务</td>
                                <td class="page_form_td"><%=level.levelHereExt("INITIALIZED", "YesNo", INITIALIZED, "")%>
                                </td>
                            </tr>
                            <tr class="query_form_tr" nowrap>
                                <td height="19" nowrap class="page_form_title_td">预计清分日期</td>
                                <td class="page_form_td" nowrap>
                                    <input type="text" name="DTGO" value="<%=DTGO==null?"":DTGO%>"
                                           class="page_form_text" size="10">
                                    <input name="button" type="button" class='page_button_active'
                                           onclick="setday(this,winform.DTGO)" value="…">
                                </td>
                                <td class="page_form_td" nowrap>&nbsp;至&nbsp;</td>
                                <td class="page_form_td" nowrap>
                                    <input type="text" name="DTER" value="<%=DTER==null?"":DTER%>"
                                           class="page_form_text" size="10">
                                    <input name="button" type="button" class='page_button_active'
                                           onclick="setday(this,winform.DTER)" value="…">
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td class="query_td" width="20%" align="center">
                        <table border='0' width='100%' bgcolor='#F1F1F1'>
                            <tr>
                                <td nowrap valign="top"><input type="submit" class="query_button"
                                                               name="Submit" value=" 检 索 "></td>
                            </tr>
                            <tr>
                                <td nowrap valign="top"><input type="button" class="query_button"
                                                               name="reset" value=" 重 置 "
                                                               onclick="check_click()"></td>
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
<table class='list_form_table' width='560' align='center' cellpadding='0' cellspacing='1'
       border='0'>
    <tr class='list_form_title_tr'>
        <td nowrap width="5%" align="center"><input name="f02" type='checkbox' id="f02"
                                                    onClick="checkSub('f02','form1','elements')"
                                                    value="1"></td>
        <td width='16%' class='list_form_title_td' nowrap align="center">预计清分日期</td>
        <td width='16%' class='list_form_title_td' nowrap align="center">清分截至日期</td>
        <td width='17%' class='list_form_title_td' nowrap align="center">已经产生清分业务</td>
        <td width='40%' class='list_form_title_td' nowrap align="center">备注</td>
        <td width='12%' class='list_form_title_td' nowrap align="center">详细</td>
    </tr>
    <%
        int j = 0;


        while (crs.next()) {
    %>
    <tr class='list_form_tr'>
        <td align='center' nowrap><input
                name="SEQNO" <%=crs.getString("INITIALIZED").equals("1")?" disabled='true' type='checkbox'":" type='checkbox' "%>
                id="f020<%=j++%>" value="<%=crs.getString("SEQNO")%>"></td>
        <td nowrap class='list_form_td'
            align="center"><%=crs.getString("DT") == null ? "" : crs.getString("DT")%>
        </td>
        <td nowrap class='list_form_td'
            align="center"><%=crs.getString("ENDDT") == null ? "" : crs.getString("ENDDT")%>
        </td>
        <td nowrap class='list_form_td'
            align="center"><%=crs.getString("INITIALIZED") == null ? "" : level.getEnumItemName("YesNo", crs.getString("INITIALIZED"))%>
        </td>
        <td nowrap class='list_form_td'
            align="left"><%=crs.getString("COMMENT") == null ? "" : DBUtil.fromDB(crs.getString("COMMENT").length() > 40 ? crs.getString("COMMENT").substring(0, 40) + "..." : crs.getString("COMMENT"))%><!--lj changed in 2007-05-16-->
        </td>
        <td nowrap class='list_form_td' align="center"><a class="list_edit_href"
                                                          href='javascript:info("<%=crs.getString("SEQNO")%>")'>详细</a>
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
                            <td class="list_form_button_td"><input name='add' type='button' class='list_button_active'
                                                                   id="add" onClick="newAffair(this.name);"
                                                                   value=' 增加 '></td>
                            <td class="list_form_button_td"><input name='del' type='button' class='list_button_active'
                                                                   id="del" onClick="newAffair(this.name);"
                                                                   value=' 删除 '></td>
                            <td class="list_form_button_td"><input type='submit' name='a' class='list_button_active'
                                                                   value=' 刷新 ' onclick="return req();"></td>

                            <script language="javascript" type="text/javascript">
                                createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "Qftime_Set.jsp?pn=", "form1");
                            </script>

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
    document.title = "清分时点设置";
    document.focus();
    function checkSub(curChkName, formnm, funcnm)
    {

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
        document.form1.INITIALIZED.value = "";
        document.form1.DTGO.value = "";
        document.form1.DTER.value = "";
    }

    function req() {
        document.location.replace("/wjflset/Qftime_Set.jsp");
    }
</script>

<%
    //MyDB.getInstance().removeCurrentThreadConn("workbench.list(END).jsp"); //added by JGO on 2004-07-17
%>