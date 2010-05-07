<%@ page import="zt.cms.scuser.Role" %>
<%@ page import="zt.cms.scuser.RoleFacotory" %>
<%@ page import="zt.cmsi.pub.define.UserRoleMan" %>
<%@ page import="zt.platform.db.ConnectionManager" %>
<%@ page import="zt.platform.db.DatabaseConnection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%--
*******************************************************************
*    程序名称:    showinfo.jsp
*    程序标识:
*    功能描述:    显示信息页面
*    连接网页:
*    传递参数:
*    作   者:     wxj
*    开发日期:    2004-02-23
*    修 改 人:    
*    修改日期:    
*    版    权:    青岛中天信息技术有限公司
*******************************************************************
--%>
<%@ page contentType="text/html; charset=gb2312" %>
<%
    request.setCharacterEncoding("GBK");

    //rooturl
    String rooturl = "http://" + request.getServerName();
    int svrport = request.getServerPort();
    if (svrport != 80) {
        rooturl += ":" + svrport;
    }
    rooturl += request.getContextPath();

//request
    String strTitle = (String) request.getAttribute("title");
    String strMsg = (String) request.getAttribute("msg");
    if (strMsg == null) {
        strMsg = (String) session.getAttribute("msg");
        if (strMsg != null && !strMsg.equals("")) {
            session.removeAttribute("msg");
        }
    }
//1:sucess 0:failed
    String strFlag = (String) request.getAttribute("flag");
    if (strFlag == null) {
        strFlag = (String) session.getAttribute("flag");
        if (strFlag != null && !strFlag.equals("")) {
            session.removeAttribute("flag");
        }
    }
//1:back button show 0:back button doesn't show
    String strIsBack = (String) request.getAttribute("isback");
    if (strIsBack == null) {
        strIsBack = (String) session.getAttribute("isback");
        if (strIsBack != null && !strIsBack.equals("")) {
            session.removeAttribute("isback");
        }
    }

    String strGoUrl = (String) request.getAttribute("goUrl");
    if (strGoUrl == null) {
        strGoUrl = (String) session.getAttribute("goUrl");
        if (strGoUrl != null && !strGoUrl.equals("")) {
            session.removeAttribute("goUrl");
        }
    }

    String strRloadOperner = (String) request.getAttribute("reloadOperner");
    if (strRloadOperner == null) {
        strRloadOperner = (String) session.getAttribute("reloadOperner");
        if (strRloadOperner != null && !strRloadOperner.equals("")) {
            session.removeAttribute("reloadOperner");
        }
    }

    String strCloseself = (String) request.getAttribute("closeSelf");
    if (strCloseself == null) {
        strCloseself = (String) session.getAttribute("closeSelf");
        if (strCloseself != null && !strCloseself.equals("")) {
            session.removeAttribute("closeSelf");
        }
    }

    String strFuncDef = (String) request.getAttribute("funcdel");
    if (strFuncDef == null) {
        strFuncDef = (String) session.getAttribute("funcdel");
        if (strFuncDef != null && !strFuncDef.equals("")) {
            session.removeAttribute("funcdel");
        }
    }

    if (request.getParameter("msgkq") != null) strMsg = request.getParameter("msgkq");//快钱接口
    if (request.getParameter("flag") != null) strFlag = request.getParameter("flag");//快钱接口
    if (request.getParameter("isback") != null) strIsBack = request.getParameter("isback");//快钱接口
    if (request.getParameter("funcdel") != null) strFuncDef = request.getParameter("funcdel");//快钱接口

    strTitle = (strTitle != null ? strTitle.trim() : "确认信息");
    strMsg = (strMsg != null ? strMsg.trim() : "");
    strFlag = (strFlag != null ? strFlag.trim() : "1");
    strIsBack = (strIsBack != null ? strIsBack.trim() : null);
    strGoUrl = (strGoUrl != null ? strGoUrl.trim() : null);
    strRloadOperner = (strRloadOperner != null ? strRloadOperner.trim() : "");
    strCloseself = (strCloseself != null ? strCloseself.trim() : "");
    strFuncDef = (strFuncDef != null ? strFuncDef.trim() : "");

    String closeClick = "pageWinClose();";
    if (strGoUrl != null) closeClick = "gopage('" + strGoUrl + "')";
    else if (strRloadOperner.equals("1")) closeClick = "refreshOp()";
    else if (strCloseself.equals("1")) closeClick = "CloseWin()";
    else if (!strFuncDef.equals("")) closeClick = strFuncDef;
//    System.out.println("strTitle:" + strTitle + "\nstrMsg:" + strMsg + "\nstrFlag:" + strFlag + "\nstrIsBack:" + strIsBack);
%>
<html>
<head>
    <title>确认信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <link href="<%=rooturl%>/css/platform.css" rel="stylesheet" type="text/css">
    <script src='/js/pagebutton.js' type='text/javascript'></script>
    <style type="text/css">
        <!--
        body {
            margin-top: 50px;
        }

        -->
    </style>
</head>
<body background="<%=rooturl%>/images/checks_02.jpg">
<table width="400" height="300" border="" align="center" bordercolorlight="#336699" bordercolordark="#336699"
       bgcolor="#9DBBD9" onKeyDown="pressKeyDown(event.keyCode);">
    <tr>
        <td valign="top">
            <table width="100%">
                <tr height="70" bgcolor="#4477AA">
                    <td background="<%=rooturl%>/images/showinfo.jpg" valign="bottom"><b><font size="2">
                        &nbsp;[<%=strTitle%>]</font></b></td>
                </tr>
            </table>
            <table width="100%" height="200" class='page_form_table'>
                <tr class='page_form_tr'>
                    <td class='page_form_td'>&nbsp;
                        <%if (strFlag != null && strFlag.equals("0")) {%><font color="#FF0000"><%}%>
                            <%=strMsg%>
                            <%if (strFlag != null && strFlag.equals("0")) {%></font><%}%>
                    </td>
                </tr>
            </table>
            <table class='page_button_tbl' align="center">
                <tr class='page_button_tbl_tr'>
                    <td class='page_button_tbl_td' align="center" valign="middle" height="30">
                        <%
                            if (strIsBack == null || strIsBack.equals("1")) {
                        %>
                        <input class="list_button_active" type="button" name="ok" value=" 上一步 "
                               onClick="history.go(-1)">
                        <%
                            }
                        %>
                        <input class="list_button_active" type="button" name="close" value=" 确 定 "
                               onClick="<%=closeClick%>">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>