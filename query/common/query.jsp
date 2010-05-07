<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.report.Query" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>

<%
RequestDispatcher rd=null;
String rtok="";

String qryNo=null;
qryNo=request.getParameter("qryNo");

if (qryNo == null){
     request.setAttribute("msg","报表编号不存在");
     rd=request.getRequestDispatcher("/showinfo.jsp");
     rd.forward(request,response);
} 

String nvBar =request.getParameter("nvBar");
String d[][]=null;
String sql ="";

 
Query qry = new Query();
qry.setRequest(request);

if (nvBar ==null){
	nvBar="";
}
if ( nvBar.equals("1")){   //导航数据
 	sql=(String) session.getAttribute("userSql"+ qryNo);
	rtok=qry.getNvData(qryNo,sql);
}else{
  rtok= qry.buildData(qryNo);
}
if (rtok =="ok"){
   d=qry.getData();
}
qry.closeDB();
String errMsg=qry.errMsg;

if (errMsg!=null){
     request.setAttribute("msg",errMsg);
     rd=request.getRequestDispatcher("/showinfo.jsp");
     rd.forward(request,response);
}else{
	if (! nvBar.equals("1")){
		sql=qry.getStrValue("userSql");
	}
	qryNo=qry.getStrValue("qryNo");

	session.setAttribute("userSql"+ qryNo,sql);
}
//JS部分
int rows=qry.rows;
int cols=qry.cols;
String dd="";
for ( int i=0;i<rows;i++){
	String tmp="";
	for(int j=0;j<cols;j++){
		tmp += d[i][j]+",";
	}
	if (tmp.endsWith(",")){
		tmp=tmp.substring(0,tmp.length()-1);
	}
	dd= dd +tmp+"||";
}

%>
<html>
<head>
<title>

</title>
<link rel="stylesheet" type="text/css" href="../setup/web.css">
<SCRIPT src="../setup/meizzDate.js" type=text/javascript></SCRIPT>
</head>

<BODY background=checks_02.jpg>

<form  name='_form'  method='get' action=''>
<input type='hidden' name='dispName' value=''>
<input type='hidden' name='qryNo' value=''>
<input type='hidden' name='listNo' value=''>
<input type='hidden' name='linkKey' value=''>
<input type='hidden' name='linkKeyValue' value=''>
<input type='hidden' name='linkKey1' value=''>
<input type='hidden' name='linkKey1Value' value=''>
<input type='hidden' name='linkKey1Name' value=''>
<input type='hidden' name='linkKey2' value=''>
<input type='hidden' name='linkKey2Value' value=''>
<input type='hidden' name='nvBar' value='0'>
<input type='hidden' name='saveBrh' value=''>
<div id=aaaa align="center">
<TABLE borderColor=#999999 cellSpacing=0 cellPadding=0 align=center border=1 width=100% bgcolor=#f1f1f1>
 <TBODY>
  <TR Align=center>
   <TD align="center">
    <TABLE width=95%>
     <TBODY>
      <TR align="center">
       <TD align="center">
		<table width=100% border=0 align="center" cellPadding=0  cellSpacing=0>
		 <TR>
	 	   <TD height=2>&nbsp;

		   </TD>
	 	 </TR>
	     <TR>
		   <td id=detailTab align="center"></td>
	     </TR>
	     <TR>
           <TD colspan=2 height=2>&nbsp;
           </TD>
	     </TR>
	    </TABLE>
		<!-- JavaScript 显示区Begin======================================================================= -->
		<div id='showTable' align=center width=100%>
		</div>
		<!-- JavaScript 显示区End========================================================================= -->
	   </TD>
	  </TR>
	 </TBODY>
    </TABLE>
    <br>
     </TD>
  </TR>
 </TBODY>
</TABLE>

</div>
</form>
<div id=bbbb>
</div>
<div id=over style="position:absolute; top:0; left:0; z-index:1; display:none;" width=100% height=700>
<table width=100% height=700>
<tr>
<td>
</td>
</tr>
</table>

</div>
<div id=sending style="position:absolute; top:50%; left:37%; z-index:2; display:none;" align=center>
<table width="250" height="80" border="0" cellpadding="0" cellspacing="1">
<tr>
<td bgcolor=#999999 align=center height=20 width=100>&nbsp;
</td>
</tr>
<tr>
  <td bgcolor=eeeeee align=center height=50>正在处理中…… </td>
</tr>
<tr>
  <td bgcolor=#cacaca align=center height=10>&nbsp;</td>
</tr>
</table>
</div>


<script language=javascript>
var tmp="<%=sql%>";
/***********************************************************************************
|*
|*   函数名     |      说明               |   CSS
|*----------------------------------------------------
|*   mClk()     |  表格中鼠标点击         |
|*              |  提交事件函数           |
|*----------------------------------------------------
|*   mOut()     |  表格鼠标特效二         |
|*----------------------------------------------------
|*   mOvr()     |  表格鼠标特效一         |
|*----------------------------------------------------
|* submitgo()   |  导航FORM中             |
|*              |  首页、上一页           |
|*              |  下一页、末页BUTTON     |
|*              |  事件函数               |
|*----------------------------------------------------
|* strToTable() |  字串转换为表格函数     |  head
|*----------------------------------------------------
|* sumData()    |  数据求和               |
|*----------------------------------------------------
|* showTable()  |  预览Table效果          |
|*----------------------------------------------------
|* formatData() |  格式化数据             |
|*              |  1、先判断是否万元显示  |
|*              |  2、其他限制条件        |
|*              |  3、最后判断0显示空     |
|*----------------------------------------------------
|* makeParams() |  转换处理提交区         |  button
|*              |  qryParams的数据        |  input
|*              |  为HTML语句             |  table
|*
|*==================================================================================
|* 公共变量区(替换区)
*/

var rows	= <%=rows%>;//总行数
var cols	= <%=cols%>;//总列数
var dbdata	= "<%=dd%>";//数据

var curBgcolor	= '#E7E7E7';//光标颜色

var dispMode	= '<%=qry.getStrValue("qryIsList")%>';//显示方式
var dispName	= '<%=qry.getStrValue("dispName")%>';//显示名称
var qryNo	= '<%=qry.getStrValue("qryNo")%>';//项目代号
var linkCol	= '<%=qry.getStrValue("linkCol")%>';//参数列
var formatCols	= '<%=qry.getStrValue("formatCols")%>';//数据列
var middleCols	= '<%=qry.getStrValue("middleCols")%>';//居中列
var rightCols	= '<%=qry.getStrValue("rightCols")%>';//右对齐列 RIGHTCOLS
var dataNoWraps	= '<%=qry.getStrValue("dataNoWraps")%>';//不折行列 dataNoWraps
var sumCols	= '<%=qry.getStrValue("sumCols")%>';            //合计列
var sumIsTop	= '<%=qry.getStrValue("sumIsTop")%>';       //合计上显,数据合计完毕后作判断
var dataIsInt	= '<%=qry.getStrValue("dataIsInt")%>';      //数据取整:0,1
var dataIsSplit	= '<%=qry.getStrValue("dataIsSplit")%>';    //取消千分
var dataDispWan	= '<%=qry.getStrValue("dataDispWan") %>';   //万元显示
var zeroToSpace	= '<%=qry.getStrValue("zeroToSpace")%>';    //0显示空
var caption	= '<%=qry.getStrValue("caption") %>';           //标题
var lTitle	= '<%=qry.getStrValue("lTitle") %>';            //标题1:左子标题
var mTitle	= '<%=qry.getStrValue("mTitle") %>';            //标题2:中子标题
var rTitle	= '<%=qry.getStrValue("rTitle") %>';            //标题3:右子标题
var colNames	= '<%=qry.getStrValue("colNames") %>';      //列名称:以逗号分割，显示
var linkKey	= '<%=qry.getStrValue("linkKey") %>';	        //关键字变量名称: -->
var linkKeyValue= '<%=qry.getStrValue("linkKeyValue") %>';	//关键字变量值: -->
var linkKey1	= '<%=qry.getStrValue("linkKey1")%>';       //上次调用时关键字
var linkKey1Name= '<%=qry.getStrValue("linkKey1Name")%>';   //上次调用时关键字
var linkKey1Value= '<%=qry.getStrValue("linkKey1Value")%>'; //上次调用时关键字值
var linkKey2    = '<%=qry.getStrValue("linkKey2")%>';       //特殊处理 使用实网点
var linkKey2Value= '<%=qry.getStrValue("linkKey2Value")%>'; //特殊处理 使用实网点的值

var listNo	= '<%=qry.getStrValue("listNo") %>';	//下级编号: -->
var qryIsList	= '<%=qry.getStrValue("qryIsList") %>';	//程序模式: -->
var qryParams	= '<%=qry.getStrValue("qryParams") %>';	//选择条件: -->
var linkUrl	= '<%=qry.getStrValue("linkUrl") %>';	//链接程序: -->
var linkNewPage	= '<%=qry.getStrValue("linkNewPage") %>';	//新页显示: -->
var pageSize	= '<%=qry.getStrValue("pageSize")%>';//页记录数
var pageNo      = '<%=qry.getStrValue("pageNo")%>';//当前页
var total	= '<%=qry.getStrValue("total")%>';//记录总条数

pageSize = parseInt(pageSize);
pageNo = parseInt(pageNo);
total = parseInt(total);

if(linkCol>0){//根据linkCol判断cols数值
	if (cols>0){
		cols = cols-1;
	}
}

var showArray0	= "";//showArray0:大表头、中表头、小表头
var showArray1	= "";//提示“数据列”行显示数据
var showArray2	= "";//数据部分显示数据
var showArray3	= "";//合计部分显示数据
var showArray4	= "";//导航条显示数据

var ff		= new Array(cols);//数据列
var mm		= new Array(cols);//居中列
var rr		= new Array(cols);//右对齐列 RIGHTCOLS
var dd		= new Array(cols);//不折行列 dataNoWraps
var ss		= new Array(cols);//合计列

var test='1010';//屏蔽多次点击变量


/**
* 处理隐藏数据
*/
document._form.qryNo.value=qryNo;  //项目编号
document._form.listNo.value=listNo; //项目清单编号
document._form.dispName.value=dispName; //显示名称 (上级网点名称)
document._form.linkKey.value=linkKey;     //关键字名称
document._form.linkKeyValue.value=linkKeyValue; //关键字值
document._form.linkKey1.value=linkKey1; //其他关键字
document._form.linkKey1Value.value=linkKey1Value; //其他关键字值
document._form.linkKey1Name.value=linkKey1Name; //其他关键字名称

document._form.nvBar.value=0; // 是否为导航，0-不导航 1-导航
/*
* 数据处理主函数
*
* function makeArray(){
*/


//导航条设置


if (dispMode == '1' && parseInt(pageSize) <parseInt(total) ){

tota1=parseInt(total)/parseInt(pageSize);
tota2=parseInt(parseInt(total)/parseInt(pageSize));
if (tota1==tota2){
tolPag=tota1;
}else{
tolPag=tota2+1;
}
//tolPag = parseInt(parseInt(total)/parseInt(pageSize)+1);

Nbar	= "每页<input type='text' name='pageSize' size='3' value='"+pageSize+"'  class='input' value="+pageSize+">条 | 共"+tolPag+"页/"+total+"条记录";
Nbar	+="&nbsp;<input type='button' value='首 页' onClick='submitgo(1);' class='button'>";
Nbar	+="&nbsp;<input type='button' value='上 页' onClick='submitgo("+parseInt(parseInt(pageNo)-1)+")' class='button'>";
Nbar	+="&nbsp;<input type='button' value='下 页' onClick='submitgo("+parseInt(parseInt(pageNo)+1) + ")' class='button'>";
Nbar	+="&nbsp;<input type='button' value='末 页' onClick='submitgo("+tolPag+ ")' class='button'>";
Nbar	+="&nbsp; 第<input type='text' size='3' name='pageNo' value='"+pageNo+"' class='input'>";
Nbar	+="页 <input type='button' value='确定' onclick='submitgo(-100)' class='button'>";
Nbar	+="<input type='hidden' name='total' value='"+total+"'>";
}else{
	Nbar ="&nbsp;";
}
//根据linkCol处理data数据，并生成dataTem临时数据数组
var dataTem		= new Array();//临时数据数组
var data		= new Array();//数据数组
var data1 = dbdata.split("||");



for ( i=0;i<rows;i++){
	dataTem[i] = new Array();
	data[i] = new Array();
	data2 = data1[i].split(",");

	if (linkCol > 0){//参数列: -->
		for(var j=1;j<=cols;j++){
		  dataTem[i][j-1] = data2[j-1];
		  data[i][j-1] = data2[j];
		}
	}else{
		for(var j=0;j<cols;j++){
		  dataTem[i][j] = data2[j];
		  data[i][j] = data2[j];
		}
	 }

}



//相对于数据数组判断何列数据为数据列or居中列or右对齐列or不折行列or合计列
forCols	= ","+formatCols+",";	//数据列
midCols	= ","+middleCols+",";	//居中列
rigCols	= ","+rightCols+",";	//右对齐列
dataArps= ","+dataNoWraps+",";	//不折行列
sumsCols= ","+sumCols+",";		//合计列
for (var colsI=1;colsI<=cols;colsI++){
	if (forCols.indexOf(","+colsI+",")>=0){//数据列2,4
		ff[colsI] = 1;
	}
	if (midCols.indexOf(","+colsI+",")>=0){//居中列4
		mm[colsI] = 1;
	}
	if (rigCols.indexOf(","+colsI+",")>=0){//右对齐列2,3,4
		rr[colsI] = 1;
	}
	if (dataArps.indexOf(","+colsI+",")>=0){//不折行列2,3,4
		dd[colsI] = 1;
		rr[colsI] = 1;
	}
	if (sumsCols.indexOf(","+colsI+",")>=0){//合计列4
		ss[colsI] = 1;
	}
}

//提交区显示
document.all.detailTab.innerHTML = makeParams(qryParams);

/*
* 输出显示部分======================================================================
*
*   变量名  |  饱含部分  |  CSS
*-----------------------------------
* showArray0|   大表头   | caption
*			|   中表头   | title
*			|   列名称   | head
*/

showArray0 = "<div class=caption align=center>"+caption+"</div>";
//中表头:单位名称:AAAAAA+日期+单位万元
showArray0 += "<div align=center>";
showArray0 += "<table cellSpacing=0 cellPadding=0 width=100% border=0><tr class=title>";
showArray0 += "<td width=33% class=title>"+lTitle+"</td>";
showArray0 += "<td align=center class=title>"+mTitle+"</td>";
showArray0 += "<td align=right width=33% class=title>"+rTitle+"</td>";
showArray0 += "</tr></table></div>";
showArray0 += '<TABLE class=table cellSpacing=1 cellPadding=1 width=100% border=0  align=center><TBODY>';
//列名称:strToTable();
showArray0 += strToTable(colNames);


/*
* showArray2:数据部分---------------------------------------------------------------
*
*   变量名  |  饱含部分  |  CSS
*-----------------------------------
* showArray2|  数据部分  |  data
*
*/
if(cols>0){
for (var i=0; i<rows; i++){//行循环开始
	if (linkCol>0){
		showArray2 += "<tr bgcolor=#FFFFFF onclick=\"mClk('"+ i +"');\" onmouseout=\"mOut(this);\" onmouseover=\"mOvr(this)\"  class=data>";
	}else{
		showArray2 += "<tr bgcolor=#FFFFFF class=data>";
	}
	for (var j=0; j<cols; j++){//列循环开始
		var datas = "";	//单元格显示数据
		var align = "";	//对齐方式
		var nowrap = "";//不折行列
		datas = data[i][j];
		if (ff[j+1] == 1){//数据列,系统默认按照 #,##0.00格式输出，自动右对齐
			align = "right";
			datas = formatData(parseFloat(data[i][j]),'yes');
		}
		if (rr[j+1] == 1){//右对齐列
			align = "right";
		}
		if (mm[j+1] == 1){//居中列,当此列为数据列，同时又为居中时，选居中。
			align = "center";
		}
		if (dd[j+1] == 1){//不折行列
			nowrap = "nowrap";
		}
		showArray2 += "<td class=data align="+align+" "+nowrap+">"+datas+"</td>";
	}//列循环结束
	showArray2 += "</tr>";
}//行循环结束

if(rows<pageSize){

	for (var i=rows;i<pageSize;i++){
	showArray2 += "<tr bgcolor=#FFFFFF class=data>";
	for (var j=0; j<cols; j++){//列循环开始
		showArray2 += "<td>&nbsp;</td>";
	}
	showArray2 += "</tr>";
	}

}

/*
* 数据求和显示:showArray3
*
*   变量名  |  饱含部分  |  CSS
*-----------------------------------
* showArray3|  求和部分  |  sumdata
*
*/
if (sumCols !=""){
showArray3 += "<tr class=sumdata>";
for (var coI=0;coI<cols;coI++){
	if (coI == 0){
		showArray3 += "<td align=center >合计</td>";
	}else if (ss[coI+1] == 1){//合计列

		if (ff[coI+1] == 1){//数据列,系统默认按照 #,##0.00格式输出，自动右对齐
			showArray3 += "<td align=right  >"+formatData(sumData(coI),'yes')+"</td>";
		}else{
			showArray3 += "<td align=right  >"+formatData(sumData(coI),'no')+"</td>";
		}

	}else{
		showArray3 += "<td >&nbsp;</td>";
	}
}
showArray3 += "</tr>";
}else{
	showArray3="";
}



}//判断cols是否为0

/*
* showArray4:导航条
*
*   变量名  |  饱含部分  |  CSS
*-----------------------------------
* showArray4|   导航条   |  sumdata
*
*/
showArray4 += "</TBODY></table>";
//showArray4 += "<tr bgcolor=#ffffff>";
//showArray4 += "<tfoot colspan="+cols+" align=right>";
showArray4 += "<table cellSpacing=0 cellPadding=0 width=100% border=0 class=head>";
showArray4 += "<tr><td>"+Nbar+"</td><td align=right><input type='button' value='后退' onclick='history.back();' class='button'>&nbsp;</tfoot>";
showArray4 += "</tr></table>";
//showArray4 += "</td></tr></TBODY></table>";

/*
* 所有HTML数据innerHTNL到DIV处
*
*   函数名    |  饱含部分  |  CSS
*-----------------------------------
* showTable() |  所有HTML  |
*             |    数据    |
*
*/

showTable();
//}function makeArray()函数结束

/*==================================================================================
* ||||||||||||||||||||||||||||||||||||||||||
* |||||||所||需||子||函||数|||||||||||||||||
* ||||||||||||||||||||||||||||||||||||||||||
**==================================================================================
*
*   函数名    |    说明          |  CSS
*-------------------------------------------
* makeParams()|  转换处理提交区  |  button
*             |  qryParams的数据 |  input
*             |  为HTML语句      |  table
*/
function makeParams(str){

var resultStr = '<table class="table"  cellSpacing=1 cellPadding=1 width=100% border=0 >';

	resultStr += "<tr class=head>";

	if(str != ''){
 		
		var tmp="";			
		step1 = str.split(",");
		for (var i=0;i<step1.length;i++){
			if(i==0 || i==4)
				tmp+="<td >";
			if (step1[i].indexOf("=")>0){//非缺省状态
				step2 = step1[i].split("=");
				step3 = step2[1].split("|");
				if (step3[1] == 'text'){// begindate=开始日期|text|:beginDate
					tmp += " "+step3[0]+":<input class='input' type=text name="+step2[0]+" size=10 value="+step3[2]+">";
					if (step2[0] == 'begindate' ||step2[0] == 'enddate' ){
					tmp +="<input type='button' value='…' class='button' onclick='setday(this,document.getElementById(\""+step2[0]+"\"))'> ";
					}
				}
				if (step3[1] == 'radio'){// dispdode=统计方式|radio|0 checked+统计;1+清单
					step4 = step3[2].split(";");// 0+统计;1+清单
					tmp += " "+step3[0];
					for (var j=0;j<step4.length;j++){
						tmp += " <input type=radio name="+step2[0];
						step5 = step4[j].split("+");
						tmp += " value="+step5[0]+">"+step5[1];
					}
					//tmp+="&nbsp;&nbsp;&nbsp;";
				}
				if (step3[1] == 'select'){// status=汇票状态|select|1+正常;3 selected+垫款;6+收回
					tmp += " "+step3[0]+"<select name="+step2[0]+" class=input>";
					step4 = step3[2].split(";");
					for (var j=0;j<step4.length;j++){
						step5 = step4[j].split("+");
						tmp += "<option value="+step5[0]+">"+step5[1]+"</option>";
					}
					tmp += "</select>";
				}

			}
			if(i==3 && i<step1.length-1)
				tmp+="</td><td>&nbsp;</td></tr> <tr class=head>";
			if(i==step1.length-1)
				tmp+="</td>";
		}//for
		
		//alert(tmp);
		if (tmp.length>0) {
			//resultStr +="<td nowrap>";
			//resultStr +=tmp;
			//resultStr += "</td>";
			resultStr+=tmp;
			resultStr += '<td align=right width=2%><input type="button" value=" 检 索 "   class="button" onclick="submitgo(-1)">&nbsp;&nbsp;';
		}else {
			resultStr +="<td align=right>";
		}

	}else {
		resultStr +="<td align=right>";
	}

	resultStr +='<input type="button" class="button"  value=" 打 印 "  onclick="printTable()">&nbsp;&nbsp;';
	resultStr +='<input type="button" class="button"  value=" 关 闭 "  onclick="self.close()">&nbsp;';

	resultStr +="</td></tr></table>";

	//alert(resultStr);
	return resultStr;
}

/*
*   函数名    |    说明
*----------------------------------------
* formatData()|  格式化数据
*             |  1、先判断是否万元显示
*             |  2、其他限制条件
*             |  3、最后判断0显示空
*/

function formatData(src,ifdata){
var fData = src;
var point; 


	if (dataDispWan == "1" && ifdata == 'yes'){//万元显示:0,1
		//fData = parseFloat(fData/10000);
		fData = ""+Math.round(parseFloat(fData)/100)/100;

		if(fData.indexOf(".")>0){
			var tmp=fData.split(".");
			fData2=tmp[0];
			fData1=tmp[1];
			if (fData1.length ==1 ) {
				fData1 += "0";
			}
			//fData1=fData.substr(fData.indexOf(".")+1,2);//取出小数点后的部分
			//fData2=fData.split(".")[0];//取出小数点前的部分
			fData = fData2+"."+fData1;
		}else{
			
			fData=fData+".00";//取出小数点前的部分
			
		}
		//fData=parseFloat(fData).toFixed(2);//格式为XXXXXXXX.XX
	}
	if (dataIsSplit == "0"  && ifdata == 'yes'){//取消千分:0,1
		//fData=parseFloat(fData).toFixed(2);//格式为XXXXXXXX.XX

		//fData=parseFloat(fData);
		fData=""+fData;
		if(fData.indexOf(".")>0){
		point=fData.substr(fData.indexOf(".")+1,2);//取出小数点后的部分
		fData=fData.split(".")[0];//取出小数点前的部分
		}else{
			fData=fData+".00";
			point = 'true';
		}



		var re=/(\d+)(\d{3})/,fData=fData.toString();//插入千分符

		while(re.test(fData))fData=fData.replace(re,"$1,$2");//插入千分符
		if(point=='true'){
			fData=fData;
		}else{
			fData=fData+"."+point;
		}

		}
	if (dataIsInt == "1" && ifdata == 'yes'){//数据取整:0,1

		if(fData.indexOf(".")>0){
			fData1=fData.substr(fData.indexOf(".")+1,2);//取出小数点后的部分
			fData2=fData.split(".")[0];//取出小数点前的部分
			fData = fData2+"."+fData1;
		}else{
			fData=fData+".00";//取出小数点前的部分
		}

		//fData=parseFloat(fData).toFixed(2);//格式为XXXXXXXX.XX
		point=fData.substr(fData.indexOf(".")+1,2);//取出小数点后的部分
		point = Math.round("0."+point);//小数点部分四舍五入
		fData = fData.split(".")[0];//格式为XX,XXX,XXX
		var d = fData.substr(fData.length-1,1);
		d = parseInt(d)+point;//数据最后一位与小数点四舍五入后的数相加
		fData = fData.substr(0,fData.length-1)+d;//生成新数据

		//var re=/(\d+)(\d{3})/,fData=fData.toString();//插入千分符

		//while(re.test(fData))fData=fData.replace(re,"$1,$2");//插入千分符

	}
	if (zeroToSpace == "1"){//0显示空:0,1
		if (fData == "0" || fData == "0.00") fData = "&nbsp;";
	}
	return fData;
}

/*
*   函数名    |      说明
*------------------------------
* showTable() |  预览Table效果
*
*/

function showTable() {
/*
var showArray0	= "";//showArray0:大表头、中表头、小表头
var showArray2	= "";//数据部分显示数据
var showArray3	= "";//合计部分显示数据
var showArray4	= "";//导航条显示数据
*/
	var s = "";
	if (sumIsTop == "1"){//合计上显
		s = showArray0+showArray3+showArray2+showArray4;
	}else{
		s = showArray0+showArray2+showArray3+showArray4;
	}
	document.all.showTable.innerHTML=s;

}
/*
*   函数名    |      说明
*------------------------------
* printTable() |  打印预览Table效果
*
*/

function printTable(){
	var s = "";
	if (sumIsTop == "1"){//合计上显
		s = showArray0+showArray3+showArray2;
	}else{
		s = showArray0+showArray2+showArray3;
	}
   document.all.aaaa.style.display='none';
	document.all.bbbb.innerHTML=s;
   window.print();

   document.all.bbbb.style.display='none';
   document.all.aaaa.style.display='';

}
function printTable1() {
	var s = "";
	if (sumIsTop == "1"){//合计上显
		s = showArray0+showArray3+showArray2;
	}else{
		s = showArray0+showArray2+showArray3;
	}



var newwindow=window.open('print.htm','fff','fullscreen=yes,resizable=yes');
newwindow.focus();


//window.open("print.htm?info="+s,"print","toolbar=no,resizable=yes,fullscreen=yes");

}
/*
*   函数名  |    说明
*------------------------
* sumData() |  数据求和
*
*/

function sumData(src){
var dataSum = 0;
	for (i=0;i<rows;i++){
		dataSum = parseFloat(dataSum) + parseFloat(data[i][src]);
	}

	return dataSum;

}

/*
*   函数名     |         说明         |  CSS
*----------------------------------------------
* strToTable() |  字串转换为表格函数  |  head
*
*/

function strToTable(str){


		htmlTable = "";	//
		getString = str;
		
		//getString=1|1,rowspan=1|1,rowspan=1|1,||
		
		if (getString.indexOf(",||") > 0){//复杂表rowspan=1|2.1,colspan=3|2.2,||
			step1 = getString.split("||")

			for (i=0;i<step1.length-1;i++){	//判断有几行，得到每一行的信息
				step2 = step1[i].split(",");
				htmlTable +="<tr class=head>";

				for(j=0;j<step2.length-1;j++){
					step3 = step2[j].split("|");
					htmlTable +='<td '+step3[0]+' align=center>'+step3[1]+'</td>';
				}

			htmlTable +="</tr>";
			}


		}else{//普通表

			step2 = getString.split(",");
			htmlTable +='<tr  class=head>';
			for(i=0;i<step2.length ;i++){
			
				if (i<step2.length){
	
					if(step2[i].indexOf("|") > 0){
						step3 = step2[i].split("|");
						htmlTable +='<td align=center>'+step3[1]+'</td>';
					}else{
						htmlTable +="<td align=center>"+step2[i]+"</td>";
					}
					
				}else{
					htmlTable += "<td align=center>错误</td>";
				}
			}
			htmlTable +='</tr>';
		}

	return htmlTable;

}

/*
* ===================================王忠===========================================
*
*   函数名   |      说明
*-----------------------------------
* submitgo() |  导航FORM中
*            |  首页、上一页
*            |  下一页、末页BUTTON
*            |  事件函数
*
*/

function submitgo(val){
if (val == -1){ //在提交区直接提交
    document._form.nvBar.value= -1;
}else{
    //导航条
    document._form.nvBar.value=1;
    if (val != -100){    //不是直接按确定
    	document._form.pageNo.value=val;
    }
}

	document._form.submit();
	divToDo();

 }

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

function mOut(src){
	if (!src.contains(event.toElement)) {
	src.style.cursor = 'default';
	src.bgColor = '#FFFFFF';
	}
}

/*
*   函数名   |      说明
*-----------------------------------
*   divToDo()   |  表格中鼠标点击
*            |  提交事件函数
*
*/
function divToDo(){
document.all.sending.style.display='';
document.all.over.style.display='';

}


/*
*   函数名   |      说明
*-----------------------------------
*   mClk()   |  表格中鼠标点击
*            |  提交事件函数
*
*/
function mClk(val){

if (qryIsList =="0" ){
	document._form.saveBrh.value=document._form.linkKeyValue.value;
	document._form.linkKeyValue.value=dataTem[val][0];
	document._form.dispName.value=dataTem[val][1];
	document._form.submit();
	divToDo();
}else{
      //修改链接参数名
	var hrf=linkUrl;
 	if (linkUrl.indexOf("?") <0) { //没跟变量
		hrf = hrf + "?" + linkKey + "=";
      	}

	var tmp=hrf.substr(hrf.length-1,1);
	if (tmp != "="){
		hrf += "=";
	}

	hrf +=dataTem[val][0];
	if(linkKey2=="brhid")
	{
		hrf=hrf+"&"+"linkKey2Value="+linkKey2Value;
	}

	if (linkNewPage == 0) {
		
			document.location.href=hrf;

	}else {
 		var st="status=yes,toolbar=yes,menubar=no,location=no,scrollbars=yes,resizable=yes";
 		
		top.window.open(hrf,"123",st);

    }

}


}

//结束!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==================================================================================
</script>


</body>
</html>


<% 
MyDB.getInstance().removeCurrentThreadConn("query.jsp"); //added by JGO on 2004-07-17
%>