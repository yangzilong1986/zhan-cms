package zt.platform.user;

import zt.platform.form.config.SystemAttributeNames;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class UserListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent se) {
    }

    /**
     * Notification that a session was invalidated.
     *
     * @param se the notification event
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("session destoryed");
        UserManager um = (UserManager) se.getSession().getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (um != null) {
            try {
                synchronized (UserManager.users) {
                    UserManager.users.remove(um);
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
