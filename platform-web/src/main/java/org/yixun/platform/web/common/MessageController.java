package org.yixun.platform.web.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/msg")
public class MessageController {
	
	@ResponseBody
	@RequestMapping("/load")
	public Map<String, Object> loadMsg(){
		Resource resource = new ClassPathResource("/META-INF/props/message.properties");
		
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			Set<Object> keySet = props.keySet();
			Iterator<Object> iterator = keySet.iterator();
			while(iterator.hasNext()){
				String key = String.valueOf(iterator.next());
				String value = props.getProperty(key);
				result.put(key, value);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
