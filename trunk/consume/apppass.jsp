<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.db.ConnectionManager" %>
<%@ page import="zt.platform.db.DatabaseConnection" %>
<%--
===============================================
Title: �����Ŵ�-�ͻ������޸�
Description: �������ѷ��ڿͻ������޸ġ�
 * @version  $Revision: 1.0 $  $Date: 2009/03/02 08:20:31 $
 * @author liujian
 * <p/>�޸ģ�$Author: liuj $
===============================================
--%>
<%
    request.setCharacterEncoding("GBK");

    String IDTYPE = request.getParameter("IDTYPE");                 //֤������
    String ID = request.getParameter("ID");                         //֤������
    String NAME = request.getParameter("NAME");                     //�ͻ����� desc=��ҵ(����)����
    String PASSWORD = request.getParameter("PASSWORD");            //�ͻ�������
    String OLDPASSWORD = request.getParameter("OLDPASSWORD");      //�ͻ�ԭ����

    OLDPASSWORD = (OLDPASSWORD == null) ? "" : OLDPASSWORD.trim();

    int temp = 0;
    if (!OLDPASSWORD.equals("")) {

        ConnectionManager manager = ConnectionManager.getInstance();
        DatabaseConnection con = manager.getConnection();
        String sql = "update CMINDVCLIENT set PASSWORD='" + PASSWORD + "' " +
                "where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and PASSWORD='" + OLDPASSWORD + "' ";

        temp = con.executeUpdate(sql);
        manager.releaseConnection(con);
        if (temp > 0) {
            session.setAttribute("msg", "�޸ĳɹ���");
            session.setAttribute("isback", "0");
            session.setAttribute("goUrl", "../app.jsp");
            response.sendRedirect("../showinfo.jsp");
        } else {
            session.setAttribute("msg", "ԭ�������������¼��!");
            session.setAttribute("goUrl", "./apppass.jsp?NAME=" + NAME + "&IDTYPE=" + IDTYPE.trim() + "&ID=" + ID.trim());
//            response.sendRedirect("../showinfo.jsp");
%>
<jsp:forward page="/showinfo.jsp"/>
<%
    }
} else {
    String readonly = "";
    String submit = "class='page_button_active'";
    String title = "���������޸�";
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
<body background="../images/checks_02.jpg">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
    <tr class='page_form_tr'>
        <td align="center" valign="middle">
            <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#816A82"
                   bgcolor="#E0E0D3">
                <tr align="left">
                    <td height="30" bgcolor="#A4AEB5"><img src="../images/form/xing1.jpg" align="absmiddle"> <font
                            size="2"
                            color="#FFFFFF"><b>�������Ų����������ι�˾�������ѷ��ڿͻ������޸�</b></font>
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
                                    <form id='winform' method='post' action='./apppass.jsp'>

                                        <table class='page_form_table' id='page_form_table' width="100%">
                                            <col width="80"/>
                                            <col width="145"/>
                                            <tr class='page_form_tr'>
                                                <td colspan="5" align="center" class="page_form_List_title">
                                                    �����޸�
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_button_tbl_tr" colspan="5" height="5"></td>
                                            </tr>
                                            <%if (!ID.equals("")) {%>
                                            <input type="hidden" name="NAME" value="<%=NAME%>">
                                            <input type="hidden" name="IDTYPE" value="<%=IDTYPE%>">
                                            <input type="hidden" name="ID" value="<%=ID%>">
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>��&nbsp;&nbsp;��</td>
                                                <td class="page_form_td" nowrap><%=NAME == null ? "" : NAME%>
                                                </td>
                                            </tr>
                                            <%} else {%>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>���֤������</td>
                                                <td class="page_form_td" nowrap><select name="IDTYPE"
                                                                                        class="page_form_select">
                                                    <option value='0'>���֤</option>
                                                </select>
                                                    <%--<%=level.levelHere("IDTYPE", "IDType", IDTYPE)%>--%>
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>֤������</td>
                                                <td class="page_form_td" nowrap><input type="text" <%=readonly%>
                                                                                       name="ID" id="ID"
                                                                                       value="<%=ID==null?"":ID%>"
                                                                                       class="page_form_text"
                                                                                       onblur="checkIDCard(document.getElementById('IDTYPE'),this);">
                                                </td>
                                            </tr>
                                            <%}%>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>ԭ����</td>
                                                <td class="page_form_td" nowrap><input type="password" <%=readonly%>
                                                                                       name="OLDPASSWORD"
                                                                                       id="OLDPASSWORD"
                                                                                       value=""
                                                                                       class="page_form_text">
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>������</td>
                                                <td class="page_form_td" nowrap><input type="password" <%=readonly%>
                                                                                       name="PASSWORD" id="PASSWORD"
                                                                                       value=""
                                                                                       class="page_form_text">
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>����ȷ��</td>
                                                <td class="page_form_td" nowrap><input type="password" <%=readonly%>
                                                                                       name="PASSWORDR" id="PASSWORDR"
                                                                                       value=""
                                                                                       class="page_form_text">
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
                                                                                  value=' ȷ �� '
                                                                                  onClick="return Regvalid();"></td>
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

    function onCheck(idobj) {
        return checkIDCard(document.getElementById("IDTYPE"), idobj);
    }

    function goSave() {
    <%if (ID.equals("")) {%>
        if (!isEmptyItem("IDTYPE"))return false;
        if (!isEmptyItem("ID"))return false;
    <%}%>
        if (!isEmptyItem("OLDPASSWORD"))return false;
        if (!isEmptyItem("PASSWORD"))return false;
        if (!isEmptyItem("PASSWORDR"))return false;
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
            <%if (ID.equals("")) {%>if (onCheck(document.getElementById("ID"))) <%}%>
                document.forms[0].submit();
        }
    }
</script>

<%
    }
%>
