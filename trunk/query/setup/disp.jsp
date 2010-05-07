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
		<!-- JavaScript 显示区Begin======================================================================= -->
		<div id=show align=center width=100%>
		</div>
		<!-- JavaScript 显示区End========================================================================= -->
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
帮助：<br>
1、说明一<br>
2、说明二<br>
3、说明三
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
  String getVal=qry.getTestSql();  //只要SQL语句
  if (getVal ==null){
    strRtn="无法生成SQL语句";
  }else{
    String[] tmp=getVal.split("!");
    for ( int i=0;i<tmp.length;i++){
      strRtn += tmp[i] +"<br>";
    }
  }
}
qryNo=request.getParameter("qryNo");

selQryNo=request.getParameter("selectQryNo");
if (selQryNo !=null){    //更新项目定义
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
|* 公共变量区(替换区)
*/

/**
* 以下为TOP页面所需变量
*/
qryNo="<%=qryNo%>";
qryList="<%=qryList%>";
etop=parent.frames[0].document.all.qrySelect;
eList=parent.frames[0].document.all.qrySelect.selectQryNo;
if (eList.length >1){  //删除所有项目
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
* 以下为SQL页面所需变量
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
*以下为SETUP页面所需变量
*/




var cols		= '<%=qry.getTblValue("sqlParams") %>';//总列数

var pageSize    = "<%=(qry.getIntValue("pageSize") ==-1) ?10:qry.getIntValue("pageSize") %>";//页记录数
var pageNo      = "<%=(qry.getIntValue("pageNo") ==-1) ?0:qry.getIntValue("pageNo") %>";
var total		= "<%=(qry.getIntValue("total") ==-1) ?0:qry.getIntValue("total") %>";
var qryList		= "<%=qryList%>";


var curBgcolor  = '#E7E7E7';//光标颜色
var linkCol		= '<%=qry.getTblValue("linkCol") %>';//参数列
var formatCols  = '<%=qry.getTblValue("formatCols") %>';//数据列
var middleCols  = '<%=qry.getTblValue("middleCols") %>';//居中列
var rightCols   = '<%=qry.getTblValue("rightCols") %>';//右对齐列 RIGHTCOLS
var dataNoWraps = '<%=qry.getTblValue("dataNoWraps") %>';//不折行列 dataNoWraps
var sumCols		= '<%=qry.getTblValue("sumCols") %>';//合计列
var sumIsTop    = '<%=qry.getTblValue("sumIsTop") %>';//合计上显,数据合计完毕后作判断
var dataIsInt   = '<%=qry.getTblValue("dataIsInt") %>';//数据取整:0,1
var dataIsSplit = '<%=qry.getTblValue("dataIsSplit") %>';//取消千分
var dataDispWan = '<%=qry.getTblValue("dataDispWan") %>';//万元显示
var zeroToSpace = '<%=qry.getTblValue("zeroToSpace") %>';//0显示空
var caption		= '<%=qry.getTblValue("caption") %>';//标题
var lTitle		= '<%=qry.getTblValue("lTitle") %>';//标题1:左子标题
var mTitle		= '<%=qry.getTblValue("mTitle") %>';//标题2:中子标题
var rTitle		= '<%=qry.getTblValue("rTitle") %>';//标题3:右子标题
var colNames    = '<%=qry.getTblValue("colNames") %>';//列名称:以逗号分割，显示
var qryIsList   = '<%=qry.getTblValue("qryIsList") %>';  //选择条件: -->

var listNo		= '<%=qry.getTblValue("listNo") %>'; //下级编号: -->
var qryParams   = '<%=qry.getTblValue("qryParams") %>';  //选择条件: -->
var linkUrl		= '<%=qry.getTblValue("linkUrl") %>';    //链接程序: -->
var linkNewPage = '<%=qry.getTblValue("linkNewPage") %>';    //新页显示: -->

var pageSize	= '<%=qry.getStrValue("pageSize")%>';//页记录数
var pageNo      = '<%=qry.getStrValue("pageNo")%>';//当前页
var total		= '<%=qry.getStrValue("total")%>';//记录总条数




//var edb =top.frames[3].document.all.dbdata;
var eset=parent.frames[3].document.all.setup;


//edb.data.value  = dd;
//edb.rows.value  = rows;
eset.sqlParams.value  = cols;


if(linkCol == '1'){
eset.linkCol[1].checked = true; //参数列,
}else{
eset.linkCol[0].checked = true; //参数列,1
}

eset.total.value       = total;   //下级编号: -->
eset.pageNo.value       = pageNo;   //下级编号: -->

eset.listNo.value       = listNo;   //下级编号: -->
eset.qryParams.value        = qryParams;    //选择条件: -->
eset.linkUrl.value      = linkUrl;      //链接程序: -->
eset.pageSize.value     = pageSize; //页记录数
eset.formatCols.value       = formatCols;   //数据列
eset.middleCols.value       = middleCols;   //居中列
eset.rightCols.value        = rightCols;    //右对齐列 RIGHTCOLS
eset.dataNoWraps.value      = dataNoWraps;  //不折行列 dataNoWraps
eset.sumCols.value      = sumCols;  //合计列


if(qryIsList == '1'){//程序类别
eset.qryIsList[1].checked = true;
parent.frames[3].document.all.qrLi1.style.display='none';
parent.frames[3].document.all.qrLi2.style.display='';
}else{
eset.qryIsList[0].checked = true;
parent.frames[3].document.all.qrLi1.style.display='';
parent.frames[3].document.all.qrLi2.style.display='none';
}


if(linkNewPage == '1'){//是否新页显示
eset.linkNewPage[0].checked    = true;
}else{
eset.linkNewPage[1].checked = true;
}

if(sumIsTop == '1'){
eset.sumIsTop[0].checked    = true; //合计上显,数据合计完毕后作判断,1
}else{
eset.sumIsTop[1].checked = true;
}
if(dataIsInt == '1'){
eset.dataIsInt[0].checked   = true; //数据取整:0,1
}else{
eset.dataIsInt[1].checked = true;   //万元显示,1
}
if(dataIsSplit == '1'){
eset.dataIsSplit[0].checked = true; //取消千分,1
}else{
eset.dataIsSplit[1].checked = true; //万元显示,1
}
if(dataDispWan == '1'){
eset.dataDispWan[0].checked = true; //万元显示,1
}else{
eset.dataDispWan[1].checked = true; //万元显示,1
}
if(zeroToSpace == '1'){
eset.zeroToSpace[0].checked = true; //0显示空,1
}else{
eset.zeroToSpace[1].checked = true; //万元显示,1
}


eset.caption.value      = caption;      //标题
eset.lTitle.value           = lTitle;       //标题1:左子标题
eset.mTitle.value           = mTitle;       //标题2:中子标题
eset.rTitle.value           = rTitle;       //标题3:右子标题
eset.colNames.value     = colNames;     //列名称:以逗号分割，显示

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
if (val !=0){
	document._table.pageNo.value=val;
     }
    document._table.submit();
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
*   mClk()   |  表格中鼠标点击
*            |  提交事件函数
*
*/

function mClk(val){
val.split(",");
alert("链接ID="+val.split(",")[0]+" 链接NMAE="+val.split(",")[1]);
}
/*
*      函数名        |      说明
*-----------------------------------
*   addQueryItem()   |  提交区函数
*
*/

function addQueryItem(){
var content=document.all.detail.content.value;
var props = document.all.detail.props.value;
document.all.detailTab.innerHTML=content;//预览Table效果
	if ( props == '' ) {
		document.all.detail.props.value = '1';
		document.all.dd.title='点击此处隐藏查询窗口域';
		document.all.detailTab.style.display = '';
	} else {
		document.all.detail.props.value = '';
		document.all.detailTab.style.display = 'none';
		document.all.dd.title='点击此处查询数据';
	}
}

</script>
</body>
</html>