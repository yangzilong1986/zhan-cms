<!--jsp:include page="/checkpermission.jsp"/-->
<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cmsi.pub.code.FCReason" %>
<%@ page import="zt.cmsi.pub.define.BMFCLimit" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>

<%--
===============================================
Title: ��ֹ���̨
Description: �ֹ���Ӵ��������Ϣ-��ʱ��ʹ�á�
 * @version   $Revision: 1.2 $  $Date: 2007/04/30 08:16:58 $
 * @author   houcs
 * <p/>�޸ģ�$Author: liuj $
===============================================
--%>

<%
    request.setCharacterEncoding("GBK");
    String mess = (String) request.getAttribute("mess");
    if (mess == null) {
        mess = "";
    } else {
        mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
    }
%>
<html>
<head>
    <title>�Ŵ�����</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <script language="javascript" src="/js/focusnext.js" type="text/javascript"></script>
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
</head>

<body background="../images/checks_02.jpg">
<form name="form1" method="post" action="Flyy_add_save.jsp" onsubmit="return Regvalid(this)">
    <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td align="center" valign="middle">
                <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699"
                       bgcolor="AACCEE">
                    <tr align="left">
                        <td height="30" bgcolor="#4477AA"><img src="../images/form/xing1.JPG" align="absmiddle"> <font
                                size="2" color="#FFFFFF"><b>�弶����ԭ������</b></font> <img src="../images/form/xing1.JPG"
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
                                        <table class='page_form_table' id='page_form_table' width="300" height="20">

                                            <tr class='page_form_tr'>
                                                <td class="page_form_td" nowrap>&nbsp;</td>
                                                <td class="page_form_td" nowrap>&nbsp;</td>
                                            </tr>


                                            <tr class='page_form_tr'>
                                                <td class="page_form_td" nowrap>�弶����</td>
                                                <td class="page_form_td" nowrap>
                                                    <%=level.levelHere("FCCLASS", "LoanCat1")%>
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_td" nowrap>ԭ��<br>(���300������)</td>
                                                <td class="page_form_td" nowrap>
                                                    <textarea name="REASON" wrap="PHYSICAL" cols="30" rows="3"
                                                              class="page_form_text" maxlength="600"></textarea></td>

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
                                                                                       class='page_button_disabled'
                                                                                       id="add"
                                                                                       value=' �� �� ' disabled='true'>
                                                </td>
                                                <td class="list_form_button_td"><input name='del' type='button'
                                                                                       class='page_button_disabled'
                                                                                       id="del"
                                                                                       value=' ɾ �� ' disabled='true'>
                                                </td>
                                                <td class='page_button_tbl_td'><input type='submit'
                                                                                      class='page_button_active'
                                                                                      id='saveadd' name='save'
                                                                                      value=' �� �� '></td>
                                                <td class='page_button_tbl_td'><input type='button'
                                                                                      class='page_button_active'
                                                                                      name='button' value=' �� �� '
                                                                                      onClick="return user_check();">
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
    document.title = "�弶����ԭ������";
    document.focus();
    function Regvalid(form) {
        var REASON = form1.REASON;
        if (REASON.value.length > 300) {
            alert("���300�����֣�");
            REASON.focus();
            return false;
        }
    }
    function user_check() {
        this.opener.location = "/wjflset/Flyy_Set.jsp?pn=" + this.opener.document.all.pnstr.value + "";
        this.close();
    }
</script>
