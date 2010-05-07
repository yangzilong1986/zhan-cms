<!doctype html public "-//w3c/dtd HTML 4.0//en">
<html>
<!-- Copyright (c) 2003 by BEA Systems, Inc. All Rights Reserved.-->
<head>
<title>Session JSP, for replication of an HTTP session in memory using a cluster</title>
</head>

<body bgcolor="#FFFFFF">
<p><img src="images/BEA_Button_Final_web.gif" align=right>
<font face="Helvetica">

<h2>
<font color=#DB1260>
Cluster Replication of an HTTP Session in Memory
</font>
</h2>

<h2>
<font color=#DB1260>
Session JSP
</font>
</h2>

<%@ page import="
import javax.naming.Context;
import javax.naming.InitialContext;
import weblogic.management.MBeanHome;
import java.lang.System;
import java.io.Serializable;
import java.util.Properties;
import javax.naming.*;
" %>





<p>
This Cluster Sample Program uses a JSP to demonstrate
the use of replication of a session in memory, using a cluster-enabled proxy server. 

An end-user client adds or deletes named values to a session. <i>Server affinity</i> allows WebLogic Server to retrieve the same session the next time the client visits the page. 
<p>
Use this form to add some names and values to the session. Enter any names and values you choose.
You will want to record or remember what you entered, so that when you visit this page again you
can see replication of a session in memory at work.

<p>
The information you enter is being replicated to other servers in the cluster. If the managed server
processing your request should fail, another member of the cluster takes over. This failover is transparent to the
end-user client. 

</p>

<p>
  <%!
  String serverName;
  String failoverMessage="";

  private String getServerName() {

    String toReturn = null;
    try {
        Context myCtx = new InitialContext();
	MBeanHome mbeanHome = (MBeanHome)myCtx.lookup("weblogic.management.home.localhome");
	toReturn=mbeanHome.getMBeanServer().getServerName();
      if (toReturn == null) {
        return "";
      } else {
        return toReturn;
      }
    }
    catch (Exception e) {
      return "";
    }
  }
%>

  <%
  System.out.println("getting Servername");
  serverName = getServerName();
%>

The server currently hosting this session is <B><%= serverName %><B><%= failoverMessage %>

<%
  if (request.getParameter("AddValue") != null) {
    String nameField = request.getParameter("NameField");
    if (nameField != null && nameField.trim().length() > 0)
      session.setAttribute(addPrefix(nameField.trim()), 
    	              request.getParameter("ValueField"));
  } else if (request.getParameter("DeleteValue") != null) {
    session.removeAttribute(addPrefix(request.getParameter("NameField")));
  }
%>
</p>
<p>&nbsp; 
<%
Context ctx = new InitialContext();
Integer aNumber = null;
String msg = "";
try{
       aNumber = (Integer)ctx.lookup("testnumber");
	   aNumber=new Integer(aNumber.intValue()+1);
	   ctx.rebind("testnumber", aNumber);
}catch (NameNotFoundException e) {
    // 
	msg="binding does not exist";
	aNumber = new Integer(0);
	ctx.bind("testnumber", aNumber);	
}catch (NamingException e) {
    msg="a failure occurred";
 // a failure occurred
}
  
  



%>

<%=msg+":"+aNumber%>
</p>

<p>&nbsp; 
<%

Integer aNumber2=(Integer)application.getAttribute("testnumber2");
if(aNumber2==null){
    aNumber2 = new Integer(0);
	application.setAttribute("testnumber2",aNumber2);
}else{
    aNumber2 = new Integer(aNumber2.intValue()+1);
	application.setAttribute("testnumber2",aNumber2);
}
  
  



%>

<%=msg+":"+aNumber2%>
</p>


<p>
<center>
<table border=1 cellspacing=2 cellpadding=5 width=400 bgcolor=#EEEEEE>
<th colspan=2>Session<br>

</th>
<tr>
<td><B>Name</B></td>
<td><B>Value</B></td>
</tr>

<%
  Enumeration sessionNames = session.getAttributeNames();
  String name;
  while (sessionNames.hasMoreElements()) {
    name = (String)sessionNames.nextElement();    
    if (!name.startsWith(PREFIX_LABEL)) continue;
    if (removePrefix(name).length() < 1) continue;
%>

<tr>
<td><%= removePrefix(name) %></td>
<td><%= "" + session.getAttribute(name) %></td>
</tr>

<%
  } // end of while loop for session names
%>

</table>
</center>
<p>

<form method="post" name="Session" action="Session.jsp">

<center>
<table border=0 cellspacing=2 cellpadding=5 width=400>
<th>Name to add/delete</th>
<th>Value</th>
<tr>
<td><input type="text" name="NameField"></td>
<td><input type="text" name="ValueField"></td>
</tr>

<tr>
<td colspan=2 align=center><input type="submit" value=" Add " name="AddValue"></td>
</tr>
<tr>
<td colspan=2 align=center><input type="submit" value="Delete" name="DeleteValue"></td>
</tr>
</table>
</center>

</form>
<p>
<font size=-1>Copyright (c) 2003 BEA Systems, Inc. All rights reserved.
</font>

</font>
</body>
</html>

<%! 
static final String PREFIX_LABEL="SessionServlet.";

private String removePrefix(String name) {
  if (name.startsWith(PREFIX_LABEL))
    name = name.substring(PREFIX_LABEL.length());
  return name;
} 

private String addPrefix(String name) {
  if (!name.startsWith(PREFIX_LABEL))
    name = PREFIX_LABEL + name;
  return name;
}

%>

