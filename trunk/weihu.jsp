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
            background-image:url('/images/��¼.jpg');
			width:70px; 
			height:22px; 
			color:#C60; 
			font-family:'΢���ź�';
			border:0;
            cursor: pointer;
        }
		
		.inp_L2 {
            background-image:url('/images/��¼2.jpg'); 
			width:70px; 
			height:22px; 
			color:#C60; 
			font-family:'΢���ź�';
			border:0;
            cursor: pointer;
        }

        -->
    </style>
</head>
<%
    String ORDERNO = request.getParameter("ORDERNO");              //������
    String NAME = request.getParameter("NAME");                 //�ͻ�����
    String EMAIL = request.getParameter("EMAIL");               //�ͻ��ʼ���ַ
    String COMMNAME = request.getParameter("COMMNAME");         //��Ʒ����
    String COMMTYPE = request.getParameter("COMMTYPE");         //��Ʒ�ͺ�
    String NUMBER = request.getParameter("NUMBER");             //��Ʒ����
    String AMT = request.getParameter("AMT");                   //�������
    String REQUESTTIME = request.getParameter("REQUESTTIME");  //�����ύʱ��

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
       style="background-image:url('images/�Ķ���.jpg'); margin:0px; opacity:0.5;-moz-opacity:0.5;filter:alpha(opacity=50);">
    <tr>
        <td>
            <div id="pnlAlert"
                 style="height: 52px; WIDTH: 380px; left:33%; z-index: 1; position:relative; top: -20px; display:block; font-weight:bold; font-size: 40px; color:#3300CC;font-family:'΢���ź�';">
                ϵͳά���У���ʱֹͣ���񡣡���<P><span style="color:#FFCC00; font-size: 30px; "></span></P>
            </div>
            <div id="contact"
                 style="height: 20px; WIDTH: 400px; left:60%; z-index: 2; position:relative; top: 240px;font-weight:normal; font-size: 12px; color:#FFCC00;font-family:'΢���ź�';">��ӭ��ʱ�µ�ҵ����ѯ�绰 <span style="color:#FFCC00">0532-88939904</span>�����ǽ��߳�Ϊ������</div>
        </td>
    </tr>
</table>
</td>
    </tr>
</table>
</body>
</html>