package zt.cms.xf.gateway.test;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-5-5
 * Time: 17:51:40
 * To change this template use File | Settings | File Templates.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ibm.ctg.client.*;

public class CtgTestBanch {


    private static JavaGateway javaGatewayObject;
    public static int iValidationFailed = 0;

    private static boolean bDataConv = true;
    private static String strDataConv = "ASCII";
    private static byte[] buffer = new byte[32000];

    public static void main(String[] args) {

        ECIRequest eciRequestObject = null;
        //String strProgram = "SCLXDYW";
        String strProgram = "SCLMPC";
        String strChosenServer = "haier";
        String strUrl = "192.168.91.2";
        String buff = "";
        int iPort = 2006;

        try {
            int iCommareaSize = 32000;
            byte[] abytCommarea = new byte[iCommareaSize];


            javaGatewayObject = new JavaGateway(strUrl, iPort);
            eciRequestObject = ECIRequest.listSystems(20);
            flowRequest(eciRequestObject);
            int x = 0;
            String zero = "010100100000000005";
            int no = 1001;

            for (int i = 1; i <= 1; i++) {
                if (eciRequestObject.SystemList.isEmpty() == true) {
                    System.out.println("No CICS servers have been defined.");
                    if (javaGatewayObject.isOpen() == true) {
                        //javaGatewayObject.close();
                    }
                    //System.exit(0);
                }
                buff = "TPEIn050  010       XD01XD01"; //包头内容，xxxx交易，010网点，MPC1终端，MPC1柜员，包头定长51个字符
                System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //打包包头

                List list = new ArrayList();

                no = no + 1;
                String result = zero.substring(0, 14) + "" + no;

//                list.add("20090602");              //交易日期
//                list.add("2009060200000001");      //交易流水号
//                list.add("801000029701105001    ");//贷款帐户                 22位，不足补空格
//                list.add("+0000000000001.01");     //本金金额
//                list.add("+0000000000001.02");     //违约金金额
//                list.add("+0000000000001.03");     //滞纳金金额
//                list.add("+0000000000001.04");     //手续费金额
//                list.add("                              ");//摘要

//n040       批量代发代扣
                list.add("20090701");                                //  交易日期   8
                list.add("1100000049046323  ");                      //  MPCNO       18
                list.add("1100000050851132");                        //  请求序列号16
                list.add("+0000000000001.01");                       //  总金额       17
                list.add("0000001");                                 //  总笔数        7
                list.add("0000001");                                 //  本包总笔数 7
                list.add("0");                                       //  是否有后续包  0-否，1-是
                list.add("801000003012011001    ");                  //  转出帐户  22
                list.add("99999998    ");                            //  用途 12
                list.add("000000000000000000000000000030");          //  备注,   30
                list.add("00000000000000000000000000000032");        //  备注1,  32
                list.add("00000000000000000000000000000032");        //  备注2,  32
                list.add("105");                                     //  银行代码,  3
                list.add("+0000000000000.00");                       //  失败金额       17
                list.add("0000000");                                 //  失败笔数        7
                list.add("BAW");                                     //  交易类别 BAP-批量报销,BAS-批量代发工资
//                list.add("0000001.01||2390049980100048890|姓名||");  //  代发代扣文件内容  29000
                list.add("0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||" +
                        "0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||" +
                        "0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||" +
//                        "0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||" +
                        "0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||" +  
                        "0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||0000001.01||2390049980100048890|姓名||");

//n042       批量代发代扣结果查询
                //list.add("9999999999999999  ");                      //  MPCNO       18
                //list.add("9999999999999999");                        //  请求序列号16
                //list.add("20090301");                                //  交易日期   8
                //list.add("000001");                                  //  起始笔数 6


                //打包包体
                setValues(list, abytCommarea);

                System.out.println("发送值ASC:" + new String(abytCommarea));
                System.out.print("发送值HEX:");
                for (int k = 0; k < abytCommarea.length; k++) {
                    System.out.print(Integer.toHexString(abytCommarea[k]));
                }

                //发送包
                eciRequestObject = new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
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
                    System.out.println("返回值01:" + new String(abytCommarea));
                }
                System.out.println("\n返回值02ASC:" + new String(abytCommarea));
                System.out.print("返回值02HEX:");
                for (int k = 0; k < abytCommarea.length; k++) {
                    System.out.print(Integer.toHexString(abytCommarea[k]));
                }

                //解包
                

                if (javaGatewayObject.isOpen() == true) {
                    //javaGatewayObject.close();
                }


            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static byte[] getBytes(String source) throws UnsupportedEncodingException {
        if (bDataConv) {
            return source.getBytes(strDataConv);
        } else {
            return source.getBytes();
        }
    }

    public static void setValues(List list, byte[] bb) {
        int start = 51;
        for (int i = 1; i <= list.size(); i++) {
            String value = list.get(i - 1).toString();

            String size = "";
            try {
                size = new String(value.getBytes("GBK"), "ISO-8859-1");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  System.out.println(i+" "+start+" "+value.length()+" "+value);

           // System.out.println(i + " " + start + " " + size.length() + " " + value);
            setVarData(start, value, bb); //根据list的数量（即输入项的数量），将内容循环加入包体
            start = start + size.length() + 2;
        }
    }

    public static void setVarData(int pos, String data, byte[] aa) {

        //说明，经测试发现，包体内容的内容长度，定长2字符，必须用十六进制形式发送
        //比如，第一个输入项的输入内容为“111111”，长度为6个字符，如果在包体中写入"06111111"，则CTG Server端无法正常读取字符长度，
        //"06"必须用十六进制形式发送，即0x00 0x60
        //如果包体中有中文字符，测了一下不能正确取到中文字符的长度，使用UTF-8字符集可以取到正确的中文字符的长度
        String size = "";
        try {
            size = new String(data.getBytes("GBK"), "ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        short len = (short) size.length();

//		short len = (short)data.length();
        byte[] slen = new byte[2];
        slen[0] = (byte) (len >> 8);
        slen[1] = (byte) (len >> 0);
        System.arraycopy(slen, 0, aa, pos, 2);
        System.arraycopy(data.getBytes(), 0, aa, pos + 2, len);
    }


    private static boolean flowRequest(ECIRequest requestObject) {
        try {
            int iRc = javaGatewayObject.flow(requestObject);
            switch (requestObject.getCicsRc()) {
                case ECIRequest.ECI_NO_ERROR:
                    if (iRc == 0) {
                        return false;
                    } else {
                        System.out.println("\nError from Gateway ("
                                + requestObject.getRcString()
                                + "), correct and rerun this sample");
                        if (javaGatewayObject.isOpen() == true) {
                            javaGatewayObject.close();
                        }
                        System.exit(0);
                    }
                case ECIRequest.ECI_ERR_SECURITY_ERROR:
                    if (iValidationFailed == 0) {
                        return true;
                    }
                    System.out.print("\n\nValidation failed. ");
                    if (iValidationFailed < 3) {
                        System.out.println("Try entering your details again.");
                        return true;
                    }
                    break;
                case ECIRequest.ECI_ERR_TRANSACTION_ABEND:
                    System.out.println("\nYou are not authorised to run this "
                            + "transaction.");
            }

            System.out.println("\nECI returned: " + requestObject.getCicsRcString());
            System.out.println("Abend code was " + requestObject.Abend_Code + "\n");
            if (javaGatewayObject.isOpen() == true) {
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