package zt.cms.xf.common.constant;

/**
 * 房贷系统银行代扣记录状态
 * User: zhanrui
 * Date: 2009-4-20
 * Time: 15:02:20
 * To change this template use File | Settings | File Templates.
 */
public class FDBillStatus {

    //待提交
    public static final String SEND_PENDING = "0";

    //已生成报文
    public static final String MSG_CREATED = "1";

    //提交成功                                                                
    public static final String SEND_SUCCESS = "2";

    //扣款失败
    public static final String CUTPAY_FAILD = "3";

    //扣款成功
    public static final String CUTPAY_SUCCESS= "4";

    //入帐失败
    public static final String SBS_ACCOUNT_FAILD = "5";

    //入帐成功
    public static final String SBS_ACCOUNT_SUCCESS= "6";

    //房贷系统处理失败
    public static final String FD_WRITEBACK_FAILD = "7";

    //房贷系统处理成功    （慎重处理：需加一次查询确认）
    public static final String FD_WRITEBACK_SUCCESS = "8";

}