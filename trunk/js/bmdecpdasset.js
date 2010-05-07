/***************************************************************
* function :���������˲��������������������
* date     :2004/01/02
* author   :yusg
*****************************************************************/

/****************************************************************
* name     :checkSubmit()
* function :���Ҵ���,�����������,����Ϊ��,�����������>0
* parameter:��
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
	if(confirm("�Ƿ����ϼ��ύ��������"))
	{
		document.all.finish.value=1;
	}
	return true;
}
/*********************************************************
* name     :chcekIs()
* function :����������ˣ���һ�����˺͵�һ�����˱�����������Ϊ��
* parameter:��
* return   :��������ʱ   true          �෴ʱ false
**********************************************************/
function checkIs()
{
	var firstresp=document.all.FIRSTRESP;
	var firstreppct=document.all.FISRTRESPPCT;

	if(checkLength(firstresp)<1)
	{
		alert("��һ�����˲�����Ϊ��");
		firstresp.focus();
		return false;
	}
	if(checkLength(firstreppct)<1)
	{
		alert("��һ�����˱���������Ϊ��");
		firstreppct.focus();
		return false;
	}
	if(!checkGZLH(firstreppct.value))
	{
		alert("��һ�����˱�������С��0");
		firstreppct.focus();
		return false;
	}
}
/****************************************************************
* name     :checkIsNull()
* function :����������Ϊͬ��ʱ�����Ҵ��룬����������������Ϊ��.
            �����������>0
* parameter:��
* return   :��������ʱ   true          �෴ʱ false
*****************************************************************/
function checkIsNullAndGreat()
{
	var curno =document.all.CURNO;
	var amt   =document.all.AMT;
	if(checkLength(curno)<1) 
	{
		alert("���Ҵ��벻����Ϊ��");
		curno.focus();
		return false;
	}
		
	if(checkLength(amt)<1) 
	{
		alert("���߽�����Ϊ��");
		amt.focus();
		return false;
	}
	if(!checkGreatZero(amt.value,2))
	{
		alert("���߽��������0");
		amt.focus();
		return false;
	}

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
* name     :checkGreatZero()
* function :����������������Ƿ����0
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
* function :����Ƿ�>=0����<=100
* parameter:str    ���������
* return   :���� true    ����  false
********************************************************************/
function checkGZLH(str)
{
	var i=parseFloat(str);
	if(i>=0)
		return true;
	else
		return false;
}