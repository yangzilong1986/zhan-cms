<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.platform.db.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.form.control.impl.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>

<%
MyDB.getInstance().removeCurrentThreadConn("workbench.list.jsp"); //added by JGO on 2004-07-17
%>

<%
request.setCharacterEncoding("GBK");
UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
if(um==null){
	response.sendRedirect("../fcworkbench/error.jsp");
}
String brhId = SCUser.getBrhId(um.getUserName()); //登录用户所在网点
String typeNo = request.getParameter("typeNo");
String clientName = request.getParameter("clientName");
String bmActType=request.getParameter("bmActType");
String typeNoCondition="";
String clientNameCondition="";
String bmActTypeCondition="";
String otherCondition="";
if(typeNo!=null && typeNo.trim().length()>0){
    typeNoCondition=" and bmtable.typeno="+typeNo;
}
else{
    typeNo="";
}
if(clientName!=null && clientName.trim().length()>0){
    clientNameCondition=" and clientname like '%"+DBUtil.toDB(clientName)+"%'";
}
else{
    clientName="";
}
if(bmActType!=null && bmActType.trim().length()>0){
    bmActTypeCondition=" and bmacttype="+bmActType;
}
else{
    bmActType="";
}
otherCondition=typeNoCondition+clientNameCondition+bmActTypeCondition;
boolean isLast=false;
int curPage=1;
String pageStr=request.getParameter("page");
if(pageStr!=null && pageStr.trim().length()>0){
    curPage=Integer.parseInt(pageStr);
}

String flag=(String)request.getParameter("flag");
if(flag==null||flag.trim().length()<1){
    flag="read";
}
else{
    flag=flag.trim();
}
%>

<jsp:include page="/checkpermission.jsp"/>

<html>
<head>
<title>信贷管理</title>
<link href="../css/platform.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type="text/css">
<!--
body {
	margin-top: 5px;
}
-->
</style>
<script language="JavaScript" type="text/JavaScript">
<!--
function checkForm1(){
    if ( check(form1)) {
        form1.submit();
    }
}
function checkSubmit(){
    return true;
}

function distribute(bmno,bmtransno,clientname,bmacttype,brhid,type){
    var url='distribute.jsp?BMNO='+trim(bmno)+'&BMTRANSNO='
        +bmtransno+'&CLIENTNAME='+trim(clientname)
	+'&flag=<%=flag%>'+'&BMACTTYPE='+bmacttype+'&BRHID='+trim(brhid)+'&BMTYPE='+type;
    window.open(url,'FI2','height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
}

function newAffair(affairName){
    var url='new_affair.jsp?affairName='+affairName;
    window.open(url,'FI2','height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
}

function buttonDisabled(){
    with(form2){
        a.disabled="true";
        b.disabled="true";
        c.disabled="true";
    }
}
//-->
</script>
<script src='/js/check.js' type='text/javascript'></script>
<script src='/js/pagebutton.js' type='text/javascript'></script>
</head>
<body background="../images/checks_02.jpg">
<form name="distributeForm" action="distribute.jsp" method="post" id="distributeForm">
<input type="hidden" name="BMNO" value="">
<input type="hidden" name="BMTRANSNO" value="">
<input type="hidden" name="CLIENTNAME" value="">
<input type="hidden" name="flag" value="">
</form>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle">
      <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" >
        <tr align="left">
          <td height="30" bgcolor="#4477AA"> <img src="../images/form/xing1.jpg" align="absmiddle"> <font size="2" color="#FFFFFF"><b>工作台</b></font> <img src="../images/form/xing1.jpg" align="absmiddle"></td>
        </tr>
        <tr align="center">
          <td height="260" valign="middle">
            <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
              <tr>
                <td width="20">&nbsp;</td>
                <td align="center" valign="middle">
                  <script src='/js/querybutton.js' type='text/javascript'></script>
                  <script src='/js/meizzDate.js' type='text/javascript'></script>
                  <table>
                    <tr>
                      <td>
                        <table align='center' cellpadding='0' cellspacing='0' border='0' bgcolor='#AAAAAA' width='538'>
                          <form action="list.jsp" method="post" name="form1" id="form1">
                            <input type="hidden" name="flag" value="<%=flag%>">
                            <tr>
                              <td height="0">
                                <table id="findDiv" class="query_table" cellpadding='0' cellspacing='0' border='0' style='display:none'>
                                  <tr class="query_tr">
                                    <td class="query_td" width="80%">
                                      <table class='query_form_table' id='query_form_table' cellpadding='1' cellspacing='1' border='0'>
                                        <tr class="query_form_tr" nowrap>
                                          <td height="19" nowrap class="page_form_title_td">客户名称</td>
                                          <td class="page_form_td" ><input name="clientName" value="<%=clientName%>" type="text" class="page_form_text" id="clientName" mayNull="1" dataType="1" errInfo="客户名称"></td>
                                        </tr>
                                        <tr>
                                          <td width="59" nowrap class="page_form_title_td">业务状态</td>
                                          <td width="140" class="page_form_td" >
                                            <select name="bmActType" class="page_form_select">
<%
EnumerationBean actTypes=EnumerationType.getEnu("BMActType");
for (Iterator iter = actTypes.getKeys().iterator(); iter.hasNext(); ) {
    Object item = (Object)iter.next();
%>
                                              <option value="<%=item.toString()%>" <%=bmActType.equals(""+item.toString())?" selected":""%>><%=actTypes.getValue(item)%></option>
<%
}
%>
                                              <option value="" <%=bmActTypeCondition.equals("")?" selected":""%>>全部</option>
                                          </select></td>
                                        </tr>
                                        <tr class="query_form_tr" nowrap>
                                          <td width="87" nowrap class="page_form_title_td">业务类型</td>
                                          <td width="115" class="page_form_td" >
                                            <select name="typeNo" class="page_form_select">
<%
EnumerationBean types=EnumerationType.getEnu("BMType");
for (Iterator iter = types.getKeys().iterator(); iter.hasNext(); ) {
    Object item = (Object)iter.next();
%>
                                              <option value="<%=item.toString()%>" <%=typeNo.equals(""+item.toString())?" selected":""%>><%=types.getValue(item)%></option>
<%
}
%>
                                              <option value="" <%=typeNoCondition.equals("")?" selected":""%>>全部</option>
                                            </select>
                                          </td>
                                        </tr>
                                    </table></td>
                                    <td class="query_td" width="20%" align="center">
                                      <table border='0' width='100%' bgcolor='#F1F1F1'>
                                        <tr>
                                          <td nowrap valign="top"><input type="button" class="query_button" name="Submit" value=" 检 索 " onclick="checkForm1();"></td>
                                        </tr>
                                        <tr>
                                          <td nowrap valign="top"><input type="reset" class="query_button" name="reset" value=" 重 置 "></td>
                                        </tr>
                                    </table></td>
                                  </tr>
                              </table></td>
                            </tr>
                            <tr>
                              <td height="0" align="center"><img id='findDivHandle' title='点击查询' onClick='menuMove()' src='/images/form/button1.jpg' style='cursor:hand;'></td>
                            </tr>
                          </form>
                        </table>
                        <table cellpadding='0' cellspacing='0' border='0'>
                          <tr>
                            <td height='5'></td>
                          </tr>
                        </table>
                        <table class='list_form_table' width='530' align='center' cellpadding='0' cellspacing='1' border='0'>
                          <form id='querywinform' method='post' action='/templates/defaultform.jsp' target='_self'>
                            <tr class='list_form_title_tr'>
                              <td width='130' class='list_form_title_td' nowrap align="center">客户姓名</td>
                              <td width='60' class='list_form_title_td' nowrap align="center">业务网点</td>
                              <td width='70' class='list_form_title_td' nowrap align="center">金额</td>
                              <td width='70' class='list_form_title_td' nowrap align="center">业务状态</td>
                              <td width='100' class='list_form_title_td' nowrap align="center">业务类型</td>
                              <td width='70' class='list_form_title_td' nowrap align="center">业务号</td>
                              <td width='30' class='list_form_title_td' nowrap align="center">办理</td>
                            </tr>
                            <%
Collection affairs=AffairFactory.findAffairsByBrhIdAndConditionsWithPage(brhId,otherCondition,curPage,SessionInfo.getLoginUserNo(session));
//System.out.println("affairs.size()=="+affairs.size());

if(affairs.size()>10){
    isLast=false;
}
else{
    isLast=true;
}
Iterator iter = affairs.iterator();

for (int i = 0; i < 10; i++) {
    if(iter.hasNext()){
    	Affair affair = (Affair)iter.next();
    	String tdClass="list_form_td";
    	if(!affair.isViewed()){
        	tdClass="list_form_td_unviewed";
    	}

%>
                            <tr class='list_form_tr'>
                              <td nowrap class='<%=tdClass%>'><%=affair.getClientName()%></td>
                              <td nowrap class='<%=tdClass%>' align="center"><%=affair.getBrhId()%></td>
                              <td nowrap class='<%=tdClass%>' align="right"><%=DBUtil.doubleToStr1(affair.getAppAmt())%></td>
                              <td nowrap class='<%=tdClass%>' align="center"><%=affair.getBmStatusName()%></td>
                              <td nowrap class='<%=tdClass%>' align="center"><%=affair.getTypeName()%></td>
                              <td nowrap class='<%=tdClass%>' align="center"><%=affair.getBmNo()%></td>
                              <td nowrap class='<%=tdClass%>' align="center"><a class="list_edit_href" href="#" onclick="distribute('<%=affair.getBmNo()%>','<%=affair.getBmTransNo()%>','<%=affair.getClientName()%>','<%=affair.getBmActType()%>','<%=affair.getBrhId()%>','<%=affair.getTypeNo()%>');">办理</a></td>
                            </tr>
                            <%
	}
	else{
%>
                            <tr class='list_form_tr'>
                              <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
                            </tr>
                            <%
	}
}
%>
                          </form>
                      </table></td>
                    </tr>
                </table></td>
                <td width="20">&nbsp;</td>
              </tr>
          </table></td>
        </tr>
        <tr height="35" align="center" valign="middle">
          <td align="center">
            <table border="0" cellspacing="0" cellpadding="0" width="538">
              <tr>
                <td nowrap>
                  <form action="list.jsp" name="form2">
                    <input type="hidden" name="typeNo" value="<%=typeNo%>">
                    <input type="hidden" name="bmActType" value="<%=bmActType%>">
                    <input type="hidden" name="clientName" value="<%=clientName%>">
                    <input type="hidden" name="flag" value="<%=flag%>">
                    <input type="hidden" name="page" value="<%=curPage%>">
                  </form>
                  <table class='list_button_tbl'>
                    <tr class="list_button_tbl_tr">
                      <td class="list_form_button_td"><input name='creditRegist' type='button' class='list_button_active' id="creditRegist2" onClick="newAffair(this.name);" value='贷款业务登记'></td>
                      <td class="list_form_button_td"><input name='non-performingLoanRegist' type='button' class='list_button_active' id="non-performingLoanRegist" onClick="newAffair(this.name);" value='不良核销登记'>&nbsp;&nbsp;</td>
                      <td class="list_form_button_td"><input type='submit' name='a' class='list_button_active' value=' 刷 新 ' onClick="buttonDisabled();form2.submit()"></td>
                      <td class="list_form_button_td"><input type='submit' name='b' class='<%=(curPage<=1)?"list_button_disabled":"list_button_active"%>' value='上一页' <%=(curPage<=1)?"disabled":""%>  onClick="buttonDisabled();form2.page.value=--form2.page.value;form2.submit()"></td>
                      <td class="list_form_button_td"><input type='submit' name='c' class='<%=(isLast)?"list_button_disabled":"list_button_active"%>' value='下一页' <%=isLast?"disabled":""%> onClick="buttonDisabled();form2.page.value=++form2.page.value;form2.submit()"></td>
                    </tr>
                </table></td>
              </tr>
          </table></td>
        </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
<script language="javascript">
document.title="工作台";
document.focus();
</script>

<%
MyDB.getInstance().removeCurrentThreadConn("workbench.list(END).jsp"); //added by JGO on 2004-07-17
%>