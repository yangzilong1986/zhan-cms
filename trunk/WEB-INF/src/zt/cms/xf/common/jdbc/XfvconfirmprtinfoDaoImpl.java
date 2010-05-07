package zt.cms.xf.common.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import zt.cms.xf.common.dao.XfvconfirmprtinfoDao;
import zt.cms.xf.common.dto.Xfvconfirmprtinfo;
import zt.cms.xf.common.exceptions.XfvcontractprtinfoDaoException;

/**
 * @author LiChen
 * @see zt.cms.xf.common.jdbc.AbstractDAO
 * @see zt.cms.xf.common.dao.XfvconfirmprtinfoDao
 * <p>查询审批单信息并生成Xfvconfirmprtinfo对象</p>
 * */

public class XfvconfirmprtinfoDaoImpl extends AbstractDAO implements XfvconfirmprtinfoDao
{
	protected java.sql.Connection userConn;
	protected int maxRows;

	// 申请人
	protected static final int COLUMN_EMPNO=25 ;// 员工卡号
	protected static final int COLUMN_NAME = 15;	// 姓名
	protected static final int COLUMN_ID = 66;	// 身份证编号
	protected static final int COLUMN_CURRENTADDRESS = 7;	// 地址
	protected static final int COLUMN_RESIDENCEADR = 20;	// 户籍所在地
	protected static final int COLUMN_PHONE1 = 12;	// 联系电话
	protected static final int COLUMN_GENDER = 3;	// 性别
	protected static final int COLUMN_MARRIAGESTATUS = 5; // 婚姻状况
	protected static final int COLUMN_BIRTHDAY = 2;	// 出生日期
	
	// 工作资料
	protected static final int COLUMN_COMPANY = 8;	// 工作单位
	protected static final int COLUMN_SERVFORM = 19;	// 现单位工作时间
	protected static final int COLUMN_COMADDR = 18;	// 单位地址
	protected static final int COLUMN_PHONE3 = 14;	// 单位电话
	protected static final int COLUMN_CONFMONPAY = 67;	// 每月收入
	
	// 分期付款情况
	protected static final int COLUMN_APPAMT = 40;	// 申请分期金额
	protected static final int COLUMN_COMMNAME = 34; // 产品名称
	protected static final int COLUMN_DIVID = 41; // 期数
	protected static final int COLUMN_COMMISSIONRATE = 43; // 月手续费率
	protected static final int COLUMN_AMT = 38; //	商品总价款
	protected static final int COLUMN_PROPORTION = 44; // 分期金额占所购产品价值比例
	protected static final int COLUMN_ADDR = 36; // 产品使用地址
	protected static final int COLUMN_APPAMTMON = 42; // 每月还款额
	protected static final int COLUMN_MONTHPROP = 45; // 月收入倍数
	
	// 征信系统记录
	protected static final int COLUMN_CCVALIDPERIOD = 71;	// 记录月数
	protected static final int COLUMN_CCDYNUM = 72; // 贷款(卡)数量_抵押贷款
	protected static final int COLUMN_CCXYNUM = 73; // 贷款(卡)数量_信用贷款
	protected static final int COLUMN_CCCDNUM = 74; // 贷款(卡)数量_信用卡
	protected static final int COLUMN_CCLOANYEARQUERYTIME = 100;	// 贷款审批次数
	protected static final int COLUMN_CCCARDYEARQUERYTIME = 101; // 信用卡审批次数
	protected static final int COLUMN_CCDYAMT = 75;	// 总贷款额/额度_抵押贷款
	protected static final int COLUMN_CCXYAMT = 76;	// 总贷款额/额度_信用贷款
	protected static final int COLUMN_CCCDAMT = 77; // 总贷款额/额度_信用卡
	protected static final int COLUMN_CCDYNOWBAL = 78;	//总贷款余额/额度_抵押贷款
	protected static final int COLUMN_CCXYNOWBAL = 79; // 总贷款余额/额度_信用贷款
	protected static final int COLUMN_CCCDNEWBAL = 80; // 总贷款余额/额度_信用卡
	protected static final int COLUMN_CCRPNOWAMT = 102; // 现有贷款还款额
	protected static final int COLUMN_CCDYRPMON = 81; // 当月还款额_抵押贷款
	protected static final int COLUMN_CCXYRPMON = 82; // 当月还款额_信用贷款
	protected static final int COLUMN_CCCDRPMON = 83; // 当月还款额_信用卡
	protected static final int COLUMN_CCDIVIDAMT = 113;	// 该笔分期还款额
	protected static final int COLUMN_CCYEARRPTIME=84; // 12个月还款记录(次数)
	protected static final int COLUMN_CCRPTOTALAMT = 114;// 总还款额
	protected static final int COLUMN_CCDYNOOVERDUE = 85; // 无逾期_抵押贷款
	protected static final int COLUMN_CCXYNOOVERDUE = 86; // 无逾期_信用贷款
	protected static final int COLUMN_CCCDNOOVERDUE = 87; // 无逾期_信用卡
	protected static final int COLUMN_CCRPRATE = 115; // 每月还款与收入比
	protected static final int COLUMN_CCDY1TIMEOVERDUE = 88; //逾期1-30天(1)_抵押贷款
	protected static final int COLUMN_CCXY1TIMEOVERDUE = 89; //逾期1-30天(1)_信用贷款
	protected static final int COLUMN_CCCD1TIMEOVERDUE = 90; //逾期1-30天(1)_信用卡
	protected static final int COLUMN_CCDY2TIMEOVERDUE = 91; //逾期1-30天(2)_抵押贷款
	protected static final int COLUMN_CCXY2TIMEOVERDUE = 92; //逾期1-30天(2)_信用贷款
	protected static final int COLUMN_CCCD2TIMEOVERDUE = 93; //逾期1-30天(2)_信用卡
	protected static final int COLUMN_CCDY3TIMEOVERDUE = 94; //逾期1-30天(3以上)_抵押贷款
	protected static final int COLUMN_CCXY3TIMEOVERDUE = 95; //逾期1-30天(3以上)_信用贷款
	protected static final int COLUMN_CCCD3TIMEOVERDUE = 96; //逾期1-30天(3以上)_信用卡
	
	// 授信要求
	protected static final int COLUMN_ACAGE = 103; // 年龄要求
	protected static final int COLUMN_ACWAGE = 104; // 每月最低工资
	protected static final int COLUMN_ACEMP = 116; // 员工身份_海尔正式员工
	protected static final int COLUMN_ACJOB = 105; // 员工身份_外包工
	protected static final int COLUMN_ACCONTRACT = 106; // 劳动合同年限要求
	protected static final int COLUMN_ACZX1 = 117; // 征信系统内最近一期无未还款记录
	protected static final int COLUMN_ACZX2 = 118; // 征信系统内过去12个月最高逾期期数在5次以下
	protected static final int COLUMN_ACZX3 = 119; // 征信系统内过去12个月最长逾期天数在60天以下
	protected static final int COLUMN_ACFACILITY = 108;// 信用分期总额超过人民币30,000 或12倍月收入
	protected static final int COLUMN_ACRATE = 109; // 每月还款与收入比是否低于70%
	protected static final int COLUMN_ACACCEPTREASON = 112;// 提供特殊接受理由
	
	public Xfvconfirmprtinfo findWhereAppnoEquals(String appno) throws XfvcontractprtinfoDaoException
	{
		String sql = "select " +
				"c.CLIENTNO,c.BIRTHDAY,c.GENDER,c.NATIONALITY,c.MARRIAGESTATUS," +
				"c.HUKOUADDRESS,c.CURRENTADDRESS,c.COMPANY,c.TITLE,c.QUALIFICATION," +
				"c.EDULEVEL,c.PHONE1,c.PHONE2,c.PHONE3,c.NAME," +
				"c.CLIENTTYPE,c.DEGREETYPE,c.COMADDR,c.SERVFROM,c.RESIDENCEADR," +
				"c.HOUSINGSTS,c.HEALTHSTATUS,c.MONTHLYPAY,c.BURDENSTATUS,c.EMPNO," +
				"c.SOCIALSECURITY,c.LIVEFROM,c.PC,c.COMPC,c.RESDPC," +
				"c.RESDADDR,c.EMAIL,m.CHANNEL,m.COMMNAME,m.COMMTYPE," +
				"m.ADDR,m.NUM,m.AMT,m.RECEIVEAMT,m.APPAMT," +
				"m.DIVID,ROUND((m.APPAMT*m.COMMISSIONRATE+m.APPAMT/m.DIVID),2) APPAMTMON,m.COMMISSIONRATE,ROUND(m.APPAMT*100/m.AMT,2) PROPORTION,ROUND(m.APPAMT/a.CONFMONPAY,2) MONTHPROP," +
                "d.ACTOPENINGBANK,d.BANKACTNO,d.XY,d.XYR,d.DY," +
                "d.DYW,d.ZY,d.ZYW,d.BZ,d.BZR," +
                "d.CREDITTYPE,d.MONPAYAMT,d.LINKMAN,d.LINKMANGENDER,d.LINKMANPHONE1," +
                "d.LINKMANPHONE2,d.APPRELATION,d.LINKMANADD,d.LINKMANCOMPANY,a.IDTYPE," +
                "a.ID,a.CONFMONPAY,a.APPDATE,a.APPTYPE,a.APPSTATUS," +
                "i.ccvalidperiod, i.ccdynum, i.ccxynum, i.cccdnum, i.ccdyamt, " +
                "i.ccxyamt, i.cccdamt, i.ccdynowbal, i.ccxynowbal,i.cccdnowbal, " +
                "i.ccdyrpmon, i.ccxyrpmon, i.cccdrpmon, i.ccyearrptime, i.ccdynooverdue, " +
                "i.ccxynooverdue, i.cccdnooverdue,i.ccdy1timeoverdue, i.ccxy1timeoverdue, i.cccd1timeoverdue, " +
                "i.ccdy2timeoverdue, i.ccxy2timeoverdue, i.cccd2timeoverdue, i.ccdym3timeoverdue, i.ccxy3timeoverdue, " +
                "i.cccd3timeoverdue, i.ccdyin3monoverdue, i.ccxyin3monoverdue, i.cccdin3monoverdue, i.ccloanyearquerytime, " +
                "i.cccardyearquerytime, i.ccrpnowamt, i.acage, i.acwage, i.acjob, " +
                "i.accontract, i.acusufruct,i.acfacility, i.acrate, i.acresidenceadr, " +
                "i.acimmunity, i.acacceptreason, i.ccdividamt, i.ccrptotalamt, i.ccrprate, " +
                "i.acemp,i.aczx1, i.aczx2, i.aczx3 " +
                "from CMINDVCLIENT c,XFAPPCOMM m,XFAPPADD d,XFAPP a,XFCREDITINFO i " +
                "where a.APPNO='" + appno + "' and a.IDTYPE=c.IDTYPE and a.ID=c.ID " +
                "and a.APPNO=m.APPNO " +
                "and a.APPNO=d.APPNO " +
                "and a.APPNO=i.APPNO";
		Xfvconfirmprtinfo ret[] = findByDynamicSelect(sql);
		return ret.length==0 ? null : ret[0];
	}

	/**
	 * Method 'XfvcontractprtinfoDaoImpl'
	 * 
	 */
	public XfvconfirmprtinfoDaoImpl()
	{
	}

	/**
	 * Method 'XfvcontractprtinfoDaoImpl'
	 * 
	 * @param userConn
	 */
	public XfvconfirmprtinfoDaoImpl(final java.sql.Connection userConn)
	{
		this.userConn = userConn;
	}



	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Xfvconfirmprtinfo[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Xfvconfirmprtinfo dto = new Xfvconfirmprtinfo();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Xfvconfirmprtinfo ret[] = new Xfvconfirmprtinfo[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Xfvconfirmprtinfo dto, ResultSet rs) throws SQLException
	{
		dto.setEmpno(rs.getString(COLUMN_EMPNO));
		dto.setName(rs.getString(COLUMN_NAME));
		dto.setId(rs.getString(COLUMN_ID));
		dto.setCurrentaddress(rs.getString(COLUMN_CURRENTADDRESS));
		dto.setResidenceadr(rs.getInt(COLUMN_RESIDENCEADR));
		dto.setPhone1(rs.getString(COLUMN_PHONE1));
		dto.setGender(rs.getInt(COLUMN_GENDER));
		dto.setMarriagestatus(rs.getString(COLUMN_MARRIAGESTATUS));
		dto.setBirthday(rs.getDate(COLUMN_BIRTHDAY));
		dto.setCompany(rs.getString(COLUMN_COMPANY));
		dto.setServform(rs.getString(COLUMN_SERVFORM));
		dto.setComaddr(rs.getString(COLUMN_COMADDR));
		dto.setPhone3(rs.getString(COLUMN_PHONE3));
		dto.setConfmonpay(rs.getBigDecimal(COLUMN_CONFMONPAY));
		dto.setAppamt(rs.getBigDecimal(COLUMN_APPAMT));
		dto.setCommname(rs.getString(COLUMN_COMMNAME));
		dto.setDivid(rs.getBigDecimal(COLUMN_DIVID));
		dto.setCommissionrate(rs.getBigDecimal(COLUMN_COMMISSIONRATE));
		dto.setAmt(rs.getBigDecimal(COLUMN_AMT));
		dto.setProportion(rs.getBigDecimal(COLUMN_PROPORTION));
		dto.setAddr(rs.getString(COLUMN_ADDR));
		dto.setAppamtmon(rs.getBigDecimal(COLUMN_APPAMTMON));
		dto.setMonthprop(rs.getBigDecimal(COLUMN_MONTHPROP));
		dto.setCcvalidperiod(rs.getString(COLUMN_CCVALIDPERIOD));
		dto.setCcdynum(rs.getBigDecimal(COLUMN_CCDYNUM));
		dto.setCcxynum(rs.getBigDecimal(COLUMN_CCXYNUM));
		dto.setCccdnum(rs.getBigDecimal(COLUMN_CCCDNUM));
		dto.setCcloanyearquerytime(rs.getBigDecimal(COLUMN_CCLOANYEARQUERYTIME));
		dto.setCccardyearquerytime(rs.getBigDecimal(COLUMN_CCCARDYEARQUERYTIME));
		dto.setCcdyamt(rs.getBigDecimal(COLUMN_CCDYAMT));
		dto.setCcxyamt(rs.getBigDecimal(COLUMN_CCXYAMT));
		dto.setCccdamt(rs.getBigDecimal(COLUMN_CCCDAMT));
		dto.setCcdynowbal(rs.getBigDecimal(COLUMN_CCDYNOWBAL));
		dto.setCcxynowbal(rs.getBigDecimal(COLUMN_CCXYNOWBAL));
		dto.setCccdnewbal(rs.getBigDecimal(COLUMN_CCCDNEWBAL));
		dto.setCcrpnowamt(rs.getBigDecimal(COLUMN_CCRPNOWAMT));
		dto.setCcdyrpmon(rs.getBigDecimal(COLUMN_CCDYRPMON));
		dto.setCcxyrpmon(rs.getBigDecimal(COLUMN_CCXYRPMON));
		dto.setCccdrpmon(rs.getBigDecimal(COLUMN_CCCDRPMON));
		dto.setCcyearrptime(rs.getBigDecimal(COLUMN_CCYEARRPTIME));
		dto.setCcdividamt(rs.getBigDecimal(COLUMN_CCDIVIDAMT));
		dto.setCcrptotalamt(rs.getBigDecimal(COLUMN_CCRPTOTALAMT));
		dto.setCcdynooverdue(rs.getString(COLUMN_CCDYNOOVERDUE));
		dto.setCcxynooverdue(rs.getString(COLUMN_CCXYNOOVERDUE));
		dto.setCccdnooverdue(rs.getString(COLUMN_CCCDNOOVERDUE));
		dto.setCcrprate(rs.getBigDecimal(COLUMN_CCRPRATE));
		dto.setCcdy1timeoverdue(rs.getBigDecimal(COLUMN_CCDY1TIMEOVERDUE));
		dto.setCcxy1timeoverdue(rs.getBigDecimal(COLUMN_CCXY1TIMEOVERDUE));
		dto.setCccd1timeoverdue(rs.getBigDecimal(COLUMN_CCCD1TIMEOVERDUE));
		dto.setCcdy2timeoverdue(rs.getBigDecimal(COLUMN_CCDY2TIMEOVERDUE));
		dto.setCcxy2timeoverdue(rs.getBigDecimal(COLUMN_CCXY2TIMEOVERDUE));
		dto.setCccd2timeoverdue(rs.getBigDecimal(COLUMN_CCCD2TIMEOVERDUE));
		dto.setCcdy3timeoverdue(rs.getBigDecimal(COLUMN_CCDY3TIMEOVERDUE));
		dto.setCcxy3timeoverdue(rs.getBigDecimal(COLUMN_CCXY3TIMEOVERDUE));
		dto.setCccd3timeoverdue(rs.getBigDecimal(COLUMN_CCCD3TIMEOVERDUE));
		dto.setAcage(rs.getString(COLUMN_ACAGE));
		dto.setAcwage(rs.getString(COLUMN_ACWAGE));
		dto.setAcemp(rs.getString(COLUMN_ACEMP));
		dto.setAcjob(rs.getString(COLUMN_ACJOB));
		dto.setAccontract(rs.getString(COLUMN_ACCONTRACT));
		dto.setAczx1(rs.getString(COLUMN_ACZX1));
		dto.setAczx2(rs.getString(COLUMN_ACZX2));
		dto.setAczx3(rs.getString(COLUMN_ACZX3));
		dto.setAcfacility(rs.getString(COLUMN_ACFACILITY));
		dto.setAcrate(rs.getString(COLUMN_ACRATE));
		dto.setAcacceptreason(rs.getString(COLUMN_ACACCEPTREASON));
	}


	public Xfvconfirmprtinfo[] findByDynamicSelect(String sql) throws XfvcontractprtinfoDaoException
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
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new XfvcontractprtinfoDaoException( "Exception: " + _e.getMessage(), _e );
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
