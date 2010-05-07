/*
*   函数名   |      说明
*-----------------------------------
*   mOvr()   |  表格鼠标特效一
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
*   函数名   |      说明
*-----------------------------------
*   mOut()   |  表格鼠标特效二
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
