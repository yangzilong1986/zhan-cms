package zt.cms.xf.fd.actnoinfo;

import com.zt.util.PropertyManager;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-21
 * Time: 17:52:06
 * To change this template use File | Settings | File Templates.
 */
public class StaticClassTmp {
    private static String hostip = PropertyManager.getProperty("SBS_HOSTIP");
    private static int port = Integer.parseInt(PropertyManager.getProperty("SBS_HOSTPORT"));

    public static String getHostip(){
        return hostip;
    }
    public static int getPort(){
        return port;
    }
}
