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
Title: �����Ŵ�-�������ѷ��ڸ����������ύ
Description: �����Ŵ�-�������ѷ��ڸ����������ύ��
 * @version  $Revision: 1.0 $  $Date: 2009/03/11 06:33:37 $
 * @author
 * <p/>�޸ģ�$Author: liuj $
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
    String PASSWORD = request.getParameter("PASSWORD");   //����
    String CLIENTNO = request.getParameter("CLIENTNO");   //�ͻ���

    String APPACTFLAG = request.getParameter("APPACTFLAG");           //���뵥ִ��״̬   ִ�ж�����־��1��������2���˻أ�3������
    String APPNO = request.getParameter("APPNO");                      //���뵥���
    String XFAPPNO = request.getParameter("XFAPPNO");                  //�����뵥���
    String APPDATE = request.getParameter("APPDATE");                  //��������
    String IDTYPE = request.getParameter("IDTYPE");                    //֤������
    String ID = request.getParameter("ID");                             //֤������
    String APPTYPE = request.getParameter("APPTYPE");                  //��������
    String APPSTATUS = request.getParameter("APPSTATUS");              //����״̬
    String BIRTHDAY = request.getParameter("BIRTHDAY");                //��������
    String GENDER = request.getParameter("GENDER");                    //�Ա� enum=Gender
    String NATIONALITY = request.getParameter("NATIONALITY");         //����
    String MARRIAGESTATUS = request.getParameter("MARRIAGESTATUS");   //����״�� enum=MarriageStatus
    String HUKOUADDRESS = request.getParameter("HUKOUADDRESS");       //�������ڵ�
    String CURRENTADDRESS = request.getParameter("CURRENTADDRESS");   //��סַ
    String COMPANY = request.getParameter("COMPANY");                  //������λ
    String TITLE = request.getParameter("TITLE");                      //ְ�� enum=Title
    String QUALIFICATION = request.getParameter("QUALIFICATION");     //ְ�� enum=Qualification
    String EDULEVEL = request.getParameter("EDULEVEL");                //ѧ�� enum=EduLevel
    String PHONE1 = request.getParameter("PHONE1");                   //�ƶ��绰
    String PHONE2 = request.getParameter("PHONE2");                   //��ͥ�绰
    String PHONE3 = request.getParameter("PHONE3");                   //�칫�绰
    String NAME = request.getParameter("NAME");                       //�ͻ����� desc=��ҵ(����)����
    String CLIENTTYPE = request.getParameter("CLIENTTYPE");           //�ͻ����� enum=ClientType1
    String DEGREETYPE = request.getParameter("DEGREETYPE");          //���ѧλ enum=DegreeType
    String COMADDR = request.getParameter("COMADDR");                 //��λ��ַ
    String SERVFROM = request.getParameter("SERVFROM");               //�ֵ�λ����ʱ��
    String RESIDENCEADR = request.getParameter("RESIDENCEADR");       //�������ڵ�(�����) enum=ResidenceADR
    String HOUSINGSTS = request.getParameter("HOUSINGSTS");           //��ס״�� enum=HousingSts
    String HEALTHSTATUS = request.getParameter("HEALTHSTATUS");       //����״�� enum=HealthStatus
    String MONTHLYPAY = request.getParameter("MONTHLYPAY");           //����������
    String BURDENSTATUS = request.getParameter("BURDENSTATUS");       //����״�� enum=BurdenStatus
    String EMPNO = request.getParameter("EMPNO");                      //Ա��������
    String SOCIALSECURITY_G[] = request.getParameterValues("SOCIALSECURITY");   //��ᱣ�� enum=SocialSecurity
    String LIVEFROM = request.getParameter("LIVEFROM");                //���ؾ�סʱ��
    String PC = request.getParameter("PC");                             //סլ�ʱ�
    String COMPC = request.getParameter("COMPC");                      //��λ�ʱ�
    String RESDPC = request.getParameter("RESDPC");                    //���͵�ַ�ʱ�
    String RESDADDR = request.getParameter("RESDADDR");                //���͵�ַ
    String EMAIL = request.getParameter("EMAIL");                      //�����ʼ�

    String PNAME = request.getParameter("PNAME");                      //��ż����
    String PIDTYPE = request.getParameter("PIDTYPE");                  //��ż֤������
    String PID = request.getParameter("PID");                          //��ż֤������
    String PCOMPANY = request.getParameter("PCOMPANY");                //��ż������λ
    String PTITLE = request.getParameter("PTITLE");                    //��żְ�� enum=Title
    String PPHONE1 = request.getParameter("PPHONE1");                  //��ż�ƶ��绰
    String PPHONE3 = request.getParameter("PPHONE3");                  //��ż�칫�绰
    String PCLIENTTYPE = request.getParameter("PCLIENTTYPE");         //��ż�ͻ�����(��λ����) enum=ClientType1
    String PSERVFROM = request.getParameter("PSERVFROM");             //��ż�ֵ�λ����ʱ��
    String PMONTHLYPAY = request.getParameter("PMONTHLYPAY");         //��ż����������
    String PLIVEFROM = request.getParameter("PLIVEFROM");             //��ż���ؾ�סʱ��

    String CHANNEL = request.getParameter("CHANNEL");                  //���۵�λ(��������)
    String COMMNAME = request.getParameter("COMMNAME");                //��Ʒ����
    String COMMTYPE = request.getParameter("COMMTYPE");                //��Ʒ�ͺ�
    String ADDR = request.getParameter("ADDR");                         //���͵�ַ
    String NUM = request.getParameter("NUM");                           //��������
    String AMT = request.getParameter("AMT");                           //�ܽ��
    String RECEIVEAMT = request.getParameter("RECEIVEAMT");            //�Ѹ����
    String APPAMT = request.getParameter("APPAMT");                     //���ڽ��
    String DIVID = request.getParameter("DIVID");                       //��������

    String ACTOPENINGBANK = request.getParameter("ACTOPENINGBANK");   //������ enum=Bank
    String BANKACTNO = request.getParameter("BANKACTNO");              //�����ʺ�
    String XY = request.getParameter("XY");                             //���� enum=YesNo
    String XYR = request.getParameter("XYR");                           //����������
    String DY = request.getParameter("DY");                             //��Ѻ enum=YesNo
    String DYW = request.getParameter("DYW");                           //��Ѻ������
    String ZY = request.getParameter("ZY");                             //��Ѻ enum=YesNo
    String ZYW = request.getParameter("ZYW");                           //��Ѻ������
    String BZ = request.getParameter("BZ");                             //��֤ enum=YesNo
    String BZR = request.getParameter("BZR");                           //��֤������
    String CREDITTYPE_G[] = request.getParameterValues("CREDITTYPE");  //�������� enum=CreditType
    String MONPAYAMT = request.getParameter("MONPAYAMT");              //�¾������
    String LINKMAN = request.getParameter("LINKMAN");                   //��ϵ������
    String LINKMANGENDER = request.getParameter("LINKMANGENDER");      //��ϵ���Ա�
    String LINKMANPHONE1 = request.getParameter("LINKMANPHONE1");       //��ϵ���ƶ��绰
    String LINKMANPHONE2 = request.getParameter("LINKMANPHONE2");       //��ϵ�˹̶��绰
    String APPRELATION = request.getParameter("APPRELATION");           //�������˹�ϵ enum=AppRelation
    String LINKMANADD = request.getParameter("LINKMANADD");             //��ϵ��סַ
    String LINKMANCOMPANY = request.getParameter("LINKMANCOMPANY");    //��ϵ�˹�����λ
    String ACTOPENINGBANK_UD = request.getParameter("ACTOPENINGBANK_UD");    //���������ƣ���㡢��������¼�����ƣ�

    String SID = request.getParameter("SID");                           //�����̳Ǳ��
    String ORDERNO = request.getParameter("ORDERNO");                   //�����̳ǵ���
    String REQUESTTIME = request.getParameter("REQUESTTIME");          //�����̳Ƕ�������ʱ��


    APPNO = (APPNO == null) ? "" : APPNO.trim();                    //���뵥���
    APPDATE = (APPDATE == null) ? "SYSDATE" : "to_date('" + APPDATE.trim() + "','YYYY-MM-DD')";      //��������
    IDTYPE = (IDTYPE == null) ? "" : IDTYPE;                        //֤������
    ID = (ID == null) ? "" : ID;                                    //֤������
    APPTYPE = (APPTYPE == null) ? "" : APPTYPE;                     //��������
    APPSTATUS = (APPSTATUS == null) ? "" : APPSTATUS;               //����״̬
    BIRTHDAY = (BIRTHDAY == null) ? "" : BIRTHDAY;                  //��������
    GENDER = (GENDER == null) ? "" : GENDER;                        //�Ա� enum=Gender
    NATIONALITY = (NATIONALITY == null) ? "" : NATIONALITY;         //����
    MARRIAGESTATUS = (MARRIAGESTATUS == null) ? "" : MARRIAGESTATUS;//����״�� enum=MarriageStatus
    HUKOUADDRESS = (HUKOUADDRESS == null) ? "" : HUKOUADDRESS;      //�������ڵ�
    CURRENTADDRESS = (CURRENTADDRESS == null) ? "" : CURRENTADDRESS;//��סַ
    COMPANY = (COMPANY == null) ? "" : COMPANY;                     //������λ
    TITLE = (TITLE == null) ? "" : TITLE;                           //ְ�� enum=Title
    QUALIFICATION = (QUALIFICATION == null) ? "" : QUALIFICATION;   //ְ�� enum=Qualification
    EDULEVEL = (EDULEVEL == null) ? "" : EDULEVEL;                  //ѧ�� enum=EduLevel
    PHONE1 = (PHONE1 == null) ? "" : PHONE1;                        //�ƶ��绰
    PHONE2 = (PHONE2 == null) ? "" : PHONE2;                        //��ͥ�绰
    PHONE3 = (PHONE3 == null) ? "" : PHONE3;                        //�칫�绰
    NAME = (NAME == null) ? "" : NAME;                              //�ͻ����� desc=(��ҵ(����)����
    CLIENTTYPE = (CLIENTTYPE == null) ? "" : CLIENTTYPE;            //�ͻ����� enum=ClientType1
    DEGREETYPE = (DEGREETYPE == null) ? "" : DEGREETYPE;            //���ѧλ enum=DegreeType
    COMADDR = (COMADDR == null) ? "" : COMADDR;                     //��λ��ַ
    SERVFROM = (SERVFROM == null) ? "" : SERVFROM;                  //�ֵ�λ����ʱ��
    RESIDENCEADR = (RESIDENCEADR == null) ? "" : RESIDENCEADR;      //�������ڵ�(�����) enum=ResidenceADR
    HOUSINGSTS = (HOUSINGSTS == null) ? "" : HOUSINGSTS;            //��ס״�� enum=HousingSts
    HEALTHSTATUS = (HEALTHSTATUS == null) ? "" : HEALTHSTATUS;      //����״�� enum=HealthStatus
    MONTHLYPAY = (MONTHLYPAY == null) ? "" : MONTHLYPAY;           //����������
    BURDENSTATUS = (BURDENSTATUS == null) ? "" : BURDENSTATUS;      //����״�� enum=BurdenStatus
    EMPNO = (EMPNO == null) ? "" : EMPNO.trim();                    //Ա��������
    LIVEFROM = (LIVEFROM == null) ? "" : LIVEFROM;                  //���ؾ�סʱ��
    PC = (PC == null) ? "" : PC;                                    //סլ�ʱ�
    COMPC = (COMPC == null) ? "" : COMPC;                           //��λ�ʱ�
    RESDPC = (RESDPC == null) ? "" : RESDPC;                        //���͵�ַ�ʱ�
    RESDADDR = (RESDADDR == null) ? "" : RESDADDR;                  //���͵�ַ
    EMAIL = (EMAIL == null) ? "" : EMAIL;                           //�����ʼ�

    PNAME = (PNAME == null) ? "" : PNAME;                           //��ż����
    PIDTYPE = (PIDTYPE == null) ? "" : PIDTYPE;                     //��ż֤������
    PID = (PID == null) ? "" : PID;                                 //��ż֤������
    PCOMPANY = (PCOMPANY == null) ? "" : PCOMPANY;                  //��ż������λ
    PTITLE = (PTITLE == null) ? "" : PTITLE;                        //��żְ�� enum=Title
    PPHONE1 = (PPHONE1 == null) ? "" : PPHONE1;                     //��ż�ƶ��绰
    PPHONE3 = (PPHONE3 == null) ? "" : PPHONE3;                     //��ż�칫�绰
    PCLIENTTYPE = (PCLIENTTYPE == null) ? "" : PCLIENTTYPE;         //��ż�ͻ�����(��λ����) enum=ClientType1
    PSERVFROM = (PSERVFROM == null) ? "" : PSERVFROM;               //��ż�ֵ�λ����ʱ��
    PMONTHLYPAY = (PMONTHLYPAY == null) ? "" : PMONTHLYPAY;        //��ż����������
    PLIVEFROM = (PLIVEFROM == null) ? "" : PLIVEFROM;               //��ż���ؾ�סʱ��

    CHANNEL = (CHANNEL == null) ? "" : CHANNEL.trim();              //���۵�λ(��������)
    COMMNAME = (COMMNAME == null) ? "" : COMMNAME.trim();           //��Ʒ����
    COMMTYPE = (COMMTYPE == null) ? "" : COMMTYPE.trim();           //��Ʒ�ͺ�
    ADDR = (ADDR == null) ? "" : ADDR;                              //���͵�ַ
    NUM = (NUM == null) ? "" : NUM;                                 //��������
    AMT = (AMT == null) ? "" : AMT;                                 //�ܽ��
    RECEIVEAMT = (RECEIVEAMT == null) ? "" : RECEIVEAMT;            //�Ѹ����
    APPAMT = (APPAMT == null) ? "" : APPAMT;                        //���ڽ��
    DIVID = (DIVID == null) ? "" : DIVID;                           //��������

    ACTOPENINGBANK = (ACTOPENINGBANK == null) ? "" : ACTOPENINGBANK;//������ enum=Bank
    BANKACTNO = (BANKACTNO == null) ? "" : BANKACTNO;               //�����ʺ�
    XY = (XY == null) ? "" : XY;                                    //���� enum=YesNo
    XYR = (XYR == null) ? "" : XYR;                                 //����������
    DY = (DY == null) ? "" : DY;                                    //��Ѻ enum=YesNo
    DYW = (DYW == null) ? "" : DYW;                                 //��Ѻ������
    ZY = (ZY == null) ? "" : ZY;                                    //��Ѻ enum=YesNo
    ZYW = (ZYW == null) ? "" : ZYW;                                 //��Ѻ������
    BZ = (BZ == null) ? "" : BZ;                                    //��֤ enum=YesNo
    BZR = (BZR == null) ? "" : BZR;                                 //��֤������
    MONPAYAMT = (MONPAYAMT == null) ? "" : MONPAYAMT;               //�¾������
    LINKMAN = (LINKMAN == null) ? "" : LINKMAN;                     //��ϵ������
    LINKMANGENDER = (LINKMANGENDER == null) ? "" : LINKMANGENDER;   //��ϵ���Ա�
    LINKMANPHONE1 = (LINKMANPHONE1 == null) ? "" : LINKMANPHONE1;   //��ϵ���ƶ��绰
    LINKMANPHONE2 = (LINKMANPHONE2 == null) ? "" : LINKMANPHONE2;   //��ϵ�˹̶��绰
    APPRELATION = (APPRELATION == null) ? "" : APPRELATION;         //�������˹�ϵ enum=AppRelation
    LINKMANADD = (LINKMANADD == null) ? "" : LINKMANADD;            //��ϵ��סַ
    LINKMANCOMPANY = (LINKMANCOMPANY == null) ? "" : LINKMANCOMPANY;//��ϵ�˹�����λ
    ACTOPENINGBANK_UD = (ACTOPENINGBANK_UD == null) ? "" : ACTOPENINGBANK_UD;//���������ƣ���㡢��������¼�����ƣ�

    SID = (SID == null) ? "" : SID;                                 //�����̳Ǳ��
    ORDERNO = (ORDERNO == null) ? "" : ORDERNO;                     //�����̳ǵ���
    REQUESTTIME = (REQUESTTIME == null) ? "" : REQUESTTIME;         //�����̳Ƕ�������ʱ��

    BigDecimal dMONTHLYPAY = new BigDecimal((MONTHLYPAY.equals("")) ? "0" : MONTHLYPAY);
    BigDecimal dPMONTHLYPAY = new BigDecimal((PMONTHLYPAY.equals("")) ? "0" : PMONTHLYPAY);
    String CONFMONPAY = String.valueOf(dMONTHLYPAY.add(dPMONTHLYPAY));//�˶�������


    if (IDTYPE.equals("") || ID.equals("")) {
        session.setAttribute("msg", "û�з��ִ�����Ĳ�����");
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

    //���ڶ�ѡ���Ĭ�ϵ� splitechar"_" �ָ��鴮
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

    //����֤��ȷ������ǼǵĿͻ�����
    //sql11 = " select CLIENTNO,APPDATE from CMINDVCLIENT where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and rownum=1  order by CLIENTNO desc";

    ConnectionManager manager = ConnectionManager.getInstance();
    CachedRowSet crs;
    try {
        if (!APPNO.equals("") && APPACTFLAG.equals("3")) {//����
            sql[0] = "update XFAPP set APPSTATUS='0' where appno='" + APPNO + "'";
        } else if (!APPNO.equals("") && APPACTFLAG.equals("2")) {//�˻�
            if (um == null) {
                response.sendRedirect("../fcworkbench/error.jsp");
            }
            BMRouteBindNode dt = Workflow.getRouteBindByUserTp(String.valueOf(um.getUser().getStatus()));
            APPSTATUS = Workflow.getBeforeStatusBeginByUserTp(dt.status, APPAMT);//���״̬Ϊ�գ��򷵻������ύ״̬��1����
            sql[0] = "update XFAPP set APPSTATUS='" + APPSTATUS + "' where appno='" + APPNO + "'";
            sql[1] = "delete from XFOPINION where appno='" + APPNO + "' and operator='" + um.getUserName() + "'";
        } else {
            //��ȡ��������
            int isEmp = (EMPNO.equals("")) ? 0 : 1;
            String SOURCEID = (SID.equals("")) ? "000" : SID;
            String COMMISSIONRATE = manager.getCellValue("SERVICECHARGE", "XFPRODUCT", "SOURCEID=" + SOURCEID + " and APPTYPE=" + APPTYPE + " and DURATION=to_number('" + DIVID + "') and CLIENTCD=" + isEmp);
            COMMISSIONRATE = (COMMISSIONRATE == null) ? "" : COMMISSIONRATE;

            if (APPNO.equals("")) {
                if (XFAPPNO == null || XFAPPNO.length() != 22) {
                    session.setAttribute("msg", "���뵥�Ż�ȡʧ�ܣ�����ϵ����!");
%>
<jsp:forward page="application_start.jsp"/>
<%
                }
                APPNO = XFAPPNO;
                if (ACTOPENINGBANK.equals("801") || ACTOPENINGBANK.equals("802"))
                    APPSTATUS = XFConf.APPSTATUS_QIANYUE; //֧��������Ǯ����ҪǩԼ״̬��
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
//                //����֤��ȷ���ͻ�
//                sql[3] = "update CMINDVCLIENT set LASTMODIFIED=SYSDATE,BIRTHDAY=to_date('" + BIRTHDAY + "','YYYY-MM-DD'),GENDER='" + GENDER + "',NATIONALITY='" + NATIONALITY + "',MARRIAGESTATUS='" + MARRIAGESTATUS + "',HUKOUADDRESS='" + HUKOUADDRESS + "',CURRENTADDRESS='" + CURRENTADDRESS + "',COMPANY='" + COMPANY + "',TITLE='" + TITLE + "',QUALIFICATION='" + QUALIFICATION + "',EDULEVEL='" + EDULEVEL + "',PHONE1='" + PHONE1 + "',PHONE2='" + PHONE2 + "',PHONE3='" + PHONE3 + "',NAME='" + NAME + "',CLIENTTYPE='" + CLIENTTYPE + "',DEGREETYPE='" + DEGREETYPE + "',COMADDR='" + COMADDR + "',SERVFROM='" + SERVFROM + "',RESIDENCEADR='" + RESIDENCEADR + "',HOUSINGSTS='" + HOUSINGSTS + "',HEALTHSTATUS='" + HEALTHSTATUS + "',MONTHLYPAY='" + MONTHLYPAY + "',BURDENSTATUS='" + BURDENSTATUS + "',EMPNO='" + EMPNO + "',SOCIALSECURITY='" + SOCIALSECURITY + "',LIVEFROM='" + LIVEFROM + "',PC='" + PC + "',COMPC='" + COMPC + "',RESDPC='" + RESDPC + "',RESDADDR='" + RESDADDR + "',EMAIL='" + EMAIL + "',actopeningbank='" + ACTOPENINGBANK + "',bankactno='" + BANKACTNO + "' where CLIENTNO='" + crs.getInt("CLIENTNO") + "'";
//            } else
//                sql[3] = "insert into CMINDVCLIENT (LASTMODIFIED,APPDATE,IDTYPE,ID,BIRTHDAY,GENDER,NATIONALITY,MARRIAGESTATUS,HUKOUADDRESS,CURRENTADDRESS,COMPANY,TITLE,QUALIFICATION,EDULEVEL,PHONE1,PHONE2,PHONE3,NAME,CLIENTTYPE,DEGREETYPE,COMADDR,SERVFROM,RESIDENCEADR,HOUSINGSTS,HEALTHSTATUS,MONTHLYPAY,BURDENSTATUS,EMPNO,SOCIALSECURITY,LIVEFROM,PC,COMPC,RESDPC,RESDADDR,EMAIL,PASSWORD, actopeningbank, bankactno) " +
//                        "values (SYSDATE," + APPDATE + ",'" + IDTYPE + "','" + ID + "',to_date('" + BIRTHDAY + "','YYYY-MM-DD'),'" + GENDER + "','" + NATIONALITY + "','" + MARRIAGESTATUS + "','" + HUKOUADDRESS + "','" + CURRENTADDRESS + "','" + COMPANY + "','" + TITLE + "','" + QUALIFICATION + "','" + EDULEVEL + "','" + PHONE1 + "','" + PHONE2 + "','" + PHONE3 + "','" + NAME + "','" + CLIENTTYPE + "','" + DEGREETYPE + "','" + COMADDR + "','" + SERVFROM + "','" + RESIDENCEADR + "','" + HOUSINGSTS + "','" + HEALTHSTATUS + "','" + MONTHLYPAY + "','" + BURDENSTATUS + "','" + EMPNO + "','" + SOCIALSECURITY + "','" + LIVEFROM + "','" + PC + "','" + COMPC + "','" + RESDPC + "','" + RESDADDR + "','" + EMAIL + "','" + PASSWORD + "','" + ACTOPENINGBANK + "','" + BANKACTNO + "')";
//
//            if (!PID.equals("")) {
//                sql12 = " select * from XFCLIENT where APPNO='" + APPNO + "' and XFCLTP='2'";
//                crs = manager.getRs(sql12);
//                if (crs.next()) {
//                    //����֤��ȷ���ͻ���ż
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
        //����̳���Ʒ��Ϣ begin
        session.removeAttribute("SID");
        session.removeAttribute("CHANNEL");
        session.removeAttribute("ORDERNO");
        session.removeAttribute("NAME");
        session.removeAttribute("EMAIL");
        session.removeAttribute("COMMATTR");
        session.removeAttribute("NUMBER");
        session.removeAttribute("AMT");
        session.removeAttribute("REQUESTTIME");
        //����̳���Ʒ��Ϣ end


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
            session.setAttribute("msg", "�����������ύ�ɹ�����֧����ʽ��ȡʧ�ܣ�����ϵ���ǣ�");
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
        //haiyu add �����ж� 2010-08-11
        if (!APPNO.equals("") && APPACTFLAG.equals("3")) {//����
            session.setAttribute("msg", "�����������ύ�ɹ������ڵ���ȷ�����������Ĵ�ӡ����<br>ѡ���������뵥��ӡ��ǩ����������ͬ����֤���ļ���ӡ��<br>һͬ���͸����ǣ����ͨ�������ǽ�����ȡ����ϵ��");
        } else {
            session.setAttribute("msg", "�����������ύ�ɹ������ڵ���ȷ�����������Ĵ�ӡ����<br>ѡ���������뵥��ӡ��ǩ����������ͬ����֤���ļ���ӡ��<br>һͬ���͸����ǣ����ͨ�������ǽ�����ȡ����ϵ��");
        }
        session.setAttribute("funcdel", funcdel);
        session.setAttribute("isback", "0");

        response.sendRedirect("/showinfo.jsp");
    }
} else {
    session.setAttribute("msg", "�ύ��Ϣʧ��!");
%>
<jsp:forward page="application_start.jsp"/>
<%
        }
    }
%>