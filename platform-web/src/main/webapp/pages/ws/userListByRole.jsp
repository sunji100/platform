<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	webservice角色的用户管理界面
	*/
	var roleId = "${param.roleId}";//角色ID

	$(function(){
		PageLoader.initGridPanel();//获得角色已分配用户
	});
	PageLoader = {
			initGridPanel:function(){
				gridManager = $("#maingrid").ligerGrid({
					width : '98%',
		            height:'100%',
		            checkbox:true,
		            columns: [ {
						display : "用户名称",
						name : "name",
						width : 100,
						type : "text",
						align : "left"
					}, {
						display : "用户帐号",
						name : "userAccount",
						width : 100,
						type : "text",
						align : "left"
					}, {
						display : "用户描述",
						name : "userDesc",
						width : 200,
						type : "text",
						align : "left"
					}, {
						display : "是否有效",
						name : "valid",
						width : 50,
						type : "text",
						align : "left",
						render : function(rowdata) {
							return rowdata.valid == true ? "是" : "否";
						}
					}, {
						display : "最后登录时间",
						name : "lastLoginTime",
						width : 100,
						type : "text",
						align : "left"
					}, {
						display : "最后更改时间",
						name : "lastModifyTime",
						width : 100,
						type : "text",
						align : "left"
					} ],
		            url:'${pageContext.request.contextPath}/ws/user/pageQueryUserByRoleId.do?roleId=' + roleId,
		            pageSize:10 ,
		            rownumbers:false,
		            toolbar: { items: [
          	        { 
          	          	id:'add',
          	          	text: '分配用户',
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
			openAssignUserToRoleDialog();
		}  else if("delete" == item.id){
			removeUserForRoleAction();
		}
	}
	/*分配用户对话框*/
	var assignUserToRoleDialog;
	function openAssignUserToRoleDialog(){
		assignUserToRoleDialog = $.ligerDialog.open({
			title : "分配用户",
			isResize : true,
			width : 470,
			height : 380,
			isHidden : true,
			buttons : [ {
				id : 'assignUser',
				text : '保存',
				onclick : dialogBtnClick
			}, {
				id : 'cancelAssignUser',
				text : '取消',
				onclick : dialogBtnClick
			} ],
			target : $("#assignUserToRoleDialog")
		});
		initUserGrid();
	}
	
	/*加载未分配的用户grid*/
	function initUserGrid(){
		userGridManager = $("#usergrid").ligerGrid({
			width : 450,
            height: 350,
            checkbox:true,
            columns: [ {
				display : "用户名称",
				name : "name",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "用户帐号",
				name : "userAccount",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "用户描述",
				name : "userDesc",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "是否有效",
				name : "valid",
				width : 50,
				type : "text",
				align : "left",
				render : function(rowdata) {
					return rowdata.valid == true ? "是" : "否";
				}
			}],
            url:'${pageContext.request.contextPath}/ws/user/findNotAssignUserByRole.do?roleId=' + roleId,
            pageSize:10 ,
            rownumbers:false
        });
	}
	
	/*处理对话框事件*/
	function dialogBtnClick(button) {
		if("cancelAssignUser" == button.id){
			assignUserToRoleDialog.hidden();
		} else if("assignUser" == button.id){
			assignUserToRoleAction();
		} 
	}
	
	/*为角色分配用户*/
	function assignUserToRoleAction(){
		var newRow = userGridManager.getSelected();
		if (!newRow) { $.ligerDialog.warn('请选择行'); return; }
		var userIds = [];
		$.each(userGridManager.getSelectedRows(), function(index, element) {
			userIds.push(element.id);
		});
		userIds = userIds.join(",");
		
		var data = {};
		data["userIds"] = userIds;
		data["roleId"] = roleId;
		
		$.ajax({ 
		    type:"POST", 
		    url:"${pageContext.request.contextPath}/ws/user/assignUserToRole.do",  
		    data:data,
		    success:function(json){ 
		    	if(json.error){
		    		$.ligerDialog.error(json.error);
				}
				if(json && json.result){
					$.ligerDialog.success(json.result);
					gridManager.loadData();
				}
				assignUserToRoleDialog.hidden();                   
		    } 
		 }); 
	}
	
	/*删除分配的用户*/
	function removeUserForRoleAction(){
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
				data["userIds"] = removeData;
				data["roleId"] = roleId;
				//return;
				$.ajax({ 
				    type:"POST", 
				    url:"${pageContext.request.contextPath}/ws/user/removeUserForRole.do",  
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
<div id="assignUserToRoleDialog" style="display: none;">
	<div id="usergrid"></div>
</div>
</body>
</html>