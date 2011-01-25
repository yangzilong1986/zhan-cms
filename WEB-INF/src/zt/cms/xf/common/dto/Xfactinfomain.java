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
import java.util.Date;
import java.math.BigDecimal;

public class Xfactinfomain implements Serializable
{
	/** 
	 * This attribute maps to the column CONTRACTNO in the XFACTINFOMAIN table.
	 */
	protected String contractno;

	/** 
	 * This attribute maps to the column POANO in the XFACTINFOMAIN table.
	 */
	protected String poano;

	/** 
	 * This attribute maps to the column STARTDATE in the XFACTINFOMAIN table.
	 */
	protected Date startdate;

	/** 
	 * This attribute maps to the column ENDDATE in the XFACTINFOMAIN table.
	 */
	protected Date enddate;

	/** 
	 * This attribute maps to the column DURATION in the XFACTINFOMAIN table.
	 */
	protected BigDecimal duration;

	/** 
	 * This attribute maps to the column CURNO in the XFACTINFOMAIN table.
	 */
	protected String curno;

	/** 
	 * This attribute maps to the column CONTRACTAMT in the XFACTINFOMAIN table.
	 */
	protected BigDecimal contractamt;

	/** 
	 * This attribute maps to the column PRINCIPALAMT in the XFACTINFOMAIN table.
	 */
	protected BigDecimal principalamt;

	/** 
	 * This attribute maps to the column FSERVICECHARGE in the XFACTINFOMAIN table.
	 */
	protected BigDecimal fservicecharge;

	/** 
	 * This attribute maps to the column POASTATUS in the XFACTINFOMAIN table.
	 */
	protected BigDecimal poastatus;

	/** 
	 * This attribute maps to the column PAYBAKAMT in the XFACTINFOMAIN table.
	 */
	protected BigDecimal paybakamt;

	/** 
	 * This attribute maps to the column LATEFEEAMT in the XFACTINFOMAIN table.
	 */
	protected BigDecimal latefeeamt;

	/** 
	 * This attribute maps to the column BREACHFEEAMT in the XFACTINFOMAIN table.
	 */
	protected BigDecimal breachfeeamt;

	/** 
	 * This attribute maps to the column MANAGERFEEAMT in the XFACTINFOMAIN table.
	 */
	protected BigDecimal managerfeeamt;

	/** 
	 * This attribute maps to the column OVERDUETIMES in the XFACTINFOMAIN table.
	 */
	protected BigDecimal overduetimes;

	/** 
	 * This attribute maps to the column CLIENTACT in the XFACTINFOMAIN table.
	 */
	protected String clientact;

	/** 
	 * This attribute maps to the column PAYBACKACT in the XFACTINFOMAIN table.
	 */
	protected String paybackact;

	/** 
	 * This attribute maps to the column PAYBACKBANKID in the XFACTINFOMAIN table.
	 */
	protected String paybackbankid;

	/** 
	 * This attribute maps to the column PAYBACKBANKNO in the XFACTINFOMAIN table.
	 */
	protected String paybackbankno;

	/** 
	 * This attribute maps to the column RECVACT in the XFACTINFOMAIN table.
	 */
	protected String recvact;

	/** 
	 * This attribute maps to the column RECVBANKID in the XFACTINFOMAIN table.
	 */
	protected String recvbankid;

	/** 
	 * This attribute maps to the column RECVBANKNO in the XFACTINFOMAIN table.
	 */
	protected String recvbankno;

	/** 
	 * This attribute maps to the column RECVBANKNAME in the XFACTINFOMAIN table.
	 */
	protected String recvbankname;

	/** 
	 * This attribute maps to the column OPERATORID in the XFACTINFOMAIN table.
	 */
	protected String operatorid;

	/** 
	 * This attribute maps to the column OPERATEDATE in the XFACTINFOMAIN table.
	 */
	protected Date operatedate;

	/** 
	 * This attribute maps to the column CHECKERID in the XFACTINFOMAIN table.
	 */
	protected String checkerid;

	/** 
	 * This attribute maps to the column CHECKDATE in the XFACTINFOMAIN table.
	 */
	protected Date checkdate;

	/** 
	 * This attribute maps to the column CREATORID in the XFACTINFOMAIN table.
	 */
	protected String creatorid;

	/** 
	 * This attribute maps to the column CREATEDATE in the XFACTINFOMAIN table.
	 */
	protected Date createdate;

	/** 
	 * This attribute maps to the column CREATEFORM in the XFACTINFOMAIN table.
	 */
	protected String createform;

	/** 
	 * This attribute maps to the column UPDATORID in the XFACTINFOMAIN table.
	 */
	protected String updatorid;

	/** 
	 * This attribute maps to the column UPDATEDATE in the XFACTINFOMAIN table.
	 */
	protected Date updatedate;

	/** 
	 * This attribute maps to the column UPDATEFORM in the XFACTINFOMAIN table.
	 */
	protected String updateform;

	/**
	 * Method 'Xfactinfomain'
	 * 
	 */
	public Xfactinfomain()
	{
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
	 * @return String
	 */
	public String getPoano()
	{
		return poano;
	}

	/**
	 * Method 'setPoano'
	 * 
	 * @param poano
	 */
	public void setPoano(String poano)
	{
		this.poano = poano;
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
	 * Method 'getEnddate'
	 * 
	 * @return Date
	 */
	public Date getEnddate()
	{
		return enddate;
	}

	/**
	 * Method 'setEnddate'
	 * 
	 * @param enddate
	 */
	public void setEnddate(Date enddate)
	{
		this.enddate = enddate;
	}

	/**
	 * Method 'getDuration'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDuration()
	{
		return duration;
	}

	/**
	 * Method 'setDuration'
	 * 
	 * @param duration
	 */
	public void setDuration(BigDecimal duration)
	{
		this.duration = duration;
	}

	/**
	 * Method 'getCurno'
	 * 
	 * @return String
	 */
	public String getCurno()
	{
		return curno;
	}

	/**
	 * Method 'setCurno'
	 * 
	 * @param curno
	 */
	public void setCurno(String curno)
	{
		this.curno = curno;
	}

	/**
	 * Method 'getContractamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getContractamt()
	{
		return contractamt;
	}

	/**
	 * Method 'setContractamt'
	 * 
	 * @param contractamt
	 */
	public void setContractamt(BigDecimal contractamt)
	{
		this.contractamt = contractamt;
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
	 * Method 'getFservicecharge'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFservicecharge()
	{
		return fservicecharge;
	}

	/**
	 * Method 'setFservicecharge'
	 * 
	 * @param fservicecharge
	 */
	public void setFservicecharge(BigDecimal fservicecharge)
	{
		this.fservicecharge = fservicecharge;
	}

	/**
	 * Method 'getPoastatus'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPoastatus()
	{
		return poastatus;
	}

	/**
	 * Method 'setPoastatus'
	 * 
	 * @param poastatus
	 */
	public void setPoastatus(BigDecimal poastatus)
	{
		this.poastatus = poastatus;
	}

	/**
	 * Method 'getPaybakamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPaybakamt()
	{
		return paybakamt;
	}

	/**
	 * Method 'setPaybakamt'
	 * 
	 * @param paybakamt
	 */
	public void setPaybakamt(BigDecimal paybakamt)
	{
		this.paybakamt = paybakamt;
	}

	/**
	 * Method 'getLatefeeamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLatefeeamt()
	{
		return latefeeamt;
	}

	/**
	 * Method 'setLatefeeamt'
	 * 
	 * @param latefeeamt
	 */
	public void setLatefeeamt(BigDecimal latefeeamt)
	{
		this.latefeeamt = latefeeamt;
	}

	/**
	 * Method 'getBreachfeeamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBreachfeeamt()
	{
		return breachfeeamt;
	}

	/**
	 * Method 'setBreachfeeamt'
	 * 
	 * @param breachfeeamt
	 */
	public void setBreachfeeamt(BigDecimal breachfeeamt)
	{
		this.breachfeeamt = breachfeeamt;
	}

	/**
	 * Method 'getManagerfeeamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getManagerfeeamt()
	{
		return managerfeeamt;
	}

	/**
	 * Method 'setManagerfeeamt'
	 * 
	 * @param managerfeeamt
	 */
	public void setManagerfeeamt(BigDecimal managerfeeamt)
	{
		this.managerfeeamt = managerfeeamt;
	}

	/**
	 * Method 'getOverduetimes'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOverduetimes()
	{
		return overduetimes;
	}

	/**
	 * Method 'setOverduetimes'
	 * 
	 * @param overduetimes
	 */
	public void setOverduetimes(BigDecimal overduetimes)
	{
		this.overduetimes = overduetimes;
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
	 * Method 'getRecvbankname'
	 * 
	 * @return String
	 */
	public String getRecvbankname()
	{
		return recvbankname;
	}

	/**
	 * Method 'setRecvbankname'
	 * 
	 * @param recvbankname
	 */
	public void setRecvbankname(String recvbankname)
	{
		this.recvbankname = recvbankname;
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
		
		if (!(_other instanceof Xfactinfomain)) {
			return false;
		}
		
		final Xfactinfomain _cast = (Xfactinfomain) _other;
		if (contractno == null ? _cast.contractno != contractno : !contractno.equals( _cast.contractno )) {
			return false;
		}
		
		if (poano == null ? _cast.poano != poano : !poano.equals( _cast.poano )) {
			return false;
		}
		
		if (startdate == null ? _cast.startdate != startdate : !startdate.equals( _cast.startdate )) {
			return false;
		}
		
		if (enddate == null ? _cast.enddate != enddate : !enddate.equals( _cast.enddate )) {
			return false;
		}
		
		if (duration == null ? _cast.duration != duration : !duration.equals( _cast.duration )) {
			return false;
		}
		
		if (curno == null ? _cast.curno != curno : !curno.equals( _cast.curno )) {
			return false;
		}
		
		if (contractamt == null ? _cast.contractamt != contractamt : !contractamt.equals( _cast.contractamt )) {
			return false;
		}
		
		if (principalamt == null ? _cast.principalamt != principalamt : !principalamt.equals( _cast.principalamt )) {
			return false;
		}
		
		if (fservicecharge == null ? _cast.fservicecharge != fservicecharge : !fservicecharge.equals( _cast.fservicecharge )) {
			return false;
		}
		
		if (poastatus == null ? _cast.poastatus != poastatus : !poastatus.equals( _cast.poastatus )) {
			return false;
		}
		
		if (paybakamt == null ? _cast.paybakamt != paybakamt : !paybakamt.equals( _cast.paybakamt )) {
			return false;
		}
		
		if (latefeeamt == null ? _cast.latefeeamt != latefeeamt : !latefeeamt.equals( _cast.latefeeamt )) {
			return false;
		}
		
		if (breachfeeamt == null ? _cast.breachfeeamt != breachfeeamt : !breachfeeamt.equals( _cast.breachfeeamt )) {
			return false;
		}
		
		if (managerfeeamt == null ? _cast.managerfeeamt != managerfeeamt : !managerfeeamt.equals( _cast.managerfeeamt )) {
			return false;
		}
		
		if (overduetimes == null ? _cast.overduetimes != overduetimes : !overduetimes.equals( _cast.overduetimes )) {
			return false;
		}
		
		if (clientact == null ? _cast.clientact != clientact : !clientact.equals( _cast.clientact )) {
			return false;
		}
		
		if (paybackact == null ? _cast.paybackact != paybackact : !paybackact.equals( _cast.paybackact )) {
			return false;
		}
		
		if (paybackbankid == null ? _cast.paybackbankid != paybackbankid : !paybackbankid.equals( _cast.paybackbankid )) {
			return false;
		}
		
		if (paybackbankno == null ? _cast.paybackbankno != paybackbankno : !paybackbankno.equals( _cast.paybackbankno )) {
			return false;
		}
		
		if (recvact == null ? _cast.recvact != recvact : !recvact.equals( _cast.recvact )) {
			return false;
		}
		
		if (recvbankid == null ? _cast.recvbankid != recvbankid : !recvbankid.equals( _cast.recvbankid )) {
			return false;
		}
		
		if (recvbankno == null ? _cast.recvbankno != recvbankno : !recvbankno.equals( _cast.recvbankno )) {
			return false;
		}
		
		if (recvbankname == null ? _cast.recvbankname != recvbankname : !recvbankname.equals( _cast.recvbankname )) {
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
		if (contractno != null) {
			_hashCode = 29 * _hashCode + contractno.hashCode();
		}
		
		if (poano != null) {
			_hashCode = 29 * _hashCode + poano.hashCode();
		}
		
		if (startdate != null) {
			_hashCode = 29 * _hashCode + startdate.hashCode();
		}
		
		if (enddate != null) {
			_hashCode = 29 * _hashCode + enddate.hashCode();
		}
		
		if (duration != null) {
			_hashCode = 29 * _hashCode + duration.hashCode();
		}
		
		if (curno != null) {
			_hashCode = 29 * _hashCode + curno.hashCode();
		}
		
		if (contractamt != null) {
			_hashCode = 29 * _hashCode + contractamt.hashCode();
		}
		
		if (principalamt != null) {
			_hashCode = 29 * _hashCode + principalamt.hashCode();
		}
		
		if (fservicecharge != null) {
			_hashCode = 29 * _hashCode + fservicecharge.hashCode();
		}
		
		if (poastatus != null) {
			_hashCode = 29 * _hashCode + poastatus.hashCode();
		}
		
		if (paybakamt != null) {
			_hashCode = 29 * _hashCode + paybakamt.hashCode();
		}
		
		if (latefeeamt != null) {
			_hashCode = 29 * _hashCode + latefeeamt.hashCode();
		}
		
		if (breachfeeamt != null) {
			_hashCode = 29 * _hashCode + breachfeeamt.hashCode();
		}
		
		if (managerfeeamt != null) {
			_hashCode = 29 * _hashCode + managerfeeamt.hashCode();
		}
		
		if (overduetimes != null) {
			_hashCode = 29 * _hashCode + overduetimes.hashCode();
		}
		
		if (clientact != null) {
			_hashCode = 29 * _hashCode + clientact.hashCode();
		}
		
		if (paybackact != null) {
			_hashCode = 29 * _hashCode + paybackact.hashCode();
		}
		
		if (paybackbankid != null) {
			_hashCode = 29 * _hashCode + paybackbankid.hashCode();
		}
		
		if (paybackbankno != null) {
			_hashCode = 29 * _hashCode + paybackbankno.hashCode();
		}
		
		if (recvact != null) {
			_hashCode = 29 * _hashCode + recvact.hashCode();
		}
		
		if (recvbankid != null) {
			_hashCode = 29 * _hashCode + recvbankid.hashCode();
		}
		
		if (recvbankno != null) {
			_hashCode = 29 * _hashCode + recvbankno.hashCode();
		}
		
		if (recvbankname != null) {
			_hashCode = 29 * _hashCode + recvbankname.hashCode();
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
		
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return XfactinfomainPk
	 */
	public XfactinfomainPk createPk()
	{
		return new XfactinfomainPk(contractno);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "zt.cms.xf.common.dto.Xfactinfomain: " );
		ret.append( "contractno=" + contractno );
		ret.append( ", poano=" + poano );
		ret.append( ", startdate=" + startdate );
		ret.append( ", enddate=" + enddate );
		ret.append( ", duration=" + duration );
		ret.append( ", curno=" + curno );
		ret.append( ", contractamt=" + contractamt );
		ret.append( ", principalamt=" + principalamt );
		ret.append( ", fservicecharge=" + fservicecharge );
		ret.append( ", poastatus=" + poastatus );
		ret.append( ", paybakamt=" + paybakamt );
		ret.append( ", latefeeamt=" + latefeeamt );
		ret.append( ", breachfeeamt=" + breachfeeamt );
		ret.append( ", managerfeeamt=" + managerfeeamt );
		ret.append( ", overduetimes=" + overduetimes );
		ret.append( ", clientact=" + clientact );
		ret.append( ", paybackact=" + paybackact );
		ret.append( ", paybackbankid=" + paybackbankid );
		ret.append( ", paybackbankno=" + paybackbankno );
		ret.append( ", recvact=" + recvact );
		ret.append( ", recvbankid=" + recvbankid );
		ret.append( ", recvbankno=" + recvbankno );
		ret.append( ", recvbankname=" + recvbankname );
		ret.append( ", operatorid=" + operatorid );
		ret.append( ", operatedate=" + operatedate );
		ret.append( ", checkerid=" + checkerid );
		ret.append( ", checkdate=" + checkdate );
		ret.append( ", creatorid=" + creatorid );
		ret.append( ", createdate=" + createdate );
		ret.append( ", createform=" + createform );
		ret.append( ", updatorid=" + updatorid );
		ret.append( ", updatedate=" + updatedate );
		ret.append( ", updateform=" + updateform );
		return ret.toString();
	}

}