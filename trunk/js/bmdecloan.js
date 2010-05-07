/***************************************************************
* function :本程序处理了贷款业务审批的问题
* date     :2003/12/31
* author   :yusg
*****************************************************************/

/****************************************************************
* name     :checkSubmit()
* function :提交时校验：如果审批意见为同意时，所要求的项不可以为空
                        是否要完成此项的审批
* parameter:无
* return   :true
*****************************************************************/
function checkSubmit()
{
	
	if(document.all.RESULTTYPE.value=="1")
	{
		var i=checkIsNullAndGreat();
		if(i==false)
			return false;
	}
	if(document.all.IFRESPLOAN.value=="1")
	{
		var m=checkIs();
		if(m==false)
			return false;
	}
	if(confirm("是否向上级提交此项审批"))
	{
		document.all.finish.value=1;
	}
	return true;
}
/*********************************************************
* name     :chcekIs()
* function :如果有责任人，第一责任人和第一责任人比例都不可以为空
* parameter:无
* return   :条件符合时   true          相反时 false
**********************************************************/
function checkIs()
{
	var firstresp=document.all.FIRSTRESP;
	var firstreppct=document.all.FISRTRESPPCT;

	if(checkLength(firstresp)<1)
	{
		alert("第一责任人不可以为空");
		firstresp.focus();
		return false;
	}
	if(checkLength(firstreppct)<1)
	{
		alert("第一责任人比例不可以为空");
		firstreppct.focus();
		return false;
	}
	if(!checkGZLH(firstreppct.value))
	{
		alert("第一责任人比例不能小于0");
		firstreppct.focus();
		return false;
	}
}
/****************************************************************
* name     :checkIsNull()
* function :如果审批意见为同意时：货币代码，决策金额，决策利率，
                                决策期限，决策放款日，决策还款日
            都不可以为空
* parameter:无
* return   :条件符合时   true          相反时 false
*****************************************************************/
function checkIsNullAndGreat()
{
	var curno =document.all.CURNO;
	var amt   =document.all.AMT;
	var rate  =document.all.RATE;
	var months=document.all.MONTHS;
	var jcdate=document.all.DATE;
	var startdate=document.all.STARTDATE;
	var enddate  =document.all.ENDDATE;
	var crtdate  =document.all.CREATEDATE;

    if(checkLength(curno)<1) 
		{
			alert("货币代码不可以为空");
			curno.focus();
			return false;
		}
		
	if(checkLength(amt)<1) 
		{
			alert("决策金额不可以为空");
			amt.focus();
			return false;
		}
	if(checkLength(rate)<1) 
		{
			alert("决策利率不可以为空");
			rate.focus();
			return false;
		}

	if(checkLength(months)<1)
		{
			alert("决策期限(月)不可以为空");
			months.focus();
			return false;
		}
	if(checkLength(startdate)<1)
		{
			alert("决策放款日不可以为空");
			startdate.focus();
			return false;
		}
	if(checkLength(enddate)<1)
		{
			alert("决策还款日不可以为空");
			enddate.focus();
			return false;
		}

	if(!checkGreatZero(amt.value,2))
		{
			alert("决策金额必须大于0");
			amt.focus();
			return false;
		}
		
		
        //alert(document.all.STARTDATE.type=='text');
        if(document.all.STARTDATE.type!='text'){
        if(!checkGreatZero(rate.value,2))
        {
			alert("决策利率必须大于0");
			rate.focus();
			return false;
		}
	if(!checkGreatZero(months.value,1))
		{
			alert("决策期限(月)必须大于0");
			months.focus();
			return false;
		}
	}
	if(!checkDateGreat2(jcdate.value,crtdate.value))
		{
			alert("决策日期必须大于建立日期");
			jcdate.focus();
			return false;
		}
	if(!checkDateGreat(enddate.value,startdate.value))
		{
			alert("决策还款日必须大于决策放款日");
			enddate.focus();
			return false;
		}
//	if(!checkDateGreat2(startdate.value,crtdate.value))
//		{
//			alert("决策放款日必须大于建立日期");
//			startdate.focus();
//			return false;
//		}
	return true;

}

/*****************************************************************
* name     :checkLength()
* function :检查字符的长度
* parameter:@len     字符串
* return   :sum      字符的长度
******************************************************************/
function checkLength(str)
{
	var i,sum,len;
	len=str.value;
	sum=0;

	for(i=0;i<len.length;i++) 
	{
		if ((len.charCodeAt(i)>=0)&&(len.charCodeAt(i)<=255)&&(len.charAt(i)!=" "))
			sum=sum+1;
		else if(len.charAt(i)==" ")
			sum=sum;
		else
			sum=sum+2;
	}
	return sum;
}
/*********************************************************************
* name     :checkAllNumAndDot
* function :检查输入的是否是数字和小数点
* parameter:str   字符串
* return   :false 不是,true 是
**********************************************************************/
function checkAllNumAndDot(str)
{
	var allNum="1234567890.";
	for(var i=0;i<str.length;i++)
	{
		var cIdx=str.substring(i,i+1);
		if(allNum.indexOf(cIdx)<0)
			return false;
	}
	return true;
}

/******************************************************************
* name     :checkAllInt
* function :检查输入的是否是数字
* parameter:str   字符串
* return   :false 不是,true 是
*******************************************************************/
function checkAllInt(str)
{
	var allNum="1234567890";
	for(var i=0;i<str.length;i++)
	{
		var cIdx=str.substring(i,i+1);
		if(allNum.indexOf(cIdx)<0)
			return false;
	}
	return true;
}

/***********************************************************************
* name     :checkDate
* function :检查输入的日期是否正确
* parameter:str   字符串
* return   :false 不是,true 是
*************************************************************************/
function checkDate(str)
{
	if (str.length!=8)
  {
	return false;
  }
  else
  {
	year1 = parseInt(str.substring(0,4),10);
	month1 = parseInt(str.substring(4,6),10)-1;
	day1 = parseInt(str.substr(6,8),10);
	date1 = new Date(year1,month1,day1);
	if (isNaN(date1))
	{
	  return false;
	}
	else
	{
	  year2 = date1.getFullYear();
	  month2 = date1.getMonth();
	  day2 = date1.getDate();
	  if ((year2!=year1) || (month2!=month1) || (day2!=day1))
	  {
		return false;
	  }
	}
  }
  return true;
}
/******************************************************************
* name     :checkDateGeat()
* function :检查日期得到大小关系
* parameter:str1，str2  日期1，日期2
* return   :true 日期1>日期2       否则  false
*******************************************************************/
function checkDateGreat(str1,str2)
{
	var date1=parseInt(str1.substring(0,8),10);
	var date2=parseInt(str2.substring(0,8),10);

	if(date1>date2)
		return true;
	else
		return false;

}
/******************************************************************
* name     :checkDateGeat2()
* function :检查日期得到大小关系
* parameter:str1，str2  日期1，日期2
* return   :true 日期1>=日期2       否则  false
*******************************************************************/
function checkDateGreat2(str1,str2)
{
	var date1=parseInt(str1.substring(0,8),10);
	var date2=parseInt(str2.substring(0,8),10);

	if(date1>=date2)
		return true;
	else
		return false;

}
/******************************************************************
* name     :checkGreatZero()
* function :检查整数，浮点数是否大于0
* parameter:str   传入的数字
            flag  1    整数
			      2    浮点数
* return   :如果大于0  true         否则 false
*******************************************************************/
function checkGreatZero(str,flag)
{
	if(flag=="1")
	{
		var i=parseInt(str,10);
		if(i>0)
			return true;
		else 
			return false;
	}
	else if(flag=="2")
	{
		var f=parseFloat(str);
		if(f>0)
			return true;
		else 
			return false;
	}
	else
		return false;
}
/*******************************************************************
* name     :checkGZLH()
* function :检查是否>=0并且<=100
* parameter:str    传入的数字
* return   :符合 true    否则  false
********************************************************************/
function checkGZLH(str)
{
	var i=parseFloat(str);
	if(i>=0)
		return true;
	else
		return false;
}

/*******************************************************************

********************************************************************/
var tmp_FIRSTRESP="";
function ifresploan_change(){
	var tbl=document.all("page_form_table");
	if(document.all("IFRESPLOAN").value=="0"){
		tmp_FIRSTRESP=document.all("FIRSTRESP").value;
		//document.all("FIRSTRESP").value="";
		document.all("FISRTRESPPCT").value="";
		//document.all("FIRSTRESP").disabled=true;
		document.all("FISRTRESPPCT").disabled=true;
		tbl.rows(4).cells(2).innerText="管理责任人*";
	}
	else{
		//document.all("FIRSTRESP").disabled=false;
		//document.all("FIRSTRESP").value=tmp_FIRSTRESP;
		document.all("FISRTRESPPCT").disabled=false;
		tbl.rows(4).cells(2).innerText="第一责任人*";
	}
}

function Rate_Change(){
	var Instance_ID=document.all("Plat_Form_Request_Instance_ID").value;
	var Event_Value=document.all("Plat_Form_Request_Event_Value").value;
	var url="/templates/defaultform.jsp?Plat_Form_Request_Instance_ID="+Instance_ID+"&Plat_Form_Request_Event_ID=2&Plat_Form_Request_Event_Value="+Event_Value+"&CHGTYPE=RATECHG";
	document.all("winform").action=url;
        
        
	document.all("winform").submit();
        
}