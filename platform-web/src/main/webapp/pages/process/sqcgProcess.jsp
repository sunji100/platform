<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	var openStatus = "${param.openStatus}";//1、新建流程\2、任务办理\3、历史查看
	var procDefKey = "${param.procDefKey}";
	var procDefId = "${param.procDefId}";
	var processInstanceId = "${param.processInstanceId}";
	var taskId = "${param.taskId}";
	var tabid = "${param.tabid}";
	var toolbarManager;
	$(function(){
		$("#procDefKeyID").val(procDefKey);
		$("#product_dateID").ligerDateEditor();//加载日期选择器
		
		if("1" == openStatus){
			initToolbar();
			$("#taskDiv").css("display", "none");
		}else if("2" == openStatus){
			initToolbar();
			toolbarManager.setDisabled('save');
			loadBusinessData();
			initGridPanel();
			viewHistoryTasks();
			findProcTitle();
			findTaskSuggestion();
		} else if("3" == openStatus){
			initGrapToolbar();
			loadBusinessData();
			initGridPanel();
			viewHistoryTasks();
			findProcTitle();
			$("#suggestionDiv").css("display", "none");
		}
	});
	
	/*初始化流程工具条*/
	function initToolbar(){
		toolbarManager = $("#toptoolbar").ligerToolBar({ items: [
                            { id:'save',text: '保存', click: itemclick, icon:'add'},
                            { line:true },
                            { id:'completeTask',text: '流转', click: itemclick,icon:'add'},
                            { line:true },
                            { id:'rollbackTask',text: '回退', click: itemclick,icon:'delete'},
                            { line:true },
                            { id:'delegateTask',text: '代理', click: itemclick,icon:'delete'},
                            { line:true },
                            { id:'processGrap',text: '流程图', click: itemclick,icon:'delete'}
                        ]
                        });
	}
	
	/*初始化历史查看状态下的工具条*/
	function initGrapToolbar(){
		toolbarManager = $("#toptoolbar").ligerToolBar({ items: [
                            { id:'processGrap',text: '流程图', click: itemclick,icon:'delete'}
                        ]
                        });
	}
	
	function itemclick(item){
		if("save" == item.id){
			saveWorkflow(item.id);
		} else if("completeTask" == item.id){
			completeTask();
		} else if("processGrap" == item.id){
			if("2" == openStatus || "3" == openStatus){//在任务办理状态下打开页面
				window.open("${pageContext.request.contextPath}/bpm/graphHistoryProcessInstance.do?processInstanceId=" + processInstanceId, "_blank");
			} else {
				window.open("${pageContext.request.contextPath}/bpmAdmin/graphProcessDefinition.do?processDefinitionId=" + procDefId, "_blank");
			}
		} else if("rollbackTask" == item.id){
			rollbackTask();
		} else if("delegateTask" == item.id){
			doDelegateTask();
		}
	}
	
	
	/*保存业务数据并启动工作流*/
	function saveWorkflow(itemId){
		var url = "${pageContext.request.contextPath}/process/sqcg/saveWorkflow.do";
		var suggestion = $("#suggestionID").val();
		var procTitle = $("#procTitleID").val();
		var data = $("#product_form").serialize();
		data += "&suggestion=" + suggestion;
		data += "&procTitle=" + procTitle;
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
		        	 toolbarManager.setDisabled(itemId);
		        	 alert(json.data);
		        	 top.removeTabEvent(tabid);
		         }
		      }
		   }
		);
	}
	
	/*加载业务数据*/
	function loadBusinessData(){
		var url = "${pageContext.request.contextPath}/process/sqcg/loadBusinessData.do";
		var data = {processInstanceId:processInstanceId};
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
		        	 json = json.data;
		        	 var elm;
					 for(var index in json){
						 elm = document.getElementById(index + "ID");
						 if(elm){
							 if("SELECT" == elm.nodeName){
								 $(elm).find("option[value='"+json[index]+"']").attr("selected","selected");
							 }else{
								 $(elm).val(json[index]);
							 }
						 }
					 }
		         }
		      }
		   }
		);
	}
	
	/*获得流程实例标题*/
	function findProcTitle(){
		var url = "${pageContext.request.contextPath}/bpm/findProcTitle.do";
		var data = {processInstanceId:processInstanceId};
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
		        	 $("#procTitleID").val(json.data);
		        	 $("#procTitleID").attr("readonly","readonly");
		         }
		      }
		   }
		);
	}
	
	/*获得任务的签字意见*/
	function findTaskSuggestion(){
		var url = "${pageContext.request.contextPath}/bpm/findTaskSuggestion.do";
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
		        	 $("#suggestionID").val(json.data);
		         }
		      }
		   }
		);
	}
	
	/*完成任务并流转*/
	function completeTask(){
		var url = "${pageContext.request.contextPath}/process/sqcg/completeTask.do";
		var suggestion = $("#suggestionID").val();
		var proceTitle = $("#procTitleID").val();
		var data;
		if(processInstanceId){
			data = {taskId:taskId,suggestion:suggestion,proceTitle:proceTitle};
		} else {
			//如果为新建流程
			data = $("#product_form").serialize();
			data += "&suggestion=" + suggestion;
			data += "&procTitle=" + proceTitle;
		}
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
		        	 top.removeTabEvent(tabid);
		         }
		      }
		   }
		);
	}
	
	var gridManager;
	/*历史任务列表*/
	function initGridPanel(){
		gridManager = $("#taskgrid").ligerGrid({
			width : '98%',
            height:300,
            checkbox:true,
            columns: [ {
				display : "编码",
				name : "taskId",
				width : 200,
				type : "text",
				align : "left"
			},{
				display : "名称",
				name : "taskName",
				width : 200,
				type : "text",
				align : "left"
			},{
				display : "办理人",
				name : "taskAssignee",
				width : 200,
				type : "text",
				align : "left"
			},{
				display : "办理开始时间",
				name : "startTime",
				width : 200,
				type : "text",
				align : "left"
			},{
				display : "办理结束时间",
				name : "endTime",
				width : 200,
				type : "text",
				align : "left"
			},{
				display : "办理状态",
				name : "taskReason",
				width : 200,
				type : "text",
				align : "left"
			},{
				display : "签字意见",
				name : "suggestion",
				width : 200,
				type : "text",
				align : "left"
			}],
			data:taskGridData,
			usePager:false,
            rownumbers:true
        });
	}
	/*查看历史任务列表*/
	var taskGridData;
	function viewHistoryTasks(){

		var url = "${pageContext.request.contextPath}/bpm/viewHistoryTasks.do";
		var data = {processInstanceId:processInstanceId};
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
		        	 taskGridData = json;
					 console.info(taskGridData);
		        	 gridManager.loadData(taskGridData);
		         }
		      }
		   }
		);
	}
	
	/*回退任务*/
	function rollbackTask(){
		var url = "${pageContext.request.contextPath}/bpm/rollback.do";
		var suggestion = $("#suggestionID").val();
		var data = {taskId:taskId,suggestion:suggestion};
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
		        	 top.removeTabEvent(tabid);
		         }
		      }
		   }
		);
	}
	
	/*任务代理--选择代理用户*/
	function doDelegateTask() {
		var manager = $("#menuTree").ligerGetTreeManager();
		if (manager != null) {
			manager.clear();
		}
		$.getJSON('${pageContext.request.contextPath}/org/findOrgAndIdentityTree.do', function(menus) {
			var menuDialog = $.ligerDialog.open({
				width : 400,
				// 获取所有菜单数据
				height: 300,
				target : $("#orgTreeDiv"),
				title : "选择人员",
				content : $("#menuTree").ligerTree({
					data : menus.Rows,
					idFieldName : 'id',
					checkbox : false
				}),
				buttons : [{
					text : "确定",
					onclick : function() {
						var manager = $("#menuTree").ligerGetTreeManager();
						var node = manager.getSelected();
						console.info(node);
						if(0 == node.data.level){//只有选择用户才有效
							attorney = node.data.id;
							doPostDelegateTask(attorney);
						}
					}
				}, {
					text : "取消",
					onclick : function() {
						menuDialog.hide();
					}
				} ]
			});
		});
	}
	
	/*任务代理--发送任务代理请求*/
	function doPostDelegateTask(attorney){

		var url = "${pageContext.request.contextPath}/bpm/doDelegate.do";
		var data = {taskId:taskId,attorney:attorney};
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
		        	 top.removeTabEvent(tabid);
		         }
		      }
		   }
		);
	}
</script>
</head>
<body>
	<div id="toptoolbar"></div> 
	<div id="proceTitleDiv">
	标题：
	<br />
	<input type="text" name="procTitle" id="procTitleID" />
	</div>
	
	<div id="businessDiv">
		业务数据：
		<form id="product_form">
			<table id="form_table" border="0" cellpadding="0" cellspacing="0"
				class="form2column">
				<tr>
					<td class="label">product_name:</td>
					<td><input name="product_name" class="input-common" type="text"
						id="product_nameID" /></td>
				</tr>
				<tr>
					<td class="label">product_date:</td>
					<td><input name="product_date" class="input-common" type="text"
						id="product_dateID" /></td>
				</tr>
			</table>
			<input name="procDefKey" type="hidden"
						id="procDefKeyID" />
		</form>
	</div>
	
	<div id="taskDiv">
		执行列表:
		<div id="taskgrid">
		</div>
	</div>
	
	<div id="suggestionDiv">
		签字意见:<br />
		<textarea rows="5" cols="100" name="suggestion" id="suggestionID"></textarea>
	</div>
	
	<div id="orgTreeDiv">
  		<ul id="menuTree">
  		</ul>
 	</div>
</body>
</body>
</html>