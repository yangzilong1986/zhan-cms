/*Page Form('BMABT0')
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
	if(button.name=='CANCELBUTTON'){
		window.close();
	}
	if(button.name=='ACPTBILLBUTTON'){
		//��ӡ
		//alert('�鿴��Ʊ�ж�');
		//window.print();
		//return false;
	}
	if(button.name=='PRINTMOPAIEDNEY'){
		//��ӡ
		//alert('��ӡ�����');
		//window.print();
		//return false;
	}
	return true;
}
function checkSubmit(){
	return true;
}