<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0">
<script type="text/javascript">

	var promptMsg;
	$(function(){
		$.getJSON('${pageContext.request.contextPath}/msg/load.do',function(msg){
			promptMsg = msg;
		});
	});
	
	

</script> 