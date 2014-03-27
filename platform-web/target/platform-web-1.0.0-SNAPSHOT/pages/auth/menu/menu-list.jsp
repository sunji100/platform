<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	var _dialog;
	$(function(){
		PageLoader.initGridPanel();
	});
	PageLoader = {
			initGridPanel:function(){
				gridManager = $("#maingrid").ligerGrid({
					width : '98%',
		            height:'100%',
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
							return "<img width='20' height='20' src='${pageContext.request.contextPath}/" + row.icon + "'/>";
						}
					},{
						display : "菜单描述",
						name : "description",
						width : 300,
						type : "text",
						align : "left"
					}],
		            url:'${pageContext.request.contextPath}/menu/findMenuTree.do',
		            pageSize:100 ,
		            pageSizeOptions:[10, 20, 30, 40, 50, 100],
		            rownumbers:false,
		            heightDiff : -10,
			        headerRowHeight: 30,//表头行的高度
		            alternatingRow: false,
		            autoCheckChildren : false,
		            tree : {
						columnId : "name"
					},
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
        				line : true
        			}, {
        				id : "up",
        				text : "上移",
        				img : rootPath + "/images/icons/16x16/up.gif",
        				click : up
        			}, {
        				line : true
        			}, {
        				id : "down",
        				text : "下移",
        				img : rootPath + "/images/icons/16x16/down.gif",
        				click : down
        			}]}
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
		} 
	}
	
	
	function openAddDialog(){
		var newRow = gridManager.getSelected();
		var url;
		if (newRow){
			var i = 0;
		     $.each(gridManager.getSelectedRows(), function(index, element) {
				  i++;
			  });
		     if(i>1){
		    	 alert('请只选择一行'); return;
		     }
			var parentId = newRow.id;
			var parentText = newRow.text;
			url = "menu-add.jsp?parentId=" + parentId + "&parentText=" + parentText;
		} else {
			url = "menu-add.jsp";
		}
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
		var url = "menu-update.jsp?id=" + id;
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
		$.post("${pageContext.request.contextPath}/menu/removeMenu.do", 
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
	
	// 上移节点
	function up() {
		if (gridManager.getSelectedRows().length > 1) {
			$.ligerDialog.alert("只能选中一行");
			return;
		}
		gridManager.up(gridManager.getSelectedRow());
		updateSortAction();
	}

	// 下移节点
	function down() {
		if (gridManager.getSelectedRows().length > 1) {
			$.ligerDialog.alert("只能选中一行");
			return;
		}
		gridManager.down(gridManager.getSelectedRow());
		updateSortAction();
	}
	
	function menu(sortOrder,id){
		this.sortOrder = sortOrder;
		this.id = id;
	}
	
	function updateSortAction(){
		var data = [];
		for ( var i = 0; i < gridManager.rows.length; i++) {
			var rowNo = i + 1;
			data.push(new menu(rowNo,gridManager.rows[i].id));
		}
		 $.ajax({ 
	            type:"POST", 
	            url:"${pageContext.request.contextPath}/menu/updateMenuSortOrder.do", 
	            dataType:"json",      
	            contentType:"application/json",               
	            data:JSON.stringify(data), 
	            success:function(data){ 
	                                       
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