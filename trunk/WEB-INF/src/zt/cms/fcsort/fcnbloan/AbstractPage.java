package zt.cms.fcsort.fcnbloan;
import java.sql.SQLException;
import java.util.List;

import zt.platform.cachedb.ConnectionManager;
import zt.platform.db.DBUtil;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
/**
 * <p/>=============================================== 
 * <p/>Title: �弶�������������ʲ���ѯҳ��BEAN����
 * <p/>===============================================
 * <p/>Description: ���岻�����ҳ��ҳ�������
 * 
 * @version $Revision: 1.2 $ $Date: 2007/05/14 07:30:11 $
 * @author houcs <p/>�޸ģ�$Author: houcs $
 */

public abstract class AbstractPage {
//	**��ҳ���
	private int pagecount;//ҳ���¼��
	private int pagesize;// ҳ���С
	private int currpage;// ��ǰҳ
	private int maxpage;// ���ҳ
	private String params = "";// ��ѯ����

	private String lkey;// Ȩ��ֵ

	private boolean show;// �Ƿ�����
	
	
	private String brhid;// ������

	private String level;// ���㼶��
	
	private String yearday;//����/����

	private String creadate;// ���ʱ��

	private String startdate;// ��ʼʱ��

	private String enddate;// ����ʱ��

	private String sartbal;// ʱ�㿪ʼ���

	private String endbal;// ʱ��������
	
	private String reason;//�γ�ԭ��
	
	private String loanday;//��������
	
	private String method;//���ʽ
	
	private String fenlei;//����Ͷ�����
	private String queryid;//��Ŀ���
	private String sqlpart;//����ҳ������Ҫ��һ����SQL
	private String sqlpartname;//����ҳ������Ҫ��һ����SQL����
	private String sqlpartvalue;//����ҳ������Ҫ��һ����SQLֵ
	
	private String managers;//�ͻ�����
	private String firstPerson;//��һ������
	private String loanFiled;//����ʱ��
	ConnectionManager manager;// ���ݲ�����
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
	
	

	

//	****************************************************��ҳ���*****************************
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
