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

public class Xfcontractpays implements Serializable
{
	/** 
	 * This attribute maps to the column CONTRACTNO in the XFCONTRACTPAYS table.
	 */
	protected String contractno;

	/** 
	 * This attribute maps to the column CPAAMT in the XFCONTRACTPAYS table.
	 */
	protected BigDecimal cpaamt;

	/** 
	 * This attribute maps to the column STARTDATE in the XFCONTRACTPAYS table.
	 */
	protected Date startdate;

	/** 
	 * This attribute maps to the column GRACEPERIOD in the XFCONTRACTPAYS table.
	 */
	protected BigDecimal graceperiod;

	/** 
	 * This attribute maps to the column POANO in the XFCONTRACTPAYS table.
	 */
	protected BigDecimal poano;

	/** 
	 * This attribute maps to the column PAYBACKACT in the XFCONTRACTPAYS table.
	 */
	protected String paybackact;

	/** 
	 * This attribute maps to the column RECVACT in the XFCONTRACTPAYS table.
	 */
	protected String recvact;

	/** 
	 * This attribute maps to the column CPATYPE in the XFCONTRACTPAYS table.
	 */
	protected BigDecimal cpatype;

	/** 
	 * This attribute maps to the column PAYBACKBANKID in the XFCONTRACTPAYS table.
	 */
	protected String paybackbankid;

	/** 
	 * This attribute maps to the column PAYBACKBANKNO in the XFCONTRACTPAYS table.
	 */
	protected String paybackbankno;

	/** 
	 * This attribute maps to the column RECVBANKID in the XFCONTRACTPAYS table.
	 */
	protected String recvbankid;

	/** 
	 * This attribute maps to the column RECVBANKNO in the XFCONTRACTPAYS table.
	 */
	protected String recvbankno;

	/** 
	 * This attribute maps to the column CREATORID in the XFCONTRACTPAYS table.
	 */
	protected String creatorid;

	/** 
	 * This attribute maps to the column CREATEDATE in the XFCONTRACTPAYS table.
	 */
	protected Date createdate;

	/** 
	 * This attribute maps to the column CREATEFORM in the XFCONTRACTPAYS table.
	 */
	protected String createform;

	/** 
	 * This attribute maps to the column UPDATORID in the XFCONTRACTPAYS table.
	 */
	protected String updatorid;

	/** 
	 * This attribute maps to the column UPDATEDATE in the XFCONTRACTPAYS table.
	 */
	protected Date updatedate;

	/** 
	 * This attribute maps to the column UPDATEFORM in the XFCONTRACTPAYS table.
	 */
	protected String updateform;

	/** 
	 * This attribute maps to the column PAYBACKACTNAME in the XFCONTRACTPAYS table.
	 */
	protected String paybackactname;

	/** 
	 * This attribute maps to the column PRINCIPALAMT in the XFCONTRACTPAYS table.
	 */
	protected BigDecimal principalamt;

	/** 
	 * This attribute maps to the column SERVICECHARGEFEE in the XFCONTRACTPAYS table.
	 */
	protected BigDecimal servicechargefee;

	/**
	 * Method 'Xfcontractpays'
	 * 
	 */
	public Xfcontractpays()
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
	 * Method 'getCpaamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCpaamt()
	{
		return cpaamt;
	}

	/**
	 * Method 'setCpaamt'
	 * 
	 * @param cpaamt
	 */
	public void setCpaamt(BigDecimal cpaamt)
	{
		this.cpaamt = cpaamt;
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
	 * Method 'getGraceperiod'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getGraceperiod()
	{
		return graceperiod;
	}

	/**
	 * Method 'setGraceperiod'
	 * 
	 * @param graceperiod
	 */
	public void setGraceperiod(BigDecimal graceperiod)
	{
		this.graceperiod = graceperiod;
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
	 * Method 'getCpatype'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCpatype()
	{
		return cpatype;
	}

	/**
	 * Method 'setCpatype'
	 * 
	 * @param cpatype
	 */
	public void setCpatype(BigDecimal cpatype)
	{
		this.cpatype = cpatype;
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
		
		if (!(_other instanceof Xfcontractpays)) {
			return false;
		}
		
		final Xfcontractpays _cast = (Xfcontractpays) _other;
		if (contractno == null ? _cast.contractno != contractno : !contractno.equals( _cast.contractno )) {
			return false;
		}
		
		if (cpaamt == null ? _cast.cpaamt != cpaamt : !cpaamt.equals( _cast.cpaamt )) {
			return false;
		}
		
		if (startdate == null ? _cast.startdate != startdate : !startdate.equals( _cast.startdate )) {
			return false;
		}
		
		if (graceperiod == null ? _cast.graceperiod != graceperiod : !graceperiod.equals( _cast.graceperiod )) {
			return false;
		}
		
		if (poano == null ? _cast.poano != poano : !poano.equals( _cast.poano )) {
			return false;
		}
		
		if (paybackact == null ? _cast.paybackact != paybackact : !paybackact.equals( _cast.paybackact )) {
			return false;
		}
		
		if (recvact == null ? _cast.recvact != recvact : !recvact.equals( _cast.recvact )) {
			return false;
		}
		
		if (cpatype == null ? _cast.cpatype != cpatype : !cpatype.equals( _cast.cpatype )) {
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
		
		if (paybackactname == null ? _cast.paybackactname != paybackactname : !paybackactname.equals( _cast.paybackactname )) {
			return false;
		}
		
		if (principalamt == null ? _cast.principalamt != principalamt : !principalamt.equals( _cast.principalamt )) {
			return false;
		}
		
		if (servicechargefee == null ? _cast.servicechargefee != servicechargefee : !servicechargefee.equals( _cast.servicechargefee )) {
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
		
		if (cpaamt != null) {
			_hashCode = 29 * _hashCode + cpaamt.hashCode();
		}
		
		if (startdate != null) {
			_hashCode = 29 * _hashCode + startdate.hashCode();
		}
		
		if (graceperiod != null) {
			_hashCode = 29 * _hashCode + graceperiod.hashCode();
		}
		
		if (poano != null) {
			_hashCode = 29 * _hashCode + poano.hashCode();
		}
		
		if (paybackact != null) {
			_hashCode = 29 * _hashCode + paybackact.hashCode();
		}
		
		if (recvact != null) {
			_hashCode = 29 * _hashCode + recvact.hashCode();
		}
		
		if (cpatype != null) {
			_hashCode = 29 * _hashCode + cpatype.hashCode();
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
		
		if (paybackactname != null) {
			_hashCode = 29 * _hashCode + paybackactname.hashCode();
		}
		
		if (principalamt != null) {
			_hashCode = 29 * _hashCode + principalamt.hashCode();
		}
		
		if (servicechargefee != null) {
			_hashCode = 29 * _hashCode + servicechargefee.hashCode();
		}
		
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return XfcontractpaysPk
	 */
	public XfcontractpaysPk createPk()
	{
		return new XfcontractpaysPk(contractno, poano);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "zt.cms.xf.common.dto.Xfcontractpays: " );
		ret.append( "contractno=" + contractno );
		ret.append( ", cpaamt=" + cpaamt );
		ret.append( ", startdate=" + startdate );
		ret.append( ", graceperiod=" + graceperiod );
		ret.append( ", poano=" + poano );
		ret.append( ", paybackact=" + paybackact );
		ret.append( ", recvact=" + recvact );
		ret.append( ", cpatype=" + cpatype );
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
		ret.append( ", paybackactname=" + paybackactname );
		ret.append( ", principalamt=" + principalamt );
		ret.append( ", servicechargefee=" + servicechargefee );
		return ret.toString();
	}

}
