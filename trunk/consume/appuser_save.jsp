<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="zt.cms.xf.XFConf" %>

<%--
===============================================
Title: �����Ŵ�-�������ѷ��ڸ���ͻ���Ϣ�ύ
Description: �����Ŵ�-�������ѷ��ڸ���ͻ���Ϣ�ύ��
 * @version  $Revision: 1.0 $  $Date: 2009/03/11 06:33:37 $
 * @author
 * <p/>�޸ģ�$Author: liuj $
===============================================
--%>

<%
    request.setCharacterEncoding("GBK");
//    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (um == null) {
//        response.sendRedirect("../fcworkbench/error.jsp");
//    }
//    System.out.println(request.getAttribute("APPNO"));

    String APPNO = request.getParameter("APPNO");   //���뵥���
    String IDTYPE = request.getParameter("IDTYPE"); //֤������
    String ID = request.getParameter("ID");          //֤������
    String APPTYPE = request.getParameter("APPTYPE");     //��������
    String APPSTATUS = request.getParameter("APPSTATUS"); //����״̬
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
    String LIVEFROM = request.getParameter("LIVEFROM");               //���ؾ�סʱ��
    String PC = request.getParameter("PC");                             //סլ�ʱ�
    String COMPC = request.getParameter("COMPC");                       //��λ�ʱ�
    String EMAIL = request.getParameter("EMAIL");                       //�����ʼ�
    String CLIENTNO = request.getParameter("CLIENTNO");               //�ͻ���
    String PASSWORD = request.getParameter("PASSWORD");               //�û�����


    APPNO = (APPNO == null) ? "" : APPNO.trim();                    //���뵥���
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
    MONTHLYPAY = (MONTHLYPAY == null) ? "" : MONTHLYPAY;            //����������
    BURDENSTATUS = (BURDENSTATUS == null) ? "" : BURDENSTATUS;      //����״�� enum=BurdenStatus
    EMPNO = (EMPNO == null) ? "" : EMPNO;                           //Ա��������
    LIVEFROM = (LIVEFROM == null) ? "" : LIVEFROM;                  //���ؾ�סʱ��
    PC = (PC == null) ? "" : PC;                                    //סլ�ʱ�
    COMPC = (COMPC == null) ? "" : COMPC;                           //��λ�ʱ�
    EMAIL = (EMAIL == null) ? "" : EMAIL;                           //�����ʼ�
    CLIENTNO = (CLIENTNO == null) ? "" : CLIENTNO;                  //�ͻ���
    PASSWORD = (PASSWORD == null) ? "" : PASSWORD;                  //�û�����


    if (IDTYPE.equals("") || ID.equals("") || PASSWORD.equals("")) {
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
<jsp:forward page="./appuser.jsp"/>
<%
} else {

    //���ڶ�ѡ���Ĭ�ϵ� splitechar"_" �ָ��鴮
    String splitechar = EnumValue.SPLIT_STR, SOCIALSECURITY = "";
    if (SOCIALSECURITY_G != null) {
        for (int i = 0; i < SOCIALSECURITY_G.length; i++) {
            SOCIALSECURITY += splitechar + SOCIALSECURITY_G[i];
        }
        SOCIALSECURITY = SOCIALSECURITY.replaceFirst(splitechar, "");
    }


    String[] sql = new String[2];
    String sql11;

    //����֤��ȷ������ǼǵĿͻ�����
    sql11 = " select CLIENTNO,NAME,APPDATE from CMINDVCLIENT where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and rownum=1  order by CLIENTNO desc";

    ConnectionManager manager = ConnectionManager.getInstance();
    CachedRowSet crs;
    try {
        if (!CLIENTNO.equals("")) {
            sql[0] = "update CMINDVCLIENT set LASTMODIFIED=SYSDATE,BIRTHDAY=to_date('" + BIRTHDAY + "','YYYY-MM-DD'),GENDER='" + GENDER + "',NATIONALITY='" + NATIONALITY + "',MARRIAGESTATUS='" + MARRIAGESTATUS + "',HUKOUADDRESS='" + HUKOUADDRESS + "',CURRENTADDRESS='" + CURRENTADDRESS + "',COMPANY='" + COMPANY + "',TITLE='" + TITLE + "',QUALIFICATION='" + QUALIFICATION + "',EDULEVEL='" + EDULEVEL + "',PHONE1='" + PHONE1 + "',PHONE2='" + PHONE2 + "',PHONE3='" + PHONE3 + "',NAME='" + NAME + "',IDTYPE='" + IDTYPE + "' ,ID='" + ID + "' ,CLIENTTYPE='" + CLIENTTYPE + "',DEGREETYPE='" + DEGREETYPE + "',COMADDR='" + COMADDR + "',SERVFROM='" + SERVFROM + "',RESIDENCEADR='" + RESIDENCEADR + "',HOUSINGSTS='" + HOUSINGSTS + "',HEALTHSTATUS='" + HEALTHSTATUS + "',MONTHLYPAY='" + MONTHLYPAY + "',BURDENSTATUS='" + BURDENSTATUS + "',EMPNO='" + EMPNO + "',SOCIALSECURITY='" + SOCIALSECURITY + "',LIVEFROM='" + LIVEFROM + "',PC='" + PC + "',COMPC='" + COMPC + "',EMAIL='" + EMAIL + "' " +
                    "where PASSWORD='" + PASSWORD + "' and CLIENTNO=" + CLIENTNO;
            sql[1] = "update XFCLIENT set LASTMODIFIED=SYSDATE,BIRTHDAY=to_date('" + BIRTHDAY + "','YYYY-MM-DD'),GENDER='" + GENDER + "',NATIONALITY='" + NATIONALITY + "',MARRIAGESTATUS='" + MARRIAGESTATUS + "',HUKOUADDRESS='" + HUKOUADDRESS + "',CURRENTADDRESS='" + CURRENTADDRESS + "',COMPANY='" + COMPANY + "',TITLE='" + TITLE + "',QUALIFICATION='" + QUALIFICATION + "',EDULEVEL='" + EDULEVEL + "',PHONE1='" + PHONE1 + "',PHONE2='" + PHONE2 + "',PHONE3='" + PHONE3 + "',NAME='" + NAME + "',IDTYPE='" + IDTYPE + "' ,ID='" + ID + "' ,CLIENTTYPE='" + CLIENTTYPE + "',DEGREETYPE='" + DEGREETYPE + "',COMADDR='" + COMADDR + "',SERVFROM='" + SERVFROM + "',RESIDENCEADR='" + RESIDENCEADR + "',HOUSINGSTS='" + HOUSINGSTS + "',HEALTHSTATUS='" + HEALTHSTATUS + "',MONTHLYPAY='" + MONTHLYPAY + "',BURDENSTATUS='" + BURDENSTATUS + "',EMPNO='" + EMPNO + "',SOCIALSECURITY='" + SOCIALSECURITY + "',LIVEFROM='" + LIVEFROM + "',PC='" + PC + "',COMPC='" + COMPC + "',EMAIL='" + EMAIL + "' " +
                    "where XFCLTP='1' and CLIENTNO=" + CLIENTNO +" and APPNO in (select APPNO from XFAPP where appno=XFCLIENT.appno and appstatus <"+ XFConf.APPSTATUS_CHUSHEN1+")";
        } else {
            crs = manager.getRs(sql11);
            if (crs.next()) {
                if (!crs.getString("NAME").equals(NAME)) {
                    String mess = "�ͻ���Ϣ���󣬴�֤���ѱ������ͻ�ʹ�ã�";
                    mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
                    session.setAttribute("msg", mess);
                    response.sendRedirect("../showinfo.jsp");
                } else {
                    String mess = "�ͻ���Ϣ�����û��Ѵ��ڣ����Ƿ����������룿";
                    mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
                    session.setAttribute("msg", mess);
                    response.sendRedirect("../showinfo.jsp");
                }
            } else
                sql[0] = "insert into CMINDVCLIENT (LASTMODIFIED,APPDATE,IDTYPE,ID,BIRTHDAY,GENDER,NATIONALITY,MARRIAGESTATUS,HUKOUADDRESS,CURRENTADDRESS,COMPANY,TITLE,QUALIFICATION,EDULEVEL,PHONE1,PHONE2,PHONE3,NAME,CLIENTTYPE,DEGREETYPE,COMADDR,SERVFROM,RESIDENCEADR,HOUSINGSTS,HEALTHSTATUS,MONTHLYPAY,BURDENSTATUS,EMPNO,SOCIALSECURITY,LIVEFROM,PC,COMPC,EMAIL,PASSWORD) " +
                        "values (SYSDATE,SYSDATE,'" + IDTYPE + "','" + ID + "',to_date('" + BIRTHDAY + "','YYYY-MM-DD'),'" + GENDER + "','" + NATIONALITY + "','" + MARRIAGESTATUS + "','" + HUKOUADDRESS + "','" + CURRENTADDRESS + "','" + COMPANY + "','" + TITLE + "','" + QUALIFICATION + "','" + EDULEVEL + "','" + PHONE1 + "','" + PHONE2 + "','" + PHONE3 + "','" + NAME + "','" + CLIENTTYPE + "','" + DEGREETYPE + "','" + COMADDR + "','" + SERVFROM + "','" + RESIDENCEADR + "','" + HOUSINGSTS + "','" + HEALTHSTATUS + "','" + MONTHLYPAY + "','" + BURDENSTATUS + "','" + EMPNO + "','" + SOCIALSECURITY + "','" + LIVEFROM + "','" + PC + "','" + COMPC + "','" + EMAIL + "','" + PASSWORD + "')";
        }
        temp = manager.execBatch(sql);
    } catch (Exception e) {
        e.printStackTrace();
    }

    if (temp) {

        if (CLIENTNO.equals("")) {
            session.setAttribute("msg", "���ĸ��˻�����Ϣ�Ѿ��ɹ��ύ�������¼����ѡ������Ʒ��Ϣ��");
            session.setAttribute("goUrl", "./consume/applist.jsp?NAME=" + NAME + "&IDTYPE=" + IDTYPE + "&ID=" + ID + "&PASSWORD=" + PASSWORD);
        } else
            session.setAttribute("msg", "�ύ�ɹ���");
        session.setAttribute("isback", "0");
        response.sendRedirect("../showinfo.jsp");
    } else {
        session.setAttribute("msg", "�ύ��Ϣʧ��!");
%>
<jsp:forward page="./appuser.jsp"/>
<%
        }
    }
%>