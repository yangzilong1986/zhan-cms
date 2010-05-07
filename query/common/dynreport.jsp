<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.report.dyn.ReportBuilder" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="com.ming.webreport.*" %>

<%
   String strErrMsg=null;                        //错误代码
   String strRptNo=request.getParameter("rptno");//获得报表编号
   ReportBuilder rbd=new ReportBuilder(request);
   RequestDispatcher rd=null;

   strErrMsg=rbd.getErrMsg();
   if(strErrMsg!=null)
  {
     request.setAttribute("msg",strErrMsg);
     rd=request.getRequestDispatcher("/showinfo.jsp");
     rd.forward(request,response);
  }
  
  rbd.setDefine();
  strErrMsg=rbd.getErrMsg();
   if(strErrMsg!=null)
  {
     request.setAttribute("msg",strErrMsg);
     rd=request.getRequestDispatcher("/showinfo.jsp");
     rd.forward(request,response);
  }

  int totalMemo=rbd.intColNum;
  String reportname=rbd.strRepName;
  String colName=rbd.getColName();
  String colLen=rbd.getColLen();
  String tLeft=rbd.getTitleLeft();

  //System.out.println("totalMemo:"+totalMemo);
  //System.out.println("reportname:"+reportname);
  //System.out.println("colName:"+colName);
  //System.out.println("colLen:"+colLen);
  //System.out.println("tLeft:"+tLeft);
  
  String[][] brhArr=rbd.getArry();
  strErrMsg=rbd.getErrMsg();
  if(strErrMsg!=null)
  {
    request.setAttribute("msg",strErrMsg);
    rd=request.getRequestDispatcher("/showinfo.jsp");
    rd.forward(request,response);
  }

  String scbrhName=rbd.getScbranchName();
  String year=rbd.getYear();
  String month=rbd.getMonth();
  
%>
<html>
<head>
<title>报表</title>
</head>

<body>
<form name="report" method="post" action="dynreport.jsp" onsubmit="return sub()">
  <table width="100%"  border="0">
    <tr>
      <td width="19%" height="20"><input type="hidden" name="rptno" value="<%=strRptNo%>"></td>
      <td width="31%" height="25">下级网点:
        <select name="brhid" size="1">
      <%
      if(brhArr!=null)
          for(int i=0;i<brhArr.length;i++)
          {
             out.print("<option value='"+brhArr[i][0]+"'>");
             out.print(brhArr[i][1]);
             out.print("</option>");
          }
      %>
        </select>
      </td>
      <td width="20%">月:
        <select name="month">
          <option value="12">12</option>
          <option value="01">01</option>
          <option value="02">02</option>
          <option value="03">03</option>
          <option value="04">04</option>
          <option value="05">05</option>
          <option value="06">06</option>
          <option value="07">07</option>
          <option value="08">08</option>
          <option value="09">09</option>
          <option value="10">10</option>
          <option value="11">11</option>

        </select>
        </td>
      <td width="35%"><input type="submit" name="Submit" value="查询"></td>
    </tr>
  <tr height="95%" >
    <td valign="top" colspan="4">

     <%
       //String str="111";
	   String str=rbd.showReport("MRViewer");
       if(str==null)
      {
          strErrMsg=rbd.getErrMsg();
          if(strErrMsg!=null)
          {
             request.setAttribute("msg",strErrMsg);
          }
          else
          {
            request.setAttribute("msg","发生不可预知错误！");
          }
             rd=request.getRequestDispatcher("/showinfo.jsp");
             rd.forward(request,response);

      }
       else
          out.print(str);
     %>

    </td>

  </tr>
  </table>
</form>
</body>
</html>
<script language=javascript>
function sub()
{
    var brhname=document.all.brhid;
    var month=document.all.month;

    if(brhname.value==null)
    {
       alert("请选择网点！");
       brhname.focus();
       return false;
    }
    if(month.value==null)
   {
      alert("请选择月份！");
      month.focus();
      return false;
   }
}
</script>
<script language="JavaScript">
var year= '<%=year%>';
var month='<%=month%>';

var name='<%=scbrhName%>';
var totalMemo='<%=totalMemo%>';
var reportname='<%=reportname%>';
var colName='<%=colName%>';
var colLen='<%=colLen%>';
var tLeft='<%=tLeft%>';

var obj = document.getElementById('MRViewer');
 obj.setVariable('year',year);
 obj.setVariable('month',month);

 obj.setVariable('name',name);
 obj.setVariable('totalMemo',totalMemo);
 obj.setVariable('reportname',reportname);
 obj.setVariable('colName',colName);
 obj.setVariable('colLen',colLen);
 obj.setVariable('tLeft',tLeft);
 
 obj.preview();
</script>

