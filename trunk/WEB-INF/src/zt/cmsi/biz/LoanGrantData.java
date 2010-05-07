package zt.cmsi.biz;

import zt.cmsi.client.CMClient;

import java.util.Calendar;

public class LoanGrantData {
    public String bmNo = null;
    public String brhID = null;
    public int authorizedStatus;
    public Calendar beginDate = null;
    public Calendar endDate = null;
    public CMClient cmClient = null;
    public UpToDateApp upToDateApp = null;
}