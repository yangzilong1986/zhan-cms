<%@ page contentType="text/html; charset=GBK" %>

<%--
===============================================
Title: 清分操作-确认
Description: 确认后返回并刷新调用的父窗口。
 * @version  $Revision: 1.5 $  $Date: 2007/05/16 13:46:17 $
 * @author
 * <p/>修改：$Author: liuj $
===============================================
--%>

<%
    String message = (String) session.getAttribute("mess");
    if (message != null || !message.equals("")) {
        session.removeAttribute("mess");
    }
%>

<html>
<head>
    <title>确认信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <style type="text/css">
        <!--
        body {
            margin-top: 200px;
        }

        -->
    </style>
</head>
<!--lj added body onunload() in 2007-04-30-->
<body background="../images/checks_02.jpg" onunload="return user_check();">
<table width="400" height="300" border="" align="center" bordercolorlight="#336699" bordercolordark="#336699"
       bgcolor="#9DBBD9" onKeyDown="pressKeyDown(event.keyCode);">
    <tr>
        <td height="260" valign="middle">
            <table width="100%">
                <tr height="70" bgcolor="#4477AA">
                    <td background="../images/showinfo.jpg" valign="bottom"><b><font size="2">&nbsp;[确认信息]</font></b>
                    </td>
                </tr>
            </table>
            <table width="100%" height="200" class='page_form_table'>
                <tr class='page_form_tr'>
                    <td class='page_form_td'>&nbsp;

                        <%=message%>

                    </td>
                </tr>
            </table>
            <table class='page_button_tbl' align="center">
                <tr class='page_button_tbl_tr'>
                    <td class='page_button_tbl_td' align="center" valign="middle" height="30">

                        <input class="list_button_active" type="button" name="ok" value=" 上一步 "
                               onClick="history.go(-1)">
                        <!--lj chenged onClick event user_check() to window.close() in 2007-04-30-->
                        <input type='submit' name='submit1' class='list_button_active' value=' 关 闭 '
                               onClick="window.close();">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
<script language="javascript" type="text/javascript">
    function user_check() {
        try {
            if (typeof(opener) != "undefined" && opener != null &&
                typeof(opener.document.all.pnstr) != "undefined" && opener.document.all.pnstr != null) {
                //lj chenged url 'opener.location.pathname+ "?fcoprtype=" + opener.document.all.fcoprtype.value' in 2007-04-26 for three workbenchs.
                opener.location = opener.location.pathname + "?fcoprtype=" + opener.document.all.fcoprtype.value +
                                  "&CREATEDATEGO=" + opener.document.all.CREATEDATEGO.value + "" +
                                  "&CREATEDATEER=" + opener.document.all.CREATEDATEER.value + "" +
                                  "&CLIENTNAME=" + opener.document.all.CLIENTNAME.value + "" +
                                  "&IDNO=" + opener.document.all.IDNO.value + "" +
                                  "&BMNO=" + opener.document.all.BMNO.value + "" +
                                  "&CNLNO=" + opener.document.all.CNLNO.value + "" +
                                  "&DUEDATEGO=" + opener.document.all.DUEDATEGO.value + "" +
                                  "&pn=" + opener.document.all.pnstr.value + "" +
                                  "&FCCrtType=" + opener.document.all.FCCrtType.value + "" +
                                  "&DUEDATEER=" + opener.document.all.DUEDATEER.value + "";
            }
        } catch(Error) {
            //alert("has error: opener.location=" + opener.location.pathname + opener.location.search);
            window.close();
        }
        //window.close(); //lj deleted in 2007-04-30 for use form onunload() event;
    }
</script>
