<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	指定用户的角色管理界面
	*/
	var userId = "${param.userId}";
	var _dialog;
	$(function(){
		PageLoader.initGridPanel();
	});
	PageLoader = {
			initGridPanel:function(){
				gridManager = $("#maingrid").ligerGrid({
					width : '98%',
		            height:'100%',
		            checkbox:true,
		            columns: [ {
						display : "角色名称",
						name : "name",
						width : 100,
						type : "text",
						align : "left"
					}, {
						display : "角色描述",
						name : "roleDesc",
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "是否有效",
						name : "valid",
						width : 50,
						type : "text",
						align : "left",
						render : function(rowdata) {
							return rowdata.valid == true ? "是" : "否";
						}
					} ],
		            url:'${pageContext.request.contextPath}/role/pageQueryRoleByUserId.do?userId=' + userId,
		            pageSize:3 ,
		            rownumbers:false,
		            toolbar: { items: [
          	        { 
          	          	id:'add',
          	          	text: '分配角色',
          	          	click: itemclick, 
          	          	img : rootPath + "/images/icons/toolbar/add.png" 
          	        },{ 
          	        	line: true 
          	        },{ 
          	        	id:'delete',
          	        	text: '删除', 
          	        	click: itemclick, 
          	        	img : rootPath + "/images/icons/toolbar/page_delete.gif"
          	        }]}
		        });
			}
	};
	
	function itemclick(item){
		if("add" == item.id){
			openAssignRoleToUserDialog();
		}  else if("delete" == item.id){
			deleteDataAction();
		}
	}
	
	function openAssignRoleToUserDialog(){
		var url = "${pageContext.request.contextPath}/pages/auth/role/assignRoleToUser.jsp?userId=" + userId;
		_dialog = jQuery.ligerDialog.open({
	  	    title:'分配角色',
	  	    url:url,
	  	    isResize: true, width: 470, height: 500, isHidden: false
	     });
	}
	
	function deleteDataAction(){
		var newRow = gridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var removeData = "";
		$.each(gridManager.getSelectedRows(), function(index, element) {
			removeData = removeData + element.id + ","; 
		});
		removeData = removeData.substr(0, removeData.length-1);
		$.post("${pageContext.request.contextPath}/role/removeRoleForUser.do", 
				{roleIds:removeData,userId:userId},
				function(json) {
					if(json.error){
						alert(json.error);
						return;
					}
					if(json && json.result){
					 alert(json.result);
					 gridManager.loadData();
					}
				});
	}
</script>
<title>Insert title here</title>
</head>
<body>
<div id="maingrid"></div>
</body>
</html>