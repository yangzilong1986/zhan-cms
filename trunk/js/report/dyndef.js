/***************************************************************
* function :本程序处理了系统管理员定义动态报表的数据校验
* date     :2004/05/26
* author   :yusg
*****************************************************************/
/****************************************************************
* name     : checkWidth()
* function : 检查输入的数据是否是以w开头接数字，如(w100,w300)
* parameter: 无
* return   : true     符合
*            false    不符合
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
			   alert("列长度格式不合法！");
			   return false;
		   }
		   else
			   return true;
		}
		else
		{
			alert('报表列名的个数和列宽度的个数不等！');
			return false;
		}
	}
}

/****************************************************************
* name     : checkPos()
* function : 检查输入的数据是否是以p开头接数字，如(p100,p300)
* parameter: 无
* return   : true     符合
*            false    不符合
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
			    alert("标题位置的格式不合法！");
			    return false;
			}
			else
				return true;
		}
		else
		{
			alert('标题位置如果定义必须为4个！');
			return false;
		}
	}
}

/****************************************************************
* name     : checkPlanish()
* function : 检查输入的数据是否是以下的样式，如(3=2+1,4=5+6)
* parameter: 无
* return   : true     符合
*            false    不符合
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
* function : 检查输入的数据是否是以下的样式，如(3=2/1,4=5/6)
* parameter: 无
* return   : true     符合
*            false    不符合
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
* function : 检查输入的数据是否是以字母开头接数字，如(w100,p300)
* parameter: @str     需要检查的字符串
*            @letter  开头的字母
* return   : true     符合
*            false    不符合
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
* function : 判断以逗号分割的字符串数组的长度
* parameter: @str          需要检查的字符串
* return   : 0或length     数组的长度
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
* function : 报表总列数和报表列名的个数是否相等
* parameter: 无
* return   : true     相等
*            false    不等
****************************************************************/
function isEqual()
{
	var colNum=document.all.COLNUM;
	var colName=document.all.COLNAME;

	if(colNum.value!=splitComma(colName.value))
	{
		alert("报表总列数和报表列名的个数不等！");
		return false;
	}
	else
		return true;
}
/****************************************************************
* name     : checkOperator()
* function : 检查输入的数据是否是以合法的运算符连接，如(3=2+1,3=2/1)
* parameter: @str    需要检查的字符串
*            @oper   合法的运算符,现定义+或/
* return   : true    符合
*            false   不符合
****************************************************************/
function checkOperator(aValue,oper){

   //匹配11=12+33
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
* function : 判断打平公式，比率公式，宽度，位置是否合法
* return   : true    符合
*            false   不符合
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
		alert("打平公式格式不合法！");
		return false;
	}
	
	if(!checkRate())
	{
		alert("比率公式格式不合法！");
		return false;
	}
	else
	{
		return true;
	}
}
