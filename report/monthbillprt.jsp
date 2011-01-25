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
    <title>分期付款月帐单打印</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
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
<table width="80%" height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#816A82" bgcolor="#E0E0D3">



<tr align="center" bordercolor="#FFFFFF" bgcolor="#CCCCCC">
<td width="100%" height="260" valign="middle">

<div id="printdiv" class="mainBox">


<table height="100%" border="0" cellpadding="0" cellspacing="0" id="printtable">

<tr class='page_form_tr'>
<td width="7">&nbsp;</td>
<td width="598" align="center" valign="middle">
<div align="left">
<p align="left" class="mytext" style='line-height:150%'><span class="mytext_bold" style="line-height:150%">尊敬的<%=info.getClientname()%>先生（小姐）：</span></p>
<p align="left" class="mytext" style='line-height:150%'>　　欢迎您成为海尔集团财务有限责任公司（以下简称：海尔财务公司）的分期付款客户!从现在您就可以体验分期付款带给您的超值服务!若您在分期过程中需要帮助,请致电0532-88939904。<br>
  　　您于<u><%=dateformat.format(info.getAppdate())%></u>向<u><%=info.getPartnername()%>
    </u>申请分期购买<u><%=info.getCommname()%>
      </u>,商品总价款￥<u><span
            style="font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333"><%=info.getCommamt().toString()%></span></u>元,首付款￥<u><span
            style="font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333"><%=info.getReceiveamt().toString()%></span></u>元,分期总金额￥<u><span
            style="font-size:10.5pt;line-height:150%;font-family:Arial;color:#333333"><%=info.getContractamt().toString()%></span></u>元,选择的分期期数:
  <u><%=info.getDuration()%></u>期，您该笔分期适用的手续费率为<u><%=info.getServicecharge().doubleValue() * 1000%>
    </u>‰，您该笔分期对应合同编号为：<u><%=info.getContractno()%>
      </u> 。<br>
  　　根据您申请材料中提报的信息，您约定的还款方式为通过 <u><%=StringUtils.trimToEmpty(info.getWithholdbankname())%>
    </u>代扣还款，还款账户为：<u><%=info.getPaybackact()%>
    </u>，我们将按时从您还款账号中扣收相应金额，下面是您的分期还款计划,请在您上述还款账户中确保每期存有足额资金，以保证您及时还款 。</p>
<div align="center" class="mytext_bold">
  <table width="549" border="1" cellspacing="0" bordercolor="#333333" class="page_form_table" style='font-size:10.5pt;'>
    <tr class="mytext">
      <th width="49" height="30" scope="col"><span class="STYLE4">期数</span></th>
        <th width="110" height="30" scope="col"><span class="STYLE4">还款日期</span></th>
        <th width="241" height="30" scope="col"><span class="STYLE4">金额(人民币大写)</span></th>
        <th width="129" height="30" scope="col"><span class="STYLE4">金额(人民币小写)</span></th>
      </tr>
    <% for (int i=0; i<pays.length; i++){%>
    <tr class="mytext">
      <td height="20" class="mytext_small"><div align="center" class="STYLE4"> <%=pays[i].getPoano()%> </div></td>
        <td height="20" class="mytext_small"><div align="center" class="STYLE4"> <%=dateformat.format(pays[i].getStartdate())%> </div></td>
        <td height="20" class="mytext_small"><div align="center" class="STYLE4"> <%=Airth.NumToChn(df.format(pays[i].getCpaamt().doubleValue()))%> </div></td>
        <td height="20" class="mytext_small"><div align="center" class="STYLE4"> <%="￥" + df.format(pays[i].getCpaamt().doubleValue())%> </div></td>
      </tr>
    <%}%>
  </table>
</div>
<p class="mytext"><span class="mytext">温馨提示：</span></p>
<div align="left" >
<p>  <span class="STYLE10">★感谢广大客户对海尔集团的长期支持，为保障您的消费安全，提请注意以下事项：<br>
  1.妥善保管个人资料:切勿将个人信息告诉他人。<br>
  2.警惕电话欺诈：我公司主动致电您本人时，除进行必要的核对外，一般不会主动询问其他不必要的资料，如遇可疑情况，烦请致电0532-88939904客服热线通知我公司。<br>
  3.警惕网络诈骗：切勿将自己的个人账户信息等透露给第三方。注意可疑电子邮件，不要回复或提供个人账户信息。</span></p>
</div>  
  
<p align="right"> <span class="mytext_footer">海尔集团财务有限责任公司</span></p>
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


