<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	流程定义界面
	*/
	var _dialog;
	var gridData;
	$(function(){
		PageLoader.initGridPanel();//显示所有流程定义
		loadGridData();
	});
	PageLoader = {
			initGridPanel:function(){
				gridManager = $("#maingrid").ligerGrid({
					width : '98%',
		            height:'100%',
		            checkbox:true,
		            columns: [ {
						display : "编码",
						name : "id",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "代码",
						name : "key",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "名称",
						name : "name",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "分类",
						name : "category",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "版本",
						name : "version",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "描述",
						name : "description",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "状态",
						width : 200,
						align : "left",
						render: function (rowdata, rowindex, value)
		                {
							var html;
							if(rowdata.suspended){
								html = '挂起';
								html += '<a href="javascript:activeProcessDefinition(\''+ rowdata.id +'\')">(激活)</a>';
							}
							if(!rowdata.suspended){
								html = '激活';
								html += '<a href="javascript:suspendProcessDefinition(\''+ rowdata.id +'\')">(挂起)</a>';
							}
		                    return html;
		                }
					},{
						width : 200,
						align : "left",
						render: function (rowdata, rowindex, value)
						{
							var html;
							html = '<a href="javascript:removeProcessDeployment(\''+ rowdata.deploymentId +'\','+ rowindex +')">删除</a>';
							html += '&nbsp;&nbsp;';
							html += '<a href="${pageContext.request.contextPath}/bpmAdmin/graphProcessDefinition.do?processDefinitionId='+ rowdata.id +'" target="_blank">查看流程图</a>';
							return html;
						}
					}],
					data:gridData,
		            pageSize:10,
		            rownumbers:true,
		            toolbar: { items: [{
      							id : "userTask",
      							text : '流程任务分配',
      							click : itemclick,
      							img : rootPath + "/images/icons/toolbar/page_edit.gif"
      						},{
      							id : "starter",
      							text : '流程实例授权',
      							click : itemclick,
      							img : rootPath + "/images/icons/toolbar/page_edit.gif"
      						}]}
		        });
			}
	};
	
	/*加载grid数据*/
	function loadGridData(){
		var url = "${pageContext.request.contextPath}/bpmAdmin/listProcessDefinitions.do";
		$.ajax({
		      url: url,
		      type: "POST",
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 gridData = json;
		        	 //console.info(JSON.stringify(gridData));
		        	 gridManager.options.newPage = 1;
					 gridManager.loadData(gridData);
		         }
		      }
		   }
		);
	}
	
	/*暂停流程定义*/
	function suspendProcessDefinition(processDefinitionId){
		var url = "${pageContext.request.contextPath}/bpmAdmin/suspendProcessDefinition.do";
		var data = {processDefinitionId:processDefinitionId};
		$.ajax({
		      url: url,
		      type: "POST",
		      data: data,
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 loadGridData();
		         }
		      }
		   }
		);
	}
	/*删除流程定义*/
	function removeProcessDeployment(deploymentId,rowindex){
		var url = "${pageContext.request.contextPath}/bpmAdmin/removeProcessDeployment.do";
		var data = {deploymentId:deploymentId};
		$.ajax({
		      url: url,
		      type: "POST",
		      data: data,
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 alert(json.result);
		        	 gridManager.remove(gridManager.getRow(rowindex));
		         }
		      }
		   }
		);
	}
	/*恢复流程定义*/
	function activeProcessDefinition(processDefinitionId){
		console.info(processDefinitionId);
		var url = "${pageContext.request.contextPath}/bpmAdmin/activeProcessDefinition.do";
		var data = {processDefinitionId:processDefinitionId};
		$.ajax({
		      url: url,
		      type: "POST",
		      data: data,
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 loadGridData();
		         }
		      }
		   }
		);
	}
	
	function itemclick(item){
		if("userTask" == item.id){
			if(selectRow()){
				top.f_addTab(selectedRow.name + "的任务管理","/pages/workflow/admin/assignUserTask.jsp?processDefinitionId=" + selectedRow.id);
			}
		} else if("starter" == item.id){
			openStarterDialog();
		}
	}
	
	var selectedRow;
	function selectRow(){
		 selectedRow = gridManager.getSelected();
	     if (!selectedRow) { alert('请选择行'); return false; }
	     var i = 0;
	     $.each(gridManager.getSelectedRows(), function(index, element) {
			  i++;
		 });
	     if(i>1){
	    	 alert('请只选择一行'); return false;
	     }
	     return true;
	}
	
	/*打开流程实例授权对话框*/
	var starterDialog;
	function openStarterDialog(){
		if(!selectRow()){
			return;
		}
		starterDialog = $.ligerDialog.open({
			title : "用户",
			isResize : true,
			width : 480,
			height : 520,
			isHidden : true,
			buttons : [ {
				id : 'cancelStarterDialog',
				text : '关闭',
				onclick : btnClick
			} ],
			target : $("#starterDialog")
		});
		loadStarter();
	}
	
	/*查找流程实例已有授权*/
	function loadStarter(){
		starterGrid = $("#starterGrid").ligerGrid({
			columns : [ {
				display : "执行人",
				name : "starter",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "类型",
				name : "starterType",
				width : 200,
				type : "text",
				align : "left"
			} ],
			pageSize:10,
			url : '${pageContext.request.contextPath}/bpmAdmin/findStarterByProcDefId.do',
			parms : {procDefId:selectedRow.id},
			rownumbers : true,
			width : 450,
			height : 430,
			checkbox : true,
			toolbar: { items: [
          	        { 
          	          	id:'assignRole',
          	          	text: '分配角色',
          	          	click: starterItemClick, 
          	          	img : rootPath + "/images/icons/toolbar/add.png" 
          	        },{ 
          	        	line: true 
          	        },{ 
          	          	id:'assignUser',
          	          	text: '分配用户',
          	          	click: starterItemClick, 
          	          	img : rootPath + "/images/icons/toolbar/add.png" 
          	        },{ 
          	        	line: true 
          	        },{ 
          	        	id:'deleteAssign',
          	        	text: '删除分配', 
          	        	click: starterItemClick, 
          	        	img : rootPath + "/images/icons/toolbar/page_delete.gif"
          	        }]}
		});
	}
	
	function btnClick(button) {
		if("cancelStarterDialog" == button.id){
			starterDialog.hidden();
		} else if("assignRolesToProcDef" == button.id){
			assignRolesToProcDefAction();
		} else if("cancelAssignRolesToProcDef" == button.id){
			assignRolesToProcDefDialog.hidden();
		} else if("assignUsersToProcDef" == button.id){
			assignUsersToProcDefAction();
		} else if("cancelAssignUsersToProcDef" == button.id){
			assignUsersToProcDefDialog.hidden();
		}
	}
	
	function starterItemClick(item){
		if("assignRole" == item.id){
			openAssignRolesToProcDefDialog();
		} else if("assignUser" == item.id){
			openAssignUsersToProcDefDialog();
		} else if("deleteAssign" == item.id){
			deleteAssignForProcDef();
		}
	}
	
	/*打开角色分配对话框*/
	var assignRolesToProcDefDialog;
	function openAssignRolesToProcDefDialog(){
		assignRolesToProcDefDialog = $.ligerDialog.open({
			title : "角色",
			isResize : true,
			width : 480,
			height : 520,
			isHidden : true,
			buttons : [ {
				id : 'assignRolesToProcDef',
				text : '保存',
				onclick : btnClick
			}, {
				id : 'cancelAssignRolesToProcDef',
				text : '取消',
				onclick : btnClick
			} ],
			target : $("#assignRolesToProcDefDialog")
		});
		loadRoles();
	}
	
	// 待选角色列表结构
	var roleGrid;
	function loadRoles() {
		roleGrid = $("#assignRolesToProcDef").ligerGrid({
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
			url : '${pageContext.request.contextPath}/bpmAdmin/findNotAssignRoleByProcDef.do',
			parms : {procDefId:selectedRow.id},
			rownumbers : true,
			width : 450,
			height : 430,
			checkbox : true
		});
	}
	/*为流程定义分配可执行角色*/
	function assignRolesToProcDefAction(){
		var newRow = roleGrid.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var roleIds = [];
		$.each(roleGrid.getSelectedRows(), function(index, element) {
			roleIds.push(element.id);
		});
		roleIds = roleIds.join(",");
		var url,data;
		url = "${pageContext.request.contextPath}/bpmAdmin/assignRoleToProcDef.do";
		data = {procDefId:selectedRow.id,roleIds:roleIds};
		
		$.ajax({
		      url: url,
		      data: data,
		      type: "POST",
		      success: function(json){
		         if(json){
		        	 loadStarter();
		        	 assignRolesToProcDefDialog.hidden();
		         }
		      }
		   }
		);
	}
	/*打开用户分配对话框*/
	var assignUsersToProcDefDialog;
	function openAssignUsersToProcDefDialog(){
		assignUsersToProcDefDialog = $.ligerDialog.open({
			title : "用户",
			isResize : true,
			width : 480,
			height : 520,
			isHidden : true,
			buttons : [ {
				id : 'assignUsersToProcDef',
				text : '保存',
				onclick : btnClick
			}, {
				id : 'cancelAssignUsersToProcDef',
				text : '取消',
				onclick : btnClick
			} ],
			target : $("#assignUsersToProcDefDialog")
		});
		loadUsers();
	}
	
	// 待选用户列表结构
	var userGrid;
	function loadUsers() {
		userGrid = $("#assignUsersToProcDef").ligerGrid({
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
			url : '${pageContext.request.contextPath}/bpmAdmin/findNotAssignUserByProcDef.do',
			parms : {procDefId:selectedRow.id},
			rownumbers : true,
			width : 450,
			height : 430,
			checkbox : true
		});
	}
	/*为流程定义分配可执行用户*/
	function assignUsersToProcDefAction(){
		var newRow = userGrid.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var userIds = [];
		$.each(userGrid.getSelectedRows(), function(index, element) {
			userIds.push(element.id);
		});
		userIds = userIds.join(",");
		var url,data;
		url = "${pageContext.request.contextPath}/bpmAdmin/assignUserToProcDef.do";
		data = {procDefId:selectedRow.id,userIds:userIds};
		
		$.ajax({
		      url: url,
		      data: data,
		      type: "POST",
		      success: function(json){
		         if(json){
		        	 loadStarter();
		        	 assignUsersToProcDefDialog.hidden();
		         }
		      }
		   }
		);
	}
	/*删除流程定义可执行用户或角色*/
	function deleteAssignForProcDef(){
		var newRow = starterGrid.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var removeData = [];
		$.each(starterGrid.getSelectedRows(), function(index, element) {
			removeData.push(element.id);
		});
		removeData = removeData.join(",");
		$.ajax({ 
		    type:"POST", 
		    url:"${pageContext.request.contextPath}/bpmAdmin/removeAssignForProcDef.do", 
		    data:{ids:removeData},
		    success:function(json){ 
		    	if(json.error){
					alert(json.error);
					return;
				}
				if(json && json.result){
					starterGrid.removeRange(starterGrid.getSelectedRows());
					alert(json.result);
				}                    
		    } 
		 });
		
	}
</script>
<title>Insert title here</title>
</head>
<body>
<div id="maingrid"></div>
<div id="starterDialog" style="display: none;">
	  <div id="starterGrid">
	  </div>
</div>
<div id="assignRolesToProcDefDialog" style="display: none;">
	  <div id="assignRolesToProcDef">
	  </div>
</div>
<div id="assignUsersToProcDefDialog" style="display: none;">
	  <div id="assignUsersToProcDef">
	  </div>
</div>
</body>
</html>