<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="zt.cms.pub.SCBranch"%>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="zt.cms.pub.SCUser"%>
<%@ page import="zt.platform.cachedb.ConnectionManager"%>
<%@ page import="zt.platform.db.DBUtil"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.util.Vector"%>
<%@ page import="zt.platform.user.UserManager"%>
<%@ page import="zt.platform.form.config.SystemAttributeNames"%>
<%--
=============================================== 
Title: ����̨���ֺܷ�����ѡ����ͼҳ��
Description: ��ʾ�����Թ�ѡ���ѯ��
 * @version   $Revision: 1.1 $  $Date: 2007/04/28 14:19:55 $
 * @author   houcs
 * <p/>�޸ģ�$Author: liuj $
=============================================== 
--%>
<%
	String BRHID = request.getParameter("BRHID");//�õ����������е������
	String LNAME = request.getParameter("LNAME");//�õ�������
	String BRHLEVEL = request.getParameter("BRHLEVEL");//�õ���������
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
			um.getUserName()).trim());//����branchid�����������¼���ʵ�����㣬����Լ���ʵ����Ҳ�������ڡ�
	SUBBRHIDs = SUBBRHIDs.replaceAll(",", "','");
	String sql = "select brhid,sname, brhlevel from scbranch where 1<>2";
	if (BRHID != null) {
		sql += " and BRHID like '" + BRHID + "%'";
	} else {
		sql += " and BRHID in ('" + SUBBRHIDs + "')";
	}//�ж��Ƿ����������������û�䣬�򷵻������¼���ʵ�����㣬����Լ���ʵ����Ҳ�������ڡ�
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
<title>�й��ź�-�ο�����</title>
<link href="../css/platform.css" rel="stylesheet" type="text/css">
<script language='javascript'>
    function check_click() {
        document.form1.BRHID.value = "";
        document.form1.LNAME.value = "";
        document.form1.BRHLEVEL.value = "";
    }
     function refselect_brhid2(name,value){
  
     name.value = value;
      window.close();
    }
    function refselect_brhid1(name,value,name1,value1) {
    name1.innerText = value1;
    name.value = value;
    window.close();
	}
</script>
</head>
<body bgcolor="#ffffff">
����̨���ֺܷ˶�
<script src='/js/pagebutton.js' type='text/javascript'></script>
<script language="javascript" src="../js/flippage.js"></script>
<script src='/js/ref.js' type='text/javascript'></script>

<form name="form1" method='post' action="view.jsp">
<table class='reference_tbl'>
	<tr class='reference_tbl_head_tr'>
		<td class='reference_tbl_head_value_td'>����</td>
		<td class='reference_tbl_head_desc_td'>����ȫ��</td>
	</tr>
	<%
	while (crs.next()){
	%>
	<tr class='reference_tbl_content_tr'>
		<td class='reference_button_tbl_td'><a href="#" id="aa"
			onClick="return refselect_brhid1(opener.winform.brhid,'<%=DBUtil.fromDB(crs.getString("brhid"))%>',opener.lname1,'<%=DBUtil.fromDB(crs.getString("sname"))%>')"><%=crs.getString("brhid")%> </a></td>
		<td class='reference_button_tbl_td'><a href="#"
			onClick="return refselect_brhid1(opener.winform.brhid,'<%=DBUtil.fromDB(crs.getString("brhid"))%>',opener.lname1,'<%=DBUtil.fromDB(crs.getString("sname"))%>')"><%=DBUtil.fromDB(crs.getString("sname"))%> </a></td>
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
			value=' �� �� '
			onclick="if ( filter.style.visibility == 'hidden' ) { filter.style.visibility ='';} else { filter.style.visibility = 'hidden';}">
		</td>
		<script language="javascript">
                createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "view.jsp?pn=", "form1");
            </script>
		<td><input type="button" name="submit1"
			class="reference_button_active" value=" �� �� "
			onClick="window.close();"></td>
	</tr>
</table>
<table class='blank_table'></table>
<div id='filter' style='visibility:hidden'>

<table class='filter_table'>
	<tr class='filter_table_tr'>
		<td class="page_form_title_td" nowrap>����</td>
		<td class="page_form_td"><input type="text" name="BRHID"
			value="<%=BRHID==null?"":BRHID%>" class="page_form_text" mayNull="1"
			dataType="1" errInfo="����"></td>
	</tr>
	<tr class='filter_table_tr'>
		<td class="page_form_title_td" nowrap>����ȫ��</td>
		<td class="page_form_td"><input type="text" name="LNAME"
			value="<%=LNAME==null?"":LNAME%>" class="page_form_text" mayNull="1"
			dataType="1" errInfo="����ȫ��"></td>
	</tr>
	<tr class='filter_table_tr'>
		<td class="page_form_title_td" nowrap>���㼶��</td>
		<td class="page_form_td"><%=level.levelHereExt("BRHLEVEL", "BrhLevel", BRHLEVEL, "")%>
		</td>
	</tr>
</table>
<table class='filter_button_table'>
	<tr class='filter_button_table_tr'>
		<td class='filter_button_table_td'><input type='submit'
			class='filter_button_active' name='submit2' value=' ȷ �� '> <input
			type="button" class='filter_button_active' name='reset' value='������д'
			onclick="check_click()"></td>
	</tr>
</table>
</div>
</form>
</body>
</html>
