<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.cmsi.pub.cenum.level" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.DecimalFormat" %>

<%--
===============================================
Title: ��ֲ���-�����ʷ��Ϣ��ϸ
Description: �����ʷ��Ϣ��ϸ��
 * @version  $Revision: 1.8 $  $Date: 2007/05/31 02:01:06 $
 * @author
 * <p/>�޸ģ�$Author: liuj $
===============================================
--%>

<%
    String BMNO = request.getParameter("BMNO");
    String FCSTATUS = request.getParameter("FCSTATUS");
    String FCNO = request.getParameter("FCNO");
    String showinfo = request.getParameter("showinfo");
    if (showinfo == null) showinfo = "1";

    if (BMNO == null) {
        session.setAttribute("mess", "û�з��ִ�����Ĳ�����");
        response.sendRedirect("../lettersucces.jsp");
    }
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
    request.setCharacterEncoding("GBK");
    String CREATEDATE = "";//�������
    String CLIENTNAME = "";//�ͻ�����
    String BRHID = "";//ά������
    String IDNO = "";//���֤����
    String CNLNO = "";//�жһ�Ʊ����
    BigDecimal BAL = null;//Ʊ����
	String SBMNO        = "";//ҵ��� 
    String PAYDATE = "";//��������
    String DUEDATE = "";//��������
    String FIRSTRESP = "";//��һ������
    String LOANTYPE3 = "";//������ʽ
    int YUQIDAY = 0;//��������
    String SIJIFL = "";//�ļ�����ռ����̬
    String CREDITCLASS = "";//��Ч���õȼ�
    String CURNO = "";//��������
    String FCAUTO = "";//�Զ���ֽ��
    String FC1 = "";//�Ŵ�Ա�϶����
    String OPERATOR = "";//���ά����
    String FC2 = "";//�Ŵ����������϶����
    String LASTMODIFIED = "";//����޸�����
    String FC3 = "";//���������϶����
//String FCNO="";
    String FCTYPE = "";
    String BMTYPE = "";//ҵ�����ʹ���
    String BADREASON = "";//�����γɲ���ԭ��  lj added in 2007-04-30
    String LOANWAY = "";//����Ͷ��           lj added in 2007-04-30
    String FCCLASS = "";  //��ֽ��         lj added in 2007-05-16
	String BILLNO = "";//Ʊ��̨�ʴ��롡������ zx added in 2007-06-07
	
    ConnectionManager manager = ConnectionManager.getInstance();
    String sql1 = "select * from FCMAIN where FCNO='" + FCNO + "'";
    CachedRowSet crs = manager.getRs(sql1);

    if (crs.next()) {
        CREATEDATE = crs.getString("CREATEDATE");//�������
        CLIENTNAME = crs.getString("CLIENTNAME");//�ͻ�����
        BRHID = crs.getString("BRHID");//ά������
        IDNO = crs.getString("IDNO");//���֤����
        CNLNO = crs.getString("CNLNO");//�жһ�Ʊ����
        BAL = crs.getBigDecimal("BAL");//Ʊ����
        SBMNO        = crs.getString("BMNO");//ҵ���
        PAYDATE = crs.getString("PAYDATE");//��������
        DUEDATE = crs.getString("DUEDATE");//��������
        FIRSTRESP = crs.getString("FIRSTRESP");//��һ������
        LOANTYPE3 = crs.getString("LOANTYPE3");//������ʽ
        YUQIDAY = crs.getInt("PASTDUEDAYS");
        ;//��������
        SIJIFL = crs.getString("LOANCAT2");//�ļ�����ռ����̬
        CREDITCLASS = crs.getString("CREDITCLASS");//��Ч���õȼ�
        CURNO = crs.getString("CURNO");//��������
        FCAUTO = crs.getString("FCAUTO");//�Զ���ֽ��
        FC1 = crs.getString("FC1");//�Ŵ�Ա�϶����
        OPERATOR = crs.getString("OPERATOR");//���ά����
        FC2 = crs.getString("FC2");//�Ŵ����������϶����
        LASTMODIFIED = crs.getString("LASTMODIFIED");//����޸�����
        FC3 = crs.getString("FC3");//���������϶����
        //FCNO        = crs.getString("FCNO");
        FCTYPE = crs.getString("FCTYPE");
        BMTYPE = crs.getString("BMTYPE");//ҵ�����ʹ���
        BADREASON = crs.getString("BADREASON");//�����γɲ���ԭ��                      lj added in 2007-04-30
        LOANWAY = crs.getString("LOANWAY");    //����Ͷ��                              lj added in 2007-04-30
        FCCLASS = crs.getString("FCCLASS");    //��ֽ�� 
       	BILLNO = crs.getString("BILLNO");  //Ʊ��̨�ʴ��롡������ zx added in 2007-06-07                     
        
    }

    String CMT1 = "";
    String CMT2 = "";
    String CMT3 = "";
    String CMT4 = "";
    String sql2 = "select * from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=1";
    CachedRowSet rs = manager.getRs(sql2);

    if (rs.next()) {
        CMT1 = DBUtil.fromDB(rs.getString("CMT1"));
        CMT2 = DBUtil.fromDB(rs.getString("CMT2"));
        CMT3 = DBUtil.fromDB(rs.getString("CMT3"));
        CMT4 = DBUtil.fromDB(rs.getString("CMT4"));
    }
//System.out.println("************************CMT2"+CMT2);


    String CLIENTNO = "";
    CachedRowSet rs1 = manager.getRs("select CLIENTNO from BMTABLE where BMNO='" + BMNO + "'");
    if (rs1.next()) {
        CLIENTNO = rs1.getString("CLIENTNO");
    }

    String submit = "";
//*****************lj deleted in 2007-05-17
//    if (FCSTATUS.equals("1")) {
//        submit = "class='page_button_active'";
//    }
//    if (FCSTATUS.equals("2")) {
//        submit = "disabled='true' class='page_button_disabled'";
//    }
//    if (FCSTATUS.equals("3")) {
//        submit = "disabled='true' class='page_button_disabled'";
//    }
//*****************lj deleted end
    submit = "disabled='true' class='page_button_disabled'";

    String butview = "";
    String butview1 = "";
    String title = "";
    String CLIENTTYPE = "";
    if (FCTYPE.equals(String.valueOf(EnumValue.FCType_ZiRanRenNongHu))) {
        butview = "";
        title = "�Ŵ�Ա�϶�:��Ȼ��һ��ũ������";
        CLIENTTYPE = "100001";
    }
    if (FCTYPE.equals(String.valueOf(EnumValue.FCType_ZiRanRenQiTa))) {
        butview1 = "<tr class='page_form_tr'>" +
                "<td class='page_form_td' colspan='6' align='center'>" +
                "<input type='button' name='z_dbqk' value='   �������   '  class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
                "<input type='button' name='z_qtqksm' value=' �������˵�� ' class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
                "</td>" +
                "</tr>";
        butview = "<tr class='page_form_tr'>" +
                "<td class='page_form_td' colspan='6' align='center'>" +
                "<input type='button' name='z_dkjbqk' value=' ���������� ' class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
                "<input type='button' name='z_cwzk' value='   ����״��   '  class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
                "<input type='button' name='z_fcwys' value='  �ǲ�������  '  class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +

                "</td>" +
                "</tr>";
        title = "�Ŵ�Ա�϶�:��Ȼ����������";
        CLIENTTYPE = "100001";
    }
    if (FCTYPE.equals(String.valueOf(EnumValue.FCType_QiYe)) || FCTYPE.equals(String.valueOf(EnumValue.FCType_WeiXingQiYe))) {//lj added FCType_WeiXingQiYe in 2007-05-16
        title = "�Ŵ�Ա�϶�:��ҵ�����";
        butview1 = "<tr class='page_form_tr'>" +
                "<td class='page_form_td' colspan='6' align='center'>" +
//                "<input type='button' name='q_other' value=' ��������˵�� '  class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
//                "<input type='button' name='q_cwfx' value='   �������   '  class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
//                "<input type='button' name='q_fcwysfx' value='�ǲ������ط���'  class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
                "<input type='button' name='qycw'   value=' ����������� '  class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
                "<input type='button' name='q_dkyt' value='   ������;   '  class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
                "<input type='button' name='q_dbfx' value='   ����Ѻ��   '  class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +

                "</td>" +
                "</tr>";
//        butview = "<tr class='page_form_tr'>" +
//                "<td class='page_form_td' colspan='6' align='center'>" +
//                "<input type='button' name='q_zycwzb' value=' ��Ҫ����ָ�� ' class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
//                "<input type='button' name='q_dkyt' value='   ������;   ' class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
//                "<input type='button' name='q_dbfx' value='   ��������   '  class='page_button_active' onClick='newAffair(this.name);'>&nbsp;" +
//
//                "</td>" +
//                "</tr>";
        CLIENTTYPE = "CMCC02";
    }


    String name1 = "";
    String name2 = "";
    String name3 = "";
    String name4 = "";
    String name5 = "";
    String name6 = "";
    String name7 = "";
    name7 = (FCSTATUS.equals(String.valueOf(EnumValue.FCStatus_WanCheng))) ? "�����϶����" : "�ϴ��϶����";

    if (BMTYPE.equals("12")) {
        name1 = "�жһ�Ʊ����";
        name2 = "Ʊ����";
        name3 = "��Ʊ��";
        name4 = "������";
        name5 = "ҵ��ĵ�����ʽ";
        name6 = "�������";
    } else if (BMTYPE.equals("13")) {
        name1 = "�жһ�Ʊ����";
        name2 = "ʵ�����ֽ��";
        name3 = "��������";
        name4 = "������";
        name5 = "ҵ��ĵ�����ʽ";
        name6 = "�������";
    } else if (BMTYPE.equals("14")) {
        name1 = "�жһ�Ʊ����";
        name2 = "ʵ�����ֽ��";
        name3 = "��������";
        name4 = "������";
        name5 = "ҵ��ĵ�����ʽ";
        name6 = "�������";
    } else {
        name1 = "��ݺ���";
        name2 = "�������";
        name3 = "��������";
        name4 = "��������";
        name5 = "������ʽ";
        name6 = "��������";
    }


    String mess = (String) request.getAttribute("mess");
    if (mess == null) {
        mess = "";
    } else {
        mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
    }
%>
<html>
<head>
    <title>�Ŵ�����</title>
    <link href="/css/platform.css" rel="stylesheet" type="text/css">
    <script src='/js/meizzDate.js' type='text/javascript'></script>
    <script src='/js/check.js' type='text/javascript'></script>
    <script src='/js/pagebutton.js' type='text/javascript'></script>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <style type="text/css">
        <!--
        body {
            margin-top: 5px;
        }

        -->
    </style>
</head>

<body background="/images/checks_02.jpg">
<form name="form1" method="post" id="winform" action="">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE">
<tr align="left">
    ά�����㣺<%=BRHID%><%=SCBranch.getLName(BRHID)%>
    <td height="30" bgcolor="#4477AA"><img src="/images/form/xing1.JPG" align="absmiddle"> <font size="2"
                                                                                                 color="#FFFFFF"><b><%=title%>
    </b></font> <img src="/images/form/xing1.JPG" align="absmiddle"></td>
</tr>
<br>
<tr align="center">
<td height="260" valign="middle">

<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">

<tr>
<td width="20">&nbsp;</td>
<td align="center" valign="middle">
<table class='error_message_tbl'>
    <tr class='error_message_tbl_tr'>
        <td class='error_message_tbl_td'><%=mess%>
        </td>
    </tr>
</table>
<table class='page_form_table' id='page_form_table' width="100%" height="20">
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>�������</td>
    <td class="page_form_td" nowrap><input type="text" readonly name="CREATEDATE"
                                           value="<%=CREATEDATE==null?"":CREATEDATE%>" class="page_form_text"></td>
    <td class="page_form_td" nowrap>�ͻ�����</td>
    <td class="page_form_td" nowrap><input type="text" readonly name="CLIENTNAME"
                                           value="<%=CLIENTNAME==null?"":DBUtil.fromDB(CLIENTNAME)%>"
                                           class="page_form_text"></td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>���֤����</td>
    <td class="page_form_td" nowrap><input type="text" readonly name="IDNO" value="<%=IDNO==null?"":IDNO%>"
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap><%=name1%>
    </td>
    <td class="page_form_td" nowrap><input type="text" readonly name="CNLNO" value="<%=CNLNO==null?"":CNLNO%>"
                                           class="page_form_text"></td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap><%=name2%>
    </td>
    <td class="page_form_td" nowrap><input type="text" readonly name="BAL" value="<%=BAL==null?"":df.format(BAL)%>"
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap>ҵ���</td>
    <td class="page_form_td" nowrap><input type="text" readonly name="BMNO" value="<%=BMNO==null?"":BMNO%>"
                                           class="page_form_text"></td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap><%=name3%>
    </td>
    <td class="page_form_td" nowrap><input type="text" readonly name="PAYDATE" value="<%=PAYDATE==null?"":PAYDATE%>"
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap><%=name4%>
    </td>
    <td class="page_form_td" nowrap><input type="text" readonly name="DUEDATE" value="<%=DUEDATE==null?"":DUEDATE%>"
                                           class="page_form_text"></td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>��һ������</td>
    <td class="page_form_td" nowrap><input type="text" readonly name="FIRSTRESP"
                                           value="<%=FIRSTRESP==null?"":SCUser.getName(FIRSTRESP)==null?"":SCUser.getName(FIRSTRESP)%>"
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap><%=name5%>
    </td>
    <td class="page_form_td" nowrap><input type="text" readonly name="LOANTYPE3"
                                           value="<%=BMTYPE.equals("12") ||  BMTYPE.equals("13") || BMTYPE.equals("14")?"��֤":LOANTYPE3==null?"":level.getEnumItemName("LoanType3",LOANTYPE3)%>"
                                           class="page_form_text"></td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap><%=name6%>
    </td>
    <td class="page_form_td" nowrap><input type="text" readonly name="YUQIDAY" value="<%=YUQIDAY<0?0:YUQIDAY%>"
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap>�ļ�����ռ����̬</td>
    <td class="page_form_td"
        nowrap><%=BMTYPE.equals("12") || BMTYPE.equals("13") || BMTYPE.equals("14") ? level.levelHere1("SIJIFL", "LoanCat2", "1") : SIJIFL == null ? "" : level.levelHere1("SIJIFL", "LoanCat2", SIJIFL)%>
    </td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>���õȼ�</td>
    <td class="page_form_td" nowrap><input type="text" readonly name="CREDITCLASS"
                                           value="<%=CREDITCLASS==null?"":level.getEnumItemName("CreditClass",CREDITCLASS)%>"
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap>���Ҵ���</td>
    <td class="page_form_td" nowrap><input type="text" readonly name="CURNO" value="<%=CURNO==null?"":CURNO%>"
                                           class="page_form_text"></td>
</tr>
<!--lj added in 2007-04-28-->
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>�����γɲ���ԭ��</td>
    <td class="page_form_td" nowrap><input type="text" readonly
                                           value="<%=BADREASON==null?"":level.getEnumItemName("BadReason",BADREASON)%>"
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap>����Ͷ��</td>
    <td class="page_form_td" nowrap><input type="text" readonly
                                           value="<%=LOANWAY==null?"":level.getEnumItemName("LoanWay",LOANWAY)%>"
                                           class="page_form_text"></td>
</tr>
<!--lj added end-->
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>�Զ���ֽ��</td>
    <td class="page_form_td" nowrap><input type="text" readonly name="FCAUTO"
                                           value="<%=FCAUTO==null?"":level.getEnumItemName("LoanCat1",FCAUTO)%>"
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap>�Ŵ�Ա�϶����</td>
    <td class="page_form_td" nowrap><input type="text" readonly
                                           value="<%=FC1==null?"":level.getEnumItemName("LoanCat1",FC1)%>"
                                           class="page_form_text"></td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>���ά����</td>
    <td class="page_form_td" nowrap><!--lj changed in 2007-05-16 ȥ������ʾnull-->
        <input type="text" readonly name="OPERATOR"
               value="<%=OPERATOR==null||OPERATOR.trim().equals("")?"":SCUser.getName(OPERATOR)%>"
               class="page_form_text"></td>
    <td class="page_form_td" nowrap>�Ŵ����������϶����&nbsp;</td>
    <td class="page_form_td" nowrap><input type="text" readonly
                                           value="<%=FC2==null?"":level.getEnumItemName("LoanCat1",FC2)%>"
                                           class="page_form_text"></td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>���ά������</td>
    <td class="page_form_td" nowrap><input type="text" readonly name="LASTMODIFIED"
                                           value="<%=LASTMODIFIED==null?"":LASTMODIFIED%>" class="page_form_text">
    </td>
    <td class="page_form_td" nowrap>���������϶����</td>
    <td class="page_form_td" nowrap><input type="text" readonly
                                           value="<%=CMT4==null?"":level.getEnumItemName("LoanCat1",FC3)%>"
                                           class="page_form_text"></td>
</tr>
<!--lj added in 2007-05-16 ��ֽ��-->
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap><%=name7%>
    </td>
    <td class="page_form_td" nowrap><input type="text" readonly name="FCCLASS"
                                           value="<%=FCCLASS==null?"":FCCLASS.equals("0")?"":level.getEnumItemName("LoanCat1",FCCLASS)%>"
                                           class="page_form_text"></td>
    <td class="page_form_td" nowrap></td>
    <td class="page_form_td" nowrap></td>
</tr>
<!--lj added end-->
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>�����������ɼ�<br>�����������</td>
    <td class="page_form_td" nowrap colspan="3"><textarea name="CMT1" readonly wrap="PHYSICAL" cols="57" rows="3"
                                                          class="page_form_text"><%=CMT1 == null ? "" : CMT1%>
    </textarea></td>
</tr>
<tr class='page_form_tr'>
    <!--lj added FCType_WeiXingQiYe in 2007-05-15-->
    <td class="page_form_td"
        nowrap><%=FCTYPE.equals(String.valueOf(EnumValue.FCType_QiYe)) || FCTYPE.equals(String.valueOf(EnumValue.FCType_WeiXingQiYe)) ? "�Ŵ����۷������" : "�ͻ�����������"%>
    </td>
    <td class="page_form_td" nowrap colspan="3"><textarea name="CMT2" readonly wrap="PHYSICAL" cols="57" rows="3"
                                                          class="page_form_text"><%=CMT2 == null ? "" : CMT2%>
    </textarea></td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>�Ŵ������������<br>���</td>
    <td class="page_form_td" nowrap colspan="3"><textarea name="CMT3" readonly wrap="PHYSICAL" cols="57" rows="3"
                                                          class="page_form_text"><%=CMT3 == null ? "" : CMT3%>
    </textarea></td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>�������������<br>��</td>
    <td class="page_form_td" nowrap colspan="3"><textarea name="CMT4" readonly wrap="PHYSICAL" cols="57" rows="3"
                                                          class="page_form_text"><%=CMT4 == null ? "" : CMT4%>
    </textarea></td>
</tr>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>&nbsp;</td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>

<tr class='page_form_tr'>

    <td class='page_form_td' colspan='6' align="center">
        <input type="button" name="CorpCapital" value=" �ͻ�������Ϣ " class="page_form_button_active"
               onclick="pressSelfButtonOpenNewWin('/templates/defaultform.jsp?Plat_Form_Request_Form_ID=<%=CLIENTTYPE%>&Plat_Form_Request_Event_ID=0&flag=read&CLIENTNO=<%=CLIENTNO%>','111','height=600,width=700,toolbar=no,scrollbars=yes,resizable=yes');">
        <input type="button" name="CorpPayables" value=" ��ʷ�����Ϣ " class="page_form_button_active"
               onClick="newAffair(this.name);">
        <input type="button" name="CorpPayables" value=" ������������ " class="page_form_button_active"
               onclick="pressSelfButtonOpenNewWin('/templates/defaultform.jsp?Plat_Form_Request_Form_ID=BMTRANSLIST&Plat_Form_Request_Event_ID=0&flag=read&BMNO=<%=BMNO%>','222','height=600,width=700,toolbar=no,scrollbars=yes,resizable=yes');">
        <%if (showinfo.equals("1")){%>
        <input type="button" name="CorpPayables" value="�ͻ�������������" class="page_form_button_active"
               onclick="pressSelfButtonOpenNewWin('../clientelseloan/elseloanlist.jsp?IDNO=<%=IDNO %>&BMNO=<%=SBMNO==null?"":SBMNO %>&BMTYPE=<%=BMTYPE %>&BILLNO=<%=BILLNO %>&CLIENTNAME=<%=DBUtil.fromDB(CLIENTNAME)%>','newwin','height=600,width=700,toolbar=no,scrollbars=yes,resizable=yes');">
        <%}%>
    </td>
</tr>
<%=butview%>
<%=butview1%>
<tr class='page_form_tr'>
    <td class="page_form_td" nowrap>&nbsp;</td>
    <td class="page_form_td" nowrap>&nbsp;</td>
</tr>
</table>
</td>
<td width="20">&nbsp;</td>
</tr>
</table>

</td>
</tr>
<tr height="35" align="center" valign="middle">
    <td align="center">
        <table border="0" cellspacing="0" cellpadding="0" width="538">
            <tr>
                <td nowrap align="center">
                    <table class='list_button_tbl'>
                        <tr class='list_button_tbl_tr'>
                            <td class='page_button_tbl_td'><input type='submit' <%=submit%> id='saveadd' name='save'
                                                                  value=' �ύ ' onClick="return Regvalid();"></td>
                            <td class='page_button_tbl_td'><input type='button' class='page_button_active' name='button'
                                                                  value=' �ر� '
                                                                  onClick="javascript:parent.parent.window.close();">
                            </td>

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
</form>
</body>
</html>
<script language="javascript" type="text/javascript">
    document.title = "<%=title%>";
    document.focus();
    function deptrefer_click() {
        var url = "Hyperlink/fl_info.jsp";
        window.open(url, 0, "height=350,width=460,toolbar=no,scrollbars=yes");
    }
    function deptrefer_click1() {
        var url = "Hyperlink/fl_info1.jsp";
        window.open(url, 0, "height=350,width=460,toolbar=no,scrollbars=yes");
    }

    function newAffair(affairName) {
        var flag = 0;
        if (affairName == 'CorpPayables') {
            var url = "history_qf.jsp?BMNO=<%=BMNO%>&FCNO=<%=FCNO%>&CREATEDATE=<%=CREATEDATE%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'q_dkyt') {
            var url = "/fcworkbench/q_dkyt/q_dkyt.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'q_cwfx') {
            var url = "/fcworkbench/q_cwfx/q_cwfx.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'q_fcwysfx') {
            var url = "/fcworkbench/q_fcwysfx/q_fcwysfx.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'q_other') {
            var url = "/fcworkbench/q_other/q_other.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'qycw') { //��ҵ�����������       lj added in 2007-05-29
            var url = "/fcworkbench/qycw/qycw_list.jsp?CLIENTNO=<%=CLIENTNO%>";
            window.open(url, 'FI7', 'width=700,height=500,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'q_dbfx') {
            var url = "/fcworkbench/q_dbfx/framwrok.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>&FCTYPE=<%=FCTYPE%>&BMNO=<%=BMNO%>";
            window.open(url, 'FI2', 'width=700,height=500,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'q_zycwzb') {
            var url = "/fcworkbench/q_zycwzb/q_zycwzb.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'z_dbqk') {
            var url = "/fcworkbench/z_dbqk/z_dbqk.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'z_fcwys') {
            var url = "/fcworkbench/z_fcwys/z_fcwys.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'z_cwzk') {
            var url = "/fcworkbench/z_cwzk/z_cwzk.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'z_dkjbqk') {
            var url = "/fcworkbench/z_dkjbqk/z_dkjbqk.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
        if (affairName == 'z_qtqksm') {
            var url = "/fcworkbench/z_qtqksm/z_qtqksm.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
            window.open(url, 'FI2', 'height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
        }
    }

</script>
