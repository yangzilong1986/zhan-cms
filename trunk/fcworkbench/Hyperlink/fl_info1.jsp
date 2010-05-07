<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.platform.cachedb.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.form.control.impl.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.utils.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%
 String FCCLASS = request.getParameter("FCCLASS");

if(FCCLASS != null && FCCLASS.trim().length() <= 0) FCCLASS = null;

String sql="select FCCLASS,REASON from FCREASON where 1<>2";
if(FCCLASS != null) sql += " and FCCLASS="+FCCLASS+"";
	sql+=" order by FCCLASS";
ConnectionManager manager=ConnectionManager.getInstance();
	String pnStr = request.getParameter("pn");
	if(pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
	int pn=Integer.parseInt(pnStr==null?"1":pnStr);
	int ps=10;
	Vector vec=manager.getPageRs(sql,pn,ps);
	int rows=((Integer)vec.get(0)).intValue();
	CachedRowSet crs=(CachedRowSet)vec.get(1);
%>
<html>
<head>
	<title>中国信合-参考窗口</title>
</head>
<body bgcolor="#ffffff">
清分意见
<link href="/css/platform.css" rel="stylesheet" type="text/css">
<script src='/js/pagebutton.js' type='text/javascript'></script>
<script language="javascript" src="/js/flippage.js"></script>
<script src='/js/ref.js' type='text/javascript'></script>

<form name="form1" method='post' action="">
<table class='reference_tbl'>
<tr class='reference_tbl_head_tr'>
<td class='reference_tbl_head_value_td' nowrap>五级分类</td>
<td class='reference_tbl_head_desc_td'>原因</td>
</tr>
<%
	while(crs.next()){
%>
<tr class='reference_tbl_content_tr'>
<td class='reference_button_tbl_td'>
<a href="#" onClick="return refselect_FCCLASS(opener.winform.CMT2,'<%=DBUtil.fromDB(crs.getString("REASON"))%>')"><%=level.getEnumItemName("LoanCat1",crs.getString("FCCLASS"))%></a>
</td>
<td class='reference_button_tbl_td'>
<a href="#" onClick="return refselect_FCCLASS(opener.winform.CMT2,'<%=DBUtil.fromDB(crs.getString("REASON"))%>')"><%=DBUtil.fromDB(crs.getString("REASON"))%></a>
</td>
 </tr>
 <%
}
%>
</table>
<table class='blank_table'></table>
<table class='reference_button_tbl'>
<tr class='reference_button_tbl_tr'>
<td class='reference_button_tbl_td'>
<input class='reference_button_active' type='button' name='submit3' value=' 过 滤 ' onclick="if ( filter.style.visibility == 'hidden' ) { filter.style.visibility ='';} else { filter.style.visibility = 'hidden';}">
<script language="javascript">
    		createFlipPage(<%=pn%>,<%=ps%>,<%=rows%>,"fl_info1.jsp?pn=","form1");
</script>
<td>
<input type="button" name="submit1" class="reference_button_active" value=" 退 出 " onClick="window.close();"></td>
</tr>
</table>
<table class='blank_table'></table>
<div id='filter' style='visibility:hidden'>

<table class='filter_table'>
<tr class='filter_table_tr'>
<td class="page_form_title_td" nowrap>网点级别</td>
<td class="page_form_td" ><%=level.levelHereExt("FCCLASS","LoanCat1",FCCLASS,"")%></td>
</tr>
</table>
<table class='filter_button_table'>
<tr class='filter_button_table_tr'>
<td class='filter_button_table_td'>
<input type='submit' class='filter_button_active' name='submit2' value=' 确 定 ' >
<input type="button" class='filter_button_active' name='reset' value='重新填写' onclick="check_click()"></td>
</tr></table></div>
</form>
</body>
</html>
<script language='javascript'>
function check_click(){
  document.form1.FCCLASS.value="";
}
</script>