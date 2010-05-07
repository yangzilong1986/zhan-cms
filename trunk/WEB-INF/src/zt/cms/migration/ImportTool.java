package zt.cms.migration;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import zt.platform.db.*;

public class ImportTool {

    public static Logger logger = Logger.getLogger("zt.cms.migration.ImportTool");

    String fileName = null;
    DatabaseConnection con = null;

    String errorInfo = null;

    public ImportTool(String fileName)
    {
        this.fileName = fileName;
    }

    public boolean importData()
    {
        BufferedReader reader =null ;
        BufferedWriter writer =null ;
        try {
            con = ConnectionManager.getInstance().getConnection();
            reader = new BufferedReader(new FileReader(fileName));
            writer = new BufferedWriter(new FileWriter("ErrorDate.txt"));

            String line = null;
            while ( (line = reader.readLine()) != null) {
                if(insertLine(line)<0){
                    writer.write(line+","+this.errorInfo+"\r\n");
                }
            }



            ConnectionManager.getInstance().releaseConnection(con);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            logger.warning("�ļ�'" + fileName + "'�Ҳ���");
            return false;
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
            logger.warning("�ļ�'" + fileName + "'��ȡ����");
            return false;
        }finally{

            try {
                writer.flush();
                writer.close();
                reader.close();
            }
            catch (IOException ex1) {
            }

            con.commit();
            ConnectionManager.getInstance().releaseConnection(con);
        }
        return true;
    }

    private int insertLine(String line){
        String[] values = line.split(",");

        if(values.length!=5){
            this.errorInfo = "�����ֶ���Ŀ����";
            return -1;
        }

        String bmno = values[0];
        String ifresploan = values[1];
        String loanType5 = values[2];
        String firstResp = values[3];
        String decidedBy = values[4];

        if(bmno.equals("")||ifresploan.equals("")||loanType5.equals("")||firstResp.equals("")||decidedBy.equals("")){
            this.errorInfo = "��������ڿ��ֶ�";
            return -2;
        }


        int a=-1;
        int b=-1;
        try {
           a = Integer.parseInt(ifresploan);
           b = Integer.parseInt(loanType5);
        }
        catch (NumberFormatException ex) {
            this.errorInfo = "�Ƿ����δ������������������";
            return -8;
        }
        if(a!=1&&a!=0){
            this.errorInfo = "�Ƿ����δ���ֻ��ʹ0��1";
            return -8;
        }
        if(b!=1&&b!=9){
            this.errorInfo = "�������ֻ��Ϊ1��9";
            return -8;
        }

        String checkStr1 = "select username from scuser where loginname='" + firstResp +
            "' and usertype='2'";
        String checkStr2 = "select username from scuser where loginname='"+decidedBy+"' and usertype='1'";
        RecordSet rs1 = con.executeQuery(checkStr1);
        RecordSet rs2 = con.executeQuery(checkStr2);
        if (!rs1.next()) {
            this.errorInfo = "��һ�����˲����ڣ����飡";
            return -3;
        }
        if (!rs2.next()) {
            this.errorInfo = "�����˲����ڣ����飡";
            return -4;
        }



        String nowBalStr = "select nowbal from rqloanledger where bmno = '"+bmno+"'";
        RecordSet nowBalRs = con.executeQuery(nowBalStr);
        if(nowBalRs.next()){
           String nowBal = nowBalRs.getString("nowbal");
           if(nowBal==null){
               this.errorInfo = "ԭ���Ϊ�գ�";
                return -12;
            }
            else {
                String updateStr = "update bmtableapp set ifresploan=" + ifresploan + ",loantype5=" +
                    loanType5 + ",firstresp='" + firstResp + "',decidedby='" + decidedBy +
                    "',fisrtresppct="+nowBal+" where bmno='" + bmno + "'";
                logger.info(updateStr);
                if (this.con.executeUpdate(updateStr) < 0) {
                    this.errorInfo = "��������ʧ�ܣ�";
                    return -13;
                }

            }
        }
        else {
            this.errorInfo = "��ѯ���ʧ��";
            return -11;
        }
        return 0;
    }

    public static void main(String[] args) {
        //ImportTool tool = new ImportTool("C:\\c.csv");
        if(args.length==0){
            System.out.println("�������ļ�����");
            return;
        }
        ImportTool tool = new ImportTool(args[0]);
        tool.importData();
        System.out.println("�����������ErrorData.txt��");
    }
}
