<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.db.ConnectionManager" %>
<%@ page import="zt.platform.db.DatabaseConnection" %>
<%--
===============================================
Title: 消费信贷-客户密码修改
Description: 个人消费分期客户密码修改。
 * @version  $Revision: 1.0 $  $Date: 2009/03/02 08:20:31 $
 * @author liujian
 * <p/>修改：$Author: liuj $
===============================================
--%>
<%
    request.setCharacterEncoding("GBK");

    String IDTYPE = request.getParameter("IDTYPE");                 //证件名称
    String ID = request.getParameter("ID");                         //证件号码
    String NAME = request.getParameter("NAME");                     //客户名称 desc=企业(个人)名称
    String PASSWORD = request.getParameter("PASSWORD");            //客户新密码
    String OLDPASSWORD = request.getParameter("OLDPASSWORD");      //客户原密码

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
            session.setAttribute("msg", "修改成功！");
            session.setAttribute("isback", "0");
            session.setAttribute("goUrl", "../app.jsp");
            response.sendRedirect("../showinfo.jsp");
        } else {
            session.setAttribute("msg", "原密码错误，请重新录入!");
            session.setAttribute("goUrl", "./apppass.jsp?NAME=" + NAME + "&IDTYPE=" + IDTYPE.trim() + "&ID=" + ID.trim());
//            response.sendRedirect("../showinfo.jsp");
%>
<jsp:forward page="/showinfo.jsp"/>
<%
    }
} else {
    String readonly = "";
    String submit = "class='btn1_mouseout'";
    String title = "个人密码修改";
%>
<html>
<head>
    <title>消费信贷</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="../js/pwdCheck.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
            margin-left:0px;
            margin-right:0px;
        }

        -->
    </style>
</head>
<body>
<div style="width:100%;">
    <table height="42px" width="100%" border="0" align="left" cellpadding="2" cellspacing="2"
          >
        <tr align="left">
            <td width="30%" style="BACKGROUND: url(../images/headlogo.JPG) no-repeat;"></td>

        </tr>
    </table>
</div>
<div class="navNo">
</div>
<div>
<br>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
    <tr class='page_form_tr'>
        <td valign="top">
            <table height="325" border="0" align="center" cellpadding="2" cellspacing="2" bordercolor="#ffffff"
                   bgcolor="#ffffff">
                <tr align="left">
                    <td height="30" bgcolor="#A4AEB5"><img src="../images/form/xing1.jpg" align="absmiddle"> <font
                            size="2"
                            color="#FFFFFF"><b>海尔集团财务有限责任公司个人消费分期客户密码修改</b></font>
                        <img src="../images/form/xing1.jpg" align="absmiddle"></td>
                </tr>
                <tr align="center">
                    <td height="260" valign="middle">
                        <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                            <tr class='page_form_tr'>
                                <td width="20">&nbsp;</td>
                                <td align="center" valign="top">
                                    <script src='../js/main.js' type='text/javascript'></script>
                                    <script src='../js/check.js' type='text/javascript'></script>
                                    <script src='../js/checkID2.js' type='text/javascript'></script>
                                    <form id='winform' method='post' action='./apppass.jsp'>

                                        <table class='page_form_regTable' id='page_form_table' width="100%">
                                            <col width="80"/>
                                            <col width="145"/>
                                            <tr class='page_form_tr'>
                                                <td colspan="5" align="center" class="title1">
                                                    密码修改
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
                                                <td class="page_form_title_td" nowrap>姓&nbsp;&nbsp;名：</td>
                                                <td class="page_form_td" nowrap><%=NAME == null ? "" : NAME%>
                                                </td>
                                            </tr>
                                            <%} else {%>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>身份证件名称：</td>
                                                <td class="page_form_td" nowrap><select name="IDTYPE"
                                                                                        class="page_form_select">
                                                    <option value='0'>身份证</option>
                                                </select>
                                                    <%--<%=level.levelHere("IDTYPE", "IDType", IDTYPE)%>--%>
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>证件号码：</td>
                                                <td class="page_form_td" nowrap><input type="text" <%=readonly%>
                                                                                       name="ID" id="ID"
                                                                                       value="<%=ID==null?"":ID%>"
                                                                                       class="page_form_text"
                                                                                       onblur="checkIDCard(document.getElementById('IDTYPE'),this);">
                                                </td>
                                            </tr>
                                            <%}%>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>原密码：</td>
                                                <td class="page_form_td" nowrap><input type="password" <%=readonly%>
                                                                                       name="OLDPASSWORD"
                                                                                       id="OLDPASSWORD"
                                                                                       value=""
                                                                                       class="page_form_text">
                                                </td>
                                            </tr>
                                            
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>新密码：</td>
                                                <td class="page_form_td" nowrap><input type="password" <%=readonly%>
                                                                                       name="PASSWORD" id="PASSWORD"
                                                                                       value=""
                                                                                       onKeyUp=pwStrength(this.value) onBlur=pwStrength(this.value)
                                                                                       onkeydown='if(event.keyCode==13) event.keyCode=9'
                                                                                       class="page_form_text">
                                                </td>
                                            </tr>
                                            <tr id="pwdStrenTab" style='display:none;'>
                                                <td>&nbsp;</td>
                                                <td>
                                                    <table width="160px" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="center" bgcolor="#eeeeee">
                                                       <td width="33%" id="strength_L">弱</td>
                                                       <td width="33%" id="strength_M">中</td>
                                                       <td width="33%" id="strength_H">强</td>
                                                    </tr>
                                                </table>
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_form_title_td" nowrap>密码确认：</td>
                                                <td class="page_form_td" nowrap><input type="password" <%=readonly%>
                                                                                       name="PASSWORDR" id="PASSWORDR"
                                                                                       value=""
                                                                                       class="page_form_text">
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_button_tbl_tr" colspan="5" height="0"></td>
                                            </tr>
                                        </table>
                                    </form>
                                </td>
                                <td width="20">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr height="35" align="center" valign="top">
                    <td align="center">
                        <table border="0" cellspacing="0" cellpadding="0" width="538">
                            <tr class='page_form_tr'>
                                <td nowrap align="center">
                                    <table>
                                        <tr class='page_button_tbl_tr'>
                                            <td class='page_button_tbl_td'><input style="width:90px;" type='button' <%=submit%> id='goback'
                                                                                  name='goback'
                                                                                  value=' 返 回 '
                                                                                  onClick="history.go(-1)"></td>
                                            <td class='page_button_tbl_td'><input style="width:90px;" type='button' <%=submit%> id='saveadd'
                                                                                  name='save'
                                                                                  value=' 确 定 '
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
</div>
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
            alert("密码长度不可小于6位！");
            obj1.focus();
            return false;
        } else if (obj1.value.length > 20) {
            alert("密码长度不要大于20位！");
            obj1.focus();
            return false;
        } else if (obj1.value != obj2.value) {
            alert("两次密码输入不一致");
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
