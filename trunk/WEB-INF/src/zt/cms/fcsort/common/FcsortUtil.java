package zt.cms.fcsort.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.sql.rowset.CachedRowSet;
import zt.platform.db.DBUtil;
import zt.platform.cachedb.ConnectionManager;

/**
 * <p/>=============================================== 
 * <p/>Title:�弶���಻���ʲ�ҳ���ѯ���������� 
 * <p/>===============================================
 * <p/>Description: ��
 * 
 * @version $Revision: 1.5 $ $Date: 2007/05/28 14:39:06 $
 * @author zhengxin <p/>�޸ģ�$Author: zhengx $
 */

public class FcsortUtil {
	ConnectionManager manager;

	CachedRowSet crs;

	public FcsortUtil() {
		manager = ConnectionManager.getInstance();
	}

	/**
	 * �������㼯��
	 * 
	 * @param strs
	 * @return String
	 */
	public String formatBrhids(String strs) {
		String str = "";
		String h[] = strs.split(",");
		for (int i = 0; i < h.length; i++) {
			if (i < h.length - 1) {
				str += h[i] + "','";
			} else {
				str += h[i];
			}

		}

		if (str == null || str.equals("")) {
			str = "('" + strs + "')";
		} else {
			str = "('" + str + "')";
		}
		// System.out.println(strs+"="+str);
		return str;

	}

	/**
	 * �õ��ͻ�����
	 * 
	 * @throws Exception
	 * @return String
	 */
	public String getClientName(String loginname) throws Exception {
		String str = "�ƶ��ͻ�";

		crs = manager.getRs("select USERNAME from SCUSER where LOGINNAME='"
				+ (loginname == null ? "00000" : loginname) + "'");
		while (crs.next()) {
			str = DBUtil.fromDB(crs.getString("USERNAME"));
		}
		return str;

	}

	/**
	 * ��������
	 * 
	 * @param i
	 * @return String
	 */
	public static String getMonthLastDay(int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String m1 = df.format(calendar.getTime());
		switch (i) {
		case 1:
			return m1;
		default:
			return "2008-08-08";
		}

	}

	/**
	 * �õ����ʱ���html����
	 * 
	 * @param id
	 * @param name
	 * @param selectd
	 * @return String
	 * @throws Exception
	 */
	public String setcreadate(String id, String name, String selectd)
			throws Exception {
		StringBuffer str = new StringBuffer();
		String sql = "select DT from FCPRD  where INITIALIZED=1 order by SEQNO desc ";
		crs = manager.getRs(sql);
		str.append("<select id='" + id + "' name='" + name + "'>");
		while (crs.next()) {
			str.append("<option value='" + crs.getString(1) + "' ");
			if (selectd != null && crs.getString(1) != null
					&& selectd.equals(crs.getString(1).trim())) {
				str.append(" selected='selected' ");
			}

			str.append(">" + crs.getString(1));

			str.append("</option>");
		}
		str.append("</select>");
		return str.toString();
	}

	/**
	 * �õ��ϴε����ʱ��
	 * 
	 * @param id
	 * @param name
	 * @param selectd
	 * @return String
	 * @throws Exception
	 */
	public String getDT() throws Exception {
		String sql = "select DT from FCPRD  where INITIALIZED=1 order by SEQNO desc ";
		crs = manager.getRs(sql);
		while (crs.next()) {
			return crs.getString(1);
		}
		return "2007-04-14";
	}

	/**
	 * �õ����㼶��
	 * 
	 * @param id
	 * @param name
	 * @param selectd
	 * @return String
	 * @throws Exception
	 */
	public String getBrhlevel(String brhid) throws Exception {
		String sql = "select  BRHLEVEL from SCBRANCH where brhid='" + brhid
				+ "'";
		crs = manager.getRs(sql);
		while (crs.next()) {
			return crs.getString(1);
		}
		return "10";
	}

	/**
	 * �õ�ʵ��������ȫ���ͻ�����
	 * 
	 * @param id
	 * @param name
	 * @param selectd
	 * @return String
	 * @throws Exception
	 *             rqloanlist.CLIENTMGR
	 */
	public String getCLIENTMGR(String id, String name, String selectd,
			String brhid) throws Exception {
		String sql = "select LOGINNAME, USERNAME  from SCUSER where brhid='"
				+ brhid + "'";
		StringBuffer str = new StringBuffer();
		crs = manager.getRs(sql);
		str.append("<select id='" + id + "' name='" + name + "'>");
		str.append("<option value='' ");
		if (selectd != null && selectd.equals("")) {
			str.append(" selected='selected' ");
		}
		str.append(">ȫ��</option>");

		while (crs.next()) {
			str.append("<option value='" + crs.getString(1) + "' ");
			if (selectd != null && crs.getString(1) != null
					&& selectd.equals(crs.getString(1).trim())) {
				str.append(" selected='selected' ");
			}

			str.append(">" + DBUtil.fromDB(crs.getString(2)));

			str.append("</option>");
		}
		str.append("</select>");
		return str.toString();
	}

	/**
	 * �õ�ʵ��������ȫ���ͻ�����
	 * 
	 * @param id
	 * @param name
	 * @param selectd
	 * @return String
	 * @throws Exception
	 *             rqloanlist.CLIENTMGR
	 */
	public String getFIRSTRESP(String id, String name, String selectd,
			String brhid) throws Exception {
		String sql = "select LOGINNAME, USERNAME  from SCUSER where brhid='"
				+ brhid + "'";
		StringBuffer str = new StringBuffer();
		crs = manager.getRs(sql);
		str.append("<select id='" + id + "' name='" + name + "'>");
		str.append("<option value='' ");
		if (selectd != null && selectd.equals("")) {
			str.append(" selected='selected' ");
		}
		str.append(">ȫ��</option>");

		while (crs.next()) {
			str.append("<option value='" + crs.getString(1) + "' ");
			if (selectd != null && crs.getString(1) != null
					&& selectd.equals(crs.getString(1).trim())) {
				str.append(" selected='selected' ");
			}

			str.append(">" + DBUtil.fromDB(crs.getString(2)));

			str.append("</option>");
		}
		str.append("</select>");
		return str.toString();
	}

	/**
	 * �������
	 * 
	 * @param id
	 * @param name
	 * @param selectd
	 * @return String
	 * @throws Exception
	 */
	public String selectType(String id, String name, String selectd)
			throws Exception {
		StringBuffer str = new StringBuffer();
		str.append("<select id='" + id + "' name='" + name + "' >");
		str.append("<option value='2' ");
		if (selectd != null && selectd.equals("2")) {
			str.append(" selected='selected' ");
		}
		str.append(">����</option>");
		str.append("<option value='1' ");
		if (selectd != null && selectd.equals("1")) {
			str.append(" selected='selected' ");
		}
		str.append(">����</option>");

		str.append("</select>");
		return str.toString();
	}
	/**
	 * �������
	 * 
	 * @param id
	 * @param name
	 * @param selectd
	 * @return String
	 * @throws Exception
	 */
	public String selectType2(String id, String name, String selectd)
			throws Exception {
		StringBuffer str = new StringBuffer();
		str.append("<select id='" + id + "' name='" + name + "' >");
		str.append("<option value='2' ");
		if (selectd != null && selectd.equals("2")) {
			str.append(" selected='selected' ");
		}
		str.append(">����</option>");
		str.append("<option value='1' ");
		if (selectd != null && selectd.equals("1")) {
			str.append(" selected='selected' ");
		}
		str.append(">����</option>");

		str.append("</select>");
		return str.toString();
	}
	/**
	 * �¼����㼯�� ����HTML select ��ʽ
	 * 
	 * @param id
	 * @param name
	 * @param brhid
	 * @return String
	 */
	public String getBrhids(String id, String name, String brhid) {

		StringBuffer str = new StringBuffer();
		String sqlA = "select brhid,sname,brhtype from scbranch where upbrh='"
				+ brhid + "' or brhid='" + brhid + "'";
		str.append("<select id='" + id + "' name='" + name + "' >");

		try {
			crs = manager.getRs(sqlA);
			while (crs.next()) {
				String value = crs.getString(1).trim();
				String text = DBUtil.fromDB(crs.getString(2).trim());

				str.append("<option value='" + value + "'");
				if (brhid != null && brhid.equals(value.trim())) {
					str.append(" selected='selected' ");
				}
				str.append(">" + text + "</option>");

			}
			str.append("</select>");
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}

		return str.toString();

	}
	 /**
     * ��ȡ��Ŀ�б�
     * @return String
     * @throws Exception
     */
    public String getAccList()throws Exception
    {
    	String sql="select AccNo,AccName from SCHostAcc where ACCSTATUS='0' and acctp='3' and BALTYPE='1' order by AccNo";
        CachedRowSet rs = null;             

        String ret="";
        rs = manager.getRs(sql);
        while(rs.next())
        {
        	String accNo=rs.getString("AccNo").trim();
        	if (accNo.length()==4)
        		accNo+="---";
        	else if(accNo.length()==3)
        		accNo+="----";
        	else
        		accNo+="--";
        	ret+="<option value='"+rs.getString("AccNo")+"'>"+accNo+DBUtil.fromDB(rs.getString("AccName"))+"</option>/n";
        }
        return ret;
    }


}
