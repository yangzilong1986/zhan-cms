function checkSelfSubmit(button){
	if(button=='NewIndvClient' || button=='NewCorpClient'){
		if(document.all.NCLIENTNO.value != '')
		{
			alert('登记新个人客户或企业客户不能选择客户代码!');
			return false;			
		}	
	}
	else
	{
		if(button=='ExistUser')
		{
			if(document.all.NCLIENTNO.value == '')
			{
				alert('使用存在客户功能必须选择客户代码!');
				return false;			
			}				
		}
			
	}
 	return true;
}

