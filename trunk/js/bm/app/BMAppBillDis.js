/***************************************************************
* function :����ҵ����ϸ��Ϣ�Ǽ�����ǰ��У��
* date     :2004/01/05
* author   :wxj
*****************************************************************/

/****************************************************************
���뻹���� > ����ſ���
������,������,��������(��) > 0
��Ʊ������,��Ʊ�ܽ��,������,������Ϣ, ʵ�����ֽ��> 0 
*****************************************************************/
function checkSubmit(){
	var appstartdate=document.all.APPSTARTDATE;
	var appenddate =document.all.APPENDDATE;
	var appamt   =document.all.APPAMT;
	var rate  =document.all.RATE;
	var appmonths=document.all.APPMONTHS;
	//var NUMOFNOTES   =document.all.NUMOFNOTES;
	//var TOTALBILLAMT  =document.all.TOTALBILLAMT;
	//var DISRATE=document.all.DISRATE;
	//var DISINTEREST   =document.all.DISINTEREST;
	//var ACTUALDISCAMT  =document.all.ACTUALDISCAMT;
	if(!checkDateGreat(appenddate.value,appstartdate.value)){
		alert("���뻹���ձ����������ſ��գ�");
		appstartdate.focus();
		return false;
	}
	if(parseInt(appamt.value,10)<=0){
		alert("������������0��");
		appamt.focus();
		return false;
	}
	if(parseFloat(rate.value)<=0){
		alert("�����ʱ������0��");
		rate.focus();
		return false;
	}
	if(parseInt(appmonths.value,10)<=0){
		alert("�������ޱ������0��");
		appmonths.focus();
		return false;
	}
//	if(parseFloat(NUMOFNOTES.value)<=0){
//		alert("��Ʊ�������������0��");
//		NUMOFNOTES.focus();
//		return false;
//	}
//	if(parseFloat(TOTALBILLAMT.value)<=0){
//		alert("��Ʊ�ܽ��������0��");
//		TOTALBILLAMT.focus();
//		return false;
//	}
//	if(parseFloat(DISRATE.value)<=0){
//		alert("�����ʱ������0��");
//		DISRATE.focus();
//		return false;
//	}
//	if(parseFloat(DISINTEREST.value)<=0){
//		alert("������Ϣ�������0��");
//		DISINTEREST.focus();
//		return false;
//	}
//	if(parseFloat(ACTUALDISCAMT.value)<=0){
//		alert("ʵ�����ֽ��������0��");
//		ACTUALDISCAMT.focus();
//		return false;
//	}
  if(confirm("��ȷ��Ҫ���������ύ������")){
		document.all.finish.value=1;
	}
	else{
		document.all.finish.value=0;
		return false;
	}
	return true;
}
/******************************************************************
* name     :checkDateGeat()
* function :�Ƚ����ڴ�С
* parameter:str1��str2 ����
* return   :true ����1>����2
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