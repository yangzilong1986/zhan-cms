package com.alipay.util;

import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
/**
 * 名称：支付类调取异步返回处理帮助类
 * 功能：辅助异步返回进行验证、写日志等动作
 * 类属性：支付宝公共类
 * 版本：2.0
 * 日期：2008-12-25
 * 作者：支付宝公司销售部技术支持团队
 * 联系：0571-26888888
 * 版权：支付宝公司
 * */
	public class SignatureHelper {
		public static String sign(Map params, String privateKey) {
			Properties properties = new Properties();

			for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				Object value = params.get(name);

				if (name == null || name.equalsIgnoreCase("sign")
						|| name.equalsIgnoreCase("sign_type")) {
					continue;
				}
				
				properties.setProperty(name, value.toString());

			}

			String content = getSignatureContent(properties);
			return sign(content, privateKey);
		}

	public static String getSignatureContent(Properties properties) {
		StringBuffer content = new StringBuffer();
		List keys = new ArrayList(properties.keySet());
		Collections.sort(keys);

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = properties.getProperty(key);
			content.append((i == 0 ? "" : "&") + key + "=" + value);
		}

		return content.toString();
	}

	public static String sign(String content, String privateKey) {
		if (privateKey == null) {
			return null;
		}
		String signBefore = content + privateKey;
		//System.out.print("signBefore=" + signBefore);
		
		//*****************************************************************
		//当alipay收到信息，会把接受的信息写程日志
		//该文件存在于和应用服务器   启动文件同一目录下，文件名是alipay log加服务器时间
		try {
			FileWriter writer  = new FileWriter("D:/alipay_log/"+"alipay_daikou_alipay_log"+"alipay_log"+System.currentTimeMillis()+".txt");
			writer.write(signBefore);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
//*********************************************************************
		return Md5Encrypt.md5(signBefore);

	}
}
