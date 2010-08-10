<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DecimalFormat" %>
<%--
===============================================
Title: �����Ŵ�-�ͻ�ע��
Description: �������ѷ��ڿͻ�ע�ᡣ
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

    String PASSWORD = request.getParameter("PASSWORD");   //�ͻ�����
    String IDTYPE = request.getParameter("IDTYPE");       //֤������
    String ID = request.getParameter("ID");                //֤������
    String NAME = request.getParameter("NAME");            //�ͻ����� desc=��ҵ(����)����

    String strGoUrl = request.getParameter("goUrl");      //�ر�ҳ���־������ҳ��رգ��ǵ���������ҳ��
    String closeClick = "pageWinClose();";
    if (strGoUrl != null) closeClick = "gopage('" + strGoUrl + "')";


    String mess = "";
    if (PASSWORD == null && (ID == null || IDTYPE == null)) {
        session.setAttribute("msg", "û�з��ִ�����Ĳ�����");
        response.sendRedirect("../showinfo.jsp");
    } else {
        PASSWORD = (PASSWORD == null) ? "" : PASSWORD;
        ID = (ID == null) ? "" : ID.trim();
        IDTYPE = (IDTYPE == null) ? "" : IDTYPE.trim();
        NAME = (NAME == null) ? "" : NAME.trim();


        ConnectionManager manager = ConnectionManager.getInstance();
        String sql1 = "";
        if (!ID.equals("") && !IDTYPE.equals("")) {
            sql1 = "select CLIENTNO,BIRTHDAY,GENDER,NATIONALITY,MARRIAGESTATUS,HUKOUADDRESS,CURRENTADDRESS," +
                    "COMPANY,TITLE,QUALIFICATION,EDULEVEL,PHONE1,PHONE2," +
                    "PHONE3,NAME,CLIENTTYPE,DEGREETYPE,COMADDR,SERVFROM,RESIDENCEADR,HOUSINGSTS," +
                    "HEALTHSTATUS,MONTHLYPAY,BURDENSTATUS,EMPNO,SOCIALSECURITY,LIVEFROM,PC,COMPC,EMAIL " +
                    "from CMINDVCLIENT c " +
                    "where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and PASSWORD='" + PASSWORD + "' " +
                    " and rownum=1  order by CLIENTNO desc";
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
        String EMAIL = (String)session.getAttribute("EMAIL");  //�����ʼ�


        boolean ifErrClient = false;
        CachedRowSet crs = null;
        if (!sql1.equals("")) crs = manager.getRs(sql1);
        if (crs != null && crs.next()) {
            if (NAME.compareTo(crs.getString("NAME")) != 0) {
                ifErrClient = true;  //�ͻ����� desc=��ҵ(����)����)
                mess = "�ͻ���Ϣ���󣬴�֤���ѱ������ͻ�ʹ�ã�";
            }
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
            LIVEFROM = crs.getString("LIVEFROM");                //���ؾ�סʱ��
            PC = crs.getString("PC");                             //סլ�ʱ�
            COMPC = crs.getString("COMPC");                      //��λ�ʱ�
            EMAIL = crs.getString("EMAIL");                      //�����ʼ�

//            ID = crs.getString("ID");                              //֤������
//            IDTYPE = crs.getString("IDTYPE");                     //֤������
//            PASSWORD = crs.getString("PASSWORD");                 //�û�����
        } else {
            //����֤��ȷ������ǼǵĿͻ�����
            sql1 = " select CLIENTNO,NAME,APPDATE from CMINDVCLIENT where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and rownum=1  order by CLIENTNO desc";
            crs = manager.getRs(sql1);
            if (crs.next()) {
                ifErrClient = true;
                if (!crs.getString("NAME").equals(NAME)) {
                    mess = "�ͻ���Ϣ���󣬴�֤���ѱ������ͻ�ʹ�ã�";
                } else {
                    mess = "�ͻ���Ϣ�����û��Ѵ��ڣ����Ƿ����������룿";
                }
            }
        }

        BIRTHDAY = (BIRTHDAY == null) ? "" : BIRTHDAY;
        EMAIL = (EMAIL == null) ? "" : EMAIL;


        String readonly = "";
        String readonly_input = "readonly";
//        String submit = "class='page_button_active'";
        String submit = "class='btn_2k3'";
        String title = "�������ѷ��ڸ���ͻ���Ϣ";


        if (ifErrClient) {
            mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
            session.setAttribute("msg", mess);
            response.sendRedirect("../showinfo.jsp");
        } else {
%>
<html>
<head>
    <title>�����Ŵ�</title>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
            margin-left:0px;
            margin-right:0px;
        }

        -->
    </style>
</head>
<body onLoad="getbirthday('<%=ID%>','BIRTHDAY');">
<div style="width:100%">
<table height="42px" width="100%" border="0" align="left" cellpadding="2" cellspacing="2"
          >
        <tr align="left">
            <td width="30%" style="BACKGROUND: url(../images/top_logo.gif) no-repeat;"></td>

        </tr>
    </table>
</div>
<div class="navNo"></div>
<br>
<div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr class='page_form_tr'>
        <td align="center" valign="middle">
<table height="325" border="0" align="center" cellpadding="2" cellspacing="2" bordercolor="#816A82" bgcolor="#ffffff"
       width="832">
<tr align="left">
    <td height="30" bgcolor="#A4AEB5"><img src="../images/form/xing1.jpg" align="absmiddle"> <font size="2"
                                                                                                   color="#FFFFFF"><b>�������Ų����������ι�˾�������ѷ����û�ע��</b></font>
        <img src="../images/form/xing1.jpg" align="absmiddle"></td>
</tr>
<tr align="center">
<td height="260" valign="middle">
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
<tr class='page_form_tr'>
<td width="20">&nbsp;</td>
<td align="center" valign="middle">
<script src='../js/main.js' type='text/javascript'></script>
<script src='../js/check.js' type='text/javascript'></script>
<script src='../js/meizzDate.js' type='text/javascript'></script>
<script src='../js/checkID2.js' type='text/javascript'></script>
<script src='../js/pagebutton.js' type='text/javascript'></script>
<script src='../js/xf/xfutil.js' type='text/javascript'></script>
<form id='winform' method='post' action='./application_save.jsp'>
    <input type="hidden" name="CLIENTNO" value="<%=CLIENTNO%>">
    <input type="hidden" name="PASSWORD" value="<%=PASSWORD%>">
    <table class='page_form_regTable' id='page_form_table' width="100%">
        <col width="80"/>
        <col width="132"/>
        <col width="200"/>
        <col width="132"/>
        <col width="180"/>
        
        <tr class='page_form_tr'>
            <td class="page_button_tbl_tr" colspan="4" height="5"></td>
        </tr>
        <tr class='page_form_tr'> </tr>
        <tr class='page_form_tr'>
            <td rowspan="11" class="page_left_table_title">������Ϣ</td>
            <td class="page_form_title_td" nowrap>��&nbsp;&nbsp;����</td>
            <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="NAME"
                                                   value="<%=NAME==null?"":NAME%>"
                                                   class="page_form_text" maxlength="40"></td>
            <td class="page_form_title_td" nowrap>&nbsp;��&nbsp;&nbsp;��</td>
            <td class="page_form_td" nowrap><%=level.radioHere("GENDER", "Gender", GENDER)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>��&nbsp;&nbsp;����</td>
            <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="PHONE1"
                                                   value="<%=PHONE1==null?"":PHONE1%>"
                                                   class="page_form_text" maxlength="15"></td>
            <td class="page_form_title_td" nowrap>&nbsp;Ա�������룺</td>
            <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="EMPNO"
                                                   value="<%=EMPNO==null?"":EMPNO%>"
                                                   class="page_form_text" maxlength="8"></td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>���֤�����ƣ�</td>
            <td class="page_form_td" nowrap>
                <%--<select name="IDTYPE" class="page_form_select">--%>
                <%--<option value='0'>���֤</option>--%>
                <%--</select>--%>
                <%=level.levelHere("IDTYPE", "IDType", IDTYPE)%>
            </td>
            <td class="page_form_title_td" nowrap>&nbsp;֤�����룺</td>
            <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="ID"
                                                   value="<%=ID==null?"":ID%>"
                                                   class="page_form_text"
                                                   onblur="if(checkIDCard(document.getElementById('IDTYPE'),this))getbirthday(this.value,'BIRTHDAY');"
                                                   maxlength="18">
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>�������ڣ�</td>
            <td class="page_form_td" nowrap><input type="text" <%=readonly_input%> name="BIRTHDAY"
                                                   id="BIRTHDAY"
                                                   value="<%=BIRTHDAY.equals("")?"":DBUtil.to_Date(BIRTHDAY)%>"
                                                   class="page_form_text">
                <input type="button" value="��" class="page_form_refbutton"
                       onClick="setday(this,winform.BIRTHDAY)"></td>
            <td class="page_form_title_td" nowrap>&nbsp;����״����</td>
            <td class="page_form_td"
                nowrap><%=level.radioHere("HEALTHSTATUS", "HealthStatus", HEALTHSTATUS)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>����״����</td>
            <td class="page_form_td"
                nowrap><%=level.radioHere("MARRIAGESTATUS", "MarriageStatus", MARRIAGESTATUS)%>
            </td>
            <td class="page_form_title_td" nowrap>&nbsp;�Ƿ�����Ů��</td>
            <td class="page_form_td"
                nowrap><%=level.radioHere("BURDENSTATUS", "BurdenStatus", BURDENSTATUS)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>�������ڵأ�</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.radioHere("RESIDENCEADR", "ResidenceADR", RESIDENCEADR)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>��ͥסַ��</td>
            <td colspan="3" class="page_form_td">
                <input type="text" <%=readonly%> name="CURRENTADDRESS"
                       value="<%=CURRENTADDRESS==null?"":CURRENTADDRESS%>"
                       class="page_form_text" style="width:517px" maxlength="40">
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>סլ�ʱࣺ</td>
            <td class="page_form_td"><input type="text" <%=readonly%> name="PC"
                                            value="<%=PC==null?"":PC%>"
                                            class="page_form_text" maxlength="6"></td>
            <td class="page_form_title_td" nowrap>&nbsp;�������䣺</td>
            <td class="page_form_td">
                <input type="text" <%=readonly%> name="EMAIL"
                       value="<%=EMAIL==null?"":EMAIL%>"
                       class="page_form_text" maxlength="40"></td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>��ͥ�绰��</td>
            <td class="page_form_td"><input type="text" <%=readonly%> name="PHONE2"
                                            value="<%=PHONE2==null?"":PHONE2%>"
                                            class="page_form_text" maxlength="15"></td>
            <td class="page_form_title_td" nowrap>&nbsp;���ؾ�סʱ�䣺</td>
            <td class="page_form_td">
                <input type="text" <%=readonly%> name="LIVEFROM"
                       value="<%=LIVEFROM==null?"":LIVEFROM%>"
                       class="page_form_text" maxlength="20">&nbsp;��
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>�ܽ����̶ȣ�</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.radioHere("EDULEVEL", "EduLevel", EDULEVEL)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>ְ&nbsp;&nbsp;�ƣ�</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.radioHere("QUALIFICATION", "Qualification", QUALIFICATION)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_button_tbl_tr" colspan="5" height="5"></td>
        </tr>
        <tr class='page_form_tr'>
            <td rowspan="8" class="page_left_table_title">��������</td>
            <td class="page_form_title_td" nowrap>������λ��</td>
            <td colspan="3" class="page_form_td">
                <input type="text" <%=readonly%> name="COMPANY"
                       value="<%=COMPANY==null?"":COMPANY%>"
                       class="page_form_text" style="width:517px" maxlength="40">
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>��λ�绰��</td>
            <td class="page_form_td">
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
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>��λ��ַ��</td>
            <td colspan="3" class="page_form_td">
                <input type="text" <%=readonly%> name="COMADDR"
                       value="<%=COMADDR==null?"":COMADDR%>"
                       class="page_form_text" style="width:517px" maxlength="40">
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>��λ���ʣ�</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.radioHere("CLIENTTYPE", "ClientType1", CLIENTTYPE)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>ְ&nbsp;&nbsp;��</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.levelHere("TITLE", "Title", TITLE)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>�ֵ�λ����ʱ�䣺</td>
            <td colspan="3" class="page_form_td" nowrap>
                <input type="text" <%=readonly%> name="SERVFROM"
                       value="<%=SERVFROM==null?"":SERVFROM%>"
                       class="page_form_text" maxlength="20">&nbsp;��
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>���������룺</td>
            <td colspan="3" class="page_form_td">
                <input type="text" <%=readonly%> name="MONTHLYPAY"
                       value="<%=MONTHLYPAY==null?"":MONTHLYPAY%>"
                       class="page_form_text" maxlength="12">&nbsp;Ԫ
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>��ḣ�������ƶ������</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.checkHere("SOCIALSECURITY", "SocialSecurity", SOCIALSECURITY)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_button_tbl_tr" colspan="5" height="5"></td>
        </tr>
        <tr height="40px">
            <td colspan="5" style="border-bottom:#3366FF 1px solid;">&nbsp;</td>
        </tr>
    </table>
    <script src='../js/pagebutton.js' type='text/javascript'></script>
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
        <table border="0" cellspacing="0" cellpadding="0" width="538">
            <tr class='page_form_tr'>
                <td nowrap align="center">
                    <table bgcolor="#ffffff">
                        <tr class='page_button_tbl_tr'>
                            <td class='page_button_tbl_td'><input style="width:90px;" type='button' <%=submit%> id='goback'
                                                                  name='goback'
                                                                  value=' �� �� '
                                                                  onClick="history.go(-1)"></td>
                            <td class='page_button_tbl_td'><input style="width:90px;" type='button' <%=submit%> id='saveadd' name='save'
                                                                  value=' �� �� ' onClick="return Regvalid();"></td>
                            <td class='page_button_tbl_td'><input type='button' style="width:90px;" class='btn_2k3' name='button'
                                                                  value=' �� �� ' onClick="<%=closeClick%>"></td>
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
</table>
</div>
</body>
</html>

<%

%>
<script language="javascript" type="text/javascript">
    document.title = "<%=title%>";
    document.focus();

    function goSave() {
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

        return true;
    }


    function Regvalid() {
        if (goSave()) {
            document.forms[0].action = "/consume/appuser_save.jsp";
            document.forms[0].submit();
            //document.getElementById("saveadd").className="page_button_disabled";
            //document.getElementById("saveadd").disabled="true";
        }
    }
</script>
<%
        }
    }
%>
