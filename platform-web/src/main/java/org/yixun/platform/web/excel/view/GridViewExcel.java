package org.yixun.platform.web.excel.view;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
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
import org.yixun.support.excel.ExcelCellInfo;
import org.yixun.support.excel.ExcelCellStyle;
import org.yixun.support.excel.ExcelRowInfo;
import org.yixun.support.excel.ExcelWriter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class GridViewExcel extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String filename = "测试.xls";//设置下载时客户端Excel的名称     
		String filename = String.valueOf(model.get("fileName"));
		filename = encodeFilename(filename, request);//处理中文文件名  
        response.setContentType("application/octet-stream"); //   application/vnd.ms-excel 
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
       
        String gridJson = String.valueOf(model.get("grid"));
        Gson gson = new Gson();
        List<ExcelRow> grid = gson.fromJson(gridJson,new TypeToken<List<ExcelRow>>(){}.getType());
//        List<Object[]> data = new ArrayList<Object[]>();
//        for (ExcelRow row : grid) {
//			List<ExcelCell> cellList = row.getCell();
//			Object[] rowdata = new Object[cellList.size()];
//			int cellIndex = 0;
//			for (ExcelCell cell : cellList) {
//				rowdata[cellIndex] = cell.getText();
//				cellIndex++;
//			}
//			data.add(rowdata);
//		}
       
        List<ExcelRowInfo> data = new ArrayList<ExcelRowInfo>();
        //获得每行数据
        for (ExcelRow row : grid) {
        	//获得行中每个格
        	List<ExcelCell> cellList = row.getCell();
        	//创建传递的行对象
        	ExcelRowInfo rowdata = new ExcelRowInfo();
        	//设置行高
        	if(!StringUtils.isEmpty(row.getHeight())){
	        	String height = row.getHeight();
	        	if(height.toLowerCase().endsWith("px")){
	        		height = height.substring(0, height.length()-2);
	        	}
	        	rowdata.setHeight(Float.parseFloat(height));
        	}
        	//创建传递列集合
        	List<ExcelCellInfo> celldataList = new ArrayList<>();
        	//获得每个单元格
        	for (ExcelCell cell : cellList) {
        		//创建传递的单元格对象
        		ExcelCellInfo celldata = new ExcelCellInfo();
        		//单元格的值
        		celldata.setContent(cell.getText());
        		//创建传递的单元格样式对象
        		ExcelCellStyle cellStyle = new ExcelCellStyle();
        		//合并单元格（列）
        		if(!StringUtils.isEmpty(cell.getColspan())){
        			cellStyle.setColspan(Integer.parseInt(cell.getColspan()));
        		}
        		//单元格宽度
        		if(!StringUtils.isEmpty(cell.getWidth())){
        			String width = cell.getWidth();
    	        	if(width.toLowerCase().endsWith("px")){
    	        		width = width.substring(0, width.length()-2);
    	        	}
        			cellStyle.setWidth(Integer.parseInt(width));
        		}
        		//单元格水平对齐方式
        		if(!StringUtils.isEmpty(cell.getAlign())){
        			cellStyle.setAlign(cell.getAlign());
        		} 
        		//单元格垂直对齐方式
        		if(!StringUtils.isEmpty(cell.getValign())){
        			cellStyle.setValign(cell.getValign());
        		}
        		celldata.setCellStyle(cellStyle);
        		
        		celldataList.add(celldata);
        	}
        	rowdata.setCells(celldataList);
        	
        	data.add(rowdata);
        }
       

        
        OutputStream ouputStream = response.getOutputStream();     
        ExcelWriter excelWriter = new ExcelWriter(ouputStream);
		excelWriter.write("Company", 0, 0, data);
        
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
