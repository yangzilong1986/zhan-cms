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
        session.setAttribute("msg", "合同号有误！");
        response.sendRedirect("../showinfo.jsp");
    } else {
        request.setCharacterEncoding("GBK");
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

        String strGoUrl = request.getParameter("goUrl");      //关闭页面标志，弹出页面关闭，非弹出返回主页面
        String closeClick = "pageWinClose();";
        if (strGoUrl != null) closeClick = "gopage('" + strGoUrl + "')";

        //ConnectionManager manager = ConnectionManager.getInstance();
        String submit = "class='page_button_active'";
        String title = "个人消费分期付款合同";
        String onLoadStr = "";

        XfvcontractprtinfoDao dao = XfvcontractprtinfoDaoFactory.create();
        Xfvcontractprtinfo info = dao.findWhereContractnoEquals(contractno);

        EnumerationBean enum1 = EnumerationType.getEnu("IDType");

        String idtype = (String) enum1.getValue(info.getClientidtype());

        String chnContractamt = Airth.NumToChn(info.getContractamt().toString());
        String chnCommamt = Airth.NumToChn(info.getCommamt().toString());
        String chnReceiveamt = Airth.NumToChn(info.getReceiveamt().toString());

        DateFormat dateformat = new SimpleDateFormat("yyyy年MM月dd日");
        String startdate = dateformat.format(info.getStartdate());

        String phone = StringUtils.trimToEmpty(info.getPhone1()) + ", " + 
                StringUtils.trimToEmpty(info.getPhone2()) + ", " +
                StringUtils.trimToEmpty(info.getPhone3());


        XfcontractpaysDao paysDao = XfcontractpaysDaoFactory.create();
        Xfcontractpays pays[] = paysDao.findWhereContractnoEquals(contractno);
%>
<html>
<head>
    <title>分期付款合同打印</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
    <!--media=print 这个属性说明可以在打印时有效-->
    <!--希望打印时不显示的内容设置class="Noprint"样式-->
    <!--希望人为设置分页的位置设置class="PageNext"样式-->
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
            font-family: "宋 体";
        "Courier New";
        "Times New Roman";
        }

        p.MsoPlainText1 {
            text-align: justify;
            text-justify: inter-ideograph;
            font-size: 12.0pt;
            font-family: "宋 体";
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
    <!--地址的写法很严格的用双斜杠-->
    hkey_path = "\\Software\\Microsoft\\Internet Explorer\\PageSetup";
    //设置网页打印的页眉页脚为空
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
            //alert('需要运行运行Activex才能进行打印设置。');
        }
    }
    //设置网页打印的页眉页脚为默认值
    function pagesetup_default() {
        try {
            var RegWsh = new ActiveXObject("WScript.Shell");
            hkey_key = "\\header";
            //RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&w&b页码,&p/&P");
            RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, hk_pageHead);
            hkey_key = "\\footer";
            //RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&u&b&d");
            RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, hk_pageFoot);
        } catch(e) {
        }
    }
    function printsetup() {
        wb.execwb(8, 1); // 打印页面设置
    }
    function printpreview() {
        wb.execwb(7, 1);// 打印页面预览
    }
    function printit() {
        if (confirm('确定打印吗？')) {
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
                                                                                                   color="#FFFFFF"><b>海尔集团财务有限责任公司个人消费分期付款合同书</b></font>
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
150%;font-family:Arial;color:#333333">个人消费分期付款合同</span></b></div>    </td>
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
color:#333333">编号：</span></div></td>
        <td style="font-size:10.5pt;"><%=info.getContractno()%></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><div align="right"><span style="font-size:10.5pt;line-height:150%;font-family:Arial;
color:#333333">合同签订日期：</span></div></td>
        <td><span style="font-size:10.5pt;line-height:150%;font-family:Arial;
color:#333333"><%=startdate%></span></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><div align="right"><span
                            style="font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333">合同签订地：</span> </div></td>
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
150%;font-family:Arial'>甲方（分期付款人）：<%=info.getClientname()%><span lang="EN-US" xml:lang="EN-US"> </span></span></b>        </p>
      <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>身份证件名称：<span lang="EN-US"
                                                                                                  xml:lang="EN-US">&nbsp;<span style="font-size:10.5pt;font-family:Arial;
color:#333333"><%=idtype%></span> &nbsp;&nbsp;</span>号码：</span><span style="font-size:10.5pt;font-family:Arial;
color:#333333"><%=info.getClientid()%></span></p>

        <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>联系电话：<span lang="EN-US"
                                                                                             xml:lang="EN-US">&nbsp; </span></span>
          <span style="font-size:10.5pt;font-family:Arial;color:#333333"><%=phone%></span></p>

        <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>邮政编码：<span lang="EN-US"
                                                                                             xml:lang="EN-US">&nbsp;&nbsp; </span></span>
          <span style="font-size:10.5pt;font-family:Arial;color:#333333"><%=(info.getPc()==null? "": info.getPc())%></span></p>
        <p class="abc">&nbsp;</p>
        <p class="MsoPlainText1"><b><span
                style='font-size:10.5pt;font-family:Arial'>乙方（放款人）：海尔集团财务有限责任公司<span
                lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

        <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>住所：<span lang="EN-US"
                                                                                           xml:lang="EN-US">&nbsp; </span>青岛市高科园海尔路海尔园 </span></p>

        <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>联系电话：<span lang="EN-US"
                                                                                             xml:lang="EN-US">&nbsp; 0532-88939904 </span></span> </p>

        <p class="abc"><span class="abc" style='font-size:10.5pt;font-family:Arial'>邮政编码：<span lang="EN-US"
                                                                                             xml:lang="EN-US">&nbsp; 266101 </span></span> </p>

        <h3 align="left">&nbsp;</h3></td>
    <td>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
<td>&nbsp;</td>
<td align="center" valign="middle">
<div align="left">
<p align="left" style='line-height:150%'><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　甲方向<u><span lang="EN-US" xml:lang="EN-US">&nbsp;

    <%=info.getPartnername()%>

    &nbsp; </span></u><span
        lang="EN-US" xml:lang="EN-US">(</span>售货单位<span lang="EN-US" xml:lang="EN-US">)</span>购</span><span
        style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>买<u><span
        lang="EN-US" xml:lang="EN-US">
    &nbsp;

<%=info.getCommname()%>

    &nbsp;
</span></u>（具体名称和数量） ，向乙方申请分期付款。经审查，乙方同意向甲方提供本合同项下分期付款总金额，经双方协商一致，签订本合同，以兹共同遵照执行。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><b><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　分期付款金额及支付<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　一、产品价款人民币（大写）<u><span lang="EN-US"
                                                              xml:lang="EN-US"> </span></u><u><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US"> &nbsp; <%=chnCommamt%> &nbsp; </span></u>，其中：已交首付款人民币（大写）<u><span lang="EN-US"
                                                              xml:lang="EN-US"> </span></u><u><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US"> &nbsp; <%=chnReceiveamt%> &nbsp; </span></u>， 分期付款总金额人民币<span lang="EN-US" xml:lang="EN-US">(</span>大写<span
        lang="EN-US" xml:lang="EN-US">)</span></span><u><span lang="EN-US"
                                                              xml:lang="EN-US"> </span></u><u><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US">
    &nbsp;
<%=chnContractamt%>
    &nbsp;

</span></u><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　二、甲方在此委托乙方，在办妥全部手续之日起的<span lang="EN-US" xml:lang="EN-US">3</span>个营业日内将上述款项全数以甲方购买耐用消费品名义划入商品销售单位在乙方开立的账户。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><b><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　款项用途<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>　　三、款项专项用于《分期付款申请书》<span
        lang="EN-US" xml:lang="EN-US">(</span>编号为<u><span lang="EN-US" xml:lang="EN-US">&nbsp;

<%=info.getAppno()%> 

    &nbsp; </span></u><span
        lang="EN-US" xml:lang="EN-US">)</span>所载购买公司所售耐用消费品。<span lang="EN-US"
                                                                  xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><b><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial'>　　声明与保证<span
        lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial'>　　甲、乙双方向对方申明并保证，在本合同签订时具有完全的权利和能力签订本合同。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><b><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　手续费率<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>　　四、本合同每月分期付款手续费率为分期付款总额的<u><span
        lang="EN-US" xml:lang="EN-US">
    &nbsp;

<%=info.getServicecharge().doubleValue()*100%>
    &nbsp;
</span></u></span><span style="font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333">%</span><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>。 </span></p>

<p align="left" style='line-height:150%'><b><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>　　款项偿还<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>　　五、本合同项下的分期付款本金及手续费，采取按月等额还款方式，分<u> <span lang="EN-US"
                                                                                             xml:lang="EN-US">
    &nbsp;

<%=info.getDuration()%>
    &nbsp;
</span></u>期归还，甲方授权乙方在放款发生的次月起每月十三日从甲方在乙方指定银行开立的还款账户扣收，直至所有分期付款本金、费用清偿为止。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>　　六、放款日期以放款凭证记载日期为准<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style='line-height:150%'><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>　　七、还款时间表</span></p>


    <blockquote>
        <table width="549" border="1" cellspacing="0" bordercolor="#333333" class="page_form_table" style='font-size:10.5pt;'>
          <tr>
            <th width="49" height="30" scope="col"><span class="STYLE4">期数</span></th>
            <th width="110" height="30" scope="col"><span class="STYLE4">还款日期</span></th>
            <th width="241" height="30" scope="col"><span class="STYLE4">金额(人民币大写)</span></th>
            <th width="129" height="30" scope="col"><span class="STYLE4">金额(人民币小写)</span></th>
          </tr>

          <% for (int i=0; i<pays.length; i++){%>
          <tr>
            <td height="30"><div align="center" class="STYLE4"> <%=pays[i].getPoano()%>  </div></td>
            <td height="30"><div align="center" class="STYLE4"> <%=dateformat.format(pays[i].getStartdate())%> </div></td>
            <td height="30"><div align="center" class="STYLE4"> <%=Airth.NumToChn(df.format(pays[i].getCpaamt().doubleValue()))%> </div></td>
             <td height="30"><div align="center" class="STYLE4"> <%="￥" + df.format(pays[i].getCpaamt().doubleValue())%> </div></td>
          </tr>
          <%}%>  
        </table>
    </blockquote>




        <p align="left" class="MsoPlainText" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;font-family:
Arial;color:#333333'>　　八、</span><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;'>乙方指定的代扣款的机构名称为<u><span
        lang="EN-US" xml:lang="EN-US">&nbsp;
          
          <%=StringUtils.trimToEmpty(info.getWithholdbankname())%>
          
          &nbsp;</span></u><span lang="EN-US"
                                                                         xml:lang="EN-US"> </span></span></p>
        <p align="left" class="MsoPlainText" style="line-height:150%">
        <span style='font-size:10.5pt;line-height:150%;font-family:Arial;'>　　　　甲方在指定还款银行开立的账户名称：<u><span lang="EN-US" xml:lang="EN-US">&nbsp;
          
          <%=info.getPaybackactname()%>
          
          &nbsp;</span></u></span><span style="font-size:10.5pt;
line-height:150%;font-family:Arial;"> 帐号: <u><span lang="EN-US" xml:lang="EN-US">&nbsp;<%=info.getPaybackact()%></span></u></span></p>
        <p align="left" class="MsoPlainText" style="line-height:150%"><span
        style='font-size:9.0pt;line-height:150%;font-family:Arial;'
        lang="EN-US" xml:lang="EN-US"> </span></p>
        <p align="left" style="text-align:justify;text-justify:inter-ideograph;line-height:150%"><b><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>　<span
        lang="EN-US" xml:lang="EN-US">&nbsp;&nbsp;</span></span></b><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>九、甲方可以提前还款：<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US">　　　(</span><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>一<span
        lang="EN-US" xml:lang="EN-US">)</span>甲方提前归还未到期应付款项的，应至少提前三个工作日书面通知乙方，该书面通知送达乙方处即为不可撤消。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US">　　　(</span><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>二<span
        lang="EN-US" xml:lang="EN-US">)</span>甲方经乙方同意可一次性提前归还全部积欠金额。乙方按未还款金额加收<span lang="EN-US"
                                                                                    xml:lang="EN-US">1%</span>的账户管理费，不退还或减免按原合同规定已收取的手续费。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　十、有下列情况之一项或几项发生时，乙方有权要求甲方提前归还全部分期付款本金及手续费，甲方无条件放弃抗辩权：<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　　<span lang="EN-US" xml:lang="EN-US">(</span>一<span
        lang="EN-US" xml:lang="EN-US">)</span>甲方违反本合同之任何责任条款。<span lang="EN-US"
                                                                   xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　　<span lang="EN-US" xml:lang="EN-US">(</span>二<span
        lang="EN-US" xml:lang="EN-US">)</span>甲方发生因不能履行本合同义务之疾病、事故、死亡等和担保人发生因不能履行本合同义务之合并、重组、解散、破产等影响甲方、担保人完全民事行为能力与责任能力之情况。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　　<span lang="EN-US" xml:lang="EN-US">(</span>三<span
        lang="EN-US"
        xml:lang="EN-US">)</span>甲方或担保人涉入诉讼、监管等由国家行政或司法机关宣布的对其财产的没收及其处分权的限制，或存在该种情况发生的可能的威胁。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333' lang="EN-US"
        xml:lang="EN-US">　　　(</span><span
        style='font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333'>四<span
        lang="EN-US" xml:lang="EN-US">)</span>甲方与耐用消费品销售单位发生退回全部商品之情况。<span lang="EN-US"
                                                                            xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><b><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　合同公证<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　十一、乙方和甲方在本合同签订后，乙方认为必要时，在乙方指定的公证机关办理具有强制执行效力的分期付款合同公证，如甲方不履行还款义务，且累计三个月未能按期如数还款的，乙方有权向有管辖权的人民法院申请强制执行，甲方自愿接受执行，于此情况下不再适用本合同第十条规定。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　十二、办理本合同的公证费用由甲方负担。<span lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><b><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　违约责任<span lang="EN-US" xml:lang="EN-US"> </span></span></b></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　十三、甲方未按期偿还分期付款本金及手续费的，乙方对其逾期金额和拖欠天数按日万分之五计收违约金，按逾期金额<span
        lang="EN-US" xml:lang="EN-US">5%</span>计收滞纳金，最少人民币<span lang="EN-US"
                                                                xml:lang="EN-US">10</span>元。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>

<p align="left" style="line-height:150%"><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'> 　　十四、甲方连续三个月未偿还分期付款本金及手续费，并且担保人未代甲方履行偿还欠款义务的，乙方有权终止本合同，并向甲方、担保人追偿，或依法处分抵押<span
        lang="EN-US" xml:lang="EN-US">(</span>质<span lang="EN-US" xml:lang="EN-US">)</span>物。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　十五、甲方申请分期付款款时提供的资料不实或未经乙方书面同意，擅自将抵押<span
        lang="EN-US" xml:lang="EN-US">(</span>质<span lang="EN-US" xml:lang="EN-US">)</span>物出售、出租、出售出借、转让、交换、赠予、再抵押或以其它方式处置抵押<span
        lang="EN-US" xml:lang="EN-US">(</span>质<span lang="EN-US" xml:lang="EN-US">)</span>物的，均属违约，乙方有权提前收回分期付款本金和手续费或处置抵押<span
        lang="EN-US" xml:lang="EN-US">(</span>质<span lang="EN-US" xml:lang="EN-US">)</span>物，并有权向甲方或担保人追索由此造成的损失和发生的相关费用。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>　　十六、与耐用消费品销售单位因产品质量等原因发生纠纷时，不得以此为理由不归还分期付款本金及手续费。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;
line-height:150%;font-family:Arial;color:#333333'>　　十七、乙方未按合同约定及时足额放款，影响甲方按本合同规定使用款项，乙方应对未发放的金额和违约天数按本合同第十三条约定的违约金率向甲方支付违约金。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　<b>合同纠纷的处理<span
        lang="EN-US" xml:lang="EN-US"> </span></b></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　十八、本合同履行期间如有争议，双方先协商解决。协商不成的，应向乙方所在地的人民法院提起诉讼。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　<b>附则<span
        lang="EN-US" xml:lang="EN-US"> </span></b></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　十九、本合同及其附件的任何修改、补充均须经双方协商一致并订立书面的协议方为有效。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left" style="line-height:150%"><span style='font-size:10.5pt;line-height:150%;
font-family:Arial;color:#333333'>　　二十、本合同经乙方法定代表人或其授权代表签章并加盖公章，甲方签名后与贷款担保合同一并生效，至甲方将本合同项下全部应付款项清偿时终止。<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p align="left">　　<span style='font-size:10.5pt;font-family:Arial;color:#333333'>二十一、本合同正本一式 <u>叁 </u>份，甲方执<u>
          壹 </u>份，乙方执<u> 贰 </u>份，具有同等的法律效力。<span lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p class="MsoPlainText">&nbsp;</p>
        <p class="MsoPlainText">&nbsp;</p>
        <p class="MsoPlainText"><span style='font-size:10.5pt;font-family:Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;甲方（签字）<span lang="EN-US"
                                                                                        xml:lang="EN-US">　　　　&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;乙方（公章）：<span
        lang="EN-US" xml:lang="EN-US"> </span></span></p>
        <p class="MsoPlainText"><span style='font-size:10.5pt;font-family:Arial'>　　　授权代理人（签字）：<span lang="EN-US"
                                                                                            xml:lang="EN-US">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>　　　　法定代表人（或授权代理人）签章：<span
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
                                                                  value=" 打 印 "></td>
                            <td class='page_button_tbl_td'><input type='button' class='page_button_active'
                                                                  name='button'
                                                                  value=' 关 闭 ' onClick="<%=closeClick%>"></td>
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


