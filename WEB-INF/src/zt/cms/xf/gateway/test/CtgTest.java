package zt.cms.xf.gateway.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.ibm.ctg.client.*;

/*
 * make by wangkai 081014
 * CTG测试程序，与SBS测试机联通通过，能够正常收发包
 */
public class CtgTest {


    private static JavaGateway javaGatewayObject;
    public static int iValidationFailed=0;

    private static boolean bDataConv = true;
    private static String  strDataConv = "ASCII";
    private static byte[] buffer = new byte[32000];

    public static void main(String[] args) {

        ECIRequest eciRequestObject = null;
        String strProgram = "SCLMPC";
        String strChosenServer = "haier"; //服务器的连接配置是写在C:\Program Files\IBM\CICS Transaction Gateway\bin\CTG.ini中
        // String strUrl = "local:";
        // String buff = "";
        // int iPort = 1445;
       String strUrl = "192.168.91.2";
       String buff = "";
       int iPort = 2006;

        try {
        int iCommareaSize = 32000;
        byte[] abytCommarea = new byte[iCommareaSize];


        javaGatewayObject = new JavaGateway(strUrl, iPort);
        eciRequestObject = ECIRequest.listSystems(20);
        flowRequest(eciRequestObject);
		int x=0;
      	String zero = "010100100000000005";
      	int no = 1001;

        for(int i = 1;i <= 1;i++){
            if (eciRequestObject.SystemList.isEmpty() == true) {
                System.out.println("No CICS servers have been defined.");
                if (javaGatewayObject.isOpen() == true) {
                    //javaGatewayObject.close();
                }
                //System.exit(0);
            }
            //打包
//            int   ascii=00;
//            char   ch=(char)ascii;
//            System.out.println("-"+ch+"-");

            //buff = "TPEIxxxx 010       MPC1SYS1                       0611111103010026004010 140000010201100118000000000000000001";
			buff = "TPEIn020  010       MPC1MPC2"; //包头内容，xxxx交易，010网点，MPC1终端，MPC2柜员，包头定长51个字符
            System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //打包包头

            List list = new ArrayList();//包体内容，将包体内容放入list中，有几个输入项，就add几个
       			//x = x + 1;
      			//int length = String.valueOf(x).length();
      			//String result = zero.substring(length)+x;
      			no = no + 1;
      			String result = zero.substring(0,14)+""+no;

//n020 CTY 汇出汇款
           list.add("2009052113010113  ");      //MPC流水号
           list.add("010");                   //交易机构
           list.add("20090526");              //委托日期
           list.add("0000010");                //客户号
           list.add("CTY");                   //交易类型
           list.add("001");                   //交易货币
           list.add("+0000000000011.02");       //交易金额
           list.add("T");                     //汇款类型
           list.add("01");                    //汇款帐户类型
           list.add("801000000102011001    ");//汇款帐户
           list.add("1");                     //费用帐户类型
           list.add("                      ");//费用帐户
           list.add("35                                 ");  //收款人帐号
           list.add("150                                                                                                                                                   ");//收款人名称
           list.add("150                                                                                                                                                   ");//收款行行名
           list.add("                                                                                ");//保留项
           list.add("                                        ");                                        //保留项
           list.add("150                                                                                                                                                   ");//汇款人名称
           list.add("150                                                                                                                                                   ");//汇款用途
           list.add(" ");           //保留项
           list.add("108452004039"); //收款行机构号、行号
           list.add("          ");   //保留项
           list.add(" ");           //保留项
           list.add("2009052114010108");   //FS流水号
           list.add("2009052114010110");          //交易流水号

//n022       对公代理支付结果查询
          // list.add("1100000053677413  ");                       //  MPCNO         18位
          // list.add("1100000058362504");                         //  交易流水号    16位
          // list.add("20090515");                                //  交易日期      8位
          // list.add("105826000014");                             //  收款行行号   12位
//a541 XDS 贷款还款
           // list.add("20090606");              //交易日期
           // list.add("2009060620000004");      //交易流水号
           // list.add("801000029701105001    ");//贷款帐户
           // list.add("+0000000000001.01");     //本金金额
           // list.add("+0000000000001.02");     //违约金金额
           // list.add("+0000000000001.03");     //滞纳金金额
           // list.add("+0000000000001.04");     //手续费金额
           // list.add("                              ");//摘要
//n070 XDS 贷款发放
           //list.add("2009061300000007");      //交易流水号
           //list.add("010");                   //交易机构
           //list.add("20090613");              //委托日期
           //list.add("0000010");               //客户号
           //list.add("XDS");                   //交易类型
           //list.add("001");                   //交易货币
           //list.add("+0000000000001.01");     //交易金额
           //list.add("T");                     //汇款类型
           //list.add("01");                    //汇款帐户类型
           //list.add("801000029701105001    ");//贷款帐户
           //list.add("1");                     //费用帐户类型
           //list.add("                      ");//费用帐户
           //list.add("35                                 ");  //收款人帐号
           //list.add("收款人名称                                                                                                                                            ");//收款人名称
           //list.add("收款行行名                                                                                                                                            ");//收款行行名
           //list.add("                                                                                ");//保留项
           //list.add("                                        ");                                        //保留项
           //list.add("150                                                                                                                                                   ");//贷款人名称
           //list.add("150                                                                                                                                                   ");//用途
           //list.add(" ");           //支票类型
           //list.add("            ");//支票号
           //list.add("          ");  //支票密码
           //list.add(" ");           //保留项



//n040       批量代发代扣
          //list.add("20090918");                                //  交易日期   8
          //list.add("1100000049046321  ");                      //  MPCNO       18
          //list.add("1100000050851131");                        //  请求序列号16
          //list.add("+0000000000001.01");                       //  总金额       17
          //list.add("0000001");                                 //  总笔数        7
          //list.add("0000001");                                 //  本包总笔数 7
          //list.add("0");                                       //  是否有后续包  0-否，1-是
          //list.add("801000003012011001    ");                  //  转出帐户  22
          //list.add("99999998    ");                            //  用途 12
          //list.add("000000000000000000000000000030");          //  备注,   30
          //list.add("00000000000000000000000000000032");        //  备注1,  32
          //list.add("00000000000000000000000000000032");        //  备注2,  32
          //list.add("105");                                     //  银行代码,  3
          //list.add("+0000000000000.00");                       //  失败金额       17
          //list.add("0000000");                                 //  失败笔数        7
          //list.add("BAS");                                     //  交易类别 BAP-批量报销,BAS-批量代发工资
          //list.add("0000001.01||2390049980100048890|name||");  //  代发代扣文件内容  29000

//n042       批量代发代扣结果查询
          //list.add("9999999999999999  ");                      //  MPCNO       18
          //list.add("9999999999999999");                        //  请求序列号16
          //list.add("20090301");                                //  交易日期   8
          //list.add("000001");                                  //  起始笔数 6

//8118 查询账户余额
          // list.add("111111");   //保留项
          // list.add("010");      //部门机构号
          // list.add("60");       //部门号
          // list.add("010");      //帐户机构号
          // list.add("801000000102011001");
//8853 查询交易明细
           //list.add("111111");          //保留项
           //list.add("010");             //部门机构号
           //list.add("60");              //部门号
           //list.add("010");             //帐户机构号
           //list.add("801000000102011001    ");
           //list.add("20090103");
           //list.add("20090202");
           //list.add("000000");

//8872 汇总对账
           // list.add("20090519"); //交易日期

//8873 明细对账
           //list.add("20090613");   //交易日期
           //list.add("000000");      //起始笔数

//aa41  NZZ  内部转账   aa51 POS POS收款
            // list.add("01");                                                                          //转出帐户类型
            // list.add("801090106001041001    ");                                                      //转出账号
            // list.add("3");                                                                           //取款方式
            // list.add("123456789012345678901234                                                ");    //转出帐户户名
            // list.add("111111");                                                                      //取款密码
            // list.add("N");                                                                           //证件类型
            // list.add("2009042710000002  ");                                                            //MPC流水号
            // list.add(" ");                                                                           //支票种类
            // list.add("          ");                                                                  //支票号
            // list.add("            ");                                                                //支票密码
            // list.add("        ");                                                                    //签发日期
            // list.add("3");                                                                           //无折标识
            // list.add("12345678");                                                                    //备用字段
            // list.add("1234");                                                                        //备用字段
            // list.add("+0000000000001.02");                                                           //交易金额
            // list.add("01");                                                                          //转入帐户类型
            // list.add("801000004002011001    ");                                                      //转入帐号
            // list.add("12345678901234567890                                                    ");    //转入帐户户名
            // list.add(" ");                                                                           //无折标识
            // list.add("20090427");                                                                    //交易日期
            // list.add("123456789012345678901234567890");                                              //摘要
            // list.add("    ");                                                                        //产品码
            // list.add(" ");                                                                           //保留项
            // list.add(" ");                                                                           //保留项



            //打包包体
            setValues(list,abytCommarea);
            //buff = "TPEI8118"+ch+ch+"010"+ch+ch+ch+ch+ch+ch+ch+"MPC1SYS2"+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+"0611111103010026004010"+ch+"140000010201100118000000000000000001";
            //buff.replace(' ',ch);
            //System.out.println("test 4.1 + "+buff.length());
            //System.out.println("test 4.2 + "+buff);
            //buff = "TPEI8118000100000000MPC1SYS20000000000000000000000006111111030100260040100140000010201100118000000000000000001";
            //String aa = "TPEI8118  010       MPC1SYS1                       ";
            //System.out.println("aa.size + "+ aa.length());
            //buff = "AAAA";
            //System.arraycopy(buffer, pos, abytCommarea, 0, len);
           // System.arraycopy(buff.getBytes(), 0, abytCommarea, 0, Math.min(abytCommarea.length, buff.length()));
            //System.out.println("test 4.3 + "+abytCommarea.toString());
            //System.out.println("test 4.4 + "+buff.getBytes() +" "+buff);
            //System.arraycopy(buff.getBytes(), 0, abytCommarea, 0, buff.length());
            //System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length());
            //System.out.println("test 4.5 + "+ new String(abytCommarea));

            //发送包
            eciRequestObject =
                    new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
                    strChosenServer, //CICS server
                    "1", //CICS userid
                    "1", //CICS password
                    strProgram, //CICS program to be run
                    null, //CICS transid to be run
                    abytCommarea, //Byte array containing the
                    // COMMAREA
                    iCommareaSize, //COMMAREA length
                    ECIRequest.ECI_NO_EXTEND, //ECI extend mode
                    0);                       //ECI LUW token


            //获取返回报文
            if (flowRequest(eciRequestObject) == true) {
             //解sof
             System.out.println("返回值11为\n"+new String(abytCommarea));
            }
            System.out.println("返回值22为\n"+new String(abytCommarea));

            if (javaGatewayObject.isOpen() == true) {
                //javaGatewayObject.close();
            }

        }
        }
        catch (Exception e) {
            e.printStackTrace();
         }
    }

    static  byte[] getBytes(String source) throws java.io.UnsupportedEncodingException {
        if (bDataConv) {
            return source.getBytes(strDataConv);
        } else {
            return source.getBytes();
        }
    }

	public static void setValues(List list,byte[] bb) {
		int start = 51;
		for (int i = 1;i <= list.size();i++)
		{
			String value = list.get(i-1).toString();

			String size = "";
			try{
				  size = new String(value.getBytes("GBK"),"ISO-8859-1");
				  }catch(Exception e){
					e.printStackTrace();
				}

			//  System.out.println(i+" "+start+" "+value.length()+" "+value);

			System.out.println(i+" "+start+" "+size.length()+" "+value);
			setVarData(start,value,bb); //根据list的数量（即输入项的数量），将内容循环加入包体
			start = start + size.length() + 2;
		}
	}

	public static void setVarData(int pos, String data,byte[] aa) {

		//说明，经测试发现，包体内容的内容长度，定长2字符，必须用十六进制形式发送
		//比如，第一个输入项的输入内容为“111111”，长度为6个字符，如果在包体中写入"06111111"，则CTG Server端无法正常读取字符长度，
		//"06"必须用十六进制形式发送，即0x00 0x60
		//如果包体中有中文字符，测了一下不能正确取到中文字符的长度，使用UTF-8字符集可以取到正确的中文字符的长度
		String size = "";
		try{
			  size = new String(data.getBytes("GBK"),"ISO-8859-1");
			  }catch(Exception e){
				e.printStackTrace();
			}

		short len = (short)size.length();

//		short len = (short)data.length();
		byte[] slen = new byte[2];
		slen[0] = (byte)(len >> 8);
        slen[1] = (byte)(len >> 0);
		System.arraycopy(slen, 0, aa, pos, 2);
		System.arraycopy(data.getBytes(), 0, aa, pos+2, len);
	}

   private static boolean

flowRequest(ECIRequest requestObject) {
      try {

         int iRc = javaGatewayObject.flow(requestObject);
         switch

(requestObject.getCicsRc()) {
         case ECIRequest.ECI_NO_ERROR:
            if (iRc == 0) {
               return false;
            }

else {
               System.out.println("\nError from Gateway ("
                                  + requestObject.getRcString()
                                  + "), correct and rerun this sample");
               if

(javaGatewayObject.isOpen() == true) {
                  javaGatewayObject.close();
               }

System.exit(0);
            }

case ECIRequest.ECI_ERR_SECURITY_ERROR:
            if (iValidationFailed == 0) {
               return true;
            }

System.out.print("\n\nValidation failed. ");
            if

(iValidationFailed < 3) {
               System.out.println("Try entering your details again.");
               return

true;
            }

break;


         case

ECIRequest.ECI_ERR_TRANSACTION_ABEND:
            System.out.println("\nYou are not authorised to run this "
                               + "transaction.");
         }

System.out.println("\nECI returned: "
                            + requestObject.getCicsRcString());
         System.out

.println("Abend code was "
                            + requestObject.Abend_Code + "\n");
         if

(javaGatewayObject.isOpen() == true) {
            javaGatewayObject.close();
         }

System.exit(0);

      }

catch (Exception e) {
         e.printStackTrace();
         System.exit(0);
      }

return true;
   }
}
