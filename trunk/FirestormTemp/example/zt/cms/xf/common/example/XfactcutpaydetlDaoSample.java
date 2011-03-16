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
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.dto.Xfactcutpaydetl;
import zt.cms.xf.common.exceptions.XfactcutpaydetlDaoException;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;

public class XfactcutpaydetlDaoSample
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
		
		// findWhereJournalnoPoanoEquals("", "");
		// findWhereCpanoEquals("");
		// findWhereServicechargeEquals(null);
		// findAll();
		// findWhereJournalnoEquals("");
		// findWhereContractnoEquals("");
		// findWherePoanoEquals(null);
		// findWhereClientnameEquals("");
		// findWhereClientactEquals("");
		// findWherePaybackamtEquals(null);
		// findWherePaybackdateEquals(null);
		// findWherePaybackactEquals("");
		// findWherePaybackactnameEquals("");
		// findWherePaybackbankidEquals("");
		// findWherePaybackbanknoEquals("");
		// findWherePaybackbanknameEquals("");
		// findWhereRecvactEquals("");
		// findWhereRecvbankidEquals("");
		// findWhereRecvbanknoEquals("");
		// findWhereStartdateEquals(null);
		// findWhereBillstatusEquals("");
		// findWherePaidupamtEquals(null);
		// findWherePaidupdateEquals(null);
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
		// findWherePrincipalamtEquals(null);
		// findWhereServicechargefeeEquals(null);
		// findWhereLatefeeEquals(null);
		// findWhereBreachfeeEquals(null);
		// findWhereBilltypeEquals("");
		// findWhereCustomerCodeEquals("");
		// findWhereSignAccountNoEquals("");
		// findWhereClientnoEquals("");
		// findWhereClientidtypeEquals("");
		// findWhereClientidEquals("");
		// findWhereFailurereasonEquals("");
		// findWhereTxjournalnoEquals("");
	}

	/**
	 * Method 'findWhereJournalnoPoanoEquals'
	 * 
	 * @param journalno
	 * @param poano
	 */
	public static void findWhereJournalnoPoanoEquals(String journalno, String poano)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereJournalnoPoanoEquals(journalno, poano);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereCpanoEquals'
	 * 
	 * @param cpano
	 */
	public static void findWhereCpanoEquals(String cpano)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereCpanoEquals(cpano);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereServicechargeEquals'
	 * 
	 * @param servicecharge
	 */
	public static void findWhereServicechargeEquals(BigDecimal servicecharge)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereServicechargeEquals(servicecharge);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findAll'
	 * 
	 */
	public static void findAll()
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findAll();
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereJournalnoEquals'
	 * 
	 * @param journalno
	 */
	public static void findWhereJournalnoEquals(String journalno)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereJournalnoEquals(journalno);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereContractnoEquals(contractno);
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
	public static void findWherePoanoEquals(BigDecimal poano)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePoanoEquals(poano);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereClientnameEquals'
	 * 
	 * @param clientname
	 */
	public static void findWhereClientnameEquals(String clientname)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereClientnameEquals(clientname);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereClientactEquals(clientact);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePaybackamtEquals'
	 * 
	 * @param paybackamt
	 */
	public static void findWherePaybackamtEquals(BigDecimal paybackamt)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePaybackamtEquals(paybackamt);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePaybackdateEquals'
	 * 
	 * @param paybackdate
	 */
	public static void findWherePaybackdateEquals(Date paybackdate)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePaybackdateEquals(paybackdate);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePaybackactEquals(paybackact);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePaybackactnameEquals'
	 * 
	 * @param paybackactname
	 */
	public static void findWherePaybackactnameEquals(String paybackactname)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePaybackactnameEquals(paybackactname);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePaybackbankidEquals(paybackbankid);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePaybackbanknoEquals(paybackbankno);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePaybackbanknameEquals'
	 * 
	 * @param paybackbankname
	 */
	public static void findWherePaybackbanknameEquals(String paybackbankname)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePaybackbanknameEquals(paybackbankname);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereRecvactEquals(recvact);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereRecvbankidEquals(recvbankid);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereRecvbanknoEquals(recvbankno);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereStartdateEquals(startdate);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereBillstatusEquals'
	 * 
	 * @param billstatus
	 */
	public static void findWhereBillstatusEquals(String billstatus)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereBillstatusEquals(billstatus);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePaidupamtEquals'
	 * 
	 * @param paidupamt
	 */
	public static void findWherePaidupamtEquals(BigDecimal paidupamt)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePaidupamtEquals(paidupamt);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWherePaidupdateEquals'
	 * 
	 * @param paidupdate
	 */
	public static void findWherePaidupdateEquals(Date paidupdate)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePaidupdateEquals(paidupdate);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereOperatoridEquals(operatorid);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereOperatedateEquals(operatedate);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereCheckeridEquals(checkerid);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereCheckdateEquals(checkdate);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereCreatoridEquals(creatorid);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereCreatedateEquals(createdate);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereCreateformEquals(createform);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereUpdatoridEquals(updatorid);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereUpdatedateEquals(updatedate);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereUpdateformEquals(updateform);
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
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWherePrincipalamtEquals(principalamt);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereServicechargefeeEquals'
	 * 
	 * @param servicechargefee
	 */
	public static void findWhereServicechargefeeEquals(BigDecimal servicechargefee)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereServicechargefeeEquals(servicechargefee);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereLatefeeEquals'
	 * 
	 * @param latefee
	 */
	public static void findWhereLatefeeEquals(BigDecimal latefee)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereLatefeeEquals(latefee);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereBreachfeeEquals'
	 * 
	 * @param breachfee
	 */
	public static void findWhereBreachfeeEquals(BigDecimal breachfee)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereBreachfeeEquals(breachfee);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereBilltypeEquals'
	 * 
	 * @param billtype
	 */
	public static void findWhereBilltypeEquals(String billtype)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereBilltypeEquals(billtype);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereCustomerCodeEquals'
	 * 
	 * @param customerCode
	 */
	public static void findWhereCustomerCodeEquals(String customerCode)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereCustomerCodeEquals(customerCode);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereSignAccountNoEquals'
	 * 
	 * @param signAccountNo
	 */
	public static void findWhereSignAccountNoEquals(String signAccountNo)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereSignAccountNoEquals(signAccountNo);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereClientnoEquals'
	 * 
	 * @param clientno
	 */
	public static void findWhereClientnoEquals(String clientno)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereClientnoEquals(clientno);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereClientidtypeEquals'
	 * 
	 * @param clientidtype
	 */
	public static void findWhereClientidtypeEquals(String clientidtype)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereClientidtypeEquals(clientidtype);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereClientidEquals'
	 * 
	 * @param clientid
	 */
	public static void findWhereClientidEquals(String clientid)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereClientidEquals(clientid);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereFailurereasonEquals'
	 * 
	 * @param failurereason
	 */
	public static void findWhereFailurereasonEquals(String failurereason)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereFailurereasonEquals(failurereason);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'findWhereTxjournalnoEquals'
	 * 
	 * @param txjournalno
	 */
	public static void findWhereTxjournalnoEquals(String txjournalno)
	{
		try {
			XfactcutpaydetlDao _dao = getXfactcutpaydetlDao();
			Xfactcutpaydetl _result[] = _dao.findWhereTxjournalnoEquals(txjournalno);
			for (int i=0; i<_result.length; i++ ) {
				display( _result[i] );
			}
		
		}
		catch (Exception _e) {
			_e.printStackTrace();
		}
		
	}

	/**
	 * Method 'getXfactcutpaydetlDao'
	 * 
	 * @return XfactcutpaydetlDao
	 */
	public static XfactcutpaydetlDao getXfactcutpaydetlDao()
	{
		return XfactcutpaydetlDaoFactory.create();
	}

	/**
	 * Method 'display'
	 * 
	 * @param dto
	 */
	public static void display(Xfactcutpaydetl dto)
	{
		StringBuffer buf = new StringBuffer();
		buf.append( dto.getJournalno() );
		buf.append( ", " );
		buf.append( dto.getPaybackamt() );
		buf.append( ", " );
		buf.append( dto.getPaybackdate() );
		buf.append( ", " );
		buf.append( dto.getPaybackact() );
		buf.append( ", " );
		buf.append( dto.getRecvact() );
		buf.append( ", " );
		buf.append( dto.getPaybackbankid() );
		buf.append( ", " );
		buf.append( dto.getPaybackbankno() );
		buf.append( ", " );
		buf.append( dto.getRecvbankid() );
		buf.append( ", " );
		buf.append( dto.getRecvbankno() );
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
		buf.append( ", " );
		buf.append( dto.getContractno() );
		buf.append( ", " );
		buf.append( dto.getPoano() );
		buf.append( ", " );
		buf.append( dto.getBillstatus() );
		buf.append( ", " );
		buf.append( dto.getPaidupamt() );
		buf.append( ", " );
		buf.append( dto.getClientname() );
		buf.append( ", " );
		buf.append( dto.getClientact() );
		buf.append( ", " );
		buf.append( dto.getPaybackbankname() );
		buf.append( ", " );
		buf.append( dto.getStartdate() );
		buf.append( ", " );
		buf.append( dto.getPaidupdate() );
		buf.append( ", " );
		buf.append( dto.getOperatorid() );
		buf.append( ", " );
		buf.append( dto.getOperatedate() );
		buf.append( ", " );
		buf.append( dto.getCheckerid() );
		buf.append( ", " );
		buf.append( dto.getCheckdate() );
		buf.append( ", " );
		buf.append( dto.getPaybackactname() );
		buf.append( ", " );
		buf.append( dto.getPrincipalamt() );
		buf.append( ", " );
		buf.append( dto.getLatefee() );
		buf.append( ", " );
		buf.append( dto.getBreachfee() );
		buf.append( ", " );
		buf.append( dto.getServicechargefee() );
		buf.append( ", " );
		buf.append( dto.getBilltype() );
		buf.append( ", " );
		buf.append( dto.getCustomerCode() );
		buf.append( ", " );
		buf.append( dto.getSignAccountNo() );
		buf.append( ", " );
		buf.append( dto.getClientno() );
		buf.append( ", " );
		buf.append( dto.getClientidtype() );
		buf.append( ", " );
		buf.append( dto.getClientid() );
		buf.append( ", " );
		buf.append( dto.getFailurereason() );
		buf.append( ", " );
		buf.append( dto.getTxjournalno() );
		System.out.println( buf.toString() );
	}

}