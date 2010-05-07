package zt.cms.util.poiutil;

import zt.platform.utils.Debug;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p/>===============================================
 * <p/>Title: �弶�������WEB���ݵ���Excelservlet
 * <p/>===============================================
 * <p/>Description:��ҳ������ת����Excel��
 *
 * @author zhengxin
 *         <p/>�޸ģ�$Author: zhengx $
 * @version $Revision: 1.2 $ $Date: 2007/05/23 06:52:27 $
 */

public class ExportExcel extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HashMap excelMap = (HashMap) request.getSession().getAttribute("excelMap");//��ӡ��ͷ
        request.getSession().removeAttribute("excelMap");

        String filenm = (String) excelMap.get("filenm");
        String date = (String) excelMap.get("date");

        //TODO:�����������Ϸ��� ����Ϊ�յ�

        POIUtil poi = new POIUtil();

        //200909 zhanrui ������Ӹ�����Ϣ����
        IWriteOtherInfos writeinfo = (IWriteOtherInfos) excelMap.get("WRITEINFO");
        if (writeinfo != null) {
            poi.setOtherinfo(writeinfo);
        }

        String fileName = new String(poi.getOutFileName(filenm, date).getBytes("GBK"), "iso8859-1");
        System.out.println(poi.getOutFileName(filenm, date) + " ������ʼ������");


        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Type", "text/html");

        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        try {
            if (poi.writeExcel(excelMap, response)) {
                System.out.println(poi.getOutFileName(filenm, date) + " �����ɹ�������");
            }
        } catch (Exception e) {
            Debug.debug(e);
//            response.setContentType("text/html;charset=gb2312");
//            response.setHeader("Content-Type", "text/html");
//            response.setHeader("Content-disposition", "  ");
            response.reset();
            request.setAttribute("msg", "Excel���ɴ���");
            request.setAttribute("flag", "0");
            request.setAttribute("isback", "0");
//            request.setAttribute("funcdel", "history.go(-1)");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/showinfo.jsp");
            rd.forward(request, response);
        }
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws javax.servlet.ServletException if an error occurred
     * @throws java.io.IOException            if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }

    /**
     * �õ�ServletInfo
     *
     * @return String
     */
    public String getServletInfo() {
        return "The Servlet export excel from web to localhost!";
    }

    /**
     * ��ʼ������
     */
    public void init() throws ServletException {
    }

}