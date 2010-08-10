<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.util.Workflow" %>
<%@ page import="zt.cms.xf.XFConf" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.cmsi.pub.define.BMRouteBindNode" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Vector" %>
<%--
===============================================
Title: 消费信贷-个人消费分期付款申请书
Description: 个人消费分期付款申请书。
 * @version  $Revision: 1.0 $  $Date: 2009/03/02 08:20:31 $
 * @author liujian
 * <p/>修改：$Author: liuj $
===============================================
--%>
<%
    request.setCharacterEncoding("GBK");
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (um == null) {
//        response.sendRedirect("../fcworkbench/error.jsp");
//    }

    String SID = (String) session.getAttribute("SID");                   //合作商城编号
    String ORDERNO = (String) session.getAttribute("ORDERNO");           //合作商城单号
    String REQUESTTIME = (String) session.getAttribute("REQUESTTIME");  //合作商城定单生成时间
    String APPNO = request.getParameter("APPNO");         //申请单编号
    String IDTYPE = request.getParameter("IDTYPE");       //证件名称
    String ID = request.getParameter("ID");                //证件号码
    String NAME = request.getParameter("NAME");            //客户名称 desc=企业(个人)名称
    String APPTYPE = request.getParameter("APPTYPE");     //申请类型
    String APPSTATUS = request.getParameter("APPSTATUS"); //申请状态
    String PASSWORD = request.getParameter("PASSWORD");   //密码

    String strGoUrl = request.getParameter("goUrl");      //关闭页面标志，弹出页面关闭，非弹出返回主页面
    String closeClick = "pageWinClose();";
    String ContractTMP = "./合同普通.htm";
    if (strGoUrl != null) closeClick = "gopage('" + strGoUrl + "')";

    if (APPNO == null && (ID == null || IDTYPE == null)) {
        session.setAttribute("msg", "没有发现传送入的参数！");
        response.sendRedirect("../showinfo.jsp");
    } else {
        APPNO = (APPNO == null) ? "" : APPNO.trim();
        ID = (ID == null) ? "" : ID.trim();
        IDTYPE = (IDTYPE == null) ? "" : IDTYPE.trim();


        ConnectionManager manager = ConnectionManager.getInstance();
        String sql1 = "", sql2 = "";
        if (!APPNO.equals("")) {
            sql1 = "select c.CLIENTNO,to_char(c.BIRTHDAY,'yyyyMMdd') BIRTHDAY,c.GENDER,c.NATIONALITY,c.MARRIAGESTATUS,c.HUKOUADDRESS,c.CURRENTADDRESS," +
                    "c.COMPANY,c.TITLE,c.QUALIFICATION,c.EDULEVEL,c.PHONE1,c.PHONE2," +
                    "c.PHONE3,c.NAME,c.CLIENTTYPE,c.DEGREETYPE,c.COMADDR,c.SERVFROM,c.RESIDENCEADR,c.HOUSINGSTS," +
                    "c.HEALTHSTATUS,c.MONTHLYPAY,c.BURDENSTATUS,c.EMPNO,c.SOCIALSECURITY,c.LIVEFROM,c.PC,c.COMPC,c.RESDPC,c.RESDADDR,c.EMAIL," +
                    "p.NAME  PNAME ,p.IDTYPE PIDTYPE ,p.ID PID ,p.COMPANY PCOMPANY ,p.TITLE PTITLE ,p.PHONE1 PPHONE1 ," +
                    "p.PHONE3 PPHONE3 ,p.CLIENTTYPE PCLIENTTYPE ,p.SERVFROM PSERVFROM ,p.MONTHLYPAY PMONTHLYPAY ," +
                    "p.LIVEFROM PLIVEFROM," +
                    "m.CHANNEL,m.COMMNAME,m.COMMTYPE,m.ADDR,m.NUM,m.AMT,m.RECEIVEAMT,m.APPAMT,m.DIVID," +
                    "d.ACTOPENINGBANK,d.BANKACTNO,d.XY,d.XYR,d.DY,d.DYW,d.ZY,d.ZYW,d.BZ,d.BZR,d.CREDITTYPE,d.MONPAYAMT," +
                    "d.LINKMAN,d.LINKMANGENDER,d.LINKMANPHONE1,d.LINKMANPHONE2,d.APPRELATION,d.LINKMANADD,d.LINKMANCOMPANY,d.ACTOPENINGBANK_UD," +
                    "a.IDTYPE,a.ID,to_char(a.APPDATE,'yyyyMMdd') as APPDATE,a.APPTYPE,a.APPSTATUS,a.SID,a.ORDERNO,a.REQUESTTIME " +
                    "from XFCLIENT c,XFAPPCOMM m,XFAPPADD d,XFAPP a " +
                    "left outer join XFCLIENT p on a.APPNO=p.APPNO and p.XFCLTP='2' " +
                    "where a.APPNO='" + APPNO + "' and a.APPNO=c.APPNO and c.XFCLTP='1' " +
                    "and a.APPNO=m.APPNO " +
                    "and a.APPNO=d.APPNO";
        } else if (!ID.equals("") && !IDTYPE.equals("")) {
            sql1 = "select c.CLIENTNO,to_char(c.BIRTHDAY,'yyyyMMdd') BIRTHDAY,c.GENDER,c.NATIONALITY,c.MARRIAGESTATUS,c.HUKOUADDRESS,c.CURRENTADDRESS," +
                    "c.COMPANY,c.TITLE,c.QUALIFICATION,c.EDULEVEL,c.PHONE1,c.PHONE2," +
                    "c.PHONE3,c.NAME,c.CLIENTTYPE,c.DEGREETYPE,c.COMADDR,c.SERVFROM,c.RESIDENCEADR,c.HOUSINGSTS," +
                    "c.HEALTHSTATUS,c.MONTHLYPAY,c.BURDENSTATUS,c.EMPNO,c.SOCIALSECURITY,c.LIVEFROM,c.PC,c.COMPC,c.RESDPC,c.RESDADDR,c.EMAIL," +
                    "p.NAME  PNAME ,p.IDTYPE PIDTYPE ,p.ID PID ,p.COMPANY PCOMPANY ,p.TITLE PTITLE ,p.PHONE1 PPHONE1 ," +
                    "p.PHONE3 PPHONE3 ,p.CLIENTTYPE PCLIENTTYPE ,p.SERVFROM PSERVFROM ,p.MONTHLYPAY PMONTHLYPAY ," +
                    "p.LIVEFROM PLIVEFROM," +
                    "'' CHANNEL,'' COMMNAME,'' COMMTYPE,ADDR,'' NUM,'' AMT,'' RECEIVEAMT,'' APPAMT,'' DIVID," +
                    "d.ACTOPENINGBANK,d.BANKACTNO,'' XY,'' XYR,'' DY,'' DYW,'' ZY,'' ZYW,'' BZ,'' BZR,'' CREDITTYPE,'' MONPAYAMT," +
                    "d.LINKMAN,d.LINKMANGENDER,d.LINKMANPHONE1,d.LINKMANPHONE2,d.APPRELATION,d.LINKMANADD,d.LINKMANCOMPANY,d.ACTOPENINGBANK_UD," +
                    "a.IDTYPE,a.ID,to_char(SYSDATE,'yyyyMMdd') APPDATE,'' APPTYPE,'' APPSTATUS,'' SID,'' ORDERNO,'' REQUESTTIME " +
                    "from CMINDVCLIENT c,XFAPPCOMM m,XFAPPADD d,XFAPP a " +
                    "left outer join XFCLIENT p on a.APPNO=p.APPNO and p.XFCLTP='2' " +
                    "where a.IDTYPE='" + IDTYPE + "' and a.ID='" + ID + "' and a.IDTYPE=c.IDTYPE and a.ID=c.ID " +
                    "and a.APPNO=m.APPNO " +
                    "and a.APPNO=d.APPNO " +
                    "and rownum=1 order by a.APPNO desc";

            sql2 = "select CLIENTNO,NAME,to_char(BIRTHDAY,'yyyyMMdd') BIRTHDAY,GENDER,NATIONALITY,MARRIAGESTATUS,HUKOUADDRESS,CURRENTADDRESS," +
                    "COMPANY,TITLE,QUALIFICATION,EDULEVEL,PHONE1,PHONE2," +
                    "PHONE3,NAME,CLIENTTYPE,DEGREETYPE,COMADDR,SERVFROM,RESIDENCEADR,HOUSINGSTS," +
                    "HEALTHSTATUS,MONTHLYPAY,BURDENSTATUS,EMPNO,SOCIALSECURITY,LIVEFROM,PC,COMPC,RESDPC,RESDADDR,EMAIL,ACTOPENINGBANK,BANKACTNO,to_char(SYSDATE,'yyyyMMdd') APPDATE " +
                    "from CMINDVCLIENT " +
                    "where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and rownum=1 order by CLIENTNO desc";
        }


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

        String PNAME = "";                     //配偶名称
        String PIDTYPE = "";                   //配偶证件名称
        String PID = "";                       //配偶证件号码
        String PCOMPANY = "";                  //配偶工作单位
        String PTITLE = "";                    //配偶职务 enum=Title
        String PPHONE1 = "";                   //配偶移动电话
        String PPHONE3 = "";                   //配偶办公电话
        String PCLIENTTYPE = "";               //配偶客户性质(单位性质) enum=ClientType1
        String PSERVFROM = "";                 //配偶现单位工作时间
        String PMONTHLYPAY = "";               //配偶个人月收入
        String PLIVEFROM = "";                 //配偶本地居住时间

        String CHANNEL = (String) session.getAttribute("CHANNEL");  //销售单位(渠道名称)
        String COMMNAME = (String) session.getAttribute("COMMATTR");//商品名称
        String COMMTYPE = "";                  //商品型号
        String ADDR = "";                      //配送地址
        String NUM = (String) session.getAttribute("NUMBER");       //购买数量
        String AMT = (String) session.getAttribute("AMT");           //总金额
        String RECEIVEAMT = "";                //已付金额
        String APPAMT = "";                    //分期金额
        String DIVID = "";                     //分期期数

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
        String ACTOPENINGBANK_UD = "";         //开户行名称（电汇、网银——录入名称）

        String APPDATE = "";                   //申请日期


        boolean ifErrClient = false;
        CachedRowSet crs = null;
        if (!sql1.equals("")) crs = manager.getRs(sql1);
        if (crs != null && crs.size() > 0) {
            crs.next();
            //if (NAME.compareTo(crs.getString("NAME")) != 0) ifErrClient = true;  //客户名称 desc=企业(个人)名称)
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

            PNAME = crs.getString("PNAME");                      //配偶名称
            PIDTYPE = crs.getString("PIDTYPE");                  //配偶证件名称
            PID = crs.getString("PID");                          //配偶证件号码
            PCOMPANY = crs.getString("PCOMPANY");                //配偶工作单位
            PTITLE = crs.getString("PTITLE");                    //配偶职务 enum=Title
            PPHONE1 = crs.getString("PPHONE1");                  //配偶移动电话
            PPHONE3 = crs.getString("PPHONE3");                  //配偶办公电话
            PCLIENTTYPE = crs.getString("PCLIENTTYPE");         //配偶客户性质(单位性质) enum=ClientType1
            PSERVFROM = crs.getString("PSERVFROM");             //配偶现单位工作时间
            PMONTHLYPAY = crs.getString("PMONTHLYPAY");         //配偶个人月收入
            PLIVEFROM = crs.getString("PLIVEFROM");             //配偶本地居住时间

            CHANNEL = crs.getString("CHANNEL");                  //销售单位(渠道名称)
            COMMNAME = crs.getString("COMMNAME");                //商品名称
            COMMTYPE = crs.getString("COMMTYPE");                //商品型号
            ADDR = crs.getString("ADDR");                         //配送地址
            NUM = crs.getString("NUM");                           //购买数量
            AMT = crs.getString("AMT");                           //总金额
            RECEIVEAMT = crs.getString("RECEIVEAMT");            //已付金额
            APPAMT = crs.getString("APPAMT");                     //分期金额
            DIVID = crs.getString("DIVID");                       //分期期数

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
            ACTOPENINGBANK_UD = crs.getString("ACTOPENINGBANK_UD");    //开户行名称（电汇、网银——录入名称）


            ID = crs.getString("ID");                              //证件号码
            IDTYPE = crs.getString("IDTYPE");                     //证件名称
            APPDATE = crs.getString("APPDATE");                   //申请日期
            APPTYPE = crs.getString("APPTYPE");                   //申请类型
            APPSTATUS = crs.getString("APPSTATUS");               //申请状态


            SID = crs.getString("SID");                            //合作商城编号
            ORDERNO = crs.getString("ORDERNO");                    //合作商城单号
            REQUESTTIME = crs.getString("REQUESTTIME");           //合作商城定单生成时间
        } else if (!sql2.equals("")) {
            crs = manager.getRs(sql2);
            if (crs != null && crs.size() > 0) {
                crs.next();
                CLIENTNO = crs.getString("CLIENTNO");                //客户号
                NAME = crs.getString("NAME");                        //客户名称 desc=企业(个人)名称)
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
                LIVEFROM = crs.getString("LIVEFROM");                //本地居住时间
                PC = crs.getString("PC");                             //住宅邮编
                COMPC = crs.getString("COMPC");                      //单位邮编
                RESDPC = crs.getString("RESDPC");                    //寄送地址邮编
                RESDADDR = crs.getString("RESDADDR");                //寄送地址
                EMAIL = crs.getString("EMAIL");                      //电子邮件
                ACTOPENINGBANK = crs.getString("ACTOPENINGBANK");   //开户行 enum=Bank
                BANKACTNO = crs.getString("BANKACTNO");              //还款帐号
                APPDATE = crs.getString("APPDATE");                  //申请日期

//                ID = crs.getString("ID");                              //证件号码
//                IDTYPE = crs.getString("IDTYPE");                     //证件名称
            }
        }


        String S_ID = (String) session.getAttribute("SID");                   //合作商城编号
        S_ID = (S_ID == null) ? "" : S_ID;
        if (!S_ID.equals("")) {
            SID = (String) session.getAttribute("SID");                   //合作商城编号
            ORDERNO = (String) session.getAttribute("ORDERNO");          //合作商城单号
            REQUESTTIME = (String) session.getAttribute("REQUESTTIME"); //合作商城定单生成时间
            CHANNEL = (String) session.getAttribute("CHANNEL");          //销售单位(渠道名称)
            COMMNAME = (String) session.getAttribute("COMMATTR");        //商品名称及型号
            NUM = (String) session.getAttribute("NUMBER");               //商品数量
            AMT = (String) session.getAttribute("AMT");                   //订单金额
            AMT = (AMT == null) ? "" : AMT.substring(0, AMT.length() - 2) + "." + AMT.substring(AMT.length() - 2, AMT.length());
        }

        NAME = (NAME == null) ? "" : NAME.trim();
        PNAME = (PNAME == null) ? "" : PNAME.trim();
        APPSTATUS = (APPSTATUS == null) ? "" : APPSTATUS;
        BIRTHDAY = (BIRTHDAY == null) ? "" : BIRTHDAY;

        SID = (SID == null) ? "" : SID.trim();                          //合作商城编号——“”：直接|001：海尔商城
        ORDERNO = (ORDERNO == null) ? "" : ORDERNO;
        REQUESTTIME = (REQUESTTIME == null) ? "" : REQUESTTIME.trim();
        CHANNEL = (CHANNEL == null) ? "" : CHANNEL.trim();
        COMMNAME = (COMMNAME == null) ? "" : COMMNAME.trim();
        NUM = (NUM == null) ? "" : NUM.trim();
        AMT = (AMT == null) ? "" : AMT.trim();


        String readonly = "";
        String readonly_input = "readonly";
        String submit = "class='btn_2k3'";
        String title = "个人消费分期付款申请书";

        if (ifErrClient) {
            String mess = "客户信息错误，此证件已被其他客户使用！";
            mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
            session.setAttribute("msg", mess);
            response.sendRedirect("../showinfo.jsp");
        }
%>
<html>
<head id="head1">
    <title>消费信贷</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <style type="text/css">
        <!--
        body {
            margin-top: 0px;
            margin-left:0px;
            margin-right:0px;
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

    function doPrint() {
        var str = "非常感谢您选择海尔财务公司的分期付款业务。下面是您分期申请需要准备的材料：\n";
        str += "\n";
        str += "* 身份证明复印件两份，并签字（第二代身份证请正反两面都复印）\n";
        str += "   员工卡复印件一份 （内部员工）\n";
        str += "* 消费分期付款申请书一份，签字\n";
        str += "* 信用记录查询许可书一份，签字\n";
        str += "* 建行还款账号签字复印件、支付宝或快钱账号截图的签字复印件\n";
        str += "\n";
        str += "如有共同还款人参与，请另附：\n";
        str += " 1、共同还款人的身份证件复印件两份，并签字（第二代身份证请正反两面都复印）\n";
        str += " 2、共同还款人的信用记录查询许可书一份，签字 \n\n";
        str += "————————————————————————————————————————————\n\n";
        var str2 = "打印后，请在打印出的申请单及信用调查授权书上分别签字后，\n连同要求的证明文件复印件寄送给我们，我们将在两个工作日内审核完成您的申请！";
        if (confirm(str + str2)) {
            getObject("HT" + 2).style.display = "none";
            getObject("HA" + 1).style.display = "block";
            getObject("HA" + 2).style.display = "block";
            //getObject("HA" + 3).style.display = "block";
            //getObject("HA" + 4).style.display = "block";
            getObject("HA" + 5).style.display = "block";
            getObject("HA" + 6).style.display = "block";
            getObject("HA" + 7).style.display = "block";
            getObject("HA" + 8).style.display = "block";
            getObject("HA" + 9).style.display = "block";
            getObject("HA" + 10).style.display = "block";

            pagesetup_null();
            var str = "<html><head>";
            str += document.all.head1.innerHTML;
            str += "</head><body>";
            str += document.all.winform.innerHTML;
            str += "<script language=\"javascript\">window.print();<\/script>";
            str += "</body></html>";
            //str += "<script language=\"javascript\">document.all.wb.ExecWB(7,1);<\/script>";
            document.open();
            document.write(str);
            document.close();
            pagesetup_default();
        }
    }
    function window.onbeforeprint() {
        window.document.url = "";
    }
    function window.onafterprint() {
        //history.go(-1);
        window.location= window.location.toString().replace("APPNO=&","APPNO="+document.getElementById("APPNO").value+"&");
    }

</SCRIPT>
<!--media=print 这个属性说明可以在打印时有效-->
<!--希望打印时不显示的内容设置class="Noprint"样式-->
<!--希望人为设置分页的位置设置class="PageNext"样式-->
<style media=print>
    .Noprint {
        display: none;
    }

    .PageNext {
        page-break-after: always;
    }

    table {
        border-color: #000 !important;
        color: #000 !important;
    }

    tr {
        border-color: #000 !important;
        color: #000 !important;
    }

    td {
        border-color: #000 !important;
        color: #000 !important;
    }
</style>

<body>
<div style="width:100%">
<table height="42px" width="100%" border="0" align="left" cellpadding="2" cellspacing="2"
          >
        <tr align="left">
            <td width="30%" style="BACKGROUND: url(../images/headlogo.JPG) no-repeat;"></td>

        </tr>
    </table>
</div>
<div class="navNo"></div>
<br>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr class='page_form_tr'>
<td align="center" valign="middle">
<table height="325" border="0" align="center" cellspacing="0" cellpadding="0" bordercolor="#816A82" bgcolor="#ffffff"
       width="678">
<tr align="left">
    <td height="30" bgcolor="#A4AEB5"><img src="../images/form/xing1.jpg" align="absmiddle"> <font size="2"
                                                                                                   color="#FFFFFF"><b>海尔集团财务有限责任公司个人消费分期付款申请书</b></font>
        <img src="../images/form/xing1.jpg" align="absmiddle"></td>
</tr>
<tr align="center">
<td height="260" valign="middle">
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
<tr class='page_form_tr'>
<td width="20">&nbsp;</td>
<td align="center" valign="middle" width="638">
<script src='../js/main.js' type='text/javascript'></script>
<script src='../js/check.js' type='text/javascript'></script>
<script src='../js/meizzDate.js' type='text/javascript'></script>
<script src='../js/checkID2.js' type='text/javascript'></script>
<script src='../js/pagebutton.js' type='text/javascript'></script>
<script src='../js/xf/xfutil.js' type='text/javascript'></script>

<script src='../js/checkReName.js' type='text/javascript'></script>



<%--<form id='winform' method='post' action='./application_save.jsp' target="_blank">--%>
<form id='winform' method='post' action='./application_save.jsp'>
<%--<OBJECT id=wb height=0 width=0 classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 name=wb></OBJECT>--%>
<table class='page_form_regTable' id='page_form_table' width="638" cellspacing="1" cellpadding="0" border=0>
<col width="69"/>
<col width="110"/>
<col width="160"/>
<col width="31"/>
<col width="95"/>
<col width="160"/>
<col width="13"/>
<tr class='page_form_tr'>
    <td colspan="7" align="center" class="page_form_List_title">海尔集团财务有限责任公司个人消费分期付款申请书</td>
</tr>
<tr class='page_form_tr'>
    <td colspan="7" style="padding:0px; margin:0px;" height="20" valign="bottom">
        <table width="100%" cellspacing="0" cellpadding="0" border=0 style="font-size:9pt">
            <tr class='page_form_tr'>
                <td>
                    <input type="hidden" name="APPACTFLAG" value="1"><%--执行动作标志：1、正常，2、退回，3、作废--%>
                    <input type="hidden" name="PASSWORD" value="<%=PASSWORD%>">
                    <input type="hidden" name="CLIENTNO" value="<%=CLIENTNO%>">
                    <input type="hidden" name="APPNO" value="<%=APPNO%>" id="APPNO">
                    <input type="hidden" name="APPSTATUS" value="<%=APPSTATUS==null?"":APPSTATUS%>" id="APPSTATUS">

                    <input type="hidden" name="SID" value="<%=SID%>">
                    <input type="hidden" name="ORDERNO" value="<%=ORDERNO%>">
                    <input type="hidden" name="REQUESTTIME" value="<%=REQUESTTIME%>">
                    申请日期:<%
                    if (um == null) {
                        out.print(APPDATE == null ? "" : APPDATE);
                    } else {
                %><input type="text" <%=readonly_input%> name="APPDATE"
                         <%--value="<%=APPDATE==null?"":DBUtil.to_Date(APPDATE)%>"--%>
                         value="<%=APPDATE==null?"":APPDATE%>"
                         class="page_form_text" style="width:63px">
                    <input type="button" value="…" class="page_form_refbutton" onClick="setday(this,winform.APPDATE)">
                    <%}%>
                    &nbsp;申请状态:<%=(APPSTATUS == null || APPSTATUS.equals("")) ? "" : level.getEnumItemName("AppStatus", APPSTATUS)%>
                </td>
                <td align="right"><%if (SID.equals("001")) {%>商城单号:<%=ORDERNO%><%;ContractTMP = "./合同商城.htm";}%></td>
                <td align="right">申请单号:<%
                    String XFAPPNO = "";
                    if (!APPNO.equals("")) {
                        out.print(APPNO);
                        XFAPPNO = APPNO;
                    } else {
                        String appsql = " select count(*) from XFAPP where IDTYPE='" + IDTYPE + "' and ID='" + ID + "'";
                        crs = manager.getRs(appsql);
                        String tmp = "0000000000000000000000";
                        XFAPPNO = "1";
                        if (crs.next()) {
                            XFAPPNO = String.valueOf(crs.getInt(1) + 1);
                        }
                        XFAPPNO = ID + tmp.substring(ID.length(), 22 - XFAPPNO.length()) + XFAPPNO;
                        out.print(XFAPPNO);
                    }
                %>&nbsp;
                    <input type="hidden" name="XFAPPNO" value="<%=XFAPPNO%>">
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="11" class="page_left_table_title" nowrap>申请人情况</td>
    <td class="page_form_title_td" nowrap>&nbsp;姓&nbsp;&nbsp;名：</td>
    <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="NAME"
                                           value="<%=NAME==null?"":NAME%>"
                                           class="page_form_text" maxlength="40"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
    <td class="page_form_title_td" nowrap>&nbsp;性&nbsp;&nbsp;别：</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("GENDER", "Gender", GENDER)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;手&nbsp;&nbsp;机：</td>
    <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="PHONE1"
                                           value="<%=PHONE1==null?"":PHONE1%>"
                                           class="page_form_text" maxlength="15"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
    <td class="page_form_title_td" nowrap>&nbsp;员工卡号码：</td>
    <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="EMPNO"
                                           value="<%=EMPNO==null?"":EMPNO%>" class="page_form_text" maxlength="8"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;身份证件名称：</td>
    <td colspan="2" nowrap class="page_form_td">
        <%--<select name="IDTYPE" class="page_form_select">--%>
        <%--<option value='0'>身份证</option>--%>
        <%--</select>--%>
        <%=level.levelHere("IDTYPE", "IDType", IDTYPE)%>
    </td>
    <td class="page_form_title_td" nowrap>&nbsp;证件号码：</td>
    <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="ID" id="ID"
                                           value="<%=ID==null?"":ID%>" class="page_form_text"
                                           onblur="if(checkIDCard(document.getElementById('IDTYPE'),this))getbirthday(this.value,'BIRTHDAY');"
                                           maxlength="18"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;出生日期：</td>
    <td class="page_form_td" nowrap><input type="text" <%=readonly_input%> name="BIRTHDAY"
                                           value="<%=BIRTHDAY.equals("")?"":BIRTHDAY%>"
                                           <%--value="<%=BIRTHDAY.equals("")?"":DBUtil.to_Date(BIRTHDAY)%>"--%>
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap><input type="button" value="…" class="page_form_refbutton"
                                           onClick="setday(this,winform.BIRTHDAY)"></td>
    <td class="page_form_title_td" nowrap>&nbsp;健康状况：</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("HEALTHSTATUS", "HealthStatus", HEALTHSTATUS)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;婚姻状况：</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("MARRIAGESTATUS", "MarriageStatus", MARRIAGESTATUS)%>
    </td>
    <td class="page_form_title_td" nowrap>&nbsp;是否有子女：</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("BURDENSTATUS", "BurdenStatus", BURDENSTATUS)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;户籍所在地：</td>
    <td colspan="5" class="page_form_td" nowrap><%=level.radioHere("RESIDENCEADR", "ResidenceADR", RESIDENCEADR)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;家庭住址：</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="CURRENTADDRESS"
               value="<%=CURRENTADDRESS==null?"":CURRENTADDRESS%>"
               class="page_form_text" style="width:100%" maxlength="40"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;住宅邮编：</td>
    <td colspan="2" class="page_form_td"><input type="text" <%=readonly%> name="PC"
                                                value="<%=PC==null?"":PC%>"
                                                class="page_form_text" maxlength="6"></td>
    <td class="page_form_title_td" nowrap>&nbsp;电子邮箱：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="EMAIL"
               value="<%=EMAIL==null?"":EMAIL%>"
               class="page_form_text" maxlength="40"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;家庭电话：</td>
    <td colspan="2" class="page_form_td"><input type="text" <%=readonly%> name="PHONE2"
                                                value="<%=PHONE2==null?"":PHONE2%>"
                                                class="page_form_text" maxlength="15"></td>
    <td class="page_form_title_td" nowrap>&nbsp;本地居住时间：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="LIVEFROM"
               value="<%=LIVEFROM==null?"":LIVEFROM%>"
               class="page_form_text" maxlength="20"></td>
    <td class="page_form_td">年</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;受教育程度：</td>
    <td colspan="5" class="page_form_td" nowrap><%=level.radioHere("EDULEVEL", "EduLevel", EDULEVEL)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;职&nbsp;&nbsp;称：</td>
    <td colspan="5" class="page_form_td"
        nowrap><%=level.radioHere("QUALIFICATION", "Qualification", QUALIFICATION)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="8" class="page_left_table_title">工作资料：</td>
    <td class="page_form_title_td" nowrap>&nbsp;工作单位：</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="COMPANY"
               value="<%=COMPANY==null?"":COMPANY%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;单位电话：</td>
    <td colspan="2" class="page_form_td">
        <input type="text" <%=readonly%> name="PHONE3"
               value="<%=PHONE3==null?"":PHONE3%>"
               class="page_form_text" maxlength="15">
    </td>
    <td class="page_form_title_td" nowrap>&nbsp;单位邮编：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="COMPC"
               value="<%=COMPC==null?"":COMPC%>"
               class="page_form_text" maxlength="6">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;单位地址：</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="COMADDR"
               value="<%=COMADDR==null?"":COMADDR%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;单位性质：</td>
    <td colspan="5" class="page_form_td" nowrap><%=level.radioHere("CLIENTTYPE", "ClientType1", CLIENTTYPE)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;职&nbsp;&nbsp;务：</td>
    <td colspan="5" class="page_form_td" nowrap><%=level.levelHere("TITLE", "Title", TITLE)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;现单位工作时间：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="SERVFROM"
               value="<%=SERVFROM==null?"":SERVFROM%>"
               class="page_form_text" maxlength="20"></td>
    <td colspan="4" class="page_form_td">年</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;个人月收入：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="MONTHLYPAY"
               value="<%=MONTHLYPAY==null?"":MONTHLYPAY%>"
               class="page_form_text" maxlength="12"></td>
    <td colspan="4" class="page_form_td">元</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;建立社会福利<br>&nbsp;保障制度情况：</td>
    <td colspan="5" class="page_form_td"
        nowrap><%=level.checkHere("SOCIALSECURITY", "SocialSecurity", SOCIALSECURITY)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>

<tr class='page_form_tr' onClick="showList(1);" id="HT2">
    <td class="page_slide_table_title" colspan="7" height="18" id="HT1" title="点击收缩" onMouseOver="mOvr(this);"
        onMouseOut="mOut(this);">
        若个人还款压力大，需配偶共同还款，请填写配偶基本信息 -- ↓<font color="red">(点击展开)</font>
    </td>
</tr>
<tr>
    <td colspan="7" bgcolor="#3366FF" style="margin:0; padding:0" width="100%">
        <table class="page_form_table_slide" width="100%" id="HA1" style="display:none;border:1px dashed #FDF8DF"
               cellspacing="0" cellpadding="0">
            <COL width=66>
            <COL width=111>
            <COL width=160>
            <COL width=31>
            <COL width=96>
            <COL width=160>
            <COL width=13>
            <tr class='page_form_tr'>
                <td rowspan="8" class="page_left_table_title" nowrap>配偶基本情况(仅在配偶为共同还款人情况下填列)</td>
                <td class="page_form_title_td" nowrap>&nbsp;配偶姓名：</td>
                <td colspan="5" class="page_form_td" nowrap><input type="text" <%=readonly%> name="PNAME"
                                                                   value="<%=PNAME==null?"":PNAME%>"
                                                                   class="page_form_text" maxlength="40"></td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>配偶身份证件名称：</td>
                <td class="page_form_td" colspan="2" nowrap><%=level.levelHere("PIDTYPE", "IDType", PIDTYPE)%>
                </td>
                <td class="page_form_title_td" nowrap>&nbsp;配偶证件号码：</td>
                <td class="page_form_td">
                    <input type="text" <%=readonly%> name="PID" id="PID" value="<%=PID==null?"":PID%>"
                           onblur="if(this.value.Trim()!=''&&document.getElementById('PNAME').value.Trim()==''){alert('配偶姓名不能为空！');document.getElementById('PNAME').focus();}else checkIDCard(document.getElementById('PIDTYPE'),this);"
                           class="page_form_text" maxlength="18"></td>
                <td class="page_form_td">&nbsp;</td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;工作单位：</td>
                <td colspan="4" class="page_form_td">
                    <input type="text" <%=readonly%> name="PCOMPANY"
                           value="<%=PCOMPANY==null?"":PCOMPANY%>"
                           class="page_form_text" style="width:337pt" maxlength="40"></td>
                <td class="page_form_td">&nbsp;</td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;单位电话：</td>
                <td colspan="5" class="page_form_td">
                    <input type="text" <%=readonly%> name="PPHONE3"
                           value="<%=PPHONE3==null?"":PPHONE3%>"
                           class="page_form_text" maxlength="15">
                </td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;单位性质：</td>
                <td colspan="5" class="page_form_td"
                    nowrap><%=level.radioHere("PCLIENTTYPE", "ClientType1", PCLIENTTYPE)%>
                </td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;职&nbsp;&nbsp;务：</td>
                <td colspan="5" class="page_form_td" nowrap><%=level.levelHere("PTITLE", "Title", PTITLE)%>
                </td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;现单位工作时间：</td>
                <td class="page_form_td">
                    <input type="text" <%=readonly%> name="PSERVFROM"
                           value="<%=PSERVFROM==null?"":PSERVFROM%>"
                           class="page_form_text" maxlength="20"></td>
                <td class="page_form_td">年</td>
                <td class="page_form_title_td" nowrap>&nbsp;本地居住时间：</td>
                <td class="page_form_td">
                    <input type="text" <%=readonly%> name="PLIVEFROM"
                           value="<%=PLIVEFROM==null?"":PLIVEFROM%>"
                           class="page_form_text" maxlength="20"></td>
                <td class="page_form_td">年</td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;个人电话：</td>
                <td class="page_form_td">
                    <input type="text" <%=readonly%> name="PPHONE1"
                           value="<%=PPHONE1==null?"":PPHONE1%>"
                           class="page_form_text" maxlength="15"></td>
                <td class="page_form_td">&nbsp;</td>
                <td class="page_form_title_td" nowrap>&nbsp;个人月收入：</td>
                <td class="page_form_td">
                    <input type="text" <%=readonly%> name="PMONTHLYPAY"
                           value="<%=PMONTHLYPAY==null?"":PMONTHLYPAY%>"
                           class="page_form_text" maxlength="12"></td>
                <td class="page_form_td">元</td>
            </tr>
        </table>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="5" class="page_left_table_title">拟购商品情况</td>
    <td class="page_form_title_td">销售单位（名称）：</td>
    <td colspan="4" class="page_form_td">
        <%--<input type="text" <%=readonly%> name="CHANNEL"--%>
        <%--value="<%=CHANNEL==null?"":CHANNEL%>"--%>
        <%--class="page_form_text" style="width:100%" maxlength="80">--%>
        <textarea name="CHANNEL" class="page_form_textfield" style="width:100%" rows="3"
                  onkeyup="limitTextarea(this)"
                  onchange="limitTextarea(this)"
                  onpropertychange="limitTextarea(this)"><%=CHANNEL == null ? "" : CHANNEL.trim()%></textarea>
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td">&nbsp;拟购商品名称<br>&nbsp;及型号：</td>
    <td colspan="4" class="page_form_td">
        <%--<input type="text" <%=readonly%> name="COMMNAME"--%>
        <%--value="<%=COMMNAME==null?"":COMMNAME%>"--%>
        <%--class="page_form_text" style="width:100%" maxlength="80">--%>
        <textarea name="COMMNAME" class="page_form_textfield" style="width:100%" rows="3"
                  onkeyup="limitTextarea(this)"
                  onchange="limitTextarea(this)"
                  onpropertychange="limitTextarea(this)"
                ><%=COMMNAME == null ? "" : COMMNAME.trim()%></textarea>
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>拟购商品使用地址：</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="ADDR"
               value="<%=ADDR==null?"":ADDR%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;拟购商品数量：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="NUM"
               value="<%=NUM==null?"":NUM%>"
               class="page_form_text" maxlength="10"></td>
    <td class="page_form_td">台/套</td>
    <td class="page_form_title_td" nowrap>&nbsp;申请类型：</td>
    <td colspan="2" class="page_form_td"><%=level.radioHere("APPTYPE", "AppType", APPTYPE)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;商品总金额：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="AMT"
               value="<%=AMT==null?"":AMT%>"
               class="page_form_text" maxlength="12" onKeyUp="countAppAmt()"></td>
    <td class="page_form_td">元</td>
    <td class="page_form_title_td">&nbsp;首付款：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="RECEIVEAMT"
               value="<%=RECEIVEAMT==null?"":RECEIVEAMT%>"
               class="page_form_text" maxlength="12" onKeyUp="countAppAmt()"
               onblur="checkAmt();"></td>
    <td class="page_form_td">元</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="3" class="page_left_table_title">申请分期情况</td>
    <td class="page_form_title_td" nowrap>申请分期总金额￥：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly_input%> name="APPAMT"
               value="<%=APPAMT==null?"":APPAMT%>"
               class="page_form_text" maxlength="15"></td>
    <td class="page_form_td">元</td>
    <td class="page_form_title_td" nowrap>&nbsp;分期期限：</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("DIVID", "Divid", DIVID)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;还款方式：</td>
    <td colspan="5" nowrap class="page_form_td"><%=level.radioHere("ACTOPENINGBANK", "Bank4App", ACTOPENINGBANK)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;还款账号：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="BANKACTNO"
               value="<%=BANKACTNO==null?"":BANKACTNO%>"
               class="page_form_text" maxlength="30">
    </td>
    <td class="page_form_td">&nbsp;</td>
    <td class="page_form_title_td" nowrap><span id="BANK_UD1" style="display:none">&nbsp;开户行名称：</span></td>
    <td nowrap class="page_form_td"><input type="text" id="BANK_UD2" style="display:none" <%=readonly%>
                                           name="ACTOPENINGBANK_UD"
                                           value="<%=ACTOPENINGBANK_UD==null?"":ACTOPENINGBANK_UD%>"
                                           class="page_form_text" maxlength="40"></td>
    <td class="page_form_td">&nbsp;</td>
</tr>
</table>

<div class="PageNext" id="HA2" style="display:none;">&nbsp;</div>
<table class="page_form_regTable" width="650" cellspacing="1" cellpadding="0" border="0">
<COL width=65>
<COL width=110>
<COL width=160>
<COL width=31>
<COL width=95>
<COL width=160>
<COL width=13>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<%--<tr class='page_form_tr'>--%>
<%--<td rowspan="4" class="page_left_table_title">提供担保方式</td>--%>
<%--<td class="page_form_title_td" nowrap><label>--%>
<%--<input name="XY" type="checkbox"--%>
<%--onClick="if (this.checked==true){ document.all.XYR.readOnly=false; }else {document.all.XYR.readOnly='<%=readonly_input%>'}"--%>
<%--value="1" <%if (XY == "1") {%>"checked"<%--%>
<%--}--%>
<%--;--%>
<%--%>/>--%>
<%--信用</label>--%>
<%--&nbsp;&nbsp;信用人名称--%>
<%--</td>--%>
<%--<td colspan="3" class="page_form_td">--%>
<%--<input type="text" <%=readonly_input%> name="XYR"--%>
<%--value="<%=XYR==null?"":XYR%>"--%>
<%--class="page_form_text">--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr class='page_form_tr'>--%>
<%--<td class="page_form_title_td" nowrap><label>--%>
<%--<input name="DY" type="checkbox"--%>
<%--onClick="if (this.checked==true){ document.all.DYW.readOnly=false; }else {document.all.DYW.readOnly='<%=readonly_input%>'}"--%>
<%--value="1" <%if (DY == "1") {%>"checked"<%--%>
<%--}--%>
<%--;--%>
<%--%>/>--%>
<%--抵押</label>--%>
<%--&nbsp;&nbsp;抵押物名称--%>
<%--</td>--%>
<%--<td colspan="3" class="page_form_td">--%>
<%--<input type="text" <%=readonly_input%> name="DYW"--%>
<%--value="<%=DYW==null?"":DYW%>"--%>
<%--class="page_form_text">--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr class='page_form_tr'>--%>
<%--<td class="page_form_title_td" nowrap><label>--%>
<%--<input name="ZY" type="checkbox"--%>
<%--onClick="if (this.checked==true){ document.all.ZYW.readOnly=false; }else {document.all.ZYW.readOnly='<%=readonly_input%>'}"--%>
<%--value="1" <%if (ZY == "1") {%>"checked"<%--%>
<%--}--%>
<%--;--%>
<%--%>/>--%>
<%--质押</label>--%>
<%--&nbsp;&nbsp;质押物名称--%>
<%--</td>--%>
<%--<td colspan="3" class="page_form_td">--%>
<%--<input type="text" <%=readonly_input%> name="ZYW"--%>
<%--value="<%=ZYW==null?"":ZYW%>"--%>
<%--class="page_form_text">--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr class='page_form_tr'>--%>
<%--<td class="page_form_title_td" nowrap><label>--%>
<%--<input name="BZ" type="checkbox"--%>
<%--onClick="if (this.checked==true){ document.all.BZR.readOnly=false; }else {document.all.BZR.readOnly='<%=readonly_input%>'}"--%>
<%--value="1" <%if (BZ == "1") {%>"checked"<%--%>
<%--}--%>
<%--;--%>
<%--%>/>--%>
<%--保证</label>--%>
<%--&nbsp;&nbsp;保证人名称--%>
<%--</td>--%>
<%--<td colspan="3" class="page_form_td">--%>
<%--<input type="text" <%=readonly_input%> name="BZR"--%>
<%--value="<%=BZR==null?"":BZR%>"--%>
<%--class="page_form_text">--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr class='page_form_tr'>--%>
<%--<td class="page_button_tbl_tr" colspan="5" height="5"></td>--%>
<%--</tr>--%>
<tr class='page_form_tr'>
    <td rowspan="2" class="page_left_table_title">现有信用<br/>
        使用情况
    </td>
    <td class="page_form_title_td" nowrap>&nbsp;信用种类：</td>
    <td colspan="5" class="page_form_td" nowrap><%=level.checkHere("CREDITTYPE", "CreditType", CREDITTYPE)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;月均还款额：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="MONPAYAMT"
               value="<%=MONPAYAMT==null?"":MONPAYAMT%>"
               class="page_form_text" maxlength="12"></td>
    <td colspan="4" class="page_form_td">元</td>
</tr>

<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="2" class="page_left_table_title" nowrap>邮寄地址<br><span
            style="font-size:12px;font-weight:normal; color:red;">（信函、账单地址）</span>
    </td>
    <td class="page_form_title_td" nowrap>&nbsp;邮寄地址：</td>
    <td colspan="4" class="page_form_td" nowrap>
        <input type="text" <%=readonly%> name="RESDADDR"
               value="<%=RESDADDR==null?"":RESDADDR%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;邮政编码：</td>
    <td colspan="4" class="page_form_td"><input type="text" <%=readonly%> name="RESDPC"
                                                value="<%=RESDPC==null?"":RESDPC%>"
                                                class="page_form_text" maxlength="6"></td>

    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="5" class="page_left_table_title">第三联系人</td>
    <td class="page_form_title_td" nowrap>&nbsp;姓&nbsp;&nbsp;名：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="LINKMAN"
               value="<%=LINKMAN==null?"":LINKMAN%>"
               class="page_form_text" maxlength="40"
               onblur="checkLinkname('LINKMAN')"></td>
    <td class="page_form_td">&nbsp;</td>
    <td class="page_form_title_td" nowrap>&nbsp;性&nbsp;&nbsp;别：</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("LINKMANGENDER", "Gender", LINKMANGENDER)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;手&nbsp;&nbsp;机：</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="LINKMANPHONE1"
               value="<%=LINKMANPHONE1==null?"":LINKMANPHONE1%>"
               class="page_form_text" maxlength="15"
               onblur="checkLinkPhone('LINKMANPHONE1');"></td>
    <td class="page_form_td">&nbsp;</td>
    <td class="page_form_title_td" nowrap>&nbsp;固定电话：</td>
    <td nowrap class="page_form_td"><input type="text" <%=readonly%> name="LINKMANPHONE2"
                                           value="<%=LINKMANPHONE2==null?"":LINKMANPHONE2%>"
                                           class="page_form_text" maxlength="15"></td>
    <td class="page_form_td">&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;与申请人关系：</td>
    <td colspan="4" class="page_form_td">
        <%=level.radioHere("APPRELATION", "AppRelation", APPRELATION)%>
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;家庭地址：</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="LINKMANADD"
               value="<%=LINKMANADD==null?"":LINKMANADD%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;工作单位：</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="LINKMANCOMPANY"
               value="<%=LINKMANCOMPANY==null?"":LINKMANCOMPANY%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_left_table_title">合约</td>
    <td class="page_form_title_td" nowrap valign="bottom">&nbsp;同意合约：</td>
    <td class="page_form_td" valign="bottom">
        <input class="page_form_radio" type="radio" name="grpCon" id="radAgr" checked>同意
        <input class="page_form_radio" type="radio" name="grpCon" id="radNoAgr">不同意
    </td>
    <td colspan="4" valign="bottom">
        <a onclick="window.open('application_preshow1.jsp')" style="color:red;" href="#about:blank">查看合约内容</a>
    </td>
</tr>
<tr height="40px">
    <td colspan="7">&nbsp;</td>
</tr>
<tr>
    <td colspan="7" bgcolor="#3366FF" style="margin:0; padding:0" width="100%">
        <table class="page_form_table_slide" width="100%" id="HA3" style="display:none;"
               cellspacing="0" cellpadding="0" border="0">
            <col width="67"/>
            <col width="571"/>
            <tr class='page_form_tr'>
                <td class="page_button_tbl_tr" colspan="2" height="5"></td>
            </tr>
            <tr class='page_form_tr'>
                <td rowspan="1" class="page_left_table_title">申请人申明及签名</td>
                <td class="page_form_td">
                    <span style="font-size:13px;font-weight: bold;">◆您的签字将作为以下法律条款的唯一依据：</span><br>
                    &nbsp;◇本人（等）证实上述资料及随此申请表附上的文件全部真实无讹；并授权海尔集团财务有限责任公司通<br>&nbsp;&nbsp;&nbsp;过联络有关机构及人士等途径查证所呈交的申请资料的真实性和完整性。<br>
                    &nbsp;◇经贵公司审查不符合规定的分期付款条件而未予受理时，本人（等）无异议。<br>
                    &nbsp;◇本人（等）已解读并同意分期付款合约的所有条款，条款将构成本人（等）与贵公司就申请并使用分期<br>&nbsp;&nbsp;&nbsp;付款方式的法律协议，如资料失实或虚假，本人将承担相应的民事及法律责任。<br>
                    &nbsp;◇本人（等）保证在取得贵公司款项后，按时足额还款，严格遵照分期付款合约执行。<br>
                    &nbsp;◇本人（等）同意海尔集团财务有限责任公司将保留一切有关批核分期付款系列产品范围及在照会分期付<br>&nbsp;&nbsp;&nbsp;款合约后修改相关条款的权利。<br>
                    &nbsp;◇提示：您的签名表示您将遵守分期付款合约的相关规定。如履约有瑕，将会影响您未来在银行的融资能<br>&nbsp;&nbsp;&nbsp;力。
                    <hr>
                    <span style="font-size:14px; text-align:right">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;分期申请人姓名：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;</span>
                </td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_button_tbl_tr" colspan="2" height="5"></td>
            </tr>
        </table>
    </td>
</tr>
</table>

<%--<div class="PageNext" id="HA4" style="display:none;"></div>--%>
<span id="HA5" style="display:none;"><br><br><br><br><jsp:include page="./需要提供的文件.htm"/></span>

<div class="PageNext" id="HA6" style="display:none;"></div>
<span id="HA7" style="display:none;"><jsp:include page="<%=ContractTMP%>"/></span>
<span id="HA8" style="display:none;"><br><br><br><br><br><br><br><jsp:include page="./申请人申明及签名.htm"/></span>

<div class="PageNext" id="HA9" style="display:none;"></div>
<Iframe src="/consume/ifm_jh_zxsq.jsp" id="HA10" style="display:none;" width="650" height="950" scrolling="no"
        frameborder="0"></Iframe>

<input type='hidden' name='Plat_Form_Request_Instance_ID' value='2'>
<input type='hidden' name='Plat_Form_Request_Event_ID' value=''>
<input type='hidden' name='Plat_Form_Request_Event_Value' value='12'>
<input type='hidden' name='Plat_Form_Request_Button_Event' value=''>
</form>
</td>
<td width="20">&nbsp;</td>
</tr>
</table>
</td>
</tr>
<tr height="35" align="center" valign="middle">
    <td align="center">
        <table border="0" cellspacing="0" cellpadding="0" width="538" class="Noprint">
            <tr class='page_form_tr'>
                <td nowrap align="center">
                    <table bgcolor="#ffffff">
                        <tr class='page_button_tbl_tr'>
                            <%
                                if (APPSTATUS.equals("") || APPSTATUS.equals(XFConf.APPSTATUS_TIJIAO)
                                        || APPSTATUS.equals(XFConf.APPSTATUS_QIANYUE) || APPSTATUS.equals(XFConf.APPSTATUS_DAYIN)) {
                            %>
                            <td class='page_button_tbl_td'><input type='button' <%=submit%> id='saveadd' name='save'
                                                                  value=' 提 交 ' onClick="return Regvalid();"></td>
                            <td class='page_button_tbl_td'><input type='button' class='btn_2k3' id='addfile'
                                                                  name='addfile'
                                                                  value='上传附件'
                                                                  onClick="fileup('<%=XFAPPNO%>','<%=APPSTATUS%>');">
                            </td>
                            <%
                                }
                                if (um != null && APPSTATUS.equals(XFConf.APPSTATUS_TIJIAO)) {
                            %>
                            <td class='page_button_tbl_td'><input type='button' <%=submit%> id='savedel' name='save'
                                                                  value=' 作 废 ' onClick="return Regvalid1();"></td>
                            <%
                                }
                                if (!APPSTATUS.equals("")) {
                            %>
                            <%--<td class='page_button_tbl_td'><input type='button' class='page_button_active' id="print"--%>
                                                                  <%--name='print'--%>
                                                                  <%--onclick="javascript:doPrint()" value=" 打 印 "></td>--%>
                            <%
                                if (Double.parseDouble(APPSTATUS) > 1 && !APPSTATUS.equals(XFConf.APPSTATUS_WANCHENG)) {
                            %>
                            <%--<td class='page_button_tbl_td'><input type='button' <%=submit%> id='saveRtn' name='save'--%>
                            <%--value=' 退 回 ' onClick="return Regvalid2();"></td>--%>
                            <%
                                    }
                                }
                            %>
                            <td class='page_button_tbl_td'><input type='button' class='btn_2k3' id="print"
                                                                  name='print'
                                                                  onclick="javascript:doPrint()" value=" 打 印 "></td>
                            <td class='page_button_tbl_td'><input type='hidden' class='btn_2k3'
                                                                  name='button'
                                                                  value=' 关 闭 ' onClick="<%=closeClick%>"></td>
                            <%--<td class='page_button_tbl_td'><input type='button' class='page_button_active' name='button'--%>
                            <%--onclick="javascript:printit()" value=" 打 印 "></td>--%>
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
<tr class="Noprint">
    <td valign="bottom">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center"
               style="height: 95px; border:none; font-weight:normal; font-size: 10px; color:#8F87E0;font-family:'微软雅黑';">
            <tr>
                <td width="30%">&nbsp;</td>
                <td width="40%" valign="middle">
                    <table border="0" cellspacing="0" cellpadding="0" width="330" align="center"
                           style="height: 20px; border:none; font-weight:normal; font-size: 12px; color:#004080;font-family:'微软雅黑';">
                        <tr>
                            <td width="30%">分期咨询电话：<span style="color:#FF0066">0532-88939384，88939383</span>&nbsp;&nbsp;邮编：266101<br>地址：山东省青岛市海尔路1号海尔工业园K座金融中心405室
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="30%" valign="bottom" align="right">最终解释权归海尔集团财务有限责任公司所有&nbsp;</td>
            </tr>
        </table>
    </td>
</tr>
</table>
</body>


<%--在线客服代码 365call-- 列表方式--%>
<%--<script type='text/javascript' src='http://chat2.365webcall.com/IMMe1.aspx?settings=mw7mNmXNNm6X7Xbz3Am600bPz3Am6wIbNz3AN6mm00&LL=0'></script>--%>

<%--在线客服代码 365call--  图标方式 不用--%>
<%--<script type='text/javascript' src='http://chat2.365webcall.com/IMMe1.aspx?settings=mw7mNXXNNm6X7Xbz3Am600bPz3Am6wIbXz3A66mm0w&LL=0'></script>--%>


</html>
<script language="javascript" type="text/javascript">
document.title = "<%=title%>";
document.focus();

function setBankRadio(str) {
    var e = document.getElementsByName(str);
    var obj2 = document.getElementsByName("BANKACTNO");
    for (var i = 0; i < e.length; i++) {
        if (e[i].checked) {
            inputObjpreObj(obj2).innerText = " " + e[i].parentNode.innerText + "帐号";
            if (e[i].value == '901') {
                getObject("BANK_UD1").style.display = "block";
                getObject("BANK_UD2").style.display = "block";
            }
        }

        if (window.addEventListener) { // Mozilla, Netscape, Firefox
            e[i].addEventListener('click', reSetActnoText("BANKACTNO"), false);
        }
        else {
            e[i].attachEvent("onclick", function() {
                reSetActnoText("BANKACTNO");
            });
        }
    }
}

document.getElementsByName("CREDITTYPE")[0].onclick = function() {
    listCheck("CREDITTYPE", 0);
}

document.getElementById("CURRENTADDRESS").onblur = function() {
    //getRESDADDR();
}
document.getElementById("PC").onblur = function() {
    //getRESDPC();
}

document.body.onload = function() {
    setBankRadio("ACTOPENINGBANK");

    listCheck("CREDITTYPE", 0);

    //getRESDADDR();
    //getRESDPC();
}

function reSetActnoText(str) {
    var obj1 = getElement();
    var obj2 = document.getElementsByName(str);
    if (obj1.value == '901') {
        getObject("BANK_UD1").style.display = "block";
        getObject("BANK_UD2").style.display = "block";
    } else {
        getObject("BANK_UD1").style.display = "none";
        getObject("BANK_UD2").style.display = "none";
    }
    inputObjpreObj(obj2).innerText = " " + obj1.parentNode.innerText + "帐号";
}

//同步家庭住址和邮寄地址
function getRESDADDR() {
    if (document.getElementById("RESDADDR").value == "")
        document.getElementById("RESDADDR").value = document.getElementById("CURRENTADDRESS").value;
}
//同步家庭邮编和邮寄邮编
function getRESDPC() {
    if (document.getElementById("RESDPC").value == "")
        document.getElementById("RESDPC").value = document.getElementById("PC").value;
}

function limitTextarea(obj) {
    var len = 150;
    //    if (obj.value.getBytes() > len){
    if (obj.value.length > len) {
        obj.value = obj.value.substr(0, len);
    }
}
//document.getElementsByName('COMMNAME')[0].attachEvent('onchange', function(o) {
//    if(o.propertyName!='value')return;  //不是value改变不执行下面的操作
//
//    //.函数处理
//});


function listCheck(objnm, id) {
    var crdtps = document.getElementsByName(objnm);
    if (crdtps[id].checked) {
        for (var i = 0; i < crdtps.length; i++) {
            if (i == id)continue;
            crdtps[i].checked = false;
            crdtps[i].disabled = true;
        }
        document.getElementById("MONPAYAMT").value = 0;
        document.getElementById("MONPAYAMT").readOnly = true;
    } else {
        for (var i = 0; i < crdtps.length; i++) {
            if (i == id)continue;
            crdtps[i].disabled = false;
        }
        document.getElementById("MONPAYAMT").readOnly = false;
    }
}

function fileup(appno, appstatus) {
    var filetp = "APP";
    var url = "../fileupdown/fileup.jsp?OPNO=" + appno + "&FILETP=" + filetp + "&APPSTATUS=" + appstatus;

    var x = window.screen.width;
    var y = window.screen.height;
    x = (x - 390) / 2;
    y = (y - 470) / 2;
    //window.showModalDialog(url, 'fileup', 'dialogHeight:390px;dialogWidth:450px;status:no;scroll:no;help:no;resizable:yes');
    window.open(url, 'fileup', 'left=' + x + ',top=' + y + ',height=390,width=470,toolbar=no,scrollbars=yes,resizable=yes');
}


function goSave(bank) {
    //        var chkobjstr=new Array("MONTHLYPAY","PMONTHLYPAY","AMT","RECEIVEAMT","APPAMT","MONPAYAMT");
    //        var chkobj=new Array(chkobjstr.length);
    //        for (var i = 0; i < chkobj.length; i++) {
    //             chkobj[i]= document.getElementsByName(chkobjstr[i])[0];
    //        }

    if (!isEmptyItem("NAME")) return false;
    if (!isEmptyItem("GENDER"))return false;
    if (!isEmptyItem("PHONE1"))return false;
    else if (!isPhone("PHONE1"))return false;
    if (!isEmptyItem("ID"))return false;
    if (!isEmptyItem("HEALTHSTATUS"))return false;
    if (!isEmptyItem("MARRIAGESTATUS"))return false;
    if (!isEmptyItem("BURDENSTATUS"))return false;
    if (!isEmptyItem("RESIDENCEADR"))return false;
    if (!isEmptyItem("CURRENTADDRESS"))return false;
    if (!isZipCode("PC"))return false;
    if (!isEmail("EMAIL"))return false;
    if (!isEmptyItem("PHONE2"))return false;
    else if (!isPhone("PHONE2"))return false;
    if (!isEmptyItem("LIVEFROM"))return false;
    else if (!chkintWithAlStr("LIVEFROM", "本地居住时间请保留整数！"))return false;
    if (!isEmptyItem("EDULEVEL"))return false;
    if (!isEmptyItem("QUALIFICATION"))return false;
    if (!isEmptyItem("COMPANY"))return false;
    if (!isEmptyItem("PHONE3"))return false;
    else if (!isPhone("PHONE3"))return false;
    if (!isZipCode("COMPC"))return false;
    if (!isEmptyItem("COMADDR"))return false;
    if (!isEmptyItem("CLIENTTYPE"))return false;

    if (!isEmptyItem("SERVFROM"))return false;
    else if (!chkintWithAlStr("SERVFROM", "现单位工作时间请保留整数！"))return false;

    if (!isEmptyItem("MONTHLYPAY"))return false;
    else if (!chkdecWithAlStr("MONTHLYPAY", "个人月收入请录入数字！"))return false;
    //if (!isEmptyItem("SOCIALSECURITY"))return false;

    if (document.getElementById("PID").value.Trim() != "") {
        if (document.getElementById("ID").value == document.getElementById("PID").value) {
            alert("配偶证件号码有误，请核实！");
            document.getElementById("PID").focus();
            return false;
        }
    }
    if (document.getElementById("PNAME").value.Trim() != "") {
        if (!isEmptyItem("PID"))return false;
        if (!isEmptyItem("PCLIENTTYPE"))return false;
        if (!isEmptyItem("PMONTHLYPAY"))return false;
        else if (!chkdecWithAlStr("PMONTHLYPAY", "个人月收入请录入数字！"))return false;
    }
    if (!isPhone("PPHONE1"))return false;
    if (!isPhone("PPHONE3"))return false;

    if (!isEmptyItem("CHANNEL"))return false;
    if (!isEmptyItem("COMMNAME"))return false;
    if (!isEmptyItem("NUM"))return false;
    else if (!chkintWithAlStr("NUM", "拟购商品数量请输入整数！"))return false;
    if (!isEmptyItem("APPTYPE"))return false;

    if (!isEmptyItem("AMT"))return false;
    else if (!chkdecWithAlStr("AMT", "商品总金额请录入数字！"))return false;
    if (!chkdecWithAlStr("RECEIVEAMT", "已付价款请录入数字！"))return false;
    //        if (!isEmptyItem("APPAMT"))return false;
    //        else if (!chkdecWithAlStr("APPAMT","金额数据请录入数字！"))return false;

    if (!isEmptyItem("DIVID"))return false;
    if (!isEmptyItem("ACTOPENINGBANK"))return false;
    if (bank != "802")
    if (!isEmptyItem("BANKACTNO"))return false;
    if (bank == "901")
        if (!isEmptyItem("ACTOPENINGBANK_UD"))return false;
    if (!isEmptyItem("CREDITTYPE"))return false;
    if (!isEmptyItem("MONPAYAMT"))return false;
    else if (!chkdecWithAlStr("MONPAYAMT", "月均还款额请录入数字！"))return false;
    if (!isEmptyItem("RESDADDR"))return false;
    if (!isEmptyItem("RESDPC"))return false;
    else if (!isZipCode("RESDPC"))return false;
    if (!isEmptyItem("LINKMAN"))return false;
    if (!isEmptyItem("LINKMANPHONE1"))return false;
    else if (!isPhone("LINKMANPHONE1"))return false;
    //if (!isEmptyItem("LINKMANPHONE2"))return false;
    if (!isPhone("LINKMANPHONE2"))return false;
    if (!isEmptyItem("APPRELATION"))return false;

	if(!checkLinkname('LINKMAN')) return false;
	if(!checkLinkPhone('LINKMANPHONE1')) return false;
	if(!checkAmt()) return false;
	
    return true;
}


function Regvalid() {//提交
    var bank;
    var e = document.getElementsByName("ACTOPENINGBANK");
    for (var i = 0; i < e.length; i++) {
        if (e[i].checked)bank = e[i].value;
    }
    if (goSave(bank)) {
        //第三方支付新窗口控制
        //if (bank == "801" || bank == "802")document.forms[0].target = "_blank";

        document.forms[0].target = "_blank";
        document.forms[0].action = "/consume/application_save.jsp";
        document.forms[0].submit();
        //document.getElementById("print").className = "page_button_active";
        //document.getElementById("print").disabled = false;

        //document.getElementById("saveadd").className="page_button_disabled";
        //document.getElementById("saveadd").disabled="true";
    }
}

function Regvalid1() {//作废
    if (confirm("确实要作废申请单？")) {
        document.all.APPACTFLAG.value = "3";
        document.forms[0].action = "/consume/application_save.jsp";
        document.forms[0].submit();
    }
}

function Regvalid2() {//退回
    if (confirm("确实要将申请单退回上一级审批？")) {
        document.all.APPACTFLAG.value = "2";
        document.forms[0].action = "/consume/application_save.jsp";
        document.forms[0].submit();
    }
}

function countAppAmt() {//自动计算分期金额
    var obj1 = document.getElementsByName("AMT")[0];
    var obj2 = document.getElementsByName("RECEIVEAMT")[0];
    var obj3 = document.getElementsByName("APPAMT")[0];

    if (obj1.value != null && chkdec("AMT")) {
        var countamt = 0;
        if (obj2.value != null && chkdec("RECEIVEAMT")) {
            countamt = obj1.value - obj2.value;
        } else countamt = obj1.value;
        obj3.value = countamt;
    }
}


function getObject(objectId) {
    if (document.getElementById && document.getElementById(objectId)) {

        return document.getElementById(objectId);
    } else if (document.all && document.all(objectId)) {

        return document.all(objectId);
    } else if (document.layers && document.layers[objectId]) {

        return document.layers[objectId];
    } else {
        return false;
    }
}

function showList(n) {
    var itext = getObject('HT' + n).innerText;
    itext = itext.substr(0, itext.indexOf(" -- "));
    if (getObject("HA" + n).style.display == "none") {
        disList(n, itext);
    }
    else {
        hidList(n, itext);
    }
}

function disList(n, itext) {
    getObject('HT' + n).title = "点击收缩";
    getObject('HT' + n).innerText = itext + " -- ↑(点击收缩)";
    getObject("HA" + n).style.display = "block";
}

function hidList(n, itext) {
    getObject('HT' + n).title = "点击展开";
    getObject('HT' + n).innerText = itext + " -- ↓(点击展开)";
    getObject("HA" + n).style.display = "none";
}


function mOvr(src) {
    if (!src.contains(event.fromElement)) {
        dataBgColor = src.bgColor;
        src.style.cursor = "hand";
        src.style.backgroundColor = "#E1FFF0";
    }
}

function mOut(src) {
    if (!src.contains(event.toElement)) {
        src.style.cursor = "default";
        src.style.backgroundColor = "#FDF8DF";
    }
}

//20100108  zhanrui 增加对第三联系人的检查
//function checkName() {
//    if (document.getElementById("RESDADDR").value == "")
//        document.getElementById("RESDADDR").value = document.getElementById("CURRENTADDRESS").value;
//}

</script>

<%if (APPNO.equals("")) {%>
<script type="text/javascript">
    document.forms[0].CHANNEL.focus();
</script>

<%
    }
    if (!PNAME.equals("")) {%>
<script type="text/javascript">
    showList(1);
</script>
<%
    }
    if (APPSTATUS.equals("")) {
%>
<script type="text/javascript">
    document.getElementById("print").className = "page_button_disabled";
    document.getElementById("print").disabled = true;
</script>
<%
    }
    if (!SID.equals("")) {
%>
<script type="text/javascript">
    countAppAmt();
    document.forms[0].CHANNEL.readOnly = true;
    document.forms[0].COMMNAME.readOnly = true;
    document.forms[0].NUM.readOnly = true;
    document.forms[0].AMT.readOnly = true;

    var obj = document.getElementsByName("APPTYPE");
    obj[0].checked = true;
    for (var i = 0; i < obj.length; i++) {
        if (!obj[i].checked) obj[i].disabled = true;
    }
</script>
<%}%>
<%}%>


