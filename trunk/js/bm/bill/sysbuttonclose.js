/*
 *���Page��Form�е�SYS_BUTTON�Ͱ�ť���رմ���
 *
 *@author ��ΰ
 *@version 1.0
 *@createdate 2004-01-02
 */
 
////////////////////////////////////////////////////////////////////////////
////                            FUNCTION                                ////
////////////////////////////////////////////////////////////////////////////

var xPos = screen.width/2 - 400;
var yPos = screen.height/2 -350;

//alert(xPos+','+yPos);
window.moveTo(xPos,yPos);

function checkSubmit(){
	//alert(value);
	//check
	alert('you shouldn"t be here.');
	window.close();
	return true;
}

function checkSelfSubmit(button) {
	//alert(button);
	if(button.name=='CANCELBUTTON'){
		window.close();
		return false;
	}
	return true;
}