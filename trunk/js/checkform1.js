/*Page Form('BMHPC0')
 *�ύ�ж�
 * @author liwei
 * @version 1.0
*/
/*****************************************************************************
                            MODULE VARIABLES
*****************************************************************************/


/*****************************************************************************
                                    FUNCTIONS
*****************************************************************************/

function checkSubmit(){
var errormsg='';
var win_form = document.getElementById("winform");
//alert(document.all.IFADVANCED.value);
 	if(win_form.IFADVANCED.value == '1'){
 		if(win_form.ADVAMT.value == ''){
 			errormsg = errormsg +'"�����" ����Ϊ��\r\n';
 		}
		if(win_form.RATE.value == ''){
 			errormsg = errormsg +'"����"     ����Ϊ��\r\n';
 		}
 		if(win_form.STARTDATE.value == ''){
 			errormsg = errormsg +'"��ʼ����" ����Ϊ��\r\n';
 		}
 		if(win_form.ENDDATE.value == ''){
 			errormsg = errormsg +'"��������" ����Ϊ��\r\n';
 		}
 		if(win_form.FIRSTRESP.value == ''){
 			errormsg = errormsg +'"��һ������" ����Ϊ��\r\n';
 		}
 		if(win_form.FISRTRESPPCT.value == ''){
 			errormsg = errormsg +'"��һ���α���" ����Ϊ��\r\n';
 		}
 	}
 	//alert(errormsg);
 	if(errormsg !=''){
 		alert(errormsg);
 		return false;
 	}
 	if((win_form.STARTDATE.value!='') && (win_form.ENDDATE.value!='')
 	  &&(!compDate(win_form.STARTDATE,win_form.ENDDATE,'��ʼ����','��������'))){
 		return false;
 	}
 	return true;
}


/*
 * �Ƚ����ں���
 *@param date1obj ��ʼ����text����
 *@param date2obj ��������text����
 *@param name1    date1obj������
 *@param name2 	  date2obj������
 *@returnvalue 	 ���ڱȽ�ʱ�κ��쳣����ֵΪfalse,��ȷ�򷵻�true 
*/
function compDate(date1obj,date2obj,name1,name2){
	//alert('1234');
	var idate1=0;
	var idate2=0;
	if(date1obj!="undefined" && date2obj!="undefined"){
		idate1 = date1obj.value;
		idate2 = date2obj.value;
	}else{
		alert('�������������.');
		return false;
	}
	if(idate1!=0&&idate2!=0){
		if(idate1>idate2){
			alert('"'+name2+'"'+'����'+'"'+name1+'"');
			return false;
		}else{
			return true;
		}
	}else{
		alert('ʱ���ʽ����ȷ.');
		return false;
	}
}