/*Page Form('BMABT0')
 *Ìá½»ÅÐ¶Ï
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
		//´òÓ¡
		//alert('²é¿´»ãÆ±³Ð¶Ò');
		//window.print();
		//return false;
	}
	if(button.name=='PRINTMOPAIEDNEY'){
		//´òÓ¡
		//alert('´òÓ¡µæ¿î½è¾Ý');
		//window.print();
		//return false;
	}
	return true;
}
function checkSubmit(){
	return true;
}