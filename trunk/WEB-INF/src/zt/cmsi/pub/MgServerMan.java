package zt.cmsi.pub;

import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.runtime.ClusterRuntimeMBean;
import zt.platform.utils.Debug;

import javax.naming.Context;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class MgServerMan {

    static public Vector getActiveMgServers() {
        Vector data = new Vector();
        MBeanHome home = null;
        ServerMBean server = null;
        ClusterRuntimeMBean clusterRuntime = null;
        String[] aliveServerArray = null;

        Set mbeanSet = null;
        Iterator mbeanIterator = null;

        try {
            Environment env = new Environment();
            Context ctx = env.getInitialContext();
            home = (MBeanHome) ctx.lookup("weblogic.management.adminhome");
        } catch (Exception e) {
            Debug.debug(e);
            return null;
        }


        try {

            mbeanSet = home.getMBeansByType("ClusterRuntime");
            mbeanIterator = mbeanSet.iterator();
            while (mbeanIterator.hasNext()) {
                // Retrieving one ClusterRuntime MBean from the list.
                clusterRuntime = (ClusterRuntimeMBean) mbeanIterator.next();
                // Get the name of the ClusterRuntime MBean.
                //name = clusterRuntime.getName();
                // Using the current ClusterRuntimeMBean to retrieve the number
                // of servers in the cluster.
                // Retrieving the names of servers in the cluster.
                aliveServerArray = clusterRuntime.getServerNames();
                break;
            }
        } catch (Exception e) {
            Debug.debug(e);
            return null;
        }

        if (aliveServerArray == null) return null;

        String name, address, port;
        try {
            mbeanSet = home.getMBeansByType("Server");
            mbeanIterator = mbeanSet.iterator();
            while (mbeanIterator.hasNext()) {
                server = (ServerMBean) mbeanIterator.next();
                name = server.getName();
                for (int i = 0; i < aliveServerArray.length; i++) {
                    if (name.compareToIgnoreCase(aliveServerArray[i]) == 0) {
                        MgServer sss = new MgServer();
                        sss.setServerName(name);
                        sss.setListenAddress(server.getListenAddress());
                        sss.setListenPort("" + server.getListenPort());
                        data.add(sss);
                        Debug.debug(Debug.TYPE_MESSAGE, "Active MG Server:" + name + " Address:" + server.getListenAddress());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Debug.debug(e);
            return null;
        }

        return data;
    }

    public static void main(String[][] args) {
        Vector data = MgServerMan.getActiveMgServers();
        {
            for (Iterator iter = data.iterator(); iter.hasNext();) {
                MgServer item = (MgServer) iter.next();
                System.out.println(item.getServerName() + item.getListenAddress() + item.getListenPort());
            }
        }

    }
}