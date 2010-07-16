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

    String IDTYPE = request.getParameter("IDTYPE");            //֤������
    String ID = request.getParameter("ID");                     //֤������
    String PASSWORD = request.getParameter("PASSWORD");        //����

    String NAME = "";       //����
    String APPNO = "";      //���뵥��
    String APPDATE = "";    //��������
    String APPTYPE = "";    //��������
    String COMMNAME = "";   //��Ʒ����
    String APPSTATUS = "";  //����״̬

    IDTYPE = (IDTYPE == null) ? "" : IDTYPE;
    ID = (ID == null) ? "" : ID;
    NAME = (NAME == null) ? "" : NAME.trim();
    PASSWORD = (PASSWORD == null) ? "" : PASSWORD.trim();

    ConnectionManager manager = ConnectionManager.getInstance();
    String sql = "SELECT * from cmindvclient WHERE IDTYPE='" + IDTYPE + "' AND ID='" + ID + "' AND PASSWORD='" + PASSWORD + "'";
    if (manager.getRs(sql).size() == 0) {
        //����֤��ȷ������ǼǵĿͻ�����
        sql = " select CLIENTNO,NAME,APPDATE from CMINDVCLIENT where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and rownum=1  order by CLIENTNO desc";
        String err = "";
        if (manager.getRs(sql).size() > 0) err = "���������������";
        else err = "����û��ע�ᣬ����ע�ᣡ";
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

    String SID = (String) session.getAttribute("SID");//�����̳Ǳ��
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
    <title>���ѷ��ں�Լ</title>
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
            font-family: "����", Courier New, Arial;
            font-size: 12px;
            color: #000000;
            background-color: #EDEBED;
            border: 1px solid #AAAAAA;
        }

        .style4 {
            font-family: �� ��, Arial, Helvetica, sans-serif;
            color: #666666;
        }

        .style5 {
            border: thin groove #F7F5EE;
            font-family: �� ��, Arial, Helvetica, sans-serif;
            font-size: 12px;
            color: #FFFFFF;
            background-color: #3E76B3;
            cursor: pointer;
        }

        .style6 {
            font-family: �� ��, Arial, Helvetica, sans-serif;
            font-weight: bold;
            color: #808080;
        }

        .style7 {
            font-family: �� ��, Arial, Helvetica, sans-serif;
            font-size: medium;
            color: #337799;
        }

        .style8 {
            font-family: �� ��, Arial, Helvetica, sans-serif;
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
                            style="font-size: 12.0pt; line-height: 125%; font-family: ����">���ѷ��ڸ����Լ����</span></b> <span
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
                                                <strong>�ͻ���Ϣ</strong><font size="2">�� <%=NAME%>
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
                                                                    style="line-height: 125%; " class="style8">�������Ų����������ι�˾���ѷ��ڸ����Լ</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal" align="center"><span lang="EN-US"
                                                                                                      style="font-size: 12.0pt; line-height: 125%; "
                                                                                                      class="style6"><o:p>
                                                                &nbsp;</o:p></span></p>

                                                            <p class="MsoNormal"><b><span style="line-height: 125%; "
                                                                                          class="style4"><span
                                                                    class="style9">������Ʒ���ѷ��ڸ�����³ơ����ڸ���������ˣ����³ơ��׷�����</span></span></b><span
                                                                    class="style9"></p>

                                                            <p class="MsoNormal"><b><span style="line-height: 125%; "
                                                                                          class="style4">�ͷ��ڸ���������ʹ�õ���������뺣�����Ų����������ι�˾�����³ơ��ҷ�����</span></b>
                                                            </p>

                                                            <p class="MsoNormal"></span><b><span
                                                                    style="line-height: 125%; " class="style4"><span
                                                                    class="style9">ǩ�����º�Լ��</span></span><o:p></o:p></b>
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
                                                                    class="style7">�� ��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">��һ�� </span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�׷���֤���ҷ��ṩ����������������ʵ��������׼ȷ�ͺϷ���ͬ���ҷ����йػ�������λ�͸����˽�׷��ĲƲ�������״����������������ϡ�</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">���Ŵ�����ͶԷ�����������Ҫ���ҷ����Ѽ׷�<span
                                                                    lang="EN-US">/</span>�׷��ĵ�����<span
                                                                    lang="EN-US">/</span>�׷��Ĺ�ͬ�����˵Ĳ��ֻ�ȫ�����ϣ��ṩ���й��������и���������Ϣ�������ݿ⣬��������δ���й�����������׼��������ཨ���ĸ���������Ϣ�������ݿ��ṩ����������Ϣ��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�ҷ���������ҵ��Χ�����й��������и���������Ϣ�������ݿ��ѯ�׷�<span
                                                                    lang="EN-US">/</span>�׷��ĵ�����<span
                                                                    lang="EN-US">/</span>�׷��Ĺ�ͬ�����˵ĸ���������Ϣ����˸������ѷ��ڸ�������ģ���˸�����Ϊ���ѷ��������˵ĵ����˵ģ��Ը������ѷ��������˷��ս�����������ġ��ҷ����ṩ�Ͳ�ѯ����������Ϣ���Ϲ����У���ȷ��������Ϣ���ϵİ�ȫ�ͱ��ܡ�</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�ҷ����ڼ׷��ĸ�����Ϣ���Ϻ���ҵ���ܣ����������ȣ����������Ա��ܣ������շ��ɡ�����涨����˾��<span
                                                                    lang="EN-US">/</span>�������ء����Ż�����Ҫ������в�ѯ�������ʵ���¶�����γ��⣩��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">�ڶ���</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> �ҷ���Ȩ���ݼ׷�������״�������Ƿ���׼�׷��ķ������롣�����Ƿ��׼�÷������룬�й����Ͼ����˻���</span><o:p></o:p>
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
                                                                    class="style7">���ڿ����</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">������</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> �ס���˫����Է���������֤���ڱ���Լǩ��ʱ������ȫ��Ȩ��������ǩ������Լ�� </span> <span
                                                                    lang="EN-US"><o:p></o:p>
																																																</span>
                                                            </p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">������</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> ���÷��ڸ���������Ʒ���޼׷���������ʹ�ã��������ھ�Ӫ���۵���ҵ��;��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">������</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> ���ڿ���ר�����ڼ׷����ڸ����������������Ĺ�˾������������Ʒ��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">������</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> �׷��ڴ�ί���ҷ����ڰ���ȫ������֮�����<span
                                                                    lang="EN-US">3</span>��Ӫҵ���ڽ���������ȫ���Լ׷�������������Ʒ���廮����Ʒ���۵�λָ�����˻���</span><o:p></o:p>
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
                                                                    class="style7">���ڻ���˵��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">������</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> ����Լ���µķ��ڸ���������ѣ���ȡ���µȶ�ʽ���׷�Ӧ�ڷſ���Ĵ����𣬰�ʱ������Ӧ���ֱ�����з��ڸ����ͷ����峥Ϊֹ����ͬ���ʽ���£�</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (1)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�׷�ѡ���ԡ�ί��ָ�����д��ۡ���ʽ����ģ��׷���Ȩ�ҷ��ڷſ���Ĵ����𣬴Ӽ׷���ָ�����п����Ļ����˻����գ�ֱ�����з��ڸ���𡢷����峥Ϊֹ��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (2) </span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�׷�ѡ���ԡ����<span lang="EN-US">/</span>���������ʽ����ģ��ɼ׷���ÿ��Լ��������ǰ���ҷ�ָ�����˺Ż�����Ӧ���ֱ�����з��ڸ���𡢷����峥Ϊֹ��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (3)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�׷�ѡ���ԡ�ͨ��������֧��ƽ̨�����ʽ����ģ��ɼ׷���ÿ��Լ��������ǰͨ��������֧��ƽ̨���ҷ�ָ���˺Ż��ֱ�����з��ڸ���𡢷����峥Ϊֹ��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"><strong>�ڰ���</strong> �ҷ�����ɷ��ڸ����������Ե����ʼ����ź������š��绰�ȷ�ʽ֪ͨ�׷�����������ˡ�</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">�ھ���</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> �׷�������ǰ���</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (1)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�׷���ǰ�黹δ����Ӧ������ģ�Ӧ������ǰ��������������֪ͨ�ҷ���������֪ͨ�ʹ��ҷ�����Ϊ���ɳ�����</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (2)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�׷����ҷ�ͬ���һ������ǰ�黹ȫ����Ƿ���ҷ���δ���������<span
                                                                    lang="EN-US">1%</span>���˻�����ѣ����˻�����ⰴԭ��Լ�涨����ȡ�������ѡ�</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">��ʮ��</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> �׷����̻��䷢���Ľ��׾���Ӧ�ɾ���˫�����н�����׷������Ծ���Ϊ�ɾܾ����ҷ�������ʹ�÷��ڸ��ʽ�����ծ����ط��á�</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">��ʮһ��</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> ���������֮һ������ʱ���ҷ���ȨҪ��׷���ǰ�黹ȫ�����ڸ���������ѣ��׷���������������Ȩ��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"><span lang="EN-US">&nbsp;&nbsp;&nbsp; (1)</span>�׷�Υ������Լ֮�κ��������</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"><span lang="EN-US">&nbsp;&nbsp;&nbsp; (2)</span>�׷������������б���Լ����֮�������¹ʡ������Ⱥ͵����˷����������б���Լ����֮�ϲ������顢��ɢ���Ʋ���Ӱ��׷�����������ȫ������Ϊ��������������֮�����</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"><span lang="EN-US">&nbsp;&nbsp;&nbsp; (3)</span>�׷��򵣱����������ϡ���ܵ��ɹ���������˾�����������Ķ���Ʋ���û�ռ��䴦��Ȩ�����ƣ�����ڸ�����������Ŀ��ܵ���в��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (4)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�׷�����������Ʒ���۵�λ�����˻�ȫ����Ʒ֮�����</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">��ʮ����</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> ��Լ��֤</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"><span lang="EN-US">&nbsp;&nbsp;&nbsp; (1)</span>�ҷ��ͼ׷��ڱ���Լǩ�����ҷ���Ϊ��Ҫʱ�����ҷ�ָ���Ĺ�֤���ذ������ǿ��ִ��Ч���ķ��ڸ����Լ��֤����׷������л����������ۼ�������δ�ܰ�����������ģ��ҷ���Ȩ���й�ϽȨ������Ժ����ǿ��ִ�У��׷���Ը����ִ�У��ڴ�����²������ñ���Լ��ʮһ���涨��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp;&nbsp; (2)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">������Լ�Ĺ�֤�����ɼ׷�������</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">��ʮ����</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> �׷�����ϵ�˵ĵ�λ��ַ��סլ��ַ��绰����ȸ�����Ϣ���б����Ӧ������ϵ�׷��������ϱ������������׷���е��ɴ˵��µ�һ�з��պ���ʧ��</span><o:p></o:p>
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
                                                                    class="style7">���ʱ�׼</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">��ʮ����</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> ���ѷ��ڸ���ҵ���������ʱ�׼��μ��±�</span><span
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
                                                                                    class="style4">���ڸ����������£�</span><span
                                                                                    lang="EN-US"><o:p></o:p></span></p>
                                                                        </td>
                                                                        <td width="52" valign="bottom"
                                                                            style="border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; width: 39.3pt; border-left: none; padding: 0cm 5.4pt 0cm 5.4pt">
                                                                            <p class="MsoNormal" align="center"><span
                                                                                    lang="EN-US"
                                                                                    style="font-size: 9.0pt; line-height: 125%; font-family: ����">3<o:p></o:p></span>
                                                                            </p>
                                                                        </td>
                                                                        <td width="52" valign="bottom"
                                                                            style="border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; width: 39.3pt; border-left: none; padding: 0cm 5.4pt 0cm 5.4pt">
                                                                            <p class="MsoNormal" align="center"><span
                                                                                    lang="EN-US"
                                                                                    style="font-size: 9.0pt; line-height: 125%; font-family: ����">6<o:p></o:p></span>
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
                                                                                    class="style4">�·�����������</span><span
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
                                                                    class="style4">���������ѷ������ҷ�����Ϊ׼���ҷ���Ȩ�������ҵ��涨����ҵ������������Լʹ�õ��������ʡ�</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">��ʮ����</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> ΥԼ����</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">����<span lang="EN-US">(1)</span>�׷�δ���ڳ������ڸ���������ѵģ��ҷ��������ڽ�����Ƿ�����������֮�����ΥԼ�𣬰����ڽ��<span
                                                                    lang="EN-US">5%</span>�������ɽ����������<span lang="EN-US">10</span>Ԫ��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">����<span lang="EN-US">(2)</span>�׷�����������δ�������ڸ���������ѣ����ҵ�����δ���׷����г���Ƿ������ģ��ҷ���Ȩ��ֹ����Լ������׷���������׷����</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">����<span lang="EN-US">(3)</span>�׷�������ڸ���ʱ�ṩ�����ϲ�ʵ���ҷ���Ȩ��ǰ�ջط��ڸ����������ѣ�����Ȩ��׷��򵣱���׷���ɴ���ɵ���ʧ�ͷ�������ط��á�</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp; (4)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�ҷ�δ����ԼԼ����ʱ���ſӰ��׷�������Լ�涨ʹ�ÿ���ҷ�Ӧ��δ���ŵĽ���ΥԼ����������Լ��ʮ����<span
                                                                    lang="EN-US">(1)</span>��Լ����ΥԼ������׷�֧��ΥԼ��</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span lang="EN-US"
                                                                                       style="font-size: 9.0pt; line-height: 125%; "
                                                                                       class="style4">&nbsp;&nbsp; (5)</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4">�׷�δ�ܰ�ʱ����ҷ��ɲ�ȡ�ź����绰�����ŵ���ʽ���д��գ�����Ȩ�ۻ��׷����ҷ��������κ��˻��ڵĿ�����ü׷��ĵ֡���Ѻ�����峥��Ƿ�������صģ��ɸ�����ع涨����˾������׷���䷨�����Σ����ɼ׷��е���ص�һ�з��á�</span><o:p></o:p>
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
                                                                    style="line-height: 125%; " class="style7">����</span><span
                                                                    lang="EN-US"><o:p></o:p></span></b></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">��ʮ����</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> ����Լδ�����������ҷ����ҵ��涨����ҵ�����������ҷ��շ���Ŀ����׼���������ʷ����޸ġ��仯�������ȣ�һ�����ڼ�ܲ�����׼���ҷ����⹫����ʱ�Զ���Ч������Լ���������Ϊ��Ӧ�޸ģ��޸ĺ������Լס���˫������Լ����������ǰ�����ݣ��ҷ����⹫���ķ�ʽΪ�����˵����ź��ȡ�</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">��ʮ����</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> ����Լ�����ڼ��������飬˫����Э�̽����Э�̲��ɵģ�Ӧ���ҷ����ڵص�����Ժ�������ϡ�</span><span
                                                                    lang="EN-US"><o:p></o:p></span></p>

                                                            <p class="MsoNormal"><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style6">��ʮ����</span><span
                                                                    style="font-size: 9.0pt; line-height: 125%; "
                                                                    class="style4"> ����Լ���׷����������ǩ�ֺ����ҷ���׼����֮������Ч�����ҷ���ʽ��׼�׷������Ĵ�����ֹ��</span><o:p></o:p>
                                                            </p>

                                                            <p class="style10">
                                                                <o:p>&nbsp;</o:p></p>

                                                        </td>
                                                    </tr>
                                                        <%
                                                            int SEQNO = 0;
                                                            while (crs.next()) {
                                                                APPSTATUS = crs.getString("APPSTATUS");         //����״̬
                                                                APPNO = crs.getString("APPNO");                 //���뵥��
                                                                APPDATE = crs.getString("APPDATE");             //��������
                                                                APPTYPE = crs.getString("APPTYPE");             //��������
                                                                COMMNAME = crs.getString("COMMNAME");           //��Ʒ����
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
                                                                                   value="ͬ�⣨��ʼ��д������Ϣ��"
                                                                                   onclick="return req1();"></td>
                                            <td class="style4">&nbsp;</td>
                                            <%--<script language="javascript" type="text/javascript">--%>
                                            <%--createFlipPage(<%=pn%><caption class="style4">, <%=ps%>, <%=rows%>, "applist.jsp?pn=", "form1");--%>
                                            <%--</script>--%>
                                            <td class="style4">&nbsp;</td>
                                            <td class="list_form_button_td"><input type="submit" name="a" class="style5"
                                                                                   value=" ��ͬ�� "
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
                   style="height: 65px; border: none; font-weight: normal; font-size: 10px; color: #8F87E0; font-family: '΢���ź�';">
                <tr>
                    <td width="30%" class="style4">&nbsp;</td>
                    <td width="40%">
                        <table border="0" cellspacing="0" cellpadding="0" width="330" align="center"
                               style="border-style: none; border-color: inherit; border-width: medium; height: 20px; font-weight: normal; font-size: 12px; color: #004080; "
                               class="style4">
                            <tr>
                                <td width="30%">������ѯ�绰��<span style="color: #FF0066">0532-88939384��88939383</span>&nbsp;
                                    �ʱࣺ266101<br>
                                    ��ַ��ɽ��ʡ�ൺ�к���·1�ź�����ҵ԰K����������405��
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="30%" valign="bottom" align="right" class="style4">���ս���Ȩ�麣�����Ų����������ι�˾����</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</form>
<span class="style4">
    <%--���߿ͷ����� 365call-- �б�ʽ--%></span> <span class="style4">
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