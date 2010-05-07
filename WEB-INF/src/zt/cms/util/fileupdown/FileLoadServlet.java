package zt.cms.util.fileupdown;

import zt.platform.db.ConnectionManager;
import zt.platform.db.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class FileLoadServlet
        extends HttpServlet {
    //static final private String CONTENT_TYPE = "image/jpg";

    //Initialize global variables

    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        //response.setContentType(CONTENT_TYPE);
        response.setContentType("application/x-msdownload");
        int objid = Integer.parseInt(request.getParameter("objid"));
        //int seqno = Integer.parseInt(request.getParameter("seqno"));
        DatabaseConnection dCon = ConnectionManager.getInstance().getConnection();
        Connection con = dCon.getConnection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from FILEBLOB where objid = " + objid);
            if (rs.next()) {
                String fileName = new String(rs.getString("filenm").getBytes("GBK"), "iso8859-1");
                response.setHeader("Content-disposition", "attachment;filename=" + fileName);
                Blob content = rs.getBlob("content");

                //byte[] b = new byte[64];
                ServletOutputStream out = response.getOutputStream();
                //out.write();
                InputStream in = content.getBinaryStream();
                int x;
                while ((x = in.read()) != -1) {
                    out.write(x);
                }
            }

        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            ConnectionManager.getInstance().releaseConnection(dCon);
        }
    }

    //Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }

    //Process the HTTP Put request
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
    }

    //Clean up resources
    public void destroy() {
    }

}
