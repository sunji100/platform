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
						name : "taskId",
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "任务名称",
						name : "taskName",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "任务开始时间",
						name : "startTime",
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "任务结束时间",
						name : "endTime",
						width : 100,
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
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "流程开始时间",
						name : "procStartTime",
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "流程结束时间",
						name : "procEndTime",
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "流程当前任务",
						name : "currentTaskName",
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
							html = '<a href="javascript:addViewTaskTab(\''+ rowdata.procDefId +'\',\''+ rowdata.procName +'\',\''+ rowdata.processInstanceId +'\')">查看</a>';
							html += '&nbsp;&nbsp;'
							html += '<a href="javascript:withdraw(\''+ rowdata.taskId +'\')">撤消</a>';
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
		var url = "${pageContext.request.contextPath}/bpm/listHistoryTasks.do";
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
	
	/*根据流程定义id，task key查询相应form url*/
	function addViewTaskTab(procDefId,procDefName,processInstanceId,taskId){
		var url = "${pageContext.request.contextPath}/bpm/findFormUrlByProcDefId.do";
		var data = {procDefId:procDefId};
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
		        	 console.info(json.data);
		        	 var tabid = processInstanceId + taskId + "listHistoryTasks";
		        	 top.f_addTab("查看["+ procDefName +"]流程详细",json.data+"?processInstanceId=" + processInstanceId + "&openStatus=3");
		         }
		      }
		   }
		);
	}
	
	/*撤消任务*/
	function withdraw(taskId){
		var url = "${pageContext.request.contextPath}/bpm/withdraw.do";
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
		        	 alert(json.result);
		        	 loadGridData();
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