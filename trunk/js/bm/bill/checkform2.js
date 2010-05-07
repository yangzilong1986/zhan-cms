/*Page Form('BMERL0')
 *提交判断
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
			//查找贷款台帐
			//alert('SEARCHLOANTABLE');
	}

	if(button.name=='SELFSAVE'){
		if((trim(win_form.CLIENTNO.value).length>0)&&(trim(win_form.SBMNO.value).length>0)){
			alert('"客户代码"和"业务号"不能同时输入。');
			return false;
		}
		if((trim(win_form.CLIENTNO.value).length<=0)&&(trim(win_form.SBMNO.value).length<=0)){
			alert('"客户代码"或"业务号"至少输入一项。');
			return false;
		}
		if(trim(win_form.CLIENTNO.value).length>0){
			if(!confirm('是否产生新贷款授权？')){
				return false;
			}
		}else{
			if(!confirm('是否使用系统存在的授权记录？')){
				return false;
			}
		}
		//保存
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