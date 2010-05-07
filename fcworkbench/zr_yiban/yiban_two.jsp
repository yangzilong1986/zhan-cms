<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.platform.cachedb.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.form.control.impl.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.utils.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%

String FCNO=request.getParameter("FCNO");
String FCSTATUS=request.getParameter("FCSTATUS");

  if(FCNO == null || FCSTATUS==null)
  {
   session.setAttribute("lettermess","没有发现传送入的参数！");
    response.sendRedirect("../fcworkbench/lettersucces.jsp");
  }
  DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
request.setCharacterEncoding("GBK");
String CREATEDATE  = "";//清分日期 
String CLIENTNAME  = "";//客户名称 
String IDNO        = "";//身份证号码 
String CNLNO       = "";//承兑汇票号码 
BigDecimal BAL     = null;//票面金额
String BMNO        = "";//业务号 
String PAYDATE     = "";//发放日期 
String DUEDATE     = "";//到期日期 
String FIRSTRESP   = "";//第一责任人 
String LOANTYPE3   = "";//担保方式
int YUQIDAY        = 0;//逾期天数
String SIJIFL      = "";//四级分类占用形态
String CREDITCLASS = "";//有效信用等级
String CURNO       = "";//货币种类 
String FCAUTO      = "";//自动清分结果
String FC1         = "";//信贷员认定结果
String OPERATOR    = "";//最后维护人
String FC2         = "";//信贷部门主任认定结果
String LASTMODIFIED= "";//最后修改日期 
String FC3         = "";//联社主任认定结果

ConnectionManager manager=ConnectionManager.getInstance();
String sql="select * from FCMAIN where FCNO='"+FCNO+"'";
CachedRowSet crs=manager.getRs(sql);
if(crs.next()){
 CREATEDATE  = crs.getString("CREATEDATE");//清分日期 
 CLIENTNAME  = crs.getString("CLIENTNAME");//客户名称 
 IDNO        = crs.getString("IDNO");//身份证号码 
 CNLNO       = crs.getString("CNLNO");//承兑汇票号码 
 BAL         = crs.getBigDecimal("BAL");//票面金额
 BMNO        = crs.getString("BMNO");//业务号 
 PAYDATE     = crs.getString("PAYDATE");//发放日期 
 DUEDATE     = crs.getString("DUEDATE");//到期日期 
 FIRSTRESP   = crs.getString("FIRSTRESP");//第一责任人 
 LOANTYPE3   = crs.getString("LOANTYPE3");//担保方式
 YUQIDAY     = Integer.parseInt(DBUtil.to_Date(CREATEDATE).trim())-Integer.parseInt(DBUtil.to_Date(DUEDATE).trim());//逾期天数
 SIJIFL      = "正常";//四级分类占用形态
 CREDITCLASS = crs.getString("CREDITCLASS");//有效信用等级
 CURNO       = crs.getString("CURNO");//货币种类 
 FCAUTO      = crs.getString("FCAUTO");//自动清分结果
 FC1         = crs.getString("FC1");//信贷员认定结果
 OPERATOR    = crs.getString("OPERATOR");//最后维护人
 FC2         = crs.getString("FC2");//信贷部门主任认定结果
 LASTMODIFIED= crs.getString("LASTMODIFIED");//最后修改日期 
 FC3         = crs.getString("FC3");//联社主任认定结果

}
 
String readonly="";
String submit="";
if(FCSTATUS.equals("1")){
  readonly="readonly";
  submit="disabled='true' class='page_button_disabled'";
}
if(FCSTATUS.equals("2")){
  readonly="";
  submit="class='page_button_active'";
}
if(FCSTATUS.equals("3")){
  readonly="readonly";
  submit="disabled='true' class='page_button_disabled'";
}
 
String mess=(String)request.getAttribute("mess");
if(mess==null){
	mess="";
}
else{
	mess="<li class='error_message_li'>"+mess.trim()+"</li>";
}
%>
<html>
	<head>
		<title>信贷管理</title>
		<link href="/css/platform.css" rel="stylesheet" type="text/css">
		<script src='/js/meizzDate.js' type='text/javascript'></script>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<style type="text/css">
		<!--
		body {
			margin-top: 5px;
		}
		-->
		</style>
	</head>
	
	<body background="../images/checks_02.jpg">
	<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" valign="middle">
          <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" >
            <tr align="left">
              <td height="30" bgcolor="#4477AA"> <img src="/images/form/xing1.jpg" align="absmiddle"> <font size="2" color="#FFFFFF"><b>自然人一般农户贷款</b></font> <img src="/images/form/xing1.jpg" align="absmiddle"></td>
            </tr>
            <tr align="center">
              <td height="260" valign="middle">
			  <form name="form1" method="post" id="winform" action="Qftime_add_save.jsp" onsubmit="return Regvalid(this)">
                <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
				
                  <tr>
                    <td width="20">&nbsp;</td>
                    <td align="center" valign="middle">
					<table class='error_message_tbl'>
					<tr class='error_message_tbl_tr'>
					<td class='error_message_tbl_td'><%=mess%></td>
					</tr>
					</table>
                      <table class='page_form_table' id='page_form_table' width="300" height="20">
						 <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>清分日期</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="CREATEDATE" value="<%=CREATEDATE==null?"":CREATEDATE%>"  class="page_form_text"></td>
                          <td class="page_form_td" nowrap>客户名称</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="CLIENTNAME" value="<%=CLIENTNAME==null?"":DBUtil.fromDB(CLIENTNAME)%>"  class="page_form_text"></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>身份证号码</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="IDNO" value="<%=IDNO==null?"":IDNO%>"  class="page_form_text"></td>
                          <td class="page_form_td" nowrap>承兑汇票号码</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="CNLNO" value="<%=CNLNO==null?"":CNLNO%>"  class="page_form_text"></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>票面金额</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="BAL" value="<%=BAL==null?"":df.format(BAL)%>"  class="page_form_text"></td>
                          <td class="page_form_td" nowrap>业务号</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="BMNO" value="<%=BMNO==null?"":BMNO%>"  class="page_form_text"></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>出票日</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="PAYDATE" value="<%=PAYDATE==null?"":PAYDATE%>"  class="page_form_text"></td>
                          <td class="page_form_td" nowrap>到期日</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="DUEDATE" value="<%=DUEDATE==null?"":DUEDATE%>"  class="page_form_text"></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>第一责任人</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="FIRSTRESP" value="<%=FIRSTRESP==null?"":FIRSTRESP%>"  class="page_form_text"></td>
                          <td class="page_form_td" nowrap>业务的担保形式</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="LOANTYPE3" value="<%=LOANTYPE3==null?"":level.getEnumItemName("LoanType3",LOANTYPE3)%>"  class="page_form_text"></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>逾期天数</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="YUQIDAY" value="<%=YUQIDAY<0?0:YUQIDAY%>"  class="page_form_text"></td>
                          <td class="page_form_td" nowrap>四级分类占用形态</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="SIJIFL" value="<%=SIJIFL==null?"":SIJIFL%>"  class="page_form_text"></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>信用等级</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="CREDITCLASS" value="<%=CREDITCLASS==null?"":level.getEnumItemName("CreditClass",CREDITCLASS)%>"  class="page_form_text"></td>
                          <td class="page_form_td" nowrap>货币代码</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="CURNO" value="<%=CURNO==null?"":CURNO%>"  class="page_form_text"></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>自动清分结果</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="FCAUTO" value="<%=FCAUTO==null?"":level.getEnumItemName("LoanCat1",FCAUTO)%>"  class="page_form_text"></td>
                          <td class="page_form_td" nowrap>信贷员认定结果</td>
                          <td class="page_form_td" nowrap><%=FC1==null?"":level.levelHere1("FC1","LoanCat1",FC1)%></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>最后维护人</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="OPERATOR" value="<%=OPERATOR==null?"":OPERATOR%>"  class="page_form_text"></td>
                          <td class="page_form_td" nowrap>信贷部门主任认定结果*</td>
                          <td class="page_form_td" nowrap><%=FCSTATUS.equals("2")?FC1!=null?level.levelHere("FC2","LoanCat1",FC1):level.levelHere("FC2","LoanCat1",FC2):level.levelHere1("FC2","LoanCat1",FC2)%></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>最后维护日期</td>
                          <td class="page_form_td" nowrap><input type="text" readonly name="LASTMODIFIED" value="<%=LASTMODIFIED==null?"":LASTMODIFIED%>"  class="page_form_text"></td>
                          <td class="page_form_td" nowrap>联社主任认定结果</td>
                          <td class="page_form_td" nowrap><%=FC3==null?"":level.levelHere1("FC3","LoanCat1",FC3)%></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>风险评级理由及<br>初步分类意见</td>
                          <td class="page_form_td" nowrap colspan="3"> <textarea name="COMMENT" readonly wrap="PHYSICAL"  cols="57" rows="3" class="page_form_text"></textarea></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>信贷员清分意见</td>
                          <td class="page_form_td" nowrap colspan="3"> <textarea name="COMMENT" readonly wrap="PHYSICAL"  cols="57" rows="3" class="page_form_text"></textarea></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>信贷部门主任清分*<br>意见</td>
                          <td class="page_form_td" nowrap colspan="3"> <textarea name="COMMENT" <%=readonly%> wrap="PHYSICAL"  cols="57" rows="3" class="page_form_text"></textarea></td>                          
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>联社主任清分意<br>见</td>
                          <td class="page_form_td" nowrap  colspan="3"> <textarea name="COMMENT" readonly wrap="PHYSICAL"  cols="57" rows="3" class="page_form_text"></textarea></td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                       
                        <tr class='page_form_tr'>
						<td class="page_form_td" nowrap>&nbsp;</td>
						<td class='page_form_td' colspan='6' align="center">		
						<input type="button" name="CorpCapital" value="客户基本信息" class="page_form_button_active">
						<input type="button" name="CorpPayables" value="历史清分信息"  class="page_form_button_active">
						<input type="button" name="CorpPayables" value="贷款审批档案"  class="page_form_button_active">
						</td>
						</tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                    </table></td>
                    <td width="20">&nbsp;</td>
                  </tr>
              </table>
			  
			  </td>
            </tr>
            <tr height="35" align="center" valign="middle">
              <td align="center">
                <table border="0" cellspacing="0" cellpadding="0" width="538">
                  <tr>
                    <td nowrap align="center">
                      <table class='list_button_tbl'>
                        <tr class='list_button_tbl_tr'>
						   <td class='page_button_tbl_td'><input type='submit' <%=submit%> id='saveadd' name='save' value=' 提交 ' ></td>
						   <td class='page_button_tbl_td'><input type='button' class='page_button_active' name='button' value=' 关闭 ' onClick="aaa()"></td>
						   <td class='page_button_tbl_td'><input type='button' class='page_button_active' name='button' value=' 取消 ' onClick="parent.close();"></td>
                        </tr>
                    </table></td>
                  </tr>
              </table></td>
            </tr>
        </table></td>
      </tr>
    </table>
	</form>
	</body>
</html>
<script language="javascript">
document.title="自然人一般农户贷款";
document.focus();
function Regvalid(form){
	 var DT=form1.DT;
  var systemdate=form1.systemdate;
  var DTVALUE=DT.value;
  var SYSVALUE=systemdate.value;
  if(checkDateGreat(DTVALUE,SYSVALUE)==false){
		alert("预计清分日期必须大于等于当天系统日期！");
		DT.focus();
		return false;
  }
  var COMMENT=form1.COMMENT;
  if(COMMENT.value.length>300){
    alert("最多300个汉字！");
    COMMENT.focus();
    return false;
  }
 
}
function aaa(){
	alert("close");
	this.opener.location="/fcworkbench/zr_yiban.jsp?aaa='aaa'";
   	//this.close();
}
function checkDateGreat(str1,str2)
{
	var year1=parseInt(str1.substring(0,4),10);
	var month1=parseInt(str1.substring(4,6),10);
	var day1=parseInt(str1.substring(6,8),10);

	date1=new Date(year1,month1,day1);

	var year2=parseInt(str2.substring(0,4),10);
	var month2=parseInt(str2.substring(4,6),10);
	var day2=parseInt(str2.substring(6,8),10);

	date2=new Date(year2,month2,day2);

	if(date1>=date2)
		return true;
	else
		return false;

}
</script>
