<%@ page contentType="text/html; charset=GBK" %>
<%@ page errorPage="../common/ErrMsg.jsp" %>
<%@ page import="zt.cms.report.Query" %>
<html>
<head>
<title>
</title>
<SCRIPT src="meizzDate.js" type=text/javascript></SCRIPT>
<link rel="stylesheet" type="text/css" href="web.css">
</head>
<body topmargin="3" leftmargin="2" bgcolor="#eeeeee">
<TABLE borderColor=#999999 cellSpacing=0 cellPadding=0 align=center bgColor=#f1f1f1 border=1 width=100%>
 <TBODY>
  <TR vAlign=center align=middle>
   <TD>
    <TABLE width=100%>
     <TBODY>
      <TR>
       <TD>
        <table cellSpacing=1 cellPadding=0 width=100% border=0>
          <TR >
		   <td id=detailTab></td>
          </TR>
	    </TABLE>
		<!-- JavaScript ��ʾ��Begin======================================================================= -->
		<div id=show align=center width=100%>
		</div>
		<!-- JavaScript ��ʾ��End========================================================================= -->
	   </TD>
	  </TR>
	 </TBODY>
    </TABLE>
   </TD>
  </TR>
  <TR vAlign=bottom align=middle>
   <TD>
   </TD>
  </TR>
 </TBODY>
</TABLE>
<div id=help>
������<br>
1��˵��һ<br>
2��˵����<br>
3��˵����
</div>

<%
Query qry = new Query();
qry.setRequest(request);

String selQryNo=null;
String qryNo=null;
String strRtn="";
String qryList="";
String cmd=request.getParameter("cmd");
if (cmd ==null){
  cmd="";
}
if (cmd.equals("getSql")){
  String getVal=qry.getTestSql();  //ֻҪSQL���
  if (getVal ==null){
    strRtn="�޷�����SQL���";
  }else{
    String[] tmp=getVal.split("!");
    for ( int i=0;i<tmp.length;i++){
      strRtn += tmp[i] +"<br>";
    }
  }
}
qryNo=request.getParameter("qryNo");

selQryNo=request.getParameter("selectQryNo");
if (selQryNo !=null){    //������Ŀ����
  qryNo=selQryNo;

}
qryList=qry.getQryList();
if (qryNo ==null){
   qryNo=qryList.substring(0,6);
}

if (cmd.startsWith("cmd")){ //add,del,chg
  strRtn=qry.saveDefine(cmd);

}

qry.getTableDefine(qryNo);

qry.closeDB();

String errMsg =qry.errMsg;
if (errMsg !=null){
  strRtn=errMsg;
}
if (!strRtn.equals("")){
  out.println("<pre>");
  out.println( strRtn );
  out.println("</pre>");
}

%>
<script language=javascript>
/***********************************************************************************
|***********************************************************************************
|* Author:Ego_xu
|* Email:ego_xu@126.com
|* QQ:100790716
|* MSN:ego_xu@hotmail.com
|***********************************************************************************
|* ����������(�滻��)
*/

/**
* ����ΪTOPҳ���������
*/
qryNo="<%=qryNo%>";
qryList="<%=qryList%>";
etop=parent.frames[0].document.all.qrySelect;
eList=parent.frames[0].document.all.qrySelect.selectQryNo;
if (eList.length >1){  //ɾ��������Ŀ
  for (i = eList.options.length - 1; i >=  0; i--) {
    eList.remove(i);
  }
}
t1=qryList.split(",");
for (i=0;i<t1.length;i++){
  t2=t1[i].split("=");
  var oOption = document.createElement("OPTION");
  oOption.text=t2[0]+"--"+t2[1];
  oOption.value=t2[0];
  if(t2[0] == qryNo)  oOption.selected=true;
    eList.add(oOption);
  }
etop.qryNo.value=qryNo;
etop.qryName.value="<%=qry.getTblValue("qryName")%>";

/**
* ����ΪSQLҳ���������
*/
esql=parent.frames[1].document.all.sql;

esql.selectQryNo.value   = "<%=qryNo%>";

esql.sqlSelect.value   = "<%=qry.getTblValue("sqlSelect") %>";
esql.sqlFrom.value     = "<%=qry.getTblValue("sqlFrom") %>";
esql.sqlWhere.value    = "<%=qry.getTblValue("sqlWhere") %>";
esql.sqlWhere1.value   = "<%=qry.getTblValue("sqlWhere1") %>";
esql.sqlGroup.value    = "<%=qry.getTblValue("sqlGroup") %>";
esql.sqlOrder.value    = "<%=qry.getTblValue("sqlOrder") %>";
esql.sqlKey.value  	= "<%=qry.getTblValue("sqlKey") %>";

/**
*����ΪSETUPҳ���������
*/




var cols		= '<%=qry.getTblValue("sqlParams") %>';//������

var pageSize    = "<%=(qry.getIntValue("pageSize") ==-1) ?10:qry.getIntValue("pageSize") %>";//ҳ��¼��
var pageNo      = "<%=(qry.getIntValue("pageNo") ==-1) ?0:qry.getIntValue("pageNo") %>";
var total		= "<%=(qry.getIntValue("total") ==-1) ?0:qry.getIntValue("total") %>";
var qryList		= "<%=qryList%>";


var curBgcolor  = '#E7E7E7';//�����ɫ
var linkCol		= '<%=qry.getTblValue("linkCol") %>';//������
var formatCols  = '<%=qry.getTblValue("formatCols") %>';//������
var middleCols  = '<%=qry.getTblValue("middleCols") %>';//������
var rightCols   = '<%=qry.getTblValue("rightCols") %>';//�Ҷ����� RIGHTCOLS
var dataNoWraps = '<%=qry.getTblValue("dataNoWraps") %>';//�������� dataNoWraps
var sumCols		= '<%=qry.getTblValue("sumCols") %>';//�ϼ���
var sumIsTop    = '<%=qry.getTblValue("sumIsTop") %>';//�ϼ�����,���ݺϼ���Ϻ����ж�
var dataIsInt   = '<%=qry.getTblValue("dataIsInt") %>';//����ȡ��:0,1
var dataIsSplit = '<%=qry.getTblValue("dataIsSplit") %>';//ȡ��ǧ��
var dataDispWan = '<%=qry.getTblValue("dataDispWan") %>';//��Ԫ��ʾ
var zeroToSpace = '<%=qry.getTblValue("zeroToSpace") %>';//0��ʾ��
var caption		= '<%=qry.getTblValue("caption") %>';//����
var lTitle		= '<%=qry.getTblValue("lTitle") %>';//����1:���ӱ���
var mTitle		= '<%=qry.getTblValue("mTitle") %>';//����2:���ӱ���
var rTitle		= '<%=qry.getTblValue("rTitle") %>';//����3:���ӱ���
var colNames    = '<%=qry.getTblValue("colNames") %>';//������:�Զ��ŷָ��ʾ
var qryIsList   = '<%=qry.getTblValue("qryIsList") %>';  //ѡ������: -->

var listNo		= '<%=qry.getTblValue("listNo") %>'; //�¼����: -->
var qryParams   = '<%=qry.getTblValue("qryParams") %>';  //ѡ������: -->
var linkUrl		= '<%=qry.getTblValue("linkUrl") %>';    //���ӳ���: -->
var linkNewPage = '<%=qry.getTblValue("linkNewPage") %>';    //��ҳ��ʾ: -->

var pageSize	= '<%=qry.getStrValue("pageSize")%>';//ҳ��¼��
var pageNo      = '<%=qry.getStrValue("pageNo")%>';//��ǰҳ
var total		= '<%=qry.getStrValue("total")%>';//��¼������




//var edb =top.frames[3].document.all.dbdata;
var eset=parent.frames[3].document.all.setup;


//edb.data.value  = dd;
//edb.rows.value  = rows;
eset.sqlParams.value  = cols;


if(linkCol == '1'){
eset.linkCol[1].checked = true; //������,
}else{
eset.linkCol[0].checked = true; //������,1
}

eset.total.value       = total;   //�¼����: -->
eset.pageNo.value       = pageNo;   //�¼����: -->

eset.listNo.value       = listNo;   //�¼����: -->
eset.qryParams.value        = qryParams;    //ѡ������: -->
eset.linkUrl.value      = linkUrl;      //���ӳ���: -->
eset.pageSize.value     = pageSize; //ҳ��¼��
eset.formatCols.value       = formatCols;   //������
eset.middleCols.value       = middleCols;   //������
eset.rightCols.value        = rightCols;    //�Ҷ����� RIGHTCOLS
eset.dataNoWraps.value      = dataNoWraps;  //�������� dataNoWraps
eset.sumCols.value      = sumCols;  //�ϼ���


if(qryIsList == '1'){//�������
eset.qryIsList[1].checked = true;
parent.frames[3].document.all.qrLi1.style.display='none';
parent.frames[3].document.all.qrLi2.style.display='';
}else{
eset.qryIsList[0].checked = true;
parent.frames[3].document.all.qrLi1.style.display='';
parent.frames[3].document.all.qrLi2.style.display='none';
}


if(linkNewPage == '1'){//�Ƿ���ҳ��ʾ
eset.linkNewPage[0].checked    = true;
}else{
eset.linkNewPage[1].checked = true;
}

if(sumIsTop == '1'){
eset.sumIsTop[0].checked    = true; //�ϼ�����,���ݺϼ���Ϻ����ж�,1
}else{
eset.sumIsTop[1].checked = true;
}
if(dataIsInt == '1'){
eset.dataIsInt[0].checked   = true; //����ȡ��:0,1
}else{
eset.dataIsInt[1].checked = true;   //��Ԫ��ʾ,1
}
if(dataIsSplit == '1'){
eset.dataIsSplit[0].checked = true; //ȡ��ǧ��,1
}else{
eset.dataIsSplit[1].checked = true; //��Ԫ��ʾ,1
}
if(dataDispWan == '1'){
eset.dataDispWan[0].checked = true; //��Ԫ��ʾ,1
}else{
eset.dataDispWan[1].checked = true; //��Ԫ��ʾ,1
}
if(zeroToSpace == '1'){
eset.zeroToSpace[0].checked = true; //0��ʾ��,1
}else{
eset.zeroToSpace[1].checked = true; //��Ԫ��ʾ,1
}


eset.caption.value      = caption;      //����
eset.lTitle.value           = lTitle;       //����1:���ӱ���
eset.mTitle.value           = mTitle;       //����2:���ӱ���
eset.rTitle.value           = rTitle;       //����3:���ӱ���
eset.colNames.value     = colNames;     //������:�Զ��ŷָ��ʾ

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
if (val !=0){
	document._table.pageNo.value=val;
     }
    document._table.submit();
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
*   mClk()   |  ����������
*            |  �ύ�¼�����
*
*/

function mClk(val){
val.split(",");
alert("����ID="+val.split(",")[0]+" ����NMAE="+val.split(",")[1]);
}
/*
*      ������        |      ˵��
*-----------------------------------
*   addQueryItem()   |  �ύ������
*
*/

function addQueryItem(){
var content=document.all.detail.content.value;
var props = document.all.detail.props.value;
document.all.detailTab.innerHTML=content;//Ԥ��TableЧ��
	if ( props == '' ) {
		document.all.detail.props.value = '1';
		document.all.dd.title='����˴����ز�ѯ������';
		document.all.detailTab.style.display = '';
	} else {
		document.all.detail.props.value = '';
		document.all.detailTab.style.display = 'none';
		document.all.dd.title='����˴���ѯ����';
	}
}

</script>
</body>
</html>