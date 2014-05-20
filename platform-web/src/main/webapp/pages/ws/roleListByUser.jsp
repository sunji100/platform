<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	webservice用户的角色管理界面
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
		            url:'${pageContext.request.contextPath}/ws/role/pageQueryRoleByUserId.do?userId=' + userId,
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
	/*工具条事件处理*/
	function itemclick(item){
		if("add" == item.id){
			openAssignRoleToUserDialog();
		}  else if("delete" == item.id){
			removeRoleForUserAction();
		}
	}
	/*分配角色对话框*/
	var assignRoleToUserDialog;
	function openAssignRoleToUserDialog(){
		assignRoleToUserDialog = $.ligerDialog.open({
			title : "分配角色",
			isResize : true,
			width : 470,
			height : 380,
			isHidden : true,
			buttons : [ {
				id : 'assignRole',
				text : '保存',
				onclick : dialogBtnClick
			}, {
				id : 'cancelAssignRole',
				text : '取消',
				onclick : dialogBtnClick
			} ],
			target : $("#assignRoleToUserDialog")
		});
		initRoleGrid();
	}
	/*加载未分配的角色grid*/
	function initRoleGrid(){
		roleGridManager = $("#rolegrid").ligerGrid({
			width : 450,
            height: 350,
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
			}],
            url:'${pageContext.request.contextPath}/ws/role/findNotAssignRoleByUser.do?userId=' + userId,
            pageSize:10 ,
            rownumbers:false
        });
	}
	
	/*处理对话框事件*/
	function dialogBtnClick(button) {
		if("cancelAssignRole" == button.id){
			assignRoleToUserDialog.hidden();
		} else if("assignRole" == button.id){
			assignRoleToUserAction();
		} 
	}
	/*为用户分配角色*/
	function assignRoleToUserAction(){
		var newRow = roleGridManager.getSelected();
		if (!newRow) { $.ligerDialog.warn('请选择行'); return; }
		var roleIds = [];
		$.each(roleGridManager.getSelectedRows(), function(index, element) {
			roleIds.push(element.id);
		});
		roleIds = roleIds.join(",");
		
		var data = {};
		data["roleIds"] = roleIds;
		data["userId"] = userId;
		
		$.ajax({ 
		    type:"POST", 
		    url:"${pageContext.request.contextPath}/ws/role/assignRoleToUser.do",  
		    data:data,
		    success:function(json){ 
		    	if(json.error){
		    		$.ligerDialog.error(json.error);
				}
				if(json && json.result){
					$.ligerDialog.success(json.result);
					gridManager.loadData();
				}
				assignRoleToUserDialog.hidden();                   
		    } 
		 }); 
	}
	/*删除分配的角色*/
	function removeRoleForUserAction(){
		var newRow = gridManager.getSelected();
		if (!newRow) { $.ligerDialog.warn('请选择行'); return; }
		$.ligerDialog.confirm("确认删除?",function(flag){
			if(flag){
				var removeData = [];
				$.each(gridManager.getSelectedRows(), function(index, element) {
					removeData.push(element.id);
				});
				removeData = removeData.join(",");
				var data = {};
				data["roleIds"] = removeData;
				data["userId"] = userId;
				//return;
				$.ajax({ 
				    type:"POST", 
				    url:"${pageContext.request.contextPath}/ws/role/removeRoleForUser.do",  
				    data:data,
				    success:function(json){ 
				    	if(json.error){
				    		$.ligerDialog.error(json.error);
						}
						if(json && json.result){
							$.ligerDialog.success(json.result);
							gridManager.removeRange(gridManager.getSelectedRows()); 
						}
				    	                   
				    } 
				 }); 
			}
		});
	}
</script>
<title>Insert title here</title>
</head>
<body>
<div id="maingrid"></div>
<div id="assignRoleToUserDialog" style="display: none;">
	<div id="rolegrid"></div>
</div>
</body>
</html>