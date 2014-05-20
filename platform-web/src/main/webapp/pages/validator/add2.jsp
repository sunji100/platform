<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<%@ include file="/pages/common/excel.jsp"%>
<script type="text/javascript">
    var eee;
    $(function ()
    {
        $.metadata.setType("attr", "validate");
        var v = $("#aaaform").validate({
            debug: true,
            errorPlacement: function (lable, element)
            {
                if (element.hasClass("l-textarea"))
                {
                    element.ligerTip({ content: lable.html(), target: element[0] }); 
                }
                else if (element.hasClass("l-text-field"))
                {
                    element.parent().ligerTip({ content: lable.html(), target: element[0] });
                }
                else
                {
                    lable.appendTo(element.parents("td:first").next("td"));
                }
            },
            success: function (lable)
            {
                lable.ligerHideTip();
                lable.remove();
            },
            submitHandler: function ()
            {
                $("form .l-text,.l-textarea").ligerHideTip();
                alert("Submitted!")
            }
        });
        var form = $("#aaaform").ligerForm({
        	validate : true
        });
        $(".l-button-test").click(function ()
        {
        	//$("#txtName").val("asdfas");
            //alert(v.element($("#txtName")));
        	if(form.valid()){
        		
        	} else {
        		//form.showInvalid();
        	}
        });
    });  
</script>
<style type="text/css">
       body{ font-size:12px;}
    .l-table-edit {}
    .l-table-edit-td{ padding:4px;}
    .l-button-submit,.l-button-test{width:80px; float:left; margin-left:10px; padding-bottom:2px;}
    .l-verify-tip{ left:230px; top:120px;}
</style>
<title>Insert title here</title>
</head>
<body>
<!-- search form -->
<form id="aaaform">
<table class="l-table-edit" cellspacing="0" cellpadding="0">
            <tbody><tr>
                <td align="right" class="l-table-edit-td">名字:</td>
                <td align="left" class="l-table-edit-td"><div class="l-text" style="width: 178px;"><input name="txtName" class="l-text-field" id="txtName" style="width: 174px;" type="text" validate="{required:true,minlength:3,maxlength:10}" ltype="text"><div class="l-text-l"></div><div class="l-text-r"></div></div></td>
                <td align="left"></td>
            </tr>
            
            <tr>
                <td align="right" class="l-table-edit-td" valign="top">性别:</td>
                <td align="left" class="l-table-edit-td">
                    <div class="l-radio-wrapper"><a class="l-radio l-radio-checked" href="javascript:void(0)"></a><input name="rbtnl" class="l-hidden" id="rbtnl_0" type="radio" checked="checked" value="1" ligeruiid="rbtnl_0"></div><label for="rbtnl_0">男</label> <div class="l-radio-wrapper"><a class="l-radio" href="javascript:void(0)"></a><input name="rbtnl" class="l-hidden" id="rbtnl_1" type="radio" value="2" ligeruiid="rbtnl_1"></div><label for="rbtnl_1">女</label>
                </td><td align="left"></td>
            </tr>   
             <tr>
                <td align="right" class="l-table-edit-td">Email:</td>
                <td align="left" class="l-table-edit-td"><div class="l-text" style="width: 178px;"><input name="txtEmail" class="l-text-field" id="txtEmail" style="width: 174px;" type="text" ligeruiid="txtEmail" validate="{required:true,email:true}" ltype="text"><div class="l-text-l"></div><div class="l-text-r"></div></div></td>
                <td align="left"></td>
            </tr>
            <tr>
                <td align="right" class="l-table-edit-td" valign="top">爱好:</td>
                <td align="left" class="l-table-edit-td">
                     <div class="l-checkbox-wrapper"><a class="l-checkbox l-checkbox-checked"></a><input name="CheckBoxList1$0" class="l-hidden" id="CheckBoxList1_0" type="checkbox" checked="checked" ligeruiid="CheckBoxList1_0"></div><label for="CheckBoxList1_0">篮球</label><br><div class="l-checkbox-wrapper"><a class="l-checkbox"></a><input name="CheckBoxList1$1" class="l-hidden" id="CheckBoxList1_1" type="checkbox" ligeruiid="CheckBoxList1_1"></div><label for="CheckBoxList1_1">网球</label> <br><div class="l-checkbox-wrapper"><a class="l-checkbox"></a><input name="CheckBoxList1$1" class="l-hidden" id="CheckBox1" type="checkbox" ligeruiid="CheckBox1"></div><label for="CheckBoxList1_1">足球</label>      
                </td><td align="left"></td>
            </tr>  
                  
            <tr>
                <td align="right" class="l-table-edit-td">入职日期:</td>
                <td align="left" class="l-table-edit-td">
                    <div class="l-text-wrapper" style="width: 178px;"><div class="l-text l-text-date" style="width: 178px;"><input name="txtDate" class="l-text-field" id="txtDate" style="width: 158px;" type="text" ligeruiid="txtDate" validate="{required:true}" ltype="date"><div class="l-text-l"></div><div class="l-text-r"></div><div class="l-trigger"><div class="l-trigger-icon"></div></div><div class="l-trigger l-trigger-cancel" style="display: none;"><div class="l-trigger-icon"></div></div></div></div>
                </td><td align="left"></td>
            </tr>
            <tr>
                <td align="right" class="l-table-edit-td">年龄:</td>
                <td align="left" class="l-table-edit-td">
                    <div class="l-text" style="width: 178px;"><input name="txtAge" class="required l-text-field" id="txtAge" style="width: 158px;" type="text" value="20" ligeruiid="txtAge" validate="{digits:true,min:1,max:100}" ltype="spinner" ligerui="{type:'int'}"><div class="l-text-l"></div><div class="l-text-r"></div><div class="l-trigger"><div class="l-spinner-up"><div class="l-spinner-icon"></div></div><div class="l-spinner-split"></div><div class="l-spinner-down"><div class="l-spinner-icon"></div></div></div></div>
                </td><td align="left"></td>
            </tr>
            <tr>
                <td align="right" class="l-table-edit-td">部门:</td>
                <td align="left" class="l-table-edit-td">
                <select name="ddlDepart" id="ddlDepart" style="display: none;" ligeruiid="ddlDepart" ltype="select">
    <option value="1">主席</option>
    <option value="2">研发中心</option>
    <option value="3">销售部</option>
    <option value="4">市场部</option>
    <option value="5">顾问组</option>
</select><div class="l-text-wrapper"><div class="l-text l-text-combobox" style="width: 178px;"><input class="l-text-field" id="ddlDepart_txt" style="width: 158px;" type="text" readonly="true"><div class="l-text-l"></div><div class="l-text-r"></div><div class="l-trigger"><div class="l-trigger-icon"></div></div></div><input name="ddlDepart_txt_val" id="ddlDepart_txt_val" type="hidden" data-ligerid="ddlDepart"></div>
                </td>
            </tr>
            <tr>
                <td align="right" class="l-table-edit-td">地址:</td>
                <td align="left" class="l-table-edit-td"> 
                <textarea class="l-textarea" id="address" style="width: 400px;" rows="4" cols="100" validate="{required:true}"></textarea>
                </td><td align="left"></td>
            </tr>
        </tbody></table>
         <br>
<input class="l-button l-button-submit" id="Button1" type="submit" value="提交"> 
<input class="l-button l-button-test" type="button" value="测试">
</form>
</body>
</html>