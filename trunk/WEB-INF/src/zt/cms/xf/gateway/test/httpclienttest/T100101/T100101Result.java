package zt.cms.xf.gateway.test.httpclienttest.T100101;

import zt.cms.xf.newcms.domain.common.MsgHeader;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-10
 * Time: 11:27:55
 * To change this template use File | Settings | File Templates.
 */
public class T100101Result {
    private MsgHeader head;
    private List<T100101ResultBody> records;

    public MsgHeader getHead() {
        return head;
    }

    public void setHead(MsgHeader head) {
        this.head = head;
    }

    public List<T100101ResultBody> getRecords() {
        return records;
    }

    public void setRecords(List<T100101ResultBody> records) {
        this.records = records;
    }
}
