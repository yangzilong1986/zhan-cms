/*Page Form('BMLG00')
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

var xPos = screen.width/2 - 400;
var yPos = screen.height/2 -350;

//alert(xPos+','+yPos);
window.moveTo(xPos,yPos);

function checkSelfSubmit(button) {
	//alert(button);
	if(button.name=='CANCELBUTTON'){
		if(!confirm('�Ƿ�ȷ��ȡ��ҵ��')){
			return false;
		}
	}else if(button.name=='AUTHORBUTTON'){
		if(!confirm('�Ƿ�ȷ�Ϸ�����Ȩ��')){
			return false;
		}
	}else if(button.name=='NONAUTHORBUTTON'){
		if(!confirm('�Ƿ�ȷ��ȡ����Ȩ��')){
			return false;
		}
	}
	return true;
}