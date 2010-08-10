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
Title: 消费信贷-客户注册
Description: 个人消费分期客户注册。
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

    String PASSWORD = request.getParameter("PASSWORD");   //客户密码
    String IDTYPE = request.getParameter("IDTYPE");       //证件名称
    String ID = request.getParameter("ID");                //证件号码
    String NAME = request.getParameter("NAME");            //客户名称 desc=企业(个人)名称

    String strGoUrl = request.getParameter("goUrl");      //关闭页面标志，弹出页面关闭，非弹出返回主页面
    String closeClick = "pageWinClose();";
    if (strGoUrl != null) closeClick = "gopage('" + strGoUrl + "')";


    String mess = "";
    if (PASSWORD == null && (ID == null || IDTYPE == null)) {
        session.setAttribute("msg", "没有发现传送入的参数！");
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
        String EMAIL = (String)session.getAttribute("EMAIL");  //电子邮件


        boolean ifErrClient = false;
        CachedRowSet crs = null;
        if (!sql1.equals("")) crs = manager.getRs(sql1);
        if (crs != null && crs.next()) {
            if (NAME.compareTo(crs.getString("NAME")) != 0) {
                ifErrClient = true;  //客户名称 desc=企业(个人)名称)
                mess = "客户信息错误，此证件已被其他客户使用！";
            }
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
            LIVEFROM = crs.getString("LIVEFROM");                //本地居住时间
            PC = crs.getString("PC");                             //住宅邮编
            COMPC = crs.getString("COMPC");                      //单位邮编
            EMAIL = crs.getString("EMAIL");                      //电子邮件

//            ID = crs.getString("ID");                              //证件号码
//            IDTYPE = crs.getString("IDTYPE");                     //证件名称
//            PASSWORD = crs.getString("PASSWORD");                 //用户密码
        } else {
            //根据证件确定最近登记的客户资料
            sql1 = " select CLIENTNO,NAME,APPDATE from CMINDVCLIENT where IDTYPE='" + IDTYPE + "' and ID='" + ID + "' and rownum=1  order by CLIENTNO desc";
            crs = manager.getRs(sql1);
            if (crs.next()) {
                ifErrClient = true;
                if (!crs.getString("NAME").equals(NAME)) {
                    mess = "客户信息错误，此证件已被其他客户使用！";
                } else {
                    mess = "客户信息错误，用户已存在，您是否忘记了密码？";
                }
            }
        }

        BIRTHDAY = (BIRTHDAY == null) ? "" : BIRTHDAY;
        EMAIL = (EMAIL == null) ? "" : EMAIL;


        String readonly = "";
        String readonly_input = "readonly";
//        String submit = "class='page_button_active'";
        String submit = "class='btn_2k3'";
        String title = "个人消费分期付款客户信息";


        if (ifErrClient) {
            mess = "<li class='error_message_li'>" + mess.trim() + "</li>";
            session.setAttribute("msg", mess);
            response.sendRedirect("../showinfo.jsp");
        } else {
%>
<html>
<head>
    <title>消费信贷</title>
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
                                                                                                   color="#FFFFFF"><b>海尔集团财务有限责任公司个人消费分期用户注册</b></font>
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
            <td rowspan="11" class="page_left_table_title">基本信息</td>
            <td class="page_form_title_td" nowrap>姓&nbsp;&nbsp;名：</td>
            <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="NAME"
                                                   value="<%=NAME==null?"":NAME%>"
                                                   class="page_form_text" maxlength="40"></td>
            <td class="page_form_title_td" nowrap>&nbsp;性&nbsp;&nbsp;别：</td>
            <td class="page_form_td" nowrap><%=level.radioHere("GENDER", "Gender", GENDER)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>手&nbsp;&nbsp;机：</td>
            <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="PHONE1"
                                                   value="<%=PHONE1==null?"":PHONE1%>"
                                                   class="page_form_text" maxlength="15"></td>
            <td class="page_form_title_td" nowrap>&nbsp;员工卡号码：</td>
            <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="EMPNO"
                                                   value="<%=EMPNO==null?"":EMPNO%>"
                                                   class="page_form_text" maxlength="8"></td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>身份证件名称：</td>
            <td class="page_form_td" nowrap>
                <%--<select name="IDTYPE" class="page_form_select">--%>
                <%--<option value='0'>身份证</option>--%>
                <%--</select>--%>
                <%=level.levelHere("IDTYPE", "IDType", IDTYPE)%>
            </td>
            <td class="page_form_title_td" nowrap>&nbsp;证件号码：</td>
            <td class="page_form_td" nowrap><input type="text" <%=readonly%> name="ID"
                                                   value="<%=ID==null?"":ID%>"
                                                   class="page_form_text"
                                                   onblur="if(checkIDCard(document.getElementById('IDTYPE'),this))getbirthday(this.value,'BIRTHDAY');"
                                                   maxlength="18">
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>出生日期：</td>
            <td class="page_form_td" nowrap><input type="text" <%=readonly_input%> name="BIRTHDAY"
                                                   id="BIRTHDAY"
                                                   value="<%=BIRTHDAY.equals("")?"":DBUtil.to_Date(BIRTHDAY)%>"
                                                   class="page_form_text">
                <input type="button" value="…" class="page_form_refbutton"
                       onClick="setday(this,winform.BIRTHDAY)"></td>
            <td class="page_form_title_td" nowrap>&nbsp;健康状况：</td>
            <td class="page_form_td"
                nowrap><%=level.radioHere("HEALTHSTATUS", "HealthStatus", HEALTHSTATUS)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>婚姻状况：</td>
            <td class="page_form_td"
                nowrap><%=level.radioHere("MARRIAGESTATUS", "MarriageStatus", MARRIAGESTATUS)%>
            </td>
            <td class="page_form_title_td" nowrap>&nbsp;是否有子女：</td>
            <td class="page_form_td"
                nowrap><%=level.radioHere("BURDENSTATUS", "BurdenStatus", BURDENSTATUS)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>户籍所在地：</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.radioHere("RESIDENCEADR", "ResidenceADR", RESIDENCEADR)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>家庭住址：</td>
            <td colspan="3" class="page_form_td">
                <input type="text" <%=readonly%> name="CURRENTADDRESS"
                       value="<%=CURRENTADDRESS==null?"":CURRENTADDRESS%>"
                       class="page_form_text" style="width:517px" maxlength="40">
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>住宅邮编：</td>
            <td class="page_form_td"><input type="text" <%=readonly%> name="PC"
                                            value="<%=PC==null?"":PC%>"
                                            class="page_form_text" maxlength="6"></td>
            <td class="page_form_title_td" nowrap>&nbsp;电子邮箱：</td>
            <td class="page_form_td">
                <input type="text" <%=readonly%> name="EMAIL"
                       value="<%=EMAIL==null?"":EMAIL%>"
                       class="page_form_text" maxlength="40"></td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>家庭电话：</td>
            <td class="page_form_td"><input type="text" <%=readonly%> name="PHONE2"
                                            value="<%=PHONE2==null?"":PHONE2%>"
                                            class="page_form_text" maxlength="15"></td>
            <td class="page_form_title_td" nowrap>&nbsp;本地居住时间：</td>
            <td class="page_form_td">
                <input type="text" <%=readonly%> name="LIVEFROM"
                       value="<%=LIVEFROM==null?"":LIVEFROM%>"
                       class="page_form_text" maxlength="20">&nbsp;年
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>受教育程度：</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.radioHere("EDULEVEL", "EduLevel", EDULEVEL)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>职&nbsp;&nbsp;称：</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.radioHere("QUALIFICATION", "Qualification", QUALIFICATION)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_button_tbl_tr" colspan="5" height="5"></td>
        </tr>
        <tr class='page_form_tr'>
            <td rowspan="8" class="page_left_table_title">工作资料</td>
            <td class="page_form_title_td" nowrap>工作单位：</td>
            <td colspan="3" class="page_form_td">
                <input type="text" <%=readonly%> name="COMPANY"
                       value="<%=COMPANY==null?"":COMPANY%>"
                       class="page_form_text" style="width:517px" maxlength="40">
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>单位电话：</td>
            <td class="page_form_td">
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
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>单位地址：</td>
            <td colspan="3" class="page_form_td">
                <input type="text" <%=readonly%> name="COMADDR"
                       value="<%=COMADDR==null?"":COMADDR%>"
                       class="page_form_text" style="width:517px" maxlength="40">
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>单位性质：</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.radioHere("CLIENTTYPE", "ClientType1", CLIENTTYPE)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>职&nbsp;&nbsp;务：</td>
            <td colspan="3" class="page_form_td"
                nowrap><%=level.levelHere("TITLE", "Title", TITLE)%>
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>现单位工作时间：</td>
            <td colspan="3" class="page_form_td" nowrap>
                <input type="text" <%=readonly%> name="SERVFROM"
                       value="<%=SERVFROM==null?"":SERVFROM%>"
                       class="page_form_text" maxlength="20">&nbsp;年
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>个人月收入：</td>
            <td colspan="3" class="page_form_td">
                <input type="text" <%=readonly%> name="MONTHLYPAY"
                       value="<%=MONTHLYPAY==null?"":MONTHLYPAY%>"
                       class="page_form_text" maxlength="12">&nbsp;元
            </td>
        </tr>
        <tr class='page_form_tr'>
            <td class="page_form_title_td" nowrap>社会福利保障制度情况：</td>
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
                                                                  value=' 返 回 '
                                                                  onClick="history.go(-1)"></td>
                            <td class='page_button_tbl_td'><input style="width:90px;" type='button' <%=submit%> id='saveadd' name='save'
                                                                  value=' 提 交 ' onClick="return Regvalid();"></td>
                            <td class='page_button_tbl_td'><input type='button' style="width:90px;" class='btn_2k3' name='button'
                                                                  value=' 关 闭 ' onClick="<%=closeClick%>"></td>
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
