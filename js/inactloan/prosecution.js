function checkSubmit(){
    //alert(winform);
    with(winform){
    	//alert(SENTENCEMODE.value);
        if(SENTENCEMODE.value==4){
           if(WITHDRAWDATE.value==''){
               alert("请输入撤销时间");
               return false;
           } 
           if(WITHDRAWREASON.value==''){
               alert("请输入撤销原因");
               return false;	
           }
        }	
    }
    return true;
}

function doChange1(){
    with(winform){
        if(IFAPPEAL.value==0){
            ERSHEN.disabled=true;	
        }else{
            ERSHEN.disabled=false;	
        }	
    }
}

function doChange2(){
    with(winform){
        if(IFSHENSU.value==0){
            	SHENSU.disabled=true;
        }else{
            	SHENSU.disabled=false;
        }	
    }

}

