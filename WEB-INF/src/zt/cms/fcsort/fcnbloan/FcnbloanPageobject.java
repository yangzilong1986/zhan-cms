package zt.cms.fcsort.fcnbloan;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;

import zt.cms.pub.SCBranch;
import zt.platform.db.DBUtil;
import zt.cmsi.pub.define.SystemDate;

/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类新增不良资产查询页面
 * <p/>===============================================
 * <p/>Description:五级分类新增不良资产查询页面。
 * 
 * @version $Revision: 1.8 $ $Date: 2007/05/28 14:28:02 $
 * @author houcs
 *  <p/>修改：$Author: houcs $
 */

public class FcnbloanPageobject extends AbstractPage {
	/**
	 * 设置查询条件和构建页面结果集
	 * 
	 * @param request
	 * @param response
	 */
	/**
	 * 系统当前时间
	 */

	private int[] sum1=new int[2];
	private int[] sum2=new int[2];
	private int[] sum3=new int[2];
	private int[] sum4=new int[2];
	private int[] sum5=new int[2];
	private int[] sum6=new int[2];
	private double[] dsum1=new double[5];
	private double[] dsum2=new double[5];
	private double[] dsum3=new double[5];
	private double[] dsum4=new double[5];
	private double[] dsum5=new double[5];
	private double[] dsum6=new double[5];
	private String brhidLevel="";
	

	public String getBrhidLevel() {
		return brhidLevel;
	}


	public void setBrhidLevel(String brhidLevel) {
		this.brhidLevel = brhidLevel;
	}


	public FcnbloanPageobject(HttpServletRequest request) {
		super(request);
		SystemDate.refresh();
	}
	
	
/**
 * 得到数据集
 */
	public List getListvp() {		
		List it = new ArrayList();
		try {
			String brhids="";
			String sqlup = "select brhid,sname,brhtype,BRHLEVEL from scbranch where upbrh='"+ this.getBrhid() + "' and BRHLEVEL <> 1";
			CachedRowSet crs = manager.getRs(sqlup);
			if(crs.size()<=0){
				FirstnbLoan ff = new FirstnbLoan();
				brhidLevel="4";
				String sqlup1 = "select brhid,sname,brhtype from scbranch where brhid='"+ this.getBrhid() + "'";
				CachedRowSet crs1= manager.getRs(sqlup1);
				String sname="";
				while(crs1.next()){
				 sname = DBUtil.fromDB(crs1.getString(2));// 网点名称
				}
				ff.setBrhid(getBrhid());
				ff.setSname(sname);
				
				brhids="('" + getBrhid().trim() + "')";
				StringBuffer sqlbn = new StringBuffer(
				"select count(a.bmno) bmnos," +
				"count(distinct(a.idno)) idnos," +
				"sum(contractamt) contractamt ," +
				"sum(a.bal) fcbals," +
				"sum(case when a.FCCLASS=3 then bal else 0 end ) h3,");
   sqlbn.append("sum(case when a.FCCLASS=4 then bal else 0 end ) h4," +
   		        "sum(case when a.FCCLASS=5 then bal else 0 end ) h5");
  sqlbn.append(" from fcbmain a,rqloanlist r where  ");
      
		if ((this.getStartdate() != null && !this.getStartdate().equals(""))&&(this.getEnddate()!= null && !this.getEnddate().equals(""))) 
		{
			
			sqlbn.append(" paydate between '" + this.getStartdate().trim()+ "' and '" + this.getEnddate().trim() + "' and ");
		}
		if ((this.getStartdate() != null && !this.getStartdate().equals("")) && ((this.getEnddate().equals("") || this.getEnddate()== null ))) 
		{
			
			sqlbn.append(" paydate >= '" + this.getStartdate().trim()+"' and ");
		}
		if ((this.getStartdate() == null || this.getStartdate().equals("")) && ((this.getEnddate()!= null && !this.getEnddate().equals("")))) 
		{
			sqlbn.append(" paydate <= '" + this.getEnddate().trim()+"' and ");
		}
		if (this.getCreadate() != null && !this.getCreadate().equals("")&& !this.getCreadate().equals("0")) {
			sqlbn.append(" CREATEDATE='" + this.getCreadate().trim()+ "' and  ");
		}
		if (this.getSartbal() != null && !this.getSartbal().equals("")) {
			sqlbn.append(" bal >="
					+ Double.valueOf(this.getSartbal().trim()).doubleValue()
					* (10000) + " and  ");
		}
		if (this.getEndbal() != null && !this.getEndbal().equals("")) {
			sqlbn.append("bal <="+ Double.valueOf(this.getEndbal().trim()).doubleValue()* (10000) + " and  ");
		}
		
		if(this.getYearday() !=null && !this.getYearday().equals("")&& !this.getYearday().equals("0")){
			
			if(DBUtil.fromDB(this.getYearday()).equals("1")){
				
				sqlbn.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in "+brhids+" and a.bmtype not in(12,13,14)");
			}else if(DBUtil.fromDB(this.getYearday()).equals("2")){
				
				sqlbn.append(" a.bmno=r.bmno and (a.LMFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in "+brhids+"  and a.bmtype not in(12,13,14)");
			}
		}else{
		sqlbn.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS<3 or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in "+brhids+" and a.bmtype not in(12,13,14)");
		}
		CachedRowSet child = manager.getRs(sqlbn.toString());
		while (child.next()){
			int a=0;
			int b=0;
			double c=0.00;
			double d=0.00;
			double e=0.00;
			double f=0.00;
			double g=0.00;
			
			int newLoanSum=Integer.valueOf(child.getString("bmnos")==null?"0.00":child.getString("bmnos")).intValue();
			a+=newLoanSum;
			String sumA=String.valueOf(a);
			ff.setNewloansum(sumA);
			int newperson=Integer.valueOf(child.getString("idnos")==null?"0.00":child.getString("idnos")).intValue();
			b+=newperson;
			String sumB=String.valueOf(b);
			ff.setNewpersonsum(sumB);
			double bal=Double.valueOf(child.getString("fcbals")==null?"0.00":child.getString("fcbals")).doubleValue()*0.0001;
			c += bal;
			String sumC=DBUtil.doubleToStr1(c).toString();
			String sumC1=DBUtil.doubleToStr(c).toString();
			ff.setBal1(sumC1);
			ff.setBal(sumC);
			double contamt=Double.valueOf(child.getString("contractamt")==null?"0.00":child.getString("contractamt")).doubleValue()*0.0001;
			d +=contamt;
			String sumD=DBUtil.doubleToStr1(d).toString();
			ff.setLoanbal(sumD);
			String sumD1=DBUtil.doubleToStr(d).toString();
			ff.setLoanbal1(sumD1);
			double H3=Double.valueOf(child.getString("H3")==null?"0.00":child.getString("H3")).doubleValue()*0.0001;
			e += H3;
			String sumE=DBUtil.doubleToStr1(e).toString();
		
			ff.setH3(sumE);
			String sumE1=DBUtil.doubleToStr(e).toString();
			
			ff.setHh3(sumE1);
			double H4=Double.valueOf(child.getString("H4")==null?"0.00":child.getString("H4")).doubleValue()*0.0001;
			f +=H4;
			String sumF=DBUtil.doubleToStr1(f).toString();
			ff.setH4(sumF);
			String sumF1=DBUtil.doubleToStr(f).toString();
			ff.setHh4(sumF1);
		   double H5=Double.valueOf(child.getString("H5")==null?"0.00":child.getString("H5")).doubleValue()*0.0001;
			g += H5;
			String sumG=DBUtil.doubleToStr1(g).toString();
			ff.setH5(sumG);
			String sumG1=DBUtil.doubleToStr(g).toString();
			ff.setHh5(sumG1);
		}
		it.add(ff);
			}else{
				String sqlup1 = "select max(BRHLEVEL) BRHLEVEL from scbranch where brhid='"+ this.getBrhid() + "'";
				CachedRowSet crs1= manager.getRs(sqlup1);
				while(crs1.next()){
					String brhidLevel1=crs1.getString("BRHLEVEL");//网点级别
					if(brhidLevel1.trim().equals("1")){
						brhidLevel="1";
					}else if(brhidLevel1.trim().equals("2")){
						brhidLevel="2";
					}
					else
					{
						brhidLevel="4";
					}
				}
			while (crs.next()) {
				FirstnbLoan ff = new FirstnbLoan();
				String cbrhid = crs.getString(1).trim();// 网点编号
				String sname = DBUtil.fromDB(crs.getString(2));// 网点名称
				String brhtype = crs.getString(3).trim();// 网点虚实
				ff.setBrhid(cbrhid);
				ff.setSname(sname);
				ff.setBrhtype(brhtype);
				 brhids = this.getString(SCBranch.getAllSubBrh1(cbrhid));// 所有下级网点编号
				if (brhids == null || brhids.equals("")) {
					brhids = "('" + cbrhid + "')";
				} else {
					brhids = "('" + brhids + "')";
				}
				StringBuffer sqlbn = new StringBuffer(
						"select count(a.bmno) bmnos,count(distinct(a.idno)) idnos,sum(contractamt) contractamt ,sum(a.bal) fcbals,sum(case when a.FCCLASS=3 then bal else 0 end ) h3,");
				sqlbn
						.append(" sum(case when a.FCCLASS=4 then bal else 0 end ) h4,sum(case when a.FCCLASS=5 then bal else 0 end ) h5");
				sqlbn.append(" from fcbmain a,rqloanlist r where  ");

				if ((this.getStartdate() != null && !this.getStartdate().equals(""))&&(this.getEnddate()!= null && !this.getEnddate().equals(""))) 
				{
					sqlbn.append(" paydate between '" + this.getStartdate().trim()+ "' and '" + this.getEnddate().trim() + "' and ");
				}
				if ((this.getStartdate() != null && !this.getStartdate().equals("")) && ((this.getEnddate().equals("") || this.getEnddate()== null ))) 
				{
					
					sqlbn.append(" paydate >= '" + this.getStartdate().trim()+"' and ");
				}
				if ((this.getStartdate() == null || this.getStartdate().equals("")) && ((this.getEnddate()!= null && !this.getEnddate().equals("")))) 
				{
					sqlbn.append(" paydate <= '" + this.getEnddate().trim()+"' and ");
				}
				if (this.getCreadate() != null && !this.getCreadate().equals("")&& !this.getCreadate().equals("0")) {
					sqlbn.append(" CREATEDATE='" + this.getCreadate().trim()+ "' and  ");
				}
				if (this.getSartbal() != null && !this.getSartbal().equals("")) {
					sqlbn.append(" bal >="
							+ Double.valueOf(this.getSartbal().trim()).doubleValue()
							* (10000) + " and  ");
				}
				if (this.getEndbal() != null && !this.getEndbal().equals("")) {
					sqlbn.append("bal <="+ Double.valueOf(this.getEndbal().trim()).doubleValue()* (10000) + " and  ");
				}
				if(this.getYearday() !=null && !this.getYearday().equals("")&& !this.getYearday().equals("0")){
					if(DBUtil.fromDB(this.getYearday()).equals("1")){
						sqlbn.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in "+brhids+" and a.bmtype not in(12,13,14)");
					}else if(DBUtil.fromDB(this.getYearday()).equals("2")){
						
						sqlbn.append(" a.bmno=r.bmno and (a.LMFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in "+brhids+" and a.bmtype not in(12,13,14)");
					}
				}else{
				sqlbn.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS<3 or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in "+brhids+" and a.bmtype not in(12,13,14)");
				}
				CachedRowSet child = manager.getRs(sqlbn.toString());
				while (child.next()){
					int a=0;
					int b=0;
					double c=0.00;
					double d=0.00;
					double e=0.00;
					double f=0.00;
					double g=0.00;
					
					int newLoanSum=Integer.valueOf(child.getString("bmnos")==null?"0.00":child.getString("bmnos")).intValue();
					a+=newLoanSum;
					String sumA=String.valueOf(a);
					ff.setNewloansum(sumA);
					int newperson=Integer.valueOf(child.getString("idnos")==null?"0.00":child.getString("idnos")).intValue();
					b+=newperson;
					String sumB=String.valueOf(b);
					ff.setNewpersonsum(sumB);
					double bal=Double.valueOf(child.getString("fcbals")==null?"0.00":child.getString("fcbals")).doubleValue()*0.0001;
					c += bal;
					String sumC=DBUtil.doubleToStr1(c).toString();
					String sumC1=DBUtil.doubleToStr(c).toString();
					ff.setBal1(sumC1);
					ff.setBal(sumC);
					double contamt=Double.valueOf(child.getString("contractamt")==null?"0.00":child.getString("contractamt")).doubleValue()*0.0001;
					d +=contamt;
					String sumD=DBUtil.doubleToStr1(d).toString();
					ff.setLoanbal(sumD);
					String sumD1=DBUtil.doubleToStr(d).toString();
					ff.setLoanbal1(sumD1);
					double H3=Double.valueOf(child.getString("H3")==null?"0.00":child.getString("H3")).doubleValue()*0.0001;
					e += H3;
					String sumE=DBUtil.doubleToStr1(e).toString();
				
					ff.setH3(sumE);
					String sumE1=DBUtil.doubleToStr(e).toString();
					
					ff.setHh3(sumE1);
					double H4=Double.valueOf(child.getString("H4")==null?"0.00":child.getString("H4")).doubleValue()*0.0001;
					f +=H4;
					String sumF=DBUtil.doubleToStr1(f).toString();
					ff.setH4(sumF);
					String sumF1=DBUtil.doubleToStr(f).toString();
					ff.setHh4(sumF1);
				   double H5=Double.valueOf(child.getString("H5")==null?"0.00":child.getString("H5")).doubleValue()*0.0001;
					g += H5;
					String sumG=DBUtil.doubleToStr1(g).toString();
					ff.setH5(sumG);
					String sumG1=DBUtil.doubleToStr(g).toString();
					ff.setHh5(sumG1);
				}
				it.add(ff);
               
			}
			}
		} catch (RuntimeException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return it;
	}

public FirstnbLoan get1LRs(){//往年发放往年到期结果
	
	FirstnbLoan ff = new FirstnbLoan();
	try{
		String brhid=getBrhid();
		brhid=SCBranch.getSubBranchAll(brhid);
		brhid=brhid.replaceAll(",", "','");
		StringBuffer sql = new StringBuffer(
		           "select count(a.bmno) bmnos,count(distinct(a.idno)) " +
		           "idnos,sum(contractamt) contractamt ," +
		           "sum(a.bal) fcbals,sum(case when a.FCCLASS=3 then bal else 0 end ) h3,");
        sql.append(" sum(case when a.FCCLASS=4 then bal else 0 end ) h4," +
        		"sum(case when a.FCCLASS=5 then bal else 0 end ) h5");
        sql.append(" from fcbmain a,rqloanlist r where  ");
        if ((this.getStartdate() != null && !this.getStartdate().equals(""))&&(this.getEnddate()!= null && !this.getEnddate().equals(""))) 
		{
			sql.append(" paydate between '" + this.getStartdate().trim()+ "' and '" + this.getEnddate().trim() + "' and ");
		}
        if ((this.getStartdate() != null && !this.getStartdate().equals("")) && ((this.getEnddate().equals("") || this.getEnddate()== null ))) 
		{
			
        	sql.append(" paydate >= '" + this.getStartdate().trim()+"' and ");
		}
		if ((this.getStartdate() == null || this.getStartdate().equals("")) && ((this.getEnddate()!= null && !this.getEnddate().equals("")))) 
		{
			sql.append(" paydate <= '" + this.getEnddate().trim()+"' and ");
		}
		if (this.getCreadate() != null && !this.getCreadate().equals("")&& !this.getCreadate().equals("0")) {
			sql.append(" CREATEDATE='" + this.getCreadate().trim()+ "' and  ");
		}
		if (this.getSartbal() != null && !this.getSartbal().equals("")) {
			sql.append(" bal >="
					+ Double.valueOf(this.getSartbal().trim()).doubleValue()
					* (10000) + " and  ");
		}
		if (this.getEndbal() != null && !this.getEndbal().equals("")) {
			sql.append("bal <="+ Double.valueOf(this.getEndbal().trim()).doubleValue()* (10000) + " and  ");
		}
        if (getReason() != null && !getReason().equals("0")&& !getReason().equals("")){
        	sql.append(" a.badreason="+Integer.valueOf(getReason().trim()).intValue()+" and ");
        }
        if (getLoanday() != null && !getLoanday().equals("0")&& !getLoanday().equals("")){
        	sql.append(" r.loantype2="+Integer.valueOf(getLoanday().trim()).intValue()+" and ");
        }
        if (getMethod() != null && !getMethod().equals("0")&& !getMethod().equals("")){
        	sql.append(" a.loantype3="+Integer.valueOf(getMethod().trim()).intValue()+" and ");
        }
        if (getFenlei() != null && !getFenlei().equals("0")&& !getFenlei().equals("")){
        	if(getFenlei().equals("allOne")){
        		sql.append(" a.loanway in ('101','102','103','199') and ");
        	}else if(getFenlei().equals("allTwo")){
        		sql.append(" a.loanway in ('201','202','203','204','205','206','299') and ");
        	}else if(getFenlei().equals("allThree")){
        		sql.append(" a.loanway in ('301','302','303','304','305','399') and ");
        	}else{
        	sql.append(" a.loanway='"+getFenlei().trim()+"' and ");
        	}
        }
        if (getManagers() != null && !getManagers().equals("0")&& !getManagers().equals("")){
        	sql.append(" r.clientmgr='"+getManagers().trim()+"'and ");
        }
        if(this.getYearday() !=null && !this.getYearday().equals("")&& !this.getYearday().equals("0")){
			
			if(DBUtil.fromDB(this.getYearday()).equals("1")){
				sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}else if(DBUtil.fromDB(this.getYearday()).equals("2")){
				
				sql.append(" a.bmno=r.bmno and (a.LMFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}
		}else{
		sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS<3 or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
		}
        sql.append(" a.duedate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+"' and a.bmtype not in(12,13,14)");
    	CachedRowSet child = manager.getRs(sql.toString());
		while (child.next()){
			int a=0;
			int b=0;
			double c=0.00;
			double d=0.00;
			double e=0.00;
			double f=0.00;
			double g=0.00;
			int newLoanSum=Integer.valueOf(child.getString("bmnos")==null?"0.00":child.getString("bmnos")).intValue();
			a+=newLoanSum;
			sum1[0]=a;
			String sumA=String.valueOf(a);
			ff.setNewloansum(sumA);
			int newperson=Integer.valueOf(child.getString("idnos")==null?"0.00":child.getString("idnos")).intValue();
			b+=newperson;
			sum1[1]=b;
			String sumB=String.valueOf(b);
			ff.setNewpersonsum(sumB);
			double bal=Double.valueOf(child.getString("fcbals")==null?"0.00":child.getString("fcbals")).doubleValue()*0.0001;
			c += bal;
			dsum1[0]=c;
			String sumC=DBUtil.doubleToStr1(c).toString();
			ff.setBal(sumC);
			double contamt=Double.valueOf(child.getString("contractamt")==null?"0.00":child.getString("contractamt")).doubleValue()*0.0001;
			d +=contamt;
			dsum1[1]=d;
			String sumD=DBUtil.doubleToStr1(d).toString();
			ff.setLoanbal(sumD);
			double H3=Double.valueOf(child.getString("H3")==null?"0.00":child.getString("H3")).doubleValue()*0.0001;
			e += H3;
			dsum1[2]=e;
			String sumE=DBUtil.doubleToStr1(e).toString();
		
			ff.setH3(sumE);
			double H4=Double.valueOf(child.getString("H4")==null?"0.00":child.getString("H4")).doubleValue()*0.0001;
			f +=H4;
			dsum1[3]=f;
			String sumF=DBUtil.doubleToStr1(f).toString();
			ff.setH4(sumF);
		   double H5=Double.valueOf(child.getString("H5")==null?"0.00":child.getString("H5")).doubleValue()*0.0001;
			g += H5;
			dsum1[4]=g;
			String sumG=DBUtil.doubleToStr1(g).toString();
			ff.setH5(sumG);
		}
		
	}catch(RuntimeException e){
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return ff;
}
public FirstnbLoan get2LRs(){//往年发放当年到期结果
	FirstnbLoan ff = new FirstnbLoan();
	try{
		String brhid=getBrhid();
		brhid=SCBranch.getSubBranchAll(brhid);
		brhid=brhid.replaceAll(",", "','");
		StringBuffer sql = new StringBuffer(
		           "select count(a.bmno) bmnos,count(distinct(a.idno)) " +
		           "idnos,sum(contractamt) contractamt ," +
		           "sum(a.bal) fcbals,sum(case when a.FCCLASS=3 then bal else 0 end ) h3,");
        sql.append(" sum(case when a.FCCLASS=4 then bal else 0 end ) h4," +
        		"sum(case when a.FCCLASS=5 then bal else 0 end ) h5");
        sql.append(" from fcbmain a,rqloanlist r where  ");
        if ((this.getStartdate() != null && !this.getStartdate().equals(""))&&(this.getEnddate()!= null && !this.getEnddate().equals(""))) 
		{
			sql.append(" paydate between '" + this.getStartdate().trim()+ "' and '" + this.getEnddate().trim() + "' and ");
		}
        if ((this.getStartdate() != null && !this.getStartdate().equals("")) && ((this.getEnddate().equals("") || this.getEnddate()== null ))) 
		{
			
        	sql.append(" paydate >= '" + this.getStartdate().trim()+"' and ");
		}
		if ((this.getStartdate() == null || this.getStartdate().equals("")) && ((this.getEnddate()!= null && !this.getEnddate().equals("")))) 
		{
			sql.append(" paydate <= '" + this.getEnddate().trim()+"' and ");
		}
		if (this.getCreadate() != null && !this.getCreadate().equals("")&& !this.getCreadate().equals("0")) {
			sql.append(" CREATEDATE='" + this.getCreadate().trim()+ "' and  ");
		}
		if (this.getSartbal() != null && !this.getSartbal().equals("")) {
			sql.append(" bal >="
					+ Double.valueOf(this.getSartbal().trim()).doubleValue()
					* (10000) + " and  ");
		}
		if (this.getEndbal() != null && !this.getEndbal().equals("")) {
			sql.append("bal <="+ Double.valueOf(this.getEndbal().trim()).doubleValue()* (10000) + " and  ");
		}
        if (getReason() != null && !getReason().equals("")&& !getReason().equals("0")){
        	sql.append(" a.badreason="+Integer.valueOf(getReason().trim()).intValue()+" and ");
        	
        }
        if (getLoanday() != null && !getLoanday().equals("")&&!getLoanday().equals("0")){
        	sql.append(" r.loantype2="+Integer.valueOf(getLoanday().trim()).intValue()+" and ");
        	
        }
        if (getMethod() != null && !getMethod().equals("")&& !getMethod().equals("0")){
        	sql.append(" a.loantype3="+Integer.valueOf(getMethod().trim()).intValue()+" and ");
        	
        }
        if (getFenlei() != null && !getFenlei().equals("")&& !getFenlei().equals("0")){
        	if(getFenlei().equals("allOne")){
        		sql.append(" a.loanway in ('101','102','103','199') and ");
        	}else if(getFenlei().equals("allTwo")){
        		sql.append(" a.loanway in ('201','202','203','204','205','206','299') and ");
        	}else if(getFenlei().equals("allThree")){
        		sql.append(" a.loanway in ('301','302','303','304','305','399') and ");
        	}else{
        	sql.append(" a.loanway='"+getFenlei().trim()+"' and ");
        	}
        	
        }
        if (getManagers() != null && !getManagers().equals("")&& !getManagers().equals("0")){
        	sql.append(" r.clientmgr='"+getManagers().trim()+"'and ");
        }
       if(this.getYearday() !=null && !this.getYearday().equals("")&& !this.getYearday().equals("0")){
			
			if(DBUtil.fromDB(this.getYearday()).equals("1")){
				sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}else if(DBUtil.fromDB(this.getYearday()).equals("2")){
				
				sql.append(" a.bmno=r.bmno and (a.LMFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}
		}else{
		sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS<3 or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
		}
        sql.append(" a.paydate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
        		"' and duedate > '"+SystemDate.getLastYearDate(SystemDate.getSystemDate2(),"-")+"'" +
        		" and a.duedate<='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"' and a.bmtype not in(12,13,14)" 
        		);
    	CachedRowSet child = manager.getRs(sql.toString());
		while (child.next()){
			int a=0;
			int b=0;
			double c=0.00;
			double d=0.00;
			double e=0.00;
			double f=0.00;
			double g=0.00;
			int newLoanSum=Integer.valueOf(child.getString("bmnos")==null?"0.00":child.getString("bmnos")).intValue();
			a+=newLoanSum;
			sum2[0]=a;
			String sumA=String.valueOf(a);
			ff.setNewloansum(sumA);
			int newperson=Integer.valueOf(child.getString("idnos")==null?"0.00":child.getString("idnos")).intValue();
			b+=newperson;
			sum2[1]=b;
			String sumB=String.valueOf(b);
			ff.setNewpersonsum(sumB);
			double bal=Double.valueOf(child.getString("fcbals")==null?"0.00":child.getString("fcbals")).doubleValue()*0.0001;
			c += bal;
			dsum2[0]=c;
			String sumC=DBUtil.doubleToStr1(c).toString();
			ff.setBal(sumC);
			double contamt=Double.valueOf(child.getString("contractamt")==null?"0.00":child.getString("contractamt")).doubleValue()*0.0001;
			d +=contamt;
			dsum2[1]=d;
			String sumD=DBUtil.doubleToStr1(d).toString();
			ff.setLoanbal(sumD);
			double H3=Double.valueOf(child.getString("H3")==null?"0.00":child.getString("H3")).doubleValue()*0.0001;
			e += H3;
			dsum2[2]=e;
			String sumE=DBUtil.doubleToStr1(e).toString();
			ff.setH3(sumE);
			double H4=Double.valueOf(child.getString("H4")==null?"0.00":child.getString("H4")).doubleValue()*0.0001;
			f +=H4;
			dsum2[3]=f;
			String sumF=DBUtil.doubleToStr1(f).toString();
			ff.setH4(sumF);
		   double H5=Double.valueOf(child.getString("H5")==null?"0.00":child.getString("H5")).doubleValue()*0.0001;
			g += H5;
			dsum2[4]=g;
			String sumG=DBUtil.doubleToStr1(g).toString();
			ff.setH5(sumG);
			
		}
      
	}catch(RuntimeException e){
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
	return ff;
}
public FirstnbLoan get8LRs(){//往年发放当年后到期
	FirstnbLoan ff = new FirstnbLoan();
	try{
		String brhid=getBrhid();
		brhid=SCBranch.getSubBranchAll(brhid);
		brhid=brhid.replaceAll(",", "','");
		StringBuffer sql = new StringBuffer(
		           "select count(a.bmno) bmnos,count(distinct(a.idno)) " +
		           "idnos,sum(contractamt) contractamt ," +
		           "sum(a.bal) fcbals,sum(case when a.FCCLASS=3 then bal else 0 end ) h3,");
        sql.append(" sum(case when a.FCCLASS=4 then bal else 0 end ) h4," +
        		"sum(case when a.FCCLASS=5 then bal else 0 end ) h5");
        sql.append(" from fcbmain a,rqloanlist r where  ");
        if ((this.getStartdate() != null && !this.getStartdate().equals(""))&&(this.getEnddate()!= null && !this.getEnddate().equals(""))) 
		{
			sql.append(" paydate between '" + this.getStartdate().trim()+ "' and '" + this.getEnddate().trim() + "' and ");
		}
        if ((this.getStartdate() != null && !this.getStartdate().equals("")) && ((this.getEnddate().equals("") || this.getEnddate()== null ))) 
		{
			
        	sql.append(" paydate >= '" + this.getStartdate().trim()+"' and ");
		}
		if ((this.getStartdate() == null || this.getStartdate().equals("")) && ((this.getEnddate()!= null && !this.getEnddate().equals("")))) 
		{
			sql.append(" paydate <= '" + this.getEnddate().trim()+"' and ");
		}
		if (this.getCreadate() != null && !this.getCreadate().equals("")&& !this.getCreadate().equals("0")) {
			sql.append(" CREATEDATE='" + this.getCreadate().trim()+ "' and  ");
		}
		if (this.getSartbal() != null && !this.getSartbal().equals("")) {
			sql.append(" bal >="
					+ Double.valueOf(this.getSartbal().trim()).doubleValue()
					* (10000) + " and  ");
		}
		if (this.getEndbal() != null && !this.getEndbal().equals("")) {
			sql.append("bal <="+ Double.valueOf(this.getEndbal().trim()).doubleValue()* (10000) + " and  ");
		}
        if (getReason() != null && !getReason().equals("")&& !getReason().equals("0")){
        	sql.append(" a.badreason="+Integer.valueOf(getReason().trim()).intValue()+" and ");
        	//System.out.println(Integer.valueOf(getReason().trim()).intValue()+"-------getReason");
        }
        if (getLoanday() != null && !getLoanday().equals("")&&!getLoanday().equals("0")){
        	sql.append(" r.loantype2="+Integer.valueOf(getLoanday().trim()).intValue()+" and ");
        	//System.out.println(Integer.valueOf(getLoanday().trim()).intValue()+"-------getloanday");
        }
        if (getMethod() != null && !getMethod().equals("")&& !getMethod().equals("0")){
        	sql.append(" a.loantype3="+Integer.valueOf(getMethod().trim()).intValue()+" and ");
        	//System.out.println(Integer.valueOf(getMethod().trim()).intValue()+"-------getMethod");
        }
        if (getFenlei() != null && !getFenlei().equals("")&& !getFenlei().equals("0")){
        	if(getFenlei().equals("allOne")){
        		sql.append(" a.loanway in ('101','102','103','199') and ");
        	}else if(getFenlei().equals("allTwo")){
        		sql.append(" a.loanway in ('201','202','203','204','205','206','299') and ");
        	}else if(getFenlei().equals("allThree")){
        		sql.append(" a.loanway in ('301','302','303','304','305','399') and ");
        	}else{
        	sql.append(" a.loanway='"+getFenlei().trim()+"' and ");
        	}
        	//System.out.println(Integer.valueOf(getFenlei().trim()).intValue()+"-------getFenlei");
        }
        if (getManagers() != null && !getManagers().equals("")&& !getManagers().equals("0")){
        	sql.append(" r.clientmgr='"+getManagers().trim()+"'and ");
        	//System.out.println(Integer.valueOf(getManagers().trim()).intValue()+"-------getManagers");
        }
       if(this.getYearday() !=null && !this.getYearday().equals("")&& !this.getYearday().equals("0")){
			
			if(DBUtil.fromDB(this.getYearday()).equals("1")){
				sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}else if(DBUtil.fromDB(this.getYearday()).equals("2")){
				
				sql.append(" a.bmno=r.bmno and (a.LMFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}
		}else{
		sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS<3 or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
		}
        sql.append(" a.paydate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
        		"' and a.duedate>'"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"' and a.bmtype not in(12,13,14)" 
        		);
    	CachedRowSet child = manager.getRs(sql.toString());
		while (child.next()){
			int a=0;
			int b=0;
			double c=0.00;
			double d=0.00;
			double e=0.00;
			double f=0.00;
			double g=0.00;
			int newLoanSum=Integer.valueOf(child.getString("bmnos")==null?"0.00":child.getString("bmnos")).intValue();
			a+=newLoanSum;
			sum5[0]=a;
			String sumA=String.valueOf(a);
			ff.setNewloansum(sumA);
			int newperson=Integer.valueOf(child.getString("idnos")==null?"0.00":child.getString("idnos")).intValue();
			b+=newperson;
			sum5[1]=b;
			String sumB=String.valueOf(b);
			ff.setNewpersonsum(sumB);
			double bal=Double.valueOf(child.getString("fcbals")==null?"0.00":child.getString("fcbals")).doubleValue()*0.0001;
			c += bal;
			dsum5[0]=c;
			String sumC=DBUtil.doubleToStr1(c).toString();
			ff.setBal(sumC);
			double contamt=Double.valueOf(child.getString("contractamt")==null?"0.00":child.getString("contractamt")).doubleValue()*0.0001;
			d +=contamt;
			dsum5[1]=d;
			String sumD=DBUtil.doubleToStr1(d).toString();
			ff.setLoanbal(sumD);
			double H3=Double.valueOf(child.getString("H3")==null?"0.00":child.getString("H3")).doubleValue()*0.0001;
			e += H3;
			dsum5[2]=e;
			String sumE=DBUtil.doubleToStr1(e).toString();
			ff.setH3(sumE);
			double H4=Double.valueOf(child.getString("H4")==null?"0.00":child.getString("H4")).doubleValue()*0.0001;
			f +=H4;
			dsum5[3]=f;
			String sumF=DBUtil.doubleToStr1(f).toString();
			ff.setH4(sumF);
		   double H5=Double.valueOf(child.getString("H5")==null?"0.00":child.getString("H5")).doubleValue()*0.0001;
			g += H5;
			dsum5[4]=g;
			String sumG=DBUtil.doubleToStr1(g).toString();
			ff.setH5(sumG);
			
		}
      
	}catch(RuntimeException e){
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
	return ff;
}
public FirstnbLoan get3LRs(){//当年发放当年往期到期结果
	FirstnbLoan ff = new FirstnbLoan();
	try{
		String brhid=getBrhid();
		brhid=SCBranch.getSubBranchAll(brhid);
		brhid=brhid.replaceAll(",", "','");
		StringBuffer sql = new StringBuffer(
		           "select count(a.bmno) bmnos,count(distinct(a.idno)) " +
		           "idnos,sum(contractamt) contractamt ," +
		           "sum(a.bal) fcbals,sum(case when a.FCCLASS=3 then bal else 0 end ) h3,");
        sql.append(" sum(case when a.FCCLASS=4 then bal else 0 end ) h4," +
        		"sum(case when a.FCCLASS=5 then bal else 0 end ) h5");
        sql.append(" from fcbmain a,rqloanlist r where  ");
        if ((this.getStartdate() != null && !this.getStartdate().equals(""))&&(this.getEnddate()!= null && !this.getEnddate().equals(""))) 
		{
			sql.append(" paydate between '" + this.getStartdate().trim()+ "' and '" + this.getEnddate().trim() + "' and ");
		}
        if ((this.getStartdate() != null && !this.getStartdate().equals("")) && ((this.getEnddate().equals("") || this.getEnddate()== null ))) 
		{
			
        	sql.append(" paydate >= '" + this.getStartdate().trim()+"' and ");
		}
		if ((this.getStartdate() == null || this.getStartdate().equals("")) && ((this.getEnddate()!= null && !this.getEnddate().equals("")))) 
		{
			sql.append(" paydate <= '" + this.getEnddate().trim()+"' and ");
		}
		if (this.getCreadate() != null && !this.getCreadate().equals("")&& !this.getCreadate().equals("0")) {
			sql.append(" CREATEDATE='" + this.getCreadate().trim()+ "' and  ");
		}
		if (this.getSartbal() != null && !this.getSartbal().equals("")) {
			sql.append(" bal >="
					+ Double.valueOf(this.getSartbal().trim()).doubleValue()
					* (10000) + " and  ");
		}
		if (this.getEndbal() != null && !this.getEndbal().equals("")) {
			sql.append("bal <="+ Double.valueOf(this.getEndbal().trim()).doubleValue()* (10000) + " and  ");
		}
        if (getReason() != null && !getReason().equals("0")&& !getReason().equals("")){
        	sql.append(" a.badreason="+Integer.valueOf(getReason().trim()).intValue()+" and ");
        }
        if (getLoanday() != null && !getLoanday().equals("0")&& !getLoanday().equals("")){
        	sql.append(" r.loantype2="+Integer.valueOf(getLoanday().trim()).intValue()+" and ");
        }
        if (getMethod() != null && !getMethod().equals("0")&& !getMethod().equals("")){
        	sql.append(" a.loantype3="+Integer.valueOf(getMethod().trim()).intValue()+" and ");
        }
        if (getFenlei() != null && !getFenlei().equals("0")&& !getFenlei().equals("")){
        	if(getFenlei().equals("allOne")){
        		sql.append(" a.loanway in ('101','102','103','199') and ");
        	}else if(getFenlei().equals("allTwo")){
        		sql.append(" a.loanway in ('201','202','203','204','205','206','299') and ");
        	}else if(getFenlei().equals("allThree")){
        		sql.append(" a.loanway in ('301','302','303','304','305','399') and ");
        	}else{
        	sql.append(" a.loanway='"+getFenlei().trim()+"' and ");
        	}
        }
        if (getManagers() != null && !getManagers().equals("0")&& !getManagers().equals("")){
        	sql.append(" r.clientmgr='"+getManagers().trim()+"'and ");
        }
      if(this.getYearday() !=null && !this.getYearday().equals("")&& !this.getYearday().equals("0")){
			
			if(DBUtil.fromDB(this.getYearday()).equals("1")){
				sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}else if(DBUtil.fromDB(this.getYearday()).equals("2")){
				
				sql.append(" a.bmno=r.bmno and (a.LMFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}
		}else{
		sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS<3 or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
		}
        sql.append("a.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
        		"' and a.duedate<='"+SystemDate.getLastMonthDate(SystemDate.getSystemDate2(), "-")+"' and a.bmtype not in(12,13,14)" 
        		);
    	CachedRowSet child = manager.getRs(sql.toString());
		while (child.next()){
			int a=0;
			int b=0;
			double c=0.00;
			double d=0.00;
			double e=0.00;
			double f=0.00;
			double g=0.00;
			int newLoanSum=Integer.valueOf(child.getString("bmnos")==null?"0.00":child.getString("bmnos")).intValue();
			a+=newLoanSum;
			sum3[0]=a;
			String sumA=String.valueOf(a);
			ff.setNewloansum(sumA);
			int newperson=Integer.valueOf(child.getString("idnos")==null?"0.00":child.getString("idnos")).intValue();
			b+=newperson;
			sum3[1]=b;
			String sumB=String.valueOf(b);
			ff.setNewpersonsum(sumB);
			double bal=Double.valueOf(child.getString("fcbals")==null?"0.00":child.getString("fcbals")).doubleValue()*0.0001;
			c += bal;
			dsum3[0]=c;
			String sumC=DBUtil.doubleToStr1(c).toString();
			ff.setBal(sumC);
			double contamt=Double.valueOf(child.getString("contractamt")==null?"0.00":child.getString("contractamt")).doubleValue()*0.0001;
			d +=contamt;
			dsum3[1]=d;
			String sumD=DBUtil.doubleToStr1(d).toString();
			ff.setLoanbal(sumD);
			double H3=Double.valueOf(child.getString("H3")==null?"0.00":child.getString("H3")).doubleValue()*0.0001;
			e += H3;
			dsum3[2]=e;
			String sumE=DBUtil.doubleToStr1(e).toString();
		
			ff.setH3(sumE);
			double H4=Double.valueOf(child.getString("H4")==null?"0.00":child.getString("H4")).doubleValue()*0.0001;
			f +=H4;
			dsum3[3]=f;
			String sumF=DBUtil.doubleToStr1(f).toString();
			ff.setH4(sumF);
		   double H5=Double.valueOf(child.getString("H5")==null?"0.00":child.getString("H5")).doubleValue()*0.00001;
			g += H5;
			dsum3[4]=g;
			String sumG=DBUtil.doubleToStr1(g).toString();
			ff.setH5(sumG);
			
		}
      
	}catch(RuntimeException e){
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
	return ff;
	
}
public FirstnbLoan get4LRs(){//当年发放本期到期结果
	FirstnbLoan ff = new FirstnbLoan();
	try{
		String brhid=getBrhid();
		brhid=SCBranch.getSubBranchAll(brhid);
		brhid=brhid.replaceAll(",", "','");
		StringBuffer sql = new StringBuffer(
		           "select count(a.bmno) bmnos,count(distinct(a.idno)) " +
		           "idnos,sum(contractamt) contractamt ," +
		           "sum(a.bal) fcbals,sum(case when a.FCCLASS=3 then bal else 0 end ) h3,");
        sql.append(" sum(case when a.FCCLASS=4 then bal else 0 end ) h4," +
        		"sum(case when a.FCCLASS=5 then bal else 0 end ) h5");
        sql.append(" from fcbmain a,rqloanlist r where  ");
        if ((this.getStartdate() != null && !this.getStartdate().equals(""))&&(this.getEnddate()!= null && !this.getEnddate().equals(""))) 
		{
			sql.append(" paydate between '" + this.getStartdate().trim()+ "' and '" + this.getEnddate().trim() + "' and ");
		}
        if ((this.getStartdate() != null && !this.getStartdate().equals("")) && ((this.getEnddate().equals("") || this.getEnddate()== null ))) 
		{
			
        	sql.append(" paydate >= '" + this.getStartdate().trim()+"' and ");
		}
		if ((this.getStartdate() == null || this.getStartdate().equals("")) && ((this.getEnddate()!= null && !this.getEnddate().equals("")))) 
		{
			sql.append(" paydate <= '" + this.getEnddate().trim()+"' and ");
		}
		if (this.getCreadate() != null && !this.getCreadate().equals("")&& !this.getCreadate().equals("0")) {
			sql.append(" CREATEDATE='" + this.getCreadate().trim()+ "' and  ");
		}
		if (this.getSartbal() != null && !this.getSartbal().equals("")) {
			sql.append(" bal >="
					+ Double.valueOf(this.getSartbal().trim()).doubleValue()
					* (10000) + " and  ");
		}
		if (this.getEndbal() != null && !this.getEndbal().equals("")) {
			sql.append("bal <="+ Double.valueOf(this.getEndbal().trim()).doubleValue()* (10000) + " and  ");
		}
        if (getReason() != null && !getReason().equals("0")&& !getReason().equals("")){
        	sql.append(" a.badreason="+Integer.valueOf(getReason().trim()).intValue()+" and ");
        }
        if (getLoanday() != null && !getLoanday().equals("0")&& !getLoanday().equals("")){
        	sql.append(" r.loantype2="+Integer.valueOf(getLoanday().trim()).intValue()+" and ");
        }
        if (getMethod() != null && !getMethod().equals("0")&& !getMethod().equals("")){
        	sql.append(" a.loantype3="+Integer.valueOf(getMethod().trim()).intValue()+" and ");
        }
        if (getFenlei() != null && !getFenlei().equals("0")&& !getFenlei().equals("")){
        	if(getFenlei().equals("allOne")){
        		sql.append(" a.loanway in ('101','102','103','199') and ");
        	}else if(getFenlei().equals("allTwo")){
        		sql.append(" a.loanway in ('201','202','203','204','205','206','299') and ");
        	}else if(getFenlei().equals("allThree")){
        		sql.append(" a.loanway in ('301','302','303','304','305','399') and ");
        	}else{
        	sql.append(" a.loanway='"+getFenlei().trim()+"' and ");
        	}
        } 
        if (getManagers() != null && !getManagers().equals("0")&& !getManagers().equals("")){
        	sql.append(" r.clientmgr='"+getManagers().trim()+"' and ");
        }
       if(this.getYearday() !=null && !this.getYearday().equals("")&& !this.getYearday().equals("0")){
			
			if(DBUtil.fromDB(this.getYearday()).equals("1")){
				sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}else if(DBUtil.fromDB(this.getYearday()).equals("2")){
				
				sql.append(" a.bmno=r.bmno and (a.LMFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}
		}else{
		sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS<3 or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
		}
        sql.append("a.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
        		"' and a.duedate>'"+SystemDate.getLastMonthDate(SystemDate.getSystemDate2(), "-")+
        		"' and a.duedate<='"+SystemDate.getThisMonthDate(SystemDate.getSystemDate2(), "-")+"' and a.bmtype not in(12,13,14)");
        		
    	CachedRowSet child = manager.getRs(sql.toString());
		while (child.next()){
			int a=0;
			int b=0;
			double c=0.00;
			double d=0.00;
			double e=0.00;
			double f=0.00;
			double g=0.00;
			int newLoanSum=Integer.valueOf(child.getString("bmnos")==null?"0.00":child.getString("bmnos")).intValue();
			a+=newLoanSum;
			sum4[0]=a;
			String sumA=String.valueOf(a);
			ff.setNewloansum(sumA);
			int newperson=Integer.valueOf(child.getString("idnos")==null?"0.00":child.getString("idnos")).intValue();
			b+=newperson;
			sum4[1]=b;
			String sumB=String.valueOf(b);
			ff.setNewpersonsum(sumB);
			double bal=Double.valueOf(child.getString("fcbals")==null?"0.00":child.getString("fcbals")).doubleValue()*0.0001;
			c += bal;
			dsum4[0]=c;
			String sumC=DBUtil.doubleToStr1(c).toString();
			ff.setBal(sumC);
			double contamt=Double.valueOf(child.getString("contractamt")==null?"0.00":child.getString("contractamt")).doubleValue()*0.0001;
			d +=contamt;
			dsum4[1]=d;
			String sumD=DBUtil.doubleToStr1(d).toString();
			ff.setLoanbal(sumD);
			double H3=Double.valueOf(child.getString("H3")==null?"0.00":child.getString("H3")).doubleValue()*0.0001;
			e += H3;
			dsum4[2]=e;
			String sumE=DBUtil.doubleToStr1(e).toString();
		
			ff.setH3(sumE);
			double H4=Double.valueOf(child.getString("H4")==null?"0.00":child.getString("H4")).doubleValue()*0.0001;
			f +=H4;
			dsum4[3]=f;
			String sumF=DBUtil.doubleToStr1(f).toString();
			ff.setH4(sumF);
		   double H5=Double.valueOf(child.getString("H5")==null?"0.00":child.getString("H5")).doubleValue()*0.0001;
			g += H5;
			dsum4[4]=g;
			String sumG=DBUtil.doubleToStr1(g).toString();
			ff.setH5(sumG);
		}
	}catch(RuntimeException e){
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
	return ff;
}
public FirstnbLoan get9LRs(){//当年发放本期后到期
	FirstnbLoan ff = new FirstnbLoan();
	try{
		String brhid=getBrhid();
		brhid=SCBranch.getSubBranchAll(brhid);
		brhid=brhid.replaceAll(",", "','");
		StringBuffer sql = new StringBuffer(
		           "select count(a.bmno) bmnos,count(distinct(a.idno)) " +
		           "idnos,sum(contractamt) contractamt ," +
		           "sum(a.bal) fcbals,sum(case when a.FCCLASS=3 then bal else 0 end ) h3,");
        sql.append(" sum(case when a.FCCLASS=4 then bal else 0 end ) h4," +
        		"sum(case when a.FCCLASS=5 then bal else 0 end ) h5");
        sql.append(" from fcbmain a,rqloanlist r where  ");
        if ((this.getStartdate() != null && !this.getStartdate().equals(""))&&(this.getEnddate()!= null && !this.getEnddate().equals(""))) 
		{
			sql.append(" paydate between '" + this.getStartdate().trim()+ "' and '" + this.getEnddate().trim() + "' and ");
		}
        if ((this.getStartdate() != null && !this.getStartdate().equals("")) && ((this.getEnddate().equals("") || this.getEnddate()== null ))) 
		{
			
        	sql.append(" paydate >= '" + this.getStartdate().trim()+"' and ");
		}
		if ((this.getStartdate() == null || this.getStartdate().equals("")) && ((this.getEnddate()!= null && !this.getEnddate().equals("")))) 
		{
			sql.append(" paydate <= '" + this.getEnddate().trim()+"' and ");
		}
		if (this.getCreadate() != null && !this.getCreadate().equals("")&& !this.getCreadate().equals("0")) {
			sql.append(" CREATEDATE='" + this.getCreadate().trim()+ "' and  ");
		}
		if (this.getSartbal() != null && !this.getSartbal().equals("")) {
			sql.append(" bal >="
					+ Double.valueOf(this.getSartbal().trim()).doubleValue()
					* (10000) + " and  ");
		}
		if (this.getEndbal() != null && !this.getEndbal().equals("")) {
			sql.append("bal <="+ Double.valueOf(this.getEndbal().trim()).doubleValue()* (10000) + " and  ");
		}
        if (getReason() != null && !getReason().equals("0")&& !getReason().equals("")){
        	sql.append(" a.badreason="+Integer.valueOf(getReason().trim()).intValue()+" and ");
        }
        if (getLoanday() != null && !getLoanday().equals("0")&& !getLoanday().equals("")){
        	sql.append(" r.loantype2="+Integer.valueOf(getLoanday().trim()).intValue()+" and ");
        }
        if (getMethod() != null && !getMethod().equals("0")&& !getMethod().equals("")){
        	sql.append(" a.loantype3="+Integer.valueOf(getMethod().trim()).intValue()+" and ");
        }
        if (getFenlei() != null && !getFenlei().equals("0")&& !getFenlei().equals("")){
        	if(getFenlei().equals("allOne")){
        		sql.append(" a.loanway in ('101','102','103','199') and ");
        	}else if(getFenlei().equals("allTwo")){
        		sql.append(" a.loanway in ('201','202','203','204','205','206','299') and ");
        	}else if(getFenlei().equals("allThree")){
        		sql.append(" a.loanway in ('301','302','303','304','305','399') and ");
        	}else{
        	sql.append(" a.loanway='"+getFenlei().trim()+"' and ");
        	}
        } 
        if (getManagers() != null && !getManagers().equals("0")&& !getManagers().equals("")){
        	sql.append(" r.clientmgr='"+getManagers().trim()+"' and ");
        }
       if(this.getYearday() !=null && !this.getYearday().equals("")&& !this.getYearday().equals("0")){
			
			if(DBUtil.fromDB(this.getYearday()).equals("1")){
				sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}else if(DBUtil.fromDB(this.getYearday()).equals("2")){
				
				sql.append(" a.bmno=r.bmno and (a.LMFCCLASS<3  or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
			}
		}else{
		sql.append(" a.bmno=r.bmno and (a.LYFCCLASS<3  or a.LMFCCLASS<3 or a.LMFCCLASS=0) and a.FCCLASS between 3 and 5 and a.brhid  in ('"+brhid+"') and ");
		}
        sql.append("a.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
        		"' and a.duedate>'"+SystemDate.getThisMonthDate(SystemDate.getSystemDate2(), "-")+"' and a.bmtype not in(12,13,14)");
        		
    	CachedRowSet child = manager.getRs(sql.toString());
		while (child.next()){
			int a=0;
			int b=0;
			double c=0.00;
			double d=0.00;
			double e=0.00;
			double f=0.00;
			double g=0.00;
			int newLoanSum=Integer.valueOf(child.getString("bmnos")==null?"0.00":child.getString("bmnos")).intValue();
			a+=newLoanSum;
			sum6[0]=a;
			String sumA=String.valueOf(a);
			ff.setNewloansum(sumA);
			int newperson=Integer.valueOf(child.getString("idnos")==null?"0.00":child.getString("idnos")).intValue();
			b+=newperson;
			sum6[1]=b;
			String sumB=String.valueOf(b);
			ff.setNewpersonsum(sumB);
			double bal=Double.valueOf(child.getString("fcbals")==null?"0.00":child.getString("fcbals")).doubleValue()*0.0001;
			c += bal;
			dsum6[0]=c;
			String sumC=DBUtil.doubleToStr1(c).toString();
			ff.setBal(sumC);
			double contamt=Double.valueOf(child.getString("contractamt")==null?"0.00":child.getString("contractamt")).doubleValue()*0.0001;
			d +=contamt;
			dsum6[1]=d;
			String sumD=DBUtil.doubleToStr1(d).toString();
			ff.setLoanbal(sumD);
			double H3=Double.valueOf(child.getString("H3")==null?"0.00":child.getString("H3")).doubleValue()*0.0001;
			e += H3;
			dsum6[2]=e;
			String sumE=DBUtil.doubleToStr1(e).toString();
		
			ff.setH3(sumE);
			double H4=Double.valueOf(child.getString("H4")==null?"0.00":child.getString("H4")).doubleValue()*0.0001;
			f +=H4;
			dsum6[3]=f;
			String sumF=DBUtil.doubleToStr1(f).toString();
			ff.setH4(sumF);
		   double H5=Double.valueOf(child.getString("H5")==null?"0.00":child.getString("H5")).doubleValue()*0.0001;
			g += H5;
			dsum6[4]=g;
			String sumG=DBUtil.doubleToStr1(g).toString();
			ff.setH5(sumG);
		}
	}catch(RuntimeException e){
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
	return ff;
}
public FirstnbLoan get5LRs(){//往年合计结果
	FirstnbLoan ff = new FirstnbLoan();
	int a1=sum1[0]+sum2[0]+sum5[0];
	int a2=sum1[1]+sum2[1]+sum5[1];
	double a3=dsum1[0]+dsum2[0]+dsum5[0];
	double a4=dsum1[1]+dsum2[1]+dsum5[1];
	double a5=dsum1[2]+dsum2[2]+dsum5[2];
	double a6=dsum1[3]+dsum2[3]+dsum5[3];
	double a7=dsum1[4]+dsum2[4]+dsum5[4];
	String sumA=String.valueOf(a1);
	String sumB=String.valueOf(a2);
	String sumC=DBUtil.doubleToStr1(a3).toString();
	String sumD=DBUtil.doubleToStr1(a4).toString();
	String sumE=DBUtil.doubleToStr1(a5).toString();
	String sumF=DBUtil.doubleToStr1(a6).toString();
	String sumG=DBUtil.doubleToStr1(a7).toString();
	ff.setNewloansum(sumA);
	ff.setNewpersonsum(sumB);
	ff.setBal(sumC);
	ff.setLoanbal(sumD);
	ff.setH3(sumE);
	ff.setH4(sumF);
	ff.setH5(sumG);
	return ff;
}
public FirstnbLoan get6LRs(){//当年合计结果
	FirstnbLoan ff = new FirstnbLoan();
	int a1=sum3[0]+sum4[0]+sum6[0];
	int a2=sum3[1]+sum4[1]+sum6[1];
	double a3=dsum3[0]+dsum4[0]+dsum6[0];
	double a4=dsum3[1]+dsum4[1]+dsum6[1];
	double a5=dsum3[2]+dsum4[2]+dsum6[2];
	double a6=dsum3[3]+dsum4[3]+dsum6[3];
	double a7=dsum3[4]+dsum4[4]+dsum6[4];
	String sumA=String.valueOf(a1);
	String sumB=String.valueOf(a2);
	String sumC=DBUtil.doubleToStr1(a3).toString();
	String sumD=DBUtil.doubleToStr1(a4).toString();
	String sumE=DBUtil.doubleToStr1(a5).toString();
	String sumF=DBUtil.doubleToStr1(a6).toString();
	String sumG=DBUtil.doubleToStr1(a7).toString();
	ff.setNewloansum(sumA);
	ff.setNewpersonsum(sumB);
	ff.setBal(sumC);
	ff.setLoanbal(sumD);
	ff.setH3(sumE);
	ff.setH4(sumF);
	ff.setH5(sumG);
	return ff;
}
public FirstnbLoan get7LRs(){//总结果
	FirstnbLoan ff = new FirstnbLoan();
	int a1=sum1[0]+sum2[0]+sum3[0]+sum4[0]+sum5[0]+sum6[0];
	int a2=sum1[1]+sum2[1]+sum3[1]+sum4[1]+sum5[1]+sum6[1];
	double a3=dsum1[0]+dsum2[0]+dsum3[0]+dsum4[0]+dsum5[0]+dsum6[0];
	double a4=dsum1[1]+dsum2[1]+dsum3[1]+dsum4[1]+dsum5[1]+dsum6[1];
	double a5=dsum1[2]+dsum2[2]+dsum3[2]+dsum4[2]+dsum5[2]+dsum6[2];
	double a6=dsum1[3]+dsum2[3]+dsum3[3]+dsum4[3]+dsum5[3]+dsum6[3];
	double a7=dsum1[4]+dsum2[4]+dsum3[4]+dsum4[4]+dsum5[4]+dsum6[4];
	String sumA=String.valueOf(a1);
	String sumB=String.valueOf(a2);
	String sumC=DBUtil.doubleToStr1(a3).toString();
	String sumD=DBUtil.doubleToStr1(a4).toString();
	String sumE=DBUtil.doubleToStr1(a5).toString();
	String sumF=DBUtil.doubleToStr1(a6).toString();
	String sumG=DBUtil.doubleToStr1(a7).toString();
	ff.setNewloansum(sumA);
	ff.setNewpersonsum(sumB);
	ff.setBal(sumC);
	ff.setLoanbal(sumD);
	ff.setH3(sumE);
	ff.setH4(sumF);
	ff.setH5(sumG);
	return ff;
}

}
