/*Page Form('BMRQLM')
 *�ύ�ж�
 * @author liwei
 * @version 1.0
*/
/*****************************************************************************
                            MODULE VARIABLES
*****************************************************************************/

var xPos = screen.width/2 - 400;
var yPos = screen.height/2 -350;


window.moveTo(xPos,yPos);

/*****************************************************************************
                                    FUNCTIONS
*****************************************************************************/

function checkSubmit(){
	var win_form = document.getElementById("winform");
	if((win_form.PAYDATE.value!='') && (win_form.ENDDATE.value!='')
 	  &&(!compDate(win_form.PAYDATE,win_form.ENDDATE,'��������','������'))){
 		return false;
 	}
 	if((win_form.PAYDATE.value!='') && (win_form.NOWENDDATE.value!='')
 	  &&(!compDate(win_form.PAYDATE,win_form.NOWENDDATE,'��������','ʵ�ʵ�����'))){
 		return false;
 	}
	if((win_form.PAYDATE.value!='') && (win_form.CLOSEDATE.value!='')
 	  &&(!compDate(win_form.PAYDATE,win_form.CLOSEDATE,'��������','���������'))){
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