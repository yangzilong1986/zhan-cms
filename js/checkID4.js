/*****************************************************************
* name     :checkTextNumber()
* function :检查是否为数字
* parameter:@theObj  要检查的字符
*           @isnull  true 符合      false 不符
* return   :true为全是数字，false为不是数字
******************************************************************/
function checkTextNumber( theObj,isnull )
{
	var checkOK = "0123456789";
	var checkStr = theObj.value;
	var allValid = true;
	if(theObj.length==0)
	{
		if(isnull==true)
			return true;
		else
			return false;
	}
	for (i = 0;  i < checkStr.length;  i++)
	{
		ch = checkStr.charAt(i);
		for (j = 0;  j < checkOK.length;  j++)
		{
			if (ch == checkOK.charAt(j))
				break;
			if (i == 17)
			{
				if(ch == "X" || ch== "x")
					break;
			}
		}
		if (j == checkOK.length)
		{
			allValid = false;
			break;
		}
	}
  if (!allValid)
  {
    alert("该字串只能包含数字,X,x，请检查是否输入了非法字符！");
    theObj.focus();
    return false;
  }
  return true;
}
/*****************************************************************
* name     :checkLength()
* function :检查字符的长度
* parameter:@len     字符
* return   :sum      字符的长度
******************************************************************/
function checkLength(len) 
{
	var i,sum;
	sum=0;
	for(i=0;i<len.length;i++) 
	{
		if ((len.charCodeAt(i)>=0)&&(len.charCodeAt(i)<=255)&&(len.charAt(i)!=" "))
			sum=sum+1;
		else if(len.charAt(i)==" ")
			sum=sum;
		else
			sum=sum+2;
	}
	return sum;
}
/*****************************************************************
* name     :checkZJHM()
* function :检查证件号码是否标准
* parameter:@idtype     要检查的证件名称
*           @id         要检查的证件号码
* return   :true为符合，false为不符合
******************************************************************/
function checkZJHM(idtype,id)
{
	var nLength;
	if (idtype.options[idtype.selectedIndex].text=="身份证"){
		nLength=checkLength(id.value);
		if (nLength!=15 && nLength!=18){
			alert("请确认身份证号码是否为15位或18位");
                        id.focus();
			return false;
		}else{	
		    return checkTextNumber(id,false);
		}
	}
}

function checkSubmit()
{
   //alert("test");
   //alert(document.all.IDTYPE.value);
   if(document.all.IDTYPE.value==1){
	   return checkZJHM(document.all.IDTYPE,document.all.ID);
   }else{
       return true;
   }
}