package zt.cms.xf.common.jdbc;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class ResourceManager
{
	public static synchronized Connection getConnection()
	throws NamingException, SQLException
	{
		InitialContext initCtx = new InitialContext();
		Object obj = initCtx.lookup( "CreditDB" );
		DataSource ds = (DataSource) obj;
		return ds.getConnection();
	}


	public static void close(Connection conn)
	{
		try {
			if (conn != null) conn.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	public static void close(PreparedStatement stmt)
	{
		try {
			if (stmt != null) stmt.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	public static void close(ResultSet rs)
	{
		try {
			if (rs != null) rs.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}

	}

}
