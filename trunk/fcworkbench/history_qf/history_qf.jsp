<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Vector" %>

<%--
===============================================
Title: 清分操作-清分历史信息列表
Description: 清分历史信息列表。
 * @version  $Revision: 1.6 $  $Date: 2007/05/29 14:17:36 $
 * @author
 * <p/>修改：$Author: liuj $
===============================================
--%>

<%
    //MyDB.getInstance().removeCurrentThreadConn("workbench.list.jsp"); //added by JGO on 2004-07-17
%>

<%
    request.setCharacterEncoding("GBK");
    ConnectionManager manager = ConnectionManager.getInstance();
    String CREATEDATE = request.getParameter("CREATEDATE");
    String BMNO = request.getParameter("BMNO");
    String FCNO = request.getParameter("FCNO");
    if (BMNO == null || FCNO == null || CREATEDATE == null) {
        session.setAttribute("lettermess", "没有发现传送入的参数！");
        response.sendRedirect("/fcworkbench/lettersucces.jsp");
    }

    String sql = "select * from fcmain where BMNO='" + BMNO + "' and fcstatus=8 and CREATEDATE<'" + CREATEDATE + "' order by createdate desc";

    //CachedRowSet crs=manager.getRs(sql);
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
    String pnStr = request.getParameter("pn");
    if (pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
    int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
    int ps = 10;
    Vector vec = manager.getPageRs(sql, pn, ps);
    int rows = ((Integer) vec.get(0)).intValue();
    CachedRowSet crs = (CachedRowSet) vec.get(1);
    ;

%>


<html>
<head>
    <title>信贷管理</title>
    <link href="/css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
    <script language="JavaScript" type="text/JavaScript">
        <!--
        function newAffair(affairName) {
            var flag = 0;
            if (affairName == 'add') {
                var url = 'Ljd_add.jsp?Flyy_add_del=' + affairName;
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
                        form1.action = 'Ljd_del.jsp';
                        form1.submit();
                    }
                    else {
                        return false;
                    }

                }
            }
        }
        function info(BMNO, FCNO, FCSTATUS) {
            var url = 'history_qf_info.jsp?BMNO=' + BMNO + '&FCNO=' + FCNO + "&FCSTATUS=" + FCSTATUS;
            window.open(url, 'few', 'height=500,width=800,toolbar=no,scrollbars=yes,resizable=yes,status=yes');
        }
        function buttonDisabled() {
            with (form2) {
                a.disabled = "true";
                b.disabled = "true";
                c.disabled = "true";
            }
        }
        //-->
    </script>
    <script src='/js/check.js' type='text/javascript'></script>
    <script src='/js/pagebutton.js' type='text/javascript'></script>
    <script src="/js/flippage2.js" type="text/javascript"></script>
</head>
<body background="/images/checks_02.jpg">
<form action="history_qf.jsp" name="form1" method="post">

<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<input type="hidden" name="BMNO" value="<%=BMNO%>">             <%--lj added in 2007-05-13--%>
<input type="hidden" name="CREATEDATE" value="<%=CREATEDATE%>"> <%--lj added in 2007-05-29--%>
<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699"
       bgcolor="AACCEE">
<tr align="left">
    <td height="30" bgcolor="#4477AA"><img src="/images/form/xing1.JPG" align="absmiddle"> <font
            size="2" color="#FFFFFF"><b>历史清分信息</b></font> <img src="/images/form/xing1.JPG"
                                                               align="absmiddle"></td>
</tr>
<tr align="center">
    <td height="260" valign="middle">
        <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
            <tr>
                <td width="20">&nbsp;</td>
                <td align="center" valign="middle">
                    <script src='/js/querybutton.js' type='text/javascript'></script>
                    <script src='/js/meizzDate.js' type='text/javascript'></script>
                    <table>
                        <tr>
                            <td>
                                <table cellpadding='0' cellspacing='0' border='0'>
                                    <tr>
                                        <td height='5'></td>
                                    </tr>
                                </table>
                                <table class='list_form_table' width='530' align='center'
                                       cellpadding='0' cellspacing='1' border='0'>
                                    <tr class='list_form_title_tr'>
                                        <td width='13%' class='list_form_title_td' nowrap align="center">清分日期</td>
                                        <td width='15%' class='list_form_title_td' nowrap align="center">自动清分结果</td>
                                        <td width='20%' class='list_form_title_td' nowrap align="center">人工认定结果</td>
                                        <td width='15%' class='list_form_title_td' nowrap align="center">时点余额</td>
                                        <td width='14%' class='list_form_title_td' nowrap align="center">最后审批人</td>
                                        <td width='14%' class='list_form_title_td' nowrap align="center">审批日期</td>
                                        <td width='8%' class='list_form_title_td' nowrap align="center">详细</td>
                                    </tr>
                                    <%
                                        while (crs.next()) {
                                    %>
                                    <tr class='list_form_tr'>
                                        <td nowrap class='list_form_td'><%=crs.getString("CREATEDATE")%>
                                        </td>
                                        <td nowrap class='list_form_td'
                                            align="center"><%=level.getEnumItemName("LoanCat1", crs.getString("FCAUTO"))%>
                                        </td>
                                        <td nowrap class='list_form_td'
                                            align="center"><%=level.getEnumItemName("LoanCat1", crs.getString("FCCLASS"))%>
                                        </td>
                                        <td nowrap class='list_form_td_money'
                                            align="left"><%=df.format(crs.getBigDecimal("BAL"))%>
                                        </td>
                                        <td nowrap class='list_form_td'
                                            align="center"><%=crs.getString("OPERATOR") == null || crs.getString("OPERATOR").trim().equals("") ? "" : SCUser.getName(crs.getString("OPERATOR"))%>
                                        </td>
                                        <td nowrap class='list_form_td'
                                            align="center"><%=crs.getString("LASTMODIFIED")%>
                                        </td>
                                        <td nowrap class='list_form_td' align="center">
                                            <a class="list_edit_href"
                                               href='javascript:info("<%=crs.getString("BMNO")%>","<%=crs.getString("FCNO")%>","<%=crs.getString("FCSTATUS")%>")'>详细</a>
                                            <input type="hidden" name="FCNO"
                                                   value="<%=crs.getString("FCNO")%>"> <%--lj added in 2007-05-13--%>
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
                            <td class="list_form_button_td"><input type='submit' name='a'
                                                                   class='list_button_active'
                                                                   value=' 刷新 '
                                                                   onclick="history.go(0)"></td>
                            <td class='page_button_tbl_td'><input type='button'
                                                                  class='page_button_active'
                                                                  name='button' value=' 关闭 '
                                                                  onClick="parent.close();"></td>
                            <script language="javascript" type="text/javascript">
                                createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "history_qf.jsp?pn=", "form1");
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
    document.title = "历史清分信息";
    document.focus();
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
        document.form1.FCCLASS.value = "";
    }
</script>

<%
    //MyDB.getInstance().removeCurrentThreadConn("workbench.list(END).jsp"); //added by JGO on 2004-07-17
%>