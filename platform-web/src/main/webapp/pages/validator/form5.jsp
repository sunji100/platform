<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<%@ include file="/pages/common/excel.jsp"%>
<!-- 表单灰色外观样式 -->
<link href="${pageContext.request.contextPath}/js/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/form-table.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
	/*
		使用自定义验证
	*/
	var departData;
	var userData;
	
	var form;
	
	var depart;
	var user;
	
	$(function(){
		
		initData();
		//初始化form显示
		initForm();
		//ajax加载第一个combox
		ajax();
         //验证
         $(".l-button-test").click(validateForm);
         
		
         $("#num").bind("change",function(){
        	 var num = $("#num").val();
        	 if(isNull(num)){
        		 return;
        	 }
        	 if(!isInteger(num)){
        		 return;
        	 }
        	 $("#form1").remove();
   			 $("#abc").append('<form name="form1" id="form1">');
   			 $("#form1").append('<table cellpadding="0" cellspacing="0" class="l-table-edit" >');
        	 for(var i=0;i<num;i++){
        		 $(".l-table-edit").append('<tr>' +
                         '<td align="right" class="l-table-edit-td">部门:</td>' +
                         '<td align="left" class="l-table-edit-td" style="width:180px">' +
                         '<input name="ddlDepart" id="ddlDepart'+ i +'" ltype="select" validate="{required:true}" ligerui="width:300,data:userData"/>' +
                         '</td><td align="left"></td>' +
                     '</tr>') ;
        		 
        	 }
        	 $("#form1").append('</table>');
        	 $("#abc").append('</form>');
        	 
        	 form = $("#form1").ligerForm({
 	 			inputWidth: 200,
 	 			validate : true
 	 		});
        	 
         });
         
        /* $("#ddlDepart").change(function(){
        	 
         })*/
        
	});
	
	function initData(){
		departData = [
           			{ id: 1, text: '部门1' },
           			{ id: 2, text: '部门2' }
           		];
		userData = [
           			{ id: 1, text: '李三',did:1 },
           			{ id: 2, text: '李四',did:1},
           			{ id: 3, text: '赵武',did:2 },
           			{ id: 4, text: '陈留',did:2 }
           		]; 
	}
	
	function initForm(){
		$.metadata.setType("attr", "validate");
		//应用ligerForm
 		form = $("#form1").ligerForm({
 			inputWidth: 200,
 			validate : true
 		});
       	
 		/*depart = $("#ddlDepart").ligerComboBox({
 			
       		onSelected:itemclick
       	});*/
       	
       	user = $("#ddlUser").ligerComboBox({
       		
       	});
	}
	
	function ajax(){
		//
		//depart.setData(departData);
       	liger.get("ddlDepart").setData(departData);
	}
	
	function itemclick(newval){
		liger.get("ddlUser").setValue();
		var newData = new Array();
        for (i = 0; i < userData.length; i++)
        {
            if (userData[i].did == newval)
            {
                newData.push(userData[i]);
            }
        }
        liger.get("ddlUser").setData(newData);
	}
	
	function validateForm(){
		if(form.valid()){
     		
     	} else {
     		
     	}
	}
	
	function temp(){
		alert("aaa");
	}
	
	
	
	
</script>
<title>Insert title here</title>
</head>
<body>
	<input type="text" id="num" />
	
	<div class="l-form">
		<div class="l-group l-group-hasicon">
			<img src="${pageContext.request.contextPath}/js/ligerUI/skins/icons/communication.gif" />
			<span style="width:300px">部门部门部门部门部门部门</span>
		</div>
		<ul><li class="l-fieldcontainer  l-fieldcontainer-first"></li></ul>
	</div>
	<div id="abc">
	<form name="form1" id="form1">
		
		<table cellpadding="0" cellspacing="0" class="l-table-edit" >
            
            <tr>
                <td align="right" class="l-table-edit-td">部门:</td>
                <td align="left" class="l-table-edit-td" style="width:180px">
                <input name="ddlDepart" id="ddlDepart" ltype="select" validate="{required:true}" ligerui="width:300,textField:'text',onSelected:itemclick"/>
                </td><td align="left"></td>
            </tr>
            <tr>
                <td align="right" class="l-table-edit-td">部门:</td>
                <td align="left" class="l-table-edit-td" style="width:180px">
                <input name="ddlUser" id="ddlUser" ltype="select" validate="{required:true,onSelected:temp}"/>
                </td><td align="left"></td>
            </tr>
            <tr>
                <td align="right" class="l-table-edit-td">部门:</td>
                <td align="left" class="l-table-edit-td" style="width:180px">
                <input name="ddlUser1" id="ddlUser1" ltype="text"  validate="{required:true}" readonly="true"/>
                </td><td align="left"></td>
            </tr>
        </table>
		 
    </form>
    
    </div>
    <br />
	<input type="submit" value="提交" id="Button1" class="l-button l-button-submit" /> 
	<input type="button" value="测试" class="l-button l-button-test"/>
    
    
</body>
</html>