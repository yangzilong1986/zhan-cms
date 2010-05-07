package zt.cmsi.fc;

import zt.platform.cachedb.DB2_81;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;
import java.io.IOException;
import java.io.PrintWriter;

public class UserServlet extends HttpServlet {

    /**
     * 获取某个网点下的负责人或客户经理
     *
     * @param request
     * @param response
     * @param method   throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String method)
            throws ServletException, IOException {
        String tablename = request.getParameter("tablename");
        String brhid = request.getParameter("brhid");
        StringBuffer bf = new StringBuffer();
        String sql = "select loginname,username from SCUSER where brhid='" + brhid + "' and usertype<'3'";//排除系统管理员
        CachedRowSet rs;
        try {
            request.setCharacterEncoding("GBK");
            response.setContentType("text/xml;charset=GBK");//初始化保文头
            response.setHeader("Cache-Control", "no-cache");
            bf.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
            bf.append("<!DOCTYPE web-app PUBLIC \"-//Sun Microsystems, ");
            bf.append("Inc.//DTD Web Application 2.3//EN\" \"http://java.sun.com/dtd/web-app_2_3.dtd\">\n");
            bf.append("<properties>\n");
            rs = DB2_81.getRs(sql);
            bf.append("<property>\n<name>").append(new String("请选择".getBytes("GBK"), "ISO-8859-1")).append("</name>\n");
            bf.append("<value>").append("0").append("</value>\n");
            bf.append("</property>\n");
            while (rs.next()) {
                bf.append("<property>\n<name>").append(rs.getString("username")).append("</name>\n");
                bf.append("<value>").append(rs.getString("loginname")).append("</value>\n");
                bf.append("</property>\n");
            }
            bf.append("</properties>\n");
            response.setContentType("text/xml");//初始化保文头
            response.setHeader("Cache-Control", "no-cache");
            PrintWriter out = response.getWriter();
            out.println(bf.toString());
            out.close();
            //System.out.println(bf.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The doGet method of the servlet. <br>
     * <p/>
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws Exception
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, "POST");
    }

    /**
     * The doPost method of the servlet. <br>
     * <p/>
     * This method is called when a form has its tag value method equals to post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request,response,"POST");
		processRequest(request,response,"POST");
	}

}
