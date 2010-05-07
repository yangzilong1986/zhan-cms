package zt.cms.xf.common.constant;

/**
 * 主帐单流程控制
 * User: zhanrui
 * Date: 2009-4-20
 * Time: 15:02:20
 * To change this template use File | Settings | File Templates.
 */
public class XFPBStatus {

    //未处理
    public static final String PBSTATUS_INIT = "0";

    //待复核
    public static final String PBSTATUS_CHECK_PENDING = "1";

    //已驳回
    public static final String PBSTATUS_DISMISSALED = "2";

    //已复核
    public static final String PBSTATUS_CHECKED = "3";

}