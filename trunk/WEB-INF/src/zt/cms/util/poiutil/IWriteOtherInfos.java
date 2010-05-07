package zt.cms.util.poiutil;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.HashMap;

/**
 * 在EXCEL中在指定的位置添加附加信息.
 * User: zhanrui
 * Date: 2009-9-27
 * Time: 13:58:04
 * To change this template use File | Settings | File Templates.
 */
public interface IWriteOtherInfos {
    void writeInfo(HSSFWorkbook workbook, HashMap paramap);
}
