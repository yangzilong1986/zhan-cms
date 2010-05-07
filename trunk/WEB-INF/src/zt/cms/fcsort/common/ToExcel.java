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
 * <p/>Title: �弶�������WEB���ݵ���Excelservlet����Ҫ��ת����
 * <p/>===============================================
 * <p/>Description:��ҳ������ת����Excel���ṩ���ء�
 * @version $Revision: 1.2 $ $Date: 2007/05/23 06:52:27 $
 * @author zhengxin
 *  <p/>�޸ģ�$Author: zhengx $
 */
public class ToExcel {
	/**
	 * ��ҳ������ת����Excel���������������ͻ���
	 * @param titles
	 * @param sum
	 * @param listTH
	 * @return boolean
	 * @throws Exception
	 */
	public boolean exportExcelForWeb(String[] titles,String[] sum ,String  listTH,List listTR,OutputStream fileOut)throws Exception{
		boolean bool = false;
		HSSFWorkbook wb = new HSSFWorkbook();// ������HSSFWorkbook����
		HSSFSheet sheet = wb.createSheet();// �����µ�sheet����
//		wb.setSheetName(0,titles[0],HSSFWorkbook.ENCODING_UTF_16);
		wb.setSheetName(0,titles[0]);
		HSSFRow row_head = sheet.createRow((short) 0);// �����
		HSSFRow row_moedel = sheet.createRow((short) 1);// �����
		HSSFRow row_th = sheet.createRow((short) 2);//�б��ͷ
	    sheet.setColumnWidth((short)0,(short)((1 * 2) * 2560));
		String[]  ths=listTH.split(",");
		int size =ths.length-1;
		
		System.out.println("size="+size);
		sheet.addMergedRegion(new Region((short)0,(short)0,(short)0,(short)size));
		
		
		sheet.addMergedRegion(new Region((short)1,(short)0,(short)1,(short)1));
		sheet.addMergedRegion(new Region((short)1,(short)2,(short)1,(short)(size-2)));	
		sheet.addMergedRegion(new Region((short)1,(short)(size-1),(short)1,(short)size));
		
		
	
		//������
		HSSFFont cencerfont = wb.createFont();
		cencerfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	
		
		//******************�������ʽ***********************
		HSSFFont headfont = wb.createFont();
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headfont.setFontHeightInPoints((short) 15); 		
		//��ͷ��ʽ
		HSSFCellStyle headcellStyle= wb.createCellStyle();
		headcellStyle.setFont(headfont);
		//�����±߿�
		headcellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	    headcellStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    //������߿�   
	    headcellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	    headcellStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	    //�����ұ߿�  
	    headcellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	    headcellStyle.setRightBorderColor(HSSFColor.BLACK.index);      
	    //�����ϱ߿�  
	    headcellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	    headcellStyle.setTopBorderColor(HSSFColor.BLACK.index);
	    //���õ�Ԫ����뷽ʽΪ���� 
	 	headcellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER );
	 	//****************************************************

	 
	 
		
		//********************�����*************************
		HSSFCellStyle leftStyle= wb.createCellStyle();
		leftStyle.setFont(cencerfont);
		//�����±߿�
		leftStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	    leftStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    //������߿�   
	    leftStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	    leftStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	    //�����ұ߿�  
	    leftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	    leftStyle.setRightBorderColor(HSSFColor.BLACK.index);      
	    //�����ϱ߿�  
	    leftStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	    leftStyle.setTopBorderColor(HSSFColor.BLACK.index);
	    //���õ�Ԫ����뷽ʽΪ���� 
	 	leftStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT );
		//****************************************************
	
		//***********************�б���***********************
		HSSFCellStyle cencerStyle= wb.createCellStyle();
		cencerStyle.setFont(cencerfont);
		//�����±߿�
		cencerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	    cencerStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    //������߿�   
	    cencerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	    cencerStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	    //�����ұ߿�  
	    cencerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	    cencerStyle.setRightBorderColor(HSSFColor.BLACK.index);      
	    //�����ϱ߿�  
	    cencerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	    cencerStyle.setTopBorderColor(HSSFColor.BLACK.index);
	    //���õ�Ԫ����뷽ʽΪ���� 
	 	cencerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	 	//*******************************************************
	 	
	 	//**************************�ұ���******************************
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
	    rightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//�޸�
		//**************************************************************
	 
	 	
	 	
	 	
	 //*************************���ñ�ͷ***********************************************
        //�Զ����ͷ��ɫ
        HSSFPalette palette = wb.getCustomPalette();
        palette.setColorAtIndex(HSSFColor.BLUE_GREY.index, (byte) 230, (byte) 230,(byte)255);
	 	//��ͷ������ʽ
		HSSFFont thfont = wb.createFont();
		thfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		thfont.setFontHeightInPoints((short) 10); 		
		//��ͷ��ʽ
		HSSFCellStyle thStyle= wb.createCellStyle();
		thStyle.setFont(thfont);
		//�����±߿�
		thStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	    thStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    //������߿�   
	    thStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	    thStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	    //�����ұ߿�  
	    thStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	    thStyle.setRightBorderColor(HSSFColor.BLACK.index);      
	    //�����ϱ߿�  
	    thStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	    thStyle.setTopBorderColor(HSSFColor.BLACK.index);
	    //���õ�Ԫ����뷽ʽΪ���� 
	 	thStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER );
	 	//���õ�Ԫ�񱳾���ɫ
		thStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        thStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
     //*************************************************************************************   
        
     //***********************************������ʽ****************************************
        HSSFCellStyle tdStyle= wb.createCellStyle();
		
		
        tdStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        tdStyle.setBottomBorderColor(HSSFColor.BLACK.index);     
	    
        tdStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        tdStyle.setLeftBorderColor(HSSFColor.BLACK.index);      
	   
        tdStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        tdStyle.setRightBorderColor(HSSFColor.BLACK.index);      
	     
        tdStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        tdStyle.setTopBorderColor(HSSFColor.BLACK.index);
        tdStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//�޸�
	 //*****************************************************************************************
        //***********************************������ʽ2****************************************
        HSSFCellStyle tdStyle2= wb.createCellStyle();
		
		
        tdStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        tdStyle2.setBottomBorderColor(HSSFColor.BLACK.index);     
	    
        tdStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        tdStyle2.setLeftBorderColor(HSSFColor.BLACK.index);      
	   
        tdStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        tdStyle2.setRightBorderColor(HSSFColor.BLACK.index);      
	     
        tdStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        tdStyle2.setTopBorderColor(HSSFColor.BLACK.index);
        tdStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);//�޸�
	 //*****************************************************************************************
        //**********************************�ϼ���ʽ****************************************
        HSSFCellStyle tdStylesum= wb.createCellStyle();
		
		
        tdStylesum.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        tdStylesum.setBottomBorderColor(HSSFColor.BLACK.index);     
	    
        tdStylesum.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        tdStylesum.setLeftBorderColor(HSSFColor.BLACK.index);      
	   
        tdStylesum.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        tdStylesum.setRightBorderColor(HSSFColor.BLACK.index);      
	     
        tdStylesum.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        tdStylesum.setTopBorderColor(HSSFColor.BLACK.index); 
        tdStylesum.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//�޸�
        tdStylesum.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        tdStylesum.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	 //*****************************************************************************************
        
        
		//����ͳ�Ʊ�����
		for(int i=0;i<ths.length;i++){
			//HSSFCell cell = row_head.createCell((short) i);			// ������cell
			HSSFCell cell = row_head.createCell(i);			// ������cell
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);			// ����cell���������ĸ�λ�ֽڽض�
			cell.setCellStyle(headcellStyle);//���ô������ʾ��ʽ
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);// ���������Ľ���ַ���
			if(i==0)
			{
				cell.setCellValue(titles[0]);
			}
			
		}
		//�����б���
		for(int i=0;i<ths.length;i++){
			//HSSFCell cell = row_moedel.createCell((short) i);			// ������cell
			HSSFCell cell = row_moedel.createCell(i);			// ������cell
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);			// ����cell���������ĸ�λ�ֽڽض�
		
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);// ���������Ľ���ַ���
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
		//�����б��ͷ
		for(int i=0;i<ths.length;i++){
			//HSSFCell cell = row_th.createCell((short) i);			// ������cell
			HSSFCell cell = row_th.createCell(i);			// ������cell
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);			// ����cell���������ĸ�λ�ֽڽض�
			cell.setCellStyle(thStyle);//���ô������ʾ��ʽ
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);// ���������Ľ���ַ���
			cell.setCellValue(ths[i]);
		}
		
		
		//����������
		for(int i=0;i<listTR.size();i++){
			List listTd=(List)listTR.get(i); 
			HSSFRow row_tr = sheet.createRow((short)(3+i));//�б��ͷ
		//	row_tr = sheet.createRow((short) i);// ��������
			for(int j=0;j<listTd.size();j++){
				//HSSFCell cell = row_tr.createCell((short) j);			// ������cell
				HSSFCell cell = row_tr.createCell(j);			// ������cell
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
		HSSFRow row_sum = sheet.createRow((short)(listTR.size()+3));//�ϼ���
		//�����ϼ���
		for(int j=0;j<sum.length;j++){
			//HSSFCell cell = row_sum.createCell((short) j);			// ������cell
			HSSFCell cell = row_sum.createCell(j);			// ������cell
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
			System.out.println("����Excel�ļ�����"+e);
			e.printStackTrace();
		}
		return bool;
	}
	
	
	/*public static void main(String agrs[]) throws Exception
	{
		String th ="����1,����2,����3,����4,����5,����6,����7,����8,";
		List listtr = new ArrayList();
		
		for(int i=0;i<10;i++)
		{
			List listTd= new ArrayList(); 
			for(int j=0;j<8;j++)
			{
				listTd.add("��Ԫ"+i);
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
