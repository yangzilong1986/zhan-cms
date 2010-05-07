<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.report.TableBuilder,
                 zt.cmsi.pub.define.SystemDate" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="com.ming.webreport.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>

<%
    String strErrMsg=null;                        //�������
    String strRptNo=request.getParameter("rptno");//��ñ�����
    TableBuilder tbd=new TableBuilder(request);
    RequestDispatcher rd=null;

    strErrMsg=tbd.getErrMsg();
    if(strErrMsg!=null){
        request.setAttribute("msg",strErrMsg);
        rd=request.getRequestDispatcher("/showinfo.jsp");
        rd.forward(request,response);
    }

    String[][] brhArr=tbd.getArray();
    strErrMsg=tbd.getErrMsg();
    if(strErrMsg!=null){
        request.setAttribute("msg",strErrMsg);
        rd=request.getRequestDispatcher("/showinfo.jsp");
        rd.forward(request,response);
    }

    String scbrhName=tbd.getScbranchName();
    String date=request.getParameter("date");
    if(date==null)date=SystemDate.getYesterday("-");
    String tenThou=tbd.getTenThousand();
%>
<html>
<head>
<title>����</title>
<SCRIPT src="../setup/meizzDate.js" type=text/javascript></SCRIPT>
<SCRIPT src="../setup/check.js" type=text/javascript></SCRIPT>
</head>
<body>
<form name="report" method="post" action="FCreport.jsp" onsubmit="return sub()">
  <table width="100%"  border="0">
    <tr>
      <td width="10%" height="20"><input type="hidden" name="rptno" value="<%=strRptNo%>"></td>
      <td width="30%" height="25">�¼�����:
        <select name="brhid" size="1">
      <%
      if(brhArr!=null)
          for(int i=0;i<brhArr.length;i++){

             out.print("<option value='"+brhArr[i][0]+"'>");
             out.print(brhArr[i][1]);
             out.print("</option>");
          }
      %>
        </select>
      </td>
      <td width="40%">�ʱ��:
        <input class='input' type=text name="date" size=10 value="<%=date%>">
        <input type='button' value='��' class='button' onclick='setday(this,document.getElementById("date"))'></td>
      <td width="20%"><input type="submit" name="Submit" value="��ѯ"></td>
    </tr>
  <tr height="95%" >
    <td valign="top" colspan="4">
     <%
       String str=tbd.showReport("MRViewer");
       if(str==null){
          strErrMsg=tbd.getErrMsg();
          if(strErrMsg!=null){
             request.setAttribute("msg",strErrMsg);
          }else{
            request.setAttribute("msg","��������Ԥ֪����");
          }
             rd=request.getRequestDispatcher("/showinfo.jsp");
             rd.forward(request,response);
      }else
          out.print(str);
     %>
    </td>
  </tr>
  </table>
</form>
</body>
</html>
<script language=javascript>
function sub(){
    var brhname=document.all.brhid;
    var date=document.all.date;

    if(brhname.value==null){
       alert("��ѡ�����㣡");
       brhname.focus();
       return false;
    }
    //if(brhname.value.substring(7,9)!="99")
    //{
    //   alert("�����㲻�����¼����㣡");
    //   brhname.focus();
    //   return false;
    //}
    if(date.value==null){
      alert("��ѡ�����ڣ�");
      date.focus();
      return false;
   }
   if (!isDate(date.value)) {
      alert("���ڸ�ʽ����");
      date.focus();
      return false;
   }
}
</script>
<script language="JavaScript">
var date= '<%=date%>';
var tenTh='<%=tenThou%>';
var name='<%=scbrhName%>';
var obj = document.getElementById('MRViewer');
 obj.setVariable('date',date);
 obj.setVariable('tenTh',tenTh);
 obj.setVariable('name',name);
 obj.preview();
</script>

<% 
MyDB.getInstance().removeCurrentThreadConn("FCreport.jsp"); //added by JGO on 2004-07-17
%>