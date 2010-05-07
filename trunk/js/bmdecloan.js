/***************************************************************
* function :���������˴���ҵ������������
* date     :2003/12/31
* author   :yusg
*****************************************************************/

/****************************************************************
* name     :checkSubmit()
* function :�ύʱУ�飺����������Ϊͬ��ʱ����Ҫ��������Ϊ��
                        �Ƿ�Ҫ��ɴ��������
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
		var m=checkIs();
		if(m==false)
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
* function :����������Ϊͬ��ʱ�����Ҵ��룬���߽��������ʣ�
                                �������ޣ����߷ſ��գ����߻�����
            ��������Ϊ��
* parameter:��
* return   :��������ʱ   true          �෴ʱ false
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
	if(checkLength(rate)<1) 
		{
			alert("�������ʲ�����Ϊ��");
			rate.focus();
			return false;
		}

	if(checkLength(months)<1)
		{
			alert("��������(��)������Ϊ��");
			months.focus();
			return false;
		}
	if(checkLength(startdate)<1)
		{
			alert("���߷ſ��ղ�����Ϊ��");
			startdate.focus();
			return false;
		}
	if(checkLength(enddate)<1)
		{
			alert("���߻����ղ�����Ϊ��");
			enddate.focus();
			return false;
		}

	if(!checkGreatZero(amt.value,2))
		{
			alert("���߽��������0");
			amt.focus();
			return false;
		}
		
		
        //alert(document.all.STARTDATE.type=='text');
        if(document.all.STARTDATE.type!='text'){
        if(!checkGreatZero(rate.value,2))
        {
			alert("�������ʱ������0");
			rate.focus();
			return false;
		}
	if(!checkGreatZero(months.value,1))
		{
			alert("��������(��)�������0");
			months.focus();
			return false;
		}
	}
	if(!checkDateGreat2(jcdate.value,crtdate.value))
		{
			alert("�������ڱ�����ڽ�������");
			jcdate.focus();
			return false;
		}
	if(!checkDateGreat(enddate.value,startdate.value))
		{
			alert("���߻����ձ�����ھ��߷ſ���");
			enddate.focus();
			return false;
		}
//	if(!checkDateGreat2(startdate.value,crtdate.value))
//		{
//			alert("���߷ſ��ձ�����ڽ�������");
//			startdate.focus();
//			return false;
//		}
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
/*********************************************************************
* name     :checkAllNumAndDot
* function :���������Ƿ������ֺ�С����
* parameter:str   �ַ���
* return   :false ����,true ��
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
* function :���������Ƿ�������
* parameter:str   �ַ���
* return   :false ����,true ��
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
* function :�������������Ƿ���ȷ
* parameter:str   �ַ���
* return   :false ����,true ��
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
* function :������ڵõ���С��ϵ
* parameter:str1��str2  ����1������2
* return   :true ����1>����2       ����  false
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
* function :������ڵõ���С��ϵ
* parameter:str1��str2  ����1������2
* return   :true ����1>=����2       ����  false
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
		tbl.rows(4).cells(2).innerText="����������*";
	}
	else{
		//document.all("FIRSTRESP").disabled=false;
		//document.all("FIRSTRESP").value=tmp_FIRSTRESP;
		document.all("FISRTRESPPCT").disabled=false;
		tbl.rows(4).cells(2).innerText="��һ������*";
	}
}

function Rate_Change(){
	var Instance_ID=document.all("Plat_Form_Request_Instance_ID").value;
	var Event_Value=document.all("Plat_Form_Request_Event_Value").value;
	var url="/templates/defaultform.jsp?Plat_Form_Request_Instance_ID="+Instance_ID+"&Plat_Form_Request_Event_ID=2&Plat_Form_Request_Event_Value="+Event_Value+"&CHGTYPE=RATECHG";
	document.all("winform").action=url;
        
        
	document.all("winform").submit();
        
}