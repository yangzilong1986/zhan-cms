/*Page Form('BMLLP0')
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
	if(button.name=='CANCELBUTTON'){
		window.close();
	}
	if(button.name=='PRINTBUTTON'){
		//打印
		alert('打印功能');
		window.print();
		return false;
	}
	return true;
}