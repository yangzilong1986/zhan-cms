<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="zt.cms.cm.servlet.*" %>

<%@ page import="zt.cmsi.mydb.MyDB" %>


<% 

MyDB.getInstance().removeCurrentThreadConn("photo.jsp"); //added by JGO on 2004-07-17

%>


<html>
	<head>
		<title>信贷管理</title>
		<link href="../css/platform.css" rel="stylesheet" type="text/css">
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<style type="text/css">
		<!--
		body {
			margin-top: 5px;
		}
		-->
		</style>
	</head>
	<body background="../images/checks_02.jpg">
	
	<%
	request.setCharacterEncoding("GB2312");
				  String tableName=(String)request.getAttribute("TABLENAME");
				  String whereCondition = (String)request.getAttribute("WHERECONDITION");
				  String flag = (String)request.getAttribute("flag");
				  String title = (String)request.getAttribute("title");
				  
				  if(tableName==null){
				      tableName=request.getParameter("TABLENAME");
				  }
				  if(whereCondition==null){
				      whereCondition=request.getParameter("WHERECONDITION");
				  }
				  
				  if(flag==null){
				      flag=request.getParameter("flag");
				  }
				  if(flag==null||flag.equals("")){
				      flag="read";
				  }
				  flag=flag.trim();
				  System.out.println("flag"+flag);
				  
				  if(title==null){
				      title=request.getParameter("title");
				  }
				  if(title==null||title.equals("")){
				      title="图片";
				  }
				  

				  int photoNo = ImageLoadServlet.findObjid(tableName,whereCondition);
	
	%>
    <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" valign="middle">
				<table width="530" height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" >
          <tr align="left">
            <td height="30" bgcolor="#4477AA">
						<img src="../images/form/xing1.jpg" align="absmiddle">
						<font size="2" color="#FFFFFF"><strong><%=title%></strong></font>
						<img src="../images/form/xing1.jpg" align="absmiddle"></td>
          </tr>
          <tr align="center">
            <td height="260" valign="middle">
              <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                <tr>
                  <td width="20">&nbsp;</td>
                  <td align="center" valign="middle">
				  
				  <%

				  
				  
				  
				  
				  if(photoNo<=0){
				  %>
				    <p>&nbsp;</p>
                    <p>还没有任何图片</p> 
					
				<%}else{
				    Object[] os = ImageLoadServlet.findPicByObjid(photoNo);
					if(os!=null){
					for(int i=0;i<os.length;i++){
				%>	
					<table height="225" border="0" cellpadding="0" cellspacing="1" class="list_form_table">
                    <tr>
                      <td bgcolor="#FFFFFF" class="list_form_td" width="300" height="225" align="center"><a href="/systemdb2image.jpg?objid=<%=photoNo%>&seqno=<%=os[i]%>"><img src="/systemdb2image.jpg?objid=<%=photoNo%>&seqno=<%=os[i]%>" border="0"></a></td>
					  <td width="37"><div align="left">
					    <p>&nbsp;</p>
					    <p>&nbsp;</p>
					    <p>&nbsp;</p>
					    <p>&nbsp;</p>
					    <p>&nbsp;</p>
					    <p>&nbsp;</p>
					    <p align="right"><%if(flag.equals("write")){%><a href="/photo/delete.jsp?objid=<%=photoNo%>&seqno=<%=os[i]%>" target="_blank">删除</a><%}%></p>
					  </div></td>
                    </tr>
                  </table>
				  <p>&nbsp;</p>
				 <%}}}%>
<p>

                    </p>
                  </td>
                  <td width="20">&nbsp;</td>
                </tr>
           	  </table>
			</td>
          </tr>
          <tr height="35" align="center" valign="middle">
			<td align="center">
			<%if(flag.equals("write")){%>
			<form action="/uploadservlet" method="post" enctype="multipart/form-data" name="form1">
  <input name="file" type="file" class="page_button_active"e>
  <input type="hidden" name="TABLENAME" value="<%=tableName%>">
  <input type="hidden" name="WHERECONDITION" value="<%=whereCondition%>">
  <input type="hidden" name="PHOTONO" value="<%=photoNo%>">
  <input type="hidden" name="title" value="<%=title%>">
  <input type="hidden" name="flag" value="<%=flag%>">
  <input name="b1" type="button" class="page_form_button_active" value="提交" onClick="form1.b1.disabled=true;form1.submit()"> 
   <input name="b2" type="button" class="page_form_button_active" value="刷新" onClick="form1.b2.disabled=true;form2.submit()">
			</form>
<form action="/photo/photo.jsp" method="post" name="form2">
  <input type="hidden" name="TABLENAME" value="<%=tableName%>">
  <input type="hidden" name="WHERECONDITION" value="<%=whereCondition%>">
  <input type="hidden" name="PHOTONO" value="<%=photoNo%>">
  <input type="hidden" name="title" value="<%=title%>">
  <input type="hidden" name="flag" value="<%=flag%>">
 </form>
 <%}%>
			
			</td>
          </tr>
        </table>
		</td>
      </tr>
    </table>    
  </body>
</html>

<% 

MyDB.getInstance().removeCurrentThreadConn("photo.jsp(END)"); //added by JGO on 2004-07-17

%>