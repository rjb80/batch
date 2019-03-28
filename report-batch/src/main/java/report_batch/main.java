package report_batch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import common.GenerateExcel;


public class main {
	
	private static final Logger logger = LoggerFactory.getLogger("main");

	public static void main(String[] args) {
		
		String ls_arg1 ="";
		String ls_arg2 ="";		
		
		//Logger logger = LoggerFactory.getLogger("main");
		
		SqlSession session =null;
		
		if(args.length >0) {
			
			ls_arg1 = args[0];
			ls_arg2 = args[1];
			
			}
		  
		
		String resource = "sqlConfig/mybatis-config.xml";
		try {
			logger.info("처리시작");
			
			retrieve(resource,ls_arg1,ls_arg2);
			
			logger.info("처리완료");
			
		}catch(Exception e) {
			
			logger.error (e.getMessage(), e);
			
		}
		
	}
	
	private static void retrieve(String as_resource,String as_from_dt,String as_to_dt ) {
		SqlSession session = null ;
		String ls_return="";
		
		try {
			InputStream inputStream = Resources.getResourceAsStream(as_resource);
			session = new SqlSessionFactoryBuilder().build(inputStream).openSession();
		}catch(IOException e1) {
			logger.error (e1.getMessage(), e1);
			return;
		}
		
			try {
			
		   	List<HashMap<String,Object>> result2 = new ArrayList();
		   	
			HashMap<String,Object> ls_map = new HashMap<String,Object>();
			
			ls_map.put("as_from_dt", as_from_dt);
			ls_map.put("as_to_dt",as_to_dt);			
			
			logger.info(as_from_dt);
			logger.info(as_to_dt);
			
			result2 = session.selectList("select_date", ls_map);			

			if(result2.size()>0) {
				
				
				for(int i=0;i<result2.size();i++){

					String ls_ymd =(String) result2.get(i).get("YMD");
					
					select_list(as_resource,ls_ymd);
					
				}
				
			}
			
			}catch(Exception e) {
				logger.error (e.getMessage(), e);
				return;
				
			}finally{
				if(session != null){
					session.close();					
				}
			}
		
	}
	
	
	
	
	private static void select_list(String as_resource,String as_date) {
		
		SqlSession session = null ;
		String ls_return="";
		

			
			try {
				InputStream inputStream = Resources.getResourceAsStream(as_resource);
				session = new SqlSessionFactoryBuilder().build(inputStream).openSession();
			}catch(IOException e1) {
				logger.error (e1.getMessage(), e1);
				return;
			}
			
			try {
			
		   	List<LinkedHashMap<String,Object>> result2 = new ArrayList();
		   	
			HashMap<String,Object> ls_map = new HashMap<String,Object>();
				
			ls_map.put("as_from_dt", as_date);
			ls_map.put("as_to_dt",as_date);
			
			
			
			String ls_report_gb ="common.select_order_list";
			
			if (ls_report_gb.equals("common.select_order_list")){
				
				ls_map.put("as_service_gb", "00");
				ls_map.put("as_site_gb", "00");				
				
			}else{
				ls_map.put("as_service_gb", "%");
				ls_map.put("as_site_gb", "%");				
			}
			
			
			result2 = session.selectList(ls_report_gb, ls_map);
			
			if(result2.size()>0) {
			
				logger.info("데이타 있음");
				
				Gson gson = new Gson();
				
		   		
		   		String ls_ret=gson.toJson(result2);
		   		
		   	
		   		
		   		/*
				
				GenerateExcel aa = new GenerateExcel();
				
				String ls_cdir ="./"+ls_report_gb + "_" + as_date +".xlsx";
				
				
				File fileex = new File(ls_cdir);
				 
				if( fileex.exists() ){
				    if(fileex.delete()){
				        logger.debug("동일파일이 있습니다.기존파일삭제 완료");
				    }else{
				    	logger.debug("동일파일이 있습니다.기존파일삭제 실패");				    	
				    }
				}else{
					logger.debug("파일이 존재하지 않습니다.");
				}
				
				
					aa.hashmaptoExcel(result2, ls_cdir);
					
					logger.debug("GENERATE EXCEL END\n");
					*/	
			}else{
				logger.info("데이타없음");
			}
			
			
		   	session.close();
		   	
		}catch(Exception e) {
			logger.error (e.getMessage(), e);
			return;
			
		}
		
	}

}
