<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.util.Workflow" %>
<%@ page import="zt.cms.xf.XFConf" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.cmsi.pub.define.BMRouteBindNode" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.math.BigDecimal" %>

<%--
===============================================
Title: 消费信贷-个人消费分期付款申请书提交
Description: 消费信贷-个人消费分期付款申请书提交。
 * @version  $Revision: 1.0 $  $Date: 2009/03/11 06:33:37 $
 * @author
 * <p/>修改：$Author: liuj $
===============================================
--%>
<SCRIPT language=JavaScript>
    <!--
    //    self.moveTo(0, 0);
    //    self.resizeTo(screen.availWidth, screen.availHeight);
    //    self.outerWidth = screen.availWidth;
    //    self.outerHeight = screen.availHeight;

    self.moveTo((screen.width - 450) / 2, (screen.height - 400) / 2);
    self.resizeTo(450, 420);

    this.onunload = function() {
        window.opener.location.reload();
    }
    //-->
</SCRIPT>
<%
    request.setCharacterEncoding("GBK");
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (um == null) {
//        response.sendRedirect("../fcworkbench/error.jsp");
//    }
//    System.out.println(request.getAttribute("APPNO"));
    String PASSWORD = request.getParameter("PASSWORD");   //密码
    String CLIENTNO = request.getParameter("CLIENTNO");   //客户号

    String APPACTFLAG = request.getParameter("APPACTFLAG");           //申请单执行状态   执行动作标志：1、正常，2、退回，3、作废
    String APPNO = request.getParameter("APPNO");                      //申请单编号
    String XFAPPNO = request.getParameter("XFAPPNO");                  //新申请单编号
    String APPDATE = request.getParameter("APPDATE");                  //申请日期
    String IDTYPE = request.getParameter("IDTYPE");                    //证件名称
    String ID = request.getParameter("ID");                             //证件号码
    String APPTYPE = request.getParameter("APPTYPE");                  //申请类型
    String APPSTATUS = request.getParameter("APPSTATUS");              //申请状态
    String BIRTHDAY = request.getParameter("BIRTHDAY");                //出生日期
    String GENDER = request.getParameter("GENDER");                    //性别 enum=Gender
    String NATIONALITY = request.getParameter("NATIONALITY");         //国籍
    String MARRIAGESTATUS = request.getParameter("MARRIAGESTATUS");   //婚姻状况 enum=MarriageStatus
    String HUKOUADDRESS = request.getParameter("HUKOUADDRESS");       //户籍所在地
    String CURRENTADDRESS = request.getParameter("CURRENTADDRESS");   //现住址
    String COMPANY = request.getParameter("COMPANY");                  //工作单位
    String TITLE = request.getParameter("TITLE");                      //职务 enum=Title
    String QUALIFICATION = request.getParameter("QUALIFICATION");     //职称 enum=Qualification
    String EDULEVEL = request.getParameter("EDULEVEL");                //学历 enum=EduLevel
    String PHONE1 = request.getParameter("PHONE1");                   //移动电话
    String PHONE2 = request.getParameter("PHONE2");                   //家庭电话
    String PHONE3 = request.getParameter("PHONE3");                   //办公电话
    String NAME = request.getParameter("NAME");                       //客户名称 desc=企业(个人)名称
    String CLIENTTYPE = request.getParameter("CLIENTTYPE");           //客户性质 enum=ClientType1
    String DEGREETYPE = request.getParameter("DEGREETYPE");          //最高学位 enum=DegreeType
    String COMADDR = request.getParameter("COMADDR");                 //单位地址
    String SERVFROM = request.getParameter("SERVFROM");               //现单位工作时间
    String RESIDENCEADR = request.getParameter("RESIDENCEADR");       //户籍所在地(本外地) enum=ResidenceADR
    String HOUSINGSTS = request.getParameter("HOUSINGSTS");           //居住状况 enum=HousingSts
    String HEALTHSTATUS = request.getParameter("HEALTHSTATUS");       //健康状况 enum=HealthStatus
    String MONTHLYPAY = request.getParameter("MONTHLYPAY");           //个人月收入
    String BURDENSTATUS = request.getParameter("BURDENSTATUS");       //负担状况 enum=BurdenStatus
    String EMPNO = request.getParameter("EMPNO");                      //员工卡号码
    String SOCIALSECURITY_G[] = request.getParameterValues("SOCIALSECURITY");   //社会保障 enum=SocialSecurity
    String LIVEFROM = request.getParameter("LIVEFROM");                //本地居住时间
    String PC = request.getParameter("PC");                             //住宅邮编
    String COMPC = request.getParameter("COMPC");                      //单位邮编
    String RESDPC = request.getParameter("RESDPC");                    //寄送地址邮编
    String RESDADDR = request.getParameter("RESDADDR");                //寄送地址
    String EMAIL = request.getParameter("EMAIL");                      //电子邮件

    String PNAME = request.getParameter("PNAME");                      //配偶名称
    String PIDTYPE = request.getParameter("PIDTYPE");                  //配偶证件名称
    String PID = request.getParameter("PID");                          //配偶证件号码
    String PCOMPANY = request.getParameter("PCOMPANY");                //配偶工作单位
    String PTITLE = request.getParameter("PTITLE");                    //配偶职务 enum=Title
    String PPHONE1 = request.getParameter("PPHONE1");                  //配偶移动电话
    String PPHONE3 = request.getParameter("PPHONE3");                  //配偶办公电话
    String PCLIENTTYPE = request.getParameter("PCLIENTTYPE");         //配偶客户性质(单位性质) enum=ClientType1
    String PSERVFROM = request.getParameter("PSERVFROM");             //配偶现单位工作时间
    String PMONTHLYPAY = request.getParameter("PMONTHLYPAY");         //配偶个人月收入
    String PLIVEFROM = request.getParameter("PLIVEFROM");             //配偶本地居住时间

    String CHANNEL = request.getParameter("CHANNEL");                  //销售单位(渠道名称)
    String COMMNAME = request.getParameter("COMMNAME");                //商品名称
    String COMMTYPE = request.getParameter("COMMTYPE");                //商品型号
    String ADDR = request.getParameter("ADDR");                         //配送地址
    String NUM = request.getParameter("NUM");                           //购买数量
    String AMT = request.getParameter("AMT");                           //总金额
    String RECEIVEAMT = request.getParameter("RECEIVEAMT");            //已付金额
    String APPAMT = request.getParameter("APPAMT");                     //分期金额
    String DIVID = request.getParameter("DIVID");                       //分期期数

    String ACTOPENINGBANK = request.getParameter("ACTOPENINGBANK");   //开户行 enum=Bank
    String BANKACTNO = request.getParameter("BANKACTNO");              //还款帐号
    String XY = request.getParameter("XY");                             //信用 enum=YesNo
    String XYR = request.getParameter("XYR");                           //信用人名称
    String DY = request.getParameter("DY");                             //抵押 enum=YesNo
    String DYW = request.getParameter("DYW");                           //抵押物名称
    String ZY = request.getParameter("ZY");                             //质押 enum=YesNo
    String ZYW = request.getParameter("ZYW");                           //质押物名称
    String BZ = request.getParameter("BZ");                             //保证 enum=YesNo
    String BZR = request.getParameter("BZR");                           //保证人名称
    String CREDITTYPE_G[] = request.getParameterValues("CREDITTYPE");  //信用种类 enum=CreditType
    String MONPAYAMT = request.getParameter("MONPAYAMT");              //月均还款额
    String LINKMAN = request.getParameter("LINKMAN");                   //联系人姓名
    String LINKMANGENDER = request.getParameter("LINKMANGENDER");      //联系人性别
    String LINKMANPHONE1 = request.getParameter("LINKMANPHONE1");       //联系人移动电话
    String LINKMANPHONE2 = request.getParameter("LINKMANPHONE2");       //联系人固定电话
    String APPRELATION = request.getParameter("APPRELATION");           //与申请人关系 enum=AppRelation
    String LINKMANADD = request.getParameter("LINKMANADD");             //联系人住址
    String LINKMANCOMPANY = request.getParameter("LINKMANCOMPANY");    //联系人工作单位
    String ACTOPENINGBANK_UD = request.getParameter("ACTOPENINGBANK_UD");    //开户行名称（电汇、网银——录入名称）

    String SID = request.getParameter("SID");                           //合作商城编号
    String ORDERNO = request.getParameter("ORDERNO");                   //合作商城单号
    String REQUESTTIME = request.getParameter("REQUESTTIME");          //合作商城定单生成时间


    APPNO = (APPNO == null) ? "" : APPNO.trim();                    //申请单编号
    APPDATE = (APPDATE == null) ? "SYSDATE" : "to_date('" + APPDATE.trim() + "','YYYY-MM-DD')";      //申请日期
    IDTYPE = (IDTYPE == null) ? "" : IDTYPE;                        //证件名称
    ID = (ID == null) ? "" : ID;                                    //证件号码
    APPTYPE = (APPTYPE == null) ? "" : APPTYPE;                     //申请类型
    APPSTATUS = (APPSTATUS == null) ? "" : APPSTATUS;               //申请状态
    BIRTHDAY = (BIRTHDAY == null) ? "" : BIRTHDAY;                  //出生日期
    GENDER = (GENDER == null) ? "" : GENDER;                        //性别 enum=Gender
    NATIONALITY = (NATIONALITY == null) ? "" : NATIONALITY;         //国籍
    MARRIAGESTATUS = (MARRIAGESTATUS == null) ? "" : MARRIAGESTATUS;//婚姻状况 enum=MarriageStatus
    HUKOUADDRESS = (HUKOUADDRESS == null) ? "" : HUKOUADDRESS;      //户籍所在地
    CURRENTADDRESS = (CURRENTADDRESS == null) ? "" : CURRENTADDRESS;//现住址
    COMPANY = (COMPANY == null) ? "" : COMPANY;                     //工作单位
    TITLE = (TITLE == null) ? "" : TITLE;                           //职务 enum=Title
    QUALIFICATION = (QUALIFICATION == null) ? "" : QUALIFICATION;   //职称 enum=Qualification
    EDULEVEL = (EDULEVEL == null) ? "" : EDULEVEL;                  //学历 enum=EduLevel
    PHONE1 = (PHONE1 == null) ? "" : PHONE1;                        //移动电话
    PHONE2 = (PHONE2 == null) ? "" : PHONE2;                        //家庭电话
    PHONE3 = (PHONE3 == null) ? "" : PHONE3;                        //办公电话
    NAME = (NAME == null) ? "" : NAME;                              //客户名称 desc=(企业(个人)名称
    CLIENTTYPE = (CLIENTTYPE == null) ? "" : CLIENTTYPE;            //客户性质 enum=ClientType1
    DEGREETYPE = (DEGREETYPE == null) ? "" : DEGREETYPE;            //最高学位 enum=DegreeType
    COMADDR = (COMADDR == null) ? "" : COMADDR;                     //单位地址
    SERVFROM = (SERVFROM == null) ? "" : SERVFROM;                  //现单位工作时间
    RESIDENCEADR = (RESIDENCEADR == null) ? "" : RESIDENCEADR;      //户籍所在地(本外地) enum=ResidenceADR
    HOUSINGSTS = (HOUSINGSTS == null) ? "" : HOUSINGSTS;            //居住状况 enum=HousingSts
    HEALTHSTATUS = (HEALTHSTATUS == null) ? "" : HEALTHSTATUS;      //健康状况 enum=HealthStatus
    MONTHLYPAY = (MONTHLYPAY == null) ? "" : MONTHLYPAY;           //个人月收入
    BURDENSTATUS = (BURDENSTATUS == null) ? "" : BURDENSTATUS;      //负担状况 enum=BurdenStatus
    EMPNO = (EMPNO == null) ? "" : EMPNO.trim();                    //员工卡号码
    LIVEFROM = (LIVEFROM == null) ? "" : LIVEFROM;                  //本地居住时间
    PC = (PC == null) ? "" : PC;                                    //住宅邮编
    COMPC = (COMPC == null) ? "" : COMPC;                           //单位邮编
    RESDPC = (RESDPC == null) ? "" : RESDPC;                        //寄送地址邮编
    RESDADDR = (RESDADDR == null) ? "" : RESDADDR;                  //寄送地址
    EMAIL = (EMAIL == null) ? "" : EMAIL;                           //电子邮件

    PNAME = (PNAME == null) ? "" : PNAME;                           //配偶名称
    PIDTYPE = (PIDTYPE == null) ? "" : PIDTYPE;                     //配偶证件名称
    PID = (PID == null) ? "" : PID;                                 //配偶证件号码
    PCOMPANY = (PCOMPANY == null) ? "" : PCOMPANY;                  //配偶工作单位
    PTITLE = (PTITLE == null) ? "" : PTITLE;                        //配偶职务 enum=Title
    PPHONE1 = (PPHONE1 == null) ? "" : PPHONE1;                     //配偶移动电话
    PPHONE3 = (PPHONE3 == null) ? "" : PPHONE3;                     //配偶办公电话
    PCLIENTTYPE = (PCLIENTTYPE == null) ? "" : PCLIENTTYPE;         //配偶客户性质(单位性质) enum=ClientType1
    PSERVFROM = (PSERVFROM == null) ? "" : PSERVFROM;               //配偶现单位工作时间
    PMONTHLYPAY = (PMONTHLYPAY == null) ? "" : PMONTHLYPAY;        //配偶个人月收入
    PLIVEFROM = (PLIVEFROM == null) ? "" : PLIVEFROM;               //配偶本地居住时间

    CHANNEL = (CHANNEL == null) ? "" : CHANNEL.trim();              //销售单位(渠道名称)
    COMMNAME = (COMMNAME == null) ? "" : COMMNAME.trim();           //商品名称
    COMMTYPE = (COMMTYPE == null) ? "" : COMMTYPE.trim();           //商品型号
    ADDR = (ADDR == null) ? "" : ADDR;                              //配送地址
    NUM = (NUM == null) ? "" : NUM;                                 //购买数量
    AMT = (AMT == null) ? "" : AMT;                                 //总金额
    RECEIVEAMT = (RECEIVEAMT == null) ? "" : RECEIVEAMT;            //已付金额
    APPAMT = (APPAMT == null) ? "" : APPAMT;                        //分期金额
    DIVID = (DIVID == null) ? "" : DIVID;                           //分期期数

    ACTOPENINGBANK = (ACTOPENINGBANK == null) ? "" : ACTOPENINGBANK;//开户行 enum=Bank
    BANKACTNO = (BANKACTNO == null) ? "" : BANKACTNO;               //还款帐号
    XY = (XY == null) ? "" : XY;                                    //信用 enum=YesNo
    XYR = (XYR == null) ? "" : XYR;                                 //信用人名称
    DY = (DY == null) ? "" : DY;                                    //抵押 enum=YesNo
    DYW = (DYW == null) ? "" : DYW;                                 //抵押物名称
    ZY = (ZY == null) ? "" : ZY;                                    //质押 enum=YesNo
    ZYW = (ZYW == null) ? "" : ZYW;                                 //质押物名称
    BZ = (BZ == null) ? "" : BZ;                                    //保证 enum=YesNo
    BZR = (BZR == null) ? "" : BZR;                                 //保证人名称
    MONPAYAMT = (MONPAYAMT == null) ? "" : MONPAYAMT;               //月均还款额
    LINKMAN = (LINKMAN == null) ? "" : LINKMAN;                     //联系人姓名
    LINKMANGENDER = (LINKMANGENDER == null) ? "" : LINKMANGENDER;   //联系人性别
    LINKMANPHONE1 = (LINKMANPHONE1 == null) ? "" : LINKMANPHONE1;   //联系人移动电话
    LINKMANPHONE2 = (LINKMANPHONE2 == null) ? "" : LINKMANPHONE2;   //联系人固定电话
    APPRELATION = (APPRELATION == null) ? "" : APPRELATION;         //与申请人关系 enum=AppRelation
    LINKMANADD = (LINKMANADD == null) ? "" : LINKMANADD;            //联系人住址
    LINKMANCOMPANY = (LINKMANCOMPANY == null) ? "" : LINKMANCOMPANY;//联系人工作单位
    ACTOPENINGBANK_UD = (ACTOPENINGBANK_UD == null) ? "" : ACTOPENINGBANK_UD;//开户行名称（电汇、网银——录入名称）

    SID = (SID == null) ? "" : SID;                                 //合作商城编号
    ORDERNO = (ORDERNO == null) ? "" : ORDERNO;                     //合作商城单号
    REQUESTTIME = (REQUESTTIME == null) ? "" : REQUESTTIME;         //合作商城定单生成时间

    BigDecimal dMONTHLYPAY = new BigDecimal((MONTHLYPAY.equals("")) ? "0" : MONTHLYPAY);
    BigDecimal dPMONTHLYPAY = new BigDecimal((PMONTHLYPAY.equals("")) ? "0" : PMONTHLYPAY);
    String CONFMONPAY = String.valueOf(dMONTHLYPAY.add(dPMONTHLYPAY));//核定月收入


    if (IDTYPE.equals("") || ID.equals("")) {
        session.setAttribute("msg", "没有发现传送入的参数！");
        response.sendRedirect("../showinfo.jsp");
    }

    boolean temp = false;

    String flag = "";
    boolean query = true;
    if (!query) {
        flag = flag.substring(0, flag.length() - 1);
        request.setAttribute("mess", flag);
%>
<jsp:forward page="application_start.jsp"/>
<%
} else {

    //对于多选项，用默认的 splitechar"_" 分隔组串
    String splitechar = EnumValue.SPLIT_STR, SOCIALSECURITY = "", CREDITTYPE = "";
    if (SOCIALSECURITY_G != null) {
        for (String aSOCIALSECURITY_G : SOCIALSECURITY_G) {
            SOCIALSECURITY += splitechar + aSOCIALSECURITY_G;
        }
        SOCIALSECURITY = SOCIALSECURITY.replaceFirst(splitechar, "");
    }
    if (CREDITTYPE_G != null) {
        for (String aCREDITTYPE_G : CREDITTYPE_G) {
            CREDITTYPE += splitechar + aCREDITTYPE_G;
        }
        CREDITTYPE = CREDITTYPE.replaceFirst(splitechar, "");
    }

    String[] sql = new String[6];
    String sql11, sql12, sql13;
    String SEQNO;

    //根据证件确定最近登记的客户资料
    //sql11 = " select CLIENTNO,APPDATE from CMINDVCLIENT where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and rownum=1  order by CLIENTNO desc";

    ConnectionManager manager = ConnectionManager.getInstance();
    CachedRowSet crs;
    try {
        if (!APPNO.equals("") && APPACTFLAG.equals("3")) {//作废
            sql[0] = "update XFAPP set APPSTATUS='0' where appno='" + APPNO + "'";
        } else if (!APPNO.equals("") && APPACTFLAG.equals("2")) {//退回
            if (um == null) {
                response.sendRedirect("../fcworkbench/error.jsp");
            }
            BMRouteBindNode dt = Workflow.getRouteBindByUserTp(String.valueOf(um.getUser().getStatus()));
            APPSTATUS = Workflow.getBeforeStatusBeginByUserTp(dt.status, APPAMT);//如果状态为空，则返回申请提交状态‘1’。
            sql[0] = "update XFAPP set APPSTATUS='" + APPSTATUS + "' where appno='" + APPNO + "'";
            sql[1] = "delete from XFOPINION where appno='" + APPNO + "' and operator='" + um.getUserName() + "'";
        } else {
            //获取手续费率
            int isEmp = (EMPNO.equals("")) ? 0 : 1;
            String SOURCEID = (SID.equals("")) ? "000" : SID;
            String COMMISSIONRATE = manager.getCellValue("SERVICECHARGE", "XFPRODUCT", "SOURCEID=" + SOURCEID + " and APPTYPE=" + APPTYPE + " and DURATION=to_number('" + DIVID + "') and CLIENTCD=" + isEmp);
            COMMISSIONRATE = (COMMISSIONRATE == null) ? "" : COMMISSIONRATE;

            if (APPNO.equals("")) {
                if (XFAPPNO == null || XFAPPNO.length() != 22) {
                    session.setAttribute("msg", "申请单号获取失败，请联系我们!");
%>
<jsp:forward page="application_start.jsp"/>
<%
                }
                APPNO = XFAPPNO;
                if (ACTOPENINGBANK.equals("801") || ACTOPENINGBANK.equals("802"))
                    APPSTATUS = XFConf.APPSTATUS_QIANYUE; //支付宝、快钱等需要签约状态。
                else APPSTATUS = XFConf.APPSTATUS_TIJIAO;

                sql[0] = "insert into XFAPP (appno,CLIENTNO,name, idtype, id, spouseidtype, spouseid, CONFMONPAY, APPTYPE, APPSTATUS, APPDATE, SID, ORDERNO, REQUESTTIME) values ('" + APPNO + "','" + CLIENTNO + "','" + NAME + "','" + IDTYPE + "','" + ID + "','" + PIDTYPE + "','" + PID + "','" + CONFMONPAY + "','" + APPTYPE + "','" + APPSTATUS + "'," + APPDATE + ",'" + SID + "','" + ORDERNO + "', to_date('" + REQUESTTIME + "','YYYYMMDDHH24MISS'))";
                sql[1] = "insert into XFAPPCOMM (channel, commname, commtype, addr, num, amt, receiveamt, appamt, divid, appno,COMMISSIONRATE) " +
                        "values ('" + CHANNEL + "','" + COMMNAME + "','" + COMMTYPE + "','" + ADDR + "','" + NUM + "','" + AMT + "','" + RECEIVEAMT + "','" + APPAMT + "','" + DIVID + "','" + APPNO + "','" + COMMISSIONRATE + "')";
                sql[2] = "insert into XFAPPADD (appno, idtype, id, actopeningbank, bankactno, xy, xyr, dy, dyw, zy, zyw, bz, bzr, credittype, monpayamt, linkman, linkmangender, linkmanphone1,linkmanphone2,apprelation,LINKMANADD,LINKMANCOMPANY,ACTOPENINGBANK_UD) " +
                        "values ('" + APPNO + "','" + IDTYPE + "','" + ID + "','" + ACTOPENINGBANK + "','" + BANKACTNO + "','" + XY + "','" + XYR + "','" + DY + "','" + DYW + "','" + ZY + "','" + ZYW + "','" + BZ + "','" + BZR + "','" + CREDITTYPE + "','" + MONPAYAMT + "','" + LINKMAN + "','" + LINKMANGENDER + "','" + LINKMANPHONE1 + "','" + LINKMANPHONE2 + "','" + APPRELATION + "','" + LINKMANADD + "','" + LINKMANCOMPANY + "','" + ACTOPENINGBANK_UD + "')";
                sql[3] = "insert into XFCLIENT (APPNO,XFCLTP,LASTMODIFIED,APPDATE,BIRTHDAY,GENDER,NATIONALITY,MARRIAGESTATUS,HUKOUADDRESS,CURRENTADDRESS,COMPANY,TITLE,QUALIFICATION,EDULEVEL,PHONE1,PHONE2,PHONE3,CLIENTNO,NAME,IDTYPE,ID,CLIENTTYPE,DEGREETYPE,COMADDR,SERVFROM,RESIDENCEADR,HOUSINGSTS,HEALTHSTATUS,MONTHLYPAY,BURDENSTATUS,EMPNO,SOCIALSECURITY,LIVEFROM,PC,COMPC,RESDPC,RESDADDR,EMAIL,actopeningbank,bankactno) " +
                        "values ('" + APPNO + "',1,SYSDATE," + APPDATE + ",to_date('" + BIRTHDAY + "','YYYY-MM-DD'),'" + GENDER + "','" + NATIONALITY + "','" + MARRIAGESTATUS + "','" + HUKOUADDRESS + "','" + CURRENTADDRESS + "','" + COMPANY + "','" + TITLE + "','" + QUALIFICATION + "','" + EDULEVEL + "','" + PHONE1 + "','" + PHONE2 + "','" + PHONE3 + "','" + CLIENTNO + "','" + NAME + "','" + IDTYPE + "','" + ID + "','" + CLIENTTYPE + "','" + DEGREETYPE + "','" + COMADDR + "','" + SERVFROM + "','" + RESIDENCEADR + "','" + HOUSINGSTS + "','" + HEALTHSTATUS + "','" + MONTHLYPAY + "','" + BURDENSTATUS + "','" + EMPNO + "','" + SOCIALSECURITY + "','" + LIVEFROM + "','" + PC + "','" + COMPC + "','" + RESDPC + "','" + RESDADDR + "','" + EMAIL + "','" + ACTOPENINGBANK + "','" + BANKACTNO + "')";
                if (!PID.equals(""))
                    sql[4] = "insert into XFCLIENT (APPNO,XFCLTP,LASTMODIFIED,APPDATE,MARRIAGESTATUS,NAME,IDTYPE,ID,COMPANY,TITLE,PHONE1,PHONE3,CLIENTTYPE,SERVFROM,MONTHLYPAY,LIVEFROM) " +
                            "values ('" + APPNO + "',2,SYSDATE," + APPDATE + ",'" + MARRIAGESTATUS + "','" + PNAME + "','" + PIDTYPE + "','" + PID + "','" + PCOMPANY + "','" + PTITLE + "','" + PPHONE1 + "','" + PPHONE3 + "','" + PCLIENTTYPE + "','" + PSERVFROM + "','" + PMONTHLYPAY + "','" + PLIVEFROM + "')";
            } else {
                sql[0] = "update XFAPP set CLIENTNO='" + CLIENTNO + "', name='" + NAME + "',idtype='" + IDTYPE + "',id='" + ID + "',spouseidtype='" + PIDTYPE + "',spouseid='" + PID + "',CONFMONPAY='" + CONFMONPAY + "',APPTYPE='" + APPTYPE + "',APPSTATUS='" + APPSTATUS + "',APPDATE=" + APPDATE + " where appno= '" + APPNO + "'";
                sql[1] = "update XFAPPCOMM set channel='" + CHANNEL + "',commname='" + COMMNAME + "',commtype='" + COMMTYPE + "',addr='" + ADDR + "',num='" + NUM + "',amt='" + AMT + "',receiveamt='" + RECEIVEAMT + "',appamt='" + APPAMT + "',divid='" + DIVID + "',COMMISSIONRATE='" + COMMISSIONRATE + "' where appno= '" + APPNO + "'";
                sql[2] = "update XFAPPADD set idtype='" + IDTYPE + "',id='" + ID + "',actopeningbank='" + ACTOPENINGBANK + "',bankactno='" + BANKACTNO + "',xy='" + XY + "',xyr='" + XYR + "',dy='" + DY + "',dyw='" + DYW + "',zy='" + ZY + "',zyw='" + ZYW + "',bz='" + BZ + "',bzr='" + BZR + "',credittype='" + CREDITTYPE + "',monpayamt='" + MONPAYAMT + "',linkman='" + LINKMAN + "',linkmangender='" + LINKMANGENDER + "',linkmanphone1='" + LINKMANPHONE1 + "',linkmanphone2='" + LINKMANPHONE2 + "',apprelation='" + APPRELATION + "',LINKMANADD='" + LINKMANADD + "',LINKMANCOMPANY='" + LINKMANCOMPANY + "',ACTOPENINGBANK_UD='" + ACTOPENINGBANK_UD + "' where appno= '" + APPNO + "'";
                sql[3] = "update XFCLIENT set LASTMODIFIED=SYSDATE,BIRTHDAY=to_date('" + BIRTHDAY + "','YYYY-MM-DD'),GENDER='" + GENDER + "',NATIONALITY='" + NATIONALITY + "',MARRIAGESTATUS='" + MARRIAGESTATUS + "',HUKOUADDRESS='" + HUKOUADDRESS + "',CURRENTADDRESS='" + CURRENTADDRESS + "',COMPANY='" + COMPANY + "',TITLE='" + TITLE + "',QUALIFICATION='" + QUALIFICATION + "',EDULEVEL='" + EDULEVEL + "',PHONE1='" + PHONE1 + "',PHONE2='" + PHONE2 + "',PHONE3='" + PHONE3 + "',CLIENTNO='" + CLIENTNO + "',NAME='" + NAME + "',IDTYPE='" + IDTYPE + "',ID='" + ID + "',CLIENTTYPE='" + CLIENTTYPE + "',DEGREETYPE='" + DEGREETYPE + "',COMADDR='" + COMADDR + "',SERVFROM='" + SERVFROM + "',RESIDENCEADR='" + RESIDENCEADR + "',HOUSINGSTS='" + HOUSINGSTS + "',HEALTHSTATUS='" + HEALTHSTATUS + "',MONTHLYPAY='" + MONTHLYPAY + "',BURDENSTATUS='" + BURDENSTATUS + "',EMPNO='" + EMPNO + "',SOCIALSECURITY='" + SOCIALSECURITY + "',LIVEFROM='" + LIVEFROM + "',PC='" + PC + "',COMPC='" + COMPC + "',RESDPC='" + RESDPC + "',RESDADDR='" + RESDADDR + "',EMAIL='" + EMAIL + "',actopeningbank='" + ACTOPENINGBANK + "',bankactno='" + BANKACTNO + "' where APPNO='" + APPNO + "' and XFCLTP='1' ";
                if (!PID.equals("")) {
                    sql11 = "select * from XFCLIENT where APPNO='" + APPNO + "' and XFCLTP='2'";
                    crs = manager.getRs(sql11);
                    if (crs.next()) {
                        sql[4] = "update XFCLIENT set LASTMODIFIED=SYSDATE, MARRIAGESTATUS='" + MARRIAGESTATUS + "', NAME='" + PNAME + "',IDTYPE='" + PIDTYPE + "',ID='" + PID + "',COMPANY='" + PCOMPANY + "',TITLE='" + PTITLE + "',PHONE1='" + PPHONE1 + "',PHONE3='" + PPHONE3 + "',CLIENTTYPE='" + PCLIENTTYPE + "',SERVFROM='" + PSERVFROM + "',MONTHLYPAY='" + PMONTHLYPAY + "',LIVEFROM='" + PLIVEFROM + "' where APPNO='" + APPNO + "' and XFCLTP='2' ";
                    } else
                        sql[4] = "insert into XFCLIENT (APPNO,XFCLTP,LASTMODIFIED,APPDATE,MARRIAGESTATUS,NAME,IDTYPE,ID,COMPANY,TITLE,PHONE1,PHONE3,CLIENTTYPE,SERVFROM,MONTHLYPAY,LIVEFROM) " +
                                "values ('" + APPNO + "',2,SYSDATE," + APPDATE + ",'" + MARRIAGESTATUS + "','" + PNAME + "','" + PIDTYPE + "','" + PID + "','" + PCOMPANY + "','" + PTITLE + "','" + PPHONE1 + "','" + PPHONE3 + "','" + PCLIENTTYPE + "','" + PSERVFROM + "','" + PMONTHLYPAY + "','" + PLIVEFROM + "')";
                } else
                    sql[4] = "delete from XFCLIENT where APPNO='" + APPNO + "' and XFCLTP='2' ";
            }

            sql[5] = "update CMINDVCLIENT set LASTMODIFIED=SYSDATE,BIRTHDAY=to_date('" + BIRTHDAY + "','YYYY-MM-DD'),GENDER='" + GENDER + "',NATIONALITY='" + NATIONALITY + "',MARRIAGESTATUS='" + MARRIAGESTATUS + "',HUKOUADDRESS='" + HUKOUADDRESS + "',CURRENTADDRESS='" + CURRENTADDRESS + "',COMPANY='" + COMPANY + "',TITLE='" + TITLE + "',QUALIFICATION='" + QUALIFICATION + "',EDULEVEL='" + EDULEVEL + "',PHONE1='" + PHONE1 + "',PHONE2='" + PHONE2 + "',PHONE3='" + PHONE3 + "',NAME='" + NAME + "',CLIENTTYPE='" + CLIENTTYPE + "',DEGREETYPE='" + DEGREETYPE + "',COMADDR='" + COMADDR + "',SERVFROM='" + SERVFROM + "',RESIDENCEADR='" + RESIDENCEADR + "',HOUSINGSTS='" + HOUSINGSTS + "',HEALTHSTATUS='" + HEALTHSTATUS + "',MONTHLYPAY='" + MONTHLYPAY + "',BURDENSTATUS='" + BURDENSTATUS + "',EMPNO='" + EMPNO + "',SOCIALSECURITY='" + SOCIALSECURITY + "',LIVEFROM='" + LIVEFROM + "',PC='" + PC + "',COMPC='" + COMPC + "',RESDPC='" + RESDPC + "',RESDADDR='" + RESDADDR + "',EMAIL='" + EMAIL + "',actopeningbank='" + ACTOPENINGBANK + "',bankactno='" + BANKACTNO + "' where CLIENTNO='" + CLIENTNO + "'";
//            crs = manager.getRs(sql11);
//            if (crs.next()) {
//                //根据证件确定客户
//                sql[3] = "update CMINDVCLIENT set LASTMODIFIED=SYSDATE,BIRTHDAY=to_date('" + BIRTHDAY + "','YYYY-MM-DD'),GENDER='" + GENDER + "',NATIONALITY='" + NATIONALITY + "',MARRIAGESTATUS='" + MARRIAGESTATUS + "',HUKOUADDRESS='" + HUKOUADDRESS + "',CURRENTADDRESS='" + CURRENTADDRESS + "',COMPANY='" + COMPANY + "',TITLE='" + TITLE + "',QUALIFICATION='" + QUALIFICATION + "',EDULEVEL='" + EDULEVEL + "',PHONE1='" + PHONE1 + "',PHONE2='" + PHONE2 + "',PHONE3='" + PHONE3 + "',NAME='" + NAME + "',CLIENTTYPE='" + CLIENTTYPE + "',DEGREETYPE='" + DEGREETYPE + "',COMADDR='" + COMADDR + "',SERVFROM='" + SERVFROM + "',RESIDENCEADR='" + RESIDENCEADR + "',HOUSINGSTS='" + HOUSINGSTS + "',HEALTHSTATUS='" + HEALTHSTATUS + "',MONTHLYPAY='" + MONTHLYPAY + "',BURDENSTATUS='" + BURDENSTATUS + "',EMPNO='" + EMPNO + "',SOCIALSECURITY='" + SOCIALSECURITY + "',LIVEFROM='" + LIVEFROM + "',PC='" + PC + "',COMPC='" + COMPC + "',RESDPC='" + RESDPC + "',RESDADDR='" + RESDADDR + "',EMAIL='" + EMAIL + "',actopeningbank='" + ACTOPENINGBANK + "',bankactno='" + BANKACTNO + "' where CLIENTNO='" + crs.getInt("CLIENTNO") + "'";
//            } else
//                sql[3] = "insert into CMINDVCLIENT (LASTMODIFIED,APPDATE,IDTYPE,ID,BIRTHDAY,GENDER,NATIONALITY,MARRIAGESTATUS,HUKOUADDRESS,CURRENTADDRESS,COMPANY,TITLE,QUALIFICATION,EDULEVEL,PHONE1,PHONE2,PHONE3,NAME,CLIENTTYPE,DEGREETYPE,COMADDR,SERVFROM,RESIDENCEADR,HOUSINGSTS,HEALTHSTATUS,MONTHLYPAY,BURDENSTATUS,EMPNO,SOCIALSECURITY,LIVEFROM,PC,COMPC,RESDPC,RESDADDR,EMAIL,PASSWORD, actopeningbank, bankactno) " +
//                        "values (SYSDATE," + APPDATE + ",'" + IDTYPE + "','" + ID + "',to_date('" + BIRTHDAY + "','YYYY-MM-DD'),'" + GENDER + "','" + NATIONALITY + "','" + MARRIAGESTATUS + "','" + HUKOUADDRESS + "','" + CURRENTADDRESS + "','" + COMPANY + "','" + TITLE + "','" + QUALIFICATION + "','" + EDULEVEL + "','" + PHONE1 + "','" + PHONE2 + "','" + PHONE3 + "','" + NAME + "','" + CLIENTTYPE + "','" + DEGREETYPE + "','" + COMADDR + "','" + SERVFROM + "','" + RESIDENCEADR + "','" + HOUSINGSTS + "','" + HEALTHSTATUS + "','" + MONTHLYPAY + "','" + BURDENSTATUS + "','" + EMPNO + "','" + SOCIALSECURITY + "','" + LIVEFROM + "','" + PC + "','" + COMPC + "','" + RESDPC + "','" + RESDADDR + "','" + EMAIL + "','" + PASSWORD + "','" + ACTOPENINGBANK + "','" + BANKACTNO + "')";
//
//            if (!PID.equals("")) {
//                sql12 = " select * from XFCLIENT where APPNO='" + APPNO + "' and XFCLTP='2'";
//                crs = manager.getRs(sql12);
//                if (crs.next()) {
//                    //根据证件确定客户配偶
//                    sql[4] = "update XFCLIENT set LASTMODIFIED=SYSDATE, NAME='" + PNAME + "',IDTYPE='" + PIDTYPE + "',ID='" + PID + "',COMPANY='" + PCOMPANY + "',TITLE='" + PTITLE + "',PHONE1='" + PPHONE1 + "',PHONE3='" + PPHONE3 + "',CLIENTTYPE='" + PCLIENTTYPE + "',SERVFROM='" + PSERVFROM + "',MONTHLYPAY='" + PMONTHLYPAY + "',LIVEFROM='" + PLIVEFROM + "' where APPNO='" + APPNO + "' and XFCLTP='2' ";
//                } else
//                    sql[4] = "insert into XFCLIENT (APPNO,XFCLTP,LASTMODIFIED,APPDATE,NAME,IDTYPE,ID,COMPANY,TITLE,PHONE1,PHONE3,CLIENTTYPE,SERVFROM,MONTHLYPAY,LIVEFROM) " +
//                            "values ('" + APPNO + "',2,SYSDATE," + APPDATE + ",'" + PNAME + "','" + PIDTYPE + "','" + PID + "','" + PCOMPANY + "','" + PTITLE + "','" + PPHONE1 + "','" + PPHONE3 + "','" + PCLIENTTYPE + "','" + PSERVFROM + "','" + PMONTHLYPAY + "','" + PLIVEFROM + "')";
//            }
        }
        temp = manager.execBatch(sql);
    } catch (Exception e) {
        e.printStackTrace();
    }

    if (temp) {
        //清空商城商品信息 begin
        session.removeAttribute("SID");
        session.removeAttribute("CHANNEL");
        session.removeAttribute("ORDERNO");
        session.removeAttribute("NAME");
        session.removeAttribute("EMAIL");
        session.removeAttribute("COMMATTR");
        session.removeAttribute("NUMBER");
        session.removeAttribute("AMT");
        session.removeAttribute("REQUESTTIME");
        //清空商城商品信息 end


        if (APPSTATUS.equals(""))
            session.setAttribute("goUrl", "./consume/applist.jsp?NAME=" + NAME + "&IDTYPE=" + IDTYPE + "&ID=" + ID + "&PASSWORD=" + PASSWORD);

        if ((ACTOPENINGBANK.equals("801") || ACTOPENINGBANK.equals("802")) && !BANKACTNO.equals("")) {
            sql13 = " select SEQNO from XFAPP where APPNO='" + APPNO + "' ";
            crs = manager.getRs(sql13);
            if (crs.next()) {
                SEQNO = crs.getString("SEQNO");
                if (ACTOPENINGBANK.equals("801") && !BANKACTNO.equals("")) {

%>
<script type="text/javascript">
    document.location = "/alipay/qianyue.jsp?out_sign_no=<%=SEQNO%>&email=<%=BANKACTNO%>";
</script>
<%
} else if (ACTOPENINGBANK.equals("802") && !BANKACTNO.equals("")) {
%>
<script type="text/javascript">
    //window.opener.close();
    document.location = "/99bill/send.jsp?out_sign_no=<%=SEQNO%>&email=<%=BANKACTNO%>&NAME=<%=NAME%>&COMMNAME=<%=COMMNAME%>";
</script>
<%--<jsp:forward page="/99bill/send.jsp">--%>
<%--<jsp:param name="out_sign_no" value="<%=SEQNO%>"/>--%>
<%--<jsp:param name="email" value="<%=BANKACTNO%>"/>--%>
<%--<jsp:param name="NAME" value="<%=NAME%>"/>--%>
<%--<jsp:param name="COMMNAME" value="<%=COMMNAME%>"/>--%>
<%--</jsp:forward>--%>
<%
            }
        } else {
            session.setAttribute("msg", "您的申请已提交成功，但支付方式获取失败，请联系我们！");
        }
    } else {
        String funcdel = "";
        funcdel += "parent.opener.document.getElementById('print').className = 'page_button_active';";
        funcdel += "parent.opener.document.getElementById('print').disabled = false;";
        funcdel += "parent.opener.document.getElementById('APPNO').value='" + APPNO + "';";
//        funcdel += "parent.opener.document.getElementById('APPNO').value='" + APPNO + "';";
        funcdel += "parent.opener.document.getElementById('APPSTATUS').value='" + APPSTATUS + "';";
//        funcdel += "window.setTimeout('window.close()',5000);";
        funcdel += "parent.opener.document.getElementById('print').click();";
                funcdel += "pageWinClose();";

//        funcdel += "window.opener.document.getElementById('print').click();";
//        funcdel += "pageWinClose();";
//        funcdel += "window.close();";
        //haiyu add 作废判断 2010-08-11
        if (!APPNO.equals("") && APPACTFLAG.equals("3")) {//作废
            session.setAttribute("msg", "作废申请已提交成功，请在单击确定后，连接您的打印机，<br>选择您的申请单打印并签署姓名，连同您的证明文件复印件<br>一同寄送给我们，审核通过后我们将与您取得联系！");
        } else {
            session.setAttribute("msg", "您的申请已提交成功，请在单击确定后，连接您的打印机，<br>选择您的申请单打印并签署姓名，连同您的证明文件复印件<br>一同寄送给我们，审核通过后我们将与您取得联系！");
        }
        session.setAttribute("funcdel", funcdel);
        session.setAttribute("isback", "0");

        response.sendRedirect("/showinfo.jsp");
    }
} else {
    session.setAttribute("msg", "提交信息失败!");
%>
<jsp:forward page="application_start.jsp"/>
<%
        }
    }
%>