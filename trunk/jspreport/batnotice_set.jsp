<!--jsp:include page="/checkpermission.jsp"/-->
<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cms.cm.client.*" %>
<%@ page import="zt.platform.db.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.cms.pub.*" %>
<%
UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
String brhid = SCUser.getBrhId(um.getUserName());
String strbrhall = SCBranch.getSubBranchAll(brhid);
String[] brharr=null;
if(strbrhall!=null && strbrhall.trim().length()>0) brharr=strbrhall.split(",");
%>
<html>
	<head>
		<title>�Ŵ�����</title>
		<link href="../css/platform.css" rel="stylesheet" type="text/css">
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<style type="text/css">
		<!--
		body {
			margin-top: 5px;
		}
		-->
		</style>
		<script src='/js/meizzDate.js' type='text/javascript'></script>
	</head>
	<script language="javascript">
	function goPrint(){
		if(document.all("brhid").value.length<1){
			alert("ҵ�����㲻��Ϊ�գ�");
			document.all("brhid").focus();
			return false;
		}
		if(document.all("bdate").value.length<1){
			alert("��ʼ���ڲ���Ϊ�գ�");
			document.all("bdate").focus();
			return false;
		}
		if(document.all("edate").value.length<1){
			alert("�������ڲ���Ϊ�գ�");
			document.all("edate").focus();
			return false;
		}
    	var url='/jspreport/batnotice_prt.jsp';
		url+='?brhid='+document.all("brhid").value;
		url+='&bdate='+document.all("bdate").value;
		url+='&edate='+document.all("edate").value;
    	window.open(url,'FI2','width=800,height=600,toolbar=no,scrollbars=yes,resizable=yes');
	}
	function goReset(){
		document.form("winform").reset();
		return true;
	}
	</script>
	<body background="../images/checks_02.jpg">
	<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" valign="middle">
          <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" >
            <tr align="left">
              <td height="30" bgcolor="#4477AA"> <img src="../images/form/xing1.jpg" align="absmiddle"> <font size="2" color="#FFFFFF"><b>�����֪ͨ��������ӡ</b></font> <img src="../images/form/xing1.jpg" align="absmiddle"></td>
            </tr>
            <tr align="center">
              <td height="260" valign="middle">
			  <form id="winform" method="post" action="/jspreport/batnotice_prt.jsp">
                <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                  <tr>
                    <td width="20">&nbsp;</td>
                    <td align="center" valign="middle">
                      <table class='page_form_table' id='page_form_table' width="538" height="200">
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap width="15%">&nbsp;</td>
                          <td class="page_form_td" nowrap width="85%">&nbsp;</td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>ҵ������*</td>
                          <td class="page_form_td" nowrap>
                            <select name="brhid" class="page_form_select">
							  <option value="" >---------��ѡ������---------</option>
<%
if(brharr!=null){
	for(int i=0;i<brharr.length;i++){
		String brhname=SCBranch.getLName(brharr[i]);
%>
                              <option value="<%=brharr[i]%>" ><%=brhname%></option>
<%
	}
}
%>
                            </select>
                          </td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>��ʼ����*</td>
                          <td class="page_form_td" nowrap>
						  <input type="text" name="bdate" value="">
                          <input name="button" type="button" onclick="setday(this,winform.bdate)" value="��">
                          </td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>��������*</td>
                          <td class="page_form_td" nowrap>
						  <input type="text" name="edate" value="">
                          <input name="button" type="button" onclick="setday(this,winform.edate)" value="��">
                          </td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap align="right"><div align="right" class="list_form_td_unviewed">��ע�⣺</div></td>
                          <td align="left" nowrap class="page_form_td list_form_td_unviewed">��ʼ���ںͽ�������֮����ò�Ҫ����һ���£������������ܴ�</td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                        <tr class='page_form_tr'>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                          <td class="page_form_td" nowrap>&nbsp;</td>
                        </tr>
                    </table></td>
                    <td width="20">&nbsp;</td>
                  </tr>
              </table>
			  </form>
			  </td>
            </tr>
            <tr height="35" align="center" valign="middle">
              <td align="center">
                <table border="0" cellspacing="0" cellpadding="0" width="538">
                  <tr>
                    <td nowrap align="center">
                      <table class='list_button_tbl'>
                        <tr class='list_button_tbl_tr'>
                          <td class='list_button_tbl_td'><input type='button' name='submit1' class='list_button_active' value=' ��ӡ ' onClick="return goPrint();"></td>
                          <td class='list_button_tbl_td'><input type="button" name="submit2" class="list_button_active" value=" ���� " onClick="return goReset();"></td>
                        </tr>
                    </table></td>
                  </tr>
              </table></td>
            </tr>
        </table></td>
      </tr>
    </table>
	</body>
</html>
<script language="javascript">
document.title="�������";
document.focus();
</script>
