<%@ page contentType="text/html; charset=gb2312" language="java" import="zt.cms.scuser.Role" errorPage="" %>
<%@ page import="zt.cms.scuser.RoleFacotory" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%
    int userid = Integer.parseInt(request.getParameter("USERNO"));
    Collection roles = RoleFacotory.getAllRoles();
    Collection userRoles = RoleFacotory.getUserRoles(userid);
%>


<html>
<head>
    <title>信贷管理</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
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
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td align="center" valign="middle">
            <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699"
                   bgcolor="AACCEE">
                <tr align="left">
                    <td width="506" height="30" bgcolor="#4477AA">
                        <img src="../images/form/xing1.jpg" align="absmiddle">
                        <font size="2" color="#FFFFFF"><strong>用户角色管理</strong></font>
                        <img src="../images/form/xing1.jpg" align="absmiddle"></td>
                </tr>
                <tr align="center">
                    <td height="260" valign="middle">
                        <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                            <tr>
                                <td width="20">&nbsp;</td>
                                <td align="center" valign="middle">
                                    <script src='/js/check.js' type='text/javascript'></script>
                                    <script src='/js/meizzDate.js' type='text/javascript'></script>
                                    <script src='/js/template.js' type='text/javascript'></script>
                                    <form id='winform' method='post' action='/userrole/storeuserrole.jsp'>
                                        <table width="474" class='page_form_table' id='page_form_table'>
                                            <tr class='page_form_tr'>
                                                <td colspan="5" nowrap class="page_form_title_td">用户的角色</td>
                                            </tr>

                                            <%
                                                Iterator r = roles.iterator();
                                                while (r.hasNext()) {
                                                    Role role = (Role) r.next();
                                            %>
                                            <tr class='page_form_tr'>
                                                <td width="156" nowrap
                                                    class="page_form_title_td"><%=role.getDescription()%>
                                                </td>
                                                <td width="20" class="page_form_td"><input name="role" type="checkbox"
                                                                                           id="role"
                                                                                           value="<%=role.getRoleID()%>" <%=userRoles.contains(new Integer(role.getRoleID()))?"checked":""%>>
                                                </td>
                                                <td width="90" nowrap class="page_form_title_td"></td>
                                                <%
                                                    boolean hasNext = false;
                                                    if (r.hasNext()) {
                                                        role = (Role) r.next();
                                                        hasNext = true;
                                                    }
                                                %>
                                                <td width="162" nowrap
                                                    class="page_form_title_td"><%=hasNext ? role.getDescription() : "&nbsp;"%>
                                                </td>
                                                <td width="20" class="page_form_td">
                                                    <%if (hasNext) {%><input name="role" type="checkbox" id="role"
                                                                             value="<%=role.getRoleID()%>" <%=userRoles.contains(new Integer(role.getRoleID()))?"checked":""%>><%}%>
                                                </td>
                                            </tr>
                                            <%}%>

                                        </table>
                                        <input type='hidden' name='userid' value='<%=userid%>'></form>
                                </td>
                                <td width="20">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr height="35" align="center" valign="middle">
                    <td>
                        <table class='page_button_tbl'>
                            <tr class='page_button_tbl_tr'>
                                <td class='page_button_tbl_td'><input type='button' class='page_button_active'
                                                                      id='savebtn' name='save' value=' 返 回 '
                                                                      onClick='history.go(-1)'></td>
                                <td class='page_button_tbl_td'><input type='button' class='page_button_active'
                                                                      id='savebtn' name='save' value=' 提 交 '
                                                                      onClick='winform.submit()'></td>
                                <td class='page_button_tbl_td'><input type='button' class='page_button_active'
                                                                      name='button' value=' 关 闭 '
                                                                      onClick="parent.close();"></td>
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
<script language="javascript">
    document.title = "用户角色管理";
    document.focus();
</script>

