function checkSubmit(){
with(winform){	
if(firstDateNotLaterThanSecond(ISSUEDATE,DUEDATE ,'出票日期','到期日期')){
    return true;
}else{
    return false;
}
}
}
