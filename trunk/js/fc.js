/*
*   ������   |      ˵��
*-----------------------------------
*   mOvr()   |  ��������Чһ
*
*/

function mOvr(src){
	if (!src.contains(event.fromElement)) {
	dataBgColor=src.bgColor;
	src.style.cursor = 'hand';
	src.bgColor = '#E7E9CF';
	}
}

/*
*   ������   |      ˵��
*-----------------------------------
*   mOut()   |  ��������Ч��
*
*/

function mOut(src,color){
	if (!src.contains(event.toElement)) {
	src.style.cursor = 'default';
	src.bgColor = color;
	}
}    
function mClk(val)
{
	window.parent.frames["ifrmDetail"].document.location.href=val;
}
function setVal(obj,val)
{
	for(var i=0;i<obj.options.length;i++)
	{
		if (obj.options[i].value==val)
		{
			obj.options[i].selected=true;
			continue;
		}
	}
}
