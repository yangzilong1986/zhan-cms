<%--
*******************************************************************
*    ��������:    bmappcancle.jsp
*    �����ʶ:
*    ��������:    �Ǽ�ȡ��ȷ��ҳ��
*    ������ҳ:
*    ���ݲ���:
*    ��   ��:     wxj
*    ��������:    2004-02-06
*    �� �� ��:    
*    �޸�����:    
*    ��    Ȩ:    �ൺ������Ϣ�������޹�˾
*
*
*******************************************************************
--%>

<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cmsi.biz.*" %>
<%
//rooturl
String rooturl="http://"+request.getServerName();
int svrport=request.getServerPort();
if(svrport!=80){
	rooturl+=":"+svrport;
}
rooturl+=request.getContextPath();
//request
String act = (String)request.getParameter("act");
String BMNo = null;
String BMTransNo = null;
String OPERATOR = null;
if(act==null){
	BMNo = (String)request.getAttribute("BMNo");
	BMTransNo = (String)request.getAttribute("BMTransNo");
	OPERATOR = (String)request.getAttribute("OPERATOR");
}
else{
	BMNo = (String)request.getParameter("BMNo");
	BMTransNo = (String)request.getParameter("BMTransNo");
	OPERATOR = (String)request.getParameter("OPERATOR");
}

String msg = "��ȷ��Ҫȡ���ñ�ҵ����";
int flg=0;

BMNo = (BMNo==null?"":BMNo.trim());
BMTransNo = (BMTransNo==null?"":BMTransNo.trim());
OPERATOR = (OPERATOR==null?"":OPERATOR.trim());
act = (act==null?"":act.trim());

if(act.equals("del")){
	try{
		flg = BMTrans.cancelBMTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
		if (flg >= 0) {
			msg="�Ǽ�ȡ���ɹ���";
		}
		else{
			msg="�Ǽ�ȡ��ʧ�ܣ�";
		}
	}
	catch(Exception ex){
		msg="δ֪ԭ���µ�ϵͳ����";
	}
}
%>
<html>
	<head>
		<title>ȷ����Ϣ</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rooturl%>/css/platform.css" rel="stylesheet" type="text/css">
	</head>
	<body background="<%=rooturl%>/images/checks_02.jpg">
	<form name="form1" method="post" action="<%=rooturl%>/jsp/bm/bmappcancle.jsp">
		<input type='hidden' name='BMNo' value='<%=BMNo%>'>
		<input type='hidden' name='BMTransNo' value='<%=BMTransNo%>'>
		<input type='hidden' name='OPERATOR' value='<%=OPERATOR%>'>
		<input type='hidden' name='act' value=''>
	</form>
	<script language="javascript">
	function gosubmit(){
		document.form1.act.value="del";
		document.form1.submit();
	}
	</script>
  <table width="160" height="200" border="" align="center" bordercolorlight="#336699" bordercolordark="#336699" bgcolor="#9DBBD9" onKeyDown="pressKeyDown(event.keyCode);" >
			<tr align="center">
			    <td valign="top">
					<table>
						<tr>
							<td><img src="<%=rooturl%>/images/title.jpg" width="290" height="49"></td>
						</tr>
					</table>
					<table width="100%" height="100" class='page_form_table'>
						<tr class='page_form_tr'>
							<td class='page_form_td'><%=msg%></td>
						</tr>
					</table>
					<table class='page_button_tbl'>
						<tr class='page_button_tbl_tr'>
							<td class='page_button_tbl_td'>
							<%
								if(!act.equals("del")){
							%>
								<input class="list_button_active" type="button" name="ok" value=" ȷ �� " onClick="gosubmit();">
							<%
								}
							%>
								<input class="list_button_active" type="button" name="close" value=" �� �� " onClick="window.close();">
							</td>
						</tr>
					</table>
					</td>
			</tr>
	</table>
	</body>
</html>
