function IfRespLoan_Change(p_this){
	var tbl=document.all("page_form_table");
	if(p_this.value=="1"){
		tbl.rows(4).cells(0).innerText="��һ������*";
	}
	else{
		tbl.rows(4).cells(0).innerText="����������*";
	}
}
function checkSubmit(){
	return true;
}
