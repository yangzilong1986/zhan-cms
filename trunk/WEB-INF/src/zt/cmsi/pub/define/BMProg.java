//Source file: e:\\java\\zt\\cmsi\\pub\\define\\BMProg.java

package zt.cmsi.pub.define;

/**
 * ��·����Ϣ�е��йص��ó������Ϣ�ļ򵥷�װ
 *
 * @author zhouwei
 *         $Date: 2005/06/28 07:00:39 $
 * @version 1.0
 *          <p/>
 *          ��Ȩ���ൺ���칫˾
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