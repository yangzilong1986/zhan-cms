<%@page contentType="text/html; charset=gb2312" language="java" %>
<%
    /**
     * @Description: ��Ǯ���׸�ҵ����Ȩ�ӿڷ���
     * @Copyright (c) �Ϻ���Ǯ��Ϣ�������޹�˾
     * @version 2.0
     */

    //��Ǯ֪ͨ���
    String flag = request.getParameter("msg").trim();


    if (flag.equals("success!")) {
        session.setAttribute("msg", "�����������ύ�ɹ������ڵ���ȷ�����������Ĵ�ӡ����ѡ���������뵥��ӡ��ǩ����������ͬ����֤���ļ���ӡ��һͬ���͸����ǣ����ͨ�������ǽ�����ȡ����ϵ��");
        session.setAttribute("funcdel", "window.opener.document.getElementById('print').click();pageWinClose();");
        session.setAttribute("isback", "0");
    } else if (flag.equals("success part!")) {
        session.setAttribute("msg", "�����������ύ�ɹ����������뵥״̬��������ϵ���ǣ�");
        session.setAttribute("flag", "0");
    } else if (flag.equals("false!")) {
        session.setAttribute("msg", "�������뻹δ��ɣ����ڿ�Ǯ��ǩԼʧ�ܣ�����ϵ��Ǯ��");
        session.setAttribute("flag", "0");
    } else if (flag.equals("error!")) {
        session.setAttribute("msg", "�������뻹δ��ɣ����ڿ�Ǯ��ǩԼ��֤ʧ�ܣ�����ϵ���ǣ�");
        session.setAttribute("flag", "0");
    }
//    System.out.println("rtnUrl = " + rtnUrl);
    response.sendRedirect("/showinfo.jsp");
%>
