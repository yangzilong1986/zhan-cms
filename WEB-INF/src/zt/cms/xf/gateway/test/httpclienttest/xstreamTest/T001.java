package zt.cms.xf.gateway.test.httpclienttest.xstreamTest;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-10
 * Time: 15:50:26
 * To change this template use File | Settings | File Templates.
 */
public class T001  {

    @XStreamImplicit(itemFieldName="ROWS")
	private List<String> content= new ArrayList();

    public List getContent() {
        return this.content;
    }

    public void add(String txnname) {
        this.content.add(txnname);
    }
}
