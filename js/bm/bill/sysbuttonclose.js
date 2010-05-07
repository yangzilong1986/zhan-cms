/*
 *点击Page型Form中的SYS_BUTTON型按钮，关闭窗口
 *
 *@author 李伟
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