<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	webservice角色管理界面
	 */
	var resourceGridManager;
	var gridManager;
	$(function() {
		// 布局
		$("#main-content").ligerLayout({
			leftWidth : 500,
			height : '100%',
			heightDiff : -34,
			space : 4,
			allowLeftCollapse : false
		});

		PageLoader.initGridPanel();//获得所有角色
	});
	PageLoader = {//初始化角色grid
		initGridPanel : function() {
			gridManager = $("#maingrid").ligerGrid({
								width : '98%',
								height : '95%',
								columns : [
										{
											display : "角色名称",
											name : "name",
											width : 100,
											type : "text",
											align : "left"
										},
										{
											display : "角色描述",
											name : "roleDesc",
											width : 100,
											type : "text",
											align : "left"
										},
										{
											display : "是否有效",
											name : "valid",
											width : 50,
											type : "text",
											align : "left",
											render : function(rowdata) {
												return rowdata.valid == true ? "是": "否";
											}
										} ],
								url : '${pageContext.request.contextPath}/ws/role/pageQueryRole.do',
								pageSize : 10,
								rownumbers : false,
								heightDiff : -10,
								headerRowHeight : 30,//表头行的高度
								toolbar : {
									items : [
											{
												id : 'add',
												text : '增加',
												click : itemclick,
												img : rootPath
														+ "/images/icons/toolbar/add.png"
											},
											{
												line : true
											},
											{
												id : 'modify',
												text : '修改',
												click : itemclick,
												img : rootPath
														+ "/images/icons/toolbar/page_edit.gif"
											},
											{
												line : true
											},
											{
												id : 'delete',
												text : '删除',
												click : itemclick,
												img : rootPath
														+ "/images/icons/toolbar/page_delete.gif"
											},
											{
												line : true
											},
											{
												id : "user",
												text : '用户',
												click : itemclick,
												img : rootPath
														+ "/images/icons/toolbar/page_edit.gif"
											} ]
								},
								onSelectRow : roleGridSelect
							});
			resourceGridManager = $("#resourcegrid").ligerGrid({
								width : '98%',
								height : '95%',
								checkbox : true,
								columns : [ {
									display : "资源名称",
									name : "text",
									width : 100,
									type : "text",
									align : "left"
								}, {
									display : "资源标识",
									name : "identifier",
									width : 200,
									type : "text",
									align : "left"
								}, {
									display : "资源描述",
									name : "description",
									width : 200,
									type : "text",
									align : "left"
								} ],
								pageSize : 10,
								rownumbers : false,
								heightDiff : -10,
								delayLoad:true,//初始化时是否不加载
								url:'${pageContext.request.contextPath}/ws/resource/pageQueryResourceByRoleId.do',
								toolbar : {
									items : [
											{
												id : 'add',
												text : '分配权限',
												click : resourceItemclick,
												img : rootPath
														+ "/images/icons/toolbar/add.png"
											},
											{
												line : true
											},
											{
												id : 'delete',
												text : '删除权限',
												click : resourceItemclick,
												img : rootPath
														+ "/images/icons/toolbar/page_delete.gif"
											} ]
								}
							});
		}
	};

	/*选择roleGrid一行后*/
	var selectedRole;
	function roleGridSelect(rowdata, rowid, rowobj) {
		selectedRole = rowdata;
		loadResourceGridData(rowdata.id);
	}

	/*获得角色所拥有的资源*/
	function loadResourceGridData(roleId) {
		resourceGridManager.setParm('roleId',roleId);
		resourceGridManager.loadData(true);//通过url,加载服务端数据
	}
	
	function itemclick(item){
		if("add" == item.id){
			openAddRoleDialog();
		} else if("modify" == item.id){
			openModifyRoleDialog();
		} else if("delete" == item.id){
			deleteRoleAction();
		} else if("user" == item.id){
			if(selectRow()){
				top.forwardTabEvent(selectedRow.name + "的用户管理","/pages/ws/userListByRole.jsp?roleId=" + selectedRow.id);
			}
		}
	}
	
	
	var selectedRow;
	function selectRow(){
		 selectedRow = gridManager.getSelected();
	     if (!selectedRow) { $.ligerDialog.warn('请选择行'); return false; }
	     var i = 0;
	     $.each(gridManager.getSelectedRows(), function(index, element) {
			  i++;
		 });
	     if(i>1){
	    	 $.ligerDialog.warn('请只选择一行'); return false;
	     }
	     return true;
	}
	
	/*角色增加对话框*/
	var addRoleDialog;
	function openAddRoleDialog(){
		addRoleDialog = $.ligerDialog.open({
			title : "增加角色",
			isResize : true,
			width : 550,
			height : 300,
			isHidden : true,
			buttons : [ {
				id : 'addRole',
				text : '保存',
				onclick : dialogBtnClick
			}, {
				id : 'cancelAddRole',
				text : '取消',
				onclick : dialogBtnClick
			} ],
			target : $("#addRoleDialog")
		});
	}
	
	/*处理对话框事件*/
	function dialogBtnClick(button) {
		if("cancelAddRole" == button.id){
			addRoleDialog.hidden();
		} else if("addRole" == button.id){
			addRoleAction();
		} else if("cancelModifyRole" == button.id){
			modifyRoleDialog.hidden();
		} else if("modifyRole" == button.id){
			modifyRoleAction();
		} 
	}
	
	/*新增角色后台操作*/
	function addRoleAction(){
		$.ajax({ 
		    type:"POST", 
		    url:"${pageContext.request.contextPath}/ws/role/saveRole.do", 
		    data:$("#add_role_form").serialize(),
		    success:function(json){ 
		    	if(json.error){
		    		$.ligerDialog.error(json.error);
				}
				if(json && json.result){
					$.ligerDialog.success(json.result);
					gridManager.loadData();
				}
				addRoleDialog.hidden();
		    } 
		 });
	}
	
	/*打开修改角色对话框*/
	var modifyRoleDialog;
	function openModifyRoleDialog(){
		var newRow = gridManager.getSelected();
		if (!newRow) { $.ligerDialog.warn('请选择行'); return; }
		var i = 0;
		$.each(gridManager.getSelectedRows(), function(index, element) {
			i++;
		});
		if(i>1){
			$.ligerDialog.warn('请只选择一行'); return;
		}
		modifyRoleDialog = $.ligerDialog.open({
			title : "修改角色",
			isResize : true,
			width : 550,
			height : 300,
			isHidden : true,
			buttons : [ {
				id : 'modifyRole',
				text : '保存',
				onclick : dialogBtnClick
			}, {
				id : 'cancelModifyRole',
				text : '取消',
				onclick : dialogBtnClick
			} ],
			target : $("#modifyRoleDialog")
		});
		loadRoleData(newRow.id);
	}
	
	/*获得角色信息*/
	function loadRoleData(id){
		if(""==id) return;
		$.ajax({
			url:"${pageContext.request.contextPath}/ws/role/findRoleById/"+ id +".do",
			success:function(json){
				if(json && json.data){
					 json = json.data;
					 var elm;
					 for(var index in json){
						 elm = $("#" + index + "ID","#modify_role_form");
						 if(elm){
							 if("checkbox" == $(elm).attr("type")){
								 if(json[index]){
									 $(elm).attr("checked","checked");
								 }
							 } else if("SELECT" == elm.nodeName){
								 $(elm).find("option[value='"+json[index]+"']").attr("selected","selected");
							 } else{
								 $(elm).val(json[index]);
							 }
						 }
					 }
				}
			}
		});
	}
	
	function modifyRoleAction(){
		$.ajax({ 
		    type:"POST", 
		    url:"${pageContext.request.contextPath}/ws/role/updateRole.do", 
		    data:$("#modify_role_form").serialize(),
		    success:function(json){ 
		    	if(json.error){
		    		$.ligerDialog.error(json.error);
				}
				if(json && json.result){
					$.ligerDialog.success(json.result);
					gridManager.loadData();
				}
				modifyRoleDialog.hidden();
		    } 
		 });
	}
	
	/*删除角色*/
	function deleteRoleAction(){
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
				data["ids"] = removeData;
				//return;
				$.ajax({ 
				    type:"POST", 
				    url:"${pageContext.request.contextPath}/ws/role/removeRole.do",  
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
	
	function resourceItemclick(item){
		if("add" == item.id){
			if(selectedRole){
				openAssignResourceToRoleDialog();
			} else {
				$.ligerDialog.warn("请选择角色");
			}
		} else if("delete" == item.id){
			removeResourceForRoleAction();
		}
	}
	/*分配资源对话框*/
	var assignResourceToRoleDialog;
	function openAssignResourceToRoleDialog(){
		assignResourceToRoleDialog = $.ligerDialog.open({
			title : "分配资源",
			isResize : true,
			width : 600,
			height : 500,
			isHidden : true,
			buttons : [ {
				id : 'assignResource',
				text : '保存',
				onclick : dialogBtnClick
			}, {
				id : 'cancelAssignResource',
				text : '取消',
				onclick : dialogBtnClick
			} ],
			target : $("#assignResourceToRoleDialog")
		});
		initNoAssignResourceGrid();
	}
	var noAssignResourcegridManager;
	/*加载未分配的资源grid*/
	function initNoAssignResourceGrid(){
		noAssignResourcegridManager = $("#noAssignResourcegrid").ligerGrid({
			columns: [ {
				display : "资源名称",
				name : "text",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "资源标识",
				name : "identifier",
				width : 100,
				type : "text",
				align : "left"
			},{
				display : "资源描述",
				name : "description",
				width : 200,
				type : "text",
				align : "left"
			} ],
			pageSize : 10,
			url : '${pageContext.request.contextPath}/ws/resource/findResourceNotAssignToRole.do',
			parms : {roleId:selectedRole.id},
			rownumbers : true,
			width : 550,
			height : 430,
			checkbox : true
        });
	}
	
	/*处理对话框事件*/
	function dialogBtnClick(button) {
		if("cancelAssignResource" == button.id){
			assignResourceToRoleDialog.hidden();
		} else if("assignResource" == button.id){
			assignResourceToUserAction();
		} 
	}
	/*为资源分配角色*/
	function assignResourceToUserAction(){
		var newRow = noAssignResourcegridManager.getSelected();
		if (!newRow) { $.ligerDialog.warn('请选择行'); return; }
		var resourceIds = [];
		$.each(noAssignResourcegridManager.getSelectedRows(), function(index, element) {
			resourceIds.push(element.id);
		});
		resourceIds = resourceIds.join(",");
		
		var data = {};
		data["resourceIds"] = resourceIds;
		data["roleId"] = selectedRole.id;
		
		$.ajax({ 
		    type:"POST", 
		    url:"${pageContext.request.contextPath}/ws/role/assignResourceToRole.do",  
		    data:data,
		    success:function(json){ 
		    	if(json.error){
		    		$.ligerDialog.error(json.error);
				}
				if(json && json.result){
					$.ligerDialog.success(json.result);
					resourceGridManager.loadData();
				}
				assignResourceToRoleDialog.hidden();                   
		    } 
		 }); 
	}
	
	/*删除分配的资源*/
	function removeResourceForRoleAction(){
		var newRow = resourceGridManager.getSelected();
		if (!newRow) { $.ligerDialog.warn('请选择行'); return; }
		$.ligerDialog.confirm("确认删除?",function(flag){
			if(flag){
				var removeData = [];
				$.each(resourceGridManager.getSelectedRows(), function(index, element) {
					removeData.push(element.id);
				});
				removeData = removeData.join(",");
				var data = {};
				data["resourceIds"] = removeData;
				data["roleId"] = selectedRole.id;
				//return;
				$.ajax({ 
				    type:"POST", 
				    url:"${pageContext.request.contextPath}/ws/role/removeResourceForRole.do",  
				    data:data,
				    success:function(json){ 
				    	if(json.error){
				    		$.ligerDialog.error(json.error);
						}
						if(json && json.result){
							$.ligerDialog.success(json.result);
							resourceGridManager.loadData();
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

	<div id="main-content"
		style="width: 99.2%; margin: 0 auto; margin-top: 4px;">
		<div position="left" id="left" title="角色管理">
			<div id="maingrid"></div>
		</div>
		<div position="center" id="center" title="资源管理">
			<div id="resourcegrid"></div>
		</div>
	</div>


	<div id="addRoleDialog" style="display: none;">
		<form id="add_role_form">
		<table id="form_table" border="0" cellpadding="0" cellspacing="0"
			class="form2column">
			<tr>
				<td class="label">角色名称:</td>
				<td><input name="name" class="input-common" type="text"id="nameID" /></td>
			</tr>
			<tr>
				<td class="label">角色描述:</td>
				<td><input name="roleDesc" class="input-common" type="text" id="roleDescID" /></td>
			</tr>
			<tr>
				<td class="label">是否有效:</td>
				<td><input name="valid" class="input-common" type="checkbox" id="validID" checked="checked" /></td>
			</tr>
		</table>
	</form>
	</div>
	<div id="modifyRoleDialog" style="display: none;">
		<form id="modify_role_form">
		<table id="form_table" border="0" cellpadding="0" cellspacing="0"
			class="form2column">
			<input name="id" type="hidden" id="idID" />
			<tr>
				<td class="label">角色名称:</td>
				<td><input name="name" class="input-common" type="text" id="nameID" /></td>
			</tr>
			<tr>
				<td class="label">角色描述:</td>
				<td><input name="roleDesc" class="input-common" type="text" id="roleDescID" /></td>
			</tr>
			<tr>
				<td class="label">是否有效:</td>
				<td><input name="valid" class="input-common" type="checkbox" id="validID" /></td>
			</tr>
		</table>
	</form>
	</div>
	
	<div id="assignResourceToRoleDialog" style="display: none;">
		<div id="noAssignResourcegrid"></div>
	</div>
</body>
</html>