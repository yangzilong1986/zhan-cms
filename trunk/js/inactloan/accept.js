function checkSubmit(){
with(winform){
	if(confirmAccept()){
	if(IFACCEPTED.value==1&&!hasVisibleChar(ADMINEDBY.value)){
	    	alert("���ѡ����գ�������������");
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
	     return confirm("ȷ�Ͻ��ղ�������");
        }else{
             return confirm("ȷ�ϲ����ղ�������");
        }
}
}
