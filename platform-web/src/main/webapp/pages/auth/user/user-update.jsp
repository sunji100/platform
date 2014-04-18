<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	修改用户界面
	*/
	var id = "${param.id}";//用户ID
	var orgId = "${param.orgId}";//组织ID
	$(function(){
		$("#id_orgId").val(orgId);
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
	/*获得用户信息*/
	function loadData(){
		if(""==id) return;
		/*$.getJSON("${pageContext.request.contextPath}/identity/findIdentityById/"+ id +".do",
				function(json){
				if(json && json.data){
					 json = json.data;
					 console.info(JSON.stringify(json));
					 var elm;
					 for(var index in json){
						 elm = document.getElementById(index + "ID");
						 if(elm){
							 if("checkbox" == $(elm).attr("type")){
								 console.info(json[index]);
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
		});*/
		$.ajax({
			url:"${pageContext.request.contextPath}/identity/findIdentityById/"+ id +".do",
			dataType:"json",
			cache:false,
			success:function(json){
				console.info(json);
				if(json && json.data){
					 json = json.data;
					 console.info(JSON.stringify(json));
					 var elm;
					 for(var index in json){
						 elm = document.getElementById(index + "ID");
						 if(elm){
							 if("checkbox" == $(elm).attr("type")){
								 console.info(json[index]);
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
	/*保存用户修改*/
	function saveDataAction(){
		$.post("${pageContext.request.contextPath}/identity/updateIdentity.do?id=" + id, 
			$("#user_form").serialize(),
			function(json) {
				if(json && json.result){
				 alert(json.result);
				 parent.gridManager.setParm('orgId',orgId);
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
				<td class="label">组织名称:</td>
				<td>
					<input name="orgId" class="input-common" type="hidden" id="id_orgId" />
					<input name="orgName" class="input-common" type="text" id="orgNameID" disabled="disabled" />
				</td>
			</tr>
			<tr>
				<td class="label">用户名称:</td>
				<td><input name="name" class="input-common" type="text" id="nameID" /></td>
			</tr>
			<tr>
				<td class="label">用户账户:</td>
				<td><input name="userAccount" class="input-common" type="text" id="userAccountID" disabled="disabled" /></td>
			</tr>
			<tr>
				<td class="label">用户描述:</td>
				<td><input name="userDesc" class="input-common" type="text" id="userDescID" /></td>
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