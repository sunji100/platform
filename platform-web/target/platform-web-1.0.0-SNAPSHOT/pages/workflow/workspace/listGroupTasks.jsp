<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	待领任务界面
	*/
	var _dialog;
	var gridData;
	var _selfTabid = "${param.tabid}";
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
						display : "任务名称",
						name : "name",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "任务创建时间",
						name : "createTime",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "流程名称",
						name : "procName",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "流程标题",
						name : "procTitle",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "流程发起人",
						name : "started",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "操作",
						width : 200,
						align : "left",
						render: function (rowdata, rowindex, value)
						{
							var html;
							html = '<a href="javascript:claimTask(\''+ rowdata.id +'\')">领取</a>';
							return html;
						}
					}],
					data:gridData,
		            pageSize:10,
		            rownumbers:true,
		            onReload:loadGridData
		        });
			}
	};
	
	/*加载grid数据*/
	function loadGridData(){
		var url = "${pageContext.request.contextPath}/bpm/listGroupTasks.do";
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
		        	 gridManager.options.newPage = 1;
					 gridManager.loadData(gridData);
		         }
		      }
		   }
		);
	}
	
	/*完成任务领取，并转到待办页面*/
	function claimTask(taskId){
		var url = "${pageContext.request.contextPath}/bpm/claimTask.do";
		var data = {taskId:taskId};
		$.ajax({
		      url: url,
		      data:data,
		      type: "POST",
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 var url = "/pages/workflow/workspace/listPersonalTasks.jsp";
		        	 top.forwardTabEvent("待办任务",url);
		        	 top.removeTabEvent(_selfTabid);
		         }
		      }
		   }
		);
	}
</script>
<title>Insert title here</title>
</head>
<body>
<div id="maingrid"></div>
</body>
</html>