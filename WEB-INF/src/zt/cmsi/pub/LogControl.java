package zt.cmsi.pub;


import com.zt.util.setup.SetupManager;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.session.SessionContext;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.User;
import zt.platform.user.UserManager;
import zt.platform.user.VirtualUser;
import zt.platform.utils.Debug;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class LogControl {

    static boolean disableMLogin = false;
    static LogControl inst = null;
    static List users = null;
    static long timenow = 0;

    public static LogControl getInstance() {
        if (inst == null)
            inst = new LogControl();
        return inst;
    }

    /**
     * LogControl
     */
    public LogControl() {
        String temp = SetupManager.getProperty(confitem.SYS_MODULE + ":" + confitem.SYS_MLOGIN);
        if (temp != null) {
            disableMLogin = (SetupManager.getIntProperty(confitem.SYS_MODULE + ":" + confitem.SYS_MLOGIN)) == 1 ? true : false;
        }
    }

    public boolean checkErrorLoginOverflow(String userid) {
        return false;

    }
    public boolean checkIfLogged(String userid, PageContext pageContext) {
        if (this.disableMLogin == false) return false;
        if (System.currentTimeMillis() - this.timenow > 4000) this.refresh(pageContext);

        for (int i = 0; i < users.size(); i++) {
            VirtualUser vu = (VirtualUser) users.get(i);
//      System.out.println("-------curr user="+userid+" list="+vu.getUsername());
            if (userid.compareToIgnoreCase(vu.getUsername()) == 0)
                return true;
        }
        return false;
    }

    private void refresh(PageContext pageContext) {
        users = new ArrayList();
        this.timenow = System.currentTimeMillis();
        try {
            WebAppServletContext ctx = (WebAppServletContext) pageContext.getServletConfig().getServletContext();
            SessionContext ctx1 = ctx.getSessionContext();
            ctx1.deleteInvalidSessions();
            //ctx1.setTimeOut(2);
            Hashtable set = ctx1.getOpenSessions();
            for (Enumeration elements = set.elements(); elements.hasMoreElements();) {
                HttpSession session1 = (HttpSession) elements.nextElement();
                UserManager um = (UserManager) session1.getAttribute(SystemAttributeNames.USER_INFO_NAME);
                if (um != null) {
                    User user = um.getUser();
                    if (user != null) {
                        try {
                            String username = user.getUsername();
                            String userid = user.getUserid();
                            VirtualUser vu = new VirtualUser(userid, username, null);
                            users.add(vu);
                        }
                        catch (Exception e) {
                            Debug.debug(e);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Debug.debug(ex);
        }
    }
}
