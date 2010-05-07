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
	   request.setAttribute("msg","无用户基本信息!");
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
<title>单笔贷款明细查询</title>
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
  <span class="style1">单笔贷款明细查询</span>
</td>
</tr>
</table>
<table width="100%"  border="0" cellspacing="0" cellpadding="0" bordercolor="AACCEE" class="list_form_table2">
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr class="list_form_tr">
        <td width="29%" class="list_form_td">查询日期：<%=sdd.strDate%></td>
        <td width="40%" class="list_form_td">&nbsp;</td>
        <td width="31%" align="right" class="list_form_td">业务编号：<%=strbmno%></td>
      </tr>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr align="center" class="list_form_tr">
        <td colspan="6" class="list_form_td"><strong>基本信息</strong></td>
        </tr>
      <tr class="list_form_tr">
        <td width="9%" class="list_form_td">网点名称</td>
        <td width="20%" class="list_form_td"><%=sdd.strScbrhName%></td>
        <td width="10%" class="list_form_td">客户名称</td>
        <td width="30%" class="list_form_td"><%=sdd.strClientName%></td>
        <td width="11%" class="list_form_td">联系电话</td>
        <td width="20%" class="list_form_td"><%=sdd.strPhone%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">贷款种类</td>
        <td class="list_form_td"><%=sdd.strLoanType%></td>
        <td class="list_form_td">贷款用途</td>
        <td class="list_form_td"><%=sdd.strLoanPurpose%></td>
        <td class="list_form_td">证件号码</td>
        <td class="list_form_td"><%=sdd.strId%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">客户经理</td>
        <td class="list_form_td"><%=sdd.strClientMgr%></td>
        <td class="list_form_td"><%=sdd.strIfResp%></td>
        <td class="list_form_td"><%=sdd.strFirstResp%></td>
        <td class="list_form_td">责任人金额</td>
        <td class="list_form_td"><%=sdd.strFisrtRespPct%></td>
      </tr>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr align="center" class="list_form_tr">
        <td colspan="8" class="list_form_td"><strong>贷款信息</strong></td>
        </tr>
      <tr class="list_form_tr">
        <td width="9%" class="list_form_td">合同编号</td>
        <td width="20%" class="list_form_td"><%=sdd.strContractNo%></td>
        <td width="10%" class="list_form_td">附属合同号</td>
        <td width="17%" class="list_form_td"><%=sdd.strSContractNo%></td>
        <td width="9%" class="list_form_td">合同金额</td>
        <td width="14%" class="list_form_td"><%=sdd.strContractAmt%></td>
        <td width="10%" class="list_form_td">合同发放日</td>
        <td width="11%"  class="list_form_td"><%=sdd.strPayDate%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">贷款帐号</td>
        <td class="list_form_td"><%=sdd.strActNo%></td>
        <td class="list_form_td">借据号</td>
        <td class="list_form_td"><%=sdd.strCnlNo%></td>
        <td class="list_form_td">科目</td>
        <td class="list_form_td"><%=sdd.strAccNo%></td>
        <td class="list_form_td">利率 </td>
        <td class="list_form_td"><%=sdd.strCrtRate%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">到期日</td>
        <td class="list_form_td"><%=sdd.strEndDate%></td>
        <td class="list_form_td">最后到期日</td>
        <td class="list_form_td"><%=sdd.strNowEndDate%></td>
        <td class="list_form_td">贷款期限</td>
        <td class="list_form_td"><%=sdd.strPerimon%></td>
        <td class="list_form_td">当前余额</td>
        <td class="list_form_td"><%=sdd.strNowBal%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">担保方式</td>
        <td class="list_form_td"><%=sdd.strLoanType3%></td>
        <td class="list_form_td">发放方式</td>
        <td class="list_form_td"><%=sdd.strLoanType5%></td>
        <td class="list_form_td">占用形态</td>
        <td class="list_form_td"><%=sdd.strLoanCat2%></td>
        <td class="list_form_td">结欠利息</td>
        <td class="list_form_td"><%=sdd.strEndRate%></td>
      </tr>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr align="center" class="list_form_tr">
        <td colspan="7" class="list_form_td"><strong>发放收回情况</strong></td>
        </tr>
      <tr class="list_form_tr">
        <td width="16%" align="center" class="list_form_td">贷款帐号</td>
        <td width="12%" align="center" class="list_form_td">借据号</td>
        <td width="14%" align="center" class="list_form_td">交易日期</td>
        <td width="15%" align="center" class="list_form_td">发放金额</td>
        <td width="16%" align="center" class="list_form_td">收回金额</td>
        <td width="12%" align="center" class="list_form_td">当前余额</td>
        <td width="15%" align="center" class="list_form_td">占用形态</td>
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
        <td colspan="8"><strong>不良时情况</strong></td>
        </tr>
      <tr class="list_form_tr">
        <td width="11%" class="list_form_td">转不良时间</td>
        <td width="16%" class="list_form_td"><%=sdd.strTransDate%></td>
        <td width="12%" class="list_form_td">清收责任人</td>
        <td width="16%" class="list_form_td"><%=sdd.strAdminedBy%></td>
        <td width="8%" class="list_form_td">催收日期</td>
        <td width="14%" class="list_form_td"><%=sdd.strLastNotifyDate%></td>
        <td width="10%" class="list_form_td">审批人</td>
        <td width="13%" class="list_form_td"><%=sdd.strReviewedBy%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">处罚时间</td>
        <td class="list_form_td"><%=sdd.strPenaltyDate%></td>
        <td class="list_form_td">适应处罚规定</td>
        <td colspan="2" class="list_form_td"><%=sdd.strPenaltyRule%></td>
        <td class="list_form_td">&nbsp;</td>
        <td class="list_form_td">处罚情况</td>
        <td class="list_form_td"><%=sdd.strPenalty%></td>
      </tr>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td class="list_form_td"><table width="100%"  border="0" cellspacing="1" cellpadding="0" class="list_form_table2">
      <tr class="list_form_tr">
        <td width="11%" class="list_form_td">目前状态</td>
        <td width="36%" class="list_form_td"><%=sdd.strBmStatus%></td>
        <td width="13%" class="list_form_td">相关链接</td>
        <td width="27%" class="list_form_td"><a href="q01010.jsp?BMNO=<%=strbmno%>" >审批详细情况</td>
        <td width="13%" align="center" class="list_form_td"><input type="button" class="list_button_active"  value=" 关 闭 "  onclick="window.close()"></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
