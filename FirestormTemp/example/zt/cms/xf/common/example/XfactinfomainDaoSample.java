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
import zt.cms.xf.common.dao.XfactinfomainDao;
import zt.cms.xf.common.dto.Xfactinfomain;
import zt.cms.xf.common.exceptions.XfactinfomainDaoException;
import zt.cms.xf.common.factory.XfactinfomainDaoFactory;

public class XfactinfomainDaoSample
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
		// findWhereContractnoEquals("");
		// findWherePoanoEquals("");
		// findWhereStartdateEquals(null);
		// findWhereEnddateEquals(null);
		// findWhereDurationEquals(null);
		// findWhereCurnoEquals("");
		// findWhereContractamtEquals(null);
		// findWherePrincipalamtEquals(null);
		// findWhereFservicechargeEquals(null);
		// findWherePoastatusEquals(null);
		// findWherePaybakamtEquals(null);
		// findWhereLatefeeamtEquals(null);
		// findWhereBreachfeeamtEquals(null);
		// findWhereManagerfeeamtEquals(null);
		// findWhereOverduetimesEquals(null);
		// findWhereClientactEquals("");
		// findWherePaybackactEquals("");
		// findWherePaybackbankidEquals("");
		// findWherePaybackbanknoEquals("");
		// findWhereRecvactEquals("");
		// findWhereRecvbankidEquals("");
		// findWhereRecvbanknoEquals("");
		// findWhereRecvbanknameEquals("");
		// findWhereOperatoridEquals("");
		// findWhereOperatedateEquals(null);
		// findWhereCheckeridEquals("");
		// findWhereCheckdateEquals(null);
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
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findAll();
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereContractnoEquals'
	 * 
	 * @param contractno
	 */
	public static void findWhereContractnoEquals(String contractno)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereContractnoEquals(contractno);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePoanoEquals'
	 * 
	 * @param poano
	 */
	public static void findWherePoanoEquals(String poano)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWherePoanoEquals(poano);
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
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereStartdateEquals(startdate);
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
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereEnddateEquals(enddate);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereDurationEquals'
	 * 
	 * @param duration
	 */
	public static void findWhereDurationEquals(BigDecimal duration)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereDurationEquals(duration);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereCurnoEquals'
	 * 
	 * @param curno
	 */
	public static void findWhereCurnoEquals(String curno)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereCurnoEquals(curno);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereContractamtEquals'
	 * 
	 * @param contractamt
	 */
	public static void findWhereContractamtEquals(BigDecimal contractamt)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereContractamtEquals(contractamt);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePrincipalamtEquals'
	 * 
	 * @param principalamt
	 */
	public static void findWherePrincipalamtEquals(BigDecimal principalamt)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWherePrincipalamtEquals(principalamt);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereFservicechargeEquals'
	 * 
	 * @param fservicecharge
	 */
	public static void findWhereFservicechargeEquals(BigDecimal fservicecharge)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereFservicechargeEquals(fservicecharge);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePoastatusEquals'
	 * 
	 * @param poastatus
	 */
	public static void findWherePoastatusEquals(BigDecimal poastatus)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWherePoastatusEquals(poastatus);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePaybakamtEquals'
	 * 
	 * @param paybakamt
	 */
	public static void findWherePaybakamtEquals(BigDecimal paybakamt)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWherePaybakamtEquals(paybakamt);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereLatefeeamtEquals'
	 * 
	 * @param latefeeamt
	 */
	public static void findWhereLatefeeamtEquals(BigDecimal latefeeamt)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereLatefeeamtEquals(latefeeamt);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereBreachfeeamtEquals'
	 * 
	 * @param breachfeeamt
	 */
	public static void findWhereBreachfeeamtEquals(BigDecimal breachfeeamt)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereBreachfeeamtEquals(breachfeeamt);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereManagerfeeamtEquals'
	 * 
	 * @param managerfeeamt
	 */
	public static void findWhereManagerfeeamtEquals(BigDecimal managerfeeamt)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereManagerfeeamtEquals(managerfeeamt);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereOverduetimesEquals'
	 * 
	 * @param overduetimes
	 */
	public static void findWhereOverduetimesEquals(BigDecimal overduetimes)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereOverduetimesEquals(overduetimes);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereClientactEquals'
	 * 
	 * @param clientact
	 */
	public static void findWhereClientactEquals(String clientact)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereClientactEquals(clientact);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePaybackactEquals'
	 * 
	 * @param paybackact
	 */
	public static void findWherePaybackactEquals(String paybackact)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWherePaybackactEquals(paybackact);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePaybackbankidEquals'
	 * 
	 * @param paybackbankid
	 */
	public static void findWherePaybackbankidEquals(String paybackbankid)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWherePaybackbankidEquals(paybackbankid);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePaybackbanknoEquals'
	 * 
	 * @param paybackbankno
	 */
	public static void findWherePaybackbanknoEquals(String paybackbankno)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWherePaybackbanknoEquals(paybackbankno);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereRecvactEquals'
	 * 
	 * @param recvact
	 */
	public static void findWhereRecvactEquals(String recvact)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereRecvactEquals(recvact);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereRecvbankidEquals'
	 * 
	 * @param recvbankid
	 */
	public static void findWhereRecvbankidEquals(String recvbankid)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereRecvbankidEquals(recvbankid);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereRecvbanknoEquals'
	 * 
	 * @param recvbankno
	 */
	public static void findWhereRecvbanknoEquals(String recvbankno)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereRecvbanknoEquals(recvbankno);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereRecvbanknameEquals'
	 * 
	 * @param recvbankname
	 */
	public static void findWhereRecvbanknameEquals(String recvbankname)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereRecvbanknameEquals(recvbankname);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereOperatoridEquals'
	 * 
	 * @param operatorid
	 */
	public static void findWhereOperatoridEquals(String operatorid)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereOperatoridEquals(operatorid);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereOperatedateEquals'
	 * 
	 * @param operatedate
	 */
	public static void findWhereOperatedateEquals(Date operatedate)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereOperatedateEquals(operatedate);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereCheckeridEquals'
	 * 
	 * @param checkerid
	 */
	public static void findWhereCheckeridEquals(String checkerid)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereCheckeridEquals(checkerid);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereCheckdateEquals'
	 * 
	 * @param checkdate
	 */
	public static void findWhereCheckdateEquals(Date checkdate)
	{
		try {
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereCheckdateEquals(checkdate);
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
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereCreatoridEquals(creatorid);
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
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereCreatedateEquals(createdate);
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
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereCreateformEquals(createform);
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
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereUpdatoridEquals(updatorid);
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
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereUpdatedateEquals(updatedate);
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
			XfactinfomainDao _dao = getXfactinfomainDao();
			Xfactinfomain _result[] = _dao.findWhereUpdateformEquals(updateform);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'getXfactinfomainDao'
	 * 
	 * @return XfactinfomainDao
	 */
	public static XfactinfomainDao getXfactinfomainDao()
	{
		return XfactinfomainDaoFactory.create();
	}

	/**
	 * Method 'display'
	 * 
	 * @param dto
	 */
	public static void display(Xfactinfomain dto)
	{
		StringBuffer buf = new StringBuffer();
		buf.append( dto.getContractno() );
		buf.append( ", " );
		buf.append( dto.getPoano() );
		buf.append( ", " );
		buf.append( dto.getStartdate() );
		buf.append( ", " );
		buf.append( dto.getEnddate() );
		buf.append( ", " );
		buf.append( dto.getDuration() );
		buf.append( ", " );
		buf.append( dto.getCurno() );
		buf.append( ", " );
		buf.append( dto.getContractamt() );
		buf.append( ", " );
		buf.append( dto.getPrincipalamt() );
		buf.append( ", " );
		buf.append( dto.getFservicecharge() );
		buf.append( ", " );
		buf.append( dto.getPoastatus() );
		buf.append( ", " );
		buf.append( dto.getPaybakamt() );
		buf.append( ", " );
		buf.append( dto.getLatefeeamt() );
		buf.append( ", " );
		buf.append( dto.getBreachfeeamt() );
		buf.append( ", " );
		buf.append( dto.getManagerfeeamt() );
		buf.append( ", " );
		buf.append( dto.getOverduetimes() );
		buf.append( ", " );
		buf.append( dto.getClientact() );
		buf.append( ", " );
		buf.append( dto.getPaybackact() );
		buf.append( ", " );
		buf.append( dto.getPaybackbankid() );
		buf.append( ", " );
		buf.append( dto.getPaybackbankno() );
		buf.append( ", " );
		buf.append( dto.getRecvact() );
		buf.append( ", " );
		buf.append( dto.getRecvbankid() );
		buf.append( ", " );
		buf.append( dto.getRecvbankno() );
		buf.append( ", " );
		buf.append( dto.getRecvbankname() );
		buf.append( ", " );
		buf.append( dto.getOperatorid() );
		buf.append( ", " );
		buf.append( dto.getOperatedate() );
		buf.append( ", " );
		buf.append( dto.getCheckerid() );
		buf.append( ", " );
		buf.append( dto.getCheckdate() );
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
