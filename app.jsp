<%
    /********************************************
     * 文件名称：app.jsp
     * 功能描述：登陆页面
     * 创建日期：2009-03-12
     * @author：wangkai
     * @version 1.0
     *********************************************/
%>
<%@page language="java" contentType="text/html;charset=GBK" %>
<html>
<head>
    <title>海尔财务公司分期申请</title>
    <style type="text/css">
        <!--

        /** {filter: Gray}*/
        .inp_L1 {
            background-image: url('./images/登录.jpg');
            width: 70px;
            height: 22px;
            color: #C60;
            font-family: '微软雅黑';
            border: 0;
            cursor: pointer;
        }

        .inp_L2 {
            background-image: url('./images/登录2.jpg');
            width: 70px;
            height: 22px;
            color: #C60;
            font-family: '微软雅黑';
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
    String SID = request.getParameter("SID");                   //标识符
    String ORDERNO = request.getParameter("ORDERNO");           //订单号
    String NAME = request.getParameter("NAME");                 //客户姓名
    String EMAIL = request.getParameter("EMAIL");               //客户邮件地址
    String COMMATTR = request.getParameter("COMMATTR");         //商品名称及型号
    String NUMBER = request.getParameter("NUMBER");             //商品数量
    String AMT = request.getParameter("AMT");                   //订单金额
    String REQUESTTIME = request.getParameter("REQUESTTIME");  //请求提交时间

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
        if (SID.equals("001")) {//海尔商城
            session.setAttribute("SID", SID);
            session.setAttribute("CHANNEL", "海尔集团电子商务有限公司");
            session.setAttribute("ORDERNO", ORDERNO);
            session.setAttribute("NAME", NAME);
            session.setAttribute("EMAIL", EMAIL);
            session.setAttribute("COMMATTR", COMMATTR);
            session.setAttribute("NUMBER", NUMBER);
            session.setAttribute("AMT", AMT);
            session.setAttribute("REQUESTTIME", REQUESTTIME);
        }
    } else {
        //清空商城商品信息 begin
        session.removeAttribute("SID");
        session.removeAttribute("CHANNEL");
        session.removeAttribute("ORDERNO");
        session.removeAttribute("NAME");
        session.removeAttribute("EMAIL");
        session.removeAttribute("COMMATTR");
        session.removeAttribute("NUMBER");
        session.removeAttribute("AMT");
        session.removeAttribute("REQUESTTIME");
        //清空商城商品信息 end
    }
//    session.setAttribute("SID", "001");
//    session.setAttribute("CHANNEL", "海尔商城");
//    session.setAttribute("ORDERNO", "HR001");
//    session.setAttribute("NAME", "测试1");
//    session.setAttribute("EMAIL", "ljqd@msn.com");
//    session.setAttribute("COMMATTR", "彩电TF01，空调02");
//    session.setAttribute("NUMBER", "2");
//    session.setAttribute("AMT", "10082.5");
//    session.setAttribute("REQUESTTIME", "20090526140507");
%>
<body marginwidth="0" marginheight="0" onLoad="thisform.ID.focus();" topmargin="0" style="margin:0;background-color:#f0f0f0;">
<table border="0" height="100%" width="100%" cellpadding="0" cellspacing="0" style="margin:0px">
    <tr>
        <td align="center" style="padding:0;">
            <table border="0" height="670" width="959" cellpadding="0" cellspacing="0" align="center"
                   style="background-image:url('./images/心动购.jpg'); margin:0px">
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
                                                        style="font-weight: bold; font-size: 14px; color:#FFCC00;font-family:'微软雅黑';">
                                                        <%--<img src="images/登录提示1.jpg">--%>
                                                        已经是分期宝用户，请登录
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right"
                                                        style="font-weight: bold; font-size: 12px; color:white"
                                                        width="29%">证件类型：
                                                    </td>
                                                    <td>
                                                        <%--<select name="IDTYPE" style="width:153px; height:20px;"--%>
                                                        <%--onkeydown='if(event.keyCode==13) event.keyCode=9'>--%>
                                                        <%--<option value='0'>身份证</option>--%>
                                                        <%--</select>--%>
                                                        <select name="IDTYPE" style="width:153px; height:20px;"
                                                                onkeydown='if(event.keyCode==13) event.keyCode=9'>
                                                            <option value='0'>身份证</option>
                                                            <option value='1'>户口簿</option>
                                                            <option value='2'>护照</option>
                                                            <option value='3'>军证官</option>
                                                            <option value='4'>士兵证</option>
                                                            <option value='5'>港澳居民来往内地通行证</option>
                                                            <option value='6'>台湾居民来往内地通行证</option>
                                                            <option value='7'>临时身份证</option>
                                                            <option value='8'>外国人居留证</option>
                                                            <option value='9'>警官证</option>
                                                            <option value='X'>其他证件</option>
                                                        </select>
                                                        <%--<input type='text' size=20 name=IDTYPE title = '证件类型' style="width:153px; height:20px;" onkeydown='if(event.keyCode==13) event.keyCode=9'>--%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right"
                                                        style="font-weight: bold; font-size: 12px; color: white">证件号码：
                                                    </td>
                                                    <td><input type='text' size=20 name=ID title='证件号码'
                                                               maxlength="18"
                                                               style="width:153px; height:20px;"
                                                               onkeydown="if(event.keyCode==13) event.keyCode=9">
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right"
                                                        style="font-weight: bold; font-size: 12px; color:white">登陆口令：
                                                    </td>
                                                    <td><input type='password' size=20 name=PASSWORD
                                                               title='登陆口令' maxlength="20"
                                                               style="width:153px; height:20px;"
                                                               onkeydown='if(event.keyCode==13) document.forms[0].input_btn1.click();'>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right" nowrap></td>
                                                    <td align="left" nowrap><input type="button" value=" 登 录 "
                                                                                   onClick="onSbumitForm(document.getElementById('ID'));"
                                                                                   class="inp_L1"
                                                                                   onMouseOver="this.className='inp_L2'"
                                                                                   onMouseOut="this.className='inp_L1'"
                                                                                   id="input_btn1"/>&nbsp;&nbsp;<input
                                                            type="button" value="修改密码" onClick="rePass();"
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
                                                        height="45%"><p>第一次光临？

                                                        <p>注册分期宝用户</td>
                                                </tr>
                                                <tr>
                                                    <td height="55%" rowspan=1 align="center" id="login_button"><a
                                                            href="#"
                                                            onClick="newUser();"
                                                            class="link_gray"
                                                            target="_self"><img
                                                            src="./images/注册.gif"
                                                            border="0"></a></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                        <div id="contact"
                             style="height: 20px; WIDTH: 400px; left:2%; z-index: 2; position:relative; top: 230px;font-weight:normal; font-size: 12px; color:#FFCC00;font-family:'微软雅黑';">
                            欢迎随时致电业务咨询电话 <span style="color:#FFCC00">0532-88939384</span>，我们将竭诚为您服务！
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
															
<%--google代码--%>
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

<%--在线客服代码 168kf--%>
<%--<script>document.write("<scr"+"ipt language=\"javascript\" src=\"http://im.168kf.com/kf.php?arg=hrsf&style=1&keyword="+escape(document.referrer)+"\"></scr"+"ipt>");</script>--%>

<%--在线客服代码 365call-- 列表方式--%>
<script type='text/javascript' src='http://chat2.365webcall.com/IMMe1.aspx?settings=mw7mNmXNNm6X7Xbz3Am600bPz3Am6wIbNz3AN6mm00&LL=0'></script>
<%--在线客服代码 365call--  图标方式--%>
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
                // document.all.login_button.innerHTML='登录中，请稍候......';
                document.thisform.action = "./consume/applist.jsp";
                document.thisform.submit();
            }
        }
    }
    //-->
</SCRIPT>