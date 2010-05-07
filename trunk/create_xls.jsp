<%@ page contentType="application/msexcel; charset=gb2312" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.cms.report.db.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.form.control.impl.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*,java.net.URLDecoder" %>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.utils.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cms.pub.SCBranch,zt.cms.report.QrySummary" %>
<%@ page import="zt.cmsi.pub.define.UserRoleMan" %>
<%--
=============================================== 
Title: 导出excel
Description: 导出excel。
 * @version  $Revision: 1.3 $  $Date: 2007/06/20 09:39:56 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<%
 String brhid=request.getParameter("brhid");
 response.setContentType("application/vnd.ms-excel;charset=GBK");
 response.setHeader("Content-disposition","inline;filename="+brhid+".xls"); 
 String s_where =URLDecoder.decode(request.getParameter("s_where"),"GBK");
 String s_count=URLDecoder.decode(request.getParameter("s_count"),"GBK");
 String s_main=URLDecoder.decode(request.getParameter("s_main"),"GBK");
 s_where=DBUtil.toDB(s_where);
 s_count=DBUtil.toDB(s_count);
 s_main=DBUtil.toDB(s_main);
 

 //去掉分页限制，取全部记录
 s_main=s_main.substring(0,s_main.indexOf("AS a1")+"AS a1".length());//此字符串不能随便修改。
 System.out.println(s_main);
 	Vector vec=null;
	int rows=0;
	CachedRowSet crs=null;
	//ConnectionManager manager=ConnectionManager.getInstance();
	vec=DB2_81.pageRs(s_main,s_count,s_where);
	rows=((Integer)vec.get(0)).intValue();
	crs=(CachedRowSet)vec.get(1);
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	String data="";
	%>
<HTML>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<head><title></title></head>
<body>
<%if (crs!=null){ %>
                        <TABLE borderColor='#111111' cellSpacing='0' cellPadding='2' width='1200' align='center' border='1'>
                            <tr class=head >
                              <!--  <td nowrap align="center">网点名称</td>-->
                              <td  nowrap align="center">业务种类</td>
                              <td nowrap align="center">贷款帐号</td>
                              <td nowrap align="center">科目号</td>
                              <td nowrap align="center">借据号</td>
                              <td nowrap align="center">发放日</td>
                              <td nowrap align="center">客户代码</td>
							  <td nowrap align="center">客户名称</td>
                              <td  nowrap align="center">担保方式</td>
                              <!--  
                              <td  nowrap align="center">担保人名称</td>
							  <td  nowrap align="center">担保人代码</td>
							  -->
                              <td  nowrap align="center">四级形态</td>
                              <td   nowrap align="center">五级分类</td>
							  <td  nowrap align="center">到期日</td>
                              <td   nowrap align="center">展期到期日</td>
                              <td   nowrap align="center">借款用途</td>
                              <td   nowrap align="center">利率</td>
                              <td  nowrap align="center"> 借款余额</td>
                              <td   nowrap align="center">合同金额</td>
                              <td  nowrap align="center"> 欠息金额</td>
                              <!-- <td   nowrap align="center">审批人</td>-->
                              <td   nowrap align="center">第一责任人</td>
                              <td   nowrap align="center">责任金额</td>
                              <td   nowrap align="center">业务号</td>
                            </tr>
                            <%
		
		while(crs.next()){
%>
                            <tr   bgcolor="#ffffff" onmouseover="mOvr(this)" onclick="mClk('<%=crs.getString("BMNO") %>');" onmouseout="mOut(this);">
                              <!--<td nowrap  align="left"  ><div title='<%=crs.getString("BRHID") %>' ><%=DBUtil.fromDB(crs.getString("LNAME"))%></div></td> //网点名称 -->
                              <td nowrap align="left" ><%=DBUtil.fromDB(crs.getString("TYPENAME"))%></td><!-- 业务种类 -->
                              <td nowrap align="center" ><%=crs.getString("ACTNO")%>&nbsp;</td><!-- 贷款帐号 -->
                              <td nowrap align="center" ><div title='<%=DBUtil.fromDB(crs.getString("ACCNAME"))%>'><%=crs.getString("ACCNO")%> </div></td><!-- 科目号 -->
                              <td nowrap align="center" ><%=crs.getString("CNLNO")%></td><!-- 借据号 -->
                              <td nowrap align="center" ><%=crs.getString("PAYDATE")%></td><!-- 发放日期 -->
                              <td nowrap  align="left"> <%=crs.getString("CLIENTNO")==null?"":DBUtil.fromDB(crs.getString("CLIENTNO"))%></td><!--//借款人名称  -->
							  <td nowrap align="left"><%=crs.getString("CLIENTNAME")==null?"":DBUtil.fromDB(crs.getString("CLIENTNAME"))%></td><!--//借款人代码  -->
                              <td nowrap align="center"><%=crs.getString("LOANTYPE3NAME")==null?"":DBUtil.fromDB(crs.getString("LOANTYPE3NAME"))%></td><!--//担保方式  -->
                              <!--<td nowrap  align="left"><%//=crs.getString("CLIENTNAME")%></td>//担保人名称  -->
							  <!--<td nowrap align="left"><%//=crs.getString("ID")%></td>//担保人代码  -->
                              <td nowrap align="center"><%=DBUtil.fromDB(crs.getString("LOANCAT2NAME"))%></td><!--//四级分类形态  -->
                              <td nowrap  align="center"><%=DBUtil.fromDB(crs.getString("LOANCAT1NAME"))%></td><!--//五级分类形态  -->
							  <td nowrap align="left"><%=crs.getString("ENDDATE")%></td><!--//到期日  -->
                              <td nowrap  align="left"><%=crs.getString("NOWENDDATE")%></td><!--//展期到期日  -->
                              <td nowrap  align="left"><%=crs.getString("LOANCAT3NAME")==null?"":DBUtil.fromDB(crs.getString("LOANCAT3NAME"))%></td><!--//借款用途  -->
                              <td nowrap  align="right"><%=crs.getBigDecimal("BRATE")%></td><!--//利率  -->
                               <td nowrap  align="right"><%=df.format(crs.getBigDecimal("NOWBAL"))%></td><!--//借款金额  -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("CONTRACTAMT"))%></td><!--//合同金额  -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("lixi")==null?new BigDecimal(0.00):crs.getBigDecimal("lixi"))%></td><!--//欠息金额  -->
                              <!--<td nowrap  align="left"><%//=crs.getString("")%></td>//审批人  -->
                              <td nowrap  align="center"><%=crs.getString("USERNAME")==null?"":DBUtil.fromDB(crs.getString("USERNAME"))%></td><!-- //第一责任人 -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("FISRTRESPPCT"))%></td><!--//责任金额  -->
                              <td nowrap  align="right"><%=crs.getString("BMNO")%></td><!--//业务号 -->
                            </tr>
<%
}
%>
</table>
<%}%>
