/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package zt.cms.xf.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** 
 * This class represents the primary key of the XFACTPAYDETL table.
 */
public class XfactpaydetlPk implements Serializable
{
	protected String journalno;

	/** 
	 * Sets the value of journalno
	 */
	public void setJournalno(String journalno)
	{
		this.journalno = journalno;
	}

	/** 
	 * Gets the value of journalno
	 */
	public String getJournalno()
	{
		return journalno;
	}

	/**
	 * Method 'XfactpaydetlPk'
	 * 
	 */
	public XfactpaydetlPk()
	{
	}

	/**
	 * Method 'XfactpaydetlPk'
	 * 
	 * @param journalno
	 */
	public XfactpaydetlPk(final String journalno)
	{
		this.journalno = journalno;
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
		
		if (!(_other instanceof XfactpaydetlPk)) {
			return false;
		}
		
		final XfactpaydetlPk _cast = (XfactpaydetlPk) _other;
		if (journalno == null ? _cast.journalno != journalno : !journalno.equals( _cast.journalno )) {
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
		ret.append( "zt.cms.xf.common.dto.XfactpaydetlPk: " );
		ret.append( "journalno=" + journalno );
		return ret.toString();
	}

}
