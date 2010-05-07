/***************************************************************
* function :����������ҵ������������
* date     :2004/01/05
* author   :yusg
*****************************************************************/

/****************************************************************
* name     :checkSubmit()
* function :�ύʱУ�飺1.	��Ч�ڽ���ʱ�� > ��Ч�ڿ�ʼʱ��
                        2.	���Ŷ��>=0
* parameter:��
* return   :true
*****************************************************************/
function checkSubmit()
{
	var startdate=document.all.STARTDATE;
	var enddate  =document.all.ENDDATE;
	var creditlimit=document.all.CREDITLIMIT;
	if(!checkDateGreat(enddate.value,startdate.value))
	{
		alert("��Ч�ڽ���ʱ����������Ч�ڿ�ʼʱ��");
		return false;
	}
	else if(!checkGreatZero(creditlimit.value,2))
	{
		alert("���Ŷ�ȱ�����ڵ���0");
		return false;
	}
	else
	{
		return true;
	}
	
}
/******************************************************************
* name     :checkDateGeat()
* function :������ڵõ���С��ϵ
* parameter:str1��str2  ����1������2
* return   :true ����1>����2       ����  false
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
* function :����������������Ƿ���ڵ���0
* parameter:str   ���������
            flag  1    ����
			      2    ������
* return   :�������0  true         ���� false
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