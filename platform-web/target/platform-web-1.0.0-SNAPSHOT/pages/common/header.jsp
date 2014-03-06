<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="ss3" %>
<%
 String contextPath = request.getContextPath();
%>

<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0"> 

<!-- base jquery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.8.3.min.js"></script>
<!-- ligerUI -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ligerUI/js/ligerui.all.js"></script>
<link href="${pageContext.request.contextPath}/js/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/js/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" /> 
<!-- common js --> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular/angular.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/date.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/msg.js"></script>
<!-- common css -->
<link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" type="text/css" /> 


<script type="text/javascript">
var rootPath = "${pageContext.request.contextPath}";
</script>
