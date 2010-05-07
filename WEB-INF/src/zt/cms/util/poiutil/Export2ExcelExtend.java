package zt.cms.util.poiutil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p/>===============================================
 * <p/>Title: Excel生成， 在EsxportExcel基础上增加根据行列输出单元格的功能  zhanrui 20090920
 * <p/>===============================================
 * <p/>Description:将页面数据转换成Excel。
 *
 */

public class Export2ExcelExtend extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HashMap excelMap = (HashMap) request.getSession().getAttribute("excelMap");//打印表头
        request.getSession().removeAttribute("excelMap");

        String filenm = (String) excelMap.get("filenm");
        String date = (String) excelMap.get("date");

        POIUtil poi = new POIUtil();
        String fileName = new String(poi.getOutFileName(filenm, date).getBytes("GBK"), "iso8859-1");
        System.out.println(poi.getOutFileName(filenm, date) + " 导出开始！！！");


        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Type", "text/html");

        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        try {
            if (poi.writeExcel(excelMap, response)) {
                System.out.println(poi.getOutFileName(filenm, date) + " 导出成功！！！");
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
     * 得到ServletInfo
     *
     * @return String
     */
    public String getServletInfo() {
        return "The Servlet export excel from web to localhost!";
    }

    /**
     * 初始化方法
     */
    public void init() throws ServletException {
    }

}