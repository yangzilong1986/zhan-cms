//Source file: e:\\java\\zt\\cmsi\\pub\\code\\SerialNumber.java

package zt.cmsi.pub.code;

import zt.cms.pub.code.SerialNumber;

public class SerialNumber2 {

    /**
     * ��ȡ��һ�����к�
     *
     * @param tblName
     * @param fldName
     * @return int
     * @roseuid 3FD02580012A
     */
    protected static long getNextSN(String tblName, String fldName) {
        return SerialNumber.getNextSN(tblName, fldName);
    }

    /**
     * ��ȡ��ǰ���к�
     *
     * @param tblName
     * @param fldName
     * @return int
     * @roseuid 3FD02580018E
     */
    protected static long getCurSN(String tblName, String fldName) {
        return SerialNumber.getCurSN(tblName, fldName);
    }
}
