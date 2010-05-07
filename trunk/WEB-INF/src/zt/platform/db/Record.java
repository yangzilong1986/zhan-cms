/**
 * Copyright 2003 ZhongTian, Inc. All rights reserved.
 *
 * Zhongtian PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * $Id: Record.java,v 1.1 2007/04/28 14:08:36 liuj Exp $
 * File:Record.java
 * Date Author Changes
 * March 5 2003 wangdeliang Created
 */

package zt.platform.db;

import java.util.ArrayList;
import java.util.List;

/**
 * ¼ÇÂ¼Àà
 *
 * @author <a href="mailto:wangdl@zhongtian.biz">WangDeLiang</a>
 * @version $Revision: 1.1 $ $Date: 2007/04/28 14:08:36 $
 */

public class Record {
    private List fields;

    /**
     * @param records
     * @roseuid 3E5D82BD0332
     */
    public Record(java.util.List fields) {
        this.fields = fields;
    }

    /**
     * @roseuid 3E5D82A700F5
     */
    public Record() {
        fields = new ArrayList();
    }

    /**
     * @param idx
     * @return boolean
     * @roseuid 3E5D80E102D3
     */
    public void insert(int idx) {
        fields.add(idx, null);
    }

    /**
     * @param idx
     * @return boolean
     * @roseuid 3E5D80F6017F
     */
    public void delete(int idx) {
        fields.remove(idx);
    }

    /**
     * @param idx
     * @return Object
     * @roseuid 3E5D82640208
     */
    public Object get(int idx) {
        return fields.get(idx);
    }

    /**
     * @param idx
     * @param value
     * @roseuid 3E5D828F010F
     */
    public void set(int idx, Object value) {
        fields.set(idx, value);
    }
}
