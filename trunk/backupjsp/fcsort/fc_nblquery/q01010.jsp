<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="zt.cms.report.SinLoanAppData" %>
<%@ page import="zt.cms.report.SinLoanApprove" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.StringBuffer"%>

<%--
===============================================
Title: �������������ѯ��ϸ��Ϣҳ��
Description: �������������ѯ��Ϣҳ��
 * @version   $Revision: 1.1 $  $Date: 2007/05/09 09:31:08 $
 * @author   houcs
 * <p/>�޸ģ�$Author: houcs $
===============================================
--%>
<html>
<head>
<title>�������������</title>
<link href="/css/platform.css" rel="stylesheet" type="text/css">
</head>
<%
     String strBmNo=null;
     RequestDispatcher rd=null;

     strBmNo=request.getParameter("BMNO");
	 if(strBmNo==null)
	   strBmNo=request.getParameter("bmno");

     //System.out.println(strBmNo);
	 if(strBmNo==null)
     {
        request.setAttribute("msg","��ҵ��ţ�");
        rd=request.getRequestDispatcher("/showinfo.jsp");
        rd.forward(request,response);
     }

     ArrayList arrlst=new ArrayList();
     StringBuffer strBuf=null;

     SinLoanApprove sla=new SinLoanApprove();
     SinLoanAppData slad=sla.getSinLoanApp(strBmNo);
	 String strErrMsg=sla.getErrMsg();
	 if(strErrMsg!=null)
     {
        request.setAttribute("msg",strErrMsg);
        rd=request.getRequestDispatcher("/showinfo.jsp");
        rd.forward(request,response);
     }

%>
<body background="/images/checks_02.jpg">
<table width="100%" height="10" border="0" >
<tr >
<td align="center">
  <span class="style1">�������������</span>
</td>
</tr>
</table>
<table width="100%" height="200" border="0" cellpadding="0" cellspacing="0" bordercolor="AACCEE" class="list_form_table2" >
  <tr class="list_form_tr">
    <td class="list_form_td">
	<table width="100%" border="0" class="list_form_table2" >
      <tr class="list_form_tr">
        <td width="40%" class="list_form_td">�������㣺<%=slad.basBrhName%></td>
        <td width="40%" class="list_form_td">��ѯ���ڣ�<%=slad.basDate%></td>
        <td width="20%" align="right" class="list_form_td">��λ��Ԫ</td>
      </tr>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td height="100">
	<table width="100%" border="0" cellpadding="0" cellspacing="1" class="list_form_table2">
      <tr class="list_form_tr">
        <td width="8%" rowspan="3" class="list_form_td"><strong>�������</strong></td>
        <td width="8%" class="list_form_td">�ͻ�����</td>
        <td width="40%" class="list_form_td"><%=slad.appClientName%></td>
        <td width="8%" class="list_form_td">֤������</td>
        <td width="20%" class="list_form_td"><%=slad.appID%></td>
        <td width="8%" class="list_form_td">�ͻ�����</td>
        <td width="8%" class="list_form_td"><input name="type" type="hidden" value="<%=slad.appClientType%>"><%=slad.appClientType%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td"> ס&nbsp;&nbsp;&nbsp; ַ<input name="clientno" type="hidden" value="<%=slad.appClientNo%>"></td>
        <td class="list_form_td"><%=slad.appAddress%></td>
        <td class="list_form_td">��ϵ�绰</td>
        <td class="list_form_td"><%=slad.appPhone%></td>
        <td class="list_form_td">���õȼ�</td>
        <td class="list_form_td"><%=slad.appCreditClass%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">������;</td>
        <td class="list_form_td"><%=slad.appLoanCat%></td>
        <td class="list_form_td">������</td>
        <td class="list_form_td"><%=slad.appAmt%></td>
        <td class="list_form_td">��������</td>
        <td class="list_form_td"><%=slad.appMonths%>��</td>
      </tr>
	  <tr class="list_form_tr">
	     <td colspan="7" class="list_form_td">&nbsp;</td>
	  </tr>
	  <tr class="list_form_tr">
        <td width="8%" rowspan="2" class="list_form_td"><strong>�������</strong></td>
        <td width="8%" class="list_form_td">����������</td>
        <td width="40%" class="list_form_td"><%=slad.secClientName%></td>
        <td width="8%" class="list_form_td">֤������</td>
        <td width="20%" class="list_form_td"><%=slad.secID%></td>
        <td width="8%" class="list_form_td">�ͻ�����</td>
        <td width="8%" class="list_form_td"><%=slad.secClientType%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td"> ס&nbsp;&nbsp;&nbsp;&nbsp; ַ</td>
        <td class="list_form_td"><%=slad.secAddress%></td>
        <td class="list_form_td">��ϵ�绰</td>
        <td class="list_form_td"><%=slad.secPhone%></td>
        <td class="list_form_td">���õȼ�</td>
        <td class="list_form_td"><%=slad.secCreditClass%></td>
      </tr>
	  </table>
	</td></tr>
	<tr class="list_form_tr"><td class="list_form_td"><table width="100%" border="0" cellpadding="0" cellspacing="1" class="list_form_table2">
      <tr class="list_form_tr">
        <td colspan="6" align="center" class="list_form_td"><strong>����Ѻ�����</strong></td>
      </tr>
      <tr align="center" class="list_form_tr">
        <td width="30%" class="list_form_td">��Ʒ����</td>
        <td width="10%" class="list_form_td">����</td>
        <td width="30%" class="list_form_td">������</td>
        <td width="10%" class="list_form_td">����</td>
        <td width="10%" class="list_form_td">������</td>
		<td width="10%" class="list_form_td">��Ѻֵ</td>
      </tr>
<%
   strBuf=new StringBuffer();
   arrlst=slad.alPlege;

   if(arrlst.size()==0)
   {
	   strBuf.append(" <tr class=\"list_form_tr\">");
	   for(int m=0;m<6;m++)
	   {
	      strBuf.append(" <td class=\"list_form_td\">");
		  strBuf.append("&nbsp;");
          strBuf.append("</td>");
	   }

   }
   for(int i=0;i<arrlst.size();i++)
   {
      ArrayList alTmp=new ArrayList();
      alTmp=(ArrayList)arrlst.get(i);
      strBuf.append(" <tr class=\"list_form_tr\">");

      for(int m=0;m<alTmp.size();m++)
      {
          String item = (String)alTmp.get(m);
          strBuf.append(" <td class=\"list_form_td\">");
          strBuf.append(item);
          strBuf.append("</td>");
      }
      alTmp=null;
   }
   strBuf.append("</tr>");
   out.print(strBuf.toString());
   strBuf=null;

%>
    </table>
	</td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td">	
	<table width="100%" border="0" cellpadding="0" cellspacing="1" class="list_form_table2">
      <tr align="center" class="list_form_tr">
        <td colspan="5" class="list_form_td"><strong>�������</strong></td>
      </tr>
      <tr align="center" class="list_form_tr">
        <td width="10%" class="list_form_td">��������</td>
        <td width="10%" class="list_form_td">�����˸�λ</td>
        <td width="9%" class="list_form_td">����������</td>
        <td width="35%" class="list_form_td">�������</td>
        <td width="36%" class="list_form_td">����</td>
      </tr>
<%
   strBuf=new StringBuffer();
   arrlst=slad.alComment;

   if(arrlst.size()==0)
   {
	   strBuf.append(" <tr class=\"list_form_tr\">");
	   for(int m=0;m<5;m++)
	   {
	      strBuf.append(" <td class=\"list_form_td\">");
		  strBuf.append("&nbsp;");
          strBuf.append("</td>");
	   }

   }
   for(int i=0;i<arrlst.size();i++)
   {
      ArrayList alTmp=new ArrayList();
      alTmp=(ArrayList)arrlst.get(i);
      strBuf.append(" <tr class=\"list_form_tr\">");

      for(int m=0;m<alTmp.size();m++)
      {
          String item = (String)alTmp.get(m);
          strBuf.append(" <td class=\"list_form_td\">");
          strBuf.append(item);
          strBuf.append("</td>");
      }
      alTmp=null;
   }
   strBuf.append("</tr>");
   out.print(strBuf.toString());
   strBuf=null;
%>
    </table>
	</td>
  </tr>
  <tr>
    <td class="list_form_td">
	<table width="100%" border="0" cellpadding="0" cellspacing="1" class="list_form_table2">
      <tr align="center" class="list_form_tr">
        <td colspan="6" class="list_form_td"><strong>�������</strong></td>
      </tr>
      <tr align="center" class="list_form_tr">
        <td width="10%" class="list_form_td">���߲���</td>
        <td width="10%" class="list_form_td">������</td>
        <td width="27%" class="list_form_td">���߽��</td>
        <td width="8%" class="list_form_td">��������</td>
        <td width="9%" class="list_form_td">��������</td>
        <td width="36%" class="list_form_td">���� </td>
      </tr>
<%
  strBuf=new StringBuffer();
  arrlst=slad.alDecision;

  if(arrlst.size()==0)
   {
	   strBuf.append(" <tr class=\"list_form_tr\">");
	   for(int m=0;m<6;m++)
	   {
	      strBuf.append(" <td class=\"list_form_td\">");
		  strBuf.append("&nbsp;");
          strBuf.append("</td>");
	   }

   }
  for(int i=0;i<arrlst.size();i++)
   {
      ArrayList alTmp=new ArrayList();
      alTmp=(ArrayList)arrlst.get(i);
      strBuf.append(" <tr class=\"list_form_tr\">");

      for(int m=0;m<alTmp.size();m++)
      {
          String item = (String)alTmp.get(m);
          strBuf.append(" <td class=\"list_form_td\">");
          strBuf.append(item);
          strBuf.append("</td>");
      }
      alTmp=null;
   }
   strBuf.append("</tr>");
   out.print(strBuf.toString());
   strBuf=null;
%>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td">
	<table width="100%" border="0" cellpadding="0" cellspacing="1" class="list_form_table2">
      <tr align="center" class="list_form_tr">
        <td colspan="8" class="list_form_td"><strong>�������</strong></td>
      </tr>
      <tr class="list_form_tr">
        <td width="8%" class="list_form_td">��ͬ���</td>
        <td width="20%" class="list_form_td"><%=slad.conNo%></td>
        <td width="10%" class="list_form_td">��ͬ������</td>
        <td width="10%" class="list_form_td"><%=slad.conBeginDate%></td>
        <td width="10%" class="list_form_td">����</td>
        <td width="14%" class="list_form_td"><%=slad.conRate%></td>
        <td width="8%" class="list_form_td">��ͬ���</td>
        <td width="20%" class="list_form_td"><%=slad.conAmt%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">��&nbsp;&nbsp;&nbsp;&nbsp;Ŀ</td>
        <td class="list_form_td"><%=slad.conAccNo%></td>
        <td class="list_form_td">��ͬ������</td>
        <td class="list_form_td"><%=slad.conEndDate%></td>
        <td class="list_form_td">����</td>
        <td class="list_form_td"><%=slad.conMonths%></td>
        <td class="list_form_td">���Ž��</td>
        <td class="list_form_td"><%=slad.conAmt%></td>
      </tr>
	  <tr class="list_form_tr">
        <td colspan="8" class="list_form_td">&nbsp;</td>
      </tr>
	  <tr class="list_form_tr">
        <td class="list_form_td" >Ŀǰ״̬</td>
        <td class="list_form_td"><%=slad.basStatusName%></td>
        <td class="list_form_td">�������</td>
        <td colspan="2" class="list_form_td">
		<%  
		    if(slad.basStatus.equals("&nbsp;"))
			   out.print("��");
			else
			{
		       if(Integer.parseInt(slad.basStatus)<7)
		         out.print("��");
		       else
		         out.print("<a href=\"loanInfo.jsp?BMNO="+strBmNo+"\" >������ϸ���");
			}
		%>
		</td>
        <td colspan="2" align="right" class="list_form_td">&nbsp;</td>
		<td align="center"><input type="button" class="list_button_active"  value=" �� �� "  onclick="window.close()"></td>
        </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
