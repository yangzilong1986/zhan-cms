<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cms.report.SinLoanDetData" %>
<%@ page import="zt.cms.report.SinLoanDetail" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.StringBuffer"%>

<%
	RequestDispatcher rd=null;

	String strbmno = request.getParameter("BMNO");
	if(strbmno==null)
	   strbmno=request.getParameter("bmno");

	if(strbmno==null)
	{
	   request.setAttribute("msg","���û�������Ϣ!");
       rd=request.getRequestDispatcher("/showinfo.jsp");
       rd.forward(request,response);
	}

	SinLoanDetail sld=new SinLoanDetail();
	SinLoanDetData sdd=sld.getLoanDetail(strbmno);

    String errMsg=sld.getErrMsg();
	if(errMsg!=null)
	{
	   request.setAttribute("msg",errMsg);
       rd=request.getRequestDispatcher("/showinfo.jsp");
       rd.forward(request,response);
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="/css/platform.css" rel="stylesheet" type="text/css">
<title>���ʴ�����ϸ��ѯ</title>
<style type="text/css">
<!--
.style1 {font-size: 16px}
-->
</style>
</head>

<body background="/images/checks_02.jpg">
<table width="100%" height="29" border="0" >
<tr >
<td height="25" align="center">
  <span class="style1">���ʴ�����ϸ��ѯ</span>
</td>
</tr>
</table>
<table width="100%"  border="0" cellspacing="0" cellpadding="0" bordercolor="AACCEE" class="list_form_table2">
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr class="list_form_tr">
        <td width="29%" class="list_form_td">��ѯ���ڣ�<%=sdd.strDate%></td>
        <td width="40%" class="list_form_td">&nbsp;</td>
        <td width="31%" align="right" class="list_form_td">ҵ���ţ�<%=strbmno%></td>
      </tr>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr align="center" class="list_form_tr">
        <td colspan="6" class="list_form_td"><strong>������Ϣ</strong></td>
        </tr>
      <tr class="list_form_tr">
        <td width="9%" class="list_form_td">��������</td>
        <td width="20%" class="list_form_td"><%=sdd.strScbrhName%></td>
        <td width="10%" class="list_form_td">�ͻ�����</td>
        <td width="30%" class="list_form_td"><%=sdd.strClientName%></td>
        <td width="11%" class="list_form_td">��ϵ�绰</td>
        <td width="20%" class="list_form_td"><%=sdd.strPhone%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">��������</td>
        <td class="list_form_td"><%=sdd.strLoanType%></td>
        <td class="list_form_td">������;</td>
        <td class="list_form_td"><%=sdd.strLoanPurpose%></td>
        <td class="list_form_td">֤������</td>
        <td class="list_form_td"><%=sdd.strId%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">�ͻ�����</td>
        <td class="list_form_td"><%=sdd.strClientMgr%></td>
        <td class="list_form_td"><%=sdd.strIfResp%></td>
        <td class="list_form_td"><%=sdd.strFirstResp%></td>
        <td class="list_form_td">�����˽��</td>
        <td class="list_form_td"><%=sdd.strFisrtRespPct%></td>
      </tr>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr align="center" class="list_form_tr">
        <td colspan="8" class="list_form_td"><strong>������Ϣ</strong></td>
        </tr>
      <tr class="list_form_tr">
        <td width="9%" class="list_form_td">��ͬ���</td>
        <td width="20%" class="list_form_td"><%=sdd.strContractNo%></td>
        <td width="10%" class="list_form_td">������ͬ��</td>
        <td width="17%" class="list_form_td"><%=sdd.strSContractNo%></td>
        <td width="9%" class="list_form_td">��ͬ���</td>
        <td width="14%" class="list_form_td"><%=sdd.strContractAmt%></td>
        <td width="10%" class="list_form_td">��ͬ������</td>
        <td width="11%"  class="list_form_td"><%=sdd.strPayDate%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">�����ʺ�</td>
        <td class="list_form_td"><%=sdd.strActNo%></td>
        <td class="list_form_td">��ݺ�</td>
        <td class="list_form_td"><%=sdd.strCnlNo%></td>
        <td class="list_form_td">��Ŀ</td>
        <td class="list_form_td"><%=sdd.strAccNo%></td>
        <td class="list_form_td">���� </td>
        <td class="list_form_td"><%=sdd.strCrtRate%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">������</td>
        <td class="list_form_td"><%=sdd.strEndDate%></td>
        <td class="list_form_td">�������</td>
        <td class="list_form_td"><%=sdd.strNowEndDate%></td>
        <td class="list_form_td">��������</td>
        <td class="list_form_td"><%=sdd.strPerimon%></td>
        <td class="list_form_td">��ǰ���</td>
        <td class="list_form_td"><%=sdd.strNowBal%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">������ʽ</td>
        <td class="list_form_td"><%=sdd.strLoanType3%></td>
        <td class="list_form_td">���ŷ�ʽ</td>
        <td class="list_form_td"><%=sdd.strLoanType5%></td>
        <td class="list_form_td">ռ����̬</td>
        <td class="list_form_td"><%=sdd.strLoanCat2%></td>
        <td class="list_form_td">��Ƿ��Ϣ</td>
        <td class="list_form_td"><%=sdd.strEndRate%></td>
      </tr>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr align="center" class="list_form_tr">
        <td colspan="7" class="list_form_td"><strong>�����ջ����</strong></td>
        </tr>
      <tr class="list_form_tr">
        <td width="16%" align="center" class="list_form_td">�����ʺ�</td>
        <td width="12%" align="center" class="list_form_td">��ݺ�</td>
        <td width="14%" align="center" class="list_form_td">��������</td>
        <td width="15%" align="center" class="list_form_td">���Ž��</td>
        <td width="16%" align="center" class="list_form_td">�ջؽ��</td>
        <td width="12%" align="center" class="list_form_td">��ǰ���</td>
        <td width="15%" align="center" class="list_form_td">ռ����̬</td>
      </tr>
<%
   ArrayList arrlst=sdd.alPay;
   if(arrlst.size()==0)
  {
%>
	  <tr class="list_form_tr">
        <td width="16%" class="list_form_td">&nbsp;</td>
        <td width="12%" class="list_form_td">&nbsp;</td>
        <td width="14%" class="list_form_td">&nbsp;</td>
        <td width="15%" class="list_form_td">&nbsp;</td>
        <td width="16%" class="list_form_td">&nbsp;</td>
        <td width="12%" class="list_form_td">&nbsp;</td>
        <td width="15%" class="list_form_td">&nbsp;</td>
      </tr>
 <%
  }
  for(int i=0;i<arrlst.size();i++)
{
      ArrayList alTmp=new ArrayList();
      alTmp=(ArrayList)arrlst.get(i);
%>
	  <tr class="list_form_tr">
        <td width="16%" align="center" class="list_form_td"><%=(String)alTmp.get(0)%></td>
        <td width="12%" align="center" class="list_form_td"><%=(String)alTmp.get(1)%></td>
        <td width="14%" align="center" class="list_form_td"><%=(String)alTmp.get(2)%></td>
        <td width="15%" align="right" class="list_form_td"><%=(String)alTmp.get(3)%></td>
        <td width="16%" align="right" class="list_form_td"><%=(String)alTmp.get(4)%></td>
        <td width="12%" align="right" class="list_form_td"><%=(String)alTmp.get(5)%></td>
        <td width="15%" align="center" class="list_form_td"><%=(String)alTmp.get(6)%></td>
      </tr>
 <%
}
%>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr align="center" class="list_form_tr">
        <td colspan="8"><strong>����ʱ���</strong></td>
        </tr>
      <tr class="list_form_tr">
        <td width="11%" class="list_form_td">ת����ʱ��</td>
        <td width="16%" class="list_form_td"><%=sdd.strTransDate%></td>
        <td width="12%" class="list_form_td">����������</td>
        <td width="16%" class="list_form_td"><%=sdd.strAdminedBy%></td>
        <td width="8%" class="list_form_td">��������</td>
        <td width="14%" class="list_form_td"><%=sdd.strLastNotifyDate%></td>
        <td width="10%" class="list_form_td">������</td>
        <td width="13%" class="list_form_td"><%=sdd.strReviewedBy%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">����ʱ��</td>
        <td class="list_form_td"><%=sdd.strPenaltyDate%></td>
        <td class="list_form_td">��Ӧ�����涨</td>
        <td colspan="2" class="list_form_td"><%=sdd.strPenaltyRule%></td>
        <td class="list_form_td">&nbsp;</td>
        <td class="list_form_td">�������</td>
        <td class="list_form_td"><%=sdd.strPenalty%></td>
      </tr>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr class="list_form_tr">
        <td width="11%" class="list_form_td">Ŀǰ״̬</td>
        <td width="36%" class="list_form_td"><%=sdd.strBmStatus%></td>
        <td width="13%" class="list_form_td">�������</td>
        <td width="27%" class="list_form_td"><a href="q01010.jsp?BMNO=<%=strbmno%>" >������ϸ���</td>
        <td width="13%" align="center" class="list_form_td"><input type="button" class="list_button_active"  value=" �� �� "  onclick="window.close()"></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
