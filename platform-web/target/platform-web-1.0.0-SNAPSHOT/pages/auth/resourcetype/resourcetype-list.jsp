<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<%@ include file="/pages/common/excel.jsp"%>
<script type="text/javascript">
	/**
	资源类型管理界面
	*/
	var _dialog;
	$(function(){
		PageLoader.initGridPanel();//显示所有资源Grid
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
						align : "left",
						columns:[{
							display : "资源类型名称",
							name : "name",
							width : 200,
							type : "text",
							align : "left",
							totalSummary:
		                    {
		                        render: function (suminf, column, cell)
		                        {
		                            return '<div>最大值:' + suminf.max + '</div>';
		                        },
		                        align: 'left'
		                    }
						},{
							display : "aaaa",
							name : "name",
							width : 200,
							type : "text",
							align : "left",
							totalSummary:
		                    {
		                        render: function (suminf, column, cell)
		                        {
		                            return '<div>最大值:' + suminf.max + '</div>';
		                        },
		                        align: 'left'
		                    }
						}]
						
					}],
		            url:'${pageContext.request.contextPath}/resourceType/pageQueryResourceType.do',
		            pageSize:3 ,
		            rownumbers:true,
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
          	        	id:'excel',
          	        	text: 'excel', 
          	        	click: itemclick, 
          	        	img : rootPath + "/images/icons/toolbar/page_delete.gif"
          	        },{ 
          	        	line: true 
          	        },{ 
          	        	id:'excelAll',
          	        	text: 'excel all', 
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
		} else if("excel" == item.id){
			exportExcel("或32基本面.xls",gridManager);
		} else if("excelAll" == item.id){
			exportExcel("或32基本面.xls",gridManager,true);
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
	/*打开资源类型增加对话框*/
	function openAddDialog(){
		var url = "resourcetype-add.jsp";
		_dialog = jQuery.ligerDialog.open({
	  	    title:'新增',
	  	    url:url,
	  	    isResize: true, width: 550, height: 550, isHidden: false
	     });
	}
	/*打开资源类型修改对话框*/
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
	/*删除选中的资源类型*/
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
<iframe name="excelFrame" style="display:none;"></iframe>
<!-- 动作权限demo -->
<ss3:authorize url="selectButton">
selectButton动作权限
</ss3:authorize>
<ss3:authorize url="insertButton">
insertButton动作权限
</ss3:authorize>
<div id="maingrid"></div>
</body>
</html>