/*Page Form('BMABT0')
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
	if(button.name=='ACPTBILLBUTTON'){
		//打印
		//alert('查看汇票承兑');
		//window.print();
		//return false;
	}
	if(button.name=='PRINTMOPAIEDNEY'){
		//打印
		//alert('打印垫款借据');
		//window.print();
		//return false;
	}
	return true;
}
function checkSubmit(){
	return true;
}