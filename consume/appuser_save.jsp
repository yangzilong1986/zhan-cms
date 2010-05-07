<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="zt.cms.xf.XFConf" %>

<%--
===============================================
Title: 消费信贷-个人消费分期付款客户信息提交
Description: 消费信贷-个人消费分期付款客户信息提交。
 * @version  $Revision: 1.0 $  $Date: 2009/03/11 06:33:37 $
 * @author
 * <p/>修改：$Author: liuj $
===============================================
--%>

<%
    request.setCharacterEncoding("GBK");
//    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (um == null) {
//        response.sendRedirect("../fcworkbench/error.jsp");
//    }
//    System.out.println(request.getAttribute("APPNO"));

    String APPNO = request.getParameter("APPNO");   //申请单编号
    String IDTYPE = request.getParameter("IDTYPE"); //证件名称
    String ID = request.getParameter("ID");          //证件号码
    String APPTYPE = request.getParameter("APPTYPE");     //申请类型
    String APPSTATUS = request.getParameter("APPSTATUS"); //申请状态
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
    String LIVEFROM = request.getParameter("LIVEFROM");               //本地居住时间
    String PC = request.getParameter("PC");                             //住宅邮编
    String COMPC = request.getParameter("COMPC");                       //单位邮编
    String EMAIL = request.getParameter("EMAIL");                       //电子邮件
    String CLIENTNO = request.getParameter("CLIENTNO");               //客户号
    String PASSWORD = request.getParameter("PASSWORD");               //用户密码


    APPNO = (APPNO == null) ? "" : APPNO.trim();                    //申请单编号
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
    MONTHLYPAY = (MONTHLYPAY == null) ? "" : MONTHLYPAY;            //个人月收入
    BURDENSTATUS = (BURDENSTATUS == null) ? "" : BURDENSTATUS;      //负担状况 enum=BurdenStatus
    EMPNO = (EMPNO == null) ? "" : EMPNO;                           //员工卡号码
    LIVEFROM = (LIVEFROM == null) ? "" : LIVEFROM;                  //本地居住时间
    PC = (PC == null) ? "" : PC;                                    //住宅邮编
    COMPC = (COMPC == null) ? "" : COMPC;                           //单位邮编
    EMAIL = (EMAIL == null) ? "" : EMAIL;                           //电子邮件
    CLIENTNO = (CLIENTNO == null) ? "" : CLIENTNO;                  //客户号
    PASSWORD = (PASSWORD == null) ? "" : PASSWORD;                  //用户密码


    if (IDTYPE.equals("") || ID.equals("") || PASSWORD.equals("")) {
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
<jsp:forward page="./appuser.jsp"/>
<%
} else {

    //对于多选项，用默认的 splitechar"_" 分隔组串
    String splitechar = EnumValue.SPLIT_STR, SOCIALSECURITY = "";
    if (SOCIALSECURITY_G != null) {
        for (int i = 0; i < SOCIALSECURITY_G.length; i++) {
            SOCIALSECURITY += splitechar + SOCIALSECURITY_G[i];
        }
        SOCIALSECURITY = SOCIALSECURITY.replaceFirst(splitechar, "");
    }


    String[] sql = new String[2];
    String sql11;

    //根据证件确定最近登记的客户资料
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
                    String mess = "客户信息错误，此证件已被其他客户使用！";
                    mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
                    session.setAttribute("msg", mess);
                    response.sendRedirect("../showinfo.jsp");
                } else {
                    String mess = "客户信息错误，用户已存在，您是否忘记了密码？";
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
            session.setAttribute("msg", "您的个人基本信息已经成功提交，请继续录入您选购的商品信息。");
            session.setAttribute("goUrl", "./consume/applist.jsp?NAME=" + NAME + "&IDTYPE=" + IDTYPE + "&ID=" + ID + "&PASSWORD=" + PASSWORD);
        } else
            session.setAttribute("msg", "提交成功！");
        session.setAttribute("isback", "0");
        response.sendRedirect("../showinfo.jsp");
    } else {
        session.setAttribute("msg", "提交信息失败!");
%>
<jsp:forward page="./appuser.jsp"/>
<%
        }
    }
%>