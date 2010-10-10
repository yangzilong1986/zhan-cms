package zt.cms.xf.newcms.domain.T100102;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import zt.cms.xf.newcms.domain.common.TxPkgHeader;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-10
 * Time: 17:18:39
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("ROOT")
public class T100102Response extends TxPkgHeader {
    @XStreamAlias("LIST")
    private T100102ResponseList body;

    public T100102ResponseList getBody() {
        return body;
    }

    public void setBody(T100102ResponseList body) {
        this.body = body;
    }
}
