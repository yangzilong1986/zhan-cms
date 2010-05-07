//****************PageParams**************
var params = "";
function setparams() {
	var brhid = document.all.brhid.value;
	var QUERYID = document.all.QUERYID.value;
	var creadate = document.all.creadate.value;
	var startdate = document.all.startdate.value;
	var enddate = document.all.enddate.value;
	var sartbal = document.all.sartbal.value;
	var endbal = document.all.endbal.value;
	//var stype = document.all.stype.value;
			//alert(stype);
	params = "&QUERYID=" + QUERYID + "&creadate=" + creadate + "&startdate=" + startdate + "&enddate=" + enddate + "&sartbal=" + sartbal + "&endbal=" + endbal;// + "&stype=" + stype;
}

//*******************PageFunction*******************************
function tosecondlistp(gerter,sqlpartname) {
	setparams();
	if(gerter=='A')
	{
		return false;
	}
	else if(gerter=='B')
	{
		var brhid = document.all.brhid.value;
		var url = currentPath + "secondlist.jsp?sqlpartname=" + sqlpartname + params + "&brhid=" + brhid;
		//alert(url);
		window.location = url;
		
	} else
	{
		var brhid = document.all.brhid.value;
		var claentmgr=document.all.CLIENTMGR.value;
		
		var url = currentPath + "secondlist.jsp?sqlpartname=" + sqlpartname + params + "&brhid=" + brhid+"&CLIENTMGR="+claentmgr;
		//alert(url);
		window.location = url;
	}
	
	
}

//返回初始页
function backStartList()
{
	var QUERYID = document.all.QUERYID.value;
	var url = currentPath + "firstlist.jsp?QUERYID=" + QUERYID;
	window.location = url;
}
//返回上级
function  backfistList()
{
	setparams();
	var brhid = document.all.brhid.value;
	var url = currentPath + "firstlist.jsp?brhid=" + brhid + params;
	window.location = url;
}

