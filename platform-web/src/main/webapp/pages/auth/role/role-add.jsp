<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	新增角色对话框
	*/
	$(function(){
		autoResize();
	});
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
	/*增加角色*/
	function saveDataAction(){
		$.post("${pageContext.request.contextPath}/role/saveRole.do", 
				$("#user_form").serialize(),
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
	<form id="user_form">
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
		<div class="form_button">
			<input type="button" class="btn-normal" onclick="saveDataAction()" value="保存" /> 
			&nbsp;&nbsp; 
			<input type="reset" class="btn-normal" value="重置" />
		</div>
	</form>
</body>
</html>