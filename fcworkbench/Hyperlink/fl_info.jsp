<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.util.Vector" %>
<%--
===============================================
Title: 清分意见维护
Description: 清分意见维护-可以选择多项意见。
 * @version  $Revision: 1.3 $  $Date: 2007/05/31 02:25:05 $
 * @author   liujian
 * <p/>修改：$Author: liuj $
===============================================
--%>


<%
    String FCCLASS = request.getParameter("FCCLASS");                          //五级分类
    if (FCCLASS != null && FCCLASS.trim().length() <= 0) FCCLASS = null;
    String CMT = "opener.winform." + request.getParameter("cmt");              //lj added in 2007-04-15 for delete the flinfo1.jsp、fl_info2、fl_info3，父页面五级分类认定原因ID
    String chaFcclass = "opener.winform." + request.getParameter("chaFcclass");//lj added in 2007-04-15 for auto change FC1，自动变更父页面五级分类认定结果
    

    String sql = "select FCCLASS,REASON from FCREASON where 1<>2";
    if (FCCLASS != null) sql += " and FCCLASS=" + FCCLASS + "";
    sql += " order by FCCLASS";
    ConnectionManager manager = ConnectionManager.getInstance();

    String pnStr = request.getParameter("pn");
    if (pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
    int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
    int ps = 10;
    Vector vec = manager.getPageRs(sql, pn, ps);
    int rows = ((Integer) vec.get(0)).intValue();
    CachedRowSet crs = (CachedRowSet) vec.get(1);
%>
<html>
<head>
    <title>中国信合-清分意见</title>
    <link href="/css/platform.css" rel="stylesheet" type="text/css">
    <script src='/js/pagebutton.js' type='text/javascript'></script>
    <script language="javascript" src="/js/flippage.js" type="text/javascript"></script>
    <script src='/js/ref.js' type='text/javascript'></script>
</head>
<body bgcolor="#ffffff" onUnload="check_click1();">
<form name="form1" method='post' action='fl_info.jsp'>
    <table class='reference_tbl'>
        <tr class='reference_tbl_head_tr'>
            <td class='reference_tbl_head_value_td' width='24'>选择</td>
            <td class='reference_tbl_head_value_td' nowrap>五级分类</td>
            <td class='reference_tbl_head_desc_td'>原因</td>
        </tr>
        <%
            int j = 0;//lj added in 2007-04-15 for add the checkbox
            while (crs.next()) {
        %>
        <tr class='reference_tbl_content_tr'>
            <!--lj added in 2007-04-15 for add the checkbox-->
            <td class='reference_button_tbl_td'><input name="SEQNO" type='checkbox' id="fs<%=j%>"></td>
            <td class='reference_button_tbl_td'>
                <input type="hidden" name="fcclass" id="fc<%=j%>" value="<%=crs.getString("FCCLASS")%>">
                <a href="#"
                   onClick="refmin_Fcclass(<%=chaFcclass%>,<%=crs.getString("FCCLASS")%>);return refselect_FCCLASS(<%=CMT%>,'<%=DBUtil.fromDB(crs.getString("REASON"))%>')"><%=
                level.getEnumItemName("LoanCat1", crs.getString("FCCLASS"))%>
                </a>
            </td>
            <td class='reference_button_tbl_td'>
                <a href="#"
                   onClick="refmin_Fcclass(<%=chaFcclass%>,<%=crs.getString("FCCLASS")%>);return refselect_FCCLASS(<%=CMT%>,'<%=DBUtil.fromDB(crs.getString("REASON"))%>')"
                   id="fr<%=j%>"><%=
                DBUtil.fromDB(crs.getString("REASON"))%>
                </a>
            </td>
            <!--lj added end-->
        </tr>
        <%
                j++;//lj added in 2007-04-15 for add the checkbox
            }
        %>
    </table>
    <table class='blank_table'></table>
    <table class='reference_button_tbl'>
        <tr class='reference_button_tbl_tr'>
            <td class='reference_button_tbl_td'>
                <input class='reference_button_active' type='button' name='submit1' value=' 过 滤 '
                       onclick="if ( filter.style.visibility == 'hidden' ) { filter.style.visibility ='';} else { filter.style.visibility = 'hidden';}">
            </td>
            <script language="javascript" type="text/javascript">
                //lj added in 2007-04-15 for auto change FC1
                createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "fl_info.jsp?cmt=<%=request.getParameter("cmt")%>&chaFcclass=<%=request.getParameter("chaFcclass")%>&pn=", "form1");
            </script>
            <td class='page_button_tbl_td'><input type='button' class='page_button_active'
                                                  value=' 提交 '
                                                  onClick="addallselect(<%=CMT%>,<%=j%>, <%=chaFcclass%>)">
                <input type="hidden" name="btn1" value="test1" onclick="test1();">
            </td>

            <td>
                <input type="button" name="submit3" class="reference_button_active" value=" 退 出 "
                       onClick="window.close();"></td>
        </tr>
    </table>
    <table class='blank_table'></table>
    <div id='filter' style='visibility:hidden'>

        <table class='filter_table'>
            <tr class='filter_table_tr'>
                <td class="page_form_title_td" nowrap>网点级别</td>
                <td class="page_form_td"><%=level.levelHereExt("FCCLASS", "LoanCat1", FCCLASS, "")%>
                </td>
            </tr>
        </table>
        <table class='filter_button_table'>
            <tr class='filter_button_table_tr'>
                <td class='filter_button_table_td'>
                    <input type='submit' class='filter_button_active' name='submit2' value=' 确 定 '>
                    <input type="button" class='filter_button_active' name='reset' value='重新填写' onClick="check_click()">

                </td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>
<script language='javascript' type="text/javascript">
    function check_click() {
        document.form1.FCCLASS.value = "";
    }
    //lj added in 2007-04-24 for add the checkbox
    function check_click1() {
        addselect(<%=j%>);
    }

    //===============================================
    //Title: 多意见选择
    //Description: 可以分页多项选择意见,采用动态创建府页面对象方式存储值。
    // * @version  $Revision: 1.3 $  $Date: 2007/05/31 02:25:05 $
    // * @author   liujian
    // * <p/>修改：$Author: liuj $
    //===============================================

    //this function added all strings into one for which is selected.
    function addselect(len) {
        var obj1;
        obj1 = (opener.window.document.getElementById("id1") == null) ? opener.window.document.createElement("input") : opener.window.document.getElementById("id1");
        obj1.id = "id1";
        obj1.type = "hidden";
        opener.window.document.forms[0].appendChild(obj1);
        var obj2 ;
        obj2 = (opener.window.document.getElementById("id2") == null) ? opener.window.document.createElement("input") : opener.window.document.getElementById("id2");
        obj2.id = "id2";
        obj2.type = "hidden";
        opener.window.document.forms[0].appendChild(obj2);

        for (var i = 0; i < len; i++) {
            if (document.getElementById("fs" + i).checked) {
                if (opener.window.document.forms[0].id1.value.indexOf(document.getElementById("fr" + i).innerHTML) < 0) {
                    if (opener.window.document.forms[0].id1.value == "")
                        opener.window.document.forms[0].id1.value = document.getElementById("fr" + i).innerHTML;
                    else
                        opener.window.document.forms[0].id1.value += "*" + document.getElementById("fr" + i).innerHTML;
                    if (opener.window.document.forms[0].id2.value == "")
                        opener.window.document.forms[0].id2.value = document.getElementById("fc" + i).value;
                    else
                        opener.window.document.forms[0].id2.value += "*" + document.getElementById("fc" + i).value;
                }
            }
        }
    }

    function addallselect(name, len, chaFcclass) {
        document.body.onunload = "";
        addselect(len);

        var sval = "";
        if (opener.window.document.forms[0].id1.value != "") {
            var vals = opener.window.document.forms[0].id1.value.split("*");
            var minFcclasss = opener.window.document.forms[0].id2.value.split("*");
            opener.window.document.forms[0].id1.value = "";
            opener.window.document.forms[0].id2.value = "";

            var val = new Array();
            var minFcclass = "1";

            for (var i = 0; i < vals.length; i++) {
                if (vals.length == 1)
                    val[i] = vals[i];
                else
                    val[i] = i + 1 + "、" + vals[i];
                minFcclass = (minFcclasss[i] > minFcclass) ? minFcclasss[i] : minFcclass;
            }
            sval = val.join("。");
            sval=sval.replace(/\；/g,"。");
            sval=sval.replace(/\。 。/g,"。");
        }
        refmin_Fcclass(chaFcclass, minFcclass);
        refselect_FCCLASS(name, sval);
    }

    function refmin_Fcclass(name, avalue) {
        name.value = avalue;
    }
    //===============================================
    //lj added end.

    function refselect_FCCLASS(name, value) {
    //  name.value = value;
        name.value = value.replace(/\；/g,"。");   //lj chenged in 2007-05-31
        window.close();
    }

    function test1() {
        var obj = new Array();
        var vals = opener.window.document.forms[0].id11.value.split("*");
        for (var i = 0; i < vals.length; i++) {
            obj[i] = i + 1 + "、" + vals[i];
        }
        var sobj = obj.join("");
        alert(sobj);
    }
</script>