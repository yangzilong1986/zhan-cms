function checkSubmit(){
    //alert(winform);
    with(winform){
    	//alert(SENTENCEMODE.value);
        if(APLSENTENCEMODE.value==4){
           if(APLWITHDRAWDATE.value==''){
               alert("�����볷��ʱ��");
               return false;
           } 
           if(APLWITHDRAWREASON.value==''){
               alert("�����볷��ԭ��");
               return false;	
           }
        }	
    }
    return true;
}


