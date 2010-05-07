package zt.cms.cm;

import com.zt.image.AdvancedImageSizer;
import com.zt.image.JpegImageWriter;
import com.zt.oreilly.servlet.MultipartRequest;
import com.zt.util.PropertyManager;
import zt.cmsi.pub.code.GLBLOBSeqNo;
import zt.cmsi.pub.code.GLBlobObjID;
import zt.platform.db.ConnectionManager;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Enumeration;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class UploadServlet extends HttpServlet {
    static final private String CONTENT_TYPE = "text/html; charset=GBK";
    int width = PropertyManager.getIntProperty("accept_image_width");
    int height = PropertyManager.getIntProperty("accept_image_height");
    String temp_path = PropertyManager.getProperty("temp_pic_path");
    float compressLevel = PropertyManager.getFloatProperty("accept_image_compress_level");
    String imageScaleMethod = PropertyManager.getProperty("image_scale_method");

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MultipartRequest req = null;
        try {
            req = new MultipartRequest(request, temp_path);
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
            request.setAttribute("msg", "IO错误，可能是上传文件大小超过1M");
            RequestDispatcher rd = request.getRequestDispatcher("/showinfo.jsp");
            rd.forward(request, response);
            return;
        }
        Enumeration es = req.getFileNames();
        //while (es.hasMoreElements()) {
        //    System.out.println("filename="+es.nextElement());
        //}
        File file = req.getFile("file");

        if (file.length() == 0) {
            request.setAttribute("msg", "文件路径不正确");
            RequestDispatcher rd = request.getRequestDispatcher("/showinfo.jsp");
            rd.forward(request, response);
            return;
        }


        Image image = ImageIO.read(file);

        if (image == null) {
            request.setAttribute("msg", "请确定正确的文件格式包括JPG,GIF,PNG");
            RequestDispatcher rd = request.getRequestDispatcher("/showinfo.jsp");
            rd.forward(request, response);
            return;
        }

        AdvancedImageSizer sizer = AdvancedImageSizer.getInstance(this.width, this.height, this.imageScaleMethod);
        BufferedImage exportImage = sizer.exportImage(image);
        File file2 = File.createTempFile("tempzoom", ".jpg", new File(temp_path));

        //ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName("jpg").next();
        //writer.write(exportImage,);

        JpegImageWriter writer = new JpegImageWriter();
        writer.writeImage(exportImage, file2, this.compressLevel);

        //ImageIO.write(exportImage,"jpg",file2);


        String str = "insert into glblob values(?,?,?)";
        Connection con = ConnectionManager.getInstance().getConnection().getConnection();
        try {
            con.setAutoCommit(false);
            int MAX_FILE_SIZE = 512 * 1000;
            PreparedStatement pst = con.prepareStatement(str);


            int objid = 0;
            int seqno;
            boolean needInit = false;
            String photoNo = req.getParameter("PHOTONO");
            String tableName = req.getParameter("TABLENAME");
            String title = new String(req.getParameter("title").getBytes("ISO8859-1"), "GBK");
            String flag = req.getParameter("flag");
            String whereCondition = req.getParameter("WHERECONDITION");
            //


            if (photoNo == null || photoNo.equals("") || photoNo.equals("0") || photoNo.equals("null")) {
                Statement stPhoto = con.createStatement();
                ResultSet photoRs = stPhoto.executeQuery("select photono from " + tableName + " where " + whereCondition);
                if (photoRs.next()) {
                    int tempPhotoNo = photoRs.getInt(1);
                    if (tempPhotoNo == 0) {
                        objid = GLBlobObjID.getNextNo();
                        if (objid < 0) {
                            request.setAttribute("msg", "系统资源忙，稍候再试！");
                            RequestDispatcher rd = request.getRequestDispatcher("/showinfo.jsp");
                            rd.forward(request, response);
                        }
                        needInit = true;
                    } else {
                        objid = tempPhotoNo;
                    }
                } else {
                    System.out.println("找不到对应的父表字段");
                }

            } else {
                objid = Integer.parseInt(photoNo);
            }
            seqno = GLBLOBSeqNo.getNextNo();

            pst.setInt(1, objid);
            pst.setInt(2, seqno);
            //Blob pic = COM.ibm.db2.app.Lob.newBlob();

            InputStream in = new FileInputStream(file2);
            pst.setBinaryStream(3, in, (int) file2.length());
            pst.execute();

            if (needInit) {
                Statement st = con.createStatement();

                String str2 = "update " + tableName + " set photono=" + objid + " where " + whereCondition;
                //System.out.println("str2:"+str2);
                st.executeUpdate(str2);
            }
            con.commit();

            file.delete();
            in.close();
            //file2.deleteOnExit();
            file2.delete();


            request.setAttribute("TABLENAME", tableName);
            request.setAttribute("WHERECONDITION", whereCondition);
            request.setAttribute("title", title);
            request.setAttribute("flag", flag);
            RequestDispatcher rd = request.getRequestDispatcher("/photo/photo.jsp");
            rd.forward(request, response);
        }
        catch (Exception ex) {
            try {
                con.rollback();
            }
            catch (SQLException ex1) {
            }


            ex.printStackTrace();
        } finally {
            try {
                con.close();
            }
            catch (SQLException ex1) {
            }
        }


        //System.out.println("file:"+file.getAbsolutePath());


    }


    //Clean up resources
    public void destroy() {
    }
}
