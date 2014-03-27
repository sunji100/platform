<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	var _dialog;
	var selectedNode;//tree当前节点
	var gridData;
	$(function(){
		// 布局
		$("#main-content").ligerLayout({
			leftWidth : 220,
			height : '100%',
			heightDiff : -34,
			space : 4,
			allowLeftCollapse:false
		});
		PageLoader.initOrgMenuTree();
	});
	PageLoader = {
			initOrgMenuTree:function(){
				$.getJSON("${pageContext.request.contextPath}/org/findOrgAndIdentityTree.do",function(menu){
					var tree = $("#orgMenu").ligerTree({
	             		data:menu.Rows,
	             		checkbox:false
	             	});
					
					tree.selectNode(function(treenodedata, treedataindex){
						if(treenodedata.level == 1){
							return true;
						}
						return false;
					});
					
					selectedNode = tree.getSelected().data;
					
					
					gridManager = $("#maingrid").ligerGrid({
						width : '98%',
			            height:'95%',
			            columns: [ {
							display : "角色名称",
							name : "name",
							width : 100,
							type : "text",
							align : "left"
						}, {
							display : "角色描述",
							name : "roleDesc",
							width : 100,
							type : "text",
							align : "left"
						},{
							display : "是否有效",
							name : "valid",
							width : 50,
							type : "text",
							align : "left",
							render : function(rowdata) {
								return rowdata.valid == true ? "是" : "否";
							}
						},{
							display : "授权组织",
							name : "orgName",
							width : 100,
							type : "text",
							align : "left"
						} ],
			            data:gridData,
			            pageSize:10 ,
			            rownumbers:true,
			            checkbox:true,
			            heightDiff : -10,
				        headerRowHeight: 30,//表头行的高度
				        onBeforeCheckRow:function(checked, data, rowid, rowdata){
				        	if(data.orgId != selectedNode.id){//继承的角色不允许删除
				        		return false;
				        	}
				        	return true;
				        },
			            toolbar: { items: [
	          	        { 
	          	          	id:'add',
	          	          	text: '分配',
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
					
					loadGridData(selectedNode);
					
					tree.bind("select", function(node) {
						selectedNode = node.data;
						loadGridData(selectedNode);
	             	});
				});
			}
	};
	
	function loadGridData(node){
		var url;
		var data;
		if(node.type == "user"){
			url = "${pageContext.request.contextPath}/role/findRoleByOrgIdAndIdentityId.do";
			data = {orgId:node.parentId,identityId:node.id};
		} else {
			url = "${pageContext.request.contextPath}/role/findParentRoleByOrgId.do";
			data = {orgId:node.id};
		}
		$.ajax({
		      url: url,
		      data: data,
		      type: "POST",
		      async:false,
		      success: function(json){
		         if(json){
		        	 gridData = json;
		        	 gridManager.options.newPage = 1;
					 gridManager.loadData(gridData);
		         }
		      }
		   }
		);
	}
	
	function itemclick(item){
		if("add" == item.id){
			openAssignRolesToOrgDialog();
		} else if("delete" == item.id){
			removeRolesForOrgAction();
		} 
	}
	
	var selectedRow;
	function selectRow(){
		 selectedRow = gridManager.getSelected();
	     if (!selectedRow) { alert('请选择行'); return false; }
	     var i = 0;
	     $.each(gridManager.getSelectedRows(), function(index, element) {
			  i++;
		 });
	     if(i>1){
	    	 alert('请只选择一行'); return false;
	     }
	     return true;
	}
	
	var assignRolesToUserDialog;
	function openAssignRolesToOrgDialog(){
		assignRolesToUserDialog = $.ligerDialog.open({
			title : "角色",
			isResize : true,
			width : 480,
			height : 520,
			isHidden : true,
			buttons : [ {
				id : 'assignRolesToUser',
				text : '保存',
				onclick : btnClick
			}, {
				id : 'cancelAssignRolesToUser',
				text : '取消',
				onclick : btnClick
			} ],
			target : $("#assignRolesToOrgDialog")
		});
		loadRoles();
	}
	
	// 待选角色列表结构
	var roleGrid;
	function loadRoles() {
		var data;
		if("user" == selectedNode.type){
			data = {identityId:selectedNode.id};
		} else {
			data = {orgId:selectedNode.id};
		}
		roleGrid = $("#assignRolesToOrg").ligerGrid({
			columns : [ {
				display : "角色名称",
				name : "name",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "角色描述",
				name : "roleDesc",
				width : 200,
				type : "text",
				align : "left"
			} ],
			pageSize : 10,
			url : '${pageContext.request.contextPath}/role/findRoleByNoAssignToIdentityIdOrOrgId.do',
			parms : data,
			sortName : 'id',
			rownumbers : true,
			width : 450,
			height : 430,
			checkbox : true
		});
	}
	
	function btnClick(button) {
		if("assignRolesToUser" == button.id){
			assignRolesToUserAction();
		} else if("cancelAssignRolesToUser" == button.id){
			assignRolesToUserDialog.hidden();
		}
	}
	
	function assignRolesToUserAction(){
		var newRow = roleGrid.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var roleIds = [];
		$.each(roleGrid.getSelectedRows(), function(index, element) {
			roleIds.push(element.id);
		});
		roleIds = roleIds.join(",");
		var url,data;
		if("user" == selectedNode.type){
			url = "${pageContext.request.contextPath}/role/assignRoleToUser.do";
			data = {userId:selectedNode.id,roleIds:roleIds};
		} else {
			url = "${pageContext.request.contextPath}/org/assignRoleToOrg.do";
			data = {orgId:selectedNode.id,roleIds:roleIds};
		}
		$.ajax({
		      url: url,
		      data: data,
		      type: "POST",
		      success: function(json){
		         if(json){
		        	 loadGridData(selectedNode);
		        	 assignRolesToUserDialog.hidden();
		         }
		      }
		   }
		);
	}
	
	function removeRolesForOrgAction(){
		var newRow = gridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var removeData = "";
		$.each(gridManager.getSelectedRows(), function(index, element) {
			removeData = removeData + element.id + ","; 
		});
		removeData = removeData.substr(0, removeData.length-1);
		var url,data;
		if("user" == selectedNode.type){
			url = "${pageContext.request.contextPath}/role/removeRoleForUser.do";
			data = {userId:selectedNode.id,roleIds:removeData};
		} else {
			url = "${pageContext.request.contextPath}/org/removeRoleForOrg.do";
			data = {orgId:selectedNode.id,roleIds:removeData};
		}
		$.post(url, data,
				function(json) {
					if(json && json.result){
					 alert(json.result);
					 loadGridData(selectedNode);
					}
				});
		
	}
</script>
<title>Insert title here</title>
</head>
<body>
<div id="main-content" style="width:99.2%; margin:0 auto; margin-top:4px; "> 
    <div position="left" id="leftmenu" title="组织结构">
    	<ul id="orgMenu"></ul>
    </div>
    <div position="center" id="framecenter" title="角色管理"> 
        <div id="maingrid"></div>
    </div> 
    
</div>

<div id="assignRolesToOrgDialog" style="display: none;">
	  <div id="assignRolesToOrg">
	  </div>
</div>
</body>
</html>