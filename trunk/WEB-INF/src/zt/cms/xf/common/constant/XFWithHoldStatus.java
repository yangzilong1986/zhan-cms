package zt.cms.xf.common.constant;

/**
 * 银行代扣记录状态
 * User: zhanrui
 * Date: 2009-4-20
 * Time: 15:02:20
 * To change this template use File | Settings | File Templates.
 */
public class XFWithHoldStatus {

    //待提交
    public static final String SEND_PENDING = "1";

    //扣款提交超时
    public static final String SEND_OVERTIME = "2";

    //扣款提交失败
    public static final String SEND_FAILD = "3";

    //扣款提交成功
    public static final String SEND_SUCCESS = "4";

    //查询提交失败
    public static final String QUERY_FAILD = "5";

    //查询提交成功
    public static final String QUERY_SUCCESS = "6";

    //状态回写失败    (根据银行查询报文返回结果，修改明细扣款记录状态)
    public static final String UPDATESTATUS_FAILD = "7";

    //状态回写成功
    public static final String UPDATESTATUS_SUCCESS = "8";

    //SBS入帐失败
    public static final String ACCOUNT_FAILD = "9";

    //SBS入帐成功
    public static final String ACCOUNT_SUCCESS = "10";

    //房贷系统结果处理失败
    //房贷系统结果处理成功
}