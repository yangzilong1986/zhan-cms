package zt.cms.xf.newcms.domain.common;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 13:24:32
 * To change this template use File | Settings | File Templates.
 */

/*
    ����	��ǩ��	����	˵��
    ��������	stdmsgtype             	4
        0100 ��ѯ������
        0200 ����������
    ������	std400trcd	6	���𷽽�����
        ���𷽱���
        100101-�����¼��ѯ
        100102������������
    �������	stdprocode	6	����д
    ���𷽱�־	std400aqid	1	1-����
    2-�����Ŵ�
    3-���ڿۿ�
    �̻����	stdmercno	15	����д
    �ն�����	stdtermtyp	1	����д
    �ն˺� 	stdtermid	8	����д
    ���׹�Ա	std400tlno	10	���𷽹�Ա��
    �ʺ�1	stdpriacno	19	����д
    ����	stdpindata	16	����д
    ��������	stdlocdate	8	��������
    ����ʱ��	stdloctime	6	����ʱ��
    ǰ̨��ˮ��	stdtermtrc	6	���ͷ���д�����첻�ظ�
    ��Ȩ��Ա	std400autl	10	����д
    ��Ȩ��	stdauthid	6	����д
    ��Ȩ��Ա����	std400aups	16	����д
    ��������	std400trdt	8	����д
    ����������ˮ��	stdrefnum	12	0
    ��������                	stdsetdate	8	����д
    ��Ա���׺�                	std400trno	14	����д
    ��Ϣ��ʶ	std400mgid	7	Ӧ����д
    ���׳ɹ�����AAAAAAA��
    �ظ�����	std400acur	2	00

 */
public class TxPkgHeader {
    String stdmsgtype = "";
    String std400trcd = "";
    String stdprocode = "";
    String std400aqid = "";
    String stdmercno = "";
    String stdtermtyp = "";
    String stdtermid = "";
    String std400tlno = "";
    String stdpriacno = "";
    String stdpindata = "";
    String stdlocdate = "";
    String stdloctime = "";
    String stdtermtrc = "";
    String std400autl = "";
    String stdauthid = "";
    String std400aups = "";
    String std400trdt = "";
    String stdrefnum = "0";
    String stdsetdate = "";
    String std400trno = "";
    String std400mgid = "";
    String std400acur = "00";


    public String getStdmsgtype() {
        return stdmsgtype;
    }

    public void setStdmsgtype(String stdmsgtype) {
        this.stdmsgtype = stdmsgtype;
    }

    public String getStd400trcd() {
        return std400trcd;
    }

    public void setStd400trcd(String std400trcd) {
        this.std400trcd = std400trcd;
    }

    public String getStdprocode() {
        return stdprocode;
    }

    public void setStdprocode(String stdprocode) {
        this.stdprocode = stdprocode;
    }

    public String getStd400aqid() {
        return std400aqid;
    }

    public void setStd400aqid(String std400aqid) {
        this.std400aqid = std400aqid;
    }

    public String getStdmercno() {
        return stdmercno;
    }

    public void setStdmercno(String stdmercno) {
        this.stdmercno = stdmercno;
    }

    public String getStdtermtyp() {
        return stdtermtyp;
    }

    public void setStdtermtyp(String stdtermtyp) {
        this.stdtermtyp = stdtermtyp;
    }

    public String getStdtermid() {
        return stdtermid;
    }

    public void setStdtermid(String stdtermid) {
        this.stdtermid = stdtermid;
    }

    public String getStd400tlno() {
        return std400tlno;
    }

    public void setStd400tlno(String std400tlno) {
        this.std400tlno = std400tlno;
    }

    public String getStdpriacno() {
        return stdpriacno;
    }

    public void setStdpriacno(String stdpriacno) {
        this.stdpriacno = stdpriacno;
    }

    public String getStdpindata() {
        return stdpindata;
    }

    public void setStdpindata(String stdpindata) {
        this.stdpindata = stdpindata;
    }

    public String getStdlocdate() {
        return stdlocdate;
    }

    public void setStdlocdate(String stdlocdate) {
        this.stdlocdate = stdlocdate;
    }

    public String getStdloctime() {
        return stdloctime;
    }

    public void setStdloctime(String stdloctime) {
        this.stdloctime = stdloctime;
    }

    public String getStdtermtrc() {
        return stdtermtrc;
    }

    public void setStdtermtrc(String stdtermtrc) {
        this.stdtermtrc = stdtermtrc;
    }

    public String getStd400autl() {
        return std400autl;
    }

    public void setStd400autl(String std400autl) {
        this.std400autl = std400autl;
    }

    public String getStdauthid() {
        return stdauthid;
    }

    public void setStdauthid(String stdauthid) {
        this.stdauthid = stdauthid;
    }

    public String getStd400aups() {
        return std400aups;
    }

    public void setStd400aups(String std400aups) {
        this.std400aups = std400aups;
    }

    public String getStd400trdt() {
        return std400trdt;
    }

    public void setStd400trdt(String std400trdt) {
        this.std400trdt = std400trdt;
    }

    public String getStdrefnum() {
        return stdrefnum;
    }

    public void setStdrefnum(String stdrefnum) {
        this.stdrefnum = stdrefnum;
    }

    public String getStdsetdate() {
        return stdsetdate;
    }

    public void setStdsetdate(String stdsetdate) {
        this.stdsetdate = stdsetdate;
    }

    public String getStd400trno() {
        return std400trno;
    }

    public void setStd400trno(String std400trno) {
        this.std400trno = std400trno;
    }

    public String getStd400mgid() {
        return std400mgid;
    }

    public void setStd400mgid(String std400mgid) {
        this.std400mgid = std400mgid;
    }

    public String getStd400acur() {
        return std400acur;
    }

    public void setStd400acur(String std400acur) {
        this.std400acur = std400acur;
    }
}
