//Source file: e:\\java\\zt\\cmsi\\biz\\BizQuery.java

package zt.cmsi.biz;

import zt.cms.pub.SCBranch;

public class BizQuery {

    /**
     * ���û�ѡ��ҵ������¼(����ͻ���,̨�ʺ���)����ҵ���ܲ���ʱ,
     * �����ܸ��ݴ����ҵ�����㷵���й�ҵ���������������(����:ĳһ����������������ʵ��
     * ��)
     * <p/>
     * ����SCBRANCH��ʵ�ֻ��ĳһ����������������ʵ����.
     *
     * @param BrhID
     * @return String
     * @roseuid 3FE69734033A
     */
    public static String getBranchQryWhere(String BrhID) {
        return SCBranch.getSubBranchAll(BrhID);
    }
}
