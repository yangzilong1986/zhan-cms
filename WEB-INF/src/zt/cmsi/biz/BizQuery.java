//Source file: e:\\java\\zt\\cmsi\\biz\\BizQuery.java

package zt.cmsi.biz;

import zt.cms.pub.SCBranch;

public class BizQuery {

    /**
     * 当用户选择业务主记录(例如客户号,台帐号码)进行业务功能操作时,
     * 本功能根据传入的业务网点返回有关业务网点的限制条件(例如:某一网点所在网点下属实网
     * 点)
     * <p/>
     * 调用SCBRANCH来实现获得某一网点所在网点下属实网点.
     *
     * @param BrhID
     * @return String
     * @roseuid 3FE69734033A
     */
    public static String getBranchQryWhere(String BrhID) {
        return SCBranch.getSubBranchAll(BrhID);
    }
}
