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
    String FILETP = request.getParameter("FILETP");              //文件相关的业务类别
    String OPNO = request.getParameter("OPNO");                  //对象业务号
    String OBJID = request.getParameter("OBJID");                //对象唯一标识

    if (FILETP == null || OPNO == null) {
        session.setAttribute("msg", "上传功能出错，请联系我们！");
        session.setAttribute("isback", "0");
        session.setAttribute("closeSelf", "1");
        response.sendRedirect("../showinfo.jsp");
    }

    if (OBJID != null) {//删除附件
        zt.platform.cachedb.ConnectionManager manager = zt.platform.cachedb.ConnectionManager.getInstance();
        try {
            String[] sql = new String[1];
            sql[0] = "delete from FILEBLOB where objid='" + OBJID + "'";

            if (manager.execBatch(sql)) {
                session.setAttribute("msg", "删除附件成功！");
                session.setAttribute("isback", "0");
                //session.setAttribute("closeSelf", "1");
                session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
                response.sendRedirect("../showinfo.jsp");
            } else {
                session.setAttribute("msg", "删除附件失败，请再次尝试，如仍有问题，请联系我们！");
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
                //存目录
                String dir = "\\temp";
                //存绝对路径
                String rootPath = "C://";
                //存相对路径
                //String rootPath = getServletConfig().getServletContext().getRealPath("/") ;
                String filePath = rootPath + dir;
                //设置成功标志
                int temp = 0;


                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(1000 * 1024); // 设置保存到内存中的大小：1M
                // 建立一个新的Upload对象
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setSizeMax(10 * 1024 * 1024);// 设置最大上传文件的大小10M
                upload.setFileSizeMax(2 * 1024 * 1024); // 设置单个最大上传文件的大小2M

                // 分析request中的传来的文件流，返回Item的集合，
                // 轮询Items，如果不是表单域，就是一个文件对象。
                List items = upload.parseRequest(ctx);//解析请求里的上传文件单元
                Iterator iter = items.iterator();

                String filenm = "";
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();

                    //如果是文件对象
                    if (!item.isFormField()) {
                        filenm = FILETP + OPNO + "_" + item.getName().substring(item.getName().lastIndexOf("\\") + 1, item.getName().length());
                        String  extnm = filenm.substring(filenm.length()-3,filenm.length());
                        if (extnm.equalsIgnoreCase("jsp")
                                ||extnm.equalsIgnoreCase("xls")
                                ||extnm.equalsIgnoreCase("bat")
                                ||extnm.equalsIgnoreCase("exe")
                                ||extnm.equalsIgnoreCase("com")) {
                            session.setAttribute("msg", "上传文件类型不在允许范围内！");
                            session.setAttribute("isback", "0");
                            session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
                            response.sendRedirect("../showinfo.jsp");
                            return;
                        }

                        //如果是文本文件，可以直接显示
                        //out.println(item.getString());
                        //将上载的文件写到服务器的默认目录下

                        //File uploadedFile = new File(filePath + "\\" + filenm);
                        //item.write(uploadedFile);

                        //下面的代码是将文件入库（略）：
                        //注意输入流的获取
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
                                    request.setAttribute("msg", "发生数据库错误");
                                    manager.releaseConnection(dCon);
                                }
                            }
                            sql = "select CONTENT from  FILEBLOB " +
                                    "where FILETP='" + FILETP + "' and OPNO='" + OPNO + "' and FILENM='" + filenm + "' for update";
                            ResultSet rs = st.executeQuery(sql);

                            if (rs.next()) {
                                //这里不能用oracle.sql.BLOB，会报ClassCast 异常
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
                    //否则是普通表单
                    else {
                        out.println("FieldName: " + item.getFieldName() + "<br>");
                        out.println("Value: " + item.getString() + "<br>");
                    }
                    temp += 1;
                }

                if (temp == items.size()) {
                    session.setAttribute("msg", "添加附件成功！");
                    session.setAttribute("isback", "0");
                    //session.setAttribute("closeSelf", "1");
                    session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
                    response.sendRedirect("../showinfo.jsp");
                } else {
                    int filesize=(int)upload.getFileSizeMax()/1024/1024;
                    session.setAttribute("msg", "添加附件失败，单个附件最大为"+filesize+"M,请重新尝试，如果仍有问题，请联系我们！");
                    session.setAttribute("isback", "0");
                    //session.setAttribute("closeSelf", "1");
                    session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
                    response.sendRedirect("../showinfo.jsp");
                }


            }
        } catch(FileUpload.FileSizeLimitExceededException e){
            session.setAttribute("msg", "上传文件过大，单个文件请不要大于2M，如果仍有问题，请联系我们！");
            session.setAttribute("isback", "0");
            //session.setAttribute("closeSelf", "1");
            session.setAttribute("goUrl", "./fileupdown/fileup.jsp?OPNO=" + OPNO + "&FILETP=APP");
            response.sendRedirect("../showinfo.jsp");
        }  catch (Exception e) {
            session.setAttribute("msg", "添加附件失败，文件总大小请不要大于10M，请重新尝试，如果仍有问题，请联系我们！");
            session.setAttribute("isback", "0");
            session.setAttribute("closeSelf", "1");
            response.sendRedirect("../showinfo.jsp");
        }
    }
%>


</body>
</html>