<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%--
===============================================
Title: �����Ŵ�-�ͻ�ע��
Description: �������ѷ��ڿͻ�ע�ᡣ
 * @version  $Revision: 1.0 $  $Date: 2009/03/02 08:20:31 $
 * @author liujian
 * <p/>�޸ģ�$Author: liuj $
===============================================
--%>
<%
    request.setCharacterEncoding("GBK");
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (um == null) {
//        response.sendRedirect("../fcworkbench/error.jsp");
//    }

    String PASSWORD = request.getParameter("PASSWORD");   //�ͻ�����
    String IDTYPE = request.getParameter("IDTYPE");       //֤������
    String ID = request.getParameter("ID");                //֤������
    String NAME = (String)session.getAttribute("NAME");    //�ͻ����� desc=��ҵ(����)����
    NAME = (NAME == null) ? "" : NAME;

    String readonly = "";
    String readonly_input = "readonly";
    String submit = "class='page_button_active'";

    String title = "�������ѷ��ڸ���ͻ���Ϣ";
%>
<html>
<head>
    <title>�����Ŵ�</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
</head>
<body background="../images/checks_02.jpg" onload="document.all.NAME.focus();">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
    <tr class='page_form_tr'>
        <td align="center" valign="middle">
            <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#816A82"
                   bgcolor="#E0E0D3">
                <tr align="left">
                    <td height="30" bgcolor="#A4AEB5"><img src="../images/form/xing1.jpg" align="absmiddle"> <font
                            size="2"
                            color="#FFFFFF"><b>�������Ų����������ι�˾�������ѷ����û�ע��</b></font>
                        <img src="../images/form/xing1.jpg" align="absmiddle"></td>
                </tr>
                <tr align="center">
                    <td height="260" valign="middle">
                        <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                            <tr class='page_form_tr'>
                                <td width="20">&nbsp;</td>
                                <td align="center" valign="middle">
                                    <script src='../js/main.js' type='text/javascript'></script>
                                    <script src='../js/check.js' type='text/javascript'></script>
                                    <script src='../js/checkID2.js' type='text/javascript'></script>
                                    <form id='winform' method='post' action='./appuser.jsp'>
                                        <table class='page_form_table' id='page_form_table' width="100%">
                                            <col width="80"/>
                                            <col width="145"/>
                                            <tr class='page_form_tr'>
                                                <td colspan="5" align="center" class="page_form_List_title">
                                                    �������Ų����������ι�˾�������ѷ����û�ע��
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_button_tbl_tr" colspan="5" height="5"></td>
                                            </tr>

                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>��&nbsp;&nbsp;��</td>
                                                <td class="page_form_td" nowrap><input type="text" <%=readonly%>
                                                                                       name="NAME"
                                                                                       value="<%=NAME==null?"":NAME%>"
                                                                                       class="page_form_text" maxlength="40"></td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>���֤������</td>
                                                <td class="page_form_td" nowrap>
                                                    <%--<select name="IDTYPE" class="page_form_select">--%>
                                                        <%--<option value='0'>���֤</option>--%>
                                                    <%--</select>--%>
                                                    <%=level.levelHere("IDTYPE", "IDType", IDTYPE)%>
                                                </td>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>֤������</td>
                                                <td class="page_form_td" nowrap><input type="text" <%=readonly%>
                                                                                       name="ID" id="ID"
                                                                                       value="<%=ID==null?"":ID%>"
                                                                                       class="page_form_text" maxlength="18">
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>��&nbsp;&nbsp;��</td>
                                                <td class="page_form_td" nowrap><input type="password" <%=readonly%>
                                                                                       name="PASSWORD" id="PASSWORD"
                                                                                       value="<%=PASSWORD==null?"":PASSWORD%>"
                                                                                       class="page_form_text"
                                                                                       onkeydown='if(event.keyCode==13) event.keyCode=9' maxlength="20">
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>����ȷ��</td>
                                                <td class="page_form_td" nowrap><input type="password" <%=readonly%>
                                                                                       name="PASSWORDR" id="PASSWORDR"
                                                                                       value=""
                                                                                       class="page_form_text"
                                                                                       onkeydown='if(event.keyCode==13) document.getElementById("saveadd").focus();' maxlength="20">
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_button_tbl_tr" colspan="5" height="5"></td>
                                            </tr>
                                        </table>
                                    </form>
                                </td>
                                <td width="20">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr height="35" align="center" valign="middle">
                    <td align="center">
                        <table border="0" cellspacing="0" cellpadding="0" width="538">
                            <tr class='page_form_tr'>
                                <td nowrap align="center">
                                    <table class='page_button_tbl'>
                                        <tr class='page_button_tbl_tr'>
                                            <td class='page_button_tbl_td'><input type='button' <%=submit%> id='goback'
                                                                                  name='goback'
                                                                                  value=' �� �� '
                                                                                  onClick="history.go(-1)"></td>
                                            <td class='page_button_tbl_td'><input type='button' <%=submit%> id='saveadd'
                                                                                  name='save'
                                                                                  value=' ��һ�� '
                                                                                  onClick="return Regvalid();"></td>
                                            <%--<td class='page_button_tbl_td'><input type='button'--%>
                                            <%--class='page_button_active'--%>
                                            <%--name='button'--%>
                                            <%--value=' �� �� '--%>
                                            <%--onClick="pageWinClose();"></td>--%>
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
</body>
</html>

<%

%>
<script language="javascript" type="text/javascript">
    document.title = "<%=title%>";
    document.focus();

    function goSave() {
        if (!isEmptyItem("NAME")) return false;
        if (!isEmptyItem("ID"))return false;
        if (!isEmptyItem("PASSWORD"))return false;
        if (!isEmptyItem("PASSWORDR"))return false;
        if (!checkIDCard(document.getElementById('IDTYPE'), document.getElementById('ID')))return false;
        if (!chkpass(document.getElementById("PASSWORD"), document.getElementById("PASSWORDR")))return false;
        return true;
    }

    function chkpass(obj1, obj2) {
        if (obj1.value.length < 6) {
            alert("���볤�Ȳ���С��6λ��");
            obj1.focus();
            return false;
        } else if (obj1.value.length > 20) {
            alert("���볤�Ȳ�Ҫ����20λ��");
            obj1.focus();
            return false;
        } else if (obj1.value != obj2.value) {
            alert("�����������벻һ��");
            obj2.value = "";
            obj1.focus();
            return false;
        }
        return true;
    }


    function Regvalid() {
        if (goSave()) {
            document.forms[0].action = "/consume/appuser.jsp?goUrl=../app.jsp";
            document.forms[0].submit();
            //            var url = "./appuser.jsp?NAME=" + document.getElementsByName("NAME")[0].value + "&IDTYPE=" + document.getElementsByName("IDTYPE")[0].value + "&ID=" + document.getElementsByName("ID")[0].value + "&PASSWORD=" + document.getElementsByName("PASSWORD")[0].value + "&showinfo=0";
            //            window.open(url, 'APPUSER', 'left=0,top=0,height=700,width=870,toolbar=no,scrollbars=yes,resizable=yes');
        }
    }
</script>
