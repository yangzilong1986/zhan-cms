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
String typeStr=request.getParameter("type");
String pStr=request.getParameter("p");
int p=1;
int type=1;
int number=10;
if(typeStr!=null&&!typeStr.equals("")){
    type=Integer.parseInt(typeStr);
}
if(pStr!=null&&!pStr.equals("")){
    p=Integer.parseInt(pStr);
}
//System.out.println("p=="+p+"type=="+type+"number=="+number);


Collection bulletins  = BulletinFactory.findBulletinsByPageAndNumber(p,number,type);

%>



<title>无标题文档</title>
<link href="bulletin.css" rel="stylesheet" type="text/css">
<link href="../css/platform.css" rel="stylesheet" type="text/css">
</head>

<body>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle"><table width="587" border="0" cellspacing="0" cellpadding="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="128" height="98"><img src="images/<%=(type==2)?"falv_001.jpg":"gonggao_001.jpg"%>" width="128" height="98"></td>
        <td width="418" valign="top" bgcolor="E6E7E9">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <td width="426" height="47" colspan="2" background="images/tiao_009.jpg"> 
        </tr>
        <tr>
          <td align="center">&nbsp;</td>
        </tr>
        </table></td>
        <td width="41"><img src="images/jiao_007.jpg" width="41" height="98"></td>
      </tr>
      <tr>
        <td width="128" height="146" background="images/tiao_006.jpg">&nbsp;</td>
        <td valign="top" bgcolor="E6E7E9"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <!--DWLayoutTable-->
            <tr class="listcaption">
              <td width="94"><div align="center">发布日期</div></td>
              <td width="276"><div align="left">标题</div></td>
              <td width="48"><div align="left">发布人</div></td>
            </tr>
            <%
              for (Iterator iter = bulletins.iterator(); iter.hasNext(); ) {
                  Bulletin item = (Bulletin)iter.next();

              %>
            <tr class="listcontent">
              <td width="94" height="14"><div align="center"><%=item.getCreateDateString()%></div></td>
                        <td width="273">
						  <div align="left">
					        <%
						   if(!item.isLink()){
						%>
						      <a target="_blank" href="bulletin_detail.jsp?seqno=<%=item.getSeqno()%>"><%=item.getTrimedCaption()%></a>
					        <%
						    }else{
							%>
							  <a target="_blank" href="<%=item.getURL()%>"><%=item.getTrimedCaption()%></a>
							  <%
							}
						
						%>
						    </div></td>
              <td width="48"><%=item.getPublishedBy()%></td>
            </tr>
            <%}%>
            <tr>
              <td height="17" colspan="3"><div align="right">
                  <%if(p!=1){%>
                  <a href="bulletin_list.jsp?p=<%=p-1%>&type=<%=type%>" class="listcontent">上一页</a>
                  <%}%>
                  <%if(bulletins.size()==number){%>
                  <a href="bulletin_list.jsp?p=<%=p+1%>&type=<%=type%>" class="listcontent">下一页</a>
                  <%}%>
              </div></td>
            </tr>
        </table></td>
        <td width="41" background="images/tiao_007.jpg">&nbsp;</td>
      </tr>
      <tr>
        <td width="128" height="48"><img src="images/jiao_006.jpg" width="128" height="48"></td>
        <td background="images/tiao_008.jpg">&nbsp;</td>
        <td width="41"><img src="images/jiao_008.jpg" width="41" height="48"></td>
      </tr>
    </table></td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
