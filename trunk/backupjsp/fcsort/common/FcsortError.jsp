
<%@ page contentType="text/html; charset=gb2312" %>
<%
    //rooturl
    String rooturl = "http://" + request.getServerName();
    int svrport = request.getServerPort();
    if (svrport != 80) {
        rooturl += ":" + svrport;
    }
    rooturl += request.getContextPath();
%>
  <%--
=============================================== 
Title:五级分类错误信息处理页面
 * @version  $Revision: 1.3 $  $Date: 2007/05/23 06:56:27 $
 * @author   zhengxin
 * <p/>修改：$Author: zhengx $   
=============================================== 
--%>
<html>
<head>
    <title>确认信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <link href="<%=rooturl%>/css/platform.css" rel="stylesheet" type="text/css">
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
                        &nbsp;提示信息！</font></b></td>
                </tr>
            </table>
            <table width="100%" height="200" class='page_form_table'>
                <tr class='page_form_tr'>
                    <td class='page_form_td'>&nbsp;
              		 系统出现异常，请重新登录本系统！或联系系统管理员！
                    </td>
                </tr>
            </table>
            <table class='page_button_tbl' align="center">
                <tr class='page_button_tbl_tr'>
                    <td class='page_button_tbl_td' align="center" valign="middle" height="30">
                    		
                    
                        <input class="list_button_active" type="button" name="ok" value=" 上一步 "
                               onClick="history.go(-1)">
                     
                        <input class="list_button_active" type="button" name="close" value=" 关 闭 "
                               onClick="window.close();">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>