/***************************************************************
* function :本程序处理了业务审批的问题
* date     :2004/01/05
* author   :yusg
*****************************************************************/

/****************************************************************
* name     :checkSubmit()
* function :提交时校验：1.	有效期结束时间 > 有效期开始时间
                        2.	授信额度>=0
* parameter:无
* return   :true
*****************************************************************/
function checkSubmit()
{
	var startdate=document.all.STARTDATE;
	var enddate  =document.all.ENDDATE;
	var creditlimit=document.all.CREDITLIMIT;
	if(!checkDateGreat(enddate.value,startdate.value))
	{
		alert("有效期结束时间必须大于有效期开始时间");
		return false;
	}
	else if(!checkGreatZero(creditlimit.value,2))
	{
		alert("授信额度必须大于等于0");
		return false;
	}
	else
	{
		return true;
	}
	
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
/******************************************************************
* name     :checkGreatZero()
* function :检查整数，浮点数是否大于等于0
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
		if(i>=0)
			return true;
		else 
			return false;
	}
	else if(flag=="2")
	{
		var f=parseFloat(str);
		if(f>=0)
			return true;
		else 
			return false;
	}
	else
		return false;
}