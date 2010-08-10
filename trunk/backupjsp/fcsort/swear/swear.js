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
	var stype = document.all.stype.value;
			
	params = "&QUERYID=" + QUERYID + "&creadate=" + creadate + "&startdate=" + startdate + "&enddate=" + enddate + "&sartbal=" + sartbal + "&endbal=" + endbal + "&stype=" + stype;
}
//*******************PageFunction*******************************
//查询条件初始化

//进入二级页面
function tosecondlist(brhid, btype) {
	setparams();
	if (btype == "9") {
		var url = currentPath + "firstlist.jsp?brhid=" + brhid + params;
	} else {
		var url = currentPath + "secondlist.jsp?brhid=" + brhid + params;
	}
	window.location = url;
}
//返回上级 本机二级
function  backfistList()
{
	setparams();
	var brhid = document.all.brhid.value;
	var url = currentPath + "firstlist.jsp?brhid=" + brhid + params;
	window.location = url;
}
//返回进入页
function backStartList()
{
	var QUERYID = document.all.QUERYID.value;
	var url = currentPath + "firstlist.jsp?QUERYID=" + QUERYID;
	window.location = url;
}
//返回二级
function backSecondlist()
{
	setparams();
	var brhid = document.all.brhid.value;
	var url = currentPath + "secondlist.jsp?brhid=" + brhid + params;
	window.location = url;
}
function tothirdlist(gerter,sqlpartname, sqlpartvalue) {
	setparams();
	//alert(gerter);
	if(gerter=='A')
	{
		return false;
	}
	else if(gerter=='B')
	{
		var brhid = document.all.brhid.value;
		var url = currentPath + "thirdList.jsp?sqlpartname=" + sqlpartname + "&sqlpartvalue=" + sqlpartvalue + params + "&brhid=" + brhid;//+"&CLIENTMGR="+claentmgr;
		window.location = url;
		
	} else
	{
		var brhid = document.all.brhid.value;
		var claentmgr=document.all.CLIENTMGR.value;
		var url = currentPath + "thirdList.jsp?sqlpartname=" + sqlpartname + "&sqlpartvalue=" + sqlpartvalue + params + "&brhid=" + brhid+"&CLIENTMGR="+claentmgr;
		window.location = url;
	}

}

