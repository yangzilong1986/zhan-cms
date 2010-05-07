<%@ page contentType="application/msword; charset=GBK" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="java.math.BigDecimal" %>

<%
    request.setCharacterEncoding("GBK");
    System.out.println("JspClass.jsp_service_method");
    String APPNO = request.getParameter("APPNO");         //���뵥���
    //if (APPNO==null)APPNO="3702021981122211360001";

    if (APPNO == null) {
        session.setAttribute("msg", "û�з��ִ�����Ĳ�����");
        response.sendRedirect("../showinfo.jsp");
    } else {
        APPNO = (APPNO == null) ? "" : APPNO.trim();

        //response.setHeader("Content-disposition","attachment;filename=/���ڸ���������-"+APPNO+".doc");

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

        String IDTYPE = "";                    //֤������
        String ID = "";                        //֤������
        String CONFMONPAY = "";                //�˶�������
        String NAME = "";                      //�ͻ����� desc=��ҵ(����)����
        String APPTYPE = "";                   //��������
        String APPSTATUS = "";                 //����״̬
        String PASSWORD = "";                  //����
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

        String CHANNEL = "";                   //���۵�λ(��������)
        String COMMNAME = "";                  //��Ʒ����
        String COMMTYPE = "";                  //��Ʒ�ͺ�
        String ADDR = "";                      //���͵�ַ
        String NUM = "";                       //��������
        String AMT = "";                       //�ܽ��
        String RECEIVEAMT = "";                //�Ѹ����
        String APPAMT = "";                    //���ڽ��
        String DIVID = "";                     //��������
        String APPAMTMON = "";                 //ÿ�ڻ����
        String COMMISSIONRATE = "";            //��������
        String PROPORTION = "";                //���ڽ��ռ������Ʒ��ֵ����
        String MONTHPROP = "";                 //�����뱶��

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

        String APPDATE = "";                   //��������


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

            CHANNEL = crs.getString("CHANNEL");                  //���۵�λ(��������)
            COMMNAME = crs.getString("COMMNAME");                //��Ʒ����
            COMMTYPE = crs.getString("COMMTYPE");                //��Ʒ�ͺ�
            ADDR = crs.getString("ADDR");                         //���͵�ַ
            NUM = crs.getString("NUM");                           //��������
            AMT = crs.getString("AMT");                           //�ܽ��
            RECEIVEAMT = crs.getString("RECEIVEAMT");            //�Ѹ����
            APPAMT = crs.getString("APPAMT");                     //���ڽ��
            DIVID = crs.getString("DIVID");                       //��������
            APPAMTMON = crs.getString("APPAMTMON");              //ÿ�ڻ����
            COMMISSIONRATE = crs.getString("COMMISSIONRATE");   //��������
            PROPORTION = crs.getString("PROPORTION");            //���ڽ��ռ������Ʒ��ֵ����
            MONTHPROP = crs.getString("MONTHPROP");              //�����뱶��

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


            ID = crs.getString("ID");                              //֤������
            IDTYPE = crs.getString("IDTYPE");                     //֤������
            CONFMONPAY = crs.getString("CONFMONPAY");             //�˶�������
            APPDATE = crs.getString("APPDATE");                   //��������
            APPTYPE = crs.getString("APPTYPE");                   //��������
            APPSTATUS = crs.getString("APPSTATUS").trim();        //����״̬

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
    <title>�������Ų����������ι�˾���������Ŵ�������</title>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <style>
        <!--
        /* Font Definitions */
        @font-face {
            font-family: "�� ��";
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
            font-family: "\@����";
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
            mso-style-link: "ҳü Char";
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
            mso-style-link: "ҳ�� Char";
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
            mso-style-name: "ҳü Char";
            mso-style-link: "ҳ ü";
        }

        span.Char0 {
            mso-style-name: "ҳ�� Char";
            mso-style-link: "ҳ ��";
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

    <%--<!--media=print �������˵�������ڴ�ӡʱ��Ч-->--%>
    <%--<!--ϣ����ӡʱ����ʾ����������class="Noprint"��ʽ-->--%>
    <%--<!--ϣ����Ϊ���÷�ҳ��λ������class="PageNext"��ʽ-->--%>
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
    <%--<!--��ַ��д�����ϸ����˫б��-->--%>
    <%--hkey_path = "\\Software\\Microsoft\\Internet Explorer\\PageSetup";--%>
    <%--//������ҳ��ӡ��ҳüҳ��Ϊ��--%>
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
            <%--//alert('��Ҫ��������Activex���ܽ��д�ӡ���á�');--%>
        <%--}--%>
    <%--}--%>
    <%--//������ҳ��ӡ��ҳüҳ��ΪĬ��ֵ--%>
    <%--function pagesetup_default() {--%>
        <%--try {--%>
            <%--var RegWsh = new ActiveXObject("WScript.Shell");--%>
            <%--hkey_key = "\\header";--%>
            <%--//RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&w&bҳ��,&p/&P");--%>
            <%--RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, hk_pageHead);--%>
            <%--hkey_key = "\\footer";--%>
            <%--//RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&u&b&d");--%>
            <%--RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, hk_pageFoot);--%>
        <%--} catch(e) {--%>
        <%--}--%>
    <%--}--%>
    <%--function printsetup() {--%>
        <%--wb.execwb(8, 1); // ��ӡҳ������--%>
    <%--}--%>
    <%--function printpreview() {--%>
        <%--wb.execwb(7, 1);// ��ӡҳ��Ԥ��--%>
    <%--}--%>
    <%--function printit() {--%>
        <%--if (confirm('ȷ����ӡ��')) {--%>
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
                       onclick="javascript:doPrint()" value=" �� ӡ ">
            </td>
            <td class='page_button_tbl_td'>
                <input type='button' class='page_button_active'
                       name='button'
                       value=' �� �� ' onClick="window.close()">
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
    <td width=755 colspan=21 valign=top style='width:566.2pt;border:none;padding:0cm 0cm 0cm 0cm' align="right"><span style="font-size:10.0pt;font-family:'����';color:black;">���뵥�ţ�<%=APPNO%></span></td></tr></table>
<table border=1 cellspacing=0 cellpadding=0 width=755
       style='width:566.2pt;margin-left:6.4pt;border-collapse:collapse;border:none'>
<tr>
    <td width=755 colspan=21 valign=top style='width:566.2pt;border:solid windowtext 1.0pt;
          border-bottom:solid windowtext 1.5pt;background:#003366;padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:3.0pt;margin-right:0cm;
 margin-bottom:3.0pt;margin-left:0cm;text-align:center'>
            <b>
              <span
                      style="font-size:12.0pt;font-family:'����';color:white">�������Ų����������ι�˾�������ѷ��ڸ���������
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
      			<span style="font-size:10.0pt;font-family:'����'">��<br>��<br>��</span>
            </b>
        </p>
    </td>
    <td width=57 height="21" valign=top style='width:42.95pt;border-top:none;border-left:     none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
      padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">������</span>
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
            <span style="font-size:10.0pt;font-family:'����'">���֤���룺</span>
        </p>
    </td>
    <td width=208 height="21" colspan=7 valign=top style='width:155.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US style='font-size:10.0pt'><%=ID == null ? "" : ID%></span>
    </p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">Ա�������룺</span></p></td>
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
            <span style="font-size:10.0pt;font-family:'����'">��ַ��</span>
        </p>
    </td>
    <td width=196 height="21" colspan=7 valign=top style='width:147.15pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=CURRENTADDRESS == null ? "" : CURRENTADDRESS%></span>
    </p></td>
    <td width=83 height="21" colspan=3 valign=top style='width:62.4pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:9.0pt;font-family:'����'">�������ڵأ�</span></p></td>
    <td width=124 height="21" colspan=4 valign=top style='width:93.35pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'����'"><%=level.getEnumItemName("ResidenceADR", RESIDENCEADR)%></span>
    </p></td>
    <td width=95 valign=top style='width:71.3pt;border-top:none;border-left:none;
border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">��ϵ�绰��</span></p></td>
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
            <span style="font-size:10.0pt;font-family:'����'">�Ա�</span></p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'����'"><%=level.getEnumItemName("Gender", GENDER)%></span></p></td>
    <td width=76 height="21" colspan=3 valign=top style='width:57.15pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">����״����</span></p></td>
    <td width=157 height="21" colspan=5 valign=top style='width:117.85pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'>
    <p><span style="font-size:10.0pt;font-family:'����'">
    <%=level.getEnumItemName("MarriageStatus", MARRIAGESTATUS)%>
</span></p>
</td>
    <td width=95 valign=top style='width:71.3pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">�������ڣ�</span><span
            lang=EN-US style='font-size:10.0pt'>&nbsp; </span></p></td>
    <td width=150 colspan=4 valign=top style='width:112.4pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p style='text-indent:25.0pt'><span lang=EN-US
                                                                             style='font-size:10.0pt'><%=BIRTHDAY.equals("") ? "&nbsp;" : BIRTHDAY.substring(0, 4)%> </span><span
            style="font-size:10.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                    style='font-size:10.0pt'><%=BIRTHDAY.equals("") ? "&nbsp;" : BIRTHDAY.substring(5, 7)%> </span><span
            style="font-size:10.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                    style='font-size:10.0pt'><%=BIRTHDAY.equals("") ? "&nbsp;" : BIRTHDAY.substring(8, 10)%> </span><span
            style="font-size:10.0pt;font-family:'����'">��</span></p></td>
</tr>
<tr>
    <td width=49 rowspan=2 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
      <span
              style="font-size:10.0pt;font-family:'����'">��������</span>
            </b>
        </p>
    </td>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">������λ��</span></p></td>
    <td width=354 height="21" colspan=12 valign=top style='width:265.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US style='font-size:10.0pt'><%=COMPANY == null ? "" : COMPANY%></span>
    </p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">�ֵ�λ����ʱ�䣺</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=SERVFROM == null ? "" : SERVFROM%>��</span>
    </p></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">��λ��ַ��</span></p>
    </td>
    <td width=272 height="21" colspan=10 valign=top style='width:204.05pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US style='font-size:10.0pt;'><%=COMADDR == null ? "" : COMADDR%></span>
    </p></td>
    <td width=178 height="21" colspan=4 valign=top style='width:133.15pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'����'">��λ�绰��<%=PHONE3 == null ? "" : PHONE3%></span></p></td>
    <td width=149 colspan=3 valign=top style='width:111.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">ÿ�����룺�� <%=CONFMONPAY == null ? "" : CONFMONPAY%></span><span
            style="font-size:10.0pt;font-family:'����'">Ԫ</span></p></td>
</tr>
<tr>
    <td width=49 rowspan=4 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
      <span
              style="font-size:10.0pt;font-family:'����'">���ڸ������
      </span>
            </b>
        </p>
    </td>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p><span style="font-size:9.0pt;font-family:'����'">��Ʒ���ƣ�</span></p>
    </td>
    <td width=46 height="21" colspan=17 valign=top style='width:138.4pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><span style="font-size:10.0pt"><%=COMMNAME == null ? "" : COMMNAME%></span></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">������ڽ�</span></p></td>
    <td width=235 height="21" colspan=9 valign=top style='width:176.0pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">��</span><span
            style='font-size:10.0pt'> <%=APPAMT == null ? "" : APPAMT%></span><span
            style="font-size:10.0pt;font-family:'����'">Ԫ</span></p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">������</span></p></td>
    <td width=245 colspan=5 valign=top style='width:183.7pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'����'"><%=level.getEnumItemName("Divid", DIVID)%></span></p></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">���������ʣ�</span>
        </p>
    </td>
    <td width=46 height="21" colspan=5 valign=top style='width:34.65pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US
                                                  style='font-size:10.0pt'><%=COMMISSIONRATE == null ? "" : String.valueOf(new BigDecimal(COMMISSIONRATE).movePointRight(3))%>��</span>
    </p></td>
    <td width=87 height="21" colspan=4 valign=top style='width:65.3pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'����'">��Ʒ�ܼۿ</span></p></td>
    <td width=78 height="21" colspan=3 valign=top style='width:58.25pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">�� <%=AMT == null ? "" : AMT%></span><span
            style="font-size:10.0pt;font-family:'����'">Ԫ</span></p></td>
    <td width=175 colspan=4 valign=top style='width:134.4pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:9.0pt;font-family:'����'">���ڽ��ռ������Ʒ��ֵ������</span></p>
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
            <span style="font-size:9.0pt;font-family:'����'">������Ʒʹ�õ�ַ��</span>
        </p>
    </td>
    <td width=235 height="21" colspan=9 valign=top style='width:176.0pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt"><%=ADDR == null ? "" : ADDR%></span></p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">ÿ�ڻ���</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">�� <%=APPAMTMON == null ? "" : APPAMTMON%></span><span
            style="font-size:10.0pt;font-family:'����'">Ԫ</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'����'">�����뱶����<%=MONTHPROP == null ? "" : MONTHPROP%></span></p></td>
</tr>
<tr>
    <td width=49 rowspan=11
        style='width:36.65pt;border-top:none;border-left: solid windowtext 1.5pt;border-bottom:solid windowtext 1.0pt;border-right: solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
                <span style="font-size:10.0pt;font-family:'����'">��<br>��<br>ϵ<br>ͳ<br>��<br>¼</span>
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
            style="font-size:10.0pt;font-family:'����'">��</span><%}%><span
            style="font-size:9.0pt;font-family:'����'">�޼�¼</span><span lang=EN-US
                                                                     style='font-size:8.0pt'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><%if (CCVALIDPERIOD.equals("1")) {%><span
            lang=EN-US style='font-size:11.0pt;font-family:"Wingdings 2"'>R</span><%} else {%><span
            style="font-size:10.0pt;font-family:'����'">��</span><%}%><span
            style="font-size:9.0pt;font-family:'����'">����6���¼�¼</span><span lang=EN-US
                                                                      style='font-size:8.0pt'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><%if (CCVALIDPERIOD.equals("2")) {%><span
            lang=EN-US style='font-size:11.0pt;font-family:"Wingdings 2"'>R</span><%} else {%><span
            style="font-size:10.0pt;font-family:'����'">��</span><%}%><span
            lang=EN-US style='font-size:9.0pt'>6�������ϼ�¼</span></p>
    </td>
    <td width=245 colspan=5 valign=top style='width:183.7pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><b><span
            style="font-size:10.0pt;font-family:'����'">��ȥ</span></b><b><span lang=EN-US
                                                                            style='font-size:10.0pt'>12</span></b><b><span
            style="font-size:10.0pt;font-family:'����'">�����ڲ�ѯ</span></b><b><span lang=EN-US
                                                                               style='font-size:10.0pt'>(</span></b><b><span
            style="font-size:10.0pt;font-family:'����'">����</span></b><b><span lang=EN-US
                                                                            style='font-size:10.0pt'>)</span></b><b><span
            style="font-size:10.0pt;font-family:'����'">��</span></b></p></td>
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
            style="font-size:10.0pt;font-family:'����'">��Ѻ����</span></b></p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
background:#99CCFF;padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><b><span
            style="font-size:10.0pt;font-family:'����'">���ô���</span></b></p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
background:#99CCFF;padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><b><span
            style="font-size:10.0pt;font-family:'����'">���ÿ�</span></b></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'����'">��������</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'����'">���ÿ�����</span></p></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:9.0pt;font-family:'����'">����(��)����</span>
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
                                                                                         style='font-size:10.0pt'><%=CCLOANYEARQUERYTIME == null ? "" : CCLOANYEARQUERYTIME%>��</span>
    </p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%=CCCARDYEARQUERYTIME == null ? "" : CCCARDYEARQUERYTIME%>��</span>
    </p></td>
</tr>
<tr>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:9.0pt;font-family:'����'">�ܴ������</span>
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
            style="font-size:8.0pt;font-family:'����';color:red">��䭳���</span><span lang=EN-US
                                                                              style='font-size:8.0pt;color:red'>5</span><span
            style="font-size:8.0pt;font-family:'����';color:red">�Σ�������ж�</span></p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:9.0pt;font-family:'����'">�ܴ��������</span>
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
            style="font-size:9.0pt;font-family:'����'">���д����</span></p></td>
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
            <span style="font-size:9.0pt;font-family:'����'">���»����</span>
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
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span style="font-size:9.0pt;font-family:'����'">�ñʷ��ڻ���</span></p></td>
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
            <span lang=EN-US style='font-size:7.0pt'>12���»����¼(����)</span>
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
            style="font-size:9.0pt;font-family:'����'">�ܻ���</span></p></td>
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
    <span style="font-size:7.0pt;font-family:'����'">������</span>
        </p>
    </td>
    <td width=120 height="21" colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%if (CCDYNOOVERDUE.equals("0")) {%>��<%} else {%>��<%}%></span>
    </p></td>
    <td width=114 height="21" colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%if (CCDYNOOVERDUE.equals("0")) {%>��<%} else {%>��<%}%></span>
    </p></td>
    <td width=119 height="21" colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=center style='text-align:center'><span lang=EN-US
                                                                                         style='font-size:10.0pt'><%if (CCDYNOOVERDUE.equals("0")) {%>��<%} else {%>��<%}%></span>
    </p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span style="font-size:9.0pt;font-family:'����'">ÿ�»���������ȣ�</span></p></td>
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
    <span style="font-size:7.0pt;font-family:'����'">����1-30��(1)</span>
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
            style="font-size:8.0pt;font-family:'����';color:red">���ô�����ÿ�����</span><span
            lang=EN-US style='font-size:8.0pt;color:red'>5</span><span style="font-size:8.0pt;font-family:'����';color:red">�Σ�</span><span
            lang=EN-US style='font-size:8.0pt;color:red'>6</span><span
            style="font-size:8.0pt;font-family:'����';color:red">�����ϸ����ж�</span></p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=107 height="21" colspan=3 valign=top style='width:80.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p style='margin-left:10.3pt'>
    <span style="font-size:7.0pt;font-family:'����'">����31-60��(2)</span>
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
    <span style="font-size:7.0pt;font-family:'����'">����60������(��3)</span>
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
                <span style="font-size:10.0pt;font-family:'����'">��<br>��<br>Ҫ<br>��</span>
            </b>
        </p>
    </td>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">����Ҫ��</span><span
            lang=EN-US style='font-size:10.0pt'>18-60</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'"><%if (ACAGE.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;��<%} else {%>��<%}%></span></p></td>
    <td width=126 colspan=2 rowspan=10 valign=top style='width:94.45pt;
border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:9.0pt;font-family:'����'">�ṩ����������ɣ�</span><br>
<font style='font-size:10.0pt; word-break : break-all;'><%=ACACCEPTREASON == null ? "" : ACACCEPTREASON%></font></p>
    </td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">ÿ����͹���</span>
    <span
            class=definition>
      <span style="font-size:10.0pt;font-family:'����'">Ҫ</span>
    </span>
    <span
            style="font-size:10.0pt;font-family:'����'">�󣺲����������
    </span>
    <span lang=EN-US
          style='font-size:10.0pt'>2,000
    </span>
    <span style="font-size:10.0pt;font-family:'����'">Ԫ
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'"><%if (ACWAGE.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;��<%} else {%>��<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=70 height="21" colspan=2 rowspan=2 style='width:52.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p align=center style='text-align:center'>
    <span
            style="font-size:10.0pt;font-family:'����'">Ա�����
    </span>
        </p>
    </td>
    <td width=391 height="21" colspan=13 valign=top style='width:293.05pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">������ʽԱ��</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'"><%if (ACEMP.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;��<%} else {%>��<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=391 height="21" colspan=13 valign=top style='width:293.05pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">�����������������</span>
    <span
            lang=EN-US style='font-size:10.0pt'>1
    </span>
    <span style="font-size:10.0pt;font-family:'����'">������
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'"><%if (ACJOB.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;��<%} else {%>��<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">�Ͷ���ͬ��</span>
    <span
            class=definition>
      <span style="font-size:10.0pt;font-family:'����'">��</span>
    </span>
    <span
            style="font-size:10.0pt;font-family:'����'">Ҫ���Ͷ���ͬʣ�����޲����ڷ������޵�
    </span>
    <span
            lang=EN-US style='font-size:10.0pt'>50%
    </span>
    <span style="font-size:10.0pt;font-family:'����'">������
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'"><%if (ACCONTRACT.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;��<%} else {%>��<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">����ϵͳ�����һ����δ�����¼</span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'"><%if (ACZX1.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;��<%} else {%>��<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">����ϵͳ�ڹ�ȥ</span>
    <span
            lang=EN-US style='font-size:10.0pt'>12
    </span>
    <span style="font-size:10.0pt;font-family:'����'">�����������������
    </span>
            <span lang=EN-US style='font-size:10.0pt'>5</span>
    <span
            style="font-size:10.0pt;font-family:'����'">������
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'"><%if (ACZX2.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;��<%} else {%>��<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">����ϵͳ�ڹ�ȥ</span>
    <span
            lang=EN-US style='font-size:10.0pt'>12
    </span>
    <span style="font-size:10.0pt;font-family:'����'">���������������
    </span>
            <span lang=EN-US style='font-size:10.0pt'>60</span>
    <span
            style="font-size:10.0pt;font-family:'����'">������
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'"><%if (ACZX3.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;��<%} else {%>��<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">���÷����ܶ�������</span>
    <span
            lang=EN-US style='font-size:10.0pt'>30,000
    </span>
    <span style="font-size:10.0pt;font-family:'����'">��
    </span>
            <span lang=EN-US style='font-size:10.0pt'>12</span>
    <span
            style="font-size:10.0pt;font-family:'����'">��������
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'"><%if (ACFACILITY.equals("0")) {%>
      &nbsp;&nbsp;&nbsp;&nbsp;��<%} else {%>��<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=461 height="21" colspan=15 valign=top style='width:345.85pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">ÿ�»�����������Ƿ����</span>
    <span
            lang=EN-US style='font-size:10.0pt'>70%
    </span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'"><%if (ACRATE.equals("0")) {%>��<%} else {%>��<%}%></span>
    </p></td>
</tr>
<tr style='page-break-inside:avoid'>
    <td width=49 rowspan=3 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
                <span style="font-size:10.0pt;font-family:'����'">ҵ<br>��<br>��<br>��</span>
            </b>
        </p>
    </td>
    <td width=228 colspan=7 valign=top style='width:170.85pt;border:none;
border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:9.0pt;font-family:'����'">�ͻ����������</span><span
            lang=EN-US style='font-size:10.0pt'>&nbsp;&nbsp;&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��ͬ��</span><span style='font-size:9.0pt'> </span><span
            style="font-size:9.0pt;font-family:'����'">����ͬ��</span></p></td>
    <td width=233 colspan=8 valign=top style='width:175.0pt;border:none;
border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:9.0pt;font-family:'����'">�Ŵ����������</span><span
            lang=EN-US style='font-size:9.0pt'>&nbsp;&nbsp;&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��ͬ��</span><span style='font-size:9.0pt'> </span><span
            style="font-size:9.0pt;font-family:'����'">����ͬ��</span></p></td>
    <td width=245 colspan=5 valign=top style='width:183.7pt;border:none;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:9.0pt;font-family:'����'">�Ŵ��ܼ������</span><span
            lang=EN-US style='font-size:9.0pt'>&nbsp;&nbsp;&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��ͬ��</span><span style='font-size:9.0pt'> </span><span
            style="font-size:9.0pt;font-family:'����'">����ͬ��</span></p></td>
</tr>
<tr style='height:32.35pt'>
    <td width=228 colspan=7 valign=top style='width:170.85pt;border:none;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:32.35pt'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">�����</span>
        </p>
    </td>
    <td width=233 colspan=8 valign=top style='width:175.0pt;border:none;
border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:32.35pt'><p><span
            style="font-size:10.0pt;font-family:'����'">�����</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
padding:0cm 0cm 0cm 0cm;height:32.35pt'><p><span
            style="font-size:10.0pt;font-family:'����'">�����</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border:none;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;height:32.35pt'><p><span lang=EN-US
                                                                                                     style='font-size:10.0pt'>&nbsp;</span>
    </p></td>
</tr>
<tr>
    <td width=107 height="16" colspan=3 valign=top style='width:80.6pt;border:none;
  border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">ǩ�֣�</span>
        </p>
    </td>
    <td width=120 colspan=4 valign=top style='width:90.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span></p></td>
    <td width=114 colspan=5 valign=top style='width:85.75pt;border:none;
border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'����'">ǩ�֣�</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'����'">ǩ�֣�</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span></p></td>
</tr>
<tr style='height:23.25pt'>
    <td width=49 rowspan=3 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:23.25pt'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
                <span style="font-size:10.0pt;font-family:'����'">��<br>��<br>��<br>��</span>
            </b>
        </p>
    </td>
    <td width=107 colspan=3 style='width:80.6pt;border:none;padding:0cm 0cm 0cm 0cm;
height:23.25pt'><p><span style="font-size:10.0pt;font-family:'����'">�÷֣�</span></p></td>
    <td width=235 colspan=9 style='width:176.0pt;border:none;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm;height:23.25pt'><p align=right style='text-align:right'><span
            style="font-size:8.0pt;font-family:'����';color:red">��������</span><span lang=EN-US
                                                                                style='font-size:8.0pt;color:red'>60</span><span
            style="font-size:8.0pt;font-family:'����';color:red">�֣�</span></p></td>
    <td width=364 colspan=8 rowspan=3 valign=top style='width:272.95pt;
border-top:none;border-left:none;border-bottom:solid windowtext 1.5pt;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;height:23.25pt'><p><span
            style="font-size:9.0pt;font-family:'����'">�������/�ܾ����ɣ�</span></p>
    </td>
</tr>
<tr style='height:23.25pt'>
    <td width=107 colspan=3 style='width:80.6pt;border:none;padding:0cm 0cm 0cm 0cm;
  height:23.25pt'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">�����</span>
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
            style="font-size:10.0pt;font-family:'����'">ǩ�֣�</span></p></td>
    <td width=114 colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span></p></td>
</tr>
<tr>
    <td width=49 rowspan=3 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm'>
        <p align=center style='margin-top:0cm;margin-right:5.65pt;
 margin-bottom:0cm;margin-left:5.65pt;margin-bottom:.0001pt;text-align:center'>
            <b>
                <span style="font-size:10.0pt;font-family:'����'">��<br>��</span>
            </b>
        </p>
    </td>
    <td width=342 colspan=12 rowspan=2 valign=top style='width:256.6pt;
border:none;border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'����'">���ܾ��������</span><span
            lang=EN-US style='font-size:10.0pt'>&nbsp;&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��ͬ��</span><span style='font-size:9.0pt'> </span><span
            style="font-size:9.0pt;font-family:'����'">����ͬ��</span></p>

        <p>
            <span style="font-size:10.0pt;font-family:'����'">�����</span>
        </p>
    </td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
padding:0cm 0cm 0cm 0cm'><p><span style="font-size:10.0pt;font-family:'����'">�ܾ��������</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:9.0pt;font-family:'����'">��ͬ��</span><span style='font-size:9.0pt'> </span><span
            style="font-size:9.0pt;font-family:'����'">����ͬ��</span><span
            lang=EN-US style='font-size:10.0pt'>&nbsp;&nbsp;&nbsp; </span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border:none;
border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span lang=EN-US>&nbsp;</span></p></td>
</tr>
<tr style='height:41.05pt'>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
  padding:0cm 0cm 0cm 0cm;height:41.05pt'>
        <p>
            <span style="font-size:10.0pt;font-family:'����'">�����</span>
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
            style="font-size:10.0pt;font-family:'����'">ǩ�֣�</span></p></td>
    <td width=114 colspan=5 valign=top style='width:85.75pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span lang=EN-US
                                                                                       style='font-size:10.0pt'>&nbsp;</span><span
            lang=EN-US style='font-size:9.0pt'>&nbsp;</span><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'����'"> </span></p></td>
    <td width=119 colspan=3 valign=top style='width:89.25pt;border:none;
border-bottom:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm'><p><span
            style="font-size:10.0pt;font-family:'����'">ǩ�֣�</span></p></td>
    <td width=126 colspan=2 valign=top style='width:94.45pt;border-top:none;
border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.5pt;
padding:0cm 0cm 0cm 0cm'><p align=right style='text-align:right'><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span><span lang=EN-US
                                                                   style='font-size:9.0pt'>&nbsp;&nbsp; </span><span
            style="font-size:9.0pt;font-family:'����'">��</span></p></td>
</tr>
<tr style='height:26.75pt'>
    <td width=49 style='width:36.65pt;border-top:none;border-left:solid windowtext 1.5pt;
  border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:26.75pt'>
        <p align=center style='text-align:center'>
            <b>
      <span
              style="font-size:10.0pt;font-family:'����'">֤��
      </span>
            </b>
            <b>
      <span lang=EN-US
            style='font-size:10.0pt'>
        <br>
      </span>
            </b>
            <b>
                <span style="font-size:10.0pt;font-family:'����'">�ļ�</span>
            </b>
        </p>
    </td>
    <td width=107 colspan=3 style='width:80.6pt;border-top:none;border-left:none;
border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm;height:26.75pt'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'����'">���֤��ӡ��</span></p></td>
    <td width=120 colspan=4 style='width:90.25pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm;height:26.75pt'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'����'">������ӡ��</span></p></td>
    <td width=114 colspan=5 style='width:85.75pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm;height:26.75pt'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'����'">�����ṩԱ����Ϣ</span></p></td>
    <td width=119 colspan=3 style='width:89.25pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.5pt;border-right:solid windowtext 1.0pt;
padding:0cm 0cm 0cm 0cm;height:26.75pt'><p align=center style='text-align:center'><span
            style="font-size:10.0pt;font-family:'����'">���ò�ѯ����</span></p></td>
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