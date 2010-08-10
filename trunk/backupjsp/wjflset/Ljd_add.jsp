<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cmsi.pub.define.BMFCLimit" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    request.setCharacterEncoding("GBK");
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
/*String brhid=request.getParameter("BRHID").trim();
ConnectionManager manager=ConnectionManager.getInstance();

String sql="select * from FCREVIEWLIMIT where BRHID='"+brhid+"' order by BRHID ";

CachedRowSet crs=manager.getRs(sql);
BigDecimal LIMITAMT=null;
if(crs.next()){
	LIMITAMT=crs.getBigDecimal("LIMITAMT"); 
}*/
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
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <script src='/js/check.js' type='text/javascript'></script>
    <script language="javascript" src="/js/focusnext.js"></script>
    <script src='/js/fcmain.js' type='text/javascript'></script>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
</head>

<body background="../images/checks_02.jpg">
<form id='winform' name="form1" method="post" action="Ljd_add_save.jsp" onsubmit="return Regvalid(this)">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699"
       bgcolor="AACCEE">
<tr align="left">
    <td height="30" bgcolor="#4477AA"><img src="../images/form/xing1.jpg" align="absmiddle"> <font
            size="2" color="#FFFFFF"><b>清分临界点额度设置</b></font> <img src="../images/form/xing1.jpg"
                                                                  align="absmiddle"></td>
</tr>
<tr align="center">
    <td height="260" valign="middle">
        <input name="referValue" type="hidden" value="">
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
                    <table class='page_form_table' id='page_form_table' width="300" height="20">

                        <tr class='page_form_tr'>
                            <td class="page_form_td" nowrap>&nbsp;</td>
                            <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>


                        <tr class='page_form_tr'>
                            <td class="page_form_td" nowrap>网点代码</td>
                            <td class="page_form_td" nowrap>
                                <input type="text" name="brhid" value="" class="page_form_text"
                                       size="25" readonly>

                                <input type="button" name="nameref" value="…"
                                       onclick="deptrefer_click()" class="page_form_refbutton">
                            </td>
                        </tr>
                        <tr class='page_form_tr'>
                            <td class="page_form_td" nowrap>网点级别</td>
                            <td class="page_form_td" nowrap>
                                <input type='text' name='brhjb' value="" class="page_form_text"
                                       size="25" readonly>

                            </td>
                        </tr>

                        <tr class='page_form_tr'>
                            <td class="page_form_td" nowrap>网点名称</td>
                            <td class="page_form_td" nowrap>
                                <input type='text' name='brhname' value="" class="page_form_text"
                                       size="25" readonly>
                            </td>
                        </tr>
                        <tr class='page_form_tr'>
                            <td class="page_form_td" nowrap>自然人额度*</td>
                            <td class="page_form_td" nowrap><input type='text' name='FCTypeAMT'
                                                                   value="" class="page_form_text"
                                                                   size="25" maxlength="14"
                                                                   mayNull="0" dataType="3"
                                                                   errInfo="自然人额度" precision="14"
                                                                   decimalDigits="2"></td>
                        </tr>
                        <tr class='page_form_tr'>
                            <td class="page_form_td" nowrap>临界点额度*</td>
                            <td class="page_form_td" nowrap><input type='text' name='LIMITAMT'
                                                                   value="" class="page_form_text"
                                                                   size="25" maxlength="14"
                                                                   mayNull="0" dataType="3"
                                                                   errInfo="临界点额度" precision="14"
                                                                   decimalDigits="2"></td>
                        </tr>
                        <tr class='page_form_tr'>
                            <td class="page_form_td" nowrap>&nbsp;</td>
                            <td class="page_form_td" nowrap>&nbsp;</td>
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
                        <tr class='list_button_tbl_tr'>
                            <td class="list_form_button_td"><input name='add' type='button'
                                                                   class='page_button_disabled' id="add"
                                                                   value=' 增 加 ' disabled='true'></td>
                            <td class="list_form_button_td"><input name='del' type='button'
                                                                   class='page_button_disabled' id="del"
                                                                   value=' 删 除 ' disabled='true'></td>
                            <td class='page_button_tbl_td'><input type='submit'
                                                                  class='page_button_active'
                                                                  id='saveadd' name='save'
                                                                  value=' 提 交 '></td>
                            <td class='page_button_tbl_td'><input type='button'
                                                                  class='page_button_active'
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
<script language="javascript">
    document.title = "清分临界点额度设置";
    document.focus();
    function Regvalid(form) {
        var brhid = form1.brhid;
        var brhidName = brhid.value;
        if (brhidName.length == 0) {
            alert("请输入正确的网点代码！");
            return false;
        }

        var FCTypeAMT = form1.FCTypeAMT;
        var FCTypeAMTName = FCTypeAMT.value;
        if (!isDigit(FCTypeAMTName)) {
            alert("自然人额度非法输入！");
            FCTypeAMT.focus();
            return false;
        }
        if (FCTypeAMTName < 0 || FCTypeAMTName.length == 0) {
            alert("请输入正确的自然人额度！");
            FCTypeAMT.focus();
            return false;
        }
        if (FCTypeAMTName > 300000) {
            alert("自然人额度不能大于300000！");
            FCTypeAMT.focus();
            return false;
        }

        var LIMITAMT = form1.LIMITAMT;
        var LIMITAMTName = LIMITAMT.value;
        if (isDigit(LIMITAMTName) == false) {
            alert("临界点额度非法输入！");
            //document.form1.LIMITAMT.value="";
            LIMITAMT.focus();
            return false;
        }
        if (LIMITAMTName < 0 || LIMITAMTName.length == 0) {
            alert("请输入正确的临界点额度！");
            //document.form1.LIMITAMT.value="";
            LIMITAMT.focus();
            return false;
        }
    }

    function deptrefer_click() {
        var url = "view.jsp";
        var vArray;
        document.all.referValue.value = window.open(url, 0, "height=400,width=460,toolbar=no,scrollbars=yes,status");
        if (document.all.referValue.value == "undefined") {
            return;
        }

    }
    function clearsearch() {
        document.form1.brhid.value = "";
        document.form1.lname.value = "";
    }

    function user_check() {
        this.opener.location = "/wjflset/Ljd_Set.jsp?pn=" + this.opener.document.all.pnstr.value + "";
        this.close();
    }
</script>
