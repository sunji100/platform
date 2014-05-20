package org.yixun.platform.web.test;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.web.test.vo.ParamVo;
import org.yixun.platform.web.test.vo.ParamVo1;

@Controller
@RequestMapping("/test")
public class TestController {
	@RequestMapping("/autoForm")
	@ResponseBody
	public Map<String, Object> autoForm(@RequestBody ParamVo[] vo) throws Exception {
		for (ParamVo paramVo : vo) {
			System.out.println(paramVo.getDdlDepart() + ":" + paramVo.getDdlUser());
		}
		return null;
	}
	
	@RequestMapping("/autoForm1")
	@ResponseBody
	public Map<String, Object> autoForm1(ParamVo1 vo) throws Exception {
		System.out.println(vo.getA());
		System.out.println(vo.getB());
		List<ParamVo> voList = vo.getVoList();
		for (ParamVo paramVo : voList) {
			System.out.println(paramVo.getDdlDepart() + ":" + paramVo.getDdlUser() + ":" + paramVo.getDdlDepart_val());
		}
		return null;
	}
}
