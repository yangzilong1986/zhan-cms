<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page contentType="text/html;charset=GBK" %>
<html >
<HEAD>
<TITLE></TITLE>
</HEAD>
<body>
<form action="create_xls.jsp" method="post" name="form1">
<input type="hidden" value="" name="s_count">
<input type="hidden" value="" name="s_where">
<input type="hidden" value="" name="s_main">
<input type="hidden" value="" name="brhid">
</form>
</body>
<script type="text/javascript">
document.all.s_count.value=opener.document.form1.s_count.value;
document.all.s_where.value=opener.document.form1.s_where.value;
document.all.s_main.value=opener.document.form1.s_main.value;
document.all.brhid.value=opener.document.form1.BRHID.value;
document.forms[0].submit();
</script>
</html>