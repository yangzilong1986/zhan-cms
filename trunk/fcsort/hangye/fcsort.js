function mOvr(src){
	if (!src.contains(event.fromElement)) {
	dataBgColor=src.bgColor;
	src.style.cursor = 'hand';
	src.bgColor = '#E7E9CF';
	}
}
function mOut(src){
	if (!src.contains(event.toElement)) {
	src.style.cursor = 'default';
	src.bgColor = '#FFFFFF';
	}
}
 function printTable(){
	listform.referValue.value=checkTable.innerHTML;
	window.open(surl+"exporttoExcel.jsp?brhid="+brhid+"&startdate="+startdate
	+"&enddate="+enddate+"&yeardays="+yeardays+"&create="+create+"&colspan="+colspan+"&cou="+cou); 
}
function toseconlist(brhid,btype)
{
		if(btype=='9')
		{
			var url = surl+"list.jsp?&show=true&brhid="+brhid+params;
		}
		else
		{
		var url = surl+"secondList.jsp?brhid="+brhid+params+"&btype="+btype;
		}
		window.location = url;
}
function back(){
		var url= surl+"list.jsp";
		window.location=url;
		}
function tothirdlist(count)
	{
	       
			var url = surl+"thirdList.jsp?brhid="+thirdbrhid+params+"&count="+count;
			
			
			window.location = url;
	}
 
	