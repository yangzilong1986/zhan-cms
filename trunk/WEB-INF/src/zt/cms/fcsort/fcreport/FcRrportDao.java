package zt.cms.fcsort.fcreport;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import com.ming.webreport.DataRecord;
import com.ming.webreport.MRDataSet;

import zt.cms.pub.SCBranch;
import zt.platform.cachedb.ConnectionManager;

public class FcRrportDao {

	/**
	 * 得到五级分类统计表的结果 
	 * @return MRDataSet
	 * @throws Exception 
	 */
	public String  getF08010SQl(String brhid,String date) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" with FC as"); 
				sql.append(" (select s.chgid brhid,dt,dim1,dim2,sum(AMT1) AMT1,sum(AMT2) AMT2   from scbranch s,fcdata F0 ");  
				sql.append(" where s.bnkid = '"+brhid+"' and s.brhid=F0.brhid and F0.ftype=1 and F0.dt='"+date+"' group by s.chgid,dt,dim1,dim2),");
				sql.append(" WC as ");
				sql.append(" (select s.chgid brhid,dt,dim1,dim2,AMT3,sum(AMT1) AMT1,sum(AMT2) AMT2 from scbranch s,fcdata F0 ");
				sql.append(" where s.bnkid = '"+brhid+"' and s.brhid=F0.brhid and F0.ftype=23 and F0.dt='"+date+"' group by s.chgid,dt,dim1,dim2,AMT3),");
				sql.append(" ZR as ");
				sql.append(" (select s.chgid brhid,dt,dim1,dim2,AMT3,sum(AMT1) AMT1,sum(AMT2) AMT2 from scbranch s,fcdata F0");
				sql.append(" where s.bnkid = '"+brhid+"' and s.brhid=F0.brhid and F0.ftype=23 and F0.dt='"+date+"' group by s.chgid,dt,dim1,dim2,AMT3),");
				sql.append(" ZT as ");
				sql.append(" (select s.chgid brhid,dt,dim1,dim2,AMT3,sum(AMT1) AMT1,sum(AMT2) AMT2 ");
				sql.append(" from scbranch s,fcdata F0");
				sql.append(" where s.bnkid = '"+brhid+"' and s.brhid=F0.brhid and F0.ftype=23 and F0.dt='"+date+"' group by s.chgid,dt,dim1,dim2,AMT3),");
				sql.append(" F as (select distinct brhid from FC)  ");
				sql.append(" select brhid, ");
				sql.append(" (select sum(AMT1) from FC where brhid=F.brhid and dim1 in(1,2,3,4,5) ) FCBAL, ");
				sql.append(" (select sum(AMT2) from FC where brhid=F.brhid and dim1 in(1,2,3,4,5) ) FCSUM,");  
				sql.append(" (select AMT1 from FC where brhid=F.brhid and dim1=1) F01, "); 
				sql.append(" (select AMT1 from FC where brhid=F.brhid and dim1=2) F02, "); 
				sql.append(" (select AMT1 from FC where brhid=F.brhid and dim1=3) F03,  ");
				sql.append(" (select AMT1 from FC where brhid=F.brhid and dim1=2) F04, ");
				sql.append(" (select AMT1 from FC where brhid=F.brhid and dim1=3) F05,");
				sql.append(" (select sum(AMT1) from WC where brhid=F.brhid and amt3 in(3,4) ) WCBAL,");
				sql.append(" (select sum(AMT2) from WC where brhid=F.brhid and amt3 in(3,4) ) WCSUM,");
				sql.append(" (select sum(AMT1) from WC where brhid=F.brhid and amt3 in(3,4) and dim1=1) W01,");
				sql.append(" (select sum(AMT1) from WC where brhid=F.brhid and amt3 in(3,4) and dim1=2) W02,");
				sql.append(" (select sum(AMT1) from WC where brhid=F.brhid and amt3 in(3,4) and dim1=3) W03,");
				sql.append(" (select sum(AMT1) from WC where brhid=F.brhid and amt3 in(3,4) and dim1=4) W04,");
				sql.append(" (select sum(AMT1) from WC where brhid=F.brhid and amt3 in(3,4) and dim1=5) W05,");
				sql.append(" (select sum(AMT1) from ZR where brhid=F.brhid and amt3=1 ) ZRBAL,");
				sql.append(" (select sum(AMT2) from ZR where brhid=F.brhid and amt3=1 ) ZRSUM,");
				sql.append(" (select sum(AMT1) from ZR where brhid=F.brhid and amt3=1 and dim1=1) ZR1,");
				sql.append(" (select sum(AMT1) from ZR where brhid=F.brhid and amt3=1 and dim1=2) ZR2,");
				sql.append(" (select sum(AMT1) from ZR where brhid=F.brhid and amt3=1 and dim1=3) ZR3,");
				sql.append(" (select sum(AMT1) from ZR where brhid=F.brhid and amt3=1 and dim1=4) ZR4,");
				sql.append(" (select sum(AMT1) from ZR where brhid=F.brhid and amt3=1 and dim1=5) ZR5,");
				sql.append(" (select sum(AMT1) from ZT where brhid=F.brhid and amt3=2 ) ZTBAL,");
				sql.append(" (select sum(AMT2) from ZT where brhid=F.brhid and amt3=2 ) ZTSUM,");
				sql.append(" (select sum(AMT1) from ZT where brhid=F.brhid and amt3=2 and dim1=1) ZT01,");
				sql.append(" (select sum(AMT1) from ZT where brhid=F.brhid and amt3=2 and dim1=2) ZT02,");
				sql.append(" (select sum(AMT1) from ZT where brhid=F.brhid and amt3=2 and dim1=3) ZT03,");
				sql.append(" (select sum(AMT1) from ZT where brhid=F.brhid and amt3=2 and dim1=4) ZT04,");
				sql.append(" (select sum(AMT1) from ZT where brhid=F.brhid and amt3=2 and dim1=5) ZT05 ");
				sql.append(" from  F ");

		return sql.toString();
	}
	
	public  MRDataSet getF08010Data(String brhid,String date)
	{
		 MRDataSet mrds = new MRDataSet();
		 try {
			
			 ConnectionManager manager = ConnectionManager.getInstance();
			 CachedRowSet crs =manager.getRs(this.getF08010SQl(brhid, date));
			 ResultSetMetaData rsmdBranch = crs.getMetaData();//得到表的数据结构
			 int count = rsmdBranch.getColumnCount();//得到列数
			 while (crs.next()) {
				DataRecord rec = new DataRecord();
				for(int i=1;i<=count;i++)
				{
					if(i==1)
					{
						rec.setValue("SNAME",SCBranch.getSName(crs.getString(1).trim()).trim());
						continue;
					}
					rec.setDouble(rsmdBranch.getColumnName(i), crs.getDouble(rsmdBranch.getColumnName(i))/1000);
				}
				mrds.addRow(rec);
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		 return mrds;
	}
}
