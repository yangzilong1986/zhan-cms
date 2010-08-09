<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%--
===============================================
Title: 消费信贷-客户注册
Description: 个人消费分期客户注册。
 * @version  $Revision: 1.0 $  $Date: 2009/03/02 08:20:31 $
 * @author liujian
 * <p/>修改：$Author: liuj $
===============================================
--%>
<%
    request.setCharacterEncoding("GBK");
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (um == null) {
//        response.sendRedirect("../fcworkbench/error.jsp");
//    }

    String PASSWORD = request.getParameter("PASSWORD");   //客户密码
    String IDTYPE = request.getParameter("IDTYPE");       //证件名称
    String ID = request.getParameter("ID");                //证件号码
    String NAME = (String)session.getAttribute("NAME");    //客户名称 desc=企业(个人)名称
    NAME = (NAME == null) ? "" : NAME;

    String readonly = "";
    String readonly_input = "readonly";
//    String submit = "class='page_button_active'";
    String submit = "class='btn1_mouseout'";
    String title = "个人消费分期付款客户信息";
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
<body onload="document.all.NAME.focus();">
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
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr class='page_form_tr'>
        <td align="center" valign="middle">
            <table width="700px" border="0" align="center" cellpadding="2" cellspacing="2" bordercolor="#816A82"
                   bgcolor="#ffffff">
                 <tr align="left">
                    <td height="30" bgcolor="#A4AEB5"><img src="../images/form/xing1.jpg" align="absmiddle"> <font
                            size="2"
                            color="#FFFFFF"><b>海尔集团财务有限责任公司个人消费分期用户注册</b></font>
                        <img src="../images/form/xing1.jpg" align="absmiddle"></td>
                </tr>
                <tr align="center">
                    <td valign="middle">
                        <script src='../js/main.js' type='text/javascript'></script>
                        <script src='../js/check.js' type='text/javascript'></script>
                        <script src='../js/checkID2.js' type='text/javascript'></script>
                        <form id='winform' method='post' action='./appuser.jsp'>
                            <table class='page_form_regTable' border="0" cellpadding="8" id='page_form_table' width="100%">
                                <col width="100"/>
                                <col width="150"/>
                                <col width="300"/>

                                <tr class="title1">
                                    <td colspan="3">
                                        设置用户名
                                    </td>
                                </tr>
                                <tr class="page_form_tr">
                                    <td class="page_form_title_td" nowrap>姓&nbsp;&nbsp;名：</td>
                                    <td class="page_form_td" nowrap><input type="text" <%=readonly%>
                                                                           name="NAME"
                                                                           value="<%=NAME==null?"":NAME%>"
                                                                           class="page_form_text" maxlength="40"></td>
                                    <td class="remarkCell">
                                        最多可输入40个字符！
                                    </td>

                                </tr>
                                 <tr class="title1">
                                    <td colspan="3">
                                        设置证件号
                                    </td>
                                </tr>
                                <tr class='page_form_tr'>
                                    <td class="page_form_title_td" nowrap>身份证件名称：</td>
                                    <td class="page_form_td" nowrap>
                                        <%--<select name="IDTYPE" class="page_form_select">--%>
                                            <%--<option value='0'>身份证</option>--%>
                                        <%--</select>--%>
                                        <%=level.levelHere("IDTYPE", "IDType", IDTYPE)%>
                                    </td>
                                    <td class="remarkCell">
                                        请选择你要输入的身份证明种类
                                    </td>
                                </tr>
                                <tr class='page_form_tr'>
                                    <td class="page_form_title_td" nowrap>证件号码：</td>
                                    <td class="page_form_td" nowrap><input type="text" <%=readonly%>
                                                                           name="ID" id="ID"
                                                                           value="<%=ID==null?"":ID%>"
                                                                           class="page_form_text" maxlength="18">
                                    </td>
                                    <td class="remarkCell">
                                        请输入正确的身份信息
                                    </td>

                                </tr>
                                 <tr class="title1">
                                    <td colspan="3">
                                        设置密码
                                    </td>
                                </tr>
                                <tr class='page_form_tr'>
                                    <td class="page_form_title_td" nowrap>密&nbsp;&nbsp;码：</td>
                                    <td class="page_form_td" nowrap><input type="password" <%=readonly%>
                                                                           name="PASSWORD" id="PASSWORD"
                                                                           value="<%=PASSWORD==null?"":PASSWORD%>"
                                                                           class="page_form_text"
                                                                           onKeyUp=pwStrength(this.value) onBlur=pwStrength(this.value)
                                                                           onkeydown='if(event.keyCode==13) event.keyCode=9' maxlength="20">

                                    </td>
                                    <td class="remarkCell">
                                        为了账户安全，请您尽量使用英文字母、数字和字符的组合
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
                                                                           class="page_form_text"
                                                                           onkeydown='if(event.keyCode==13) document.getElementById("saveadd").focus();' maxlength="20">
                                    </td>
                                    <td class="remarkCell">
                                        请再输入一遍您设置的密码
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
                <tr height="10px">
                    <td style="border-bottom:#3366FF 1px solid;">&nbsp;</td>
                </tr>
                <tr height="30px" align="center" valign="top">
                    <td align="center">
                        <table border="0" cellspacing="0" cellpadding="0" width="538">
                            <tr class='page_form_tr'>
                                <td nowrap align="center">
                                    <table bgcolor="#ffffff">
                                        <tr class='page_button_tbl_tr'>
                                            <td class='page_button_tbl_td'><input style="width:90px;" type='button' <%=submit%> id='goback'
                                                                                  name='goback'
                                                                                  value=' 返 回 '
                                                                                  onClick="history.go(-1)"></td>
                                            <td class='page_button_tbl_td'><input style="width:90px;" type='button' <%=submit%> id='saveadd'
                                                                                  name='save'
                                                                                  value=' 下一步 '
                                                                                  onClick="return Regvalid();"></td>
                                            <%--<td class='page_button_tbl_td'><input type='button'--%>
                                            <%--class='page_button_active'--%>
                                            <%--name='button'--%>
                                            <%--value=' 关 闭 '--%>
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
</div>
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
    //

    function Regvalid() {
        if (goSave()) {
            document.forms[0].action = "/consume/appuser.jsp?goUrl=../app.jsp";
            document.forms[0].submit();
            //            var url = "./appuser.jsp?NAME=" + document.getElementsByName("NAME")[0].value + "&IDTYPE=" + document.getElementsByName("IDTYPE")[0].value + "&ID=" + document.getElementsByName("ID")[0].value + "&PASSWORD=" + document.getElementsByName("PASSWORD")[0].value + "&showinfo=0";
            //            window.open(url, 'APPUSER', 'left=0,top=0,height=700,width=870,toolbar=no,scrollbars=yes,resizable=yes');
        }
    }
</script>
