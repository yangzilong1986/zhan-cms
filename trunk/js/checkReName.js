/**
 * ��������ȡһ������
 * */
function $(obj_name) {
	return document.getElementsByName(obj_name)[0];
}
/**
 * ��������ȡ�����valueֵ
 * */
function $F(obj_name) {
	var obj = $(obj_name);
	return obj.value.Trim();
}

function checkLinkname(obj_name) {
	var obj = $(obj_name);
	var pname = $F(obj_name);
	var name = $F('NAME');
	if(pname == name) {
		alert("������ϵ�����������뱾��������ͬ��");
		obj.select();
		//obj.focus();
		return false;
	} 
	return true;
}

function checkLinkPhone(obj_name) {
	var obj = $(obj_name);
	var linkPhone = $F(obj_name);
	var phone = $F('PHONE1');
	if(phone == linkPhone) {
		alert("������ϵ���ֻ����벻���뱾���ֻ�������ͬ��");
		obj.select();
		//obj.focus();
		return false;
	} 
	return true;
}

function checkAmt() {
	var v_EMPNO = $F('EMPNO');
	var v_AMT = $F('AMT');
	var v_RECEIVEAMT = $F('RECEIVEAMT');
	var v_MONTHLYPAY = $F('MONTHLYPAY');

	if(!(v_EMPNO != null && v_EMPNO != '' && v_MONTHLYPAY >= 2000)) {
		var v_r = Math.floor(v_AMT * 0.2);
		if(v_RECEIVEAMT < v_r) {
			alert('�׸��������' + v_r + "Ԫ��");
			$('RECEIVEAMT').select();
			return false;
		}
	}
	return true;
}