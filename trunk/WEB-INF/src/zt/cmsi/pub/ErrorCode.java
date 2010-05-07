package zt.cmsi.pub;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class ErrorCode {

    //周伟定义的错误代码从20001-30000
    public static final int NO_RIGHT_OF_ACT = -20001; //没有执行业务的权限
    public static final int getAlertTypeofBrh_FAILED = -20002; //获得网点的警戒点失败
    public static final int getAlertTypeofUsr_FAILED = -20003; //获得用户的警戒点失败
    public static final int UPDATE_LSTNOTIFIED_DATE_OF_INACTLOAN_FAILED = -20004; //更改不良贷款的最后催收日期失败
    public static final int UPDATE_CHECK_DATE_OF_INACTLOAN_FAILED = -20005; //更新不良贷款检查日期失败
    public static final int UPDATE_ADMINBY_OF_INACTLOAN_FAILED = -20006; //更改贷款清收人失败
    public static final int UPDATE_TRANS_OF_ASSERT_FAILED = -20007; //冲销低债资产失败
    public static final int UPDATE_ILSTAT_OF_ILOAN_FAILED = -20008; //贷款形态调整失败
    public static final int GET_NEXT_BM_STATUS_ERROR = -20009; //获得流程的下一步状态错误

    //GZL定义的从30001-40000
    public static final int MYDB_EXCEPT_ADDCONN = -30001; //获得数据库连接异常
    public static final int PARAM_IS_NULL = -30002; //公共函数参数为空
    public static final int NO_DB_CONN = -30003;  //找不到可用的数据库连接
    public static final int NO_DB_CONN_PTCON = -30004; //平台数据库连接为空
    public static final int CLIENT_NOT_FOUND = -30005; //客户不存在
    public static final int EXCPT_FOUND = -30006; //出现异常
    public static final int BRANCH_NOT_FOUND = -30007; //网点不存在
    public static final int BUSI_DATE_NOT_FOUND = -30008; //业务日期未找到
    public static final int DB_UPDATE_FAILED = -30009; //数据库更新记录错误
    public static final int DB_INSERT_FAILED = -30010; //数据库插入记录错误
    public static final int GET_BMTRANSDATA_ERROR = -30011;//获得业务步骤信息错误
    public static final int BMTYPE_ERROR_COMPLT_TRANS = -30012;//完成业务步骤时，业务步骤类型定义错误
    public static final int GET_BMTABLE_ERROR = -30013;//获得业务主表信息错误
    public static final int GET_ROUTETBL_ERROR = -30014;//获得业务流程错误
    public static final int GET_APPDATE_ERROR = -30015;//获得业务登记附加信息错误
    public static final int GET_UP_BRH_ERROR = -30016;//获得上级网点错误
    public static final int NO_ANY_COMMENTS = -30017;//未输入任何审批意见
    public static final int NO_ANY_DECISION = -30018;//未输入决策意见
    public static final int BM_TRANS_NOT_EXECUTING = -30019;//业务步骤不是正在执行状态
    public static final int INIT_BRH_IS_NULL = -30020;//业务发起网点为空
    public static final int REIVEW_LIMIT_NOT_FOUND = -30021;//业务审批额度不存在
    public static final int NEXT_BM_STATUS_IS_NULL = -30022;//下一步业务状态为空
    public static final int GET_SN_ERROR = -30023;//获得顺序号错误
    public static final int ACPT_BILL_APP_NOT_EXIST = -30024;//承兑汇票申请信息不存在
    public static final int GRANT_DAYS_NOT_DEFINE = -30025;//授权有效天数为定义
    public static final int GRANT_DAYS_INVALID = -30026;//授权有效天数定义非法
    public static final int GRANT_RECORD_ALREADY_EXIST = -30027;//授权记录已经存在
    public static final int GRANT_RECORD_NOT_EXIST = -30028;//授权记录不存在
    public static final int BILLDIS_APP_NO_RECORD = -30029;//贴现登记信息不存在
    public static final int BILLDIS_LEDGER_IS_NULL = -30030;//贴现台帐不存在
    public static final int PDASSETS_APP_NO_RECORD = -30031;//抵债资产登记信息不存在
    public static final int GET_UPTODATE_APP_NULL = -30032;//获得业务主表附加信息错误
    public static final int GET_CONTRACT_IS_NULL = -30033;//合同信息不存在
    public static final int GRANT_SEND_REQUEST_FAILED = -30034;//授权发送请求错误
    public static final int CONTRACT_DATA_NOT_FOUND = -30035;//合同信息不存在
    public static final int ACPT_BILL_HONOUR_DB_NOTFOUND = -30036;//承兑汇票承兑汇申请信息不存在
    public static final int ACPT_BILL_HONOUR_NO_ADV = -30037;//承兑汇票承兑汇申请未发生垫款
    public static final int ACPT_BILL_HONOUR_FLD_EMPTY = -30038;//承兑汇票承兑汇申请有字段为空
    public static final int NOT_FOUND_BMNO_IN_ACPTBILLLEDGER = -30039;//承兑汇票台帐没有业务号
    public static final int NOT_FOUND_BMNO_IN_BILLDISLEDGER = -30040;//贴现台帐没有业务号
    public static final int CAN_NOT_CREATE_BMTABLE = -30041;//不能建立业务主表
    public static final int BILL_DIS_HONOUR_DB_NOTFOUND = -30042;//贴现申请信息不存在
    public static final int BILL_DIS_HONOUR_FLD_EMPTY = -30043;//贴现申请信息有字段为空
    public static final int BILLDIS_HONOUR_NOT_DIS_STATUS = -30044;//贴现垫款申请信息不是贴现状态
    public static final int BILLDIS_HONOUR_NOT_REDIS_STATUS = -30045;//转贴现垫款申请信息不是转贴现状态
    public static final int ORIG_BMTABLE_CLIENT_BRH_INITBRH_NULL = -30046;//原业务表的客户号，网点，发起网点为空
    public static final int GET_BTERRLEDGER_FAILED = -30047;//获得未授权记录信息错误
    public static final int ERRLEDGER_STATUS_NOT_UNPROCESSED = -30048;//未授权记录状态不是未处理状态
    public static final int SEND_GRANT_SEND_FAILED = -30049;//线程发送授权到综合业务系统发送错误
    public static final int SEND_GRANT_RECV_FAILED = -30050;//线程获得授权响应错误
    public static final int SEND_GRANT_RECV_RESULT_FAILED = -30051;//授权响应结果为空或结果错误
    public static final int NOT_ALL_GRANT_SEND_OK = -30052; //授权发送程序启动时，有授权发送失败
    public static final int ACPT_BILL_STS_NOT_ZHENGCHANG = -30053; //承兑汇票台帐状态不是正常
    public static final int BILL_DIS_STS_NOT_ZHENGCHANG = -30054;//贴现汇票台帐状态不是正常
    public static final int BT_ERR_LEDGER_STATSUS_NOT_UNPROCESSED = -30055;  //未授权贷款台帐状态不是未处理
    public static final int BT_ERR_LEDGER_NOT_FOUND = -30056;  //未授权贷款台帐未发现
    public static final int LOAN_LEDGER_REC_NOT_FOUND = -30057;  //贷款台帐未发现
    public static final int BT_ERR_LEDGER_BOTH_CLIENT_BMNO = -30058; //业务号和客户号只能有一个
    public static final int DB_QUERY_ERROR = -30059; //数据库查询操作失败
    public static final int APP_DIS_PLDG_NO_REC = -30060; //贴现登记未输入票据信息
    public static final int APP_REDIS_PLDG_NO_REC = -30061; //转贴现登记未输入票据信息
    public static final int APP_PDASSET_PLDG_NO_REC = -30062; //以资抵债登记未输入票据信息
    public static final int BM_STATUS_NOT_FAFANG = -30063; //业务状态必须是发放才能执行本操作
    public static final int ACPT_BILL_STS_NOT_ZHENGCHANGSHOUHUI = -30064; //汇票状态不是正常收回
    public static final int UPDATE_DIS_BILL_ADV_BMNO_ERROR = -30065; //贴现垫款中更新垫款业务号失败
    public static final int UPDATE_REDIS_BILL_ADV_BMNO_ERROR = -30066; //转贴现垫款中更新垫款业务号失败
    public static final int HONOUR_REC_NOT_DIS_TYPE = -30067; //垫款记录不是贴现类型
    public static final int HONOUR_REC_NOT_REDIS_TYPE = -30068; //垫款记录不是转贴现类型
    public static final int DIS_OR_REDIS_ADVBMNO_NOT_EXIST = -30069; //垫款业务记录不存在
    public static final int SEND_AUTHORIZATION_TIME_OUT = -30070; //发送授权超时
    public static final int SEND_AUTHORIZATION_RET_ERROR = -30071; //发送授权返回错误
    public static final int GRNAT_HAS_BEEN_CANCELLED = -30072; //授权已经被取消
    public static final int GRNAT_MUST_BE_YISHOUQUAN = -30073; //授权状态必须是已授权
    public static final int DECISION_TYPE_MUST_BE_BUTONGYI = -30074; //详细意见为不同意时，决策意见必须是不同意
    public static final int DECISION_AMT_CANNOT_INCREASE = -30075; //修改后的决策金额不能超过登记金额
    public static final int INIT_CLIENT_NOT_EXIST = -30076; //移行客户信息不存在
    public static final int INIT_CLIENT_NOT_UNPROCESSED = -30077; //移行客户已经被处理过
    public static final int INIT_CLIENT_WITHOUT_OLDCLIENTNO = -30078; //选择已存在客户时，必须输入已经存在的客户号
    public static final int CANNOT_FOUND_CURRENT_USERNO = -30079; //不能获得当前用户的标识
    public static final int NOT_INPUT_APP_AMT = -30080; //未输入申请金额
    public static final int RESP_AMT_LT_DEC_AMT = -30081; //第一责任金额不能大于决策金额
    public static final int LOANTYPE3_IS_NULL = -30082; //审批时在登记信息中没有找到担保类型(不能为空)
    public static final int LOANTYPE5_IS_NULL = -30083; //审批时在登记信息中没有找到发放类型(不能为空)
    public static final int CLIENTTYPE_IS_NULL = -30084; //审批时在登记信息中没有找到客户类别(不能为空)
    public static final int YIZIDIZHAI_APPAMT_NOT_MATCH = -30085; //登记的抵债资产总金额，抵债贷款总金额，登记审批金额三者必须相等
    public static final int REV_LIMIT_TYPE_NOT_DEFINE = -30086; //未定义的审批额度类型
    public static final int NEXT_FCNO_IS_NULL = -30087; //未定义的审批额度类型
    public static final int DECISION_PRD_CANNOT_INCREASE = -30089; //修改后的决策期限不能超过登记申请期限
    public static final int PDASSETAMT_MUST_GE_ILLOANAMT = -30090; //抵债资产总金额必须大于或等于抵顶贷款总额
    public static final int YIZIDIZHAI_DATE_ERROR = -30091; //决策时间必须晚于或等于抵债时间(或前次审批时间)

    //WXJ定义的从40001-50000
    public static final int USER_NOT_FOUND = -40001;        //用户不存在
}
