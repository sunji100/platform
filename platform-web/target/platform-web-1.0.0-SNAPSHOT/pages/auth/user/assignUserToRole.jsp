<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	var roleId = "${param.roleId}";
	$(function(){
		PageLoader.initGridPanel();
	});
	PageLoader = {
			initGridPanel:function(){
				gridManager = $("#maingrid").ligerGrid({
					width : 450,
		            height: 350,
		            checkbox:true,
		            columns: [ {
						display : "用户名称",
						name : "name",
						width : 100,
						type : "text",
						align : "left"
					}, {
						display : "用户帐号",
						name : "userAccount",
						width : 100,
						type : "text",
						align : "left"
					}, {
						display : "用户描述",
						name : "userDesc",
						width : 100,
						type : "text",
						align : "left"
					}, {
						display : "是否有效",
						name : "valid",
						width : 50,
						type : "text",
						align : "left",
						render : function(rowdata) {
							return rowdata.valid == true ? "是" : "否";
						}
					}],
		            url:'${pageContext.request.contextPath}/identity/findNotAssignUserByRole.do?roleId=' + roleId,
		            pageSize:3 ,
		            rownumbers:false
		        });
			}
	};
	
	
	
	function saveDataAction(){
		var newRow = gridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var userids = "";
		$.each(gridManager.getSelectedRows(), function(index, element) {
			userids = userids + element.id + ","; 
		});
		userids = userids.substr(0, userids.length-1);
		
		$.post("${pageContext.request.contextPath}/identity/assignUserToRole.do", 
			{roleId:roleId,identityIds:userids},
			function(json) {
				if(json && json.result){
					if(json.error){
						alert(json.error);
						return;
					}
					alert(json.result);
					parent.gridManager.loadData();
					parent._dialog.close();
				}
		});
	}
	
	function closeDialog(){
		parent._dialog.close();
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<div id="maingrid"></div>
	<div class="form_button">
		<input type="button" class="btn-normal" onclick="saveDataAction()" value="保存" /> 
		&nbsp;&nbsp; 
		<input type="button" class="btn-normal" onclick="closeDialog()" value="关闭" /> 
	</div>
</body>
</html>