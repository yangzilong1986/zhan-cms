package zt.cms.fcsort.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * <p/>===============================================
 * <p/>Title: 五级分类贷款WEB数据导成Excelservlet
 * <p/>===============================================
 * <p/>Description:将页面数据转换成Excel。
 *
 * @author zhengxin
 *         <p/>修改：$Author: zhengx $
 * @version $Revision: 1.2 $ $Date: 2007/05/23 06:52:27 $
 */

public class ExportExcel extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = -857567743575872355L;

    /**
     * Constructor of the object.
     */
    public ExportExcel() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doDelete method of the servlet. <br>
     * <p/>
     * This method is called when a HTTP delete request is received.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {


    }

    /**
     * The doGet method of the servlet. <br>
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("导出开始了！！！");

        HashMap printMap = (HashMap) request.getSession().getAttribute("printMap");//打印表头
        //request.getSession().removeAttribute("printMap");
        String listTH = (String) printMap.get("listTH");
        List listTR = (List) printMap.get("listTR");
        String[] titles = (String[]) printMap.get("titles");
        String[] sum = (String[]) printMap.get("sum");
        ;
        if (titles[0] == null) {
            titles[0] = "统计表";
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Type", "text/html");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(titles[0].getBytes("GBK"), "ISO8859-1") + ".xls");

        try {
            ToExcel util = new ToExcel();
            if (util.exportExcelForWeb(titles, sum, listTH, listTR, response.getOutputStream())) {
                System.out.println("导出成功！！！" + listTR.size());
            }
        } catch (Exception e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
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
        return "The Servlet  export excel from web to localhost!";
	}
	/**
	 * 初始化方法
	 */
	public void init() throws ServletException {
	}

}
