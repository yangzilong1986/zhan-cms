//Select¶àÑ¡¿ò
function getCategory()
{
	if (document.all.Layer1.style.display=='none')
	{
		document.all.Layer1.style.display='';
	}
	else
	{
		document.all.Layer1.style.display='none';
		var s=new Array();
		var acclist = document.all.ACCLIST;
		for (var i=0;i<acclist.options.length;i++)
		{
			if (acclist.options[i].selected==true)
			s.push(acclist.options[i].value.Trim());
		}
		document.forms[0].arracclist.value=s.join(',');
	}
}