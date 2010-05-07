function winform_on_click() {
    document.all("query").disabled = "true";
    document.all("submit1").disabled = "true";
    document.all("submit2").disabled = "true";
    document.all("submit3").disabled = "true";
    document.all("submit4").disabled = "true";
    document.all("submit5").disabled = "true";
    winform.submit();
}
function selectAll() {
    for (i = 0; i < querywinform.Plat_Form_Delete_Range_Name.length; i++) {
        querywinform.Plat_Form_Delete_Range_Name[i].checked = querywinform.allcheck.checked;
    }
    if (querywinform.Plat_Form_Delete_Range_Name.length == undefined) {
        querywinform.Plat_Form_Delete_Range_Name.checked = querywinform.allcheck.checked;
    }
}
function buttonClick(pageno, pagecount) {
    document.all("query").disabled = "true";
    document.all("submit1").disabled = "true";
    document.all("submit2").disabled = "true";
    document.all("submit3").disabled = "true";
    document.all("submit4").disabled = "true";
    document.all("submit5").disabled = "true";
    listform.Plat_Form_Request_List_Pageno.value = pageno;
    listform.Plat_Form_Request_List_PageCount.value = pagecount;
    listform.submit();
}
function pressDelete() {
    if (querywinform.Plat_Form_Delete_Range_Name.length == "undefined" && !querywinform.Plat_Form_Delete_Range_Name.checked) {
        alert("请选择要删除的记录!");
        return false;
    }
    for (i = 0; i < querywinform.Plat_Form_Delete_Range_Name.length; i++) {
        if (querywinform.Plat_Form_Delete_Range_Name[i].checked)
            break;
    }
    if (i == querywinform.Plat_Form_Delete_Range_Name.length) {
        alert("请选择要删除的记录!");
        return false;
    }
    if (confirm("请确认要删除吗?")) {
        querywinform.target = "_self";
        querywinform.Plat_Form_Request_Event_ID.value = "9";
        querywinform.Plat_Form_Request_Button_Event.value = "Plat_Form_Request_Button_Delete";
        querywinform.submit();
    }
    return true;
}

function pressAdd() {
    //if ( confirm("请确认要增加吗?") ) {
    querywinform.target = "_blank";
    querywinform.Plat_Form_Request_Event_ID.value = "9";
    querywinform.Plat_Form_Request_Button_Event.value = "Plat_Form_Request_Button_Add";
    querywinform.submit();
    //}
}
function pressSelfButton(buttonName) {
    querywinform.Plat_Form_Request_Event_ID.value = "9";
    querywinform.Plat_Form_Request_Button_Event.value = buttonName;
    querywinform.target = "_blank";
    querywinform.submit();
}

function pressSelfButtonConfirm(buttonName) {
    if (confirm("请确认要继续处理吗?")) {
        querywinform.Plat_Form_Request_Event_ID.value = "9";
        querywinform.Plat_Form_Request_Button_Event.value = buttonName;
        querywinform.target = "_blank";
        querywinform.submit();
    }
}

function showQueryItem() {
    row = document.all.detail.insertRow();
    row.align = "center";
    cell = row.insertCell();
    cell.id = 'filtertbl';
    cell.innerHTML = content;
}
function hideQueryItem(obj) {
    obj.deleteRow();
}

var menuAppear = false;
function menuMove() {
    if (menuAppear) {
        document.all("findDiv").style.display = "none";
        document.all("findDivHandle").src = "../images/form/button1.jpg";
        menuAppear = false;
    }
    else {
        document.all("findDiv").style.display = "";
        document.all("findDivHandle").src = "../images/form/button2.jpg";
        menuAppear = true;
    }
}
function pressKeyDown(keyCode) {

}

//function overIt(){
//    var the_obj = event.srcElement;
//    if(the_obj.tagName.toLowerCase() == "td"){
//        the_obj=the_obj.parentElement;
//        the_obj.oBgc=the_obj.currentStyle.backgroundColor;
//		the_obj.oFc=the_obj.currentStyle.color;
//        the_obj.style.backgroundColor='#FFF9DF';
//        the_obj.style.color='#330066';
//        the_obj.style.textDecoration='';
//    }
//}
//
//function outIt(){
//    var the_obj = event.srcElement;
//    if(the_obj.tagName.toLowerCase() == "td"){
//        the_obj=the_obj.parentElement;
//        the_obj.style.backgroundColor=the_obj.oBgc;
//        the_obj.style.color=the_obj.oFc;
//        the_obj.style.textDecoration='';
//    }
//}
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