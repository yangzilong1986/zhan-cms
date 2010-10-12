package zt.cms.xf.gateway.test.httpclienttest.xstreamTest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import zt.cms.xf.newcms.domain.common.MsgHeader;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-10
 * Time: 15:28:01
 * To change this template use File | Settings | File Templates.
 */

@XStreamAlias("ROOT")
public class RendezvousMessage extends MsgHeader {


//	private int messageType;
//    @XStreamAlias("head")
//    private T001 t001;
//    @XStreamImplicit(itemFieldName="ROWS")
//	private List<String> content;

    @XStreamAlias("LIST")
    private T001 t001;
	public RendezvousMessage(int messageType, String ... content) {
//		this.messageType = messageType;
//        this.t001 = new T001();
//        this.t001.setId("001");
//        this.t001.setName("name001");
//        this.t001.setTxnname("txnname001");
        super.setStd400acur("aaa");
        super.setStd400aqid("1111");
/*
        this.t001 = new T001();
        this.t001.add("aaaaaaaaaa");
        this.t001.add("bbbbbbbbbbb");
        this.t001.add("ccccccccccc");
*/
//		this.content = Arrays.asList(content);
	}
}
