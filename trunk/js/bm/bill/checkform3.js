/*Page Form('BMLLP0')
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
	if(button.name=='PRINTBUTTON'){
		//��ӡ
		alert('��ӡ����');
		window.print();
		return false;
	}
	return true;
}