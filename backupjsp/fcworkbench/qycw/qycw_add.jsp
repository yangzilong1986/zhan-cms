<%@ page contentType="text/html; charset=GBK" errorPage="/fc/error.jsp"%>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.form.control.impl.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.math.BigDecimal,java.text.*"%>
<%@ page import="zt.platform.utils.*" %>
<%@ page import="zt.cmsi.fc.DBUtil" %>
<%@ page import="zt.cms.pub.SCBranch,zt.cmsi.pub.define.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//String sTemp=sdf.format(Calendar.getInstance().getTime());
String sTemp=SystemDate.getSystemDate2();

String clientno=request.getParameter("CLIENTNO");
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
<base href="<%=basePath%>">
<title>信贷管理</title>
<link href="css/platform.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
body {
	margin-top: 5px;
}
-->
</style>
<script src='js/check.js' type='text/javascript'></script>
<script src='js/pagebutton.js' type='text/javascript'></script>
<script src='js/fcmain.js' type='text/javascript'></script>
<script src='js/main.js' type='text/javascript'></script>
<script language="javascript" src="js/flippage2.js"></script>
<script language="javascript" src="js/focusnext.js"></script>
  <SCRIPT   LANGUAGE="JavaScript">   
  <!--   
  //   format:$,#.0   
  //alert(formatNumber(2323333.456712345678,",###,00"));   
  function   formatNumber(   num,   format   ){   
  var   f   =   0,   n   =   0;   //   f:小数位   n:整数   
  var   c   =   "",s   =   0,z   =   0;   //   c:货币符号   s:分位数   z:四舍五入位数   
  num   +=   "";   
    
  if(num.indexOf(".")>-1){   
  f   =   num.split(".")[1]   -   0;   
  n   =   num.split(".")[0]   -   0;   
  }else{   
  n   =   num   -   0;   
  }   
    
  var   arrFormat   =   format.split(/[\,\.]/g);   
  for(var   i=0;i<arrFormat.length;i++){   
  if(arrFormat[i].indexOf("0")>-1){   
  z   =   arrFormat[i].length;   
  }else   if(arrFormat[i].indexOf("#")>-1){   
  s   =   arrFormat[i].length;   
  }else{   
  c   =   arrFormat[i];   
  }   
  }   
    
  var   ss   =   "1";   
  for(var   i=0;i<z;i++){   
  ss   +=   "0";   
  }   
  var   ss   =   ss   -   0;   
  f   =   Math.round(("0."   +   f)*ss)/ss+"";   
  if(f.indexOf(".")>-1){   
  f   =   f.split(".")[1]-0;   
  }   
  if(f.length<z){   
  for(var   i=f.length;i<z;i++){   
  f   +=   "0";   
  }   
  }   
  n   +=   "";   
  var   iStart   =   n.indexOf(".");   
  if   (iStart   <   0)   
  iStart   =   n.length;   
    
  iStart   -=   s;   
  while   (iStart   >=   1)   {   
  n   =   n.substring(0,iStart)   +   ","   +   n.substring(iStart,n.length)   
  iStart   -=   s;   
  }   
  return   c+n+"."+f;   
  }   
  function moneytoNum(num)
  {
	var s='';
	var obj=num.split(',');
	for(var i=0;i<obj.length;i++)
	{
		s+=obj[i];
	
	}
	return s;
  
  }
  //-->   

  </SCRIPT>
</head>
<body background="images/checks_02.jpg">
<form action="/fcworkbench/qycw/qycw_add_save.jsp" name="form1" method="post"  id='winform'  >
<input type="hidden" name="CLIENTNO" value="<%=clientno%>">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle">
      <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" >
        <tr align="left">
          <td height="30" bgcolor="#4477AA"> <img src="images/form/xing1.jpg" align="absmiddle"> <font size="2" color="#FFFFFF"><b>主要财务指标</b></font> <img src="/images/form/xing1.jpg" align="absmiddle"></td>
        </tr>
        <tr align="center">
		
          <td height="260" valign="middle">
            <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
              <tr>
                <td width="20">&nbsp;</td>
                <td align="center" valign="middle">
				<table class='error_message_tbl'>
					<tr class='error_message_tbl_tr'>
					<td class='error_message_tbl_td'><%=mess%></td>
					</tr>
					</table>
                  <script src='js/querybutton.js' type='text/javascript'></script>
                  <script src='js/meizzMonth.js' type='text/javascript'></script>
                  <table>
                    <tr>
                      <td>
                        <table cellpadding='0' cellspacing='0' border='0'>
                          <tr>
                            <td height='5'></td>
                          </tr>
                        </table>
                        <table class='page_form_table' width='100%' align='center' cellpadding='0' cellspacing='1' border='0'>
						 <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
						
                            <tr class='page_form_tr'>
                              <td  class='list_form_title_td'  align="left">报表日期*</td>
                              <td  class='list_form_title_td'  align="left"> 
							  <input type="text" name="DT" value="" title='调查日期'  class="page_form_text" size=10>
                              <input name="button" type="button" class='page_button_active'  onclick="setday(this,winform.DT)" value="…">
							  </td>
							  <td  class="page_form_td"  align="left">净现金流量(万元)*</td>
							   <td class="page_form_td" > <input name="CUZB1" title='净现金流量' type="text" class="page_form_text" size="20" value="" maxlength="14" onfocus="this.select();"></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">经营性净现金流量(万元)*</td>
							   <td class="page_form_td" > <input name="CUZB2" title='经营性净现金流量' type="text" class="page_form_text" size="20" value="" maxlength="14" onfocus="this.select();"></td>
							  <td  class="page_form_td"  align="left">投资活动净现金流量(万元)*</td>
							   <td class="page_form_td" > <input name="CUZB3" title='投资活动经现金流量' type="text" class="page_form_text" size="20" value="" maxlength="14" onfocus="this.select();"></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">筹资活动净现金流量(万元)*</td>
							   <td class="page_form_td" > <input name="CUZB4" title='筹资活动净现金流量' type="text" class="page_form_text" size="20" value="" maxlength="14" onfocus="this.select();"></td>
							  <td  class="page_form_td"  align="left">流动比率*</td>
							   <td class="page_form_td" > <input name="CUZB5" title='流动比率' type="text" class="page_form_text" size="20" value="" maxlength="14" onfocus="this.select();"></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">资产负债率(%)*</td>
							   <td class="page_form_td" > <input name="CUZB6" title='资产负债率' type="text" class="page_form_text" size="20" value="" maxlength="14" onfocus="this.select();"></td>
							  <td  class="page_form_td"  align="left">销售利润率(%)*</td>
							   <td class="page_form_td" > <input name="CUZB7" title='销售利润率' type="text" class="page_form_text" size="20" value="" maxlength="14" onfocus="this.select();"></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">资产利润率(%)*</td>
							   <td class="page_form_td" > <input name="CUZB8" title='资产利润率' type="text" class="page_form_text" size="20" value="" maxlength="14" onfocus="this.select();"></td>
							  <td  class="page_form_td"  align="left">存货周转率(次/年)*</td>
							   <td class="page_form_td" > <input name="CUZB10" title='存货周转率'  type="text" class="page_form_text" size="20" value="" maxlength="14" onfocus="this.select();"></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">应收账款周转率(次/年)*</td>
							   <td class="page_form_td" > <input name="CUZB9" title='应收账款周转' type="text" class="page_form_text" size="20" value="" maxlength="14" onfocus="this.select();"></td>
							  <td  class="page_form_td"  align="left">主营业务收入(元)*</td>
							   <td class="page_form_td" > <input name="CUZB11"  title='主营业务收入' type="text" class="page_form_text" size="20" value="" maxlength="14" 
							   onchange="this.value=formatNumber(moneytoNum(this.value),',###,00');" onfocus="this.select();" onfocus="this.select();"></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">担保分析(最长200汉字)*</td>
							   <td class="page_form_td"  colspan=3> <textarea name="DBFX" title='担保分析' rows="3"
																class="page_form_textfield"   maxlength="400"></textarea></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">财务分析(最长200汉字)*</td>
							   <td class="page_form_td"  colspan=3> <textarea name="CWFX" title='财务分析' rows="3"
																class="page_form_textfield"   maxlength="400"></textarea></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">非财务分析(最长200汉字)*</td>
							   <td class="page_form_td"  colspan=3> <textarea name="FCWFX" title='非财务分析' rows="3"
																class="page_form_textfield"   maxlength="400"></textarea></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">第一还款来源(最长200汉字)*</td>
							   <td class="page_form_td"  colspan=3> <textarea name="HKLY1" title='第一还款来源' rows="3"
																class="page_form_textfield"   maxlength="400"></textarea></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">第二还款来源(最长200汉字)</td>
							   <td class="page_form_td"  colspan=3> <textarea name="HKLY2" title='第二还款来源' rows="3"
																class="page_form_textfield" mayNull='false'  maxlength="400"></textarea></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">第三还款来源(最长200汉字)</td>
							   <td class="page_form_td"  colspan=3> <textarea name="HKLY3" title='第三还款来源' rows="3"
																class="page_form_textfield" mayNull='false'  maxlength="400"></textarea></td>
							</tr>
							<tr class='page_form_tr'>
							  <td  class="page_form_td"  align="left">其他事项说明(最长200汉字)</td>
							   <td class="page_form_td"  colspan=3> <textarea name="QTSM" title='其他事项说明' rows="3"
																class="page_form_textfield" mayNull='false'   maxlength="400"></textarea></td>
							</tr>
							 <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
						 
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
                <td nowrap align="center">
             
                  <table class='list_button_tbl'>
                    <tr class="list_button_tbl_tr">
					  <td class="list_form_button_td"><input type='button' class='page_button_active' id='savebtn' name='save' value=' 提 交 ' onclick='return Regvalid()'></td>
					  <td class='page_button_tbl_td'><input type='button' class='page_button_active' name='button' value=' 关 闭 ' onClick="return user_check();"></td>
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
document.title="主要财务指标";
document.focus();

function Regvalid(){
var els=document.forms[0].elements;
for(var i=0;i<els.length;i++)
{
		var field = els[i];
		if(field.type=='text' || field.type=='textarea')
		{
			mayNull = ((field.mayNull == null)?true:false);
			if (mayNull&&field.value.Trim()=='')
			{
				alert(field.title+'不能为空!');
				return;
			}
		}
}

  var DT=form1.DT;
  var DTVALUE=DT.value;
  var sDate =DTVALUE.substring(0,4)+'-'+DTVALUE.substring(4,6)+'-01';
  var CUZB1=form1.CUZB1;
  var CUZB1VAL=CUZB1.value;
    var CUZB2=form1.CUZB2;
  var CUZB2VAL=CUZB2.value;
    var CUZB3=form1.CUZB3;
  var CUZB3VAL=CUZB1.value;
    var CUZB4=form1.CUZB4;
  var CUZB4VAL=CUZB4.value;
    var CUZB5=form1.CUZB5;
  var CUZB5VAL=CUZB5.value;
    var CUZB6=form1.CUZB6;
  var CUZB6VAL=CUZB1.value;
    var CUZB7=form1.CUZB7;
  var CUZB7VAL=CUZB7.value;
    var CUZB8=form1.CUZB8;
  var CUZB8VAL=CUZB8.value;
var CUZB9=form1.CUZB9;
  var CUZB9VAL=CUZB9.value;
var CUZB10=form1.CUZB10;
  var CUZB10VAL=CUZB10.value;
var CUZB11=form1.CUZB11;
  var CUZB11VAL=moneytoNum(CUZB11.value);
  CUZB11.value=CUZB11VAL;
  if(DTVALUE.length==0){
		alert("请输入正确的日期！");
		DT.focus();
		return false;
  }
  else if(isDate2(DTVALUE)==false){
	  alert("请输入正确的日期！");
		DT.focus();
		return false;
  }
  else if (!CompareDate(sDate,'<%=sTemp%>'))
  {
  	alert('日期不能大于【<%=sTemp%>】');
  	return ;
  }
  else if(isDigit(CUZB1VAL)==false){
    alert(CUZB1.title+"非法输入！");
    CUZB1.focus();
    return false;
  }

  else if(isDigit(CUZB2VAL)==false){
    alert(CUZB2.title+"非法输入！");
    CUZB2.focus();
    return false;
  }

  else if(isDigit(CUZB3VAL)==false){
    alert(CUZB3.title+"非法输入！");
    CUZB3.focus();
    return false;
  }

  else if(isDigit(CUZB4VAL)==false){
    alert(CUZB4.title+"非法输入！");
    CUZB4.focus();
    return false;
  }

  else if(isDigit(CUZB5VAL)==false){
    alert(CUZB5.title+"非法输入！");
    CUZB5.focus();
    return false;
  }

  else if(isDigit(CUZB6VAL)==false){
    alert(CUZB6.title+"非法输入！");
    CUZB6.focus();
    return false;
  }

  else if(isDigit(CUZB7VAL)==false){
    alert(CUZB7.title+"非法输入！");
    CUZB7.focus();
    return false;
  }

  else if(isDigit(CUZB8VAL)==false){
    alert(CUZB8.title+"非法输入！");
    CUZB8.focus();
    return false;
  }

  else if(isDigit(CUZB9VAL)==false){
    alert(CUZB9.title+"非法输入！");
    CUZB9.focus();
    return false;
  }

  else if(isDigit(CUZB10VAL)==false){
    alert(CUZB10.title+"非法输入！");
    CUZB10.focus();
    return false;
  }
    else if(isDigit(CUZB11VAL)==false){
    alert(CUZB11.title+"非法输入！");
    CUZB10.focus();
    return false;
  }
  //else if(CMT1VAL.length>300){
    //alert("最多输入300个汉字！");
    //CMT1.focus();
    //return false;
  //}
  else{
	  //form1.action="";
	  winform.submit();
  }
}
function user_check(){
   this.opener.location="/fcworkbench/qycw/qycw_list.jsp?pn="+this.opener.document.all.pnstr.value+"&CLIENTNO=<%=clientno%>";
   this.close();   
}
String.prototype.isDate = function()
 {
  var r = this.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
  if(r==null)
   return false; 
  var d = new Date(r[1], r[3]-1, r[4]); 
     return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
 }
//日期比较....
 function CompareDate(v1, v2)
 {
   //大小判断，转成日期变量，然后比较符判断即可...
   var v1Ary = v1.split('-');
   var v2Ary = v2.split('-');
   var d1 = new Date(v1Ary[1] + '/' + v1Ary[2] + '/' + v1Ary[0]);
   var d2 = new Date(v2Ary[1] + '/' + v2Ary[2] + '/' + v2Ary[0]);
   if(d1 < d2)
   {
    return true;
   }
   else
   return false;
  
  
 }


</script>