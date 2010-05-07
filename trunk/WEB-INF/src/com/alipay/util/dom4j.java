package com.alipay.util;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.nio.charset.Charset;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
/**
 * 功能：外部解析XML文件类（此类不是支付宝所有，也可以使用其他方法解析）
 * 类属性：非支付宝解析类
 * 公司名称：alipay
 * 修改时间：2008-10-10
 * */
public class dom4j {
	String filepath = "";

	public String DomXml(String filepath) throws Exception {

        Charset  charset  =  Charset.forName("GBK");

		SAXReader reader = new SAXReader();
		Document doc = reader.read(new URL(filepath).openStream());
		System.out.println(doc.toString());
		
//		List<Node> nodeList = doc.selectNodes("/alipay/*"); //
//		StringBuffer buf = new StringBuffer();
//		for (Node node : nodeList) {
//			buf.append(node.getName()).append("=").append(node.getText())
//			.append("&");
//		}
		List   nodeList = doc.selectNodes("/alipay/*");
		StringBuffer buf = new StringBuffer();
        Iterator it =  nodeList.iterator();
        while ( it.hasNext()) {
            Node node =(Node)it.next();
			buf.append(node.getName()).append("=").append(node.getText())
			.append("&");
        }


//		List<Node> nodeList1 = doc.selectNodes("//request/*"); //
//		StringBuffer buf1 = new StringBuffer();
//		for (Node node1 : nodeList1) {
//			buf1.append(node1.getName()).append("=").append(node1.getText())
//			.append("&");
//		}
        List   nodeList1 = doc.selectNodes("//request/*");
        StringBuffer buf1 = new StringBuffer();
        Iterator it1 =  nodeList1.iterator();
        while ( it1.hasNext()) {
            Node node1 =(Node)it.next();
            buf.append(node1.getName()).append("=").append(node1.getText())
            .append("&");
        }




		//List<Node> nodeList2 = doc.selectNodes("//response/item");
		//StringBuffer buf2 = new StringBuffer();
		//for (Node node2 : nodeList2) {
		//	buf2.append(node2.getName()).append("=").append(node2.getText())
		//	.append("&");
		//}
		
		System.out.println(buf.toString()+buf1.toString());

		return buf.toString()+"    "+buf1.toString();

	}

	public static void main(String[] args) throws Exception {
		dom4j dom = new dom4j();
		String sss = dom.DomXml("https://www.alipay.com/cooperate/gateway.do?amount=0.01&user_email=zeven.wu@gmail.com&service=unfreeze_trade_fee&partner=2088002037527206&gmt_out_order_create=2008-07-30 11:51:39&op_type=0008&old_out_order_no=20080730052947212&_input_charset=gbk&subject=您的订单是720080731110304&out_order_no=20080731110304&sign=f36c29467187131c6db9b3844214bcbc&sign_type=MD5");
		//String sss = dom.DomXml("https://www.alipay.com/cooperate/gateway.do?amount=0.01&service=cae_charge_agent&partner=2088101568349711&gmt_out_order_create=2009-05-10+03%3A12%3A56&input_charset=GBK&subject=%3F%A1%EC%3F%3F%A1%C0%3F%3F%3F%3F%3F%3F%A8%B2%3F%3F%3F%3F%3F%3F&notify_url=http%3A%2F%2Flocalhost%2Ftest%2Falipay%2Falipay_notify.jsp&customer_code=122850000035&t5=%CD%C6%BC%F6%CA%B9%D3%C3%D6%A7%B8%B6%B1%A6%B8%B6%BF%EE&t4=images%2Falipay_bwrx.gif&out_order_no=20090510031256&type_code=TEST100011000101&sign=3557158b4902091c4342ba42d8764136&sign_type=MD5");
		System.out.println(sss);

	}
}
