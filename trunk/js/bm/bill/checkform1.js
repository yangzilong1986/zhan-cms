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
			alert('ֻ�ܶ����м�¼����ȡ��������');
			return false;
		}else{
			 if(win_form.ACPTBILLNO!="undefined"){
				if(!confirm('�Ƿ�ִ��{ȡ���ж�}����?')){
					return false;
				}else{
					return true;
				}
 			}
 			if(win_form.BILLDISNO!="undefined"){
 				if(win_form.DISOBJECT.value==''){
 					if(confirm('�Ƿ�ִ��{ȡ����������}����?')){
	 					return true;
 					}else{
 						return false;
	 				}
 				}else if(win_form.DISOBJECT.value == 'BMZTX1'){
 					if(confirm('�Ƿ�ִ��{ȡ��ת���ֵ��}����?')){
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
 	if(trim(win_form.RATE.value)!=''){
 		if(win_form.RATE.value<=0){
 		alert('����  �������0.');
 		return false;
 		}
 	}
 	if(button.name=='SAVEBUTTON'){
 		if(document.all.Plat_Form_Request_Event_Value.value=='13'){
 			alert('ֻ��״̬�²��ܱ����޸�!');
 			return false;
 		}else{
 		
 			if(win_form.ACPTBILLNO!="undefined"){
 				if(confirm('�Ƿ���л�Ʊ�жҲ�����')){
 					return true;
 				}else{
 					return false;
 				}
 			}
 			if(win_form.BILLDISNO!="undefined"){
 				//alert('123:' + win_form.DISOBJECT.value);
 				if(win_form.DISOBJECT.value==''){
 					if(confirm('�Ƿ���е������ֲ�����')){
	 					return true;
 					}else{
 						return false;
	 				}
 				}else if(win_form.DISOBJECT.value == 'BMZTX1'){
 					if(confirm('�Ƿ����ת���ֵ��������')){
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

