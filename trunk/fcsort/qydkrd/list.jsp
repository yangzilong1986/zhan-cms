<%@ page language="java" pageEncoding="GB2312" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.cms.fcsort.qydkrd.CompanyDAO" %>
<%@ page import="zt.cms.fcsort.qydkrd.CompanyInfo" %>
<%@ page import="zt.cms.fcsort.qydkrd.CompanyInfo2" %>
<%@ page import="java.util.List" %>
<%@ page import="zt.cmsi.pub.define.SystemDate"   %>
<%--
=============================================== 
Title:��ҵ�϶���ӡ����ҳ��
Description:�弶�������ͳ��ҳ��
 * @version  $Revision: 1.26 $  $Date: 2007/06/22 01:26:39 $
 * @author   houcs
 * <p/>�޸ģ�$Author: houcs $
=============================================== 
--%>
<%
    //***************************ҳ������*****************************
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String currentPath = basePath + "fcsort/qydkrd/";
    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("currentPath", currentPath);
   //***********ҳ�����*********************************************
    String create = request.getParameter("createdate");
    String brhid=request.getParameter("brhid");
    String name=SCBranch.getLName(brhid);
    String clientno=request.getParameter("clientno");
    String colspan = "7";
    String startdate = "";
    String enddate = "";
    String params = "";
    CompanyDAO dao=new CompanyDAO(request);
    dao.setClientno(clientno);
    dao.setBrhid(brhid);
    dao.setCreatedate(create);
    dao.setCreatedate2(create);
     //************************������ʾ����*****************************

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="web.css">
    <script language="javascript" src="<%= basePath %>query/setup/meizzDate.js"></script>
    <script language="javascript" src="ajax.js"></script>
    <script type="text/javascript" src="fcsort.js"></script>
    <script type="text/javascript">
        var select = "<%=create%>";
        var surl = "<%=currentPath%>";
        var params = "<%=params%>";
        var yeardays = "";
        var startdate = "<%=startdate%>";
        var enddate = "<%=enddate%>";
        var brhid = "<%=brhid%>";
        var create = "<%=create%>";
        var colspan = "<%=colspan%>";
        var cou = "";
        /*
*   ������    |      ˵��
*------------------------------
* printTable() |  ��ӡԤ��TableЧ��
*
*/

function printTables(){
	var s="";
	s=showTable.innerHTML;
    document.all.aaaa.style.display='none';
    document.all.bbbb.innerHTML=s;
    window.print();
    document.all.bbbb.style.display='none';
   document.all.aaaa.style.display='';

}
    </script>
</head>
<body background="/images/checks_02.jpg">
<form action="<%=currentPath%>list.jsp" name="listform" id="listform">
<input name="referValue" type="hidden">
<input type="hidden" name="brhid" value="<%=brhid%>">

<div id=aaaa align="center">
<TABLE borderColor=#999999 cellSpacing=0 cellPadding=0 align=center
       border=1 width=100% bgcolor=#f1f1f1>
<TBODY>
<TR Align=center>
<TD align="center">
<TABLE width=95%>
<TBODY>
<TR align="center">
<TD align="center">
<table width=900 border=0 align="center" cellPadding=0
       cellSpacing=0>
   
    <TR>
        <td id=detailTab align="center">
            <div class=head align="right">
            <input type=button class=button value=" �� ��EXCEL " onclick="printTable()">
                &nbsp;&nbsp;
                <input type=button class=button value=" �� ӡ " onclick="printTables()">
                 &nbsp;&nbsp;
                
                <input type="button" class="button" value=" �� �� " onclick="self.close()">
                &nbsp;
            </div>
        </td>
    </TR>
   
</TABLE>
<div id='showTable' align=center width=900>
<table cellSpacing=0 cellPadding=0 width=900 border=0>
        <tr class=title align="center">
            <td align=center class=title>
            <font size="2">
    ��ҵ��λ��������϶���
               </font>
            </td>
        </tr>
        </table>

<div align=center width="900">
    <table cellSpacing=0 cellPadding=0 width="900" border=0>
        <tr class=title align="center" height="2">
            <td align=center class=title>
            </td>
        </tr>
        <%
         SystemDate.refresh();
        
        String today=create.substring(0,4)+"��"+create.substring(5,7)+"��";
         %>
        <tr class=title align="center">
            <td class=title align="left">
                �����磺<%=name%>
            </td>
            <td align=center class=title>
                 ��Ч���ڣ�<%=today%>
            </td>
            <td align=right class=title>
               ���֣������
            </td>
            <td align=right class=title>
                ��λ����Ԫ
            </td>
        </tr>
    </table>
</div>
<table borderColor="#111111" cellSpacing='0' cellPadding='2'
	width='900' align='center' border='1'id="checkTable">
<TBODY>
<%
         CompanyInfo info=dao.getCompanyInfo();
           String lastYear=SystemDate.getLastYearDate(create, "-");
		lastYear=lastYear.replaceAll("-31", "-01");
		String qyear=String.valueOf((Integer.parseInt(lastYear.substring(0, 4))-1));
		String dqyear=String.valueOf((Integer.parseInt(lastYear.substring(0, 4))-2));
		String lyear=lastYear.substring(0,4);
		String nowdate=info.getCreate();
		
		String nowdateYear="";
		String nowdateMonth="";
		if(nowdate !=null){
		 nowdateYear=nowdate.substring(0,4);
	     nowdateMonth=nowdate.substring(5,7);
		}
 %>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
       ���������
    </td>
    <td  align="left" valign="bottom" class="head" width="200" colspan="3">
     <%=info.getName() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="60">
        ���������
    </td>
    <td  align="left" valign="bottom" class="head" width="60">
     <%=info.getClienttype() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="100" colspan="2">
        �����˵����ܲ��Ż�ĸ��˾
    </td>
    <td  align="left" valign="bottom" class="head" width="100">
   <%=info.getSuperCompany() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="90" colspan="2">
         ����������  
    </td>
    <td  align="center" valign="bottom" class="head" width="40">
      <%=info.getLawPerson() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="80" colspan="2">
        ��ҵ��������
    </td>
    <td  align="center" valign="bottom" class="head" width="70" colspan="2">
      <%=info.getFounddate() %>&nbsp;
    </td>
</tr>

<tr>
    <td  align="center" valign="bottom" class="head" width="100">
       ��Ӫ��ַ
    </td>
    <td  align="left" valign="bottom" class="head" width="200" colspan="3">
     <%=info.getAddresslaw() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="60">
        ע���ʱ�
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
     <%=info.getCapitalamt() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="100" colspan="2">
        ��Ӫ��Ŀ
    </td>
    <td  align="left" valign="bottom" class="head" width="100">
    <%=info.getBizcope() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="90" colspan="2">
             ��Ӫҵ������
    </td>
    <td  align="right" valign="bottom" class="head" width="40">
     <%=info.getCuzb11()==null?"":info.getCuzb11()%>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="80" colspan="2">
       �����ʻ�������
    </td>
    <td  align="left" valign="bottom" class="head" width="70" colspan="2">
      <%=info.getBrhid() %>&nbsp;
    </td>
</tr>
        
<tr>
    <td  align="center" valign="bottom" class="head" width="100" rowspan="2">
       ����ͬ��
    </td>
    <td  align="center" valign="bottom" class="head" width="60" rowspan="2">
     �������
    </td>
    <td  align="center" valign="bottom" class="head" width="80" rowspan="2">
      �����ͬ��� 
    </td>
    <td  align="center" valign="bottom" class="head"  width="60" rowspan="2">
    ���ʽ
    </td>
    <td  align="center" valign="bottom" class="head" width="60" rowspan="2">
       ��ֹ����
    </td>
    <td  align="center" valign="bottom" class="head" width="60" rowspan="2">
  չ�ں�����
    </td>
    <td  align="center" valign="bottom" class="head" width="100" colspan="2" >
     ������;
    </td>
    <td  align="center" valign="bottom" class="head" width="100" rowspan="2">
     ��֤������ 
    </td>
    <td  align="center" valign="bottom" class="head" width="40" rowspan="2">
     ��Ѻ������
    </td>
    <td  align="center" valign="bottom" class="head" width="50" rowspan="2">
    ����Ѻ����ֵ��������ֵ
    </td>
    <td  align="center" valign="bottom" class="head" width="50" rowspan="2">
     ����Ѻ����
    </td>
    <td  align="center" valign="bottom" class="head" width="40" rowspan="2">
    �Ƿ�����Ѻ�Ǽ�
    </td>
    <td  align="center" valign="bottom" class="head" width="40" rowspan="2">
   ��Ϣ�������
    </td>
    <td  align="center" valign="bottom" class="head" width="35" rowspan="2">
    ǷϢ���
    </td>
    <td  align="center" valign="bottom" class="head" width="35" rowspan="2">
    ���õȼ�
    </td>
    
</tr>
<tr>
  <td  align="center" valign="bottom" class="head" width="50" >
   ��ͬȷ����;
    </td>
     <td  align="center" valign="bottom" class="head" width="50" >
    ʵ����;
    </td>
</tr>
 <%         
            int i=0;
            List list=dao.getResult2();
            i=list.size();
            int j=3-i;
            for(int m=0;m<list.size();m++){
            CompanyInfo2 info2=(CompanyInfo2)list.get(m);
            String ifreg=info2.getIfreg();
            if(ifreg !=null){
            if(ifreg.equals("2")){
            ifreg="��";
            }else{
            ifreg="��";
            }
            }
            double due1=Double.valueOf(info2.getDuebal1()).doubleValue();
            double due2=Double.valueOf(info2.getDuebal2()).doubleValue();
            double due3=Double.valueOf(info2.getDuebal3()).doubleValue();
            double due4=due1+due2+due3;
            String due5=DBUtil.doubleToStr1(due4);
            %>
<tr>
    <td  align="center" valign="bottom" class="head" width="100" >
      <%=DBUtil.fromDB(info2.getScontractno())%>
      <%
      if(info2.getFcno()!=null && !info2.getFcno().trim().equals("")){
       %>
      -
      <%
      }
       %>
      <%=DBUtil.fromDB(info2.getFcno())%>&nbsp;
    </td>
    <td  align="center" valign="bottom" width="60" class="head"  >
   <%=info2.getTypeno() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80"class="head"  >
     <%=info2.getContractamt() %>&nbsp;
       
    </td>
    <td  align="center" valign="bottom" width="60"class="head"  >
     <%=info2.getLoantype3() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="60" >
         <%=info2.getPaydate()%><%
         if(!info2.getPaydate().equals("")|| !info2.getEnddate().equals("")){
         %>
         ��
         <%
         }
         %><%=info2.getEnddate() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="60" >
            <%=info2.getEnddate() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="55"  >
           <%=info2.getCmt1() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="55"  >
   <%=info2.getCmt2() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="80" >
      <%=info2.getClientname() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="40" >
       <%=info2.getPledgename() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="50" >
      <%=info2.getEstimateprice() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="50" >
       <%=info2.getPldgmortrate() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="40" >
      <%=ifreg%>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="40" >
   <%=info2.getLoancat2() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="35" >
    <%=due5 %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="35">
    <%=info2.getCreditclass() %>&nbsp;
    </td>
    
</tr>
<%
    }
if(j>0){
    for(int a=0;a<j;a++){
    %>
    <tr height="25">
    <td  align="center" valign="bottom" class="head" width="100"  >
    &nbsp;
    </td>
    <td  align="center" valign="bottom"width="60" class="head"  >
&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80"class="head"  >
    &nbsp;
       
    </td>
    <td  align="center" valign="bottom" width="60"class="head"  >
    &nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="60" >
        &nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="60" >
          &nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="55"  >
        &nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="55"  >
   &nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="80" >
     &nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="80" >
      &nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="40" >
     &nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="50" >
     &nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="40" >
    &nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="40" >
  &nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="35" >
   &nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="35">
  &nbsp;
    </td>
    
</tr>
    
  <% 
    }
}
 %>
<tr>
    <td  align="center" valign="middle" class="head" width="100" rowspan="4">
     ����˹̶��ʲ�״��
    </td>
    <td  align="center" valign="bottom" width="60" class="head"  >
     �ʲ�����
    </td>
    <td  align="center" valign="bottom" width="80" class="head" >
       ʵ�����(����)
    </td>
    <td  align="center" valign="bottom" width="60" class="head" >
      �Ѱ�֤���
    </td>
    <td  align="center" valign="bottom" class="head" width="60">
      ������ֵ
    </td>
    <td  align="center" valign="bottom" class="head" width="60">
      ��Ѻ״��
    </td>
    <td  align="left" valign="top" class="head" width="200" rowspan="4" colspan="3">
      ��������:<%=info.getDbfx()==null?"":info.getDbfx()%>
    </td>
    <td  align="center" valign="bottom" class="head" width="130" colspan="3">
      Ŀǰ������Դ
    </td>
    <td  align="left" valign="top" class="head" width="150" rowspan="4" colspan="4">
      ��������˵��:<%=info.getQtsm()==null?"":info.getQtsm() %>
    </td>
</tr>
<tr>
<td  align="center" valign="bottom"width="60" class="head" >
     ����ʹ��Ȩ
    </td>
    <td  align="right" valign="bottom" width="80"class="head">
     <%=info.getArea1()==null?"0.00":info.getArea1() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60"class="head">
     <%=info.getMj1()==null?"0.00":info.getMj1() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60"class="head" >
     <%=info.getEstimate1()==null?"0.00":info.getEstimate1() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" width="60"class="head" >
     <%=info.getMortgaged1()==null?"":info.getMortgaged1() %>&nbsp;
    </td>
    <td  align="left" valign="bottom" class="head" width="130" colspan="3">
    ����:<%=info.getHkly1()==null?"":info.getHkly1() %>
    </td>
</tr>
<tr>
<td  align="center" valign="bottom" width="60" class="head" >
      ����
    </td>
    <td  align="right" valign="bottom" width="80" class="head" >
     <%=info.getArea2()==null?"0.00":info.getArea2() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60" class="head" >
     <%=info.getMj2()==null?"0.00":info.getMj2() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60"class="head" >
      <%=info.getEstimate2()==null?"0.00":info.getEstimate2()  %>&nbsp;
    </td>
    <td  align="center" valign="bottom"width="60" class="head" >
      <%=info.getMortgaged2()==null?"":info.getMortgaged2()  %>&nbsp;
    </td>
    <td  align="left" valign="bottom" class="head" width="130" colspan="3">
    ��Σ�<%=info.getHkly2()==null?"":info.getHkly2() %>
    </td>
</tr>
<tr>
<td  align="center" valign="bottom" width="60" class="head">
      ��Ҫ�豸����ͨ����
    </td>
    <td  align="right" valign="bottom" width="80" class="head">
     <%=info.getQty()==null?"0.00":info.getQty() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" width="60" class="head">
     &nbsp;
    </td>
    <td  align="right" valign="bottom" width="60"class="head" >
       <%=info.getEstimate3()==null?"0.00":info.getEstimate3() %>&nbsp;
    </td>
    <td  align="center" valign="bottom" class="head" width="60">
     <%=info.getMortgaged3()==null?"":info.getMortgaged3() %>&nbsp;
    </td>
    <td  align="left" valign="top" class="head" width="130" colspan="3">
    ������<%=info.getHkly3()==null?"":info.getHkly3()%>
    </td>
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
       ��Ҫ����ָ��
    </td>
    <td  align="center" valign="bottom" width="60"class="head" >
     <%=dqyear%> �� 12 ��
    </td>
    <td  align="center" valign="bottom" width="80"class="head" >
       <%=qyear%> �� 12 ��
    </td>
    <td  align="center" valign="bottom" width="60"class="head" >
      <%=lyear %>�� 12 ��
    </td>
    <td  align="center" valign="bottom" class="head" width="60">
       <%=nowdateYear%>�� <%=nowdateMonth%>��
    </td>
    <td  align="left" valign="top" class="head" width="530" rowspan="4" colspan="11">
     �������:<%=info.getCwfx()==null?"":info.getCwfx()%>&nbsp;
    </td>
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
       ���ֽ�����
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
     <%=info.getCuzb013() ==null?"0.00":info.getCuzb013() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80" class="head">
       <%=info.getCuzb012() ==null?"0.00":info.getCuzb012() %>&nbsp;  
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
      <%=info.getCuzb011() ==null?"0.00":info.getCuzb011() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
       <%=info.getCuzb01() ==null?"0.00":info.getCuzb01() %>&nbsp;
    </td>
    
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
      ��Ӫ�Ծ��ֽ�����
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
      <%=info.getCuzb023() ==null?"0.00":info.getCuzb023() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80" class="head" >
       <%=info.getCuzb022() ==null?"0.00":info.getCuzb022() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
     <%=info.getCuzb021() ==null?"0.00":info.getCuzb021() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
    <%=info.getCuzb02() ==null?"0.00":info.getCuzb02() %>&nbsp;
    </td>
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
     Ͷ�ʻ���ֽ�����
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
    <%=info.getCuzb033() ==null?"0.00":info.getCuzb033() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80" class="head">
       <%=info.getCuzb032()  ==null?"0.00":info.getCuzb032()%>&nbsp;
    </td>
    <td  align="right" valign="bottom"width="60" class="head">
       <%=info.getCuzb031()  ==null?"0.00":info.getCuzb031()%>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
      <%=info.getCuzb03()  ==null?"0.00":info.getCuzb03()%>&nbsp;
    </td>
    
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
      ���ʻ���ֽ�����
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
      <%=info.getCuzb043() ==null?"0.00":info.getCuzb043() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80" class="head">
        <%=info.getCuzb042() ==null?"0.00":info.getCuzb042() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
      <%=info.getCuzb041() ==null?"0.00":info.getCuzb041() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
       <%=info.getCuzb04() ==null?"0.00":info.getCuzb04() %>&nbsp;
    </td>
    <td  align="left" valign="top" class="head" width="530" rowspan="4" colspan="11">
     �ǲ������:<%=info.getFcwfx()==null?"":info.getFcwfx()%>&nbsp;
    </td>
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
     ��������
    </td>
    <td  align="right" valign="bottom" width="60"class="head" >
      <%=info.getCuzb053() ==null?"0":info.getCuzb053() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80" class="head">
       <%=info.getCuzb052() ==null?"0":info.getCuzb052()%>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
     <%=info.getCuzb051() ==null?"0":info.getCuzb051()%>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
      <%=info.getCuzb05()==null?"0":info.getCuzb05() %>&nbsp;
    </td>
    
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
    �ʲ���ծ��(%)
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
       <%=info.getCuzb063()==null?"0":info.getCuzb063() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80" class="head">
        <%=info.getCuzb062()==null?"0":info.getCuzb062() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
        <%=info.getCuzb061()==null?"0":info.getCuzb061() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
       <%=info.getCuzb06() ==null?"0":info.getCuzb06()%>&nbsp;
    </td>
    
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
    ����������(%)
    </td>
    <td  align="right" valign="bottom" width="60"class="head">
        <%=info.getCuzb073()==null?"0":info.getCuzb073() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80"class="head" >
       <%=info.getCuzb072()==null?"0":info.getCuzb072() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60" class="head" >
       <%=info.getCuzb071()==null?"0":info.getCuzb071() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
        <%=info.getCuzb07()==null?"0":info.getCuzb07() %>&nbsp;
    </td>
    
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
       �ʲ�������(%)
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
      <%=info.getCuzb083()==null?"0":info.getCuzb083() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80" class="head" >
        <%=info.getCuzb082()==null?"0":info.getCuzb082() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60" class="head" >
      <%=info.getCuzb081()==null?"0":info.getCuzb081() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
       <%=info.getCuzb08()==null?"0":info.getCuzb08() %>&nbsp;
    </td>
    <td  align="left" valign="top" class="head" width="530" rowspan="3" colspan="11">
    �����������ɼ������������:<%=info.getFccmt1()==null?"":info.getFccmt1() %>
    </td>
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
      Ӧ���˿���ת��(��/��)
    </td>
    <td  align="right" valign="bottom" width="60" class="head" >
     <%=info.getCuzb093()==null?"":info.getCuzb093() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80" class="head" >
      <%=info.getCuzb092()==null?"":info.getCuzb092() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
    <%=info.getCuzb091()==null?"":info.getCuzb091() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
     <%=info.getCuzb09()==null?"":info.getCuzb09() %>&nbsp;
    </td>
    
</tr>
<tr>
    <td  align="center" valign="bottom" class="head" width="100">
     �����ת��(��/��)
    </td>
    <td  align="right" valign="bottom" width="60" class="head">
      <%=info.getCuzb103()==null?"":info.getCuzb103() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="80" class="head">
         <%=info.getCuzb102() ==null?"":info.getCuzb102()%>&nbsp;
    </td>
    <td  align="right" valign="bottom" width="60" class="head" >
         <%=info.getCuzb101()==null?"":info.getCuzb101() %>&nbsp;
    </td>
    <td  align="right" valign="bottom" class="head" width="60">
      <%=info.getCuzb10() ==null?"":info.getCuzb10()%>&nbsp;
    </td>
    
</tr>
<tr>
    <td  align="left" valign="top" class="head" width="240" colspan="3">
    �Ŵ����۷������:<%=info.getFccmt2()==null?"":info.getFccmt2() %>
    </td>
    <td  align="left" valign="top" class="head" width="280" colspan="5">
   �Ŵ����ŷ������:<%=info.getFccmt3()==null?"":info.getFccmt3() %>
    </td>
    <td  align="left" valign="top" class="head" width="320" colspan="6">
      �ϼ������ŷ������:<%=info.getFccmt4()==null?"":info.getFccmt4() %>
    </td>
    <td  align="center" valign="top" class="head" width="35">
      ������
    </td>
    <td  align="center" valign="top" class="head" width="35">
    &nbsp;
    </td>
    
</tr>
</TBODY>
</TABLE>
</div>

</TD>
</TR>
</TBODY>
</TABLE>

</TD>
</TR>
</TBODY>
</TABLE>

</div>
<div id=bbbb>
</div>
<div id=over style="position:absolute; top:0; left:0; z-index:1; display:none;"
     width=100% height=700>
    <table width=100% height=700>
        <tr>
            <td>
            </td>
        </tr>
    </table>
</div>
<div id=sending style="position:absolute; top:50%; left:37%; z-index:2; display:none;" align=center>
    <table width="250" height="80" border="0" cellpadding="0" cellspacing="1">
        <tr>
            <td bgcolor=#999999 align=center height=20 width=100>
                &nbsp;
            </td>
        </tr>
        <tr>
            <td bgcolor=eeeeee align=center height=50>
                ���ڴ����С���
            </td>
        </tr>
        <tr>
            <td bgcolor=#cacaca align=center height=10>
                &nbsp;
            </td>
        </tr>
    </table>
</div>
</form>
</body>
<script type="text/javascript">
   
</script>
</html>

