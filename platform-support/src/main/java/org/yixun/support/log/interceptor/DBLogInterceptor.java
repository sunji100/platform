package org.yixun.support.log.interceptor;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yixun.support.log.annotation.MethodLog;
import org.yixun.support.log.dto.LogDTO;


@Named
@Aspect
public class DBLogInterceptor {
	
	@Inject
	private JdbcTemplate jdbcTemplate;
	
	@Pointcut("execution(* org.yixun.platform.application..*.*(..))")
	private void controllerMethod() {
	}
	@Pointcut("execution(* org.yixun.platform.application.security..AuthDataService*.*(..))")
	private void loginExecution(){
		
	}
	@Pointcut("execution(* org.yixun.platform.application.log..*.*(..))")
	private void logExecution(){
		
	}
	
	@AfterReturning("controllerMethod() && !loginExecution() && !logExecution()")
	public void doAfter(JoinPoint jp) {
		
		
		LogDTO logDTO = new LogDTO();
		UserDetails userDetails;
		try {
			userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			logDTO.setLoginName(userDetails.getUsername());
		} catch (Exception e) {
			logDTO.setLoginName("匿名用户");
		}
		
		logDTO.setCreateDate(Calendar.getInstance().getTime());
		logDTO.setClassName(jp.getSignature().getDeclaringType().getName());
		
		String methodName = jp.getSignature().getName();
		logDTO.setMethod(methodName);
		
		Object[] args = jp.getArgs();
		
		try {
			logDTO.setArgs(getArgs(args, methodName));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		logDTO.setRemark(getRemark(jp));
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String ipAddr = getIpAddr(request);
		
		addLog(logDTO);
		
		
	}
	
	private String getArgs(Object[] args, String mName) throws Exception{  
		  
        if (args == null) {  
            return null;  
        }  
          
        StringBuffer rs = new StringBuffer();  
        rs.append(mName);  
        String className = null;  
        int index = 1;  
        // 遍历参数对象  
        for (Object info : args) {  
              
            //获取对象类型  
            className = info.getClass().getName();  
            className = className.substring(className.lastIndexOf(".") + 1);  
            rs.append("[参数" + index + "，类型：" + className + "，值：");  
              
            // 获取对象的所有方法  
            Method[] methods = info.getClass().getDeclaredMethods();  
              
            // 遍历方法，判断get方法  
            for (Method method : methods) {  
                  
                String methodName = method.getName();  
                // 判断是不是get方法  
                if (methodName.indexOf("get") == -1) {// 不是get方法  
                    continue;// 不处理  
                }  
                  
                Object rsValue = null;  
                try {  
                      
                    // 调用get方法，获取返回值  
                    rsValue = method.invoke(info);  
                      
                    if (rsValue == null) {//没有返回值  
                        continue;  
                    }  
                      
                } catch (Exception e) {  
                    continue;  
                }  
                  
                //将值加入内容中  
                rs.append("(" + methodName + " : " + rsValue + ")");  
            }  
              
            rs.append("]");  
              
            index++;  
        }  
          
        return rs.toString();  
    }  
	
	private String getRemark(JoinPoint jp){
		Class<? extends Object> targetClass  = jp.getTarget().getClass();
		String methodName = jp.getSignature().getName();
		Object[] args = jp.getArgs();
		
		Class[] parameterTypes = null;
		if(null != args){
			parameterTypes = new Class[args.length];
			for(int i=0;i<args.length;i++){
				parameterTypes[i] = args[i].getClass();
			}
		}
		
		String remark;
		try {
			Method method = targetClass.getMethod(methodName, parameterTypes);
			MethodLog methodLog = method.getAnnotation(MethodLog.class);
			if(null != methodLog){
				remark = methodLog.remark();
				return remark;
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		if(Pattern.compile("^(add|insert)").matcher(methodName).find()){
			remark = "增加";
		} else if(Pattern.compile("^(remove|delete)").matcher(methodName).find()){
			remark = "删除";
		} else if(Pattern.compile("^(update|modify)").matcher(methodName).find()){
			remark = "修改";
		} else if(Pattern.compile("^(find|select|get|pageQuery)").matcher(methodName).find()){
			remark = "查询";
		} else {
			remark = methodName;
		}
		return remark;
		
	}
	
	private void addLog(final LogDTO logDTO){
		final long seq = jdbcTemplate.queryForLong("select hibernate_sequence.nextval from dual");
		jdbcTemplate.update("insert into sys_log values(?,?,?,?,sysdate,?,?,?,?)", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, seq);
				ps.setInt(2, 0);
				ps.setString(3, logDTO.getArgs());
				ps.setString(4, logDTO.getClassName());
				ps.setString(5, logDTO.getIp());
				ps.setString(6, logDTO.getLoginName());
				ps.setString(7, logDTO.getMethod());
				ps.setString(8, logDTO.getRemark());
			}
		});
	}
	
	private String getIpAddr(HttpServletRequest request) { 
	       String ip = request.getHeader("x-forwarded-for"); 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getHeader("Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getHeader("WL-Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getRemoteAddr(); 
	       } 
	       return ip; 
	   }
}
