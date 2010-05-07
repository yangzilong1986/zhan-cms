package zt.cms.xf.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author LiChen
 * 2010-01-25<p>
 * ������
 * */
public class Xfvconfirmprtinfo implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String empno; // Ա������
	protected String name;	// ����
	protected String id;	// ���֤���
	protected String currentaddress;	// ��ַ
	protected int residenceadr;	// �������ڵ�
	protected String phone1;	// ��ϵ�绰
	protected int gender;	// �Ա�
	protected String marriagestatus; // ����״��
	protected Date birthday;	// ��������
	
	// ��������
	protected String company;	// ������λ
	protected String servform;	// �ֵ�λ����ʱ��
	protected String comaddr;	// ��λ��ַ
	protected String phone3;	// ��λ�绰
	protected BigDecimal confmonpay;	// ÿ������
	
	//���ڸ������
	protected BigDecimal appamt;	// ������ڽ��
	protected String commname; // ��Ʒ����
	protected BigDecimal divid; // ����
	protected BigDecimal commissionrate; // ����������
	protected BigDecimal amt; //	��Ʒ�ܼۿ�
	protected BigDecimal proportion; // ���ڽ��ռ������Ʒ��ֵ����
	protected String addr; // ��Ʒʹ�õ�ַ
	protected BigDecimal appamtmon; // ÿ�»����
	protected BigDecimal monthprop; // �����뱶��
	
	// ����ϵͳ��¼
	protected String ccvalidperiod;	// ��¼����
	protected BigDecimal ccdynum; // ����(��)����_��Ѻ����
	protected BigDecimal ccxynum; // ����(��)����_���ô���
	protected BigDecimal cccdnum; // ����(��)����_���ÿ�
	protected BigDecimal ccloanyearquerytime;	// ������������
	protected BigDecimal cccardyearquerytime; // ���ÿ���������
	protected BigDecimal ccdyamt;	// �ܴ����/���_��Ѻ����
	protected BigDecimal ccxyamt;	// �ܴ����/���_���ô���
	protected BigDecimal cccdamt; // �ܴ����/���_���ÿ�
	protected BigDecimal ccdynowbal;	//�ܴ������/���_��Ѻ����
	protected BigDecimal ccxynowbal; // �ܴ������/���_���ô���
	protected BigDecimal cccdnewbal; // �ܴ������/���_���ÿ�
	protected BigDecimal ccrpnowamt; // ���д�����
	protected BigDecimal ccdyrpmon; // ���»����_��Ѻ����
	protected BigDecimal ccxyrpmon; // ���»����_���ô���
	protected BigDecimal cccdrpmon; // ���»����_���ÿ�
	protected BigDecimal ccdividamt;	// �ñʷ��ڻ����
	protected BigDecimal ccyearrptime;	// 12���»����¼(����)
	protected BigDecimal ccrptotalamt;// �ܻ����
	protected String ccdynooverdue; // ������_��Ѻ����
	protected String ccxynooverdue; // ������_���ô���
	protected String cccdnooverdue; // ������_���ÿ�
	protected BigDecimal ccrprate; // ÿ�»����������
	protected BigDecimal ccdy1timeoverdue; //����1-30��(1)_��Ѻ����
	protected BigDecimal ccxy1timeoverdue; //����1-30��(1)_���ô���
	protected BigDecimal cccd1timeoverdue; //����1-30��(1)_���ÿ�
	protected BigDecimal ccdy2timeoverdue; //����1-30��(2)_��Ѻ����
	protected BigDecimal ccxy2timeoverdue; //����1-30��(2)_���ô���
	protected BigDecimal cccd2timeoverdue; //����1-30��(2)_���ÿ�
	protected BigDecimal ccdy3timeoverdue; //����1-30��(3����)_��Ѻ����
	protected BigDecimal ccxy3timeoverdue; //����1-30��(3����)_���ô���
	protected BigDecimal cccd3timeoverdue; //����1-30��(3����)_���ÿ�
	
	// ����Ҫ��
	protected String acage; // ����Ҫ��
	protected String acwage; // ÿ����͹���
	protected String acemp; // Ա�����_������ʽԱ��
	protected String acjob; // Ա�����_�����
	protected String accontract; // �Ͷ���ͬ����Ҫ��
	protected String aczx1; // ����ϵͳ�����һ����δ�����¼
	protected String aczx2; // ����ϵͳ�ڹ�ȥ12�����������������5������
	protected String aczx3; // ����ϵͳ�ڹ�ȥ12���������������60������
	protected String acfacility;// ���÷����ܶ�������30,000 ��12��������
	protected String acrate; // ÿ�»�����������Ƿ����70%
	protected String acacceptreason;// �ṩ�����������
	
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
