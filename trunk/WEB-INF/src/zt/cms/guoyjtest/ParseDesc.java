/*
 * Created on 2004-10-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package zt.cms.guoyjtest;

import java.util.logging.Logger;
import java.util.*;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ParseDesc {

	private Logger log = Logger.getLogger("zt.cms.guoyjtest.ParseDesc");

	private String sDesc;

	private HashMap map = new HashMap();

	public void Parse(String desc) throws Exception {
		this.setDesc(desc);
		this.Parse();
//		this.check();
	}

	private void setDesc(String desc) {
		this.sDesc = desc;
	}

	/*
	 * 解析字符串 格式:para1=value1,para2=value2...paraN=valueN
	 */
	private void Parse() throws Exception {
		try {
			String[] paras = sDesc.split(",");
			for (int i = 0; i < paras.length; i++) {
				String[] exp = paras[i].split("=");
				if (exp.length != 2)
					throw new Exception("数据库参数配置错误，请检查[Description:" + sDesc
							+ "]");
				map.put(exp[0], exp[1]);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void check() throws Exception {
		this.checkEmpty("formid", this.getParaValue("formid"));
	}

	private void checkEmpty(String sName, String sValue) throws Exception {
		if (sValue == null || sValue.equals(""))
			throw new Exception("缺少数据库参数:[" + sName + "]");
	}

	public String getParaValue(String sParaName) {
	
		try {
			return this.map.get(sParaName).toString();
		} catch (Exception e) {
			return "";
		}
	}
}