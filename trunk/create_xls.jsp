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
Title: ����excel
Description: ����excel��
 * @version  $Revision: 1.3 $  $Date: 2007/06/20 09:39:56 $
 * @author   weiyb
 * <p/>�޸ģ�$Author: weiyb $
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
 

 //ȥ����ҳ���ƣ�ȡȫ����¼
 s_main=s_main.substring(0,s_main.indexOf("AS a1")+"AS a1".length());//���ַ�����������޸ġ�
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
                              <!--  <td nowrap align="center">��������</td>-->
                              <td  nowrap align="center">ҵ������</td>
                              <td nowrap align="center">�����ʺ�</td>
                              <td nowrap align="center">��Ŀ��</td>
                              <td nowrap align="center">��ݺ�</td>
                              <td nowrap align="center">������</td>
                              <td nowrap align="center">�ͻ�����</td>
							  <td nowrap align="center">�ͻ�����</td>
                              <td  nowrap align="center">������ʽ</td>
                              <!--  
                              <td  nowrap align="center">����������</td>
							  <td  nowrap align="center">�����˴���</td>
							  -->
                              <td  nowrap align="center">�ļ���̬</td>
                              <td   nowrap align="center">�弶����</td>
							  <td  nowrap align="center">������</td>
                              <td   nowrap align="center">չ�ڵ�����</td>
                              <td   nowrap align="center">�����;</td>
                              <td   nowrap align="center">����</td>
                              <td  nowrap align="center"> ������</td>
                              <td   nowrap align="center">��ͬ���</td>
                              <td  nowrap align="center"> ǷϢ���</td>
                              <!-- <td   nowrap align="center">������</td>-->
                              <td   nowrap align="center">��һ������</td>
                              <td   nowrap align="center">���ν��</td>
                              <td   nowrap align="center">ҵ���</td>
                            </tr>
                            <%
		
		while(crs.next()){
%>
                            <tr   bgcolor="#ffffff" onmouseover="mOvr(this)" onclick="mClk('<%=crs.getString("BMNO") %>');" onmouseout="mOut(this);">
                              <!--<td nowrap  align="left"  ><div title='<%=crs.getString("BRHID") %>' ><%=DBUtil.fromDB(crs.getString("LNAME"))%></div></td> //�������� -->
                              <td nowrap align="left" ><%=DBUtil.fromDB(crs.getString("TYPENAME"))%></td><!-- ҵ������ -->
                              <td nowrap align="center" ><%=crs.getString("ACTNO")%>&nbsp;</td><!-- �����ʺ� -->
                              <td nowrap align="center" ><div title='<%=DBUtil.fromDB(crs.getString("ACCNAME"))%>'><%=crs.getString("ACCNO")%> </div></td><!-- ��Ŀ�� -->
                              <td nowrap align="center" ><%=crs.getString("CNLNO")%></td><!-- ��ݺ� -->
                              <td nowrap align="center" ><%=crs.getString("PAYDATE")%></td><!-- �������� -->
                              <td nowrap  align="left"> <%=crs.getString("CLIENTNO")==null?"":DBUtil.fromDB(crs.getString("CLIENTNO"))%></td><!--//���������  -->
							  <td nowrap align="left"><%=crs.getString("CLIENTNAME")==null?"":DBUtil.fromDB(crs.getString("CLIENTNAME"))%></td><!--//����˴���  -->
                              <td nowrap align="center"><%=crs.getString("LOANTYPE3NAME")==null?"":DBUtil.fromDB(crs.getString("LOANTYPE3NAME"))%></td><!--//������ʽ  -->
                              <!--<td nowrap  align="left"><%//=crs.getString("CLIENTNAME")%></td>//����������  -->
							  <!--<td nowrap align="left"><%//=crs.getString("ID")%></td>//�����˴���  -->
                              <td nowrap align="center"><%=DBUtil.fromDB(crs.getString("LOANCAT2NAME"))%></td><!--//�ļ�������̬  -->
                              <td nowrap  align="center"><%=DBUtil.fromDB(crs.getString("LOANCAT1NAME"))%></td><!--//�弶������̬  -->
							  <td nowrap align="left"><%=crs.getString("ENDDATE")%></td><!--//������  -->
                              <td nowrap  align="left"><%=crs.getString("NOWENDDATE")%></td><!--//չ�ڵ�����  -->
                              <td nowrap  align="left"><%=crs.getString("LOANCAT3NAME")==null?"":DBUtil.fromDB(crs.getString("LOANCAT3NAME"))%></td><!--//�����;  -->
                              <td nowrap  align="right"><%=crs.getBigDecimal("BRATE")%></td><!--//����  -->
                               <td nowrap  align="right"><%=df.format(crs.getBigDecimal("NOWBAL"))%></td><!--//�����  -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("CONTRACTAMT"))%></td><!--//��ͬ���  -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("lixi")==null?new BigDecimal(0.00):crs.getBigDecimal("lixi"))%></td><!--//ǷϢ���  -->
                              <!--<td nowrap  align="left"><%//=crs.getString("")%></td>//������  -->
                              <td nowrap  align="center"><%=crs.getString("USERNAME")==null?"":DBUtil.fromDB(crs.getString("USERNAME"))%></td><!-- //��һ������ -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("FISRTRESPPCT"))%></td><!--//���ν��  -->
                              <td nowrap  align="right"><%=crs.getString("BMNO")%></td><!--//ҵ��� -->
                            </tr>
<%
}
%>
</table>
<%}%>
