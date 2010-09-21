/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package zt.cms.xf.common.jdbc;

import zt.cms.xf.common.dao.*;
import zt.cms.xf.common.factory.*;
import java.util.Date;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.exceptions.*;
import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class FdactnoinfoDaoImpl extends AbstractDAO implements FdactnoinfoDao
{
	/** 
	 * The factory class for this DAO has two versions of the create() method - one that
takes no arguments and one that takes a Connection argument. If the Connection version
is chosen then the connection will be stored in this attribute and will be used by all
calls to this DAO, otherwise a new Connection will be allocated for each operation.
	 */
	protected java.sql.Connection userConn;

	/** 
	 * All finder methods in this class use this SELECT constant to build their queries
	 */
	protected final String SQL_SELECT = "SELECT ACTNO, CUTPAYACTNO, CONTRACTNO, NAME, STARTDATE, ENDDATE, ACTNOSTATUS, CREATORID, CREATEDATE, CREATEFORM, UPDATORID, UPDATEDATE, UPDATEFORM, REGIONCD, BANKCD, CUSTOMERID FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( ACTNO, CUTPAYACTNO, CONTRACTNO, NAME, STARTDATE, ENDDATE, ACTNOSTATUS, CREATORID, CREATEDATE, CREATEFORM, UPDATORID, UPDATEDATE, UPDATEFORM, REGIONCD, BANKCD, CUSTOMERID ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET ACTNO = ?, CUTPAYACTNO = ?, CONTRACTNO = ?, NAME = ?, STARTDATE = ?, ENDDATE = ?, ACTNOSTATUS = ?, CREATORID = ?, CREATEDATE = ?, CREATEFORM = ?, UPDATORID = ?, UPDATEDATE = ?, UPDATEFORM = ?, REGIONCD = ?, BANKCD = ?, CUSTOMERID = ? WHERE CONTRACTNO = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE CONTRACTNO = ?";

	/** 
	 * Index of column ACTNO
	 */
	protected static final int COLUMN_ACTNO = 1;

	/** 
	 * Index of column CUTPAYACTNO
	 */
	protected static final int COLUMN_CUTPAYACTNO = 2;

	/** 
	 * Index of column CONTRACTNO
	 */
	protected static final int COLUMN_CONTRACTNO = 3;

	/** 
	 * Index of column NAME
	 */
	protected static final int COLUMN_NAME = 4;

	/** 
	 * Index of column STARTDATE
	 */
	protected static final int COLUMN_STARTDATE = 5;

	/** 
	 * Index of column ENDDATE
	 */
	protected static final int COLUMN_ENDDATE = 6;

	/** 
	 * Index of column ACTNOSTATUS
	 */
	protected static final int COLUMN_ACTNOSTATUS = 7;

	/** 
	 * Index of column CREATORID
	 */
	protected static final int COLUMN_CREATORID = 8;

	/** 
	 * Index of column CREATEDATE
	 */
	protected static final int COLUMN_CREATEDATE = 9;

	/** 
	 * Index of column CREATEFORM
	 */
	protected static final int COLUMN_CREATEFORM = 10;

	/** 
	 * Index of column UPDATORID
	 */
	protected static final int COLUMN_UPDATORID = 11;

	/** 
	 * Index of column UPDATEDATE
	 */
	protected static final int COLUMN_UPDATEDATE = 12;

	/** 
	 * Index of column UPDATEFORM
	 */
	protected static final int COLUMN_UPDATEFORM = 13;

	/** 
	 * Index of column REGIONCD
	 */
	protected static final int COLUMN_REGIONCD = 14;

	/** 
	 * Index of column BANKCD
	 */
	protected static final int COLUMN_BANKCD = 15;

	/** 
	 * Index of column CUSTOMERID
	 */
	protected static final int COLUMN_CUSTOMERID = 16;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 16;

	/** 
	 * Index of primary-key column CONTRACTNO
	 */
	protected static final int PK_COLUMN_CONTRACTNO = 1;

	/** 
	 * Inserts a new row in the FDACTNOINFO table.
	 */
	public FdactnoinfoPk insert(Fdactnoinfo dto) throws FdactnoinfoDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			stmt = conn.prepareStatement( SQL_INSERT );
			int index = 1;
			stmt.setString( index++, dto.getActno() );
			stmt.setString( index++, dto.getCutpayactno() );
			stmt.setString( index++, dto.getContractno() );
			stmt.setString( index++, dto.getName() );
			stmt.setTimestamp(index++, dto.getStartdate()==null ? null : new java.sql.Timestamp( dto.getStartdate().getTime() ) );
			stmt.setTimestamp(index++, dto.getEnddate()==null ? null : new java.sql.Timestamp( dto.getEnddate().getTime() ) );
			stmt.setString( index++, dto.getActnostatus() );
			stmt.setString( index++, dto.getCreatorid() );
			stmt.setTimestamp(index++, dto.getCreatedate()==null ? null : new java.sql.Timestamp( dto.getCreatedate().getTime() ) );
			stmt.setString( index++, dto.getCreateform() );
			stmt.setString( index++, dto.getUpdatorid() );
			stmt.setTimestamp(index++, dto.getUpdatedate()==null ? null : new java.sql.Timestamp( dto.getUpdatedate().getTime() ) );
			stmt.setString( index++, dto.getUpdateform() );
			stmt.setString( index++, dto.getRegioncd() );
			stmt.setString( index++, dto.getBankcd() );
			stmt.setString( index++, dto.getCustomerid() );
			System.out.println( "Executing " + SQL_INSERT + " with DTO: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new FdactnoinfoDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the FDACTNOINFO table.
	 */
	public void update(FdactnoinfoPk pk, Fdactnoinfo dto) throws FdactnoinfoDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			System.out.println( "Executing " + SQL_UPDATE + " with DTO: " + dto );
			stmt = conn.prepareStatement( SQL_UPDATE );
			int index=1;
			stmt.setString( index++, dto.getActno() );
			stmt.setString( index++, dto.getCutpayactno() );
			stmt.setString( index++, dto.getContractno() );
			stmt.setString( index++, dto.getName() );
			stmt.setTimestamp(index++, dto.getStartdate()==null ? null : new java.sql.Timestamp( dto.getStartdate().getTime() ) );
			stmt.setTimestamp(index++, dto.getEnddate()==null ? null : new java.sql.Timestamp( dto.getEnddate().getTime() ) );
			stmt.setString( index++, dto.getActnostatus() );
			stmt.setString( index++, dto.getCreatorid() );
			stmt.setTimestamp(index++, dto.getCreatedate()==null ? null : new java.sql.Timestamp( dto.getCreatedate().getTime() ) );
			stmt.setString( index++, dto.getCreateform() );
			stmt.setString( index++, dto.getUpdatorid() );
			stmt.setTimestamp(index++, dto.getUpdatedate()==null ? null : new java.sql.Timestamp( dto.getUpdatedate().getTime() ) );
			stmt.setString( index++, dto.getUpdateform() );
			stmt.setString( index++, dto.getRegioncd() );
			stmt.setString( index++, dto.getBankcd() );
			stmt.setString( index++, dto.getCustomerid() );
			stmt.setString( 17, pk.getContractno() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new FdactnoinfoDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the FDACTNOINFO table.
	 */
	public void delete(FdactnoinfoPk pk) throws FdactnoinfoDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			System.out.println( "Executing " + SQL_DELETE + " with PK: " + pk );
			stmt = conn.prepareStatement( SQL_DELETE );
			stmt.setString( 1, pk.getContractno() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new FdactnoinfoDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the FDACTNOINFO table that matches the specified primary-key value.
	 */
	public Fdactnoinfo findByPrimaryKey(FdactnoinfoPk pk) throws FdactnoinfoDaoException
	{
		return findByPrimaryKey( pk.getContractno() );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'CONTRACTNO = :contractno'.
	 */
	public Fdactnoinfo findByPrimaryKey(String contractno) throws FdactnoinfoDaoException
	{
		Fdactnoinfo ret[] = findByDynamicSelect( SQL_SELECT + " WHERE CONTRACTNO = ?", new Object[] { contractno } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria ''.
	 */
	public Fdactnoinfo[] findAll() throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY CONTRACTNO", null );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'ACTNO = :actno'.
	 */
	public Fdactnoinfo[] findWhereActnoEquals(String actno) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ACTNO = ? ORDER BY ACTNO", new Object[] { actno } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'CUTPAYACTNO = :cutpayactno'.
	 */
	public Fdactnoinfo[] findWhereCutpayactnoEquals(String cutpayactno) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CUTPAYACTNO = ? ORDER BY CUTPAYACTNO", new Object[] { cutpayactno } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'CONTRACTNO = :contractno'.
	 */
	public Fdactnoinfo[] findWhereContractnoEquals(String contractno) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CONTRACTNO = ? ORDER BY CONTRACTNO", new Object[] { contractno } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'NAME = :name'.
	 */
	public Fdactnoinfo[] findWhereNameEquals(String name) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NAME = ? ORDER BY NAME", new Object[] { name } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'STARTDATE = :startdate'.
	 */
	public Fdactnoinfo[] findWhereStartdateEquals(Date startdate) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE STARTDATE = ? ORDER BY STARTDATE", new Object[] { startdate==null ? null : new java.sql.Timestamp( startdate.getTime() ) } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'ENDDATE = :enddate'.
	 */
	public Fdactnoinfo[] findWhereEnddateEquals(Date enddate) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ENDDATE = ? ORDER BY ENDDATE", new Object[] { enddate==null ? null : new java.sql.Timestamp( enddate.getTime() ) } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'ACTNOSTATUS = :actnostatus'.
	 */
	public Fdactnoinfo[] findWhereActnostatusEquals(String actnostatus) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ACTNOSTATUS = ? ORDER BY ACTNOSTATUS", new Object[] { actnostatus } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'CREATORID = :creatorid'.
	 */
	public Fdactnoinfo[] findWhereCreatoridEquals(String creatorid) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CREATORID = ? ORDER BY CREATORID", new Object[] { creatorid } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'CREATEDATE = :createdate'.
	 */
	public Fdactnoinfo[] findWhereCreatedateEquals(Date createdate) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CREATEDATE = ? ORDER BY CREATEDATE", new Object[] { createdate==null ? null : new java.sql.Timestamp( createdate.getTime() ) } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'CREATEFORM = :createform'.
	 */
	public Fdactnoinfo[] findWhereCreateformEquals(String createform) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CREATEFORM = ? ORDER BY CREATEFORM", new Object[] { createform } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'UPDATORID = :updatorid'.
	 */
	public Fdactnoinfo[] findWhereUpdatoridEquals(String updatorid) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE UPDATORID = ? ORDER BY UPDATORID", new Object[] { updatorid } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'UPDATEDATE = :updatedate'.
	 */
	public Fdactnoinfo[] findWhereUpdatedateEquals(Date updatedate) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE UPDATEDATE = ? ORDER BY UPDATEDATE", new Object[] { updatedate==null ? null : new java.sql.Timestamp( updatedate.getTime() ) } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'UPDATEFORM = :updateform'.
	 */
	public Fdactnoinfo[] findWhereUpdateformEquals(String updateform) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE UPDATEFORM = ? ORDER BY UPDATEFORM", new Object[] { updateform } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'REGIONCD = :regioncd'.
	 */
	public Fdactnoinfo[] findWhereRegioncdEquals(String regioncd) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE REGIONCD = ? ORDER BY REGIONCD", new Object[] { regioncd } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'BANKCD = :bankcd'.
	 */
	public Fdactnoinfo[] findWhereBankcdEquals(String bankcd) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE BANKCD = ? ORDER BY BANKCD", new Object[] { bankcd } );
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the criteria 'CUSTOMERID = :customerid'.
	 */
	public Fdactnoinfo[] findWhereCustomeridEquals(String customerid) throws FdactnoinfoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CUSTOMERID = ? ORDER BY CUSTOMERID", new Object[] { customerid } );
	}

	/**
	 * Method 'FdactnoinfoDaoImpl'
	 * 
	 */
	public FdactnoinfoDaoImpl()
	{
	}

	/**
	 * Method 'FdactnoinfoDaoImpl'
	 * 
	 * @param userConn
	 */
	public FdactnoinfoDaoImpl(final java.sql.Connection userConn)
	{
		this.userConn = userConn;
	}

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows)
	{
		this.maxRows = maxRows;
	}

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows()
	{
		return maxRows;
	}

	/**
	 * Method 'getTableName'
	 * 
	 * @return String
	 */
	public String getTableName()
	{
		return "CMS.FDACTNOINFO";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected Fdactnoinfo fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			Fdactnoinfo dto = new Fdactnoinfo();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Fdactnoinfo[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Fdactnoinfo dto = new Fdactnoinfo();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Fdactnoinfo ret[] = new Fdactnoinfo[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Fdactnoinfo dto, ResultSet rs) throws SQLException
	{
		dto.setActno( rs.getString( COLUMN_ACTNO ) );
		dto.setCutpayactno( rs.getString( COLUMN_CUTPAYACTNO ) );
		dto.setContractno( rs.getString( COLUMN_CONTRACTNO ) );
		dto.setName( rs.getString( COLUMN_NAME ) );
		dto.setStartdate( rs.getTimestamp(COLUMN_STARTDATE ) );
		dto.setEnddate( rs.getTimestamp(COLUMN_ENDDATE ) );
		dto.setActnostatus( rs.getString( COLUMN_ACTNOSTATUS ) );
		dto.setCreatorid( rs.getString( COLUMN_CREATORID ) );
		dto.setCreatedate( rs.getTimestamp(COLUMN_CREATEDATE ) );
		dto.setCreateform( rs.getString( COLUMN_CREATEFORM ) );
		dto.setUpdatorid( rs.getString( COLUMN_UPDATORID ) );
		dto.setUpdatedate( rs.getTimestamp(COLUMN_UPDATEDATE ) );
		dto.setUpdateform( rs.getString( COLUMN_UPDATEFORM ) );
		dto.setRegioncd( rs.getString( COLUMN_REGIONCD ) );
		dto.setBankcd( rs.getString( COLUMN_BANKCD ) );
		dto.setCustomerid( rs.getString( COLUMN_CUSTOMERID ) );
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(Fdactnoinfo dto)
	{
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the specified arbitrary SQL statement
	 */
	public Fdactnoinfo[] findByDynamicSelect(String sql, Object[] sqlParams) throws FdactnoinfoDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = sql;
		
		
			System.out.println( "Executing " + SQL );
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new FdactnoinfoDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns all rows from the FDACTNOINFO table that match the specified arbitrary SQL statement
	 */
	public Fdactnoinfo[] findByDynamicWhere(String sql, Object[] sqlParams) throws FdactnoinfoDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = SQL_SELECT + " WHERE " + sql;
		
		
			System.out.println( "Executing " + SQL );
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new FdactnoinfoDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

}