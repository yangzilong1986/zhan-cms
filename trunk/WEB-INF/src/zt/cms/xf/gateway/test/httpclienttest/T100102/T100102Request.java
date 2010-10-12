package zt.cms.xf.gateway.test.httpclienttest.T100102;

import zt.cms.xf.newcms.domain.common.MsgHeader;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 17:11:06
 * To change this template use File | Settings | File Templates.
 */
public class T100102Request {
    private MsgHeader head;
    private String stdjjh;
    private String stdqch;
    private String stdjhkkr;
    private String stdkkjg;

    public MsgHeader getHead() {
        return head;
    }

    public void setHead(MsgHeader head) {
        this.head = head;
    }

    public String getStdjjh() {
        return stdjjh;
    }

    public void setStdjjh(String stdjjh) {
        this.stdjjh = stdjjh;
    }

    public String getStdqch() {
        return stdqch;
    }

    public void setStdqch(String stdqch) {
        this.stdqch = stdqch;
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
