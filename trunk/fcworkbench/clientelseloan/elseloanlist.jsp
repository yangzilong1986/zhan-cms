<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Vector" %>
<%--
===============================================
Title: �ͻ��������������ѯ
Description:  �ͻ��������������б�
 * @version   $Revision: 1.8 $  $Date: 2007/06/21 00:55:37 $
 * @author   zhengxin
 * <p/>�޸ģ�$Author: liuj $
===============================================
--%>
<%

    request.setCharacterEncoding("GBK");
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
        response.sendRedirect("../error.jsp");
    }

    String IDNO = request.getParameter("IDNO");             //���֤����

    String sql = "select BRHID,BMNO,BAL,CREATEDATE,CLIENTNAME,FCNO,FCAUTO,FCTYPE,FCCLASS,FCSTATUS " +
            "from fcmain where  IDNO='" + IDNO + "'  and createdate=(select max(Dt) from fcprd where initialized=1)";

    ConnectionManager manager = ConnectionManager.getInstance();
    String pnStr = request.getParameter("pn");
    if (pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
    int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
    int ps = 10;
    Vector vec = manager.getPageRs(sql, pn, ps);
    int rows = ((Integer) vec.get(0)).intValue();
    CachedRowSet crs = (CachedRowSet) vec.get(1);
    int count = crs.size();
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

    String FCSTATUS = "";  //����״̬
    String FCNO = "";      //���ҵ���
    String CREATEDATE = "";//�������
    String BRHID = "";     //ά������
    BigDecimal BAL;        //Ʊ����
    String BMNO = "";      //ҵ���
    String FCAUTO = "";    //�Զ���ֽ��
    String FCCLASS = "";   //��ֽ��

    String name = "";
    if (!crs.wasNull()) {
        crs.next();
        name = DBUtil.fromDB(crs.getString("CLIENTNAME"));
        crs.beforeFirst();
    }


    String mess = (String) request.getAttribute("mess");
    if (mess == null) {
        mess = "";
    } else {
        mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
    }
%>
<html>
<head>
    <title>�Ŵ�����</title>
    <link href="/css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
    <script language="JavaScript" type="text/JavaScript">
        function info(FCNO, BMNO, FCSTATUS) {
            var url = "../history_qf/history_qf_info.jsp?FCNO=" + FCNO + "&BMNO=" + BMNO + "&FCSTATUS=" + FCSTATUS + "&showinfo=0";
            window.open(url, '123', 'height=700,width=800,toolbar=no,scrollbars=yes,resizable=yes');
        }
    </script>
    <script language="javascript" type="text/JavaScript" src="/js/flippage.js"></script>
</head>
<body background="/images/checks_02.jpg">
<form action="elseloanlist.jsp" name="form1" method="post">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699"
       bgcolor="AACCEE">
<tr align="left">
    <td height="30" bgcolor="#4477AA"><img src="/images/form/xing1.JPG" align="absmiddle"> <font size="2"
                                                                                                 color="#FFFFFF"><b><%=name %>
        :�ͻ���������������Ϣ</b></font> <img src="/images/form/xing1.JPG" align="absmiddle"></td>
</tr>
<tr align="center">
<td height="260" valign="middle">
    <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
        <input type="hidden" name="IDNO" value="<%=IDNO%>">
        <tr>
            <td width="20">&nbsp;</td>
            <td align="center" valign="middle">
                <script src='/js/querybutton.js' type='text/javascript'></script>
                <script src='/js/meizzDate.js' type='text/javascript'></script>
                <table>
                    <tr>
                        <td>
                            <table cellpadding='0' cellspacing='0' border='0'>
                                <tr>
                                    <td height='5' class='list_form_title_td'><font
                                            size="2">�ͻ����ƣ� <%=name%>
                                    </font></td>
                                </tr>
                            </table>
                            <table class='list_form_table' width='530' align='center' cellpadding='0'
                                   cellspacing='1' border='0'>
                                <tr class='list_form_title_tr'>

                                    <td width='12%' class='list_form_title_td' nowrap align="center">����</td>
                                    <td width='13%' class='list_form_title_td' nowrap align="center">ҵ���
                                    </td>
                                    <td width='15%' class='list_form_title_td' nowrap align="center">��Ƿ���
                                    </td>
                                    <td width='11%' class='list_form_title_td' nowrap align="center">�������
                                    </td>
                                    <td width='15%' class='list_form_title_td' nowrap align="center">�Զ���ֽ��
                                    </td>
                                    <td width='15%' class='list_form_title_td' nowrap align="center">�ϴ��϶����
                                    </td>
                                    <td width='12%' class='list_form_title_td' nowrap align="center">����״̬
                                    </td>
                                    <td width='7%' class='list_form_title_td' nowrap align="center">��ϸ</td>
                                </tr>
                                <%
                                    while (crs.next()) {
                                        FCSTATUS = crs.getString("FCSTATUS");    //����״̬
                                        FCNO = crs.getString("FCNO");            //���ҵ���
                                        CREATEDATE = crs.getString("CREATEDATE");//�������
                                        BRHID = crs.getString("BRHID");          //ά������
                                        BAL = crs.getBigDecimal("BAL");          //Ʊ����
                                        BMNO = crs.getString("BMNO");            //ҵ���
                                        FCAUTO = crs.getString("FCAUTO");        //�Զ���ֽ��
                                        FCCLASS = crs.getString("FCCLASS");      //��ֽ��
                                %>
                                <tr class='list_form_tr'>
                                    <td nowrap class='list_form_td' align="center"><%=BRHID%>
                                    </td>
                                    <td nowrap class='list_form_td' align="center"><%=BMNO%>
                                    </td>
                                    <td nowrap class='list_form_td' align="center"><%=df.format(BAL)%>
                                    </td>
                                    <td nowrap class='list_form_td' align="center"><%=CREATEDATE%>
                                    </td>
                                    <td nowrap class='list_form_td' align="center">
                                        <%=FCAUTO == null ? "" : level.getEnumItemName("LoanCat1", FCAUTO)%>
                                    </td>
                                    <td nowrap class='list_form_td' align="center">
                                        <%=FCCLASS == null ? "" : level.getEnumItemName("LoanCat1", FCCLASS)%>
                                    </td>
                                    <td nowrap class='list_form_td' align="center">
                                        <%=FCSTATUS == null ? "" : level.getEnumItemName("FCStatus", FCSTATUS)%>
                                    </td>
                                    <td nowrap class='list_form_td' align="center">
                                        <a class="list_edit_href"
                                           href="javascript:info('<%=FCNO.trim()%>','<%=BMNO.trim()%>','<%=FCSTATUS.trim()%>')">��ϸ</a>
                                    </td>
                                </tr>
                                <%} %>
                                <%
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
                                    <td nowrap class='list_form_td' align="left"></td>
                                    <td nowrap class='list_form_td' align="center"></td>
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
            <tr>
                <td nowrap align="left">

                    <table class='list_button_tbl'>
                        <tr class="list_button_tbl_tr">

                            <td class="list_form_button_td"><input type='submit' name='a'
                                                                   class='list_button_active' value=' ˢ�� '
                                                                   onclick="return req();"></td>
                            <td class="list_form_button_td"><input type='submit' name='a'
                                                                   class='list_button_active' value=' �ر� '
                                                                   onclick="window.close();"></td>
                            <script language="javascript" type="text/javascript">
                                createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "elseloanlist.jsp?pn=", "form1");
                            </script>
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
</form>
</body>
</html>
<script language="javascript" type="text/javascript">
    function req() {
        document.location.replace("elseloanlist.jsp");
    }
</script>

