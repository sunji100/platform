<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	指定角色的用户管理界面
	*/
	var roleId = "${param.roleId}";//角色ID
	var _dialog;
	$(function(){
		PageLoader.initGridPanel();//获得角色已分配用户
	});
	PageLoader = {
			initGridPanel:function(){
				gridManager = $("#maingrid").ligerGrid({
					width : '98%',
		            height:'100%',
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
						width : 200,
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
					}, {
						display : "最后登录时间",
						name : "lastLoginTime",
						width : 100,
						type : "text",
						align : "left"
					}, {
						display : "最后更改时间",
						name : "lastModifyTime",
						width : 100,
						type : "text",
						align : "left"
					} ],
		            url:'${pageContext.request.contextPath}/identity/pageQueryIdentityByRoleId.do?roleId=' + roleId,
		            pageSize:3 ,
		            rownumbers:false,
		            toolbar: { items: [
          	        { 
          	          	id:'add',
          	          	text: '分配用户',
          	          	click: itemclick, 
          	          	img : rootPath + "/images/icons/toolbar/add.png" 
          	        },{ 
          	        	line: true 
          	        },{ 
          	        	id:'delete',
          	        	text: '删除', 
          	        	click: itemclick, 
          	        	img : rootPath + "/images/icons/toolbar/page_delete.gif"
          	        }]}
		        });
			}
	};
	
	function itemclick(item){
		if("add" == item.id){
			openAssignUserToRoleDialog();
		}  else if("delete" == item.id){
			deleteDataAction();
		}
	}
	/*打开分配用户对话框*/
	function openAssignUserToRoleDialog(){
		var url = "${pageContext.request.contextPath}/pages/auth/user/assignUserToRole.jsp?roleId=" + roleId;
		_dialog = jQuery.ligerDialog.open({
	  	    title:'分配用户',
	  	    url:url,
	  	    isResize: true, width: 470, height: 500, isHidden: false
	     });
	}
	/*删除分配的用户*/
	function deleteDataAction(){
		var newRow = gridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var removeData = "";
		$.each(gridManager.getSelectedRows(), function(index, element) {
			removeData = removeData + element.id + ","; 
		});
		removeData = removeData.substr(0, removeData.length-1);
		$.post("${pageContext.request.contextPath}/identity/removeUserForRole.do", 
				{identityIds:removeData,roleId:roleId},
				function(json) {
					if(json.error){
						alert(json.error);
						return;
					}
					if(json && json.result){
					 alert(json.result);
					 gridManager.loadData();
					}
				});
	}
</script>
<title>Insert title here</title>
</head>
<body>
<div id="maingrid"></div>
</body>
</html>