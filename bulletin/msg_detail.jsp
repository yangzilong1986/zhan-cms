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
<%
MsgFactory.setViewed(Integer.parseInt(request.getParameter("msgno")));
Msg msg = MsgFactory.findMsgByNo(Integer.parseInt(request.getParameter("msgno")));

RequestDispatcher rd=null;
if(msg==null){
    request.setAttribute("msg","记录不存在，可能已删除");	
    rd=request.getRequestDispatcher("/showinfo.jsp");
    rd.forward(request,response);
}else{


%>
<title>短消息</title>

<link href="bulletin.css" rel="stylesheet" type="text/css">
<link href="../css/platform.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style1 {font-size: 9px}
-->
</style>
</head>

<body>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle"><table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr background="images/checks_02.jpg">
        <td width="15">&nbsp;</td>
        <td width="569"><table width="569" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="128"><img src="images/duanxiaoxi_001.jpg" width="128" height="98"></td>
              <td width="400" valign="top" bgcolor="E6E7E9">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <td height="52" colspan="2" background="images/tiao_009.jpg"> 
              </tr>
              <tr>
                <td align="center"></td>
              </tr>
              <tr>
                <td align="center">
                  <table width="300" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td class="detail_title"><%=msg.getCreateDateString()%>来自的<%=msg.getFromUser()%>短消息</td>
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
              <td height="80" background="images/tiao_006.jpg">&nbsp;</td>
              <td valign="top" bgcolor="E6E7E9" class="detailcontent">　　
                <div align="left" class="listcontent"><%=msg.getFilteredContent()%></div></td>
              <td valign="bottom" background="images/tiao_007.jpg">&nbsp;</td>
            </tr>
            <tr>
              <td height="24" background="images/tiao_006.jpg">&nbsp;</td>
              <td valign="top" bgcolor="E6E7E9" class="detailcontent"><div align="center"><span class="style1"><a href="#" class="listcontent" onClick="if(confirm('确定删除这条信息')){location='delete.jsp?msgno=<%=msg.getMsgNo()%>'}else{};">删除</a> <a href="#" class="listcontent" onClick="parent.close();">关闭</a> <a href="#" class="listcontent" onClick="location='/templates/defaultform.jsp?Plat_Form_Request_Form_ID=GLMSGPAGE&Plat_Form_Request_Event_ID=0&flag=write&TOUSER=<%=msg.getFromUser()%>'">回复</a></span></div></td>
              <td valign="bottom" background="images/tiao_007.jpg">&nbsp;</td>
            </tr>
            <tr>
              <td width="128"><img src="images/jiao_006.jpg" width="128" height="48"></td>
              <td background="images/tiao_008.jpg">&nbsp;</td>
              <td width="41"><img src="images/jiao_008.jpg" width="41" height="48"></td>
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
<%}%>
