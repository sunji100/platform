package org.yixun.platform.web.file;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.yixun.platform.web.auth.util.AuthDetailUtil;
import org.yixun.support.date.util.DateUtils;
import org.yixun.support.i18n.I18NManager;
import org.yixun.support.properties.util.RWProperties;

@Controller
@RequestMapping("/file")
public class FileController {
	private Logger logger = LoggerFactory.getLogger(FileController.class);
	
	/**
	 * 文件上传
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public Map<String, Object> upload(MultipartFile file,HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<>();
	
		if(file.isEmpty()){
			result.put("result", I18NManager.getMessage("请选择文件!"));
		} else {
			try {
				RWProperties properties = new RWProperties("META-INF/props/file.properties");
				
				String loginName = AuthDetailUtil.getLoginName();
				 //获取保存的路径
				String path = request.getSession().getServletContext().getRealPath(properties.getProperty("upload.path") + "/" + loginName);
				
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
				newFileName = URLEncoder.encode(newFileName, "utf-8");
				byte[] encode = Base64.encode(newFileName.getBytes());
				newFileName = new String(encode);
				//进行文件保存 
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path, newFileName));
				result.put("status", "success");
				result.put("result", I18NManager.getMessage("文件上传成功!"));
			} catch (Exception e) {
				logger.error(e.getMessage());
				result.put("error", I18NManager.getMessage("文件上传失败!"));
			}
		}
		return result;
	}
	/**
	 * 文件下载
	 * @param fileName
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(String fileName,HttpServletRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        
        byte[] decode = Base64.decode(fileName.getBytes());
		String newFileName = new String(decode);
		newFileName = URLDecoder.decode(newFileName, "utf-8");
		newFileName = encodeFilename(newFileName, request);
        
        headers.setContentDispositionFormData("attachment", newFileName);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(getDictionaryFile(fileName,request)),
                                          headers, HttpStatus.CREATED);
    }
    
    /**
     * 显示目录下的文件列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/listFiles")
    @ResponseBody
    public Map<String, Object> listFiles(HttpServletRequest request) throws Exception {
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
    	
    	RWProperties properties = new RWProperties("META-INF/props/file.properties");
    	String loginName = AuthDetailUtil.getLoginName();
    	
    	File dictionary = getDictionary(request); 
    	Collection<File> listFiles = FileUtils.listFiles(dictionary, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE), FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE));
    	
    	Iterator<File> iterator = listFiles.iterator();
    	while(iterator.hasNext()){
    		Map<String, Object> row = new HashMap<String,Object>();
    		File file = iterator.next();
    		
    		String fileName = file.getName();
    		
    		byte[] decode = Base64.decode(fileName.getBytes());
    		String newFileName = new String(decode);
    		newFileName = URLDecoder.decode(newFileName, "utf-8");
    		row.put("name",newFileName);
    		
    		long size = FileUtils.sizeOf(file);
    		DecimalFormat df = new DecimalFormat(",### KB");
    		row.put("size", df.format(Math.ceil(size*1.0/1024)));
    		
    		if(file.isFile()){
    			row.put("type", "file");
    		} else {
    			row.put("type", "directory");
    		}
    		row.put("lastModified", DateUtils.convertDateTimeToString(new Date(file.lastModified())));
    		
    		int idx = newFileName.lastIndexOf(".");
			if(idx != -1){
				//获得文件后缀名
				String suffix = newFileName.substring(idx+1);
				row.put("suffix", suffix);
			}
    		
			row.put("path", properties.getProperty("upload.path") + "/" + loginName);
			
			row.put("fileName", fileName);
    		rows.add(row);
    	}
    	
    	Collections.sort(rows, new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				try {
					Date d1 = DateUtils.convertStringToDate(DateUtils.DATETIME_FORMAT,String.valueOf(o1.get("lastModified")));
					Date d2 = DateUtils.convertStringToDate(DateUtils.DATETIME_FORMAT,String.valueOf(o2.get("lastModified")));
					if(d1.getTime() > d2.getTime()){ 
						return 1;
					} else {
						return -1;
					}
				} catch (ParseException e) {
					logger.error(e.getMessage());
				} 
				return 0;
			}
		});
    	result.put("Rows", rows);
    	return result;
    }
    
    @RequestMapping("/deleteFile")
    @ResponseBody
    public Map<String, Object> deleteFile(String fileName,HttpServletRequest request) throws Exception {
    	Map<String, Object> result = new HashMap<String, Object>();

		try{
			FileUtils.deleteQuietly(getDictionaryFile(fileName, request));
			result.put("result", I18NManager.getMessage("文件删除成功!"));
		} catch(Exception e) {
			result.put("error", I18NManager.getMessage("文件删除失败!"));
		}
    	return result;
    }
    
    private File getDictionary(HttpServletRequest request){
    	RWProperties properties = new RWProperties("META-INF/props/file.properties");
    	
    	String loginName = AuthDetailUtil.getLoginName();
    	//获取保存的路径
		String path = request.getSession().getServletContext().getRealPath(properties.getProperty("upload.path") + "/" + loginName);
		
		return new File(path);
    }
    
    private File getDictionaryFile(String fileName,HttpServletRequest request){
    	RWProperties properties = new RWProperties("META-INF/props/file.properties");
    	
    	String loginName = AuthDetailUtil.getLoginName();
    	//获取保存的路径
		String path = request.getSession().getServletContext().getRealPath(properties.getProperty("upload.path") + "/" + loginName);
		
		return new File(path, fileName);
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

}
