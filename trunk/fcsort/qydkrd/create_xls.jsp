<%@ page contentType="application/msexcel; charset=GB2312"%>
<%@ page import="zt.platform.db.DBUtil"%>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%--
===============================================
Title: �������������ҳ��
Description: ���������������Ϣҳ��
 * @version   $Revision: 1.1 $  $Date: 2007/06/19 03:20:28 $
 * @author   houcs
 * <p/>�޸ģ�$Author: houcs $
===============================================
--%>
<%
   String path = request.getContextPath();
   String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
   String strScbrhId=request.getParameter("brhid")==null?"":request.getParameter("brhid");
   String strScbrhName = SCBranch.getSName(strScbrhId);//��������
   String create=request.getParameter("create")==null?"":request.getParameter("create");
   String subCreate=create.substring(0,7);
   subCreate=subCreate.replaceAll("-","��");
   if(subCreate.substring(5,6).equals("0")){
    subCreate= subCreate.substring(0,5)+subCreate.substring(6,7)+"��";
    }else{
    subCreate=subCreate+"��";
    }
%>
<%
response.setContentType("application/vnd.ms-excel;charset=gb2312");
%>
<%			String thisMonth=" ��ҵ��λ��������϶��� ";
           
			String  filenamc = new String(thisMonth.getBytes("GBK"),"ISO-8859-1");  
			
			response.setHeader("Content-disposition",
			"inline;filename="+filenamc+".xls");
			
			
%>
<html>

<head>
<title> ��ҵ��λ��������϶��� </title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<link rel="stylesheet" type="text/css" href="web.css">
		<script language="javascript" src="<%= basePath %>query/setup/meizzDate.js"></script>
		<script language="javascript" src="ajax.js"></script>
		<script type="text/javascript" src="fcsort.js"></script>
</head>
<body>
    <div class="caption" align="center">
     <font size="4"> 
  ��ҵ��λ��������϶���
 
  </font>
  </div>
  <div class="caption" align="center">
		<font class="title">
        ��Ч���ڣ�<%=subCreate%>
 </font>
</div>
<div align="center" width="100%">
<table cellSpacing='0' cellPadding='2' width="100%">
<tr width="2200">
	<td width="200">
	<font class="title" align="left">
         �������ƣ�<%=strScbrhName%>
         </font>
	</td>
	<td class="title" align="right" colspan="15" width="2000">
	����:�����&nbsp;
	��λ����Ԫ
	</td>
	</tr>
</table>
</div>
<TABLE borderColor="#111111" cellSpacing='0' cellPadding='2'
	width='2200' align='center' border='1'>
	<%=DBUtil.fromDB(request.getParameter("tabledata"))%>
</TABLE>
</body>
</html>
