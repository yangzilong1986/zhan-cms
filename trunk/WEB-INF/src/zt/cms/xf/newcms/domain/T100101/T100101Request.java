package zt.cms.xf.newcms.domain.T100101;

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
public class T100101Request extends MsgHeader {
/*
    @XStreamAlias("LIST")
    private T100101RequestList body;

    public T100101RequestList getBody() {
        return body;
    }

    public void setBody(T100101RequestList body) {
        this.body = body;
    }
*/
    //查询类型 1：房贷  2：消费信贷
    private String stdcxlx;

    public String getStdcxlx() {
        return stdcxlx;
    }

    public void setStdcxlx(String stdcxlx) {
        this.stdcxlx = stdcxlx;
    }
}
