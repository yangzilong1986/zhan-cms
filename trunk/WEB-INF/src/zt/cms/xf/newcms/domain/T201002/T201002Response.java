package zt.cms.xf.newcms.domain.T201002;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import zt.cms.xf.newcms.domain.common.MsgHeader;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-10
 * Time: 17:18:39
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("ROOT")
public class T201002Response extends MsgHeader {
    String stdsqdzt;  //���뵥״̬
    String stdkhh;  //�ͻ���
    String stdkhxm;  //�ͻ�����
    String stdzjlx;  //֤������
    String stdzjhm;  //֤������
    String stdhth;  //��ͬ��
    String stdjjh;  //��ݺ�
    String stdfkje;  //�ſ���
    String stdfkrq;  //�ſ�����
    String stddqrq;  //��������
    String stddkje;  //������
    String stdye;  //�������
    String stddkxt;  //������̬

    public String getStdsqdzt() {
        return stdsqdzt;
    }

    public void setStdsqdzt(String stdsqdzt) {
        this.stdsqdzt = stdsqdzt;
    }

    public String getStdkhh() {
        return stdkhh;
    }

    public void setStdkhh(String stdkhh) {
        this.stdkhh = stdkhh;
    }

    public String getStdkhxm() {
        return stdkhxm;
    }

    public void setStdkhxm(String stdkhxm) {
        this.stdkhxm = stdkhxm;
    }

    public String getStdzjlx() {
        return stdzjlx;
    }

    public void setStdzjlx(String stdzjlx) {
        this.stdzjlx = stdzjlx;
    }

    public String getStdzjhm() {
        return stdzjhm;
    }

    public void setStdzjhm(String stdzjhm) {
        this.stdzjhm = stdzjhm;
    }

    public String getStdhth() {
        return stdhth;
    }

    public void setStdhth(String stdhth) {
        this.stdhth = stdhth;
    }

    public String getStdjjh() {
        return stdjjh;
    }

    public void setStdjjh(String stdjjh) {
        this.stdjjh = stdjjh;
    }

    public String getStdfkje() {
        return stdfkje;
    }

    public void setStdfkje(String stdfkje) {
        this.stdfkje = stdfkje;
    }

    public String getStdfkrq() {
        return stdfkrq;
    }

    public void setStdfkrq(String stdfkrq) {
        this.stdfkrq = stdfkrq;
    }

    public String getStddqrq() {
        return stddqrq;
    }

    public void setStddqrq(String stddqrq) {
        this.stddqrq = stddqrq;
    }

    public String getStddkje() {
        return stddkje;
    }

    public void setStddkje(String stddkje) {
        this.stddkje = stddkje;
    }

    public String getStdye() {
        return stdye;
    }

    public void setStdye(String stdye) {
        this.stdye = stdye;
    }

    public String getStddkxt() {
        return stddkxt;
    }

    public void setStddkxt(String stddkxt) {
        this.stddkxt = stddkxt;
    }
}
