//Source file: e:\\java\\zt\\cmsi\\pub\\define\\BMProg.java

package zt.cmsi.pub.define;

/**
 * 对路由信息中的有关调用程序的信息的简单封装
 *
 * @author zhouwei
 *         $Date: 2005/06/28 07:00:39 $
 * @version 1.0
 *          <p/>
 *          版权：青岛中天公司
 */

public class BMProg {
    protected int progType;
    protected String progName;

    public String getProgName() {
        return progName;
    }

    public void setProgName(String progName) {
        this.progName = progName;
    }

    public int getProgType() {
        return progType;
    }

    public void setProgType(int progType) {
        this.progType = progType;
    }

    public String toString() {
        return "Prog :\n" + "name=" + getProgName() + "\n" + "type=" + getProgType();
    }

    public boolean isJsp() {
        if (getProgType() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isForm() {
        if (getProgType() == 2) {
            return true;
        } else {
            return false;
        }
    }


}