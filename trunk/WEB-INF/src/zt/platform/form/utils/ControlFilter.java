package zt.platform.form.utils;

import com.zt.util.PropertyManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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

public class ControlFilter implements Filter {
    public ControlFilter() {
    }

    private long intvals = PropertyManager.getIntProperty("AccessIntervals");

    public void init(FilterConfig filterConfig) throws ServletException {
        /**@todo Implement this javax.servlet.Filter method*/
        System.out.println("ControlFilter init");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /**@todo Implement this javax.servlet.Filter method*/
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Enumeration e = req.getHeaderNames();


        Long lastAccessTime = (Long) session.getAttribute("lastAccessTime");
        String referer = req.getHeader("Referer");
        boolean isHomeReferer = false;
        Object o = req.getAttribute("FilterRecorded");
        //System.out.println("Object==="+o);


        if (referer != null && (referer.indexOf("home.jsp") != -1)) {
            isHomeReferer = true;
        }
        if (o != null) {
            isHomeReferer = true;
        }

        long now = System.currentTimeMillis();
        //System.out.println("URI:"+req.getRequestURI()+"------------Referer:"+referer);
        if (!isHomeReferer) {
            if (lastAccessTime != null) {
                if (now - lastAccessTime.longValue() < intvals) {
                    RequestDispatcher rd = req.getRequestDispatcher("/tooquick.html");
                    rd.forward(request, response);
                    //System.out.println("too quick");
                } else {
                    //System.out.println("not quick");
                }
            }
            session.setAttribute("lastAccessTime", new Long(now));
        }

        req.setAttribute("FilterRecorded", "true");

        chain.doFilter(request, response);
    }

    public void destroy() {
        /**@todo Implement this javax.servlet.Filter method*/
        System.out.println("ControlFilter destroy");
    }

}
