
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.util.Vector" %>

<%--
=============================================== 
Title: 科目管理
Description: 显示多条信息。
 * @version   $Revision: 1.1 $  $Date: 2007/05/28 06:26:52 $
 * @author   
 * <p/>修改：$Author: houcs $
=============================================== 
--%>

<%
    request.setCharacterEncoding("GBK");
    ConnectionManager manager = ConnectionManager.getInstance();
//    String INITIALIZED = request.getParameter("INITIALIZED");
//    String DTGO = request.getParameter("DTGO");
//    String DTER = request.getParameter("DTER");

//    if (INITIALIZED != null && INITIALIZED.trim().length() <= 0) INITIALIZED = null;
//    if (DTGO != null && DTGO.trim().length() <= 0) DTGO = null;
//    if (DTER != null && DTER.trim().length() <= 0) DTER = null;

//    String sql = "select * from FCPRD where 1<>2";
//    if (INITIALIZED != null) sql += " and INITIALIZED=" + INITIALIZED + "";
//    if (DTGO != null) sql += " and DT >= " + DBUtil.toSqlDate(DTGO) + "";
//    if (DTGO != null && DTER != null)
//        sql += " and DT > " + DBUtil.toSqlDate(DTGO) + " and DT <= " + DBUtil.toSqlDate(DTER) + "";
//    sql += " order by DT desc";

    //CachedRowSet crs=manager.getRs(sql);
    String sql = "select ACCNO,ACCSTATUS,ACCNAME,ACCLEVEL,ACCBLG,ACCTP,ACCDLTP,BALTYPE,ACCCLASS from SCHOSTACC where acctp='3' and baltype='1' order by ACCNO";
    String pnStr = request.getParameter("pn");
    if (pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
    int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
    int ps = 30;
    Vector vec = manager.getPageRs(sql, pn, ps);
    int rows = ((Integer) vec.get(0)).intValue();
    CachedRowSet crs = (CachedRowSet) vec.get(1);

%>


<html>
<head>
    <title>信贷管理</title>
    <link href="../../css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
    <script language="JavaScript" type="text/JavaScript">

        function newAffair(affairName) {
// 备用
			var flag = 0;
            if (affairName == 'add') {
                var url = 'Qftime_add.jsp?Flyy_add_del=' + affairName;
                window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
            }
            else {

                for (i = 0; i < form1.elements.length; i++) {
                    if (form1.elements[i].checked == true) {
                    }
                    else {
                        flag = flag + 1;
                    }
                }
                if (flag >= form1.elements.length) {
                    alert("请选择要删除的纪录！");
                    return false;
                }
                else {
                    if (confirm('确定删除吗？')) {
                        form1.action = 'Qftime_del.jsp';
                        form1.submit();
                    }
                    else {
                        return false;
                    }

                }
            }
        }
        function info(accno) {
            var url = 'iteminfo.jsp?accno=' + accno;
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        function buttonDisabled() {
            with (form2) {
                a.disabled = "true";
                b.disabled = "true";
                c.disabled = "true";
            }
        }
    </script>
    <script src='../js/check.js' type='text/javascript'></script>
    <script src='../js/pagebutton.js' type='text/javascript'></script>
    <script src="../../js/flippage2.js" type="text/javascript"></script>
    <script src='../js/meizzDate.js' type='text/javascript'></script>
</head>
<body background="../../images/checks_02.jpg">
<form action="itemlist.jsp" name="form1" method="post" id="winform">
<input type="hidden" name="pnstr" value="<%=pnStr==null?"1":pnStr%>">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE">
<tr align="left">
    <td height="30" bgcolor="#4477AA"><img src="../../images/form/xing1.JPG" align="absmiddle"> <font size="2"             color="#FFFFFF"><b>科目管理</b></font>
        <img src="../../images/form/xing1.JPG" align="absmiddle"></td>
</tr>
<tr align="center">
<td height="260" valign="middle">
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
<td width="20">&nbsp;</td>
<td align="center" valign="middle">
<script src='/js/querybutton.js' type='text/javascript'></script>

<table>
<tr>
<td>
<table align='center' cellpadding='0' cellspacing='0' border='0' bgcolor='#AAAAAA' width='560'>
    <tr>
       
    </tr>
   
</table>
<table cellpadding='0' cellspacing='0' border='0'>
    <tr>
        <td height='5'></td>
    </tr>
</table>

<table class='list_form_table' width='560' align='center' cellpadding='0' cellspacing='1'
       border='0'>
    <tr class='list_form_title_tr'>
        <td width='15%' class='list_form_title_td' nowrap align="center">科目代号</td>
        <td width='25%' class='list_form_title_td' nowrap align="center">科目名称</td>
        <td width='15%' class='list_form_title_td' nowrap align="center">科目级别</td>
        <td width='15%' class='list_form_title_td' nowrap align="center">余额方向</td>
		<td width='15%' class='list_form_title_td' nowrap align="center">科目类别</td>
		<td width='15%' class='list_form_title_td' nowrap align="center">科目状态</td>

    </tr>
    <%
        int j = 0;


        while (crs.next()) {
    %>
    <tr class='list_form_tr'>
    
        <td nowrap class='list_form_td' align="center"><%=crs.getString("accno")%></td>
        <td nowrap class='list_form_td' align="left"> <%=DBUtil.fromDB(crs.getString("accname"))%></td>
        <td nowrap class='list_form_td' align="center"><%=level.getEnumItemName("AccLevel",crs.getString("acclevel"))%></td>
        <td nowrap class='list_form_td' align="center"><%=level.getEnumItemName("BalType",crs.getString("baltype"))%></td>
        <td nowrap class='list_form_td' align="center"><%=level.getEnumItemName("AccClass",crs.getString("accclass"))%></td>
         <td nowrap class='list_form_td' align="center"><%=level.getEnumItemName("AccStatus",crs.getString("accstatus"))%></td>
   
      
    </tr>
    <%
        }

        for (int i = crs.size(); i < 30; i++) {
    %>

    <tr class='list_form_tr'>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
        <td nowrap class='list_form_td'></td>
    </tr>
    <%

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
                <td nowrap align="center">

                    <table class='list_button_tbl'>
                        <tr class="list_button_tbl_tr">
                            <td class="list_form_button_td"><input name='add' type='button' disabled class='list_button_active'
                                                                   id="add" onClick="newAffair(this.name);"
                                                                   value=' 增加 '></td>
                            <td class="list_form_button_td"><input name='del' type='button' disabled class='list_button_active'
                                                                   id="del" onClick="newAffair(this.name);"
                                                                   value=' 删除 '></td>
                            <td class="list_form_button_td"><input type='submit' name='a' class='list_button_active'
                                                                   value=' 刷新 ' onclick="return req();"></td>

                            <script language="javascript" type="text/javascript">
                                createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "itemlist.jsp?pn=", "form1");
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
    document.title = "科目管理";
    document.focus();
    function checkSub(curChkName, formnm, funcnm)
    {

        if (curChkName == "" || formnm == "" || funcnm == "") {
            alert("checkSub error!");
            return;
        }
        var i;

        if (eval(formnm + "." + curChkName + ".checked")) {
            for (i = 0; i < eval(formnm + "." + funcnm + ".length"); i++) {
                if (eval(formnm + "." + funcnm + "[i].id.indexOf('" + curChkName + "')!=-1"))
                    eval(formnm + "." + eval(formnm + "." + funcnm + "[i].id") + ".checked=true");
            }
        }

        if (eval(formnm + "." + curChkName + ".checked==false")) {
            for (i = 0; i < eval(formnm + "." + funcnm + ".length"); i++) {
                if (eval(formnm + "." + funcnm + "[i].id.indexOf('" + curChkName + "')!=-1"))
                    eval(formnm + "." + eval(formnm + "." + funcnm + "[i].id") + ".checked=false");
            }
        }

        if (eval(formnm + "." + curChkName + ".checked")) {
            for (i = 0; i < eval(formnm + "." + funcnm + ".length"); i++) {
                if (eval(formnm + "." + funcnm + "[i].id.indexOf('" + curChkName + "')!=-1"))
                    eval(formnm + "." + eval(formnm + "." + funcnm + "[i].id") + ".checked=true");
            }
        }
    }
    function check_click() {
        document.form1.INITIALIZED.value = "";
        document.form1.DTGO.value = "";
        document.form1.DTER.value = "";
    }

    function req() {
        document.location.replace("/jsp/bm/itemlist.jsp");
    }
</script>

