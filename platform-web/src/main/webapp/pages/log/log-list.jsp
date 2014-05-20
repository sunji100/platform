<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<!-- 表单灰色外观样式 -->
<link href="${pageContext.request.contextPath}/css/form-table.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	/**
	webservice用户管理界面
	*/
	var v;
	var form;
	var gridParam = {};
	$(function(){
		$.metadata.setType("attr", "validate");
		$("#createDate").val(new Date().Format("yyyy-MM") + "-01");
		$("#createDate_end").val(new Date().Format("yyyy-MM-dd"));
		form = $("#form1").ligerForm({
 			validate : true
 		});
		
		gridParam["createDate"] = new Date().Format("yyyy-MM") + "-01";
		gridParam["createDate_end"] = new Date().Format("yyyy-MM-dd");
		PageLoader.initLogGrid();
		$("#Button1").bind("click",seachLogAction);
	});
	PageLoader = {
			initLogGrid:function(){
					
					gridManager = $("#maingrid").ligerGrid({
						width : '98%',
			            height:'92%',
			            columns: [ {
							display : "账号",
							name : "loginName",
							width : 100,
							type : "text",
							align : "left"
						}, {
							display : "操作描述",
							name : "remark",
							width : 100,
							type : "text",
							align : "left"
						}, {
							display : "操作类",
							name : "className",
							width : 300,
							type : "text",
							align : "left"
						}, {
							display : "操作方法",
							name : "method",
							width : 200,
							type : "text",
							align : "left"
						}, {
							display : "具体参数",
							name : "args",
							width : 200,
							type : "text",
							align : "left"
						}, {
							display : "ip",
							name : "ip",
							width : 100,
							type : "text",
							align : "left",
							hide:true
						},{
							display : "操作时间",
							name : "createDate",
							width : 200,
							type : "text",
							align : "left"
						} ],
			            url:'${pageContext.request.contextPath}/log/pageQueryLog.do',
			            parms:gridParam,
			            pageSize:10 ,
			            rownumbers:true,
			            onReload : seachLogAction
			        });

			}
	};
	
	function seachLogAction(){
		if(form.valid()){
			var param=$("#form1").serialize();
			param += "&page=1";
			param += "&pagesize=" + gridManager.options.pageSize;
			gridManager.loadServerData(param);/*执行服务器查询*/
		}
		return false;
	}
	
	
</script>
<title>Insert title here</title>
</head>
<body>
	<form name="form1" id="form1">
		<table cellpadding="0" cellspacing="0" class="l-table-edit">
            <tr>
                <td align="right" class="l-table-edit-td">账号:</td>
                <td align="left" class="l-table-edit-td" style="width:160px"><input name="loginName" type="text" id="loginName" ltype="text" /></td>
                <td align="right" class="l-table-edit-td">起始时间:</td>
                <td colspan="3">
                	<table cellpadding="0" cellspacing="0" class="l-table-edit" >
			            <tr>
			                <td align="left" class="l-table-edit-td" style="width:160px"><input name="createDate" type="text" id="createDate" ltype="date"  /></td>
			                <td align="right" class="l-table-edit-td">-</td>
			                <td align="left" class="l-table-edit-td" style="width:160px"><input name="createDate_end" type="text" id="createDate_end" ltype="date" validate="{compareStartDate:'#createDate'}" /></td>
			            </tr>
			        </table>
                </td>
                <td align="right" class="l-table-edit-td"><input type="button" value="提交" id="Button1" class="l-button l-button-submit" /> </td>
            </tr>
           
        </table>

		
    </form>
    <div id="maingrid"></div>
    
</body>
</html>