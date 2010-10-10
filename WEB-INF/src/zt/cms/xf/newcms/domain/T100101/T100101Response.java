package zt.cms.xf.newcms.domain.T100101;

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
public class T100101Response extends TxPkgHeader {
    @XStreamAlias("LIST")
    private T100101ResponseList body;
}
