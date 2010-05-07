<%@ page contentType="application/msword; charset=GBK" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="java.math.BigDecimal" %>

<%
    request.setCharacterEncoding("GBK");
    System.out.println("JspClass.jsp_service_method");
    String APPNO = request.getParameter("APPNO");         //申请单编号
    //if (APPNO==null)APPNO="3702021981122211360001";

    if (APPNO == null) {
        session.setAttribute("msg", "没有发现传送入的参数！");
        response.sendRedirect("../showinfo.jsp");
    } else {
        APPNO = (APPNO == null) ? "" : APPNO.trim();

        //response.setHeader("Content-disposition","attachment;filename=/分期付款审批表-"+APPNO+".doc");

        ConnectionManager manager = ConnectionManager.getInstance();
        String sql1 = "select c.CLIENTNO,c.BIRTHDAY,c.GENDER,c.NATIONALITY,c.MARRIAGESTATUS,c.HUKOUADDRESS,c.CURRENTADDRESS," +
                "c.COMPANY,c.TITLE,c.QUALIFICATION,c.EDULEVEL,c.PHONE1,c.PHONE2," +
                "c.PHONE3,c.NAME,c.CLIENTTYPE,c.DEGREETYPE,c.COMADDR,c.SERVFROM,c.RESIDENCEADR,c.HOUSINGSTS," +
                "c.HEALTHSTATUS,c.MONTHLYPAY,c.BURDENSTATUS,c.EMPNO,c.SOCIALSECURITY,c.LIVEFROM,c.PC,c.COMPC,c.RESDPC,c.RESDADDR,c.EMAIL," +
                "m.CHANNEL,m.COMMNAME,m.COMMTYPE,m.ADDR,m.NUM,m.AMT,m.RECEIVEAMT,m.APPAMT,m.DIVID," +
                "ROUND((m.APPAMT*m.COMMISSIONRATE+m.APPAMT/m.DIVID),2) APPAMTMON,m.COMMISSIONRATE," +
                //"ROUND(m.APPAMT/m.DIVID,2) APPAMTMON,m.COMMISSIONRATE," +
                "ROUND(m.APPAMT*100/m.AMT,2) PROPORTION,ROUND(m.APPAMT/a.CONFMONPAY,2) MONTHPROP," +
                "d.ACTOPENINGBANK,d.BANKACTNO,d.XY,d.XYR,d.DY,d.DYW,d.ZY,d.ZYW,d.BZ,d.BZR,d.CREDITTYPE,d.MONPAYAMT," +
                "d.LINKMAN,d.LINKMANGENDER,d.LINKMANPHONE1,d.LINKMANPHONE2,d.APPRELATION,d.LINKMANADD,d.LINKMANCOMPANY," +
                "a.IDTYPE,a.ID,a.CONFMONPAY,a.APPDATE,a.APPTYPE,a.APPSTATUS," +
                "i.ccvalidperiod, i.ccdynum, i.ccxynum, i.cccdnum, i.ccdyamt, i.ccxyamt, i.cccdamt, i.ccdynowbal, i.ccxynowbal," +
                "i.cccdnowbal, i.ccdyrpmon, i.ccxyrpmon, i.cccdrpmon, i.ccyearrptime, i.ccdynooverdue, i.ccxynooverdue, i.cccdnooverdue, " +
                "i.ccdy1timeoverdue, i.ccxy1timeoverdue, i.cccd1timeoverdue, i.ccdy2timeoverdue, i.ccxy2timeoverdue, i.cccd2timeoverdue, " +
                "i.ccdym3timeoverdue, i.ccxy3timeoverdue, i.cccd3timeoverdue, i.ccdyin3monoverdue, i.ccxyin3monoverdue, i.cccdin3monoverdue, " +
                "i.ccloanyearquerytime, i.cccardyearquerytime, i.ccrpnowamt, i.acage, i.acwage, i.acjob, i.accontract, i.acusufruct, " +
                "i.acfacility, i.acrate, i.acresidenceadr, i.acimmunity, i.acacceptreason, i.ccdividamt, i.ccrptotalamt, i.ccrprate, i.acemp, " +
                "i.aczx1, i.aczx2, i.aczx3 " +
                "from CMINDVCLIENT c,XFAPPCOMM m,XFAPPADD d,XFAPP a,XFCREDITINFO i " +
                "where a.APPNO='" + APPNO + "' and a.IDTYPE=c.IDTYPE and a.ID=c.ID " +
                "and a.APPNO=m.APPNO " +
                "and a.APPNO=d.APPNO " +
                "and a.APPNO=i.APPNO";

        String IDTYPE = "";                    //证件名称
        String ID = "";                        //证件号码
        String CONFMONPAY = "";                //核定月收入
        String NAME = "";                      //客户名称 desc=企业(个人)名称
        String APPTYPE = "";                   //申请类型
        String APPSTATUS = "";                 //申请状态
        String PASSWORD = "";                  //密码
        String CLIENTNO = "";                  //客户号
        String BIRTHDAY = "";                  //出生日期
        String GENDER = "";                    //性别 enum=Gender
        String NATIONALITY = "";               //国籍
        String MARRIAGESTATUS = "";            //婚姻状况 enum=MarriageStatus
        String HUKOUADDRESS = "";              //户籍所在地
        String CURRENTADDRESS = "";            //现住址
        String COMPANY = "";                   //工作单位
        String TITLE = "";                     //职务 enum=Title
        String QUALIFICATION = "";             //职称 enum=Qualification
        String EDULEVEL = "";                  //学历 enum=EduLevel
        String PHONE1 = "";                    //移动电话
        String PHONE2 = "";                    //家庭电话
        String PHONE3 = "";                    //办公电话
        String CLIENTTYPE = "";                //客户性质 enum=ClientType1
        String DEGREETYPE = "";                //最高学位 enum=DegreeType
        String COMADDR = "";                   //单位地址
        String SERVFROM = "";                  //现单位工作时间
        String RESIDENCEADR = "";              //户籍所在地(本外地) enum=ResidenceADR
        String HOUSINGSTS = "";                //居住状况 enum=HousingSts
        String HEALTHSTATUS = "";              //健康状况 enum=HealthStatus
        String MONTHLYPAY = "";                //个人月收入
        String BURDENSTATUS = "";              //负担状况 enum=BurdenStatus
        String EMPNO = "";                     //员工卡号码
        String SOCIALSECURITY = "";            //社会保障 enum=SocialSecurity
        String LIVEFROM = "";                  //本地居住时间
        String PC = "";                        //住宅邮编
        String COMPC = "";                     //单位邮编
        String RESDPC = "";                    //寄送地址邮编
        String RESDADDR = "";                  //寄送地址
        String EMAIL = "";                     //电子邮件

        String CHANNEL = "";                   //销售单位(渠道名称)
        String COMMNAME = "";                  //商品名称
        String COMMTYPE = "";                  //商品型号
        String ADDR = "";                      //配送地址
        String NUM = "";                       //购买数量
        String AMT = "";                       //总金额
        String RECEIVEAMT = "";                //已付金额
        String APPAMT = "";                    //分期金额
        String DIVID = "";                     //分期期数
        String APPAMTMON = "";                 //每期还款额
        String COMMISSIONRATE = "";            //手续费率
        String PROPORTION = "";                //分期金额占所购产品价值比例
        String MONTHPROP = "";                 //月收入倍数

        String ACTOPENINGBANK = "";            //开户行 enum=Bank
        String BANKACTNO = "";                 //还款帐号
        String XY = "";                        //信用 enum=YesNo
        String XYR = "";                       //信用人名称
        String DY = "";                        //抵押 enum=YesNo
        String DYW = "";                       //抵押物名称
        String ZY = "";                        //质押 enum=YesNo
        String ZYW = "";                       //质押物名称
        String BZ = "";                        //保证 enum=YesNo
        String BZR = "";                       //保证人名称
        String CREDITTYPE = "";                //信用种类 enum=CreditType
        String MONPAYAMT = "";                 //月均还款额
        String LINKMAN = "";                   //联系人姓名
        String LINKMANGENDER = "";             //联系人性别
        String LINKMANPHONE1 = "";              //联系人移动电话
        String LINKMANPHONE2 = "";              //联系人固定电话
        String APPRELATION = "";               //与申请人关系 enum=AppRelation
        String LINKMANADD = "";                //联系人住址
        String LINKMANCOMPANY = "";            //联系人工作单位

        String APPDATE = "";                   //申请日期


        String CCVALIDPERIOD = "";                  //
        String CCDYNUM = "";                  //
        String CCXYNUM = "";                  //
        String CCCDNUM = "";                  //
        String CCDYAMT = "";                  //
        String CCXYAMT = "";                  //
        String CCCDAMT = "";                  //
        String CCDYNOWBAL = "";                  //
        String CCXYNOWBAL = "";                  //
        String CCCDNOWBAL = "";                  //
        String CCDYRPMON = "";                  //
        String CCXYRPMON = "";                  //
        String CCCDRPMON = "";                  //
        String CCYEARRPTIME = "";                  //
        String CCDYNOOVERDUE = "";                  //
        String CCXYNOOVERDUE = "";                  //
        String CCCDNOOVERDUE = "";                  //
        String CCDY1TIMEOVERDUE = "";                  //
        String CCXY1TIMEOVERDUE = "";                  //
        String CCCD1TIMEOVERDUE = "";                  //
        String CCDY2TIMEOVERDUE = "";                  //
        String CCXY2TIMEOVERDUE = "";                  //
        String CCCD2TIMEOVERDUE = "";                  //
        String CCDYM3TIMEOVERDUE = "";                  //
        String CCXY3TIMEOVERDUE = "";                  //
        String CCCD3TIMEOVERDUE = "";                  //
        String CCDYIN3MONOVERDUE = "";                  //
        String CCXYIN3MONOVERDUE = "";                  //
        String CCCDIN3MONOVERDUE = "";                  //
        String CCLOANYEARQUERYTIME = "";                  //
        String CCCARDYEARQUERYTIME = "";                  //
        String CCRPNOWAMT = "";                  //
        String ACAGE = "";                  //
        String ACWAGE = "";                  //
        String ACJOB = "";                  //
        String ACCONTRACT = "";                  //
        String ACUSUFRUCT = "";                  //
        String ACFACILITY = "";                  //
        String ACRATE = "";                  //
        String ACRESIDENCEADR = "";                  //
        String ACIMMUNITY = "";                  //
        String ACACCEPTREASON = "";                  //
        String CCDIVIDAMT = "";                  //
        String CCRPTOTALAMT = "";                  //
        String CCRPRATE = "";                  //
        String ACEMP = "";                  //
        String ACZX1 = "";                  //
        String ACZX2 = "";                  //
        String ACZX3 = "";                  //


        boolean ifErrClient = false;
        CachedRowSet crs = null;
        if (!sql1.equals("")) crs = manager.getRs(sql1);
        if (crs != null && crs.size() > 0) {
            crs.next();
            NAME = crs.getString("NAME");                           //客户名称 desc=企业(个人)名称)
            CLIENTNO = crs.getString("CLIENTNO");                //客户号
            BIRTHDAY = crs.getString("BIRTHDAY");                //出生日期
            GENDER = crs.getString("GENDER");                    //性别 enum=Gender
            NATIONALITY = crs.getString("NATIONALITY");         //国籍
            MARRIAGESTATUS = crs.getString("MARRIAGESTATUS");   //婚姻状况 enum=MarriageStatus
            HUKOUADDRESS = crs.getString("HUKOUADDRESS");       //户籍所在地
            CURRENTADDRESS = crs.getString("CURRENTADDRESS");   //现住址
            COMPANY = crs.getString("COMPANY");                  //工作单位
            TITLE = crs.getString("TITLE");                      //职务 enum=Title
            QUALIFICATION = crs.getString("QUALIFICATION");     //职称 enum=Qualification
            EDULEVEL = crs.getString("EDULEVEL");                //学历 enum=EduLevel
            PHONE1 = crs.getString("PHONE1");                   //移动电话
            PHONE2 = crs.getString("PHONE2");                   //家庭电话
            PHONE3 = crs.getString("PHONE3");                   //办公电话
            CLIENTTYPE = crs.getString("CLIENTTYPE");           //客户性质 enum=ClientType1
            DEGREETYPE = crs.getString("DEGREETYPE");          //最高学位 enum=DegreeType
            COMADDR = crs.getString("COMADDR");                 //单位地址
            SERVFROM = crs.getString("SERVFROM");               //现单位工作时间
            RESIDENCEADR = crs.getString("RESIDENCEADR");       //户籍所在地(本外地) enum=ResidenceADR
            HOUSINGSTS = crs.getString("HOUSINGSTS");           //居住状况 enum=HousingSts
            HEALTHSTATUS = crs.getString("HEALTHSTATUS");       //健康状况 enum=HealthStatus
            MONTHLYPAY = crs.getString("MONTHLYPAY");           //个人月收入
            BURDENSTATUS = crs.getString("BURDENSTATUS");       //负担状况 enum=BurdenStatus
            EMPNO = crs.getString("EMPNO");                      //员工卡号码
            SOCIALSECURITY = crs.getString("SOCIALSECURITY");   //社会保障 enum=SocialSecurity
            LIVEFROM = crs.getString("LIVEFROM");               //本地居住时间
            PC = crs.getString("PC");                            //住宅邮编
            COMPC = crs.getString("COMPC");                      //单位邮编
            RESDPC = crs.getString("RESDPC");                    //寄送地址邮编
            RESDADDR = crs.getString("RESDADDR");                //寄送地址
            EMAIL = crs.getString("EMAIL");                      //电子邮件

            CHANNEL = crs.getString("CHANNEL");                  //销售单位(渠道名称)
            COMMNAME = crs.getString("COMMNAME");                //商品名称
            COMMTYPE = crs.getString("COMMTYPE");                //商品型号
            ADDR = crs.getString("ADDR");                         //配送地址
            NUM = crs.getString("NUM");                           //购买数量
            AMT = crs.getString("AMT");                           //总金额
            RECEIVEAMT = crs.getString("RECEIVEAMT");            //已付金额
            APPAMT = crs.getString("APPAMT");                     //分期金额
            DIVID = crs.getString("DIVID");                       //分期期数
            APPAMTMON = crs.getString("APPAMTMON");              //每期还款额
            COMMISSIONRATE = crs.getString("COMMISSIONRATE");   //手续费率
            PROPORTION = crs.getString("PROPORTION");            //分期金额占所购产品价值比例
            MONTHPROP = crs.getString("MONTHPROP");              //月收入倍数

            ACTOPENINGBANK = crs.getString("ACTOPENINGBANK");   //开户行 enum=Bank
            BANKACTNO = crs.getString("BANKACTNO");              //还款帐号
            XY = crs.getString("XY");                             //信用 enum=YesNo
            XYR = crs.getString("XYR");                           //信用人名称
            DY = crs.getString("DY");                             //抵押 enum=YesNo
            DYW = crs.getString("DYW");                           //抵押物名称
            ZY = crs.getString("ZY");                             //质押 enum=YesNo
            ZYW = crs.getString("ZYW");                           //质押物名称
            BZ = crs.getString("BZ");                             //保证 enum=YesNo
            BZR = crs.getString("BZR");                           //保证人名称
            CREDITTYPE = crs.getString("CREDITTYPE");            //信用种类 enum=CreditType
            MONPAYAMT = crs.getString("MONPAYAMT");              //月均还款额
            LINKMAN = crs.getString("LINKMAN");                   //联系人姓名
            LINKMANGENDER = crs.getString("LINKMANGENDER");      //联系人性别
            LINKMANPHONE1 = crs.getString("LINKMANPHONE1");      //联系人移动电话
            LINKMANPHONE2 = crs.getString("LINKMANPHONE2");      //联系人固定电话
            APPRELATION = crs.getString("APPRELATION");          //与申请人关系 enum=AppRelation
            LINKMANADD = crs.getString("LINKMANADD");            //联系人住址
            LINKMANCOMPANY = crs.getString("LINKMANCOMPANY");    //联系人工作单位


            ID = crs.getString("ID");                              //证件号码
            IDTYPE = crs.getString("IDTYPE");                     //证件名称
            CONFMONPAY = crs.getString("CONFMONPAY");             //核定月收入
            APPDATE = crs.getString("APPDATE");                   //申请日期
            APPTYPE = crs.getString("APPTYPE");                   //申请类型
            APPSTATUS = crs.getString("APPSTATUS").trim();        //申请状态

            CCVALIDPERIOD = crs.getString("CCVALIDPERIOD");//
            CCDYNUM = crs.getString("CCDYNUM");//
            CCXYNUM = crs.getString("CCXYNUM");//
            CCCDNUM = crs.getString("CCCDNUM");//
            CCDYAMT = crs.getString("CCDYAMT");//
            CCXYAMT = crs.getString("CCXYAMT");//
            CCCDAMT = crs.getString("CCCDAMT");//
            CCDYNOWBAL = crs.getString("CCDYNOWBAL");//
            CCXYNOWBAL = crs.getString("CCXYNOWBAL");//
            CCCDNOWBAL = crs.getString("CCCDNOWBAL");//
            CCDYRPMON = crs.getString("CCDYRPMON");//
            CCXYRPMON = crs.getString("CCXYRPMON");//
            CCCDRPMON = crs.getString("CCCDRPMON");//
            CCYEARRPTIME = crs.getString("CCYEARRPTIME");//
            CCDYNOOVERDUE = crs.getString("CCDYNOOVERDUE");//
            CCXYNOOVERDUE = crs.getString("CCXYNOOVERDUE");//
            CCCDNOOVERDUE = crs.getString("CCCDNOOVERDUE");//
            CCDY1TIMEOVERDUE = crs.getString("CCDY1TIMEOVERDUE");//
            CCXY1TIMEOVERDUE = crs.getString("CCXY1TIMEOVERDUE");//
            CCCD1TIMEOVERDUE = crs.getString("CCCD1TIMEOVERDUE");//
            CCDY2TIMEOVERDUE = crs.getString("CCDY2TIMEOVERDUE");//
            CCXY2TIMEOVERDUE = crs.getString("CCXY2TIMEOVERDUE");//
            CCCD2TIMEOVERDUE = crs.getString("CCCD2TIMEOVERDUE");//
            CCDYM3TIMEOVERDUE = crs.getString("CCDYM3TIMEOVERDUE");//
            CCXY3TIMEOVERDUE = crs.getString("CCXY3TIMEOVERDUE");//
            CCCD3TIMEOVERDUE = crs.getString("CCCD3TIMEOVERDUE");//
            CCDYIN3MONOVERDUE = crs.getString("CCDYIN3MONOVERDUE");//
            CCXYIN3MONOVERDUE = crs.getString("CCXYIN3MONOVERDUE");//
            CCCDIN3MONOVERDUE = crs.getString("CCCDIN3MONOVERDUE");//
            CCLOANYEARQUERYTIME = crs.getString("CCLOANYEARQUERYTIME");//
            CCCARDYEARQUERYTIME = crs.getString("CCCARDYEARQUERYTIME");//
            CCRPNOWAMT = crs.getString("CCRPNOWAMT");//
            ACAGE = crs.getString("ACAGE");//
            ACWAGE = crs.getString("ACWAGE");//
            ACJOB = crs.getString("ACJOB");//
            ACCONTRACT = crs.getString("ACCONTRACT");//
            ACUSUFRUCT = crs.getString("ACUSUFRUCT");//
            ACFACILITY = crs.getString("ACFACILITY");//
            ACRATE = crs.getString("ACRATE");//
            ACRESIDENCEADR = crs.getString("ACRESIDENCEADR");//
            ACIMMUNITY = crs.getString("ACIMMUNITY");//
            ACACCEPTREASON = crs.getString("ACACCEPTREASON");//
            CCDIVIDAMT = crs.getString("CCDIVIDAMT");//
            CCRPTOTALAMT = crs.getString("CCRPTOTALAMT");//
            CCRPRATE = crs.getString("CCRPRATE");//
            ACEMP = crs.getString("ACEMP");//
            ACZX1 = crs.getString("ACZX1");//
            ACZX2 = crs.getString("ACZX2");//
            ACZX3 = crs.getString("ACZX3");//
        }


        BIRTHDAY = (BIRTHDAY == null) ? "" : BIRTHDAY;
        CCVALIDPERIOD = (CCVALIDPERIOD == null) ? "" : CCVALIDPERIOD;
        CCDYNOOVERDUE = (CCDYNOOVERDUE == null) ? "" : CCDYNOOVERDUE;
        System.out.println("COMMISSIONRATE = " + COMMISSIONRATE);
%>
<html>
<head id="head1">
    <title>海尔集团财务有限责任公司个人消费信贷申请书</title>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <style>
        <!--
        /* Font Definitions */
        @font-face {
            font-family: "宋 体";
            panose-1: 2 1 6 0 3 1 1 1 1 1;
        }
        @font-face {
            font-family: PMingLiU;
            panose-1: 2 2 3 0 0 0 0 0 0 0;
        }
        @font-face {
            font-family: "Cambria Math";
            panose-1: 2 4 5 3 5 4 6 3 2 4;
        }
        @font-face {
            font-family: "\@宋体";
            panose-1: 2 1 6 0 3 1 1 1 1 1;
        }
        @font-face {
            font-family: "\@PMingLiU";
            panose-1: 2 2 3 0 0 0 0 0 0 0;
        }
        /* Style Definitions */
        p.MsoNormal, li.MsoNormal, div.MsoNormal {
            margin: 0cm;
            margin-bottom: .0001pt;
            text-align: justify;
            text-justify: inter-ideograph;
            font-size: 10.5pt;
            font-family: "Times New Roman", "serif";
        }

        p.MsoHeader, li.MsoHeader, div.MsoHeader {
            mso-style-link: "页眉 Char";
            margin: 0cm;
            margin-bottom: .0001pt;
            text-align: center;
            layout-grid-mode: char;
            border: none;
            padding: 0cm;
            font-size: 9.0pt;
            font-family: "Times New Roman", "serif";
        }

        p.MsoFooter, li.MsoFooter, div.MsoFooter {
            mso-style-link: "页脚 Char";
            margin: 0cm;
            margin-bottom: .0001pt;
            layout-grid-mode: char;
            font-size: 9.0pt;
            font-family: "Times New Roman", "serif";
        }

        span.definition {
            mso-style-name: definition;
            font-family: "Arial", "sans-serif";
        }

        span.Char {
            mso-style-name: "页眉 Char";
            mso-style-link: "页 眉";
        }

        span.Char0 {
            mso-style-name: "页脚 Char";
            mso-style-link: "页 脚";
        }

        /* Page Definitions */
        @page Section1 {
            size: 595.3pt 841.9pt;
            margin: 18.0pt 18.0pt 18.0pt 18.0pt;
            layout-grid: 15.6pt;
        }

        div.Section1 {
            page: Section1;
        }

        /* List Definitions */
        ol {
            margin-bottom: 0cm;
        }

        ul {
            margin-bottom: 0cm;
        }

        -->
    </style>

    <%--<!--media=print 这个属性说明可以在打印时有效-->--%>
    <%--<!--希望打印时不显示的内容设置class="Noprint"样式-->--%>
    <%--<!--希望人为设置分页的位置设置class="PageNext"样式-->--%>
    <%--<style media="print">--%>
        <%--.Noprint {--%>
            <%--display: none;--%>
        <%--}--%>

        <%--.PageNext {--%>
            <%--page-break-after: always;--%>
        <%--}--%>
    <%--</style>--%>
<%--</head>--%>

<%--<SCRIPT type="text/javascript">--%>
    <%--var hkey_root,hkey_path,hkey_key;--%>
    <%--hkey_root = "HKEY_CURRENT_USER";--%>

    <%--var hk_pageHead,hk_pageFoot;--%>
    <%--<!--地址的写法很严格的用双斜杠-->--%>
    <%--hkey_path = "\\Software\\Microsoft\\Internet Explorer\\PageSetup";--%>
    <%--//设置网页打印的页眉页脚为空--%>
    <%--function pagesetup_null() {--%>
        <%--try {--%>
            <%--var RegWsh = new ActiveXObject("WScript.Shell");--%>
            <%--hkey_key = "\\header";--%>
            <%--hk_pageHead = RegWsh.RegRead(hkey_root + hkey_path + hkey_key);--%>
            <%--RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");--%>
            <%--hkey_key = "\\footer";--%>
            <%--hk_pageFoot = RegWsh.RegRead(hkey_root + hkey_path + hkey_key);--%>
            <%--RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");--%>
        <%--} catch(e) {--%>
            <%--//alert('需要运行运行Activex才能进行打印设置。');--%>
        <%--}--%>
    <%--}--%>
    <%--//设置网页打印的页眉页脚为默认值--%>
    <%--function pagesetup_default() {--%>
        <%--try {--%>
            <%--var RegWsh = new ActiveXObject("WScript.Shell");--%>
            <%--hkey_key = "\\header";--%>
            <%--//RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&w&b页码,&p/&P");--%>
            <%--RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, hk_pageHead);--%>
            <%--hkey_key = "\\footer";--%>
            <%--//RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&u&b&d");--%>
            <%--RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, hk_pageFoot);--%>
        <%--} catch(e) {--%>
        <%--}--%>
    <%--}--%>
    <%--function printsetup() {--%>
        <%--wb.execwb(8, 1); // 打印页面设置--%>
    <%--}--%>
    <%--function printpreview() {--%>
        <%--wb.execwb(7, 1);// 打印页面预览--%>
    <%--}--%>
    <%--function printit() {--%>
        <%--if (confirm('确定打印吗？')) {--%>
            <%--pagesetup_null();--%>
            <%--wb.execwb(6, 1);--%>
            <%--pagesetup_default();--%>
        <%--}--%>
    <%--}--%>

    <%--function doPrint() {--%>
        <%--pagesetup_null();--%>
        <%--var str = "<html><head>";--%>
        <%--str += document.all.head1.innerHTML;--%>
        <%--str += "</head><body>";--%>
        <%--str += document.all.spp.innerHTML;--%>
        <%--str += "<script language=\"javascript\">window.print();<\/script>";--%>
        <%--str += "</body></html>";--%>
        <%--//str += "<script language=\"javascript\">document.all.wb.ExecWB(7,1);<\/script>";--%>
        <%--document.open();--%>
        <%--document.write(str);--%>
        <%--document.close();--%>
        <%--pagesetup_default();--%>
    <%--}--%>
    <%--function window.onbeforeprint() {--%>
        <%--window.document.url = "";--%>
    <%--}--%>
    <%--function window.onafterprint() {--%>
        <%--history.go(-1);--%>
    <%--}--%>

<%--</SCRIPT>--%>

<body lang="ZH-CN" style='text-justify-trim:punctuation' marginwidth="0" marginheight="0">
<div class="" id="printbt" style="display:none;">
    <table class='page_button_tbl' style="display:none;">
        <tr class='page_button_tbl_tr'>
            <td class='page_button_tbl_td'>
                <input type='button' class='page_button_active' name='button'
                       onclick="javascript:doPrint()" value=" 打 印 ">
            </td>
            <td class='page_button_tbl_td'>
                <input type='button' class='page_button_active'
                       name='button'
                       value=' 关 闭 ' onClick="window.close()">
            </td>
        </tr>
    </table>
    <hr style="display:none;">
</div>
<div class=Section1 style='layout-grid:15.6pt' id="spp">
<div align=center>
<table border=1 cellspacing=0 cellpadding=0 width=755
       style='width:566.2pt;margin-left:6.4pt;border-collapse:collapse;border:none'>
<tr>
    <td width=755 colspan=21 valign=top style='width:566.2pt;border:none;padding:0cm 0cm 0cm 0cm' align="right"><span style="font-size:10.0pt;font-family:'宋体';color:black;">申请单号：<%=APPNO%></span></td></tr></table>
<table border=1 cellspacing=0 cellpadding=0 width=755
       style='width:566.2pt;margin-left:6.4pt;border-collapse:collapse;border:none'>
<tr>
    <td width=755 colspan=21 valign=top style='width:566.2pt;border:solid windowtext 1.0pt;
          border-bottom:solid windowtext 1.5pt;background:#003366;padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:3.0pt;margin-right:0cm;
 margin-bottom:3.0pt;margin-left:0cm;text-align:center'>
            <b>
              <span
                      style="font-size:12.0pt;font-family:'宋体';color:white">海尔集团财务有限责任公司个人消费分期付款审批表
              </span>
            </b>
        </p>
    </td>
</tr>
<tr>
    <td width=49 rowspan=3 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
        border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
        padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
      			<span style="font-size:10.0pt;font-family:'宋体'">申<br>请<br>人</span>
            </b>
        </p>
    </td>
    <td width=57 height="21" valign=top style='width:42.95pt;border-top:none;border-left:     none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
      padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">姓名：</span>
        </p>
    </td>
    <td width=101 height="21" colspan=4 valign=top style='width:75.65pt;border-top:none;
    border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 0cm 0cm 0cm'>
        <p>
            <span lang=EN-US style='font-size:10.0pt'><%=NAME == null ? "" : NAME%></span>
        </p>
    </td>
    <td width=95 height="21" colspan=3 valign=top style='width:71.5pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">身份证号码：</span>
        </p>
    </td>
    <td width=208 height="21" colspan=7 valign=top style='width:155.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US style='font-size:10.0pt'><%=ID == null ? "" : ID%></span>
    </p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">员工卡号码：</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=EMPNO == null ? "" : EMPNO%></span></p>
    </td>
</tr>
<tr>
    <td width=57 height="21" valign=top style='width:42.95pt;border-top:none;border-left: none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">地址：</span>
        </p>
    </td>
    <td width=196 height="21" colspan=7 valign=top style='width:147.15pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=CURRENTADDRESS == null ? "" : CURRENTADDRESS%></span>
    </p></td>
    <td width=83 height="21" colspan=3 valign=top style='width:62.4pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:9.0pt;font-family:'宋体'">户籍所在地：</span></p></td>
    <td width=124 height="21" colspan=4 valign=top style='width:93.35pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'"><%=level.getEnumItemName("ResidenceADR", RESIDENCEADR)%></span>
    </p></td>
    <td width=95 valign=top style='width:71.3pt;border-top:none;border-left:none;
border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">联系电话：</span></p></td>
    <td width=150 colspan=4 valign=top style='width:112.4pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=PHONE1 == null ? "" : PHONE1%></span></p>
    </td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">性别：</span></p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'"><%=level.getEnumItemName("Gender", GENDER)%></span></p></td>
    <td width=76 height="21" colspan=3 valign=top style='width:57.15pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">婚姻状况：</span></p></td>
    <td width=157 height="21" colspan=5 valign=top style='width:117.85pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'>
    <p><span style="font-size:10.0pt;font-family:'宋体'">
    <%=level.getEnumItemName("MarriageStatus", MARRIAGESTATUS)%>
</span></p>
</td>
    <td width=95 valign=top style='width:71.3pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">出生日期：</span><span
            lang=EN-US style='font-size:10.0pt'>&nbsp; </span></p></td>
    <td width=150 colspan=4 valign=top style='width:112.4pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p style='text-indent:25.0pt'><span lang=EN-US
                                                                             style='font-size:10.0pt'><%=BIRTHDAY.equals("") ? "&nbsp;" : BIRTHDAY.substring(0, 4)%> </span><span
            style="font-size:10.0pt;font-family:'宋体'">年</span><span lang=EN-US
                                                                    style='font-size:10.0pt'><%=BIRTHDAY.equals("") ? "&nbsp;" : BIRTHDAY.substring(5, 7)%> </span><span
            style="font-size:10.0pt;font-family:'宋体'">月</span><span lang=EN-US
                                                                    style='font-size:10.0pt'><%=BIRTHDAY.equals("") ? "&nbsp;" : BIRTHDAY.substring(8, 10)%> </span><span
            style="font-size:10.0pt;font-family:'宋体'">日</span></p></td>
</tr>
<tr>
    <td width=49 rowspan=2 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
      <span
              style="font-size:10.0pt;font-family:'宋体'">工作资料</span>
            </b>
        </p>
    </td>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">工作单位：</span></p></td>
    <td width=354 height="21" colspan=12 valign=top style='width:265.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US style='font-size:10.0pt'><%=COMPANY == null ? "" : COMPANY%></span>
    </p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">现单位工作时间：</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=SERVFROM == null ? "" : SERVFROM%>年</span>
    </p></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">单位地址：</span></p>
    </td>
    <td width=272 height="21" colspan=10 valign=top style='width:204.05pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US style='font-size:10.0pt;'><%=COMADDR == null ? "" : COMADDR%></span>
    </p></td>
    <td width=178 height="21" colspan=4 valign=top style='width:133.15pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'">单位电话：<%=PHONE3 == null ? "" : PHONE3%></span></p></td>
    <td width=149 colspan=3 valign=top style='width:111.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">每月收入：￥ <%=CONFMONPAY == null ? "" : CONFMONPAY%></span><span
            style="font-size:10.0pt;font-family:'宋体'">元</span></p></td>
</tr>
<tr>
    <td width=49 rowspan=4 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
      <span
              style="font-size:10.0pt;font-family:'宋体'">分期付款情况
      </span>
            </b>
        </p>
    </td>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p><span style="font-size:9.0pt;font-family:'宋体'">产品名称：</span></p>
    </td>
    <td width=46 height="21" colspan=17 valign=top style='width:138.4pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><span style="font-size:10.0pt"><%=COMMNAME == null ? "" : COMMNAME%></span></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">申请分期金额：</span></p></td>
    <td width=235 height="21" colspan=9 valign=top style='width:176.0pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">￥</span><span
            style='font-size:10.0pt'> <%=APPAMT == null ? "" : APPAMT%></span><span
            style="font-size:10.0pt;font-family:'宋体'">元</span></p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">期数：</span></p></td>
    <td width=245 colspan=5 valign=top style='width:183.7pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'"><%=level.getEnumItemName("Divid", DIVID)%></span></p></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">月手续费率：</span>
        </p>
    </td>
    <td width=46 height="21" colspan=5 valign=top style='width:34.65pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=COMMISSIONRATE == null ? "" : String.valueOf(new BigDecimal(COMMISSIONRATE).movePointRight(3))%>‰</span>
    </p></td>
    <td width=87 height="21" colspan=4 valign=top style='width:65.3pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'宋体'">商品总价款：</span></p></td>
    <td width=78 height="21" colspan=3 valign=top style='width:58.25pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">￥ <%=AMT == null ? "" : AMT%></span><span
            style="font-size:10.0pt;font-family:'宋体'">元</span></p></td>
    <td width=175 colspan=4 valign=top style='width:134.4pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:9.0pt;font-family:'宋体'">分期金额占所购产品价值比例：</span></p>
    </td>
    <td width=70 valign=top style='width:45.3pt;border-top:none;border-left:none;
border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt"><%=PROPORTION == null ? "" : PROPORTION%>%</span></p></td>
</tr>

<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:9.0pt;font-family:'宋体'">所购产品使用地址：</span>
        </p>
    </td>
    <td width=235 height="21" colspan=9 valign=top style='width:176.0pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt"><%=ADDR == null ? "" : ADDR%></span></p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">每期还款额：</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">￥ <%=APPAMTMON == null ? "" : APPAMTMON%></span><span
            style="font-size:10.0pt;font-family:'宋体'">元</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'">月收入倍数：<%=MONTHPROP == null ? "" : MONTHPROP%></span></p></td>
</tr>
<tr>
    <td width=49 rowspan=11
        style='width:36.65pt;border-top:none;border-left: solid windowtext 1.5pt;border-bottom:solid windowtext 1.0pt;border-right: solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
                <span style="font-size:10.0pt;font-family:'宋体'">征<br>信<br>系<br>统<br>纪<br>录</span>
            </b>
        </p>
    </td>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US style='font-size:8.0pt'>&nbsp;</span></p></td>
    <td width=354 height="21" colspan=12 valign=top style='width:265.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><%if (CCVALIDPERIOD.equals("0")) {%><span lang=EN-US
                                                                                      style='font-size:11.0pt;font-family:"Wingdings 2"'>R</span><%} else {%><span
            style="font-size:10.0pt;font-family:'宋体'">□</span><%}%><span
            style="font-size:9.0pt;font-family:'宋体'">无纪录</span><span lang=EN-US
                                                                     style='font-size:8.0pt'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><%if (CCVALIDPERIOD.equals("1")) {%><span
            lang=EN-US style='font-size:11.0pt;font-family:"Wingdings 2"'>R</span><%} else {%><span
            style="font-size:10.0pt;font-family:'宋体'">□</span><%}%><span
            style="font-size:9.0pt;font-family:'宋体'">少于6个月纪录</span><span lang=EN-US
                                                                      style='font-size:8.0pt'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><%if (CCVALIDPERIOD.equals("2")) {%><span
            lang=EN-US style='font-size:11.0pt;font-family:"Wingdings 2"'>R</span><%} else {%><span
            style="font-size:10.0pt;font-family:'宋体'">□</span><%}%><span
            lang=EN-US style='font-size:9.0pt'>6个月以上纪录</span></p>
    </td>
    <td width=245 colspan=5 valign=top style='width:183.7pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><b><span
            style="font-size:10.0pt;font-family:'宋体'">过去</span></b><b><span lang=EN-US
                                                                            style='font-size:10.0pt'>12</span></b><b><span
            style="font-size:10.0pt;font-family:'宋体'">个月内查询</span></b><b><span lang=EN-US
                                                                               style='font-size:10.0pt'>(</span></b><b><span
            style="font-size:10.0pt;font-family:'宋体'">次数</span></b><b><span lang=EN-US
                                                                            style='font-size:10.0pt'>)</span></b><b><span
            style="font-size:10.0pt;font-family:'宋体'">：</span></b></p></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span lang=EN-US style='font-size:10.0pt'>&nbsp;</span>
        </p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
background:#99CCFF;padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><b><span
            style="font-size:10.0pt;font-family:'宋体'">抵押贷款</span></b></p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
background:#99CCFF;padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><b><span
            style="font-size:10.0pt;font-family:'宋体'">信用贷款</span></b></p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
background:#99CCFF;padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><b><span
            style="font-size:10.0pt;font-family:'宋体'">信用卡</span></b></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'宋体'">贷款审批</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'宋体'">信用卡审批</span></p></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:9.0pt;font-family:'宋体'">贷款(卡)数量</span>
        </p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCDYNUM == null ? "" : CCDYNUM%></span>
    </p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCXYNUM == null ? "" : CCXYNUM%></span>
    </p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCCDNUM == null ? "" : CCCDNUM%></span>
    </p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCLOANYEARQUERYTIME == null ? "" : CCLOANYEARQUERYTIME%>次</span>
    </p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCCARDYEARQUERYTIME == null ? "" : CCCARDYEARQUERYTIME%>次</span>
    </p></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:9.0pt;font-family:'宋体'">总贷款额／额度</span>
        </p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span class="MsoNormal"
                                                                                         style="text-align:center"><span
            style="font-size:10.0pt"><%=CCDYAMT == null ? "" : CCDYAMT%></span></span></p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCXYAMT == null ? "" : CCXYAMT%></span>
    </p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCCDAMT == null ? "" : CCCDAMT%></span>
    </p></td>
    <td width=245 colspan=5 valign=top style='width:183.7pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=left style='text-align:left'><span
            style="font-size:8.0pt;font-family:'宋体';color:red">查洵超过</span><span lang=EN-US
                                                                              style='font-size:8.0pt;color:red'>5</span><span
            style="font-size:8.0pt;font-family:'宋体';color:red">次，需个别判断</span></p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:9.0pt;font-family:'宋体'">总贷款余额／额度</span>
        </p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span class="MsoNormal"
                                                                                         style="text-align:center"><span
            style="font-size:10.0pt"><%=CCDYNOWBAL == null ? "" : CCDYNOWBAL%></span></span></p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCXYNOWBAL == null ? "" : CCXYNOWBAL%></span>
    </p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCCDNOWBAL == null ? "" : CCCDNOWBAL%></span>
    </p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'宋体'">现有贷款还款额：</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=CCRPNOWAMT == null ? "" : CCRPNOWAMT%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:9.0pt;font-family:'宋体'">当月还款额</span>
        </p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCDYRPMON == null ? "" : CCDYRPMON%></span>
    </p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCXYRPMON == null ? "" : CCXYRPMON%></span>
    </p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCCDRPMON == null ? "" : CCCDRPMON%></span>
    </p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span style="font-size:9.0pt;font-family:'宋体'">该笔分期还款额：</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=CCDIVIDAMT == null ? "" : CCDIVIDAMT%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span lang=EN-US style='font-size:7.0pt'>12个月还款记录(次数)</span>
        </p>
    </td>
    <td width=354 height="21" colspan=12 valign=top style='width:265.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'>&nbsp;</span>
    </p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'宋体'">总还款额：</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=CCRPTOTALAMT == null ? "" : CCRPTOTALAMT%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p style='margin-left:10.3pt'>
    <span style="font-size:7.0pt;font-family:'宋体'">无逾期</span>
        </p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%if (CCDYNOOVERDUE.equals("0")) {%>否<%} else {%>是<%}%></span>
    </p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%if (CCDYNOOVERDUE.equals("0")) {%>否<%} else {%>是<%}%></span>
    </p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%if (CCDYNOOVERDUE.equals("0")) {%>否<%} else {%>是<%}%></span>
    </p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span style="font-size:9.0pt;font-family:'宋体'">每月还款与收入比：</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US style='font-size:10.0pt'><%=CCRPRATE == null ? "" : CCRPRATE%>%</span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p style='margin-left:10.3pt'>
    <span style="font-size:7.0pt;font-family:'宋体'">逾期1-30天(1)</span>
        </p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCDY1TIMEOVERDUE == null ? "" : CCDY1TIMEOVERDUE%></span>
    </p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCXY1TIMEOVERDUE == null ? "" : CCXY1TIMEOVERDUE%></span>
    </p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCCD1TIMEOVERDUE == null ? "" : CCCD1TIMEOVERDUE%></span>
    </p></td>
    <td width=245 colspan=5 rowspan=3 valign=top style='width:183.7pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:8.0pt;font-family:'宋体';color:red">信用贷款及信用卡上限</span><span
            lang=EN-US style='font-size:8.0pt;color:red'>5</span><span style="font-size:8.0pt;font-family:'宋体';color:red">次，</span><span
            lang=EN-US style='font-size:8.0pt;color:red'>6</span><span
            style="font-size:8.0pt;font-family:'宋体';color:red">次以上个别判断</span></p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p style='margin-left:10.3pt'>
    <span style="font-size:7.0pt;font-family:'宋体'">逾期31-60天(2)</span>
        </p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCDY2TIMEOVERDUE == null ? "" : CCDY2TIMEOVERDUE%></span>
    </p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCXY2TIMEOVERDUE == null ? "" : CCXY2TIMEOVERDUE%></span>
    </p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCCD2TIMEOVERDUE == null ? "" : CCCD2TIMEOVERDUE%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p style='margin-left:10.3pt'>
    <span style="font-size:7.0pt;font-family:'宋体'">逾期60天以上(≥3)</span>
        </p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCDYM3TIMEOVERDUE == null ? "" : CCDYM3TIMEOVERDUE%></span>
    </p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCXY3TIMEOVERDUE == null ? "" : CCXY3TIMEOVERDUE%></span>
    </p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCCD3TIMEOVERDUE == null ? "" : CCCD3TIMEOVERDUE%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=49 rowspan=10
        style='width:36.65pt;border-top:none;border-left: solid windowtext 1.5pt;border-bottom:solid windowtext 1.0pt;border-right: solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
                <span style="font-size:10.0pt;font-family:'宋体'">授<br>信<br>要<br>求</span>
            </b>
        </p>
    </td>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">年龄要求：</span><span
            lang=EN-US style='font-size:10.0pt'>18-60</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'"><%if (ACAGE.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;否<%} else {%>是<%}%></span></p></td>
    <td width=126 colspan=2 rowspan=10 valign=top style='width:94.45pt;
border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:9.0pt;font-family:'宋体'">提供特殊接受理由：</span><br>
<font style='font-size:10.0pt; word-break : break-all;'><%=ACACCEPTREASON == null ? "" : ACACCEPTREASON%></font></p>
    </td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">每月最低工资</span>
    <span
            class=definition>
      <span style="font-size:10.0pt;font-family:'宋体'">要</span>
    </span>
    <span
            style="font-size:10.0pt;font-family:'宋体'">求：不低于人民币
    </span>
    <span lang=EN-US
          style='font-size:10.0pt'>2,000
    </span>
    <span style="font-size:10.0pt;font-family:'宋体'">元
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'"><%if (ACWAGE.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;否<%} else {%>是<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=70 height="21" colspan=2 rowspan=2 style='width:52.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p align=center style='text-align:center'>
    <span
            style="font-size:10.0pt;font-family:'宋体'">员工身份
    </span>
        </p>
    </td>
    <td width=391 height="21" colspan=13 valign=top style='width:293.05pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">海尔正式员工</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'"><%if (ACEMP.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;否<%} else {%>是<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=391 height="21" colspan=13 valign=top style='width:293.05pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">外包工：海尔工龄在</span>
    <span
            lang=EN-US style='font-size:10.0pt'>1
    </span>
    <span style="font-size:10.0pt;font-family:'宋体'">年以上
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'"><%if (ACJOB.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;否<%} else {%>是<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">劳动合同年</span>
    <span
            class=definition>
      <span style="font-size:10.0pt;font-family:'宋体'">限</span>
    </span>
    <span
            style="font-size:10.0pt;font-family:'宋体'">要求：劳动合同剩余期限不低于分期期限的
    </span>
    <span
            lang=EN-US style='font-size:10.0pt'>50%
    </span>
    <span style="font-size:10.0pt;font-family:'宋体'">（含）
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'"><%if (ACCONTRACT.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;否<%} else {%>是<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">征信系统内最近一期无未还款记录</span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'"><%if (ACZX1.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;否<%} else {%>是<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">征信系统内过去</span>
    <span
            lang=EN-US style='font-size:10.0pt'>12
    </span>
    <span style="font-size:10.0pt;font-family:'宋体'">个月最高逾期期数在
    </span>
            <span lang=EN-US style='font-size:10.0pt'>5</span>
    <span
            style="font-size:10.0pt;font-family:'宋体'">次以下
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'"><%if (ACZX2.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;否<%} else {%>是<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">征信系统内过去</span>
    <span
            lang=EN-US style='font-size:10.0pt'>12
    </span>
    <span style="font-size:10.0pt;font-family:'宋体'">个月最长逾期天数在
    </span>
            <span lang=EN-US style='font-size:10.0pt'>60</span>
    <span
            style="font-size:10.0pt;font-family:'宋体'">天以下
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'"><%if (ACZX3.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;否<%} else {%>是<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">信用分期总额超过人民币</span>
    <span
            lang=EN-US style='font-size:10.0pt'>30,000
    </span>
    <span style="font-size:10.0pt;font-family:'宋体'">或
    </span>
            <span lang=EN-US style='font-size:10.0pt'>12</span>
    <span
            style="font-size:10.0pt;font-family:'宋体'">倍月收入
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'"><%if (ACFACILITY.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;否<%} else {%>是<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">每月还款与收入比是否低于</span>
    <span
            lang=EN-US style='font-size:10.0pt'>70%
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'"><%if (ACRATE.equals("0")) {%>否<%} else {%>是<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=49 rowspan=3 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
                <span style="font-size:10.0pt;font-family:'宋体'">业<br>务<br>部<br>门</span>
            </b>
        </p>
    </td>
    <td width=228 colspan=7 valign=top style='width:170.85pt;border:none;
border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:9.0pt;font-family:'宋体'">客户经理意见：</span><span
            lang=EN-US style='font-size:10.0pt'>&nbsp;&nbsp;&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">□同意</span><span style='font-size:9.0pt'> </span><span
            style="font-size:9.0pt;font-family:'宋体'">□不同意</span></p></td>
    <td width=233 colspan=8 valign=top style='width:175.0pt;border:none;
border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:9.0pt;font-family:'宋体'">信贷经理意见：</span><span
            lang=EN-US style='font-size:9.0pt'>&nbsp;&nbsp;&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">□同意</span><span style='font-size:9.0pt'> </span><span
            style="font-size:9.0pt;font-family:'宋体'">□不同意</span></p></td>
    <td width=245 colspan=5 valign=top style='width:183.7pt;border:none;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:9.0pt;font-family:'宋体'">信贷总监意见：</span><span
            lang=EN-US style='font-size:9.0pt'>&nbsp;&nbsp;&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">□同意</span><span style='font-size:9.0pt'> </span><span
            style="font-size:9.0pt;font-family:'宋体'">□不同意</span></p></td>
</tr>
<tr style='height:32.35pt'>
    <td width=228 colspan=7 valign=top style='width:170.85pt;border:none;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:32.35pt'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">意见：</span>
        </p>
    </td>
    <td width=233 colspan=8 valign=top style='width:175.0pt;border:none;
border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:32.35pt'><p><span
            style="font-size:10.0pt;font-family:'宋体'">意见：</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
padding:0cm 0cm 0cm 0cm;height:32.35pt'><p><span
            style="font-size:10.0pt;font-family:'宋体'">意见：</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border:none;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;height:32.35pt'><p><span lang=EN-US
                                                                                                     style='font-size:10.0pt'>&nbsp;</span>
    </p></td>
</tr>
<tr>
    <td width=107 height="16" colspan=3 valign=top style='width:80.6pt;border:none;
  border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">签字：</span>
        </p>
    </td>
    <td width=120 colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'宋体'">年</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">月</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">日</span></p></td>
    <td width=114 colspan=5 valign=top style='width:85.75pt;border:none;
border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'">签字：</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'宋体'">年</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">月</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">日</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'">签字：</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'宋体'">年</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">月</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">日</span></p></td>
</tr>
<tr style='height:23.25pt'>
    <td width=49 rowspan=3 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:23.25pt'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
                <span style="font-size:10.0pt;font-family:'宋体'">风<br>险<br>测<br>评</span>
            </b>
        </p>
    </td>
    <td width=107 colspan=3 style='width:80.6pt;border:none;padding:0cm 0cm 0cm 0cm;
height:23.25pt'><p><span style="font-size:10.0pt;font-family:'宋体'">得分：</span></p></td>
    <td width=235 colspan=9 style='width:176.0pt;border:none;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm;height:23.25pt'><p align=right style='text-align:right'><span
            style="font-size:8.0pt;font-family:'宋体';color:red">（不低于</span><span lang=EN-US
                                                                                style='font-size:8.0pt;color:red'>60</span><span
            style="font-size:8.0pt;font-family:'宋体';color:red">分）</span></p></td>
    <td width=364 colspan=8 rowspan=3 valign=top style='width:272.95pt;
border-top:none;border-left:none;border-bottom:solid windowtext 1.5pt;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;height:23.25pt'><p><span
            style="font-size:9.0pt;font-family:'宋体'">特殊接受/拒绝理由：</span></p>
    </td>
</tr>
<tr style='height:23.25pt'>
    <td width=107 colspan=3 style='width:80.6pt;border:none;padding:0cm 0cm 0cm 0cm;
  height:23.25pt'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">意见：</span>
        </p>
    </td>
    <td width=120 colspan=4 style='width:90.25pt;border:none;padding:0cm 0cm 0cm 0cm;
height:23.25pt'><p><span lang=EN-US style='font-size:10.0pt'>&nbsp;</span></p></td>
    <td width=114 colspan=5 valign=top style='width:85.75pt;border:none;
border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:23.25pt'><p><span lang=EN-US
                                                                                                     style='font-size:8.0pt;color:red'>&nbsp;</span>
    </p></td>
</tr>
<tr>
    <td width=107 height="16" colspan=3 valign=top style='width:80.6pt;border:none;
  border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'>
        <p>
            <span lang=EN-US style='font-size:10.0pt'>&nbsp;</span>
        </p>
    </td>
    <td width=120 colspan=4 valign=top style='width:90.25pt;border:none;
border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'">签字：</span></p></td>
    <td width=114 colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'宋体'">年</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">月</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">日</span></p></td>
</tr>
<tr>
    <td width=49 rowspan=3 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
                <span style="font-size:10.0pt;font-family:'宋体'">审<br>批</span>
            </b>
        </p>
    </td>
    <td width=342 colspan=12 rowspan=2 valign=top style='width:256.6pt;
border:none;border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'">副总经理意见：</span><span
            lang=EN-US style='font-size:10.0pt'>&nbsp;&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">□同意</span><span style='font-size:9.0pt'> </span><span
            style="font-size:9.0pt;font-family:'宋体'">□不同意</span></p>

        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">意见：</span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'宋体'">总经理意见：</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:9.0pt;font-family:'宋体'">□同意</span><span style='font-size:9.0pt'> </span><span
            style="font-size:9.0pt;font-family:'宋体'">□不同意</span><span
            lang=EN-US style='font-size:10.0pt'>&nbsp;&nbsp;&nbsp; </span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border:none;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US>&nbsp;</span></p></td>
</tr>
<tr style='height:41.05pt'>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
  padding:0cm 0cm 0cm 0cm;height:41.05pt'>
        <p>
            <span style="font-size:10.0pt;font-family:'宋体'">意见：</span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
padding:0cm 0cm 0cm 0cm;height:41.05pt'><p><span lang=EN-US>&nbsp;</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border:none;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;height:41.05pt'><p><span
            lang=EN-US>&nbsp;</span></p></td>
</tr>
<tr>
    <td width=107 height="16" colspan=3 valign=top style='width:80.6pt;border:none;
  border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'>
        <p align=left style='text-align:left'>
    <span lang=EN-US
          style='font-size:10.0pt'>&nbsp;</span>
        </p>
    </td>
    <td width=120 colspan=4 valign=top style='width:90.25pt;border:none;
border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'">签字：</span></p></td>
    <td width=114 colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span lang=EN-US
                                                                                       style='font-size:10.0pt'>&nbsp;</span><span
            lang=EN-US style='font-size:9.0pt'>&nbsp;</span><span
            style="font-size:9.0pt;font-family:'宋体'">年</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">月</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">日</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'"> </span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'宋体'">签字：</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'宋体'">年</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">月</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'宋体'">日</span></p></td>
</tr>
<tr style='height:26.75pt'>
    <td width=49 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:26.75pt'>
        <p align=center style='text-align:center'>
            <b>
      <span
              style="font-size:10.0pt;font-family:'宋体'">证明
      </span>
            </b>
            <b>
      <span lang=EN-US
            style='font-size:10.0pt'>
        <br>
      </span>
            </b>
            <b>
                <span style="font-size:10.0pt;font-family:'宋体'">文件</span>
            </b>
        </p>
    </td>
    <td width=107 colspan=3 style='width:80.6pt;border-top:none;border-left:none;
border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm;height:26.75pt'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'宋体'">身份证复印本</span></p></td>
    <td width=120 colspan=4 style='width:90.25pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm;height:26.75pt'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'宋体'">工卡复印本</span></p></td>
    <td width=114 colspan=5 style='width:85.75pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm;height:26.75pt'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'宋体'">人力提供员工信息</span></p></td>
    <td width=119 colspan=3 style='width:89.25pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm;height:26.75pt'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'宋体'">信用查询报告</span></p></td>
    <td width=245 colspan=5 valign=top style='width:183.7pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm;height:26.75pt'><p><span lang=EN-US style='font-size:8.0pt'>&nbsp;</span></p></td>
</tr>
<tr height=0>
    <td width=49 style='border:none'></td>
    <td width=57 style='border:none'></td>
    <td width=13 style='border:none'></td>
    <td width=37 style='border:none'></td>
    <td width=46 style='border:none'></td>
    <td width=4 style='border:none'></td>
    <td width=57 style='border:none'></td>
    <td width=12 style='border:none'></td>
    <td width=26 style='border:none'></td>
    <td width=43 style='border:none'></td>
    <td width=8 style='border:none'></td>
    <td width=33 style='border:none'></td>
    <td width=5 style='border:none'></td>
    <td width=37 style='border:none'></td>
    <td width=4 style='border:none'></td>
    <td width=78 style='border:none'></td>
    <td width=95 style='border:none'></td>
    <td width=1 style='border:none'></td>
    <td width=23 style='border:none'></td>
    <td width=66 style='border:none'></td>
    <td width=60 style='border:none'></td>
</tr>
</table>
</div>
</div>
</body>
</html>
<%}%>