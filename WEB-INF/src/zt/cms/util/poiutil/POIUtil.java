package zt.cms.util.poiutil;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.AreaReference;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.zt.util.PropertyManager;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;


/**
 * @author shiwt
 *         <p/>
 *         java��ȡexcel�ļ�
 *         <p/>
 *         һ��Excel�ļ��Ĳ�Σ�Excel�ļ�->������->��->��Ԫ�� ��Ӧ��POI�У�Ϊ��workbook->sheet->row->cell
 */
public class POIUtil {


    public static String outputFile = "e:\\test\\tels1.xls";
    public static String fileToBeRead = "e:\\test\\tels1.xls";

    public static String cellName = "dataArea";

    private IWriteOtherInfos otherinfo;

    public void setOtherinfo(IWriteOtherInfos otherinfo) {
        this.otherinfo = otherinfo;
    }


    public void CreateExcel() {
        try {
// �����µ�Excel ������
            HSSFWorkbook workbook = new HSSFWorkbook();
// ��Excel�������н�һ����������Ϊȱʡֵ
// ��Ҫ�½�һ��Ϊ"Ч��ָ��"�Ĺ����������Ϊ��
// HSSFSheet sheet = workbook.createSheet("Ч��ָ��");
            HSSFSheet sheet = workbook.createSheet();
// ������0��λ�ô����У���˵��У�
            HSSFRow row = sheet.createRow((short) 0);
//������0��λ�ô�����Ԫ�����϶ˣ�
            HSSFCell cell = row.createCell(10);
// ���嵥Ԫ��Ϊ�ַ�������
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
// �ڵ�Ԫ��������һЩ����
            cell.setCellValue(new HSSFRichTextString("����"));
// �½�һ����ļ���
            FileOutputStream fOut = new FileOutputStream(outputFile);
// ����Ӧ��Excel ����������
            workbook.write(fOut);
            fOut.flush();
// �����������ر��ļ�
            fOut.close();
            System.out.println("�ļ�����...");


        } catch (Exception e) {
            System.out.println("������ xlCreate() : " + e);
        }
    }


    /**
     * ��ȡexcel����������С���ȡ������Ϣ�����ж����Ƿ����ֻ����룬������ȷ���ֻ����������ʾ
     * <p/>
     * <p/>
     * ע�⣺ 1.sheet�� ��0��ʼ����workbook.getNumberOfSheets()-1���� 2.row��
     * ��0��ʼ(getFirstRowNum)����getLastRowNum���� 3.cell��
     * ��0��ʼ(getFirstCellNum)����getLastCellNum����, ��������Ŀ��֪ʲôԭ������ʾ�ĳ��Ȳ�ͬ�����ܻ�ƫ��
     */
    public void readExcel1() {
        //������ʾ��1.3922433397E10���ֻ���ת��Ϊ13922433397,��һ������õ�ת������
        DecimalFormat df = new DecimalFormat("#");
        try {
            // ������Excel�������ļ�������
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileToBeRead));
            for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
                if (null != workbook.getSheetAt(numSheets)) {
                    HSSFSheet aSheet = workbook.getSheetAt(numSheets);//���һ��sheet

                    for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
                            .getLastRowNum(); rowNumOfSheet++) {
                        if (null != aSheet.getRow(rowNumOfSheet)) {
                            HSSFRow aRow = aSheet.getRow(rowNumOfSheet);

                            for (int cellNumOfRow = 0; cellNumOfRow <= aRow
                                    .getLastCellNum(); cellNumOfRow++) {
                                if (null != aRow.getCell(cellNumOfRow)) {
                                    HSSFCell aCell = aRow.getCell(cellNumOfRow);
                                    int cellType = aCell.getCellType();

                                    switch (cellType) {
                                        case 0://Numeric
                                            String strCell = df.format(aCell
                                                    .getNumericCellValue());
                                            System.out.println(strCell);
                                            break;
                                        case 1://String
                                            strCell = aCell.getRichStringCellValue().getString();
                                            System.out.println(strCell);
                                            break;
                                        default:
                                            //System.out.println("��ʽ���Բ���");//������ʽ������
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ReadExcelError" + e);
        }
    }

    public HSSFWorkbook readExcel(String excelPath) {
        HSSFWorkbook result = null;
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
                    excelPath));
            result = new HSSFWorkbook(fs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * ��õ�һ��д�뵥Ԫ��λ��
     *
     * @param excelMap �ļ�����
     * @param response HttpServletResponse
     * @return tFirstCell
     * @throws SQLException 
     */
    public boolean writeExcel(HashMap excelMap, HttpServletResponse response) throws SQLException {
        String filenm = (String) excelMap.get("filenm");
        String cellname = (String) excelMap.get("cellname");
        CachedRowSet crs = (CachedRowSet) excelMap.get("crs");
        if (cellname != null) cellName = cellname;

        //�����µ�Excel ������
        HSSFWorkbook workbook = readExcel(filenm);
        if (workbook == null) workbook = new HSSFWorkbook();
        //д�����ݵ��ļ�
        if (!"������-�ڲ�Ա��.xls".equalsIgnoreCase(filenm.substring(filenm.lastIndexOf("\\") + 1)))
        	writeData(workbook, crs);

        //��Ӹ�����Ϣ
        if (this.otherinfo != null) {
            this.otherinfo.writeInfo(workbook,excelMap);
        }


        try {
            // �½�һ����ļ���
            // FileOutputStream fOut = new FileOutputStream(filenm);
            ServletOutputStream fOut = response.getOutputStream();
            // ����Ӧ��Excel ����������
            workbook.write(fOut);
            fOut.flush();
            // �����������ر��ļ�
            fOut.close();
            //System.out.println("�ļ�����...");
        } catch (Exception e) {
            System.out.println("������ writeExcel() : " + e);
            return false;
        }
        return true;
    }

    /**
     * ����ļ�����
     *
     * @param workbook ģ��workbook����
     * @param crs      д������
     */
    public void writeData(HSSFWorkbook workbook, CachedRowSet crs) {
        // ��Excel�������н�һ����������Ϊȱʡֵ
        HSSFSheet aSheet = workbook.getSheetAt(0);
        int writefoot = FootNoteRows(workbook);
        int datarow, datacol, firstrow, firstcol;

        try {
            HSSFCell firstdatacell = getFirstDataCell(workbook);
            HSSFCellStyle hc;
            HSSFRichTextString hrs;
            int celltp;
            firstrow = firstdatacell.getRowIndex();
            firstcol = firstdatacell.getColumnIndex();

            datarow = crs.size();
            datacol = crs.getMetaData().getColumnCount();

            HSSFRow row;
            HSSFCell cell;
            for (int i = 0; i < datarow; i++) {
                crs.next();
                // ������0��λ�ô����У���˵��У�
                row = aSheet.getRow(i + firstrow);
                row = (row == null) ? aSheet.createRow((short) i + firstrow) : row;

                for (int j = 0; j < datacol; j++) {
                    //������0��λ�ô�����Ԫ�����϶ˣ�
                    cell = row.getCell(j + firstcol);
                    cell = (cell == null) ? row.createCell((short) j + firstcol) : cell;
                    celltp = aSheet.getRow(firstrow).getCell(j + firstcol).getCellType();
                    hc = aSheet.getRow(firstrow).getCell(j + firstcol).getCellStyle();

                    // �ڵ�Ԫ������������
                    if (celltp == HSSFCell.CELL_TYPE_NUMERIC) {
                        cell.setCellValue(crs.getDouble(crs.getMetaData().getColumnName(j + 1)));
                    } else {
                        hrs = new HSSFRichTextString(crs.getString(crs.getMetaData().getColumnName(j + 1)));
                        cell.setCellValue(hrs);
                    }

                    // ���嵥Ԫ����ʽ
                    if (hc != null) {
                        //cell.setCellType(celltp);
                        cell.setCellStyle(hc);
                    } else
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);  // ���嵥Ԫ��Ϊ�ַ�������
                }
                if (writefoot != 0 && i + 1 < datarow) {
                    writeFootNote(aSheet, writefoot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����ļ�����
     *
     * @param excelPath �ļ�����
     * @return FileName
     */
    public String getFileName(String excelPath) {
        File file = new File(excelPath);
        return file.getName();
    }

    /**
     * ����ļ�����
     *
     * @param excelPath �ļ�����
     * @param date      �ļ�����
     * @return OutFileName
     */
    public String getOutFileName(String excelPath, String date) {
        String fn = getFileName(excelPath);
        return fn.split("\\.")[0] + date + "." + fn.split("\\.")[1];
    }


    /**
     * ��õ�һ��д�뵥Ԫ��
     *
     * @param workbook ģ��workbook����
     * @return FirstDataCell
     */
    public HSSFCell getFirstDataCell(HSSFWorkbook workbook) {
        AreaReference aref = getDataAreaReference(workbook);
        HSSFRow arow = getFirstDataRow(workbook);
        if (aref != null) {
            if (aref.getFirstCell().getRow() == aref.getLastCell().getRow())
                return arow.getCell((int) aref.getFirstCell().getCol());
        }

        return arow.getCell(0);
    }

    /**
     * ���д�뿪ʼ��
     *
     * @param workbook ģ��workbook����
     * @return FirstDataRow
     */
    public HSSFRow getFirstDataRow(HSSFWorkbook workbook) {
        HSSFSheet aSheet = workbook.getSheetAt(0);
        AreaReference aref = getDataAreaReference(workbook);
        if (aref != null) {
            if (aref.getFirstCell().getRow() == aref.getLastCell().getRow())
                return aSheet.getRow(aref.getLastCell().getRow());
        }

        CellRangeAddress aRegion = getRegion(aSheet);
        return aSheet.getRow(aRegion.getLastRow() - 1);
    }

    /**
     * ��ȡģ���ͷ����
     *
     * @param aSheet ģ��sheet����
     * @return RangeAddress
     */
    public CellRangeAddress getRegion(HSSFSheet aSheet) {
        int fr = aSheet.getFirstRowNum();
        int lr = aSheet.getPhysicalNumberOfRows() - 1;
        int fc = aSheet.getRow(lr).getFirstCellNum();
        int lc = aSheet.getRow(lr).getLastCellNum() - 1;
        return new CellRangeAddress(fr, lr, fc, lc);
    }

    /**
     * �����������
     *
     * @param workbook ģ��workbook����
     * @return DataAreaReference
     */
    public AreaReference getDataAreaReference(HSSFWorkbook workbook) {
        int namedCellIdx = workbook.getNameIndex(cellName);
        if (namedCellIdx < 0) return null;
        HSSFName aNamedCell = workbook.getNameAt(namedCellIdx);

        return new AreaReference(aNamedCell.getReference());
    }

    /**
     * ���������Ԫ������
     *
     * @param workbook ģ��workbook����
     * @return CellReference[]
     */
    public CellReference[] getCellReference(HSSFWorkbook workbook) {
        AreaReference aref = getDataAreaReference(workbook);
        if (aref == null) return null;
        return (CellReference[]) aref.getAllReferencedCells();
    }

    /**
     * ��ȡģ��
     *
     * @param excelPath �ļ�����
     * @return TemplateSheet
     */
    public HSSFSheet getTemplateSheet(String excelPath) {
        HSSFSheet aSheet = null;
        try {
            // ������Excel�������ļ�������
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelPath));
            if (null != workbook.getSheetAt(0)) {
                aSheet = workbook.getSheetAt(0);//���һ��sheet

            }
        } catch (Exception e) {
            System.out.println("ReadExcelError" + e);
            return aSheet;
        }
        return aSheet;
    }

    /**
     * ��ȡģ���ͷ����
     *
     * @param workbook ģ��workbook����
     * @return FootNoteRows
     */
    public int FootNoteRows(HSSFWorkbook workbook) {
        HSSFSheet aSheet = workbook.getSheetAt(0);
        if (getFirstDataRow(workbook).compareTo(aSheet.getRow(aSheet.getLastRowNum())) < 0) {
            return aSheet.getLastRowNum() - getFirstDataRow(workbook).getRowNum();
        }
        return 0;
    }

    /**
     * ��ȡģ���ͷ����
     *
     * @param aSheet    ģ��sheet����
     * @param writefoot ģ��β������
     */
    public void writeFootNote(HSSFSheet aSheet, int writefoot) {
        aSheet.shiftRows(aSheet.getLastRowNum() - writefoot + 1, aSheet.getLastRowNum(), 1);
    }

    public static void main
            (String[] args) {
        POIUtil poi = new POIUtil();
        poi.CreateExcel();
        //report.readExcel1();
    }
}


