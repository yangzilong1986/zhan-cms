/*****************************************************************
* name     :checkTextNumber()
* function :����Ƿ�Ϊ����
* parameter:@theObj  Ҫ�����ַ�
*           @isnull  true ����      false ����
* return   :trueΪȫ�����֣�falseΪ��������
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
    alert("���ִ�ֻ�ܰ�������,X,x�������Ƿ������˷Ƿ��ַ���");
    theObj.focus();
    return false;
  }
  return true;
}
/*****************************************************************
* name     :checkLength()
* function :����ַ��ĳ���
* parameter:@len     �ַ�
* return   :sum      �ַ��ĳ���
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
* function :���֤�������Ƿ��׼
* parameter:@idtype     Ҫ����֤������
*           @id         Ҫ����֤������
* return   :trueΪ���ϣ�falseΪ������
******************************************************************/
function checkZJHM(idtype,id)
{
	var nLength;
	if (idtype.options[idtype.selectedIndex].text=="���֤"){
		nLength=checkLength(id.value);
		if (nLength!=15 && nLength!=18){
			alert("��ȷ�����֤�����Ƿ�Ϊ15λ��18λ");
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