package zt.cms.cm.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import zt.platform.db.*;
import java.sql.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ImageLoadServlet
    extends HttpServlet {
    static final private String CONTENT_TYPE = "image/jpg";

    //Initialize global variables
    public void init() throws ServletException
    {
    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException
    {
        response.setContentType(CONTENT_TYPE);
        int objid = Integer.parseInt(request.getParameter("objid"));
        int seqno = Integer.parseInt(request.getParameter("seqno"));
        DatabaseConnection dCon = ConnectionManager.getInstance().getConnection();
        Connection con = dCon.getConnection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from GLBLOB where objid = " + objid +
                                           " and seqno=" + seqno + " ");
            if (rs.next()) {
                Blob content = rs.getBlob(3);

                //byte[] b = new byte[64];
                ServletOutputStream out = response.getOutputStream();
                //out.write();
                InputStream in = content.getBinaryStream();
                int x;
                while((x=in.read())!=-1){
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
        ServletException, IOException
    {
        doGet(request, response);
    }

    //Process the HTTP Put request
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException
    {
    }

    //Clean up resources
    public void destroy()
    {
    }

    public static Object[] findPicByObjid(int objid){
        DatabaseConnection dCon = ConnectionManager.getInstance().getConnection();
            Connection con = dCon.getConnection();
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select seqno from GLBLOB where objid = " + objid );
                Collection seqnos=new Vector();
                while (rs.next()) {
                    seqnos.add(rs.getObject(1));
                }
                return seqnos.toArray();

            }
            catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
            finally {
                ConnectionManager.getInstance().releaseConnection(dCon);
            }

    }

    public static int findObjid(String tableName,String whereCondition){
    DatabaseConnection dCon = ConnectionManager.getInstance().getConnection();
        Connection con = dCon.getConnection();
        try {
            Statement st = con.createStatement();
            String str = "select photono from "+tableName+" where "+whereCondition;
            //System.out.println("str==="+str);
            ResultSet rs = st.executeQuery( str );

            if (rs.next()) {
               return rs.getInt(1);
            }else{
                return -1;
            }

        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
        finally {
            ConnectionManager.getInstance().releaseConnection(dCon);
        }

}

}
