function PDPldgType_Change(p_this){
	var Instance_ID=document.all("Plat_Form_Request_Instance_ID").value;
	var Event_Value=document.all("Plat_Form_Request_Event_Value").value;
	var url="/templates/defaultform.jsp?Plat_Form_Request_Instance_ID="+Instance_ID+"&Plat_Form_Request_Event_ID=9&Plat_Form_Request_Event_Value="+Event_Value+"&Plat_Form_Request_Button_Event="+p_this.value;
	document.all("winform").action=url;
	document.all("winform").submit();
}
function checkSubmit(){
        //alert(document.all("winform").IFPERMITOK!=undefined);
          
	if(document.all("winform").IFPERMITOK!=undefined&&document.all("winform").IFPERMITOK.value==1){
	    if(document.all("winform").PERMITNO.value.length==0){
	    	alert('证件号码必须输入');
	    	return false;
	    }	
	}
	return true;
}