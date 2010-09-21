package zt.cms.xf.gateway.test.httpclienttest.T100102;

import zt.cms.xf.gateway.test.httpclienttest.TxPkgHeader;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 17:11:06
 * To change this template use File | Settings | File Templates.
 */
public class T100102Request {
    private TxPkgHeader head;
    private String stdjjh;
    private String stdjhkkr;
    private String stdkkjg;

    public TxPkgHeader getHead() {
        return head;
    }

    public void setHead(TxPkgHeader head) {
        this.head = head;
    }

    public String getStdjjh() {
        return stdjjh;
    }

    public void setStdjjh(String stdjjh) {
        this.stdjjh = stdjjh;
    }

    public String getStdjhkkr() {
        return stdjhkkr;
    }

    public void setStdjhkkr(String stdjhkkr) {
        this.stdjhkkr = stdjhkkr;
    }

    public String getStdkkjg() {
        return stdkkjg;
    }

    public void setStdkkjg(String stdkkjg) {
        this.stdkkjg = stdkkjg;
    }
}
