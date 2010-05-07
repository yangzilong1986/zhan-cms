package com.ebis.ebank.dx;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class InitServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=GBK";
    //Initialize global variables
    public void init() throws ServletException {

    }
    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    //Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    //Clean up resources
    public void destroy() {
    }
}