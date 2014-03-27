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
						display : "资源类型名称",
						name : "name",
						width : 200,
						type : "text",
						align : "left"
					}],
		            url:'${pageContext.request.contextPath}/resourceType/pageQueryResourceType.do',
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
		var url = "resourcetype-add.jsp";
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
	     
		var url = "resourcetype-update.jsp?id=" + id;
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
		$.post("${pageContext.request.contextPath}/resourceType/removeResourceType.do", 
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
</script>
<title>Insert title here</title>
</head>
<body>
<ss3:authorize url="selectButton">
selectButton动作权限
</ss3:authorize>
<ss3:authorize url="insertButton">
insertButton动作权限
</ss3:authorize>
<div id="maingrid"></div>
</body>
</html>