<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	
	$(function(){
		autoResize();
		initAddForm();
	});
	function initAddForm(){
		var _parentId = "${param.parentId}";
		$("#parentIds").val(_parentId);
		loadAllResourceType();
	}
	function loadAllResourceType(){
		$.post("${pageContext.request.contextPath}/resource/findAllResourceType.do",
				function(json) {
					if(json.error){
						alert(json.error);
						return;
					}
					if(json){
					 	$(json.data).each(function(idx,elm){
					 		$("#resourceType").append("<option value='"+ elm.id +"'>"+ elm.name +"</option>");
					 	});
					}
			});
	}
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
	
	function saveDataAction(){
		$.post("${pageContext.request.contextPath}/resource/saveResource.do", 
				$("#resource_form").serialize(),
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
	<form id="resource_form">
		<table id="form_table" border="0" cellpadding="0" cellspacing="0"
			class="form2column">
			<tr>
				<td class="label">父资源名称:</td>
				<td><input name="parentIds" class="input-common" type="text" id="parentIds" disabled="disabled" /></td>
			</tr>
			<tr>
				<td class="label">资源名称:</td>
				<td><input name="text" class="input-common" type="text" id="textID" /></td>
			</tr>
			<tr>
				<td class="label">资源标识:</td>
				<td><input name="identifier" class="input-common" type="text" id="identifierID" /></td>
			</tr>
			<tr>
				<td class="label">资源描述:</td>
				<td><input name="description" class="input-common" type="text" id="descriptionID" /></td>
			</tr>
			<tr>
				<td class="label">资源类型:</td>
				<td>
					<select style="width:155px;height:25px" name="resourceTypeId" class="input-common" id="resourceType" dataType="Require">
	  					<option value="0">--请选择--</option>
	  				</select>
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