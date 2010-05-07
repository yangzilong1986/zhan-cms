package zt.cms.fcsort.common;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.*;
import  org.apache.poi.hssf.util.Region;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类贷款WEB数据导成Excelservlet所需要的转换类
 * <p/>===============================================
 * <p/>Description:将页面数据转换成Excel并提供下载。
 * @version $Revision: 1.2 $ $Date: 2007/05/23 06:52:27 $
 * @author zhengxin
 *  <p/>修改：$Author: zhengx $
 */
public class ToExcel {
	/**
	 * 将页面数据转换成Excel并从输出流输出到客户端
	 * @param titles
	 * @param sum
	 * @param listTH
	 * @return boolean
	 * @throws Exception
	 */
	public boolean exportExcelForWeb(String[] titles,String[] sum ,String  listTH,List listTR,OutputStream fileOut)throws Exception{
		boolean bool = false;
		HSSFWorkbook wb = new HSSFWorkbook();// 建立新HSSFWorkbook对象
		HSSFSheet sheet = wb.createSheet();// 建立新的sheet对象
//		wb.setSheetName(0,titles[0],HSSFWorkbook.ENCODING_UTF_16);
		wb.setSheetName(0,titles[0]);
		HSSFRow row_head = sheet.createRow((short) 0);// 大标题
		HSSFRow row_moedel = sheet.createRow((short) 1);// 大标题
		HSSFRow row_th = sheet.createRow((short) 2);//列表表头
	    sheet.setColumnWidth((short)0,(short)((1 * 2) * 2560));
		String[]  ths=listTH.split(",");
		int size =ths.length-1;
		
		System.out.println("size="+size);
		sheet.addMergedRegion(new Region((short)0,(short)0,(short)0,(short)size));
		
		
		sheet.addMergedRegion(new Region((short)1,(short)0,(short)1,(short)1));
		sheet.addMergedRegion(new Region((short)1,(short)2,(short)1,(short)(size-2)));	
		sheet.addMergedRegion(new Region((short)1,(short)(size-1),(short)1,(short)size));
		
		
	
		//黑体字
		HSSFFont cencerfont = wb.createFont();
		cencerfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	
		
		//******************大标题样式***********************
		HSSFFont headfont = wb.createFont();
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headfont.setFontHeightInPoints((short) 15); 		
		//表头样式
		HSSFCellStyle headcellStyle= wb.createCellStyle();
		headcellStyle.setFont(headfont);
		//设置下边框
		headcellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	    headcellStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    //设置左边框   
	    headcellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	    headcellStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	    //设置右边框  
	    headcellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	    headcellStyle.setRightBorderColor(HSSFColor.BLACK.index);      
	    //设置上边框  
	    headcellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	    headcellStyle.setTopBorderColor(HSSFColor.BLACK.index);
	    //设置单元格对齐方式为剧中 
	 	headcellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER );
	 	//****************************************************

	 
	 
		
		//********************左标题*************************
		HSSFCellStyle leftStyle= wb.createCellStyle();
		leftStyle.setFont(cencerfont);
		//设置下边框
		leftStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	    leftStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    //设置左边框   
	    leftStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	    leftStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	    //设置右边框  
	    leftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	    leftStyle.setRightBorderColor(HSSFColor.BLACK.index);      
	    //设置上边框  
	    leftStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	    leftStyle.setTopBorderColor(HSSFColor.BLACK.index);
	    //设置单元格对齐方式为剧中 
	 	leftStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT );
		//****************************************************
	
		//***********************中标题***********************
		HSSFCellStyle cencerStyle= wb.createCellStyle();
		cencerStyle.setFont(cencerfont);
		//设置下边框
		cencerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	    cencerStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    //设置左边框   
	    cencerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	    cencerStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	    //设置右边框  
	    cencerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	    cencerStyle.setRightBorderColor(HSSFColor.BLACK.index);      
	    //设置上边框  
	    cencerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	    cencerStyle.setTopBorderColor(HSSFColor.BLACK.index);
	    //设置单元格对齐方式为剧中 
	 	cencerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	 	//*******************************************************
	 	
	 	//**************************右标题******************************
	 	HSSFCellStyle rightStyle= wb.createCellStyle();
		rightStyle.setFont(cencerfont);
		rightStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	    rightStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    rightStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	    rightStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	    rightStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	    rightStyle.setRightBorderColor(HSSFColor.BLACK.index);       
	    rightStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	    rightStyle.setTopBorderColor(HSSFColor.BLACK.index);
	    rightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//修改
		//**************************************************************
	 
	 	
	 	
	 	
	 //*************************设置表头***********************************************
        //自定义表头颜色
        HSSFPalette palette = wb.getCustomPalette();
        palette.setColorAtIndex(HSSFColor.BLUE_GREY.index, (byte) 230, (byte) 230,(byte)255);
	 	//表头字体样式
		HSSFFont thfont = wb.createFont();
		thfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		thfont.setFontHeightInPoints((short) 10); 		
		//表头样式
		HSSFCellStyle thStyle= wb.createCellStyle();
		thStyle.setFont(thfont);
		//设置下边框
		thStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	    thStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    //设置左边框   
	    thStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	    thStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	    //设置右边框  
	    thStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	    thStyle.setRightBorderColor(HSSFColor.BLACK.index);      
	    //设置上边框  
	    thStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	    thStyle.setTopBorderColor(HSSFColor.BLACK.index);
	    //设置单元格对齐方式为剧中 
	 	thStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER );
	 	//设置单元格背景颜色
		thStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        thStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
     //*************************************************************************************   
        
     //***********************************数据样式****************************************
        HSSFCellStyle tdStyle= wb.createCellStyle();
		
		
        tdStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        tdStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    
        tdStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        tdStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	   
        tdStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        tdStyle.setRightBorderColor(HSSFColor.BLACK.index);      
	     
        tdStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        tdStyle.setTopBorderColor(HSSFColor.BLACK.index);
        tdStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//修改
	 //*****************************************************************************************
        //***********************************数据样式2****************************************
        HSSFCellStyle tdStyle2= wb.createCellStyle();
		
		
        tdStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        tdStyle2.setBottomBorderColor(HSSFColor.BLACK.index);     
	    
        tdStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        tdStyle2.setLeftBorderColor(HSSFColor.BLACK.index);      
	   
        tdStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        tdStyle2.setRightBorderColor(HSSFColor.BLACK.index);      
	     
        tdStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        tdStyle2.setTopBorderColor(HSSFColor.BLACK.index);
        tdStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);//修改
	 //*****************************************************************************************
        //**********************************合计样式****************************************
        HSSFCellStyle tdStylesum= wb.createCellStyle();
		
		
        tdStylesum.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        tdStylesum.setBottomBorderColor(HSSFColor.BLACK.index);     
	    
        tdStylesum.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        tdStylesum.setLeftBorderColor(HSSFColor.BLACK.index);      
	   
        tdStylesum.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        tdStylesum.setRightBorderColor(HSSFColor.BLACK.index);      
	     
        tdStylesum.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        tdStylesum.setTopBorderColor(HSSFColor.BLACK.index); 
        tdStylesum.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//修改
        tdStylesum.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        tdStylesum.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	 //*****************************************************************************************
        
        
		//创建统计表大标题
		for(int i=0;i<ths.length;i++){
			//HSSFCell cell = row_head.createCell((short) i);			// 建立新cell
			HSSFCell cell = row_head.createCell(i);			// 建立新cell
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);			// 设置cell编码解决中文高位字节截断
			cell.setCellStyle(headcellStyle);//设置大标题显示样式
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置中西文结合字符串
			if(i==0)
			{
				cell.setCellValue(titles[0]);
			}
			
		}
		//创建中标题
		for(int i=0;i<ths.length;i++){
			//HSSFCell cell = row_moedel.createCell((short) i);			// 建立新cell
			HSSFCell cell = row_moedel.createCell(i);			// 建立新cell
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);			// 设置cell编码解决中文高位字节截断
		
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置中西文结合字符串
			if(i==0)
			{
				cell.setCellStyle(leftStyle);
				cell.setCellValue(titles[1]);
			} else
			if(i==2)
			{
				cell.setCellStyle(cencerStyle);
				cell.setCellValue(titles[2]);
			} else
			if(i==(ths.length-2))
			{		cell.setCellStyle(rightStyle);
					cell.setCellValue(titles[3]);
			}
			else
			{
				cell.setCellStyle(rightStyle);
			}
			
			
		}
		//创建列表表头
		for(int i=0;i<ths.length;i++){
			//HSSFCell cell = row_th.createCell((short) i);			// 建立新cell
			HSSFCell cell = row_th.createCell(i);			// 建立新cell
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);			// 设置cell编码解决中文高位字节截断
			cell.setCellStyle(thStyle);//设置大标题显示样式
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置中西文结合字符串
			cell.setCellValue(ths[i]);
		}
		
		
		//创建数据列
		for(int i=0;i<listTR.size();i++){
			List listTd=(List)listTR.get(i); 
			HSSFRow row_tr = sheet.createRow((short)(3+i));//列表表头
		//	row_tr = sheet.createRow((short) i);// 建立新行
			for(int j=0;j<listTd.size();j++){
				//HSSFCell cell = row_tr.createCell((short) j);			// 建立新cell
				HSSFCell cell = row_tr.createCell(j);			// 建立新cell
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				/*if(j==1||j==2)
				{
					cell.setCellStyle(tdStyle);	
					cell.setCellValue(Integer.valueOf((String)listTd.get(j)).intValue());
				}
					
				else if(j>2)
				{
					cell.setCellStyle(tdStyle);	
					cell.setCellValue(Double.valueOf((String)listTd.get(j)).doubleValue());
				}
					
				else
				{*/
					cell.setCellStyle(tdStyle2);	
					cell.setCellValue((String)listTd.get(j));
				//}
					
				
			}
		}
		HSSFRow row_sum = sheet.createRow((short)(listTR.size()+3));//合计列
		//创建合计列
		for(int j=0;j<sum.length;j++){
			//HSSFCell cell = row_sum.createCell((short) j);			// 建立新cell
			HSSFCell cell = row_sum.createCell(j);			// 建立新cell
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							
			if((j==1)||(j==2))
			{
				cell.setCellStyle(tdStylesum);
				//cell.setCellValue(Integer.valueOf((String)sum[j]).intValue());
			}
				
			else if(j>2)
			{
				cell.setCellStyle(tdStylesum);
				//cell.setCellValue(Double.valueOf((String)sum[j]).doubleValue());
			}	
			else
			{
				cell.setCellStyle(thStyle);
				
			}
			cell.setCellValue(sum[j]);	
			
		}
		
		try {
		
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			bool = true;
		} catch (Exception e) {
			System.out.println("导出Excel文件错误："+e);
			e.printStackTrace();
		}
		return bool;
	}
	
	
	/*public static void main(String agrs[]) throws Exception
	{
		String th ="标题1,标题2,标题3,标题4,标题5,标题6,标题7,标题8,";
		List listtr = new ArrayList();
		
		for(int i=0;i<10;i++)
		{
			List listTd= new ArrayList(); 
			for(int j=0;j<8;j++)
			{
				listTd.add("单元"+i);
			}
			listtr.add(listTd);
		}
		
		
		FileOutputStream fileOut = new FileOutputStream("C:\\ToExcel.xls");
		ToExcel test = new ToExcel();
		if(test.exportExcelForWeb(th,listtr,fileOut))
		System.out.println("ok!");
		else
		System.out.println("bad!"); 
	}*/
}
