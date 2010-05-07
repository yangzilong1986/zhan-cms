<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Vector" %>
<%
    //MyDB.getInstance().removeCurrentThreadConn("workbench.list.jsp"); //added by JGO on 2004-07-17
%>

<%
    request.setCharacterEncoding("GBK");
//request.setCharacterEncoding("GBK");
//UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//String brhId = SCUser.getBrhId(um.getUserName()); //登录用户所在网点
    ConnectionManager manager = ConnectionManager.getInstance();

    String sql = "select * from FCREVIEWLIMIT where LIMITAMT >= 0 ";

    sql += " order by BRHID ";

    //CachedRowSet crs=manager.getRs(sql);
    String pnStr = request.getParameter("pn");
    if (pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
    int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
    int ps = 10;
    Vector vec = manager.getPageRs(sql, pn, ps);
    int rows = ((Integer) vec.get(0)).intValue();
    CachedRowSet crs = (CachedRowSet) vec.get(1);
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

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
/*function newAffair(affairName){
	var flag=0;
	var val;
	for(i=0;i<form1.elements.length;i++){
			if(form1.elements[i].checked == true){			
				flag= flag+1;
				val=form1.elements[i].value;
			}
			else{
				
			}
	}
	
	if(flag==0 || val==1){
		alert("请选择要增加的业务！");
		return false;
	}
	if(flag >=2){
		alert("每次只能增加一笔业务！");
		return false;
	}
	//alert(val);
	if(affairName=='add'){
		var url='Ljd_add.jsp?BRHID='+val;
		window.open(url,'FI2','height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
	}
}
function newAffair_del(affairName){
	if(affairName=='del'){
		var flag=0;
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
				form1.action='Ljd_del.jsp';
				form1.submit();
			}
			else{return false;}
		
		}
	}
}*/
function newAffair(affairName) {
    var flag = 0;
    if (affairName == 'add') {
        var url = 'Ljd_add.jsp?Flyy_add_del=' + affairName;
        window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes,status');
    }
    else {

        for (i = 0; i < form1.elements.length; i++) {
            if (form1.elements[i].checked == true) {
            }
            else {
                flag = flag + 1;
            }
        }
        if (flag >= form1.elements.length) {
            alert("请选择要删除的纪录！");
            return false;
        }
        else {
            if (confirm('确定删除吗？')) {
                form1.action = 'Ljd_del.jsp';
                form1.submit();
            }
            else {
                return false;
            }

        }
    }
}
function info(infoname) {
    var url = 'Ljd_info.jsp?BRHID=' + infoname;
    window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
}
function buttonDisabled() {
    with (form2) {
        a.disabled = "true";
        b.disabled = "true";
        c.disabled = "true";
    }
}
</script>
<script src='/js/check.js' type='text/javascript'></script>
<script src='/js/pagebutton.js' type='text/javascript'></script>
<script language="javascript" src="../js/flippage2.js"></script>
</head>
<body background="../images/checks_02.jpg">
<form action="" name="form1" method="post">
<input type="hidden" name="pnstr" value="<%=pnStr==null?"1":pnStr%>">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699"
       bgcolor="AACCEE">
<tr align="left">
    <td height="30" bgcolor="#4477AA"><img src="../images/form/xing1.jpg" align="absmiddle"> <font
            size="2" color="#FFFFFF"><b>清分临界点额度设置</b></font> <img src="../images/form/xing1.jpg"
                                                                  align="absmiddle"></td>
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
                        <table cellpadding='0' cellspacing='0' border='0'>
                            <tr>
                                <td height='5'></td>
                            </tr>
                        </table>
                        <table class='list_form_table' width='630' align='center'
                               cellpadding='0' cellspacing='1' border='0'>
                            <tr class='list_form_title_tr'>
                                <td nowrap width="3%" align="center"><input name="f02"
                                                                            type='checkbox'
                                                                            id="f02"
                                                                            onClick="checkSub('f02','form1','elements')"
                                                                            value="1"></td>
                                <td width='13%' class='list_form_title_td' nowrap
                                    align="center">网点代码
                                </td>
                                <td width='11%' class='list_form_title_td' nowrap
                                    align="center">网点级别
                                </td>
                                <td width='31%' class='list_form_title_td' nowrap
                                    align="center">网点名称
                                </td>
                                <td width='17%' class='list_form_title_td' nowrap
                                    align="center">自然人额度
                                </td>
                                <td width='17%' class='list_form_title_td' nowrap
                                    align="center">临界点额度
                                </td>
                                <td width='8%' class='list_form_title_td' nowrap
                                    align="center">详细
                                </td>
                            </tr>
                            <%
                                int j = 0;


                                while (crs.next()) {
                            %>
                            <tr class='list_form_tr'>
                                <td align='center' nowrap><input name="BRHID"
                                                                 type='checkbox'
                                                                 id="f020<%=j++%>"
                                                                 value="<%=crs.getString("BRHID")%>">
                                </td>
                                <td nowrap class='list_form_td' align="center"><%=
                                crs.getString("BRHID")%>
                                </td>
                                <td nowrap class='list_form_td' align="center"><%=
                                SCBranch.getBrhlevel(crs.getString("BRHID")) == null ? "" : level.getEnumItemName("BrhLevel", SCBranch.getBrhlevel(crs.getString("BRHID")))%>
                                </td>
                                <td nowrap class='list_form_td'><%=
                                SCBranch.getLName(crs.getString("BRHID")) == null ? "" : SCBranch.getLName(crs.getString("BRHID"))%>
                                </td>
                                <td nowrap class='list_form_td_money' align="left"><%=
                                crs.getBigDecimal("FCTypeAMT") == null ? "" : df.format(crs.getBigDecimal("FCTypeAMT"))%>
                                </td>
                                <td nowrap class='list_form_td_money' align="left"><%=
                                df.format(crs.getBigDecimal("LIMITAMT"))%>
                                </td>
                                <td nowrap class='list_form_td' align="center"><a
                                        class="list_edit_href"
                                        href='javascript:info("<%=crs.getString("BRHID")%>")'>详细</a>
                                </td>
                            </tr>
                            <%
                                }

                                for (int i = crs.size(); i < 10; i++) {
                            %>

                            <tr class='list_form_tr'>
                                <td nowrap class='list_form_td'></td>
                                <td nowrap class='list_form_td'></td>
                                <td nowrap class='list_form_td'></td>
                                <td nowrap class='list_form_td'></td>
                                <td nowrap class='list_form_td'></td>
                                <td nowrap class='list_form_td'></td>
                                <td nowrap class='list_form_td'></td>
                            </tr>
                            <%

                                }
                            %>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
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
                        <tr class="list_button_tbl_tr">
                            <td class="list_form_button_td"><input name='add' type='button'
                                                                   class='list_button_active'
                                                                   id="add"
                                                                   onClick="newAffair(this.name);"
                                                                   value=' 增加 '></td>
                            <td class="list_form_button_td"><input name='del' type='button'
                                                                   class='list_button_active'
                                                                   id="del"
                                                                   onClick="newAffair(this.name);"
                                                                   value=' 删除 '></td>
                            <td class="list_form_button_td"><input type='submit' name='a'
                                                                   class='list_button_active'
                                                                   value=' 刷新 '
                                                                   onclick="return req();"></td>
                            <td class="list_form_button_td">
                                <script language="javascript">
                                    createFlipPage(<%=pn%>, <%=ps%>, <%=rows%>, "Ljd_Set.jsp?pn=", "form1");
                                </script>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
</td>
</tr>
</table>
</form>
</body>
</html>
<script language="javascript">
    document.title = "清分临界点额度设置";
    document.focus();
    function checkSub(curChkName, formnm, funcnm)
    {
        //alert("dddddddd");
        if (curChkName == "" || formnm == "" || funcnm == "") {
            alert("checkSub error!");
            return;
        }
        var i;

        if (eval(formnm + "." + curChkName + ".checked")) {
            for (i = 0; i < eval(formnm + "." + funcnm + ".length"); i++) {
                if (eval(formnm + "." + funcnm + "[i].id.indexOf('" + curChkName + "')!=-1"))
                    eval(formnm + "." + eval(formnm + "." + funcnm + "[i].id") + ".checked=true");
            }
        }

        if (eval(formnm + "." + curChkName + ".checked==false")) {
            for (i = 0; i < eval(formnm + "." + funcnm + ".length"); i++) {
                if (eval(formnm + "." + funcnm + "[i].id.indexOf('" + curChkName + "')!=-1"))
                    eval(formnm + "." + eval(formnm + "." + funcnm + "[i].id") + ".checked=false");
            }
        }

        if (eval(formnm + "." + curChkName + ".checked")) {
            for (i = 0; i < eval(formnm + "." + funcnm + ".length"); i++) {
                if (eval(formnm + "." + funcnm + "[i].id.indexOf('" + curChkName + "')!=-1"))
                    eval(formnm + "." + eval(formnm + "." + funcnm + "[i].id") + ".checked=true");
            }
        }
    }
    function check_click() {
        document.form1.FCCLASS.value = "";
    }
    function req() {
        document.location.replace("/wjflset/Ljd_Set.jsp");
    }
</script>

<%
    //MyDB.getInstance().removeCurrentThreadConn("workbench.list(END).jsp"); //added by JGO on 2004-07-17
%>