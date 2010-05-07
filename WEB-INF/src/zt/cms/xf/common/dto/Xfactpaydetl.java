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

public class Xfactpaydetl implements Serializable
{
	/** 
	 * This attribute maps to the column JOURNALNO in the XFACTPAYDETL table.
	 */
	protected String journalno;

	/** 
	 * This attribute maps to the column CONTRACTNO in the XFACTPAYDETL table.
	 */
	protected String contractno;

	/** 
	 * This attribute maps to the column CPANO in the XFACTPAYDETL table.
	 */
	protected String cpano;

	/** 
	 * This attribute maps to the column PAYDATE in the XFACTPAYDETL table.
	 */
	protected Date paydate;

	/** 
	 * This attribute maps to the column PAYSTATUS in the XFACTPAYDETL table.
	 */
	protected String paystatus;

	/** 
	 * This attribute maps to the column PAYAMT in the XFACTPAYDETL table.
	 */
	protected BigDecimal payamt;

	/** 
	 * This attribute maps to the column PAYACT in the XFACTPAYDETL table.
	 */
	protected String payact;

	/** 
	 * This attribute maps to the column RECVACT in the XFACTPAYDETL table.
	 */
	protected String recvact;

	/** 
	 * This attribute maps to the column RECVBANKID in the XFACTPAYDETL table.
	 */
	protected String recvbankid;

	/** 
	 * This attribute maps to the column RECVBANKNO in the XFACTPAYDETL table.
	 */
	protected String recvbankno;

	/** 
	 * This attribute maps to the column RECVBANKNAME in the XFACTPAYDETL table.
	 */
	protected String recvbankname;

	/** 
	 * This attribute maps to the column OPERATORID in the XFACTPAYDETL table.
	 */
	protected String operatorid;

	/** 
	 * This attribute maps to the column OPERATEDATE in the XFACTPAYDETL table.
	 */
	protected Date operatedate;

	/** 
	 * This attribute maps to the column CHECKERID in the XFACTPAYDETL table.
	 */
	protected String checkerid;

	/** 
	 * This attribute maps to the column CHECKDATE in the XFACTPAYDETL table.
	 */
	protected Date checkdate;

	/**
	 * Method 'Xfactpaydetl'
	 * 
	 */
	public Xfactpaydetl()
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
	 * Method 'getCpano'
	 * 
	 * @return String
	 */
	public String getCpano()
	{
		return cpano;
	}

	/**
	 * Method 'setCpano'
	 * 
	 * @param cpano
	 */
	public void setCpano(String cpano)
	{
		this.cpano = cpano;
	}

	/**
	 * Method 'getPaydate'
	 * 
	 * @return Date
	 */
	public Date getPaydate()
	{
		return paydate;
	}

	/**
	 * Method 'setPaydate'
	 * 
	 * @param paydate
	 */
	public void setPaydate(Date paydate)
	{
		this.paydate = paydate;
	}

	/**
	 * Method 'getPaystatus'
	 * 
	 * @return String
	 */
	public String getPaystatus()
	{
		return paystatus;
	}

	/**
	 * Method 'setPaystatus'
	 * 
	 * @param paystatus
	 */
	public void setPaystatus(String paystatus)
	{
		this.paystatus = paystatus;
	}

	/**
	 * Method 'getPayamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPayamt()
	{
		return payamt;
	}

	/**
	 * Method 'setPayamt'
	 * 
	 * @param payamt
	 */
	public void setPayamt(BigDecimal payamt)
	{
		this.payamt = payamt;
	}

	/**
	 * Method 'getPayact'
	 * 
	 * @return String
	 */
	public String getPayact()
	{
		return payact;
	}

	/**
	 * Method 'setPayact'
	 * 
	 * @param payact
	 */
	public void setPayact(String payact)
	{
		this.payact = payact;
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
		
		if (!(_other instanceof Xfactpaydetl)) {
			return false;
		}
		
		final Xfactpaydetl _cast = (Xfactpaydetl) _other;
		if (journalno == null ? _cast.journalno != journalno : !journalno.equals( _cast.journalno )) {
			return false;
		}
		
		if (contractno == null ? _cast.contractno != contractno : !contractno.equals( _cast.contractno )) {
			return false;
		}
		
		if (cpano == null ? _cast.cpano != cpano : !cpano.equals( _cast.cpano )) {
			return false;
		}
		
		if (paydate == null ? _cast.paydate != paydate : !paydate.equals( _cast.paydate )) {
			return false;
		}
		
		if (paystatus == null ? _cast.paystatus != paystatus : !paystatus.equals( _cast.paystatus )) {
			return false;
		}
		
		if (payamt == null ? _cast.payamt != payamt : !payamt.equals( _cast.payamt )) {
			return false;
		}
		
		if (payact == null ? _cast.payact != payact : !payact.equals( _cast.payact )) {
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
		
		if (contractno != null) {
			_hashCode = 29 * _hashCode + contractno.hashCode();
		}
		
		if (cpano != null) {
			_hashCode = 29 * _hashCode + cpano.hashCode();
		}
		
		if (paydate != null) {
			_hashCode = 29 * _hashCode + paydate.hashCode();
		}
		
		if (paystatus != null) {
			_hashCode = 29 * _hashCode + paystatus.hashCode();
		}
		
		if (payamt != null) {
			_hashCode = 29 * _hashCode + payamt.hashCode();
		}
		
		if (payact != null) {
			_hashCode = 29 * _hashCode + payact.hashCode();
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
		
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return XfactpaydetlPk
	 */
	public XfactpaydetlPk createPk()
	{
		return new XfactpaydetlPk(journalno);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "zt.cms.xf.common.dto.Xfactpaydetl: " );
		ret.append( "journalno=" + journalno );
		ret.append( ", contractno=" + contractno );
		ret.append( ", cpano=" + cpano );
		ret.append( ", paydate=" + paydate );
		ret.append( ", paystatus=" + paystatus );
		ret.append( ", payamt=" + payamt );
		ret.append( ", payact=" + payact );
		ret.append( ", recvact=" + recvact );
		ret.append( ", recvbankid=" + recvbankid );
		ret.append( ", recvbankno=" + recvbankno );
		ret.append( ", recvbankname=" + recvbankname );
		ret.append( ", operatorid=" + operatorid );
		ret.append( ", operatedate=" + operatedate );
		ret.append( ", checkerid=" + checkerid );
		ret.append( ", checkdate=" + checkdate );
		return ret.toString();
	}

}
