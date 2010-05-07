//$Id: SetupServlet.java,v 1.1 2007/04/28 14:08:11 liuj Exp $
package com.zt.util.setup;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author     not attributable
 *@created    2003Äê9ÔÂ2ÈÕ
 *@version    1.0
 */

public class SetupServlet
         extends HttpServlet {
    private final static String CONTENT_TYPE = "text/html; charset=GBK";


    //Initialize global variables

    /**
     *  Description of the Method
     *
     *@exception  ServletException  Description of the Exception
     */
    public void init() throws ServletException { }


    //Process the HTTP Get request

    /**
     *  Description of the Method
     *
     *@param  request               Description of the Parameter
     *@param  response              Description of the Parameter
     *@exception  ServletException  Description of the Exception
     *@exception  IOException       Description of the Exception
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String setupEvent = request.getParameter("setupEvent");
        request.setCharacterEncoding("GB2312");

        if (setupEvent == null) {
            setupEvent = "moduleList";
        }

        if (setupEvent.equals("moduleList")) {
            request.setAttribute("moduleList", SetupManager.getModules());
            this.redirect("module_list.jsp", request, response);
        }

        if (setupEvent.equals("moduleAdd")) {
            this.redirect("module_add.jsp", request, response);
        }

        if (setupEvent.equals("moduleStore")) {
            SetupManager.addModule(request.getParameter("moduleName"));
            request.setAttribute("moduleList", SetupManager.getModules());
            this.redirect("module_list.jsp", request, response);
        }
        if (setupEvent.equals("moduleDelete")) {
            SetupManager.removeModule(request.getParameter("moduleName"));
            request.setAttribute("moduleList", SetupManager.getModules());
            this.redirect("module_list.jsp", request, response);
        }

        if (setupEvent.equals("propertyList")) {
            //request.setAttribute("propertyList", SetupManager.getModules());
            String moduleName = request.getParameter("moduleName");
            request.setAttribute("moduleName", moduleName);
            request.setAttribute("propertyList",
                    SetupManager.getProperties(moduleName));
            this.redirect("property_list.jsp", request, response);
        }

        if (setupEvent.equals("propertyEdit")) {
            this.redirect("property_edit.jsp", request, response);
        }

        if (setupEvent.equals("propertyModify")) {
            String propertyName = request.getParameter("propertyName");
            SetupManager.setProperty(propertyName,
                    request.getParameter("propertyValue"));
            String moduleName = request.getParameter("propertyName").split(":")[0];
            request.setAttribute("moduleName", moduleName);
            request.setAttribute("propertyList",
                    SetupManager.getProperties(moduleName));
            this.redirect("property_list.jsp", request, response);
        }

        if (setupEvent.equals("propertyAdd")) {
            this.redirect("property_add.jsp", request, response);
        }

        if (setupEvent.equals("propertyStore")) {
            String propertyName = request.getParameter("propertyName");
            String moduleName = request.getParameter("moduleName");
            String propertyValue = request.getParameter("propertyValue");
            SetupManager.setProperty(moduleName + ":" + propertyName, propertyValue);
            request.setAttribute("moduleName", moduleName);
            request.setAttribute("propertyList",
                    SetupManager.getProperties(moduleName));
            this.redirect("property_list.jsp", request, response);
        }

        if (setupEvent.equals("propertyDelete")) {
            String propertyName = request.getParameter("propertyName");
            SetupManager.removeProperty(propertyName);

            String moduleName = propertyName.split(":")[0];
            request.setAttribute("moduleName", moduleName);
            request.setAttribute("propertyList",
                    SetupManager.getProperties(moduleName));
            this.redirect("property_list.jsp", request, response);

//      else if(setupEvent.equals(""))

//      else if(setupEvent.equals(""))

//      else if(setupEvent.equals(""))
        }
    }


    //Process the HTTP Post request
    /**
     *  Description of the Method
     *
     *@param  request               Description of the Parameter
     *@param  response              Description of the Parameter
     *@exception  ServletException  Description of the Exception
     *@exception  IOException       Description of the Exception
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }


    //Clean up resources

    /**
     *  Description of the Method
     */
    public void destroy() { }


    /**
     *  Description of the Method
     *
     *@param  url       Description of the Parameter
     *@param  request   Description of the Parameter
     *@param  response  Description of the Parameter
     */
    public void redirect(String url, HttpServletRequest request,
            HttpServletResponse response) {
        ServletContext context = this.getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/" + url);
        try {
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
