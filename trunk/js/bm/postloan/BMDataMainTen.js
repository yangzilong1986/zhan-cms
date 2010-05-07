function IfRespLoan_Change(p_this){
	var tbl=document.all("page_form_table");
	if(p_this.value=="1"){
		tbl.rows(4).cells(0).innerText="第一责任人*";
	}
	else{
		tbl.rows(4).cells(0).innerText="管理责任人*";
	}
}
function checkSubmit(){
	return true;
}
