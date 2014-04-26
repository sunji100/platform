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
	var resourceGridData;
	$(function() {
		// 布局
		$("#main-content").ligerLayout({
			leftWidth : 500,
			height : '100%',
			heightDiff : -34,
			space : 4,
			allowLeftCollapse : false
		});

		var height = $(".l-layout-center").height();

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
								data : resourceGridData,
								pageSize : 10,
								rownumbers : false,
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
	function roleGridSelect(rowdata, rowid, rowobj) {
		loadResourceGridData(rowdata.id);
	}

	/*获得角色所拥有的资源*/
	function loadResourceGridData(roleId) {
		$.ajax({
					url : "${pageContext.request.contextPath}/resource/findResourceByRole.do",
					data : "roleId=" + roleId,
					type : "POST",
					success : function(json) {
						if (json) {
							resourceGridData = json;
							resourceGridManager.options.newPage = 1;
							resourceGridManager.loadData(resourceGridData);
						}
					}
				});
	}

	function itemclick(item) {
		if ("add" == item.id) {
			openAddUserDialog();
		} else if ("modify" == item.id) {
			openModifyUserDialog();
		} else if ("delete" == item.id) {
			deleteUserAction();
		} else if ("role" == item.id) {
			if (selectRow()) {
				top.forwardTabEvent(selectedRow.name + "的角色管理","/pages/auth/role/roleListByUser.jsp?userId="+ selectedRow.id);
			}
		}
	}

	function resourceItemclick(item) {
		if ("add" == item.id) {
			if (selectedRole) {
				openAssignResourceToRoleDialog();
			} else {
				alert("请选择角色");
			}
		} else if ("delete" == item.id) {
			removeResourceForRoleAction();
		}
	}

	var selectedRow;
	function selectRow() {
		selectedRow = gridManager.getSelected();
		if (!selectedRow) {
			$.ligerDialog.warn('请选择行');
			return false;
		}
		var i = 0;
		$.each(gridManager.getSelectedRows(), function(index, element) {
			i++;
		});
		if (i > 1) {
			$.ligerDialog.warn('请只选择一行');
			return false;
		}
		return true;
	}

	/*打开增加用户对话框*/
	var addUserDialog;
	function openAddUserDialog() {
		addUserDialog = $.ligerDialog.open({
			title : "增加用户",
			isResize : true,
			width : 550,
			height : 300,
			isHidden : true,
			buttons : [ {
				id : 'addUser',
				text : '保存',
				onclick : dialogBtnClick
			}, {
				id : 'cancelAddUser',
				text : '取消',
				onclick : dialogBtnClick
			} ],
			target : $("#addUserDialog")
		});

	}

	/*处理对话框事件*/
	function dialogBtnClick(button) {
		if ("cancelAddUser" == button.id) {
			addUserDialog.hidden();
		} else if ("addUser" == button.id) {
			addUserAction();
		} else if ("cancelModifyUser" == button.id) {
			modifyUserDialog.hidden();
		} else if ("modifyUser" == button.id) {
			modifyUserAction();
		}
	}

	/*新增用户后台操作*/
	function addUserAction() {
		$.ajax({
			type : "POST",
			url : "${pageContext.request.contextPath}/ws/user/saveUser.do",
			data : $("#add_user_form").serialize(),
			success : function(json) {
				if (json.error) {
					$.ligerDialog.error(json.error);
				}
				if (json && json.result) {
					$.ligerDialog.success(json.result);
					gridManager.loadData();
				}
				addUserDialog.hidden();
			}
		});
	}

	/*删除用户*/
	function deleteUserAction() {
		var newRow = gridManager.getSelected();
		if (!newRow) {
			$.ligerDialog.warn('请选择行');
			return;
		}
		$.ligerDialog
				.confirm(
						"确认删除?",
						function(flag) {
							if (flag) {
								var removeData = [];
								$.each(gridManager.getSelectedRows(), function(
										index, element) {
									removeData.push(element.id);
								});
								removeData = removeData.join(",");
								var data = {};
								data["ids"] = removeData;
								//return;
								$
										.ajax({
											type : "POST",
											url : "${pageContext.request.contextPath}/ws/user/removeUser.do",
											data : data,
											success : function(json) {
												if (json.error) {
													$.ligerDialog
															.error(json.error);
												}
												if (json && json.result) {
													$.ligerDialog
															.success(json.result);
													gridManager
															.removeRange(gridManager
																	.getSelectedRows());
												}

											}
										});
							}
						});

	}

	/*打开修改用户对话框*/
	var modifyUserDialog;
	function openModifyUserDialog() {
		var newRow = gridManager.getSelected();
		if (!newRow) {
			$.ligerDialog.warn('请选择行');
			return;
		}
		var i = 0;
		$.each(gridManager.getSelectedRows(), function(index, element) {
			i++;
		});
		if (i > 1) {
			$.ligerDialog.warn('请只选择一行');
			return;
		}
		modifyUserDialog = $.ligerDialog.open({
			title : "修改用户",
			isResize : true,
			width : 550,
			height : 300,
			isHidden : true,
			buttons : [ {
				id : 'modifyUser',
				text : '保存',
				onclick : dialogBtnClick
			}, {
				id : 'cancelModifyUser',
				text : '取消',
				onclick : dialogBtnClick
			} ],
			target : $("#modifyUserDialog")
		});
		loadUserData(newRow.id);
	}

	/*获得用户信息*/
	function loadUserData(id) {
		if ("" == id)
			return;
		$.ajax({
			url : "${pageContext.request.contextPath}/ws/user/findUserById/"
					+ id + ".do",
			success : function(json) {
				if (json && json.data) {
					json = json.data;
					var elm;
					for ( var index in json) {
						elm = $("#" + index + "ID", "#modify_user_form");
						if (elm) {
							if ("checkbox" == $(elm).attr("type")) {
								if (json[index]) {
									$(elm).attr("checked", "checked");
								}
							} else if ("SELECT" == elm.nodeName) {
								$(elm).find(
										"option[value='" + json[index] + "']")
										.attr("selected", "selected");
							} else {
								$(elm).val(json[index]);
							}
						}
					}
				}
			}
		});
	}

	function modifyUserAction() {
		$.ajax({
			type : "POST",
			url : "${pageContext.request.contextPath}/ws/user/updateUser.do",
			data : $("#modify_user_form").serialize(),
			success : function(json) {
				if (json.error) {
					$.ligerDialog.error(json.error);
				}
				if (json && json.result) {
					$.ligerDialog.success(json.result);
					gridManager.loadData();
				}
				modifyUserDialog.hidden();
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
			<div id="menugrid"></div>
		</div>
	</div>


	<div id="addUserDialog" style="display: none;">
		<form id="add_user_form">
			<table id="form_table" border="0" cellpadding="0" cellspacing="0"
				class="form2column">

				<tr>
					<td class="label">用户名称:</td>
					<td><input name="name" class="input-common" type="text"
						id="nameID" /></td>
				</tr>
				<tr>
					<td class="label">用户账户:</td>
					<td><input name="userAccount" class="input-common" type="text"
						id="userAccountID" /></td>
				</tr>
				<tr>
					<td class="label">用户密码:</td>
					<td><input name="userPassword" class="input-common"
						type="text" id="userPasswordID" /></td>
				</tr>
				<tr>
					<td class="label">用户描述:</td>
					<td><input name="userDesc" class="input-common" type="text"
						id="userDescID" /></td>
				</tr>
				<tr>
					<td class="label">是否有效:</td>
					<td><input name="valid" class="input-common" type="checkbox"
						id="validID" checked="checked" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="modifyUserDialog" style="display: none;">
		<form id="modify_user_form">
			<table id="form_table" border="0" cellpadding="0" cellspacing="0"
				class="form2column">
				<input name="id" type="hidden" id="idID" />
				<tr>
					<td class="label">用户名称:</td>
					<td><input name="name" class="input-common" type="text"
						id="nameID" /></td>
				</tr>
				<tr>
					<td class="label">用户账户:</td>
					<td><input name="userAccount" class="input-common" type="text"
						id="userAccountID" readonly="readonly" /></td>
				</tr>
				<tr>
					<td class="label">用户描述:</td>
					<td><input name="userDesc" class="input-common" type="text"
						id="userDescID" /></td>
				</tr>
				<tr>
					<td class="label">是否有效:</td>
					<td><input name="valid" class="input-common" type="checkbox"
						id="validID" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>