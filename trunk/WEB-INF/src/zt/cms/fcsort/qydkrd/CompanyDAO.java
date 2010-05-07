package zt.cms.fcsort.qydkrd;

import java.util.ArrayList;
import java.util.List;
import zt.cms.pub.SCBranch;
import zt.platform.cachedb.ConnectionManager;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import zt.platform.db.DBUtil;
import zt.cmsi.pub.define.SystemDate;

public class CompanyDAO {

	private boolean hasCheck = false;

	private String name;// 客户名称

	private String number;// 客户证件号码

	private String yynumber;// 营业执照号

	private String brhid;// 开户网点

	private String creditClass;// 信用等级

	private String clienttype;// 客户类型

	private String clientno;// 客户代码

	public String createdate;// 查询时点

	public String createdate2;// 查询时点2

	private String date1;// 时点1

	private String date2;// 时点2

	private String date3;// 时点3

	private String date4;// 时点4

	private String[] date;

	ConnectionManager manager;// 数据库链接

	private int pageCount;// 结果行数

	private int pageSize;// 每页显示行数

	private int currentPage;// 当前页数

	private int maxPage;// 最大页

	/*
	 * 构造函数
	 */
	public CompanyDAO(HttpServletRequest request) {

		if (manager == null) {
			manager = ConnectionManager.getInstance();
		}
		SystemDate.refresh();
		pageSize = Integer.valueOf(
				request.getParameter("pageSize") == null ? "10" : request
						.getParameter("pageSize")).intValue();
		currentPage = Integer.valueOf(
				request.getParameter("currentPage") == null ? "1" : request
						.getParameter("currentPage")).intValue();
	}

	public int getCurrentPage() {
		return currentPage > getMaxPage() ? getMaxPage() : currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getMaxPage() {
		return maxPage == 0 ? 1 : maxPage;
	}

	public void setMaxPage() {
		maxPage = pageCount / pageSize == 0 ? 1 : (pageCount + pageSize - 1)
				/ pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public String getBrhid() {
		return brhid;
	}

	public void setBrhid(String brhid) {
		this.brhid = brhid;
	}

	public String getClienttype() {
		return clienttype;
	}

	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getYynumber() {
		return yynumber;
	}

	public void setYynumber(String yynumber) {
		this.yynumber = yynumber;
	}

	public int getEndrow() {
		return currentPage >= maxPage ? pageCount : currentPage * pageSize;
	}

	public int getStartrow() {
		return (currentPage - 1) * pageSize + 1;
	}

	/*
	 * 得到结果集行数-一级界面
	 */
	public int getCount() {
		int sum = 0;
		if (brhid != null && !brhid.equals("")) {

			brhid = SCBranch.getSubBranchAll(brhid);

			brhid = brhid.replaceAll(",", "','");
		}
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select distinct C.name D01,rownumber() over() rn,"
					+ "C.id D02," + "C.ICBREGCODE D03," + "C.CLIENTTYPE D04,"
					+ "C.appbrhid D05," + "C.clientno clientno "
					+ "from CMCORPCLIENT C,FCMAIN f where  ");

			if (!name.equals("")) {

				sql.append("C.name like '%" + name + "%' and ");
			}
			if (!number.equals("")) {

				sql.append("C.id ='" + number + "' and ");
			}
			if (!yynumber.equals("")) {
				sql.append("C.ICBREGCODE ='" + yynumber + "' and ");
			}
			if (!clienttype.equals("") && !clienttype.equals("0")) {
				sql.append("C.CLIENTTYPE=" + clienttype + " and ");
			}
			if (!brhid.equals("")) {
				sql
						.append("C.appbrhid in('"
								+ brhid
								+ "') and C.id=f.idno and f.createdate='"
								+ createdate
								+ "'"
								+ " group by C.name,C.id,C.ICBREGCODE,C.CLIENTTYPE,C.appbrhid,C.clientno");
			}

			CachedRowSet crs = manager.getRs(sql.toString());

			sum = crs.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		hasCheck = true;
		setPageCount(sum);
		setHasCheck(hasCheck);
		return sum;
	}

	/*
	 * 得到结果集-一级界面
	 */
	public List getResult() {
		List infos = new ArrayList();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from( ");
			sql
					.append("select distinct C.name D01,rownumber() over() rn,"
							+ "C.id D02,"
							+ "C.ICBREGCODE D03,"
							+ "(select enudt from ptenuminfodetl where enuid='ClientType' and C.clienttype= enutp) D04,"
							+ "C.appbrhid D05," + "C.clientno clientno "
							+ "from CMCORPCLIENT C,fcmain f where  ");

			if (!name.equals("")) {

				sql.append("C.name like '%" + name + "%' and ");
			}
			if (!number.equals("")) {

				sql.append("C.id ='" + number + "' and ");
			}
			if (!yynumber.equals("")) {
				sql.append("C.ICBREGCODE ='" + yynumber + "' and ");
			}
			if (!clienttype.equals("") && !clienttype.equals("0")) {
				sql.append("C.CLIENTTYPE=" + clienttype + " and ");
			}
			if (!brhid.equals("")) {
				sql
						.append("C.appbrhid in('"
								+ brhid
								+ "') and C.id=f.idno and f.createdate='"
								+ createdate
								+ "'"
								+ " group by C.name,C.id,C.ICBREGCODE,C.CLIENTTYPE,C.appbrhid,C.clientno");
			}

			sql.append(" ) as A where A.rn between " + getStartrow() + " and "
					+ getEndrow() + "");

			CachedRowSet crs = manager.getRs(sql.toString());
			while (crs.next()) {
				CompanyInfo info = new CompanyInfo();
				String name = DBUtil.fromDB(crs.getString("D01") == null ? ""
						: crs.getString("D01"));
				String number = crs.getString("D02") == null ? "" : crs
						.getString("D02");
				String yynumber = crs.getString("D03") == null ? "" : crs
						.getString("D03");
				String clienttype = DBUtil
						.fromDB(crs.getString("D04") == null ? "" : crs
								.getString("D04"));
				String brhid = crs.getString("D05") == null ? "" : crs
						.getString("D05");
				String clientno = DBUtil.fromDB(crs.getString("clientno") == null ? "" : crs
						.getString("clientno"));
				info.setName(name);
				info.setNumber(number);
				info.setYynumber(yynumber);
				info.setClienttype(clienttype);
				info.setClientno(clientno);
				info.setBrhid(brhid);
				infos.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	public String getClientno() {
		return clientno;
	}

	public void setClientno(String clientno) {
		this.clientno = clientno;
	}

	public CompanyInfo getCompanyInfo() {
		String lastYear = SystemDate.getLastYearDate(createdate, "-");

		lastYear = lastYear.replaceAll("-31", "-01");

		String qYear = String.valueOf((Integer.parseInt(lastYear
				.substring(0, 4)) - 1))
				+ "-12-01";

		String dqyear = String.valueOf((Integer.parseInt(lastYear.substring(0,
				4)) - 2))
				+ "-12-01";

		String month = createdate.substring(4, 7);
		CompanyInfo info = new CompanyInfo();
		try {
			StringBuffer sql = new StringBuffer();// 企业基本信息
			sql
					.append("select C.name D01,"
							+ "(select enudt from ptenuminfodetl where enuid='ClientType' and C.clienttype= enutp) D02,"
							+ "SUPERVISION D03," + "LAWPERSON D04,"
							+ "FOUNDDATE D05," + "ADDRESSINLAW D06,"
							+ "CAPITALAMT D07," + "BIZSCOPE D08,"
							+ " appbrhid brhid ");
			sql.append(" from CMCORPCLIENT C where C.clientno='" + clientno
					+ "'");
			CachedRowSet crs = manager.getRs(sql.toString());
			while (crs.next()) {
				String name = DBUtil.fromDB(crs.getString("D01") == null ? ""
						: crs.getString("D01"));
				String clienttype = DBUtil
						.fromDB(crs.getString("D02") == null ? "" : crs
								.getString("D02"));
				String suppervision = DBUtil
						.fromDB(crs.getString("D03") == null ? "" : crs
								.getString("D03"));
				String lawperson = DBUtil
						.fromDB(crs.getString("D04") == null ? "" : crs
								.getString("D04"));
				String foundate = crs.getString("D05") == null ? "" : crs
						.getString("D05");
				String address = DBUtil
						.fromDB(crs.getString("D06") == null ? "" : crs
								.getString("D06"));
				String capital = DBUtil.doubleToStr1(Double.valueOf(
						crs.getString("D07") == null ? "0.00" : crs
								.getString("D07")).doubleValue());
				String bizscope = DBUtil
						.fromDB(crs.getString("D08") == null ? "" : crs
								.getString("D08"));
				String brhidname = SCBranch
						.getSName(crs.getString("brhid") == null ? "" : crs
								.getString("brhid"));
				info.setName(name);
				info.setClienttype(clienttype);
				info.setSuperCompany(suppervision);
				info.setLawPerson(lawperson);
				info.setFounddate(foundate);
				info.setAddresslaw(address);
				info.setCapitalamt(capital);
				info.setBizcope(bizscope);
				info.setBrhid(brhidname);

			}

			StringBuffer sql2 = new StringBuffer();// 企业固定资产
			StringBuffer sql6 = new StringBuffer();
			StringBuffer sql7 = new StringBuffer();
			sql2.append("select g.area area1,g.ESTIMATE estimate1,");
			sql2
					.append("(case when g.MORTAGAGED=1 then '是' else '否' end) mort1 "
							+ "from CMCORPLANDHOLDING g ");
			sql2.append(" where g.clientno='" + clientno + "'");
			sql6.append("select e.area area2,e.landarea landarea,");
			sql6.append("e.ESTIMATE estimate2," +

			"(case when e.MORTAGAGED=1 then '是' else '否' end) mort2 ");
			sql6.append(" from CMCORPESTATE e ");

			sql6.append(" where e.clientno='" + clientno + "'");
			sql7.append("select t.qty qty,");
			sql7.append("t.ESTIMATE estimate3,");
			sql7
					.append("(case when t.MORTAGAGED=1 then '是' else '否' end) mort3 "
							+ "from CMCORPEQUIPMENT t");
			sql7.append(" where t.clientno='" + clientno + "'");
			CachedRowSet crs2 = manager.getRs(sql2.toString());
			CachedRowSet crs6 = manager.getRs(sql6.toString());
			CachedRowSet crs7 = manager.getRs(sql7.toString());
			double are1 = 0.00;
			double are2 = 0.00;
			double lare = 0.00;
			double esti1 = 0.00;
			double esti2 = 0.00;
			double esti3 = 0.00;
			int qtys = 0;
			String ifdiya = "否";
			String ifdiya2 = "否";
			String ifdiya3 = "否";
			while (crs6.next()) {

				are2 += Double.valueOf(
						crs6.getString("area2") == null ? "0.00" : crs6
								.getString("area2")).doubleValue();

				lare += Double.valueOf(
						crs6.getString("landarea") == null ? "0.00" : crs6
								.getString("landarea")).doubleValue();

				esti2 += Double.valueOf(
						crs6.getString("estimate2") == null ? "0.00" : crs6
								.getString("estimate2")).doubleValue();

				String mort2 = crs6.getString("mort2");
				if (mort2.equals("是")) {
					ifdiya2 = "是";
				}
			}
			while (crs7.next()) {

				qtys += Integer.parseInt(crs7.getString("qty") == null ? "0"
						: crs7.getString("qty"));

				esti3 += Double.valueOf(
						crs7.getString("estimate3") == null ? "0.00" : crs7
								.getString("estimate3")).doubleValue();

				String mort3 = crs7.getString("mort3");
				if (mort3.equals("是")) {
					ifdiya3 = "是";
				}
			}
			while (crs2.next()) {
				are1 += Double.valueOf(
						crs2.getString("area1") == null ? "0.00" : crs2
								.getString("area1")).doubleValue();
				esti1 += Double.valueOf(
						crs2.getString("estimate1") == null ? "0.00" : crs2
								.getString("estimate1")).doubleValue();
				String mort1 = crs2.getString("mort1");
				if (mort1.equals("是")) {
					ifdiya = "是";
				}

			}
			String area1 = DBUtil.doubleToStr1(are1);
			String area2 = DBUtil.doubleToStr1(are2);
			String landarea = DBUtil.doubleToStr1(lare);
			String estimate1 = DBUtil.doubleToStr1(esti1);
			String estimate2 = DBUtil.doubleToStr1(esti2);
			String estimate3 = DBUtil.doubleToStr1(esti3);
			String qty = String.valueOf(qtys);
			info.setArea1(area1);
			info.setArea2(area2);
			info.setQty(qty);
			info.setMj1(area1);
			info.setMj2(landarea);
			info.setEstimate1(estimate1);
			info.setEstimate2(estimate2);
			info.setEstimate3(estimate3);
			info.setMortgaged1(ifdiya);
			info.setMortgaged2(ifdiya2);
			info.setMortgaged3(ifdiya3);
			// ****************主要财务指标*************************************
			if (month.equals("-01")) {
				createdate = lastYear;

			} else if (month.equals("-02")) {
				createdate = createdate.substring(0, 4) + "-01-01";
				date2 = lastYear;
			} else if (month.equals("-03")) {

				createdate = createdate.substring(0, 4) + "-02-01";
				date1 = createdate.substring(0, 4) + "-01-01";
				date2 = lastYear;
			} else if (month.equals("-04")) {
				createdate = createdate.substring(0, 4) + "-03-01";
			} else if (month.equals("-05")) {
				createdate = createdate.substring(0, 4) + "-04-01";
				date2 = createdate.substring(0, 4) + "-03-01";
			} else if (month.equals("-06")) {
				createdate = createdate.substring(0, 4) + "-05-01";
				date1 = createdate.substring(0, 4) + "-04-01";
				date2 = createdate.substring(0, 4) + "-03-01";
			} else if (month.equals("-07")) {
				createdate = createdate.substring(0, 4) + "-06-01";
			} else if (month.equals("-08")) {
				createdate = createdate.substring(0, 4) + "-07-01";
				date2 = createdate.substring(0, 4) + "-06-01";
			} else if (month.equals("-09")) {
				createdate = createdate.substring(0, 4) + "-08-01";
				date1 = createdate.substring(0, 4) + "-07-01";
				date2 = createdate.substring(0, 4) + "-06-01";
			} else if (month.equals("-10")) {
				createdate = createdate.substring(0, 4) + "-09-01";
			} else if (month.equals("-11")) {
				createdate = createdate.substring(0, 4) + "-10-01";
				date2 = createdate.substring(0, 4) + "-09-01";
			} else if (month.equals("-12")) {
				createdate = createdate.substring(0, 4) + "-11-01";
				date1 = createdate.substring(0, 4) + "-10-01";
				date2 = createdate.substring(0, 4) + "-09-01";
			}
			StringBuffer sql3 = new StringBuffer();
			if (createdate != null && !createdate.equals("")
					&& (date1 == null || date1.equals(""))
					&& (date2 == null || date2.equals(""))) {
				sql3.append(" with T as (select ");
				sql3.append("(case when dt='" + createdate
						+ "' then cuzb11 else 0 end) D29," + "(case when dt='"
						+ createdate + "' then DBFX else '' end) D30,"
						+ "(case when dt='" + createdate
						+ "' then HKLY1 else '' end) D31," + "(case when dt='"
						+ createdate + "' then HKLY2 else '' end) D32,"
						+ "(case when dt='" + createdate
						+ "' then HKLY3 else '' end) D33," + "(case when dt='"
						+ createdate + "' then QTSM else '' end) D34,"
						+ "(case when dt='" + createdate
						+ "' then CWFX else '' end) D35," + "(case when dt='"
						+ createdate + "' then FCWFX else '' end) D36,"
						+ "(case when dt='" + createdate
						+ "' then cuzb1 else 0 end) CUZB101,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb1 else 0 end) CUZB102,"
						+ "(case when dt='" + qYear
						+ "' then cuzb1 else 0 end) CUZB103,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb1 else 0 end) CUZB104,"
						+ "(case when dt='" + createdate
						+ "' then cuzb2 else 0 end) CUZB201,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb2 else 0 end) CUZB202,"
						+ "(case when dt='" + qYear
						+ "' then cuzb2 else 0 end) CUZB203,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb2 else 0 end) CUZB204,"
						+ "(case when dt='" + createdate
						+ "' then cuzb3 else 0 end) CUZB301,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb3 else 0 end) CUZB302,"
						+ "(case when dt='" + qYear
						+ "' then cuzb3 else 0 end) CUZB303,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb3 else 0 end) CUZB304,"
						+ "(case when dt='" + createdate
						+ "' then cuzb4 else 0 end) CUZB401,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb4 else 0 end) CUZB402,"
						+ "(case when dt='" + qYear
						+ "' then cuzb4 else 0 end) CUZB403,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb4 else 0 end) CUZB404,");
				sql3.append("(case when dt='" + createdate
						+ "' then cuzb5 else 0 end) CUZB501,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb5 else 0 end) CUZB502,"
						+ "(case when dt='" + qYear
						+ "' then cuzb5 else 0 end) CUZB503,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb5 else 0 end) CUZB504,"
						+ "(case when dt='" + createdate
						+ "' then cuzb6 else 0 end) CUZB601,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb6 else 0 end) CUZB602,"
						+ "(case when dt='" + qYear
						+ "' then cuzb6 else 0 end) CUZB603,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb6 else 0 end) CUZB604,"
						+ "(case when dt='" + createdate
						+ "' then cuzb7 else 0 end) CUZB701,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb7 else 0 end) CUZB702,"
						+ "(case when dt='" + qYear
						+ "' then cuzb7 else 0 end) CUZB703,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb7 else 0 end) CUZB704,"
						+ "(case when dt='" + createdate
						+ "' then cuzb8 else 0 end) CUZB801,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb8 else 0 end) CUZB802,"
						+ "(case when dt='" + qYear
						+ "' then cuzb8 else 0 end) CUZB803,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb8 else 0 end) CUZB804,"
						+ "(case when dt='" + createdate
						+ "' then cuzb9 else 0 end) CUZB901,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb9 else 0 end) CUZB902,"
						+ "(case when dt='" + qYear
						+ "' then cuzb9 else 0 end) CUZB903,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb9 else 0 end) CUZB904,"
						+ "(case when dt='" + createdate
						+ "' then cuzb10 else 0 end) CUZB1001,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb10 else 0 end) CUZB1002,"
						+ "(case when dt='" + qYear
						+ "' then cuzb10 else 0 end) CUZB1003,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb10 else 0 end) CUZB1004");
				sql3.append(" from FCQYCW f where ");
				sql3
						.append(" f.clientno='"
								+ clientno
								+ "')"
								+ "select max(D29) D29,max(D30) D30,max(D31) D31,max(D32) D32,max(D33) D33,max(D34) D34,max(D35) D35,max(D36) D36,"
								+ "max(CUZB101) CUZB101,max(CUZB102) CUZB102,"
								+ " max(CUZB103) CUZB103,max(CUZB104) CUZB104,max(CUZB201) CUZB201,max(CUZB202) CUZB202,"
								+ "max(CUZB203) CUZB203,max(CUZB104) CUZB204,max(CUZB301) CUZB301,max(CUZB302) CUZB302,"
								+ "max(CUZB303) CUZB303,"
								+ " max(CUZB304) CUZB304,max(CUZB401) CUZB401,max(CUZB402) CUZB402,max(CUZB403) CUZB403,max(CUZB404) CUZB404,"
								+ "max(CUZB501) CUZB501,max(CUZB502) CUZB502,max(CUZB503) CUZB503,max(CUZB504) CUZB504,"
								+ " max(CUZB601) CUZB601,max(CUZB602) CUZB602,max(CUZB603) CUZB603,max(CUZB604) CUZB604,"
								+ "max(CUZB701) CUZB701,max(CUZB702) CUZB702,max(CUZB703) CUZB703,max(CUZB704) CUZB704,"
								+ "max(CUZB801)CUZB801,"
								+ " max(CUZB802) CUZB802,max(CUZB803) CUZB803,max(CUZB804) CUZB804,"
								+ "max(CUZB901) CUZB901,max(CUZB902) CUZB902,max(CUZB903) CUZB903,max(CUZB904) CUZB904,"
								+ "max(CUZB1001) CUZB1001,max(CUZB1002) CUZB1002,"
								+ " max(CUZB1003) CUZB1003,max(CUZB1004) CUZB1004"
								+ " from T ");
				CachedRowSet crs3 = manager.getRs(sql3.toString());
				while (crs3.next()) {
					StringBuffer sqls = new StringBuffer();
					sqls
							.append("select cmt1,cmt2,cmt3,cmt4 from FCCMT fc,fcmain f,BMTABLEAPP b "
									+ "where fccmttype=1 and f.fcno=fc.fcno and f.createdate='"
									+ createdate2
									+ "' and"
									+ " b.bmno=f.bmno and b.clientno='"
									+ clientno + "'");
					CachedRowSet crss = manager.getRs(sqls.toString());
					while (crss.next()) {
						String cmt3 = DBUtil
								.fromDB(crss.getString("cmt1") == null ? ""
										: crss.getString("cmt1"));
						String cmt4 = DBUtil
								.fromDB(crss.getString("cmt2") == null ? ""
										: crss.getString("cmt2"));
						String cmt5 = DBUtil
								.fromDB(crss.getString("cmt3") == null ? ""
										: crss.getString("cmt3"));
						String cmt6 = DBUtil
								.fromDB(crss.getString("cmt4") == null ? ""
										: crss.getString("cmt4"));
						info.setFccmt1(cmt3);
						info.setFccmt2(cmt4);
						info.setFccmt3(cmt5);
						info.setFccmt4(cmt6);
					}

					String cuzb11 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("D29") == null ? "0.00" : crs3
									.getString("D29")).doubleValue() / 10000);
					String dbfx = DBUtil
							.fromDB(crs3.getString("D30") == null ? "" : crs3
									.getString("D30"));
					String hkly1 = DBUtil
							.fromDB(crs3.getString("D31") == null ? "" : crs3
									.getString("D31"));
					String hkly2 = DBUtil
							.fromDB(crs3.getString("D32") == null ? "" : crs3
									.getString("D32"));
					String hkly3 = DBUtil
							.fromDB(crs3.getString("D33") == null ? "" : crs3
									.getString("D33"));
					String qtsm = DBUtil
							.fromDB(crs3.getString("D34") == null ? "" : crs3
									.getString("D34"));
					String cwfx = DBUtil
							.fromDB(crs3.getString("D35") == null ? "" : crs3
									.getString("D35"));
					String fcwfx = DBUtil
							.fromDB(crs3.getString("D36") == null ? "" : crs3
									.getString("D36"));
					String cuzb011 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB101") == null ? "0.00" : crs3
									.getString("CUZB101")).doubleValue());
					String cuzb012 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB102") == null ? "0.00" : crs3
									.getString("CUZB102")).doubleValue());
					String cuzb013 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB103") == null ? "0.00" : crs3
									.getString("CUZB103")).doubleValue());
					String cuzb014 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB104") == null ? "0.00" : crs3
									.getString("CUZB104")).doubleValue());
					String cuzb021 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB201") == null ? "0.00" : crs3
									.getString("CUZB201")).doubleValue());
					String cuzb022 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB202") == null ? "0.00" : crs3
									.getString("CUZB202")).doubleValue());
					String cuzb023 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB203") == null ? "0.00" : crs3
									.getString("CUZB203")).doubleValue());
					String cuzb024 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB204") == null ? "0.00" : crs3
									.getString("CUZB204")).doubleValue());
					String cuzb031 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB301") == null ? "0.00" : crs3
									.getString("CUZB301")).doubleValue());
					String cuzb032 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB302") == null ? "0.00" : crs3
									.getString("CUZB302")).doubleValue());
					String cuzb033 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB303") == null ? "0.00" : crs3
									.getString("CUZB303")).doubleValue());
					String cuzb034 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB304") == null ? "0.00" : crs3
									.getString("CUZB304")).doubleValue());
					String cuzb041 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB401") == null ? "0.00" : crs3
									.getString("CUZB401")).doubleValue());
					String cuzb042 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB402") == null ? "0.00" : crs3
									.getString("CUZB402")).doubleValue());
					String cuzb043 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB403") == null ? "0.00" : crs3
									.getString("CUZB403")).doubleValue());
					String cuzb044 = DBUtil.doubleToStr1(Double.valueOf(
							crs3.getString("CUZB404") == null ? "0.00" : crs3
									.getString("CUZB404")).doubleValue());
					String cuzb051 = crs3.getString("CUZB501") == null ? "0.00"
							: crs3.getString("CUZB501");
					String cuzb052 = crs3.getString("CUZB502") == null ? ""
							: crs3.getString("CUZB502");
					String cuzb053 = crs3.getString("CUZB503") == null ? ""
							: crs3.getString("CUZB503");
					String cuzb054 = crs3.getString("CUZB504") == null ? ""
							: crs3.getString("CUZB504");
					String cuzb061 = crs3.getString("CUZB601") == null ? ""
							: crs3.getString("CUZB601");
					String cuzb062 = crs3.getString("CUZB602") == null ? ""
							: crs3.getString("CUZB602");
					String cuzb063 = crs3.getString("CUZB603") == null ? ""
							: crs3.getString("CUZB603");
					String cuzb064 = crs3.getString("CUZB604") == null ? ""
							: crs3.getString("CUZB604");
					String cuzb071 = crs3.getString("CUZB701") == null ? ""
							: crs3.getString("CUZB701");
					String cuzb072 = crs3.getString("CUZB702") == null ? ""
							: crs3.getString("CUZB702");
					String cuzb073 = crs3.getString("CUZB703") == null ? ""
							: crs3.getString("CUZB703");
					String cuzb074 = crs3.getString("CUZB704") == null ? ""
							: crs3.getString("CUZB704");
					String cuzb081 = crs3.getString("CUZB801") == null ? ""
							: crs3.getString("CUZB801");
					String cuzb082 = crs3.getString("CUZB802") == null ? ""
							: crs3.getString("CUZB802");
					String cuzb083 = crs3.getString("CUZB803") == null ? ""
							: crs3.getString("CUZB803");
					String cuzb084 = crs3.getString("CUZB804") == null ? ""
							: crs3.getString("CUZB804");
					String cuzb091 = crs3.getString("CUZB901") == null ? ""
							: crs3.getString("CUZB901");
					String cuzb092 = crs3.getString("CUZB902") == null ? ""
							: crs3.getString("CUZB902");
					String cuzb093 = crs3.getString("CUZB903") == null ? ""
							: crs3.getString("CUZB903");
					String cuzb094 = crs3.getString("CUZB904") == null ? ""
							: crs3.getString("CUZB904");
					String cuzb101 = crs3.getString("CUZB1001") == null ? ""
							: crs3.getString("CUZB1001");
					String cuzb102 = crs3.getString("CUZB1002") == null ? ""
							: crs3.getString("CUZB1002");
					String cuzb103 = crs3.getString("CUZB1003") == null ? ""
							: crs3.getString("CUZB1003");
					String cuzb104 = crs3.getString("CUZB1004") == null ? ""
							: crs3.getString("CUZB1004");
					info.setCuzb01(cuzb011);
					info.setCuzb011(cuzb012);
					info.setCuzb012(cuzb013);
					info.setCuzb013(cuzb014);
					info.setCuzb02(cuzb021);
					info.setCuzb021(cuzb022);
					info.setCuzb022(cuzb023);
					info.setCuzb023(cuzb024);
					info.setCuzb03(cuzb031);
					info.setCuzb031(cuzb032);
					info.setCuzb032(cuzb033);
					info.setCuzb033(cuzb034);
					info.setCuzb04(cuzb041);
					info.setCuzb041(cuzb042);
					info.setCuzb042(cuzb043);
					info.setCuzb043(cuzb044);
					info.setCuzb05(cuzb051);
					info.setCuzb051(cuzb052);
					info.setCuzb052(cuzb053);
					info.setCuzb053(cuzb054);
					info.setCuzb06(cuzb061);
					info.setCuzb061(cuzb062);
					info.setCuzb062(cuzb063);
					info.setCuzb063(cuzb064);
					info.setCuzb07(cuzb071);
					info.setCuzb071(cuzb072);
					info.setCuzb072(cuzb073);
					info.setCuzb073(cuzb074);
					info.setCuzb08(cuzb081);
					info.setCuzb081(cuzb082);
					info.setCuzb082(cuzb083);
					info.setCuzb083(cuzb084);
					info.setCuzb09(cuzb091);
					info.setCuzb091(cuzb092);
					info.setCuzb092(cuzb093);
					info.setCuzb093(cuzb094);
					info.setCuzb10(cuzb101);
					info.setCuzb101(cuzb102);
					info.setCuzb102(cuzb103);
					info.setCuzb103(cuzb104);
					info.setCuzb11(cuzb11);
					info.setDbfx(dbfx);
					info.setHkly1(hkly1);
					info.setHkly2(hkly2);
					info.setHkly3(hkly3);
					info.setQtsm(qtsm);
					info.setCwfx(cwfx);
					info.setFcwfx(fcwfx);
					info.setCreate(createdate);
				}
			} else if (createdate != null && !createdate.equals("")
					&& (date1 == null || date1.equals(""))
					&& (date2 != null || !date2.equals(""))) {
				sql3.append(" with T as (select ");
				sql3.append("(case when dt='" + createdate
						+ "' then cuzb11 else 0 end) D29," + "(case when dt='"
						+ createdate + "' then DBFX else '' end) D30,"
						+ "(case when dt='" + createdate
						+ "' then HKLY1 else '' end) D31," + "(case when dt='"
						+ createdate + "' then HKLY2 else '' end) D32,"
						+ "(case when dt='" + createdate
						+ "' then HKLY3 else '' end) D33," + "(case when dt='"
						+ createdate + "' then QTSM else '' end) D34,"
						+ "(case when dt='" + createdate
						+ "' then CWFX else '' end) D35," + "(case when dt='"
						+ createdate + "' then FCWFX else '' end) D36,"
						+ "(case when dt='" + createdate
						+ "' then cuzb1 else 0 end) CUZB101,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb1 else 0 end) CUZB102,"
						+ "(case when dt='" + qYear
						+ "' then cuzb1 else 0 end) CUZB103,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb1 else 0 end) CUZB104,"
						+ "(case when dt='" + createdate
						+ "' then cuzb2 else 0 end) CUZB201,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb2 else 0 end) CUZB202,"
						+ "(case when dt='" + qYear
						+ "' then cuzb2 else 0 end) CUZB203,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb2 else 0 end) CUZB204,"
						+ "(case when dt='" + createdate
						+ "' then cuzb3 else 0 end) CUZB301,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb3 else 0 end) CUZB302,"
						+ "(case when dt='" + qYear
						+ "' then cuzb3 else 0 end) CUZB303,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb3 else 0 end) CUZB304,"
						+ "(case when dt='" + createdate
						+ "' then cuzb4 else 0 end) CUZB401,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb4 else 0 end) CUZB402,"
						+ "(case when dt='" + qYear
						+ "' then cuzb4 else 0 end) CUZB403,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb4 else 0 end) CUZB404,");
				sql3.append("(case when dt='" + createdate
						+ "' then cuzb5 else 0 end) CUZB501,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb5 else 0 end) CUZB502,"
						+ "(case when dt='" + qYear
						+ "' then cuzb5 else 0 end) CUZB503,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb5 else 0 end) CUZB504,"
						+ "(case when dt='" + createdate
						+ "' then cuzb6 else 0 end) CUZB601,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb6 else 0 end) CUZB602,"
						+ "(case when dt='" + qYear
						+ "' then cuzb6 else 0 end) CUZB603,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb6 else 0 end) CUZB604,"
						+ "(case when dt='" + createdate
						+ "' then cuzb7 else 0 end) CUZB701,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb7 else 0 end) CUZB702,"
						+ "(case when dt='" + qYear
						+ "' then cuzb7 else 0 end) CUZB703,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb7 else 0 end) CUZB704,"
						+ "(case when dt='" + createdate
						+ "' then cuzb8 else 0 end) CUZB801,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb8 else 0 end) CUZB802,"
						+ "(case when dt='" + qYear
						+ "' then cuzb8 else 0 end) CUZB803,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb8 else 0 end) CUZB804,"
						+ "(case when dt='" + createdate
						+ "' then cuzb9 else 0 end) CUZB901,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb9 else 0 end) CUZB902,"
						+ "(case when dt='" + qYear
						+ "' then cuzb9 else 0 end) CUZB903,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb9 else 0 end) CUZB904,"
						+ "(case when dt='" + createdate
						+ "' then cuzb10 else 0 end) CUZB1001,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb10 else 0 end) CUZB1002,"
						+ "(case when dt='" + qYear
						+ "' then cuzb10 else 0 end) CUZB1003,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb10 else 0 end) CUZB1004");
				sql3.append(" from FCQYCW f where ");
				sql3
						.append(" f.clientno='"
								+ clientno
								+ "')"
								+ "select max(D29) D29,max(D30) D30,max(D31) D31,max(D32) D32,max(D33) D33,max(D34) D34,max(D35) D35,max(D36) D36,"
								+ "max(CUZB101) CUZB101,max(CUZB102) CUZB102,"
								+ " max(CUZB103) CUZB103,max(CUZB104) CUZB104,max(CUZB201) CUZB201,max(CUZB202) CUZB202,"
								+ "max(CUZB203) CUZB203,max(CUZB104) CUZB204,max(CUZB301) CUZB301,max(CUZB302) CUZB302,"
								+ "max(CUZB303) CUZB303,"
								+ " max(CUZB304) CUZB304,max(CUZB401) CUZB401,max(CUZB402) CUZB402,max(CUZB403) CUZB403,max(CUZB404) CUZB404,"
								+ "max(CUZB501) CUZB501,max(CUZB502) CUZB502,max(CUZB503) CUZB503,max(CUZB504) CUZB504,"
								+ " max(CUZB601) CUZB601,max(CUZB602) CUZB602,max(CUZB603) CUZB603,max(CUZB604) CUZB604,"
								+ "max(CUZB701) CUZB701,max(CUZB702) CUZB702,max(CUZB703) CUZB703,max(CUZB704) CUZB704,"
								+ "max(CUZB801)CUZB801,"
								+ " max(CUZB802) CUZB802,max(CUZB803) CUZB803,max(CUZB804) CUZB804,"
								+ "max(CUZB901) CUZB901,max(CUZB902) CUZB902,max(CUZB903) CUZB903,max(CUZB904) CUZB904,"
								+ "max(CUZB1001) CUZB1001,max(CUZB1002) CUZB1002,"
								+ " max(CUZB1003) CUZB1003,max(CUZB1004) CUZB1004"
								+ " from T ");
				CachedRowSet crs3 = manager.getRs(sql3.toString());
				StringBuffer sql10=new StringBuffer();
                sql10.append("select dt from FCQYCW where clientno='"+clientno+"' and dt='"+createdate+"'");
                CachedRowSet crs10 = manager.getRs(sql10.toString());
                if(crs10.size()>0){
				if (crs3.size() > 0) {
					while (crs3.next()) {
						StringBuffer sqls = new StringBuffer();
						sqls
								.append("select cmt1,cmt2,cmt3,cmt4 from FCCMT fc,fcmain f,BMTABLEAPP b "
										+ "where fccmttype=1 and f.fcno=fc.fcno and f.createdate='"
										+ createdate2
										+ "' and"
										+ " b.bmno=f.bmno and b.clientno='"
										+ clientno + "'");
						CachedRowSet crss = manager.getRs(sqls.toString());
						while (crss.next()) {
							String cmt3 = DBUtil
									.fromDB(crss.getString("cmt1") == null ? ""
											: crss.getString("cmt1"));
							String cmt4 = DBUtil
									.fromDB(crss.getString("cmt2") == null ? ""
											: crss.getString("cmt2"));
							String cmt5 = DBUtil
									.fromDB(crss.getString("cmt3") == null ? ""
											: crss.getString("cmt3"));
							String cmt6 = DBUtil
									.fromDB(crss.getString("cmt4") == null ? ""
											: crss.getString("cmt4"));
							info.setFccmt1(cmt3);
							info.setFccmt2(cmt4);
							info.setFccmt3(cmt5);
							info.setFccmt4(cmt6);
						}

						String cuzb11 = DBUtil
								.doubleToStr1(Double.valueOf(
										crs3.getString("D29") == null ? "0.00"
												: crs3.getString("D29"))
										.doubleValue() / 10000);
						String dbfx = DBUtil
								.fromDB(crs3.getString("D30") == null ? ""
										: crs3.getString("D30"));
						String hkly1 = DBUtil
								.fromDB(crs3.getString("D31") == null ? ""
										: crs3.getString("D31"));
						String hkly2 = DBUtil
								.fromDB(crs3.getString("D32") == null ? ""
										: crs3.getString("D32"));
						String hkly3 = DBUtil
								.fromDB(crs3.getString("D33") == null ? ""
										: crs3.getString("D33"));
						String qtsm = DBUtil
								.fromDB(crs3.getString("D34") == null ? ""
										: crs3.getString("D34"));
						String cwfx = DBUtil
								.fromDB(crs3.getString("D35") == null ? ""
										: crs3.getString("D35"));
						String fcwfx = DBUtil
								.fromDB(crs3.getString("D36") == null ? "0.00"
										: crs3.getString("D36"));
						String cuzb011 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB101") == null ? "0.00"
										: crs3.getString("CUZB101"))
								.doubleValue());
						String cuzb012 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB102") == null ? "0.00"
										: crs3.getString("CUZB102"))
								.doubleValue());
						String cuzb013 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB103") == null ? "0.00"
										: crs3.getString("CUZB103"))
								.doubleValue());
						String cuzb014 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB104") == null ? "0.00"
										: crs3.getString("CUZB104"))
								.doubleValue());
						String cuzb021 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB201") == null ? "0.00"
										: crs3.getString("CUZB201"))
								.doubleValue());
						String cuzb022 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB202") == null ? "0.00"
										: crs3.getString("CUZB202"))
								.doubleValue());
						String cuzb023 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB203") == null ? "0.00"
										: crs3.getString("CUZB203"))
								.doubleValue());
						String cuzb024 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB204") == null ? "0.00"
										: crs3.getString("CUZB204"))
								.doubleValue());
						String cuzb031 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB301") == null ? "0.00"
										: crs3.getString("CUZB301"))
								.doubleValue());
						String cuzb032 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB302") == null ? "0.00"
										: crs3.getString("CUZB302"))
								.doubleValue());
						String cuzb033 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB303") == null ? "0.00"
										: crs3.getString("CUZB303"))
								.doubleValue());
						String cuzb034 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB304") == null ? "0.00"
										: crs3.getString("CUZB304"))
								.doubleValue());
						String cuzb041 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB401") == null ? "0.00"
										: crs3.getString("CUZB401"))
								.doubleValue());
						String cuzb042 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB402") == null ? "0.00"
										: crs3.getString("CUZB402"))
								.doubleValue());
						String cuzb043 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB403") == null ? "0.00"
										: crs3.getString("CUZB403"))
								.doubleValue());
						String cuzb044 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB404") == null ? "0.00"
										: crs3.getString("CUZB404"))
								.doubleValue());
						String cuzb051 = crs3.getString("CUZB501") == null ? "0.00"
								: crs3.getString("CUZB501");
						String cuzb052 = crs3.getString("CUZB502") == null ? ""
								: crs3.getString("CUZB502");
						String cuzb053 = crs3.getString("CUZB503") == null ? ""
								: crs3.getString("CUZB503");
						String cuzb054 = crs3.getString("CUZB504") == null ? ""
								: crs3.getString("CUZB504");
						String cuzb061 = crs3.getString("CUZB601") == null ? ""
								: crs3.getString("CUZB601");
						String cuzb062 = crs3.getString("CUZB602") == null ? ""
								: crs3.getString("CUZB602");
						String cuzb063 = crs3.getString("CUZB603") == null ? ""
								: crs3.getString("CUZB603");
						String cuzb064 = crs3.getString("CUZB604") == null ? ""
								: crs3.getString("CUZB604");
						String cuzb071 = crs3.getString("CUZB701") == null ? ""
								: crs3.getString("CUZB701");
						String cuzb072 = crs3.getString("CUZB702") == null ? ""
								: crs3.getString("CUZB702");
						String cuzb073 = crs3.getString("CUZB703") == null ? ""
								: crs3.getString("CUZB703");
						String cuzb074 = crs3.getString("CUZB704") == null ? ""
								: crs3.getString("CUZB704");
						String cuzb081 = crs3.getString("CUZB801") == null ? ""
								: crs3.getString("CUZB801");
						String cuzb082 = crs3.getString("CUZB802") == null ? ""
								: crs3.getString("CUZB802");
						String cuzb083 = crs3.getString("CUZB803") == null ? ""
								: crs3.getString("CUZB803");
						String cuzb084 = crs3.getString("CUZB804") == null ? ""
								: crs3.getString("CUZB804");
						String cuzb091 = crs3.getString("CUZB901") == null ? ""
								: crs3.getString("CUZB901");
						String cuzb092 = crs3.getString("CUZB902") == null ? ""
								: crs3.getString("CUZB902");
						String cuzb093 = crs3.getString("CUZB903") == null ? ""
								: crs3.getString("CUZB903");
						String cuzb094 = crs3.getString("CUZB904") == null ? ""
								: crs3.getString("CUZB904");
						String cuzb101 = crs3.getString("CUZB1001") == null ? ""
								: crs3.getString("CUZB1001");
						String cuzb102 = crs3.getString("CUZB1002") == null ? ""
								: crs3.getString("CUZB1002");
						String cuzb103 = crs3.getString("CUZB1003") == null ? ""
								: crs3.getString("CUZB1003");
						String cuzb104 = crs3.getString("CUZB1004") == null ? ""
								: crs3.getString("CUZB1004");
						info.setCuzb01(cuzb011);
						info.setCuzb011(cuzb012);
						info.setCuzb012(cuzb013);
						info.setCuzb013(cuzb014);
						info.setCuzb02(cuzb021);
						info.setCuzb021(cuzb022);
						info.setCuzb022(cuzb023);
						info.setCuzb023(cuzb024);
						info.setCuzb03(cuzb031);
						info.setCuzb031(cuzb032);
						info.setCuzb032(cuzb033);
						info.setCuzb033(cuzb034);
						info.setCuzb04(cuzb041);
						info.setCuzb041(cuzb042);
						info.setCuzb042(cuzb043);
						info.setCuzb043(cuzb044);
						info.setCuzb05(cuzb051);
						info.setCuzb051(cuzb052);
						info.setCuzb052(cuzb053);
						info.setCuzb053(cuzb054);
						info.setCuzb06(cuzb061);
						info.setCuzb061(cuzb062);
						info.setCuzb062(cuzb063);
						info.setCuzb063(cuzb064);
						info.setCuzb07(cuzb071);
						info.setCuzb071(cuzb072);
						info.setCuzb072(cuzb073);
						info.setCuzb073(cuzb074);
						info.setCuzb08(cuzb081);
						info.setCuzb081(cuzb082);
						info.setCuzb082(cuzb083);
						info.setCuzb083(cuzb084);
						info.setCuzb09(cuzb091);
						info.setCuzb091(cuzb092);
						info.setCuzb092(cuzb093);
						info.setCuzb093(cuzb094);
						info.setCuzb10(cuzb101);
						info.setCuzb101(cuzb102);
						info.setCuzb102(cuzb103);
						info.setCuzb103(cuzb104);
						info.setCuzb11(cuzb11);
						info.setDbfx(dbfx);
						info.setHkly1(hkly1);
						info.setHkly2(hkly2);
						info.setHkly3(hkly3);
						info.setQtsm(qtsm);
						info.setCwfx(cwfx);
						info.setFcwfx(fcwfx);
						info.setCreate(createdate);
					}
				} 
                }else {
					StringBuffer sql4 = new StringBuffer();
					sql4.append(" with T as (select ");
					sql4.append("(case when dt='" + date2
							+ "' then cuzb11 else 0 end) D29,"
							+ "(case when dt='" + date2
							+ "' then DBFX else '' end) D30,"
							+ "(case when dt='" + date2
							+ "' then HKLY1 else '' end) D31,"
							+ "(case when dt='" + date2
							+ "' then HKLY2 else '' end) D32,"
							+ "(case when dt='" + date2
							+ "' then HKLY3 else '' end) D33,"
							+ "(case when dt='" + date2
							+ "' then QTSM else '' end) D34,"
							+ "(case when dt='" + date2
							+ "' then CWFX else '' end) D35,"
							+ "(case when dt='" + date2
							+ "' then FCWFX else '' end) D36,"
							+ "(case when dt='" + date2
							+ "' then cuzb1 else 0 end) CUZB101,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb1 else 0 end) CUZB102,"
							+ "(case when dt='" + qYear
							+ "' then cuzb1 else 0 end) CUZB103,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb1 else 0 end) CUZB104,"
							+ "(case when dt='" + date2
							+ "' then cuzb2 else 0 end) CUZB201,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb2 else 0 end) CUZB202,"
							+ "(case when dt='" + qYear
							+ "' then cuzb2 else 0 end) CUZB203,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb2 else 0 end) CUZB204,"
							+ "(case when dt='" + date2
							+ "' then cuzb3 else 0 end) CUZB301,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb3 else 0 end) CUZB302,"
							+ "(case when dt='" + qYear
							+ "' then cuzb3 else 0 end) CUZB303,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb3 else 0 end) CUZB304,"
							+ "(case when dt='" + date2
							+ "' then cuzb4 else 0 end) CUZB401,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb4 else 0 end) CUZB402,"
							+ "(case when dt='" + qYear
							+ "' then cuzb4 else 0 end) CUZB403,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb4 else 0 end) CUZB404,");
					sql4.append("(case when dt='" + date2
							+ "' then cuzb5 else 0 end) CUZB501,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb5 else 0 end) CUZB502,"
							+ "(case when dt='" + qYear
							+ "' then cuzb5 else 0 end) CUZB503,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb5 else 0 end) CUZB504,"
							+ "(case when dt='" + date2
							+ "' then cuzb6 else 0 end) CUZB601,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb6 else 0 end) CUZB602,"
							+ "(case when dt='" + qYear
							+ "' then cuzb6 else 0 end) CUZB603,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb6 else 0 end) CUZB604,"
							+ "(case when dt='" + date2
							+ "' then cuzb7 else 0 end) CUZB701,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb7 else 0 end) CUZB702,"
							+ "(case when dt='" + qYear
							+ "' then cuzb7 else 0 end) CUZB703,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb7 else 0 end) CUZB704,"
							+ "(case when dt='" + date2
							+ "' then cuzb8 else 0 end) CUZB801,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb8 else 0 end) CUZB802,"
							+ "(case when dt='" + qYear
							+ "' then cuzb8 else 0 end) CUZB803,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb8 else 0 end) CUZB804,"
							+ "(case when dt='" + date2
							+ "' then cuzb9 else 0 end) CUZB901,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb9 else 0 end) CUZB902,"
							+ "(case when dt='" + qYear
							+ "' then cuzb9 else 0 end) CUZB903,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb9 else 0 end) CUZB904,"
							+ "(case when dt='" + date2
							+ "' then cuzb10 else 0 end) CUZB1001,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb10 else 0 end) CUZB1002,"
							+ "(case when dt='" + qYear
							+ "' then cuzb10 else 0 end) CUZB1003,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb10 else 0 end) CUZB1004");
					sql4.append(" from FCQYCW f where ");
					sql4
							.append(" f.clientno='"
									+ clientno
									+ "')"
									+ "select max(D29) D29,max(D30) D30,max(D31) D31,max(D32) D32,max(D33) D33,max(D34) D34,max(D35) D35,max(D36) D36,"
									+ "max(CUZB101) CUZB101,max(CUZB102) CUZB102,"
									+ " max(CUZB103) CUZB103,max(CUZB104) CUZB104,max(CUZB201) CUZB201,max(CUZB202) CUZB202,"
									+ "max(CUZB203) CUZB203,max(CUZB104) CUZB204,max(CUZB301) CUZB301,max(CUZB302) CUZB302,"
									+ "max(CUZB303) CUZB303,"
									+ " max(CUZB304) CUZB304,max(CUZB401) CUZB401,max(CUZB402) CUZB402,max(CUZB403) CUZB403,max(CUZB404) CUZB404,"
									+ "max(CUZB501) CUZB501,max(CUZB502) CUZB502,max(CUZB503) CUZB503,max(CUZB504) CUZB504,"
									+ " max(CUZB601) CUZB601,max(CUZB602) CUZB602,max(CUZB603) CUZB603,max(CUZB604) CUZB604,"
									+ "max(CUZB701) CUZB701,max(CUZB702) CUZB702,max(CUZB703) CUZB703,max(CUZB704) CUZB704,"
									+ "max(CUZB801)CUZB801,"
									+ " max(CUZB802) CUZB802,max(CUZB803) CUZB803,max(CUZB804) CUZB804,"
									+ "max(CUZB901) CUZB901,max(CUZB902) CUZB902,max(CUZB903) CUZB903,max(CUZB904) CUZB904,"
									+ "max(CUZB1001) CUZB1001,max(CUZB1002) CUZB1002,"
									+ " max(CUZB1003) CUZB1003,max(CUZB1004) CUZB1004"
									+ " from T ");
					CachedRowSet crs4 = manager.getRs(sql4.toString());
					while (crs4.next()) {

						StringBuffer sqls = new StringBuffer();
						sqls
								.append("select cmt1,cmt2,cmt3,cmt4 from FCCMT fc,fcmain f,BMTABLEAPP b "
										+ "where fccmttype=1 and f.fcno=fc.fcno and f.createdate='"
										+ createdate2
										+ "' and"
										+ " b.bmno=f.bmno and b.clientno='"
										+ clientno + "'");
						CachedRowSet crss = manager.getRs(sqls.toString());
						while (crss.next()) {
							String cmt3 = DBUtil
									.fromDB(crss.getString("cmt1") == null ? ""
											: crss.getString("cmt1"));
							String cmt4 = DBUtil
									.fromDB(crss.getString("cmt2") == null ? ""
											: crss.getString("cmt2"));
							String cmt5 = DBUtil
									.fromDB(crss.getString("cmt3") == null ? ""
											: crss.getString("cmt3"));
							String cmt6 = DBUtil
									.fromDB(crss.getString("cmt4") == null ? ""
											: crss.getString("cmt4"));
							info.setFccmt1(cmt3);
							info.setFccmt2(cmt4);
							info.setFccmt3(cmt5);
							info.setFccmt4(cmt6);
						}

						String cuzb11 = DBUtil
								.doubleToStr1(Double.valueOf(
										crs4.getString("D29") == null ? "0.00"
												: crs4.getString("D29"))
										.doubleValue() / 10000);
						String dbfx = DBUtil
								.fromDB(crs4.getString("D30") == null ? ""
										: crs4.getString("D30"));
						String hkly1 = DBUtil
								.fromDB(crs4.getString("D31") == null ? ""
										: crs4.getString("D31"));
						String hkly2 = DBUtil
								.fromDB(crs4.getString("D32") == null ? ""
										: crs4.getString("D32"));
						String hkly3 = DBUtil
								.fromDB(crs4.getString("D33") == null ? ""
										: crs4.getString("D33"));
						String qtsm = DBUtil
								.fromDB(crs4.getString("D34") == null ? ""
										: crs4.getString("D34"));
						String cwfx = DBUtil
								.fromDB(crs4.getString("D35") == null ? ""
										: crs4.getString("D35"));
						String fcwfx = DBUtil
								.fromDB(crs4.getString("D36") == null ? ""
										: crs4.getString("D36"));
						String cuzb011 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB101") == null ? "0.00"
										: crs4.getString("CUZB101"))
								.doubleValue());
						String cuzb012 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB102") == null ? "0.00"
										: crs4.getString("CUZB102"))
								.doubleValue());
						String cuzb013 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB103") == null ? "0.00"
										: crs4.getString("CUZB103"))
								.doubleValue());
						String cuzb014 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB104") == null ? "0.00"
										: crs4.getString("CUZB104"))
								.doubleValue());
						String cuzb021 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB201") == null ? "0.00"
										: crs4.getString("CUZB201"))
								.doubleValue());
						String cuzb022 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB202") == null ? "0.00"
										: crs4.getString("CUZB202"))
								.doubleValue());
						String cuzb023 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB203") == null ? "0.00"
										: crs4.getString("CUZB203"))
								.doubleValue());
						String cuzb024 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB204") == null ? "0.00"
										: crs4.getString("CUZB204"))
								.doubleValue());
						String cuzb031 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB301") == null ? "0.00"
										: crs4.getString("CUZB301"))
								.doubleValue());
						String cuzb032 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB302") == null ? "0.00"
										: crs4.getString("CUZB302"))
								.doubleValue());
						String cuzb033 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB303") == null ? "0.00"
										: crs4.getString("CUZB303"))
								.doubleValue());
						String cuzb034 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB304") == null ? "0.00"
										: crs4.getString("CUZB304"))
								.doubleValue());
						String cuzb041 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB401") == null ? "0.00"
										: crs4.getString("CUZB401"))
								.doubleValue());
						String cuzb042 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB402") == null ? "0.00"
										: crs4.getString("CUZB402"))
								.doubleValue());
						String cuzb043 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB403") == null ? "0.00"
										: crs4.getString("CUZB403"))
								.doubleValue());
						String cuzb044 = DBUtil.doubleToStr1(Double.valueOf(
								crs4.getString("CUZB404") == null ? "0.00"
										: crs4.getString("CUZB404"))
								.doubleValue());
						String cuzb051 = crs4.getString("CUZB501") == null ? "0.00"
								: crs4.getString("CUZB501");
						String cuzb052 = crs4.getString("CUZB502") == null ? ""
								: crs4.getString("CUZB502");
						String cuzb053 = crs4.getString("CUZB503") == null ? ""
								: crs4.getString("CUZB503");
						String cuzb054 = crs4.getString("CUZB504") == null ? ""
								: crs4.getString("CUZB504");
						String cuzb061 = crs4.getString("CUZB601") == null ? ""
								: crs4.getString("CUZB601");
						String cuzb062 = crs4.getString("CUZB602") == null ? ""
								: crs4.getString("CUZB602");
						String cuzb063 = crs4.getString("CUZB603") == null ? ""
								: crs4.getString("CUZB603");
						String cuzb064 = crs4.getString("CUZB604") == null ? ""
								: crs4.getString("CUZB604");
						String cuzb071 = crs4.getString("CUZB701") == null ? ""
								: crs4.getString("CUZB701");
						String cuzb072 = crs4.getString("CUZB702") == null ? ""
								: crs4.getString("CUZB702");
						String cuzb073 = crs4.getString("CUZB703") == null ? ""
								: crs4.getString("CUZB703");
						String cuzb074 = crs4.getString("CUZB704") == null ? ""
								: crs4.getString("CUZB704");
						String cuzb081 = crs4.getString("CUZB801") == null ? ""
								: crs4.getString("CUZB801");
						String cuzb082 = crs4.getString("CUZB802") == null ? ""
								: crs4.getString("CUZB802");
						String cuzb083 = crs4.getString("CUZB803") == null ? ""
								: crs4.getString("CUZB803");
						String cuzb084 = crs4.getString("CUZB804") == null ? ""
								: crs4.getString("CUZB804");
						String cuzb091 = crs4.getString("CUZB901") == null ? ""
								: crs4.getString("CUZB901");
						String cuzb092 = crs4.getString("CUZB902") == null ? ""
								: crs4.getString("CUZB902");
						String cuzb093 = crs4.getString("CUZB903") == null ? ""
								: crs4.getString("CUZB903");
						String cuzb094 = crs4.getString("CUZB904") == null ? ""
								: crs4.getString("CUZB904");
						String cuzb101 = crs4.getString("CUZB1001") == null ? ""
								: crs4.getString("CUZB1001");
						String cuzb102 = crs4.getString("CUZB1002") == null ? ""
								: crs4.getString("CUZB1002");
						String cuzb103 = crs4.getString("CUZB1003") == null ? ""
								: crs4.getString("CUZB1003");
						String cuzb104 = crs4.getString("CUZB1004") == null ? ""
								: crs4.getString("CUZB1004");
						info.setCuzb01(cuzb011);
						info.setCuzb011(cuzb012);
						info.setCuzb012(cuzb013);
						info.setCuzb013(cuzb014);
						info.setCuzb02(cuzb021);
						info.setCuzb021(cuzb022);
						info.setCuzb022(cuzb023);
						info.setCuzb023(cuzb024);
						info.setCuzb03(cuzb031);
						info.setCuzb031(cuzb032);
						info.setCuzb032(cuzb033);
						info.setCuzb033(cuzb034);
						info.setCuzb04(cuzb041);
						info.setCuzb041(cuzb042);
						info.setCuzb042(cuzb043);
						info.setCuzb043(cuzb044);
						info.setCuzb05(cuzb051);
						info.setCuzb051(cuzb052);
						info.setCuzb052(cuzb053);
						info.setCuzb053(cuzb054);
						info.setCuzb06(cuzb061);
						info.setCuzb061(cuzb062);
						info.setCuzb062(cuzb063);
						info.setCuzb063(cuzb064);
						info.setCuzb07(cuzb071);
						info.setCuzb071(cuzb072);
						info.setCuzb072(cuzb073);
						info.setCuzb073(cuzb074);
						info.setCuzb08(cuzb081);
						info.setCuzb081(cuzb082);
						info.setCuzb082(cuzb083);
						info.setCuzb083(cuzb084);
						info.setCuzb09(cuzb091);
						info.setCuzb091(cuzb092);
						info.setCuzb092(cuzb093);
						info.setCuzb093(cuzb094);
						info.setCuzb10(cuzb101);
						info.setCuzb101(cuzb102);
						info.setCuzb102(cuzb103);
						info.setCuzb103(cuzb104);
						info.setCuzb11(cuzb11);
						info.setDbfx(dbfx);
						info.setHkly1(hkly1);
						info.setHkly2(hkly2);
						info.setHkly3(hkly3);
						info.setQtsm(qtsm);
						info.setCwfx(cwfx);
						info.setFcwfx(fcwfx);
						info.setCreate(date2);
					}
				}
			} else if (createdate != null && !createdate.equals("")
					&& (date1 != null || !date1.equals(""))
					&& (date2 != null || !date2.equals(""))) {

				sql3.append(" with T as (select ");
				sql3.append("(case when dt='" + createdate
						+ "' then cuzb11 else 0 end) D29," + "(case when dt='"
						+ createdate + "' then DBFX else '' end) D30,"
						+ "(case when dt='" + createdate
						+ "' then HKLY1 else '' end) D31," + "(case when dt='"
						+ createdate + "' then HKLY2 else '' end) D32,"
						+ "(case when dt='" + createdate
						+ "' then HKLY3 else '' end) D33," + "(case when dt='"
						+ createdate + "' then QTSM else '' end) D34,"
						+ "(case when dt='" + createdate
						+ "' then CWFX else '' end) D35," + "(case when dt='"
						+ createdate + "' then FCWFX else '' end) D36,"
						+ "(case when dt='" + createdate
						+ "' then cuzb1 else 0 end) CUZB101,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb1 else 0 end) CUZB102,"
						+ "(case when dt='" + qYear
						+ "' then cuzb1 else 0 end) CUZB103,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb1 else 0 end) CUZB104,"
						+ "(case when dt='" + createdate
						+ "' then cuzb2 else 0 end) CUZB201,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb2 else 0 end) CUZB202,"
						+ "(case when dt='" + qYear
						+ "' then cuzb2 else 0 end) CUZB203,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb2 else 0 end) CUZB204,"
						+ "(case when dt='" + createdate
						+ "' then cuzb3 else 0 end) CUZB301,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb3 else 0 end) CUZB302,"
						+ "(case when dt='" + qYear
						+ "' then cuzb3 else 0 end) CUZB303,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb3 else 0 end) CUZB304,"
						+ "(case when dt='" + createdate
						+ "' then cuzb4 else 0 end) CUZB401,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb4 else 0 end) CUZB402,"
						+ "(case when dt='" + qYear
						+ "' then cuzb4 else 0 end) CUZB403,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb4 else 0 end) CUZB404,");
				sql3.append("(case when dt='" + createdate
						+ "' then cuzb5 else 0 end) CUZB501,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb5 else 0 end) CUZB502,"
						+ "(case when dt='" + qYear
						+ "' then cuzb5 else 0 end) CUZB503,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb5 else 0 end) CUZB504,"
						+ "(case when dt='" + createdate
						+ "' then cuzb6 else 0 end) CUZB601,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb6 else 0 end) CUZB602,"
						+ "(case when dt='" + qYear
						+ "' then cuzb6 else 0 end) CUZB603,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb6 else 0 end) CUZB604,"
						+ "(case when dt='" + createdate
						+ "' then cuzb7 else 0 end) CUZB701,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb7 else 0 end) CUZB702,"
						+ "(case when dt='" + qYear
						+ "' then cuzb7 else 0 end) CUZB703,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb7 else 0 end) CUZB704,"
						+ "(case when dt='" + createdate
						+ "' then cuzb8 else 0 end) CUZB801,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb8 else 0 end) CUZB802,"
						+ "(case when dt='" + qYear
						+ "' then cuzb8 else 0 end) CUZB803,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb8 else 0 end) CUZB804,"
						+ "(case when dt='" + createdate
						+ "' then cuzb9 else 0 end) CUZB901,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb9 else 0 end) CUZB902,"
						+ "(case when dt='" + qYear
						+ "' then cuzb9 else 0 end) CUZB903,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb9 else 0 end) CUZB904,"
						+ "(case when dt='" + createdate
						+ "' then cuzb10 else 0 end) CUZB1001,"
						+ "(case when dt='" + lastYear
						+ "' then cuzb10 else 0 end) CUZB1002,"
						+ "(case when dt='" + qYear
						+ "' then cuzb10 else 0 end) CUZB1003,"
						+ "(case when dt='" + dqyear
						+ "' then cuzb10 else 0 end) CUZB1004");
				sql3.append(" from FCQYCW f where ");
				sql3
						.append(" f.clientno='"
								+ clientno
								+ "')"
								+ "select max(D29) D29,max(D30) D30,max(D31) D31,max(D32) D32,max(D33) D33,max(D34) D34,max(D35) D35,max(D36) D36,"
								+ "max(CUZB101) CUZB101,max(CUZB102) CUZB102,"
								+ " max(CUZB103) CUZB103,max(CUZB104) CUZB104,max(CUZB201) CUZB201,max(CUZB202) CUZB202,"
								+ "max(CUZB203) CUZB203,max(CUZB104) CUZB204,max(CUZB301) CUZB301,max(CUZB302) CUZB302,"
								+ "max(CUZB303) CUZB303,"
								+ " max(CUZB304) CUZB304,max(CUZB401) CUZB401,max(CUZB402) CUZB402,max(CUZB403) CUZB403,max(CUZB404) CUZB404,"
								+ "max(CUZB501) CUZB501,max(CUZB502) CUZB502,max(CUZB503) CUZB503,max(CUZB504) CUZB504,"
								+ " max(CUZB601) CUZB601,max(CUZB602) CUZB602,max(CUZB603) CUZB603,max(CUZB604) CUZB604,"
								+ "max(CUZB701) CUZB701,max(CUZB702) CUZB702,max(CUZB703) CUZB703,max(CUZB704) CUZB704,"
								+ "max(CUZB801)CUZB801,"
								+ " max(CUZB802) CUZB802,max(CUZB803) CUZB803,max(CUZB804) CUZB804,"
								+ "max(CUZB901) CUZB901,max(CUZB902) CUZB902,max(CUZB903) CUZB903,max(CUZB904) CUZB904,"
								+ "max(CUZB1001) CUZB1001,max(CUZB1002) CUZB1002,"
								+ " max(CUZB1003) CUZB1003,max(CUZB1004) CUZB1004"
								+ " from T ");
				CachedRowSet crs3 = manager.getRs(sql3.toString());
                StringBuffer sql8=new StringBuffer();
                sql8.append("select dt from FCQYCW where clientno='"+clientno+"' and dt='"+createdate+"'");
                CachedRowSet crs8 = manager.getRs(sql8.toString());
                if(crs8.size()>0){
				if (crs3.size() > 0 ) {
                   
					while (crs3.next()) {
						StringBuffer sqls = new StringBuffer();
						sqls
								.append("select cmt1,cmt2,cmt3,cmt4 from FCCMT fc,fcmain f,BMTABLEAPP b "
										+ "where fccmttype=1 and f.fcno=fc.fcno and f.createdate='"
										+ createdate2
										+ "' and"
										+ " b.bmno=f.bmno and b.clientno='"
										+ clientno + "'");
						CachedRowSet crss = manager.getRs(sqls.toString());
						while (crss.next()) {
							String cmt3 = DBUtil
									.fromDB(crss.getString("cmt1") == null ? ""
											: crss.getString("cmt1"));
							String cmt4 = DBUtil
									.fromDB(crss.getString("cmt2") == null ? ""
											: crss.getString("cmt2"));
							String cmt5 = DBUtil
									.fromDB(crss.getString("cmt3") == null ? ""
											: crss.getString("cmt3"));
							String cmt6 = DBUtil
									.fromDB(crss.getString("cmt4") == null ? ""
											: crss.getString("cmt4"));
							info.setFccmt1(cmt3);
							info.setFccmt2(cmt4);
							info.setFccmt3(cmt5);
							info.setFccmt4(cmt6);
						}

						String cuzb11 = DBUtil
								.doubleToStr1(Double.valueOf(
										crs3.getString("D29") == null ? "0.00"
												: crs3.getString("D29"))
										.doubleValue() / 10000);
						String dbfx = DBUtil
								.fromDB(crs3.getString("D30") == null ? ""
										: crs3.getString("D30"));
						String hkly1 = DBUtil
								.fromDB(crs3.getString("D31") == null ? ""
										: crs3.getString("D31"));
						String hkly2 = DBUtil
								.fromDB(crs3.getString("D32") == null ? ""
										: crs3.getString("D32"));
						String hkly3 = DBUtil
								.fromDB(crs3.getString("D33") == null ? ""
										: crs3.getString("D33"));
						String qtsm = DBUtil
								.fromDB(crs3.getString("D34") == null ? ""
										: crs3.getString("D34"));
						String cwfx = DBUtil
								.fromDB(crs3.getString("D35") == null ? ""
										: crs3.getString("D35"));
						String fcwfx = DBUtil
								.fromDB(crs3.getString("D36") == null ? ""
										: crs3.getString("D36"));
						String cuzb011 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB101") == null ? "0.00"
										: crs3.getString("CUZB101"))
								.doubleValue());
						String cuzb012 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB102") == null ? "0.00"
										: crs3.getString("CUZB102"))
								.doubleValue());
						String cuzb013 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB103") == null ? "0.00"
										: crs3.getString("CUZB103"))
								.doubleValue());
						String cuzb014 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB104") == null ? "0.00"
										: crs3.getString("CUZB104"))
								.doubleValue());
						String cuzb021 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB201") == null ? "0.00"
										: crs3.getString("CUZB201"))
								.doubleValue());
						String cuzb022 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB202") == null ? "0.00"
										: crs3.getString("CUZB202"))
								.doubleValue());
						String cuzb023 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB203") == null ? "0.00"
										: crs3.getString("CUZB203"))
								.doubleValue());
						String cuzb024 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB204") == null ? "0.00"
										: crs3.getString("CUZB204"))
								.doubleValue());
						String cuzb031 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB301") == null ? "0.00"
										: crs3.getString("CUZB301"))
								.doubleValue());
						String cuzb032 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB302") == null ? "0.00"
										: crs3.getString("CUZB302"))
								.doubleValue());
						String cuzb033 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB303") == null ? "0.00"
										: crs3.getString("CUZB303"))
								.doubleValue());
						String cuzb034 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB304") == null ? "0.00"
										: crs3.getString("CUZB304"))
								.doubleValue());
						String cuzb041 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB401") == null ? "0.00"
										: crs3.getString("CUZB401"))
								.doubleValue());

						String cuzb042 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB402") == null ? "0.00"
										: crs3.getString("CUZB402"))
								.doubleValue());
						String cuzb043 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB403") == null ? "0.00"
										: crs3.getString("CUZB403"))
								.doubleValue());
						String cuzb044 = DBUtil.doubleToStr1(Double.valueOf(
								crs3.getString("CUZB404") == null ? "0.00"
										: crs3.getString("CUZB404"))
								.doubleValue());
						String cuzb051 = crs3.getString("CUZB501") == null ? ""
								: crs3.getString("CUZB501");
						String cuzb052 = crs3.getString("CUZB502") == null ? ""
								: crs3.getString("CUZB502");
						String cuzb053 = crs3.getString("CUZB503") == null ? ""
								: crs3.getString("CUZB503");
						String cuzb054 = crs3.getString("CUZB504") == null ? ""
								: crs3.getString("CUZB504");
						String cuzb061 = crs3.getString("CUZB601") == null ? ""
								: crs3.getString("CUZB601");
						String cuzb062 = crs3.getString("CUZB602") == null ? ""
								: crs3.getString("CUZB602");
						String cuzb063 = crs3.getString("CUZB603") == null ? ""
								: crs3.getString("CUZB603");
						String cuzb064 = crs3.getString("CUZB604") == null ? ""
								: crs3.getString("CUZB604");
						String cuzb071 = crs3.getString("CUZB701") == null ? ""
								: crs3.getString("CUZB701");
						String cuzb072 = crs3.getString("CUZB702") == null ? ""
								: crs3.getString("CUZB702");
						String cuzb073 = crs3.getString("CUZB703") == null ? ""
								: crs3.getString("CUZB703");
						String cuzb074 = crs3.getString("CUZB704") == null ? ""
								: crs3.getString("CUZB704");
						String cuzb081 = crs3.getString("CUZB801") == null ? ""
								: crs3.getString("CUZB801");
						String cuzb082 = crs3.getString("CUZB802") == null ? ""
								: crs3.getString("CUZB802");
						String cuzb083 = crs3.getString("CUZB803") == null ? ""
								: crs3.getString("CUZB803");
						String cuzb084 = crs3.getString("CUZB804") == null ? ""
								: crs3.getString("CUZB804");
						String cuzb091 = crs3.getString("CUZB901") == null ? ""
								: crs3.getString("CUZB901");
						String cuzb092 = crs3.getString("CUZB902") == null ? ""
								: crs3.getString("CUZB902");
						String cuzb093 = crs3.getString("CUZB903") == null ? ""
								: crs3.getString("CUZB903");
						String cuzb094 = crs3.getString("CUZB904") == null ? ""
								: crs3.getString("CUZB904");
						String cuzb101 = crs3.getString("CUZB1001") == null ? ""
								: crs3.getString("CUZB1001");
						String cuzb102 = crs3.getString("CUZB1002") == null ? ""
								: crs3.getString("CUZB1002");
						String cuzb103 = crs3.getString("CUZB1003") == null ? ""
								: crs3.getString("CUZB1003");
						String cuzb104 = crs3.getString("CUZB1004") == null ? ""
								: crs3.getString("CUZB1004");
						info.setCuzb01(cuzb011);
						info.setCuzb011(cuzb012);
						info.setCuzb012(cuzb013);
						info.setCuzb013(cuzb014);
						info.setCuzb02(cuzb021);
						info.setCuzb021(cuzb022);
						info.setCuzb022(cuzb023);
						info.setCuzb023(cuzb024);
						info.setCuzb03(cuzb031);
						info.setCuzb031(cuzb032);
						info.setCuzb032(cuzb033);
						info.setCuzb033(cuzb034);
						info.setCuzb04(cuzb041);
						info.setCuzb041(cuzb042);
						info.setCuzb042(cuzb043);
						info.setCuzb043(cuzb044);

						info.setCuzb05(cuzb051);
						info.setCuzb051(cuzb052);
						info.setCuzb052(cuzb053);
						info.setCuzb053(cuzb054);
						info.setCuzb06(cuzb061);
						info.setCuzb061(cuzb062);
						info.setCuzb062(cuzb063);
						info.setCuzb063(cuzb064);
						info.setCuzb07(cuzb071);
						info.setCuzb071(cuzb072);
						info.setCuzb072(cuzb073);
						info.setCuzb073(cuzb074);
						info.setCuzb08(cuzb081);
						info.setCuzb081(cuzb082);
						info.setCuzb082(cuzb083);
						info.setCuzb083(cuzb084);
						info.setCuzb09(cuzb091);
						info.setCuzb091(cuzb092);
						info.setCuzb092(cuzb093);
						info.setCuzb093(cuzb094);
						info.setCuzb10(cuzb101);
						info.setCuzb101(cuzb102);
						info.setCuzb102(cuzb103);
						info.setCuzb103(cuzb104);
						info.setCuzb11(cuzb11);
						info.setDbfx(dbfx);
						info.setHkly1(hkly1);
						info.setHkly2(hkly2);
						info.setHkly3(hkly3);
						info.setQtsm(qtsm);
						info.setCwfx(cwfx);
						info.setFcwfx(fcwfx);

						info.setCreate(createdate);

					}
				} 
                }else {
                  
					StringBuffer sql4 = new StringBuffer();
					sql4.append(" with T as (select ");
					sql4.append("(case when dt='" + date1
							+ "' then cuzb11 else 0 end) D29,"
							+ "(case when dt='" + date1
							+ "' then DBFX else '' end) D30,"
							+ "(case when dt='" + date1
							+ "' then HKLY1 else '' end) D31,"
							+ "(case when dt='" + date1
							+ "' then HKLY2 else '' end) D32,"
							+ "(case when dt='" + date1
							+ "' then HKLY3 else '' end) D33,"
							+ "(case when dt='" + date1
							+ "' then QTSM else '' end) D34,"
							+ "(case when dt='" + date1
							+ "' then CWFX else '' end) D35,"
							+ "(case when dt='" + date1
							+ "' then FCWFX else '' end) D36,"
							+ "(case when dt='" + date1
							+ "' then cuzb1 else 0 end) CUZB101,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb1 else 0 end) CUZB102,"
							+ "(case when dt='" + qYear
							+ "' then cuzb1 else 0 end) CUZB103,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb1 else 0 end) CUZB104,"
							+ "(case when dt='" + date1
							+ "' then cuzb2 else 0 end) CUZB201,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb2 else 0 end) CUZB202,"
							+ "(case when dt='" + qYear
							+ "' then cuzb2 else 0 end) CUZB203,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb2 else 0 end) CUZB204,"
							+ "(case when dt='" + date1
							+ "' then cuzb3 else 0 end) CUZB301,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb3 else 0 end) CUZB302,"
							+ "(case when dt='" + qYear
							+ "' then cuzb3 else 0 end) CUZB303,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb3 else 0 end) CUZB304,"
							+ "(case when dt='" + date1
							+ "' then cuzb4 else 0 end) CUZB401,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb4 else 0 end) CUZB402,"
							+ "(case when dt='" + qYear
							+ "' then cuzb4 else 0 end) CUZB403,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb4 else 0 end) CUZB404,");
					sql4.append("(case when dt='" + date1
							+ "' then cuzb5 else 0 end) CUZB501,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb5 else 0 end) CUZB502,"
							+ "(case when dt='" + qYear
							+ "' then cuzb5 else 0 end) CUZB503,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb5 else 0 end) CUZB504,"
							+ "(case when dt='" + date1
							+ "' then cuzb6 else 0 end) CUZB601,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb6 else 0 end) CUZB602,"
							+ "(case when dt='" + qYear
							+ "' then cuzb6 else 0 end) CUZB603,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb6 else 0 end) CUZB604,"
							+ "(case when dt='" + date1
							+ "' then cuzb7 else 0 end) CUZB701,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb7 else 0 end) CUZB702,"
							+ "(case when dt='" + qYear
							+ "' then cuzb7 else 0 end) CUZB703,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb7 else 0 end) CUZB704,"
							+ "(case when dt='" + date1
							+ "' then cuzb8 else 0 end) CUZB801,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb8 else 0 end) CUZB802,"
							+ "(case when dt='" + qYear
							+ "' then cuzb8 else 0 end) CUZB803,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb8 else 0 end) CUZB804,"
							+ "(case when dt='" + date1
							+ "' then cuzb9 else 0 end) CUZB901,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb9 else 0 end) CUZB902,"
							+ "(case when dt='" + qYear
							+ "' then cuzb9 else 0 end) CUZB903,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb9 else 0 end) CUZB904,"
							+ "(case when dt='" + date1
							+ "' then cuzb10 else 0 end) CUZB1001,"
							+ "(case when dt='" + lastYear
							+ "' then cuzb10 else 0 end) CUZB1002,"
							+ "(case when dt='" + qYear
							+ "' then cuzb10 else 0 end) CUZB1003,"
							+ "(case when dt='" + dqyear
							+ "' then cuzb10 else 0 end) CUZB1004");
					sql4.append(" from FCQYCW f where ");
					sql4
							.append(" f.clientno='"
									+ clientno
									+ "')"
									+ "select max(D29) D29,max(D30) D30,max(D31) D31,max(D32) D32,max(D33) D33,max(D34) D34,max(D35) D35,max(D36) D36,"
									+ "max(CUZB101) CUZB101,max(CUZB102) CUZB102,"
									+ " max(CUZB103) CUZB103,max(CUZB104) CUZB104,max(CUZB201) CUZB201,max(CUZB202) CUZB202,"
									+ "max(CUZB203) CUZB203,max(CUZB104) CUZB204,max(CUZB301) CUZB301,max(CUZB302) CUZB302,"
									+ "max(CUZB303) CUZB303,"
									+ " max(CUZB304) CUZB304,max(CUZB401) CUZB401,max(CUZB402) CUZB402,max(CUZB403) CUZB403,max(CUZB404) CUZB404,"
									+ "max(CUZB501) CUZB501,max(CUZB502) CUZB502,max(CUZB503) CUZB503,max(CUZB504) CUZB504,"
									+ " max(CUZB601) CUZB601,max(CUZB602) CUZB602,max(CUZB603) CUZB603,max(CUZB604) CUZB604,"
									+ "max(CUZB701) CUZB701,max(CUZB702) CUZB702,max(CUZB703) CUZB703,max(CUZB704) CUZB704,"
									+ "max(CUZB801)CUZB801,"
									+ " max(CUZB802) CUZB802,max(CUZB803) CUZB803,max(CUZB804) CUZB804,"
									+ "max(CUZB901) CUZB901,max(CUZB902) CUZB902,max(CUZB903) CUZB903,max(CUZB904) CUZB904,"
									+ "max(CUZB1001) CUZB1001,max(CUZB1002) CUZB1002,"
									+ " max(CUZB1003) CUZB1003,max(CUZB1004) CUZB1004"
									+ " from T ");
					CachedRowSet crs4 = manager.getRs(sql4.toString());
					StringBuffer sql9=new StringBuffer();
	                sql9.append("select dt from FCQYCW where clientno='"+clientno+"' and dt='"+date1+"'");
	                CachedRowSet crs9 = manager.getRs(sql9.toString());
	                if(crs9.size()>0){
					if (crs4.size() > 0) {
                               
						while (crs4.next()) {
							StringBuffer sqls = new StringBuffer();
							sqls
									.append("select cmt1,cmt2,cmt3,cmt4 from FCCMT fc,fcmain f,BMTABLEAPP b "
											+ "where fccmttype=1 and f.fcno=fc.fcno and f.createdate='"
											+ createdate2
											+ "' and"
											+ " b.bmno=f.bmno and b.clientno='"
											+ clientno + "'");
							CachedRowSet crss = manager.getRs(sqls.toString());
							while (crss.next()) {
								String cmt3 = DBUtil.fromDB(crss
										.getString("cmt1") == null ? "" : crss
										.getString("cmt1"));
								String cmt4 = DBUtil.fromDB(crss
										.getString("cmt2") == null ? "" : crss
										.getString("cmt2"));
								String cmt5 = DBUtil.fromDB(crss
										.getString("cmt3") == null ? "" : crss
										.getString("cmt3"));
								String cmt6 = DBUtil.fromDB(crss
										.getString("cmt4") == null ? "" : crss
										.getString("cmt4"));
								info.setFccmt1(cmt3);
								info.setFccmt2(cmt4);
								info.setFccmt3(cmt5);
								info.setFccmt4(cmt6);
							}

							String cuzb11 = DBUtil.doubleToStr1(Double.valueOf(
									crs4.getString("D29") == null ? "0.00"
											: crs4.getString("D29"))
									.doubleValue() / 10000);
							String dbfx = DBUtil
									.fromDB(crs4.getString("D30") == null ? ""
											: crs4.getString("D30"));
							String hkly1 = DBUtil
									.fromDB(crs4.getString("D31") == null ? ""
											: crs4.getString("D31"));
							String hkly2 = DBUtil
									.fromDB(crs4.getString("D32") == null ? ""
											: crs4.getString("D32"));
							String hkly3 = DBUtil
									.fromDB(crs4.getString("D33") == null ? ""
											: crs4.getString("D33"));
							String qtsm = DBUtil
									.fromDB(crs4.getString("D34") == null ? ""
											: crs4.getString("D34"));
							String cwfx = DBUtil
									.fromDB(crs4.getString("D35") == null ? ""
											: crs4.getString("D35"));
							String fcwfx = DBUtil
									.fromDB(crs4.getString("D36") == null ? ""
											: crs4.getString("D36"));
							String cuzb011 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB101") == null ? "0.00"
															: crs4
																	.getString("CUZB101"))
											.doubleValue());
							String cuzb012 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB102") == null ? "0.00"
															: crs4
																	.getString("CUZB102"))
											.doubleValue());
							String cuzb013 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB103") == null ? "0.00"
															: crs4
																	.getString("CUZB103"))
											.doubleValue());
							String cuzb014 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB104") == null ? "0.00"
															: crs4
																	.getString("CUZB104"))
											.doubleValue());
							String cuzb021 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB201") == null ? "0.00"
															: crs4
																	.getString("CUZB201"))
											.doubleValue());
							String cuzb022 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB202") == null ? "0.00"
															: crs4
																	.getString("CUZB202"))
											.doubleValue());
							String cuzb023 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB203") == null ? "0.00"
															: crs4
																	.getString("CUZB203"))
											.doubleValue());
							String cuzb024 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB204") == null ? "0.00"
															: crs4
																	.getString("CUZB204"))
											.doubleValue());
							String cuzb031 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB301") == null ? "0.00"
															: crs4
																	.getString("CUZB301"))
											.doubleValue());
							String cuzb032 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB302") == null ? "0.00"
															: crs4
																	.getString("CUZB302"))
											.doubleValue());
							String cuzb033 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB303") == null ? "0.00"
															: crs4
																	.getString("CUZB303"))
											.doubleValue());
							String cuzb034 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB304") == null ? "0.00"
															: crs4
																	.getString("CUZB304"))
											.doubleValue());
							String cuzb041 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB401") == null ? "0.00"
															: crs4
																	.getString("CUZB401"))
											.doubleValue());
							String cuzb042 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB402") == null ? "0.00"
															: crs4
																	.getString("CUZB402"))
											.doubleValue());
							String cuzb043 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB403") == null ? "0.00"
															: crs4
																	.getString("CUZB403"))
											.doubleValue());
							String cuzb044 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs4.getString("CUZB404") == null ? "0.00"
															: crs4
																	.getString("CUZB404"))
											.doubleValue());
							String cuzb051 = crs4.getString("CUZB501") == null ? ""
									: crs4.getString("CUZB501");
							String cuzb052 = crs4.getString("CUZB502") == null ? ""
									: crs4.getString("CUZB502");
							String cuzb053 = crs4.getString("CUZB503") == null ? ""
									: crs4.getString("CUZB503");
							String cuzb054 = crs4.getString("CUZB504") == null ? ""
									: crs4.getString("CUZB504");
							String cuzb061 = crs4.getString("CUZB601") == null ? ""
									: crs4.getString("CUZB601");
							String cuzb062 = crs4.getString("CUZB602") == null ? ""
									: crs4.getString("CUZB602");
							String cuzb063 = crs4.getString("CUZB603") == null ? ""
									: crs4.getString("CUZB603");
							String cuzb064 = crs4.getString("CUZB604") == null ? ""
									: crs4.getString("CUZB604");
							String cuzb071 = crs4.getString("CUZB701") == null ? ""
									: crs4.getString("CUZB701");
							String cuzb072 = crs4.getString("CUZB702") == null ? ""
									: crs4.getString("CUZB702");
							String cuzb073 = crs4.getString("CUZB703") == null ? ""
									: crs4.getString("CUZB703");
							String cuzb074 = crs4.getString("CUZB704") == null ? ""
									: crs4.getString("CUZB704");
							String cuzb081 = crs4.getString("CUZB801") == null ? ""
									: crs4.getString("CUZB801");
							String cuzb082 = crs4.getString("CUZB802") == null ? ""
									: crs4.getString("CUZB802");
							String cuzb083 = crs4.getString("CUZB803") == null ? ""
									: crs4.getString("CUZB803");
							String cuzb084 = crs4.getString("CUZB804") == null ? ""
									: crs4.getString("CUZB804");
							String cuzb091 = crs4.getString("CUZB901") == null ? ""
									: crs4.getString("CUZB901");
							String cuzb092 = crs4.getString("CUZB902") == null ? ""
									: crs4.getString("CUZB902");
							String cuzb093 = crs4.getString("CUZB903") == null ? ""
									: crs4.getString("CUZB903");
							String cuzb094 = crs4.getString("CUZB904") == null ? ""
									: crs4.getString("CUZB904");
							String cuzb101 = crs4.getString("CUZB1001") == null ? ""
									: crs4.getString("CUZB1001");
							String cuzb102 = crs4.getString("CUZB1002") == null ? ""
									: crs4.getString("CUZB1002");
							String cuzb103 = crs4.getString("CUZB1003") == null ? ""
									: crs4.getString("CUZB1003");
							String cuzb104 = crs4.getString("CUZB1004") == null ? ""
									: crs4.getString("CUZB1004");
							info.setCuzb01(cuzb011);
							info.setCuzb011(cuzb012);
							info.setCuzb012(cuzb013);
							info.setCuzb013(cuzb014);
							info.setCuzb02(cuzb021);
							info.setCuzb021(cuzb022);
							info.setCuzb022(cuzb023);
							info.setCuzb023(cuzb024);
							info.setCuzb03(cuzb031);
							info.setCuzb031(cuzb032);
							info.setCuzb032(cuzb033);
							info.setCuzb033(cuzb034);
							info.setCuzb04(cuzb041);
							info.setCuzb041(cuzb042);
							info.setCuzb042(cuzb043);
							info.setCuzb043(cuzb044);

							info.setCuzb05(cuzb051);
							info.setCuzb051(cuzb052);
							info.setCuzb052(cuzb053);
							info.setCuzb053(cuzb054);
							info.setCuzb06(cuzb061);
							info.setCuzb061(cuzb062);
							info.setCuzb062(cuzb063);
							info.setCuzb063(cuzb064);
							info.setCuzb07(cuzb071);
							info.setCuzb071(cuzb072);
							info.setCuzb072(cuzb073);
							info.setCuzb073(cuzb074);
							info.setCuzb08(cuzb081);
							info.setCuzb081(cuzb082);
							info.setCuzb082(cuzb083);
							info.setCuzb083(cuzb084);
							info.setCuzb09(cuzb091);
							info.setCuzb091(cuzb092);
							info.setCuzb092(cuzb093);
							info.setCuzb093(cuzb094);
							info.setCuzb10(cuzb101);
							info.setCuzb101(cuzb102);
							info.setCuzb102(cuzb103);
							info.setCuzb103(cuzb104);
							info.setCuzb11(cuzb11);
							info.setDbfx(dbfx);
							info.setHkly1(hkly1);
							info.setHkly2(hkly2);
							info.setHkly3(hkly3);
							info.setQtsm(qtsm);
							info.setCwfx(cwfx);
							info.setFcwfx(fcwfx);
							info.setCreate(date1);
						}
					}
	                }else {
						StringBuffer sql5 = new StringBuffer();
						sql5.append(" with T as (select ");
						sql5.append("(case when dt='" + date2
								+ "' then cuzb11 else 0 end) D29,"
								+ "(case when dt='" + date2
								+ "' then DBFX else '' end) D30,"
								+ "(case when dt='" + date2
								+ "' then HKLY1 else '' end) D31,"
								+ "(case when dt='" + date2
								+ "' then HKLY2 else '' end) D32,"
								+ "(case when dt='" + date2
								+ "' then HKLY3 else '' end) D33,"
								+ "(case when dt='" + date2
								+ "' then QTSM else '' end) D34,"
								+ "(case when dt='" + date2
								+ "' then CWFX else '' end) D35,"
								+ "(case when dt='" + date2
								+ "' then FCWFX else '' end) D36,"
								+ "(case when dt='" + date2
								+ "' then cuzb1 else 0 end) CUZB101,"
								+ "(case when dt='" + lastYear
								+ "' then cuzb1 else 0 end) CUZB102,"
								+ "(case when dt='" + qYear
								+ "' then cuzb1 else 0 end) CUZB103,"
								+ "(case when dt='" + dqyear
								+ "' then cuzb1 else 0 end) CUZB104,"
								+ "(case when dt='" + date2
								+ "' then cuzb2 else 0 end) CUZB201,"
								+ "(case when dt='" + lastYear
								+ "' then cuzb2 else 0 end) CUZB202,"
								+ "(case when dt='" + qYear
								+ "' then cuzb2 else 0 end) CUZB203,"
								+ "(case when dt='" + dqyear
								+ "' then cuzb2 else 0 end) CUZB204,"
								+ "(case when dt='" + date2
								+ "' then cuzb3 else 0 end) CUZB301,"
								+ "(case when dt='" + lastYear
								+ "' then cuzb3 else 0 end) CUZB302,"
								+ "(case when dt='" + qYear
								+ "' then cuzb3 else 0 end) CUZB303,"
								+ "(case when dt='" + dqyear
								+ "' then cuzb3 else 0 end) CUZB304,"
								+ "(case when dt='" + date2
								+ "' then cuzb4 else 0 end) CUZB401,"
								+ "(case when dt='" + lastYear
								+ "' then cuzb4 else 0 end) CUZB402,"
								+ "(case when dt='" + qYear
								+ "' then cuzb4 else 0 end) CUZB403,"
								+ "(case when dt='" + dqyear
								+ "' then cuzb4 else 0 end) CUZB404,");
						sql5.append("(case when dt='" + date2
								+ "' then cuzb5 else 0 end) CUZB501,"
								+ "(case when dt='" + lastYear
								+ "' then cuzb5 else 0 end) CUZB502,"
								+ "(case when dt='" + qYear
								+ "' then cuzb5 else 0 end) CUZB503,"
								+ "(case when dt='" + dqyear
								+ "' then cuzb5 else 0 end) CUZB504,"
								+ "(case when dt='" + date2
								+ "' then cuzb6 else 0 end) CUZB601,"
								+ "(case when dt='" + lastYear
								+ "' then cuzb6 else 0 end) CUZB602,"
								+ "(case when dt='" + qYear
								+ "' then cuzb6 else 0 end) CUZB603,"
								+ "(case when dt='" + dqyear
								+ "' then cuzb6 else 0 end) CUZB604,"
								+ "(case when dt='" + date2
								+ "' then cuzb7 else 0 end) CUZB701,"
								+ "(case when dt='" + lastYear
								+ "' then cuzb7 else 0 end) CUZB702,"
								+ "(case when dt='" + qYear
								+ "' then cuzb7 else 0 end) CUZB703,"
								+ "(case when dt='" + dqyear
								+ "' then cuzb7 else 0 end) CUZB704,"
								+ "(case when dt='" + date2
								+ "' then cuzb8 else 0 end) CUZB801,"
								+ "(case when dt='" + lastYear
								+ "' then cuzb8 else 0 end) CUZB802,"
								+ "(case when dt='" + qYear
								+ "' then cuzb8 else 0 end) CUZB803,"
								+ "(case when dt='" + dqyear
								+ "' then cuzb8 else 0 end) CUZB804,"
								+ "(case when dt='" + date2
								+ "' then cuzb9 else 0 end) CUZB901,"
								+ "(case when dt='" + lastYear
								+ "' then cuzb9 else 0 end) CUZB902,"
								+ "(case when dt='" + qYear
								+ "' then cuzb9 else 0 end) CUZB903,"
								+ "(case when dt='" + dqyear
								+ "' then cuzb9 else 0 end) CUZB904,"
								+ "(case when dt='" + date2
								+ "' then cuzb10 else 0 end) CUZB1001,"
								+ "(case when dt='" + lastYear
								+ "' then cuzb10 else 0 end) CUZB1002,"
								+ "(case when dt='" + qYear
								+ "' then cuzb10 else 0 end) CUZB1003,"
								+ "(case when dt='" + dqyear
								+ "' then cuzb10 else 0 end) CUZB1004");
						sql5.append(" from FCQYCW f where ");
						sql5
								.append(" f.clientno='"
										+ clientno
										+ "')"
										+ "select max(D29) D29,max(D30) D30,max(D31) D31,max(D32) D32,max(D33) D33,max(D34) D34,max(D35) D35,max(D36) D36,"
										+ "max(CUZB101) CUZB101,max(CUZB102) CUZB102,"
										+ " max(CUZB103) CUZB103,max(CUZB104) CUZB104,max(CUZB201) CUZB201,max(CUZB202) CUZB202,"
										+ "max(CUZB203) CUZB203,max(CUZB104) CUZB204,max(CUZB301) CUZB301,max(CUZB302) CUZB302,"
										+ "max(CUZB303) CUZB303,"
										+ " max(CUZB304) CUZB304,max(CUZB401) CUZB401,max(CUZB402) CUZB402,max(CUZB403) CUZB403,max(CUZB404) CUZB404,"
										+ "max(CUZB501) CUZB501,max(CUZB502) CUZB502,max(CUZB503) CUZB503,max(CUZB504) CUZB504,"
										+ " max(CUZB601) CUZB601,max(CUZB602) CUZB602,max(CUZB603) CUZB603,max(CUZB604) CUZB604,"
										+ "max(CUZB701) CUZB701,max(CUZB702) CUZB702,max(CUZB703) CUZB703,max(CUZB704) CUZB704,"
										+ "max(CUZB801)CUZB801,"
										+ " max(CUZB802) CUZB802,max(CUZB803) CUZB803,max(CUZB804) CUZB804,"
										+ "max(CUZB901) CUZB901,max(CUZB902) CUZB902,max(CUZB903) CUZB903,max(CUZB904) CUZB904,"
										+ "max(CUZB1001) CUZB1001,max(CUZB1002) CUZB1002,"
										+ " max(CUZB1003) CUZB1003,max(CUZB1004) CUZB1004"
										+ " from T ");
						CachedRowSet crs5 = manager.getRs(sql5.toString());
						while (crs5.next()) {

							StringBuffer sqls = new StringBuffer();
							sqls
									.append("select cmt1,cmt2,cmt3,cmt4 from FCCMT fc,fcmain f,BMTABLEAPP b "
											+ "where fccmttype=1 and f.fcno=fc.fcno and f.createdate='"
											+ createdate2
											+ "' and"
											+ " b.bmno=f.bmno and b.clientno='"
											+ clientno + "'");
							CachedRowSet crss = manager.getRs(sqls.toString());
							while (crss.next()) {
								String cmt3 = DBUtil.fromDB(crss
										.getString("cmt1") == null ? "" : crss
										.getString("cmt1"));
								String cmt4 = DBUtil.fromDB(crss
										.getString("cmt2") == null ? "" : crss
										.getString("cmt2"));
								String cmt5 = DBUtil.fromDB(crss
										.getString("cmt3") == null ? "" : crss
										.getString("cmt3"));
								String cmt6 = DBUtil.fromDB(crss
										.getString("cmt4") == null ? "" : crss
										.getString("cmt4"));
								info.setFccmt1(cmt3);
								info.setFccmt2(cmt4);
								info.setFccmt3(cmt5);
								info.setFccmt4(cmt6);
							}

							String cuzb11 = DBUtil.doubleToStr1(Double.valueOf(
									crs5.getString("D29") == null ? "0.00"
											: crs5.getString("D29"))
									.doubleValue() / 10000);
							String dbfx = DBUtil
									.fromDB(crs5.getString("D30") == null ? ""
											: crs5.getString("D30"));
							String hkly1 = DBUtil
									.fromDB(crs5.getString("D31") == null ? ""
											: crs5.getString("D31"));
							String hkly2 = DBUtil
									.fromDB(crs5.getString("D32") == null ? ""
											: crs5.getString("D32"));
							String hkly3 = DBUtil
									.fromDB(crs5.getString("D33") == null ? ""
											: crs5.getString("D33"));
							String qtsm = DBUtil
									.fromDB(crs5.getString("D34") == null ? ""
											: crs5.getString("D34"));
							String cwfx = DBUtil
									.fromDB(crs5.getString("D35") == null ? ""
											: crs5.getString("D35"));
							String fcwfx = DBUtil
									.fromDB(crs5.getString("D36") == null ? ""
											: crs5.getString("D36"));
							String cuzb011 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB101") == null ? "0.00"
															: crs5
																	.getString("CUZB101"))
											.doubleValue());
							String cuzb012 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB102") == null ? "0.00"
															: crs5
																	.getString("CUZB102"))
											.doubleValue());
							String cuzb013 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB103") == null ? "0.00"
															: crs5
																	.getString("CUZB103"))
											.doubleValue());
							String cuzb014 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB104") == null ? "0.00"
															: crs5
																	.getString("CUZB104"))
											.doubleValue());
							String cuzb021 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB201") == null ? "0.00"
															: crs5
																	.getString("CUZB201"))
											.doubleValue());
							String cuzb022 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB202") == null ? "0.00"
															: crs5
																	.getString("CUZB202"))
											.doubleValue());
							String cuzb023 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB203") == null ? "0.00"
															: crs5
																	.getString("CUZB203"))
											.doubleValue());
							String cuzb024 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB204") == null ? "0.00"
															: crs5
																	.getString("CUZB204"))
											.doubleValue());
							String cuzb031 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB301") == null ? "0.00"
															: crs5
																	.getString("CUZB301"))
											.doubleValue());
							String cuzb032 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB302") == null ? "0.00"
															: crs5
																	.getString("CUZB302"))
											.doubleValue());
							String cuzb033 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB303") == null ? "0.00"
															: crs5
																	.getString("CUZB303"))
											.doubleValue());
							String cuzb034 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB304") == null ? "0.00"
															: crs5
																	.getString("CUZB304"))
											.doubleValue());
							String cuzb041 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB401") == null ? "0.00"
															: crs5
																	.getString("CUZB401"))
											.doubleValue());
							String cuzb042 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB402") == null ? "0.00"
															: crs5
																	.getString("CUZB402"))
											.doubleValue());
							String cuzb043 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB403") == null ? "0.00"
															: crs5
																	.getString("CUZB403"))
											.doubleValue());
							String cuzb044 = DBUtil
									.doubleToStr1(Double
											.valueOf(
													crs5.getString("CUZB404") == null ? "0.00"
															: crs5
																	.getString("CUZB404"))
											.doubleValue());
							String cuzb051 = crs5.getString("CUZB501") == null ? ""
									: crs5.getString("CUZB501");
							String cuzb052 = crs5.getString("CUZB502") == null ? ""
									: crs5.getString("CUZB502");
							String cuzb053 = crs5.getString("CUZB503") == null ? ""
									: crs5.getString("CUZB503");
							String cuzb054 = crs5.getString("CUZB504") == null ? ""
									: crs5.getString("CUZB504");
							String cuzb061 = crs5.getString("CUZB601") == null ? ""
									: crs5.getString("CUZB601");
							String cuzb062 = crs5.getString("CUZB602") == null ? ""
									: crs5.getString("CUZB602");
							String cuzb063 = crs5.getString("CUZB603") == null ? ""
									: crs5.getString("CUZB603");
							String cuzb064 = crs5.getString("CUZB604") == null ? ""
									: crs5.getString("CUZB604");
							String cuzb071 = crs5.getString("CUZB701") == null ? ""
									: crs5.getString("CUZB701");
							String cuzb072 = crs5.getString("CUZB702") == null ? ""
									: crs5.getString("CUZB702");
							String cuzb073 = crs5.getString("CUZB703") == null ? ""
									: crs5.getString("CUZB703");
							String cuzb074 = crs5.getString("CUZB704") == null ? ""
									: crs5.getString("CUZB704");
							String cuzb081 = crs5.getString("CUZB801") == null ? ""
									: crs5.getString("CUZB801");
							String cuzb082 = crs5.getString("CUZB802") == null ? ""
									: crs5.getString("CUZB802");
							String cuzb083 = crs5.getString("CUZB803") == null ? ""
									: crs5.getString("CUZB803");
							String cuzb084 = crs5.getString("CUZB804") == null ? ""
									: crs5.getString("CUZB804");
							String cuzb091 = crs5.getString("CUZB901") == null ? ""
									: crs5.getString("CUZB901");
							String cuzb092 = crs5.getString("CUZB902") == null ? ""
									: crs5.getString("CUZB902");
							String cuzb093 = crs5.getString("CUZB903") == null ? ""
									: crs5.getString("CUZB903");
							String cuzb094 = crs5.getString("CUZB904") == null ? ""
									: crs5.getString("CUZB904");
							String cuzb101 = crs5.getString("CUZB1001") == null ? ""
									: crs5.getString("CUZB1001");
							String cuzb102 = crs5.getString("CUZB1002") == null ? ""
									: crs5.getString("CUZB1002");
							String cuzb103 = crs5.getString("CUZB1003") == null ? ""
									: crs5.getString("CUZB1003");
							String cuzb104 = crs5.getString("CUZB1004") == null ? ""
									: crs5.getString("CUZB1004");
							info.setCuzb01(cuzb011);
							info.setCuzb011(cuzb012);
							info.setCuzb012(cuzb013);
							info.setCuzb013(cuzb014);
							info.setCuzb02(cuzb021);
							info.setCuzb021(cuzb022);
							info.setCuzb022(cuzb023);
							info.setCuzb023(cuzb024);
							info.setCuzb03(cuzb031);
							info.setCuzb031(cuzb032);
							info.setCuzb032(cuzb033);
							info.setCuzb033(cuzb034);
							info.setCuzb04(cuzb041);
							info.setCuzb041(cuzb042);
							info.setCuzb042(cuzb043);
							info.setCuzb043(cuzb044);
							info.setCuzb05(cuzb051);
							info.setCuzb051(cuzb052);
							info.setCuzb052(cuzb053);
							info.setCuzb053(cuzb054);
							info.setCuzb06(cuzb061);
							info.setCuzb061(cuzb062);
							info.setCuzb062(cuzb063);
							info.setCuzb063(cuzb064);
							info.setCuzb07(cuzb071);
							info.setCuzb071(cuzb072);
							info.setCuzb072(cuzb073);
							info.setCuzb073(cuzb074);
							info.setCuzb08(cuzb081);
							info.setCuzb081(cuzb082);
							info.setCuzb082(cuzb083);
							info.setCuzb083(cuzb084);
							info.setCuzb09(cuzb091);
							info.setCuzb091(cuzb092);
							info.setCuzb092(cuzb093);
							info.setCuzb093(cuzb094);
							info.setCuzb10(cuzb101);
							info.setCuzb101(cuzb102);
							info.setCuzb102(cuzb103);
							info.setCuzb103(cuzb104);
							info.setCuzb11(cuzb11);
							info.setDbfx(dbfx);
							info.setHkly1(hkly1);
							info.setHkly2(hkly2);
							info.setHkly3(hkly3);
							info.setQtsm(qtsm);
							info.setCwfx(cwfx);
							info.setFcwfx(fcwfx);
							info.setCreate(date2);
						}
					}
				}
			}
			// *********************************财务指标结束**************************

		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public List getResult2() {
		List list = new ArrayList();
		try {
			StringBuffer sql = new StringBuffer();
			sql
					.append("with T as(select distinct f.fcno D10, "
							+ "(select enudt from ptenuminfodetl where enuid='BMType' and f.bmtype=enutp) D14, "
							+ "(select enudt from ptenuminfodetl where enuid='LoanType3' and f.LOANTYPE3=enutp) D15, "
							+ "f.bal D11, "
							+ "f.paydate D12, "
							+ "f.duedate D13,c.clientname,t.PLEDGENAME, t.ESTIMATEPRICE,t.PLDGMORTRATE,t.IFREG, "
							+ "rq.DUEBAL1,rq.DUEBAL2,rq.DUEBAL3, "
							+ "(select enudt from ptenuminfodetl where enuid='LoanCat2' and f.loancat2=enutp) loancat2, "
							+ "fc.cmt1, fc.cmt2, "
							+ "(select enudt from ptenuminfodetl where enuid='CreditClass' and f.CREDITCLASS=enutp) D09, "
							+ " b.SCONTRACTNO  "
							+ "from FCMAIN f "
							+ "left outer join RQDUEINTRST rq on(rq.bmno=f.bmno) "
							+ "left outer join BMPLDGMORT t on(f.bmno=t.bmno) "
							+ "left outer join BMPLDGSECURITY c on(f.bmno=c.bmno)"
							+ "left outer join BMTABLEAPP b on(f.bmno=b.bmno) "
							+ "left outer join FCCMT fc on(fc.fcno=f.fcno and fc.fccmttype=5) "
							+ "where b.clientno='"
							+ clientno
							+ "' and f.createdate='"+createdate2+"'), "
							+ "F as(select "
							+ "SCONTRACTNO, "
							+ "sum(DUEBAL1) DUEBAL1, "
							+ "sum(DUEBAL2) DUEBAL2, "
							+ "sum(DUEBAL3) DUEBAL3, "
							+ "max(clientname) clientname, "
							+ "max(ifreg) ifreg, "
							+ "max(pledgename) pledgename, "
							+ "min(PLDGMORTRATE) PLDGMORTRATE, "
							+ "ESTIMATEPRICE, "
							+ "max(cmt1) cmt1, "
							+ "max(cmt2) cmt2, "
							+ "D09,D10,D11,D12,D13,D14,D15,loancat2 "
							+ "from T group by SCONTRACTNO,D09,D11,D12,D13,D14,D15,loancat2,D10,ESTIMATEPRICE)" +
							" select SCONTRACTNO,sum(ESTIMATEPRICE) ESTIMATEPRICE,max(clientname) clientname,"+
                             " max(ifreg) ifreg,"+
                            " max(pledgename) pledgename,"+
                            " min(PLDGMORTRATE) PLDGMORTRATE,"+
                             " max(cmt1) cmt1,"+
                            " max(cmt2) cmt2, D09,D10,D11,D12,D13,D14,D15,loancat2,max(DUEBAL1) DUEBAL1," +
                            "max(DUEBAL2) DUEBAL2,max(DUEBAL3) DUEBAL3" +
                            " from F group by SCONTRACTNO,D09,D11,D12,D13,D14,D15,loancat2,D10," +
                            "DUEBAL1,DUEBAL2,DUEBAL3");
			CachedRowSet crs = manager.getRs(sql.toString());
			while (crs.next()) {
				CompanyInfo2 info = new CompanyInfo2();
				String scontractno = DBUtil
						.fromDB(crs.getString("D10") == null ? "" : crs
								.getString("D10"));
				String fcno = crs.getString("SCONTRACTNO") == null ? "" : crs
						.getString("SCONTRACTNO");
				String bmtype = DBUtil.fromDB(crs.getString("D14") == null ? ""
						: crs.getString("D14"));
				String loantype3 = DBUtil
						.fromDB(crs.getString("D15") == null ? "" : crs
								.getString("D15"));
				String contractamt = DBUtil.doubleToStr1(Double.valueOf(
						crs.getString("D11") == null ? "0.00" : crs
								.getString("D11")).doubleValue() / 10000);
				String paydate = crs.getString("D12") == null ? "" : crs
						.getString("D12");
				String enddate = crs.getString("D13") == null ? "" : crs
						.getString("D13");
				String clientname = DBUtil
						.fromDB(crs.getString("clientname") == null ? "" : crs
								.getString("clientname"));
				String pledgename = DBUtil
						.fromDB(crs.getString("PLEDGENAME") == null ? "" : crs
								.getString("PLEDGENAME"));
				String estimateprice = DBUtil
						.doubleToStr1(Double.valueOf(
								crs.getString("ESTIMATEPRICE") == null ? "0.00"
										: crs.getString("ESTIMATEPRICE"))
								.doubleValue() / 10000);
				String pldgmortrate = crs.getString("PLDGMORTRATE") == null ? "0"
						: crs.getString("PLDGMORTRATE");
				String ifreg = crs.getString("ifreg") == null ? "" : crs
						.getString("ifreg");
				String due1 = crs.getString("DUEBAL1") == null ? "0.00" : crs
						.getString("DUEBAL1");
				String due2 = crs.getString("DUEBAL2") == null ? "0.00" : crs
						.getString("DUEBAL2");
				String due3 = crs.getString("DUEBAL3") == null ? "0.00" : crs
						.getString("DUEBAL3");
				String loancat2 = DBUtil
						.fromDB(crs.getString("loancat2") == null ? "" : crs
								.getString("loancat2"));
				String cmt1 = DBUtil.fromDB(crs.getString("cmt1") == null ? ""
						: crs.getString("cmt1"));
				String cmt2 = DBUtil.fromDB(crs.getString("cmt2") == null ? ""
						: crs.getString("cmt2"));
				String creditclass = DBUtil
						.fromDB(crs.getString("D09") == null ? "" : crs
								.getString("D09"));
				info.setCreditclass(creditclass);
				info.setCmt1(cmt1);
				info.setCmt2(cmt2);
				info.setScontractno(scontractno);
				info.setTypeno(bmtype);
				info.setLoantype3(loantype3);
				info.setContractamt(contractamt);
				info.setPaydate(paydate);
				info.setEnddate(enddate);
				info.setClientname(clientname);
				info.setPledgename(pledgename);
				info.setEstimateprice(estimateprice);
				info.setPldgmortrate(pldgmortrate);
				info.setIfreg(ifreg);
				info.setDuebal1(due1);
				info.setDuebal2(due2);
				info.setDuebal3(due3);
				info.setLoancat2(loancat2);
				info.setFcno(fcno);
				list.add(info);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public String getDate3() {
		return date3;
	}

	public void setDate3(String date3) {
		this.date3 = date3;
	}

	public String getDate4() {
		return date4;
	}

	public void setDate4(String date4) {
		this.date4 = date4;
	}

	public String[] getDate() {
		return date;
	}

	public void setDate(String[] date) {
		this.date = date;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public boolean isHasCheck() {
		return hasCheck;
	}

	public void setHasCheck(boolean hasCheck) {
		this.hasCheck = hasCheck;
	}

	public String getCreditClass() {
		return creditClass;
	}

	public void setCreditClass(String creditClass) {
		this.creditClass = creditClass;
	}

	public String getCreatedate2() {
		return createdate2;
	}

	public void setCreatedate2(String createdate2) {
		this.createdate2 = createdate2;
	}
}
