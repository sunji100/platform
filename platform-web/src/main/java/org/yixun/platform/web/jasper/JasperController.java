package org.yixun.platform.web.jasper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yixun.platform.application.security.MenuApplication;
import org.yixun.platform.application.security.dto.MenuDTO;
import org.yixun.platform.application.wssecurity.ResourceApplication;
import org.yixun.platform.application.wssecurity.UserApplication;
import org.yixun.platform.application.wssecurity.dto.ResourceDTO;
import org.yixun.platform.application.wssecurity.dto.UserDTO;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/jasper")
public class JasperController {
	@Inject
	private ResourceApplication resourceApplication;
	@Inject
	private UserApplication userApplication;
	@Inject
	private MenuApplication menuApplication;
	@RequestMapping("/pdfReport")
	public ModelAndView pdfReport() throws Exception{
		
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("datasource", result);//批明report数据源
//		model.put("p1", "eng");//指明report所有参数值
//		return new ModelAndView("report6",model);//指明所用report bean id
		
//		Page<ResourceDTO> page = resourceApplication.pageQueryResource(new ResourceDTO(), 1, 100);
//		List<ResourceDTO> result = page.getResult();
//		
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("datasource", result);
//		model.put("p1", "eng");
//		model.put("dataList", result); 
//		Page<UserDTO> page = userApplication.pageQueryUser(new UserDTO(), 1, 100);
//		List<UserDTO> result = page.getResult();
		
		List<MenuDTO> result = new ArrayList<MenuDTO>();
		
		MenuDTO m1 = new MenuDTO();
		m1.setText("第一个");
		m1.setDescription("adsfas");

		
		MenuDTO c1 = new MenuDTO();
		c1.setText("第一个的子1");
		c1.setDescription("adsfas");
		MenuDTO c2 = new MenuDTO();
		c2.setText("第一个的子2");
		c2.setDescription("adsfas");
		
		List<MenuDTO> list = new ArrayList<MenuDTO>();
		list.add(c1);
		list.add(c2);
		m1.setChildren(list);
		
		MenuDTO m2 = new MenuDTO();
		m2.setText("第二个");
		m2.setDescription("adsfas");
		MenuDTO c3 = new MenuDTO();
		c3.setText("第二个的子1");
		c3.setDescription("adsfas");
		MenuDTO c4 = new MenuDTO();
		c4.setText("第二个的子2");
		c4.setDescription("adsfas");
		
		List<MenuDTO> list1 = new ArrayList<MenuDTO>();
		list1.add(c3);
		list1.add(c4);
		m2.setChildren(list1);
		
		result.add(m1);
		result.add(m2);
		
		
		
//		List<MenuDTO> result = menuApplication.findMenu();
		List<MenuDTO> reportList = new ArrayList<>();
		reportList.add(new MenuDTO());
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("dataList", reportList); 
		model.put("ds", result);
		return new ModelAndView("report6",model);
	}
}
