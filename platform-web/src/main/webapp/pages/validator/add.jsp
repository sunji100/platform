<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<%@ include file="/pages/common/excel.jsp"%>
<link href="${pageContext.request.contextPath}/js/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	$(function ()
	{ 
		var dataGrid = [
			{ id: 1, text: '李三' },
			{ id: 2, text: '李四' },
			{ id: 3, text: '赵武' },
			{ id: 4, text: '陈留' }
		]; 
	    //创建表单结构 
	    var f = $("#form").ligerForm({
	    	validate:true,
	        inputWidth: 170, 
	        labelWidth: 140, 
	        space: 40,
	        fields: [
	        { name: "ProductID", type: "hidden" },
	        { display: "申请人姓名11", name: "name", newline: true, type: "text" , group: "基础信息",validate:{required:true} },
	        { display: "申请人身份证号码 ", name: "id_card", newline: true, type: "text",validate:{required:true} },
	        { display: "申请类别 ", name: "apply_type", id: "apply_type", newline: true, type: "select", width: 140,validate:{required:true} },
	        { display: "年度", name: "year", newline: true, type: "select", comboboxName: "year_name", width: 100,validate:{required:true} }
	        ]
	    }); 
	    
	    var data = {};
	    data["name"] = "123";
	    f.setData(data);
	    //liger.get("apply_type").setData(dataGrid);
	    //f_validate();
	    
	    $(".l-button-test").click(function (){
            //alert(v.element($("#txtName")));
	    	f_validate();
        });
	});

	function f_validate() {
        var form = liger.get('form');
        if (form.valid()) {
            alert('验证通过');
        }
        else {
        	//alert("f_validate");
            form.showInvalid();
        }
    }
</script>
<title>Insert title here</title>
</head>
<body>
<!-- search form -->
<form id="form">
	<input type="button" value="测试11" class="l-button l-button-test"/>
</form>
<input >
</body>
</html>