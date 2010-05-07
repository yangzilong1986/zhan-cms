package zt.platform.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
//import java.util.logging.Logger;


public class Debug {

//    public static Logger logger = Logger.getLogger("zt.cmsi.biz");
    private static Log logger = LogFactory.getLog(Debug.class);

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_WARNING = 1;
    public static final int TYPE_ERROR = 2;
    public static final int TYPE_SQL = 3;
    public static final boolean isDebugMode = true;

    private static boolean isDebug = true;


    public static void debug(String message) {
        if (isDebug) {
            System.out.println(message);
            logger.debug(message);
        }
    }

    public static void debug(Exception e) {
        ByteArrayOutputStream sout = new ByteArrayOutputStream(500);
        PrintStream ps = new PrintStream(sout);

        e.printStackTrace(ps);
//        logger.severe(sout.toString());
        logger.error(sout.toString());
    }

    /**
     * @param typ     : MESSAGE or ERROR or WARNING
     * @param message
     */
//    public static void debug(int typ, String message) {
//        if ( isDebug ) {
//          if(typ == Debug.TYPE_ERROR) System.out.println("[ERROR]" +message);
//            else if(typ == Debug.TYPE_WARNING) System.out.println("[WARNING]" +message);
//              else if(typ == Debug.TYPE_MESSAGE) System.out.println("[MESSAGE]" +message);
//                else if(typ == Debug.TYPE_SQL) System.out.println("[SQL]" +message);
//                  else System.out.println("[OTHER]" +message);
//
//        }
//    }
    public static void debug(int typ, String message) {
//        if (isDebug) {
//            if (typ == Debug.TYPE_ERROR) logger.severe(message);
//            else if (typ == Debug.TYPE_WARNING) logger.warning(message);
//            else if (typ == Debug.TYPE_MESSAGE) logger.info(message);
//            else if (typ == Debug.TYPE_SQL) logger.info("[SQL]" + message);
//            else logger.info(message);
//        }
        if (isDebug) {
//            if (typ == Debug.TYPE_ERROR) logger.severe(message);
            if (typ == Debug.TYPE_ERROR) logger.error(message);
            else if (typ == Debug.TYPE_WARNING) logger.warn(message);
            else if (typ == Debug.TYPE_MESSAGE) logger.info(message);
            else if (typ == Debug.TYPE_SQL) logger.info("[SQL]" + message);
            else logger.info(message);
        }
    }

    public static void print(String message) {
        if (isDebug) {
            System.out.print(message);
        }
    }

    public static void println(String message) {
        if (isDebug) {
            System.out.println(message);
        }
    }

}
