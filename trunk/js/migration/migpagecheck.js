function checkSelfSubmit(button){
	if(button=='NewIndvClient' || button=='NewCorpClient'){
		if(document.all.NCLIENTNO.value != '')
		{
			alert('�Ǽ��¸��˿ͻ�����ҵ�ͻ�����ѡ��ͻ�����!');
			return false;			
		}	
	}
	else
	{
		if(button=='ExistUser')
		{
			if(document.all.NCLIENTNO.value == '')
			{
				alert('ʹ�ô��ڿͻ����ܱ���ѡ��ͻ�����!');
				return false;			
			}				
		}
			
	}
 	return true;
}

