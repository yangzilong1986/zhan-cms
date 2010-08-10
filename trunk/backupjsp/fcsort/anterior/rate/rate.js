
/**
*迁徙率页面JS
*@author zhengxin 
*/
var params = "";
//返回到正常类迁徙率首页
function backStartListKS() {
	var QUERYID = document.all.QUERYID.value;
	var url = currentPath + "Rate_KS.jsp?QUERYID=" + QUERYID;
	window.location = url;
}
//设置页面传递的参数
function setparams() {
	var QUERYID = document.all.QUERYID.value;
	var creadate = document.all.creadate.value;
	params = "&QUERYID=" + QUERYID + "&creadate=" + creadate;
}
//点击每行出发的事件
function tosecondlistKS(brhid) {
	setparams();
	var url = currentPath + "Rate_KS.jsp?brhid=" + brhid + params;
	window.location = url;
}
//点击每行出发的事件
function tosecondlistZS(brhid) {
	setparams();
	var url = currentPath + "Rate_ZS.jsp?brhid=" + brhid + params;
	window.location = url;
}
//返回到非正常类迁徙率首页
function backStartListZS() {
	var QUERYID = document.all.QUERYID.value;
	var url = currentPath + "Rate_ZS.jsp?QUERYID=" + QUERYID;
	window.location = url;
}

