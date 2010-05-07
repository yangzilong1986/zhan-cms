package zt.cms.xf.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-9-15
 * Time: 9:24:54
 * To change this template use File | Settings | File Templates.
 */
public class CommonLogTest {
    private static Log log = LogFactory.getLog(CommonLogTest.class);

    public static void main(String[] args) {
        log.error("ERROR");
        log.debug("DEBUG");
        log.warn("WARN");
        log.info("INFO");
        log.trace("TRACE");
        System.out.println("===" + log.getClass());
    }
}
