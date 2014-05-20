<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	webservice资源管理界面
	 */
	$(function() {
		PageLoader.initGridPanel();//显示所有资源
	});
	PageLoader = {
		initGridPanel : function() {
			gridManager = $("#maingrid")
					.ligerGrid(
							{
								width : '98%',
								height : '100%',
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
								url : '${pageContext.request.contextPath}/ws/resource/pageQueryResource.do',
								pageSize : 10,
								rownumbers : false,
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
											} ]
								}
							});
		}
	};

	function itemclick(item) {
		if ("add" == item.id) {
			openAddResourceDialog();
		} else if ("modify" == item.id) {
			openModifyResourceDialog();
		} else if ("delete" == item.id) {
			deleteResourceAction();
		}
	}

	/*打开增加资源对话框*/
	var addResourceDialog;
	function openAddResourceDialog() {
		addResourceDialog = $.ligerDialog.open({
			title : "增加资源",
			isResize : true,
			width : 550,
			height : 300,
			isHidden : true,
			buttons : [ {
				id : 'addResource',
				text : '保存',
				onclick : dialogBtnClick
			}, {
				id : 'cancelAddResource',
				text : '取消',
				onclick : dialogBtnClick
			} ],
			target : $("#addResourceDialog")
		});
	}

	/*处理对话框事件*/
	function dialogBtnClick(button) {
		if ("cancelAddResource" == button.id) {
			addResourceDialog.hidden();
		} else if ("addResource" == button.id) {
			addResourceAction();
		} else if ("cancelModifyResource" == button.id) {
			modifyResourceDialog.hidden();
		} else if ("modifyResource" == button.id) {
			modifyResourceAction();
		}
	}

	/*新增资源后台操作*/
	function addResourceAction() {
		$.ajax({
					type : "POST",
					url : "${pageContext.request.contextPath}/ws/resource/saveResource.do",
					data : $("#add_resource_form").serialize(),
					success : function(json) {
						if (json.error) {
							$.ligerDialog.error(json.error);
						}
						if (json && json.result) {
							$.ligerDialog.success(json.result);
							gridManager.loadData();
						}
						addResourceDialog.hidden();
					}
				});
	}

	/*打开修改资源对话框*/
	var modifyResourceDialog;
	function openModifyResourceDialog() {
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
		modifyResourceDialog = $.ligerDialog.open({
			title : "修改资源",
			isResize : true,
			width : 550,
			height : 300,
			isHidden : true,
			buttons : [ {
				id : 'modifyResource',
				text : '保存',
				onclick : dialogBtnClick
			}, {
				id : 'cancelModifyResource',
				text : '取消',
				onclick : dialogBtnClick
			} ],
			target : $("#modifyResourceDialog")
		});
		loadResourceData(newRow.id);
	}

	/*获得用户信息*/
	function loadResourceData(id) {
		if ("" == id)
			return;
		$.ajax({
					url : "${pageContext.request.contextPath}/ws/resource/findResourceById/"
							+ id + ".do",
					success : function(json) {
						if (json && json.data) {
							json = json.data;
							var elm;
							for ( var index in json) {
								elm = $("#" + index + "ID",
										"#modify_resource_form");
								if (elm) {
									if ("checkbox" == $(elm).attr("type")) {
										if (json[index]) {
											$(elm).attr("checked", "checked");
										}
									} else if ("SELECT" == elm.nodeName) {
										$(elm).find(
												"option[value='" + json[index]
														+ "']").attr(
												"selected", "selected");
									} else {
										$(elm).val(json[index]);
									}
								}
							}
						}
					}
				});
	}

	function modifyResourceAction() {
		$.ajax({
					type : "POST",
					url : "${pageContext.request.contextPath}/ws/resource/updateResource.do",
					data : $("#modify_resource_form").serialize(),
					success : function(json) {
						if (json.error) {
							$.ligerDialog.error(json.error);
						}
						if (json && json.result) {
							$.ligerDialog.success(json.result);
							gridManager.loadData();
						}
						modifyResourceDialog.hidden();
					}
				});
	}
	/*删除选中的资源*/
	function deleteResourceAction() {
		var newRow = gridManager.getSelected();
		if (!newRow) {
			$.ligerDialog.warn('请选择行');
			return;
		}
		$.ligerDialog.confirm(
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
								data["resourceIds"] = removeData;
								//return;
								$.ajax({
											type : "POST",
											url : "${pageContext.request.contextPath}/ws/resource/removeResource.do",
											data : data,
											success : function(json) {
												if (json.error) {
													$.ligerDialog.error(json.error);
												}
												if (json && json.result) {
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
	<div id="addResourceDialog" style="display: none;">
		<form id="add_resource_form">
			<table id="form_table" border="0" cellpadding="0" cellspacing="0"
				class="form2column">
				<tr>
					<td class="label">资源名称:</td>
					<td><input name="text" class="input-common" type="text"
						id="textID" /></td>
				</tr>
				<tr>
					<td class="label">资源标识:</td>
					<td><input name="identifier" class="input-common" type="text"
						id="identifierID" /></td>
				</tr>
				<tr>
					<td class="label">资源描述:</td>
					<td><input name="description" class="input-common" type="text"
						id="descriptionID" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="modifyResourceDialog" style="display: none;">
		<form id="modify_resource_form">
			<table id="form_table" border="0" cellpadding="0" cellspacing="0"
				class="form2column">
				<input name="id" type="hidden" id="idID" />
				<tr>
					<td class="label">资源名称:</td>
					<td><input name="text" class="input-common" type="text"
						id="textID" /></td>
				</tr>
				<tr>
					<td class="label">资源标识:</td>
					<td><input name="identifier" class="input-common" type="text"
						id="identifierID" /></td>
				</tr>
				<tr>
					<td class="label">资源描述:</td>
					<td><input name="description" class="input-common" type="text"
						id="descriptionID" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>