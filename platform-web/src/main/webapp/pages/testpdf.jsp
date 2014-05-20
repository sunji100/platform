
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<%
	File reportFile = new File(this.getServletContext().getRealPath("/report/report1.jasper"));
	
	Class.forName("com.mysql.jdbc.Driver");
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost/platform","root","yixun");
	
	Map params = new HashMap();
	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), params, con);
	
	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	
	ServletOutputStream os = response.getOutputStream();
	os.write(bytes, 0, bytes.length);
	os.flush();
	os.close();
	
	out.clear();
	out = pageContext.pushBody();
%>
</body>
</html>