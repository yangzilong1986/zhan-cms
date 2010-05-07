function checkSubmit(){
with(winform){
	if(confirmAccept()){
	if(IFACCEPTED.value==1&&!hasVisibleChar(ADMINEDBY.value)){
	    	alert("如果选择接收，请输入清收人");
	    	return false;
	}else{
	    return true;	
	}
    }else{
        return false;
    }
}
}
function confirmAccept(){
with(winform){	
	if(IFACCEPTED.value==1){
	     return confirm("确认接收不良贷款");
        }else{
             return confirm("确认不接收不良贷款");
        }
}
}
