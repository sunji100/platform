<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	流程委托管理界面
	*/
	var _dialog;
	var gridData;
	$(function(){
		PageLoader.initGridPanel();//当前用户委托列表
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
						display : "流程ID",
						name : "procDefId",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "流程名称",
						name : "procDefName",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "委托人",
						name : "attorney",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "委托开始时间",
						name : "startTime",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "委托结束时",
						name : "endTime",
						width : 200,
						type : "text",
						align : "left"
					},{
						width : 200,
						align : "left",
						render: function (rowdata, rowindex, value)
						{
							var html;
							html = '<a href="javascript:removeDelegateInfo(\''+ rowdata.id +'\',\''+ rowindex +'\')">删除</a>';
							return html;
						}
					}],
					data:gridData,
		            pageSize:10,
		            rownumbers:true
		        });
			}
	};
	
	/*加载grid数据*/
	function loadGridData(){
		var url = "${pageContext.request.contextPath}/bpm/delegate/listMyDelegateInfos.do";
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
	/*删除委托*/
	function removeDelegateInfo(id,rowindex){
		var url = "${pageContext.request.contextPath}/bpm/delegate/removeDelegateInfo.do";
		var data = {idList:id};
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
		        	 gridManager.remove(gridManager.getRow(rowindex));
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