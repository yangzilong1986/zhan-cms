/***************************************************************
* function :���������˺�ͬ����������
* date     :2004/01/02
* author   :yusg
*****************************************************************/

/****************************************************************
* name     :checkSubmit()
* function :��ͬ�Ŀ�ʼ���ڱ���С�ں�ͬ��������
* parameter:��
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
		   alert("ʵ�����ֽ�����Ϊ��");
		   actualdisamt.focus();
		   return false;
	    }
	}
	/*if(document.all.BmType.value=="12")
	{
		if(checkLength(banknoteno)<1)
		{
			alert("�жһ�Ʊ���벻����Ϊ��");
			banknoteno.focus();
		   return false;
		}
	}
	*/
	if(!checkDateGreat(enddate.value,startdate.value))
	{
		alert("��ͬ��ʼ���ڱ���С�ں�ͬ��������!");
		return false;
	}
	if(confirm("�Ƿ����ϼ��ύ��������"))
	{
		document.all.finish.value=1;
	}
	return true;
}
/*****************************************************************
* name     :checkLength()
* function :����ַ��ĳ���
* parameter:@len     �ַ���
* return   :sum      �ַ��ĳ���
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
