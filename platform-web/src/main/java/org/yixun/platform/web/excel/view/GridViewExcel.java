package org.yixun.platform.web.excel.view;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import org.yixun.platform.web.vo.ExcelData;

import com.dayatang.excel.ExcelWriter;
import com.google.gson.Gson;


public class GridViewExcel extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String filename = "测试.xls";//设置下载时客户端Excel的名称     
		String filename = String.valueOf(model.get("fileName"));
		filename = encodeFilename(filename, request);//处理中文文件名  
        response.setContentType("application/octet-stream"); //   application/vnd.ms-excel 
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        
        int headRows = Integer.parseInt(String.valueOf(model.get("headRows")));
        String headJson = String.valueOf(model.get("head"));
        Gson gson = new Gson();
        
        Map<String, Map<String, Object>> headColMap = new HashMap<String, Map<String,Object>>();
        LinkedHashMap<String,Object> headMap = gson.fromJson(headJson, LinkedHashMap.class);
        Set<String> keySet = headMap.keySet();
        for (String key : keySet) {
			LinkedHashMap<String,Object> colMap = gson.fromJson(String.valueOf(headMap.get(key)), LinkedHashMap.class);
			headColMap.put(key, colMap);
		}
        
        List<Object[]> heads = new ArrayList<Object[]>();
        for(int i=1;i<=headRows;i++){
        	List<Object> headRow = new ArrayList<Object>();
        	
        	for(String colKey:headColMap.keySet()){
        		Map<String, Object> headCol = headColMap.get(colKey);
        		if(Double.valueOf(String.valueOf(headCol.get("__level"))).intValue() == i){
        			headRow.add(headCol.get("display"));
        		}
        	}
        	heads.add(headRow.toArray());
        }
       
        
         
       
//        String gridJson = String.valueOf(model.get("grid"));
//        List<Object[]> results = buildExcelDataFromJson(gridJson);
//        
//        HSSFWorkbook wo
        OutputStream ouputStream = response.getOutputStream();     
        ExcelWriter excelWriter = new ExcelWriter(ouputStream);
		excelWriter .write("Company", 0, 0, heads);
        
        ouputStream.flush();     
        ouputStream.close(); 
		
	}
	
	/**
	 * 将json串转换成List<Object[]>
	 * @param json
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	private List<Object[]> buildExcelDataFromJson(String json) throws JsonParseException, JsonMappingException, IOException{
		Gson gson = new Gson();
		ExcelData excelData = gson.fromJson(json, ExcelData.class);
//		ExcelData excelData = mapper.readValue(json, ExcelData.class);
		
		List<Object[]> results = new ArrayList<Object[]>();
		
		List<LinkedHashMap<String, Object>> headList = excelData.getHead();
		List<Object> headResultList = null;
		for (LinkedHashMap<String, Object> head : headList) {
			headResultList = new ArrayList<Object>();
			Set<String> keySet = head.keySet();
			for (String key : keySet) {
				headResultList.add(head.get(key));
			}
			results.add(headResultList.toArray());
		}
        
        List<LinkedHashMap<String, Object>> bodyList = excelData.getBody();
        List<Object> bodyResultList = null;
		for (LinkedHashMap<String, Object> body : bodyList) {
			bodyResultList = new ArrayList<Object>();
			Set<String> keySet = body.keySet();
			for (String key : keySet) {
				bodyResultList.add(body.get(key));
			}
			results.add(bodyResultList.toArray());
		}
       
        return results;
	}
	
	/**  
     * 设置下载文件中文件的名称  
     *     
     * @param filename  
     * @param request  
     * @return  
     */    
    public static String encodeFilename(String filename, HttpServletRequest request) {    
      /**  
       * 获取客户端浏览器和操作系统信息  
       * 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)  
       * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6  
       */    
      String agent = request.getHeader("USER-AGENT");    
      try {    
        if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {    
          String newFileName = URLEncoder.encode(filename, "UTF-8");    
          newFileName = StringUtils.replace(newFileName, "+", "%20");    
          if (newFileName.length() > 150) {    
            newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");    
            newFileName = StringUtils.replace(newFileName, " ", "%20");    
          }    
          return newFileName;    
        }    
        if ((agent != null) && (-1 != agent.indexOf("Mozilla")))    
          return MimeUtility.encodeText(filename, "UTF-8", "B");    
      
        return filename;    
      } catch (Exception ex) {    
        return filename;    
      }    
    }   
    
    /**
	 * 创建测试数据
	 * @return
	 */
	private List<Object[]> createData() {
		List<Object[]> results = new ArrayList<Object[]>();
		results.add(new Object[] {"编号", "公司", "创建日期", "撤销日期", "排序号", "上级机构", "已撤销"});
		results.add(new Object[] {"suilink", "穗灵公司", createDate("2002-7-1"), createDate("8888-1-1"), 1, null, false});
		results.add(new Object[] {"dayatang", "大雅堂公司", createDate("2004-10-1"), createDate("8888-1-1"), 2, "suilink", true});
		return results;
	}

	private Object createDate(String value) {
		try {
			return DateUtils.parseDate(value, new String[] {
					"yyyy-MM-dd",
					"yyyy-M-d",
					"yy-MM-dd",
					"yy-M-d"
			});
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
