<%
/********************************************
 * 文件名称：view.jsp
 * 功能描述：实网点显示页面
 * 创建日期：2007-04-09
 * @author：weiyb
 * @version 1.0
*********************************************/
%>
<%@ page contentType="text/html; charset=GBK"  errorPage="error.jsp"%>
<%@ page import="zt.cms.pub.SCBranch"%>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="zt.cms.pub.SCUser"%>
<%@ page import="zt.platform.cachedb.ConnectionManager"%>
<%@ page import="zt.platform.db.DBUtil"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.util.Vector"%>
<%@ page import="zt.platform.user.UserManager"%>
<%@ page import="zt.platform.form.config.SystemAttributeNames"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	String BRHID = request.getParameter("BRHID");//得到过滤条件中的网点号
	String LNAME = request.getParameter("LNAME");//得到网点名
	String BRHLEVEL = request.getParameter("BRHLEVEL");//得到网点类型
	if (BRHID != null && BRHID.trim().length() <= 0)
		BRHID = null;
	if (LNAME != null && LNAME.trim().length() <= 0)
		LNAME = null;
	if (BRHLEVEL != null && BRHLEVEL.trim().length() <= 0)
		BRHLEVEL = null;
	UserManager um = (UserManager) session
			.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	if (um == null) {
		response.sendRedirect("../rqlcheck/error.jsp");
	}
	String SUBBRHIDs = SCBranch.getSubBranchAll(SCUser.getBrhId(
			um.getUserName()).trim());//根据branchid返回其所有下级纯实子网点，如果自己是实网点也包含在内。
	SUBBRHIDs = SUBBRHIDs.replaceAll(",", "','");
	String sql = "select brhid,lname, brhlevel from scbranch where 1<>2";
	if (BRHID != null) {
		sql += " and BRHID like '" + BRHID + "%'";
	} else {
		sql += " and BRHID in ('" + SUBBRHIDs + "')";
	}//判断是否输入过滤条件，若没输，则返回所有下级纯实子网点，如果自己是实网点也包含在内。
	if (LNAME != null)
		sql += " and LNAME like '" + LNAME + "%'";
	if (BRHLEVEL != null)
		sql += " and BRHLEVEL=" + BRHLEVEL + "";
	ConnectionManager manager = ConnectionManager.getInstance();
	String pnStr = request.getParameter("pn");
	if (pnStr == null || pnStr.trim().length() <= 0)
		pnStr = null;
	int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
	int ps = 10;
	Vector vec = manager.getPageRs(sql, pn, ps);
	int rows = ((Integer) vec.get(0)).intValue();
	CachedRowSet crs = (CachedRowSet) vec.get(1);
%>
<html>
<head>
<base href="<%=basePath%>">
<title>网点查询</title>
<link href="css/platform.css" rel="stylesheet" type="text/css">
<script language='javascript'>
    function check_click() {
        document.form1.BRHID.value = "";
        document.form1.LNAME.value = "";
        document.form1.BRHLEVEL.value = "";
    }
    function refselect_brhid1(name,value) {
    var val=value.split('/');
    name.value = val[0];
    //opener.form1.branchname.value=val[1];
    //opener.window.document.all.branchname1.innerText='网点名称:'+val[1];
    window.close();
}
</script>
</head>
<body bgcolor="#ffffff">
选择网点
<script src='js/pagebutton.js' type='text/javascript'></script>
<script language="javascript" src="js/flippage.js"></script>
<script src='js/ref.js' type='text/javascript'></script>

<form name="form1" method='post' action="query/common/view.jsp">
<table class='reference_tbl'>
	<tr class='reference_tbl_head_tr'>
		<td class='reference_tbl_head_value_td'>网点</td>
		<td class='reference_tbl_head_desc_td'>网点全名</td>
	</tr>
	<%
	while (crs.next()){
	%>
	<tr class='reference_tbl_content_tr'>
		<td class='reference_button_tbl_td'><a href="javascript:;"
			onClick="return refselect_brhid1(opener.winform.BRHID,'<%=DBUtil.fromDB(crs.getString("brhid"))%>/<%=DBUtil.fromDB(crs.getString("lname"))%>')"><%=crs.getString("brhid")%> </a></td>
		<td class='reference_button_tbl_td'><a href="javascript:;"
			onClick="return refselect_brhid1(opener.winform.BRHID,'<%=DBUtil.fromDB(crs.getString("brhid"))%>/<%=DBUtil.fromDB(crs.getString("lname"))%>')"><%=DBUtil.fromDB(crs.getString("lname"))%> </a></td>
	</tr>
	<%
	}
	%>
</table>
<table class='blank_table'></table>
<table class='reference_button_tbl'>
	<tr class='reference_button_tbl_tr'>
		<td class='reference_button_tbl_td'><input
			class='reference_button_active' type='button' name='submit3'
			value=' 过 滤 '
			onclick="if ( filter.style.visibility == 'hidden' ) { filter.style.visibility ='';} else { filter.style.visibility = 'hidden';}">
		</td>
		<script language="javascript">
                createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "query/common/view.jsp?pn=", "form1");
            </script>
		<td><input type="button" name="submit1"
			class="reference_button_active" value=" 退 出 "
			onClick="window.close();"></td>
	</tr>
</table>
<table class='blank_table'></table>
<div id='filter' style='visibility:hidden'>

<table class='filter_table'>
	<tr class='filter_table_tr'>
		<td class="page_form_title_td" nowrap>网点</td>
		<td class="page_form_td"><input type="text" name="BRHID"
			value="<%=BRHID==null?"":BRHID%>" class="page_form_text" mayNull="1"
			dataType="1" errInfo="网点"></td>
	</tr>
	<tr class='filter_table_tr'>
		<td class="page_form_title_td" nowrap>网点全名</td>
		<td class="page_form_td"><input type="text" name="LNAME"
			value="<%=LNAME==null?"":LNAME%>" class="page_form_text" mayNull="1"
			dataType="1" errInfo="网点全名"></td>
	</tr>
	<tr class='filter_table_tr'>
		<td class="page_form_title_td" nowrap>网点级别</td>
		<td class="page_form_td"><%=level.levelHereExt("BRHLEVEL", "BrhLevel", BRHLEVEL, "")%>
		</td>
	</tr>
</table>
<table class='filter_button_table'>
	<tr class='filter_button_table_tr'>
		<td class='filter_button_table_td'><input type='submit'
			class='filter_button_active' name='submit2' value=' 确 定 '> <input
			type="button" class='filter_button_active' name='reset' value='重新填写'
			onclick="check_click()"></td>
	</tr>
</table>
</div>
</form>
</body>
</html>

