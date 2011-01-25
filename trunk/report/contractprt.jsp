<%@ page contentType="text/html; charset=GBK" %>
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
<%
    request.setCharacterEncoding("GBK");

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
    <title>���ڸ����ͬ��ӡ</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
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
        p.MsoPlainText {
            text-align: justify;
            text-justify: inter-ideograph;
            font-size: 12.0pt;
            font-family: "�� ��";
        "Courier New";
        "Times New Roman";
        }

        p.MsoPlainText1 {
            text-align: justify;
            text-justify: inter-ideograph;
            font-size: 12.0pt;
            font-family: "�� ��";
        "Courier New";
        "Times New Roman";
        }
mynormal {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	line-height: 2px;
}
.abc {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	line-height: 1px;
}
.STYLE4 {font-size:10.5pt; }

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
<table width="60%" height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#816A82" bgcolor="#E0E0D3">
<tr align="left">
    <td width=100% height="30" bgcolor="#A4AEB5"><img src="../images/form/xing1.jpg" align="absmiddle"> <font size="2"
                                                                                                   color="#FFFFFF"><b>�������Ų����������ι�˾�������ѷ��ڸ����ͬ��</b></font>
        <img src="../images/form/xing1.jpg" align="absmiddle"></td>
</tr>


<tr align="center">
<td height="260" valign="middle">

<div id="printdiv">


<table id="printtable" height="100%" cellspacing="0" cellpadding="0" border="0">

<tr class='page_form_tr'>
    <td width="7" height="58">&nbsp;</td>
    <td width="522" align="center" valign="middle">
        <div align="center"><b><span style="font-size:14.0pt;line-height:
150%;font-family:Arial;color:#333333">�������ѷ��ڸ����ͬ</span></b></div>    </td>
    <td width="56">&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td>&nbsp;</td>
    <td align="center" valign="middle"><table width=94% border="0">
      <tr>
        <td width="4">&nbsp;</td>
        <td width="388">&nbsp;</td>
        <td width="189">&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><div align="right"><span style="font-size:10.5pt;line-height:150%;font-family:Arial;
color:#333333">��ţ�</span></div></td>
        <td style="font-size:10.5pt;"><%=info.getContractno()%></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><div align="right"><span style="font-size:10.5pt;line-height:150%;font-family:Arial;
color:#333333">��ͬǩ�����ڣ�</span></div></td>
        <td><span style="font-size:10.5pt;line-height:150%;font-family:Arial;
color:#333333"><%=startdate%></span></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><div align="right"><span
                            style="font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333">��ͬǩ���أ�</span> </div></td>
        <td><span
                        style="font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333"><%=(info.getPlace()==null? "": info.getPlace())%></span> </td>
      </tr>
    </table></td>
    <td>&nbsp;</td>
</tr>

<tr class='page_form_tr'>
    <td>&nbsp;</td>
    <td align="left" valign="middle"><p class="MsoPlainText1" style='line-height:150%'>&nbsp;</p>
      <p class="MsoPlainText1" style='line-height:150%'><b><span style='font-size:10.5pt;line-height:
150%;font-family:Arial'>�׷������ڸ����ˣ���<%=info.getClientname()%><span lang="EN-US" xml:lang="EN-US"> </span></span></b>        </p>
      <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>���֤�����ƣ�<span lang="EN-US"
                                                                                                  xml:lang="EN-US">&nbsp;<span style="font-size:10.5pt;font-family:Arial;
color:#333333"><%=idtype%></span> &nbsp;&nbsp;</span>���룺</span><span style="font-size:10.5pt;font-family:Arial;
color:#333333"><%=info.getClientid()%></span></p>

        <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>��ϵ�绰��<span lang="EN-US"
                                                                                             xml:lang="EN-US">&nbsp; </span></span>
          <span style="font-size:10.5pt;font-family:Arial;color:#333333"><%=phone%></span></p>

        <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>�������룺<span lang="EN-US"
                                                                                             xml:lang="EN-US">&nbsp;&nbsp; </span></span>
          <span style="font-size:10.5pt;font-family:Arial;color:#333333"><%=(info.getPc()==null? "": info.getPc())%></span></p>
        <p class="abc">&nbsp;</p>
        <p class="MsoPlainText1"><b><span
                style='font-size:10.5pt;font-family:Arial'>�ҷ����ſ��ˣ����������Ų����������ι�˾<span
                lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

        <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>ס����<span lang="EN-US"
                                                                                           xml:lang="EN-US">&nbsp; </span>�ൺ�и߿�԰����·����԰ </span></p>

        <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>��ϵ�绰��<span lang="EN-US"
                                                                                             xml:lang="EN-US">&nbsp; 0532-88939904 </span></span> </p>

        <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>�������룺<span lang="EN-US"
                                                                                             xml:lang="EN-US">&nbsp; 266101 </span></span> </p>

        <h3 align="left">&nbsp;</h3></td>
    <td>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
<td>&nbsp;</td>
<td align="center" valign="middle">
<div align="left">
<p align="left" style='line-height:150%'><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>�����׷���<u><span lang="EN-US" xml:lang="EN-US">&nbsp;

    <%=info.getPartnername()%>

    &nbsp; </span></u><span
        lang="EN-US" xml:lang="EN-US">(</span>�ۻ���λ<span lang="EN-US" xml:lang="EN-US">)</span>��</span><span
        style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>��<u><span
        lang="EN-US" xml:lang="EN-US">
    &nbsp;

<%=info.getCommname()%>

    &nbsp;
</span></u>���������ƺ������� �����ҷ�������ڸ������飬�ҷ�ͬ����׷��ṩ����ͬ���·��ڸ����ܽ���˫��Э��һ�£�ǩ������ͬ�����ȹ�ͬ����ִ�С�<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><b><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>�������ڸ����֧��<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����һ����Ʒ�ۿ�����ң���д��<u><span lang="EN-US"
                                                              xml:lang="EN-US"> </span></u><u><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US"> &nbsp; <%=chnCommamt%> &nbsp; </span></u>�����У��ѽ��׸�������ң���д��<u><span lang="EN-US"
                                                              xml:lang="EN-US"> </span></u><u><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US"> &nbsp; <%=chnReceiveamt%> &nbsp; </span></u>�� ���ڸ����ܽ�������<span lang="EN-US" xml:lang="EN-US">(</span>��д<span
        lang="EN-US" xml:lang="EN-US">)</span></span><u><span lang="EN-US"
                                                              xml:lang="EN-US"> </span></u><u><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US">
    &nbsp;
<%=chnContractamt%>
    &nbsp;

</span></u><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>��<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>���������׷��ڴ�ί���ҷ����ڰ���ȫ������֮�����<span lang="EN-US" xml:lang="EN-US">3</span>��Ӫҵ���ڽ���������ȫ���Լ׷�������������Ʒ���廮����Ʒ���۵�λ���ҷ��������˻���<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><b><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����������;<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>������������ר�����ڡ����ڸ��������顷<span
        lang="EN-US" xml:lang="EN-US">(</span>���Ϊ<u><span lang="EN-US" xml:lang="EN-US">&nbsp;

<%=info.getAppno()%> 

    &nbsp; </span></u><span
        lang="EN-US" xml:lang="EN-US">)</span>���ع���˾������������Ʒ��<span lang="EN-US"
                                                                  xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><b><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial'>���������뱣֤<span
        lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial'>�����ס���˫����Է���������֤���ڱ���ͬǩ��ʱ������ȫ��Ȩ��������ǩ������ͬ��<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><b><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>������������<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>�����ġ�����ͬÿ�·��ڸ�����������Ϊ���ڸ����ܶ��<u><span
        lang="EN-US" xml:lang="EN-US">
    &nbsp;

<%=info.getServicecharge().doubleValue()*100%>
    &nbsp;
</span></u></span><span style="font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333">%</span><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>�� </span></p>

<p align="left" style='line-height:150%'><b><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>���������<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>�����塢����ͬ���µķ��ڸ���������ѣ���ȡ���µȶ�ʽ����<u> <span lang="EN-US"
                                                                                             xml:lang="EN-US">
    &nbsp;

<%=info.getDuration()%>
    &nbsp;
</span></u>�ڹ黹���׷���Ȩ�ҷ��ڷſ���Ĵ�����ÿ��ʮ���մӼ׷����ҷ�ָ�����п����Ļ����˻����գ�ֱ�����з��ڸ���𡢷����峥Ϊֹ��<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>���������ſ������Էſ�ƾ֤��������Ϊ׼<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>�����ߡ�����ʱ���</span></p>


    <blockquote>
        <table width="549" border="1" cellspacing="0" bordercolor="#333333" class="page_form_table" style='font-size:10.5pt;'>
          <tr>
            <th width="49" height="30" scope="col"><span class="STYLE4">����</span></th>
            <th width="110" height="30" scope="col"><span class="STYLE4">��������</span></th>
            <th width="241" height="30" scope="col"><span class="STYLE4">���(����Ҵ�д)</span></th>
            <th width="129" height="30" scope="col"><span class="STYLE4">���(�����Сд)</span></th>
          </tr>

          <% for (int i=0; i<pays.length; i++){%>
          <tr>
            <td height="30"><div align="center" class="STYLE4"> <%=pays[i].getPoano()%>  </div></td>
            <td height="30"><div align="center" class="STYLE4"> <%=dateformat.format(pays[i].getStartdate())%> </div></td>
            <td height="30"><div align="center" class="STYLE4"> <%=Airth.NumToChn(df.format(pays[i].getCpaamt().doubleValue()))%> </div></td>
             <td height="30"><div align="center" class="STYLE4"> <%="��" + df.format(pays[i].getCpaamt().doubleValue())%> </div></td>
          </tr>
          <%}%>  
        </table>
    </blockquote>




        <p align="left" class="MsoPlainText" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;font-family:
Arial;color:#333333'>�����ˡ�</span><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;'>�ҷ�ָ���Ĵ��ۿ�Ļ�������Ϊ<u><span
        lang="EN-US" xml:lang="EN-US">&nbsp;
          
          <%=StringUtils.trimToEmpty(info.getWithholdbankname())%>
          
          &nbsp;</span></u><span lang="EN-US"
                                                                         xml:lang="EN-US"> </span></span></p>
        <p align="left" class="MsoPlainText" style="line-height:150%">
        <span style='font-size:10.5pt;line-height:150%;font-family:Arial;'>���������׷���ָ���������п������˻����ƣ�<u><span lang="EN-US" xml:lang="EN-US">&nbsp;
          
          <%=info.getPaybackactname()%>
          
          &nbsp;</span></u></span><span style="font-size:10.5pt;
line-height:150%;font-family:Arial;"> �ʺ�: <u><span lang="EN-US" xml:lang="EN-US">&nbsp;<%=info.getPaybackact()%></span></u></span></p>
        <p align="left" class="MsoPlainText" style="line-height:150%"><span
        style='font-size:9.0pt;line-height:150%;font-family:Arial;'
        lang="EN-US" xml:lang="EN-US"> </span></p>
        <p align="left" style="text-align:justify;text-justify:inter-ideograph;line-height:150%"><b><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>��<span
        lang="EN-US" xml:lang="EN-US">&nbsp;&nbsp;</span></span></b><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>�š��׷�������ǰ���<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US">������(</span><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>һ<span
        lang="EN-US" xml:lang="EN-US">)</span>�׷���ǰ�黹δ����Ӧ������ģ�Ӧ������ǰ��������������֪ͨ�ҷ���������֪ͨ�ʹ��ҷ�����Ϊ���ɳ�����<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US">������(</span><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>��<span
        lang="EN-US" xml:lang="EN-US">)</span>�׷����ҷ�ͬ���һ������ǰ�黹ȫ����Ƿ���ҷ���δ���������<span lang="EN-US"
                                                                                    xml:lang="EN-US">1%</span>���˻�����ѣ����˻�����ⰴԭ��ͬ�涨����ȡ�������ѡ�<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����ʮ�����������֮һ������ʱ���ҷ���ȨҪ��׷���ǰ�黹ȫ�����ڸ���������ѣ��׷���������������Ȩ��<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>������<span lang="EN-US" xml:lang="EN-US">(</span>һ<span
        lang="EN-US" xml:lang="EN-US">)</span>�׷�Υ������֮ͬ�κ��������<span lang="EN-US"
                                                                   xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>������<span lang="EN-US" xml:lang="EN-US">(</span>��<span
        lang="EN-US" xml:lang="EN-US">)</span>�׷������������б���ͬ����֮�������¹ʡ������Ⱥ͵����˷����������б���ͬ����֮�ϲ������顢��ɢ���Ʋ���Ӱ��׷�����������ȫ������Ϊ��������������֮�����<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>������<span lang="EN-US" xml:lang="EN-US">(</span>��<span
        lang="EN-US"
        xml:lang="EN-US">)</span>�׷��򵣱����������ϡ���ܵ��ɹ���������˾�����������Ķ���Ʋ���û�ռ��䴦��Ȩ�����ƣ�����ڸ�����������Ŀ��ܵ���в��<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US">������(</span><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>��<span
        lang="EN-US" xml:lang="EN-US">)</span>�׷�����������Ʒ���۵�λ�����˻�ȫ����Ʒ֮�����<span lang="EN-US"
                                                                            xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><b><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>������ͬ��֤<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����ʮһ���ҷ��ͼ׷��ڱ���ͬǩ�����ҷ���Ϊ��Ҫʱ�����ҷ�ָ���Ĺ�֤���ذ������ǿ��ִ��Ч���ķ��ڸ����ͬ��֤����׷������л����������ۼ�������δ�ܰ�����������ģ��ҷ���Ȩ���й�ϽȨ������Ժ����ǿ��ִ�У��׷���Ը����ִ�У��ڴ�����²������ñ���ͬ��ʮ���涨��<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����ʮ����������ͬ�Ĺ�֤�����ɼ׷�������<span lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><b><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����ΥԼ����<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����ʮ�����׷�δ���ڳ������ڸ���������ѵģ��ҷ��������ڽ�����Ƿ�����������֮�����ΥԼ�𣬰����ڽ��<span
        lang="EN-US" xml:lang="EN-US">5%</span>�������ɽ����������<span lang="EN-US"
                                                                xml:lang="EN-US">10</span>Ԫ��<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style="line-height:150%"><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'> ����ʮ�ġ��׷�����������δ�������ڸ���������ѣ����ҵ�����δ���׷����г���Ƿ������ģ��ҷ���Ȩ��ֹ����ͬ������׷���������׷�������������ֵ�Ѻ<span
        lang="EN-US" xml:lang="EN-US">(</span>��<span lang="EN-US" xml:lang="EN-US">)</span>�<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����ʮ�塢�׷�������ڸ����ʱ�ṩ�����ϲ�ʵ��δ���ҷ�����ͬ�⣬���Խ���Ѻ<span
        lang="EN-US" xml:lang="EN-US">(</span>��<span lang="EN-US" xml:lang="EN-US">)</span>����ۡ����⡢���۳��衢ת�á����������衢�ٵ�Ѻ����������ʽ���õ�Ѻ<span
        lang="EN-US" xml:lang="EN-US">(</span>��<span lang="EN-US" xml:lang="EN-US">)</span>��ģ�����ΥԼ���ҷ���Ȩ��ǰ�ջط��ڸ����������ѻ��õ�Ѻ<span
        lang="EN-US" xml:lang="EN-US">(</span>��<span lang="EN-US" xml:lang="EN-US">)</span>�����Ȩ��׷��򵣱���׷���ɴ���ɵ���ʧ�ͷ�������ط��á�<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>����ʮ��������������Ʒ���۵�λ���Ʒ������ԭ��������ʱ�������Դ�Ϊ���ɲ��黹���ڸ���������ѡ�<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>����ʮ�ߡ��ҷ�δ����ͬԼ����ʱ���ſӰ��׷�������ͬ�涨ʹ�ÿ���ҷ�Ӧ��δ���ŵĽ���ΥԼ����������ͬ��ʮ����Լ����ΥԼ������׷�֧��ΥԼ��<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����<b>��ͬ���׵Ĵ���<span
        lang="EN-US" xml:lang="EN-US"> </span></b></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����ʮ�ˡ�����ͬ�����ڼ��������飬˫����Э�̽����Э�̲��ɵģ�Ӧ���ҷ����ڵص�����Ժ�������ϡ�<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����<b>����<span
        lang="EN-US" xml:lang="EN-US"> </span></b></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>����ʮ�š�����ͬ���丽�����κ��޸ġ�������뾭˫��Э��һ�²����������Э�鷽Ϊ��Ч��<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>������ʮ������ͬ���ҷ����������˻�����Ȩ����ǩ�²��Ӹǹ��£��׷�ǩ������������ͬһ����Ч�����׷�������ͬ����ȫ��Ӧ�������峥ʱ��ֹ��<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left">����<span style='font-size:10.5pt;font-family:Arial;color:#333333'>��ʮһ������ͬ����һʽ <u>�� </u>�ݣ��׷�ִ<u>
          Ҽ </u>�ݣ��ҷ�ִ<u> �� </u>�ݣ�����ͬ�ȵķ���Ч����<span lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p class="MsoPlainText">&nbsp;</p>
        <p class="MsoPlainText">&nbsp;</p>
        <p class="MsoPlainText"><span style='font-size:10.5pt;font-family:Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�׷���ǩ�֣�<span lang="EN-US"
                                                                                        xml:lang="EN-US">��������&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�ҷ������£���<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p class="MsoPlainText"><span style='font-size:10.5pt;font-family:Arial'>��������Ȩ�����ˣ�ǩ�֣���<span lang="EN-US"
                                                                                            xml:lang="EN-US">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>�����������������ˣ�����Ȩ�����ˣ�ǩ�£�<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p class="MsoPlainText">&nbsp;</p>
        <p class="MsoPlainText">&nbsp;</p>
</div></td>
<td align="center" valign="middle">&nbsp;</td>
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


