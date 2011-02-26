/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package zt.cms.xf.common.example;

import java.math.*;
import java.util.Date;
import java.util.Collection;
import zt.cms.xf.common.dao.XfactnoinfoDao;
import zt.cms.xf.common.dto.Xfactnoinfo;
import zt.cms.xf.common.exceptions.XfactnoinfoDaoException;
import zt.cms.xf.common.factory.XfactnoinfoDaoFactory;

public class XfactnoinfoDaoSample
{
	/**
	 * Method 'main'
	 * 
	 * @param arg
	 * @throws Exception
	 */
	public static void main(String[] arg) throws Exception
	{
		// Uncomment one of the lines below to test the generated code
		
		// findAll();
		// findWhereAppactnoEquals("");
		// findWhereAppnoEquals("");
		// findWhereStartdateEquals(null);
		// findWhereEnddateEquals(null);
		// findWhereActnostatusEquals(null);
		// findWhereCreatoridEquals("");
		// findWhereCreatedateEquals(null);
		// findWhereCreateformEquals("");
		// findWhereUpdatoridEquals("");
		// findWhereUpdatedateEquals(null);
		// findWhereUpdateformEquals("");
	}

	/**
	 * Method 'findAll'
	 * 
	 */
	public static void findAll()
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findAll();
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereAppactnoEquals'
	 * 
	 * @param appactno
	 */
	public static void findWhereAppactnoEquals(String appactno)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereAppactnoEquals(appactno);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereAppnoEquals'
	 * 
	 * @param appno
	 */
	public static void findWhereAppnoEquals(String appno)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereAppnoEquals(appno);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereStartdateEquals'
	 * 
	 * @param startdate
	 */
	public static void findWhereStartdateEquals(Date startdate)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereStartdateEquals(startdate);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereEnddateEquals'
	 * 
	 * @param enddate
	 */
	public static void findWhereEnddateEquals(Date enddate)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereEnddateEquals(enddate);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereActnostatusEquals'
	 * 
	 * @param actnostatus
	 */
	public static void findWhereActnostatusEquals(BigDecimal actnostatus)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereActnostatusEquals(actnostatus);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereCreatoridEquals'
	 * 
	 * @param creatorid
	 */
	public static void findWhereCreatoridEquals(String creatorid)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereCreatoridEquals(creatorid);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereCreatedateEquals'
	 * 
	 * @param createdate
	 */
	public static void findWhereCreatedateEquals(Date createdate)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereCreatedateEquals(createdate);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereCreateformEquals'
	 * 
	 * @param createform
	 */
	public static void findWhereCreateformEquals(String createform)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereCreateformEquals(createform);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereUpdatoridEquals'
	 * 
	 * @param updatorid
	 */
	public static void findWhereUpdatoridEquals(String updatorid)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereUpdatoridEquals(updatorid);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereUpdatedateEquals'
	 * 
	 * @param updatedate
	 */
	public static void findWhereUpdatedateEquals(Date updatedate)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereUpdatedateEquals(updatedate);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereUpdateformEquals'
	 * 
	 * @param updateform
	 */
	public static void findWhereUpdateformEquals(String updateform)
	{
		try {
			XfactnoinfoDao _dao = getXfactnoinfoDao();
			Xfactnoinfo _result[] = _dao.findWhereUpdateformEquals(updateform);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'getXfactnoinfoDao'
	 * 
	 * @return XfactnoinfoDao
	 */
	public static XfactnoinfoDao getXfactnoinfoDao()
	{
		return XfactnoinfoDaoFactory.create();
	}

	/**
	 * Method 'display'
	 * 
	 * @param dto
	 */
	public static void display(Xfactnoinfo dto)
	{
		StringBuffer buf = new StringBuffer();
		buf.append( dto.getAppactno() );
		buf.append( ", " );
		buf.append( dto.getAppno() );
		buf.append( ", " );
		buf.append( dto.getStartdate() );
		buf.append( ", " );
		buf.append( dto.getEnddate() );
		buf.append( ", " );
		buf.append( dto.getActnostatus() );
		buf.append( ", " );
		buf.append( dto.getCreatorid() );
		buf.append( ", " );
		buf.append( dto.getCreatedate() );
		buf.append( ", " );
		buf.append( dto.getCreateform() );
		buf.append( ", " );
		buf.append( dto.getUpdatorid() );
		buf.append( ", " );
		buf.append( dto.getUpdatedate() );
		buf.append( ", " );
		buf.append( dto.getUpdateform() );
		System.out.println( buf.toString() );
	}

}
