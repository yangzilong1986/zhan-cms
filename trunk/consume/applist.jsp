<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Vector" %>
<%--
===============================================
Title: 消费信贷-个人消费分期付款申请列表
Description:  消费信贷-个人消费分期付款申请列表。
 * @version   $Revision: 1.0 $  $Date: 2009/03/12 00:55:37 $
 * @author   liujian
 * <p/>修改：$Author: liuj $
===============================================
--%>
<%

    request.setCharacterEncoding("GBK");
//    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (um == null) {
//        response.sendRedirect("../error.jsp");
//    }

    String IDTYPE = request.getParameter("IDTYPE");            //证件类型
    String ID = request.getParameter("ID");                     //证件号码
    String PASSWORD = request.getParameter("PASSWORD");        //密码

    String NAME = "";       //姓名
    String APPNO = "";      //申请单号
    String APPDATE = "";    //申请日期
    String APPTYPE = "";    //申请类型
    String COMMNAME = "";   //商品名称
    String APPSTATUS = "";  //申请状态

    IDTYPE = (IDTYPE == null) ? "" : IDTYPE;
    ID = (ID == null) ? "" : ID;
    NAME = (NAME == null) ? "" : NAME.trim();
    PASSWORD = (PASSWORD == null) ? "" : PASSWORD.trim();

    ConnectionManager manager = ConnectionManager.getInstance();
    String sql = "SELECT * from cmindvclient WHERE IDTYPE='" + IDTYPE + "' AND ID='" + ID + "' AND PASSWORD='" + PASSWORD + "'";
    if (manager.getRs(sql).size() == 0) {
        //根据证件确定最近登记的客户资料
        sql = " select CLIENTNO,NAME,APPDATE from CMINDVCLIENT where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and rownum=1  order by CLIENTNO desc";
        String err = "";
        if (manager.getRs(sql).size() > 0) err = "您输入的密码有误！";
        else err = "您还没有注册，请先注册！";
        request.setAttribute("err", err);
%>
<jsp:forward page="./apperror.jsp"/>
<%
} else {

    sql = "SELECT NAME, IDTYPE, ID, A.APPNO, APPSTATUS, APPTYPE, APPDATE, COMMNAME " +
            " from XFAPP A,XFAPPCOMM C WHERE A.APPNO=C.APPNO AND A.IDTYPE='" + IDTYPE + "' AND A.ID='" + ID + "' order by A.APPNO desc";
    String pnStr = request.getParameter("pn");
    if (pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
    int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
    int ps = 10;
    Vector vec = manager.getPageRs(sql, pn, ps);
    int rows = ((Integer) vec.get(0)).intValue();
    CachedRowSet crs = (CachedRowSet) vec.get(1);
    int count = crs.size();
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

    String SID = (String) session.getAttribute("SID");//合作商城编号
    SID = (SID == null) ? "" : SID;
    if (count == 0 || !SID.equals("")) {
%>
<%--<jsp:forward page="application_start.jsp?goUrl=../app.jsp"/>--%>
<jsp:forward page="application_preshow.jsp?goUrl=../app.jsp"/>
<%
} else {
    if (NAME.equals("") && crs.next()) {
        NAME = crs.getString("NAME");
        crs.beforeFirst();
    }
%>

<html>
<head>
    <title>消费分期申请</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
    <script language="JavaScript" type="text/JavaScript">
        function info(APPNO, NAME, IDTYPE, ID, PASSWORD, APPSTATUS) {
            //haiyu 2010-08-09
            var   url = "./application_start.jsp?NAME=" + NAME + "&IDTYPE=" + IDTYPE + "&ID=" + ID + "&PASSWORD=" + PASSWORD;
//            var url = "./application_preshow.jsp?APPNO=" + APPNO + "&NAME=" + NAME + "&IDTYPE=" + IDTYPE + "&ID=" + ID + "&PASSWORD=" + PASSWORD + "&APPSTATUS=" + APPSTATUS + "&showinfo=0";
            window.open(url, 'APPLICATION', 'left=0,top=0,height=700,width=870,toolbar=no,scrollbars=yes,resizable=yes');
        }
        function info_xiangxi(APPNO, NAME, IDTYPE, ID, PASSWORD, APPSTATUS) {
            var url = "./application_start.jsp?APPNO=" + APPNO + "&NAME=" + NAME + "&IDTYPE=" + IDTYPE + "&ID=" + ID + "&PASSWORD=" + PASSWORD + "&APPSTATUS=" + APPSTATUS + "&showinfo=0";
            window.open(url, 'APPLICATION', 'left=0,top=0,height=700,width=870,toolbar=no,scrollbars=yes,resizable=yes');
        }
        function info1(NAME, IDTYPE, ID, PASSWORD) {
            var url = "./appuser.jsp?NAME=" + NAME + "&IDTYPE=" + IDTYPE + "&ID=" + ID + "&PASSWORD=" + PASSWORD + "&showinfo=0";
            window.open(url, 'APPUSER', 'left=0,top=0,height=700,width=870,toolbar=no,scrollbars=yes,resizable=yes');
        }
    </script>
    <script language="javascript" type="text/JavaScript" src="/js/flippage.js"></script>
</head>
<body background="/images/checks_02.jpg">
<br>
<br>
<form action="applist.jsp" name="form1" method="post">
    <input type="hidden" name="IDTYPE" value="<%=IDTYPE%>">
    <input type="hidden" name="ID" value="<%=ID%>">
    <input type="hidden" name="NAME" value="<%=NAME.trim()%>">
    <input type="hidden" name="PASSWORD" value="<%=PASSWORD%>">
    <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
        <tr class='page_form_tr'>
            <td align="center" valign="middle">
                <table height="325" width="860" border="2" align="center" cellpadding="2" cellspacing="2"
                       bordercolor="#816A82"
                       bgcolor="#E0E0D3">
                    <tr align="left">
                        <td height="30" bgcolor="#A4AEB5"><img src="../images/form/xing1.jpg" align="absmiddle"> <font
                                size="2"
                                color="#FFFFFF"><b>客户申请信息</b></font> <img src="../images/form/xing1.jpg"
                                                                          align="absmiddle"></td>
                    </tr>
                    <tr align="center" class='page_form_tr'>
                        <td height="260" valign="middle">
                            <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                                <tr class='page_form_tr'>
                                    <td width="20">&nbsp;</td>
                                    <td align="center" valign="middle">
                                        <script src='/js/querybutton.js' type='text/javascript'></script>
                                        <script src='/js/meizzDate.js' type='text/javascript'></script>
                                        <table class='page_form_table' id='page_form_table' width="100%">
                                            <tr class='page_form_tr'>
                                                <td height='5' class='list_form_title_td'><font
                                                        size="2">客户名称： <%=NAME%>
                                                </font></td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td>
                                                    <table class='list_form_table' width="100%" align='center'
                                                           cellpadding='0'
                                                           cellspacing='1' border='0'>
                                                        <tr class='list_form_title_tr'>
                                                            <td width='4%' class='list_form_title_td' nowrap
                                                                align="center">序号
                                                            </td>
                                                            <td width='18%' class='list_form_title_td' nowrap
                                                                align="center">申请单号
                                                            </td>
                                                            <td width='40%' class='list_form_title_td' nowrap
                                                                align="center">申请商品
                                                            </td>
                                                            <td width='8%' class='list_form_title_td' nowrap
                                                                align="center">申请类型
                                                            </td>
                                                            <td width='17%' class='list_form_title_td' nowrap
                                                                align="center">申请日期
                                                            </td>
                                                            <td width='8%' class='list_form_title_td' nowrap
                                                                align="center">状态
                                                            </td>
                                                            <td width='5%' class='list_form_title_td' nowrap
                                                                align="center">详细
                                                            </td>
                                                        </tr>
                                                        <%
                                                            int SEQNO = 0;
                                                            while (crs.next()) {
                                                                APPSTATUS = crs.getString("APPSTATUS");         //申请状态
                                                                APPNO = crs.getString("APPNO");                 //申请单号
                                                                APPDATE = crs.getString("APPDATE");             //申请日期
                                                                APPTYPE = crs.getString("APPTYPE");             //申请类型
                                                                COMMNAME = crs.getString("COMMNAME");           //商品名称
                                                                ++SEQNO;
                                                        %>
                                                        <tr class='list_form_tr'>
                                                            <td nowrap class='list_form_td'
                                                                align="center"><%=SEQNO + (pn - 1) * ps%>
                                                            </td>
                                                            <td nowrap class='list_form_td' align="center"><%=APPNO%>
                                                            </td>
                                                            <td class='list_form_td' align="left">
                                                                &nbsp;<%=COMMNAME%>
                                                            </td>
                                                            <td nowrap class='list_form_td' align="center">
                                                                <%=APPTYPE == null ? "" : level.getEnumItemName("AppType", APPTYPE)%>
                                                            </td>
                                                            <td nowrap class='list_form_td' align="center"><%=APPDATE%>
                                                            </td>
                                                            <td nowrap class='list_form_td' align="center">
                                                                <%=APPSTATUS == null ? "" : level.getEnumItemName("AppStatus", APPSTATUS)%>
                                                            </td>
                                                            <td nowrap class='list_form_td' align="center">
                                                                <a class="list_edit_href"
                                                                   href="javascript:info_xiangxi('<%=APPNO%>','<%=NAME%>','<%=IDTYPE%>','<%=ID%>','<%=PASSWORD.trim()%>','<%=APPSTATUS%>')">详细</a>
                                                            </td>
                                                        </tr>
                                                        <%
                                                            }
                                                            if (count < ps) {
                                                                for (int i = 0; i < ps - count; i++) {
                                                        %>
                                                        <tr class='list_form_tr'>
                                                            <td nowrap class='list_form_td' align="center"></td>
                                                            <td nowrap class='list_form_td' align="left"></td>
                                                            <td nowrap class='list_form_td' align="left"></td>
                                                            <td nowrap class='list_form_td' align="left"></td>
                                                            <td nowrap class='list_form_td' align="left"></td>
                                                            <td nowrap class='list_form_td' align="left"></td>
                                                            <td nowrap class='list_form_td' align="left"></td>
                                                        </tr>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </table>
                                                </td>
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
                                <tr class='page_form_tr'>
                                    <td nowrap align="left">

                                        <table class='list_button_tbl'>
                                            <tr class="list_button_tbl_tr">
                                                <td class="list_form_button_td"><input type='button' name='a'
                                                                                       class='list_button_active'
                                                                                       value='新申请'
                                                                                       onclick="return req();"></td>
                                                <td class="list_form_button_td"><input type='button' name='a'
                                                                                       class='list_button_active'
                                                                                       value='修改个人信息'
                                                                                       onclick="return req1();"></td>
                                                <td class="list_form_button_td"><input type='button' name='a'
                                                                                       class='list_button_active'
                                                                                       value='修改密码'
                                                                                       onclick="return req2();"></td>
                                                <script language="javascript" type="text/javascript">
                                                    createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "applist.jsp?pn=", "form1");
                                                </script>
                                                <td class="list_form_button_td"><input type='button' name='submit5'
                                                                                       class='list_button_active'
                                                                                       value=' 刷新 ' onClick="submit();">
                                                </td>
                                                <td class="list_form_button_td"><input type='submit' name='a'
                                                                                       class='list_button_active'
                                                                                       value=' 关闭 '
                                                                                       onclick="window.close();"></td>
                                                <td class="list_form_button_td"><input type='button' name='a'
                                                                                       class='list_button_active'
                                                                                       value=' 返回 '
                                                                                       onclick="location='../app.jsp'">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr height="35" align="center" valign="middle">
                        <td align="center">
                            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                <tr class='list_form_tr'>
                                    <td class='list_form_td' nowrap>
                                        <br>

                                        <p align="left" style="font-size:10pt; color:#004080">&nbsp;&nbsp;&nbsp;&nbsp;【非常感谢您选择海尔财务公司的分期付款业务。下面是您分期申请需要准备的材料:】
                                            <br>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 身份证明复印件<strong>两份，并签字</strong>（第二代身份证请正反两面都复印）<br>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 员工卡复印件一份 （内部员工）<br>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 消费分期付款申请书<strong>一份，签字</strong><br>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 信用记录查询许可书<strong>一份，签字</strong><br>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 建行还款账号<strong>签字</strong>复印件、支付宝或快钱账号截图的签字复印件<br><br>
                                            &nbsp;&nbsp;&nbsp;&nbsp;【如有共同还款人参与，请另附：】<br>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、共同还款人的身份证件复印件<strong>两份，并签字</strong>（第二代身份证请正反两面都复印）<br>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、共同还款人的信用记录查询许可书<strong>一份，签字 </strong><br><br>
                                        </p></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td valign="bottom">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center"
                       style="height: 65px; border:none; font-weight:normal; font-size: 10px; color:#8F87E0;font-family:'微软雅黑';">
                    <tr>
                        <td width="30%">&nbsp;</td>
                        <td width="40%">
                            <table border="0" cellspacing="0" cellpadding="0" width="330" align="center"
                                   style="height: 20px; border:none; font-weight:normal; font-size: 12px; color:#004080;font-family:'微软雅黑';">
                                <tr>
                                    <td width="30%">分期咨询电话：<span style="color:#FF0066">0532-88939384，88939383</span>&nbsp;&nbsp;邮编：266101<br>
                                        地址：山东省青岛市海尔路1号海尔工业园K座金融中心405室
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="30%" valign="bottom" align="right">最终解释权归海尔集团财务有限责任公司所有&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</form>
<%--在线客服代码 365call-- 列表方式--%>
<script type='text/javascript' src='http://chat2.365webcall.com/IMMe1.aspx?settings=mw7mNmXNNm6X7Xbz3Am600bPz3Am6wIbNz3AN6mm00&LL=0'></script>

</body>
</html>
<script language="javascript" type="text/javascript">
    function req() {
        //alert(document.location);
        //document.location.replace("applist.jsp");
        info('', '<%=NAME%>', '<%=IDTYPE.trim()%>', '<%=ID.trim()%>', '<%=PASSWORD.trim()%>', '');
    }

    function req1() {
        info1('<%=NAME%>', '<%=IDTYPE.trim()%>', '<%=ID.trim()%>', '<%=PASSWORD.trim()%>');
    }

    function req2() {
        document.location.replace("./apppass.jsp?NAME=<%=NAME%>&IDTYPE=<%=IDTYPE.trim()%>&ID=<%=ID.trim()%>");
    }
</script>
<%
        }
    }
%>
