<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Vector" %>
<%

    request.setCharacterEncoding("GBK");
//    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (um == null) {
//        response.sendRedirect("../error.jsp");
//    }

    String IDTYPE = request.getParameter("IDTYPE");            //证件类型
    String ID = request.getParameter("ID");                     //证件号码
    String PASSWORD = request.getParameter("PASSWORD");        //密码

    String NAME = "";       //姓名
    String APPNO = "";      //申请单号
    String APPDATE = "";    //申请日期
    String APPTYPE = "";    //申请类型
    String COMMNAME = "";   //商品名称
    String APPSTATUS = "";  //申请状态

    IDTYPE = (IDTYPE == null) ? "" : IDTYPE;
    ID = (ID == null) ? "" : ID;
    NAME = (NAME == null) ? "" : NAME.trim();
    PASSWORD = (PASSWORD == null) ? "" : PASSWORD.trim();

    ConnectionManager manager = ConnectionManager.getInstance();
    String sql = "SELECT * from cmindvclient WHERE IDTYPE='" + IDTYPE + "' AND ID='" + ID + "' AND PASSWORD='" + PASSWORD + "'";
    if (manager.getRs(sql).size() == 0) {
        //根据证件确定最近登记的客户资料
        sql = " select CLIENTNO,NAME,APPDATE from CMINDVCLIENT where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and rownum=1  order by CLIENTNO desc";
        String err = "";
        if (manager.getRs(sql).size() > 0) err = "您输入的密码有误！";
        else err = "您还没有注册，请先注册！";
        request.setAttribute("err", err);
%>
<jsp:forward page="./apperror.jsp"/>
<%
} else {

    sql = "SELECT NAME, IDTYPE, ID, A.APPNO, APPSTATUS, APPTYPE, APPDATE, COMMNAME " +
            " from XFAPP A,XFAPPCOMM C WHERE A.APPNO=C.APPNO AND A.IDTYPE='" + IDTYPE + "' AND A.ID='" + ID + "' order by A.APPNO desc";
    String pnStr = request.getParameter("pn");
    if (pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
    int pn = Integer.parseInt(pnStr == null ? "1" : pnStr);
    int ps = 10;
    Vector vec = manager.getPageRs(sql, pn, ps);
    int rows = ((Integer) vec.get(0)).intValue();
    CachedRowSet crs = (CachedRowSet) vec.get(1);
    int count = crs.size();
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

    String SID = (String) session.getAttribute("SID");//合作商城编号
    SID = (SID == null) ? "" : SID;
    if (count == 0 || !SID.equals("")) {
%>
<jsp:forward page="application_start.jsp?goUrl=../app.jsp"/>
<%
} else {
    if (NAME.equals("") && crs.next()) {
        NAME = crs.getString("NAME");
        crs.beforeFirst();
    }
%>
<html xmlns:m="http://schemas.microsoft.com/office/2004/12/omml" xmlns:v="urn:schemas-microsoft-com:vml"
      xmlns:o="urn:schemas-microsoft-com:office:office">

<head>
    <title>消费分期合约</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        .style1 {
            text-align: left;
        }

        .style2 {
            border-collapse: collapse;
            text-align: justify;
            font-size: 10.0pt;
            font-family: "Times New Roman", serif;
            border: 1.0pt solid windowtext;
        }

        .style3 {
            font-family: "宋体", Courier New, Arial;
            font-size: 12px;
            color: #000000;
            background-color: #EDEBED;
            border: 1px solid #AAAAAA;
        }

        .style4 {
            font-family: 宋 体, Arial, Helvetica, sans-serif;
            color: #666666;
        }

        .style5 {
            border: thin groove #F7F5EE;
            font-family: 宋 体, Arial, Helvetica, sans-serif;
            font-size: 12px;
            color: #FFFFFF;
            background-color: #3E76B3;
            cursor: pointer;
        }

        .style6 {
            font-family: 宋 体, Arial, Helvetica, sans-serif;
            font-weight: bold;
            color: #808080;
        }

        .style7 {
            font-family: 宋 体, Arial, Helvetica, sans-serif;
            font-size: medium;
            color: #337799;
        }

        .style8 {
            font-family: 宋 体, Arial, Helvetica, sans-serif;
            font-size: large;
        }

        .style9 {
            font-size: small;
        }

        .style10 {
            font-size: 9pt;
        }

        -->
    </style>
    <script language="JavaScript" type="text/JavaScript">
        function info(APPNO, NAME, IDTYPE, ID, PASSWORD, APPSTATUS) {
            var url = "./application.jsp?APPNO=" + APPNO + "&NAME=" + NAME + "&IDTYPE=" + IDTYPE + "&ID=" + ID + "&PASSWORD=" + PASSWORD + "&APPSTATUS=" + APPSTATUS + "&showinfo=0";
            window.open(url, 'APPLICATION', 'left=0,top=0,height=700,width=870,toolbar=no,scrollbars=yes,resizable=yes');
        }
        function info1(NAME, IDTYPE, ID, PASSWORD) {
            var url = "./appuser.jsp?NAME=" + NAME + "&IDTYPE=" + IDTYPE + "&ID=" + ID + "&PASSWORD=" + PASSWORD + "&showinfo=0";
            window.open(url, 'APPUSER', 'left=0,top=0,height=700,width=870,toolbar=no,scrollbars=yes,resizable=yes');
        }
    </script>
    <script language="javascript" type="text/JavaScript" src="/js/flippage.js"></script>
</head>

<body background="/images/checks_02.jpg">

<form action="applist.jsp" name="form1" method="post">
<span class="style4">
				<input type="hidden" name="IDTYPE" value="<%=IDTYPE%>">
				<input type="hidden" name="ID" value="<%=ID%>">
				<input type="hidden" name="NAME" value="<%=NAME.trim()%>">
				<input type="hidden" name="PASSWORD" value="<%=PASSWORD%>">
				</span>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
    <tr class="page_form_tr">
        <td align="center" valign="middle">
            <table height="325" width="860" border="2" align="center" cellpadding="2" cellspacing="2"
                   bordercolor="#816A82" bgcolor="#E0E0D3">
                <tr align="left">
                    <td height="30" bgcolor="#A4AEB5"><span class="style4"><img src="../images/form/xing1.jpg"
                                                                                align="absmiddle"></span> <b><span
                            style="font-size: 12.0pt; line-height: 125%; font-family: 宋体">消费分期付款合约内容</span></b> <span
                            class="style4"> <img src="../images/form/xing1.jpg" align="absmiddle"></span></td>
                </tr>
                <tr align="center" class="page_form_tr">
                    <td height="260" valign="middle">
                        <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
                            <tr class="page_form_tr">
                                <td width="20" class="style4">&nbsp;</td>
                                <td align="center" valign="middle" style="width: 838px">
                                    <script src="/js/querybutton.js" type="text/javascript"></script>
                                    <script src="/js/meizzDate.js" type="text/javascript"></script>
                                    <table class="page_form_table" id="page_form_table" width="100%">
                                        <tr class="page_form_tr">
                                            <td height="5" class="style4" style="width: 667px">
                                                <strong>客户信息</strong><font size="2">： <%=NAME%>
                                            </font></td>
                                        </tr>
                                        <tr class="page_form_tr">
                                            <td class="style1" style="width: 667px">
                                                <table class="style3" align="center" cellpadding="0" cellspacing="1"
                                                       border="0" style="width: 100%">
                                                    <tbody class="style1">
                                                    <tr class="list_form_title_tr">
                                                        <td class="style1" nowrap style="width: 860px"
                                                            bgcolor="#FFFFFF">
                                                            <p class="MsoNormal" align="center"><b><span
                                                                    style="line-height: 125%; " class="style8">海尔集团财务有限责任公司消费分期付款合约</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal" align="center"><span lang="EN-US"
                                                                                                      style="font-size: 12.0pt; line-height: 125%; "
                                                                                                      class="style6"><o:p>
                                                                &nbsp;</o:p></span></p>

                                                            <p class="MsoNormal"><b><span style="line-height: 125%; "
                                                                                          class="style4"><span
                                                                    class="style9">海尔产品消费分期付款（以下称“分期付款”）申请人（以下称“甲方”）</span></span></b><span
                                                                    class="style9"></p>

                                                            <p class="MsoNormal"><b><span style="line-height: 125%; "
                                                                                          class="style4">就分期付款的申请和使用等相关事宜与海尔集团财务有限责任公司（以下称“乙方”）</span></b>
                                                            </p>

                                                            <p class="MsoNormal"></span><b><span
                                                                    style="line-height: 125%; " class="style4"><span
                                                                    class="style9">签订如下合约：</span></span><o:p></o:p></b>
                                                            </p>
                                                            <hr>
                                                            <p class="MsoNormal">
                                                                <o:p></o:p></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="line-height: 125%; "
                                                                                       class="style7"><img width="9"
                                                                                                           height="9"
                                                                                                           src="file:///C:/Users/zhanrui/AppData/Local/Temp/msohtmlclip1/01/clip_image001.gif"
                                                                                                           alt="*"></span><span
                                                                    lang="EN-US"
                                                                    style="font-size: 12.0pt; line-height: 125%; font-family: Symbol; "><span
                                                                    style="font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;"
                                                                    class="style7">&nbsp;</span></span><b><span
                                                                    style="line-height: 125%; "
                                                                    class="style7">申 请</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第一条 </span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">甲方保证向乙方提供的所有申请资料真实、完整、准确和合法，同意乙方向有关机构、单位和个人了解甲方的财产及资信状况，并保留相关资料。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">因信贷报告和对风险评估的需要，乙方将把甲方<span
                                                                    lang="EN-US">/</span>甲方的担保人<span
                                                                    lang="EN-US">/</span>甲方的共同还款人的部分或全部资料，提供给中国人民银行个人信用信息基础数据库，并不得向未经中国人民银行批准建立或变相建立的个人信用信息基础数据库提供个人信用信息。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">乙方将在以下业务范围内向中国人民银行个人信用信息基础数据库查询甲方<span
                                                                    lang="EN-US">/</span>甲方的担保人<span
                                                                    lang="EN-US">/</span>甲方的共同还款人的个人信用信息：审核个人消费分期付款申请的；审核个人作为消费分期申请人的担保人的；对个人消费分期申请人风险进行评估管理的。乙方在提供和查询个人信用信息资料过程中，将确保个人信息资料的安全和保密。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">乙方对于甲方的个人信息资料和商业秘密（包括申请表等）将依法予以保密（但依照法律、法规规定或者司法<span
                                                                    lang="EN-US">/</span>行政机关、征信机构的要求而进行查询或做出适当披露的情形除外）。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第二条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 乙方有权依据甲方的资信状况决定是否批准甲方的分期申请。无论是否核准该分期申请，有关资料均不退还。</span><o:p></o:p>
                                                            </p>

                                                            <p class="MsoNormal"><span lang="en-us">&nbsp;</span>&nbsp;
                                                            </p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="line-height: 125%; "
                                                                                       class="style7"><img width="9"
                                                                                                           height="9"
                                                                                                           src="file:///C:/Users/zhanrui/AppData/Local/Temp/msohtmlclip1/01/clip_image001.gif"
                                                                                                           alt="*"></span><span
                                                                    lang="EN-US"
                                                                    style="font-size: 12.0pt; line-height: 125%; font-family: Symbol; "><span
                                                                    style="font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;"
                                                                    class="style7">&nbsp;&nbsp;</span></span><b><span
                                                                    style="line-height: 125%; "
                                                                    class="style7">分期款项发放</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第三条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 甲、乙双方向对方申明并保证，在本合约签订时具有完全的权利和能力签订本合约。 </span> <span
                                                                    lang="EN-US"><o:p></o:p>
																																																</span>
                                                            </p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第四条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 采用分期付款所购商品仅限甲方本人消费使用，不得用于经营销售等商业用途。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第五条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 分期款项专项用于甲方分期付款申请书所载明的公司所售耐用消费品。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第六条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 甲方在此委托乙方，在办妥全部手续之日起的<span
                                                                    lang="EN-US">3</span>个营业日内将上述款项全数以甲方购买耐用消费品名义划入商品销售单位指定的账户。</span><o:p></o:p>
                                                            </p>

                                                            <p class="style10">
                                                                <o:p>&nbsp;</o:p></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 12.0pt; line-height: 125%; "
                                                                                       class="style4"><img width="9"
                                                                                                           height="9"
                                                                                                           src="file:///C:/Users/zhanrui/AppData/Local/Temp/msohtmlclip1/01/clip_image001.gif"
                                                                                                           alt="*"></span><span
                                                                    lang="EN-US"
                                                                    style="font-size: 12.0pt; line-height: 125%; font-family: Symbol; "><span
                                                                    style="font-style: normal; font-variant: normal; font-weight: normal; font-size: 7.0pt; line-height: normal;"
                                                                    class="style4">&nbsp;</span></span><b><span
                                                                    style="line-height: 125%; "
                                                                    class="style7">分期还款说明</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第七条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 本合约项下的分期付款本金及手续费，采取按月等额还款方式，甲方应在放款发生的次月起，按时足额偿还相应款项，直至所有分期付款本金和费用清偿为止。不同还款方式如下：</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (1)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">甲方选择以“委托指定银行代扣”方式还款的，甲方授权乙方在放款发生的次月起，从甲方在指定银行开立的还款账户扣收，直至所有分期付款本金、费用清偿为止。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (2) </span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">甲方选择以“电汇<span lang="EN-US">/</span>网银还款”方式还款的，由甲方在每月约定还款日前向乙方指定的账号汇入相应款项，直至所有分期付款本金、费用清偿为止。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (3)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">甲方选择以“通过第三方支付平台还款”方式还款的，由甲方在每月约定还款日前通过第三方支付平台向乙方指定账号还款，直至所有分期付款本金、费用清偿为止。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"><strong>第八条</strong> 乙方在完成分期付款审批后，以电子邮件、信函、短信、电话等方式通知甲方还款相关事宜。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第九条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 甲方可以提前还款，</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (1)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">甲方提前归还未到期应付款项的，应至少提前三个工作日书面通知乙方，该书面通知送达乙方处即为不可撤消。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (2)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">甲方经乙方同意可一次性提前归还全部积欠金额。乙方按未还款金额加收<span
                                                                    lang="EN-US">1%</span>的账户管理费，不退还或减免按原合约规定已收取的手续费。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第十条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 甲方与商户间发生的交易纠纷应由纠纷双方自行解决，甲方不得以纠纷为由拒绝向乙方偿还因使用分期付款方式引起的债务及相关费用。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第十一条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 有下列情况之一项或几项发生时，乙方有权要求甲方提前归还全部分期付款本金及手续费，甲方无条件放弃抗辩权：</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"><span lang="EN-US">&nbsp;&nbsp;&nbsp; (1)</span>甲方违反本合约之任何责任条款。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"><span lang="EN-US">&nbsp;&nbsp;&nbsp; (2)</span>甲方发生因不能履行本合约义务之疾病、事故、死亡等和担保人发生因不能履行本合约义务之合并、重组、解散、破产等影响甲方、担保人完全民事行为能力与责任能力之情况。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"><span lang="EN-US">&nbsp;&nbsp;&nbsp; (3)</span>甲方或担保人涉入诉讼、监管等由国家行政或司法机关宣布的对其财产的没收及其处分权的限制，或存在该种情况发生的可能的威胁。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (4)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">甲方与耐用消费品销售单位发生退回全部商品之情况。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第十二条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 合约公证</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"><span lang="EN-US">&nbsp;&nbsp;&nbsp; (1)</span>乙方和甲方在本合约签订后，乙方认为必要时，在乙方指定的公证机关办理具有强制执行效力的分期付款合约公证，如甲方不履行还款义务，且累计三个月未能按期如数还款的，乙方有权向有管辖权的人民法院申请强制执行，甲方自愿接受执行，于此情况下不再适用本合约第十一条规定。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (2)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">办理本合约的公证费用由甲方负担。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第十三条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 甲方及联系人的单位地址、住宅地址或电话号码等个人信息如有变更，应立即联系甲方办理资料变更手续，否则甲方需承担由此导致的一切风险和损失。</span><o:p></o:p>
                                                            </p>

                                                            <p class="style10">
                                                                <o:p>&nbsp;</o:p></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="line-height: 125%; "
                                                                                       class="style7"><img width="9"
                                                                                                           height="9"
                                                                                                           src="file:///C:/Users/zhanrui/AppData/Local/Temp/msohtmlclip1/01/clip_image001.gif"
                                                                                                           alt="*"></span><span
                                                                    lang="EN-US"
                                                                    style="font-size: 12.0pt; line-height: 125%; font-family: Symbol; "><span
                                                                    style="font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;"
                                                                    class="style7">&nbsp;</span></span><b><span
                                                                    style="line-height: 125%; "
                                                                    class="style7">费率标准</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第十四条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 消费分期付款业务手续费率标准请参见下表：</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <div align="center" style="width: 453px">
                                                                <table border="1" cellspacing="0" cellpadding="0"
                                                                       style="text-justify: inter-ideograph;"
                                                                       class="style2">
                                                                    <tr>
                                                                        <td width="140" valign="bottom"
                                                                            style="width: 104.95pt; border: solid windowtext 1.0pt; padding: 0cm 5.4pt 0cm 5.4pt">
                                                                            <p class="MsoNormal"><span
                                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                                    class="style4">分期付款期数（月）</span><span
                                                                                    lang="EN-US"><o:p></o:p></span></p>
                                                                        </td>
                                                                        <td width="52" valign="bottom"
                                                                            style="border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; width: 39.3pt; border-left: none; padding: 0cm 5.4pt 0cm 5.4pt">
                                                                            <p class="MsoNormal" align="center"><span
                                                                                    lang="EN-US"
                                                                                    style="font-size: 9.0pt; line-height: 125%; font-family: 宋体">3<o:p></o:p></span>
                                                                            </p>
                                                                        </td>
                                                                        <td width="52" valign="bottom"
                                                                            style="border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; width: 39.3pt; border-left: none; padding: 0cm 5.4pt 0cm 5.4pt">
                                                                            <p class="MsoNormal" align="center"><span
                                                                                    lang="EN-US"
                                                                                    style="font-size: 9.0pt; line-height: 125%; font-family: 宋体">6<o:p></o:p></span>
                                                                            </p>
                                                                        </td>
                                                                        <td width="52" valign="bottom"
                                                                            style="border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; width: 39.35pt; border-left: none; padding: 0cm 5.4pt 0cm 5.4pt">
                                                                            <p class="MsoNormal" align="center"><span
                                                                                    lang="EN-US"
                                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                                    class="style4">12<o:p></o:p></span>
                                                                            </p>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td width="140" valign="bottom"
                                                                            style="border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; width: 104.95pt; border-top: none; padding: 0cm 5.4pt 0cm 5.4pt">
                                                                            <p class="MsoNormal"><span
                                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                                    class="style4">月分期手续费率</span><span
                                                                                    lang="EN-US"><o:p></o:p></span></p>
                                                                        </td>
                                                                        <td width="52" valign="bottom"
                                                                            style="width: 39.3pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; padding: 0cm 5.4pt 0cm 5.4pt">
                                                                            <p class="MsoNormal" align="center"><span
                                                                                    lang="EN-US"
                                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                                    class="style4">1%<o:p></o:p></span>
                                                                            </p>
                                                                        </td>
                                                                        <td width="52" valign="bottom"
                                                                            style="width: 39.3pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; padding: 0cm 5.4pt 0cm 5.4pt">
                                                                            <p class="MsoNormal" align="center"><span
                                                                                    lang="EN-US"
                                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                                    class="style4">0.8%<o:p></o:p></span>
                                                                            </p>
                                                                        </td>
                                                                        <td width="52" valign="bottom"
                                                                            style="width: 39.35pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; padding: 0cm 5.4pt 0cm 5.4pt">
                                                                            <p class="MsoNormal" align="center"><span
                                                                                    lang="EN-US"
                                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                                    class="style4">0.6%<o:p></o:p></span>
                                                                            </p>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">具体手续费费率以乙方公告为准，乙方有权根据相关业务规定及行业惯例调整本合约使用的手续费率。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第十五条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 违约责任</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">　　<span lang="EN-US">(1)</span>甲方未按期偿还分期付款本金及手续费的，乙方对其逾期金额和拖欠天数按日万分之五计收违约金，按逾期金额<span
                                                                    lang="EN-US">5%</span>计收滞纳金，最少人民币<span lang="EN-US">10</span>元。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">　　<span lang="EN-US">(2)</span>甲方连续三个月未偿还分期付款本金及手续费，并且担保人未代甲方履行偿还欠款义务的，乙方有权终止本合约，并向甲方、担保人追偿。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">　　<span lang="EN-US">(3)</span>甲方申请分期付款时提供的资料不实，乙方有权提前收回分期付款本金和手续费，并有权向甲方或担保人追索由此造成的损失和发生的相关费用。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp; (4)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">乙方未按合约约定及时足额放款，影响甲方按本合约规定使用款项，乙方应对未发放的金额和违约天数按本合约第十四条<span
                                                                    lang="EN-US">(1)</span>中约定的违约金率向甲方支付违约金。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp; (5)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">甲方未能按时还款，乙方可采取信函、电话、上门等形式进行催收，并有权扣划甲方在乙方开立的任何账户内的款项或处置甲方的抵、质押物以清偿其欠款。情况严重的，可根据相关规定报请司法机关追究其法律责任，并由甲方承担相关的一切费用。</span><o:p></o:p>
                                                            </p>

                                                            <p class="style10">
                                                                <o:p>&nbsp;</o:p></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="line-height: 125%; "
                                                                                       class="style7"><img width="9"
                                                                                                           height="9"
                                                                                                           src="file:///C:/Users/zhanrui/AppData/Local/Temp/msohtmlclip1/01/clip_image001.gif"
                                                                                                           alt="*"></span><span
                                                                    lang="EN-US"
                                                                    style="font-size: 12.0pt; line-height: 125%; font-family: Symbol; "><span
                                                                    style="font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;"
                                                                    class="style7">&nbsp;&nbsp;</span></span><b><span
                                                                    style="line-height: 125%; " class="style7">附则</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第十六条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 本合约未尽事宜依据乙方相关业务规定及行业惯例办理。如乙方收费项目、标准和手续费率发生修改、变化、调整等，一经金融监管部门批准，乙方对外公布后及时自动生效。本合约相关内容视为相应修改，修改后的条款对甲、乙双方均有约束力。对于前述内容，乙方对外公布的方式为：对账单或信函等。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第十七条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 本合约履行期间如有争议，双方先协商解决。协商不成的，应向乙方所在地的人民法院提起诉讼。</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">第十八条</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> 本合约经甲方在申请表上签字后自乙方批准申请之日起生效，至乙方正式核准甲方销户的次日终止。</span><o:p></o:p>
                                                            </p>

                                                            <p class="style10">
                                                                <o:p>&nbsp;</o:p></p>

                                                        </td>
                                                    </tr>
                                                        <%
                                                            int SEQNO = 0;
                                                            while (crs.next()) {
                                                                APPSTATUS = crs.getString("APPSTATUS");         //申请状态
                                                                APPNO = crs.getString("APPNO");                 //申请单号
                                                                APPDATE = crs.getString("APPDATE");             //申请日期
                                                                APPTYPE = crs.getString("APPTYPE");             //申请类型
                                                                COMMNAME = crs.getString("COMMNAME");           //商品名称
                                                                ++SEQNO;
                                                        %> <%
                                                            }
                                                            if (count < ps) {
                                                                for (int i = 0; i < ps - count; i++) {
                                                        %> <%
                                                                }
                                                            }
                                                        %><span class="style4"> </span>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="20" class="style4">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr height="35" align="center" valign="middle">
                    <td align="center">
                        <table border="0" cellspacing="0" cellpadding="0" style="width: 365px">
                            <tr class="page_form_tr">
                                <td nowrap align="left">
                                    <table class="list_button_tbl">
                                        <tr class="list_button_tbl_tr">
                                            <td class="style4">&nbsp;</td>
                                            <td class="list_form_button_td"><input type="button" name="a" class="style5"
                                                                                   value="同意（开始填写申请信息）"
                                                                                   onclick="return req1();"></td>
                                            <td class="style4">&nbsp;</td>
                                            <%--<script language="javascript" type="text/javascript">--%>
                                            <%--createFlipPage(<%=pn%><caption class="style4">, <%=ps%>, <%=rows%>, "applist.jsp?pn=", "form1");--%>
                                            <%--</script>--%>
                                            <td class="style4">&nbsp;</td>
                                            <td class="list_form_button_td"><input type="submit" name="a" class="style5"
                                                                                   value=" 不同意 "
                                                                                   onclick="window.close();"></td>
                                            <td class="style4">&nbsp;</td>
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
    <tr>
        <td valign="bottom">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center"
                   style="height: 65px; border: none; font-weight: normal; font-size: 10px; color: #8F87E0; font-family: '微软雅黑';">
                <tr>
                    <td width="30%" class="style4">&nbsp;</td>
                    <td width="40%">
                        <table border="0" cellspacing="0" cellpadding="0" width="330" align="center"
                               style="border-style: none; border-color: inherit; border-width: medium; height: 20px; font-weight: normal; font-size: 12px; color: #004080; "
                               class="style4">
                            <tr>
                                <td width="30%">分期咨询电话：<span style="color: #FF0066">0532-88939384，88939383</span>&nbsp;
                                    邮编：266101<br>
                                    地址：山东省青岛市海尔路1号海尔工业园K座金融中心405室
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="30%" valign="bottom" align="right" class="style4">最终解释权归海尔集团财务有限责任公司所有</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</form>
<span class="style4">
    <%--在线客服代码 365call-- 列表方式--%></span> <span class="style4">
    <%--<script type="text/javascript" src="http://chat2.365webcall.com/IMMe1.aspx?settings=mw7mNmXNNm6X7Xbz3Am600bPz3Am6wIbNz3AN6mm00&amp;LL=0"></script>--%>

</body>

</span>

</html>
<span class="style4">
<script language="javascript" type="text/javascript">
    function req() {
        //alert(document.location);
        //document.location.replace("applist.jsp");
        info('', '<%=NAME%>', '<%=IDTYPE.trim()%>', '<%=ID.trim()%>', '<%=PASSWORD.trim()%>', '');
    }

    function req1() {
        info1('<%=NAME%>', '<%=IDTYPE.trim()%>', '<%=ID.trim()%>', '<%=PASSWORD.trim()%>');
    }

    function req2() {
        document.location.replace("./apppass.jsp?NAME=<%=NAME%>&IDTYPE=<%=IDTYPE.trim()%>&ID=<%=ID.trim()%>");
    }
</script>
</span>
<%
        }
    }
%>