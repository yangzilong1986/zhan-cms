package zt.cms.xf.common.constant;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-4-20
 * Time: 15:02:20
 * To change this template use File | Settings | File Templates.
 */

/*
合同状态：签订、放款、结清、呆滞、呆账、坏账核销
 */

public class XFContractStatus {

    //合同签订待审核
    public static final String QIANDING_DAISHENHE = "1";

    //合同签订审核驳回
    public static final String QIANDING_BOHUI = "2";

    //合同签订审核通过
    public static final String QIANDING_TONGGUO = "3";

    //合同放款待审核
    public static final String FANGKUAN_DAISHENHE = "4";

    //合同放款审核驳回
    public static final String FANGKUAN_BOHUI = "5";

    //合同放款审核通过
    public static final String FANGKUAN_TONGGUO = "6";

    //提前还款待审核
    public static final String TIQIANHUANKUAN_DAISHENHE = "7";

    //提前还款审核驳回
    public static final String TIQIANHUANKUAN_BOHUI = "8";

    //提前还款审核通过
    public static final String TIQIANHUANKUAN_TONGGUO = "9";


    //合同废止待审核
    //合同废止审核通过
    //合同废止已成功

    //合同核销待审核
    public static final String  HEXIAO_DAISHENHE = "21";
    //合同核销审核驳回
    public static final String  HEXIAO_BOHUI = "22";
    //合同核销通过
    public static final String  HEXIAO_TONGGUO= "23";


    //合同正常终止
    public static final String HETONG_OVER_NORMAL = "90";
    //合同已核销
    public static final String HETONG_OVER_HEXIAO = "91";


}
