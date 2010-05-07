package zt.cms.fcsort.anterior.rate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import java.util.HashMap;
import zt.platform.db.DBUtil;
import zt.cms.fcsort.anterior.AnteriorQueryPath;
import zt.cms.fcsort.common.FcsortUtil;
import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.platform.cachedb.ConnectionManager;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类贷款迁徙率页面BEAN
 * <p/>===============================================
 * <p/>Description:提供五级分类贷款迁徙率页面所需显示数据，和初始化方法
 * @version $Revision: 1.5 $  $Date: 2007/05/29 12:18:00 $
 * @author zhengxin
 *  <p/>修改：$Author: zhengx $
 */
public class RatePageObject {
	private int sumA;

	private double sumB[] = new double[13];// 浮点型合计列

	private double sunE = 0.00;

	// 数据库连接
	ConnectionManager manager;// 数据操作类

	private HashMap AnteriorQueryPathMap = new HashMap();

	private AnteriorQueryPath AnteriorQuery;

	private String queryid;// 项目编号

	// 查询条件
	private String brhid;// 网点编号

	private String scbrhname;// 网点名称

	private String creadate;// 查询时点

	private String righttitle;// 右标题

	private String lefttitle;// 左标题
	
	private String mory;//本期或本月

	// 格式化显示
	DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

	DecimalFormat dfB = new DecimalFormat("###,###,###,##0.0000");

	FcsortUtil util;

	/**
	 * 设置查询条件和构建页面结果集
	 * @param request
	 * @param response
	 */
	public RatePageObject() {
		util = new FcsortUtil();
		manager = ConnectionManager.getInstance();
	}

	/**
	 * 设置查询参数
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	public boolean setRequest(HttpServletRequest request) throws Exception {
		this.init(request);
		if (request.getParameter("QUERYID") == null
				|| request.getParameter("QUERYID").equals("")) {
			return false;
		} else {
			this.queryid = request.getParameter("QUERYID").trim();
		}
		// Session 中取得 hashmap - FcsortQueryPath
		AnteriorQueryPathMap = (HashMap) request.getSession().getAttribute(
				"FCSORT_ANTERI_PATH_MAP");
		AnteriorQuery = (AnteriorQueryPath) AnteriorQueryPathMap.get(queryid);
		this.brhid = request.getParameter("brhid");// 得到网点编号
		if (this.brhid == null)// 如果为空就取用户所在网点
		{
			UserManager um = (UserManager) request.getSession().getAttribute(
					SystemAttributeNames.USER_INFO_NAME);
			String strUserName = um.getUserName();
			this.brhid = SCUser.getBrhId(strUserName).trim();
			System.out.println(strUserName);
		}
		this.scbrhname = SCBranch.getSName(brhid);
		this.creadate = request.getParameter("creadate") == null ? util.getDT(): request.getParameter("creadate").trim();// 有效日期
		this.mory=request.getParameter("MORY") == null ? "2":request.getParameter("MORY");
		return true;
	}

	/**
	 * 得到一级页面的查询语句
	 * 
	 * @param i
	 * @return String
	 */
	private String getSqlPart1(int i) {
		switch (i) {
		case 1:
			return "select brhid,sname,brhtype from scbranch where BRHLEVEL >1 and upbrh='"
					+ this.getBrhid() + "'";

		case 2:
			return "select brhid,sname,brhtype from scbranch where BRHLEVEL >1 and brhid='"
					+ this.getBrhid() + "'";

		}
		return "";

	}
	/**
	 * 处理网点集合
	 * @param strs
	 * @return String
	 */
	public String getString(String strs) {
		String str = "";
		String h[] = strs.split(",");
		for (int i = 0; i < h.length; i++) {
			if (i < h.length - 1) {
				str += h[i] + "','";
			} else {
				str += h[i];
			}

		}
		return str;

	}
	/**
	 * 得到一级页面的结果集 KS
	 * 
	 * @return List
	 * @throws Exception
	 */
	public List getListKS() throws Exception {
		List list = new ArrayList();
		CachedRowSet crs = manager.getRs(this.getSqlPart1(1));
		if (crs.size() <= 0) {
			crs = manager.getRs(this.getSqlPart1(2));
		}
		
		//本期或本年
		String ftype ="22";
		if(this.mory.trim().equals("1"))
		{
			ftype="26";
		}
		
		while (crs.next()) {
			FirstTr tr = new FirstTr();
			tr.setBrhid(crs.getString("brhid").trim());
			tr.setBrhname(DBUtil.fromDB(crs.getString("sname").trim()));
			tr.setBrhtype(crs.getString("brhtype"));
			String brhids = this.getString(SCBranch.getAllSubBrh1(tr.getBrhid()));
			if (brhids == null || brhids.equals("")) {
				brhids = "('" +tr.getBrhid() + "')";
			} else {
				brhids = "('" + brhids + "')";
			}
			StringBuffer sql = new StringBuffer("select sum(amt1) from fcdata where ftype="+ftype+" and brhid in " +brhids + " and dt='" + this.creadate+ "' and dim1="+this.AnteriorQuery.getSqlpart().trim()+" and dim2 = ");
			for (int i = 1; i <= 3; i++) {
				CachedRowSet child = manager.getRs(sql.toString()+i);
				while (child.next()) {
					if (i == 1) {
						tr.setBala(child.getDouble(1) / 10000);

					}
					if (i == 2) {
						tr.setBalb(child.getDouble(1) / 10000);

					}
					if (i == 3) {
						tr.setBalc(child.getDouble(1) / 10000);

					}
				}
			}
			double C=(tr.getBalb()-tr.getBalc())==0?1:(tr.getBalb()-tr.getBalc());
			tr.setRate((tr.getBala()/C)*100);
			list.add(tr);

		}

		return list;
	}
	/**
	 * 得到一级页面的结果集zs
	 * 
	 * @return List
	 * @throws Exception
	 */
	public List getListZS() throws Exception {
		List list = new ArrayList();
		CachedRowSet crs = manager.getRs(this.getSqlPart1(1));
		if (crs.size() <= 0) {
			crs = manager.getRs(this.getSqlPart1(2));
		}
		String ftype ="22";
		if(this.mory.trim().equals("1"))
		{
			ftype="26";
		}
		while (crs.next()) {
			FirstTr tr = new FirstTr();
			tr.setBrhid(crs.getString("brhid").trim());
			tr.setBrhname(DBUtil.fromDB(crs.getString("sname").trim()));
			tr.setBrhtype(crs.getString("brhtype"));
			String brhids = this.getString(SCBranch.getAllSubBrh1(tr.getBrhid()));
			if (brhids == null || brhids.equals("")) {
				brhids = "('" +tr.getBrhid() + "')";
			} else {
				brhids = "('" + brhids + "')";
			}
			
			
			
			StringBuffer sql = new StringBuffer("select sum(case when dim1=1 then amt1 else 0 end),sum(case when dim1=2 then amt1 else 0 end) from fcdata where ftype="+ftype+" and brhid in " +brhids + " and dt='" + this.creadate+ "' and dim1<=2 and dim2 = ");
			for (int i = 1; i <= 3; i++) {
				CachedRowSet child = manager.getRs(sql.toString()+i);
				while (child.next()) {
					if (i == 1) {
						tr.setBalza(child.getDouble(1) / 10000);
						tr.setBalga(child.getDouble(2) / 10000);
					}
					if (i == 2) {
						tr.setBalzb(child.getDouble(1) / 10000);
						tr.setBalgb(child.getDouble(2) / 10000);

					}
					if (i == 3) {
						tr.setBalzc(child.getDouble(1) / 10000);
						tr.setBalgc(child.getDouble(2) / 10000);


					}
				}
			}
			double C=(tr.getBalgb()+tr.getBalzb()-tr.getBalzc()-tr.getBalgc())==0?1:(tr.getBalgb()+tr.getBalzb()-tr.getBalzc()-tr.getBalgc());
			
			
			tr.setRate(((tr.getBalga()+tr.getBalza())/C)*100);
			list.add(tr);

		}
		return list;
	}

	/**
	 * 设置查询页面所需要的模块信息
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	public boolean init(HttpServletRequest request) throws Exception {
		HashMap map = new HashMap();
		map.put("RATEZS", this.setRATEZS());
		map.put("RATECJ", this.setRATECJ());
		map.put("RATEKY", this.setRATEKY());

		request.getSession().setAttribute("FCSORT_ANTERI_PATH_MAP", map);

		return true;
	}

	/**
	 * 设置五级分类正常贷款迁徙率模块基本参数
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setRATEZS() {

		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("RATEZS");
		query.setQueryname("五级分类正常贷款迁徙率");
		query.setSqlpart("RATEZS");
		return query;
	}
	/**
	 * 设置五级分类次级类贷款迁徙率模块基本参数
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setRATECJ() {

		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("RATECJ");
		query.setQueryname("五级分类次级类贷款迁徙率");
		query.setSqlpart("3");
		return query;
	}

	/**
	 * 设置五级分类可疑类贷款迁徙率模块基本参数
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setRATEKY() {

		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("RATEKY");
		query.setQueryname("五级分类可疑类贷款迁徙率");
		query.setSqlpart("4");
		return query;
	}
	/**
	 * 得到列表的大标题
	 * @return String
	 */
	public String getTitles() {
		
		return (this.mory.trim().equals("1")?"本年":"本期")+this.AnteriorQuery.getQueryname();
	}

	/**
	 * 得到列表的右标题
	 * @return String
	 */
	public String getRighttitle() {
		return righttitle;
	}
	/**
	 * 得到列表的中标题
	 * @return String
	 */
	public String getMoeltitle() {
		return this.creadate.trim().substring(0, 4)
				+ "年"
				+ this.creadate.trim().substring(4,
						this.creadate.trim().length()) + "月份";
	}
	/**
	 * 得到列表的左标题
	 * @return String
	 */
	public String getLefttitle() {
		return lefttitle;
	}

	/**
	 * @return anteriorQuery
	 */
	public AnteriorQueryPath getAnteriorQuery() {
		return AnteriorQuery;
	}

	/**
	 * 设置 anteriorQuery
	 * @param anteriorQuery 
	 */
	public void setAnteriorQuery(AnteriorQueryPath anteriorQuery) {
		AnteriorQuery = anteriorQuery;
	}

	/**
	 * @return anteriorQueryPathMap
	 */
	public HashMap getAnteriorQueryPathMap() {
		return AnteriorQueryPathMap;
	}

	/**
	 * 设置 anteriorQueryPathMap
	 * @param anteriorQueryPathMap 
	 */
	public void setAnteriorQueryPathMap(HashMap anteriorQueryPathMap) {
		AnteriorQueryPathMap = anteriorQueryPathMap;
	}

	/**
	 * @return brhid
	 */
	public String getBrhid() {
		return brhid;
	}

	/**
	 * 设置 brhid
	 * @param brhid 
	 */
	public void setBrhid(String brhid) {
		this.brhid = brhid;
	}

	/**
	 * @return creadate
	 */
	public String getCreadate() {
		return creadate;
	}

	/**
	 * 设置 creadate
	 * @param creadate 
	 */
	public void setCreadate(String creadate) {
		this.creadate = creadate;
	}

	/**
	 * @return queryid
	 */
	public String getQueryid() {
		return queryid;
	}

	/**
	 * 设置 queryid
	 * @param queryid 
	 */
	public void setQueryid(String queryid) {
		this.queryid = queryid;
	}

	/**
	 * @return scbrhname
	 */
	public String getScbrhname() {
		return scbrhname;
	}

	/**
	 * 设置 scbrhname
	 * @param scbrhname 
	 */
	public void setScbrhname(String scbrhname) {
		this.scbrhname = scbrhname;
	}

	/**
	 * @return sumA
	 */
	public int getSumA() {
		return sumA;
	}

	/**
	 * 设置 sumA
	 * @param sumA 
	 */
	public void setSumA(int sumA) {
		this.sumA = sumA;
	}

	/**
	 * @return sumB
	 */
	public double[] getSumB() {
		return sumB;
	}

	/**
	 * 设置 sumB
	 * @param sumB 
	 */
	public void setSumB(double[] sumB) {
		this.sumB = sumB;
	}

	/**
	 * @return sunE
	 */
	public double getSunE() {
		return sunE;
	}

	/**
	 * 设置 sunE
	 * @param sunE 
	 */
	public void setSunE(double sunE) {
		this.sunE = sunE;
	}

	/**
	 * 设置 lefttitle
	 * @param lefttitle 
	 */
	public void setLefttitle(String lefttitle) {
		this.lefttitle = lefttitle;
	}

	/**
	 * 设置 righttitle
	 * @param righttitle 
	 */
	public void setRighttitle(String righttitle) {
		this.righttitle = righttitle;
	}

	/**
	 * @return mory
	 */
	public String getMory() {
		return mory;
	}

	/**
	 * 设置 mory
	 * @param mory 
	 */
	public void setMory(String mory) {
		this.mory = mory;
	}

	
}
