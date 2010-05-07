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
            logger.error("参数（合同号）为空！");
            throw new RuntimeException();
        }

        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

        Xfvcontractprtinfo info;
        try {
            XfvcontractprtinfoDao dao = XfvcontractprtinfoDaoFactory.create();
            info = dao.findWhereContractnoEquals(contractno);
        } catch (Exception e) {
            Debug.debug(e);
            logger.error("读取合同信息（View）时有误！");
            throw new RuntimeException(e);
        }





        
        //TITLE
//        sheet.getRow(1).getCell(2).setCellValue(new HSSFRichTextString("aaa"));
//        sheet.getRow(0).getCell(2).setCellValue(1.23);
        sheet.getRow(1).getCell(1).setCellValue(new HSSFRichTextString("尊敬的 "+info.getClientname()+" 先生(女士)："));

        int rownum = 5;
        int colnum = 4;

        //申请日期
        DateFormat dateformat = new SimpleDateFormat("yyyy年MM月dd日");
        String startdate = dateformat.format(info.getAppdate());
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(startdate));
        //商户
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getPartnername()));
        //购买信息
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getCommname()));
        //商品总金额
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString("￥"+info.getCommamt().toString()+"元"));
        //首付款
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString("￥"+info.getReceiveamt().toString()+"元"));
        //分期总金额
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString("￥"+info.getContractamt().toString()+"元"));
        //分期期数
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getDuration().toString()));
        //手续费率
        sheet.getRow(++rownum).getCell(colnum).setCellValue(info.getServicecharge().doubleValue() * 1000);
        //合同编号
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getContractno()));
        //还款方式
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getWithholdbankname()));
        //还款帐户
        sheet.getRow(++rownum).getCell(colnum).setCellValue(new HSSFRichTextString(info.getPaybackact()));

        //打印日期
        sheet.getRow(35 + info.getDuration().intValue()-1).getCell(5).setCellValue(new HSSFRichTextString(dateformat.format(new Date())));

    }
}