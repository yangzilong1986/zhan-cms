/*Page Form('BMHPC0')
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

function checkSubmit(){
var errormsg='';
var win_form = document.getElementById("winform");
//alert(document.all.IFADVANCED.value);
 	if(win_form.IFADVANCED.value == '1'){
 		if(win_form.ADVAMT.value == ''){
 			errormsg = errormsg +'"垫款金额" 不能为空\r\n';
 		}
		if(win_form.RATE.value == ''){
 			errormsg = errormsg +'"利率"     不能为空\r\n';
 		}
 		if(win_form.STARTDATE.value == ''){
 			errormsg = errormsg +'"开始日期" 不能为空\r\n';
 		}
 		if(win_form.ENDDATE.value == ''){
 			errormsg = errormsg +'"结束日期" 不能为空\r\n';
 		}
 		if(win_form.FIRSTRESP.value == ''){
 			errormsg = errormsg +'"第一责任人" 不能为空\r\n';
 		}
 		if(win_form.FISRTRESPPCT.value == ''){
 			errormsg = errormsg +'"第一责任比例" 不能为空\r\n';
 		}
 	}
 	//alert(errormsg);
 	if(errormsg !=''){
 		alert(errormsg);
 		return false;
 	}
 	if((win_form.STARTDATE.value!='') && (win_form.ENDDATE.value!='')
 	  &&(!compDate(win_form.STARTDATE,win_form.ENDDATE,'开始日期','结束日期'))){
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
	//alert('1234');
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