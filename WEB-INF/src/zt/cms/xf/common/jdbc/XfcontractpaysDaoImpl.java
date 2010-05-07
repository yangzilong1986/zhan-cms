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
import java.math.BigDecimal;
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

public class XfcontractpaysDaoImpl extends AbstractDAO implements XfcontractpaysDao
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
	protected final String SQL_SELECT = "SELECT CONTRACTNO, CPAAMT, STARTDATE, GRACEPERIOD, POANO, PAYBACKACT, RECVACT, CPATYPE, PAYBACKBANKID, PAYBACKBANKNO, RECVBANKID, RECVBANKNO, CREATORID, CREATEDATE, CREATEFORM, UPDATORID, UPDATEDATE, UPDATEFORM, PAYBACKACTNAME, PRINCIPALAMT, SERVICECHARGEFEE FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( CONTRACTNO, CPAAMT, STARTDATE, GRACEPERIOD, POANO, PAYBACKACT, RECVACT, CPATYPE, PAYBACKBANKID, PAYBACKBANKNO, RECVBANKID, RECVBANKNO, CREATORID, CREATEDATE, CREATEFORM, UPDATORID, UPDATEDATE, UPDATEFORM, PAYBACKACTNAME, PRINCIPALAMT, SERVICECHARGEFEE ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET CONTRACTNO = ?, CPAAMT = ?, STARTDATE = ?, GRACEPERIOD = ?, POANO = ?, PAYBACKACT = ?, RECVACT = ?, CPATYPE = ?, PAYBACKBANKID = ?, PAYBACKBANKNO = ?, RECVBANKID = ?, RECVBANKNO = ?, CREATORID = ?, CREATEDATE = ?, CREATEFORM = ?, UPDATORID = ?, UPDATEDATE = ?, UPDATEFORM = ?, PAYBACKACTNAME = ?, PRINCIPALAMT = ?, SERVICECHARGEFEE = ? WHERE CONTRACTNO = ? AND POANO = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE CONTRACTNO = ? AND POANO = ?";

	/** 
	 * Index of column CONTRACTNO
	 */
	protected static final int COLUMN_CONTRACTNO = 1;

	/** 
	 * Index of column CPAAMT
	 */
	protected static final int COLUMN_CPAAMT = 2;

	/** 
	 * Index of column STARTDATE
	 */
	protected static final int COLUMN_STARTDATE = 3;

	/** 
	 * Index of column GRACEPERIOD
	 */
	protected static final int COLUMN_GRACEPERIOD = 4;

	/** 
	 * Index of column POANO
	 */
	protected static final int COLUMN_POANO = 5;

	/** 
	 * Index of column PAYBACKACT
	 */
	protected static final int COLUMN_PAYBACKACT = 6;

	/** 
	 * Index of column RECVACT
	 */
	protected static final int COLUMN_RECVACT = 7;

	/** 
	 * Index of column CPATYPE
	 */
	protected static final int COLUMN_CPATYPE = 8;

	/** 
	 * Index of column PAYBACKBANKID
	 */
	protected static final int COLUMN_PAYBACKBANKID = 9;

	/** 
	 * Index of column PAYBACKBANKNO
	 */
	protected static final int COLUMN_PAYBACKBANKNO = 10;

	/** 
	 * Index of column RECVBANKID
	 */
	protected static final int COLUMN_RECVBANKID = 11;

	/** 
	 * Index of column RECVBANKNO
	 */
	protected static final int COLUMN_RECVBANKNO = 12;

	/** 
	 * Index of column CREATORID
	 */
	protected static final int COLUMN_CREATORID = 13;

	/** 
	 * Index of column CREATEDATE
	 */
	protected static final int COLUMN_CREATEDATE = 14;

	/** 
	 * Index of column CREATEFORM
	 */
	protected static final int COLUMN_CREATEFORM = 15;

	/** 
	 * Index of column UPDATORID
	 */
	protected static final int COLUMN_UPDATORID = 16;

	/** 
	 * Index of column UPDATEDATE
	 */
	protected static final int COLUMN_UPDATEDATE = 17;

	/** 
	 * Index of column UPDATEFORM
	 */
	protected static final int COLUMN_UPDATEFORM = 18;

	/** 
	 * Index of column PAYBACKACTNAME
	 */
	protected static final int COLUMN_PAYBACKACTNAME = 19;

	/** 
	 * Index of column PRINCIPALAMT
	 */
	protected static final int COLUMN_PRINCIPALAMT = 20;

	/** 
	 * Index of column SERVICECHARGEFEE
	 */
	protected static final int COLUMN_SERVICECHARGEFEE = 21;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 21;

	/** 
	 * Index of primary-key column CONTRACTNO
	 */
	protected static final int PK_COLUMN_CONTRACTNO = 1;

	/** 
	 * Index of primary-key column POANO
	 */
	protected static final int PK_COLUMN_POANO = 2;

	/** 
	 * Inserts a new row in the XFCONTRACTPAYS table.
	 */
	public XfcontractpaysPk insert(Xfcontractpays dto) throws XfcontractpaysDaoException
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
			stmt.setString( index++, dto.getContractno() );
			stmt.setBigDecimal( index++, dto.getCpaamt() );
			stmt.setTimestamp(index++, dto.getStartdate()==null ? null : new java.sql.Timestamp( dto.getStartdate().getTime() ) );
			stmt.setBigDecimal( index++, dto.getGraceperiod() );
			stmt.setBigDecimal( index++, dto.getPoano() );
			stmt.setString( index++, dto.getPaybackact() );
			stmt.setString( index++, dto.getRecvact() );
			stmt.setBigDecimal( index++, dto.getCpatype() );
			stmt.setString( index++, dto.getPaybackbankid() );
			stmt.setString( index++, dto.getPaybackbankno() );
			stmt.setString( index++, dto.getRecvbankid() );
			stmt.setString( index++, dto.getRecvbankno() );
			stmt.setString( index++, dto.getCreatorid() );
			stmt.setTimestamp(index++, dto.getCreatedate()==null ? null : new java.sql.Timestamp( dto.getCreatedate().getTime() ) );
			stmt.setString( index++, dto.getCreateform() );
			stmt.setString( index++, dto.getUpdatorid() );
			stmt.setTimestamp(index++, dto.getUpdatedate()==null ? null : new java.sql.Timestamp( dto.getUpdatedate().getTime() ) );
			stmt.setString( index++, dto.getUpdateform() );
			stmt.setString( index++, dto.getPaybackactname() );
			stmt.setBigDecimal( index++, dto.getPrincipalamt() );
			stmt.setBigDecimal( index++, dto.getServicechargefee() );
			System.out.println( "Executing " + SQL_INSERT + " with DTO: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new XfcontractpaysDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the XFCONTRACTPAYS table.
	 */
	public void update(XfcontractpaysPk pk, Xfcontractpays dto) throws XfcontractpaysDaoException
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
			stmt.setString( index++, dto.getContractno() );
			stmt.setBigDecimal( index++, dto.getCpaamt() );
			stmt.setTimestamp(index++, dto.getStartdate()==null ? null : new java.sql.Timestamp( dto.getStartdate().getTime() ) );
			stmt.setBigDecimal( index++, dto.getGraceperiod() );
			stmt.setBigDecimal( index++, dto.getPoano() );
			stmt.setString( index++, dto.getPaybackact() );
			stmt.setString( index++, dto.getRecvact() );
			stmt.setBigDecimal( index++, dto.getCpatype() );
			stmt.setString( index++, dto.getPaybackbankid() );
			stmt.setString( index++, dto.getPaybackbankno() );
			stmt.setString( index++, dto.getRecvbankid() );
			stmt.setString( index++, dto.getRecvbankno() );
			stmt.setString( index++, dto.getCreatorid() );
			stmt.setTimestamp(index++, dto.getCreatedate()==null ? null : new java.sql.Timestamp( dto.getCreatedate().getTime() ) );
			stmt.setString( index++, dto.getCreateform() );
			stmt.setString( index++, dto.getUpdatorid() );
			stmt.setTimestamp(index++, dto.getUpdatedate()==null ? null : new java.sql.Timestamp( dto.getUpdatedate().getTime() ) );
			stmt.setString( index++, dto.getUpdateform() );
			stmt.setString( index++, dto.getPaybackactname() );
			stmt.setBigDecimal( index++, dto.getPrincipalamt() );
			stmt.setBigDecimal( index++, dto.getServicechargefee() );
			stmt.setString( 22, pk.getContractno() );
			stmt.setBigDecimal( 23, pk.getPoano() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new XfcontractpaysDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the XFCONTRACTPAYS table.
	 */
	public void delete(XfcontractpaysPk pk) throws XfcontractpaysDaoException
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
			stmt.setBigDecimal( 2, pk.getPoano() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new XfcontractpaysDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the XFCONTRACTPAYS table that matches the specified primary-key value.
	 */
	public Xfcontractpays findByPrimaryKey(XfcontractpaysPk pk) throws XfcontractpaysDaoException
	{
		return findByPrimaryKey( pk.getContractno(), pk.getPoano() );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'CONTRACTNO = :contractno AND POANO = :poano'.
	 */
	public Xfcontractpays findByPrimaryKey(String contractno, BigDecimal poano) throws XfcontractpaysDaoException
	{
		Xfcontractpays ret[] = findByDynamicSelect( SQL_SELECT + " WHERE CONTRACTNO = ? AND POANO = ?", new Object[] { contractno, poano } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria ''.
	 */
	public Xfcontractpays[] findAll() throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY CONTRACTNO, POANO", null );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'CONTRACTNO = :contractno'.
	 */
	public Xfcontractpays[] findWhereContractnoEquals(String contractno) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CONTRACTNO = ? ORDER BY CONTRACTNO", new Object[] { contractno } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'CPAAMT = :cpaamt'.
	 */
	public Xfcontractpays[] findWhereCpaamtEquals(BigDecimal cpaamt) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CPAAMT = ? ORDER BY CPAAMT", new Object[] { cpaamt } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'STARTDATE = :startdate'.
	 */
	public Xfcontractpays[] findWhereStartdateEquals(Date startdate) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE STARTDATE = ? ORDER BY STARTDATE", new Object[] { startdate==null ? null : new java.sql.Timestamp( startdate.getTime() ) } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'GRACEPERIOD = :graceperiod'.
	 */
	public Xfcontractpays[] findWhereGraceperiodEquals(BigDecimal graceperiod) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE GRACEPERIOD = ? ORDER BY GRACEPERIOD", new Object[] { graceperiod } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'POANO = :poano'.
	 */
	public Xfcontractpays[] findWherePoanoEquals(BigDecimal poano) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE POANO = ? ORDER BY POANO", new Object[] { poano } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'PAYBACKACT = :paybackact'.
	 */
	public Xfcontractpays[] findWherePaybackactEquals(String paybackact) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE PAYBACKACT = ? ORDER BY PAYBACKACT", new Object[] { paybackact } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'RECVACT = :recvact'.
	 */
	public Xfcontractpays[] findWhereRecvactEquals(String recvact) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RECVACT = ? ORDER BY RECVACT", new Object[] { recvact } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'CPATYPE = :cpatype'.
	 */
	public Xfcontractpays[] findWhereCpatypeEquals(BigDecimal cpatype) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CPATYPE = ? ORDER BY CPATYPE", new Object[] { cpatype } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'PAYBACKBANKID = :paybackbankid'.
	 */
	public Xfcontractpays[] findWherePaybackbankidEquals(String paybackbankid) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE PAYBACKBANKID = ? ORDER BY PAYBACKBANKID", new Object[] { paybackbankid } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'PAYBACKBANKNO = :paybackbankno'.
	 */
	public Xfcontractpays[] findWherePaybackbanknoEquals(String paybackbankno) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE PAYBACKBANKNO = ? ORDER BY PAYBACKBANKNO", new Object[] { paybackbankno } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'RECVBANKID = :recvbankid'.
	 */
	public Xfcontractpays[] findWhereRecvbankidEquals(String recvbankid) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RECVBANKID = ? ORDER BY RECVBANKID", new Object[] { recvbankid } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'RECVBANKNO = :recvbankno'.
	 */
	public Xfcontractpays[] findWhereRecvbanknoEquals(String recvbankno) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RECVBANKNO = ? ORDER BY RECVBANKNO", new Object[] { recvbankno } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'CREATORID = :creatorid'.
	 */
	public Xfcontractpays[] findWhereCreatoridEquals(String creatorid) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CREATORID = ? ORDER BY CREATORID", new Object[] { creatorid } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'CREATEDATE = :createdate'.
	 */
	public Xfcontractpays[] findWhereCreatedateEquals(Date createdate) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CREATEDATE = ? ORDER BY CREATEDATE", new Object[] { createdate==null ? null : new java.sql.Timestamp( createdate.getTime() ) } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'CREATEFORM = :createform'.
	 */
	public Xfcontractpays[] findWhereCreateformEquals(String createform) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CREATEFORM = ? ORDER BY CREATEFORM", new Object[] { createform } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'UPDATORID = :updatorid'.
	 */
	public Xfcontractpays[] findWhereUpdatoridEquals(String updatorid) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE UPDATORID = ? ORDER BY UPDATORID", new Object[] { updatorid } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'UPDATEDATE = :updatedate'.
	 */
	public Xfcontractpays[] findWhereUpdatedateEquals(Date updatedate) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE UPDATEDATE = ? ORDER BY UPDATEDATE", new Object[] { updatedate==null ? null : new java.sql.Timestamp( updatedate.getTime() ) } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'UPDATEFORM = :updateform'.
	 */
	public Xfcontractpays[] findWhereUpdateformEquals(String updateform) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE UPDATEFORM = ? ORDER BY UPDATEFORM", new Object[] { updateform } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'PAYBACKACTNAME = :paybackactname'.
	 */
	public Xfcontractpays[] findWherePaybackactnameEquals(String paybackactname) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE PAYBACKACTNAME = ? ORDER BY PAYBACKACTNAME", new Object[] { paybackactname } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'PRINCIPALAMT = :principalamt'.
	 */
	public Xfcontractpays[] findWherePrincipalamtEquals(BigDecimal principalamt) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE PRINCIPALAMT = ? ORDER BY PRINCIPALAMT", new Object[] { principalamt } );
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the criteria 'SERVICECHARGEFEE = :servicechargefee'.
	 */
	public Xfcontractpays[] findWhereServicechargefeeEquals(BigDecimal servicechargefee) throws XfcontractpaysDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE SERVICECHARGEFEE = ? ORDER BY SERVICECHARGEFEE", new Object[] { servicechargefee } );
	}

	/**
	 * Method 'XfcontractpaysDaoImpl'
	 * 
	 */
	public XfcontractpaysDaoImpl()
	{
	}

	/**
	 * Method 'XfcontractpaysDaoImpl'
	 * 
	 * @param userConn
	 */
	public XfcontractpaysDaoImpl(final java.sql.Connection userConn)
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
		return "CMS.XFCONTRACTPAYS";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected Xfcontractpays fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			Xfcontractpays dto = new Xfcontractpays();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Xfcontractpays[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Xfcontractpays dto = new Xfcontractpays();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Xfcontractpays ret[] = new Xfcontractpays[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Xfcontractpays dto, ResultSet rs) throws SQLException
	{
		dto.setContractno( rs.getString( COLUMN_CONTRACTNO ) );
		dto.setCpaamt( rs.getBigDecimal(COLUMN_CPAAMT));
		dto.setStartdate( rs.getTimestamp(COLUMN_STARTDATE ) );
		dto.setGraceperiod( rs.getBigDecimal(COLUMN_GRACEPERIOD));
		dto.setPoano( rs.getBigDecimal(COLUMN_POANO));
		dto.setPaybackact( rs.getString( COLUMN_PAYBACKACT ) );
		dto.setRecvact( rs.getString( COLUMN_RECVACT ) );
		dto.setCpatype( rs.getBigDecimal(COLUMN_CPATYPE));
		dto.setPaybackbankid( rs.getString( COLUMN_PAYBACKBANKID ) );
		dto.setPaybackbankno( rs.getString( COLUMN_PAYBACKBANKNO ) );
		dto.setRecvbankid( rs.getString( COLUMN_RECVBANKID ) );
		dto.setRecvbankno( rs.getString( COLUMN_RECVBANKNO ) );
		dto.setCreatorid( rs.getString( COLUMN_CREATORID ) );
		dto.setCreatedate( rs.getTimestamp(COLUMN_CREATEDATE ) );
		dto.setCreateform( rs.getString( COLUMN_CREATEFORM ) );
		dto.setUpdatorid( rs.getString( COLUMN_UPDATORID ) );
		dto.setUpdatedate( rs.getTimestamp(COLUMN_UPDATEDATE ) );
		dto.setUpdateform( rs.getString( COLUMN_UPDATEFORM ) );
		dto.setPaybackactname( rs.getString( COLUMN_PAYBACKACTNAME ) );
		dto.setPrincipalamt( rs.getBigDecimal(COLUMN_PRINCIPALAMT));
		dto.setServicechargefee( rs.getBigDecimal(COLUMN_SERVICECHARGEFEE));
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(Xfcontractpays dto)
	{
	}

	/** 
	 * Returns all rows from the XFCONTRACTPAYS table that match the specified arbitrary SQL statement
	 */
	public Xfcontractpays[] findByDynamicSelect(String sql, Object[] sqlParams) throws XfcontractpaysDaoException
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
			throw new XfcontractpaysDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the XFCONTRACTPAYS table that match the specified arbitrary SQL statement
	 */
	public Xfcontractpays[] findByDynamicWhere(String sql, Object[] sqlParams) throws XfcontractpaysDaoException
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
			throw new XfcontractpaysDaoException( "Exception: " + _e.getMessage(), _e );
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
