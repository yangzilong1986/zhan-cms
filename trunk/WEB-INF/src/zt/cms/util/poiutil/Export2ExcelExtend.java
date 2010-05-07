package zt.cms.util.poiutil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p/>===============================================
 * <p/>Title: Excel���ɣ� ��EsxportExcel���������Ӹ������������Ԫ��Ĺ���  zhanrui 20090920
 * <p/>===============================================
 * <p/>Description:��ҳ������ת����Excel��
 *
 */

public class Export2ExcelExtend extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HashMap excelMap = (HashMap) request.getSession().getAttribute("excelMap");//��ӡ��ͷ
        request.getSession().removeAttribute("excelMap");

        String filenm = (String) excelMap.get("filenm");
        String date = (String) excelMap.get("date");

        POIUtil poi = new POIUtil();
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
            e.printStackTrace();
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