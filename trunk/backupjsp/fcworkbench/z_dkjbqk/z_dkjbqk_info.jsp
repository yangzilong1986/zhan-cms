<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%
    request.setCharacterEncoding("GBK");
    ConnectionManager manager = ConnectionManager.getInstance();
    String FCNO = request.getParameter("FCNO");
    String DT = request.getParameter("DT");
    String FCSTATUS = request.getParameter("FCSTATUS");
    if (FCNO == null || FCSTATUS == null) {
        session.setAttribute("lettermess", "没有发现传送入的参数！");
        response.sendRedirect("/fcworkbench/lettersucces.jsp");
    }
    String CMT1 = "";
    String CMT2 = "";
    String CMT3 = "";
    String CMT4 = "";


    String sql = "select * from FCMCMT where FCNO='" + FCNO + "' and DT='" + DT + "' and FCCMTTYPE=2";
    CachedRowSet crs = manager.getRs(sql);
    if (crs.next()) {
        CMT1 = crs.getString("CMT1");
        CMT2 = crs.getString("CMT2");
        CMT3 = crs.getString("CMT3");
        CMT4 = crs.getString("CMT4");
    }
    String mess = (String) request.getAttribute("mess");
    if (mess == null) {
        mess = "";
    } else {
        mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
    }


    String readonly = "";
    String submit = "";
    if (FCSTATUS.equals("1")) {
        readonly = "";
        submit = "class='page_button_active'";
    }
    if (FCSTATUS.equals("2")) {
        readonly = "readonly";
        submit = "disabled='true' class='page_button_disabled'";
    }
    if (FCSTATUS.equals("3")) {
        readonly = "readonly";
        submit = "disabled='true' class='page_button_disabled'";
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

    <script src='/js/fcmain.js' type='text/javascript'></script>
    <script src='/js/pagebutton.js' type='text/javascript'></script>
    <script src="/js/flippage2.js" type='text/javascript'></script>
    <script src="/js/focusnext.js" type='text/javascript'></script>
    <script src='/js/check.js' type='text/javascript'></script>
</head>
<body background="/images/checks_02.jpg">
<form action="z_dkjbqk_info_save.jsp" name="form1" method="post" id="winform">
<input type="hidden" name="FCNO" value="<%=FCNO%>">
<input type="hidden" name="FCSTATUS" value="<%=FCSTATUS%>">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699"
       bgcolor="AACCEE">
<tr align="left">
    <td height="30" bgcolor="#4477AA"><img src="/images/form/xing1.JPG" align="absmiddle"> <font
            size="2" color="#FFFFFF"><b>贷款基本情况</b></font> <img src="/images/form/xing1.JPG"
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
                    <script src='/js/meizzDate.js' type='text/javascript'></script>
                    <table>
                        <tr>
                            <td>
                                <table cellpadding='0' cellspacing='0' border='0'>
                                    <tr>
                                        <td height='5'></td>
                                    </tr>
                                </table>
                                <table class='page_form_table' width='400' align='center'
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
                                        <td class='list_form_title_td' align="left">调查日期</td>
                                        <td class='list_form_title_td' align="left">
                                            <input type="text" name="DT" value="<%=DT%>" readonly
                                                   class="page_form_text">

                                        </td>
                                    </tr>
                                    <tr class='page_form_tr'>
                                        <td class="page_form_td" align="left" nowrap>是否按约定用途使<br>用贷款<br>（最多300汉字）
                                        </td>
                                        <td class="page_form_td"><textarea name="CMT1" <%=readonly%>
                                                                           wrap="PHYSICAL" cols="55"
                                                                           rows="3"
                                                                           class="page_form_text"><%=CMT1 == null ? "" : DBUtil.fromDB(CMT1)%>
                                        </textarea></td>
                                    </tr>
                                    <tr class='page_form_tr'>
                                        <td class='page_form_td' align="left" nowrap>贷款逾期情况<br>（最多300汉字）
                                        </td>
                                        <td class="page_form_td" nowrap><textarea
                                                name="CMT2" <%=readonly%> wrap="PHYSICAL" cols="55"
                                                rows="3"
                                                class="page_form_text"><%=CMT2 == null ? "" : DBUtil.fromDB(CMT2)%>
                                        </textarea></td>
                                    </tr>
                                    <tr class='page_form_tr'>
                                        <td class='page_form_td' align="left" nowrap>贷款欠息情况<br>（最多300汉字）
                                        </td>
                                        <td class="page_form_td" nowrap><textarea
                                                name="CMT3" <%=readonly%> wrap="PHYSICAL" cols="55"
                                                rows="3"
                                                class="page_form_text"><%=CMT3 == null ? "" : DBUtil.fromDB(CMT3)%>
                                        </textarea></td>
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
                            <td class="list_form_button_td"><input type='button' class='page_button_active' id='savebtn'
                                                                   name='save' value=' 提 交 '
                                                                   onclick='return Regvalid()' <%=submit%> ></td>
                            <td class='page_button_tbl_td'><input type='button' class='page_button_active' name='button'
                                                                  value=' 关 闭 ' onClick="return user_check();"></td>
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
    document.title = "贷款基本情况";
    document.focus();
    function Regvalid() {
        var DT = form1.DT;
        var DTVALUE = DT.value;
        var CMT1 = form1.CMT1;
        var CMT1VAL = CMT1.value;
        var CMT2 = form1.CMT2;
        var CMT2VAL = CMT2.value;
        var CMT3 = form1.CMT3;
        var CMT3VAL = CMT3.value;

        if (DTVALUE.length == 0) {
            alert("请输入正确的日期！");
            DT.focus();
            return false;
        }
        else if (CMT1VAL.length > 300) {
            alert("最多输入300个汉字！");
            CMT1.focus();
            return false;
        }
        else if (CMT2VAL.length > 300) {
            alert("最多输入300个汉字！");
            CMT2.focus();
            return false;
        }
        else if (CMT3VAL.length > 300) {
            alert("最多输入300个汉字！");
            CMT3.focus();
            return false;
        }

        else {
            winform.submit();
            return true;
        }
    }
    function user_check() {
        this.opener.location = "/fcworkbench/z_dkjbqk/z_dkjbqk.jsp?pn=" + this.opener.document.all.pnstr.value + "" +
                               "&FCNO=<%=FCNO%>" +
                               "&FCSTATUS=<%=FCSTATUS%>";
        this.close();
    }
</script>