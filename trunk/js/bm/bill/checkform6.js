/*Page Form('BMLRC0')
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
		return false;
	}
	if(button.name=='SAVEBUTTON'){
		if(confirm('�Ƿ���д���Ӳ�����')){
			return true;
		}else{
			return false;
		}
	}
	return true;
}