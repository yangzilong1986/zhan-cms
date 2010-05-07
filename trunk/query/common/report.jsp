<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.report.TableBuilder" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="com.ming.webreport.*" %>

<%@ page import="zt.cmsi.mydb.MyDB" %>

<%
   String strErrMsg=null;                        //错误代码
   String strRptNo=request.getParameter("rptno");//获得报表编号
   TableBuilder tbd=new TableBuilder(request);
   RequestDispatcher rd=null;

   strErrMsg=tbd.getErrMsg();
   if(strErrMsg!=null)
  {
     request.setAttribute("msg",strErrMsg);
     rd=request.getRequestDispatcher("/showinfo.jsp");
     rd.forward(request,response);
  }

   String[][] brhArr=tbd.getArray();
   strErrMsg=tbd.getErrMsg();
   if(strErrMsg!=null)
   {
     request.setAttribute("msg",strErrMsg);
     rd=request.getRequestDispatcher("/showinfo.jsp");
     rd.forward(request,response);
   }

   String scbrhName=tbd.getScbranchName();
   String year=tbd.getYear();
   String month=tbd.getMonth();
   String tenThou=tbd.getTenThousand();
%>
<html>
<head>
<title>报表</title>
</head>

<body>
<form name="report" method="post" action="report.jsp" onsubmit="return sub()">
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
          <option value="00">00</option>]
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
       String str=tbd.showReport("MRViewer");
       if(str==null)
      {
          strErrMsg=tbd.getErrMsg();
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
    //if(brhname.value.substring(7,9)!="99")
    //{
    //   alert("该网点不存在下级网点！");
    //   brhname.focus();
    //   return false;
    //}
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
var tenTh='<%=tenThou%>';
var name='<%=scbrhName%>';
var obj = document.getElementById('MRViewer');
 obj.setVariable('year',year);
 obj.setVariable('month',month);
 obj.setVariable('tenTh',tenTh);
 obj.setVariable('name',name);
 obj.preview();
</script>

<% 
MyDB.getInstance().removeCurrentThreadConn("report.jsp"); //added by JGO on 2004-07-17
%>