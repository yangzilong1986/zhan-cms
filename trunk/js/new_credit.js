function BMTYPEOnClick(){

//�жһ�Ʊ	���ж������պͳжҵ�����
//����		�����������պ����ֵ�����
//ת����	��ת���������պ�ת���ֵ�����

	/*
	if (document.all.BMTYPE.value==12){
		document.all("page_form_table").rows(2).cells(0).innerText="�ж�������*";
		document.all("page_form_table").rows(3).cells(0).innerText="�жҵ�����*";
	}
	else if (document.all.BMTYPE.value==13){
		document.all("page_form_table").rows(2).cells(0).innerText="����������*";
		document.all("page_form_table").rows(3).cells(0).innerText="���ֵ�����*";
	}else if (document.all.BMTYPE.value==14){
		document.all("page_form_table").rows(2).cells(0).innerText="ת����������*";
		document.all("page_form_table").rows(3).cells(0).innerText="ת���ֵ�����*";
		
	}else{
		document.all("page_form_table").rows(2).cells(0).innerText="����ſ���*";
		document.all("page_form_table").rows(3).cells(0).innerText="���뻹����*";
	}
	*/
	document.all.nameref[1].onclick=tests;
}

function tests(){
	
	var Instance_ID=document.all("Plat_Form_Request_Instance_ID").value;
	var Event_Value=document.all("Plat_Form_Request_Event_Value").value;
	window.open("/templates/ref.jsp?Plat_Form_Request_Instance_ID="+Instance_ID+"&Plat_Form_Request_Event_ID=11&reference_field=CLIENTNO&BMTYPE=" + document.all.BMTYPE.value ,"FIREFCLIENTNO1","height=350,width=460,toolbar=no,scrollbars=yes");
	//window.open(&quot;/templates/ref.jsp?BMTYPE="+document.all.BMTYPE.value+"&Plat_Form_Request_Instance_ID="+Instance_ID+"&Plat_Form_Request_Event_ID=11&reference_field=APPBRHID,height=350,width=460,toolbar=no,scrollbars=yes;);
}

function checkSelfSubmit(button){
	
with(winform){
	
if(validNumber(APPAMT.value,12,2)){
}else{
    infoAlert(APPAMT,"���Ƿ�����");
    return false;
}


if(firstDateNotLaterThanSecond(APPBEGINDATE,APPENDDATE,'����ſ���','���뻹����')){
}else{
    return false;
}

//15����չ��
if(BMTYPE.value==15){
	if(trim(ORIGACCNO.value)==''){
	    infoAlert(ORIGACCNO,"ԭ�˺�չ��ʱ������Ϊ��");
	    return false;	
	}
	if(trim(ORIGDUEBILLNO.value)==''){
	    infoAlert(ORIGDUEBILLNO,"Ը��ݺ�չ��ʱ������Ϊ��");	
	    return false;
	}
	
}


return true;	
}



}
