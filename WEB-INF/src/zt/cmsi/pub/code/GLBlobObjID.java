package zt.cmsi.pub.code;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class GLBlobObjID {

    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("GLBLOB", "OBJID");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("GLBLOB", "OBJID");
    }

    static public void main(String[] args) {
        System.out.print(GLBlobObjID.getNextNo());
        System.out.print(GLBlobObjID.getNextNo());
    }

}
