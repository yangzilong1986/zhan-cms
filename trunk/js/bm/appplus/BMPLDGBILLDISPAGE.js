function checkSubmit(){
with(winform){	
if(firstDateNotLaterThanSecond(ISSUEDATE,DUEDATE ,'��Ʊ����','��������')){
    return true;
}else{
    return false;
}
}
}
