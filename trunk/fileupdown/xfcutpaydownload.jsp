<%--
  Created by IntelliJ IDEA.
  User: zhanrui
  Date: 2009-5-12
  Time: 9:09:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=gbk" %>
<%@ page language="java" import="java.io.*,java.net.*" pageEncoding="gbk" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%
    response.reset();
    response.setContentType("text/html");
    javax.servlet.ServletOutputStream ou = response.getOutputStream();
    //String filepath = "d:/temp/";
    String filepath = PropertyManager.getProperty("BANK_CUTPAYFILE_PATH");
    //String filename = new String(request.getParameter("filename").getBytes("ISO8859_1"), "GBK").toString();
    String filename = (String) request.getAttribute("CUTPAYFILENAME");

    System.out.println("DownloadFile filepath:" + filepath);
    System.out.println("DownloadFile filename:" + filename);

    java.io.File file = new java.io.File(filepath + filename);
    if (!file.exists()) {
        System.out.println(file.getAbsolutePath() + " �ļ�������!");
        session.setAttribute("msg", "�ļ�" + filepath + filename + "�����ڣ���ȷ�ϣ�");
        response.sendRedirect("/showinfo.jsp");
//         response.sendRedirect("http://192.168.91.20/showinfo.jsp");
        return;
    }
    // ��ȡ�ļ���
    java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
    // �����ļ�
    // ������Ӧͷ�����ر�����ļ���
    if (filename != null && filename.length() > 0) {
    response.reset();
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(filename.getBytes("gb2312"), "iso8859-1"));
        if (fileInputStream != null) {
            int filelen = fileInputStream.available();
            //���ļ�����
            byte a[] = new byte[filelen];
            fileInputStream.read(a);
            ou.write(a);
        }
        fileInputStream.close();
        ou.close();
    }
%>