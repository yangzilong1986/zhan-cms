<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%
    request.setCharacterEncoding("GBK");
    String FCNO = request.getParameter("FCNO");
    String FCSTATUS = request.getParameter("FCSTATUS");
    if (FCNO == null || FCSTATUS == null) {
        session.setAttribute("lettermess", "没有发现传送入的参数！");
        response.sendRedirect("/fcworkbench/lettersucces.jsp");
    }
    String mess = (String) request.getAttribute("mess");
    if (mess == null) {
        mess = "";
    } else {
        mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
    }
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
    <script src='/js/check.js' type='text/javascript'></script>
    <script src='/js/pagebutton.js' type='text/javascript'></script>
    <script src="/js/flippage2.js" type='text/javascript'></script>
    <script src="/js/focusnext.js" type='text/javascript'></script>
</head>
<body background="/images/checks_02.jpg">
<form action="q_fcwysfx_add_save.jsp" name="form1" method="post" id="winform" onsubmit="return Regvalid(this)">
<input type="hidden" name="FCNO" value="<%=FCNO%>">
<input type="hidden" name="FCSTATUS" value="<%=FCSTATUS%>">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
    <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699"
           bgcolor="AACCEE">
        <tr align="left">
            <td height="30" bgcolor="#4477AA"><img src="/images/form/xing1.JPG" align="absmiddle"> <font
                    size="2" color="#FFFFFF"><b>非财务因素分析</b></font> <img src="/images/form/xing1.JPG"
                                                                        align="absmiddle"></td>
        </tr>
        <tr align="center">

            <td height="260" valign="middle">
                <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <td width="20">&nbsp;</td>
                        <td align="center" valign="middle">
                            <table class='error_message_tbl'>
                                <tr class='error_message_tbl_tr'>
                                    <td class='error_message_tbl_td'><%=mess%>
                                    </td>
                                </tr>
                            </table>
                            <script src='/js/querybutton.js' type='text/javascript'></script>
                            <script src='/js/meizzMonth.js' type='text/javascript'></script>
                            <table>
                                <tr>
                                    <td>
                                        <table cellpadding='0' cellspacing='0' border='0'>
                                            <tr>
                                                <td height='5'></td>
                                            </tr>
                                        </table>
                                        <table class='page_form_table' width='530' align='center'
                                               cellpadding='0' cellspacing='1' border='0'>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_td" nowrap>&nbsp;</td>
                                                <td class="page_form_td" nowrap>&nbsp;</td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_td" nowrap>&nbsp;</td>
                                                <td class="page_form_td" nowrap>&nbsp;</td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class='list_form_title_td' align="left">日期（年月）</td>
                                                <td class='list_form_title_td' align="left">
                                                    <input type="text" name="DT" value=""
                                                           class="page_form_text">
                                                    <input name="button" type="button"
                                                           class='page_button_active'
                                                           onclick="setday(this,winform.DT)" value="…">
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class=
                                                        "page_form_td" align="left">非财务因素分析<br>（最多300汉字）
                                                </td>
                                                <td class="page_form_td"><textarea name="CMT1" wrap="PHYSICAL" cols="55"
                                                                                   rows="3"
                                                                                   class="page_form_text"></textarea>
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_td" nowrap>&nbsp;</td>
                                                <td class="page_form_td" nowrap>&nbsp;</td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_td" nowrap>&nbsp;</td>
                                                <td class="page_form_td" nowrap>&nbsp;</td>
                                            </tr>
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
                                                                           class='list_button_active' value=' 提 交 '>
                                    </td>
                                    <td class='page_button_tbl_td'><input type='button' class='page_button_active'
                                                                          name='button' value=' 关 闭 '
                                                                          onClick="return user_check();"></td>
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
    document.title = "非财务因素分析";
    document.focus();
    function Regvalid(form) {
        var DT = form1.DT;
        var DTVALUE = DT.value;
        var CMT1 = form1.CMT1;
        var CMT1VAL = CMT1.value;
        if (DTVALUE.length == 0) {
            alert("请输入正确的日期！");
            DT.focus();
            return false;
        }
        else if (!isDate2(DTVALUE)) {
            alert("请输入正确的日期！");
            DT.focus();
            return false;
        }
        if (CMT1VAL.length > 300) {
            alert("最多输入300个汉字！");
            CMT1.focus();
            return false;
        }
        return true;
    }

    function user_check() {
        this.opener.location = "/fcworkbench/q_fcwysfx/q_fcwysfx.jsp?pn=" + this.opener.document.all.pnstr.value + "" +
                               "&FCNO=<%=FCNO%>" +
                               "&FCSTATUS=<%=FCSTATUS%>";
        this.close();
    }
</script>