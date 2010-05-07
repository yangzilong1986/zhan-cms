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

    //��ΰ����Ĵ�������20001-30000
    public static final int NO_RIGHT_OF_ACT = -20001; //û��ִ��ҵ���Ȩ��
    public static final int getAlertTypeofBrh_FAILED = -20002; //�������ľ����ʧ��
    public static final int getAlertTypeofUsr_FAILED = -20003; //����û��ľ����ʧ��
    public static final int UPDATE_LSTNOTIFIED_DATE_OF_INACTLOAN_FAILED = -20004; //���Ĳ������������������ʧ��
    public static final int UPDATE_CHECK_DATE_OF_INACTLOAN_FAILED = -20005; //���²�������������ʧ��
    public static final int UPDATE_ADMINBY_OF_INACTLOAN_FAILED = -20006; //���Ĵ���������ʧ��
    public static final int UPDATE_TRANS_OF_ASSERT_FAILED = -20007; //������ծ�ʲ�ʧ��
    public static final int UPDATE_ILSTAT_OF_ILOAN_FAILED = -20008; //������̬����ʧ��
    public static final int GET_NEXT_BM_STATUS_ERROR = -20009; //������̵���һ��״̬����

    //GZL����Ĵ�30001-40000
    public static final int MYDB_EXCEPT_ADDCONN = -30001; //������ݿ������쳣
    public static final int PARAM_IS_NULL = -30002; //������������Ϊ��
    public static final int NO_DB_CONN = -30003;  //�Ҳ������õ����ݿ�����
    public static final int NO_DB_CONN_PTCON = -30004; //ƽ̨���ݿ�����Ϊ��
    public static final int CLIENT_NOT_FOUND = -30005; //�ͻ�������
    public static final int EXCPT_FOUND = -30006; //�����쳣
    public static final int BRANCH_NOT_FOUND = -30007; //���㲻����
    public static final int BUSI_DATE_NOT_FOUND = -30008; //ҵ������δ�ҵ�
    public static final int DB_UPDATE_FAILED = -30009; //���ݿ���¼�¼����
    public static final int DB_INSERT_FAILED = -30010; //���ݿ�����¼����
    public static final int GET_BMTRANSDATA_ERROR = -30011;//���ҵ������Ϣ����
    public static final int BMTYPE_ERROR_COMPLT_TRANS = -30012;//���ҵ����ʱ��ҵ�������Ͷ������
    public static final int GET_BMTABLE_ERROR = -30013;//���ҵ��������Ϣ����
    public static final int GET_ROUTETBL_ERROR = -30014;//���ҵ�����̴���
    public static final int GET_APPDATE_ERROR = -30015;//���ҵ��ǼǸ�����Ϣ����
    public static final int GET_UP_BRH_ERROR = -30016;//����ϼ��������
    public static final int NO_ANY_COMMENTS = -30017;//δ�����κ��������
    public static final int NO_ANY_DECISION = -30018;//δ����������
    public static final int BM_TRANS_NOT_EXECUTING = -30019;//ҵ���費������ִ��״̬
    public static final int INIT_BRH_IS_NULL = -30020;//ҵ��������Ϊ��
    public static final int REIVEW_LIMIT_NOT_FOUND = -30021;//ҵ��������Ȳ�����
    public static final int NEXT_BM_STATUS_IS_NULL = -30022;//��һ��ҵ��״̬Ϊ��
    public static final int GET_SN_ERROR = -30023;//���˳��Ŵ���
    public static final int ACPT_BILL_APP_NOT_EXIST = -30024;//�жһ�Ʊ������Ϣ������
    public static final int GRANT_DAYS_NOT_DEFINE = -30025;//��Ȩ��Ч����Ϊ����
    public static final int GRANT_DAYS_INVALID = -30026;//��Ȩ��Ч��������Ƿ�
    public static final int GRANT_RECORD_ALREADY_EXIST = -30027;//��Ȩ��¼�Ѿ�����
    public static final int GRANT_RECORD_NOT_EXIST = -30028;//��Ȩ��¼������
    public static final int BILLDIS_APP_NO_RECORD = -30029;//���ֵǼ���Ϣ������
    public static final int BILLDIS_LEDGER_IS_NULL = -30030;//����̨�ʲ�����
    public static final int PDASSETS_APP_NO_RECORD = -30031;//��ծ�ʲ��Ǽ���Ϣ������
    public static final int GET_UPTODATE_APP_NULL = -30032;//���ҵ����������Ϣ����
    public static final int GET_CONTRACT_IS_NULL = -30033;//��ͬ��Ϣ������
    public static final int GRANT_SEND_REQUEST_FAILED = -30034;//��Ȩ�����������
    public static final int CONTRACT_DATA_NOT_FOUND = -30035;//��ͬ��Ϣ������
    public static final int ACPT_BILL_HONOUR_DB_NOTFOUND = -30036;//�жһ�Ʊ�жһ�������Ϣ������
    public static final int ACPT_BILL_HONOUR_NO_ADV = -30037;//�жһ�Ʊ�жһ�����δ�������
    public static final int ACPT_BILL_HONOUR_FLD_EMPTY = -30038;//�жһ�Ʊ�жһ��������ֶ�Ϊ��
    public static final int NOT_FOUND_BMNO_IN_ACPTBILLLEDGER = -30039;//�жһ�Ʊ̨��û��ҵ���
    public static final int NOT_FOUND_BMNO_IN_BILLDISLEDGER = -30040;//����̨��û��ҵ���
    public static final int CAN_NOT_CREATE_BMTABLE = -30041;//���ܽ���ҵ������
    public static final int BILL_DIS_HONOUR_DB_NOTFOUND = -30042;//����������Ϣ������
    public static final int BILL_DIS_HONOUR_FLD_EMPTY = -30043;//����������Ϣ���ֶ�Ϊ��
    public static final int BILLDIS_HONOUR_NOT_DIS_STATUS = -30044;//���ֵ��������Ϣ��������״̬
    public static final int BILLDIS_HONOUR_NOT_REDIS_STATUS = -30045;//ת���ֵ��������Ϣ����ת����״̬
    public static final int ORIG_BMTABLE_CLIENT_BRH_INITBRH_NULL = -30046;//ԭҵ���Ŀͻ��ţ����㣬��������Ϊ��
    public static final int GET_BTERRLEDGER_FAILED = -30047;//���δ��Ȩ��¼��Ϣ����
    public static final int ERRLEDGER_STATUS_NOT_UNPROCESSED = -30048;//δ��Ȩ��¼״̬����δ����״̬
    public static final int SEND_GRANT_SEND_FAILED = -30049;//�̷߳�����Ȩ���ۺ�ҵ��ϵͳ���ʹ���
    public static final int SEND_GRANT_RECV_FAILED = -30050;//�̻߳����Ȩ��Ӧ����
    public static final int SEND_GRANT_RECV_RESULT_FAILED = -30051;//��Ȩ��Ӧ���Ϊ�ջ�������
    public static final int NOT_ALL_GRANT_SEND_OK = -30052; //��Ȩ���ͳ�������ʱ������Ȩ����ʧ��
    public static final int ACPT_BILL_STS_NOT_ZHENGCHANG = -30053; //�жһ�Ʊ̨��״̬��������
    public static final int BILL_DIS_STS_NOT_ZHENGCHANG = -30054;//���ֻ�Ʊ̨��״̬��������
    public static final int BT_ERR_LEDGER_STATSUS_NOT_UNPROCESSED = -30055;  //δ��Ȩ����̨��״̬����δ����
    public static final int BT_ERR_LEDGER_NOT_FOUND = -30056;  //δ��Ȩ����̨��δ����
    public static final int LOAN_LEDGER_REC_NOT_FOUND = -30057;  //����̨��δ����
    public static final int BT_ERR_LEDGER_BOTH_CLIENT_BMNO = -30058; //ҵ��źͿͻ���ֻ����һ��
    public static final int DB_QUERY_ERROR = -30059; //���ݿ��ѯ����ʧ��
    public static final int APP_DIS_PLDG_NO_REC = -30060; //���ֵǼ�δ����Ʊ����Ϣ
    public static final int APP_REDIS_PLDG_NO_REC = -30061; //ת���ֵǼ�δ����Ʊ����Ϣ
    public static final int APP_PDASSET_PLDG_NO_REC = -30062; //���ʵ�ծ�Ǽ�δ����Ʊ����Ϣ
    public static final int BM_STATUS_NOT_FAFANG = -30063; //ҵ��״̬�����Ƿ��Ų���ִ�б�����
    public static final int ACPT_BILL_STS_NOT_ZHENGCHANGSHOUHUI = -30064; //��Ʊ״̬���������ջ�
    public static final int UPDATE_DIS_BILL_ADV_BMNO_ERROR = -30065; //���ֵ���и��µ��ҵ���ʧ��
    public static final int UPDATE_REDIS_BILL_ADV_BMNO_ERROR = -30066; //ת���ֵ���и��µ��ҵ���ʧ��
    public static final int HONOUR_REC_NOT_DIS_TYPE = -30067; //����¼������������
    public static final int HONOUR_REC_NOT_REDIS_TYPE = -30068; //����¼����ת��������
    public static final int DIS_OR_REDIS_ADVBMNO_NOT_EXIST = -30069; //���ҵ���¼������
    public static final int SEND_AUTHORIZATION_TIME_OUT = -30070; //������Ȩ��ʱ
    public static final int SEND_AUTHORIZATION_RET_ERROR = -30071; //������Ȩ���ش���
    public static final int GRNAT_HAS_BEEN_CANCELLED = -30072; //��Ȩ�Ѿ���ȡ��
    public static final int GRNAT_MUST_BE_YISHOUQUAN = -30073; //��Ȩ״̬����������Ȩ
    public static final int DECISION_TYPE_MUST_BE_BUTONGYI = -30074; //��ϸ���Ϊ��ͬ��ʱ��������������ǲ�ͬ��
    public static final int DECISION_AMT_CANNOT_INCREASE = -30075; //�޸ĺ�ľ��߽��ܳ����Ǽǽ��
    public static final int INIT_CLIENT_NOT_EXIST = -30076; //���пͻ���Ϣ������
    public static final int INIT_CLIENT_NOT_UNPROCESSED = -30077; //���пͻ��Ѿ��������
    public static final int INIT_CLIENT_WITHOUT_OLDCLIENTNO = -30078; //ѡ���Ѵ��ڿͻ�ʱ�����������Ѿ����ڵĿͻ���
    public static final int CANNOT_FOUND_CURRENT_USERNO = -30079; //���ܻ�õ�ǰ�û��ı�ʶ
    public static final int NOT_INPUT_APP_AMT = -30080; //δ����������
    public static final int RESP_AMT_LT_DEC_AMT = -30081; //��һ���ν��ܴ��ھ��߽��
    public static final int LOANTYPE3_IS_NULL = -30082; //����ʱ�ڵǼ���Ϣ��û���ҵ���������(����Ϊ��)
    public static final int LOANTYPE5_IS_NULL = -30083; //����ʱ�ڵǼ���Ϣ��û���ҵ���������(����Ϊ��)
    public static final int CLIENTTYPE_IS_NULL = -30084; //����ʱ�ڵǼ���Ϣ��û���ҵ��ͻ����(����Ϊ��)
    public static final int YIZIDIZHAI_APPAMT_NOT_MATCH = -30085; //�Ǽǵĵ�ծ�ʲ��ܽ���ծ�����ܽ��Ǽ�����������߱������
    public static final int REV_LIMIT_TYPE_NOT_DEFINE = -30086; //δ����������������
    public static final int NEXT_FCNO_IS_NULL = -30087; //δ����������������
    public static final int DECISION_PRD_CANNOT_INCREASE = -30089; //�޸ĺ�ľ������޲��ܳ����Ǽ���������
    public static final int PDASSETAMT_MUST_GE_ILLOANAMT = -30090; //��ծ�ʲ��ܽ�������ڻ���ڵֶ������ܶ�
    public static final int YIZIDIZHAI_DATE_ERROR = -30091; //����ʱ��������ڻ���ڵ�ծʱ��(��ǰ������ʱ��)

    //WXJ����Ĵ�40001-50000
    public static final int USER_NOT_FOUND = -40001;        //�û�������
}
