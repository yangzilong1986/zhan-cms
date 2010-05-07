<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="zt.cms.report.SinLoanAppData" %>
<%@ page import="zt.cms.report.SinLoanApprove" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.StringBuffer"%>

<%--
===============================================
Title: 新增不良贷款查询详细信息页面
Description: 新增不良贷款查询信息页面
 * @version   $Revision: 1.1 $  $Date: 2007/05/09 09:31:08 $
 * @author   houcs
 * <p/>修改：$Author: houcs $
===============================================
--%>
<html>
<head>
<title>贷款审批情况表</title>
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
        request.setAttribute("msg","无业务号！");
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
  <span class="style1">贷款审批情况表</span>
</td>
</tr>
</table>
<table width="100%" height="200" border="0" cellpadding="0" cellspacing="0" bordercolor="AACCEE" class="list_form_table2" >
  <tr class="list_form_tr">
    <td class="list_form_td">
	<table width="100%" border="0" class="list_form_table2" >
      <tr class="list_form_tr">
        <td width="40%" class="list_form_td">所属网点：<%=slad.basBrhName%></td>
        <td width="40%" class="list_form_td">查询日期：<%=slad.basDate%></td>
        <td width="20%" align="right" class="list_form_td">单位：元</td>
      </tr>
    </table></td>
  </tr>
  <tr class="list_form_tr">
    <td height="100">
	<table width="100%" border="0" cellpadding="0" cellspacing="1" class="list_form_table2">
      <tr class="list_form_tr">
        <td width="8%" rowspan="3" class="list_form_td"><strong>申请情况</strong></td>
        <td width="8%" class="list_form_td">客户名称</td>
        <td width="40%" class="list_form_td"><%=slad.appClientName%></td>
        <td width="8%" class="list_form_td">证件号码</td>
        <td width="20%" class="list_form_td"><%=slad.appID%></td>
        <td width="8%" class="list_form_td">客户类型</td>
        <td width="8%" class="list_form_td"><input name="type" type="hidden" value="<%=slad.appClientType%>"><%=slad.appClientType%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td"> 住&nbsp;&nbsp;&nbsp; 址<input name="clientno" type="hidden" value="<%=slad.appClientNo%>"></td>
        <td class="list_form_td"><%=slad.appAddress%></td>
        <td class="list_form_td">联系电话</td>
        <td class="list_form_td"><%=slad.appPhone%></td>
        <td class="list_form_td">信用等级</td>
        <td class="list_form_td"><%=slad.appCreditClass%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">贷款用途</td>
        <td class="list_form_td"><%=slad.appLoanCat%></td>
        <td class="list_form_td">申请金额</td>
        <td class="list_form_td"><%=slad.appAmt%></td>
        <td class="list_form_td">申请期限</td>
        <td class="list_form_td"><%=slad.appMonths%>月</td>
      </tr>
	  <tr class="list_form_tr">
	     <td colspan="7" class="list_form_td">&nbsp;</td>
	  </tr>
	  <tr class="list_form_tr">
        <td width="8%" rowspan="2" class="list_form_td"><strong>担保情况</strong></td>
        <td width="8%" class="list_form_td">担保人名称</td>
        <td width="40%" class="list_form_td"><%=slad.secClientName%></td>
        <td width="8%" class="list_form_td">证件号码</td>
        <td width="20%" class="list_form_td"><%=slad.secID%></td>
        <td width="8%" class="list_form_td">客户类型</td>
        <td width="8%" class="list_form_td"><%=slad.secClientType%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td"> 住&nbsp;&nbsp;&nbsp;&nbsp; 址</td>
        <td class="list_form_td"><%=slad.secAddress%></td>
        <td class="list_form_td">联系电话</td>
        <td class="list_form_td"><%=slad.secPhone%></td>
        <td class="list_form_td">信用等级</td>
        <td class="list_form_td"><%=slad.secCreditClass%></td>
      </tr>
	  </table>
	</td></tr>
	<tr class="list_form_tr"><td class="list_form_td"><table width="100%" border="0" cellpadding="0" cellspacing="1" class="list_form_table2">
      <tr class="list_form_tr">
        <td colspan="6" align="center" class="list_form_td"><strong>抵质押物情况</strong></td>
      </tr>
      <tr align="center" class="list_form_tr">
        <td width="30%" class="list_form_td">物品名称</td>
        <td width="10%" class="list_form_td">分类</td>
        <td width="30%" class="list_form_td">所有人</td>
        <td width="10%" class="list_form_td">数量</td>
        <td width="10%" class="list_form_td">评估价</td>
		<td width="10%" class="list_form_td">抵押值</td>
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
        <td colspan="5" class="list_form_td"><strong>审批情况</strong></td>
      </tr>
      <tr align="center" class="list_form_tr">
        <td width="10%" class="list_form_td">审批部门</td>
        <td width="10%" class="list_form_td">处理人岗位</td>
        <td width="9%" class="list_form_td">处理人名称</td>
        <td width="35%" class="list_form_td">处理意见</td>
        <td width="36%" class="list_form_td">结论</td>
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
        <td colspan="6" class="list_form_td"><strong>决策情况</strong></td>
      </tr>
      <tr align="center" class="list_form_tr">
        <td width="10%" class="list_form_td">决策部门</td>
        <td width="10%" class="list_form_td">决策人</td>
        <td width="27%" class="list_form_td">决策金额</td>
        <td width="8%" class="list_form_td">决策利率</td>
        <td width="9%" class="list_form_td">决策期限</td>
        <td width="36%" class="list_form_td">结论 </td>
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
        <td colspan="8" class="list_form_td"><strong>发放情况</strong></td>
      </tr>
      <tr class="list_form_tr">
        <td width="8%" class="list_form_td">合同编号</td>
        <td width="20%" class="list_form_td"><%=slad.conNo%></td>
        <td width="10%" class="list_form_td">合同发放日</td>
        <td width="10%" class="list_form_td"><%=slad.conBeginDate%></td>
        <td width="10%" class="list_form_td">利率</td>
        <td width="14%" class="list_form_td"><%=slad.conRate%></td>
        <td width="8%" class="list_form_td">合同金额</td>
        <td width="20%" class="list_form_td"><%=slad.conAmt%></td>
      </tr>
      <tr class="list_form_tr">
        <td class="list_form_td">科&nbsp;&nbsp;&nbsp;&nbsp;目</td>
        <td class="list_form_td"><%=slad.conAccNo%></td>
        <td class="list_form_td">合同到期日</td>
        <td class="list_form_td"><%=slad.conEndDate%></td>
        <td class="list_form_td">期限</td>
        <td class="list_form_td"><%=slad.conMonths%></td>
        <td class="list_form_td">发放金额</td>
        <td class="list_form_td"><%=slad.conAmt%></td>
      </tr>
	  <tr class="list_form_tr">
        <td colspan="8" class="list_form_td">&nbsp;</td>
      </tr>
	  <tr class="list_form_tr">
        <td class="list_form_td" >目前状态</td>
        <td class="list_form_td"><%=slad.basStatusName%></td>
        <td class="list_form_td">相关连接</td>
        <td colspan="2" class="list_form_td">
		<%  
		    if(slad.basStatus.equals("&nbsp;"))
			   out.print("无");
			else
			{
		       if(Integer.parseInt(slad.basStatus)<7)
		         out.print("无");
		       else
		         out.print("<a href=\"loanInfo.jsp?BMNO="+strBmNo+"\" >贷款详细情况");
			}
		%>
		</td>
        <td colspan="2" align="right" class="list_form_td">&nbsp;</td>
		<td align="center"><input type="button" class="list_button_active"  value=" 关 闭 "  onclick="window.close()"></td>
        </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
