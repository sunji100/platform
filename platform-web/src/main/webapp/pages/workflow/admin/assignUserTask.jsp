<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	流程任务分配界面
	*/
	var _dialog;
	var assignGridData;
	var processDefinitionId = '${param.processDefinitionId}';
	$(function(){
		// 布局
		$("#main-content").ligerLayout({
			leftWidth : 700,
			height : '100%',
			heightDiff : -34,
			space : 4,
			allowLeftCollapse:false
		});
		
		PageLoader.initGridPanel();//流程定义中的用户任务
	});
	PageLoader = {
			initGridPanel:function(){
				gridManager = $("#maingrid").ligerGrid({
					width : '98%',
		            height:'95%',
		            columns: [ {
						display : "流程定义ID",
						name : "procDefId",
						width : 200,
						type : "text",
						align : "left"
					}, {
						display : "流程定义Key",
						name : "procDefKey",
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "流程定义名称",
						name : "procName",
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "任务名称",
						name : "taskName",
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "任务Key",
						name : "taskDefKey",
						width : 100,
						type : "text",
						align : "left"
					} ],
		            url:'${pageContext.request.contextPath}/bpmAdmin/listProcessDefinitionUserTasks.do',
		            parms:{processDefinitionId:processDefinitionId},
		            usePager:false,
		            rownumbers:true,
		            heightDiff : -10,
			        headerRowHeight: 30,//表头行的高度
			        onSelectRow:roleGridSelect
		        });
				//初始化执行人grid
				assignGridManager = $("#assigngrid").ligerGrid({
					width : '98%',
		            height:'95%',
		            checkbox:true,
		            columns: [ {
						display : "执行人/执行角色",
						name : "userName",
						width : 100,
						type : "text",
						align : "left"
					}, {
						display : "类型",
						name : "userType",
						width : 200,
						type : "text",
						align : "left"
					}],
		            data:assignGridData,
		            pageSize:10 ,
		            rownumbers:true,
		            toolbar: { items: [
          	        { 
          	          	id:'assignRole',
          	          	text: '分配角色',
          	          	click: itemclick, 
          	          	img : rootPath + "/images/icons/toolbar/add.png" 
          	        },{ 
          	        	line: true 
          	        },{ 
          	          	id:'assignUser',
          	          	text: '分配用户',
          	          	click: itemclick, 
          	          	img : rootPath + "/images/icons/toolbar/add.png" 
          	        },{ 
          	        	line: true 
          	        },{ 
          	        	id:'deleteAssign',
          	        	text: '删除分配', 
          	        	click: itemclick, 
          	        	img : rootPath + "/images/icons/toolbar/page_delete.gif"
          	        }]}
		        });
				
			}
	};
	
	function roleGridSelect(rowdata, rowid, rowobj){
		loadAssignGridData(rowdata);
	}
	
	var selectedUserTask;
	/*获得用户任务的执行人或角色*/
	function loadAssignGridData(rowdata){
		selectedUserTask = rowdata;
		$.ajax({
		      url: "${pageContext.request.contextPath}/bpmAdmin/findUserTaskAssign.do",
		      data: {procDefId:rowdata.procDefId, procDefKey:rowdata.procDefKey, taskDefKey:rowdata.taskDefKey},
		      type: "POST",
		      success: function(json){
		         if(json){
		        	 assignGridData = json;
		        	 assignGridManager.options.newPage = 1;
		        	 assignGridManager.loadData(assignGridData);
		         }
		      }
		   }
		);
	}
	
	function itemclick(item){
		if("assignRole" == item.id){
			openAssignRolesToUserTaskDialog();
		} else if("assignUser" == item.id){
			openAssignUsersToUserTaskDialog();
		} else if("deleteAssign" == item.id){
			deleteAssignForUserTask();
		}
	}
	/*删除用户任务的办理人或角色*/
	function deleteAssignForUserTask(){
		var newRow = assignGridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var removeData = [];
		$.each(assignGridManager.getSelectedRows(), function(index, element) {
			removeData.push({procDefId:selectedUserTask.procDefId, procDefKey:selectedUserTask.procDefKey, taskDefKey:selectedUserTask.taskDefKey,userId:element.userId,userType:element.userType});
		});
		$.ajax({ 
		    type:"POST", 
		    url:"${pageContext.request.contextPath}/bpmAdmin/removeAssignForUserTask.do", 
		    dataType:"json",      
		    contentType:"application/json",  //这个需要             
		    data:JSON.stringify(removeData),//将json数组，格式化json串的方式提交
		    success:function(json){ 
		    	if(json.error){
					alert(json.error);
					return;
				}
				if(json && json.result){
					assignGridManager.removeRange(assignGridManager.getSelectedRows());
					alert(json.result);
				}                    
		    } 
		 });
		
	}
	
	var assignRolesToUserTaskDialog;
	/*打开办理角色分配对话框*/
	function openAssignRolesToUserTaskDialog(){
		assignRolesToUserTaskDialog = $.ligerDialog.open({
			title : "角色",
			isResize : true,
			width : 480,
			height : 520,
			isHidden : true,
			buttons : [ {
				id : 'assignRolesToUserTask',
				text : '保存',
				onclick : btnClick
			}, {
				id : 'cancelAssignRolesToUserTask',
				text : '取消',
				onclick : btnClick
			} ],
			target : $("#assignRolesToUserTaskDialog")
		});
		loadRoles();
	}
	
	// 待选角色列表结构
	var roleGrid;
	function loadRoles() {
		roleGrid = $("#assignRolesToUserTask").ligerGrid({
			columns : [ {
				display : "角色名称",
				name : "name",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "角色描述",
				name : "roleDesc",
				width : 200,
				type : "text",
				align : "left"
			} ],
			pageSize:10,
			url : '${pageContext.request.contextPath}/bpmAdmin/findNotAssignRoleByUserTask.do',
			parms : {procDefId:selectedUserTask.procDefId, procDefKey:selectedUserTask.procDefKey, taskDefKey:selectedUserTask.taskDefKey},
			rownumbers : true,
			width : 450,
			height : 430,
			checkbox : true
		});
	}
		
	function btnClick(button) {
		if("assignRolesToUserTask" == button.id){
			assignRolesToUserTaskAction();
		} else if("cancelAssignRolesToUserTask" == button.id){
			assignRolesToUserTaskDialog.hidden();
		} else if("assignUsersToUserTask" == button.id){
			assignUsersToUserTaskAction();
		} else if("cancelAssignUsersToUserTask" == button.id){
			assignUsersToUserTaskDialog.hidden();
		}
	}
	/*为用户任务分配办理角色*/
	function assignRolesToUserTaskAction(){
		var newRow = roleGrid.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var roleIds = [];
		$.each(roleGrid.getSelectedRows(), function(index, element) {
			roleIds.push(element.id);
		});
		roleIds = roleIds.join(",");
		var url,data;
		url = "${pageContext.request.contextPath}/bpmAdmin/assignRoleToUserTask.do";
		data = {procDefId:selectedUserTask.procDefId, procDefKey:selectedUserTask.procDefKey, taskDefKey:selectedUserTask.taskDefKey,roleIds:roleIds};
		
		$.ajax({
		      url: url,
		      data: data,
		      type: "POST",
		      success: function(json){
		         if(json){
		        	 loadAssignGridData(selectedUserTask);
		        	 assignRolesToUserTaskDialog.hidden();
		         }
		      }
		   }
		);
	}
	
	var assignUsersToUserTaskDialog;
	/*打开办理人分配对话框*/
	function openAssignUsersToUserTaskDialog(){
		assignUsersToUserTaskDialog = $.ligerDialog.open({
			title : "用户",
			isResize : true,
			width : 480,
			height : 520,
			isHidden : true,
			buttons : [ {
				id : 'assignUsersToUserTask',
				text : '保存',
				onclick : btnClick
			}, {
				id : 'cancelAssignUsersToUserTask',
				text : '取消',
				onclick : btnClick
			} ],
			target : $("#assignUsersToUserTaskDialog")
		});
		loadUsers();
	}
	
	// 待选用户列表结构
	var userGrid;
	function loadUsers() {
		userGrid = $("#assignUsersToUserTask").ligerGrid({
			columns : [ {
				display : "用户名称",
				name : "name",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "用户描述",
				name : "userDesc",
				width : 200,
				type : "text",
				align : "left"
			} ],
			pageSize:10,
			url : '${pageContext.request.contextPath}/bpmAdmin/findNotAssignUserByUserTask.do',
			parms : {procDefId:selectedUserTask.procDefId, procDefKey:selectedUserTask.procDefKey, taskDefKey:selectedUserTask.taskDefKey},
			rownumbers : true,
			width : 450,
			height : 430,
			checkbox : true
		});
	}
	/*为用户任务分配办理人*/
	function assignUsersToUserTaskAction(){
		var newRow = userGrid.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var userIds = [];
		$.each(userGrid.getSelectedRows(), function(index, element) {
			userIds.push(element.id);
		});
		userIds = userIds.join(",");
		var url,data;
		url = "${pageContext.request.contextPath}/bpmAdmin/assignUserToUserTask.do";
		data = {procDefId:selectedUserTask.procDefId, procDefKey:selectedUserTask.procDefKey, taskDefKey:selectedUserTask.taskDefKey,userIds:userIds};
		
		$.ajax({
		      url: url,
		      data: data,
		      type: "POST",
		      success: function(json){
		         if(json){
		        	 loadAssignGridData(selectedUserTask);
		        	 assignUsersToUserTaskDialog.hidden();
		         }
		      }
		   }
		);
	}
</script>
<title>Insert title here</title>
</head>
<body>
<div id="main-content" style="width:99.2%; margin:0 auto; margin-top:4px; "> 
	<div position="left" id="left" title="任务列表">
		<div id="maingrid"></div>
	</div>
	<div position="center" id="center" title="执行人"> 
        <div id="assigngrid"></div>
	</div> 
</div>

<div id="treeDiv">
  	<ul id="menuTree">
  	</ul>
 </div>
<div id="assignRolesToUserTaskDialog" style="display: none;">
	  <div id="assignRolesToUserTask">
	  </div>
</div>
<div id="assignUsersToUserTaskDialog" style="display: none;">
	  <div id="assignUsersToUserTask">
	  </div>
</div>
</body>
</html>