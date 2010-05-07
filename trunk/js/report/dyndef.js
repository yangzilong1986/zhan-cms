/***************************************************************
* function :����������ϵͳ����Ա���嶯̬���������У��
* date     :2004/05/26
* author   :yusg
*****************************************************************/
/****************************************************************
* name     : checkWidth()
* function : �������������Ƿ�����w��ͷ�����֣���(w100,w300)
* parameter: ��
* return   : true     ����
*            false    ������
****************************************************************/
function checkWidth()
{
	var col=document.all.COLLEN;
	var colName=document.all.COLNAME;

	//alert(col.value);
	if(col.value.length == 0)
		return true;
	else
	{
	    if(splitComma(col.value)==splitComma(colName.value))
		{
		   if(!checkLetter(col.value,'w'))
		   {
			   alert("�г��ȸ�ʽ���Ϸ���");
			   return false;
		   }
		   else
			   return true;
		}
		else
		{
			alert('���������ĸ������п�ȵĸ������ȣ�');
			return false;
		}
	}
}

/****************************************************************
* name     : checkPos()
* function : �������������Ƿ�����p��ͷ�����֣���(p100,p300)
* parameter: ��
* return   : true     ����
*            false    ������
****************************************************************/
function checkPos()
{
	var psn=document.all.MTITLE;
    
	//alert(psn.value);
	if(psn.value.length == 0)
		return true;
	else
	{
	    if(splitComma(psn.value)==4)
		{
		    if(!checkLetter(psn.value,'p'))
			{
			    alert("����λ�õĸ�ʽ���Ϸ���");
			    return false;
			}
			else
				return true;
		}
		else
		{
			alert('����λ������������Ϊ4����');
			return false;
		}
	}
}

/****************************************************************
* name     : checkPlanish()
* function : �������������Ƿ������µ���ʽ����(3=2+1,4=5+6)
* parameter: ��
* return   : true     ����
*            false    ������
****************************************************************/
function checkPlanish()
{
	var pl=document.all.PLANISH;

	//alert(pl.value);
	if(pl.value.length == 0)
		return true;
    else
	    return checkOperator(pl.value,'+');
}

/****************************************************************
* name     : checkRate()
* function : �������������Ƿ������µ���ʽ����(3=2/1,4=5/6)
* parameter: ��
* return   : true     ����
*            false    ������
****************************************************************/
function checkRate()
{
	var rate=document.all.RATE;

	//alert(rate.value);
	if(rate.value.length == 0)
		return true;
	else
	    return checkOperator(rate.value,'/');

}
/****************************************************************
* name     : checkLetter()
* function : �������������Ƿ�������ĸ��ͷ�����֣���(w100,p300)
* parameter: @str     ��Ҫ�����ַ���
*            @letter  ��ͷ����ĸ
* return   : true     ����
*            false    ������
****************************************************************/
function checkLetter(str,lett)
{
	var gs="(^["+lett+"][1-9][0-9]*)(,["+lett+"][1-9][0-9]*)*$";
	//var re=new RegExp("(^[w|p][1-9][0-9]*)(,[w|p][1-9][0-9]*)*$");
	var re=new RegExp(gs);
	if(re.test(str))
	{
		return true;
	}
	else
	{
		return false;
	}
}
/****************************************************************
* name     : splitComma()
* function : �ж��Զ��ŷָ���ַ�������ĳ���
* parameter: @str          ��Ҫ�����ַ���
* return   : 0��length     ����ĳ���
****************************************************************/
function splitComma(str)
{
	var ss;
	if(str.length==0)
		return 0;
	else
	{
		ss = str.split(',');
		return ss.length;
	}

}
/****************************************************************
* name     : isEqual()
* function : �����������ͱ��������ĸ����Ƿ����
* parameter: ��
* return   : true     ���
*            false    ����
****************************************************************/
function isEqual()
{
	var colNum=document.all.COLNUM;
	var colName=document.all.COLNAME;

	if(colNum.value!=splitComma(colName.value))
	{
		alert("�����������ͱ��������ĸ������ȣ�");
		return false;
	}
	else
		return true;
}
/****************************************************************
* name     : checkOperator()
* function : �������������Ƿ����ԺϷ�����������ӣ���(3=2+1,3=2/1)
* parameter: @str    ��Ҫ�����ַ���
*            @oper   �Ϸ��������,�ֶ���+��/
* return   : true    ����
*            false   ������
****************************************************************/
function checkOperator(aValue,oper){

   //ƥ��11=12+33
   //^[1-9][0-9]*=[1-9][0-9]*(\\+|/)[1-9][0-9]*
    var gs='-1';
    if(oper=='+')
		gs="(^[1-9][0-9]*=[1-9][0-9]*\\+[1-9][0-9]*)(,[1-9][0-9]*=[1-9][0-9]*\\+[1-9][0-9]*)*$";
	if(oper=='/')
		gs="(^[1-9][0-9]*=[1-9][0-9]*/[1-9][0-9]*)(,[1-9][0-9]*=[1-9][0-9]*/[1-9][0-9]*)*$";

    if(gs=='-1')
		return false;
	var re = new RegExp(gs);
    if(re.test(aValue))
	{
        return true;
    }
	else
	{
		return false;
    } 
}
/****************************************************************
* name     : checkSubmit()
* function : �жϴ�ƽ��ʽ�����ʹ�ʽ����ȣ�λ���Ƿ�Ϸ�
* return   : true    ����
*            false   ������
****************************************************************/
function checkSubmit()
{
	if(!isEqual())
	{
		return false;
	}
	if(!checkWidth())
	{
		return false;
	}
	if(!checkPos())
	{
		return false;
	}
    if(!checkPlanish())
	{
		alert("��ƽ��ʽ��ʽ���Ϸ���");
		return false;
	}
	
	if(!checkRate())
	{
		alert("���ʹ�ʽ��ʽ���Ϸ���");
		return false;
	}
	else
	{
		return true;
	}
}
