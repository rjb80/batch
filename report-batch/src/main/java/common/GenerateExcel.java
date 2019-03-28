package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateExcel {
	
	Logger logger = LoggerFactory.getLogger("GenerateExcel");

    private static final int HEADER_ROW = 0;

    private static final int DATA_ROW = 1;	
	
	public GenerateExcel() {
		
		// TODO Auto-generated constructor stub
	}

	public void hashmaptoExcel(List<LinkedHashMap<String,Object>> mlist,String filepathname){
		  try {		
				
			  //dir1.getCanonicalPath()
			  //dir1.
			  		
			  		File dir1 = new File (".");
//			  		File dir2 = new File ("..");
//			  		 try {
	//		  		       System.out.println ("Current dir : " + dir1.getCanonicalPath());
		//	  		       System.out.println ("Parent  dir : " + dir2.getCanonicalPath());
			  		       
			  		       
			  		       //String ls_cdir = dir1.getCanonicalPath().toString()
			  		       
//			  		} catch(Exception e) {
	//		  		       e.printStackTrace();
		//	  		}
			  		
			  		String ls_cdir = dir1.getCanonicalPath().toString();
			 
			  		logger.debug(ls_cdir);
			  		
					FileInputStream inputStream = new FileInputStream(ls_cdir + "/" + "mytemplate.xlsx");
			  		//FileInputStream inputStream = new FileInputStream("./mytemplate.xlsx");
					XSSFWorkbook  wb_template = new XSSFWorkbook(inputStream);
					inputStream.close();
					
					SXSSFWorkbook hssfWorkBook = new SXSSFWorkbook(wb_template);
					
			  		hssfWorkBook.setCompressTempFiles(true);
			  		
			  		
			  		
					generateWorksheet("sheet1", hssfWorkBook, mlist);
					
					FileOutputStream fileOut = new FileOutputStream(filepathname, true);
			       hssfWorkBook.write(fileOut);
			        fileOut.close();	
		
		   } catch (Exception e) {
			   e.printStackTrace();
	        }		
	}
	
	public void rstoExcel(ResultSet rs,String filepathname){
	
		
		  try {		 
		
			  		File dir1 = new File (".");

			  		
			  		String ls_cdir = dir1.getCanonicalPath().toString();
			  
					//FileInputStream inputStream = new FileInputStream(ls_cdir + "/" + "mytemplate.xlsx");
			  		FileInputStream inputStream = new FileInputStream("./mytemplate.xlsx");
					XSSFWorkbook  wb_template = new XSSFWorkbook(inputStream);
					inputStream.close();
					
					SXSSFWorkbook hssfWorkBook = new SXSSFWorkbook(wb_template);
					
			  		hssfWorkBook.setCompressTempFiles(true);
			  		
			  		
			  		
					generateWorksheet("sheet1", hssfWorkBook, rs);
					
					FileOutputStream fileOut = new FileOutputStream(filepathname, true);
			       hssfWorkBook.write(fileOut);
			        fileOut.close();	
		
		   } catch (Exception e) {
			   e.printStackTrace();
	        }			        
	}
	
	
	  private static void generateHeaderRow(SXSSFSheet sheet, List<LinkedHashMap<String,Object>> mlist) throws SQLException {

		  Row headerRow = sheet.createRow(HEADER_ROW);

		  LinkedHashMap<String,Object> map =  mlist.get(0);
				
				Iterator<String> keys2 = map.keySet().iterator();
				
				int i = 0;
				
				while(keys2.hasNext()){
					
					String key = keys2.next();
					
					Cell cell = headerRow.createCell(i);
		            cell.setCellValue(key);					
		            i = i+1;
		
				}

	    }	
	  
	  
	  private static void generateHeaderRow(SXSSFSheet sheet, ResultSet rs) throws SQLException {

		  Row headerRow = sheet.createRow(HEADER_ROW);

	        ResultSetMetaData resultsetMetadata = rs.getMetaData();
	        int columnCount = resultsetMetadata.getColumnCount();

	        for (int i = 0; i < columnCount; i++) {
	        	Cell cell = headerRow.createCell(i);
	            cell.setCellValue(resultsetMetadata.getColumnName(i + 1));
	        }
	    }		  
	
	    private static void populateRows(SXSSFSheet sheet, List<LinkedHashMap<String,Object>> mlist) throws SQLException {
	        int rowCounter = DATA_ROW;
	        
	    	   for(int i = 0; i < mlist.size(); i++) {
	    		   Row row = sheet.createRow(rowCounter);
			
	    		   LinkedHashMap<String,Object> map =  mlist.get(i);
				
				Iterator<String> keys2 = map.keySet().iterator();
				
				int y = 0;
				
				while(keys2.hasNext()){
					
					String key = keys2.next();

					if (map.get(key) != null && map.get(key).toString().length() != 0 && map.get(key).toString().equals("null") ==false) {
						//System.out.print("11");
							
						
						if(map.get(key).getClass().getName().equals("java.lang.String")){
						   row.createCell(y).setCellValue("" + map.get(key).toString());
						   
						}else{
							BigDecimal ld_amt =new BigDecimal(map.get(key).toString());
							//ld_amt = map.get(key);
							row.createCell(y).setCellValue(ld_amt.longValue());
							
						}
					} else { 
						// null 일경우는 처리하지 않음
					}
		            y = y+1;
				
				}


				rowCounter = rowCounter + 1;				
				
	       }		        
	        	        
	  
	    }	  	  
	  
	    private static void populateRows(SXSSFSheet sheet, ResultSet rs) throws SQLException {
	        int rowCounter = DATA_ROW;
	        while (rs.next()) {

	        	Row row = sheet.createRow(rowCounter);
	            int columnCount = rs.getMetaData().getColumnCount();

	            int coltype = 0;
	            
	            for (int i = 0; i < columnCount; i++) {
	            	
	            	coltype = rs.getMetaData().getColumnType(i + 1);	            	
	            	
	            	if(coltype==-6 || coltype==5 || coltype==4 || coltype==-5 || coltype==6 || coltype==7 || coltype==8 || coltype==2 || coltype==3){
		                row.createCell(i).setCellValue(rs.getDouble(i + 1));	            		
	            	}else{
	                row.createCell(i).setCellValue(rs.getString(i + 1));
	            	}
	            }
	            rowCounter++;
	            
	            
	           if((rowCounter%10000) ==0){
	        	   System.out.println(rowCounter);

	    
	            }
	        }
	        
	        
	 
	    }	  
	  
	    
	    private static void generateWorksheet(String workSheetName, SXSSFWorkbook workbook, ResultSet resultSet) throws SQLException {
	    
	    	
	    	SXSSFSheet workSheet = (SXSSFSheet) workbook.getSheetAt(0);

	    	workSheet.setRandomAccessWindowSize(100);// keep 100 rows in memory, exceeding rows will be flushed to disk	    	

	  	  try {	        
	        
		        // Create the first Header row
		        // Get all the column names from the ResultSet
		        generateHeaderRow(workSheet, resultSet);
	
		        // Populate the data in the rows
		        populateRows(workSheet, resultSet);
		    	
		   } catch (Exception e) {
			   e.printStackTrace();
	        }					        
	    }
	    
	    
    private static void generateWorksheet(String workSheetName, SXSSFWorkbook workbook, List<LinkedHashMap<String,Object>> mlist) throws SQLException {
	    
	    	
	    	SXSSFSheet workSheet = (SXSSFSheet) workbook.getSheetAt(0);

	    	workSheet.setRandomAccessWindowSize(100);// keep 100 rows in memory, exceeding rows will be flushed to disk	    	

	  	  try {	        
	        
		        // Create the first Header row
		        // Get all the column names from the ResultSet
		        generateHeaderRow(workSheet, mlist);
	
		        // Populate the data in the rows
		        populateRows(workSheet, mlist);
		    	
		   } catch (Exception e) {
			   e.printStackTrace();
	        }					        
	    }	 	    
	    
}
