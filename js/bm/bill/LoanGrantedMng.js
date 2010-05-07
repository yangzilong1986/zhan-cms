/*Page Form('BMLG00')
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

var xPos = screen.width/2 - 400;
var yPos = screen.height/2 -350;

//alert(xPos+','+yPos);
window.moveTo(xPos,yPos);

function checkSelfSubmit(button) {
	//alert(button);
	if(button.name=='CANCELBUTTON'){
		if(!confirm('是否确认取消业务？')){
			return false;
		}
	}else if(button.name=='AUTHORBUTTON'){
		if(!confirm('是否确认发送授权？')){
			return false;
		}
	}else if(button.name=='NONAUTHORBUTTON'){
		if(!confirm('是否确认取消授权？')){
			return false;
		}
	}
	return true;
}