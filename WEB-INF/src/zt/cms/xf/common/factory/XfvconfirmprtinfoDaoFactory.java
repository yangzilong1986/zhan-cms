package zt.cms.xf.common.factory;

import java.sql.Connection;
import zt.cms.xf.common.dao.*;
import zt.cms.xf.common.jdbc.*;

/**
 * @author LiChen
 * 
 * */
public class XfvconfirmprtinfoDaoFactory
{
	/**
	 * Method 'create'
	 * 
	 * @return XfvcontractprtinfoDao
	 */
	public static XfvconfirmprtinfoDao create()
	{
		return new XfvconfirmprtinfoDaoImpl();
	}

	/**
	 * Method 'create'
	 * 
	 * @param conn
	 * @return XfvcontractprtinfoDao
	 */
	public static XfvconfirmprtinfoDao create(Connection conn)
	{
		return new XfvconfirmprtinfoDaoImpl( conn );
	}

}
