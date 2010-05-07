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
                buff = "TPEIn050  010       XD01XD01"; //��ͷ���ݣ�xxxx���ף�010���㣬MPC1�նˣ�MPC1��Ա����ͷ����51���ַ�
                System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //�����ͷ

                List list = new ArrayList();

                no = no + 1;
                String result = zero.substring(0, 14) + "" + no;

//                list.add("20090602");              //��������
//                list.add("2009060200000001");      //������ˮ��
//                list.add("801000029701105001    ");//�����ʻ�                 22λ�����㲹�ո�
//                list.add("+0000000000001.01");     //������
//                list.add("+0000000000001.02");     //ΥԼ����
//                list.add("+0000000000001.03");     //���ɽ���
//                list.add("+0000000000001.04");     //�����ѽ��
//                list.add("                              ");//ժҪ

//n040       ������������
                list.add("20090701");                                //  ��������   8
                list.add("1100000049046323  ");                      //  MPCNO       18
                list.add("1100000050851132");                        //  �������к�16
                list.add("+0000000000001.01");                       //  �ܽ��       17
                list.add("0000001");                                 //  �ܱ���        7
                list.add("0000001");                                 //  �����ܱ��� 7
                list.add("0");                                       //  �Ƿ��к�����  0-��1-��
                list.add("801000003012011001    ");                  //  ת���ʻ�  22
                list.add("99999998    ");                            //  ��; 12
                list.add("000000000000000000000000000030");          //  ��ע,   30
                list.add("00000000000000000000000000000032");        //  ��ע1,  32
                list.add("00000000000000000000000000000032");        //  ��ע2,  32
                list.add("105");                                     //  ���д���,  3
                list.add("+0000000000000.00");                       //  ʧ�ܽ��       17
                list.add("0000000");                                 //  ʧ�ܱ���        7
                list.add("BAW");                                     //  ������� BAP-��������,BAS-������������
//                list.add("0000001.01||2390049980100048890|����||");  //  ���������ļ�����  29000
                list.add("0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||" +
                        "0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||" +
                        "0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||" +
//                        "0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||" +
                        "0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||" +  
                        "0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||0000001.01||2390049980100048890|����||");

//n042       �����������۽����ѯ
                //list.add("9999999999999999  ");                      //  MPCNO       18
                //list.add("9999999999999999");                        //  �������к�16
                //list.add("20090301");                                //  ��������   8
                //list.add("000001");                                  //  ��ʼ���� 6


                //�������
                setValues(list, abytCommarea);

                System.out.println("����ֵASC:" + new String(abytCommarea));
                System.out.print("����ֵHEX:");
                for (int k = 0; k < abytCommarea.length; k++) {
                    System.out.print(Integer.toHexString(abytCommarea[k]));
                }

                //���Ͱ�
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


                //��ȡ���ر���
                if (flowRequest(eciRequestObject) == true) {
                    //��sof
                    System.out.println("����ֵ01:" + new String(abytCommarea));
                }
                System.out.println("\n����ֵ02ASC:" + new String(abytCommarea));
                System.out.print("����ֵ02HEX:");
                for (int k = 0; k < abytCommarea.length; k++) {
                    System.out.print(Integer.toHexString(abytCommarea[k]));
                }

                //���
                

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
            setVarData(start, value, bb); //����list�������������������������������ѭ���������
            start = start + size.length() + 2;
        }
    }

    public static void setVarData(int pos, String data, byte[] aa) {

        //˵���������Է��֣��������ݵ����ݳ��ȣ�����2�ַ���������ʮ��������ʽ����
        //���磬��һ�����������������Ϊ��111111��������Ϊ6���ַ�������ڰ�����д��"06111111"����CTG Server���޷�������ȡ�ַ����ȣ�
        //"06"������ʮ��������ʽ���ͣ���0x00 0x60
        //����������������ַ�������һ�²�����ȷȡ�������ַ��ĳ��ȣ�ʹ��UTF-8�ַ�������ȡ����ȷ�������ַ��ĳ���
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