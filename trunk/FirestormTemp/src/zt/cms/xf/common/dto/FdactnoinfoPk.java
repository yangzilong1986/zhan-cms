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
 * This class represents the primary key of the FDACTNOINFO table.
 */
public class FdactnoinfoPk implements Serializable
{
	protected String contractno;

	/** 
	 * Sets the value of contractno
	 */
	public void setContractno(String contractno)
	{
		this.contractno = contractno;
	}

	/** 
	 * Gets the value of contractno
	 */
	public String getContractno()
	{
		return contractno;
	}

	/**
	 * Method 'FdactnoinfoPk'
	 * 
	 */
	public FdactnoinfoPk()
	{
	}

	/**
	 * Method 'FdactnoinfoPk'
	 * 
	 * @param contractno
	 */
	public FdactnoinfoPk(final String contractno)
	{
		this.contractno = contractno;
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
		
		if (!(_other instanceof FdactnoinfoPk)) {
			return false;
		}
		
		final FdactnoinfoPk _cast = (FdactnoinfoPk) _other;
		if (contractno == null ? _cast.contractno != contractno : !contractno.equals( _cast.contractno )) {
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
		ret.append( "zt.cms.xf.common.dto.FdactnoinfoPk: " );
		ret.append( "contractno=" + contractno );
		return ret.toString();
	}

}
