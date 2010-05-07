/*Page Form('BMLLP0')
 *提交判断
 * @author liwei
 * @version 1.0
*/
/*****************************************************************************
                            MODULE VARIABLES
*****************************************************************************/
var xPos = screen.width/2 - 400;
var yPos = screen.height/2 -350;

//alert(xPos+','+yPos);
window.moveTo(xPos,yPos);

/*****************************************************************************
                                    FUNCTIONS
*****************************************************************************/


function checkSelfSubmit(button) {
	//alert(button);
	if(button.name=='CANCELBUTTON'){
		window.close();
		return false;
	}
	if(button.name=='ACPTBILLBUTTON'){
		//打印
		//alert('查看(转)贴现信息');
		//window.print();
		return true;
	}
	if(button.name=='PRINTMOPAIEDNEY'){
		//打印
		//alert('打印垫款借据');
		//window.print();
		return true;
	}
	return true;
}
function checkSubmit(){
	return true;
}