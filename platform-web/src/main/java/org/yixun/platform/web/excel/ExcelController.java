package org.yixun.platform.web.excel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yixun.platform.web.excel.view.GridViewExcel;
import org.yixun.platform.web.excel.view.ViewExcel;

@Controller
@RequestMapping("/excel")
public class ExcelController {
	@RequestMapping("/export")
	public ModelAndView exportByHtml(String fileName,String grid,HttpServletResponse response) throws Exception{
//		response.setContentType("application/vnd.ms-excel");
////		try {
//			response.addHeader("Content-disposition", "attachment; filename=test.xls");
////		} catch (UnsupportedEncodingException e) {
////			response.addHeader("Content-disposition", "attachment; filename="
////					+ fileName);
////		}
//		ServletOutputStream os = response.getOutputStream();
//		ExcelWriter excelWriter = new ExcelWriter(os);
//		excelWriter .write("Company", 0, 0, createData());
//		os.flush();
//		os.close();
		
		ViewExcel viewExcel = new ViewExcel();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("fileName", fileName);
		model.put("grid", grid);
		
		return new ModelAndView(viewExcel, model);
	}
	
	@RequestMapping("/exportByData")
	public ModelAndView exportByData(String fileName,String headRows,String head,String grid) throws Exception{
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("fileName", fileName);
		model.put("head", head);
		model.put("grid", grid);
		model.put("headRows", headRows);
		
		return new ModelAndView(new GridViewExcel(), model);
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
