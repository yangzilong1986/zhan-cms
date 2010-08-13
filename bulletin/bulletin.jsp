<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cms.bm.bulletin.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>
 
<%
MyDB.getInstance().removeCurrentThreadConn("bulletin.jsp"); //added by JGO on 2004-07-17
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>无标题文档</title>

<link href="bulletin.css" rel="stylesheet" type="text/css">
<link href="../css/platform.css" rel="stylesheet" type="text/css">

<script language="JavaScript1.1">
var popUpWin=0;
function popUpWindow(URLStr, left, top, width, height)
{
  if(popUpWin)
  {
    if(!popUpWin.closed) popUpWin.close();
  }
  popUpWin = open(URLStr, 'popUpWin', 'toolbar=no,location=no,directories=no,status=no,menub ar=no,scrollbar=no,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
}

</script>

</head>

<body background="../images/bks.gif">
<table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="middle"><table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr background="images/checks_02.jpg">
        <td width="562"><table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
            <!--DWLayoutTable-->
            <tr>
              <td width="124" rowspan="2" align="left" valign="top" background="images/tiao_001.jpg"><img src="images/gonggao.jpg" width="124" height="93"></td>
              <td width="364" height="40" valign="top" bgcolor="E6E7E9" class="linebg"><!--DWLayoutEmptyCell-->&nbsp;</td>
              <td width="42" rowspan="2" align="left" valign="top" background="images/tiao_003.jpg"><img src="images/jiao_001.jpg" width="41" height="93"></td>
            </tr>
            <tr>
              <td height="62" bgcolor="E6E7E9"><div align="left">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <%
              Collection bulletins = BulletinFactory.findTop5Bulletins();
              if(bulletins != null)
              {
              for (Iterator iter = bulletins.iterator(); iter.hasNext(); ) {
                  Bulletin item = (Bulletin)iter.next();

              %>
                      <tr class="listcontent">
                        <td width="73" height="14" nowrap><div align="center"><%=item.getCreateDateString()%></div></td>
                        <td width="187" nowrap>
						<%
						   if(!item.isLink()){
						%>
						<a  target="_blank" href="bulletin_detail.jsp?seqno=<%=item.getSeqno()%>"><%=item.getTrimedCaption()%></a>
						<%
						    }else{
							%>
							<a target="_blank" href="<%=item.getURL()%>"><%=item.getTrimedCaption()%></a>
							<%
							}
						
						%>
						</td>
                        <td width="104" nowrap><%=item.getFullName()%></td>
                      </tr>
                      <%}}%>
                    </table>
              </div></td>
            </tr>
            <tr>
              <td height="24" background="images/tiao_001.jpg">&nbsp;</td>
              <td align="right" valign="top" bgcolor="E6E7E9"><input name="Submit" type="button" class="page_button_active" value="更多" onClick="location='bulletin_list.jsp?p=1&type=1'"></td>
              <td background="images/tiao_003.jpg"><!--DWLayoutEmptyCell-->&nbsp;</td>
            </tr>
            <tr>
              <td rowspan="2" align="left" valign="top" background="images/tiao_001.jpg"><img src="images/falv.jpg" width="124" height="85"></td>
              <td height="32" valign="top" bgcolor="E6E7E9" class="linebg2"><!--DWLayoutEmptyCell-->&nbsp; </td>
              <td width="42" rowspan="2" align="right" valign="top" background="images/tiao_003.jpg"><img src="images/jiao_002.jpg" width="42" height="85"></td>
            </tr>
            <tr>
              <td height="66" valign="top" bgcolor="E6E7E9" nowrap><div align="left">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <%
              Collection rules = BulletinFactory.findTop5Rules();
              if(rules != null)
              {              
              for (Iterator iter = rules.iterator(); iter.hasNext(); ) {
                  Bulletin item = (Bulletin)iter.next();

              %>
                      <tr class="listcontent">
                        <td width="75" height="14"><div align="center"><%=item.getCreateDateString()%></div></td>
                        <td width="192">
						<%
						   if(!item.isLink()){
						%>
						<a  target="_blank" href="bulletin_detail.jsp?seqno=<%=item.getSeqno()%>"><%=item.getTrimedCaption()%></a>
						<%
						    }else{
							%>
							<a target="_blank" href="<%=item.getURL()%>"><%=item.getTrimedCaption()%></a>
							<%
							}
						
						%>
</td>
                        <td width="97" nowrap><%=item.getFullName()%></td>
                      </tr>
                      <%}}%>
                    </table>
              </div></td>
            </tr>
            <tr>
              <td height="23" align="right" valign="top" background="images/tiao_001.jpg" bgcolor="E6E7E9">&nbsp;</td>
              <td align="right" valign="top" bgcolor="E6E7E9"><input name="Submit2" type="button" class="page_button_active" value="更多"  onClick="location='bulletin_list.jsp?p=1&type=2'"></td>
              <td background="images/tiao_003.jpg"><!--DWLayoutEmptyCell-->&nbsp;</td>
            </tr>
            <tr>
              <td rowspan="2" align="left" valign="top" background="images/tiao_001.jpg"><img src="images/duanxiaoxi.jpg" width="124" height="85"></td>
              <td height="30" valign="top" bgcolor="E6E7E9" class="linebg2"><!--DWLayoutEmptyCell-->&nbsp; </td>
              <td width="42" rowspan="2" align="right" valign="top" background="images/tiao_003.jpg"><img src="images/jiao_002.jpg" width="42" height="85"></td>
            </tr>
            <tr>
              <td height="58" valign="top" bgcolor="E6E7E9"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr class="listcontent">
                    <%String user=SessionInfo.getLoginUserName(session);%>
                    <td height="14" colspan="3"><div align="right">你现有（<%=MsgFactory.getMyMsgsNumber(user)%>）条短信息，<span class="style2">（<%=MsgFactory.getMyUnviewedMsgsNumber(user)%>）</span>条未读</div></td>
                  </tr>
                  <%

              Collection msgs = MsgFactory.findTop5Msgs(user);
              if(msgs != null)
              {              
              for (Iterator iter = msgs.iterator(); iter.hasNext(); ) {
                  Msg item = (Msg)iter.next();

              %>
                  <tr class="listcontent">
                    <td width="80" height="14" nowrap><div align="center"><%=item.getCreateDateString()%></div></td>
                    <td width="196" nowrap><div align="left"><a target="_blank" href="msg_detail.jsp?msgno=<%=item.getMsgNo()%>" class="<%=item.isReadFlag()?"":"unviewed2"%>"><%=item.getTrimedContent()%></a></div></td>
                    <td width="88" nowrap><%=item.getFullName()%></td>
                  </tr>
                  <%}}%>
              </table></td>
            </tr>
            <tr>
              <td height="23" align="right" valign="top" background="images/tiao_001.jpg" bgcolor="E6E7E9">&nbsp;</td>
              <td align="right" valign="top" bgcolor="E6E7E9"><input name="Submit32" type="button" class="page_button_active" value="发送"  onClick="popUpWindow('/templates/defaultform.jsp?Plat_Form_Request_Form_ID=GLMSGPAGE&Plat_Form_Request_Event_ID=0&flag=write',50,50,600,400)">
                  <input name="Submit3" type="button" class="page_button_active" value="更多"  onClick="location='msg_list.jsp?p=1&type=1'">
              </td>
              <td background="images/tiao_003.jpg"><!--DWLayoutEmptyCell-->&nbsp;</td>
            </tr>
            <tr>
              <td height="37"><img src="images/jiao_004.jpg" width="124" height="37"></td>
              <td background="images/tiao_005.jpg">&nbsp;</td>
              <td><img src="images/jiao_005.jpg" width="41" height="37"></td>
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
