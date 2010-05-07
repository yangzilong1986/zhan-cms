<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cms.cm.report.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="java.util.*" %>
<%
//request.setAttribute("flag","read");
String flag="read";
Object oValue=request.getParameter("flag");
if(oValue==null){
    oValue=request.getAttribute("flag");
}
String buttonDisabled="disabled";
if(oValue!=null){
    String value=(String)oValue;
	if(value.trim().equals("write")){
	    flag="write";
	    buttonDisabled="";
	}
}


//Report report = ReportType.getReportTemplateByType("1111");
String reportType=request.getParameter("REPORTTYPE");

Map types=Report.getReportTypes();


if(reportType==null||reportType.equals("")){
    for (Iterator iter = types.keySet().iterator(); iter.hasNext(); ) {
            String item = (String)iter.next();
            reportType=item;
            break;
    }
}
Report report = ReportType.getReportTemplateByType(reportType);
String entityNo=request.getParameter("ENTITYNO");



boolean isEdit=false;
String readOnly="";
String disabled="";
String actionJsp="addReport.jsp";

if(request.getParameter("REPORTDATE")!=null){
    isEdit=true;
    report.setReportDate(request.getParameter("REPORTDATE"));
    report.setEntityNo(entityNo);
    report.setReportType(request.getParameter("REPORTTYPE"));
    report.getValues();
}
if(isEdit){
    readOnly=" readonly";
    actionJsp="editReport.jsp";
	disabled=" disabled";
}else{
    report.setReportName(report.getReportTypeName());
    report.setEntityNo(entityNo);
}

%>
<html>
	<head>
		<title>defaultform</title>
		<link href="/css/platform.css" rel="stylesheet" type="text/css">

<script language="JavaScript" type="text/JavaScript">

<!--
function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}

function checkSubmit2(){
	//alert("gqsy");

	with(form1){
		//alert(r1.value);
    <%for (int i = 0; i < report.getLimits().length; i++) {%>
    var a = <%=report.getCondition(i)[0]%>;
    var b = <%=report.getCondition(i)[1]%>;
		if(Math.abs(a-b)>0.01){
		    alert("数据应该保证 : <%=report.getMessage(i)%>");
		    return false;
		}
    <%}%>
	}

	return true;
}

function checkForm1(){
	//alert("xxx");
	if ( check(form1) && checkSubmit2()) {
			//alert("success");
			form1.submit();
	}
}

function deleteCheck(){
    	if ( confirm("请确认要删除这个报表吗?") ) {
		form1.action='/report/deleteReport.jsp';
		form1.submit();
	    }
}

function checkClose(){
    	if ( confirm("确认关闭这个窗口?") ) {
		window.close();
	    }
}

function moveToNext(index){
    //alert(window.event.keyCode);
    theNextField=eval("form1.r"+index);
    if(window.event.keyCode==13){
        theNextField.select();
    }
}

function moveToSave(index){
    if(window.event.keyCode==13){
	form1.submit1.select();
    }
}

function ifMoveToNext(index){
    //theField=eval("form1.r"+(index-1));
    //alert(theField.value);
    //alert('onchange');
}
function ifMoveToSave(){

}

function likeInputCompleted(filed){
    //if
}
//-->
</script>
<script language="JavaScript" src="../js/meizzDate.js"></script>
<script src='/js/check.js' type='text/javascript'></script>
<script src='/js/pagebutton.js' type='text/javascript'></script>
</head>
	<body background="../images/checks_02.jpg">

<table height="400" border="" align="center" cellspacing="1" bordercolorlight="#336699" bordercolordark="#336699" bgcolor="#9DBBD9">
  <tr align="left">

    <td height="30" class="form_title_label"> 报表</td>
			</tr>
			<tr align="center" valign="middle">

    <td height="350">

	<form action="/report/<%=actionJsp%>" method="post" name="form1">
        <input type="hidden" name="Plat_Form_Request_Form_ID" value="CMREDL">
	<input type="hidden" name="Plat_Form_Request_Event_ID" value="0">
	<input type="hidden" name="reportType" value="<%=reportType%>">
        <table width="100%" border="0" class="page_form_table">
          <tr class="page_form_td">
          <td width="55">报表名称 </td>
          <td width="175">
          <input name="reportName" type="text" class="page_form_text" id="reportName" value="<%=report.getReportName().trim()%>" size="30" maxlength="30" mayNull='0' errInfo="报表名称"></td>
          <td width="49">报表日期</td>
          <td width="231"><input name="reportDate" type="text" class="page_form_text" id="reportDate" size="10" maxlength="10" value="<%=report.getDataString()%>" readOnly><input type="button" value="…" class="page_form_refbutton" onclick="setday(this,document.all.reportDate)" <%=disabled%>></td>
        </tr>
        <tr class="page_form_td">
            <td height="25">客户代码</td>
          <td><input name="entityNo" type="text" class="page_form_text" id="entityNo" size="13" maxlength="13" value="<%=report.getEntityNo()%>" readonly></td>
            <td>报表类型</td>
          <td><select name="reportType2" id="reportType2" onChange="MM_jumpMenu('self',this,0)">
                <%
                   if(!isEdit){
                    for (Iterator iter = types.keySet().iterator(); iter.hasNext(); ) {
                        String item = (String)iter.next();
                        String url="";
                        String selected="";
                        url="/report/report.jsp?REPORTTYPE="+item+"&flag="+flag+"&ENTITYNO="+entityNo;
                        if(item.equals(reportType)){
                                selected=" selected";
                        }
                %>
                <option value="<%=url%>" <%=selected%>><%=types.get(item)%></option>
                <%}
                   }else{%>
                   <option value="<%=reportType%>"><%=types.get(reportType)%></option>
                <%}%>
              </select></td>
        </tr>
      </table>
        <br>
        <table border="0" cellspacing="1" bgcolor="#FFFFFF" class="page_form_table">
          <tr bgcolor="#336699" class="list_form_td">
            <td height="21" nowrap>项目</td>
            <td nowrap>序号</td>
            <td nowrap>数额</td>
            <td nowrap>项目</td>
            <td nowrap>序号</td>
            <td nowrap>数额</td>
          </tr>
          <%
	        Item[] items=report.getPrintItems();
			//System.out.println(items.length/2);
			//System.out.println(items.length%2);
			int rows=items.length/2+items.length%2;

			for(int i=0;i<rows;i++){
	  %>
          <tr bgcolor="#336699" class="list_form_td">
            <td  height="21" nowrap><%=items[i].getItemName()%></td>
            <td nowrap><%=items[i].getItemNo()%></td>
            <td nowrap>
              <input name="r<%=items[i].getItemNo()%>" type="text" class="page_form_text" id="1" size="15" maxlength="15" value="<%=DBUtil.doubleToStr(items[i].getItemValue())%>" tabindex="<%=items[i].getItemNo()%>" mayNull="0" dataType="3" errInfo="<%=items[i].getClearItemName()%>" precision="14" decimalDigits="2" onKeyUp="<%=(i!=items.length-1)?"moveToNext("+(items[i].getItemNo()+1)+")":"moveToSave("+(items[i].getItemNo()+1)+")"%>" onchange="<%=(i!=items.length-1)?"ifMoveToNext("+(items[i].getItemNo()+1)+")":"ifMoveToSave()"%>">
            </td>
            <%
			     if(rows+i>=items.length){
			%>
            <td nowrap>&nbsp;</td>
            <td nowrap>&nbsp;</td>
            <td nowrap>&nbsp;</td>
            <%
			     }else{
			%>
            <td nowrap><%=items[rows+i].getItemName()%></td>
            <td nowrap><%=items[rows+i].getItemNo()%></td>
            <td nowrap>
              <input name="r<%=items[rows+i].getItemNo()%>" type="text" class="page_form_text" id="1" size="15" maxlength="15" value="<%=DBUtil.doubleToStr(items[rows+i].getItemValue())%>" tabindex="<%=items[rows+i].getItemNo()%>" mayNull="0" dataType="3" errInfo="<%=items[rows+i].getClearItemName()%>" precision="14" decimalDigits="2" onkeyUp="<%=(i+rows!=items.length-1)?"moveToNext("+(items[rows+i].getItemNo()+1)+")":"moveToSave("+(items[rows+i].getItemNo()+1)+")"%>"></td>
            <%}}%>
          </tr>
        </table>
        <br>
        <table width="91%" border="0" class="page_form_table">
          <tr class="page_form_button_td">
            <td>&nbsp;</td>
            <td> <div align="right">
                <input type="button" name="Submit4" value="上一步" class="page_form_button_active" onClick="history.go(-1)">
                <input type="button" name="Submit3" value="退出" class="page_form_button_active" onClick="checkClose()">
				<%
				String canDelete=buttonDisabled;
				if(canDelete.equals("")){
				    if(!isEdit){
					   canDelete=" disabled";
					}
				}%>
                <input type="button" name="Submit2" value="删除"  class="page_form_button_active" onclick="deleteCheck()" <%=canDelete%>>
                <input name="submit1" type="button" class="page_form_button_active" id="submit1" onClick="checkForm1()" value="存盘" <%=buttonDisabled%>>
              </div></td>
          </tr>
        </table>
        <p>&nbsp;</p>
      </form>
      <p>&nbsp;</p></td>
			</tr>
			<tr align="center" valign="bottom">

    <td>&nbsp; </td>
			</tr>
	</table>
	</body>
</html>
