/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package zt.cms.xf.common.dao;

import java.util.Date;
import java.math.BigDecimal;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.exceptions.*;

public interface XfvcontractprtinfoDao
{
	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria ''.
	 */
	public Xfvcontractprtinfo[] findAll() throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'APPNO = :appno'.
	 */
	public Xfvcontractprtinfo findWhereAppnoEquals(String appno) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'CONTRACTNO = :contractno'.
	 */
	public Xfvcontractprtinfo findWhereContractnoEquals(String contractno) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'STARTDATE = :startdate'.
	 */
	public Xfvcontractprtinfo[] findWhereStartdateEquals(Date startdate) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'PLACE = :place'.
	 */
	public Xfvcontractprtinfo[] findWherePlaceEquals(String place) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'DURATION = :duration'.
	 */
	public Xfvcontractprtinfo[] findWhereDurationEquals(BigDecimal duration) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'CONTRACTAMT = :contractamt'.
	 */
	public Xfvcontractprtinfo[] findWhereContractamtEquals(BigDecimal contractamt) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'SERVICECHARGE = :servicecharge'.
	 */
	public Xfvcontractprtinfo[] findWhereServicechargeEquals(BigDecimal servicecharge) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'PARTNERNAME = :partnername'.
	 */
	public Xfvcontractprtinfo[] findWherePartnernameEquals(String partnername) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'CLIENTIDTYPE = :clientidtype'.
	 */
	public Xfvcontractprtinfo[] findWhereClientidtypeEquals(String clientidtype) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'CLIENTID = :clientid'.
	 */
	public Xfvcontractprtinfo[] findWhereClientidEquals(String clientid) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'CLIENTNAME = :clientname'.
	 */
	public Xfvcontractprtinfo[] findWhereClientnameEquals(String clientname) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'PAYBACKACT = :paybackact'.
	 */
	public Xfvcontractprtinfo[] findWherePaybackactEquals(String paybackact) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'PAYBACKACTNAME = :paybackactname'.
	 */
	public Xfvcontractprtinfo[] findWherePaybackactnameEquals(String paybackactname) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'PAYBACKBANKNAME = :paybackbankname'.
	 */
	public Xfvcontractprtinfo[] findWherePaybackbanknameEquals(String paybackbankname) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'COMMNAME = :commname'.
	 */
	public Xfvcontractprtinfo[] findWhereCommnameEquals(String commname) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the criteria 'NUM = :num'.
	 */
	public Xfvcontractprtinfo[] findWhereNumEquals(String num) throws XfvcontractprtinfoDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the specified arbitrary SQL statement
	 */
	public Xfvcontractprtinfo[] findByDynamicSelect(String sql, Object[] sqlParams) throws XfvcontractprtinfoDaoException;

	/** 
	 * Returns all rows from the XFVCONTRACTPRTINFO table that match the specified arbitrary SQL statement
	 */
	public Xfvcontractprtinfo[] findByDynamicWhere(String sql, Object[] sqlParams) throws XfvcontractprtinfoDaoException;

}