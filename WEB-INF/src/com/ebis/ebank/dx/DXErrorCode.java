package com.ebis.ebank.dx;

public class DXErrorCode {
    public static final int NO_THIS_LU        = -2000;  //无此流水号
    public static final int RCV_TOA_FAIL      = -2001;  //接收TOA失败
    public static final int TIA_DATA_ERROR    = -2002;  //AP设置数据非法(NULL)
    public static final int BTP_RESOURCE_BUSY = -2003;  //无可用的网点终端桂员
    public static final int TRANS_DEFINE_ERROR= -2004;  //定义错误
    public static final int TRANS_TYPE_DEFINE_ERROR = -2005; //AP交易栏位TYPE定义错误
    public static final int AP_DATA_ERROR     = -3001;  //AP赋植错误
    public static final int TRANS_MAC_ERROR   = -2006;  //算MAC失败
    public static final int TIA_SEND_EXCEPTION= -2007;

}
