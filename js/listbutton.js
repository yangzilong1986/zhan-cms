function selectAll() {
	for ( i = 0 ; i < winform.Plat_Form_Delete_Range_Name.length ; i++ ) {
		winform.Plat_Form_Delete_Range_Name[i].checked = winform.allcheck.checked;
	}
	if ( winform.Plat_Form_Delete_Range_Name.length == "undefined" ) {
		winform.Plat_Form_Delete_Range_Name.checked = winform.allcheck.checked;
	}
}
function buttonClick(pageno,pagecount) {
	listform.Plat_Form_Request_List_Pageno.value = pageno;
	listform.Plat_Form_Request_List_PageCount.value = pagecount;
	listform.submit();
}
function pressDelete() {
	if ( winform.Plat_Form_Delete_Range_Name.length == "undefined" && !winform.Plat_Form_Delete_Range_Name.checked ) {
		alert("请选择要删除的记录!");
		return false;
	}
	for ( i = 0 ; i < winform.Plat_Form_Delete_Range_Name.length ; i++ ) {
		if ( winform.Plat_Form_Delete_Range_Name[i].checked )
			break;
	}
	if ( i == winform.Plat_Form_Delete_Range_Name.length ) {
		alert("请选择要删除的记录!");
		return false;
	}	
	if ( confirm("请确认要删除吗?") ) {
		winform.target = "_self";
		winform.Plat_Form_Request_Event_ID.value="9";
		winform.Plat_Form_Request_Button_Event.value="Plat_Form_Request_Button_Delete";
		winform.submit();
	}
	return true;
}
function pressAdd() {
	//if ( confirm("请确认要增加吗?") ) {
		winform.target = "_blank";
		winform.Plat_Form_Request_Event_ID.value="9";
		winform.Plat_Form_Request_Button_Event.value="Plat_Form_Request_Button_Add";
		winform.submit();
	//}
}
function pressSelfButton(buttonName) {
	winform.Plat_Form_Request_Event_ID.value = "9";
	winform.Plat_Form_Request_Button_Event.value = buttonName;
	winform.target = "_blank";
	winform.submit();
}

function overIt(obj) {
    var the_obj = obj;
    if (the_obj.tagName.toLowerCase() == "tr") {
        //the_obj=the_obj.parentElement;
        the_obj.oBgc = the_obj.currentStyle.backgroundColor;
        the_obj.oFc = the_obj.currentStyle.color;
        the_obj.style.backgroundColor = '#FFF9DF';
        the_obj.style.color = '#330066';
        the_obj.style.textDecoration = '';
    }
}

function outIt(obj) {
    var the_obj = obj
    if (the_obj.tagName.toLowerCase() == "tr") {
        //the_obj=the_obj.parentElement;
        the_obj.style.backgroundColor = the_obj.oBgc;
        the_obj.style.color = the_obj.oFc;
        the_obj.style.textDecoration = '';
    }
}
