<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	发起流程界面
	*/
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
							}
							if(!rowdata.suspended){
								html = '激活';
							}
		                    return html;
		                }
					},{
						display : "操作",
						width : 200,
						align : "left",
						render: function (rowdata, rowindex, value)
						{
							var html;
							html = '<a href="javascript:addStartProcessInstanceTab(\''+ rowdata.id +'\',\''+ rowdata.key +'\',\''+ rowdata.name +'\')">发起流程</a>';
							html += "&nbsp;&nbsp;";
							html += '<a href="${pageContext.request.contextPath}/bpmAdmin/graphProcessDefinition.do?processDefinitionId='+ rowdata.id +'" target="_blank">查看流程图</a>';
							return html;
						}
					}],
					data:gridData,
		            pageSize:10,
		            rownumbers:true
		        });
			}
	};
	
	function addStartProcessInstanceTab(procDefId,procDefKey,proceDefName){
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
		        	 var tabid = procDefKey;
		        	 top.f_addTab("新建["+ proceDefName +"]流程",json.data+"?procDefKey=" + procDefKey + "&procDefId=" + procDefId + "&openStatus=1");
		         }
		      }
		   }
		);
	}
	
	/*加载grid数据*/
	function loadGridData(){
		var url = "${pageContext.request.contextPath}/bpm/listProcessDefinitionsByUserId.do";//显示用户可发起的流程定义
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
	
</script>
<title>Insert title here</title>
</head>
<body>
<div id="maingrid"></div>
</body>
</html>