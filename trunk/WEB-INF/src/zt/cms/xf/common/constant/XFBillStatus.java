package zt.cms.xf.common.constant;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-4-20
 * Time: 15:02:20
 * To change this template use File | Settings | File Templates.
 */
public class XFBillStatus {

    //������
    public static final String BILLSTATUS_CHECK_PENDING = "1";

    //�Ѹ���
    public static final String BILLSTATUS_CHECKED = "2";
    //�ѳ���
    public static final String BILLSTATUS_CHARGEDOFF = "3";
    //�ύ�ɹ����ѷ��͵����д��ۻ���������ۣ��ۿ�������ѯȷ�ϣ�
    public static final String BILLSTATUS_SEND_SUCCESS = "4";
    //�ۿ�ʧ��
    public static final String BILLSTATUS_CUTPAY_FAILED = "5";
    //���ֿۿ�ɹ�
    public static final String BILLSTATUS_CUTPAY_SUCCESS_HALF = "6";
    //�ۿ�ɹ�
    public static final String BILLSTATUS_CUTPAY_SUCCESS = "7";

    //��̨����ʧ��
    public static final String BILLSTATUS_CORE_FAILED = "8";
    //��̨���ʳ�ʱ
    public static final String BILLSTATUS_CORE_OVERTIME = "9";
    //��̨���ʳɹ�
    public static final String BILLSTATUS_CORE_SUCCESS = "10";                         

    //����ϵͳ����ʧ��
    public static final String FD_WRITEBACK_FAILD = "11";

    //����ϵͳ����ɹ�
    public static final String FD_WRITEBACK_SUCCESS = "12";

}