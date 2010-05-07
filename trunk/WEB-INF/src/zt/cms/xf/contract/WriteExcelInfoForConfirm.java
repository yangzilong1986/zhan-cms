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
            logger.error("��������ͬ�ţ�Ϊ�գ�");
            throw new RuntimeException();
        }

        Xfvconfirmprtinfo info;
        try {
        	XfvconfirmprtinfoDao dao = XfvconfirmprtinfoDaoFactory.create();
            info = dao.findWhereAppnoEquals(appno);
        } catch (Exception e) {
            Debug.debug(e);
            logger.error("��ȡ��ͬ��Ϣ��View��ʱ����");
            throw new RuntimeException(e);
        }

        sheet.getRow(2).getCell(3).setCellValue(new HSSFRichTextString(info.getName()));	//����
        sheet.getRow(2).getCell(6).setCellValue(new HSSFRichTextString(info.getId()));	//���֤���
        sheet.getRow(2).getCell(11).setCellValue(new HSSFRichTextString(info.getEmpno())); // Ա������
        sheet.getRow(3).getCell(3).setCellValue(new HSSFRichTextString(info.getCurrentaddress()));	//��ַ
        sheet.getRow(4).getCell(3).setCellValue(new HSSFRichTextString(info.getResidenceadr() == 1 ? "����" : "���"));	//�������ڵ�
        sheet.getRow(4).getCell(6).setCellValue(new HSSFRichTextString(info.getPhone1()));	//��ϵ�绰
        sheet.getRow(5).getCell(3).setCellValue(new HSSFRichTextString(info.getGender() == 1 ? "��" : "Ů"));	//�Ա�
        sheet.getRow(5).getCell(6).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getMarriagestatus()) ? "�ѻ�" : "δ��/����"));	//����״��
        sheet.getRow(5).getCell(9).setCellValue(new HSSFRichTextString(info.getBirthday().toString()));	// ��������
        sheet.getRow(7).getCell(3).setCellValue(new HSSFRichTextString(info.getCompany()));	// ������λ
        sheet.getRow(7).getCell(9).setCellValue(new HSSFRichTextString(info.getServform() + "��")); // �ֵ�λ����ʱ��
        sheet.getRow(8).getCell(3).setCellValue(new HSSFRichTextString(info.getComaddr())); 	// ��λ��ַ
        sheet.getRow(8).getCell(9).setCellValue(new HSSFRichTextString(info.getPhone3()));	// ��λ�绰
        sheet.getRow(8).getCell(12).setCellValue(new HSSFRichTextString("��" + info.getConfmonpay().toString() + "Ԫ"));	// ÿ������
        sheet.getRow(10).getCell(3).setCellValue(new HSSFRichTextString("��" + info.getAppamt().toString() + "Ԫ"));// ������ڽ��
        sheet.getRow(10).getCell(6).setCellValue(new HSSFRichTextString(info.getDivid().toString()));	//	����
        sheet.getRow(10).getCell(9).setCellValue(new HSSFRichTextString(info.getCommissionrate().doubleValue() * 1000 + "��"));	//	����������
        sheet.getRow(11).getCell(3).setCellValue(new HSSFRichTextString(info.getCommname()));	// ��Ʒ����
        sheet.getRow(12).getCell(3).setCellValue(new HSSFRichTextString(info.getAddr()));	// ��Ʒʹ�õ�ַ
        sheet.getRow(13).getCell(3).setCellValue(new HSSFRichTextString("��" + (info.getAmt() == null ? "0" : info.getAmt().toString()) + "Ԫ"));	// ��Ʒ�ܼۿ�
        sheet.getRow(13).getCell(7).setCellValue(new HSSFRichTextString(info.getProportion() == null ? "0" : info.getProportion().toString() + "%"));	//���ڽ��ռ������Ʒ��ֵ����
        sheet.getRow(13).getCell(9).setCellValue(new HSSFRichTextString("��" + (info.getAppamtmon() == null ? "0" : info.getAppamtmon().toString()) + "Ԫ"));	// ÿ�ڻ����
        sheet.getRow(13).getCell(12).setCellValue(new HSSFRichTextString(info.getMonthprop().toString()));	// �����뱶��
        String ccvalidperiod = info.getCcvalidperiod();
        String str = "";
        if("0".equals(ccvalidperiod))
        	str = "�޼�¼";
        else if ("1".equals(ccvalidperiod))
        	str = "����6���¼�¼";
        else if("2".equalsIgnoreCase(ccvalidperiod))
        	str = "6�������ϼ�¼";
        sheet.getRow(15).getCell(2).setCellValue(new HSSFRichTextString(str));
        sheet.getRow(17).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdynum() == null ? "0" : info.getCcdynum().toString()));	// ����(��)����_��Ѻ����
        sheet.getRow(17).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxynum() == null ? "0" : info.getCcxynum().toString())); // ����(��)����_���ô���
    	sheet.getRow(17).getCell(6).setCellValue(new HSSFRichTextString(info.getCccdnum() == null ? "0" : info.getCccdnum().toString())); // ����(��)����_���ÿ�
        sheet.getRow(17).getCell(8).setCellValue(new HSSFRichTextString(info.getCcloanyearquerytime() == null ? "0" : info.getCcloanyearquerytime().toString() + "��"));	// ������������
        sheet.getRow(17).getCell(11).setCellValue(new HSSFRichTextString(info.getCccardyearquerytime() == null ? "0" : info.getCccardyearquerytime().toString() + "��")); // ���ÿ���������
        sheet.getRow(18).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdyamt() == null ? "0" : info.getCcdyamt().toString()));	// �ܴ����/���_��Ѻ����
        sheet.getRow(18).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxyamt() == null ? "0" : info.getCcxyamt().toString())); // �ܴ����/���_���ô���
    	sheet.getRow(18).getCell(6).setCellValue(new HSSFRichTextString(info.getCccdamt() == null ? "0" : info.getCccdamt().toString())); // �ܴ����/���_���ÿ�
    	sheet.getRow(19).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdynowbal() == null ? "0" : info.getCcdynowbal().toString()));	// �ܴ������/���_��Ѻ����
        sheet.getRow(19).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxynowbal() == null ? "0" : info.getCcxynowbal().toString())); // �ܴ������/���_���ô���
    	sheet.getRow(19).getCell(6).setCellValue(new HSSFRichTextString(info.getCccdnewbal() == null ? "0" : info.getCccdnewbal().toString())); // �ܴ������/���_���ÿ�
    	sheet.getRow(19).getCell(10).setCellValue(new HSSFRichTextString("��" + (info.getCcrpnowamt() == null ? "0" : info.getCcrpnowamt().toString()) + "Ԫ")); // ���д�����
    	sheet.getRow(20).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdyrpmon() == null ? "0" : info.getCcdyrpmon().toString()));	// ���»����_��Ѻ����
        sheet.getRow(20).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxyrpmon() == null ? "0" : info.getCcxyrpmon().toString())); // ���»����_���ô���
    	sheet.getRow(20).getCell(6).setCellValue(new HSSFRichTextString(info.getCccdrpmon() == null ? "0" : info.getCccdrpmon().toString())); // ���»����_���ÿ�
    	sheet.getRow(20).getCell(10).setCellValue(new HSSFRichTextString("��" + (info.getAppamtmon() == null ? "0" : info.getAppamtmon().toString()) + "Ԫ")); // �ñʷ��ڻ����
    	sheet.getRow(21).getCell(4).setCellValue(new HSSFRichTextString((info.getCcyearrptime() == null ? "0" : info.getCcyearrptime().toString()) + "��"));// 12���»����¼(����)
    	sheet.getRow(21).getCell(10).setCellValue(new HSSFRichTextString("��" + (info.getCcrptotalamt() == null ? "0" : info.getCcrptotalamt().toString()) + "Ԫ"));// �ܻ����
    	sheet.getRow(22).getCell(4).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getCcdynooverdue()) ? "��" : "��"));	// ������_��Ѻ����
        sheet.getRow(22).getCell(5).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getCcxynooverdue()) ? "��" : "��"));// ������_���ô���
    	sheet.getRow(22).getCell(6).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getCccdnooverdue()) ? "��" : "��")); // ������_���ÿ�
    	sheet.getRow(22).getCell(10).setCellValue(new HSSFRichTextString(info.getCcrprate() == null ? "0" : info.getCcrprate().toString() + "%")); // ÿ�»����������
    	sheet.getRow(23).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdy1timeoverdue() == null ? "0" : info.getCcdy1timeoverdue().toString()));	// ����1-30��(1)__��Ѻ����
        sheet.getRow(23).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxy1timeoverdue() == null ? "0" : info.getCcxy1timeoverdue().toString())); // ����1-30��(1)__���ô���
    	sheet.getRow(23).getCell(6).setCellValue(new HSSFRichTextString(info.getCccd1timeoverdue() == null ? "0" : info.getCccd1timeoverdue().toString())); // ����1-30��(1)__���ÿ�
    	sheet.getRow(24).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdy2timeoverdue() == null ? "0" : info.getCcdy2timeoverdue().toString()));	// ����1-30��(2)_��Ѻ����
        sheet.getRow(24).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxy2timeoverdue() == null ? "0" : info.getCcxy2timeoverdue().toString())); // ����1-30��(2)_���ô���
    	sheet.getRow(24).getCell(6).setCellValue(new HSSFRichTextString(info.getCccd2timeoverdue() == null ? "0" : info.getCccd2timeoverdue().toString())); // ����1-30��(2)_���ÿ�
    	sheet.getRow(25).getCell(4).setCellValue(new HSSFRichTextString(info.getCcdy3timeoverdue() == null ? "0" : info.getCcdy3timeoverdue().toString()));	// ����1-30��(3����)_��Ѻ����
        sheet.getRow(25).getCell(5).setCellValue(new HSSFRichTextString(info.getCcxy3timeoverdue() == null ? "0" : info.getCcxy3timeoverdue().toString())); // ����1-30��(3����)_���ô���
    	sheet.getRow(25).getCell(6).setCellValue(new HSSFRichTextString(info.getCccd3timeoverdue() == null ? "0" : info.getCccd3timeoverdue().toString())); // ����1-30��(3����)_���ÿ�
    	sheet.getRow(27).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcage()) ? "��" : "��")); // ����Ҫ��
    	sheet.getRow(28).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcwage()) ? "��" : "��")); // ÿ����͹���
    	sheet.getRow(29).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcemp()) ? "��" : "��")); // Ա�����_������ʽԱ��
    	sheet.getRow(30).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcjob()) ? "��" : "��")); // Ա�����_�����
    	sheet.getRow(31).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAccontract()) ? "��" : "��")); // �Ͷ���ͬ����Ҫ��
    	sheet.getRow(32).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAczx1()) ? "��" : "��")); // ����ϵͳ�����һ����δ�����¼
    	sheet.getRow(33).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAczx2()) ? "��" : "��")); // ����ϵͳ�ڹ�ȥ12�����������������5������
    	sheet.getRow(34).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAczx3()) ? "��" : "��"));  // ����ϵͳ�ڹ�ȥ12���������������60������
    	sheet.getRow(35).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcfacility()) ? "��" : "��")); // ���÷����ܶ�������30,000 ��12��������
    	sheet.getRow(36).getCell(8).setCellValue(new HSSFRichTextString("1".equalsIgnoreCase(info.getAcrate()) ? "��" : "��")); // ÿ�»�����������Ƿ����70%
    	sheet.getRow(38).getCell(2).setCellValue(new HSSFRichTextString(info.getAcacceptreason() == null ? "��" : info.getAcacceptreason())); // �ṩ�����������
    }
}
