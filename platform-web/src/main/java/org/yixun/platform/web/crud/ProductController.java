package org.yixun.platform.web.crud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.crud.ProductApplication;
import org.yixun.platform.application.crud.dto.ProductDTO;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	@Inject
	private ProductApplication productApplication;
	
	@ResponseBody
	@RequestMapping("/findAll")
	public Map<String, Object> findAll(){
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductDTO> all = productApplication.findAllProduct();
		result.put("Rows", all);
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/find")
	public Map<String, Object> findByCondition(ProductDTO productDTO,String sortname,String sortorder){
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductDTO> all = productApplication.queryProduct(productDTO,sortname,sortorder);
		result.put("Rows", all);
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/pageJson")
	public Map<String, Object> pageJson(ProductDTO productDTO, @RequestParam("page") int page, @RequestParam("pagesize") int pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Page<ProductDTO> all = productApplication.pageQueryProduct(productDTO, page, pageSize);
		result.put("Rows", all.getResult());
//		result.put("start", page * pagesize - pagesize);
//		result.put("limit", pagesize);
		result.put("Total", all.getTotalCount());
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/get/{id}")
	public Map<String, Object> get(@PathVariable Long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", productApplication.getProduct(id));
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(ProductDTO productDTO){
		Map<String, Object> result = new HashMap<String, Object>();
		ProductDTO resultDTO = productApplication.saveProduct(productDTO);
		result.put("result", "sucess");
		result.put("data", resultDTO);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(ProductDTO productDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		productApplication.updateProduct(productDTO);
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/remove")
	public Map<String, Object> remove(@RequestParam("ids") String ids) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		String[] value = ids.split(",");
        Long[] idArrs = new Long[value.length];
        for (int i = 0; i < value.length; i ++) {
        	idArrs[i] = Long.parseLong(value[i]);			        	
        }
        productApplication.removeProducts(idArrs);
		result.put("result", "success");
		return result;
	}
}
