/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package zt.cms.xf.common.dto;

import zt.cms.xf.common.dao.*;
import zt.cms.xf.common.factory.*;
import zt.cms.xf.common.exceptions.*;
import java.io.Serializable;
import java.util.*;
import java.math.BigDecimal;
import java.util.Date;

public class Xfactcutpaydetl implements Serializable
{
	/** 
	 * This attribute maps to the column JOURNALNO in the XFACTCUTPAYDETL table.
	 */
	protected String journalno;

	/** 
	 * This attribute maps to the column PAYBACKAMT in the XFACTCUTPAYDETL table.
	 */
	protected BigDecimal paybackamt;

	/** 
	 * This attribute maps to the column PAYBACKDATE in the XFACTCUTPAYDETL table.
	 */
	protected Date paybackdate;

	/** 
	 * This attribute maps to the column PAYBACKACT in the XFACTCUTPAYDETL table.
	 */
	protected String paybackact;

	/** 
	 * This attribute maps to the column RECVACT in the XFACTCUTPAYDETL table.
	 */
	protected String recvact;

	/** 
	 * This attribute maps to the column PAYBACKBANKID in the XFACTCUTPAYDETL table.
	 */
	protected String paybackbankid;

	/** 
	 * This attribute maps to the column PAYBACKBANKNO in the XFACTCUTPAYDETL table.
	 */
	protected String paybackbankno;

	/** 
	 * This attribute maps to the column RECVBANKID in the XFACTCUTPAYDETL table.
	 */
	protected String recvbankid;

	/** 
	 * This attribute maps to the column RECVBANKNO in the XFACTCUTPAYDETL table.
	 */
	protected String recvbankno;

	/** 
	 * This attribute maps to the column CREATORID in the XFACTCUTPAYDETL table.
	 */
	protected String creatorid;

	/** 
	 * This attribute maps to the column CREATEDATE in the XFACTCUTPAYDETL table.
	 */
	protected Date createdate;

	/** 
	 * This attribute maps to the column CREATEFORM in the XFACTCUTPAYDETL table.
	 */
	protected String createform;

	/** 
	 * This attribute maps to the column UPDATORID in the XFACTCUTPAYDETL table.
	 */
	protected String updatorid;

	/** 
	 * This attribute maps to the column UPDATEDATE in the XFACTCUTPAYDETL table.
	 */
	protected Date updatedate;

	/** 
	 * This attribute maps to the column UPDATEFORM in the XFACTCUTPAYDETL table.
	 */
	protected String updateform;

	/** 
	 * This attribute maps to the column CONTRACTNO in the XFACTCUTPAYDETL table.
	 */
	protected String contractno;

	/** 
	 * This attribute maps to the column POANO in the XFACTCUTPAYDETL table.
	 */
	protected BigDecimal poano;

	/** 
	 * This attribute maps to the column BILLSTATUS in the XFACTCUTPAYDETL table.
	 */
	protected String billstatus;

	/** 
	 * This attribute maps to the column PAIDUPAMT in the XFACTCUTPAYDETL table.
	 */
	protected BigDecimal paidupamt;

	/** 
	 * This attribute maps to the column CLIENTNAME in the XFACTCUTPAYDETL table.
	 */
	protected String clientname;

	/** 
	 * This attribute maps to the column CLIENTACT in the XFACTCUTPAYDETL table.
	 */
	protected String clientact;

	/** 
	 * This attribute maps to the column PAYBACKBANKNAME in the XFACTCUTPAYDETL table.
	 */
	protected String paybackbankname;

	/** 
	 * This attribute maps to the column STARTDATE in the XFACTCUTPAYDETL table.
	 */
	protected Date startdate;

	/** 
	 * This attribute maps to the column PAIDUPDATE in the XFACTCUTPAYDETL table.
	 */
	protected Date paidupdate;

	/** 
	 * This attribute maps to the column OPERATORID in the XFACTCUTPAYDETL table.
	 */
	protected String operatorid;

	/** 
	 * This attribute maps to the column OPERATEDATE in the XFACTCUTPAYDETL table.
	 */
	protected Date operatedate;

	/** 
	 * This attribute maps to the column CHECKERID in the XFACTCUTPAYDETL table.
	 */
	protected String checkerid;

	/** 
	 * This attribute maps to the column CHECKDATE in the XFACTCUTPAYDETL table.
	 */
	protected Date checkdate;

	/** 
	 * This attribute maps to the column PAYBACKACTNAME in the XFACTCUTPAYDETL table.
	 */
	protected String paybackactname;

	/** 
	 * This attribute maps to the column PRINCIPALAMT in the XFACTCUTPAYDETL table.
	 */
	protected BigDecimal principalamt;

	/** 
	 * This attribute maps to the column LATEFEE in the XFACTCUTPAYDETL table.
	 */
	protected BigDecimal latefee;

	/** 
	 * This attribute maps to the column BREACHFEE in the XFACTCUTPAYDETL table.
	 */
	protected BigDecimal breachfee;

	/** 
	 * This attribute maps to the column SERVICECHARGEFEE in the XFACTCUTPAYDETL table.
	 */
	protected BigDecimal servicechargefee;

	/** 
	 * This attribute maps to the column BILLTYPE in the XFACTCUTPAYDETL table.
	 */
	protected String billtype;

	/** 
	 * This attribute maps to the column CUSTOMER_CODE in the XFACTCUTPAYDETL table.
	 */
	protected String customerCode;

	/** 
	 * This attribute maps to the column SIGN_ACCOUNT_NO in the XFACTCUTPAYDETL table.
	 */
	protected String signAccountNo;

	/** 
	 * This attribute maps to the column CLIENTNO in the XFACTCUTPAYDETL table.
	 */
	protected String clientno;

	/** 
	 * This attribute maps to the column CLIENTIDTYPE in the XFACTCUTPAYDETL table.
	 */
	protected String clientidtype;

	/** 
	 * This attribute maps to the column CLIENTID in the XFACTCUTPAYDETL table.
	 */
	protected String clientid;

	/** 
	 * This attribute maps to the column FAILUREREASON in the XFACTCUTPAYDETL table.
	 */
	protected String failurereason;

	/** 
	 * This attribute maps to the column TXJOURNALNO in the XFACTCUTPAYDETL table.
	 */
	protected String txjournalno;

	/**
	 * Method 'Xfactcutpaydetl'
	 * 
	 */
	public Xfactcutpaydetl()
	{
	}

	/**
	 * Method 'getJournalno'
	 * 
	 * @return String
	 */
	public String getJournalno()
	{
		return journalno;
	}

	/**
	 * Method 'setJournalno'
	 * 
	 * @param journalno
	 */
	public void setJournalno(String journalno)
	{
		this.journalno = journalno;
	}

	/**
	 * Method 'getPaybackamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPaybackamt()
	{
		return paybackamt;
	}

	/**
	 * Method 'setPaybackamt'
	 * 
	 * @param paybackamt
	 */
	public void setPaybackamt(BigDecimal paybackamt)
	{
		this.paybackamt = paybackamt;
	}

	/**
	 * Method 'getPaybackdate'
	 * 
	 * @return Date
	 */
	public Date getPaybackdate()
	{
		return paybackdate;
	}

	/**
	 * Method 'setPaybackdate'
	 * 
	 * @param paybackdate
	 */
	public void setPaybackdate(Date paybackdate)
	{
		this.paybackdate = paybackdate;
	}

	/**
	 * Method 'getPaybackact'
	 * 
	 * @return String
	 */
	public String getPaybackact()
	{
		return paybackact;
	}

	/**
	 * Method 'setPaybackact'
	 * 
	 * @param paybackact
	 */
	public void setPaybackact(String paybackact)
	{
		this.paybackact = paybackact;
	}

	/**
	 * Method 'getRecvact'
	 * 
	 * @return String
	 */
	public String getRecvact()
	{
		return recvact;
	}

	/**
	 * Method 'setRecvact'
	 * 
	 * @param recvact
	 */
	public void setRecvact(String recvact)
	{
		this.recvact = recvact;
	}

	/**
	 * Method 'getPaybackbankid'
	 * 
	 * @return String
	 */
	public String getPaybackbankid()
	{
		return paybackbankid;
	}

	/**
	 * Method 'setPaybackbankid'
	 * 
	 * @param paybackbankid
	 */
	public void setPaybackbankid(String paybackbankid)
	{
		this.paybackbankid = paybackbankid;
	}

	/**
	 * Method 'getPaybackbankno'
	 * 
	 * @return String
	 */
	public String getPaybackbankno()
	{
		return paybackbankno;
	}

	/**
	 * Method 'setPaybackbankno'
	 * 
	 * @param paybackbankno
	 */
	public void setPaybackbankno(String paybackbankno)
	{
		this.paybackbankno = paybackbankno;
	}

	/**
	 * Method 'getRecvbankid'
	 * 
	 * @return String
	 */
	public String getRecvbankid()
	{
		return recvbankid;
	}

	/**
	 * Method 'setRecvbankid'
	 * 
	 * @param recvbankid
	 */
	public void setRecvbankid(String recvbankid)
	{
		this.recvbankid = recvbankid;
	}

	/**
	 * Method 'getRecvbankno'
	 * 
	 * @return String
	 */
	public String getRecvbankno()
	{
		return recvbankno;
	}

	/**
	 * Method 'setRecvbankno'
	 * 
	 * @param recvbankno
	 */
	public void setRecvbankno(String recvbankno)
	{
		this.recvbankno = recvbankno;
	}

	/**
	 * Method 'getCreatorid'
	 * 
	 * @return String
	 */
	public String getCreatorid()
	{
		return creatorid;
	}

	/**
	 * Method 'setCreatorid'
	 * 
	 * @param creatorid
	 */
	public void setCreatorid(String creatorid)
	{
		this.creatorid = creatorid;
	}

	/**
	 * Method 'getCreatedate'
	 * 
	 * @return Date
	 */
	public Date getCreatedate()
	{
		return createdate;
	}

	/**
	 * Method 'setCreatedate'
	 * 
	 * @param createdate
	 */
	public void setCreatedate(Date createdate)
	{
		this.createdate = createdate;
	}

	/**
	 * Method 'getCreateform'
	 * 
	 * @return String
	 */
	public String getCreateform()
	{
		return createform;
	}

	/**
	 * Method 'setCreateform'
	 * 
	 * @param createform
	 */
	public void setCreateform(String createform)
	{
		this.createform = createform;
	}

	/**
	 * Method 'getUpdatorid'
	 * 
	 * @return String
	 */
	public String getUpdatorid()
	{
		return updatorid;
	}

	/**
	 * Method 'setUpdatorid'
	 * 
	 * @param updatorid
	 */
	public void setUpdatorid(String updatorid)
	{
		this.updatorid = updatorid;
	}

	/**
	 * Method 'getUpdatedate'
	 * 
	 * @return Date
	 */
	public Date getUpdatedate()
	{
		return updatedate;
	}

	/**
	 * Method 'setUpdatedate'
	 * 
	 * @param updatedate
	 */
	public void setUpdatedate(Date updatedate)
	{
		this.updatedate = updatedate;
	}

	/**
	 * Method 'getUpdateform'
	 * 
	 * @return String
	 */
	public String getUpdateform()
	{
		return updateform;
	}

	/**
	 * Method 'setUpdateform'
	 * 
	 * @param updateform
	 */
	public void setUpdateform(String updateform)
	{
		this.updateform = updateform;
	}

	/**
	 * Method 'getContractno'
	 * 
	 * @return String
	 */
	public String getContractno()
	{
		return contractno;
	}

	/**
	 * Method 'setContractno'
	 * 
	 * @param contractno
	 */
	public void setContractno(String contractno)
	{
		this.contractno = contractno;
	}

	/**
	 * Method 'getPoano'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPoano()
	{
		return poano;
	}

	/**
	 * Method 'setPoano'
	 * 
	 * @param poano
	 */
	public void setPoano(BigDecimal poano)
	{
		this.poano = poano;
	}

	/**
	 * Method 'getBillstatus'
	 * 
	 * @return String
	 */
	public String getBillstatus()
	{
		return billstatus;
	}

	/**
	 * Method 'setBillstatus'
	 * 
	 * @param billstatus
	 */
	public void setBillstatus(String billstatus)
	{
		this.billstatus = billstatus;
	}

	/**
	 * Method 'getPaidupamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPaidupamt()
	{
		return paidupamt;
	}

	/**
	 * Method 'setPaidupamt'
	 * 
	 * @param paidupamt
	 */
	public void setPaidupamt(BigDecimal paidupamt)
	{
		this.paidupamt = paidupamt;
	}

	/**
	 * Method 'getClientname'
	 * 
	 * @return String
	 */
	public String getClientname()
	{
		return clientname;
	}

	/**
	 * Method 'setClientname'
	 * 
	 * @param clientname
	 */
	public void setClientname(String clientname)
	{
		this.clientname = clientname;
	}

	/**
	 * Method 'getClientact'
	 * 
	 * @return String
	 */
	public String getClientact()
	{
		return clientact;
	}

	/**
	 * Method 'setClientact'
	 * 
	 * @param clientact
	 */
	public void setClientact(String clientact)
	{
		this.clientact = clientact;
	}

	/**
	 * Method 'getPaybackbankname'
	 * 
	 * @return String
	 */
	public String getPaybackbankname()
	{
		return paybackbankname;
	}

	/**
	 * Method 'setPaybackbankname'
	 * 
	 * @param paybackbankname
	 */
	public void setPaybackbankname(String paybackbankname)
	{
		this.paybackbankname = paybackbankname;
	}

	/**
	 * Method 'getStartdate'
	 * 
	 * @return Date
	 */
	public Date getStartdate()
	{
		return startdate;
	}

	/**
	 * Method 'setStartdate'
	 * 
	 * @param startdate
	 */
	public void setStartdate(Date startdate)
	{
		this.startdate = startdate;
	}

	/**
	 * Method 'getPaidupdate'
	 * 
	 * @return Date
	 */
	public Date getPaidupdate()
	{
		return paidupdate;
	}

	/**
	 * Method 'setPaidupdate'
	 * 
	 * @param paidupdate
	 */
	public void setPaidupdate(Date paidupdate)
	{
		this.paidupdate = paidupdate;
	}

	/**
	 * Method 'getOperatorid'
	 * 
	 * @return String
	 */
	public String getOperatorid()
	{
		return operatorid;
	}

	/**
	 * Method 'setOperatorid'
	 * 
	 * @param operatorid
	 */
	public void setOperatorid(String operatorid)
	{
		this.operatorid = operatorid;
	}

	/**
	 * Method 'getOperatedate'
	 * 
	 * @return Date
	 */
	public Date getOperatedate()
	{
		return operatedate;
	}

	/**
	 * Method 'setOperatedate'
	 * 
	 * @param operatedate
	 */
	public void setOperatedate(Date operatedate)
	{
		this.operatedate = operatedate;
	}

	/**
	 * Method 'getCheckerid'
	 * 
	 * @return String
	 */
	public String getCheckerid()
	{
		return checkerid;
	}

	/**
	 * Method 'setCheckerid'
	 * 
	 * @param checkerid
	 */
	public void setCheckerid(String checkerid)
	{
		this.checkerid = checkerid;
	}

	/**
	 * Method 'getCheckdate'
	 * 
	 * @return Date
	 */
	public Date getCheckdate()
	{
		return checkdate;
	}

	/**
	 * Method 'setCheckdate'
	 * 
	 * @param checkdate
	 */
	public void setCheckdate(Date checkdate)
	{
		this.checkdate = checkdate;
	}

	/**
	 * Method 'getPaybackactname'
	 * 
	 * @return String
	 */
	public String getPaybackactname()
	{
		return paybackactname;
	}

	/**
	 * Method 'setPaybackactname'
	 * 
	 * @param paybackactname
	 */
	public void setPaybackactname(String paybackactname)
	{
		this.paybackactname = paybackactname;
	}

	/**
	 * Method 'getPrincipalamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPrincipalamt()
	{
		return principalamt;
	}

	/**
	 * Method 'setPrincipalamt'
	 * 
	 * @param principalamt
	 */
	public void setPrincipalamt(BigDecimal principalamt)
	{
		this.principalamt = principalamt;
	}

	/**
	 * Method 'getLatefee'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLatefee()
	{
		return latefee;
	}

	/**
	 * Method 'setLatefee'
	 * 
	 * @param latefee
	 */
	public void setLatefee(BigDecimal latefee)
	{
		this.latefee = latefee;
	}

	/**
	 * Method 'getBreachfee'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBreachfee()
	{
		return breachfee;
	}

	/**
	 * Method 'setBreachfee'
	 * 
	 * @param breachfee
	 */
	public void setBreachfee(BigDecimal breachfee)
	{
		this.breachfee = breachfee;
	}

	/**
	 * Method 'getServicechargefee'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getServicechargefee()
	{
		return servicechargefee;
	}

	/**
	 * Method 'setServicechargefee'
	 * 
	 * @param servicechargefee
	 */
	public void setServicechargefee(BigDecimal servicechargefee)
	{
		this.servicechargefee = servicechargefee;
	}

	/**
	 * Method 'getBilltype'
	 * 
	 * @return String
	 */
	public String getBilltype()
	{
		return billtype;
	}

	/**
	 * Method 'setBilltype'
	 * 
	 * @param billtype
	 */
	public void setBilltype(String billtype)
	{
		this.billtype = billtype;
	}

	/**
	 * Method 'getCustomerCode'
	 * 
	 * @return String
	 */
	public String getCustomerCode()
	{
		return customerCode;
	}

	/**
	 * Method 'setCustomerCode'
	 * 
	 * @param customerCode
	 */
	public void setCustomerCode(String customerCode)
	{
		this.customerCode = customerCode;
	}

	/**
	 * Method 'getSignAccountNo'
	 * 
	 * @return String
	 */
	public String getSignAccountNo()
	{
		return signAccountNo;
	}

	/**
	 * Method 'setSignAccountNo'
	 * 
	 * @param signAccountNo
	 */
	public void setSignAccountNo(String signAccountNo)
	{
		this.signAccountNo = signAccountNo;
	}

	/**
	 * Method 'getClientno'
	 * 
	 * @return String
	 */
	public String getClientno()
	{
		return clientno;
	}

	/**
	 * Method 'setClientno'
	 * 
	 * @param clientno
	 */
	public void setClientno(String clientno)
	{
		this.clientno = clientno;
	}

	/**
	 * Method 'getClientidtype'
	 * 
	 * @return String
	 */
	public String getClientidtype()
	{
		return clientidtype;
	}

	/**
	 * Method 'setClientidtype'
	 * 
	 * @param clientidtype
	 */
	public void setClientidtype(String clientidtype)
	{
		this.clientidtype = clientidtype;
	}

	/**
	 * Method 'getClientid'
	 * 
	 * @return String
	 */
	public String getClientid()
	{
		return clientid;
	}

	/**
	 * Method 'setClientid'
	 * 
	 * @param clientid
	 */
	public void setClientid(String clientid)
	{
		this.clientid = clientid;
	}

	/**
	 * Method 'getFailurereason'
	 * 
	 * @return String
	 */
	public String getFailurereason()
	{
		return failurereason;
	}

	/**
	 * Method 'setFailurereason'
	 * 
	 * @param failurereason
	 */
	public void setFailurereason(String failurereason)
	{
		this.failurereason = failurereason;
	}

	/**
	 * Method 'getTxjournalno'
	 * 
	 * @return String
	 */
	public String getTxjournalno()
	{
		return txjournalno;
	}

	/**
	 * Method 'setTxjournalno'
	 * 
	 * @param txjournalno
	 */
	public void setTxjournalno(String txjournalno)
	{
		this.txjournalno = txjournalno;
	}

	/**
	 * Method 'equals'
	 * 
	 * @param _other
	 * @return boolean
	 */
	public boolean equals(Object _other)
	{
		if (_other == null) {
			return false;
		}
		
		if (_other == this) {
			return true;
		}
		
		if (!(_other instanceof Xfactcutpaydetl)) {
			return false;
		}
		
		final Xfactcutpaydetl _cast = (Xfactcutpaydetl) _other;
		if (journalno == null ? _cast.journalno != journalno : !journalno.equals( _cast.journalno )) {
			return false;
		}
		
		if (paybackamt == null ? _cast.paybackamt != paybackamt : !paybackamt.equals( _cast.paybackamt )) {
			return false;
		}
		
		if (paybackdate == null ? _cast.paybackdate != paybackdate : !paybackdate.equals( _cast.paybackdate )) {
			return false;
		}
		
		if (paybackact == null ? _cast.paybackact != paybackact : !paybackact.equals( _cast.paybackact )) {
			return false;
		}
		
		if (recvact == null ? _cast.recvact != recvact : !recvact.equals( _cast.recvact )) {
			return false;
		}
		
		if (paybackbankid == null ? _cast.paybackbankid != paybackbankid : !paybackbankid.equals( _cast.paybackbankid )) {
			return false;
		}
		
		if (paybackbankno == null ? _cast.paybackbankno != paybackbankno : !paybackbankno.equals( _cast.paybackbankno )) {
			return false;
		}
		
		if (recvbankid == null ? _cast.recvbankid != recvbankid : !recvbankid.equals( _cast.recvbankid )) {
			return false;
		}
		
		if (recvbankno == null ? _cast.recvbankno != recvbankno : !recvbankno.equals( _cast.recvbankno )) {
			return false;
		}
		
		if (creatorid == null ? _cast.creatorid != creatorid : !creatorid.equals( _cast.creatorid )) {
			return false;
		}
		
		if (createdate == null ? _cast.createdate != createdate : !createdate.equals( _cast.createdate )) {
			return false;
		}
		
		if (createform == null ? _cast.createform != createform : !createform.equals( _cast.createform )) {
			return false;
		}
		
		if (updatorid == null ? _cast.updatorid != updatorid : !updatorid.equals( _cast.updatorid )) {
			return false;
		}
		
		if (updatedate == null ? _cast.updatedate != updatedate : !updatedate.equals( _cast.updatedate )) {
			return false;
		}
		
		if (updateform == null ? _cast.updateform != updateform : !updateform.equals( _cast.updateform )) {
			return false;
		}
		
		if (contractno == null ? _cast.contractno != contractno : !contractno.equals( _cast.contractno )) {
			return false;
		}
		
		if (poano == null ? _cast.poano != poano : !poano.equals( _cast.poano )) {
			return false;
		}
		
		if (billstatus == null ? _cast.billstatus != billstatus : !billstatus.equals( _cast.billstatus )) {
			return false;
		}
		
		if (paidupamt == null ? _cast.paidupamt != paidupamt : !paidupamt.equals( _cast.paidupamt )) {
			return false;
		}
		
		if (clientname == null ? _cast.clientname != clientname : !clientname.equals( _cast.clientname )) {
			return false;
		}
		
		if (clientact == null ? _cast.clientact != clientact : !clientact.equals( _cast.clientact )) {
			return false;
		}
		
		if (paybackbankname == null ? _cast.paybackbankname != paybackbankname : !paybackbankname.equals( _cast.paybackbankname )) {
			return false;
		}
		
		if (startdate == null ? _cast.startdate != startdate : !startdate.equals( _cast.startdate )) {
			return false;
		}
		
		if (paidupdate == null ? _cast.paidupdate != paidupdate : !paidupdate.equals( _cast.paidupdate )) {
			return false;
		}
		
		if (operatorid == null ? _cast.operatorid != operatorid : !operatorid.equals( _cast.operatorid )) {
			return false;
		}
		
		if (operatedate == null ? _cast.operatedate != operatedate : !operatedate.equals( _cast.operatedate )) {
			return false;
		}
		
		if (checkerid == null ? _cast.checkerid != checkerid : !checkerid.equals( _cast.checkerid )) {
			return false;
		}
		
		if (checkdate == null ? _cast.checkdate != checkdate : !checkdate.equals( _cast.checkdate )) {
			return false;
		}
		
		if (paybackactname == null ? _cast.paybackactname != paybackactname : !paybackactname.equals( _cast.paybackactname )) {
			return false;
		}
		
		if (principalamt == null ? _cast.principalamt != principalamt : !principalamt.equals( _cast.principalamt )) {
			return false;
		}
		
		if (latefee == null ? _cast.latefee != latefee : !latefee.equals( _cast.latefee )) {
			return false;
		}
		
		if (breachfee == null ? _cast.breachfee != breachfee : !breachfee.equals( _cast.breachfee )) {
			return false;
		}
		
		if (servicechargefee == null ? _cast.servicechargefee != servicechargefee : !servicechargefee.equals( _cast.servicechargefee )) {
			return false;
		}
		
		if (billtype == null ? _cast.billtype != billtype : !billtype.equals( _cast.billtype )) {
			return false;
		}
		
		if (customerCode == null ? _cast.customerCode != customerCode : !customerCode.equals( _cast.customerCode )) {
			return false;
		}
		
		if (signAccountNo == null ? _cast.signAccountNo != signAccountNo : !signAccountNo.equals( _cast.signAccountNo )) {
			return false;
		}
		
		if (clientno == null ? _cast.clientno != clientno : !clientno.equals( _cast.clientno )) {
			return false;
		}
		
		if (clientidtype == null ? _cast.clientidtype != clientidtype : !clientidtype.equals( _cast.clientidtype )) {
			return false;
		}
		
		if (clientid == null ? _cast.clientid != clientid : !clientid.equals( _cast.clientid )) {
			return false;
		}
		
		if (failurereason == null ? _cast.failurereason != failurereason : !failurereason.equals( _cast.failurereason )) {
			return false;
		}
		
		if (txjournalno == null ? _cast.txjournalno != txjournalno : !txjournalno.equals( _cast.txjournalno )) {
			return false;
		}
		
		return true;
	}

	/**
	 * Method 'hashCode'
	 * 
	 * @return int
	 */
	public int hashCode()
	{
		int _hashCode = 0;
		if (journalno != null) {
			_hashCode = 29 * _hashCode + journalno.hashCode();
		}
		
		if (paybackamt != null) {
			_hashCode = 29 * _hashCode + paybackamt.hashCode();
		}
		
		if (paybackdate != null) {
			_hashCode = 29 * _hashCode + paybackdate.hashCode();
		}
		
		if (paybackact != null) {
			_hashCode = 29 * _hashCode + paybackact.hashCode();
		}
		
		if (recvact != null) {
			_hashCode = 29 * _hashCode + recvact.hashCode();
		}
		
		if (paybackbankid != null) {
			_hashCode = 29 * _hashCode + paybackbankid.hashCode();
		}
		
		if (paybackbankno != null) {
			_hashCode = 29 * _hashCode + paybackbankno.hashCode();
		}
		
		if (recvbankid != null) {
			_hashCode = 29 * _hashCode + recvbankid.hashCode();
		}
		
		if (recvbankno != null) {
			_hashCode = 29 * _hashCode + recvbankno.hashCode();
		}
		
		if (creatorid != null) {
			_hashCode = 29 * _hashCode + creatorid.hashCode();
		}
		
		if (createdate != null) {
			_hashCode = 29 * _hashCode + createdate.hashCode();
		}
		
		if (createform != null) {
			_hashCode = 29 * _hashCode + createform.hashCode();
		}
		
		if (updatorid != null) {
			_hashCode = 29 * _hashCode + updatorid.hashCode();
		}
		
		if (updatedate != null) {
			_hashCode = 29 * _hashCode + updatedate.hashCode();
		}
		
		if (updateform != null) {
			_hashCode = 29 * _hashCode + updateform.hashCode();
		}
		
		if (contractno != null) {
			_hashCode = 29 * _hashCode + contractno.hashCode();
		}
		
		if (poano != null) {
			_hashCode = 29 * _hashCode + poano.hashCode();
		}
		
		if (billstatus != null) {
			_hashCode = 29 * _hashCode + billstatus.hashCode();
		}
		
		if (paidupamt != null) {
			_hashCode = 29 * _hashCode + paidupamt.hashCode();
		}
		
		if (clientname != null) {
			_hashCode = 29 * _hashCode + clientname.hashCode();
		}
		
		if (clientact != null) {
			_hashCode = 29 * _hashCode + clientact.hashCode();
		}
		
		if (paybackbankname != null) {
			_hashCode = 29 * _hashCode + paybackbankname.hashCode();
		}
		
		if (startdate != null) {
			_hashCode = 29 * _hashCode + startdate.hashCode();
		}
		
		if (paidupdate != null) {
			_hashCode = 29 * _hashCode + paidupdate.hashCode();
		}
		
		if (operatorid != null) {
			_hashCode = 29 * _hashCode + operatorid.hashCode();
		}
		
		if (operatedate != null) {
			_hashCode = 29 * _hashCode + operatedate.hashCode();
		}
		
		if (checkerid != null) {
			_hashCode = 29 * _hashCode + checkerid.hashCode();
		}
		
		if (checkdate != null) {
			_hashCode = 29 * _hashCode + checkdate.hashCode();
		}
		
		if (paybackactname != null) {
			_hashCode = 29 * _hashCode + paybackactname.hashCode();
		}
		
		if (principalamt != null) {
			_hashCode = 29 * _hashCode + principalamt.hashCode();
		}
		
		if (latefee != null) {
			_hashCode = 29 * _hashCode + latefee.hashCode();
		}
		
		if (breachfee != null) {
			_hashCode = 29 * _hashCode + breachfee.hashCode();
		}
		
		if (servicechargefee != null) {
			_hashCode = 29 * _hashCode + servicechargefee.hashCode();
		}
		
		if (billtype != null) {
			_hashCode = 29 * _hashCode + billtype.hashCode();
		}
		
		if (customerCode != null) {
			_hashCode = 29 * _hashCode + customerCode.hashCode();
		}
		
		if (signAccountNo != null) {
			_hashCode = 29 * _hashCode + signAccountNo.hashCode();
		}
		
		if (clientno != null) {
			_hashCode = 29 * _hashCode + clientno.hashCode();
		}
		
		if (clientidtype != null) {
			_hashCode = 29 * _hashCode + clientidtype.hashCode();
		}
		
		if (clientid != null) {
			_hashCode = 29 * _hashCode + clientid.hashCode();
		}
		
		if (failurereason != null) {
			_hashCode = 29 * _hashCode + failurereason.hashCode();
		}
		
		if (txjournalno != null) {
			_hashCode = 29 * _hashCode + txjournalno.hashCode();
		}
		
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return XfactcutpaydetlPk
	 */
	public XfactcutpaydetlPk createPk()
	{
		return new XfactcutpaydetlPk(journalno);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "zt.cms.xf.common.dto.Xfactcutpaydetl: " );
		ret.append( "journalno=" + journalno );
		ret.append( ", paybackamt=" + paybackamt );
		ret.append( ", paybackdate=" + paybackdate );
		ret.append( ", paybackact=" + paybackact );
		ret.append( ", recvact=" + recvact );
		ret.append( ", paybackbankid=" + paybackbankid );
		ret.append( ", paybackbankno=" + paybackbankno );
		ret.append( ", recvbankid=" + recvbankid );
		ret.append( ", recvbankno=" + recvbankno );
		ret.append( ", creatorid=" + creatorid );
		ret.append( ", createdate=" + createdate );
		ret.append( ", createform=" + createform );
		ret.append( ", updatorid=" + updatorid );
		ret.append( ", updatedate=" + updatedate );
		ret.append( ", updateform=" + updateform );
		ret.append( ", contractno=" + contractno );
		ret.append( ", poano=" + poano );
		ret.append( ", billstatus=" + billstatus );
		ret.append( ", paidupamt=" + paidupamt );
		ret.append( ", clientname=" + clientname );
		ret.append( ", clientact=" + clientact );
		ret.append( ", paybackbankname=" + paybackbankname );
		ret.append( ", startdate=" + startdate );
		ret.append( ", paidupdate=" + paidupdate );
		ret.append( ", operatorid=" + operatorid );
		ret.append( ", operatedate=" + operatedate );
		ret.append( ", checkerid=" + checkerid );
		ret.append( ", checkdate=" + checkdate );
		ret.append( ", paybackactname=" + paybackactname );
		ret.append( ", principalamt=" + principalamt );
		ret.append( ", latefee=" + latefee );
		ret.append( ", breachfee=" + breachfee );
		ret.append( ", servicechargefee=" + servicechargefee );
		ret.append( ", billtype=" + billtype );
		ret.append( ", customerCode=" + customerCode );
		ret.append( ", signAccountNo=" + signAccountNo );
		ret.append( ", clientno=" + clientno );
		ret.append( ", clientidtype=" + clientidtype );
		ret.append( ", clientid=" + clientid );
		ret.append( ", failurereason=" + failurereason );
		ret.append( ", txjournalno=" + txjournalno );
		return ret.toString();
	}

}