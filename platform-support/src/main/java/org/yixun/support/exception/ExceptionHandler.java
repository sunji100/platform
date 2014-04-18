package org.yixun.support.exception;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.yixun.support.i18n.I18NManager;

public class ExceptionHandler extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		String exceptionMsg = null;
		exceptionMsg = ex.getMessage();
//		exceptionMsg = WebErrUtils.formatException(ex);
//		if(ex instanceof BusinessException){
//			exceptionMsg = ex.getMessage();
//		}
//		if(StringUtils.isBlank(exceptionMsg)){
//			exceptionMsg = "其他异常:" + ex.toString();
//		}
		
		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			
			if (!isAsynRequest(request)) {
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				//ex.printStackTrace();
				Map<String, String> result = new HashMap<String, String>();
				result.put("errorMsg", exceptionMsg);
				return new ModelAndView(viewName, result);
			} else {
				writeJSON(response, "error", exceptionMsg);
				//ex.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * 向页面输出JSON数据
	 * @param response	response对象
	 * @param key		JSON中的键
	 * @param value		JSON中的值
	 */
	private void writeJSON(HttpServletResponse response, String key, String value) {
		Writer writer = null;
		try {
			response.setContentType("text/x-json;charset=UTF-8");
			writer = response.getWriter();
			writer.write(String.format("{\"%s\":\"%s\"}", key , value));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 是否是异步请求
	 * 
	 * @param request
	 * @return
	 */
	private boolean isAsynRequest(HttpServletRequest request) {
		return (request.getHeader("accept").indexOf("application/json") != -1 || (request
				.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With")
				.indexOf("XMLHttpRequest") != -1));
	}

}
