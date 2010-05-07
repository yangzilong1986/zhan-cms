package zt.cms.xf.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author LiChen
 * 2010-01-25<p>
 * 审批表
 * */
public class Xfvconfirmprtinfo implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String empno; // 员工卡号
	protected String name;	// 姓名
	protected String id;	// 身份证编号
	protected String currentaddress;	// 地址
	protected int residenceadr;	// 户籍所在地
	protected String phone1;	// 联系电话
	protected int gender;	// 性别
	protected String marriagestatus; // 婚姻状况
	protected Date birthday;	// 出生日期
	
	// 工作资料
	protected String company;	// 工作单位
	protected String servform;	// 现单位工作时间
	protected String comaddr;	// 单位地址
	protected String phone3;	// 单位电话
	protected BigDecimal confmonpay;	// 每月收入
	
	//分期付款情况
	protected BigDecimal appamt;	// 申请分期金额
	protected String commname; // 产品名称
	protected BigDecimal divid; // 期数
	protected BigDecimal commissionrate; // 月手续费率
	protected BigDecimal amt; //	商品总价款
	protected BigDecimal proportion; // 分期金额占所购产品价值比例
	protected String addr; // 产品使用地址
	protected BigDecimal appamtmon; // 每月还款额
	protected BigDecimal monthprop; // 月收入倍数
	
	// 征信系统记录
	protected String ccvalidperiod;	// 记录月数
	protected BigDecimal ccdynum; // 贷款(卡)数量_抵押贷款
	protected BigDecimal ccxynum; // 贷款(卡)数量_信用贷款
	protected BigDecimal cccdnum; // 贷款(卡)数量_信用卡
	protected BigDecimal ccloanyearquerytime;	// 贷款审批次数
	protected BigDecimal cccardyearquerytime; // 信用卡审批次数
	protected BigDecimal ccdyamt;	// 总贷款额/额度_抵押贷款
	protected BigDecimal ccxyamt;	// 总贷款额/额度_信用贷款
	protected BigDecimal cccdamt; // 总贷款额/额度_信用卡
	protected BigDecimal ccdynowbal;	//总贷款余额/额度_抵押贷款
	protected BigDecimal ccxynowbal; // 总贷款余额/额度_信用贷款
	protected BigDecimal cccdnewbal; // 总贷款余额/额度_信用卡
	protected BigDecimal ccrpnowamt; // 现有贷款还款额
	protected BigDecimal ccdyrpmon; // 当月还款额_抵押贷款
	protected BigDecimal ccxyrpmon; // 当月还款额_信用贷款
	protected BigDecimal cccdrpmon; // 当月还款额_信用卡
	protected BigDecimal ccdividamt;	// 该笔分期还款额
	protected BigDecimal ccyearrptime;	// 12个月还款记录(次数)
	protected BigDecimal ccrptotalamt;// 总还款额
	protected String ccdynooverdue; // 无逾期_抵押贷款
	protected String ccxynooverdue; // 无逾期_信用贷款
	protected String cccdnooverdue; // 无逾期_信用卡
	protected BigDecimal ccrprate; // 每月还款与收入比
	protected BigDecimal ccdy1timeoverdue; //逾期1-30天(1)_抵押贷款
	protected BigDecimal ccxy1timeoverdue; //逾期1-30天(1)_信用贷款
	protected BigDecimal cccd1timeoverdue; //逾期1-30天(1)_信用卡
	protected BigDecimal ccdy2timeoverdue; //逾期1-30天(2)_抵押贷款
	protected BigDecimal ccxy2timeoverdue; //逾期1-30天(2)_信用贷款
	protected BigDecimal cccd2timeoverdue; //逾期1-30天(2)_信用卡
	protected BigDecimal ccdy3timeoverdue; //逾期1-30天(3以上)_抵押贷款
	protected BigDecimal ccxy3timeoverdue; //逾期1-30天(3以上)_信用贷款
	protected BigDecimal cccd3timeoverdue; //逾期1-30天(3以上)_信用卡
	
	// 授信要求
	protected String acage; // 年龄要求
	protected String acwage; // 每月最低工资
	protected String acemp; // 员工身份_海尔正式员工
	protected String acjob; // 员工身份_外包工
	protected String accontract; // 劳动合同年限要求
	protected String aczx1; // 征信系统内最近一期无未还款记录
	protected String aczx2; // 征信系统内过去12个月最高逾期期数在5次以下
	protected String aczx3; // 征信系统内过去12个月最长逾期天数在60天以下
	protected String acfacility;// 信用分期总额超过人民币30,000 或12倍月收入
	protected String acrate; // 每月还款与收入比是否低于70%
	protected String acacceptreason;// 提供特殊接受理由
	
	public Xfvconfirmprtinfo() {
		super();
	}

	public String getEmpno() {
		return empno;
	}

	public void setEmpno(String empno) {
		this.empno = empno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrentaddress() {
		return currentaddress;
	}

	public void setCurrentaddress(String currentaddress) {
		this.currentaddress = currentaddress;
	}

	public int getResidenceadr() {
		return residenceadr;
	}

	public void setResidenceadr(int residenceadr) {
		this.residenceadr = residenceadr;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getMarriagestatus() {
		return marriagestatus;
	}

	public void setMarriagestatus(String marriagestatus) {
		this.marriagestatus = marriagestatus;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getServform() {
		return servform;
	}

	public void setServform(String servform) {
		this.servform = servform;
	}

	public String getComaddr() {
		return comaddr;
	}

	public void setComaddr(String comaddr) {
		this.comaddr = comaddr;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}

	public BigDecimal getConfmonpay() {
		return confmonpay;
	}

	public void setConfmonpay(BigDecimal confmonpay) {
		this.confmonpay = confmonpay;
	}

	public BigDecimal getAppamt() {
		return appamt;
	}

	public void setAppamt(BigDecimal appamt) {
		this.appamt = appamt;
	}

	public String getCommname() {
		return commname;
	}

	public void setCommname(String commname) {
		this.commname = commname;
	}

	public BigDecimal getDivid() {
		return divid;
	}

	public void setDivid(BigDecimal divid) {
		this.divid = divid;
	}

	public BigDecimal getCommissionrate() {
		return commissionrate;
	}

	public void setCommissionrate(BigDecimal commissionrate) {
		this.commissionrate = commissionrate;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getProportion() {
		return proportion;
	}

	public void setProportion(BigDecimal proportion) {
		this.proportion = proportion;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public BigDecimal getAppamtmon() {
		return appamtmon;
	}

	public void setAppamtmon(BigDecimal appamtmon) {
		this.appamtmon = appamtmon;
	}

	public BigDecimal getMonthprop() {
		return monthprop;
	}

	public void setMonthprop(BigDecimal monthprop) {
		this.monthprop = monthprop;
	}

	public String getCcvalidperiod() {
		return ccvalidperiod;
	}

	public void setCcvalidperiod(String ccvalidperiod) {
		this.ccvalidperiod = ccvalidperiod;
	}

	public BigDecimal getCcdynum() {
		return ccdynum;
	}

	public void setCcdynum(BigDecimal ccdynum) {
		this.ccdynum = ccdynum;
	}

	public BigDecimal getCcxynum() {
		return ccxynum;
	}

	public void setCcxynum(BigDecimal ccxynum) {
		this.ccxynum = ccxynum;
	}

	public BigDecimal getCccdnum() {
		return cccdnum;
	}

	public void setCccdnum(BigDecimal cccdnum) {
		this.cccdnum = cccdnum;
	}

	public BigDecimal getCcloanyearquerytime() {
		return ccloanyearquerytime;
	}

	public void setCcloanyearquerytime(BigDecimal ccloanyearquerytime) {
		this.ccloanyearquerytime = ccloanyearquerytime;
	}

	public BigDecimal getCccardyearquerytime() {
		return cccardyearquerytime;
	}

	public void setCccardyearquerytime(BigDecimal cccardyearquerytime) {
		this.cccardyearquerytime = cccardyearquerytime;
	}

	public BigDecimal getCcdyamt() {
		return ccdyamt;
	}

	public void setCcdyamt(BigDecimal ccdyamt) {
		this.ccdyamt = ccdyamt;
	}

	public BigDecimal getCcxyamt() {
		return ccxyamt;
	}

	public void setCcxyamt(BigDecimal ccxyamt) {
		this.ccxyamt = ccxyamt;
	}

	public BigDecimal getCccdamt() {
		return cccdamt;
	}

	public void setCccdamt(BigDecimal cccdamt) {
		this.cccdamt = cccdamt;
	}

	public BigDecimal getCcdynowbal() {
		return ccdynowbal;
	}

	public void setCcdynowbal(BigDecimal ccdynowbal) {
		this.ccdynowbal = ccdynowbal;
	}

	public BigDecimal getCcxynowbal() {
		return ccxynowbal;
	}

	public void setCcxynowbal(BigDecimal ccxynowbal) {
		this.ccxynowbal = ccxynowbal;
	}

	public BigDecimal getCccdnewbal() {
		return cccdnewbal;
	}

	public void setCccdnewbal(BigDecimal cccdnewbal) {
		this.cccdnewbal = cccdnewbal;
	}

	public BigDecimal getCcrpnowamt() {
		return ccrpnowamt;
	}

	public void setCcrpnowamt(BigDecimal ccrpnowamt) {
		this.ccrpnowamt = ccrpnowamt;
	}

	public BigDecimal getCcdyrpmon() {
		return ccdyrpmon;
	}

	public void setCcdyrpmon(BigDecimal ccdyrpmon) {
		this.ccdyrpmon = ccdyrpmon;
	}

	public BigDecimal getCcxyrpmon() {
		return ccxyrpmon;
	}

	public void setCcxyrpmon(BigDecimal ccxyrpmon) {
		this.ccxyrpmon = ccxyrpmon;
	}

	public BigDecimal getCccdrpmon() {
		return cccdrpmon;
	}

	public void setCccdrpmon(BigDecimal cccdrpmon) {
		this.cccdrpmon = cccdrpmon;
	}

	public BigDecimal getCcdividamt() {
		return ccdividamt;
	}

	public void setCcdividamt(BigDecimal ccdividamt) {
		this.ccdividamt = ccdividamt;
	}

	public BigDecimal getCcyearrptime() {
		return ccyearrptime;
	}

	public void setCcyearrptime(BigDecimal ccyearrptime) {
		this.ccyearrptime = ccyearrptime;
	}

	public BigDecimal getCcrptotalamt() {
		return ccrptotalamt;
	}

	public void setCcrptotalamt(BigDecimal ccrptotalamt) {
		this.ccrptotalamt = ccrptotalamt;
	}

	public String getCcdynooverdue() {
		return ccdynooverdue;
	}

	public void setCcdynooverdue(String ccdynooverdue) {
		this.ccdynooverdue = ccdynooverdue;
	}

	public String getCcxynooverdue() {
		return ccxynooverdue;
	}

	public void setCcxynooverdue(String ccxynooverdue) {
		this.ccxynooverdue = ccxynooverdue;
	}

	public String getCccdnooverdue() {
		return cccdnooverdue;
	}

	public void setCccdnooverdue(String cccdnooverdue) {
		this.cccdnooverdue = cccdnooverdue;
	}

	public BigDecimal getCcrprate() {
		return ccrprate;
	}

	public void setCcrprate(BigDecimal ccrprate) {
		this.ccrprate = ccrprate;
	}

	public BigDecimal getCcdy1timeoverdue() {
		return ccdy1timeoverdue;
	}

	public void setCcdy1timeoverdue(BigDecimal ccdy1timeoverdue) {
		this.ccdy1timeoverdue = ccdy1timeoverdue;
	}

	public BigDecimal getCcxy1timeoverdue() {
		return ccxy1timeoverdue;
	}

	public void setCcxy1timeoverdue(BigDecimal ccxy1timeoverdue) {
		this.ccxy1timeoverdue = ccxy1timeoverdue;
	}

	public BigDecimal getCccd1timeoverdue() {
		return cccd1timeoverdue;
	}

	public void setCccd1timeoverdue(BigDecimal cccd1timeoverdue) {
		this.cccd1timeoverdue = cccd1timeoverdue;
	}

	public BigDecimal getCcdy2timeoverdue() {
		return ccdy2timeoverdue;
	}

	public void setCcdy2timeoverdue(BigDecimal ccdy2timeoverdue) {
		this.ccdy2timeoverdue = ccdy2timeoverdue;
	}

	public BigDecimal getCcxy2timeoverdue() {
		return ccxy2timeoverdue;
	}

	public void setCcxy2timeoverdue(BigDecimal ccxy2timeoverdue) {
		this.ccxy2timeoverdue = ccxy2timeoverdue;
	}

	public BigDecimal getCccd2timeoverdue() {
		return cccd2timeoverdue;
	}

	public void setCccd2timeoverdue(BigDecimal cccd2timeoverdue) {
		this.cccd2timeoverdue = cccd2timeoverdue;
	}

	public BigDecimal getCcdy3timeoverdue() {
		return ccdy3timeoverdue;
	}

	public void setCcdy3timeoverdue(BigDecimal ccdy3timeoverdue) {
		this.ccdy3timeoverdue = ccdy3timeoverdue;
	}

	public BigDecimal getCcxy3timeoverdue() {
		return ccxy3timeoverdue;
	}

	public void setCcxy3timeoverdue(BigDecimal ccxy3timeoverdue) {
		this.ccxy3timeoverdue = ccxy3timeoverdue;
	}

	public BigDecimal getCccd3timeoverdue() {
		return cccd3timeoverdue;
	}

	public void setCccd3timeoverdue(BigDecimal cccd3timeoverdue) {
		this.cccd3timeoverdue = cccd3timeoverdue;
	}

	public String getAcage() {
		return acage;
	}

	public void setAcage(String acage) {
		this.acage = acage;
	}

	public String getAcwage() {
		return acwage;
	}

	public void setAcwage(String acwage) {
		this.acwage = acwage;
	}

	public String getAcemp() {
		return acemp;
	}

	public void setAcemp(String acemp) {
		this.acemp = acemp;
	}

	public String getAcjob() {
		return acjob;
	}

	public void setAcjob(String acjob) {
		this.acjob = acjob;
	}

	public String getAccontract() {
		return accontract;
	}

	public void setAccontract(String accontract) {
		this.accontract = accontract;
	}

	public String getAczx1() {
		return aczx1;
	}

	public void setAczx1(String aczx1) {
		this.aczx1 = aczx1;
	}

	public String getAczx2() {
		return aczx2;
	}

	public void setAczx2(String aczx2) {
		this.aczx2 = aczx2;
	}

	public String getAczx3() {
		return aczx3;
	}

	public void setAczx3(String aczx3) {
		this.aczx3 = aczx3;
	}

	public String getAcfacility() {
		return acfacility;
	}

	public void setAcfacility(String acfacility) {
		this.acfacility = acfacility;
	}

	public String getAcrate() {
		return acrate;
	}

	public void setAcrate(String acrate) {
		this.acrate = acrate;
	}

	public String getAcacceptreason() {
		return acacceptreason;
	}

	public void setAcacceptreason(String acacceptreason) {
		this.acacceptreason = acacceptreason;
	}
	
	
}
