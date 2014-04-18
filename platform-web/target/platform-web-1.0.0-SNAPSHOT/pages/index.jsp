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
	var tab = null;
	var tabidcounter = 0;
	var treeManager = [];
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
			height : height,
			onAfterRemoveTabItem : cancelTreeSelect
		});
		
		tab = $("#framecenter").ligerGetTabManager();
		/*获得登录用户的菜单项*/
		$.getJSON("${pageContext.request.contextPath}/menu/findMenuByUser.do",function(menu){
			
			$(menu.data).each(function(i,menu){
				$("#leftmenu").append('<div id="main_'+menu.id+'" title="' + menu.text + '" class="l-scroll"><ul id="sub_'+menu.id+'"></ul></div>');
				var tree = $("#sub_"+menu.id).ligerTree({
             		data:menu.children,
             		checkbox:false
             	});
				
				tree.bind("select", function(node) {
             		var url = node.data.identifier;
             		var text = node.data.text;
             		var tabid = $(node.target).attr("tabid");
             		if (!url) {
             			return;
             		}
             		if (node.data.menuType == "DIRETORY") {
             			return;
             		}
             		//if (!tabid) {
                        //tabidcounter++;
                        //tabid = "tabid" + url.replace(/\/|\./g, "");
                        //$(node.target).attr("tabid", tabid);
                    //}
             		addTabEvent(text,url);
             	});
				
				treeManager.push($("#sub_"+menu.id).ligerGetTreeManager());
			});
			//Accordion
	         accordion = $("#leftmenu").ligerAccordion({ height: $(".l-layout-center").height() - 40, speed: null });
		});
		//window['f_addTab'] = addTabEvent;
		//window.prototype.f_addTab = addTabEvent;
		//window.prototype.f_removeTab = removeTabEvent;
		
		window['f_addTab'] = addTabEvent;
		
	});
	/*新建标签页*/
	function addTabEvent(text,url){
		var idx = url.indexOf("?");
		var tabid = "tabid";
		if(idx != -1){
			tabid += url.substring(0, idx);
		} else {
			tabid += url;
		}
		tabid = tabid.replace(/\/|\./g, "");
		if(idx != -1){
			url += "&tabid="+tabid; 
		} else {
			url += "?tabid="+tabid;
		}
		
		tab.addTabItem({
			tabid :tabid,
			text : text,
			url : "${pageContext.request.contextPath}" + url
		});
	}
	
	function removeTabEvent(tabid){
		tab.removeTabItem(tabid);
	}
	
	/*tab之间跳转*/
	function forwardTabEvent(text,url){
		var idx = url.indexOf("?");
		var tabid = "tabid";
		if(idx != -1){
			tabid += url.substring(0, idx);
		} else {
			tabid += url;
		}
		tabid = tabid.replace(/\/|\./g, "");
		if(idx != -1){
			url += "&tabid="+tabid; 
		} else {
			url += "?tabid="+tabid;
		}
		if(tab.isTabItemExist(tabid)){
   		 	tab.selectTabItem(tabid);
   		 	tab.reload(tabid);
   	 	} else {
	   	 	tab.addTabItem({
				tabid :tabid,
				text : text,
				url : "${pageContext.request.contextPath}" + url
			});
   	 	}
	}
	
	/*关闭tab标签后，取消tree的选择节点*/
	function cancelTreeSelect(tabid){
		for(var i=0;i<treeManager.length;i++){
			if(treeManager[i].getSelected()){
				treeManager[i].cancelSelect(treeManager[i].getSelected().data);
			}
		}
		//tree.cancelSelect(tree.getSelected());
	}
</script>
</head>
<body style="padding:0px;background:#EAEEF5;">
	<div id="pageloading"></div>  
	<div id="topmenu" class="l-topmenu">
        <div class="l-topmenu-logo">yixun</div>
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