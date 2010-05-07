/***************************************************************
* function :处理业务详细信息登记内容前端校验
* date     :2004/01/05
* author   :wxj
*****************************************************************/

/****************************************************************
申请还款日 > 申请放款日
申请金额,月利率,申请期限(月) > 0
汇票总张数,汇票总金额,贴现率,贴现利息, 实付贴现金额> 0 
*****************************************************************/
function checkSubmit(){
	var appstartdate=document.all.APPSTARTDATE;
	var appenddate =document.all.APPENDDATE;
	var appamt   =document.all.APPAMT;
	var rate  =document.all.RATE;
	var appmonths=document.all.APPMONTHS;
	var CONTRACTAMT   =document.all.CONTRACTAMT;
	var MARGINAMT  =document.all.MARGINAMT;
	var MARGINRATE=document.all.MARGINRATE;
	var CONTRACTDATE   =document.all.CONTRACTDATE;
	var APPDATE  =document.all.APPDATE;
	var APPMONTHS  =document.all.APPMONTHS;
	if(!checkDateGreat(appenddate.value,appstartdate.value)){
		alert("申请还款日必须大于申请放款日！");
		appstartdate.focus();
		return false;
	}
	if(parseInt(appamt.value,10)<=0){
		alert("申请金额必须大于0！");
		appamt.focus();
		return false;
	}
	if(parseFloat(rate.value)<=0){
		alert("月利率必须大于0！");
		rate.focus();
		return false;
	}
	if(parseInt(appmonths.value,10)<=0){
		alert("申请期限必须大于0！");
		appmonths.focus();
		return false;
	}
	if(parseFloat(CONTRACTAMT.value)<=0){
		alert("交易合同金额必须大于0！");
		CONTRACTAMT.focus();
		return false;
	}
	if(parseFloat(MARGINAMT.value)<0){
		alert("保证金金额必须大于0！");
		MARGINAMT.focus();
		return false;
	}
	if(parseFloat(MARGINRATE.value)<0){
		alert("保证金比率必须大于0！");
		MARGINRATE.focus();
		return false;
	}
	if(!checkDateGreat(APPDATE.value,CONTRACTDATE.value)){
		alert("申请日期必须大于交易合同签订日期！");
		CONTRACTDATE.focus();
		return false;
	}

	if(APPMONTHS.value>180){
		//alert(APPMONTHS);
		alert("贷款申请期限不能超过180天");
		return false;
	}
	if(parseFloat(MARGINAMT.value)*2<parseFloat(appamt.value)) {
		alert("承兑保证金比例低于50%!");
		MARGINAMT.focus();
		return false;
	}

	
  if(confirm("你确定要将此申请提交审批吗？")){
		document.all.finish.value=1;
	}
	else{
		document.all.finish.value=0;
		return false;
	}
	
	//var startDays = appstartday.split("-");
	//var endDays = appendday.split("-");
	//if(endDays[0]-startDays[0]>1){
		//alert("贷款申请期限超过计六个月");
	//}else if(endDays[1]-startDays[1]>6){
		//alert("贷款申请期限超过计六个月");
		//}

	
	return true;
}
/******************************************************************
* name     :checkDateGeat()
* function :比较日期大小
* parameter:str1，str2 日期
* return   :true 日期1>日期2
*******************************************************************/
function checkDateGreat(str1,str2){
	var year1=parseInt(str1.substring(0,4),10);
	var month1=parseInt(str1.substring(4,6),10);
	var day1=parseInt(str1.substring(6,8),10);

	var year2=parseInt(str2.substring(0,4),10);
	var month2=parseInt(str2.substring(4,6),10);
	var day2=parseInt(str2.substring(6,8),10);
	
	date1=new Date(year1,month1,day1);
	date2=new Date(year2,month2,day2);

	if(date1>date2){
		return true;
	}
	else{
		return false;
	}
	
	//var startDays = appstartday.split("-");
	//var endDays = appendday.split("-");
	//if(endDays[0]-startDays[0]>1){
	//	alert("贷款申请期限");
	//}
	//Date sDay=new Date();
	//Date eDay=new Date();
	//sDay.setYear(startDays[0]);
	//sDay.setMonth(startDays[1]);
	//sDay.setDate(startDays[2]);
	//eDay.setYear(endDays[0]);
	//eDay.setMonth(endDays[1]);
	//eDay.setDate(endDays[2]);
	
	
}

function checkSelfSubmit(button){
	return true;
}

function Rate_Change(){
	var Instance_ID=document.all("Plat_Form_Request_Instance_ID").value;
	var Event_Value=document.all("Plat_Form_Request_Event_Value").value;
	var url="/templates/defaultform.jsp?Plat_Form_Request_Instance_ID="+Instance_ID+"&Plat_Form_Request_Event_ID=2&Plat_Form_Request_Event_Value="+Event_Value+"&CHGTYPE=RATECHG";
	document.all("winform").action=url;
	document.all("winform").submit();
}
