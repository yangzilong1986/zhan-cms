/**
 * Copyright 2003 ZhongTian, Inc. All rights reserved.
 *
 * Zhongtian PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * $Id: NoAvailableResourceException.java,v 1.1 2007/04/28 14:08:36 liuj Exp $
 * File:NoAvailableResourceException.java
 * Date Author Changes
 * March 5 2003 wangdeliang Created
 */

package zt.platform.db;

/**
 * 当系统无可用资源时抛出这个例外.
 *
 * @author <a href="mailto:wangdl@zhongtian.biz">WangDeLiang</a>
 * @version $Revision: 1.1 $ $Date: 2007/04/28 14:08:36 $
 */

public class NoAvailableResourceException extends Exception {
    public NoAvailableResourceException() {
        super();
    }

    public NoAvailableResourceException(String msg) {
        super(msg);
    }
}