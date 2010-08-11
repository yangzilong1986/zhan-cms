<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.cmsi.pub.define.XFGradeMark" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DecimalFormat" %>
<%--
===============================================
Title: 个人消费分期付款信用评分表
Description: 个人消费分期付款信用评分表。
 * @version  $Revision: 1.0 $  $Date: 2009/03/20 08:20:31 $
 * @author liujian
 * <p/>修改：$Author: liuj $
===============================================
--%>
<%
    request.setCharacterEncoding("GBK");
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

    String flag = request.getAttribute("flag").toString();  //读写标志
    String APPNO = request.getParameter("APPNO");           //申请单编号
    String NAME = request.getParameter("NAME");             //客户名称 desc=企业(个人)名称
    String IDTYPE = request.getParameter("IDTYPE");         //证件名称
    String ID = request.getParameter("ID");                 //证件号码
    String APPTYPE = request.getParameter("APPTYPE");       //申请类型
    String APPSTATUS = request.getParameter("APPSTATUS");   //申请状态
    String APPDATE = request.getParameter("APPDATE");       //申请日期
    String AGE = "";                                        //年龄
    String BURDENSTATUS = "";                               //负担状况
    String CCRPRATE = "";                                   //债务收入比
    String SORT1 = "";                  //评分类型1
    String SORT2 = "";                  //评分类型2
    String SORT3 = "";                  //评分类型3
    String SORT4 = "";                  //评分类型4
    String SORT5 = "";                  //评分类型5
    String SORT6 = "";                  //评分类型6
    String SORT7 = "";                  //评分类型7
    String SORT8 = "";                  //评分类型8
    String SORT9 = "";                  //评分类型9
    String SORT10 = "";                 //评分类型10

    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
        session.setAttribute("msg", "长时间未操作，系统登陆超时，请重新登陆！");
        response.sendRedirect("../showinfo.jsp");
    } else if (APPNO == null) {
        session.setAttribute("msg", "没有发现传送入的参数！");
        response.sendRedirect("../showinfo.jsp");
    } else {

        APPNO = (APPNO == null) ? "" : APPNO.trim();
        NAME = (NAME == null) ? "" : NAME.trim();
        APPSTATUS = (APPSTATUS == null) ? "" : APPSTATUS;


        ConnectionManager manager = ConnectionManager.getInstance();
        String sql1 = "SELECT a.GENDER SORT1,a.MARRIAGESTATUS SORT2,a.EDULEVEL SORT3, SORT4, SORT5, SORT6, SORT7, SORT8, SORT9, SORT10," +
                "a.APPDATE,a.APPTYPE,a.APPSTATUS,a.NAME,a.IDTYPE,a.ID,a.AGE,a.BURDENSTATUS,a.CCRPRATE FROM XFAPPGRADE g " +
                "right outer join XFGRADE a on a.APPNO=g.APPNO " +
                "where a.APPNO='" + APPNO + "'";

        CachedRowSet crs = null;
        try {
            crs = manager.getRs(sql1);

            if (crs != null && crs.next()) {
                NAME = crs.getString("NAME");                          //客户名称 desc=企业(个人)名称
                ID = crs.getString("ID");                              //证件号码
                IDTYPE = crs.getString("IDTYPE");                     //证件名称
                APPDATE = crs.getString("APPDATE");                   //申请日期
                APPTYPE = crs.getString("APPTYPE");                   //申请类型
                APPSTATUS = crs.getString("APPSTATUS").trim();        //申请状态
                AGE = crs.getString("AGE");                            //年龄
                BURDENSTATUS = crs.getString("BURDENSTATUS");         //负担状况
                CCRPRATE = crs.getString("CCRPRATE");                 //债务收入比
                SORT1 = crs.getString("SORT1");
                SORT2 = crs.getString("SORT2");
                SORT3 = crs.getString("SORT3");
                SORT4 = crs.getString("SORT4");
                SORT5 = crs.getString("SORT5");
                SORT6 = crs.getString("SORT6");
                SORT7 = crs.getString("SORT7");
                SORT8 = crs.getString("SORT8");
                SORT9 = crs.getString("SORT9");
                SORT10 = crs.getString("SORT10");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String readonly = "";
        String submit = "class='page_button_active'";
        String title = "个人消费分期付款申请书";

        if (flag.equals("read")) {
            readonly = "disabled";
            submit = "class='page_button_disabled'";
        }
%>
<html>
<head>
    <title>消费信贷</title>
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
                    <td height="30" bgcolor="#A4AEB5">
                        <img src="../images/form/formtile1.gif" height="22px" width="22px" align="absmiddle">
                        <font size="2" color="#FFFFFF"><b>个人消费分期付款信用评分表</b></font>
                    </td>
                </tr>
                <tr align="center">
                    <td height="100" valign="middle">
                        <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                            <tr class='page_form_tr'>
                                <td width="20">&nbsp;</td>
                                <td align="center" valign="middle">
                                    <script src='../js/main.js' type='text/javascript'></script>
                                    <script src='../js/check.js' type='text/javascript'></script>
                                    <script src='../js/pagebutton.js' type='text/javascript'></script>
                                    <form id='winform' method='post' action='./appgrade_save.jsp'>
                                        <table class='page_form_table' id='page_form_table' width="100%">
                                            <col width="120"/>
                                            <col width="610"/>
                                            <tr class='page_form_tr'>
                                                <td colspan="2" align="center" class="page_form_List_title">
                                                    个人消费分期付款信用评分表
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td><input type="hidden" name="APPNO" value="<%=APPNO%>">
                                                    <input type="hidden" name="AGE" value="<%=AGE%>">
                                                    <input type="hidden" name="BURDENSTATUS" value="<%=BURDENSTATUS%>">
                                                    <input type="hidden" name="CCRPRATE" value="<%=CCRPRATE%>">
                                                    <input type="hidden" name="SORT1" value="<%=SORT1%>">
                                                    <input type="hidden" name="SORT2" value="<%=SORT2%>">
                                                    <input type="hidden" name="SORT3" value="<%=SORT3%>">
                                                    申请日期：<%=APPDATE == null ? "" : APPDATE%>
                                                </td>
                                                <td align="right">
                                                    申请状态：<%=(APPSTATUS == null || APPSTATUS.equals("")) ? "" : level.getEnumItemName("AppStatus", APPSTATUS)%>
                                                    &nbsp;&nbsp;编&nbsp;&nbsp;号：<%=APPNO%>
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_button_tbl_tr" colspan="3" height="5"></td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_left_table_title">债务收入比职业类别</td>
                                                <td class="page_form_td"
                                                    nowrap><%=level.radioHereExt("SORT4", "DeptEmpTp", SORT4, "<br>")%>
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_button_tbl_tr" colspan="3" height="5"></td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_left_table_title">所在行业</td>
                                                <td class="page_form_td"
                                                    nowrap><%=level.radioHereExt("SORT5", "SectorLev", SORT5, "<br>")%>
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_button_tbl_tr" colspan="3" height="5"></td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_left_table_title">从业性质</td>
                                                <td class="page_form_td"
                                                    nowrap><%=level.radioHereExt("SORT6", "EmpKind", SORT6, "<br>")%>
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_button_tbl_tr" colspan="3" height="5"></td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_left_table_title">职位</td>
                                                <td class="page_form_td"
                                                    nowrap><%=level.radioHereExt("SORT7", "Position", SORT7, "<br>")%>
                                                </td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_button_tbl_tr" colspan="3" height="5"></td>
                                            </tr>
                                            <tr class='page_form_tr'>
                                                <td class="page_left_table_title">工作年限</td>
                                                <td class="page_form_td"
                                                    nowrap><%=level.radioHereExt("SORT8", "WorkYear", SORT8, "<br>")%>
                                                </td>
                                            </tr>
                                        </table>
                                        <script src='../js/pagebutton.js' type='text/javascript'></script>
                                        <input type='hidden' name='Plat_Form_Request_Instance_ID' value='2'>
                                        <input type='hidden' name='Plat_Form_Request_Event_ID' value=''>
                                        <input type='hidden' name='Plat_Form_Request_Event_Value' value='12'>
                                        <input type='hidden' name='Plat_Form_Request_Button_Event' value=''>
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
                                            <%--<%if (!APPSTATUS.equals("") && APPSTATUS.equals("4")) {%>--%>
                                            <td class='page_button_tbl_td'><input
                                                    type='button' <%=submit%> <%=readonly%> id='saveadd'
                                                    name='save'
                                                    value=' 提交 '
                                                    onClick="return Regvalid();"></td>
                                            <%--<%--%>
                                                <%--}--%>
                                            <%--%>--%>
                                            <td class='page_button_tbl_td'><input type='button'
                                                                                  class='page_button_active'
                                                                                  name='button'
                                                                                  value=' 关 闭 '
                                                                                  onClick="pageWinClose();"></td>
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
<script language="javascript" type="text/javascript">
    document.title = "<%=title%>";
    document.focus();

    function goSave() {
        if (!isEmptyItem("SORT4")) return false;
        if (!isEmptyItem("SORT5")) return false;
        if (!isEmptyItem("SORT6")) return false;
        if (!isEmptyItem("SORT7")) return false;
        if (!isEmptyItem("SORT8")) return false;
        return true;
    }

    function Regvalid() {
        if (goSave()) {
            document.forms[0].action = "/consume/appgrade_save.jsp";
            document.forms[0].submit();
        }
    }
</script>
<%}%>
