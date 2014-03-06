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
		$("#product_dateID").ligerDateEditor();//加载日期选择器
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
	
	function saveDataAction(){
		$.post("${pageContext.request.contextPath}/product/add.do", 
				$("#product_form").serialize(),
				function(json) {
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
	<form id="product_form">
		<table id="form_table" border="0" cellpadding="0" cellspacing="0"
			class="form2column">
			<tr>
				<td class="label">product_name:</td>
				<td><input name="product_name" class="input-common" type="text"
					id="product_nameID" /></td>
			</tr>
			<tr>
				<td class="label">product_date:</td>
				<td><input name="product_date" class="input-common" type="text"
					id="product_dateID" /></td>
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