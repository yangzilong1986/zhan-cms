package zt.cms.report;

/**
 * <p>Title: �Ŵ�����ϵͳ</p>
 * <p>Description: Ϋ���Ŵ�</p>
 * <p>Copyright: Copyright (c) 2003 ������Ϣ�������޹�˾</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
 * @author Yusg
 * @version 1.0
 */

import java.util.ArrayList;
/*******************************************
 *
 *     ���ʴ�����ϸ��ѯ    ------ֵ����
 *
 *******************************************/

public class SinLoanDetData
{
    public ArrayList alPay=new ArrayList();          //�����ջ�����б�

    /*******************������Ϣ********************/
    public String strScbrhName="&nbsp;";              //�������� bmtable
    public String strClientName="&nbsp;";             //�ͻ����� bmtable
    public String strPhone="&nbsp;";                  //��ϵ�绰 cmclient
    public String strLoanType="&nbsp;";               //�������� bmtableapp
    public String strLoanPurpose="&nbsp;";            //������; bmtableapp
    public String strId="&nbsp;";                     //֤������ cmclient
    public String strClientMgr="&nbsp;";              //�ͻ����� bmtableapp
    public String strFirstResp="&nbsp;";              //��һ�����˻�������������� bmtableapp
    public String strFisrtRespPct="&nbsp;";           //�����˽�� bmtableapp
    public String strIfResp="&nbsp;";                 //�ǵ�һ���ǹ���������

    /*******************������Ϣ********************/
    public String strContractNo="&nbsp;";             //��ͬ���    bmtableapp
    public String strSContractNo="&nbsp;";            //������ͬ��  bmtableapp
    public String strContractAmt="&nbsp;";            //��ͬ���   rqloanledger
    public String strPayDate="&nbsp;";                //��ͬ������ rqloanledger
    public String strActNo="&nbsp;";                  //�����ʺ�   rqloanledger
    public String strCnlNo="&nbsp;";                  //��ݺ�     rqloanledger
    public String strAccNo="&nbsp;";                  //��Ŀ       rqloanledger
    public String strCrtRate="&nbsp;";                //����       rqloanledger
    public String strEndDate="&nbsp;";                //������     rqloanledger
    public String strNowEndDate="&nbsp;";             //�������  rqloanledger
    public String strPerimon="&nbsp;";                //��������    rqloanledger
    public String strNowBal="&nbsp;";                 //��ǰ���    rqloanledger
    public String strLoanType3="&nbsp;";              //������ʽ  bmtableapp
    public String strLoanType5="&nbsp;";              //���ŷ�ʽ  bmtableapp
    public String strLoanCat2="&nbsp;";               //ռ����̬   rqloanledger
    public String strEndRate="&nbsp;";                //��Ƿ��Ϣ   rqloanledger

    /********************�����ջ����******************/
    public String strPActNo="&nbsp;";                 //�����ʺ�  rqpayback
    public String strPCnlNo="&nbsp;";                 //��ݺ�    rqpayback
    public String strPTxnDate="&nbsp;";               //��������  rqpayback
    public String strPayCrBal="&nbsp;";               //���Ž��  rqpayback
    public String strPayDbBal="&nbsp;";               //�ջؽ��  rqpayback
    public String strPDtlBal="&nbsp;";                //��ǰ���  rqpayback
    public String strPLoanCat2="&nbsp;";              //ռ����̬  rqpayback

    /*********************����ʱ���******************/
    public String strTransDate="&nbsp;";             //ת����ʱ��  bminactloan
    public String strAdminedBy="&nbsp;";             //����������  bminactloan
    public String strLastNotifyDate="&nbsp;";        //��������    bminactloan
    public String strReviewedBy="&nbsp;";            //������      bminactloan
    public String strPenaltyDate="&nbsp;";           //����ʱ��    bminactloan
    public String strPenaltyRule="&nbsp;";           //��Ӧ�����涨 bminactloan
    public String strPenalty="&nbsp;";               //�������    bminactloan

    /**********************�������******************/
    public String strDate="&nbsp;";                  //��ѯ����
    public String strBmStatus="&nbsp;";              //Ŀǰ״̬ bmtable



}