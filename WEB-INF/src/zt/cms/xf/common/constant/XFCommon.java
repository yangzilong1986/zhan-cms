package zt.cms.xf.common.constant;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-4-29
 * Time: 17:17:07
 * To change this template use File | Settings | File Templates.
 */
public class XFCommon {

    //分期付款每月扣款出帐日
    public static final int  COMMON_CHARGEOFFDATE = 13;

    //还款宽限期 为 7天
    public static final int  COMMON_GRACEPERIOD = 7;

    //滞纳金率 逾期金额5%; 最低为人民币10元
    public static final double  COMMON_LATEFEERATE = 0.05;

    //滞纳金最低为人民币10元
    public static final int  COMMON_LOWESTLATEFEE = 10;

    //违约金率 每天0.05%
    public static final double  COMMON_BREACHFEERATE = 0.0005;

    //提前还款管理费率（未还清本金的1%，记作手续费）
    public static final double  COMMON_PRECUTPAYRATE = 0.01;

}
