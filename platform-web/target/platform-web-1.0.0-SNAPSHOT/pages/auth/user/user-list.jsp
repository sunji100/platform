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
				$.getJSON("${pageContext.request.contextPath}/org/findOrgTree.do",function(menu){
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
					var selectedTreeId = selectedNode.id;
					
					
					gridManager = $("#maingrid").ligerGrid({
						width : '98%',
			            height:'92%',
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
			            url:'${pageContext.request.contextPath}/identity/pageQueryIdentityByOrgId.do',
			            parms:[{name:'orgId',value:selectedTreeId}],
			            pageSize:3 ,
			            rownumbers:false,
			            toolbar: { items: [
	          	        { 
	          	          	id:'add',
	          	          	text: '增加',
	          	          	click: itemclick, 
	          	          	img : rootPath + "/images/icons/toolbar/add.png" 
	          	        },{ 
	          	        	line: true 
	          	        },{ 
	          	        	id:'modify',
	          	        	text: '修改', 
	          	        	click: itemclick, 
	          	        	img : rootPath + "/images/icons/toolbar/page_edit.gif" 
	          	        },{ 
	          	        	line: true 
	          	        },{ 
	          	        	id:'delete',
	          	        	text: '删除', 
	          	        	click: itemclick, 
	          	        	img : rootPath + "/images/icons/toolbar/page_delete.gif"
	          	        },{ 
	          	        	line: true 
	          	        },{
							id : "role",
							text : '角色',
							click : itemclick,
							img : rootPath + "/images/icons/toolbar/page_edit.gif"
						}]}
			        });
					
					
					tree.bind("select", function(node) {
						selectedNode = node.data;
						gridManager.options.page = 1;
						gridManager.setParm('orgId',node.data.id);
						gridManager.loadData();
	             	});
				});
			}
	};
	
	function itemclick(item){
		if("add" == item.id){
			openAddDialog();
		} else if("modify" == item.id){
			openModifyDialog();
		} else if("delete" == item.id){
			deleteDataAction();
		} else if("role" == item.id){
			if(selectRow()){
				top.f_addTab(selectedRow.name + "的角色管理","/pages/auth/role/roleListByUser.jsp?userId=" + selectedRow.id);
			}
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
	
	function openAddDialog(){
		var url = "user-add.jsp?orgId=" + selectedNode.id + "&orgName=" + selectedNode.text;
		_dialog = jQuery.ligerDialog.open({
	  	    title:'新增',
	  	    url:url,
	  	    isResize: true, width: 550, height: 550, isHidden: false
	     });
	}
	
	function openModifyDialog(){
		var newRow = gridManager.getSelected();
	     if (!newRow) { alert('请选择行'); return; }
	     var i = 0;
	     $.each(gridManager.getSelectedRows(), function(index, element) {
			  i++;
		  });
	     if(i>1){
	    	 alert('请只选择一行'); return;
	     }
	     id = newRow.id;
	     
		var url = "user-update.jsp?id=" + id + "&orgId=" + selectedNode.id;
		_dialog = jQuery.ligerDialog.open({
	  	    title:'修改',
	  	    url:url,
	  	    isResize: true, width: 550, height: 550, isHidden: false
	     });
	}
	
	function deleteDataAction(){
		var newRow = gridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var removeData = "";
		$.each(gridManager.getSelectedRows(), function(index, element) {
			removeData = removeData + element.id + ","; 
		});
		removeData = removeData.substr(0, removeData.length-1);
		$.post("${pageContext.request.contextPath}/identity/removeIdentity.do", 
				{'ids':removeData},
				function(json) {
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
<div id="main-content" style="width:99.2%; margin:0 auto; margin-top:4px; "> 
    <div position="left" id="leftmenu" title="组织结构">
    	<ul id="orgMenu"></ul>
    </div>
    <div position="center" id="framecenter" title="用户管理"> 
        <div id="maingrid"></div>
    </div> 
    
</div>
</body>
</html>