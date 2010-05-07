package zt.cms.bm.workbench;
/**
 * <p/>===============================================
 * <p/>Title: 企业基本财务分析信息维护
 * <p/>===============================================
 * <p/>新增、修改企业基本财务分析信息
 * @version  $Revision: 1.2 $  $Date: 2007/05/28 09:26:02 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $     
*/
import zt.cms.report.db.*;
import zt.cmsi.fc.DBUtil;
public class Cwfc {

	
	/**
	 * 新增企业基本财务记录
	 * @param fm FormCwfx 企业基本财务信息
	 * @return boolean
	 * @throws Exception
	 */
	public boolean addCwfx(FormCwfx fm)throws Exception
	{
		StringBuffer sql=new StringBuffer();
		sql.append("insert into fcqycw(clientno,dt,cuzb1,cuzb2,cuzb3,cuzb4,cuzb5,cuzb6,cuzb7,cuzb8,cuzb9,cuzb10,");
		sql.append("cuzb11,cuzb12,cuzb13,cuzb14,dbfx,cwfx,fcwfx,hkly1,hkly2,hkly3,qtsm)values");
		sql.append("('"+fm.getCLIENTNO()+"',"+DBUtil.toSqlDate(fm.getDT())+","+fm.getCUZB1()+","+fm.getCUZB2()+",");
		sql.append(fm.getCUZB3()+","+fm.getCUZB4()+","+fm.getCUZB5()+","+fm.getCUZB6()+",");
		sql.append(fm.getCUZB7()+","+fm.getCUZB8()+","+fm.getCUZB9()+","+fm.getCUZB10()+","+fm.getCUZB11()+",");
		sql.append(fm.getCUZB12()+","+fm.getCUZB13()+","+fm.getCUZB14()+",'");
		sql.append(fm.getDBFX()+"','"+fm.getCWFX()+"','"+fm.getFCWFX()+"','"+fm.getHKLY1()+"','"+fm.getHKLY2()+"','");
		sql.append(fm.getHKLY3()+"','"+fm.getQTSM()+"')");
		return DB2_81.ExecCmd(sql.toString().replaceAll("'null'", "NULL"));
	}
	/**
	 * 修改企业基本财务记录
	 * @param fm FormCwfx 企业基本财务信息
	 * @return boolean
	 * @throws Exception
	 */
	public boolean editCwfx(FormCwfx fm)throws Exception
	{
		StringBuffer sql=new StringBuffer();
		sql.append("update fcqycw set ");
		sql.append(" cuzb1="+DBUtil.MoneytoNumber(fm.getCUZB1())+",");
		sql.append(" cuzb2="+DBUtil.MoneytoNumber(fm.getCUZB2())+",");
		sql.append(" cuzb3="+DBUtil.MoneytoNumber(fm.getCUZB3())+",");
		sql.append(" cuzb4="+DBUtil.MoneytoNumber(fm.getCUZB4())+",");
		sql.append(" cuzb5="+fm.getCUZB5()+",");
		sql.append("cuzb6="+fm.getCUZB6()+",");
		sql.append("cuzb7="+fm.getCUZB7()+",");
		sql.append("cuzb8="+fm.getCUZB8()+",");
		sql.append("cuzb9="+fm.getCUZB9()+",");
		sql.append("cuzb10="+fm.getCUZB10()+",");
		sql.append("cuzb11="+fm.getCUZB11()+",");
		sql.append("cuzb12="+fm.getCUZB12()+",");
		sql.append("cuzb13="+fm.getCUZB13()+",");
		sql.append("cuzb14="+fm.getCUZB14()+",");
		sql.append("dbfx='"+fm.getDBFX()+"',");
		sql.append("cwfx='"+fm.getCWFX()+"',");
		sql.append("fcwfx='"+fm.getFCWFX()+"',");
		sql.append("hkly1='"+fm.getHKLY1()+"',");
		sql.append("hkly2='"+fm.getHKLY2()+"',");
		sql.append("hkly3='"+fm.getHKLY3()+"',");
		sql.append("qtsm='"+fm.getQTSM()+"'");
		sql.append(" where clientno='"+fm.getCLIENTNO()+"' and dt='"+fm.getDT()+"'");
		return DB2_81.ExecCmd(sql.toString().replaceAll("'null'", "NULL"));
	}
}
