<%@page import="org.springframework.web.util.WebUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/pages/common/header.jsp"%>
<link href="${pageContext.request.contextPath}/css/auth-common.css" rel="stylesheet" type="text/css" /> 
<link href="${pageContext.request.contextPath}/css/auth-index.css" rel="stylesheet" type="text/css" /> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<script type="text/javascript">
	var accordion = null;
	$(function(){
		// 布局
		$("#main-content").ligerLayout({
			leftWidth : 190,
			height : '100%',
			heightDiff : -34,
			space : 4
		});
		
		var height = $(".l-layout-center").height();
		// Tab
		$("#framecenter").ligerTab({
			height : height
		});
		
		$.getJSON("/resource/findMenuByUser.do",function(menu){
			
			$(menu.data).each(function(i,menu){
				$("#leftmenu").append('<div id="main_'+menu.id+'" title="' + menu.text + '" class="l-scroll"><ul id="sub_'+menu.id+'"></ul></div>');
				var tree = $("#sub_"+menu.id).ligerTree({
             		data:menu.children,
             		checkbox:false
             	});
				
				tree.bind("select", function(node) {
					console.info(JSON.stringify(node));
             		/*var url = node.data.identifier;
             		var text = node.data.text;
             		var tabid = $(node.target).attr("tabid");
             		if (!url) {
             			return;
             		}
             		if (node.data.menuType == "2") {
             			return;
             		}
	                if (!tabid) {
	                    tabidcounter++;
	                    tabid = "tabid" + tabidcounter;
	                    $(node.target).attr("tabid", tabid);
	                }
	                addTabEvent(tabid, text, url);*/
             	});
			});
			//Accordion
	         accordion = $("#leftmenu").ligerAccordion({ height: $(".l-layout-center").height() - 24, speed: null });
		});
	});
</script>
</head>
<body style="padding:0px;background:#EAEEF5;">
	<div id="pageloading"></div>  
	<div id="topmenu" class="l-topmenu">
        <div class="l-topmenu-logo">xxx</div>
        <div class="l-topmenu-welcome"> 
            <span class="l-topmenu-username">[<ss3:authentication property="principal.username" />]</span>欢迎您  &nbsp; 
            [<a href="javascript:Koala.changepassword()">修改密码</a>] &nbsp; 
            [<a href="${pageContext.request.contextPath}/j_spring_security_logout">切换用户</a>]
            [<a href="${pageContext.request.contextPath}/j_spring_security_logout">退出</a>]
        </div>
	</div>
  
	<div id="main-content" style="width:99.2%; margin:0 auto; margin-top:4px; "> 
        <div position="left"  title="主要菜单" id="leftmenu">
        </div>
        <div position="center" id="framecenter"> 
            <div tabid="home" title="我的主页" style="height:300px" >
                
            </div> 
        </div> 
        
    </div>
</body>
</html>