<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	角色修改对话框
	*/
	var id = "${param.id}";//角色ID
	$(function(){
		autoResize();
		loadData();
		$.ajaxSetup ({
		      cache: false //关闭AJAX相应的缓存
		}); 
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
	
	function loadData(){
		if(""==id) return;
		$.ajax({
			url:"${pageContext.request.contextPath}/role/findRoleById/"+ id +".do",
			dataType:"json",
			cache:false,
			success:function(json){
				console.info(JSON.stringify(json));
				if(json && json.data){
					if(json.error){
						alert(json.error);
						return;
					} 
					 json = json.data;
					 var elm;
					 for(var index in json){
						 elm = document.getElementById(index + "ID");
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
	/*修改角色*/
	function saveDataAction(){
		$.post("${pageContext.request.contextPath}/role/updateRole.do?id=" + id, 
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
		<div class="form_button">
			<input type="button" class="btn-normal" onclick="saveDataAction()" value="保存" /> 
			&nbsp;&nbsp; 
			<input type="reset" class="btn-normal" value="重置" />
		</div>
	</form>
</body>
</html>