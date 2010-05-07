<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.platform.cachedb.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.form.control.impl.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.utils.*" %>
<%@ page import="zt.platform.db.DBUtil" %>

<%
//MyDB.getInstance().removeCurrentThreadConn("workbench.list.jsp"); //added by JGO on 2004-07-17
%>

<%
request.setCharacterEncoding("GBK");
//request.setCharacterEncoding("GBK");
//UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//String brhId = SCUser.getBrhId(um.getUserName()); //登录用户所在网点
ConnectionManager manager=ConnectionManager.getInstance();
 String FCCLASS = request.getParameter("FCCLASS");

if(FCCLASS != null && FCCLASS.trim().length() <= 0) FCCLASS = null;

String sql="select * from FCREASON ";
if(FCCLASS != null) sql += "where FCCLASS="+FCCLASS+"";
sql +=" order by FCCLASS desc,seqno";

	
	//CachedRowSet crs=manager.getRs(sql);
	String pnStr = request.getParameter("pn");
	if(pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
	int pn=Integer.parseInt(pnStr==null?"1":pnStr);
	int ps=10;
	Vector vec=manager.getPageRs(sql,pn,ps);
	int rows=((Integer)vec.get(0)).intValue();
	CachedRowSet crs=(CachedRowSet)vec.get(1);


 %>



<html>
<head>
<title>信贷管理</title>
<link href="../css/platform.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type="text/css">
<!--
body {
	margin-top: 5px;
}
-->
</style>
<script language="JavaScript" type="text/JavaScript">
<!--
/*
function checkForm1(){
    if ( check(form1)) {
        form1.submit();
    }
}
function checkSubmit(){
    return true;
}

function distribute(bmno,bmtransno,clientname,bmacttype,brhid,type){
    var url='distribute.jsp?BMNO='+trim(bmno)+'&BMTRANSNO='
        +bmtransno+'&CLIENTNAME='+trim(clientname)
	+'&flag='+'&BMACTTYPE='+bmacttype+'&BRHID='+trim(brhid)+'&BMTYPE='+type;
    window.open(url,'FI2','height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
}

function newAffair(affairName){
    var url='new_affair.jsp?affairName='+affairName;
    window.open(url,'FI2','height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
}


}*/
//-->
function newAffair(affairName){
	var flag=0;
	if(affairName=='add'){
		var url='Flyy_add.jsp?Flyy_add_del='+affairName;
		window.open(url,'FI2','height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
	}
	else{
		
		for(i=0;i<form1.elements.length;i++){
			if(form1.elements[i].checked == true){}
			else{
				flag= flag+1;
			}
		}
		if(flag>=form1.elements.length){
			alert("请选择要删除的纪录！");
			return false;
		}
		else{
			if(confirm('确定删除吗？')){
				form1.action='Flyy_del.jsp';
				form1.submit();
			}
			else{return false;}
		
		}
	}
}
function info(infoname){
		var url='Flyy_info.jsp?SEQNO='+infoname;
		window.open(url,'FI2','height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
}
function buttonDisabled(){
    with(form2){
        a.disabled="true";
        b.disabled="true";
        c.disabled="true";
    }
}
</script>
<script src='/js/check.js' type='text/javascript'></script>
<script src='/js/pagebutton.js' type='text/javascript'></script>
<script language="javascript" src="../js/flippage2.js"></script>
</head>
<body background="../images/checks_02.jpg">
<form action="" name="form1" method="post" >
<input type="hidden" name="pnstr" value="<%=pnStr==null?"1":pnStr%>">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle">
      <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" >
        <tr align="left">
          <td height="30" bgcolor="#4477AA"> <img src="../images/form/xing1.jpg" align="absmiddle"> <font size="2" color="#FFFFFF"><b>五级分类原因设置</b></font> <img src="../images/form/xing1.jpg" align="absmiddle"></td>
        </tr>
        <tr align="center">
          <td height="260" valign="middle">
            <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
              <tr>
                <td width="20">&nbsp;</td>
                <td align="center" valign="middle">
                  <script src='/js/querybutton.js' type='text/javascript'></script>
                  <script src='/js/meizzDate.js' type='text/javascript'></script>
                  <table>
                    <tr>
                      <td>
                        <table align='center' cellpadding='0' cellspacing='0' border='0' bgcolor='#AAAAAA' width='538'>
                            <input type="hidden" name="flag" value="<%//flag%>">
                            <tr>
                              <td height="0">
                                <table id="findDiv" class="query_table" cellpadding='0' cellspacing='0' border='0' style='display:none'>
                                  <tr class="query_tr">
                                    <td class="query_td" width="80%">
                                      <table class='query_form_table' id='query_form_table' cellpadding='1' cellspacing='1' border='0'>
                                        <tr class="query_form_tr" nowrap>
                                          <td height="19" nowrap class="page_form_title_td">五级分类</td>
                                          <td class="page_form_td" ><%=level.levelHereExt("FCCLASS","LoanCat1",FCCLASS,"")%></td>
                                        </tr>
                                    </table></td>
                                    <td class="query_td" width="20%" align="center">
                                      <table border='0' width='100%' bgcolor='#F1F1F1'>
                                        <tr>
                                          <td nowrap valign="top"><input type="submit" class="query_button" name="Submit" value=" 检 索 " ></td>
                                        </tr>
                                        <tr>
                                          <td nowrap valign="top"><input type="button" class="query_button" name="reset" value=" 重 置 "  onclick="check_click()"></td>
                                        </tr>
                                    </table></td>
                                  </tr>
                              </table></td>
                            </tr>
                            <tr>
                              <td height="0" align="center"><img id='findDivHandle' title='点击查询' onClick='menuMove()' src='/images/form/button1.jpg' style='cursor:hand;'></td>
                            </tr>
                        </table>
                        <table cellpadding='0' cellspacing='0' border='0'>
                          <tr>
                            <td height='5'></td>
                          </tr>
                        </table>
                        <table class='list_form_table' width='530' align='center' cellpadding='0' cellspacing='1' border='0'>
                            <tr class='list_form_title_tr'>
							 <td   nowrap width="6%" align="center"><input name="f02" type='checkbox' id="f02" onClick="checkSub('f02','form1','elements')" value="1"></td>
                              <td width='15%' class='list_form_title_td' nowrap align="center">五级分类 </td>
                              <td width='70%' class='list_form_title_td' nowrap align="center">原因 </td>
                              <td width='9%' class='list_form_title_td' nowrap align="center">详细</td>
                            </tr>
                            <%
int j=0;


while(crs.next()){
%>
                            <tr class='list_form_tr'>
							<td align='center' nowrap><input name="SEQNO" type='checkbox' id="f020<%=j++%>" value="<%=crs.getString("SEQNO")%>"></td>
                              <td nowrap class='list_form_td' align="center"><%=crs.getString("FCCLASS")==null?"":level.getEnumItemName("LoanCat1",crs.getString("FCCLASS"))%></td>
                              <td nowrap class='list_form_td' align="left"><%=DBUtil.fromDB(crs.getString("REASON").length()>55?crs.getString("REASON").substring(0,55)+"...":crs.getString("REASON"))%></td>
                              <td nowrap class='list_form_td' align="center"><a class="list_edit_href" href='javascript:info("<%=crs.getString("SEQNO")%>")' >详细</a></td>
                            </tr>
                            <%
}

for(int i = crs.size();i<10;i++){
%>
        
                            <tr class='list_form_tr'>
							<td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
                            </tr>
                            <%

}
%>
                      </table></td>
                    </tr>
                </table></td>
                <td width="20">&nbsp;</td>
              </tr>
          </table></td>
        </tr>
        <tr height="35" align="center" valign="middle">
          <td align="center">
            <table border="0" cellspacing="0" cellpadding="0" width="538">
              <tr>
                <td nowrap align="center">
             
                  <table class='list_button_tbl'>
                    <tr class="list_button_tbl_tr">
                      <td class="list_form_button_td"><input name='add' type='button' class='list_button_active' id="add" onClick="newAffair(this.name);" value=' 增加 '></td>
                      <td class="list_form_button_td"><input name='del' type='button' class='list_button_active' id="del" onClick="newAffair(this.name);" value=' 删除 '></td>
                      <td class="list_form_button_td"><input type='submit' name='a' class='list_button_active' value=' 刷新 '  onclick="return req();"></td>
					  
						<script language="javascript">
    						createFlipPage(<%=pn%>,<%=ps%>,<%=rows%>,"Flyy_Set.jsp?pn=","form1");
						</script>
					
                    </tr>
                </table></td>
              </tr>
          </table></td>
        </tr>
    </table></td>
  </tr>
</table>
</form>
</body>
</html>
<script language="javascript">
document.title="五级分类原因设置";
document.focus();
function checkSub(curChkName,formnm,funcnm)
{
  //alert("dddddddd");
	if(curChkName=="" || formnm=="" || funcnm==""){
		alert("checkSub error!");
		return;
	}
	var i;

	if(eval(formnm+"."+curChkName+".checked")){
		for(i=0;i<eval(formnm +"." + funcnm +".length");i++){
			if(eval(formnm + "." + funcnm + "[i].id.indexOf('"+ curChkName + "')!=-1"))
				eval(formnm+"."+eval(formnm + "." + funcnm + "[i].id")+".checked=true");
		}
	}

	if(eval(formnm+"."+curChkName+".checked==false")){
		for(i=0;i<eval(formnm +"." + funcnm +".length");i++){
			if(eval(formnm + "." + funcnm + "[i].id.indexOf('"+ curChkName + "')!=-1"))
				eval(formnm+"."+eval(formnm + "." + funcnm + "[i].id")+".checked=false");
		}
	}

	if(eval(formnm+"."+curChkName+".checked")){
		for(i=0;i<eval(formnm +"." + funcnm +".length");i++){
			if(eval(formnm + "." + funcnm + "[i].id.indexOf('"+ curChkName + "')!=-1"))
				eval(formnm+"."+eval(formnm + "." + funcnm + "[i].id")+".checked=true");
		}
	}
}
function check_click(){
  document.form1.FCCLASS.value="";
}
function req(){
	document.location.replace("/wjflset/Flyy_Set.jsp");
}
</script>

<%
//MyDB.getInstance().removeCurrentThreadConn("workbench.list(END).jsp"); //added by JGO on 2004-07-17
%>