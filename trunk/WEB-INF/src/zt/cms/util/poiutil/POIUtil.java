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
 *         java读取excel文件
 *         <p/>
 *         一个Excel文件的层次：Excel文件->工作表->行->单元格 对应到POI中，为：workbook->sheet->row->cell
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
// 创建新的Excel 工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
// 在Excel工作簿中建一工作表，其名为缺省值
// 如要新建一名为"效益指标"的工作表，其语句为：
// HSSFSheet sheet = workbook.createSheet("效益指标");
            HSSFSheet sheet = workbook.createSheet();
// 在索引0的位置创建行（最顶端的行）
            HSSFRow row = sheet.createRow((short) 0);
//在索引0的位置创建单元格（左上端）
            HSSFCell cell = row.createCell(10);
// 定义单元格为字符串类型
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
// 在单元格中输入一些内容
            cell.setCellValue(new HSSFRichTextString("中文"));
// 新建一输出文件流
            FileOutputStream fOut = new FileOutputStream(outputFile);
// 把相应的Excel 工作簿存盘
            workbook.write(fOut);
            fOut.flush();
// 操作结束，关闭文件
            fOut.close();
            System.out.println("文件生成...");


        } catch (Exception e) {
            System.out.println("已运行 xlCreate() : " + e);
        }
    }


    /**
     * 读取excel，遍历各个小格获取其中信息，并判断其是否是手机号码，并对正确的手机号码进行显示
     * <p/>
     * <p/>
     * 注意： 1.sheet， 以0开始，以workbook.getNumberOfSheets()-1结束 2.row，
     * 以0开始(getFirstRowNum)，以getLastRowNum结束 3.cell，
     * 以0开始(getFirstCellNum)，以getLastCellNum结束, 结束的数目不知什么原因与显示的长度不同，可能会偏长
     */
    public void readExcel1() {
        //将被表示成1.3922433397E10的手机号转化为13922433397,不一定是最好的转换方法
        DecimalFormat df = new DecimalFormat("#");
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileToBeRead));
            for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
                if (null != workbook.getSheetAt(numSheets)) {
                    HSSFSheet aSheet = workbook.getSheetAt(numSheets);//获得一个sheet

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
                                            //System.out.println("格式不对不读");//其它格式的数据
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
     * 获得第一个写入单元格位置
     *
     * @param excelMap 文件内容
     * @param response HttpServletResponse
     * @return tFirstCell
     * @throws SQLException 
     */
    public boolean writeExcel(HashMap excelMap, HttpServletResponse response) throws SQLException {
        String filenm = (String) excelMap.get("filenm");
        String cellname = (String) excelMap.get("cellname");
        CachedRowSet crs = (CachedRowSet) excelMap.get("crs");
        if (cellname != null) cellName = cellname;

        //创建新的Excel 工作簿
        HSSFWorkbook workbook = readExcel(filenm);
        if (workbook == null) workbook = new HSSFWorkbook();
        //写入数据到文件
        if (!"审批单-内部员工.xls".equalsIgnoreCase(filenm.substring(filenm.lastIndexOf("\\") + 1)))
        	writeData(workbook, crs);

        //添加附加信息
        if (this.otherinfo != null) {
            this.otherinfo.writeInfo(workbook,excelMap);
        }


        try {
            // 新建一输出文件流
            // FileOutputStream fOut = new FileOutputStream(filenm);
            ServletOutputStream fOut = response.getOutputStream();
            // 把相应的Excel 工作簿存盘
            workbook.write(fOut);
            fOut.flush();
            // 操作结束，关闭文件
            fOut.close();
            //System.out.println("文件生成...");
        } catch (Exception e) {
            System.out.println("已运行 writeExcel() : " + e);
            return false;
        }
        return true;
    }

    /**
     * 获得文件名称
     *
     * @param workbook 模板workbook对象
     * @param crs      写入数据
     */
    public void writeData(HSSFWorkbook workbook, CachedRowSet crs) {
        // 在Excel工作簿中建一工作表，其名为缺省值
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
                // 在索引0的位置创建行（最顶端的行）
                row = aSheet.getRow(i + firstrow);
                row = (row == null) ? aSheet.createRow((short) i + firstrow) : row;

                for (int j = 0; j < datacol; j++) {
                    //在索引0的位置创建单元格（左上端）
                    cell = row.getCell(j + firstcol);
                    cell = (cell == null) ? row.createCell((short) j + firstcol) : cell;
                    celltp = aSheet.getRow(firstrow).getCell(j + firstcol).getCellType();
                    hc = aSheet.getRow(firstrow).getCell(j + firstcol).getCellStyle();

                    // 在单元格中输入内容
                    if (celltp == HSSFCell.CELL_TYPE_NUMERIC) {
                        cell.setCellValue(crs.getDouble(crs.getMetaData().getColumnName(j + 1)));
                    } else {
                        hrs = new HSSFRichTextString(crs.getString(crs.getMetaData().getColumnName(j + 1)));
                        cell.setCellValue(hrs);
                    }

                    // 定义单元格样式
                    if (hc != null) {
                        //cell.setCellType(celltp);
                        cell.setCellStyle(hc);
                    } else
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);  // 定义单元格为字符串类型
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
     * 获得文件名称
     *
     * @param excelPath 文件名称
     * @return FileName
     */
    public String getFileName(String excelPath) {
        File file = new File(excelPath);
        return file.getName();
    }

    /**
     * 获得文件名称
     *
     * @param excelPath 文件名称
     * @param date      文件日期
     * @return OutFileName
     */
    public String getOutFileName(String excelPath, String date) {
        String fn = getFileName(excelPath);
        return fn.split("\\.")[0] + date + "." + fn.split("\\.")[1];
    }


    /**
     * 获得第一个写入单元格
     *
     * @param workbook 模板workbook对象
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
     * 获得写入开始行
     *
     * @param workbook 模板workbook对象
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
     * 读取模板表头区域
     *
     * @param aSheet 模板sheet对象
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
     * 获得命名区域
     *
     * @param workbook 模板workbook对象
     * @return DataAreaReference
     */
    public AreaReference getDataAreaReference(HSSFWorkbook workbook) {
        int namedCellIdx = workbook.getNameIndex(cellName);
        if (namedCellIdx < 0) return null;
        HSSFName aNamedCell = workbook.getNameAt(namedCellIdx);

        return new AreaReference(aNamedCell.getReference());
    }

    /**
     * 获得命名单元格数组
     *
     * @param workbook 模板workbook对象
     * @return CellReference[]
     */
    public CellReference[] getCellReference(HSSFWorkbook workbook) {
        AreaReference aref = getDataAreaReference(workbook);
        if (aref == null) return null;
        return (CellReference[]) aref.getAllReferencedCells();
    }

    /**
     * 读取模板
     *
     * @param excelPath 文件名称
     * @return TemplateSheet
     */
    public HSSFSheet getTemplateSheet(String excelPath) {
        HSSFSheet aSheet = null;
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelPath));
            if (null != workbook.getSheetAt(0)) {
                aSheet = workbook.getSheetAt(0);//获得一个sheet

            }
        } catch (Exception e) {
            System.out.println("ReadExcelError" + e);
            return aSheet;
        }
        return aSheet;
    }

    /**
     * 读取模板表头区域
     *
     * @param workbook 模板workbook对象
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
     * 读取模板表头区域
     *
     * @param aSheet    模板sheet对象
     * @param writefoot 模板尾部内容
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


