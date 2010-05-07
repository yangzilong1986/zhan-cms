<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Vector" %>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<head>
    <title>海尔财务公司分期申请</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <style type="text/css">
        <!--
        html, body {
            margin: 0px;
            height: 100%;
        }

        body {
            background: white;
        }

        .text_info {
            font-family: "宋体", "Courier New", Arial;
            font-size: 16px;
            text-shadow: #F9C;
            text-align: center;
            filter: Glow(Color = #CC99FF, Strength = 2);
            font-weight: bolder;
            color: #F30;
        }

        .border {
            border: thin dashed #FFC;
        }

        -->
    </style>
</head>
<%
    String err = request.getAttribute("err").toString();
    if (err.equals("null")) err = "";

    String IDTYPE = request.getParameter("IDTYPE");            //证件类型
    String ID = request.getParameter("ID");                     //证件号码
%>
<body>
<table width="100%" height="100%" border="0">
    <tr>
        <td valign="middle">
            <table width="400" border="0" align="center" valign="center" class="border">
                <tr>
                    <td rowspan="2" width="40%"><img src="../images/错误提示.jpg" width="140" height="131" alt="出错了"/></td>

                    <td height="120" colspan="2" class="text_info"><%=err%>&nbsp;</td>
                </tr>
                <tr>
                    <td width="26%" align="center" class=""><a href="#" class="link_gray" target="_self"
                                                               onClick="history.go(-1)">返&nbsp;&nbsp;&nbsp;回</a>
                    </td>
                    <td width="34%" align="center" class=""><a href="#" onClick="newUser();"
                                                               class="link_gray"
                                                               target="_self">新用户</a></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height="35%">&nbsp;</td>
    </tr>
</table>
<SCRIPT LANGUAGE="JavaScript">
        <!--
        function newUser() {
            document.location = "./appnewuser.jsp?IDTYPE=<%=IDTYPE%>&ID=<%=ID%>";
            //document.thisform.action = "./appnewuser.jsp";
            //document.thisform.submit();
        }
        //-->
    </SCRIPT>
</body>
</html>