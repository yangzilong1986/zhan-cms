<%@page  errorPage="error.jsp" %>
<%@page import="java.io.*"%>
<%@page import="zt.cmsi.fc.FcUpXML,zt.platform.db.DBUtil" %>

<%
  try
  {
    //byte data[] = (byte[])request.getAttribute("excelData");
    //String fileName = (String)request.getAttribute("fileName");
    String brhid =request.getParameter("brhid");
    String enddate=request.getParameter("fljzrq");
    String jgdm=request.getParameter("jgdm").trim();
    FcUpXML xml=new FcUpXML();
    xml.impUPxlsxx(brhid,jgdm,enddate);
    String fileName=jgdm+".xml";
    response.reset();
    response.setContentType("APPLICATION/OCTET-STREAM");
    response.setHeader("Content-Disposition","attachment; attachment; filename="+ fileName);

    ServletOutputStream os = response.getOutputStream();

    xml.exportXML(os,jgdm,enddate);
    //os.write(data);
    //os.flush();
    //os.close();
 }
 catch(Exception e)
 {
   throw e;
 }
%>