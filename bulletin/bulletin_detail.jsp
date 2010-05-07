<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cms.bm.bulletin.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.db.DBUtil" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<%Bulletin bulletin = BulletinFactory.findBulletinBySeqno(Integer.parseInt(request.getParameter("seqno")));

RequestDispatcher rd=null;
if(bulletin==null){
    request.setAttribute("msg","记录不存在，可能已删除");	
    rd=request.getRequestDispatcher("/showinfo.jsp");
    rd.forward(request,response);
}else{

%>
<title><%=bulletin.isRule()?"法律法规":"公告板"%></title>

<link href="bulletin.css" rel="stylesheet" type="text/css">
<link href="../css/platform.css" rel="stylesheet" type="text/css">
</head>

<body>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle"><table width="530" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="128"><img src="images/<%=bulletin.isRule()?"falv_001.jpg":"gonggao_001.jpg"%>" width="128" height="98"></td>
        <td width="413" valign="top" bgcolor="E6E7E9">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <td height="52" colspan="2" background="images/tiao_009.jpg"> 
        </tr>
        <tr>
          <td align="center" class="detail_title"><strong><%=bulletin.getCaption()%></strong></td>
        </tr>
        <tr>
          <td align="center">
            <table width="300" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td background="images/dian.gif"><img src="images/kongbai.gif" width="1" height="1"></td>
              </tr>
          </table></td>
        </tr>
        </table></td>
        <td width="41"><img src="images/jiao_007.jpg" width="41" height="98"></td>
      </tr>
      <tr>
        <td width="128" background="images/tiao_006.jpg">&nbsp;</td>
        <td valign="top" bgcolor="E6E7E9" class="listcontent"><div align="left"><span class="unnamed1"><br><%=bulletin.getFilteredContent()%></span></div></td>
        <td width="41" background="images/tiao_007.jpg">&nbsp;</td>
      </tr>
      <tr>
        <td width="128"><img src="images/jiao_006.jpg" width="128" height="48"></td>
        <td background="images/tiao_008.jpg">&nbsp;</td>
        <td width="41"><img src="images/jiao_008.jpg" width="41" height="48"></td>
      </tr>
    </table></td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
<%}%>
