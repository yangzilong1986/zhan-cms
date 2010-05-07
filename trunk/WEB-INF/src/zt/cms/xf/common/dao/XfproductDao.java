/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package zt.cms.xf.common.dao;

import java.math.BigDecimal;
import java.util.Date;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.exceptions.*;

public interface XfproductDao
{
	/** 
	 * Inserts a new row in the XFPRODUCT table.
	 */
	public XfproductPk insert(Xfproduct dto) throws XfproductDaoException;

	/** 
	 * Updates a single row in the XFPRODUCT table.
	 */
	public void update(XfproductPk pk, Xfproduct dto) throws XfproductDaoException;

	/** 
	 * Deletes a single row in the XFPRODUCT table.
	 */
	public void delete(XfproductPk pk) throws XfproductDaoException;

	/** 
	 * Returns the rows from the XFPRODUCT table that matches the specified primary-key value.
	 */
	public Xfproduct findByPrimaryKey(XfproductPk pk) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'PRODUCTID = :productid'.
	 */
	public Xfproduct findByPrimaryKey(String productid) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'BRATE = :brate'.
	 */
	public Xfproduct[] findWhereBrateEquals(BigDecimal brate) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'FRATESCOPE = :fratescope'.
	 */
	public Xfproduct[] findWhereFratescopeEquals(BigDecimal fratescope) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'LATEFEERATE = :latefeerate'.
	 */
	public Xfproduct[] findWhereLatefeerateEquals(BigDecimal latefeerate) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'BREACHFEERATE = :breachfeerate'.
	 */
	public Xfproduct[] findWhereBreachfeerateEquals(BigDecimal breachfeerate) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'MANAGERFEERATE = :managerfeerate'.
	 */
	public Xfproduct[] findWhereManagerfeerateEquals(BigDecimal managerfeerate) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'LOWESTLATEFEE = :lowestlatefee'.
	 */
	public Xfproduct[] findWhereLowestlatefeeEquals(BigDecimal lowestlatefee) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria ''.
	 */
	public Xfproduct[] findAll() throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'PRODUCTID = :productid'.
	 */
	public Xfproduct[] findWhereProductidEquals(String productid) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'PRODUCTNAME = :productname'.
	 */
	public Xfproduct[] findWhereProductnameEquals(String productname) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'SOURCEID = :sourceid'.
	 */
	public Xfproduct[] findWhereSourceidEquals(String sourceid) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'APPTYPE = :apptype'.
	 */
	public Xfproduct[] findWhereApptypeEquals(String apptype) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'CLIENTCD = :clientcd'.
	 */
	public Xfproduct[] findWhereClientcdEquals(String clientcd) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'DURATION = :duration'.
	 */
	public Xfproduct[] findWhereDurationEquals(BigDecimal duration) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'SERVICECHARGE = :servicecharge'.
	 */
	public Xfproduct[] findWhereServicechargeEquals(BigDecimal servicecharge) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'CREATORID = :creatorid'.
	 */
	public Xfproduct[] findWhereCreatoridEquals(String creatorid) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'CREATEDATE = :createdate'.
	 */
	public Xfproduct[] findWhereCreatedateEquals(Date createdate) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'CREATEFORM = :createform'.
	 */
	public Xfproduct[] findWhereCreateformEquals(String createform) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'UPDATORID = :updatorid'.
	 */
	public Xfproduct[] findWhereUpdatoridEquals(String updatorid) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'UPDATEDATE = :updatedate'.
	 */
	public Xfproduct[] findWhereUpdatedateEquals(Date updatedate) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the criteria 'UPDATEFORM = :updateform'.
	 */
	public Xfproduct[] findWhereUpdateformEquals(String updateform) throws XfproductDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the XFPRODUCT table that match the specified arbitrary SQL statement
	 */
	public Xfproduct[] findByDynamicSelect(String sql, Object[] sqlParams) throws XfproductDaoException;

	/** 
	 * Returns all rows from the XFPRODUCT table that match the specified arbitrary SQL statement
	 */
	public Xfproduct[] findByDynamicWhere(String sql, Object[] sqlParams) throws XfproductDaoException;

}
