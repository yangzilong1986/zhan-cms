<%@ page contentType="text/html; charset=GBK" errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.cmsi.fc.DBUtil,zt.cms.pub.SCBranch,zt.cmsi.fc.FcUpXML" %>
<%--
=============================================== 
Title: ����xml
Description: ����xml��
 * @version  $Revision: 1.1 $  $Date: 2007/05/31 02:29:40 $
 * @author   weiyb
 * <p/>�޸ģ�$Author: weiyb $
=============================================== 
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> New Document </TITLE>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
</HEAD>

<BODY>
<form name="winform" method="post"></form>
</BODY>
</HTML>
<%
	FcUpXML upxml= new FcUpXML();
	//��¼�û�Ϊ�������û�
    String brhid =request.getParameter("brhid");
    String enddate=request.getParameter("fljzrq");
    String jgdm=request.getParameter("jgdm").trim();
    String msg=upxml.verifyData(brhid,jgdm,enddate);
    if (!msg.equals(""))
    {
    	msg+="ȷ��Ҫ������?";
    	out.write("<script language='javascript'>");
    	out.write("if (confirm('"+msg+"'))");
    	out.write("{");
    	out.write("document.forms[0].action='downloadXML.jsp?brhid="+brhid+"&fljzrq="+enddate+"&jgdm="+jgdm+"';");
    	out.write("document.forms[0].submit();");
    	out.write("}");
    	out.write("</script>");
    }
    else
    {
        out.write("<script language='javascript'>");
    	out.write("document.forms[0].action='downloadXML.jsp?brhid="+brhid+"&fljzrq="+enddate+"&jgdm="+jgdm+"';");
    	out.write("document.forms[0].submit();");
    	out.write("</script>");
    
    }
%>
