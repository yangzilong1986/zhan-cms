package zt.cms.fcsort.fcnbloan;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DBUtil;
import zt.cms.pub.SCBranch;
/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类新增不良资产查询页面逻辑类
 * <p/>===============================================
 * <p/>Description: 处理业务逻辑。
 * 
 * @version $Revision: 1.3 $ $Date: 2007/05/28 09:55:47 $
 * @author houcs <p/>修改：$Author: houcs $
 */

public class FcnblTPageobject  extends AbstractPage{
 


	public FcnblTPageobject(HttpServletRequest request) {
		super(request);
		// TODO 自动生成构造函数存根
	}
	/**
	 * 得到记录数
	 */
	public void setPagecount() {
		int sum =0;
		try {
			String brhids=getBrhid();
			brhids=SCBranch.getSubBranchAll(brhids);
			brhids=brhids.replaceAll(",", "','");
			 StringBuffer sql=new StringBuffer("");
				sql.append("select rownumber() over() rn,");
			 sql.append(" rtrim(f.clientname),f.bmno,r.CONTRACTAMT,f.bal,r.crtrate,f.paydate,f.duedate,s.username firstPerson," +
				        "(select enudt from PTENUMINFODETL where enuid='LoanCat3' and enutp=(select LOANCAT3 from RQLOANLIST r where r.bmno=f.bmno)) loanyt," +
				     	"r.PERIMON loanqx," +
				     	"(select enudt from PTENUMINFODETL where enuid='LoanCat2' and enutp=(select max(loancat2) from fcbmain fc where f.bmno=fc.bmno)) fourS," +
				     	"(select enudt from PTENUMINFODETL where enuid='BadReason' and enutp=(select max(badreason) from fcbmain fc where f.bmno=fc.bmno)) badReason," +
				        "(select enudt from PTENUMINFODETL where enuid='LoanCat1' and enutp=(select max(FCCLASS) from fcbmain fc where f.bmno=fc.bmno)) fiveS" +
				        " from fcbmain f,RQLOANLIST r,SCUSER s where " 
				        );   	
						if(this.getStartdate()!=null&&!this.getStartdate().equals("")&&this.getEnddate()!=null&&!this.getEnddate().equals(""))
						{
							sql.append(" f.duedate between '"+this.getStartdate().trim()+"' and '"+this.getEnddate().trim()+"' and ");
						}
						if(this.getCreadate()!=null&&!this.getCreadate().equals("")&&!this.getCreadate().equals("0"))
						{
							sql.append("f.CREATEDATE='"+this.getCreadate().trim()+"' and  ");
						}
						 if ((this.getStartdate() != null && !this.getStartdate().equals("")) && ((this.getEnddate().equals("") || this.getEnddate()== null ))) 
							{
								
					        	sql.append(" duedate >= '" + this.getStartdate().trim()+"' and ");
							}
							if ((this.getStartdate() == null || this.getStartdate().equals("")) && ((this.getEnddate()!= null && !this.getEnddate().equals("")))) 
							{
								sql.append(" duedate <= '" + this.getEnddate().trim()+"' and ");
							}
						if(this.getSartbal()!=null&&!this.getSartbal().equals(""))
						{
							sql.append("f.bal >="+Double.valueOf(this.getSartbal().trim()).doubleValue()*(10000)+" and  ");
						}
						if(this.getEndbal()!=null&&!this.getEndbal().equals(""))
						{
							sql.append("bal <="+Double.valueOf(this.getEndbal().trim()).doubleValue()*(10000)+" and  ");

						}
						 if (getReason() != null && !getReason().equals("0")&& !getReason().equals("")){
			        	sql.append(" f.badreason="+Integer.valueOf(getReason().trim()).intValue()+" and ");
			        }
			        if (getLoanday() != null && !getLoanday().equals("0")&& !getLoanday().equals("")){
			        	sql.append(" r.loantype2="+Integer.valueOf(getLoanday().trim()).intValue()+" and ");
			        }
			        if (getMethod() != null && !getMethod().equals("0")&& !getMethod().equals("")){
			        	sql.append(" f.loantype3="+Integer.valueOf(getMethod().trim()).intValue()+" and ");
			        }
			        if (getFenlei() != null && !getFenlei().equals("0") &&!getFenlei().equals("")){
			        	sql.append(" f.loanway='"+getFenlei().trim()+"' and ");
			        }
			        if (getManagers() != null && !getManagers().equals("0")&& !getManagers().equals("")){
			        	sql.append(" r.clientmgr='"+getManagers().trim()+"'and ");
			        }
					if (getFirstPerson() != null && !getFirstPerson().equals("0")&& !getFirstPerson().equals("")){
			        	sql.append(" f.FIRSTRESP ='"+getFirstPerson().trim()+"'and ");
			        }
						if(getLoanFiled()!=null && !getLoanFiled().equals("")&& !getLoanFiled().equals("0")){
							
							if(getLoanFiled().equals("1")){
								sql.append(" f.duedate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+"' and ");
							}else if(getLoanFiled().equals("2")){
								 sql.append(" f.paydate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
							        		"' and duedate > '"+SystemDate.getLastYearDate(SystemDate.getSystemDate2(),"-")+"'" +
							        		" and f.duedate<='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"' and " 
							        		);
							}else if(getLoanFiled().equals("3")){
								 sql.append("f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
							        		"' and f.duedate<='"+SystemDate.getLastMonthDate(SystemDate.getSystemDate2(), "-")+"' and " 
							        		);
							}else if(getLoanFiled().equals("4")){
								sql.append("f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
						        		"' and f.duedate<='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"' and " 
						        		);
							}else if(getLoanFiled().equals("5")){
								sql.append(" f.paydate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
						        		"' and f.duedate>='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"' and" 
						        		);
							}else if(getLoanFiled().equals("6")){
								sql.append("f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
						        		"' and f.duedate>='"+SystemDate.getThisMonthDate(SystemDate.getSystemDate2(), "-")+"' and");
							}else if(getLoanFiled().equals("8")){
								sql.append(" ((f.duedate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+"') or");
								sql.append(" (f.paydate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
							        		"' and duedate > '"+SystemDate.getLastYearDate(SystemDate.getSystemDate2(),"-")+"'" +
							        		" and f.duedate<='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"') or " 
							        		);
								sql.append(" (f.paydate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
						        		"' and f.duedate>='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"')) and" 
						        		);
							}else if(getLoanFiled().equals("9")){
								sql.append(" ((f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
						        		"' and f.duedate<='"+SystemDate.getLastMonthDate(SystemDate.getSystemDate2(), "-")+"') or " 
						        		);
								sql.append(" (f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
						        		"' and f.duedate<='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"') or " 
						        		);
								sql.append(" (f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
						        		"' and f.duedate>='"+SystemDate.getThisMonthDate(SystemDate.getSystemDate2(), "-")+"')) and");
							}
						}
						if(this.getYearday() !=null && !this.getYearday().equals("")&& !this.getYearday().equals("0")){
							
							if(DBUtil.fromDB(this.getYearday()).equals("1")){
								sql.append(" (f.LYFCCLASS<3  or f.LMFCCLASS=0) and f.FCCLASS between 3 and 5 and ");
							}else if(DBUtil.fromDB(this.getYearday()).equals("2")){
								
								sql.append(" (f.LMFCCLASS<3  or f.LMFCCLASS=0) and f.FCCLASS between 3 and 5 and ");
							}
						}else{
						sql.append(" (f.LYFCCLASS<3  or f.LMFCCLASS<3 or f.LMFCCLASS=0) and f.FCCLASS between 3 and 5 and ");
						}
						sql.append(" s.loginname=f.firstresp and f.brhid=r.brhid and f.brhid in ('"+brhids+"') and r.bmno=f.bmno and f.bmtype not in(12,13,14)");
					
						CachedRowSet crs =  manager.getRs(sql.toString());
			          sum=crs.size();
			        
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		this.setPagecount(sum) ;
	}
	/**
	 * 得到列表数据
	 */
	public List getListvp(){
		List it = new ArrayList();
	    try {
	    	String brhids=getBrhid();
	    	brhids=SCBranch.getSubBranchAll(brhids);
			brhids=brhids.replaceAll(",", "','");
	    	StringBuffer sql=new StringBuffer("");
			 sql.append("select * from( ");
				sql.append("select rownumber() over() rn,");
	       sql.append(" f.clientname,f.bmno,r.CONTRACTAMT,f.bal,r.crtrate,f.paydate,f.duedate,s.username firstPerson," +
	        "(select enudt from PTENUMINFODETL where enuid='LoanCat3' and enutp=(select LOANCAT3 from RQLOANLIST r where r.bmno=f.bmno)) loanyt," +
	     	"r.PERIMON loanqx," +
	     	"(select enudt from PTENUMINFODETL where enuid='LoanCat2' and enutp=(select max(loancat2) from fcbmain fc where f.bmno=fc.bmno)) fourS," +
	     	 "(select enudt from PTENUMINFODETL where enuid='BadReason' and enutp=(select max(badreason) from fcbmain fc where f.bmno=fc.bmno)) badReason," +
	        "(select enudt from PTENUMINFODETL where enuid='LoanCat1' and enutp=(select max(FCCLASS) from fcbmain fc where f.bmno=fc.bmno)) fiveS" +
	        " from fcbmain f,RQLOANLIST r,SCUSER s where " 
	        );   	
			if(this.getStartdate()!=null&&!this.getStartdate().equals("")&&this.getEnddate()!=null&&!this.getEnddate().equals(""))
			{
				sql.append(" f.duedate between '"+this.getStartdate().trim()+"' and '"+this.getEnddate().trim()+"' and ");
			}
			 if ((this.getStartdate() != null && !this.getStartdate().equals("")) && ((this.getEnddate().equals("") || this.getEnddate()== null ))) 
				{
					
		        	sql.append(" duedate >= '" + this.getStartdate().trim()+"' and ");
				}
				if ((this.getStartdate() == null || this.getStartdate().equals("")) && ((this.getEnddate()!= null && !this.getEnddate().equals("")))) 
				{
					sql.append(" duedate <= '" + this.getEnddate().trim()+"' and ");
				}
			if(this.getCreadate()!=null&&!this.getCreadate().equals("")&&!this.getCreadate().equals("0"))
			{
				sql.append("f.CREATEDATE='"+this.getCreadate().trim()+"' and  ");
			}
			if(this.getSartbal()!=null&&!this.getSartbal().equals(""))
			{
				sql.append("f.bal >="+Double.valueOf(this.getSartbal().trim()).doubleValue()*(10000)+" and  ");
			}
			if(this.getEndbal()!=null&&!this.getEndbal().equals(""))
			{
				sql.append("bal <="+Double.valueOf(this.getEndbal().trim()).doubleValue()*(10000)+" and  ");

			}
			 if (getReason() != null && !getReason().equals("0")&& !getReason().equals("")){
        	sql.append(" f.badreason="+Integer.valueOf(getReason().trim()).intValue()+" and ");
        }
        if (getLoanday() != null && !getLoanday().equals("0")&& !getLoanday().equals("")){
        	sql.append(" r.loantype2="+Integer.valueOf(getLoanday().trim()).intValue()+" and ");
        }
        if (getMethod() != null && !getMethod().equals("0")&& !getMethod().equals("")){
        	sql.append(" f.loantype3="+Integer.valueOf(getMethod().trim()).intValue()+" and ");
        }
        if (getFenlei() != null && !getFenlei().equals("0") &&!getFenlei().equals("")){
        	sql.append(" f.loanway="+Integer.valueOf(getFenlei().trim()).intValue()+" and ");
        }
        if (getManagers() != null && !getManagers().equals("0")&& !getManagers().equals("")){
        	sql.append(" r.clientmgr='"+getManagers().trim()+"'and ");
        }
		if (getFirstPerson() != null && !getFirstPerson().equals("0")&& !getFirstPerson().equals("")){
        	sql.append(" f.FIRSTRESP ='"+getFirstPerson().trim()+"'and ");
        }
			if(getLoanFiled()!=null && !getLoanFiled().equals("")&& !getLoanFiled().equals("0")){
				
				if(getLoanFiled().equals("1")){
					sql.append(" f.duedate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+"' and ");
				}else if(getLoanFiled().equals("2")){
					 sql.append(" f.paydate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
				        		"' and duedate > '"+SystemDate.getLastYearDate(SystemDate.getSystemDate2(),"-")+"'" +
				        		" and f.duedate<='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"' and " 
				        		);
				}else if(getLoanFiled().equals("3")){
					 sql.append("f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
				        		"' and f.duedate<='"+SystemDate.getLastMonthDate(SystemDate.getSystemDate2(), "-")+"' and " 
				        		);
				}else if(getLoanFiled().equals("4")){
					sql.append("f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
			        		"' and f.duedate<='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"' and " 
			        		);
				}else if(getLoanFiled().equals("5")){
					sql.append(" f.paydate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
			        		"' and f.duedate>='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"' and" 
			        		);
				}else if(getLoanFiled().equals("6")){
					sql.append(" f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
			        		"' and f.duedate>='"+SystemDate.getThisMonthDate(SystemDate.getSystemDate2(), "-")+"' and");
			        		
				}else if(getLoanFiled().equals("8")){
					sql.append(" ((f.duedate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+"') or");
					sql.append(" (f.paydate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
				        		"' and duedate > '"+SystemDate.getLastYearDate(SystemDate.getSystemDate2(),"-")+"'" +
				        		" and f.duedate<='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"') or " 
				        		);
					sql.append(" (f.paydate<='" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
			        		"' and f.duedate>='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"')) and" 
			        		);
				}else if(getLoanFiled().equals("9")){
					sql.append(" ((f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
			        		"' and f.duedate<='"+SystemDate.getLastMonthDate(SystemDate.getSystemDate2(), "-")+"') or " 
			        		);
					sql.append(" (f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
			        		"' and f.duedate<='"+SystemDate.getThisYearDate(SystemDate.getSystemDate2(), "-")+"') or " 
			        		);
					sql.append(" (f.paydate>'" +SystemDate.getLastYearDate(SystemDate.getSystemDate2(), "-")+
			        		"' and f.duedate>='"+SystemDate.getThisMonthDate(SystemDate.getSystemDate2(), "-")+"')) and");
				}
			}
			if(this.getYearday() !=null && !this.getYearday().equals("")&& !this.getYearday().equals("0")){
				
				if(DBUtil.fromDB(this.getYearday()).equals("1")){
					sql.append(" (f.LYFCCLASS<3  or f.LMFCCLASS=0) and f.FCCLASS between 3 and 5 and ");
				}else if(DBUtil.fromDB(this.getYearday()).equals("2")){
					
					sql.append(" (f.LMFCCLASS<3  or f.LMFCCLASS=0) and f.FCCLASS between 3 and 5 and ");
				}
			}else{
			sql.append(" (f.LYFCCLASS<3  or f.LMFCCLASS<3 or f.LMFCCLASS=0) and f.FCCLASS between 3 and 5 and ");
			}
			
			sql.append(" s.loginname=f.firstresp and f.brhid=r.brhid and f.brhid in ('"+brhids+"') and r.bmno=f.bmno and f.bmtype not in(12,13,14)");
			sql.append(" ) AS A1 WHERE A1.rn BETWEEN " + this.getStartrow()+" AND "+ this.getEndrow()+" ");
			CachedRowSet crs =  manager.getRs(sql.toString());
			
			while (crs.next()) {
				FcnbloanT ft= new FcnbloanT();
				ft.setBmno(crs.getString("bmno"));
				ft.setClientname(DBUtil.fromDB(crs.getString("clientname")).trim());
				ft.setLoanyt(DBUtil.fromDB(crs.getString("loanyt")));
				double loanbal=Double.valueOf(crs.getString("CONTRACTAMT")).doubleValue();
				String loanbal1=DBUtil.doubleToStr1(loanbal);
				String loanbal2=DBUtil.doubleToStr(loanbal);
				ft.setLoanbal(loanbal1);
				ft.setLoanbal1(loanbal2);
				double bal=Double.valueOf(crs.getString("bal")).doubleValue();
				String bal1=DBUtil.doubleToStr1(bal);
				String bal2=DBUtil.doubleToStr(bal);
				ft.setBal(bal1);
				ft.setBal1(bal2);
				
				ft.setEnudate(DBUtil.fromDB(crs.getString("loanqx")));
				String crtrate=crs.getString("crtrate");
				int j=crtrate.lastIndexOf("0");
				String crt=crtrate.substring(0, j);
				ft.setPerimon(crt);
				ft.setPadate(crs.getString("paydate"));
				ft.setDuedate(crs.getString("duedate"));
				ft.setFourS(DBUtil.fromDB(crs.getString("FourS")));
				ft.setFiveS(DBUtil.fromDB(crs.getString("fiveS")));
				ft.setBadReason(DBUtil.fromDB(crs.getString("badReason")));
				ft.setFirstPerson(DBUtil.fromDB(crs.getString("firstPerson")));
				it.add(ft);
			}
			
		} catch (RuntimeException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return it;
	}

	

}
