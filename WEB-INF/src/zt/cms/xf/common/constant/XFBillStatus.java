package zt.cms.xf.common.constant;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-4-20
 * Time: 15:02:20
 * To change this template use File | Settings | File Templates.
 */
public class XFBillStatus {

    //待复核
    public static final String BILLSTATUS_CHECK_PENDING = "1";

    //已复核
    public static final String BILLSTATUS_CHECKED = "2";
    //已出帐
    public static final String BILLSTATUS_CHARGEDOFF = "3";
    //提交成功（已发送到银行代扣或第三方代扣，扣款结果待查询确认）
    public static final String BILLSTATUS_SEND_SUCCESS = "4";
    //扣款失败
    public static final String BILLSTATUS_CUTPAY_FAILED = "5";
    //部分扣款成功
    public static final String BILLSTATUS_CUTPAY_SUCCESS_HALF = "6";
    //扣款成功
    public static final String BILLSTATUS_CUTPAY_SUCCESS = "7";

    //后台入帐失败
    public static final String BILLSTATUS_CORE_FAILED = "8";
    //后台入帐超时
    public static final String BILLSTATUS_CORE_OVERTIME = "9";
    //后台入帐成功
    public static final String BILLSTATUS_CORE_SUCCESS = "10";                         

    //房贷系统处理失败
    public static final String FD_WRITEBACK_FAILD = "11";

    //房贷系统处理成功
    public static final String FD_WRITEBACK_SUCCESS = "12";

}