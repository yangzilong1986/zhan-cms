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

function checkSelfSubmit(button){
	var errormsg='';
	var win_form = document.getElementById("winform");
	if (button.name=='CANCELBUTTON'){
		window.close();
		return false;
	}
	if(button.name=='CANCELOPERATION'){
		//alert('event value:'+ document.all.Plat_Form_Request_Event_Value.value);
		if(document.all.Plat_Form_Request_Event_Value.value=='12'){
			alert('只能对已有记录进行取消操作！');
			return false;
		}else{
			 if(win_form.ACPTBILLNO!="undefined"){
				if(!confirm('是否执行{取消承兑}操作?')){
					return false;
				}else{
					return true;
				}
 			}
 			if(win_form.BILLDISNO!="undefined"){
 				if(win_form.DISOBJECT.value==''){
 					if(confirm('是否执行{取消到期贴现}操作?')){
	 					return true;
 					}else{
 						return false;
	 				}
 				}else if(win_form.DISOBJECT.value == 'BMZTX1'){
 					if(confirm('是否执行{取消转贴现垫款}操作?')){
 						return true;
 					}else{
 						return false;
 					}
 				}
 			}

		}
	}

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
 	if(trim(win_form.RATE.value)!=''){
 		if(win_form.RATE.value<=0){
 		alert('利率  必须大于0.');
 		return false;
 		}
 	}
 	if(button.name=='SAVEBUTTON'){
 		if(document.all.Plat_Form_Request_Event_Value.value=='13'){
 			alert('只读状态下不能保存修改!');
 			return false;
 		}else{
 		
 			if(win_form.ACPTBILLNO!="undefined"){
 				if(confirm('是否进行汇票承兑操作？')){
 					return true;
 				}else{
 					return false;
 				}
 			}
 			if(win_form.BILLDISNO!="undefined"){
 				//alert('123:' + win_form.DISOBJECT.value);
 				if(win_form.DISOBJECT.value==''){
 					if(confirm('是否进行到期贴现操作？')){
	 					return true;
 					}else{
 						return false;
	 				}
 				}else if(win_form.DISOBJECT.value == 'BMZTX1'){
 					if(confirm('是否进行转贴现垫款处理操作？')){
 						return true;
 					}else{
 						return false;
 					}
 				}
 			}
 		}
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

function doCheck(){
	if(document.all("IFADVANCED").value=="0"){
		document.all("ADVAMT").value="";
		document.all("RATE").value="";
		document.all("STARTDATE").value="";
		document.all("ENDDATE").value="";
		document.all("FIRSTRESP").value="";
		document.all("FISRTRESPPCT").value="";
		document.all("FRATE").value="";
		document.all("BRATE").value="";
		document.all("ADVAMT").disabled=true;
		document.all("RATE").disabled=true;
		document.all("STARTDATE").disabled=true;
		document.all("ENDDATE").disabled=true;
		document.all("FIRSTRESP").disabled=true;
		document.all("FISRTRESPPCT").disabled=true;
		document.all("FRATE").disabled=true;

		document.all("BRATE").mayNull="1";
		document.all("FRATE").mayNull="1";
	}
	else{
		document.all("ADVAMT").disabled=false;
		document.all("RATE").disabled=false;
		document.all("STARTDATE").disabled=false;
		document.all("ENDDATE").disabled=false;
		document.all("FIRSTRESP").disabled=false;
		document.all("FISRTRESPPCT").disabled=false;
		document.all("FRATE").disabled=false;
	}
}

function Rate_Change(){
	var Instance_ID=document.all("Plat_Form_Request_Instance_ID").value;
	var Event_Value=document.all("Plat_Form_Request_Event_Value").value;
	var url="/templates/defaultform.jsp?Plat_Form_Request_Instance_ID="+Instance_ID+"&Plat_Form_Request_Event_ID=2&Plat_Form_Request_Event_Value="+Event_Value+"&CHGTYPE=RATECHG";
	document.all("winform").action=url;
	document.all("winform").submit();
}

