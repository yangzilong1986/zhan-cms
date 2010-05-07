function EntertoFocusNext()
{
 try
 {
	if(event.keyCode == 13)
	{  
	  //alert(event.srcElement.type.toLowerCase());
	  if(typeof(event.srcElement.name) != "undefined" && event.srcElement.name != null && (event.srcElement.type.toLowerCase() == 'text' || event.srcElement.type.toLowerCase().indexOf('select') >=0 ))
	  {
		var i = 0;
		for(;i<document.all.length;i++)
		{	
			if(document.all[i].name == event.srcElement.name)
			{
				break;
			}
		} 
		
		if(i < document.all.length)
		{
			for(t=i+1;t<document.all.length;t++)
			{
				//if(document.all[t].name != null && document.all[t].style.visibility != "hidden")
				if(document.all[t].name != null && document.all[t].style.visibility != "hidden" && document.all[t].type != "hidden" && document.all[t].readOnly != true && document.all[t].disabled != true)				
				{
					//alert("found");
					document.all[t].focus();
					break;
				}	
			}
		} //end if
		
		event.returnValue=false;
		
	  }
	}
 }
 catch (Error) {};
}

window.document.onkeydown=EntertoFocusNext;