package com.ebis.ebank.dx;

public class DXErrorCode {
    public static final int NO_THIS_LU        = -2000;  //�޴���ˮ��
    public static final int RCV_TOA_FAIL      = -2001;  //����TOAʧ��
    public static final int TIA_DATA_ERROR    = -2002;  //AP�������ݷǷ�(NULL)
    public static final int BTP_RESOURCE_BUSY = -2003;  //�޿��õ������ն˹�Ա
    public static final int TRANS_DEFINE_ERROR= -2004;  //�������
    public static final int TRANS_TYPE_DEFINE_ERROR = -2005; //AP������λTYPE�������
    public static final int AP_DATA_ERROR     = -3001;  //AP��ֲ����
    public static final int TRANS_MAC_ERROR   = -2006;  //��MACʧ��
    public static final int TIA_SEND_EXCEPTION= -2007;

}
