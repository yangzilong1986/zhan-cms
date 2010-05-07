<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cms.bm.bulletin.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cms.bm.common.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<%
String pStr=request.getParameter("p");
int p=1;
int number=10;

if(pStr!=null&&!pStr.equals("")){
    p=Integer.parseInt(pStr);
}
//System.out.println("p=="+p+"number=="+number);

String user=SessionInfo.getLoginUserName(session);
Collection msgs  = MsgFactory.findAllMyMsgs(user,p,number);

%>



<title>无标题文档</title>
<link href="bulletin.css" rel="stylesheet" type="text/css">
<link href="../css/platform.css" rel="stylesheet" type="text/css">
</head>

<body>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle"><table width="587" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr background="images/checks_02.jpg">
        <td width="15">&nbsp;</td>
        <td width="631"><table width="580" border="0" cellspacing="0" cellpadding="0">
            <!--DWLayoutTable-->
            <tr>
              <td width="128" height="98"><img src="images/duanxiaoxi_001.jpg" width="128" height="98"></td>
              <td width="411" valign="top" bgcolor="E6E7E9">
                <table width="411" border="0" cellspacing="0" cellpadding="0">
              <td width="411" height="47" colspan="2" background="images/tiao_009.jpg"> 
              </tr>
              <tr>
                <td align="center">&nbsp;</td>
              </tr>
              </table></td>
              <td width="38"><img src="images/jiao_007.jpg" width="41" height="98"></td>
            </tr>
            <tr>
              <td width="128" height="146" background="images/tiao_006.jpg">&nbsp;</td>
              <td valign="top" bgcolor="E6E7E9"><table width="404" border="0" cellspacing="0" cellpadding="0">
                  <!--DWLayoutTable-->
                  <tr class="listcaption">
                    <td width="94"><div align="center">发布日期</div></td>
                    <td width="257"><div align="left">内容</div></td>
                    <td width="53"><div align="left">发送人</div></td>
                    <td width="53"><!--DWLayoutEmptyCell-->&nbsp;</td>
                  </tr>
                  <%
              for (Iterator iter = msgs.iterator(); iter.hasNext(); ) {
                  Msg item = (Msg)iter.next();

              %>
                  <tr class="listcontent">
                    <td width="94" height="14"><div align="center"><%=item.getCreateDateString()%></div></td>
                    <td width="257"><div align="left"><a target="_blank" href="msg_detail.jsp?msgno=<%=item.getMsgNo()%>"class="<%=item.isReadFlag()?"":"unviewed2"%>"><%=item.getTrimedContent()%></a></div></td>
                    <td width="53"><%=item.getFromUser()%></td>
                    <td width="53"><a href="#" onClick="if(confirm('确定删除这条信息')){location='delete.jsp?msgno=<%=item.getMsgNo()%>'}else{};">删除</a></td>
                  </tr>
                  <%}%>
                  <tr>
                    <td height="17" colspan="4"><div align="right">
                        <%if(p!=1){%>
                        <a href="bulletin_list.jsp?p=<%=p-1%>&type=" class="listcontent">上一页</a>
                        <%}%>
                        <%if(msgs.size()==number){%>
                        <a href="bulletin_list.jsp?p=<%=p+1%>&type=" class="listcontent">下一页</a>
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
      <tr background="images/checks_02.jpg">
        <td width="15">&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table></td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
