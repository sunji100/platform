<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<%@ include file="/pages/common/auth/orgUserDialog.jsp"%>
<script type="text/javascript">
	/**
	设置流程委托界面
	*/
	var tabid = "${param.tabid}";
	var _dialog;
	var gridData;
	$(function(){
		$("#startTime").ligerDateEditor();//加载日期选择器
		$("#endTime").ligerDateEditor();//加载日期选择器
		PageLoader.initGridPanel();//获得流程定义列表
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
							html = '<a href="${pageContext.request.contextPath}/bpmAdmin/graphProcessDefinition.do?processDefinitionId='+ rowdata.id +'" target="_blank">查看流程图</a>';
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
		var url = "${pageContext.request.contextPath}/bpm/listProcessDefinitions.do";
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
	/*保存委托设置*/
	function addDelegateInfo(){
		var newRow = gridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var procDefIdData = "";
		$.each(gridManager.getSelectedRows(), function(index, element) {
			procDefIdData = procDefIdData + element.id + ","; 
		});
		procDefIdData = procDefIdData.substr(0, procDefIdData.length-1);
		
		var url = "${pageContext.request.contextPath}/bpm/delegate/addDelegateInfo.do";
		var data = $("#delegateForm").serialize();
		data += "&procDefIdList=" + procDefIdData;
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
		        	 var url = "/pages/workflow/delegate/listMyDelegateInfos.jsp";
		        
		        	 top.forwardTabEvent("自动委托规则",url);
		         	 top.removeTabEvent(tabid);
		         }
		      }
		   }
		);
	}
	
	/*选择代理人*/
	function selectAttorney(){
		openOrgUserDialog(function(userInfo){
			$("#attorney").val(userInfo.id);
			$("#attorneyName").val(userInfo.text);
		});
	}
	
</script>
<title>Insert title here</title>
</head>
<body>
<form name="delegateForm" id="delegateForm" target="_self">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
<table width="800" border="0" style="margin:5px">
 <tr height="25px">
  <td width="12%">代理人:</td>
  <td width="22%">
  	<table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><input name="attorneyName" class="input-common" type="text" id="attorneyName" />
  			<input name="attorney" class="input-common" type="hidden" id="attorney"/>
  		</td>
        <td><input type="button" class="btn-normal" onclick="selectAttorney()" value="选择用户" /></td>
      </tr>
    </table>
  </td>
  <td width="12%">代理日期:</td>
  <td colspan="3">
  	 <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><input type="text" name="startTime" id="startTime"></td>
        <td width="15px">&nbsp;-&nbsp;</td>
        <td><input type="text" name="endTime" id="endTime"></td>
      </tr>
    </table>
  	</td>
</tr>
</table>	
</td>
    <td><input type="button" class="btn-normal" onclick="addDelegateInfo()" value="保存" /></td>
  </tr>
</table>	
</form>
<div id="maingrid"></div>

</body>
</html>