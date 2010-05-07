<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*" %>
<%@ page import="weblogic.servlet.internal.WebAppServletContext" %>
<%@ page import="weblogic.management.runtime.ServletRuntimeMBean" %>
<%@ page import="weblogic.servlet.internal.session.SessionContext" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.platform.user.User" %>
<%@ page import="zt.platform.user.DatabaseAgent" %>
<%@ page import="zt.platform.user.VirtualUser" %>
 
<%
  List users = new ArrayList();
  try {
    WebAppServletContext ctx  = (WebAppServletContext)pageContext.getServletConfig().getServletContext();
    SessionContext       ctx1 =  ctx.getSessionContext();
    ctx1.deleteInvalidSessions();
    //ctx1.setTimeOut(2);
    Hashtable set = ctx1.getOpenSessions();
    for ( Enumeration  elements = set.elements() ;  elements.hasMoreElements() ; ) {
      HttpSession session1 = (HttpSession)elements.nextElement();
      UserManager um = (UserManager)session1.getAttribute(SystemAttributeNames.USER_INFO_NAME);
      if ( um != null ) {
        User user = um.getUser();
        try {
        	Map map = DatabaseAgent.getBrachAndUserName(user.getUserid());
        	String brhname  = (String)map.get("brhname");
       	 	String username = (String)map.get("username");
        	String userid   = user.getUsername(); 
        	VirtualUser vu = new VirtualUser(userid,username,brhname);
        	users.add(vu);
        } catch ( Exception e ) {
        	
        }
      }
    }
  } catch ( Exception ex ) { 
    
  }
%>
<html>
	<head>
		<title>在线用户列表</title>
	    <link href="css/platform.css" rel="stylesheet" type="text/css">
	</head>
	<body background="../images/checks_02.jpg">
    <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" valign="middle">
          <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" >
            <tr align="left">
              <td height="30" bgcolor="#4477AA"> <img src="../images/form/xing1.jpg" align="absmiddle"> <font size="2" color="#FFFFFF"><b>在线人数：(<%=users.size()%>)</b></font> <img src="../images/form/xing1.jpg" align="absmiddle"></td>
            </tr>
            <tr align="center">
              <td height="260" valign="middle">
                <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                  <tr>
                    <td width="20">&nbsp;</td>
                    <td valign="middle"><table cellspacing="1">
                        <tr>
                          <td>
                            <table width='496' border="0" cellpadding="0" cellspacing="1" class='list_form_table'>
                              <tr class='list_form_title_tr'>
                                <td width='166' class='list_form_title_td' nowrap>用户网点</td>
                                <td width='166' class='list_form_title_td' nowrap>用户名称</td>
                                <td width='164' class='list_form_title_td' nowrap>登录标识</td>
                              </tr>
                              <%                                                
                                                for ( int i = 0 ; i < users.size() ; i++ ) {
                                                    VirtualUser vu = (VirtualUser)users.get(i);
%>
                              <tr class='list_form_tr'>
                                <td class='list_form_td'><%=vu.getBrhname()%></td>
                                <td class='list_form_td'><%=vu.getUsername()%></td>
                                <td class='list_form_td'><%=vu.getLoginid()%></td>
                              </tr>
                              <%
                                                }
                                                for ( int i = users.size() ; i < 10 ; i ++ ) {
%>
                              <tr class='list_form_tr'>
                                <td class='list_form_td'>&nbsp;</td>
                                <td class='list_form_td'>&nbsp;</td>
                                <td class='list_form_td'>&nbsp;</td>
                              </tr>
                              <%
                                                }
%>
                          </table></td>
                        </tr>
                    </table></td>
                    <td width="20">&nbsp;</td>
                  </tr>
              </table></td>
            </tr>
            <tr height="35" align="center" valign="middle">
              <td>&nbsp;</td>
            </tr>
        </table></td>
      </tr>
    </table>
	</body>
</html>
