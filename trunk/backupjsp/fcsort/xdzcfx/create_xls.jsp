<%@ page contentType="application/msexcel; charset=GB2312"%>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.platform.db.DBUtil"%>
<%@ page import="zt.cms.pub.SCBranch" %>
<%--
===============================================
Title: �������������ҳ��
Description: ���������������Ϣҳ��
 * @version   $Revision: 1.2 $  $Date: 2007/05/31 03:13:58 $
 * @author   houcs
 * <p/>�޸ģ�$Author: zhengx $
===============================================
--%>
<%
   String path = request.getContextPath();
   String colspan=request.getParameter("colspan");
   int i=Integer.valueOf(colspan).intValue();
   String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
   String yearday = DBUtil.fromDB(request.getParameter("yeardays") ==null? "2": request.getParameter("yeardays"));
   String startdate=request.getParameter("startdate")==null?"":request.getParameter("startdate");
   String enddate=request.getParameter("enddate")==null?"":request.getParameter("enddate");
   String strScbrhId=request.getParameter("brhid")==null?"":request.getParameter("brhid");
   String strScbrhName = SCBranch.getSName(strScbrhId);//��������
   String create=request.getParameter("create")==null?"":request.getParameter("create");
   String counts=request.getParameter("cou")==null?"":request.getParameter("cou");
   if(!counts.equals("")){
   i=i-1;
   if(counts.equals("1")){
	 counts="���귢�����굽��";
	}else if(counts.equals("2")){
	counts="���귢�ŵ��굽��";
	}else if(counts.equals("3")){
	counts="���귢�ŵ������ڵ���";
	}else if(counts.equals("4")){
	counts="���귢�ű��ڵ���";
	}else if(counts.equals("5")){
	counts="���귢�ŵ������";
	}else if(counts.equals("6")){
	counts="���귢�ű��ں���";
	}
   }
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
<%			String thisMonth="�Ŵ��ʲ����շ�������±��� ";
           
			String  filenamc = new String(thisMonth.getBytes("GBK"),"ISO-8859-1");  
			
			response.setHeader("Content-disposition",
			"inline;filename="+filenamc+".xls");
			
			
%>
<html>

<head>
<title>�Ŵ��ʲ����շ�������±��� </title>
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
 �Ŵ��ʲ����շ�������±��� 
 
  </font>
  </div>
  <div class="caption" align="center">
		<font class="title">
        ͳ�����ڣ�<%=subCreate%>
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
	
	<td class="title" align="right" colspan="12" width="2000">
	
	��λ����Ԫ��%
	
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
