
function pwdenter1()
{
	if(event.keyCode == 13)
	{
		event.returnValue=false;
		document.all.NEWPWD.focus();
	}	
}

function pwdenter2()
{
	if(event.keyCode == 13)
	{
		event.returnValue=false;
		document.all.CONFIRMPWD.focus();
	}	
}

function pwdenter3()
{
	if(event.keyCode == 13)
	{
		event.returnValue=false;
		document.all.CHANGE.focus();
	}	
}

function checkSelfSubmit(button){
	return true;
}