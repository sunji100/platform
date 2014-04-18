<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	/**
	流程模型管理界面
	*/
	var _dialog;
	var _selftabid = "${param.tabid}";
	var gridData;
	$(function(){
		PageLoader.initGridPanel();//所有流程模型
		loadGridData();
	});
	PageLoader = {
			initGridPanel:function(){
				gridManager = $("#maingrid").ligerGrid({
					width : '98%',
		            height:'100%',
		            columns: [ {
						display : "编码",
						name : "id",
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "代码",
						name : "key",
						width : 100,
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
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "创建时间",
						name : "createTime",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "更新时间",
						name : "lastUpdateTime",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "部署ID",
						name : "deploymentId",
						width : 200,
						type : "text",
						align : "left"
					},{
						display : "metaInfo",
						name : "metaInfo",
						width : 200,
						type : "text",
						align : "left"
					},{
						width : 200,
						align : "left",
						render: function (rowdata, rowindex, value)
						{
							var html;
							html = '<a href="javascript:openEditModelTab('+ rowdata.id +')">编辑</a>';
							html += '&nbsp;&nbsp';
							html += '<a href="javascript:deployModel('+ rowdata.id +')">发布</a>';
							html += '&nbsp;&nbsp';
							html += '<a href="javascript:removeModel('+ rowdata.id +','+ rowindex +')">删除</a>';
							return html;
						}
					}],
					data:gridData,
		            pageSize:10,
		            rownumbers:true,
		            onReload : loadGridData,
		            toolbar: { items: [{
      							id : "createModel",
      							text : '新建模型',
      							click : itemclick,
      							img : rootPath + "/images/icons/toolbar/page_edit.gif"
      						}]}
		        });
			}
	};
	
	/*加载grid数据*/
	function loadGridData(){
		var url = "${pageContext.request.contextPath}/bpm/modeler/list.do";
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
	
	/*新建模型，获得新modelID,并转到编辑页面*/
	function createModel(){
		var url = "${pageContext.request.contextPath}/bpm/modeler/createModel.do";
		$.ajax({
		      url: url,
		      type: "POST",
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 top.forwardTabEvent("新建模型","/widgets/modeler/editor.html?id=" + json.data);
		        	 //window.open("${pageContext.request.contextPath}/widgets/modeler/editor.html?id=" + json.data, "_blank");
		        	 //top.removeTabEvent(_selftabid);
		         }
		      }
		   }
		);
	}
	
	/*删除模型定义*/
	function removeModel(modelId,rowindex){
		var url = "${pageContext.request.contextPath}/bpm/modeler/remove.do";
		var data = {id:modelId};
		$.ajax({
		      url: url,
		      type: "POST",
		      data:data,
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
	
	/*发布流程*/
	function deployModel(modelId){
		var url = "${pageContext.request.contextPath}/bpm/modeler/deploy.do";
		var data = {id:modelId};
		$.ajax({
		      url: url,
		      type: "POST",
		      data:data,
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 alert(json.result);
		         }
		      }
		   }
		);
	}
	
	/*打开模型编辑标签页面*/
	function openEditModelTab(modelId){
		 top.forwardTabEvent("新建模型","/widgets/modeler/editor.html?id=" + modelId);
	}
	
	function itemclick(item){
		if("createModel" == item.id){
			createModel();
		}
	}
	
	
</script>
<title>Insert title here</title>
</head>
<body>
<div id="maingrid"></div>
</body>
</html>