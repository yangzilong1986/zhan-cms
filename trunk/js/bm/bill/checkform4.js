/*Page Form('BMLLP0')
 *�ύ�ж�
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
		//��ӡ
		//alert('�鿴(ת)������Ϣ');
		//window.print();
		return true;
	}
	if(button.name=='PRINTMOPAIEDNEY'){
		//��ӡ
		//alert('��ӡ�����');
		//window.print();
		return true;
	}
	return true;
}
function checkSubmit(){
	return true;
}