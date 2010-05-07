function checkSubmit(){
    //alert(winform);
    with(winform){
    	//alert(SENTENCEMODE.value);
        if(APLSENTENCEMODE.value==4){
           if(APLWITHDRAWDATE.value==''){
               alert("请输入撤销时间");
               return false;
           } 
           if(APLWITHDRAWREASON.value==''){
               alert("请输入撤销原因");
               return false;	
           }
        }	
    }
    return true;
}


