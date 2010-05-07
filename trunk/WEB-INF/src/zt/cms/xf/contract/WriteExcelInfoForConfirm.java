package zt.cms.xf.contract;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import zt.cms.util.poiutil.IWriteOtherInfos;
import zt.cms.xf.common.dao.XfvconfirmprtinfoDao;
import zt.cms.xf.common.dto.Xfvconfirmprtinfo;
import zt.cms.xf.common.factory.XfvconfirmprtinfoDaoFactory;
import zt.platform.utils.Debug;

/**
 * @author LiChen
 * @see zt.cms.util.poiutil.IWriteOtherInfos
 */
public class WriteExcelInfoForConfirm implements IWriteOtherInfos {
    private static Log logger = LogFactory.getLog(XFContractListDY.class);


    @SuppressWarnings("unchecked")
	public void writeInfo(HSSFWorkbook workbook, HashMap paramap) {
        logger.debug("aaa");

        HSSFSheet sheet = workbook.getSheetAt(0);

        String appno = (String) paramap.get("APPNO");
        if (appno == null) {
            logger.error("参数（合同号）为空！");
            throw new RuntimeException();
        }

        Xfvconfirmprtinfo info;
        try {
        	XfvconfirmprtinfoDao dao = XfvconfirmprtinfoDaoFactory.create();
            info = dao.findWhereAppnoEquals(appno);
        } catch (Exception e) {
            Debug.debug(e);
            logger.error("读取合同信息（View）时有误！");
            throw new RuntimeException(e);
        }

        sheet.getRow(2).getCell(3).setCellValue(new HSSFRichTextString(info.getName()));	//姓名
        sheet.getRow(2).getCell(6).setCellValue(new HSSFRichTextString(info.getId()));	//身份证编号
        sheet.getRow(2).getCell(11).setCellValue(new HSSFRichTextString(info.getEmpno())); // 员工卡号
        sheet.getRow(3).getCell(3).setCellValue(new HSSFRichTextString(info.getCurrentaddress()));	//地址
        sheet.getRow(4).getCell(3).setCellValue(new HSSFRichTextString(info.getResidenceadr() == 1 ? "本地" : "外地"));	//户籍所在地
        sheet.getRow(4).getCell(6).setCellValue(new HSSFRichTextString(info.getPhone1()));	//联系电话
        sheet.getRow(5).getCell(3).setCellValue(new HSSFRichTextString(info.getGender() == 1 ? "男" : "女"));	//性别
        sheet.getRow(5).getCell(6).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getMarriagestatus()) ? "已婚" : "未婚/单身"));	//婚姻状况
        sheet.getRow(5).getCell(9).setCellValue(new HSSFRichTextString(info.getBirthday().toString()));	// 出生日期
        sheet.getRow(7).getCell(3).setCellValue(new HSSFRichTextString(info.getCompany()));	// 工作单位
        sheet.getRow(7).getCell(9).setCellValue(new HSSFRichTextString(info.getServform() + "年")); // 现单位工作时间
        sheet.getRow(8).getCell(3).setCellValue(new HSSFRichTextString(info.getComaddr())); 	// 单位地址
        sheet.getRow(8).getCell(9).setCellValue(new HSSFRichTextString(info.getPhone3()));	// 单位电话
        sheet.getRow(8).getCell(12).setCellValue(new HSSFRichTextString("￥" + info.getConfmonpay().toString() + "元"));	// 每月收入
        sheet.getRow(10).getCell(3).setCellValue(new HSSFRichTextString("￥" + info.getAppamt().toString() + "元"));// 申请分期金额
        sheet.getRow(10).getCell(6).setCellValue(new HSSFRichTextString(info.getDivid().toString()));	//	期数
        sheet.getRow(10).getCell(9).setCellValue(new HSSFRichTextString(info.getCommissionrate().doubleValue() * 1000 + "‰"));	//	月手续费率
        sheet.getRow(11).getCell(3).setCellValue(new HSSFRichTextString(info.getCommname()));	// 产品名称
        sheet.getRow(12).getCell(3).setCellValue(new HSSFRichTextString(info.getAddr()));	// 产品使用地址
        sheet.getRow(13).getCell(3).setCellValue(new HSSFRichTextString("￥" + (info.getAmt() == null ? "0" : info.getAmt().toString()) + "元"));	// 商品总价款
        sheet.getRow(13).getCell(7).setCellValue(new HSSFRichTextString(info.getProportion() == null ? "0" : info.getProportion().toString() + "%"));	//分期金额占所购产品价值比例
        sheet.getRow(13).getCell(9).setCellValue(new HSSFRichTextString("￥" + (info.getAppamtmon() == null ? "0" : info.getAppamtmon().toString()) + "元"));	// 每期还款额
        sheet.getRow(13).getCell(12).setCellValue(new HSSFRichTextString(info.getMonthprop().toString()));	// 月收入倍数
        String ccvalidperiod = info.getCcvalidperiod();
        String str = "";
        if("0".equals(ccvalidperiod))
        	str = "无记录";
        else if ("1".equals(ccvalidperiod))
        	str = "少于6个月记录";
        else if("2".equalsIgnoreCase(ccvalidperiod))
        	str = "6个月以上记录";
        sheet.getRow(15).getCell(2).setCellValue(new HSSFRichTextString(str));
        sheet.getRow(17).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdynum() == null ? "0" : info.getCcdynum().toString()));	// 贷款(卡)数量_抵押贷款
        sheet.getRow(17).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxynum() == null ? "0" : info.getCcxynum().toString())); // 贷款(卡)数量_信用贷款
    	sheet.getRow(17).getCell(6).setCellValue(new HSSFRichTextString(info.getCccdnum() == null ? "0" : info.getCccdnum().toString())); // 贷款(卡)数量_信用卡
        sheet.getRow(17).getCell(8).setCellValue(new HSSFRichTextString(info.getCcloanyearquerytime() == null ? "0" : info.getCcloanyearquerytime().toString() + "次"));	// 贷款审批次数
        sheet.getRow(17).getCell(11).setCellValue(new HSSFRichTextString(info.getCccardyearquerytime() == null ? "0" : info.getCccardyearquerytime().toString() + "次")); // 信用卡审批次数
        sheet.getRow(18).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdyamt() == null ? "0" : info.getCcdyamt().toString()));	// 总贷款额/额度_抵押贷款
        sheet.getRow(18).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxyamt() == null ? "0" : info.getCcxyamt().toString())); // 总贷款额/额度_信用贷款
    	sheet.getRow(18).getCell(6).setCellValue(new HSSFRichTextString(info.getCccdamt() == null ? "0" : info.getCccdamt().toString())); // 总贷款额/额度_信用卡
    	sheet.getRow(19).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdynowbal() == null ? "0" : info.getCcdynowbal().toString()));	// 总贷款余额/额度_抵押贷款
        sheet.getRow(19).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxynowbal() == null ? "0" : info.getCcxynowbal().toString())); // 总贷款余额/额度_信用贷款
    	sheet.getRow(19).getCell(6).setCellValue(new HSSFRichTextString(info.getCccdnewbal() == null ? "0" : info.getCccdnewbal().toString())); // 总贷款余额/额度_信用卡
    	sheet.getRow(19).getCell(10).setCellValue(new HSSFRichTextString("￥" + (info.getCcrpnowamt() == null ? "0" : info.getCcrpnowamt().toString()) + "元")); // 现有贷款还款额
    	sheet.getRow(20).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdyrpmon() == null ? "0" : info.getCcdyrpmon().toString()));	// 当月还款额_抵押贷款
        sheet.getRow(20).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxyrpmon() == null ? "0" : info.getCcxyrpmon().toString())); // 当月还款额_信用贷款
    	sheet.getRow(20).getCell(6).setCellValue(new HSSFRichTextString(info.getCccdrpmon() == null ? "0" : info.getCccdrpmon().toString())); // 当月还款额_信用卡
    	sheet.getRow(20).getCell(10).setCellValue(new HSSFRichTextString("￥" + (info.getAppamtmon() == null ? "0" : info.getAppamtmon().toString()) + "元")); // 该笔分期还款额
    	sheet.getRow(21).getCell(4).setCellValue(new HSSFRichTextString((info.getCcyearrptime() == null ? "0" : info.getCcyearrptime().toString()) + "次"));// 12个月还款记录(次数)
    	sheet.getRow(21).getCell(10).setCellValue(new HSSFRichTextString("￥" + (info.getCcrptotalamt() == null ? "0" : info.getCcrptotalamt().toString()) + "元"));// 总还款额
    	sheet.getRow(22).getCell(4).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getCcdynooverdue()) ? "是" : "否"));	// 无逾期_抵押贷款
        sheet.getRow(22).getCell(5).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getCcxynooverdue()) ? "是" : "否"));// 无逾期_信用贷款
    	sheet.getRow(22).getCell(6).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getCccdnooverdue()) ? "是" : "否")); // 无逾期_信用卡
    	sheet.getRow(22).getCell(10).setCellValue(new HSSFRichTextString(info.getCcrprate() == null ? "0" : info.getCcrprate().toString() + "%")); // 每月还款与收入比
    	sheet.getRow(23).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdy1timeoverdue() == null ? "0" : info.getCcdy1timeoverdue().toString()));	// 逾期1-30天(1)__抵押贷款
        sheet.getRow(23).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxy1timeoverdue() == null ? "0" : info.getCcxy1timeoverdue().toString())); // 逾期1-30天(1)__信用贷款
    	sheet.getRow(23).getCell(6).setCellValue(new HSSFRichTextString(info.getCccd1timeoverdue() == null ? "0" : info.getCccd1timeoverdue().toString())); // 逾期1-30天(1)__信用卡
    	sheet.getRow(24).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdy2timeoverdue() == null ? "0" : info.getCcdy2timeoverdue().toString()));	// 逾期1-30天(2)_抵押贷款
        sheet.getRow(24).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxy2timeoverdue() == null ? "0" : info.getCcxy2timeoverdue().toString())); // 逾期1-30天(2)_信用贷款
    	sheet.getRow(24).getCell(6).setCellValue(new HSSFRichTextString(info.getCccd2timeoverdue() == null ? "0" : info.getCccd2timeoverdue().toString())); // 逾期1-30天(2)_信用卡
    	sheet.getRow(25).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdy3timeoverdue() == null ? "0" : info.getCcdy3timeoverdue().toString()));	// 逾期1-30天(3以上)_抵押贷款
        sheet.getRow(25).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxy3timeoverdue() == null ? "0" : info.getCcxy3timeoverdue().toString())); // 逾期1-30天(3以上)_信用贷款
    	sheet.getRow(25).getCell(6).setCellValue(new HSSFRichTextString(info.getCccd3timeoverdue() == null ? "0" : info.getCccd3timeoverdue().toString())); // 逾期1-30天(3以上)_信用卡
    	sheet.getRow(27).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcage()) ? "是" : "否")); // 年龄要求
    	sheet.getRow(28).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcwage()) ? "是" : "否")); // 每月最低工资
    	sheet.getRow(29).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcemp()) ? "是" : "否")); // 员工身份_海尔正式员工
    	sheet.getRow(30).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcjob()) ? "是" : "否")); // 员工身份_外包工
    	sheet.getRow(31).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAccontract()) ? "是" : "否")); // 劳动合同年限要求
    	sheet.getRow(32).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAczx1()) ? "是" : "否")); // 征信系统内最近一期无未还款记录
    	sheet.getRow(33).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAczx2()) ? "是" : "否")); // 征信系统内过去12个月最高逾期期数在5次以下
    	sheet.getRow(34).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAczx3()) ? "是" : "否"));  // 征信系统内过去12个月最长逾期天数在60天以下
    	sheet.getRow(35).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcfacility()) ? "是" : "否")); // 信用分期总额超过人民币30,000 或12倍月收入
    	sheet.getRow(36).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcrate()) ? "是" : "否")); // 每月还款与收入比是否低于70%
    	sheet.getRow(38).getCell(2).setCellValue(new HSSFRichTextString(info.getAcacceptreason() == null ? "无" : info.getAcacceptreason())); // 提供特殊接受理由
    }
}
