
/**
*Ǩ����ҳ��JS
*@author zhengxin 
*/
var params = "";
//���ص�������Ǩ������ҳ
function backStartListKS() {
	var QUERYID = document.all.QUERYID.value;
	var url = currentPath + "Rate_KS.jsp?QUERYID=" + QUERYID;
	window.location = url;
}
//����ҳ�洫�ݵĲ���
function setparams() {
	var QUERYID = document.all.QUERYID.value;
	var creadate = document.all.creadate.value;
	params = "&QUERYID=" + QUERYID + "&creadate=" + creadate;
}
//���ÿ�г������¼�
function tosecondlistKS(brhid) {
	setparams();
	var url = currentPath + "Rate_KS.jsp?brhid=" + brhid + params;
	window.location = url;
}
//���ÿ�г������¼�
function tosecondlistZS(brhid) {
	setparams();
	var url = currentPath + "Rate_ZS.jsp?brhid=" + brhid + params;
	window.location = url;
}
//���ص���������Ǩ������ҳ
function backStartListZS() {
	var QUERYID = document.all.QUERYID.value;
	var url = currentPath + "Rate_ZS.jsp?QUERYID=" + QUERYID;
	window.location = url;
}

