/*Page Form('BMLRC0')
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
		return false;
	}
	if(button.name=='SAVEBUTTON'){
		if(confirm('是否进行贷款交接操作？')){
			return true;
		}else{
			return false;
		}
	}
	return true;
}