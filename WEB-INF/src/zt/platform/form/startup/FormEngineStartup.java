//Source file: e:\\java\\zt\\platform\\form\\startup\\FormEngineStartup.java

package zt.platform.form.startup;

import com.zt.util.PropertyManager;
import zt.platform.form.config.EnumerationType;
import zt.platform.form.config.FormBeanManager;
import zt.platform.form.config.TableBeanManager;

/**
 * @author ÇëÌæ»»
 * @version 1.0
 */
public class FormEngineStartup {

    /**
     * @param argv
     * @roseuid 3F73B39B0316
     */

    public static void init() {
        TableBeanManager.getTable("testtbl");
        FormBeanManager.getForm("000001");
        EnumerationType.getEnu("");
        PropertyManager.getProperty("-100");
        MemoryManager mm = new MemoryManager();
        mm.start();
    }

    public static void main(String[] argv) {
        init();
    }
}
