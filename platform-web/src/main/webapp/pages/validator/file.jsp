<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<%@ include file="/pages/common/excel.jsp"%>
<%@ include file="/pages/common/file.jsp"%>
<!-- 表单灰色外观样式 -->
<link href="${pageContext.request.contextPath}/js/ligerUI/skins/Gray/css/form.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/form-table.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
$(function(){
	$('#fileUpload').live('change',function(){
		uploadFile(loadGrid);
	});	
	PageLoader.initFileGrid();
});

PageLoader = {
		initFileGrid:function(){
				
				//显示所有用户
				gridManager = $("#maingrid").ligerGrid({
					width : '98%',
		            height:'92%',
		            columns: [ {
						display : "文件名称",
						name : "name",
						width : 200,
						type : "text",
						align : "left"
					}, {
						display : "修改日期",
						name : "lastModified",
						width : 200,
						type : "text",
						align : "center"
					}, {
						display : "类型",
						name : "type",
						width : 200,
						type : "text",
						align : "center"
					}, {
						display : "大小",
						name : "size",
						width : 100,
						type : "text",
						align : "left"
					},{
						display : "预览",
						name : "size",
						width : 50,
						type : "text",
						align : "left",
						render : function (rowdata, rowindex, value) {
							var re = new RegExp("^(jpg)|(png)|(bmp)|(gif)$");
							var suffix = rowdata.suffix;
							if(validation.isNull(suffix)){
								return "";
							}
							if(!re.exec(suffix)){
								return "";
							}
							var h;
							h = "<a href='javascript:preview(\""+ rowdata.path +"\",\""+ rowdata.fileName +"\")'>预览</a> ";
							return h;
						}
					},{
						display : "操作",
						name : "valid",
						width : 100,
						type : "text",
						align : "center",
						render : function (rowdata, rowindex, value) {
							var h;
							h = "<a href='javascript:downloadFile(\"" + rowdata.fileName + "\")'>下载</a> ";
							h += "&nbsp;&nbsp;&nbsp;";
							h += "<a href='javascript:deleteFile(\"" + rowdata.fileName + "\","+ loadGrid +")'>删除</a> ";
							return h;
						}
					}],
					usePager:false,
		            url:'${pageContext.request.contextPath}/file/listFiles.do',
		            rownumbers:true
		        });

		}
};

function preview(path,name){
	var img;
	//name = "oracle%E6%9C%8D%E5%8A%A1_a626177c-2c62-4de6-b5e7-cc1768c42fca.png";
	img = "<img src='${pageContext.request.contextPath}"+ path +"/"+ name +"' style='width: 200px;height: 200px'>";
	console.info(img);
	
	dialog = $.ligerDialog.open({
		width : 250,
		height : 250,
		title : "图片资源",
		content : img
	});
}

function loadGrid(){
	gridManager.loadData();
}

function downloadSelectedFile(){
	var newRow = gridManager.getSelected();
	if (!newRow) { $.ligerDialog.warn('请选择行'); return; }
	downloadFile(newRow.name);
}
</script>
<title>Insert title here</title>
</head>
<body>
	
	
	<div id="maingrid"></div>
	
	<div>
		<span class="l-button l-button-test fileinput-button">
		<span>上传文件</span>
		<input type="file" id="fileUpload" name="file" class="input"/>
		</span>
		<span class="l-button l-button-test fileinput-button" onclick="downloadSelectedFile()">
			<span>下载文件</span>
		</span>
	</div>
	<a onmouseover=""></a>
</body>
</html>