/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package zt.cms.xf.common.factory;

import java.sql.Connection;
import zt.cms.xf.common.dao.*;
import zt.cms.xf.common.jdbc.*;

public class XfcontractDaoFactory
{
	/**
	 * Method 'create'
	 * 
	 * @return XfcontractDao
	 */
	public static XfcontractDao create()
	{
		return new XfcontractDaoImpl();
	}

	/**
	 * Method 'create'
	 * 
	 * @param conn
	 * @return XfcontractDao
	 */
	public static XfcontractDao create(Connection conn)
	{
		return new XfcontractDaoImpl( conn );
	}

}