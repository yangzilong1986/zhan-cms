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

public class Xfvcontractprtinfo implements Serializable
{
	/** 
	 * This attribute maps to the column CONTRACTNO in the XFVCONTRACTPRTINFO table.
	 */
	protected String contractno;

	/** 
	 * This attribute maps to the column STARTDATE in the XFVCONTRACTPRTINFO table.
	 */
	protected Date startdate;

	/** 
	 * This attribute maps to the column PLACE in the XFVCONTRACTPRTINFO table.
	 */
	protected String place;

	/** 
	 * This attribute maps to the column DURATION in the XFVCONTRACTPRTINFO table.
	 */
	protected BigDecimal duration;

	/** 
	 * This attribute maps to the column CONTRACTAMT in the XFVCONTRACTPRTINFO table.
	 */
	protected BigDecimal contractamt;

	/** 
	 * This attribute maps to the column SERVICECHARGE in the XFVCONTRACTPRTINFO table.
	 */
	protected BigDecimal servicecharge;

	/** 
	 * This attribute maps to the column PARTNERNAME in the XFVCONTRACTPRTINFO table.
	 */
	protected String partnername;

	/** 
	 * This attribute maps to the column CLIENTIDTYPE in the XFVCONTRACTPRTINFO table.
	 */
	protected String clientidtype;

	/** 
	 * This attribute maps to the column CLIENTID in the XFVCONTRACTPRTINFO table.
	 */
	protected String clientid;

	/** 
	 * This attribute maps to the column CLIENTNAME in the XFVCONTRACTPRTINFO table.
	 */
	protected String clientname;

	/** 
	 * This attribute maps to the column PAYBACKACT in the XFVCONTRACTPRTINFO table.
	 */
	protected String paybackact;

	/** 
	 * This attribute maps to the column PAYBACKACTNAME in the XFVCONTRACTPRTINFO table.
	 */
	protected String paybackactname;

	/** 
	 * This attribute maps to the column COMMNAME in the XFVCONTRACTPRTINFO table.
	 */
	protected String commname;

	/** 
	 * This attribute maps to the column NUM in the XFVCONTRACTPRTINFO table.
	 */
	protected String num;

	/** 
	 * This attribute maps to the column PC in the XFVCONTRACTPRTINFO table.
	 */
	protected String pc;

	/** 
	 * This attribute maps to the column PHONE1 in the XFVCONTRACTPRTINFO table.
	 */
	protected String phone1;

	/** 
	 * This attribute maps to the column PHONE2 in the XFVCONTRACTPRTINFO table.
	 */
	protected String phone2;

	/** 
	 * This attribute maps to the column PHONE3 in the XFVCONTRACTPRTINFO table.
	 */
	protected String phone3;

	/** 
	 * This attribute maps to the column APPNO in the XFVCONTRACTPRTINFO table.
	 */
	protected String appno;

	/** 
	 * This attribute maps to the column WITHHOLDBANKNAME in the XFVCONTRACTPRTINFO table.
	 */
	protected String withholdbankname;

	/** 
	 * This attribute maps to the column COMMAMT in the XFVCONTRACTPRTINFO table.
	 */
	protected BigDecimal commamt;

	/** 
	 * This attribute maps to the column RECEIVEAMT in the XFVCONTRACTPRTINFO table.
	 */
	protected BigDecimal receiveamt;

	/** 
	 * This attribute maps to the column APPDATE in the XFVCONTRACTPRTINFO table.
	 */
	protected Date appdate;

	/**
	 * Method 'Xfvcontractprtinfo'
	 * 
	 */
	public Xfvcontractprtinfo()
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
	 * Method 'getPlace'
	 * 
	 * @return String
	 */
	public String getPlace()
	{
		return place;
	}

	/**
	 * Method 'setPlace'
	 * 
	 * @param place
	 */
	public void setPlace(String place)
	{
		this.place = place;
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
	 * Method 'getServicecharge'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getServicecharge()
	{
		return servicecharge;
	}

	/**
	 * Method 'setServicecharge'
	 * 
	 * @param servicecharge
	 */
	public void setServicecharge(BigDecimal servicecharge)
	{
		this.servicecharge = servicecharge;
	}

	/**
	 * Method 'getPartnername'
	 * 
	 * @return String
	 */
	public String getPartnername()
	{
		return partnername;
	}

	/**
	 * Method 'setPartnername'
	 * 
	 * @param partnername
	 */
	public void setPartnername(String partnername)
	{
		this.partnername = partnername;
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
	 * Method 'getCommname'
	 * 
	 * @return String
	 */
	public String getCommname()
	{
		return commname;
	}

	/**
	 * Method 'setCommname'
	 * 
	 * @param commname
	 */
	public void setCommname(String commname)
	{
		this.commname = commname;
	}

	/**
	 * Method 'getNum'
	 * 
	 * @return String
	 */
	public String getNum()
	{
		return num;
	}

	/**
	 * Method 'setNum'
	 * 
	 * @param num
	 */
	public void setNum(String num)
	{
		this.num = num;
	}

	/**
	 * Method 'getPc'
	 * 
	 * @return String
	 */
	public String getPc()
	{
		return pc;
	}

	/**
	 * Method 'setPc'
	 * 
	 * @param pc
	 */
	public void setPc(String pc)
	{
		this.pc = pc;
	}

	/**
	 * Method 'getPhone1'
	 * 
	 * @return String
	 */
	public String getPhone1()
	{
		return phone1;
	}

	/**
	 * Method 'setPhone1'
	 * 
	 * @param phone1
	 */
	public void setPhone1(String phone1)
	{
		this.phone1 = phone1;
	}

	/**
	 * Method 'getPhone2'
	 * 
	 * @return String
	 */
	public String getPhone2()
	{
		return phone2;
	}

	/**
	 * Method 'setPhone2'
	 * 
	 * @param phone2
	 */
	public void setPhone2(String phone2)
	{
		this.phone2 = phone2;
	}

	/**
	 * Method 'getPhone3'
	 * 
	 * @return String
	 */
	public String getPhone3()
	{
		return phone3;
	}

	/**
	 * Method 'setPhone3'
	 * 
	 * @param phone3
	 */
	public void setPhone3(String phone3)
	{
		this.phone3 = phone3;
	}

	/**
	 * Method 'getAppno'
	 * 
	 * @return String
	 */
	public String getAppno()
	{
		return appno;
	}

	/**
	 * Method 'setAppno'
	 * 
	 * @param appno
	 */
	public void setAppno(String appno)
	{
		this.appno = appno;
	}

	/**
	 * Method 'getWithholdbankname'
	 * 
	 * @return String
	 */
	public String getWithholdbankname()
	{
		return withholdbankname;
	}

	/**
	 * Method 'setWithholdbankname'
	 * 
	 * @param withholdbankname
	 */
	public void setWithholdbankname(String withholdbankname)
	{
		this.withholdbankname = withholdbankname;
	}

	/**
	 * Method 'getCommamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCommamt()
	{
		return commamt;
	}

	/**
	 * Method 'setCommamt'
	 * 
	 * @param commamt
	 */
	public void setCommamt(BigDecimal commamt)
	{
		this.commamt = commamt;
	}

	/**
	 * Method 'getReceiveamt'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getReceiveamt()
	{
		return receiveamt;
	}

	/**
	 * Method 'setReceiveamt'
	 * 
	 * @param receiveamt
	 */
	public void setReceiveamt(BigDecimal receiveamt)
	{
		this.receiveamt = receiveamt;
	}

	/**
	 * Method 'getAppdate'
	 * 
	 * @return Date
	 */
	public Date getAppdate()
	{
		return appdate;
	}

	/**
	 * Method 'setAppdate'
	 * 
	 * @param appdate
	 */
	public void setAppdate(Date appdate)
	{
		this.appdate = appdate;
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
		
		if (!(_other instanceof Xfvcontractprtinfo)) {
			return false;
		}
		
		final Xfvcontractprtinfo _cast = (Xfvcontractprtinfo) _other;
		if (contractno == null ? _cast.contractno != contractno : !contractno.equals( _cast.contractno )) {
			return false;
		}
		
		if (startdate == null ? _cast.startdate != startdate : !startdate.equals( _cast.startdate )) {
			return false;
		}
		
		if (place == null ? _cast.place != place : !place.equals( _cast.place )) {
			return false;
		}
		
		if (duration == null ? _cast.duration != duration : !duration.equals( _cast.duration )) {
			return false;
		}
		
		if (contractamt == null ? _cast.contractamt != contractamt : !contractamt.equals( _cast.contractamt )) {
			return false;
		}
		
		if (servicecharge == null ? _cast.servicecharge != servicecharge : !servicecharge.equals( _cast.servicecharge )) {
			return false;
		}
		
		if (partnername == null ? _cast.partnername != partnername : !partnername.equals( _cast.partnername )) {
			return false;
		}
		
		if (clientidtype == null ? _cast.clientidtype != clientidtype : !clientidtype.equals( _cast.clientidtype )) {
			return false;
		}
		
		if (clientid == null ? _cast.clientid != clientid : !clientid.equals( _cast.clientid )) {
			return false;
		}
		
		if (clientname == null ? _cast.clientname != clientname : !clientname.equals( _cast.clientname )) {
			return false;
		}
		
		if (paybackact == null ? _cast.paybackact != paybackact : !paybackact.equals( _cast.paybackact )) {
			return false;
		}
		
		if (paybackactname == null ? _cast.paybackactname != paybackactname : !paybackactname.equals( _cast.paybackactname )) {
			return false;
		}
		
		if (commname == null ? _cast.commname != commname : !commname.equals( _cast.commname )) {
			return false;
		}
		
		if (num == null ? _cast.num != num : !num.equals( _cast.num )) {
			return false;
		}
		
		if (pc == null ? _cast.pc != pc : !pc.equals( _cast.pc )) {
			return false;
		}
		
		if (phone1 == null ? _cast.phone1 != phone1 : !phone1.equals( _cast.phone1 )) {
			return false;
		}
		
		if (phone2 == null ? _cast.phone2 != phone2 : !phone2.equals( _cast.phone2 )) {
			return false;
		}
		
		if (phone3 == null ? _cast.phone3 != phone3 : !phone3.equals( _cast.phone3 )) {
			return false;
		}
		
		if (appno == null ? _cast.appno != appno : !appno.equals( _cast.appno )) {
			return false;
		}
		
		if (withholdbankname == null ? _cast.withholdbankname != withholdbankname : !withholdbankname.equals( _cast.withholdbankname )) {
			return false;
		}
		
		if (commamt == null ? _cast.commamt != commamt : !commamt.equals( _cast.commamt )) {
			return false;
		}
		
		if (receiveamt == null ? _cast.receiveamt != receiveamt : !receiveamt.equals( _cast.receiveamt )) {
			return false;
		}
		
		if (appdate == null ? _cast.appdate != appdate : !appdate.equals( _cast.appdate )) {
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
		
		if (startdate != null) {
			_hashCode = 29 * _hashCode + startdate.hashCode();
		}
		
		if (place != null) {
			_hashCode = 29 * _hashCode + place.hashCode();
		}
		
		if (duration != null) {
			_hashCode = 29 * _hashCode + duration.hashCode();
		}
		
		if (contractamt != null) {
			_hashCode = 29 * _hashCode + contractamt.hashCode();
		}
		
		if (servicecharge != null) {
			_hashCode = 29 * _hashCode + servicecharge.hashCode();
		}
		
		if (partnername != null) {
			_hashCode = 29 * _hashCode + partnername.hashCode();
		}
		
		if (clientidtype != null) {
			_hashCode = 29 * _hashCode + clientidtype.hashCode();
		}
		
		if (clientid != null) {
			_hashCode = 29 * _hashCode + clientid.hashCode();
		}
		
		if (clientname != null) {
			_hashCode = 29 * _hashCode + clientname.hashCode();
		}
		
		if (paybackact != null) {
			_hashCode = 29 * _hashCode + paybackact.hashCode();
		}
		
		if (paybackactname != null) {
			_hashCode = 29 * _hashCode + paybackactname.hashCode();
		}
		
		if (commname != null) {
			_hashCode = 29 * _hashCode + commname.hashCode();
		}
		
		if (num != null) {
			_hashCode = 29 * _hashCode + num.hashCode();
		}
		
		if (pc != null) {
			_hashCode = 29 * _hashCode + pc.hashCode();
		}
		
		if (phone1 != null) {
			_hashCode = 29 * _hashCode + phone1.hashCode();
		}
		
		if (phone2 != null) {
			_hashCode = 29 * _hashCode + phone2.hashCode();
		}
		
		if (phone3 != null) {
			_hashCode = 29 * _hashCode + phone3.hashCode();
		}
		
		if (appno != null) {
			_hashCode = 29 * _hashCode + appno.hashCode();
		}
		
		if (withholdbankname != null) {
			_hashCode = 29 * _hashCode + withholdbankname.hashCode();
		}
		
		if (commamt != null) {
			_hashCode = 29 * _hashCode + commamt.hashCode();
		}
		
		if (receiveamt != null) {
			_hashCode = 29 * _hashCode + receiveamt.hashCode();
		}
		
		if (appdate != null) {
			_hashCode = 29 * _hashCode + appdate.hashCode();
		}
		
		return _hashCode;
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "zt.cms.xf.common.dto.Xfvcontractprtinfo: " );
		ret.append( "contractno=" + contractno );
		ret.append( ", startdate=" + startdate );
		ret.append( ", place=" + place );
		ret.append( ", duration=" + duration );
		ret.append( ", contractamt=" + contractamt );
		ret.append( ", servicecharge=" + servicecharge );
		ret.append( ", partnername=" + partnername );
		ret.append( ", clientidtype=" + clientidtype );
		ret.append( ", clientid=" + clientid );
		ret.append( ", clientname=" + clientname );
		ret.append( ", paybackact=" + paybackact );
		ret.append( ", paybackactname=" + paybackactname );
		ret.append( ", commname=" + commname );
		ret.append( ", num=" + num );
		ret.append( ", pc=" + pc );
		ret.append( ", phone1=" + phone1 );
		ret.append( ", phone2=" + phone2 );
		ret.append( ", phone3=" + phone3 );
		ret.append( ", appno=" + appno );
		ret.append( ", withholdbankname=" + withholdbankname );
		ret.append( ", commamt=" + commamt );
		ret.append( ", receiveamt=" + receiveamt );
		ret.append( ", appdate=" + appdate );
		return ret.toString();
	}

}
