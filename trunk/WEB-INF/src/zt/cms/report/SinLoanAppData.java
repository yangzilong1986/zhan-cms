package zt.cms.report;

/**
 * <p>Title: �Ŵ�����</p>
 * <p>Description: Ϋ���Ŵ�����ϵͳ</p>
 * <p>Copyright: Copyright (c) 2003 ������Ϣ�������޹�˾</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
 * @author YUSG
 * @version 1.0
 */
import java.util.ArrayList;
/***********************************************
 *function: ���浥�ʴ����������������
 ***********************************************/
public class SinLoanAppData
{
    public ArrayList alPlege=new ArrayList();          //����Ѻ������б�
    public ArrayList alComment=new ArrayList();        //��������б�
    public ArrayList alDecision=new ArrayList();       //��������б�

    /**********************��������********************/
    public String basBrhName="&nbsp;";          //��������
    public String basDate="&nbsp;";             //��ѯ����
    public String basStatus="&nbsp;";           //ҵ��״̬
    public String basStatusName="&nbsp;";       //ҵ��״̬��

    /**********************�������*******************/
    public String appClientNo="&nbsp;";         //�ͻ�����
    public String appClientName="&nbsp;";       //�ͻ�����
    public String appID="&nbsp;";               //֤������
    public String appClientType="&nbsp;";       //�ͻ�����
    public String appAddress="&nbsp;";          //סַ
    public String appPhone="&nbsp;";            //��ϵ�绰
    public String appCreditClass="&nbsp;";      //���õȼ�����
    public String appLoanCat="&nbsp;";          //������;
    public String appAmt="&nbsp;";              //������
    public String appMonths="&nbsp;";           //��������

    /**********************���������*******************/
    public String secClientName="&nbsp;";       //����������
    public String secID="&nbsp;";               //֤������
    public String secClientType="&nbsp;";       //����������
    public String secAddress="&nbsp;";          //�����˵�ַ
    public String secPhone="&nbsp;";            //��ϵ�绰
    public String secCreditClass="&nbsp;";      //���������õȼ�

    /*********************����Ѻ�����******************/
    public String pleName="&nbsp;";             //����Ѻ������
    public String pleType="&nbsp;";             //����Ѻ������
    public String pleOwner="&nbsp;";            //����Ѻ��������
    public String pleAmt="&nbsp;";             //����
    public String pleEstimate="&nbsp;";         //������
    public String plePrice="&nbsp;";            //��Ѻֵ
    public String pleRate="&nbsp;";             //��Ѻ��
    /**********************�������********************/
    public String comType="&nbsp;";             //�����˸�λ
    public String comReviewd="&nbsp;";          //����������
    public String comRemark="&nbsp;";           //�������
    public String comResultType="&nbsp;";       //����
    public String comBrhLevel="&nbsp;";         //��������

    /**********************�������********************/
    public String decDecided="&nbsp;";          //������
    public String decAmt="&nbsp;";              //���߽��
    public String decRate="&nbsp;";             //��������
    public String decMonths="&nbsp;";           //��������
    public String decResultType="&nbsp;";       //����
    public String decRrhLevel="&nbsp;";         //���߲���

    /**********************�������********************/
    public String conNo="&nbsp;";             //��ͬ���
    public String conBeginDate="&nbsp;";      //��ͬ������
    public String conEndDate="&nbsp;";        //��ͬ������
    public String conAmt="&nbsp;";            //��ͬ���
    public String conLoanAmt="&nbsp;";        //��ͬ���Ž��
    public String conMonths="&nbsp;";         //��ͬ����
    public String conRate="&nbsp;";           //����
    public String conAccNo="&nbsp;";          //��Ŀ

}