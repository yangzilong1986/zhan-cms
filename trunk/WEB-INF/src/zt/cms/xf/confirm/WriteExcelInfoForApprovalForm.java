package zt.cms.xf.confirm;

import zt.cms.util.poiutil.IWriteOtherInfos;
import zt.cms.xf.common.dto.Xfvcontractprtinfo;
import zt.cms.xf.common.factory.XfvcontractprtinfoDaoFactory;
import zt.cms.xf.common.dao.XfvcontractprtinfoDao;
import zt.cms.xf.contract.XFContractListDY;
import zt.platform.utils.Debug;
import org.apache.poi.hssf.usermodel.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-9-27
 * Time: 14:11:01
 * To change this template use File | Settings | File Templates.
 */
public class WriteExcelInfoForApprovalForm implements IWriteOtherInfos {
    private static Log logger = LogFactory.getLog(XFContractListDY.class);


    public void writeInfo(HSSFWorkbook workbook, HashMap paramap) {

        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);

        String contractno = (String) paramap.get("CONTRACTNO");
        if (contractno == null) {
            logger.error("��������ͬ�ţ�Ϊ�գ�");
            throw new RuntimeException();
        }

        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

        Xfvcontractprtinfo info;
        try {
            XfvcontractprtinfoDao dao = XfvcontractprtinfoDaoFactory.create();
            info = dao.findWhereContractnoEquals(contractno);
        } catch (Exception e) {
            Debug.debug(e);
            logger.error("��ȡ��ͬ��Ϣ��View��ʱ����");
            throw new RuntimeException(e);
        }





        
        //TITLE
//        sheet.getRow(1).getCell(2).setCellValue(new HSSFRichTextString("aaa"));
//        sheet.getRow(0).getCell(2).setCellValue(1.23);
        sheet.getRow(1).getCell(1).setCellValue(new HSSFRichTextString("�𾴵� "+info.getClientname()+" ����(Ůʿ)��"));

        int rownum = 5;
        int colnum = 4;

        //��������
        DateFormat dateformat = new SimpleDateFormat("yyyy��MM��dd��");
        String startdate = dateformat.format(info.getAppdate());
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(startdate));
        //�̻�
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getPartnername()));
        //������Ϣ
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getCommname()));
        //��Ʒ�ܽ��
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString("��"+info.getCommamt().toString()+"Ԫ"));
        //�׸���
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString("��"+info.getReceiveamt().toString()+"Ԫ"));
        //�����ܽ��
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString("��"+info.getContractamt().toString()+"Ԫ"));
        //��������
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getDuration().toString()));
        //��������
        sheet.getRow(++rownum).getCell(colnum).setCellValue(info.getServicecharge().doubleValue() * 1000);
        //��ͬ���
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getContractno()));
        //���ʽ
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getWithholdbankname()));
        //�����ʻ�
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getPaybackact()));

        //��ӡ����
        sheet.getRow(35 + info.getDuration().intValue()-1).getCell(5).setCellValue(new HSSFRichTextString(dateformat.format(new Date())));

    }
}