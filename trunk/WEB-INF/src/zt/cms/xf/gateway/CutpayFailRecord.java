package zt.cms.xf.gateway;

/**
 * �������۲�ѯ�����Ӧ��ʧ�ܼ�¼��
 * User: zhanrui
 * Date: 2009-7-6
 * Time: 15:44:45
 * To change this template use File | Settings | File Templates.
 */
public class CutpayFailRecord {
/*
    T541-ACTNUM	�˺�	X(32)
    T541-ACTNAM	����	X(60)
    T541-REASON	ʧ��ԭ��	X(40)
    T541-TXNAMT	���׽��	9(14).99
*/
    String actnum;
    String actnam;
    String reason;
    String txnamt;
    boolean  processed;

    public String getActnum() {
        return actnum;
    }

    public void setActnum(String actnum) {
        this.actnum = actnum;
    }

    public String getActnam() {
        return actnam;
    }

    public void setActnam(String actnam) {
        this.actnam = actnam;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTxnamt() {
        return txnamt;
    }

    public void setTxnamt(String txnamt) {
        this.txnamt = txnamt;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
