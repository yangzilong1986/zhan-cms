/*Page Form('BMERL0')
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
function checkSelfSubmit(button) {
	//alert(button);
	var win_form = document.getElementById("winform");
	if(button.name=='CANCELBUTTON'){
		window.close();
	}
	if(button.name=='SEARCHLOANTABLE'){
			var str = win_form.action+'?Plat_Form_Request_Instance_ID='+win_form.Plat_Form_Request_Instance_ID.value+'&Plat_Form_Request_Event_ID=9&Plat_Form_Request_Event_Value='+win_form.Plat_Form_Request_Event_Value.value+'&Plat_Form_Request_Button_Event='+button.name+'&BMNO='+win_form.BMNO.value;
			//alert(str);
			var sswin = window.open(str,'temp','height=600,width=800,toolbar=no,scrollbars=yes');
			return false;
			//���Ҵ���̨��
			//alert('SEARCHLOANTABLE');
	}

	if(button.name=='SELFSAVE'){
		if((trim(win_form.CLIENTNO.value).length>0)&&(trim(win_form.SBMNO.value).length>0)){
			alert('"�ͻ�����"��"ҵ���"����ͬʱ���롣');
			return false;
		}
		if((trim(win_form.CLIENTNO.value).length<=0)&&(trim(win_form.SBMNO.value).length<=0)){
			alert('"�ͻ�����"��"ҵ���"��������һ�');
			return false;
		}
		if(trim(win_form.CLIENTNO.value).length>0){
			if(!confirm('�Ƿ�����´�����Ȩ��')){
				return false;
			}
		}else{
			if(!confirm('�Ƿ�ʹ��ϵͳ���ڵ���Ȩ��¼��')){
				return false;
			}
		}
		//����
		//alert('SELFSAVE');
	}
	return true;
}
function checkSubmit(){
	//alert('1234');
	//var instanceid = document.all.Plat_Form_Request_Instance_ID.value;
	//alert('Plat_Form_Request_Instance_ID:'+document.all.Plat_Form_Request_Instance_ID.value);
	window.open('/templates/authref.jsp','temp','height=350,width=460,toolbar=no,scrollbars=yes');
	return false;
}