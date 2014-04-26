package org.yixun.platform.web.excel;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yixun.platform.web.excel.view.GridViewExcel;
import org.yixun.platform.web.excel.view.ViewExcel;
import org.yixun.support.excel.ExcelRangeData;
import org.yixun.support.excel.ExcelReader;
import org.yixun.support.excel.Version;
import org.yixun.support.i18n.I18NManager;

import com.google.gson.Gson;




@Controller
@RequestMapping("/excel")
public class ExcelController {
	private Logger logger = LoggerFactory.getLogger(ExcelController.class);
	@RequestMapping("/exportByHtml")
	public ModelAndView exportByHtml(String fileName,String grid,HttpServletResponse response) throws Exception{
		ViewExcel viewExcel = new ViewExcel();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("fileName", fileName);
		model.put("grid", grid);
		
		return new ModelAndView(viewExcel, model);
	}
	
	@RequestMapping("/exportByData")
	public ModelAndView exportByData(String fileName,String grid) throws Exception{
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("fileName", fileName);
		model.put("grid", grid);
		
		return new ModelAndView(new GridViewExcel(), model);
	}
	
	@RequestMapping("/upload")
	@ResponseBody
	public Map<String, Object> upload(MultipartFile file,HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<>();
	
		if(file.isEmpty()){
			result.put("result", I18NManager.getMessage("请选择文件!"));
		} else {
			try {
				 //获取保存的路径
				String path = request.getSession().getServletContext().getRealPath("/upload");
				
				//获得文件名
				String fileName = file.getOriginalFilename();
				int idx = fileName.lastIndexOf(".");
				String newFileName;
				if(idx != -1){
					//获得文件后缀名
					String suffix = fileName.substring(idx);
					String prefix = fileName.substring(0,idx);
					//拼接文件路径,以UUID生成文件名
					newFileName = prefix + "_" + UUID.randomUUID().toString() + suffix;
				} else {
					newFileName = fileName + "_" + UUID.randomUUID().toString();
				}
				//进行文件保存
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path, newFileName));
				result.put("result", I18NManager.getMessage("文件上传成功!"));
			} catch (Exception e) {
				logger.error(e.getMessage());
				result.put("result", I18NManager.getMessage("文件上传失败!"));
			}
		}
		return result;
	}
	
	@RequestMapping("/uploadExcel")
	@ResponseBody
	public Map<String, Object> uploadExcel(MultipartFile file,HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<>();
	
		if(file.isEmpty()){
			result.put("result", I18NManager.getMessage("请选择文件!"));
		} else {
			try {
				 //获取保存的路径
				String path = request.getSession().getServletContext().getRealPath("/upload");
				
				//获得文件名
				String fileName = file.getOriginalFilename();
				int idx = fileName.lastIndexOf(".");
				Version version = Version.XLS;
				if(idx != -1){
					//获得文件后缀名
					String suffix = fileName.substring(idx+1);
					if("xlsx".equalsIgnoreCase(suffix)){
						version = Version.XLSX;
					}
				}
				
				ExcelReader importer = ExcelReader.builder().inputStream(file.getInputStream(), version).sheetAt(0).rowFrom(0).build();
				ExcelRangeData data = importer.read();
				int rowCount = data.getRowCount();
				
//				for(int i=0;i<rowCount;i++){
//					int columnCount = data.getColumnCount(i);
//					for(int j=0;j<columnCount;j++){
//						System.out.print(data.getString(i, j) + "\t");
//					}
//					System.out.println();
//				}
				Gson gson = new Gson();

				result.put("result", I18NManager.getMessage("文件上传成功!"));
				result.put("status", "success");
				result.put("data", gson.toJson(data.getData()));
			} catch (Exception e) {
				logger.error(e.getMessage());
				result.put("result", I18NManager.getMessage("文件上传失败!"));
			}
		}
		return result;
	}
}
