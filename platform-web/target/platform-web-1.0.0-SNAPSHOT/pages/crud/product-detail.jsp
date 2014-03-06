<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	var id = "${param.id}";
	$(function(){
		autoResize();
		loadData();
	});
	function autoResize(){
		var $dialog = $(parent.document).find("div.l-dialog:last");
		var totalWidth = $(parent.window).width();
		var totalHeight = $(parent.window).height();
	    var width = 650;
		var height =90;
		$("table.form4column").each(function(){
		   height += $(this).height();
		});
	    height = height < 250 ? 250 :height;
		var leftoff = (totalWidth - width)/2;
		var topoff = (totalHeight - height)/2;
		
		try {
			$dialog.css('left',leftoff).css('top',topoff);
			$(parent.document).find("div.l-dialog-body:last").height(height).width(width);
			$(parent.document).find('div.l-dialog-content:last').height(height-5);
		} catch (e) {}
	}
	
	function loadData(){
		if(""==id) return;
		$.getJSON("/product/get/"+ id +".do",
				function(json){
				if(json && json.data){
					 json = json.data;
					 var elm;
					 for(var index in json){
						 var _td;
						 var innerHtml;
						 for(var index in json){
						   _td = $("#"+index + "ID");
						   if(_td.attr('format') == 'boolean'){
						     innerHtml = json[index] == "1" ? "是" : "否";
						   }else{
						     innerHtml = json[index];
						   }
						   _td.html(innerHtml);
						 }
					 }
				}
		});
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<table id="top_table" border="0" cellpadding="0" cellspacing="0" class="form4column">
		<tr>
         <td class="label">product_name:</td>  
         <td id='product_nameID' class="content">&nbsp;</td>
         <td class="label">product_date:</td>  
         <td id='product_dateID' class="content">&nbsp;</td>
		</tr>
  </table>
</body>
</html>