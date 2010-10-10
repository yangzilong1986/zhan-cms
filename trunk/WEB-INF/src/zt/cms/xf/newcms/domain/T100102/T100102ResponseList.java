package zt.cms.xf.newcms.domain.T100102;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-10
 * Time: 17:32:16
 * To change this template use File | Settings | File Templates.
 */
public class T100102ResponseList {
    @XStreamImplicit(itemFieldName="ROWS")
	private List<T100102ResponseRecord> content= new ArrayList();

    public List getContent() {
        return this.content;
    }

    public void add(T100102ResponseRecord record) {
        this.content.add(record);
    }

}
