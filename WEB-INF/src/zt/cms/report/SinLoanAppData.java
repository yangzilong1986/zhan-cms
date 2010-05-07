package zt.cms.report;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: 潍坊信贷管理系统</p>
 * <p>Copyright: Copyright (c) 2003 中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author YUSG
 * @version 1.0
 */
import java.util.ArrayList;
/***********************************************
 *function: 保存单笔贷款审批情况的数据
 ***********************************************/
public class SinLoanAppData
{
    public ArrayList alPlege=new ArrayList();          //抵质押物情况列表
    public ArrayList alComment=new ArrayList();        //审批情况列表
    public ArrayList alDecision=new ArrayList();       //决策情况列表

    /**********************基本资料********************/
    public String basBrhName="&nbsp;";          //所属网点
    public String basDate="&nbsp;";             //查询日期
    public String basStatus="&nbsp;";           //业务状态
    public String basStatusName="&nbsp;";       //业务状态名

    /**********************申请情况*******************/
    public String appClientNo="&nbsp;";         //客户代码
    public String appClientName="&nbsp;";       //客户名称
    public String appID="&nbsp;";               //证件号码
    public String appClientType="&nbsp;";       //客户类型
    public String appAddress="&nbsp;";          //住址
    public String appPhone="&nbsp;";            //联系电话
    public String appCreditClass="&nbsp;";      //信用等级名称
    public String appLoanCat="&nbsp;";          //贷款用途
    public String appAmt="&nbsp;";              //申请金额
    public String appMonths="&nbsp;";           //申请期限

    /**********************担保人情况*******************/
    public String secClientName="&nbsp;";       //担保人名称
    public String secID="&nbsp;";               //证件号码
    public String secClientType="&nbsp;";       //担保人类型
    public String secAddress="&nbsp;";          //担保人地址
    public String secPhone="&nbsp;";            //联系电话
    public String secCreditClass="&nbsp;";      //担保人信用等级

    /*********************抵质押物情况******************/
    public String pleName="&nbsp;";             //抵质押物名称
    public String pleType="&nbsp;";             //抵质押物类型
    public String pleOwner="&nbsp;";            //抵质押物所有人
    public String pleAmt="&nbsp;";             //数量
    public String pleEstimate="&nbsp;";         //评估价
    public String plePrice="&nbsp;";            //抵押值
    public String pleRate="&nbsp;";             //抵押率
    /**********************审批情况********************/
    public String comType="&nbsp;";             //处理人岗位
    public String comReviewd="&nbsp;";          //处理人名称
    public String comRemark="&nbsp;";           //处理意见
    public String comResultType="&nbsp;";       //结论
    public String comBrhLevel="&nbsp;";         //审批部门

    /**********************决策情况********************/
    public String decDecided="&nbsp;";          //决策人
    public String decAmt="&nbsp;";              //决策金额
    public String decRate="&nbsp;";             //决策利率
    public String decMonths="&nbsp;";           //决策期限
    public String decResultType="&nbsp;";       //结论
    public String decRrhLevel="&nbsp;";         //决策部门

    /**********************发放情况********************/
    public String conNo="&nbsp;";             //合同编号
    public String conBeginDate="&nbsp;";      //合同发放日
    public String conEndDate="&nbsp;";        //合同到期日
    public String conAmt="&nbsp;";            //合同金额
    public String conLoanAmt="&nbsp;";        //合同发放金额
    public String conMonths="&nbsp;";         //合同期限
    public String conRate="&nbsp;";           //利率
    public String conAccNo="&nbsp;";          //科目

}