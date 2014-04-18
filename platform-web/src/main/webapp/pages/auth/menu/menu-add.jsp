<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	var parentId = "${param.parentId}";//父菜单ID
	var parentText = "${param.parentText}";
	var combox;
	$(function(){
		autoResize();
		initCombox();
		initAddForm();
	});
	/*初始化画面数据*/
	function initAddForm(){
		//$("#parentIdID").val(parentId);
		//$("#parentTextID").val(parentText);
		if(parentId){
			combox.selectValue(parentId);
		}
		$("#menuIcon").attr("src", rootPath + "/images/icons/menu/attibutes.gif");
		$("#iconID").val("images/icons/menu/attibutes.gif");
		$("#menuIcon").attr("alt", "图片为空");
		loadAllMenuIcons();
	}
	function parentIdChange(value,text){
		$("#parentIdID").val(value);
	}
	
	/*初始化父菜单combox*/
	function initCombox(){
		combox = $("#parentTextID").ligerComboBox({
			width : 180, 
            selectBoxWidth: 200,
            selectBoxHeight: 200, textField:'text',valueField: 'id', treeLeafOnly: false,
            tree: { url: "${pageContext.request.contextPath}/menu/findDiretoryTree.do?selfId=0",ajaxType:'get',single:true},
            onSelected:parentIdChange,
        }); 
	}
	
	/*获得/images/icons/menu目录下所有的图标资源*/
	function loadAllMenuIcons(){
		$.post("${pageContext.request.contextPath}/menu/getIconNames.do",
				function(json) {
					if(json.error){
						alert(json.error);
						return;
					}
					if(json){
						var icons = json.data;
						var content = [];
						var dialog;
						for ( var i = 0; i < icons.length; i++) {
							content.push("<img src='" + rootPath + "/" + icons[i] + "' />");
						}
						var imgs = $(content.join(""));
						$("#iconBtn").click(function() {
							dialog = $.ligerDialog.open({
								width : 200,
								height : 300,
								title : "图片资源",
								content : imgs
							});
						});

						imgs.click(function() {
							$("#menuIcon").attr("src", $(this).attr("src"));
							$("#iconID").val(new String($(this).attr("src")).substring(rootPath.length+1));
							dialog.hide();
						});
					}
			});
	}
	/*根据表单大小设置对话框大小*/
	function autoResize(){
		var _form = $("#form_table");
		var width = 550;
		var height = _form.height() + 100;
		if(_form.hasClass('form4column')){
			width = 760;
		}else if(_form.hasClass('form2column')){
			width = 550;
		}
		if(height<280)height = 280;
		try {
			$(parent.document).find("div.l-dialog-body:last").height(height).width(width);
			$(parent.document).find('div.l-dialog-content:last').height(height-5);
		} catch (e) {}
	}
	/*保存菜单*/
	function saveDataAction(){
		console.info($("#menu_form").serialize());
		$.post("${pageContext.request.contextPath}/menu/saveMenu.do", 
				$("#menu_form").serialize(),
				function(json) {
					if(json.error){
						alert(json.error);
						return;
					}  
					if(json && json.result){
					 alert(json.result);
					 parent.gridManager.loadData();
					 parent._dialog.close();
					}
				});
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<form id="menu_form">
		<table id="form_table" border="0" cellpadding="0" cellspacing="0"
			class="form2column">
			<tr>
				<td class="label">父菜单名称:</td>
				<td>
					<input name="parentId" class="input-common" type="hidden" id="parentIdID" />
					<input name="parentText" class="input-common" type="text" id="parentTextID" disabled="disabled" />
				</td>
			</tr>
			<tr>
				<td class="label">菜单类型:</td>
				<td>
					<select style="width:155px;height:25px" name="menuTypeId" class="input-common" id="menuTypeidID" dataType="Require">
	  					<option value="1">目录</option>
	  					<option value="2">菜单</option>
	  				</select>
	  			</td>
			</tr>
			<tr>
				<td class="label">菜单名称:</td>
				<td><input name="text" class="input-common" type="text" id="textID" /></td>
			</tr>
			<tr>
				<td class="label">菜单标识:</td>
				<td><input name="identifier" class="input-common" type="text" id="identifierID" /></td>
			</tr>
			<tr>
				<td class="label">菜单描述:</td>
				<td><input name="description" class="input-common" type="text" id="descriptionID" /></td>
			</tr>
			<tr>
	  			<td class="label">菜单图片</td>
	  			<td>
	  			<img id="menuIcon" name="menuIcon" width="20" height="20" />
	  			<input id="iconBtn" type="button" value="浏览图片" />
	  			<input name="icon" type="hidden" id="iconID" />
	  			</td>
	  		</tr>
		</table>
		<div class="form_button">
			<input type="button" class="btn-normal" onclick="saveDataAction()" value="保存" /> 
			&nbsp;&nbsp; 
			<input type="reset" class="btn-normal" value="重置" />
		</div>
	</form>
</body>
</html>