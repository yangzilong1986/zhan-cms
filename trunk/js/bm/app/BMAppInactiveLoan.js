/***************************************************************
* function :�������ʵ�ծ�Ǽ���Ϣǰ��У��
* date     :2004/01/08
* author   :ego_xu
*****************************************************************/

/****************************************************************
* �����۸�> 0
* ��������, ������Ϣ > 0
*****************************************************************/
function checkSubmit(){
//	var appstartdate= document.all.APPSTARTDATE;
//	var appenddate	= document.all.APPENDDATE;
	var appamt		= document.all.APPAMT;
//	var cancelamt	= document.all.CANCELAMT;
//	var cancelint	= document.all.CANCELINT;

//	if(!checkDateGreat(appenddate.value,appstartdate.value)){
//		alert("���뻹���ձ����������ſ��գ�");
//		appstartdate.focus();
//		return false;
//	}
	if(parseInt(appamt.value,10)<=0){//ok!
		alert("�����۸�������0��");
		appamt.focus();
		return false;
	}
//	if(parseFloat(cancelamt.value,10)<=0){
//		alert("��������������0��");
//		cancelamt.focus();
//		return false;
//	}
//	if(parseFloat(cancelint.value,10)<=0){
//		alert("������Ϣ�������0��");
//		cancelint.focus();
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
*******************************************************************
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

*/

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