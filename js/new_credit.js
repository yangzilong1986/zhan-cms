function BMTYPEOnClick(){

//承兑汇票	：承兑申请日和承兑到期日
//贴现		：贴现申请日和贴现到期日
//转贴现	：转贴现申请日和转贴现到期日

	/*
	if (document.all.BMTYPE.value==12){
		document.all("page_form_table").rows(2).cells(0).innerText="承兑申请日*";
		document.all("page_form_table").rows(3).cells(0).innerText="承兑到期日*";
	}
	else if (document.all.BMTYPE.value==13){
		document.all("page_form_table").rows(2).cells(0).innerText="贴现申请日*";
		document.all("page_form_table").rows(3).cells(0).innerText="贴现到期日*";
	}else if (document.all.BMTYPE.value==14){
		document.all("page_form_table").rows(2).cells(0).innerText="转贴现申请日*";
		document.all("page_form_table").rows(3).cells(0).innerText="转贴现到期日*";
		
	}else{
		document.all("page_form_table").rows(2).cells(0).innerText="申请放款日*";
		document.all("page_form_table").rows(3).cells(0).innerText="申请还款日*";
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
    infoAlert(APPAMT,"金额非法数字");
    return false;
}


if(firstDateNotLaterThanSecond(APPBEGINDATE,APPENDDATE,'申请放款日','申请还款日')){
}else{
    return false;
}

//15就是展期
if(BMTYPE.value==15){
	if(trim(ORIGACCNO.value)==''){
	    infoAlert(ORIGACCNO,"原账号展期时不可以为空");
	    return false;	
	}
	if(trim(ORIGDUEBILLNO.value)==''){
	    infoAlert(ORIGDUEBILLNO,"愿借据号展期时不可以为空");	
	    return false;
	}
	
}


return true;	
}



}
