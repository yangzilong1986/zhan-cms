/*Page Form('BMTXTM')
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
	if((win_form.ISSUEDATE.value!='') && (win_form.DUEDATE.value!='')
 	  &&(!compDate(win_form.ISSUEDATE,win_form.DUEDATE,'出票日','到期日'))){
 		return false;
 	}
 	if(win_form.BRHID.value==''){
 		alert('网点号不能为空!');
 		win_form.BRHID.focus();
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