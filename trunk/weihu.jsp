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
            background-image:url('/images/登录.jpg');
			width:70px; 
			height:22px; 
			color:#C60; 
			font-family:'微软雅黑';
			border:0;
            cursor: pointer;
        }
		
		.inp_L2 {
            background-image:url('/images/登录2.jpg'); 
			width:70px; 
			height:22px; 
			color:#C60; 
			font-family:'微软雅黑';
			border:0;
            cursor: pointer;
        }

        -->
    </style>
</head>
<%
    String ORDERNO = request.getParameter("ORDERNO");              //订单号
    String NAME = request.getParameter("NAME");                 //客户姓名
    String EMAIL = request.getParameter("EMAIL");               //客户邮件地址
    String COMMNAME = request.getParameter("COMMNAME");         //商品名称
    String COMMTYPE = request.getParameter("COMMTYPE");         //商品型号
    String NUMBER = request.getParameter("NUMBER");             //商品数量
    String AMT = request.getParameter("AMT");                   //订单金额
    String REQUESTTIME = request.getParameter("REQUESTTIME");  //请求提交时间

    if (ORDERNO != null) {
        System.out.println("ORDERNO =||" + ORDERNO + "||");
        System.out.println("NAME =||" + NAME + "||");
        System.out.println("EMAIL =||" + EMAIL + "||");
        System.out.println("COMMNAME =||" + COMMNAME + "||");
        System.out.println("COMMTYPE =||" + COMMTYPE + "||");
        System.out.println("NUMBER =||" + NUMBER + "||");
        System.out.println("AMT =||" + AMT + "||");
        System.out.println("REQUESTTIME =||" + REQUESTTIME + "||");
    }
%>
<body marginwidth="0" marginheight="0" topmargin="0" style="margin:0">
<table border="0" height="100%" width="100%" cellpadding="0" cellspacing="0" style="margin:0px">
    <tr>
        <td align="center" style="padding:0;">
<table border="0" height="670" width="959" cellpadding="0" cellspacing="0" align="center"
       style="background-image:url('images/心动购.jpg'); margin:0px; opacity:0.5;-moz-opacity:0.5;filter:alpha(opacity=50);">
    <tr>
        <td>
            <div id="pnlAlert"
                 style="height: 52px; WIDTH: 380px; left:33%; z-index: 1; position:relative; top: -20px; display:block; font-weight:bold; font-size: 40px; color:#3300CC;font-family:'微软雅黑';">
                系统维护中，暂时停止服务。。。<P><span style="color:#FFCC00; font-size: 30px; "></span></P>
            </div>
            <div id="contact"
                 style="height: 20px; WIDTH: 400px; left:60%; z-index: 2; position:relative; top: 240px;font-weight:normal; font-size: 12px; color:#FFCC00;font-family:'微软雅黑';">欢迎随时致电业务咨询电话 <span style="color:#FFCC00">0532-88939904</span>，我们将竭诚为您服务！</div>
        </td>
    </tr>
</table>
</td>
    </tr>
</table>
</body>
</html>