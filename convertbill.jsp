<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.biz.Ledger"%>
<html>
<head>
<title>
refresh
</title>
</head>
<body bgcolor="#ffffff">
<h1>
Bill Conversion Program
</h1>
<% 
Ledger.LoopEntryAcptBillManually();
Ledger.LoopEntryBillDisManually();
%>

Conversion Finished! Please check the Console

</body>
</html>
