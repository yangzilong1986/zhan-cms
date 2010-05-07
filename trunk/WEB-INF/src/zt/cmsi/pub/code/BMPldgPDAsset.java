//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMPldgBillRedis.java

package zt.cmsi.pub.code;


public class BMPldgPDAsset extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMPLDGPDASSET", "PLEDGENO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMPLDGPDASSET", "PLEDGENO");
    }

}
