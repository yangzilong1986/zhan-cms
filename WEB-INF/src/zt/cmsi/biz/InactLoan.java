//Source file: e:\\java\\zt\\cmsi\\biz\\InactLoan.java

package zt.cmsi.biz;

import java.util.Calendar;

public class InactLoan {
    public boolean ifAccepted;
    public String BMNo;
    public Calendar acceptDate;
    public Integer ILStatus;
    public String adminedBy;

    public String toString() {
        return ifAccepted + "_" + BMNo + "_" + acceptDate + "_" + ILStatus + "_" + adminedBy;
    }
}
