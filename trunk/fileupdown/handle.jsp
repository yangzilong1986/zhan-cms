<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="org.apache.commons.fileupload.FileUpload" %>
<%@ page import="org.apache.commons.fileupload.RequestContext" %>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletRequestContext" %>
<%@ page import="zt.platform.db.ConnectionManager" %>
<%@ page import="zt.platform.db.DatabaseConnection" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2009-4-24
  Time: 9:49:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=GBK" %>
<html>
<head><title>Simple jsp page</title></head>
<body>
<%
    String FILETP = request.getParameter("FILETP");              //�ļ���ص�ҵ�����
    String OPNO = request.getParameter("OPNO");                  //����ҵ���
    String OBJID = request.getParameter("OBJID");                //����Ψһ��ʶ

    if (FILETP == null || OPNO == null) {
        session.setAttribute("msg", "�ϴ����ܳ�������ϵ���ǣ�");
        session.setAttribute("isback", "0");
        session.setAttribute("closeSelf", "1");
        response.sendRedirect("../showinfo.jsp");
    }

    if (OBJID != null) {//ɾ������
        zt.platform.cachedb.ConnectionManager manager = zt.platform.cachedb.ConnectionManager.getInstance();
        try {
            String[] sql = new String[1];
            sql[0] = "delete from FILEBLOB where objid='" + OBJID + "'";

            if (manager.execBatch(sql)) {
                session.setAttribute("msg", "ɾ�������ɹ���");
                session.setAttribute("isback", "0");
                //session.setAttribute("closeSelf", "1");
                session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
                response.sendRedirect("../showinfo.jsp");
            } else {
                session.setAttribute("msg", "ɾ������ʧ�ܣ����ٴγ��ԣ����������⣬����ϵ���ǣ�");
                session.setAttribute("isback", "0");
                //session.setAttribute("closeSelf", "1");
                session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
                response.sendRedirect("../showinfo.jsp");
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    } else {

        try {
            RequestContext ctx = new ServletRequestContext(request);
            boolean isMultipart = FileUpload.isMultipartContent(ctx);

            if (isMultipart) {
                //��Ŀ¼
                String dir = "\\temp";
                //�����·��
                String rootPath = "C://";
                //�����·��
                //String rootPath = getServletConfig().getServletContext().getRealPath("/") ;
                String filePath = rootPath + dir;
                //���óɹ���־
                int temp = 0;


                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(1000 * 1024); // ���ñ��浽�ڴ��еĴ�С��1M
                // ����һ���µ�Upload����
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setSizeMax(10 * 1024 * 1024);// ��������ϴ��ļ��Ĵ�С10M
                upload.setFileSizeMax(2 * 1024 * 1024); // ���õ�������ϴ��ļ��Ĵ�С2M

                // ����request�еĴ������ļ���������Item�ļ��ϣ�
                // ��ѯItems��������Ǳ��򣬾���һ���ļ�����
                List items = upload.parseRequest(ctx);//������������ϴ��ļ���Ԫ
                Iterator iter = items.iterator();

                String filenm = "";
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();

                    //������ļ�����
                    if (!item.isFormField()) {
                        filenm = FILETP + OPNO + "_" + item.getName().substring(item.getName().lastIndexOf("\\") + 1, item.getName().length());
                        String  extnm = filenm.substring(filenm.length()-3,filenm.length());
                        if (extnm.equalsIgnoreCase("jsp")
                                ||extnm.equalsIgnoreCase("xls")
                                ||extnm.equalsIgnoreCase("bat")
                                ||extnm.equalsIgnoreCase("exe")
                                ||extnm.equalsIgnoreCase("com")) {
                            session.setAttribute("msg", "�ϴ��ļ����Ͳ�������Χ�ڣ�");
                            session.setAttribute("isback", "0");
                            session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
                            response.sendRedirect("../showinfo.jsp");
                            return;
                        }

                        //������ı��ļ�������ֱ����ʾ
                        //out.println(item.getString());
                        //�����ص��ļ�д����������Ĭ��Ŀ¼��

                        //File uploadedFile = new File(filePath + "\\" + filenm);
                        //item.write(uploadedFile);

                        //����Ĵ����ǽ��ļ���⣨�ԣ���
                        //ע���������Ļ�ȡ
                        InputStream fin = item.getInputStream();

                        ConnectionManager manager = ConnectionManager.getInstance();
                        DatabaseConnection dCon = manager.getConnection();
                        Connection con = dCon.getConnection();
                        try {
                            Statement st = con.createStatement();
                            String sql = "select * from FILEBLOB where FILETP='" + FILETP + "' and OPNO='" + OPNO + "' and FILENM='" + filenm + "' ";

                            dCon.begin();
                            if (!st.executeQuery(sql).next()) {
                                sql = "insert into FILEBLOB (FILETP, OPNO, FILENM, CONTENT) " +
                                        "values('" + FILETP + "','" + OPNO + "','" + filenm + "',empty_blob())";
                                if (st.executeUpdate(sql) < 0) {
                                    dCon.rollback();
                                    request.setAttribute("msg", "�������ݿ����");
                                    manager.releaseConnection(dCon);
                                }
                            }
                            sql = "select CONTENT from  FILEBLOB " +
                                    "where FILETP='" + FILETP + "' and OPNO='" + OPNO + "' and FILENM='" + filenm + "' for update";
                            ResultSet rs = st.executeQuery(sql);

                            if (rs.next()) {
                                //���ﲻ����oracle.sql.BLOB���ᱨClassCast �쳣
                                weblogic.jdbc.vendor.oracle.OracleThinBlob blob = (weblogic.jdbc.vendor.oracle.OracleThinBlob) rs.getBlob(1);
                                //oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(1);

                                OutputStream outStream = blob.getBinaryOutputStream();

                                byte[] b = new byte[blob.getBufferSize()];
                                int len = 0;
                                while ((len = fin.read(b)) != -1) {
                                    outStream.write(b, 0, len);
                                }

                                fin.close();
                                outStream.flush();
                                outStream.close();
                                dCon.commit();
                            }
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        finally {
                            manager.releaseConnection(dCon);
                        }
                    }
                    //��������ͨ��
                    else {
                        out.println("FieldName: " + item.getFieldName() + "<br>");
                        out.println("Value: " + item.getString() + "<br>");
                    }
                    temp += 1;
                }

                if (temp == items.size()) {
                    session.setAttribute("msg", "��Ӹ����ɹ���");
                    session.setAttribute("isback", "0");
                    //session.setAttribute("closeSelf", "1");
                    session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
                    response.sendRedirect("../showinfo.jsp");
                } else {
                    int filesize=(int)upload.getFileSizeMax()/1024/1024;
                    session.setAttribute("msg", "��Ӹ���ʧ�ܣ������������Ϊ"+filesize+"M,�����³��ԣ�����������⣬����ϵ���ǣ�");
                    session.setAttribute("isback", "0");
                    //session.setAttribute("closeSelf", "1");
                    session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
                    response.sendRedirect("../showinfo.jsp");
                }


            }
        } catch(FileUpload.FileSizeLimitExceededException e){
            session.setAttribute("msg", "�ϴ��ļ����󣬵����ļ��벻Ҫ����2M������������⣬����ϵ���ǣ�");
            session.setAttribute("isback", "0");
            //session.setAttribute("closeSelf", "1");
            session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
            response.sendRedirect("../showinfo.jsp");
        }  catch (Exception e) {
            session.setAttribute("msg", "��Ӹ���ʧ�ܣ��ļ��ܴ�С�벻Ҫ����10M�������³��ԣ�����������⣬����ϵ���ǣ�");
            session.setAttribute("isback", "0");
            session.setAttribute("closeSelf", "1");
            response.sendRedirect("../showinfo.jsp");
        }
    }
%>


</body>
</html>