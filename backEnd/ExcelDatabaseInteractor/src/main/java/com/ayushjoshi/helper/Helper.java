package com.ayushjoshi.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.ayushjoshi.model.Product;

public class Helper {

	public static final String EXCEL_FILE_TYPE="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
//	public static final String EXCEL_FILE_TYPE="text/csv";
	public static final String[] DB_TABLE_HEADERS= {
			"product_id",
			"product_desc",
			"product_name",
            "product_price"  			
	};
	public static final String SHEET_NAME="MyDBData.xlxs";
	
	public static boolean checkExcelFormat(MultipartFile file) {
		String contentType=file.getContentType();	
		System.out.println("contentType : "+contentType);
		if(contentType.equalsIgnoreCase(EXCEL_FILE_TYPE)) {
			return true;
		}
		return false;
	}
	
	//convert excel to list of products
	public static List<Product> convertExcelToList(InputStream iStream) {
		System.out.println("Helper.convertExcelToList()");
        List<Product> list = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(iStream);
            XSSFSheet sheet = workbook.getSheet("sheet1");
            int rowNumber = 0;
            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cells = row.iterator();
                int cid = 0;
                Product p = new Product();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    switch (cid) {
                        case 0:
                            p.setProductId((int)cell.getNumericCellValue());
                            break;
                        case 1:
                            p.setProductName(cell.getStringCellValue());
                            break;
                        case 2:
                            p.setProductDesc(cell.getStringCellValue());
                            break;
                        case 3:
                            p.setProductPrice(cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cid++;
                }
                list.add(p);
            }
            workbook.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
	
	//convert DB data to excel
	public static ByteArrayInputStream dbDataToExcel(List<Product> products) throws IOException {
		//create a work book (excel)
		Workbook workbook=new XSSFWorkbook();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try {
			//create excel sheet
			Sheet sheet=workbook.createSheet(SHEET_NAME);
			//create row
			Row row=sheet.createRow(0); 
			for(int i=0;i<DB_TABLE_HEADERS.length;i++) {
				Cell cell=row.createCell(0); //create cell inside sheet
				 cell.setCellValue(DB_TABLE_HEADERS[i]);
			}
			//set headers 
			int rowIndex=1;
			for(Product p:products) {
				Row dataRow=sheet.createRow(rowIndex);
				rowIndex++;
				dataRow.createCell(0).setCellValue(p.getProductId());
				dataRow.createCell(1).setCellValue(p.getProductName());
				dataRow.createCell(2).setCellValue(p.getProductDesc());
				dataRow.createCell(3).setCellValue(p.getProductPrice());
			}
			workbook.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to import data....");
		}finally {
			workbook.close();
			baos.close();
		}
		return null;
	}
	
	
}
