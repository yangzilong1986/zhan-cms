<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cms.cm.report.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="zt.platform.form.util.*" %>

<%

Report report = ReportType.getReportTemplateByType(request.getParameter("reportType").trim());
request.setCharacterEncoding("GB2312");
report.setReportName(request.getParameter("reportName"));
report.setEntityNo(request.getParameter("entityNo"));
Item[] items=report.getPrintItems();

String theDate=request.getParameter("reportDate");
//Calendar c=Calendar.getInstance();
//String[] d=theDate.split("-");
//c.set(Integer.parseInt(d[0]),Integer.parseInt(d[1])-1,Integer.parseInt(d[2]));
report.setReportDate(theDate);

for(int i=0;i<items.length;i++){
    //System.out.println(request.getParameter(""+items[i].getItemNo()));
    items[i].setItemValue(Double.parseDouble(request.getParameter("r"+items[i].getItemNo())));
}
boolean actualReturn = ReportStorer.storeReport(report);
//System.out.println(actualReturn);
if(actualReturn){
    request.setAttribute("msg","添加报表成功");
}else{
    request.setAttribute("msg","添加报表失败,可能已经存在这个日期的相同类型报表");
}
//response.addHeader("Plat_Form_Request_Form_ID","CMREDL");
//response.addHeader("Plat_Form_Request_Event_ID","0");
//response.addHeader("CLIENTNO",request.getParameter("entityNo"));
      
      //request.setAttribute(SessionAttributes.BACKGROUND_DISPATCH,"111");	  
	  //request.setAttribute("msg","增加成功");
      RequestDispatcher rd=request.getRequestDispatcher("/showinfo.jsp");
      rd.forward(request,response);
//

//response.sendRedirect("/templates/defaultform.jsp");

//response.sendRedirect("/report/result.jsp");
%>