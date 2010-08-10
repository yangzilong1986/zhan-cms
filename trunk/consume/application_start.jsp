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
Title: �����Ŵ�-�������ѷ��ڸ���������
Description: �������ѷ��ڸ��������顣
 * @version  $Revision: 1.0 $  $Date: 2009/03/02 08:20:31 $
 * @author liujian
 * <p/>�޸ģ�$Author: liuj $
===============================================
--%>
<%
    request.setCharacterEncoding("GBK");
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (um == null) {
//        response.sendRedirect("../fcworkbench/error.jsp");
//    }

    String SID = (String) session.getAttribute("SID");                   //�����̳Ǳ��
    String ORDERNO = (String) session.getAttribute("ORDERNO");           //�����̳ǵ���
    String REQUESTTIME = (String) session.getAttribute("REQUESTTIME");  //�����̳Ƕ�������ʱ��
    String APPNO = request.getParameter("APPNO");         //���뵥���
    String IDTYPE = request.getParameter("IDTYPE");       //֤������
    String ID = request.getParameter("ID");                //֤������
    String NAME = request.getParameter("NAME");            //�ͻ����� desc=��ҵ(����)����
    String APPTYPE = request.getParameter("APPTYPE");     //��������
    String APPSTATUS = request.getParameter("APPSTATUS"); //����״̬
    String PASSWORD = request.getParameter("PASSWORD");   //����

    String strGoUrl = request.getParameter("goUrl");      //�ر�ҳ���־������ҳ��رգ��ǵ���������ҳ��
    String closeClick = "pageWinClose();";
    String ContractTMP = "./��ͬ��ͨ.htm";
    if (strGoUrl != null) closeClick = "gopage('" + strGoUrl + "')";

    if (APPNO == null && (ID == null || IDTYPE == null)) {
        session.setAttribute("msg", "û�з��ִ�����Ĳ�����");
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


        String CLIENTNO = "";                  //�ͻ���
        String BIRTHDAY = "";                  //��������
        String GENDER = "";                    //�Ա� enum=Gender
        String NATIONALITY = "";               //����
        String MARRIAGESTATUS = "";            //����״�� enum=MarriageStatus
        String HUKOUADDRESS = "";              //�������ڵ�
        String CURRENTADDRESS = "";            //��סַ
        String COMPANY = "";                   //������λ
        String TITLE = "";                     //ְ�� enum=Title
        String QUALIFICATION = "";             //ְ�� enum=Qualification
        String EDULEVEL = "";                  //ѧ�� enum=EduLevel
        String PHONE1 = "";                    //�ƶ��绰
        String PHONE2 = "";                    //��ͥ�绰
        String PHONE3 = "";                    //�칫�绰
        String CLIENTTYPE = "";                //�ͻ����� enum=ClientType1
        String DEGREETYPE = "";                //���ѧλ enum=DegreeType
        String COMADDR = "";                   //��λ��ַ
        String SERVFROM = "";                  //�ֵ�λ����ʱ��
        String RESIDENCEADR = "";              //�������ڵ�(�����) enum=ResidenceADR
        String HOUSINGSTS = "";                //��ס״�� enum=HousingSts
        String HEALTHSTATUS = "";              //����״�� enum=HealthStatus
        String MONTHLYPAY = "";                //����������
        String BURDENSTATUS = "";              //����״�� enum=BurdenStatus
        String EMPNO = "";                     //Ա��������
        String SOCIALSECURITY = "";            //��ᱣ�� enum=SocialSecurity
        String LIVEFROM = "";                  //���ؾ�סʱ��
        String PC = "";                        //סլ�ʱ�
        String COMPC = "";                     //��λ�ʱ�
        String RESDPC = "";                    //���͵�ַ�ʱ�
        String RESDADDR = "";                  //���͵�ַ
        String EMAIL = "";                     //�����ʼ�

        String PNAME = "";                     //��ż����
        String PIDTYPE = "";                   //��ż֤������
        String PID = "";                       //��ż֤������
        String PCOMPANY = "";                  //��ż������λ
        String PTITLE = "";                    //��żְ�� enum=Title
        String PPHONE1 = "";                   //��ż�ƶ��绰
        String PPHONE3 = "";                   //��ż�칫�绰
        String PCLIENTTYPE = "";               //��ż�ͻ�����(��λ����) enum=ClientType1
        String PSERVFROM = "";                 //��ż�ֵ�λ����ʱ��
        String PMONTHLYPAY = "";               //��ż����������
        String PLIVEFROM = "";                 //��ż���ؾ�סʱ��

        String CHANNEL = (String) session.getAttribute("CHANNEL");  //���۵�λ(��������)
        String COMMNAME = (String) session.getAttribute("COMMATTR");//��Ʒ����
        String COMMTYPE = "";                  //��Ʒ�ͺ�
        String ADDR = "";                      //���͵�ַ
        String NUM = (String) session.getAttribute("NUMBER");       //��������
        String AMT = (String) session.getAttribute("AMT");           //�ܽ��
        String RECEIVEAMT = "";                //�Ѹ����
        String APPAMT = "";                    //���ڽ��
        String DIVID = "";                     //��������

        String ACTOPENINGBANK = "";            //������ enum=Bank
        String BANKACTNO = "";                 //�����ʺ�
        String XY = "";                        //���� enum=YesNo
        String XYR = "";                       //����������
        String DY = "";                        //��Ѻ enum=YesNo
        String DYW = "";                       //��Ѻ������
        String ZY = "";                        //��Ѻ enum=YesNo
        String ZYW = "";                       //��Ѻ������
        String BZ = "";                        //��֤ enum=YesNo
        String BZR = "";                       //��֤������
        String CREDITTYPE = "";                //�������� enum=CreditType
        String MONPAYAMT = "";                 //�¾������
        String LINKMAN = "";                   //��ϵ������
        String LINKMANGENDER = "";             //��ϵ���Ա�
        String LINKMANPHONE1 = "";              //��ϵ���ƶ��绰
        String LINKMANPHONE2 = "";              //��ϵ�˹̶��绰
        String APPRELATION = "";               //�������˹�ϵ enum=AppRelation
        String LINKMANADD = "";                //��ϵ��סַ
        String LINKMANCOMPANY = "";            //��ϵ�˹�����λ
        String ACTOPENINGBANK_UD = "";         //���������ƣ���㡢��������¼�����ƣ�

        String APPDATE = "";                   //��������


        boolean ifErrClient = false;
        CachedRowSet crs = null;
        if (!sql1.equals("")) crs = manager.getRs(sql1);
        if (crs != null && crs.size() > 0) {
            crs.next();
            //if (NAME.compareTo(crs.getString("NAME")) != 0) ifErrClient = true;  //�ͻ����� desc=��ҵ(����)����)
            NAME = crs.getString("NAME");                           //�ͻ����� desc=��ҵ(����)����)
            CLIENTNO = crs.getString("CLIENTNO");                //�ͻ���
            BIRTHDAY = crs.getString("BIRTHDAY");                //��������
            GENDER = crs.getString("GENDER");                    //�Ա� enum=Gender
            NATIONALITY = crs.getString("NATIONALITY");         //����
            MARRIAGESTATUS = crs.getString("MARRIAGESTATUS");   //����״�� enum=MarriageStatus
            HUKOUADDRESS = crs.getString("HUKOUADDRESS");       //�������ڵ�
            CURRENTADDRESS = crs.getString("CURRENTADDRESS");   //��סַ
            COMPANY = crs.getString("COMPANY");                  //������λ
            TITLE = crs.getString("TITLE");                      //ְ�� enum=Title
            QUALIFICATION = crs.getString("QUALIFICATION");     //ְ�� enum=Qualification
            EDULEVEL = crs.getString("EDULEVEL");                //ѧ�� enum=EduLevel
            PHONE1 = crs.getString("PHONE1");                   //�ƶ��绰
            PHONE2 = crs.getString("PHONE2");                   //��ͥ�绰
            PHONE3 = crs.getString("PHONE3");                   //�칫�绰
            CLIENTTYPE = crs.getString("CLIENTTYPE");           //�ͻ����� enum=ClientType1
            DEGREETYPE = crs.getString("DEGREETYPE");          //���ѧλ enum=DegreeType
            COMADDR = crs.getString("COMADDR");                 //��λ��ַ
            SERVFROM = crs.getString("SERVFROM");               //�ֵ�λ����ʱ��
            RESIDENCEADR = crs.getString("RESIDENCEADR");       //�������ڵ�(�����) enum=ResidenceADR
            HOUSINGSTS = crs.getString("HOUSINGSTS");           //��ס״�� enum=HousingSts
            HEALTHSTATUS = crs.getString("HEALTHSTATUS");       //����״�� enum=HealthStatus
            MONTHLYPAY = crs.getString("MONTHLYPAY");           //����������
            BURDENSTATUS = crs.getString("BURDENSTATUS");       //����״�� enum=BurdenStatus
            EMPNO = crs.getString("EMPNO");                      //Ա��������
            SOCIALSECURITY = crs.getString("SOCIALSECURITY");   //��ᱣ�� enum=SocialSecurity
            LIVEFROM = crs.getString("LIVEFROM");               //���ؾ�סʱ��
            PC = crs.getString("PC");                            //סլ�ʱ�
            COMPC = crs.getString("COMPC");                      //��λ�ʱ�
            RESDPC = crs.getString("RESDPC");                    //���͵�ַ�ʱ�
            RESDADDR = crs.getString("RESDADDR");                //���͵�ַ
            EMAIL = crs.getString("EMAIL");                      //�����ʼ�

            PNAME = crs.getString("PNAME");                      //��ż����
            PIDTYPE = crs.getString("PIDTYPE");                  //��ż֤������
            PID = crs.getString("PID");                          //��ż֤������
            PCOMPANY = crs.getString("PCOMPANY");                //��ż������λ
            PTITLE = crs.getString("PTITLE");                    //��żְ�� enum=Title
            PPHONE1 = crs.getString("PPHONE1");                  //��ż�ƶ��绰
            PPHONE3 = crs.getString("PPHONE3");                  //��ż�칫�绰
            PCLIENTTYPE = crs.getString("PCLIENTTYPE");         //��ż�ͻ�����(��λ����) enum=ClientType1
            PSERVFROM = crs.getString("PSERVFROM");             //��ż�ֵ�λ����ʱ��
            PMONTHLYPAY = crs.getString("PMONTHLYPAY");         //��ż����������
            PLIVEFROM = crs.getString("PLIVEFROM");             //��ż���ؾ�סʱ��

            CHANNEL = crs.getString("CHANNEL");                  //���۵�λ(��������)
            COMMNAME = crs.getString("COMMNAME");                //��Ʒ����
            COMMTYPE = crs.getString("COMMTYPE");                //��Ʒ�ͺ�
            ADDR = crs.getString("ADDR");                         //���͵�ַ
            NUM = crs.getString("NUM");                           //��������
            AMT = crs.getString("AMT");                           //�ܽ��
            RECEIVEAMT = crs.getString("RECEIVEAMT");            //�Ѹ����
            APPAMT = crs.getString("APPAMT");                     //���ڽ��
            DIVID = crs.getString("DIVID");                       //��������

            ACTOPENINGBANK = crs.getString("ACTOPENINGBANK");   //������ enum=Bank
            BANKACTNO = crs.getString("BANKACTNO");              //�����ʺ�
            XY = crs.getString("XY");                             //���� enum=YesNo
            XYR = crs.getString("XYR");                           //����������
            DY = crs.getString("DY");                             //��Ѻ enum=YesNo
            DYW = crs.getString("DYW");                           //��Ѻ������
            ZY = crs.getString("ZY");                             //��Ѻ enum=YesNo
            ZYW = crs.getString("ZYW");                           //��Ѻ������
            BZ = crs.getString("BZ");                             //��֤ enum=YesNo
            BZR = crs.getString("BZR");                           //��֤������
            CREDITTYPE = crs.getString("CREDITTYPE");            //�������� enum=CreditType
            MONPAYAMT = crs.getString("MONPAYAMT");              //�¾������
            LINKMAN = crs.getString("LINKMAN");                   //��ϵ������
            LINKMANGENDER = crs.getString("LINKMANGENDER");      //��ϵ���Ա�
            LINKMANPHONE1 = crs.getString("LINKMANPHONE1");      //��ϵ���ƶ��绰
            LINKMANPHONE2 = crs.getString("LINKMANPHONE2");      //��ϵ�˹̶��绰
            APPRELATION = crs.getString("APPRELATION");          //�������˹�ϵ enum=AppRelation
            LINKMANADD = crs.getString("LINKMANADD");            //��ϵ��סַ
            LINKMANCOMPANY = crs.getString("LINKMANCOMPANY");    //��ϵ�˹�����λ
            ACTOPENINGBANK_UD = crs.getString("ACTOPENINGBANK_UD");    //���������ƣ���㡢��������¼�����ƣ�


            ID = crs.getString("ID");                              //֤������
            IDTYPE = crs.getString("IDTYPE");                     //֤������
            APPDATE = crs.getString("APPDATE");                   //��������
            APPTYPE = crs.getString("APPTYPE");                   //��������
            APPSTATUS = crs.getString("APPSTATUS");               //����״̬


            SID = crs.getString("SID");                            //�����̳Ǳ��
            ORDERNO = crs.getString("ORDERNO");                    //�����̳ǵ���
            REQUESTTIME = crs.getString("REQUESTTIME");           //�����̳Ƕ�������ʱ��
        } else if (!sql2.equals("")) {
            crs = manager.getRs(sql2);
            if (crs != null && crs.size() > 0) {
                crs.next();
                CLIENTNO = crs.getString("CLIENTNO");                //�ͻ���
                NAME = crs.getString("NAME");                        //�ͻ����� desc=��ҵ(����)����)
                BIRTHDAY = crs.getString("BIRTHDAY");                //��������
                GENDER = crs.getString("GENDER");                    //�Ա� enum=Gender
                NATIONALITY = crs.getString("NATIONALITY");         //����
                MARRIAGESTATUS = crs.getString("MARRIAGESTATUS");   //����״�� enum=MarriageStatus
                HUKOUADDRESS = crs.getString("HUKOUADDRESS");       //�������ڵ�
                CURRENTADDRESS = crs.getString("CURRENTADDRESS");   //��סַ
                COMPANY = crs.getString("COMPANY");                  //������λ
                TITLE = crs.getString("TITLE");                      //ְ�� enum=Title
                QUALIFICATION = crs.getString("QUALIFICATION");     //ְ�� enum=Qualification
                EDULEVEL = crs.getString("EDULEVEL");                //ѧ�� enum=EduLevel
                PHONE1 = crs.getString("PHONE1");                   //�ƶ��绰
                PHONE2 = crs.getString("PHONE2");                   //��ͥ�绰
                PHONE3 = crs.getString("PHONE3");                   //�칫�绰
                CLIENTTYPE = crs.getString("CLIENTTYPE");           //�ͻ����� enum=ClientType1
                DEGREETYPE = crs.getString("DEGREETYPE");          //���ѧλ enum=DegreeType
                COMADDR = crs.getString("COMADDR");                 //��λ��ַ
                SERVFROM = crs.getString("SERVFROM");               //�ֵ�λ����ʱ��
                RESIDENCEADR = crs.getString("RESIDENCEADR");       //�������ڵ�(�����) enum=ResidenceADR
                HOUSINGSTS = crs.getString("HOUSINGSTS");           //��ס״�� enum=HousingSts
                HEALTHSTATUS = crs.getString("HEALTHSTATUS");       //����״�� enum=HealthStatus
                MONTHLYPAY = crs.getString("MONTHLYPAY");           //����������
                BURDENSTATUS = crs.getString("BURDENSTATUS");       //����״�� enum=BurdenStatus
                EMPNO = crs.getString("EMPNO");                      //Ա��������
                SOCIALSECURITY = crs.getString("SOCIALSECURITY");   //��ᱣ�� enum=SocialSecurity
                LIVEFROM = crs.getString("LIVEFROM");                //���ؾ�סʱ��
                PC = crs.getString("PC");                             //סլ�ʱ�
                COMPC = crs.getString("COMPC");                      //��λ�ʱ�
                RESDPC = crs.getString("RESDPC");                    //���͵�ַ�ʱ�
                RESDADDR = crs.getString("RESDADDR");                //���͵�ַ
                EMAIL = crs.getString("EMAIL");                      //�����ʼ�
                ACTOPENINGBANK = crs.getString("ACTOPENINGBANK");   //������ enum=Bank
                BANKACTNO = crs.getString("BANKACTNO");              //�����ʺ�
                APPDATE = crs.getString("APPDATE");                  //��������

//                ID = crs.getString("ID");                              //֤������
//                IDTYPE = crs.getString("IDTYPE");                     //֤������
            }
        }


        String S_ID = (String) session.getAttribute("SID");                   //�����̳Ǳ��
        S_ID = (S_ID == null) ? "" : S_ID;
        if (!S_ID.equals("")) {
            SID = (String) session.getAttribute("SID");                   //�����̳Ǳ��
            ORDERNO = (String) session.getAttribute("ORDERNO");          //�����̳ǵ���
            REQUESTTIME = (String) session.getAttribute("REQUESTTIME"); //�����̳Ƕ�������ʱ��
            CHANNEL = (String) session.getAttribute("CHANNEL");          //���۵�λ(��������)
            COMMNAME = (String) session.getAttribute("COMMATTR");        //��Ʒ���Ƽ��ͺ�
            NUM = (String) session.getAttribute("NUMBER");               //��Ʒ����
            AMT = (String) session.getAttribute("AMT");                   //�������
            AMT = (AMT == null) ? "" : AMT.substring(0, AMT.length() - 2) + "." + AMT.substring(AMT.length() - 2, AMT.length());
        }

        NAME = (NAME == null) ? "" : NAME.trim();
        PNAME = (PNAME == null) ? "" : PNAME.trim();
        APPSTATUS = (APPSTATUS == null) ? "" : APPSTATUS;
        BIRTHDAY = (BIRTHDAY == null) ? "" : BIRTHDAY;

        SID = (SID == null) ? "" : SID.trim();                          //�����̳Ǳ�š���������ֱ��|001�������̳�
        ORDERNO = (ORDERNO == null) ? "" : ORDERNO;
        REQUESTTIME = (REQUESTTIME == null) ? "" : REQUESTTIME.trim();
        CHANNEL = (CHANNEL == null) ? "" : CHANNEL.trim();
        COMMNAME = (COMMNAME == null) ? "" : COMMNAME.trim();
        NUM = (NUM == null) ? "" : NUM.trim();
        AMT = (AMT == null) ? "" : AMT.trim();


        String readonly = "";
        String readonly_input = "readonly";
        String submit = "class='btn_2k3'";
        String title = "�������ѷ��ڸ���������";

        if (ifErrClient) {
            String mess = "�ͻ���Ϣ���󣬴�֤���ѱ������ͻ�ʹ�ã�";
            mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
            session.setAttribute("msg", mess);
            response.sendRedirect("../showinfo.jsp");
        }
%>
<html>
<head id="head1">
    <title>�����Ŵ�</title>
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

    function doPrint() {
        var str = "�ǳ���л��ѡ�񺣶�����˾�ķ��ڸ���ҵ��������������������Ҫ׼���Ĳ��ϣ�\n";
        str += "\n";
        str += "* ���֤����ӡ�����ݣ���ǩ�֣��ڶ������֤���������涼��ӡ��\n";
        str += "   Ա������ӡ��һ�� ���ڲ�Ա����\n";
        str += "* ���ѷ��ڸ���������һ�ݣ�ǩ��\n";
        str += "* ���ü�¼��ѯ�����һ�ݣ�ǩ��\n";
        str += "* ���л����˺�ǩ�ָ�ӡ����֧�������Ǯ�˺Ž�ͼ��ǩ�ָ�ӡ��\n";
        str += "\n";
        str += "���й�ͬ�����˲��룬������\n";
        str += " 1����ͬ�����˵����֤����ӡ�����ݣ���ǩ�֣��ڶ������֤���������涼��ӡ��\n";
        str += " 2����ͬ�����˵����ü�¼��ѯ�����һ�ݣ�ǩ�� \n\n";
        str += "����������������������������������������������������������������������������������������\n\n";
        var str2 = "��ӡ�����ڴ�ӡ�������뵥�����õ�����Ȩ���Ϸֱ�ǩ�ֺ�\n��ͬҪ���֤���ļ���ӡ�����͸����ǣ����ǽ����������������������������룡";
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
<!--media=print �������˵�������ڴ�ӡʱ��Ч-->
<!--ϣ����ӡʱ����ʾ����������class="Noprint"��ʽ-->
<!--ϣ����Ϊ���÷�ҳ��λ������class="PageNext"��ʽ-->
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
                                                                                                   color="#FFFFFF"><b>�������Ų����������ι�˾�������ѷ��ڸ���������</b></font>
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
    <td colspan="7" align="center" class="page_form_List_title">�������Ų����������ι�˾�������ѷ��ڸ���������</td>
</tr>
<tr class='page_form_tr'>
    <td colspan="7" style="padding:0px; margin:0px;" height="20" valign="bottom">
        <table width="100%" cellspacing="0" cellpadding="0" border=0 style="font-size:9pt">
            <tr class='page_form_tr'>
                <td>
                    <input type="hidden" name="APPACTFLAG" value="1"><%--ִ�ж�����־��1��������2���˻أ�3������--%>
                    <input type="hidden" name="PASSWORD" value="<%=PASSWORD%>">
                    <input type="hidden" name="CLIENTNO" value="<%=CLIENTNO%>">
                    <input type="hidden" name="APPNO" value="<%=APPNO%>" id="APPNO">
                    <input type="hidden" name="APPSTATUS" value="<%=APPSTATUS==null?"":APPSTATUS%>" id="APPSTATUS">

                    <input type="hidden" name="SID" value="<%=SID%>">
                    <input type="hidden" name="ORDERNO" value="<%=ORDERNO%>">
                    <input type="hidden" name="REQUESTTIME" value="<%=REQUESTTIME%>">
                    ��������:<%
                    if (um == null) {
                        out.print(APPDATE == null ? "" : APPDATE);
                    } else {
                %><input type="text" <%=readonly_input%> name="APPDATE"
                         <%--value="<%=APPDATE==null?"":DBUtil.to_Date(APPDATE)%>"--%>
                         value="<%=APPDATE==null?"":APPDATE%>"
                         class="page_form_text" style="width:63px">
                    <input type="button" value="��" class="page_form_refbutton" onClick="setday(this,winform.APPDATE)">
                    <%}%>
                    &nbsp;����״̬:<%=(APPSTATUS == null || APPSTATUS.equals("")) ? "" : level.getEnumItemName("AppStatus", APPSTATUS)%>
                </td>
                <td align="right"><%if (SID.equals("001")) {%>�̳ǵ���:<%=ORDERNO%><%;ContractTMP = "./��ͬ�̳�.htm";}%></td>
                <td align="right">���뵥��:<%
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
    <td rowspan="11" class="page_left_table_title" nowrap>���������</td>
    <td class="page_form_title_td" nowrap>&nbsp;��&nbsp;&nbsp;����</td>
    <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="NAME"
                                           value="<%=NAME==null?"":NAME%>"
                                           class="page_form_text" maxlength="40"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
    <td class="page_form_title_td" nowrap>&nbsp;��&nbsp;&nbsp;��</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("GENDER", "Gender", GENDER)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;��&nbsp;&nbsp;����</td>
    <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="PHONE1"
                                           value="<%=PHONE1==null?"":PHONE1%>"
                                           class="page_form_text" maxlength="15"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
    <td class="page_form_title_td" nowrap>&nbsp;Ա�������룺</td>
    <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="EMPNO"
                                           value="<%=EMPNO==null?"":EMPNO%>" class="page_form_text" maxlength="8"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;���֤�����ƣ�</td>
    <td colspan="2" nowrap class="page_form_td">
        <%--<select name="IDTYPE" class="page_form_select">--%>
        <%--<option value='0'>���֤</option>--%>
        <%--</select>--%>
        <%=level.levelHere("IDTYPE", "IDType", IDTYPE)%>
    </td>
    <td class="page_form_title_td" nowrap>&nbsp;֤�����룺</td>
    <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="ID" id="ID"
                                           value="<%=ID==null?"":ID%>" class="page_form_text"
                                           onblur="if(checkIDCard(document.getElementById('IDTYPE'),this))getbirthday(this.value,'BIRTHDAY');"
                                           maxlength="18"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;�������ڣ�</td>
    <td class="page_form_td" nowrap><input type="text" <%=readonly_input%> name="BIRTHDAY"
                                           value="<%=BIRTHDAY.equals("")?"":BIRTHDAY%>"
                                           <%--value="<%=BIRTHDAY.equals("")?"":DBUtil.to_Date(BIRTHDAY)%>"--%>
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap><input type="button" value="��" class="page_form_refbutton"
                                           onClick="setday(this,winform.BIRTHDAY)"></td>
    <td class="page_form_title_td" nowrap>&nbsp;����״����</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("HEALTHSTATUS", "HealthStatus", HEALTHSTATUS)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;����״����</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("MARRIAGESTATUS", "MarriageStatus", MARRIAGESTATUS)%>
    </td>
    <td class="page_form_title_td" nowrap>&nbsp;�Ƿ�����Ů��</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("BURDENSTATUS", "BurdenStatus", BURDENSTATUS)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;�������ڵأ�</td>
    <td colspan="5" class="page_form_td" nowrap><%=level.radioHere("RESIDENCEADR", "ResidenceADR", RESIDENCEADR)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;��ͥסַ��</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="CURRENTADDRESS"
               value="<%=CURRENTADDRESS==null?"":CURRENTADDRESS%>"
               class="page_form_text" style="width:100%" maxlength="40"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;סլ�ʱࣺ</td>
    <td colspan="2" class="page_form_td"><input type="text" <%=readonly%> name="PC"
                                                value="<%=PC==null?"":PC%>"
                                                class="page_form_text" maxlength="6"></td>
    <td class="page_form_title_td" nowrap>&nbsp;�������䣺</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="EMAIL"
               value="<%=EMAIL==null?"":EMAIL%>"
               class="page_form_text" maxlength="40"></td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;��ͥ�绰��</td>
    <td colspan="2" class="page_form_td"><input type="text" <%=readonly%> name="PHONE2"
                                                value="<%=PHONE2==null?"":PHONE2%>"
                                                class="page_form_text" maxlength="15"></td>
    <td class="page_form_title_td" nowrap>&nbsp;���ؾ�סʱ�䣺</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="LIVEFROM"
               value="<%=LIVEFROM==null?"":LIVEFROM%>"
               class="page_form_text" maxlength="20"></td>
    <td class="page_form_td">��</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;�ܽ����̶ȣ�</td>
    <td colspan="5" class="page_form_td" nowrap><%=level.radioHere("EDULEVEL", "EduLevel", EDULEVEL)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;ְ&nbsp;&nbsp;�ƣ�</td>
    <td colspan="5" class="page_form_td"
        nowrap><%=level.radioHere("QUALIFICATION", "Qualification", QUALIFICATION)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="8" class="page_left_table_title">�������ϣ�</td>
    <td class="page_form_title_td" nowrap>&nbsp;������λ��</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="COMPANY"
               value="<%=COMPANY==null?"":COMPANY%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;��λ�绰��</td>
    <td colspan="2" class="page_form_td">
        <input type="text" <%=readonly%> name="PHONE3"
               value="<%=PHONE3==null?"":PHONE3%>"
               class="page_form_text" maxlength="15">
    </td>
    <td class="page_form_title_td" nowrap>&nbsp;��λ�ʱࣺ</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="COMPC"
               value="<%=COMPC==null?"":COMPC%>"
               class="page_form_text" maxlength="6">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;��λ��ַ��</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="COMADDR"
               value="<%=COMADDR==null?"":COMADDR%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;��λ���ʣ�</td>
    <td colspan="5" class="page_form_td" nowrap><%=level.radioHere("CLIENTTYPE", "ClientType1", CLIENTTYPE)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;ְ&nbsp;&nbsp;��</td>
    <td colspan="5" class="page_form_td" nowrap><%=level.levelHere("TITLE", "Title", TITLE)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;�ֵ�λ����ʱ�䣺</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="SERVFROM"
               value="<%=SERVFROM==null?"":SERVFROM%>"
               class="page_form_text" maxlength="20"></td>
    <td colspan="4" class="page_form_td">��</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;���������룺</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="MONTHLYPAY"
               value="<%=MONTHLYPAY==null?"":MONTHLYPAY%>"
               class="page_form_text" maxlength="12"></td>
    <td colspan="4" class="page_form_td">Ԫ</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;������ḣ��<br>&nbsp;�����ƶ������</td>
    <td colspan="5" class="page_form_td"
        nowrap><%=level.checkHere("SOCIALSECURITY", "SocialSecurity", SOCIALSECURITY)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>

<tr class='page_form_tr' onClick="showList(1);" id="HT2">
    <td class="page_slide_table_title" colspan="7" height="18" id="HT1" title="�������" onMouseOver="mOvr(this);"
        onMouseOut="mOut(this);">
        �����˻���ѹ��������ż��ͬ�������д��ż������Ϣ -- ��<font color="red">(���չ��)</font>
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
                <td rowspan="8" class="page_left_table_title" nowrap>��ż�������(������żΪ��ͬ���������������)</td>
                <td class="page_form_title_td" nowrap>&nbsp;��ż������</td>
                <td colspan="5" class="page_form_td" nowrap><input type="text" <%=readonly%> name="PNAME"
                                                                   value="<%=PNAME==null?"":PNAME%>"
                                                                   class="page_form_text" maxlength="40"></td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>��ż���֤�����ƣ�</td>
                <td class="page_form_td" colspan="2" nowrap><%=level.levelHere("PIDTYPE", "IDType", PIDTYPE)%>
                </td>
                <td class="page_form_title_td" nowrap>&nbsp;��ż֤�����룺</td>
                <td class="page_form_td">
                    <input type="text" <%=readonly%> name="PID" id="PID" value="<%=PID==null?"":PID%>"
                           onblur="if(this.value.Trim()!=''&&document.getElementById('PNAME').value.Trim()==''){alert('��ż��������Ϊ�գ�');document.getElementById('PNAME').focus();}else checkIDCard(document.getElementById('PIDTYPE'),this);"
                           class="page_form_text" maxlength="18"></td>
                <td class="page_form_td">&nbsp;</td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;������λ��</td>
                <td colspan="4" class="page_form_td">
                    <input type="text" <%=readonly%> name="PCOMPANY"
                           value="<%=PCOMPANY==null?"":PCOMPANY%>"
                           class="page_form_text" style="width:337pt" maxlength="40"></td>
                <td class="page_form_td">&nbsp;</td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;��λ�绰��</td>
                <td colspan="5" class="page_form_td">
                    <input type="text" <%=readonly%> name="PPHONE3"
                           value="<%=PPHONE3==null?"":PPHONE3%>"
                           class="page_form_text" maxlength="15">
                </td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;��λ���ʣ�</td>
                <td colspan="5" class="page_form_td"
                    nowrap><%=level.radioHere("PCLIENTTYPE", "ClientType1", PCLIENTTYPE)%>
                </td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;ְ&nbsp;&nbsp;��</td>
                <td colspan="5" class="page_form_td" nowrap><%=level.levelHere("PTITLE", "Title", PTITLE)%>
                </td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;�ֵ�λ����ʱ�䣺</td>
                <td class="page_form_td">
                    <input type="text" <%=readonly%> name="PSERVFROM"
                           value="<%=PSERVFROM==null?"":PSERVFROM%>"
                           class="page_form_text" maxlength="20"></td>
                <td class="page_form_td">��</td>
                <td class="page_form_title_td" nowrap>&nbsp;���ؾ�סʱ�䣺</td>
                <td class="page_form_td">
                    <input type="text" <%=readonly%> name="PLIVEFROM"
                           value="<%=PLIVEFROM==null?"":PLIVEFROM%>"
                           class="page_form_text" maxlength="20"></td>
                <td class="page_form_td">��</td>
            </tr>
            <tr class='page_form_tr'>
                <td class="page_form_title_td" nowrap>&nbsp;���˵绰��</td>
                <td class="page_form_td">
                    <input type="text" <%=readonly%> name="PPHONE1"
                           value="<%=PPHONE1==null?"":PPHONE1%>"
                           class="page_form_text" maxlength="15"></td>
                <td class="page_form_td">&nbsp;</td>
                <td class="page_form_title_td" nowrap>&nbsp;���������룺</td>
                <td class="page_form_td">
                    <input type="text" <%=readonly%> name="PMONTHLYPAY"
                           value="<%=PMONTHLYPAY==null?"":PMONTHLYPAY%>"
                           class="page_form_text" maxlength="12"></td>
                <td class="page_form_td">Ԫ</td>
            </tr>
        </table>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="5" class="page_left_table_title">�⹺��Ʒ���</td>
    <td class="page_form_title_td">���۵�λ�����ƣ���</td>
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
    <td class="page_form_title_td">&nbsp;�⹺��Ʒ����<br>&nbsp;���ͺţ�</td>
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
    <td class="page_form_title_td" nowrap>�⹺��Ʒʹ�õ�ַ��</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="ADDR"
               value="<%=ADDR==null?"":ADDR%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;�⹺��Ʒ������</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="NUM"
               value="<%=NUM==null?"":NUM%>"
               class="page_form_text" maxlength="10"></td>
    <td class="page_form_td">̨/��</td>
    <td class="page_form_title_td" nowrap>&nbsp;�������ͣ�</td>
    <td colspan="2" class="page_form_td"><%=level.radioHere("APPTYPE", "AppType", APPTYPE)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;��Ʒ�ܽ�</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="AMT"
               value="<%=AMT==null?"":AMT%>"
               class="page_form_text" maxlength="12" onKeyUp="countAppAmt()"></td>
    <td class="page_form_td">Ԫ</td>
    <td class="page_form_title_td">&nbsp;�׸��</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="RECEIVEAMT"
               value="<%=RECEIVEAMT==null?"":RECEIVEAMT%>"
               class="page_form_text" maxlength="12" onKeyUp="countAppAmt()"
               onblur="checkAmt();"></td>
    <td class="page_form_td">Ԫ</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="3" class="page_left_table_title">����������</td>
    <td class="page_form_title_td" nowrap>��������ܽ���</td>
    <td class="page_form_td">
        <input type="text" <%=readonly_input%> name="APPAMT"
               value="<%=APPAMT==null?"":APPAMT%>"
               class="page_form_text" maxlength="15"></td>
    <td class="page_form_td">Ԫ</td>
    <td class="page_form_title_td" nowrap>&nbsp;�������ޣ�</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("DIVID", "Divid", DIVID)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;���ʽ��</td>
    <td colspan="5" nowrap class="page_form_td"><%=level.radioHere("ACTOPENINGBANK", "Bank4App", ACTOPENINGBANK)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;�����˺ţ�</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="BANKACTNO"
               value="<%=BANKACTNO==null?"":BANKACTNO%>"
               class="page_form_text" maxlength="30">
    </td>
    <td class="page_form_td">&nbsp;</td>
    <td class="page_form_title_td" nowrap><span id="BANK_UD1" style="display:none">&nbsp;���������ƣ�</span></td>
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
<%--<td rowspan="4" class="page_left_table_title">�ṩ������ʽ</td>--%>
<%--<td class="page_form_title_td" nowrap><label>--%>
<%--<input name="XY" type="checkbox"--%>
<%--onClick="if (this.checked==true){ document.all.XYR.readOnly=false; }else {document.all.XYR.readOnly='<%=readonly_input%>'}"--%>
<%--value="1" <%if (XY == "1") {%>"checked"<%--%>
<%--}--%>
<%--;--%>
<%--%>/>--%>
<%--����</label>--%>
<%--&nbsp;&nbsp;����������--%>
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
<%--��Ѻ</label>--%>
<%--&nbsp;&nbsp;��Ѻ������--%>
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
<%--��Ѻ</label>--%>
<%--&nbsp;&nbsp;��Ѻ������--%>
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
<%--��֤</label>--%>
<%--&nbsp;&nbsp;��֤������--%>
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
    <td rowspan="2" class="page_left_table_title">��������<br/>
        ʹ�����
    </td>
    <td class="page_form_title_td" nowrap>&nbsp;�������ࣺ</td>
    <td colspan="5" class="page_form_td" nowrap><%=level.checkHere("CREDITTYPE", "CreditType", CREDITTYPE)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;�¾�����</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="MONPAYAMT"
               value="<%=MONPAYAMT==null?"":MONPAYAMT%>"
               class="page_form_text" maxlength="12"></td>
    <td colspan="4" class="page_form_td">Ԫ</td>
</tr>

<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="2" class="page_left_table_title" nowrap>�ʼĵ�ַ<br><span
            style="font-size:12px;font-weight:normal; color:red;">���ź����˵���ַ��</span>
    </td>
    <td class="page_form_title_td" nowrap>&nbsp;�ʼĵ�ַ��</td>
    <td colspan="4" class="page_form_td" nowrap>
        <input type="text" <%=readonly%> name="RESDADDR"
               value="<%=RESDADDR==null?"":RESDADDR%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;�������룺</td>
    <td colspan="4" class="page_form_td"><input type="text" <%=readonly%> name="RESDPC"
                                                value="<%=RESDPC==null?"":RESDPC%>"
                                                class="page_form_text" maxlength="6"></td>

    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_button_tbl_tr" colspan="7" height="5"></td>
</tr>
<tr class='page_form_tr'>
    <td rowspan="5" class="page_left_table_title">������ϵ��</td>
    <td class="page_form_title_td" nowrap>&nbsp;��&nbsp;&nbsp;����</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="LINKMAN"
               value="<%=LINKMAN==null?"":LINKMAN%>"
               class="page_form_text" maxlength="40"
               onblur="checkLinkname('LINKMAN')"></td>
    <td class="page_form_td">&nbsp;</td>
    <td class="page_form_title_td" nowrap>&nbsp;��&nbsp;&nbsp;��</td>
    <td colspan="2" nowrap class="page_form_td"><%=level.radioHere("LINKMANGENDER", "Gender", LINKMANGENDER)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;��&nbsp;&nbsp;����</td>
    <td class="page_form_td">
        <input type="text" <%=readonly%> name="LINKMANPHONE1"
               value="<%=LINKMANPHONE1==null?"":LINKMANPHONE1%>"
               class="page_form_text" maxlength="15"
               onblur="checkLinkPhone('LINKMANPHONE1');"></td>
    <td class="page_form_td">&nbsp;</td>
    <td class="page_form_title_td" nowrap>&nbsp;�̶��绰��</td>
    <td nowrap class="page_form_td"><input type="text" <%=readonly%> name="LINKMANPHONE2"
                                           value="<%=LINKMANPHONE2==null?"":LINKMANPHONE2%>"
                                           class="page_form_text" maxlength="15"></td>
    <td class="page_form_td">&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;�������˹�ϵ��</td>
    <td colspan="4" class="page_form_td">
        <%=level.radioHere("APPRELATION", "AppRelation", APPRELATION)%>
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;��ͥ��ַ��</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="LINKMANADD"
               value="<%=LINKMANADD==null?"":LINKMANADD%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_title_td" nowrap>&nbsp;������λ��</td>
    <td colspan="4" class="page_form_td">
        <input type="text" <%=readonly%> name="LINKMANCOMPANY"
               value="<%=LINKMANCOMPANY==null?"":LINKMANCOMPANY%>"
               class="page_form_text" style="width:100%" maxlength="40">
    </td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
<tr class='page_form_tr'>
    <td class="page_left_table_title">��Լ</td>
    <td class="page_form_title_td" nowrap valign="bottom">&nbsp;ͬ���Լ��</td>
    <td class="page_form_td" valign="bottom">
        <input class="page_form_radio" type="radio" name="grpCon" id="radAgr" checked>ͬ��
        <input class="page_form_radio" type="radio" name="grpCon" id="radNoAgr">��ͬ��
    </td>
    <td colspan="4" valign="bottom">
        <a onclick="window.open('application_preshow1.jsp')" style="color:red;" href="#about:blank">�鿴��Լ����</a>
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
                <td rowspan="1" class="page_left_table_title">������������ǩ��</td>
                <td class="page_form_td">
                    <span style="font-size:13px;font-weight: bold;">������ǩ�ֽ���Ϊ���·��������Ψһ���ݣ�</span><br>
                    &nbsp;���ˣ��ȣ�֤ʵ�������ϼ����������ϵ��ļ�ȫ����ʵ�޶����Ȩ�������Ų����������ι�˾ͨ<br>&nbsp;&nbsp;&nbsp;�������йػ�������ʿ��;����֤���ʽ����������ϵ���ʵ�Ժ������ԡ�<br>
                    &nbsp;�󾭹�˾��鲻���Ϲ涨�ķ��ڸ���������δ������ʱ�����ˣ��ȣ������顣<br>
                    &nbsp;���ˣ��ȣ��ѽ����ͬ����ڸ����Լ���������������ɱ��ˣ��ȣ����˾�����벢ʹ�÷���<br>&nbsp;&nbsp;&nbsp;���ʽ�ķ���Э�飬������ʧʵ����٣����˽��е���Ӧ�����¼��������Ρ�<br>
                    &nbsp;���ˣ��ȣ���֤��ȡ�ù�˾����󣬰�ʱ����ϸ����շ��ڸ����Լִ�С�<br>
                    &nbsp;���ˣ��ȣ�ͬ�⺣�����Ų����������ι�˾������һ���й����˷��ڸ���ϵ�в�Ʒ��Χ�����ջ���ڸ�<br>&nbsp;&nbsp;&nbsp;���Լ���޸���������Ȩ����<br>
                    &nbsp;����ʾ������ǩ����ʾ�������ط��ڸ����Լ����ع涨������Լ��覣�����Ӱ����δ�������е�������<br>&nbsp;&nbsp;&nbsp;����
                    <hr>
                    <span style="font-size:14px; text-align:right">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;����������������&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    ���ڣ�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;</span>
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
<span id="HA5" style="display:none;"><br><br><br><br><jsp:include page="./��Ҫ�ṩ���ļ�.htm"/></span>

<div class="PageNext" id="HA6" style="display:none;"></div>
<span id="HA7" style="display:none;"><jsp:include page="<%=ContractTMP%>"/></span>
<span id="HA8" style="display:none;"><br><br><br><br><br><br><br><jsp:include page="./������������ǩ��.htm"/></span>

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
                                                                  value=' �� �� ' onClick="return Regvalid();"></td>
                            <td class='page_button_tbl_td'><input type='button' class='btn_2k3' id='addfile'
                                                                  name='addfile'
                                                                  value='�ϴ�����'
                                                                  onClick="fileup('<%=XFAPPNO%>','<%=APPSTATUS%>');">
                            </td>
                            <%
                                }
                                if (um != null && APPSTATUS.equals(XFConf.APPSTATUS_TIJIAO)) {
                            %>
                            <td class='page_button_tbl_td'><input type='button' <%=submit%> id='savedel' name='save'
                                                                  value=' �� �� ' onClick="return Regvalid1();"></td>
                            <%
                                }
                                if (!APPSTATUS.equals("")) {
                            %>
                            <%--<td class='page_button_tbl_td'><input type='button' class='page_button_active' id="print"--%>
                                                                  <%--name='print'--%>
                                                                  <%--onclick="javascript:doPrint()" value=" �� ӡ "></td>--%>
                            <%
                                if (Double.parseDouble(APPSTATUS) > 1 && !APPSTATUS.equals(XFConf.APPSTATUS_WANCHENG)) {
                            %>
                            <%--<td class='page_button_tbl_td'><input type='button' <%=submit%> id='saveRtn' name='save'--%>
                            <%--value=' �� �� ' onClick="return Regvalid2();"></td>--%>
                            <%
                                    }
                                }
                            %>
                            <td class='page_button_tbl_td'><input type='button' class='btn_2k3' id="print"
                                                                  name='print'
                                                                  onclick="javascript:doPrint()" value=" �� ӡ "></td>
                            <td class='page_button_tbl_td'><input type='hidden' class='btn_2k3'
                                                                  name='button'
                                                                  value=' �� �� ' onClick="<%=closeClick%>"></td>
                            <%--<td class='page_button_tbl_td'><input type='button' class='page_button_active' name='button'--%>
                            <%--onclick="javascript:printit()" value=" �� ӡ "></td>--%>
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
               style="height: 95px; border:none; font-weight:normal; font-size: 10px; color:#8F87E0;font-family:'΢���ź�';">
            <tr>
                <td width="30%">&nbsp;</td>
                <td width="40%" valign="middle">
                    <table border="0" cellspacing="0" cellpadding="0" width="330" align="center"
                           style="height: 20px; border:none; font-weight:normal; font-size: 12px; color:#004080;font-family:'΢���ź�';">
                        <tr>
                            <td width="30%">������ѯ�绰��<span style="color:#FF0066">0532-88939384��88939383</span>&nbsp;&nbsp;�ʱࣺ266101<br>��ַ��ɽ��ʡ�ൺ�к���·1�ź�����ҵ԰K����������405��
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="30%" valign="bottom" align="right">���ս���Ȩ�麣�����Ų����������ι�˾����&nbsp;</td>
            </tr>
        </table>
    </td>
</tr>
</table>
</body>


<%--���߿ͷ����� 365call-- �б�ʽ--%>
<%--<script type='text/javascript' src='http://chat2.365webcall.com/IMMe1.aspx?settings=mw7mNmXNNm6X7Xbz3Am600bPz3Am6wIbNz3AN6mm00&LL=0'></script>--%>

<%--���߿ͷ����� 365call--  ͼ�귽ʽ ����--%>
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
            inputObjpreObj(obj2).innerText = " " + e[i].parentNode.innerText + "�ʺ�";
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
    inputObjpreObj(obj2).innerText = " " + obj1.parentNode.innerText + "�ʺ�";
}

//ͬ����ͥסַ���ʼĵ�ַ
function getRESDADDR() {
    if (document.getElementById("RESDADDR").value == "")
        document.getElementById("RESDADDR").value = document.getElementById("CURRENTADDRESS").value;
}
//ͬ����ͥ�ʱ���ʼ��ʱ�
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
//    if(o.propertyName!='value')return;  //����value�ı䲻ִ������Ĳ���
//
//    //.��������
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
    else if (!chkintWithAlStr("LIVEFROM", "���ؾ�סʱ���뱣��������"))return false;
    if (!isEmptyItem("EDULEVEL"))return false;
    if (!isEmptyItem("QUALIFICATION"))return false;
    if (!isEmptyItem("COMPANY"))return false;
    if (!isEmptyItem("PHONE3"))return false;
    else if (!isPhone("PHONE3"))return false;
    if (!isZipCode("COMPC"))return false;
    if (!isEmptyItem("COMADDR"))return false;
    if (!isEmptyItem("CLIENTTYPE"))return false;

    if (!isEmptyItem("SERVFROM"))return false;
    else if (!chkintWithAlStr("SERVFROM", "�ֵ�λ����ʱ���뱣��������"))return false;

    if (!isEmptyItem("MONTHLYPAY"))return false;
    else if (!chkdecWithAlStr("MONTHLYPAY", "������������¼�����֣�"))return false;
    //if (!isEmptyItem("SOCIALSECURITY"))return false;

    if (document.getElementById("PID").value.Trim() != "") {
        if (document.getElementById("ID").value == document.getElementById("PID").value) {
            alert("��ż֤�������������ʵ��");
            document.getElementById("PID").focus();
            return false;
        }
    }
    if (document.getElementById("PNAME").value.Trim() != "") {
        if (!isEmptyItem("PID"))return false;
        if (!isEmptyItem("PCLIENTTYPE"))return false;
        if (!isEmptyItem("PMONTHLYPAY"))return false;
        else if (!chkdecWithAlStr("PMONTHLYPAY", "������������¼�����֣�"))return false;
    }
    if (!isPhone("PPHONE1"))return false;
    if (!isPhone("PPHONE3"))return false;

    if (!isEmptyItem("CHANNEL"))return false;
    if (!isEmptyItem("COMMNAME"))return false;
    if (!isEmptyItem("NUM"))return false;
    else if (!chkintWithAlStr("NUM", "�⹺��Ʒ����������������"))return false;
    if (!isEmptyItem("APPTYPE"))return false;

    if (!isEmptyItem("AMT"))return false;
    else if (!chkdecWithAlStr("AMT", "��Ʒ�ܽ����¼�����֣�"))return false;
    if (!chkdecWithAlStr("RECEIVEAMT", "�Ѹ��ۿ���¼�����֣�"))return false;
    //        if (!isEmptyItem("APPAMT"))return false;
    //        else if (!chkdecWithAlStr("APPAMT","���������¼�����֣�"))return false;

    if (!isEmptyItem("DIVID"))return false;
    if (!isEmptyItem("ACTOPENINGBANK"))return false;
    if (bank != "802")
    if (!isEmptyItem("BANKACTNO"))return false;
    if (bank == "901")
        if (!isEmptyItem("ACTOPENINGBANK_UD"))return false;
    if (!isEmptyItem("CREDITTYPE"))return false;
    if (!isEmptyItem("MONPAYAMT"))return false;
    else if (!chkdecWithAlStr("MONPAYAMT", "�¾��������¼�����֣�"))return false;
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


function Regvalid() {//�ύ
    var bank;
    var e = document.getElementsByName("ACTOPENINGBANK");
    for (var i = 0; i < e.length; i++) {
        if (e[i].checked)bank = e[i].value;
    }
    if (goSave(bank)) {
        //������֧���´��ڿ���
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

function Regvalid1() {//����
    if (confirm("ȷʵҪ�������뵥��")) {
        document.all.APPACTFLAG.value = "3";
        document.forms[0].action = "/consume/application_save.jsp";
        document.forms[0].submit();
    }
}

function Regvalid2() {//�˻�
    if (confirm("ȷʵҪ�����뵥�˻���һ��������")) {
        document.all.APPACTFLAG.value = "2";
        document.forms[0].action = "/consume/application_save.jsp";
        document.forms[0].submit();
    }
}

function countAppAmt() {//�Զ�������ڽ��
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
    getObject('HT' + n).title = "�������";
    getObject('HT' + n).innerText = itext + " -- ��(�������)";
    getObject("HA" + n).style.display = "block";
}

function hidList(n, itext) {
    getObject('HT' + n).title = "���չ��";
    getObject('HT' + n).innerText = itext + " -- ��(���չ��)";
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

//20100108  zhanrui ���ӶԵ�����ϵ�˵ļ��
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


