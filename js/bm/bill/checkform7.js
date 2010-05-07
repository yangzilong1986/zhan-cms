/*Page Form('BMRQLM')
 *提交判断
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
 	  &&(!compDate(win_form.PAYDATE,win_form.ENDDATE,'发放日期','到期日'))){
 		return false;
 	}
 	if((win_form.PAYDATE.value!='') && (win_form.NOWENDDATE.value!='')
 	  &&(!compDate(win_form.PAYDATE,win_form.NOWENDDATE,'发放日期','实际到期日'))){
 		return false;
 	}
	if((win_form.PAYDATE.value!='') && (win_form.CLOSEDATE.value!='')
 	  &&(!compDate(win_form.PAYDATE,win_form.CLOSEDATE,'发放日期','贷款结清日'))){
 		return false;
 	}

	return true;
}

/*
 * 比较日期函数
 *@param date1obj 开始日期text对象
 *@param date2obj 结束日期text对象
 *@param name1    date1obj的名称
 *@param name2 	  date2obj的名称
 *@returnvalue 	 日期比较时任何异常返回值为false,正确则返回true 
*/
function compDate(date1obj,date2obj,name1,name2){
	var idate1=0;
	var idate2=0;
	if(date1obj!="undefined" && date2obj!="undefined"){
		idate1 = date1obj.value;
		idate2 = date2obj.value;
	}else{
		alert('输入参数不存在.');
		return false;
	}
	if(idate1!=0&&idate2!=0){
		if(idate1>idate2){
			alert('"'+name2+'"'+'早于'+'"'+name1+'"');
			return false;
		}else{
			return true;
		}
	}else{
		alert('时间格式不正确.');
		return false;
	}
}