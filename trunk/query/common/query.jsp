<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.report.Query" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>

<%
RequestDispatcher rd=null;
String rtok="";

String qryNo=null;
qryNo=request.getParameter("qryNo");

if (qryNo == null){
     request.setAttribute("msg","�����Ų�����");
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
if ( nvBar.equals("1")){   //��������
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
//JS����
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
		<!-- JavaScript ��ʾ��Begin======================================================================= -->
		<div id='showTable' align=center width=100%>
		</div>
		<!-- JavaScript ��ʾ��End========================================================================= -->
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
  <td bgcolor=eeeeee align=center height=50>���ڴ����С��� </td>
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
|*   ������     |      ˵��               |   CSS
|*----------------------------------------------------
|*   mClk()     |  ����������         |
|*              |  �ύ�¼�����           |
|*----------------------------------------------------
|*   mOut()     |  ��������Ч��         |
|*----------------------------------------------------
|*   mOvr()     |  ��������Чһ         |
|*----------------------------------------------------
|* submitgo()   |  ����FORM��             |
|*              |  ��ҳ����һҳ           |
|*              |  ��һҳ��ĩҳBUTTON     |
|*              |  �¼�����               |
|*----------------------------------------------------
|* strToTable() |  �ִ�ת��Ϊ�����     |  head
|*----------------------------------------------------
|* sumData()    |  �������               |
|*----------------------------------------------------
|* showTable()  |  Ԥ��TableЧ��          |
|*----------------------------------------------------
|* formatData() |  ��ʽ������             |
|*              |  1�����ж��Ƿ���Ԫ��ʾ  |
|*              |  2��������������        |
|*              |  3������ж�0��ʾ��     |
|*----------------------------------------------------
|* makeParams() |  ת�������ύ��         |  button
|*              |  qryParams������        |  input
|*              |  ΪHTML���             |  table
|*
|*==================================================================================
|* ����������(�滻��)
*/

var rows	= <%=rows%>;//������
var cols	= <%=cols%>;//������
var dbdata	= "<%=dd%>";//����

var curBgcolor	= '#E7E7E7';//�����ɫ

var dispMode	= '<%=qry.getStrValue("qryIsList")%>';//��ʾ��ʽ
var dispName	= '<%=qry.getStrValue("dispName")%>';//��ʾ����
var qryNo	= '<%=qry.getStrValue("qryNo")%>';//��Ŀ����
var linkCol	= '<%=qry.getStrValue("linkCol")%>';//������
var formatCols	= '<%=qry.getStrValue("formatCols")%>';//������
var middleCols	= '<%=qry.getStrValue("middleCols")%>';//������
var rightCols	= '<%=qry.getStrValue("rightCols")%>';//�Ҷ����� RIGHTCOLS
var dataNoWraps	= '<%=qry.getStrValue("dataNoWraps")%>';//�������� dataNoWraps
var sumCols	= '<%=qry.getStrValue("sumCols")%>';            //�ϼ���
var sumIsTop	= '<%=qry.getStrValue("sumIsTop")%>';       //�ϼ�����,���ݺϼ���Ϻ����ж�
var dataIsInt	= '<%=qry.getStrValue("dataIsInt")%>';      //����ȡ��:0,1
var dataIsSplit	= '<%=qry.getStrValue("dataIsSplit")%>';    //ȡ��ǧ��
var dataDispWan	= '<%=qry.getStrValue("dataDispWan") %>';   //��Ԫ��ʾ
var zeroToSpace	= '<%=qry.getStrValue("zeroToSpace")%>';    //0��ʾ��
var caption	= '<%=qry.getStrValue("caption") %>';           //����
var lTitle	= '<%=qry.getStrValue("lTitle") %>';            //����1:���ӱ���
var mTitle	= '<%=qry.getStrValue("mTitle") %>';            //����2:���ӱ���
var rTitle	= '<%=qry.getStrValue("rTitle") %>';            //����3:���ӱ���
var colNames	= '<%=qry.getStrValue("colNames") %>';      //������:�Զ��ŷָ��ʾ
var linkKey	= '<%=qry.getStrValue("linkKey") %>';	        //�ؼ��ֱ�������: -->
var linkKeyValue= '<%=qry.getStrValue("linkKeyValue") %>';	//�ؼ��ֱ���ֵ: -->
var linkKey1	= '<%=qry.getStrValue("linkKey1")%>';       //�ϴε���ʱ�ؼ���
var linkKey1Name= '<%=qry.getStrValue("linkKey1Name")%>';   //�ϴε���ʱ�ؼ���
var linkKey1Value= '<%=qry.getStrValue("linkKey1Value")%>'; //�ϴε���ʱ�ؼ���ֵ
var linkKey2    = '<%=qry.getStrValue("linkKey2")%>';       //���⴦�� ʹ��ʵ����
var linkKey2Value= '<%=qry.getStrValue("linkKey2Value")%>'; //���⴦�� ʹ��ʵ�����ֵ

var listNo	= '<%=qry.getStrValue("listNo") %>';	//�¼����: -->
var qryIsList	= '<%=qry.getStrValue("qryIsList") %>';	//����ģʽ: -->
var qryParams	= '<%=qry.getStrValue("qryParams") %>';	//ѡ������: -->
var linkUrl	= '<%=qry.getStrValue("linkUrl") %>';	//���ӳ���: -->
var linkNewPage	= '<%=qry.getStrValue("linkNewPage") %>';	//��ҳ��ʾ: -->
var pageSize	= '<%=qry.getStrValue("pageSize")%>';//ҳ��¼��
var pageNo      = '<%=qry.getStrValue("pageNo")%>';//��ǰҳ
var total	= '<%=qry.getStrValue("total")%>';//��¼������

pageSize = parseInt(pageSize);
pageNo = parseInt(pageNo);
total = parseInt(total);

if(linkCol>0){//����linkCol�ж�cols��ֵ
	if (cols>0){
		cols = cols-1;
	}
}

var showArray0	= "";//showArray0:���ͷ���б�ͷ��С��ͷ
var showArray1	= "";//��ʾ�������С�����ʾ����
var showArray2	= "";//���ݲ�����ʾ����
var showArray3	= "";//�ϼƲ�����ʾ����
var showArray4	= "";//��������ʾ����

var ff		= new Array(cols);//������
var mm		= new Array(cols);//������
var rr		= new Array(cols);//�Ҷ����� RIGHTCOLS
var dd		= new Array(cols);//�������� dataNoWraps
var ss		= new Array(cols);//�ϼ���

var test='1010';//���ζ�ε������


/**
* ������������
*/
document._form.qryNo.value=qryNo;  //��Ŀ���
document._form.listNo.value=listNo; //��Ŀ�嵥���
document._form.dispName.value=dispName; //��ʾ���� (�ϼ���������)
document._form.linkKey.value=linkKey;     //�ؼ�������
document._form.linkKeyValue.value=linkKeyValue; //�ؼ���ֵ
document._form.linkKey1.value=linkKey1; //�����ؼ���
document._form.linkKey1Value.value=linkKey1Value; //�����ؼ���ֵ
document._form.linkKey1Name.value=linkKey1Name; //�����ؼ�������

document._form.nvBar.value=0; // �Ƿ�Ϊ������0-������ 1-����
/*
* ���ݴ���������
*
* function makeArray(){
*/


//����������


if (dispMode == '1' && parseInt(pageSize) <parseInt(total) ){

tota1=parseInt(total)/parseInt(pageSize);
tota2=parseInt(parseInt(total)/parseInt(pageSize));
if (tota1==tota2){
tolPag=tota1;
}else{
tolPag=tota2+1;
}
//tolPag = parseInt(parseInt(total)/parseInt(pageSize)+1);

Nbar	= "ÿҳ<input type='text' name='pageSize' size='3' value='"+pageSize+"'  class='input' value="+pageSize+">�� | ��"+tolPag+"ҳ/"+total+"����¼";
Nbar	+="&nbsp;<input type='button' value='�� ҳ' onClick='submitgo(1);' class='button'>";
Nbar	+="&nbsp;<input type='button' value='�� ҳ' onClick='submitgo("+parseInt(parseInt(pageNo)-1)+")' class='button'>";
Nbar	+="&nbsp;<input type='button' value='�� ҳ' onClick='submitgo("+parseInt(parseInt(pageNo)+1) + ")' class='button'>";
Nbar	+="&nbsp;<input type='button' value='ĩ ҳ' onClick='submitgo("+tolPag+ ")' class='button'>";
Nbar	+="&nbsp; ��<input type='text' size='3' name='pageNo' value='"+pageNo+"' class='input'>";
Nbar	+="ҳ <input type='button' value='ȷ��' onclick='submitgo(-100)' class='button'>";
Nbar	+="<input type='hidden' name='total' value='"+total+"'>";
}else{
	Nbar ="&nbsp;";
}
//����linkCol����data���ݣ�������dataTem��ʱ��������
var dataTem		= new Array();//��ʱ��������
var data		= new Array();//��������
var data1 = dbdata.split("||");



for ( i=0;i<rows;i++){
	dataTem[i] = new Array();
	data[i] = new Array();
	data2 = data1[i].split(",");

	if (linkCol > 0){//������: -->
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



//��������������жϺ�������Ϊ������or������or�Ҷ�����or��������or�ϼ���
forCols	= ","+formatCols+",";	//������
midCols	= ","+middleCols+",";	//������
rigCols	= ","+rightCols+",";	//�Ҷ�����
dataArps= ","+dataNoWraps+",";	//��������
sumsCols= ","+sumCols+",";		//�ϼ���
for (var colsI=1;colsI<=cols;colsI++){
	if (forCols.indexOf(","+colsI+",")>=0){//������2,4
		ff[colsI] = 1;
	}
	if (midCols.indexOf(","+colsI+",")>=0){//������4
		mm[colsI] = 1;
	}
	if (rigCols.indexOf(","+colsI+",")>=0){//�Ҷ�����2,3,4
		rr[colsI] = 1;
	}
	if (dataArps.indexOf(","+colsI+",")>=0){//��������2,3,4
		dd[colsI] = 1;
		rr[colsI] = 1;
	}
	if (sumsCols.indexOf(","+colsI+",")>=0){//�ϼ���4
		ss[colsI] = 1;
	}
}

//�ύ����ʾ
document.all.detailTab.innerHTML = makeParams(qryParams);

/*
* �����ʾ����======================================================================
*
*   ������  |  ��������  |  CSS
*-----------------------------------
* showArray0|   ���ͷ   | caption
*			|   �б�ͷ   | title
*			|   ������   | head
*/

showArray0 = "<div class=caption align=center>"+caption+"</div>";
//�б�ͷ:��λ����:AAAAAA+����+��λ��Ԫ
showArray0 += "<div align=center>";
showArray0 += "<table cellSpacing=0 cellPadding=0 width=100% border=0><tr class=title>";
showArray0 += "<td width=33% class=title>"+lTitle+"</td>";
showArray0 += "<td align=center class=title>"+mTitle+"</td>";
showArray0 += "<td align=right width=33% class=title>"+rTitle+"</td>";
showArray0 += "</tr></table></div>";
showArray0 += '<TABLE class=table cellSpacing=1 cellPadding=1 width=100% border=0  align=center><TBODY>';
//������:strToTable();
showArray0 += strToTable(colNames);


/*
* showArray2:���ݲ���---------------------------------------------------------------
*
*   ������  |  ��������  |  CSS
*-----------------------------------
* showArray2|  ���ݲ���  |  data
*
*/
if(cols>0){
for (var i=0; i<rows; i++){//��ѭ����ʼ
	if (linkCol>0){
		showArray2 += "<tr bgcolor=#FFFFFF onclick=\"mClk('"+ i +"');\" onmouseout=\"mOut(this);\" onmouseover=\"mOvr(this)\"  class=data>";
	}else{
		showArray2 += "<tr bgcolor=#FFFFFF class=data>";
	}
	for (var j=0; j<cols; j++){//��ѭ����ʼ
		var datas = "";	//��Ԫ����ʾ����
		var align = "";	//���뷽ʽ
		var nowrap = "";//��������
		datas = data[i][j];
		if (ff[j+1] == 1){//������,ϵͳĬ�ϰ��� #,##0.00��ʽ������Զ��Ҷ���
			align = "right";
			datas = formatData(parseFloat(data[i][j]),'yes');
		}
		if (rr[j+1] == 1){//�Ҷ�����
			align = "right";
		}
		if (mm[j+1] == 1){//������,������Ϊ�����У�ͬʱ��Ϊ����ʱ��ѡ���С�
			align = "center";
		}
		if (dd[j+1] == 1){//��������
			nowrap = "nowrap";
		}
		showArray2 += "<td class=data align="+align+" "+nowrap+">"+datas+"</td>";
	}//��ѭ������
	showArray2 += "</tr>";
}//��ѭ������

if(rows<pageSize){

	for (var i=rows;i<pageSize;i++){
	showArray2 += "<tr bgcolor=#FFFFFF class=data>";
	for (var j=0; j<cols; j++){//��ѭ����ʼ
		showArray2 += "<td>&nbsp;</td>";
	}
	showArray2 += "</tr>";
	}

}

/*
* ���������ʾ:showArray3
*
*   ������  |  ��������  |  CSS
*-----------------------------------
* showArray3|  ��Ͳ���  |  sumdata
*
*/
if (sumCols !=""){
showArray3 += "<tr class=sumdata>";
for (var coI=0;coI<cols;coI++){
	if (coI == 0){
		showArray3 += "<td align=center >�ϼ�</td>";
	}else if (ss[coI+1] == 1){//�ϼ���

		if (ff[coI+1] == 1){//������,ϵͳĬ�ϰ��� #,##0.00��ʽ������Զ��Ҷ���
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



}//�ж�cols�Ƿ�Ϊ0

/*
* showArray4:������
*
*   ������  |  ��������  |  CSS
*-----------------------------------
* showArray4|   ������   |  sumdata
*
*/
showArray4 += "</TBODY></table>";
//showArray4 += "<tr bgcolor=#ffffff>";
//showArray4 += "<tfoot colspan="+cols+" align=right>";
showArray4 += "<table cellSpacing=0 cellPadding=0 width=100% border=0 class=head>";
showArray4 += "<tr><td>"+Nbar+"</td><td align=right><input type='button' value='����' onclick='history.back();' class='button'>&nbsp;</tfoot>";
showArray4 += "</tr></table>";
//showArray4 += "</td></tr></TBODY></table>";

/*
* ����HTML����innerHTNL��DIV��
*
*   ������    |  ��������  |  CSS
*-----------------------------------
* showTable() |  ����HTML  |
*             |    ����    |
*
*/

showTable();
//}function makeArray()��������

/*==================================================================================
* ||||||||||||||||||||||||||||||||||||||||||
* |||||||��||��||��||��||��|||||||||||||||||
* ||||||||||||||||||||||||||||||||||||||||||
**==================================================================================
*
*   ������    |    ˵��          |  CSS
*-------------------------------------------
* makeParams()|  ת�������ύ��  |  button
*             |  qryParams������ |  input
*             |  ΪHTML���      |  table
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
			if (step1[i].indexOf("=")>0){//��ȱʡ״̬
				step2 = step1[i].split("=");
				step3 = step2[1].split("|");
				if (step3[1] == 'text'){// begindate=��ʼ����|text|:beginDate
					tmp += " "+step3[0]+":<input class='input' type=text name="+step2[0]+" size=10 value="+step3[2]+">";
					if (step2[0] == 'begindate' ||step2[0] == 'enddate' ){
					tmp +="<input type='button' value='��' class='button' onclick='setday(this,document.getElementById(\""+step2[0]+"\"))'> ";
					}
				}
				if (step3[1] == 'radio'){// dispdode=ͳ�Ʒ�ʽ|radio|0 checked+ͳ��;1+�嵥
					step4 = step3[2].split(";");// 0+ͳ��;1+�嵥
					tmp += " "+step3[0];
					for (var j=0;j<step4.length;j++){
						tmp += " <input type=radio name="+step2[0];
						step5 = step4[j].split("+");
						tmp += " value="+step5[0]+">"+step5[1];
					}
					//tmp+="&nbsp;&nbsp;&nbsp;";
				}
				if (step3[1] == 'select'){// status=��Ʊ״̬|select|1+����;3 selected+���;6+�ջ�
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
			resultStr += '<td align=right width=2%><input type="button" value=" �� �� "   class="button" onclick="submitgo(-1)">&nbsp;&nbsp;';
		}else {
			resultStr +="<td align=right>";
		}

	}else {
		resultStr +="<td align=right>";
	}

	resultStr +='<input type="button" class="button"  value=" �� ӡ "  onclick="printTable()">&nbsp;&nbsp;';
	resultStr +='<input type="button" class="button"  value=" �� �� "  onclick="self.close()">&nbsp;';

	resultStr +="</td></tr></table>";

	//alert(resultStr);
	return resultStr;
}

/*
*   ������    |    ˵��
*----------------------------------------
* formatData()|  ��ʽ������
*             |  1�����ж��Ƿ���Ԫ��ʾ
*             |  2��������������
*             |  3������ж�0��ʾ��
*/

function formatData(src,ifdata){
var fData = src;
var point; 


	if (dataDispWan == "1" && ifdata == 'yes'){//��Ԫ��ʾ:0,1
		//fData = parseFloat(fData/10000);
		fData = ""+Math.round(parseFloat(fData)/100)/100;

		if(fData.indexOf(".")>0){
			var tmp=fData.split(".");
			fData2=tmp[0];
			fData1=tmp[1];
			if (fData1.length ==1 ) {
				fData1 += "0";
			}
			//fData1=fData.substr(fData.indexOf(".")+1,2);//ȡ��С�����Ĳ���
			//fData2=fData.split(".")[0];//ȡ��С����ǰ�Ĳ���
			fData = fData2+"."+fData1;
		}else{
			
			fData=fData+".00";//ȡ��С����ǰ�Ĳ���
			
		}
		//fData=parseFloat(fData).toFixed(2);//��ʽΪXXXXXXXX.XX
	}
	if (dataIsSplit == "0"  && ifdata == 'yes'){//ȡ��ǧ��:0,1
		//fData=parseFloat(fData).toFixed(2);//��ʽΪXXXXXXXX.XX

		//fData=parseFloat(fData);
		fData=""+fData;
		if(fData.indexOf(".")>0){
		point=fData.substr(fData.indexOf(".")+1,2);//ȡ��С�����Ĳ���
		fData=fData.split(".")[0];//ȡ��С����ǰ�Ĳ���
		}else{
			fData=fData+".00";
			point = 'true';
		}



		var re=/(\d+)(\d{3})/,fData=fData.toString();//����ǧ�ַ�

		while(re.test(fData))fData=fData.replace(re,"$1,$2");//����ǧ�ַ�
		if(point=='true'){
			fData=fData;
		}else{
			fData=fData+"."+point;
		}

		}
	if (dataIsInt == "1" && ifdata == 'yes'){//����ȡ��:0,1

		if(fData.indexOf(".")>0){
			fData1=fData.substr(fData.indexOf(".")+1,2);//ȡ��С�����Ĳ���
			fData2=fData.split(".")[0];//ȡ��С����ǰ�Ĳ���
			fData = fData2+"."+fData1;
		}else{
			fData=fData+".00";//ȡ��С����ǰ�Ĳ���
		}

		//fData=parseFloat(fData).toFixed(2);//��ʽΪXXXXXXXX.XX
		point=fData.substr(fData.indexOf(".")+1,2);//ȡ��С�����Ĳ���
		point = Math.round("0."+point);//С���㲿����������
		fData = fData.split(".")[0];//��ʽΪXX,XXX,XXX
		var d = fData.substr(fData.length-1,1);
		d = parseInt(d)+point;//�������һλ��С�������������������
		fData = fData.substr(0,fData.length-1)+d;//����������

		//var re=/(\d+)(\d{3})/,fData=fData.toString();//����ǧ�ַ�

		//while(re.test(fData))fData=fData.replace(re,"$1,$2");//����ǧ�ַ�

	}
	if (zeroToSpace == "1"){//0��ʾ��:0,1
		if (fData == "0" || fData == "0.00") fData = "&nbsp;";
	}
	return fData;
}

/*
*   ������    |      ˵��
*------------------------------
* showTable() |  Ԥ��TableЧ��
*
*/

function showTable() {
/*
var showArray0	= "";//showArray0:���ͷ���б�ͷ��С��ͷ
var showArray2	= "";//���ݲ�����ʾ����
var showArray3	= "";//�ϼƲ�����ʾ����
var showArray4	= "";//��������ʾ����
*/
	var s = "";
	if (sumIsTop == "1"){//�ϼ�����
		s = showArray0+showArray3+showArray2+showArray4;
	}else{
		s = showArray0+showArray2+showArray3+showArray4;
	}
	document.all.showTable.innerHTML=s;

}
/*
*   ������    |      ˵��
*------------------------------
* printTable() |  ��ӡԤ��TableЧ��
*
*/

function printTable(){
	var s = "";
	if (sumIsTop == "1"){//�ϼ�����
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
	if (sumIsTop == "1"){//�ϼ�����
		s = showArray0+showArray3+showArray2;
	}else{
		s = showArray0+showArray2+showArray3;
	}



var newwindow=window.open('print.htm','fff','fullscreen=yes,resizable=yes');
newwindow.focus();


//window.open("print.htm?info="+s,"print","toolbar=no,resizable=yes,fullscreen=yes");

}
/*
*   ������  |    ˵��
*------------------------
* sumData() |  �������
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
*   ������     |         ˵��         |  CSS
*----------------------------------------------
* strToTable() |  �ִ�ת��Ϊ�����  |  head
*
*/

function strToTable(str){


		htmlTable = "";	//
		getString = str;
		
		//getString=1|1,rowspan=1|1,rowspan=1|1,||
		
		if (getString.indexOf(",||") > 0){//���ӱ�rowspan=1|2.1,colspan=3|2.2,||
			step1 = getString.split("||")

			for (i=0;i<step1.length-1;i++){	//�ж��м��У��õ�ÿһ�е���Ϣ
				step2 = step1[i].split(",");
				htmlTable +="<tr class=head>";

				for(j=0;j<step2.length-1;j++){
					step3 = step2[j].split("|");
					htmlTable +='<td '+step3[0]+' align=center>'+step3[1]+'</td>';
				}

			htmlTable +="</tr>";
			}


		}else{//��ͨ��

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
					htmlTable += "<td align=center>����</td>";
				}
			}
			htmlTable +='</tr>';
		}

	return htmlTable;

}

/*
* ===================================����===========================================
*
*   ������   |      ˵��
*-----------------------------------
* submitgo() |  ����FORM��
*            |  ��ҳ����һҳ
*            |  ��һҳ��ĩҳBUTTON
*            |  �¼�����
*
*/

function submitgo(val){
if (val == -1){ //���ύ��ֱ���ύ
    document._form.nvBar.value= -1;
}else{
    //������
    document._form.nvBar.value=1;
    if (val != -100){    //����ֱ�Ӱ�ȷ��
    	document._form.pageNo.value=val;
    }
}

	document._form.submit();
	divToDo();

 }

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

function mOut(src){
	if (!src.contains(event.toElement)) {
	src.style.cursor = 'default';
	src.bgColor = '#FFFFFF';
	}
}

/*
*   ������   |      ˵��
*-----------------------------------
*   divToDo()   |  ����������
*            |  �ύ�¼�����
*
*/
function divToDo(){
document.all.sending.style.display='';
document.all.over.style.display='';

}


/*
*   ������   |      ˵��
*-----------------------------------
*   mClk()   |  ����������
*            |  �ύ�¼�����
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
      //�޸����Ӳ�����
	var hrf=linkUrl;
 	if (linkUrl.indexOf("?") <0) { //û������
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

//����!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==================================================================================
</script>


</body>
</html>


<% 
MyDB.getInstance().removeCurrentThreadConn("query.jsp"); //added by JGO on 2004-07-17
%>