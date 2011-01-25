<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="zt.cms.xf.common.factory.XfvcontractprtinfoDaoFactory" %>
<%@ page import="zt.cms.xf.common.dao.XfvcontractprtinfoDao" %>
<%@ page import="zt.cms.xf.common.dto.Xfvcontractprtinfo" %>
<%@ page import="zt.platform.form.config.EnumerationBean" %>
<%@ page import="zt.platform.form.config.EnumerationType" %>
<%@ page import="zt.cms.xf.account.Airth" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="zt.cms.xf.common.factory.XfcontractpaysDaoFactory" %>
<%@ page import="zt.cms.xf.common.dto.Xfcontractpays" %>
<%@ page import="zt.cms.xf.common.dao.XfcontractpaysDao" %>
<%@ page import="java.util.Date" %>
<%
    request.setCharacterEncoding("gb2312");

           String contractno = request.getParameter("CONTRACTNO");

    if (contractno == null ) {
        session.setAttribute("msg", "��ͬ������");
        response.sendRedirect("../showinfo.jsp");
    } else {
        request.setCharacterEncoding("GBK");
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

        String strGoUrl = request.getParameter("goUrl");      //�ر�ҳ���־������ҳ��رգ��ǵ���������ҳ��
        String closeClick = "pageWinClose();";
        if (strGoUrl != null) closeClick = "gopage('" + strGoUrl + "')";

        //ConnectionManager manager = ConnectionManager.getInstance();
        String submit = "class='page_button_active'";
        String title = "�������ѷ��ڸ����ͬ";
        String onLoadStr = "";

        XfvcontractprtinfoDao dao = XfvcontractprtinfoDaoFactory.create();
        Xfvcontractprtinfo info = dao.findWhereContractnoEquals(contractno);

        EnumerationBean enum1 = EnumerationType.getEnu("IDType");

        String idtype = (String) enum1.getValue(info.getClientidtype());

        String chnContractamt = Airth.NumToChn(info.getContractamt().toString());
        String chnCommamt = Airth.NumToChn(info.getCommamt().toString());
        String chnReceiveamt = Airth.NumToChn(info.getReceiveamt().toString());

        DateFormat dateformat = new SimpleDateFormat("yyyy��MM��dd��");
        String startdate = dateformat.format(info.getStartdate());

        String phone = StringUtils.trimToEmpty(info.getPhone1()) + ", " + 
                StringUtils.trimToEmpty(info.getPhone2()) + ", " +
                StringUtils.trimToEmpty(info.getPhone3());


        XfcontractpaysDao paysDao = XfcontractpaysDaoFactory.create();
        Xfcontractpays pays[] = paysDao.findWhereContractnoEquals(contractno);
%>
<html>
<head>
    <title>���ڸ������ʵ���ӡ</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <!--media=print �������˵�������ڴ�ӡʱ��Ч-->
    <!--ϣ����ӡʱ����ʾ����������class="Noprint"��ʽ-->
    <!--ϣ����Ϊ���÷�ҳ��λ������class="PageNext"��ʽ-->
    <style media="print">
        <!--
        .Noprint {
            display: none;
        }

        .PageNext {
            page-break-after: always;
        }

        -->
    </style>
    <style type="text/css">
        <!--
.mytext {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 10.5pt;
	line-height: 30px;
	color: #000000;
}
.mytext_small {font-family: Arial, Helvetica, sans-serif; font-size: 10pt; line-height: 20px; color: #000000; }
.mytext_bold {font-family: Arial, Helvetica, sans-serif; font-size: 10.5pt; line-height: 20px; color: #000000; font-weight: bold; }
.mytext_footer {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 10.5pt;
	line-height: 2pt;
}
.STYLE10 {font-family: Arial, Helvetica, sans-serif; font-size: 10px; line-height: 20px; color: #000000; }

        .mainBox {
	border: 1px dashed #0099CC;
	margin: 30px;
	padding: 30px;
	float: left;
	height: 300px;
	width: 192px;
	margin-left: 50px;
}

        -->
    </style>
</head>
<SCRIPT type="text/javascript">
    var hkey_root,hkey_path,hkey_key;
    hkey_root = "HKEY_CURRENT_USER";

    var hk_pageHead,hk_pageFoot;
    <!--��ַ��д�����ϸ����˫б��-->
    hkey_path = "\\Software\\Microsoft\\Internet Explorer\\PageSetup";
    //������ҳ��ӡ��ҳüҳ��Ϊ��
    function pagesetup_null() {
        try {
            var RegWsh = new ActiveXObject("WScript.Shell");
            hkey_key = "\\header";
            hk_pageHead = RegWsh.RegRead(hkey_root + hkey_path + hkey_key);
            RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
            hkey_key = "\\footer";
            hk_pageFoot = RegWsh.RegRead(hkey_root + hkey_path + hkey_key);
            RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
        } catch(e) {
            //alert('��Ҫ��������Activex���ܽ��д�ӡ���á�');
        }
    }
    //������ҳ��ӡ��ҳüҳ��ΪĬ��ֵ
    function pagesetup_default() {
        try {
            var RegWsh = new ActiveXObject("WScript.Shell");
            hkey_key = "\\header";
            //RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&w&bҳ��,&p/&P");
            RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, hk_pageHead);
            hkey_key = "\\footer";
            //RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&u&b&d");
            RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, hk_pageFoot);
        } catch(e) {
        }
    }
    function printsetup() {
        wb.execwb(8, 1); // ��ӡҳ������
    }
    function printpreview() {
        wb.execwb(7, 1);// ��ӡҳ��Ԥ��
    }
    function printit() {
        if (confirm('ȷ����ӡ��')) {
            pagesetup_null();
            wb.execwb(6, 1);
            pagesetup_default();
        }
    }

    //    function doPrint() {
    //        pagesetup_null();
    //        var str = "<link href=\"../css/platform.css\" rel=\"stylesheet\" type=\"text/css\">";
    //        str += document.all.winform.innerHTML;
    //        str += "<script language=\"javascript\">window.print();<\/script>";
    //        //str += "<script language=\"javascript\">document.all.wb.ExecWB(7,1);<\/script>";
    //
    //        document.open();
    //        document.write(str);
    //        document.close();
    //        pagesetup_default();
    //        history.go(-1);
    //    }

    function doPrint(printDiv) {
        try {
            pagesetup_null();

            newwin = window.open("", "newwin", "height=" + window.screen.height + ",width=" + window.screen.width + ",toolbar=no,scrollbars=auto,menubar=no");
            newwin.document.body.innerHTML = document.getElementById(printDiv).innerHTML;
            //newwin.document.body.innerHTML = document.getElementById("printdiv").innerHTML;
            //            newwin.document.body.innerHTML = document.getElementById().innerHTML;
            newwin.window.print();
            newwin.window.close();
            pagesetup_default();
        } catch(e) {
        }
    }

</SCRIPT>

<body background="../images/checks_02.jpg" <%=onLoadStr%>>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr class='page_form_tr'>
<td align="center" valign="middle">
<table width="80%" height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#816A82" bgcolor="#E0E0D3">



<tr align="center" bordercolor="#FFFFFF" bgcolor="#CCCCCC">
<td width="100%" height="260" valign="middle">

<div id="printdiv" class="mainBox">


<table height="100%" border="0" cellpadding="0" cellspacing="0" id="printtable">

<tr class='page_form_tr'>
<td width="7">&nbsp;</td>
<td width="598" align="center" valign="middle">
<div align="left">
<p align="left" class="mytext" style='line-height:150%'><span class="mytext_bold" style="line-height:150%">�𾴵�<%=info.getClientname()%>������С�㣩��</span></p>
<p align="left" class="mytext" style='line-height:150%'>������ӭ����Ϊ�������Ų����������ι�˾�����¼�ƣ���������˾���ķ��ڸ���ͻ�!���������Ϳ���������ڸ���������ĳ�ֵ����!�����ڷ��ڹ�������Ҫ����,���µ�0532-88939904��<br>
  ��������<u><%=dateformat.format(info.getAppdate())%></u>��<u><%=info.getPartnername()%>
    </u>������ڹ���<u><%=info.getCommname()%>
      </u>,��Ʒ�ܼۿ<u><span
            style="font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333"><%=info.getCommamt().toString()%></span></u>Ԫ,�׸��<u><span
            style="font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333"><%=info.getReceiveamt().toString()%></span></u>Ԫ,�����ܽ�<u><span
            style="font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333"><%=info.getContractamt().toString()%></span></u>Ԫ,ѡ��ķ�������:
  <u><%=info.getDuration()%></u>�ڣ����ñʷ������õ���������Ϊ<u><%=info.getServicecharge().doubleValue() * 1000%>
    </u>�룬���ñʷ��ڶ�Ӧ��ͬ���Ϊ��<u><%=info.getContractno()%>
      </u> ��<br>
  ��������������������ᱨ����Ϣ����Լ���Ļ��ʽΪͨ�� <u><%=StringUtils.trimToEmpty(info.getWithholdbankname())%>
    </u>���ۻ�������˻�Ϊ��<u><%=info.getPaybackact()%>
    </u>�����ǽ���ʱ���������˺��п�����Ӧ�����������ķ��ڻ���ƻ�,���������������˻���ȷ��ÿ�ڴ�������ʽ��Ա�֤����ʱ���� ��</p>
<div align="center" class="mytext_bold">
  <table width="549" border="1" cellspacing="0" bordercolor="#333333" class="page_form_table" style='font-size:10.5pt;'>
    <tr class="mytext">
      <th width="49" height="30" scope="col"><span class="STYLE4">����</span></th>
        <th width="110" height="30" scope="col"><span class="STYLE4">��������</span></th>
        <th width="241" height="30" scope="col"><span class="STYLE4">���(����Ҵ�д)</span></th>
        <th width="129" height="30" scope="col"><span class="STYLE4">���(�����Сд)</span></th>
      </tr>
    <% for (int i=0; i<pays.length; i++){%>
    <tr class="mytext">
      <td height="20" class="mytext_small"><div align="center" class="STYLE4"> <%=pays[i].getPoano()%> </div></td>
        <td height="20" class="mytext_small"><div align="center" class="STYLE4"> <%=dateformat.format(pays[i].getStartdate())%> </div></td>
        <td height="20" class="mytext_small"><div align="center" class="STYLE4"> <%=Airth.NumToChn(df.format(pays[i].getCpaamt().doubleValue()))%> </div></td>
        <td height="20" class="mytext_small"><div align="center" class="STYLE4"> <%="��" + df.format(pays[i].getCpaamt().doubleValue())%> </div></td>
      </tr>
    <%}%>
  </table>
</div>
<p class="mytext"><span class="mytext">��ܰ��ʾ��</span></p>
<div align="left" >
<p>  <span class="STYLE10">���л���ͻ��Ժ������ŵĳ���֧�֣�Ϊ�����������Ѱ�ȫ������ע���������<br>
  1.���Ʊ��ܸ�������:���𽫸�����Ϣ�������ˡ�<br>
  2.����绰��թ���ҹ�˾�����µ�������ʱ�������б�Ҫ�ĺ˶��⣬һ�㲻������ѯ����������Ҫ�����ϣ�������������������µ�0532-88939904�ͷ�����֪ͨ�ҹ�˾��<br>
  3.��������թƭ�������Լ��ĸ����˻���Ϣ��͸¶����������ע����ɵ����ʼ�����Ҫ�ظ����ṩ�����˻���Ϣ��</span></p>
</div>  
  
<p align="right"> <span class="mytext_footer">�������Ų����������ι�˾</span></p>
<p align="right"> <span class="mytext_footer"><%=dateformat.format(new Date())%></span></p>
</div></td>
<td width="10" align="center" valign="middle">&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td>&nbsp;</td>
    <td align="center" valign="middle">
        <script src='../js/main.js' type='text/javascript'></script>
        <script src='../js/check.js' type='text/javascript'></script>
        <script src='../js/meizzDate.js' type='text/javascript'></script>
        <script src='../js/checkID2.js' type='text/javascript'></script>
        <script src='../js/pagebutton.js' type='text/javascript'></script>


            <OBJECT id=wb height=0 width=0 classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 name=wb></OBJECT>
        <%--<form id='winform' method='post' action='./application_save.jsp'>  --%>
            <%--<input type='hidden' name='Plat_Form_Request_Instance_ID' value='2'>--%>
            <%--<input type='hidden' name='Plat_Form_Request_Event_ID' value=''>--%>
            <%--<input type='hidden' name='Plat_Form_Request_Event_Value' value='12'>--%>
            <%--<input type='hidden' name='Plat_Form_Request_Button_Event' value=''>--%>
        <%--</form>--%>    </td>
    <td>&nbsp;</td>
</tr>
</table>

<%--print div--%>                   
</div></td>
</tr>


<tr height="35" align="center" valign="middle">
    <td align="center">
        <table border="0" cellspacing="0" cellpadding="0" width="214" class="Noprint">
            <tr class='page_form_tr'>
                <td width="246" align="center" nowrap>
                    <table class='page_button_tbl'>
                        <tr class='page_button_tbl_tr'>
                            <td class='page_button_tbl_td'><input type='button' class='page_button_active' name='button'
                                                                  onclick="javascript:doPrint('printdiv')"
                                                                  value=" �� ӡ "></td>
                            <td class='page_button_tbl_td'><input type='button' class='page_button_active'
                                                                  name='button'
                                                                  value=' �� �� ' onClick="<%=closeClick%>"></td>
                        </tr>
                </table>                </td>
            </tr>
        </table>    </td>
</tr>
</table></td>
</tr>
</table>
</body>
</html>


<%}%>


