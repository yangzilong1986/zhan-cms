<%
    /********************************************
     * �ļ����ƣ�app.jsp
     * ������������½ҳ��
     * �������ڣ�2009-03-12
     * @author��wangkai
     * @version 1.0
     *********************************************/
%>
<%@page language="java" contentType="text/html;charset=GBK" %>
<html>
<head>
    <title>��������˾��������</title>
    <style type="text/css">
        <!--

        /** {filter: Gray}*/
        .inp_L1 {
            background-image: url('./images/��¼.jpg');
            width: 70px;
            height: 22px;
            color: #C60;
            font-family: '΢���ź�';
            border: 0;
            cursor: pointer;
        }

        .inp_L2 {
            background-image: url('./images/��¼2.jpg');
            width: 70px;
            height: 22px;
            color: #C60;
            font-family: '΢���ź�';
            border: 0;
            cursor: pointer;
        }

        -->
    </style>

    <script src='js/main.js' type='text/javascript'></script>
    <script src='js/check.js' type='text/javascript'></script>
    <script src='js/checkID2.js' type='text/javascript'></script>
</head>
<%
    request.setCharacterEncoding("UTF-8");
    String SID = request.getParameter("SID");                   //��ʶ��
    String ORDERNO = request.getParameter("ORDERNO");           //������
    String NAME = request.getParameter("NAME");                 //�ͻ�����
    String EMAIL = request.getParameter("EMAIL");               //�ͻ��ʼ���ַ
    String COMMATTR = request.getParameter("COMMATTR");         //��Ʒ���Ƽ��ͺ�
    String NUMBER = request.getParameter("NUMBER");             //��Ʒ����
    String AMT = request.getParameter("AMT");                   //�������
    String REQUESTTIME = request.getParameter("REQUESTTIME");  //�����ύʱ��

    if (SID != null) {
        System.out.println("SID =||" + SID + "||");
        System.out.println("ORDERNO =||" + ORDERNO + "||");
        System.out.println("NAME =||" + NAME + "||");
        System.out.println("EMAIL =||" + EMAIL + "||");
        System.out.println("COMMATTR =||" + COMMATTR + "||");
        System.out.println("NUMBER =||" + NUMBER + "||");
        System.out.println("AMT =||" + AMT + "||");
        System.out.println("REQUESTTIME =||" + REQUESTTIME + "||");
        System.out.println("--------------------------------------------");
        if (SID.equals("001")) {//�����̳�
            session.setAttribute("SID", SID);
            session.setAttribute("CHANNEL", "�������ŵ����������޹�˾");
            session.setAttribute("ORDERNO", ORDERNO);
            session.setAttribute("NAME", NAME);
            session.setAttribute("EMAIL", EMAIL);
            session.setAttribute("COMMATTR", COMMATTR);
            session.setAttribute("NUMBER", NUMBER);
            session.setAttribute("AMT", AMT);
            session.setAttribute("REQUESTTIME", REQUESTTIME);
        }
    } else {
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
    }
//    session.setAttribute("SID", "001");
//    session.setAttribute("CHANNEL", "�����̳�");
//    session.setAttribute("ORDERNO", "HR001");
//    session.setAttribute("NAME", "����1");
//    session.setAttribute("EMAIL", "ljqd@msn.com");
//    session.setAttribute("COMMATTR", "�ʵ�TF01���յ�02");
//    session.setAttribute("NUMBER", "2");
//    session.setAttribute("AMT", "10082.5");
//    session.setAttribute("REQUESTTIME", "20090526140507");
%>
<body marginwidth="0" marginheight="0" onLoad="thisform.ID.focus();" topmargin="0" style="margin:0;background-color:#f0f0f0;">
<table border="0" height="100%" width="100%" cellpadding="0" cellspacing="0" style="margin:0px">
    <tr>
        <td align="center" style="padding:0;">
            <table border="0" height="670" width="959" cellpadding="0" cellspacing="0" align="center"
                   style="background-image:url('./images/�Ķ���.jpg'); margin:0px">
                <tr>
                    <td>
                        <div id="pnlAlert"
                             style="height: 100px; WIDTH: 356px; left:63%; z-index: 1; position:relative; top: 250px;">
                            <FORM name="thisform" action="consume" method=post>
                                <table style="border:none" width="100%" border="0">
                                    <tr>
                                        <td width="245" height="100%">
                                            <table style="border:none" width="100%">
                                                <tr>
                                                    <td align="left" colspan="2"
                                                        style="font-weight: bold; font-size: 14px; color:#FFCC00;font-family:'΢���ź�';">
                                                        <%--<img src="images/��¼��ʾ1.jpg">--%>
                                                        �Ѿ��Ƿ��ڱ��û������¼
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right"
                                                        style="font-weight: bold; font-size: 12px; color:white"
                                                        width="29%">֤�����ͣ�
                                                    </td>
                                                    <td>
                                                        <%--<select name="IDTYPE" style="width:153px; height:20px;"--%>
                                                        <%--onkeydown='if(event.keyCode==13) event.keyCode=9'>--%>
                                                        <%--<option value='0'>���֤</option>--%>
                                                        <%--</select>--%>
                                                        <select name="IDTYPE" style="width:153px; height:20px;"
                                                                onkeydown='if(event.keyCode==13) event.keyCode=9'>
                                                            <option value='0'>���֤</option>
                                                            <option value='1'>���ڲ�</option>
                                                            <option value='2'>����</option>
                                                            <option value='3'>��֤��</option>
                                                            <option value='4'>ʿ��֤</option>
                                                            <option value='5'>�۰ľ��������ڵ�ͨ��֤</option>
                                                            <option value='6'>̨����������ڵ�ͨ��֤</option>
                                                            <option value='7'>��ʱ���֤</option>
                                                            <option value='8'>����˾���֤</option>
                                                            <option value='9'>����֤</option>
                                                            <option value='X'>����֤��</option>
                                                        </select>
                                                        <%--<input type='text' size=20 name=IDTYPE title = '֤������' style="width:153px; height:20px;" onkeydown='if(event.keyCode==13) event.keyCode=9'>--%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right"
                                                        style="font-weight: bold; font-size: 12px; color: white">֤�����룺
                                                    </td>
                                                    <td><input type='text' size=20 name=ID title='֤������'
                                                               maxlength="18"
                                                               style="width:153px; height:20px;"
                                                               onkeydown="if(event.keyCode==13) event.keyCode=9">
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right"
                                                        style="font-weight: bold; font-size: 12px; color:white">��½���
                                                    </td>
                                                    <td><input type='password' size=20 name=PASSWORD
                                                               title='��½����' maxlength="20"
                                                               style="width:153px; height:20px;"
                                                               onkeydown='if(event.keyCode==13) document.forms[0].input_btn1.click();'>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right" nowrap></td>
                                                    <td align="left" nowrap><input type="button" value=" �� ¼ "
                                                                                   onClick="onSbumitForm(document.getElementById('ID'));"
                                                                                   class="inp_L1"
                                                                                   onMouseOver="this.className='inp_L2'"
                                                                                   onMouseOut="this.className='inp_L1'"
                                                                                   id="input_btn1"/>&nbsp;&nbsp;<input
                                                            type="button" value="�޸�����" onClick="rePass();"
                                                            class="inp_L1" onMouseOver="this.className='inp_L2'"
                                                            onMouseOut="this.className='inp_L1'" id="input_btn2"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                        <td width="1" height="100%" bgcolor="#E0E4F1" style="padding:0;"></td>
                                        <td width="109" height="100%" rowspan=1 align="center" id="login">
                                            <table border="0" width="100%" height="100%">
                                                <tr>
                                                    <td style="font-weight: bold; font-size: 12px; color:#FFCC00"
                                                        height="45%"><p>��һ�ι��٣�

                                                        <p>ע����ڱ��û�</td>
                                                </tr>
                                                <tr>
                                                    <td height="55%" rowspan=1 align="center" id="login_button"><a
                                                            href="#"
                                                            onClick="newUser();"
                                                            class="link_gray"
                                                            target="_self"><img
                                                            src="./images/ע��.gif"
                                                            border="0"></a></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                        <div id="contact"
                             style="height: 20px; WIDTH: 400px; left:2%; z-index: 2; position:relative; top: 230px;font-weight:normal; font-size: 12px; color:#FFCC00;font-family:'΢���ź�';">
                            ��ӭ��ʱ�µ�ҵ����ѯ�绰 <span style="color:#FFCC00">0532-88939384</span>�����ǽ��߳�Ϊ������
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
															
<%--google����--%>
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-591631-4");
pageTracker._trackPageview();
} catch(err) {}</script>
															
</body>

<%--���߿ͷ����� 168kf--%>
<%--<script>document.write("<scr"+"ipt language=\"javascript\" src=\"http://im.168kf.com/kf.php?arg=hrsf&style=1&keyword="+escape(document.referrer)+"\"></scr"+"ipt>");</script>--%>

<%--���߿ͷ����� 365call-- �б�ʽ--%>
<script type='text/javascript' src='http://chat2.365webcall.com/IMMe1.aspx?settings=mw7mNmXNNm6X7Xbz3Am600bPz3Am6wIbNz3AN6mm00&LL=0'></script>
<%--���߿ͷ����� 365call--  ͼ�귽ʽ--%>
<%--<script type='text/javascript' src='http://chat2.365webcall.com/IMMe1.aspx?settings=mw7mNXXNNm6X7Xbz3Am600bPz3Am6wIbXz3A66mm0w&LL=0'></script>--%>

</html>
<SCRIPT LANGUAGE="JavaScript">
    <!--
    function newUser() {
        document.thisform.action = "./consume/appnewuser.jsp";
        document.thisform.submit();
    }

    function rePass() {
        document.thisform.action = "./consume/apppass.jsp";
        document.thisform.submit();
    }
    function onCheck(idobj) {
        return checkIDCard(document.getElementById("IDTYPE"), idobj);
    }

    function goSave() {
        if (!isEmptyItem("IDTYPE"))return false;
        if (!isEmptyItem("ID"))return false;
        if (!isEmptyItem("PASSWORD"))return false;
        return true;
    }

    function onSbumitForm(idobj) {
        if (goSave() && onCheck(idobj)) {
            if (checkPass(document.getElementById("PASSWORD"))) {
                // document.all.login_button.innerHTML='��¼�У����Ժ�......';
                document.thisform.action = "./consume/applist.jsp";
                document.thisform.submit();
            }
        }
    }
    //-->
</SCRIPT>