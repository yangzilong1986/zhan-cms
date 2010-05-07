/***************************************************************
* function :本程序处理了不良贷款核销审批的问题
* date     :2004/01/02
* author   :yusg
*****************************************************************/

/****************************************************************
* name     :checkSubmit()
* function :货币代码,决策评估金额,不能为空,决策评估金额>0
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
		var i=checkIs();
		if(i==false)
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
* function :如果审批意见为同意时：货币代码，决策评估金额，不可以为空.
            决策评估金额>0
* parameter:无
* return   :条件符合时   true          相反时 false
*****************************************************************/
function checkIsNullAndGreat()
{
	var curno =document.all.CURNO;
	var amt   =document.all.AMT;
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
	if(!checkGreatZero(amt.value,2))
	{
		alert("决策金额必须大于0");
		amt.focus();
		return false;
	}

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