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
         //验证
         $(".l-button-test").click(validateForm);
         
		 $(".l-button-submit").click(submitAction);
         $("#num").bind("change",buildForm);
         
         $(".l-button-submit").click(function(){
        	 var json = {};
        	 json["a"] = "asdf";
        	 json["b"] = "asdf";
        	 
        	 var vo = {};
        	 vo["ddlDepart"] = 1;
        	 vo["ddlUser"] = 2;
        	 var vo1 = {};
        	 vo1["ddlDepart"] = 2;
        	 vo1["ddlUser"] = 3;
        	 var voList = [];
        	 voList.push(vo);
        	 voList.push(vo1);
        	 
        	 json["voList"] = voList;
 
        	 for(var key in json){
        		 //表单元素与json key不对应，需要处理的
        		 //处理json中的集合
        		 if("voList" == key){
        			 for(var index in json[key]){
        				 var row = json[key][index];
        				 for(var rowkey in row){
        					 liger.get(rowkey + index).setValue(row[rowkey]);
        				 }
        			 }
        			 return;
        		 }
        		 //表单元素与json中的key对应即可
        		 liger.get(key).setValue(json[key]);
        	 }
         });
        
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
	}
	
	var cnt=1;//默认动态行数量
	
	function buildForm(){

		 var num = $("#num").val();
		 
    	 if(validation.isNull(num)){
    		 return;
    	 }
    	 if(!validation.isInteger(num)){
    		 return;
    	 }
    	 
    	 //$("#formDiv").empty();
    	 //$("#formDiv").html($("#formTemplateDiv").html());
    	 
   		 while(num < cnt){
   		 	$("#tr" + --cnt,$(".l-table-edit","#formDiv")).remove();
   		 }
    	 
    	 for(var i=cnt;i<num;i++){
    		 $(".l-table-edit","#formDiv").append('<tr id="tr'+ i +'">' +
    			     '<td align="right" class="l-table-edit-td">部门:</td>' +
    		         '<td align="left" class="l-table-edit-td" style="width:180px">' +
    		         '<input name="voList['+ i +'].ddlDepart" id="ddlDepart'+ i +'" ltype="select" validate="{required:true}" ligerui="width:300,data:departData"/>'+
    		         '</td><td align="left"></td>' +
    		         '<td align="right" class="l-table-edit-td">部门:</td>' +
    		         '<td align="left" class="l-table-edit-td" style="width:180px">' +
    		         '<input name="voList['+ i +'].ddlUser" id="ddlUser'+ i +'" ltype="text" validate="{required:true}" ligerui="width:200"/>' +
    		         '</td><td align="left"></td>' +
    		     	 '</tr>');
    		 $("#ddlDepart" + i).ligerComboBox();
    		 $("#ddlUser" + i).ligerTextBox();
    	 }
    	 
    	 cnt = num;
    	//应用ligerForm
    	/*
  		form = $("#form1").ligerForm({
  			inputWidth: 200,
  			validate : true
  		});*/
	}

	
	function validateForm(){
		if(form.valid()){
     		
     	} else {
     		
     	}
	}
	//提交表单
	function submitAction(){
		var num = $("#num").val();
		var postdata = $("#form1","#formDiv").serialize();
		//处理combox框值的传递
		for(var i=0;i<num;i++){
			postdata += "&voList["+ i +"].ddlDepart_val=" + $("#ddlDepart" + i + "_val").val();
		}
		console.info(postdata);
		$.ajax({ 
		    type:"POST", 
		    url:"${pageContext.request.contextPath}/test/autoForm1.do", 
		    data:postdata,
		    success:function(json){ 
		    	if(json.error){
		    		$.ligerDialog.error(json.error);
				}
				if(json && json.result){
					$.ligerDialog.success(json.result);
				}
		    } 
		 });
	}
	
	
	
	
</script>
<title>Insert title here</title>
</head>
<body>
	<input type="text" id="num" />
	<!-- 分组图标 -->
	<div class="l-form">
		<div class="l-group l-group-hasicon">
			<img src="${pageContext.request.contextPath}/js/ligerUI/skins/icons/communication.gif" />
			<span style="width:300px">部门部门部门部门部门部门</span>
		</div>
		<ul><li class="l-fieldcontainer  l-fieldcontainer-first"></li></ul>
	</div>
	<!-- 表单区域 -->
	<div id="formDiv">
		<form name="form1" id="form1">
			<table cellpadding="0" cellspacing="0" class="l-table-edit" >
	            <tr>
	            	<td align="right" class="l-table-edit-td">a:</td>
	                <td align="left" class="l-table-edit-td" style="width:180px">
	                <input name="a" id="a" ltype="text" validate="{required:true}"/>
	                </td><td align="left"></td>
	                <td align="right" class="l-table-edit-td">b:</td>
	                <td align="left" class="l-table-edit-td" style="width:180px">
	                <input name="b" id="b" ltype="text" validate="{required:true}"/>
	                </td><td align="left"></td>
	            </tr>
	            <tr id="tr0">
	                <td align="right" class="l-table-edit-td">部门:</td>
	                <td align="left" class="l-table-edit-td" style="width:180px">
	                <input name="voList[0].ddlDepart" id="ddlDepart0" ltype="select" validate="{required:true}" ligerui="width:300,data:departData"/>
	                </td><td align="left"></td>
	                <td align="right" class="l-table-edit-td">部门:</td>
	                <td align="left" class="l-table-edit-td" style="width:180px">
	                <input name="voList[0].ddlUser" id="ddlUser0" ltype="text" validate="{required:true}" ligerui="width:200"/>
	                </td><td align="left"></td>
	            </tr>
	        </table> 
	    </form>
    </div>
    <br />
	<input type="button" value="提交" id="Button1" class="l-button l-button-submit" /> 
	<input type="button" value="测试" class="l-button l-button-test"/>
	<!-- 表单模板区域 -->
    <div id="formTemplateDiv" style="display: none;">
    	<form name="form1" id="form1">
			<table cellpadding="0" cellspacing="0" class="l-table-edit" >
				<tr>
	            	<td align="right" class="l-table-edit-td">a:</td>
	                <td align="left" class="l-table-edit-td" style="width:180px">
	                <input name="a" id="a" ltype="text" validate="{required:true}"/>
	                </td><td align="left"></td>
	                <td align="right" class="l-table-edit-td">b:</td>
	                <td align="left" class="l-table-edit-td" style="width:180px">
	                <input name="b" id="b" ltype="text" validate="{required:true}"/>
	                </td><td align="left"></td>
	            </tr>
	        </table> 
	    </form>
    </div>
    
</body>
</html>