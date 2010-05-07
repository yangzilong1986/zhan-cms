package zt.cms.report;

/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 潍坊信贷</p>
 * <p>Copyright: Copyright (c) 2003 中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author Yusg
 * @version 1.0
 */

import java.util.ArrayList;
/*******************************************
 *
 *     单笔贷款明细查询    ------值对象
 *
 *******************************************/

public class SinLoanDetData
{
    public ArrayList alPay=new ArrayList();          //发放收回情况列表

    /*******************基本信息********************/
    public String strScbrhName="&nbsp;";              //网点名称 bmtable
    public String strClientName="&nbsp;";             //客户名称 bmtable
    public String strPhone="&nbsp;";                  //联系电话 cmclient
    public String strLoanType="&nbsp;";               //贷款种类 bmtableapp
    public String strLoanPurpose="&nbsp;";            //贷款用途 bmtableapp
    public String strId="&nbsp;";                     //证件号码 cmclient
    public String strClientMgr="&nbsp;";              //客户经理 bmtableapp
    public String strFirstResp="&nbsp;";              //第一责任人或管理责任人名称 bmtableapp
    public String strFisrtRespPct="&nbsp;";           //责任人金额 bmtableapp
    public String strIfResp="&nbsp;";                 //是第一还是管理责任人

    /*******************贷款信息********************/
    public String strContractNo="&nbsp;";             //合同编号    bmtableapp
    public String strSContractNo="&nbsp;";            //附属合同号  bmtableapp
    public String strContractAmt="&nbsp;";            //合同金额   rqloanledger
    public String strPayDate="&nbsp;";                //合同发放日 rqloanledger
    public String strActNo="&nbsp;";                  //贷款帐号   rqloanledger
    public String strCnlNo="&nbsp;";                  //借据号     rqloanledger
    public String strAccNo="&nbsp;";                  //科目       rqloanledger
    public String strCrtRate="&nbsp;";                //利率       rqloanledger
    public String strEndDate="&nbsp;";                //到期日     rqloanledger
    public String strNowEndDate="&nbsp;";             //最后到期日  rqloanledger
    public String strPerimon="&nbsp;";                //贷款期限    rqloanledger
    public String strNowBal="&nbsp;";                 //当前余额    rqloanledger
    public String strLoanType3="&nbsp;";              //担保方式  bmtableapp
    public String strLoanType5="&nbsp;";              //发放方式  bmtableapp
    public String strLoanCat2="&nbsp;";               //占用形态   rqloanledger
    public String strEndRate="&nbsp;";                //结欠利息   rqloanledger

    /********************发放收回情况******************/
    public String strPActNo="&nbsp;";                 //贷款帐号  rqpayback
    public String strPCnlNo="&nbsp;";                 //借据号    rqpayback
    public String strPTxnDate="&nbsp;";               //交易日期  rqpayback
    public String strPayCrBal="&nbsp;";               //发放金额  rqpayback
    public String strPayDbBal="&nbsp;";               //收回金额  rqpayback
    public String strPDtlBal="&nbsp;";                //当前余额  rqpayback
    public String strPLoanCat2="&nbsp;";              //占用形态  rqpayback

    /*********************不良时情况******************/
    public String strTransDate="&nbsp;";             //转不良时间  bminactloan
    public String strAdminedBy="&nbsp;";             //清收责任人  bminactloan
    public String strLastNotifyDate="&nbsp;";        //催收日期    bminactloan
    public String strReviewedBy="&nbsp;";            //审批人      bminactloan
    public String strPenaltyDate="&nbsp;";           //处罚时间    bminactloan
    public String strPenaltyRule="&nbsp;";           //适应处罚规定 bminactloan
    public String strPenalty="&nbsp;";               //处罚情况    bminactloan

    /**********************其他情况******************/
    public String strDate="&nbsp;";                  //查询日期
    public String strBmStatus="&nbsp;";              //目前状态 bmtable



}