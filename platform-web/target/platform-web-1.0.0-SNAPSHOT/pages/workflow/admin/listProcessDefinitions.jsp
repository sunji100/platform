<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	var _dialog;
	var gridData;
	$(function(){
		PageLoader.initGridPanel();
		loadGridData();
	});
	PageLoader = {
			initGridPanel:function(){
				gridManager = $("#maingrid").ligerGrid({
					width : '98%',
		            height:'100%',
		            checkbox:true,
		            columns: [ {
						display : "编码",
						name : "id",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "代码",
						name : "key",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "名称",
						name : "name",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "分类",
						name : "category",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "版本",
						name : "version",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "描述",
						name : "description",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "状态",
						width : 200,
						align : "left",
						render: function (rowdata, rowindex, value)
		                {
							var html;
							if(rowdata.suspended){
								html = '挂起';
								html += '<a href="javascript:activeProcessDefinition(\''+ rowdata.id +'\')">(激活)</a>';
							}
							if(!rowdata.suspended){
								html = '激活';
								html += '<a href="javascript:suspendProcessDefinition(\''+ rowdata.id +'\')">(挂起)</a>';
							}
		                    return html;
		                }
					},{
						width : 200,
						align : "left",
						render: function (rowdata, rowindex, value)
						{
							var html;
							html = '<a href="${pageContext.request.contextPath}/bpmAdmin/graphProcessDefinition.do?processDefinitionId='+ rowdata.id +'" target="_blank">查看流程图</a>';
							return html;
						}
					}],
					data:gridData,
		            pageSize:10,
		            rownumbers:true,
		            toolbar: { items: [{
      							id : "userTask",
      							text : '流程任务分配',
      							click : itemclick,
      							img : rootPath + "/images/icons/toolbar/page_edit.gif"
      						}]}
		        });
			}
	};
	
	/*加载grid数据*/
	function loadGridData(){
		var url = "${pageContext.request.contextPath}/bpmAdmin/listProcessDefinitions.do";
		$.ajax({
		      url: url,
		      type: "POST",
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 gridData = json;
		        	 //console.info(JSON.stringify(gridData));
		        	 gridManager.options.newPage = 1;
					 gridManager.loadData(gridData);
		         }
		      }
		   }
		);
	}
	
	/*暂停流程定义*/
	function suspendProcessDefinition(processDefinitionId){
		var url = "${pageContext.request.contextPath}/bpmAdmin/suspendProcessDefinition.do";
		var data = {processDefinitionId:processDefinitionId};
		$.ajax({
		      url: url,
		      type: "POST",
		      data: data,
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 loadGridData();
		         }
		      }
		   }
		);
	}
	
	function activeProcessDefinition(processDefinitionId){
		console.info(processDefinitionId);
		var url = "${pageContext.request.contextPath}/bpmAdmin/activeProcessDefinition.do";
		var data = {processDefinitionId:processDefinitionId};
		$.ajax({
		      url: url,
		      type: "POST",
		      data: data,
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 loadGridData();
		         }
		      }
		   }
		);
	}
	
	function itemclick(item){
		if("userTask" == item.id){
			if(selectRow()){
				top.f_addTab(selectedRow.name + "的任务管理","/pages/workflow/admin/assignUserTask.jsp?processDefinitionId=" + selectedRow.id);
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
	
</script>
<title>Insert title here</title>
</head>
<body>
<div id="maingrid"></div>
</body>
</html>