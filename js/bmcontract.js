/***************************************************************
* function :本程序处理了合同审批的问题
* date     :2004/01/02
* author   :yusg
*****************************************************************/

/****************************************************************
* name     :checkSubmit()
* function :合同的开始日期必须小于合同结束日期
* parameter:无
* return   :true
*****************************************************************/
function checkSubmit()
{
	
	var startdate=document.all.STARTDATE;
	var enddate  =document.all.ENDDATE;
	var actualdisamt=document.all.ACTUALDISAMT;
	var banknoteno=document.all.BANKNOTENO;


	if(document.all.BmType.value=="13")
	{
		if(checkLength(actualdisamt)<1)
	    {
		   alert("实付贴现金额不可以为空");
		   actualdisamt.focus();
		   return false;
	    }
	}
	/*if(document.all.BmType.value=="12")
	{
		if(checkLength(banknoteno)<1)
		{
			alert("承兑汇票号码不可以为空");
			banknoteno.focus();
		   return false;
		}
	}
	*/
	if(!checkDateGreat(enddate.value,startdate.value))
	{
		alert("合同开始日期必须小于合同结束日期!");
		return false;
	}
	if(confirm("是否向上级提交此项审批"))
	{
		document.all.finish.value=1;
	}
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
/******************************************************************
* name     :checkDateGeat()
* function :检查日期得到大小关系
* parameter:str1，str2  日期1，日期2
* return   :true 日期1>日期2       否则  false
*******************************************************************/
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

	if(date1>date2)
		return true;
	else
		return false;

}
