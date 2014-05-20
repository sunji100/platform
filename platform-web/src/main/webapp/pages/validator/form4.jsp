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
	var dataGrid;
	$(function(){
		$.metadata.setType("attr", "validate");
      	
		dataGrid = [
             			{ id: 1, text: '李三' },
             			{ id: 2, text: '李四' },
             			{ id: 3, text: '赵武' },
             			{ id: 4, text: '陈留' }
             		]; 
		
       	//应用ligerForm
 		var form = $("#form1").ligerForm({
 			validate : true
 		});
        
 		
         //只验证一个字段
         $(".l-button-test").click(function (){
        	 if(form.valid()){
         		
         	} else {
         		//form.showInvalid();
         	}
         });
         
         $(".l-button-submit").click(function(){
        	 var json = {};
        	 json["txtName"] = "asdf";
        	 json["ddlDepart"] = "1;2";
        	 for(var key in json){
        		 liger.get(key).setValue(json[key]);
        	 }
         });
         
         //liger.get("ddlDepart").setData(dataGrid);
        
	});
	
	
	
	
</script>
<title>Insert title here</title>
</head>
<body>
	<form name="form1" id="form1">
		<table cellpadding="0" cellspacing="0" class="l-table-edit" >
            <tr>
                <td align="right" class="l-table-edit-td">名字:</td>
                <td align="left" class="l-table-edit-td" style="width:160px"><input name="txtName" type="text" id="txtName" ltype="text" group="基础信息" validate="{required:true,isIdCardNo:true}" /></td>
                <td align="left"></td>
            </tr>
           
             
             <tr>
                <td align="right" class="l-table-edit-td">Email:</td>
                <td align="left" class="l-table-edit-td" style="width:160px"><input name="txtEmail" type="text" id="txtEmail" ltype="text" validate="{required:true,email:true}" /></td>
                <td align="left"></td>
            </tr>
            <tr>
                <td align="right" class="l-table-edit-td" valign="top">爱好:</td>
                <td align="left" class="l-table-edit-td" style="width:160px">
                     <input id="CheckBoxList1_0" type="checkbox" name="CheckBoxList1$0" checked="checked" /><label for="CheckBoxList1_0">篮球</label><br /><input id="CheckBoxList1_1" type="checkbox" name="CheckBoxList1$1" /><label for="CheckBoxList1_1">网球</label> <br /><input id="CheckBox1" type="checkbox" name="CheckBoxList1$1" /><label for="CheckBoxList1_1">足球</label>      
                </td><td align="left"></td>
            </tr>  
                 
            <tr>
                <td align="right" class="l-table-edit-td">入职日期:</td>
                <td align="left" class="l-table-edit-td" style="width:160px">
                    <input name="txtDate" type="text" id="txtDate" ltype="date" validate="{required:true}" />
                </td><td align="left"></td>
            </tr>
            <tr>
                <td align="right" class="l-table-edit-td">年龄:</td>
                <td align="left" class="l-table-edit-td" style="width:160px">
                    <input name="txtAge" type="text" id="txtAge" ltype='spinner' ligerui="{type:'int'}" value="20" class="required" validate="{digits:true,min:1,max:100}" />
                </td><td align="left"></td>
            </tr>
            <tr>
                <td align="right" class="l-table-edit-td">部门:</td>
                <td align="left" class="l-table-edit-td" style="width:180px">
                <input name="ddlDepart" id="ddlDepart" ltype="select" validate="{required:true}" ligerui="isMultiSelect:true,isShowCheckBox:true,data:dataGrid"/>
                </td><td align="left"></td>
            </tr>
            <tr>
                <td align="right" class="l-table-edit-td">地址:</td>
                <td align="left" class="l-table-edit-td" colspan="2"> 
                <textarea cols="100" rows="4" class="l-textarea" id="address" style="width:400px" validate="{required:true}" ></textarea>
                </td> 
            </tr>
        </table>
		 <br />
		<input type="button" value="提交" id="Button1" class="l-button l-button-submit" /> 
		<input type="button" value="测试" class="l-button l-button-test"/>
    </form>
</body>
</html>