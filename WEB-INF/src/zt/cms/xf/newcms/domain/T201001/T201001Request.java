package zt.cms.xf.newcms.domain.T201001;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import zt.cms.xf.newcms.domain.common.MsgHeader;

/**
 * 消费信贷申请资料上传
 * User: zhanrui
 * Date: 2010-10-10
 * Time: 17:18:39
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("ROOT")
public class T201001Request extends MsgHeader {
    @XStreamAlias("LIST")
    private T201001RequestList body;

    public T201001RequestList getBody() {
        return body;
    }

    public void setBody(T201001RequestList body) {
        this.body = body;
    }
}
