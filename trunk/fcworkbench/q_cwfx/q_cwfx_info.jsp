<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    DecimalFormat df = new DecimalFormat("###########0.00");
    request.setCharacterEncoding("GBK");
    ConnectionManager manager = ConnectionManager.getInstance();
    String FCNO = request.getParameter("FCNO");
    String DT = request.getParameter("DT");
    String FCSTATUS = request.getParameter("FCSTATUS");
    if (FCNO == null || FCSTATUS == null) {
        session.setAttribute("lettermess", "û�з��ִ�����Ĳ�����");
        response.sendRedirect("/fcworkbench/lettersucces.jsp");
    }
    if (DT.length() != 10) {
        DT = DBUtil.toSqlDate2(DT + "01");
        System.out.println("DT.length()С��10");
    }

    System.out.println("DT.length()����10");
    String sql = "select * from FCMCMT where FCNO='" + FCNO + "' and DT='" + DT + "' and FCCMTTYPE=7";

    BigDecimal AMT1 = null;
    BigDecimal AMT2 = null;
    BigDecimal AMT3 = null;
    BigDecimal AMT4 = null;
    BigDecimal AMT5 = null;
    BigDecimal AMT6 = null;
    BigDecimal AMT7 = null;
    BigDecimal AMT8 = null;
    BigDecimal AMT9 = null;
    BigDecimal AMT10 = null;
    String CMT1 = "";
    CachedRowSet crs = manager.getRs(sql);
    if (crs.next()) {
        AMT1 = crs.getBigDecimal("AMT1");
        AMT2 = crs.getBigDecimal("AMT2");
        AMT3 = crs.getBigDecimal("AMT3");
        AMT4 = crs.getBigDecimal("AMT4");
        AMT5 = crs.getBigDecimal("AMT5");
        AMT6 = crs.getBigDecimal("AMT6");
        AMT7 = crs.getBigDecimal("AMT7");
        AMT8 = crs.getBigDecimal("AMT8");
        AMT9 = crs.getBigDecimal("AMT9");
        AMT10 = crs.getBigDecimal("AMT10");
        CMT1 = crs.getString("CMT1");
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
    <title>�Ŵ�����</title>
    <link href="/css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
    <script src="/js/focusnext.js" type='text/javascript'></script>
    <script src='/js/pagebutton.js' type='text/javascript'></script>
    <script src='/js/fcmain.js' type='text/javascript'></script>
    <script src="/js/flippage2.js" type='text/javascript'></script>
</head>
<body background="/images/checks_02.jpg">
<form action="" name="form1" method="post" id="winform">
<input type="hidden" name="FCNO" value="<%=FCNO%>">
<input type="hidden" name="FCSTATUS" value="<%=FCSTATUS%>">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699"
       bgcolor="AACCEE">
<tr align="left">
    <td height="30" bgcolor="#4477AA"><img src="/images/form/xing1.JPG" align="absmiddle"> <font
            size="2" color="#FFFFFF"><b>�������</b></font> <img src="/images/form/xing1.JPG"
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
<table class='page_form_table' width='400' align='center'
       cellpadding='0' cellspacing='1' border='0'>
    <tr class='page_form_tr'>
        <td class="page_form_td" nowrap>&nbsp;</td>
        <td class="page_form_td" nowrap>&nbsp;</td>
    </tr>

    <tr class='page_form_tr'>
        <td class='list_form_title_td' align="left">��������</td>
        <td class='list_form_title_td' align="left">
            <input type="text" name="DT" readonly
                   value="<%=DT.length()>8?DBUtil.to_Date(DT).substring(0,6):DT.substring(0,6)%>"
                   class="page_form_text">

        </td>
    </tr>
    <tr class='page_form_tr'>
        <td class=
                "page_form_td" align="left">���ֽ�����
        </td>
        <td class="page_form_td"><input name="AMT1" type="text" <%=readonly%>
                                        class="page_form_text" size="20"
                                        value="<%=AMT1==null?"":df.format(AMT1)%>"
                                        maxlength="14"></td>
    </tr>
    <tr class='page_form_tr'>
        <td class=
                "page_form_td" align="left">��Ӫ�Ծ��ֽ�����
        </td>
        <td class="page_form_td"><input name="AMT2" type="text" <%=readonly%>
                                        class="page_form_text" size="20"
                                        value="<%=AMT2==null?"":df.format(AMT2)%>"
                                        maxlength="14"></td>
    </tr>
    <tr class='page_form_tr'>
        <td class=
                "page_form_td" align="left">��������
        </td>
        <td class="page_form_td"><input name="AMT3" type="text" <%=readonly%> class="page_form_text"
                                        size="20" value="<%=AMT3==null?"":df.format(AMT3)%>"
                                        maxlength="14"></td>
    </tr>
    <tr class='page_form_tr'>
        <td class=
                "page_form_td" align="left">�ʲ���ծ��
        </td>
        <td class="page_form_td"><input name="AMT4" type="text" <%=readonly%> class="page_form_text" size="20"
                                        value="<%=AMT4==null?"":df.format(AMT4)%>" maxlength="14"></td>
    </tr>
    <tr class='page_form_tr'>
        <td class=
                "page_form_td" align="left">����������
        </td>
        <td class="page_form_td"><input name="AMT5" type="text" <%=readonly%> class="page_form_text" size="20"
                                        value="<%=AMT5==null?"":df.format(AMT5)%>" maxlength="14"></td>
    </tr>
    <tr class='page_form_tr'>
        <td class=
                "page_form_td" align="left">�ʲ�������
        </td>
        <td class="page_form_td"><input name="AMT6" type="text" <%=readonly%> class="page_form_text" size="20"
                                        value="<%=AMT6==null?"":df.format(AMT6)%>" maxlength="14"></td>
    </tr>
    <tr class='page_form_tr'>
        <td class=
                "page_form_td" align="left">Ӧ���˿���ת��
        </td>
        <td class="page_form_td"><input name="AMT7" type="text" <%=readonly%> class="page_form_text" size="20"
                                        value="<%=AMT7==null?"":df.format(AMT7)%>" maxlength="14"></td>
    </tr>
    <tr class='page_form_tr'>
        <td class=
                "page_form_td" align="left">�����ת��
        </td>
        <td class="page_form_td"><input name="AMT8" type="text" <%=readonly%> class="page_form_text" size="20"
                                        value="<%=AMT8==null?"":df.format(AMT8)%>" maxlength="14"></td>
    </tr>
    <tr class='page_form_tr'>
        <td class=
                "page_form_td" align="left">Ͷ�ʻ���ֽ�����
        </td>
        <td class="page_form_td"><input name="AMT9" type="text" <%=readonly%> class="page_form_text" size="20"
                                        value="<%=AMT9==null?"":df.format(AMT9)%>" maxlength="14"></td>
    </tr>
    <tr class='page_form_tr'>
        <td class=
                "page_form_td" align="left">���ʻ���ֽ�����
        </td>
        <td class="page_form_td"><input name="AMT10" type="text" <%=readonly%> class="page_form_text" size="20"
                                        value="<%=AMT10==null?"":df.format(AMT10)%>" maxlength="14"></td>
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
                                                                   name='save' value=' �� �� '
                                                                   onclick='return Regvalid()' <%=submit%>></td>
                            <td class='page_button_tbl_td'><input type='button' class='page_button_active' name='button'
                                                                  value=' �� �� ' onClick="return user_check();"></td>
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
document.title = "�������";
document.focus();
function Regvalid() {
    var DT = form1.DT;
    var DTVALUE = DT.value;
    var AMT1 = form1.AMT1;
    var AMT1VAL = AMT1.value;
    var AMT2 = form1.AMT2;
    var AMT2VAL = AMT2.value;
    var AMT3 = form1.AMT3;
    var AMT3VAL = AMT3.value;
    var AMT4 = form1.AMT4;
    var AMT4VAL = AMT4.value;
    var AMT5 = form1.AMT5;
    var AMT5VAL = AMT5.value;
    var AMT6 = form1.AMT6;
    var AMT6VAL = AMT6.value;
    var AMT7 = form1.AMT7;
    var AMT7VAL = AMT7.value;
    var AMT8 = form1.AMT8;
    var AMT8VAL = AMT8.value;
    var AMT9 = form1.AMT9;
    var AMT9VAL = AMT9.value;
    var AMT10 = form1.AMT10;
    var AMT10VAL = AMT10.value;
    // var CMT1=form1.CMT1;
    //var CMT1VAL=CMT1.value;
    if (DTVALUE.length == 0) {
        alert("��������ȷ�����ڣ�");
        DT.focus();
        return false;
    }

    else if (!isDigit(AMT1VAL)) {
        alert("���ֽ������Ƿ����룡");
        //document.form1.AMT1.value="";
        AMT1.focus();
        return false;
    }

    else if (!isDigit(AMT2VAL)) {
        alert("��Ӫ�Ծ��ֽ������Ƿ����룡");
        //document.form1.AMT2.value="";
        AMT2.focus();
        return false;
    }

    else if (!isDigit(AMT3VAL)) {
        alert("�������ʷǷ����룡");
        //document.form1.AMT3.value="";
        AMT3.focus();
        return false;
    }

    else if (!isDigit(AMT4VAL)) {
        alert("�ʲ���ծ�ʷǷ����룡");
        //document.form1.AMT4.value="";
        AMT4.focus();
        return false;
    }

    else if (!isDigit(AMT5VAL)) {
        alert("���������ʷǷ����룡");
        //document.form1.AMT5.value="";
        AMT5.focus();
        return false;
    }

    else if (!isDigit(AMT6VAL)) {
        alert("�ʲ������ʷǷ����룡");
        //document.form1.AMT6.value="";
        AMT6.focus();
        return false;
    }

    else if (!isDigit(AMT7VAL)) {
        alert("Ӧ���˿���ת�ʷǷ����룡");
        //document.form1.AMT7.value="";
        AMT7.focus();
        return false;
    }

    else if (!isDigit(AMT8VAL)) {
        alert("�����ת�ʷǷ����룡");
        //document.form1.AMT8.value="";
        AMT8.focus();
        return false;
    }

    else if (!isDigit(AMT9VAL)) {
        alert("Ͷ�ʻ���ֽ������Ƿ����룡");
        //document.form1.AMT8.value="";
        AMT9.focus();
        return false;
    }

    else if (!isDigit(AMT10VAL)) {
        alert("���ʻ���ֽ������Ƿ����룡");
        //document.form1.AMT10.value="";
        AMT10.focus();
        return false;
    }
    //else if(CMT1VAL.length>300){
    // alert("�������300�����֣�");
    //CMT1.focus();
    //return false;
    // }
    else {
        form1.action = "q_cwfx_info_save.jsp";
        form1.submit();
    }
}
function user_check() {
    this.opener.location = "/fcworkbench/q_cwfx/q_cwfx.jsp?pn=" + this.opener.document.all.pnstr.value + "" +
                           "&FCNO=<%=FCNO%>" +
                           "&FCSTATUS=<%=FCSTATUS%>";
    this.close();
}
</script>