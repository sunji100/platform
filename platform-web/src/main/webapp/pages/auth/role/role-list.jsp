<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	角色管理界面
	*/
	var _dialog;
	var menuGridData;
	var resourceGridData;
	var tab;
	$(function(){
		// 布局
		$("#main-content").ligerLayout({
			leftWidth : 500,
			height : '100%',
			heightDiff : -34,
			space : 4,
			allowLeftCollapse:false
		});
		
		var height = $(".l-layout-center").height();
		// Tab
		$("#center").ligerTab({
			height : height,
			onAfterSelectTabItem:onAfterSelectTabItem
		});
		
		tab = $("#center").ligerGetTabManager();
		
		PageLoader.initGridPanel();//获得所有角色
	});
	PageLoader = {
			//初始化角色grid
			initGridPanel:function(){
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
					} ],
		            url:'${pageContext.request.contextPath}/role/pageQueryRole.do',
		            pageSize:10 ,
		            rownumbers:false,
		            heightDiff : -10,
			        headerRowHeight: 30,//表头行的高度
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
						id : "user",
						text : '用户',
						click : itemclick,
						img : rootPath + "/images/icons/toolbar/page_edit.gif"
					}]},
					onSelectRow:roleGridSelect
		        });
				
				//初始化菜单grid
				menuGridManager = $("#menugrid").ligerGrid({
					width : '98%',
		            height:'95%',
		            checkbox:true,
		            columns: [ {
						display : "菜单名称",
						name : "text",
						width : 200,
						type : "text",
						align : "left",
						id : "name"
					}, {
						display : "菜单标识",
						name : "identifier",
						width : 250,
						type : "text",
						align : "left"
					},{
						display : "菜单图片",
						name : "resourceType",
						width : 100,
						align : "center",
						render : function(row) {
							return "<img width='20' height='20' src='" +rootPath + "/" + row.icon + "'/>";
						}
					},{
						display : "菜单描述",
						name : "description",
						width : 300,
						type : "text",
						align : "left"
					}],
					tree : {
						columnId : "name"
					},
					data:menuGridData,
					pageSize:100,
					pageSizeOptions:[10, 20, 30, 40, 50, 100],
		            heightDiff : -10,
			        headerRowHeight: 30,//表头行的高度
			        toolbar: { items: [
			                 	        { 
			                 	          	id:'add',
			                 	          	text: '分配权限',
			                 	          	click: menuItemClick, 
			                 	          	img : rootPath + "/images/icons/toolbar/add.png" 
			                 	        },{ 
			                 	        	line: true 
			                 	        },{ 
			                 	        	id:'delete',
			                 	        	text: '删除权限', 
			                 	        	click: menuItemClick, 
			                 	        	img : rootPath + "/images/icons/toolbar/page_delete.gif"
			                 	        }
			        ]}
		        });
				//初始化资源grid
				resourceGridManager = $("#resourcegrid").ligerGrid({
					width : '98%',
		            height:'95%',
		            checkbox:true,
		            columns: [ {
						display : "资源名称",
						name : "text",
						width : 100,
						type : "text",
						align : "left"
					}, {
						display : "资源标识",
						name : "identifier",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "资源描述",
						name : "description",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "资源类型",
						name : "resourceType",
						width : 100,
						type : "text",
						align : "left"
					} ],
		            data:resourceGridData,
		            pageSize:10 ,
		            rownumbers:false,
		            toolbar: { items: [
          	        { 
          	          	id:'add',
          	          	text: '分配权限',
          	          	click: resourceItemclick, 
          	          	img : rootPath + "/images/icons/toolbar/add.png" 
          	        },{ 
          	        	line: true 
          	        },{ 
          	        	id:'delete',
          	        	text: '删除权限', 
          	        	click: resourceItemclick, 
          	        	img : rootPath + "/images/icons/toolbar/page_delete.gif"
          	        }]}
		        });
				
			}
	};
	var selectedRole;
	
	/*选择roleGrid一行后*/
	function roleGridSelect(rowdata, rowid, rowobj){
		selectedRole = rowdata;
		if("menu" == tab.getSelectedTabItemID()){
			loadMenuGridData(rowdata.id);
		} else {
			loadResourceGridData(rowdata.id);
		}
	}
	
	function onAfterSelectTabItem(tabid){
		if("other" == tabid){
			if(selectedRole){
				loadResourceGridData(selectedRole.id);
			} else {
				//初始化other标签页的表头
				resourceGridManager.options.newPage = 1;
	       	 	resourceGridManager.loadData(resourceGridData);
			}
		} else {
			if(selectedRole){
				loadMenuGridData(selectedRole.id);
			}
		}
	}
	/*获得角色所拥有的菜单树*/
	function loadMenuGridData(roleId){
		$.ajax({
		      url: "${pageContext.request.contextPath}/menu/findMenuTreeByRole.do",
		      data: "roleId=" + roleId,
		      type: "POST",
		      success: function(json){
		         if(json){
		        	 menuGirdData = json;
		        	 menuGridManager.options.newPage = 1;
		        	 menuGridManager.loadData(menuGirdData);
		         }
		      }
		   }
		);
	}
	/*获得角色所拥有的资源*/
	function loadResourceGridData(roleId){
		$.ajax({
		      url: "${pageContext.request.contextPath}/resource/findResourceByRole.do",
		      data: "roleId=" + roleId,
		      type: "POST",
		      success: function(json){
		         if(json){
		        	 resourceGridData = json;
		        	 resourceGridManager.options.newPage = 1;
		        	 resourceGridManager.loadData(resourceGridData);
		         }
		      }
		   }
		);
	}
	
	function menuItemClick(item){
		if("add" == item.id){
			assignMenuToRoleAction();
		} else if("delete" == item.id){
			removeMenuForRoleAction();
		}
	}
	
	
	
	function itemclick(item){
		if("add" == item.id){
			openAddDialog();
		} else if("modify" == item.id){
			openModifyDialog();
		} else if("delete" == item.id){
			deleteDataAction();
		} else if("user" == item.id){
			if(selectRow()){
				top.f_addTab(selectedRow.name + "的用户管理","/pages/auth/user/userListByRole.jsp?roleId=" + selectedRow.id);
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
	/*新增角色对话框*/
	function openAddDialog(){
		var url = "role-add.jsp";
		_dialog = jQuery.ligerDialog.open({
	  	    title:'新增',
	  	    url:url,
	  	    isResize: true, width: 550, height: 550, isHidden: false
	     });
	}
	/*修改角色对话框*/
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
		var url = "role-update.jsp?id=" + id;
		_dialog = jQuery.ligerDialog.open({
	  	    title:'修改',
	  	    url:url,
	  	    isResize: true, width: 550, height: 550, isHidden: false
	     });
	}
	/*删除角色*/
	function deleteDataAction(){
		var newRow = gridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var removeData = "";
		$.each(gridManager.getSelectedRows(), function(index, element) {
			removeData = removeData + element.id + ","; 
		});
		removeData = removeData.substr(0, removeData.length-1);
		$.post("${pageContext.request.contextPath}/role/removeRole.do", 
				{'ids':removeData},
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
	//初始化菜单分配对话框
	function initMenuTree() {
		var manager = $("#menuTree").ligerGetTreeManager();
		if (manager != null) {
			manager.clear();
		}
		/*获得菜单树*/
		$.getJSON('${pageContext.request.contextPath}/menu/findMenuTree.do', function(menus) {
			var menuDialog = $.ligerDialog.open({
				width : 400,
				// 获取所有菜单数据
				height: 300,
				target : $("#treeDiv"),
				title : "为【" + selectedRole.name + "】分配菜单资源",
				content : $("#menuTree").ligerTree({
					data : menus.Rows,
					idFieldName : 'id'
				}),
				buttons : [{
					text : "确定",
					onclick : function() {
						var manager = $("#menuTree").ligerGetTreeManager();
						var nodes = manager.getChecked();
						var postData = [];
						for ( var i = 0; i < nodes.length; i++) {
							postData.push(nodes[i].data.id);
							//获取上一级菜单
							var parent = manager.getParent(nodes[i].data);
							while(parent != null){
								postData.push(parent.id);
								parent = manager.getParent(parent);
							}
						}
						postData = postData.join(",");
						$.ajax({
							type : "post",
							url : "${pageContext.request.contextPath}/role/assignMenuToRole.do",
							data : {'roleId':selectedRole.id,'menuIds':postData},
							success : function(json) {
								if(json.error){
									alert(json.error);
									return;
								}
								if(json && json.result){
									alert(json.result);
									loadMenuGridData(selectedRole.id);
									menuDialog.hide();
								}
							}
						});
					}
				}, {
					text : "取消",
					onclick : function() {
						menuDialog.hide();
					}
				} ]
			});
			var manager = $("#menuTree").ligerGetTreeManager();
			/*将角色已有菜单设为选中*/
			manager.selectNode(function(data,index){
				var flag = false;
				var listData = treeDataToListData(menuGirdData);
				$(listData).each(function(i,item){
					if(item.id == data.id){
						flag = true;
						return;
					}
				});
				return flag;
			});
		});
	}
	
	/*将tree结构数据转为list结构*/
	function treeDataToListData(treeData){
		var listData = [];
		$(treeData.Rows).each(function(i,item){
			listData.push(item);
			var tempListData = findAllChildItem(item);
			$(tempListData).each(function(i,row){
				listData.push(row);
			});
		});
		return listData;
	}
	
	/*迭代获取下一级每一项*/
	function findAllChildItem(item){
		var listData = []; 
		if(item.__hasChildren){
			$(item.children).each(function(i,row){
				listData.push(row);
				var tempListData = findAllChildItem(row);
				$(tempListData).each(function(i,row){
					listData.push(row);
				});
			})
		}
		return listData;
	}
	/*菜单分配对话框*/
	function assignMenuToRoleAction(){
		initMenuTree();
	}
	/*删除菜单分配*/
	function removeMenuForRoleAction(){
		var newRow = menuGridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var removeData="";
		$.each(menuGridManager.getSelectedRows(), function(index, element) {
			removeData = removeData + element.id + ",";
		});
		removeData = removeData.substr(0, removeData.length-1);
		$.post("${pageContext.request.contextPath}/role/removeMenuForRole.do", 
				{'roleId':selectedRole.id,'menuIds':removeData},
				function(json) {
					if(json.error){
						alert(json.error);
						return;
					}
					if(json && json.result){
						menuGridManager.removeRange(menuGridManager.getSelectedRows());
						alert(json.result);
					}
				});
	}
	
	function resourceItemclick(item){
		if("add" == item.id){
			if(selectedRole){
				openAssignResourceToRoleDialog();
			} else {
				alert("请选择角色");
			}
		} else if("delete" == item.id){
			removeResourceForRoleAction();
		}
	}
	/*资源分配对话框*/
	var assignResourceToRoleDialog;
	function openAssignResourceToRoleDialog(){
		assignResourceToRoleDialog = $.ligerDialog.open({
			title : "资源",
			isResize : true,
			width : 580,
			height : 520,
			isHidden : true,
			buttons : [ {
				id : 'assignResourceToRole',
				text : '保存',
				onclick : btnClick
			}, {
				id : 'cancelAssignResourceToRole',
				text : '取消',
				onclick : btnClick
			} ],
			target : $("#assignResourceToRoleDialog")
		});
		loadResources();
	}
	
	// 待选角色列表结构
	var resourceGrid;
	function loadResources() {
		resourceGrid = $("#assignResourceToRole").ligerGrid({
			columns: [ {
				display : "资源名称",
				name : "text",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "资源标识",
				name : "identifier",
				width : 100,
				type : "text",
				align : "left"
			},{
				display : "资源描述",
				name : "description",
				width : 200,
				type : "text",
				align : "left"
			},{
				display : "资源类型",
				name : "resourceType",
				width : 100,
				type : "text",
				align : "left"
			} ],
			pageSize : 10,
			url : '${pageContext.request.contextPath}/resource/findResourceNotAssignToRole.do',
			parms : {roleId:selectedRole.id},
			rownumbers : true,
			width : 550,
			height : 430,
			checkbox : true
		});
	}
	
	function btnClick(button) {
		if("assignResourceToRole" == button.id){
			assignResourceToRoleAction();
		} else if("cancelAssignResourceToRole" == button.id){
			assignResourceToRoleDialog.hidden();
		}
	}
	/*分配资源到角色*/
	function assignResourceToRoleAction(){
		var newRow = resourceGrid.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var resourceIds = [];
		$.each(resourceGrid.getSelectedRows(), function(index, element) {
			resourceIds.push(element.id);
		});
		resourceIds = resourceIds.join(",");
		var url,data;
		url = "${pageContext.request.contextPath}/role/assignResourceToRole.do";
		data = {roleId:selectedRole.id,resourceIds:resourceIds};
		$.ajax({
		      url: url,
		      data: data,
		      type: "POST",
		      success: function(json){
		         if(json){
		        	 loadResourceGridData(selectedRole.id);
		        	 assignResourceToRoleDialog.hidden();
		         }
		      }
		   }
		);
	}
	/*删除分配的资源*/
	function removeResourceForRoleAction(){
		var newRow = resourceGridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var resourceIds = [];
		$.each(resourceGridManager.getSelectedRows(), function(index, element) {
			resourceIds.push(element.id);
		});
		resourceIds = resourceIds.join(",");
		var url,data;
		url = "${pageContext.request.contextPath}/role/removeResourceForRole.do";
		data = {roleId:selectedRole.id,resourceIds:resourceIds};
		$.ajax({
		      url: url,
		      data: data,
		      type: "POST",
		      success: function(json){
		         if(json){
		        	 loadResourceGridData(selectedRole.id);
		         }
		      }
		   }
		);
	}
</script>
<title>Insert title here</title>
</head>
<body>
<div id="main-content" style="width:99.2%; margin:0 auto; margin-top:4px; "> 
	<div position="left" id="left" title="角色管理">
		<div id="maingrid"></div>
	</div>
	<div position="center" id="center"> 
		<div tabid="menu" title="菜单管理" style="height:300px" >
        	<div id="menugrid"></div> 
        </div> 
		<div tabid="other" title="资源管理" style="height:300px" >
        	<div id="resourcegrid"></div>
        </div> 
	</div> 
</div>

<div id="treeDiv">
  	<ul id="menuTree">
  	</ul>
 </div>
<div id="assignResourceToRoleDialog" style="display: none;">
	  <div id="assignResourceToRole">
	  </div>
</div>
</body>
</html>