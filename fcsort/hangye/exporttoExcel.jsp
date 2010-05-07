<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page contentType="text/html;charset=gb2312" %>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%--
===============================================
Title: 新增不良贷款导出页面
Description: 新增不良贷款导出信息页面
 * @version   $Revision: 1.2 $  $Date: 2007/05/22 08:06:30 $
 * @author   houcs
 * <p/>修改：$Author: houcs $
===============================================
--%>
<%
String brhid=request.getParameter("brhid")==""?"":request.getParameter("brhid");
String startdate=request.getParameter("startdate")==""?"":request.getParameter("startdate");
String enddate=request.getParameter("enddate")==""?"":request.getParameter("enddate");
String yeardays=request.getParameter("yeardays")==""?"":request.getParameter("yeardays");
String create=request.getParameter("create")==""?"":request.getParameter("create");
String colspan=request.getParameter("colspan")==null?"":request.getParameter("colspan");
String cou=request.getParameter("cou")==null?"":request.getParameter("cou");
System.out.println(brhid+".....................");
 %>
<html >
<HEAD>
<TITLE></TITLE>
</HEAD>
<body>
<form action="create_xls.jsp" method="post" name="form1">
    <input type="hidden" name="brhid" value="<%=brhid%>">
    <input type="hidden" name="creadate" value="<%=startdate%>">
	<input type="hidden" name="enddate" value="<%=enddate%>">
	<input type="hidden" name="yeardays" value="<%=yeardays%>">
	<input type="hidden" name="create" value="<%=create%>">
	<input type="hidden" name="colspan" value="<%=colspan%>">
	<input type="hidden" name="cou" value="<%=cou%>">
<input type="hidden" value="" name="tabledata">
</form>
</body>
<script type="text/javascript">
document.all.tabledata.value=opener.document.listform.referValue.value;
document.forms[0].submit();
</script>
</html>