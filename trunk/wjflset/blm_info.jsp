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
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.utils.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%
DecimalFormat df=new DecimalFormat("###########0.00");
request.setCharacterEncoding("GBK");
String FCCLASS=request.getParameter("FCCLASS");
ConnectionManager manager=ConnectionManager.getInstance();
BigDecimal FACTOR=null;
String sql="select * from FCFACTOR where FCCLASS="+FCCLASS+"";
CachedRowSet crs=manager.getRs(sql);
if(crs.next()){
	FCCLASS=crs.getString("FCCLASS");
	FACTOR=crs.getBigDecimal("FACTOR");
}
String mess=(String)session.getAttribute("mess");
if(mess==null){
	mess="";
}
else{
	mess="<li class='error_message_li'>"+mess.trim()+"</li>";
	session.removeAttribute("mess");
}
%>
<html>
	<head>
		<title>�Ŵ�����</title>
		<link href="../css/platform.css" rel="stylesheet" type="text/css">
		<script src='/js/fcmain.js' type='text/javascript'></script>
		<script language="javascript" src="/js/focusnext.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<style type="text/css">
		<!--
		body {
			margin-top: 5px;
		}
		-->
		</style>
	</head>
	
	<body background="../images/checks_02.jpg">
	<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" valign="middle">
          <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" >
            <tr align="left">
              <td height="30" bgcolor="#4477AA"> <img src="../images/form/xing1.jpg" align="absmiddle"> <font size="2" color="#FFFFFF"><b>����������ʧ����</b></font> <img src="../images/form/xing1.jpg" align="absmiddle"></td>
            </tr>
            <tr align="center">
              <td height="260" valign="middle">
			  <form name="form1" method="post" action="" >
                <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                  <tr>
                    <td width="20">&nbsp;</td>
                    <td align="center" valign="middle">
					<table class='error_message_tbl'>
					<tr class='error_message_tbl_tr'>
					<td class='error_message_tbl_td'><%=mess%></td>
					</tr>
					</table>
                      <table class='page_form_table' id='page_form_table' width="300" height="20">
                     
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                        
                    
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>�弶����</td>
                          <td class="page_form_td" nowrap>
						 <%=level.levelHere1("FCCLASS","LoanCat1",FCCLASS)%>
                          </td>
                        </tr>
						<tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                        
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>Ȩ��</td>
                          <td class="page_form_td" nowrap>
						     <input type="text" name="FACTOR" value="<%=FACTOR==null?"":df.format(FACTOR)%>" size="20"    class="page_form_text"></td>

                          </td>
                        </tr>
                     
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                        
                    </table></td>
                    <td width="20">&nbsp;</td>
                  </tr>
              </table>
			  
			  </td>
            </tr>
            <tr height="35" align="center" valign="middle">
              <td align="center">
                <table border="0" cellspacing="0" cellpadding="0" width="538">
                  <tr>
                    <td nowrap align="center">
                      <table class='list_button_tbl'>
                        <tr class='list_button_tbl_tr'>
                           <td class="list_form_button_td"><input name='add' type='button' class='page_button_disabled' id="add"  value=' �� �� ' disabled='true'></td>
						   <td class="list_form_button_td"><input name='del' type='button' class='page_button_disabled' id="del" value=' ɾ �� ' disabled='true'></td>
						   <td class='page_button_tbl_td'><input type='submit' class='page_button_active' id='saveup' name='save' value=' �� �� '  onclick="return Regvalid()"></td>
						   <td class='page_button_tbl_td'><input type='button' class='page_button_active' name='button' value=' �� �� ' onClick="return user_check();"></td>
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
document.title="����������ʧ����";
document.focus();
function Regvalid(){
  var FACTOR=form1.FACTOR;
  var FACTORVAL=FACTOR.value;
  if(isDigit(FACTORVAL)==false || FACTORVAL.length==0){
    alert("Ȩ�طǷ����룡");
    FACTOR.focus();
    return false;
  }else{
	  form1.action="blm_info_save.jsp";
	  form1.submit();
  }
}
function user_check(){
   this.opener.location="/wjflset/blm_set.jsp?pn="+this.opener.document.all.pnstr.value+"";
   this.close();   
}
</script>


%>