package zt.cms.fcsort.fcnbloan;
import java.sql.SQLException;
import java.util.List;

import zt.platform.cachedb.ConnectionManager;
import zt.platform.db.DBUtil;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类新增不良资产查询页面BEAN父类
 * <p/>===============================================
 * <p/>Description: 定义不良查分页和页面参数。
 * 
 * @version $Revision: 1.2 $ $Date: 2007/05/14 07:30:11 $
 * @author houcs <p/>修改：$Author: houcs $
 */

public abstract class AbstractPage {
//	**分页相关
	private int pagecount;//页面记录数
	private int pagesize;// 页面大小
	private int currpage;// 当前页
	private int maxpage;// 最大页
	private String params = "";// 查询参数

	private String lkey;// 权限值

	private boolean show;// 是否搜索
	
	
	private String brhid;// 网点编号

	private String level;// 网点级别
	
	private String yearday;//本年/本期

	private String creadate;// 清分时点

	private String startdate;// 开始时间

	private String enddate;// 结束时间

	private String sartbal;// 时点开始余额

	private String endbal;// 时点结束余额
	
	private String reason;//形成原因
	
	private String loanday;//贷款期限
	
	private String method;//贷款方式
	
	private String fenlei;//贷款投向分类
	private String queryid;//项目编号
	private String sqlpart;//三级页面所需要的一部分SQL
	private String sqlpartname;//三级页面所需要的一部分SQL名称
	private String sqlpartvalue;//三级页面所需要的一部分SQL值
	
	private String managers;//客户经理
	private String firstPerson;//第一责任人
	private String loanFiled;//贷款时段
	ConnectionManager manager;// 数据操作类
	public AbstractPage(HttpServletRequest request)
	{
		manager = ConnectionManager.getInstance();
		this.brhid = request.getParameter("brhid") == null ? "" : request.getParameter("brhid");
		this.creadate = request.getParameter("creadate") == null ? "" : request.getParameter("creadate");
		this.startdate = request.getParameter("startdate") == null ? "": request.getParameter("startdate");
		this.enddate = request.getParameter("enddate") == null ? "" : request.getParameter("enddate");
		this.sartbal = request.getParameter("sartbal") == null ? "" : request.getParameter("sartbal");
		this.endbal = request.getParameter("endbal") == null ? "" : request.getParameter("endbal");
	    this.yearday=request.getParameter("yearday")==null ? "":request.getParameter("yearday");
	    reason=request.getParameter("reason")==null?"":request.getParameter("reason");
	    loanday=request.getParameter("loanday")==null?"":request.getParameter("loanday");
	    method=request.getParameter("method")==null?"":request.getParameter("method");
	    fenlei=request.getParameter("fenlei")==null?"":request.getParameter("fenlei");
	    managers=request.getParameter("managers")==null?"":request.getParameter("managers");
	    firstPerson=request.getParameter("firstPerson")==null?"":request.getParameter("firstPerson");
	    this.pagesize=Integer.valueOf(request.getParameter("pagesize")==null?"10":request.getParameter("pagesize")).intValue();
		this.currpage=Integer.valueOf(request.getParameter("currpage")==null?"1":request.getParameter("currpage")).intValue();
	}
	
	

	

//	****************************************************分页相关*****************************
	public int getCurrpage() {		
		return currpage > getMaxpage() ? getMaxpage() : currpage;
	}
	public void setCurrpage(int currpage) {
		this.currpage = currpage;
	}
	public int getMaxpage() {
		return maxpage == 0 ? 1 : this.maxpage;
	}
	public void setMaxpage() {
		this.maxpage = pagecount / this.pagesize == 0 ? 1 : (pagecount + pagesize - 1)/ pagesize;
	}
	public int getPagecount() {
		return pagecount;
	}

	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getEndrow() {
		return this.currpage >= this.maxpage ? this.pagecount:this.currpage * this.pagesize;
	}
	public int getStartrow() {
		return (this.currpage - 1) * this.pagesize+1;
	}
	

	public String getParams() {
		return params;
	}
    
	public void setParams(String params) {
		this.params += params;
	}
	public List toList(int col,List it,CachedRowSet crs) throws SQLException
	{
		while (crs.next()) {
			String[] str = new String[col];
			for(int i =0;i<col;i++)
			{
				str[i] =DBUtil.fromDB(crs.getString(i+1));
			}
			it.add(str);
			
		}	
		return it;
	}
	public List toListForStrings(int col,List it,CachedRowSet crs) throws SQLException
	{
		while (crs.next()) {
			for(int i =0;i<col;i++)
			{
				String str =DBUtil.fromDB(crs.getString(i+1)==null? "0.00":crs.getString(i+1));
				it.add(str);
			}
		}	
		return it;
	}

	public String getString(String strs) {
		String str = "";
		String h[] = strs.split(",");
		System.out.println(h.length);
		for (int i = 0; i < h.length; i++) {
			if (i < h.length - 1) {
				str += h[i] + "','";
			} else {
				str += h[i];
			}

		}
		return str;

	}






	
	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getLkey() {
		return lkey;
	}

	public void setLkey(String lkey) {
		this.lkey = lkey;
	}

	

	public String getBrhid() {
		return brhid;
	}



	public void setBrhid(String brhid) {
		this.brhid = brhid;
	}



	public String getCreadate() {
		return creadate;
	}



	public void setCreadate(String creadate) {
		this.creadate = creadate;
	}



	public String getEndbal() {
		return endbal;
	}



	public void setEndbal(String endbal) {
		this.endbal = endbal;
	}



	public String getEnddate() {
		return enddate;
	}



	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}


   public String getFirstPerson(){

       return firstPerson;
   }
   public void setFirstPerson(String firstPerson){

     this.firstPerson=firstPerson;
   }
	public String getLevel() {
		return level;
	}
   


	public void setLevel(String level) {
		this.level = level;
	}



	public String getSartbal() {
		return sartbal;
	}



	public void setSartbal(String sartbal) {
		this.sartbal = sartbal;
	}



	public String getStartdate() {
		return startdate;
	}



	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}



	public String getYearday() {
		return yearday;
	}



	public void setYearday(String yearday) {
		this.yearday = yearday;
	}



	public String getFenlei() {
		return fenlei;
	}



	public void setFenlei(String fenlei) {
		this.fenlei = fenlei;
	}



	



	public String getLoanday() {
		return loanday;
	}



	public void setLoanday(String loanday) {
		this.loanday = loanday;
	}






	public String getMethod() {
		return method;
	}



	public void setMethod(String method) {
		this.method = method;
	}



	public String getReason() {
		return reason;
	}



	public void setReason(String reason) {
		this.reason = reason;
	}



	public String getManagers() {
		return managers;
	}



	public void setManagers(String managers) {
		this.managers = managers;
	}



	public String getLoanFiled() {
		return loanFiled;
	}



	public void setLoanFiled(String loanFiled) {
		this.loanFiled = loanFiled;
	}





	public String getSqlpart() {
		return sqlpart;
	}





	public void setSqlpart(String sqlpart) {
		this.sqlpart = sqlpart;
	}





	public String getSqlpartname() {
		return sqlpartname;
	}





	public void setSqlpartname(String sqlpartname) {
		this.sqlpartname = sqlpartname;
	}





	public String getSqlpartvalue() {
		return sqlpartvalue;
	}





	public void setSqlpartvalue(String sqlpartvalue) {
		this.sqlpartvalue = sqlpartvalue;
	}





	public void setMaxpage(int maxpage) {
		this.maxpage = maxpage;
	}





	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}





	public String getQueryid() {
		return queryid;
	}





	public void setQueryid(String queryid) {
		this.queryid = queryid;
	}
}
